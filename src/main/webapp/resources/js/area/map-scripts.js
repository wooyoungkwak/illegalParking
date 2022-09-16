$(function (callback) {
    let mapId;
    let drawingMap;
    let options;
    let manager;
    let kakaoEvent;
    let marker;

    // 맵 초기화
    function initialize() {
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
        manager.addListener('drawend', function (data) {

        });

        /** MAP EVENT */
        kakaoEvent = kakao.maps.event;

        // 지도에 마우스 왼쪽 클릭 이벤트
        kakaoEvent.addListener(drawingMap, 'click', function (mouseEvent) {
            console.log("click");
        });

        // 지도에 마우스 오른쪽 클릭 이벤트
        kakaoEvent.addListener(drawingMap, 'rightclick', function (mouseEvent) {
            console.log("rightclick");
        });

        // 지도에 마우스 떠블 클릭 이벤트
        kakaoEvent.addListener(drawingMap, 'dblclick', function (mouseEvent) {
            console.log("dblclick");
        });

        //
        kakaoEvent.addListener(drawingMap, 'idle', function () {
            // 지도의  레벨을 얻어옵니다
            var level = drawingMap.getLevel();
            log("level = ", level);
        })

        // 지도를 클릭한 위치에 표출할 마커입니다
        marker = new kakao.maps.Marker({
            // 지도 중심좌표에 마커를 생성합니다
            position: drawingMap.getCenter()
        });
        marker.setMap(drawingMap);
    }


    initialize();
});
