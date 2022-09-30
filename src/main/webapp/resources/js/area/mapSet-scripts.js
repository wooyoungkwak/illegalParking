$(function () {
    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];
    let drawingDataTargets = [];
    let zoneAreas = [];

    let beforeCodes = [];

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    // Drawing Manager로 도형을 그릴 지도 div
    let drawingMapContainer = document.getElementById('drawingMap');

    let drawingMap;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let customOverlay;
    let infoWindow;
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

    // Drawing Manager에서 데이터를 가져와 도형을 표시할 아래쪽 지도 div
    /*
    let mapContainer = document.getElementById('map'),
        mapOptions = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };
    */

    let searchIllegalTypeSeq = $('input:radio[name=searchIllegalTypeSeq]:checked').val();
    // 주정차 별 구역 조회
    $('input:radio[name=searchIllegalTypeSeq]').change(async function () {
        $('#areaSettingModal').offcanvas('hide');
        if(drawingDataTargets.length > 0){
            if(confirm("Are you sure!"))
            {
                //do your job
            }
            else
            {
                log(searchIllegalTypeSeq)
                return false;
            }
        }
        searchIllegalTypeSeq = $('input:radio[name=searchIllegalTypeSeq]:checked').val();
        log(searchIllegalTypeSeq);
        removeOverlays();
        await getsZone();
        await drawingPolygon(getZonesInBounds(), 'load');
    });

    // 가져오기 버튼을 클릭하면 호출되는 핸들러 함수입니다
    // Drawing Manager로 그려진 객체 데이터를 가져와 아래 지도에 표시합니다
    $('#btnSet').click(async function () {
        $('#areaSettingModal').offcanvas('hide');
        // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
        let data = manager.getData();
        let opt = {
            url: $.getContextPath() + "/zone/set",
            data: {
                polygonData: data[kakao.maps.drawing.OverlayType.POLYGON],
                illegalTypeSeq: searchIllegalTypeSeq === '' ? '0' : searchIllegalTypeSeq,
            }
        }

        if (opt.data.polygonData.length === 0) {
            alert('구역을 지정하시기 바랍니다.');
            return false;
        } else {

            for (const polygon of opt.data.polygonData) {

                let points = polygon.points;

                let centroidPoints = centroid(points);
                polygon.code = await coordinatesToDongCodeKakaoApi(centroidPoints.x, centroidPoints.y);
                // zoneSeq 추가
                // let lastZoneSeq = zoneSeqs[zoneSeqs.length - 1];
                let lastZoneSeq = zoneSeqs.slice(-1)[0]
                zoneSeqs.push(lastZoneSeq + 1);

                // illegalTypeSeq push
                zoneTypes.push(Number(opt.data.illegalTypeSeq));

                let cnt = 1;
                let firstPath = '';
                let polygonPath = '';
                points.forEach(function (point) {
                    if (cnt === 1) firstPath = point.x + ' ' + point.y;
                    polygonPath += point.x + ' ' + point.y + ',';
                    cnt++;
                })
                polygonPath += firstPath;
                zonePolygons.push(polygonPath);
                zoneAreas.push(polygon);
            }
            // 데이터 저장
            initializeZone(opt);
            // 지도에 가져온 데이터로 도형들을 그립니다
            // drawingPolygon(opt.data.polygonData, 'drawing');
            drawingPolygon(getZonesInBounds(), 'drawing');
            // 생성한 폴리곤 삭제
            removeDrawingOverlays();
        }
    });

    // 생성한 그리기 도형 삭제
    function removeDrawingOverlays() {
        drawingDataTargets.forEach(function (target) {
            manager.remove(target);
        })
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
    function drawingPolygon(polygons, stat) {
        // let areas = getPolygonData();
        // if (stat === 'drawing') {
        //     areas.forEach(function (element) {
        //         polygons.push(element);
        //     })
        // }
        removeOverlays();

        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (const element of polygons) {
            displayArea(element);
        }
    }

    // 주정차 타입에 따른 폴리곤 색 구별
    function fillColorSetting(area) {
        let fillColor;
        if (area.type === 1) fillColor = '#ff6f00';
        else fillColor = '#FF3333';

        return fillColor;
    }

    // 폴리곤 클릭 시 모달 창 오픈
    function showModal(area) {
        let result = initializeZone({
            url: $.getContextPath() + '/zone/get',
            data: {
                zoneSeq: area.seq
            }
        });

        $('#zoneSeq').val(result.zoneSeq);

        let illegalTypeSeq = result.illegalTypeSeq;

        $('input:radio[name=illegalTypeSeq]:input[value="' + illegalTypeSeq + '"]').prop('checked', true);

        let checkVal = $('input:radio[name="illegalTypeSeq"]:checked').val();
        // timeHideAndShow(checkVal);
        timeSetting(result);

        // $('#areaSettingModal').modal('show');
        $('#areaSettingModal').offcanvas('show');
    }

    // 탄력적일 경우 시간 표시
    // function timeHideAndShow(checkVal) {
    //     if (checkVal === '2') { //탄력적 가능일 경우
    //         $('#timeRow').css('display', 'block');
    //         $('#startTime, #endTime').attr('disabled', false);
    //     } else {
    //         $('#timeRow').css('display', 'none');
    //         $('#startTime, #endTime').attr('disabled', true);
    //     }
    // }

    // 기본 시간 설정
    function timeSetting(result) {
        let startTime = result.startTime;
        let endTime = result.endTime;
        if (startTime === null || endTime === null) {
            startTime = "09:00";
            endTime = "18:00";
        }
        $('#startTime').val(startTime).prop('selected', true);
        $('#endTime').val(endTime).prop('selected', true);
    }

    // 탄력적 가능 시간 설정
    // $('input:radio[name=illegalTypeSeq]').click(function () {
    //     let checkVal = $('input:radio[name=illegalTypeSeq]:checked').val();
    //     timeHideAndShow(checkVal);
    // });

    // 폴리곤 삭제
    $('#btnRemove').click(function () {
        if (confirm("삭제하시겠습니까?")) {
            let opt = {
                url: $.getContextPath() + "/zone/remove",
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

            drawingPolygon(getZonesInBounds(), 'load');
            $('#areaSettingModal').offcanvas('hide');
            alert("삭제되었습니다.");
        }
    });

    // 구역 설정
    $('#btnModify').click(function () {
        if (confirm("설정하시겠습니까?")) {
            let form = $('#formAreaSetting').serializeObject();
            if (form.startTime === undefined) form.startTime = "";
            if (form.endTime === undefined) form.endTime = "";

            let result = initializeZone({
                url: $.getContextPath() + '/zone/modify',
                data: form
            });

            if (result.success === 'true') {
                let index = zoneSeqs.indexOf(Number(form.zoneSeq));
                zoneTypes[index] = Number(form.illegalTypeSeq);
                // log(index," :: ", zoneTypes[index]);
            }

            drawingPolygon(getPolygonData(), 'load');
            $('#areaSettingModal').offcanvas('hide');
            alert("설정되었습니다.");
        }
    });

    // 보여지는 맵에 포함된 폴리곤 찾기
    function getZonesInBounds() {
        //맵 구역
        let bounds = drawingMap.getBounds();
        let zonesInBounds = [];

        getPolygonData().filter(function (overlay) {
            let obj = {}, points = [];
            let paths = pointsToPath(overlay.points);
            //log(paths);
            paths.forEach(function (element) {
                points.push(bounds.contain(element));
                obj.inBound = points;
            });
            // 맵 안에 포함되어있는지 확인
            if (obj.inBound.some(inBoundPoint => inBoundPoint === true)) {
                obj.overlay = overlay;
                zonesInBounds.push(obj.overlay);
                //log(inBoundsPath);
            }
            // log(obj.inBound.every(x => x === false));

        });
        // drawingPolygon(inBoundsPath, 'load');
        log('zonesInBounds : ', zonesInBounds);
        return zonesInBounds;
    }

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

        // log(centroid(area.points));

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
        let upCount = 0;
        setKakaoEvent({
                target: polygon,
                event: 'click',
                func: function (mouseEvent) {
                    let resultDiv = document.getElementById('result');
                    resultDiv.innerHTML = '다각형에 mouseup 이벤트가 발생했습니다!' + (++upCount);
                    if(manager._mode === undefined || manager._mode === '')
                        showModal(area);
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
        customOverlay = new kakao.maps.CustomOverlay({})
        infoWindow = new kakao.maps.InfoWindow({removable: true});

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

        manager.addListener('drawend', function (data) {
            drawingDataTargets.push(data.target);
        });

        // 폴리곤 생성 후 새로 그릴 때 생성된 폴리곤 삭제를 위해 manager 데이터 저장
        // setKakaoEvent({
        //         target: manager, event: 'drawend', func: function (data) {
        //             drawingDataTargets.push(data.target);
        //         }
        //     }
        // );

        // 폴리곤 내부 포함여부 확인
        setKakaoEvent({
            target: drawingMap,
            event: 'click',
            func: function (mouseEvent) {
                let latlng = mouseEvent.latLng;
                log('click! ' + latlng.toString());
                log("x : " + latlng.getLat() + ", y : " + latlng.getLng());
                let p = new Point(latlng.getLng(), latlng.getLat());
                // let polys = getPolygonData();
                let len = overlays.length;
                for (let i = 0; i < len; i++) {
                    let points = [];
                    overlays[i].getPath().forEach(function (overlay) {
                        let x = overlay.getLng(), y = overlay.getLat();
                        points.push(new Point(x, y));
                    })
                    // let onePolygon = polys[i].points;
                    let onePolygon = points;
                    let n = onePolygon.length;
                    if (isInside(onePolygon, n, p)) {
                        log(i + " : Yes");
                    } else {
                        // $('#areaSettingModal').offcanvas('hide');
                        // log(i + " : No");
                    }
                }
            }
        });

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

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: drawingMap,
            event: 'idle',
            func: async function () {
                $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = drawingMap.getLevel();

                if (level > 3) {
                    removeOverlays();
                } else {
                    await getsZone();
                    log('zonePolygons.length : ', zonePolygons.length)
                    if(zonePolygons.length > 0)
                        await drawingPolygon(getZonesInBounds(), 'load');
                }
            }
        });
    }

    async function getsZone() {
        let select = SELECT_TYPE_AND_DONG;
        if (searchIllegalTypeSeq === '') select = SELECT_DONG;
        let codes = await getDongCodesBounds(drawingMap);
        // let sameArrChk = JSON.stringify(beforeCodes) === JSON.stringify(codes);
        let sameArrChk = _.isEmpty(_.xor(beforeCodes, codes));
        log('1 : ',beforeCodes);

        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        if (!sameArrChk) {
            initializeZone({
                url: $.getContextPath() + '/zone/gets',
                data: {
                    select: select,
                    illegalTypeSeq: searchIllegalTypeSeq,
                    codes: codes
                }
            })
            log('ok');
            beforeCodes = codes;
        } else if (sameArrChk && select === SELECT_TYPE_AND_DONG) {
            initializeZone({
                url: $.getContextPath() + '/zone/gets',
                data: {
                    select: select,
                    illegalTypeSeq: searchIllegalTypeSeq,
                    codes: codes
                }
            })
        }
        log('2: ',beforeCodes);
        // drawingPolygon(getZonesInBounds(), 'load');
    }

    // zone 초기화
    function initializeZone(opt) {
        let result = $.JJAjaxAsync(opt);

        if (opt.data.select === undefined) {
            return result;
        }

        zonePolygons = result.zonePolygons;
        zoneSeqs = result.zoneSeqs;
        zoneTypes = result.zoneTypes;
    }

    // 초기화
    function initialize() {
        initializeKakao();
        getCurrentPosition(drawingMap);
    }

    initialize();

});
