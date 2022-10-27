$(function (callback) {
  let mapId;
  let map;
  let options;
  let manager;
  let kakaoEvent;
  let markers = [];
  let parkingOverlay = new kakao.maps.CustomOverlay({zIndex:1, yAnchor: 3 });

  let CENTER_LATITUDE = 35.02035492064902;
  let CENTER_LONGITUDE = 126.79383256393594;


  // function getParking(seq) {
  //   let result = $.JJAjaxAsync({
  //     url: _contextPath + '/get',
  //     data: {
  //       parkingSeq: seq
  //     }
  //   });
  //
  // }

  // 지도 위에 표시되고 있는 마커를 모두 제거합니다
  function removeMarker() {
    for (const marker of markers) {
      marker.setMap(null);
    }
    markers = [];
  }

  async function getsParking() {
    let obj = await getDongCodesBounds(map);
    let codes = obj.codes;

    //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
    let result;
    if (!obj.uniqueCodesCheck) {
      result = $.JJAjaxAsync({
        url: _contextPath + '/gets',
        data: {
          codes: codes
        }
      });

      if (result.success) {
        removeMarker();

        result.data.forEach(function(data){
          let marker = addMarker(new kakao.maps.LatLng(data.latitude, data.longitude));
          kakaoEvent.addListener(marker, 'click', function() {
            map.panTo(marker.getPosition());
            // 커스텀 오버레이 컨텐츠를 설정합니다
            displayParkingInfo(data);
          });
        });
      }
    }
  }

  // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
  function addMarker(position) {
    // let imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
    let imageSrc = '/resources/assets/img/parking.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
    let imageSize = new kakao.maps.Size(30, 30);  // 마커 이미지의 크기
    let imageOption = {/*offset: new kakao.maps.Point(18, 55)*/}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
    let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
    let marker = new kakao.maps.Marker({
      position: position, // 마커의 위치
      image: markerImage
    });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
  }

  // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
  function displayParkingInfo (parking) {

    let obj = {
              type: 'parking',
              data: {
                  pkName: parking.prkplceNm,
                  pkAddr: parking.rdnmadr,
                  pkPrice: `기본 ${parking.basicTime} 분 | ${parking.addUnitTime} 분당 ${parking.addUnitCharge}원 추가`,
                  pkOper: parking.parkingchrgeInfo,
                  pkCount: parking.prkcmprt,
                  pkPhone: parking.phoneNumber
              }
          };
    // webToApp.postMessage(JSON.stringify(obj));

    let contentNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
    // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
    contentNode.className = 'parkingInfo_wrap';

    let infoNode = document.createElement('div');
    infoNode.className = 'parkingInfo';

    let title = document.createElement('div');
    title.className = 'title';
    title.appendChild(document.createTextNode(parking.prkplceNm));

    let closeBtn = document.createElement('div');
    closeBtn.className = 'close';
    closeBtn.onclick = function () {
      parkingOverlay.setMap(null);
    };
    title.appendChild(closeBtn);

    infoNode.appendChild(title);

    let addr = document.createElement('span');
    addr.className = 'addr';
    addr.appendChild(document.createTextNode(parking.rdnmadr));
    infoNode.appendChild(addr);

    let jibun = document.createElement('span');
    jibun.className = 'jibun';
    jibun.appendChild(document.createTextNode(`(지번 : ${parking.lnmadr})`));
    infoNode.appendChild(jibun);

    let price = document.createElement('span');
    price.className = 'price';
    price.appendChild(document.createTextNode(`요금정보 : ${parking.parkingchrgeInfo}`));
    infoNode.appendChild(price);

    let tel = document.createElement('span');
    tel.className = 'tel';
    tel.appendChild(document.createTextNode(parking.phoneNumber));
    infoNode.appendChild(tel);

    contentNode.appendChild(infoNode);
    let after = document.createElement('div');
    after.className = 'after';
    contentNode.appendChild(after);

    parkingOverlay.setPosition(new kakao.maps.LatLng(parking.latitude, parking.longitude));
    parkingOverlay.setContent(contentNode)
    parkingOverlay.setMap(map);
  }

  // 맵 초기화
  function initialize() {

    mapId = document.getElementById('map');

    // mapId.style.width = "1024px";
    // mapId.style.height = "768px";

    // 지도를 표시할 지도 옵션으로  지도를 생성합니다
    map = new kakao.maps.Map(mapId, {
      center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
      level: 3, // 지도의 확대 레벨
      disableDoubleClickZoom: true
    });

    options = { // Drawing Manager를 생성할 때 사용할 옵션입니다
      map: map, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
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

    /** MAP EVENT */
    kakaoEvent = kakao.maps.event;

    // 확대, 중심좌표 변경 시
    kakaoEvent.addListener(map, 'idle', async function () {
      // 지도의  레벨을 얻어옵니다
      let level = map.getLevel();
      log("level = ", level);
      let obj = (await getDongCodesBounds(map)).codes;

      //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
      if (!obj.uniqueCodesCheck) {
        await getsParking();
        beforeCodes = obj.codes;
      }
    })
  }

  initialize();

  (async () => {
    await getsParking();
  })();

});
