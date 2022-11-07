$(function () {

    $.isModifyArea = false;
    let zoneAreas = [];
    let zones = {};

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;
    let searchIllegalType = '';

    // Drawing Manager로 도형을 그릴 지도 div
    let drawingMapContainer = document.getElementById('drawingMap');

    let drawingMap;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    // 위에 작성한 옵션으로 Drawing Manager를 생성합니다
    let manager;

    let polygonStyle = {
        "draggable": true,
        "removable": true,
        "editable": true,
        "strokeWeight": 0,
        "fillColor": "#000000",
        "fillOpacity": 0.5
    };

    // Overlay Type 설정 함수
    $.setOverlayType = function(type) {
        // 그리기 중이면 그리기를 취소합니다
        manager.cancel();

        // 클릭한 그리기 요소 타입을 선택합니다
        manager.select(kakao.maps.drawing.OverlayType[type]);
    }

    $.cancelDrawing = function() {
        // 그리기 중이면 그리기를 취소합니다
        manager.cancel();
    }

    // 생성한 Manager 의 Overlay 삭제 함수
    $.removeOverlaysOfManager = function() {
        let getPolygons = manager.getOverlays().polygon;
        let len = getPolygons.length;
        for (let i = 0; i < len; i++) {
            manager.remove(getPolygons[0]);
        }
    }

    // Overlay 삭제 함수
    function removeOverlays() {
        let len = overlays.length, i = 0;
        for (; i < len; i++) {
            overlays[i].setMap(null);
        }
        overlays = [];
    }

    // 폴리곤 그리기
    function drawingPolygons(polygons) {
        removeOverlays();
        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (const element of polygons) {
            displayArea(element);
        }
    }

    // 가져온 zone 데이터 카카오 폴리곤 형식으로 변경
    function getPolygonData() {
        let areas = [];
        for (let j = 0; j < zones.zonePolygons.length; j++) {
            let pointsPoly = [], obj = {};
            let zonePolygonArr = zones.zonePolygons[j].split(",");
            obj.type = zones.zoneTypes[j];
            obj.seq = zones.zoneSeqs[j];
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
        let path = $.pointsToPath(area.points);
        let style = area.options;

        // 다각형을 생성합니다
        let polygon = new kakao.maps.Polygon({
            map: drawingMap, // 다각형을 표시할 지도 객체
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: $.setFillColor(area),
            fillOpacity: style.fillOpacity
        });

        // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
        // 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseover',
            func: function (mouseEvent) {
                polygon.setOptions({
                    fillColor: '#EFFFED',   // 채우기 색깔입니다
                    fillOpacity: 0.8        // 채우기 불투명도 입니다
                });
            }
        });

        // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
        // 커스텀 오버레이를 지도에서 제거합니다
        setKakaoEvent({
                target: polygon,
                event: 'mouseout',
                func: function () {
                    polygon.setOptions($.changeOptionByMouseOut(area));
                }
            });

        // 다각형에 클릭 이벤트를 등록합니다
        setKakaoEvent({
                target: polygon,
                event: 'click',
                func: function (mouseEvent) {
                    kakao.maps.event.preventMap();
                    if($.isModifyArea) {
                        let managerOverlay = manager.getOverlays().polygon;
                        if(managerOverlay.length > 0) {
                            manager.cancel();
                            manager.remove(managerOverlay[0]);
                        }
                        polygon.setMap(null);

                        manager.put(kakao.maps.drawing.OverlayType.POLYGON, path);

                        manager.addListener('remove', function(e) {
                            polygon.setMap(drawingMap);
                            polygon.setOptions($.changeOptionByMouseOut(area));
                        });

                    } else {
                        if (manager._mode === undefined || manager._mode === '') {
                            $('#areaSettingModal').offcanvas('show');
                            let center = centroid(area.points);
                            let centerLatLng = new kakao.maps.LatLng(center.y,
                                center.x);
                            drawingMap.panTo(centerLatLng);
                            $.showModal(area.seq);
                        }
                    }
                }
            });
        overlays.push(polygon);
    }

    function drawingZone(codes) {
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
        $.beforeCodes = codes;
        drawingPolygons(getPolygonData());
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
                $('#btnAddOverlay').addClass("btn-outline-success");
                $('#btnAddOverlay').removeClass("btn-success");
                $('#btnModifyOverlay').addClass("btn-outline-dark");
                $('#btnModifyOverlay').removeClass("btn-dark");
                $('#btnAddOverlay').show();
                $('#btnModifyOverlay').show();
                if(manager.getOverlays().polygon.length === 0) {
                    $('#btnSet').hide();
                    $('#btnCancel').hide();
                    $('#btnModify').hide();
                }
                manager.cancel()
                $.isModifyArea = false;
            }
        });

        // 맵 더블클릭 이벤트 등록
        setKakaoEvent({
            target: drawingMap,
            event: 'dblclick',
            func: function (mouseEvent) {
                if(!$.isModifyArea) {
                    $.isModifyArea = false;
                    $('#btnAddOverlay').removeClass("btn-outline-success");
                    $('#btnAddOverlay').addClass("btn-success");
                    $('#btnModifyOverlay').addClass("btn-outline-success");
                    $('#btnModifyOverlay').removeClass("btn-success");
                    $('#btnModifyOverlay').hide();
                    $('#btnSet').show();
                    $('#btnModify').hide();
                    $('#btnCancel').show();
                    $('#areaSettingModal').offcanvas('hide');
                    $.setOverlayType('POLYGON');
                }
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

        let obj;

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'idle',
            func: async function () {
                // $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = drawingMap.getLevel();

                $('#mapLevel').text(level + '레벨');

                if(level <= 3 && !$.isModifyArea) {
                    obj = await $.getDongCodesBounds(drawingMap);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if(!obj.uniqueCodesCheck) {
                        drawingZone(obj.codes);
                    }
                }
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'zoom_changed',
            func: async function () {
                // 지도의  레벨을 얻어옵니다
                let level = drawingMap.getLevel();

                if (level > 3) {
                    removeOverlays();
                } else {
                    if (level === 3) {
                        drawingZone(obj.codes);
                    }
                }
            }
        });

        manager.addListener('drawend', function(mouseEvent) {
            $('#btnAddOverlay').addClass("btn-outline-success");
            $('#btnAddOverlay').removeClass("btn-success");
        });

    }

    // zone 초기화
    function initializeZone(opt) {
        let result = $.JJAjaxAsync(opt);

        if ( result.success) {
            if (opt.data.select === undefined) {
                return result;
            }

            zones.zonePolygons = result.data.zonePolygons;
            zones.zoneSeqs = result.data.zoneSeqs;
            zones.zoneTypes = result.data.zoneTypes;
        }
    }

    // 초기화
    function initialize() {
        initializeKakao();
        // 주정차 별 구역 조회
        $('input:radio[name=searchIllegalType]').change(async function () {
            $('#areaSettingModal').offcanvas('hide');
            if(manager.getOverlays().polygon.length > 0){
                if(confirm("저장하지 않은 구역은 삭제됩니다. 검색하시겠습니까?")) {
                    $.removeOverlaysOfManager();
                }
                else {
                    $('input:radio[name=searchIllegalType]').eq(0).prop('checked', true)
                    return false;
                }
            }
            searchIllegalType = $('input:radio[name=searchIllegalType]:checked').val();
            if(searchIllegalType === '') {
                $('#btnCancel').trigger('click');
            } else {
                $('#btnAddOverlay').hide();
                $('#btnModifyOverlay').hide();
                $('#btnModify').hide();
                $('#btnSet').hide();
                $('#btnCancel').hide();
            }

            let codes = (await $.getDongCodesBounds(drawingMap)).codes;

            drawingZone(codes);
        });

        // 구역 저장 함수
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
                $.initBtnState();
                // 폴리곤 중심좌표를 구해서 법정동 코드 넣기
                for (const polygon of opt.data.polygonData) {
                    let points = polygon.points;
                    let centroidPoints = centroid(points);
                    polygon.code = await coordinatesToDongCodeKakaoApi(centroidPoints.x, centroidPoints.y);
                }
                // 데이터 저장
                initializeZone(opt);
                // 지도에 가져온 데이터로 도형들을 그립니다
                let codes = (await $.getDongCodesBounds(drawingMap)).codes;
                await drawingZone(codes);
                // 생성한 폴리곤 삭제
                $.removeOverlaysOfManager();
            }
        });

        // 구역 수정 함수
        $('#btnModify').click(function () {
            if (confirm("삭제하시겠습니까?")) {

            }
        });

        // 폴리곤 삭제 함수
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
                    let index = zones.zoneSeqs.indexOf(Number(opt.data.zoneSeq));
                    zones.zoneTypes.splice(index, 1)
                    zones.zoneSeqs.splice(index, 1)
                    zoneAreas.splice(index, 1)
                    zones.zonePolygons.splice(index, 1);
                }

                drawingPolygons(getPolygonData());
                $('#areaSettingModal').offcanvas('hide');
                alert("삭제되었습니다.");
            }
        });

        // 구역 이벤트 설정
        $('#btnModifyEvent').click(function () {
            if (confirm("설정하시겠습니까?")) {
                let form = $('#formAreaSetting').serializeObject();
                form['usedFirst'] = !$('#usedFirst').is(':checked');
                form['usedSecond'] = !$('#usedSecond').is(':checked');
                form['firstStartTime'] = $('#firstStartTimeHour').val() + ':' + $('#firstStartTimeMinute').val();
                form['firstEndTime'] = $('#firstEndTimeHour').val() + ':' + $('#firstEndTimeMinute').val();
                form['secondStartTime'] = $('#secondStartTimeHour').val() + ':' + $('#secondStartTimeMinute').val();
                form['secondEndTime'] = $('#secondEndTimeHour').val() + ':' + $('#secondEndTimeMinute').val();

                let result = initializeZone({
                    url: _contextPath + '/event/addAndModify',
                    data: form
                });

                if (result.success) {
                    let index = zones.zoneSeqs.indexOf(Number(form.zoneSeq));
                    zones.zoneTypes[index] = form.illegalType;
                    drawingPolygons(getPolygonData());
                    $('#areaSettingModal').offcanvas('hide');
                    alert("설정되었습니다.");
                } else {
                    alert(result.msg);
                }
            }
        });

        $.getCurrentPosition(drawingMap);

    }
    //
    initialize();
});
