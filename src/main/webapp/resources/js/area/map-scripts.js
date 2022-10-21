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

    //parking
    let mapId;
    let markers = [];

    let _url = _contextPath + '/zone/gets';

    let _markerImageSrc = '/resources/assets/img/parking.png';


    let mapSelected = 'zone';

    function handleRadioButton(event) {
        if (event.currentTarget.classList[2] === "btn-dark") {
            event.currentTarget.classList.remove("btn-dark");
            event.currentTarget.classList.remove("rounded-pill");
            event.currentTarget.classList.add("btn-white");
        } else {
            let mapType = $('.mapType');

            for (const type of mapType) {
                type.classList.remove("btn-dark");
                type.classList.remove("rounded-pill");
                type.classList.add("btn-white");
            }

            event.currentTarget.classList.remove("btn-white");
            event.currentTarget.classList.add("btn-dark");
            event.currentTarget.classList.add("rounded-pill");
        }
    }

    $('input:radio[name=mapSelect]').change(function (event){
        log('1 ::::::::::::::::', CENTER_LONGITUDE);
        log('1 ::::::::::::::::', CENTER_LATITUDE);
        if(window.location.pathname.includes('api')){
            log(gpsLatitude, ':::::::::::', gpsLongitude);
            CENTER_LATITUDE = gpsLatitude;
            CENTER_LONGITUDE = gpsLongitude;


            // map.panTo(new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE));
        }
        log('2 2222::::::::::::::::', CENTER_LONGITUDE);
        log('2 222::::::::::::::::', CENTER_LATITUDE);

        log(window.location.pathname);

        let mapType = $('.mapType');

        for (const type of mapType) {
            type.addEventListener("change", handleRadioButton);
        }
        mapSelected = event.target.id

        if(event.target.id === 'zone'){
            removeMarker();
            (async () => {
                getsZone(await getDongCodesBounds(map));
            })();
        }
        if(event.target.id === 'parking'){
            removeOverlays();
            _url = _contextPath + '/parking/gets';

            (async () => {
                await getsParking(await getDongCodesBounds(map));
            })();
        }
        if(event.target.id === 'pm'){
            removeOverlays();
            _url = _contextPath + '/pm/gets';
        }
    });

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

    // 보여지는 맵에 포함된 폴리곤 찾기 20221014 동코드로 zone을 가져오기 때문에 기능 제거
    // function getZonesInBounds() {
    //     //맵 구역
    //     let bounds = map.getBounds();
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
    //
    //     });
    //     log('zonesInBounds : ', zonesInBounds);
    //     return zonesInBounds;
    // }

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

        map.setZoomable(false);

        log(map.getCenter());
        // 폴리곤 내부 포함여부 확인
        setKakaoEvent({
            target: map,
            event: 'click',
            func: function (mouseEvent) {
                let latlng = mouseEvent.latLng;
                log('click! ' + latlng.toString());
                log("x : " + latlng.getLat() + ", y : " + latlng.getLng());
                let p = new Point(latlng.getLng(), latlng.getLat());
                let len = overlays.length;
                for (let i = 0; i < len; i++) {
                    let points = [];
                    overlays[i].getPath().forEach(function (overlay) {
                        let x = overlay.getLng(), y = overlay.getLat();
                        points.push(new Point(x, y));
                    })
                    let onePolygon = points;
                    let n = onePolygon.length;
                    if (isInside(onePolygon, n, p)) {
                        log(i + " : Yes");
                        log(zoneSeqs[i]);
                        // alert('hello')
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
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

                if (level > 3) {
                    removeOverlays();
                } else {
                    let codes = await getDongCodesBounds(map);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    log(uniqueCodesCheck);
                    if(mapSelected === 'zone') {
                        if(uniqueCodesCheck) await drawingPolygon(getPolygonData());
                        else getsZone(codes);
                    } else if (mapSelected === 'parking') {
                        await getsParking(codes);
                    } else {
                        log('pm');
                    }

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
                isSetting: false,
            }
        })
        log('ok');
        beforeCodes = codes;
        drawingPolygon(getPolygonData());
    }

    // parking
    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    function removeMarker() {
        for (const marker of markers) {
            marker.setMap(null);
        }
        markers = [];
    }

    // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    function markerInfo (type, data) {
        let dataObj = null;
        if(type === 'parking') {
            dataObj = {
                pkName: data.prkplceNm,
                pkAddr: data.rdnmadr,
                pkPrice: data.parkingchrgeInfo === '유료' ? `기본 ${data.basicTime} 분 | ${data.addUnitTime} 분당 ${data.addUnitCharge}원 추가` : '',
                pkOper: data.parkingchrgeInfo,
                pkCount: data.prkcmprt,
                pkPhone: data.phoneNumber,
                pkLat: data.latitude,
                pkLng: data.longitude
            }
        } else if (type === 'pm') {
            dataObj = {
                pmName: data.pmNmae,
                pmPrice: data.pmPrice,
                pmOper: data.pmOper
            }
        }

        return {
            type: type,
            data: dataObj
        };
        // webToApp.postMessage(JSON.stringify(obj));
    }

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    function addMarker(position) {
        // let imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
        let imageSrc = _markerImageSrc; // 마커 이미지 url, 스프라이트 이미지를 씁니다
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

    async function getsParking(codes) {
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result = $.JJAjaxAsync({
            url: _url,
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
                    let obj = markerInfo('parking', data);
                    log(obj);
                    webToApp.postMessage(JSON.stringify(obj));
                });
            });
        }
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
        $('#debug').val(gpsLatitude + "," + gpsLongitude + " :: " + (typeof gpsLatitude));

        if(isMobile) getMobileCurrentPosition();
        else getCurrentPosition();

    }

    initialize();

    $.setCurrentPosition = function (){
        if(isMobile) getMobileCurrentPosition();
        else getCurrentPosition();
    }

    // 지도 확대, 축소 컨트롤에서 확대 버튼을 누르면 호출되어 지도를 확대하는 함수입니다
    $.zoomIn = function() {
        map.setLevel(map.getLevel() - 1);
    }

// 지도 확대, 축소 컨트롤에서 축소 버튼을 누르면 호출되어 지도를 확대하는 함수입니다
    $.zoomOut = function() {
        map.setLevel(map.getLevel() + 1);
    }

    $.geoLocation = function(position){
        map.panTo(position);
    }

});
