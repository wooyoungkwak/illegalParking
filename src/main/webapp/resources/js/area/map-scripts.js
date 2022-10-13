$(function () {
    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    let mapContainer = document.getElementById('map');

    let map;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    // 다각형에 마우스오버 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    let mouseoverOption = {
        fillColor: '#EFFFED', // 채우기 색깔입니다
        fillOpacity: 0.8 // 채우기 불투명도 입니다
    };

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

    let searchIllegalType = '';
    // 주정차 별 구역 조회
    $('input:radio[name=searchIllegalType]').change(async function () {
        searchIllegalType = $('input:radio[name=searchIllegalType]:checked').val();
        let codes = await getDongCodesBounds(map);

        getsZone(codes);
    });


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

    // 보여지는 맵에 포함된 폴리곤 찾기
    function getZonesInBounds() {
        //맵 구역
        let bounds = map.getBounds();
        let zonesInBounds = [];

        getPolygonData().filter(function (overlay) {
            let obj = {}, points = [];
            let paths = pointsToPath(overlay.points);
            paths.forEach(function (element) {
                points.push(bounds.contain(element));
                obj.inBound = points;
            });
            // 맵 안에 포함되어있는지 확인
            if (obj.inBound.some(inBoundPoint => inBoundPoint === true)) {
                obj.overlay = overlay;
                zonesInBounds.push(obj.overlay);
            }

        });
        log('zonesInBounds : ', zonesInBounds);
        return zonesInBounds;
    }

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
            map: map, // 다각형을 표시할 지도 객체
            path: path,
            strokeColor: style.strokeColor,
            strokeOpacity: style.strokeOpacity,
            strokeStyle: style.strokeStyle,
            strokeWeight: style.strokeWeight,
            fillColor: fillColorSetting(area),
            fillOpacity: style.fillOpacity
        });

        // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseover',
            func: function (mouseEvent) {
                polygon.setOptions(mouseoverOption);
            }
        });

        // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseout',
            func: function () {
                polygon.setOptions(mouseoutOption(area));
            }
        });
        overlays.push(polygon);
    }

    // 카카오 초기화
    function initializeKakao() {
        map = {
            center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        map = new kakao.maps.Map(mapContainer, map);

        // 폴리곤 내부 포함여부 확인
        setKakaoEvent({
            target: map,
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
                        break;
                    } else {
                        log(i + " : No");
                    }
                }
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: map,
            event: 'idle',
            func: async function () {
                // $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

                if (level > 3) {
                    removeOverlays();
                } else {
                    let codes = await getDongCodesBounds(map);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    if(uniqueCodesCheck) await drawingPolygon(getZonesInBounds());
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
                codes: codes
            }
        })
        log('ok');
        beforeCodes = codes;
        drawingPolygon(getZonesInBounds());
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
        getCurrentPosition(map);
    }

    initialize();
});
