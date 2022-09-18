$(function () {

    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];
    let overlays = []
    let drawingDataTargets = [];

    let mapId;
    let drawingMap;
    let options;
    let manager;
    let kakaoEvent = kakao.maps.event;
    let marker;

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


    //
    function fillColorSetting(area) {
        let fillColor;
        if (area.type === 3) fillColor = '#ffff22';
        else if (area.type === 2) fillColor = '#ff6f00';
        else fillColor = '#FF3333';

        return fillColor;
    }

    //
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

    //
    function drawingPolygon(polygons, stat) {
        let areas = getPolygonData();
        if (stat === 'drawing') {
            areas.forEach(function (element) {
                polygons.push(element);
            })
        }
        removeOverlays();

        // 지도에 영역데이터를 폴리곤으로 표시합니다
        for (let i = 0; i < polygons.length; i++) {
            displayArea(polygons[i]);
        }
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

        // 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseover',
            func: function (mouseEvent) {
                // polygon.setOptions(mouseoverOption);
            }
        });

        // 커스텀 오버레이를 지도에서 제거합니다
        setKakaoEvent({
            target: polygon,
            event: 'mouseout',
            func: function (mouseEvent) {
                // polygon.setOptions(mouseoutOption(area));
            }
        });

        // 다각형에 마우스다운 이벤트를 등록합니다
        let upCount = 0;
        setKakaoEvent({
            target: polygon,
            event: 'click',
            func: function (mouseEvent) {
                // var resultDiv = document.getElementById('result');
                // resultDiv.innerHTML = '다각형에 mouseup 이벤트가 발생했습니다!' + (++upCount);
                // coordinatesToDongCodeKakaoApi();
            }
        });

        overlays.push(polygon);
    }

    // 아래 지도에 그려진 도형이 있다면 모두 지웁니다
    function removeOverlays() {
        let len = overlays.length, i = 0;

        for (i = 0; i < len; i++) {
            overlays[i].setMap(null);
        }

        overlays = [];
    }

    // 카카오 맵 이벤트 설정
    function setKakaoEvent(opt){
        kakaoEvent.addListener(opt.target, opt.event, opt.func);
    }

    // 마커 초기화
    function initializeKakaoMarker() {
        // 지도를 클릭한 위치에 표출할 마커입니다
        marker = new kakao.maps.Marker({
            // 지도 중심좌표에 마커를 생성합니다
            position: drawingMap.getCenter()
        });
        marker.setMap(drawingMap);
    }

    // 카카오 맵 설정 초기화
    function initializeKakao() {
        mapId = document.getElementById('drawingMap');

        // mapId.style.width = "1024px";
        // mapId.style.height = "768px";

        // 지도를 표시할 지도 옵션으로  지도를 생성합니다
        drawingMap = new kakao.maps.Map(mapId, {
            center: new kakao.maps.LatLng(35.02035492064902, 126.79383256393594), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
            disableDoubleClickZoom: true
        });

        options = { // Drawing Manager를 생성할 때 사용할 옵션입니다
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
                strokeColor: '#a2a0a0',
                fillColor: '#FF3333',
                fillOpacity: 0.5,
                hintStrokeStyle: 'dash',
                hintStrokeOpacity: 0.5
            }
        }

        /** DRAW EVENT */
        // 위에 작성한 옵션으로 Drawing Manager를 생성합니다
        manager = new kakao.maps.drawing.DrawingManager(options);

        // 폴리곤 생성 후 새로 그릴 때 생성된 폴리곤 삭제를 위해 manager 데이터 저장
        setKakaoEvent({
            target: manager,
            event: 'drawend',
            func: function (data) {
                drawingDataTargets.push(data.target);
            }
        });

        /** MAP EVENT */
        setKakaoEvent({
            target: drawingMap,
            event: 'click',
            func: function (mouseEvent) {
                console.log("click");
            }
        });

        setKakaoEvent({
            target: drawingMap,
            event: 'rightclick',
            func: function (mouseEvent) {
                console.log("rightclick");
            }
        });

        setKakaoEvent({
            target: drawingMap,
            event: 'dblclick',
            func: function (mouseEvent) {
                console.log("dblclick");
            }
        });

        setKakaoEvent({
            target: drawingMap,
            event: 'idle',
            func: function () {
                console.log("dblclick");
            }
        });

        initializeKakaoMarker();

    }

    // Polygon 초기화
    function initializeZone(opt) {
        let result = $.JJAjaxAsync(opt);

        if (opt.data.select === undefined) {
            return result;
        }

        zonePolygons = result.zonePolygons;
        zoneSeqs = result.zoneSeqs;
        zoneTypes = result.zoneTypes;
    }

    // bound 안에 polygon 초기화
    function initializeInBounds() {
        //맵 구역
        let bounds = drawingMap.getBounds();
        let inBoundsPath = [];

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
                inBoundsPath.push(obj.overlay);
            }
        });
        drawingPolygon(inBoundsPath, 'load');
    }


    // 맵 초기화
    initializeKakao();

    initializeZone({
        url: $.getContextPath() + '/polygons',
        data: {
            select: SELECT_ALL
        }
    });

    initializeInBounds();

});
