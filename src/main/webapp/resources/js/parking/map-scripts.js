$(function (callback) {
    let mapId;
    let drawingMap;
    let options;
    let manager;
    let kakaoEvent;
    let markers = [];
    let selectedMarker = null; // 클릭한 마커를 담을 변수
    let parkingOverlay = new kakao.maps.CustomOverlay({zIndex:1});
    let contentNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다






    function getParking(seq) {
        let result = $.JJAjaxAsync({
            url: _contextPath + '/get',
            data: {
                parkingSeq: seq
            }
        });
    }

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    function removeMarker() {
        for (const marker of markers) {
            marker.setMap(null);
        }
        markers = [];
    }

    function displayMarker(marker, parking) {
        kakaoEvent.addListener(marker, 'click', function() {
            // getParking(data.parkingSeq);
            displayParkingInfo(parking);
        });
    }
    async function getsParking() {
        let codes = await getDongCodesBounds(drawingMap);

        let sameArrChk = _.isEmpty(_.xor(beforeCodes, codes));
        log('1 : ',beforeCodes);

        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result;
        if (!sameArrChk) {
            result = $.JJAjaxAsync({
                url: _contextPath + '/gets',
                data: {
                    codes: codes
                }
            });
            removeMarker();
        }

        // log(result.parkingSeq);

        $.each(result, function (key, value) {
            // 마커를 생성하고 지도에 표시합니다
            let marker = addMarker(new kakao.maps.LatLng(value.latitude, value.longitude));
            displayMarker(marker, value);
        });
        log(markers)
        // 지도를 클릭한 위치에 표출할 마커입니다
        // for (const marker of markers) {
        //
        // }
    }

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    function addMarker(position, order) {
        let imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
        let imageSize = new kakao.maps.Size(40, 41);  // 마커 이미지의 크기
        let imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
        let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
        let marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

        marker.setMap(drawingMap); // 지도 위에 마커를 표출합니다
        markers.push(marker);  // 배열에 생성된 마커를 추가합니다

        return marker;
    }

    // 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
    function closeOverlay() {
        log('test');
        parkingOverlay.setMap(null);
    }

    // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    function displayParkingInfo (parking) {
        contentNode.innerHTML =`
            <div class="parkinginfo">
                  <div class="close" title="닫기"></div>
                  <span title="테스트" class="btnClose"> 테스트67 </span>
                  <span class="jibun" title="테스트">(지번 : 테스트)</span>
                  <span class="tel">테스트</span>
            </div>
            <div class="after"></div>
        `;

        let closeBtn = document.createElement('button');
        closeBtn.innerHTML = '닫기';
        closeBtn.onclick = function () {
            parkingOverlay.setMap(null);
        };
        contentNode.appendChild(closeBtn);

        parkingOverlay.setPosition(new kakao.maps.LatLng(parking.latitude, parking.longitude));
        parkingOverlay.setMap(drawingMap);
    }

    // 맵 초기화
    function initialize() {
        // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
        contentNode.className = 'parkinginfo_wrap';
        // 커스텀 오버레이 컨텐츠를 설정합니다
        parkingOverlay.setContent(contentNode);

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
                kakao.maps.drawing.OverlayType.MARKER
            ],
            // 사용자에게 제공할 그리기 가이드 툴팁입니다
            // 사용자에게 도형을 그릴때, 드래그할때, 수정할때 가이드 툴팁을 표시하도록 설정합니다
            guideTooltip: ['draw', 'drag', 'edit'],
            markerOptions: {
                draggable: true,
                removable: true,
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

        // 지도에 마우스 오른쪽 클릭 이벤트
        kakaoEvent.addListener(drawingMap, 'rightclick', function (mouseEvent) {
            console.log("rightclick");
        });

        // 지도에 마우스 떠블 클릭 이벤트
        kakaoEvent.addListener(drawingMap, 'dblclick', function (mouseEvent) {
            console.log("dblclick");
        });

        //
        kakaoEvent.addListener(drawingMap, 'idle', async function () {
            // 지도의  레벨을 얻어옵니다
            let level = drawingMap.getLevel();
            log("level = ", level);
            let codes = await getDongCodesBounds(drawingMap);

            let sameArrChk = _.isEmpty(_.xor(beforeCodes, codes));
            log('1 : ',beforeCodes);

            log(sameArrChk);

            //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
            if (!sameArrChk) {
                await getsParking();
                beforeCodes = codes;
            }
        })
    }

    initialize();
    (async () => {
        await getsParking();
    })();

});
