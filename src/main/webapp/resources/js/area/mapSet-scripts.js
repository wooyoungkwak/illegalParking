$(function () {
    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];
    let zoneAreas = [];

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    // Drawing Manager로 도형을 그릴 지도 div
    let drawingMapContainer = document.getElementById('drawingMap');

    let drawingMap;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    // 다각형에 마우스오버 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    let mouseoverOption = {
        fillColor: '#EFFFED', // 채우기 색깔입니다
        fillOpacity: 0.8 // 채우기 불투명도 입니다
    };

    // 위에 작성한 옵션으로 Drawing Manager를 생성합니다
    let manager;

    let polygonStyle = {
        "draggable": true,
        "removable": true,
        "editable": true,
        "strokeColor": "#330000",
        "strokeWeight": 2,
        "strokeStyle": "solid",
        "strokeOpacity": 1,
        "fillColor": "#000000",
        "fillOpacity": 0.5
    };


    // 다각형에 마우스아웃 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    function mouseoutOption(area) {
        return {
            fillColor: fillColorSetting(area), // 채우기 색깔입니다
            fillOpacity: 0.5
        } // 채우기 불투명도 입니다
    }

    // 버튼 클릭 시 호출되는 핸들러 입니다
    function selectOverlay(type) {
        // 그리기 중이면 그리기를 취소합니다
        manager.cancel();

        // 클릭한 그리기 요소 타입을 선택합니다
        manager.select(kakao.maps.drawing.OverlayType[type]);
    }

    $('#btnAddOverlay').click(function () {
        $('#areaSettingModal').offcanvas('hide');
        selectOverlay('POLYGON');
    });

    let searchIllegalType = '';
    // 주정차 별 구역 조회
    $('input:radio[name=searchIllegalType]').change(async function () {
        $('#areaSettingModal').offcanvas('hide');
        if(manager.getOverlays().polygon.length > 0){
            if(confirm("저장하지 않은 구역은 삭제됩니다. 검색하시겠습니까?")) {
                removeDrawingOverlays();
            }
            else {
                $('input:radio[name=searchIllegalType]').eq(0).prop('checked', true)
                return false;
            }
        }
        searchIllegalType = $('input:radio[name=searchIllegalType]:checked').val();
        if(searchIllegalType === '') {
            $('#btnAddOverlay').show();
            $('#btnSet').show();
        } else {
            $('#btnAddOverlay').hide();
            $('#btnSet').hide();
        }

        let codes = await getDongCodesBounds(drawingMap);

        getsZone(codes);
    });

    // 가져오기 버튼을 클릭하면 호출되는 핸들러 함수입니다
    // Drawing Manager로 그려진 객체 데이터를 가져와 아래 지도에 표시합니다
    $('#btnSet').click(async function () {
        $('#areaSettingModal').offcanvas('hide');
        // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
        let data = manager.getData();
        let opt = {
            url: _contextPath + "/zone/set",
            data: {
                polygonData: data[kakao.maps.drawing.OverlayType.POLYGON],
            }
        }

        if (opt.data.polygonData.length === 0) {
            alert('구역을 지정하시기 바랍니다.');
            return false;
        } else {
            // 폴리곤 중심좌표를 구해서 법정동 코드 넣기
            for (const polygon of opt.data.polygonData) {
                let points = polygon.points;
                let centroidPoints = centroid(points);
                polygon.code = await coordinatesToDongCodeKakaoApi(centroidPoints.x, centroidPoints.y);
            }
            // 데이터 저장
            initializeZone(opt);
            // 지도에 가져온 데이터로 도형들을 그립니다
            let codes = await getDongCodesBounds(drawingMap);
            await getsZone(codes);
            // 생성한 폴리곤 삭제
            removeDrawingOverlays();
        }
    });

    // 생성한 그리기 도형 삭제
    function removeDrawingOverlays() {
        let getPolygons = manager.getOverlays().polygon;
        let len = getPolygons.length;
        for (let i = 0; i < len; i++) {
            manager.remove(getPolygons[0]);
        }
    }

    // 아래 지도에 그려진 도형이 있다면 모두 지웁니다
    function removeOverlays() {
        let len = overlays.length, i = 0;
        for (; i < len; i++) {
            overlays[i].setMap(null);
        }
        overlays = [];
    }

    // 폴리곤 그리기
    function drawingPolygon(polygons) {
        removeOverlays();
        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (const element of polygons) {
            displayArea(element);
        }
    }

    // 주정차 타입에 따른 폴리곤 색 구별
    function fillColorSetting(area) {
        let fillColor;
        if (area.type === 'FIVE_MINUTE') fillColor = '#ff6f00';
        else if (area.type === 'ILLEGAL') fillColor = '#FF3333';
        else fillColor = '#00afff';

        return fillColor;
    }

    function setEventHtml(data) {
        let locationType = $('#locationType');
        let btnModify = $('#btnModify');
        let usedFirst = $('#usedFirst');
        let usedSecond= $('#usedSecond');

        $('#offcanvasRightLabel').text(data.zoneSeq + '번 구역설정')
        if (data.eventSeq === null) {
            data.usedFirst = true;
            data.usedSecond = true;

            $('input:radio[name=illegalType]').eq(0).prop('checked', true);
            usedFirst.prop('checked', false);
            usedSecond.prop('checked', false);
            $('.timeSelect').attr('disabled', true);
            locationType.trigger('change');
            btnModify.text('등록');
            btnModify.addClass('btn-primary');
            btnModify.removeClass('btn-danger');
            locationType.trigger('change');
        } else {
            data.usedFirst === false ? usedFirst.prop('checked', true) : usedFirst.prop('checked', false);
            data.usedSecond === false ? usedSecond.prop('checked', true) : usedSecond.prop('checked', false);
            usedFirst.trigger('change');
            usedSecond.trigger('change');
            $('#eventSeq').val(data.eventSeq);
            $('input:radio[name=illegalType]:input[value="'+ data.illegalType +'"]').prop('checked', true);
            $.setGroupNames(data.locationType);
            locationType.val(data.locationType).prop('selected', true);
            locationType.trigger('change');
            $('#name').val(data.groupSeq).prop('selected', true);
            btnModify.text('수정');
            btnModify.addClass('btn-danger');
            btnModify.removeClass('btn-primary');
        }
    }

    // 폴리곤 클릭 시 모달 창 오픈
    function showModal(area) {
        let result = initializeZone({
            url: _contextPath + '/zone/get',
            data: {
                zoneSeq: area.seq
            }
        });

        if(result.success) {
            let data = result.data;
            $('#zoneSeq').val(data.zoneSeq);
            setEventHtml(data);
            timeSetting(data);
        } else {
            alert(result.msg);
        }

    }

    $('#usedFirst').change(function() {
        $.setTime(this);
    });
    $('#usedSecond').change(function() {
        $.setTime(this);
    })


    function initializeTime(first, second) {
        let firstStartTimeHour = '12';
        let firstEndTimeHour = '14';
        let secondStartTimeHour = '20';
        let secondEndTimeHour = '08';
        if(first) {
            $('#firstStartTimeHour').val(firstStartTimeHour).prop('selected', true);
            $('#firstStartTimeMinute').val('00').prop('selected', true);

            $('#firstEndTimeHour').val(firstEndTimeHour).prop('selected', true);
            $('#firstEndTimeMinute').val('00').prop('selected', true);
        }
        if(second) {
            $('#secondStartTimeHour').val(secondStartTimeHour).prop('selected',true);
            $('#secondStartTimeMinute').val('00').prop('selected', true);

            $('#secondEndTimeHour').val(secondEndTimeHour).prop('selected',true);
            $('#secondEndTimeMinute').val('00').prop('selected', true);
        }
    }

    // 기본 시간 설정
    function timeSetting(data) {
        let first = data.usedFirst;
        let second = data.usedSecond;
        if(first === true || second === true ) {
            initializeTime(first, second);
        }
        if(first === false) {
            let firstStartTime = data.firstStartTime.split(':');
            let firstEndTime = data.firstEndTime.split(':');
            $('#firstStartTimeHour').val(firstStartTime[0]).prop('selected', true);
            $('#firstStartTimeMinute').val(firstStartTime[1]).prop('selected', true);

            $('#firstEndTimeHour').val(firstEndTime[0]).prop('selected', true);
            $('#firstEndTimeMinute').val(firstEndTime[1]).prop('selected', true);
        }
        if(second === false) {
            let secondStartTime = data.secondStartTime.split(':');
            let secondEndTime = data.secondEndTime.split(':');
            $('#secondStartTimeHour').val(secondStartTime[0]).prop('selected', true);
            $('#secondStartTimeMinute').val(secondStartTime[1]).prop('selected', true);

            $('#secondEndTimeHour').val(secondEndTime[0]).prop('selected', true);
            $('#secondEndTimeMinute').val(secondEndTime[1]).prop('selected', true);
        }
    }

    // 폴리곤 삭제
    $('#btnRemove').click(function () {
        if (confirm("삭제하시겠습니까?")) {
            let opt = {
                url: _contextPath + "/zone/remove",
                data: {
                    'zoneSeq': $('#zoneSeq').val()
                }
            };
            let result = initializeZone(opt);

            if (result.success === 'true') {
                let index = zoneSeqs.indexOf(Number(opt.data.zoneSeq));
                zoneTypes.splice(index, 1)
                zoneSeqs.splice(index, 1)
                zoneAreas.splice(index, 1)
                zonePolygons.splice(index, 1);
            }

            drawingPolygon(getPolygonData());
            $('#areaSettingModal').offcanvas('hide');
            alert("삭제되었습니다.");
        }
    });

    // 구역 설정
    $('#btnModify').click(function () {
        if (confirm("설정하시겠습니까?")) {
            let form = $('#formAreaSetting').serializeObject();
            form['usedFirst'] = !$('#usedFirst').is(':checked');
            form['usedSecond'] = !$('#usedSecond').is(':checked');
            form['firstStartTime'] = $('#firstStartTimeHour').val() + ':' + $('#firstStartTimeMinute').val();
            form['firstEndTime'] = $('#firstEndTimeHour').val() + ':' + $('#firstEndTimeMinute').val();
            form['secondStartTime'] = $('#secondStartTimeHour').val() + ':' + $('#secondStartTimeMinute').val();
            form['secondEndTime'] = $('#secondEndTimeHour').val() + ':' + $('#secondEndTimeMinute').val();

            let result = initializeZone({
                url: _contextPath + '/zone/modify',
                data: form
            });

            if (result.success) {
                let index = zoneSeqs.indexOf(Number(form.zoneSeq));
                zoneTypes[index] = form.illegalType;
                // log(index," :: ", zoneTypes[index]);
                drawingPolygon(getPolygonData());
                $('#areaSettingModal').offcanvas('hide');
                alert("설정되었습니다.");
            } else {
                alert(result.msg);
            }
        }
    });

    // 보여지는 맵에 포함된 폴리곤 찾기 20221014 동코드로 zone을 가져오기 때문에 기능 제거
    // function getZonesInBounds() {
    //     //맵 구역
    //     let bounds = drawingMap.getBounds();
    //     let zonesInBounds = [];
    //
    //     getPolygonData().filter(function (overlay) {
    //         let obj = {}, points = [];
    //         let paths = pointsToPath(overlay.points);
    //         paths.forEach(function (element) {
    //             points.push(bounds.contain(element));
    //             obj.inBound = points;
    //         });
    //         // 맵 안에 포함되어있는지 확인
    //         if (obj.inBound.some(inBoundPoint => inBoundPoint === true)) {
    //             obj.overlay = overlay;
    //             zonesInBounds.push(obj.overlay);
    //         }
    //     });
    //     log('zonesInBounds : ', zonesInBounds);
    //     return zonesInBounds;
    // }

    // Drawing Manager에서 가져온 데이터 중
    // 선과 다각형의 꼭지점 정보를 kakao.maps.LatLng객체로 생성하고 배열로 반환하는 함수입니다
    function pointsToPath(points) {
        let len = points.length,
            path = [],
            i = 0;

        for (; i < len; i++) {
            let latlng = new kakao.maps.LatLng(points[i].y, points[i].x);
            path.push(latlng);
        }

        return path;
    }

    // 가져온 zone 데이터 카카오 폴리곤 형식으로 변경
    function getPolygonData() {
        let areas = [];
        for (let j = 0; j < zonePolygons.length; j++) {
            let pointsPoly = [], obj = {};
            let zonePolygonArr = zonePolygons[j].split(",");
            obj.type = zoneTypes[j];
            obj.seq = zoneSeqs[j];
            for (let i = 0; i < zonePolygonArr.length - 1; i++) {
                let pathPoints = zonePolygonArr[i].split(" ");
                pointsPoly[i] = new Point(pathPoints[0], pathPoints[1]);
                obj.points = pointsPoly;
            }
            obj.coordinate = 'wgs84';
            obj.options = polygonStyle;
            areas.push(obj);
            zoneAreas.push(obj);
        }
        return areas;
    }

    // 카카오 맵 이벤트 설정
    function setKakaoEvent(opt) {
        kakaoEvent.addListener(opt.target, opt.event, opt.func);
    }

    // 다각형을 생상하고 이벤트를 등록하는 함수입니다
    function displayArea(area) {
        let path = pointsToPath(area.points);
        let style = area.options;

        // 다각형을 생성합니다
        let polygon = new kakao.maps.Polygon({
            map: drawingMap, // 다각형을 표시할 지도 객체
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: fillColorSetting(area),
            fillOpacity: style.fillOpacity
        });

        // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
        // 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseover',
            func: function (mouseEvent) {
                polygon.setOptions(mouseoverOption);
            }
        });

        // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
        // 커스텀 오버레이를 지도에서 제거합니다
        setKakaoEvent({
                target: polygon,
                event: 'mouseout',
                func: function () {
                    polygon.setOptions(mouseoutOption(area));
                }
            });

        // 다각형에 클릭 이벤트를 등록합니다
        setKakaoEvent({
                target: polygon,
                event: 'click',
                func: function (mouseEvent) {
                    // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
                    kakao.maps.event.preventMap();



                    if(manager._mode === undefined || manager._mode === '') {
                        $('#areaSettingModal').offcanvas('show');
                        let center = centroid(area.points);
                        let centerLatLng = new kakao.maps.LatLng(center.y, center.x);
                        drawingMap.panTo(centerLatLng);
                        showModal(area);
                    }
                }
            });
        overlays.push(polygon);
    }

    // 카카오 초기화
    function initializeKakao() {
        drawingMap = {
            center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        drawingMap = new kakao.maps.Map(drawingMapContainer, drawingMap);

        let options = { // Drawing Manager를 생성할 때 사용할 옵션입니다
            map: drawingMap, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
            drawingMode: [ // Drawing Manager로 제공할 그리기 요소 모드입니다
                kakao.maps.drawing.OverlayType.POLYGON
            ],
            // 사용자에게 제공할 그리기 가이드 툴팁입니다
            // 사용자에게 도형을 그릴때, 드래그할때, 수정할때 가이드 툴팁을 표시하도록 설정합니다
            guideTooltip: ['draw', 'drag', 'edit'],
            polygonOptions: {
                draggable: true,
                removable: true,
                editable: true,
                strokeColor: '#000000',
                fillColor: '#00afff',
                fillOpacity: 0.5,
                hintStrokeStyle: 'dash',
                hintStrokeOpacity: 0.5
            }
        };
        manager = new kakao.maps.drawing.DrawingManager(options);

        // 지도에 마우스 오른쪽 클릭 이벤트를 등록합니다
        // 선을 그리고있는 상태에서 마우스 오른쪽 클릭 이벤트가 발생하면 그리기를 종료합니다
        setKakaoEvent({
            target: drawingMap,
            event: 'rightclick',
            func: function (mouseEvent) {
                // 그리기 중이면 그리기를 취소합니다
                manager.cancel();
            }
        });

        // 맵 더블클릭 이벤트 등록
        setKakaoEvent({
            target: drawingMap,
            event: 'dblclick',
            func: function (mouseEvent) {
                $('#areaSettingModal').offcanvas('hide');
                selectOverlay('POLYGON');
            }
        });

        // 맵 클릭 이벤트 등록
        setKakaoEvent({
            target: drawingMap,
            event: 'click',
            func: function (mouseEvent) {
                $('#areaSettingModal').offcanvas('hide');
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'idle',
            func: async function () {
                // $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = drawingMap.getLevel();

                $('#mapLevel').text(level + '레벨');

                if (level > 3) {
                    removeOverlays();
                } else {
                    let codes = await getDongCodesBounds(drawingMap);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if(uniqueCodesCheck) await drawingPolygon(getPolygonData());
                    else getsZone(codes);
                }
            }
        });
    }

    function getsZone(codes) {
        let select = SELECT_TYPE_AND_DONG;
        if (searchIllegalType === '') select = SELECT_DONG;
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        initializeZone({
            url: _contextPath + '/zone/gets',
            data: {
                select: select,
                illegalType: searchIllegalType,
                codes: codes,
                isSetting: true
            }
        })
        beforeCodes = codes;
        drawingPolygon(getPolygonData());
    }

    // zone 초기화
    function initializeZone(opt) {
        let result = $.JJAjaxAsync(opt);

        if ( result.success) {
            if (opt.data.select === undefined) {
                return result;
            }

            zonePolygons = result.data.zonePolygons;
            zoneSeqs = result.data.zoneSeqs;
            zoneTypes = result.data.zoneTypes;
        }
    }

    // 초기화
    function initialize() {
        initializeKakao();
        getCurrentPosition(drawingMap);
    }

    initialize();

    $.setGroupNames = function (locationType) {

        function getNamesSelectHtml(groups) {
            let html = '';
            for (const group of groups) {
                html += '<option value="' + group.groupSeq + '">' + group.name + '</option>';
            }
            return html;
        }
        let result = $.JJAjaxAsync({
            url: _contextPath + '/event/group/name/get',
            data: {
                locationType: locationType
            }
        });

        if (result.success) {
            let groups = result.data;
            let html = getNamesSelectHtml(groups);
            $('#name').append(html);
        }
    }

    $.setTime = function(_this) {
        let id = _this.id.substr(4).toLowerCase();
        if($('#'+_this.id).is(':checked')){
            $('#'+ id +'StartTimeHour').attr('disabled', false);
            $('#'+ id +'StartTimeMinute').attr('disabled', false);
            $('#'+ id +'EndTimeHour').attr('disabled', false);
            $('#'+ id +'EndTimeMinute').attr('disabled', false);
        } else {
            $('#'+ id +'StartTimeHour').attr('disabled', true);
            $('#'+ id +'StartTimeMinute').attr('disabled', true);
            $('#'+ id +'EndTimeHour').attr('disabled', true);
            $('#'+ id +'EndTimeMinute').attr('disabled', true);
        }
    }

});
