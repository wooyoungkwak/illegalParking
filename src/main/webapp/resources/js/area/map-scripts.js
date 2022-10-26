$(function () {

    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];
    let receiptCnts = [];

    let isMobile = window.location.pathname.includes('api');

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    let mapContainer = document.getElementById('map');

    let map;
    let customOverlays = [];
    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    //parking
    let markers = [];
    let selectedMarker = false;
    // let markerOverlays  = [];

    let _url = _contextPath + '/zone/gets';

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
        if(isMobile){
            log(gpsLatitude, ':::::::::::', gpsLongitude);
            CENTER_LATITUDE = gpsLatitude;
            CENTER_LONGITUDE = gpsLongitude;
            // map.panTo(new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE));
        }
        log('2 2222::::::::::::::::', CENTER_LONGITUDE);
        log('2 222::::::::::::::::', CENTER_LATITUDE);

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
                await getsMarker(await getDongCodesBounds(map));
            })();
        }
        if(event.target.id === 'pm'){
            removeOverlays();
            _url = _contextPath + '/parking/gets';
            (async () => {
                await getsMarker(await getDongCodesBounds(map));
            })();
        }
    });

    // 다각형에 마우스오버 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    // let mouseoverOption = {
    //     fillColor: '#EFFFED', // 채우기 색깔입니다
    //     fillOpacity: 0.8 // 채우기 불투명도 입니다
    // };

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
    // function mouseoutOption(area) {
    //     return {
    //         fillColor: fillColorSetting(area), // 채우기 색깔입니다
    //         fillOpacity: 0.5
    //     } // 채우기 불투명도 입니다
    // }

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
            customOverlays[i].setMap(null);
        }
        customOverlays = [];
        overlays = [];
    }

    // 폴리곤 그리기
    function drawingPolygon(polygons) {
        removeOverlays();
        log(polygons)
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
            obj.receiptCnt = receiptCnts[j];
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
        // setKakaoEvent({
        //     target: polygon,
        //     event: 'mouseover',
        //     func: function (mouseEvent) {
        //         polygon.setOptions(mouseoverOption);
        //     }
        // });

        // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
        // setKakaoEvent({
        //     target: polygon,
        //     event: 'mouseout',
        //     func: function () {
        //         polygon.setOptions(mouseoutOption(area));
        //     }
        // });

        let cnt = area.receiptCnt;
        let balloonImg = 'balloon_orange.png';
        if(area.type === 'ILLEGAL') balloonImg = 'balloon_red.png'

        let customOverlay = new kakao.maps.CustomOverlay({
            position: path[0],
            content: `<div class="balloon-image"><img src="/resources/assets/img/${balloonImg}" alt="불법주정차"/><span class="balloon-text">${cnt}</span></div>`,
            map: map
        });

        overlays.push(polygon);
        customOverlays.push(customOverlay);
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

        setKakaoEvent({
            target: map,
            event: 'click',
            func: function (mouseEvent) {
                log('click')
                // let latlng = mouseEvent.latLng;
                // log('click! ' + latlng.toString());
                // log("x : " + latlng.getLat() + ", y : " + latlng.getLng());
                // let p = new Point(latlng.getLng(), latlng.getLat());
                // let len = overlays.length;
                // for (let i = 0; i < len; i++) {
                //     let points = [];
                //     overlays[i].getPath().forEach(function (overlay) {
                //         let x = overlay.getLng(), y = overlay.getLat();
                //         points.push(new Point(x, y));
                //     })
                //     let onePolygon = points;
                //     let n = onePolygon.length;
                //     if (isInside(onePolygon, n, p)) {
                //         log(i + " : Yes");
                //         log(zoneSeqs[i]);
                //         // alert('hello')
                //         break;
                //     } else {
                //         log(i + " : No");
                //     }
                // }

                // log(selectedMarker, 'selectedMarker')
                //
                // if (!!selectedMarker && selectedMarker.includes('panel_off.png')) {
                //     selectedMarker = null
                //     $('#img_wrap').trigger('click');
                //     //$('.markerImg').src = selectedMarker;
                //     // selectedMarker.setImage(selectedMarker.normalImage);
                // }
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
                    log(mapSelected);
                    let codes = await getDongCodesBounds(map);
                    // 법정동 코드 변동이 없다면 폴리곤만 표시, 변동 있다면 다시 호출
                    log(uniqueCodesCheck);
                    if(mapSelected === 'zone') {
                        if(uniqueCodesCheck) await drawingPolygon(getPolygonData());
                        else getsZone(codes);
                    } else if (mapSelected === 'parking') {
                        if(!uniqueCodesCheck) await getsMarker(codes);
                    } else {
                        if(!uniqueCodesCheck) await getsMarker(codes);
                    }

                }
            }
        });

        if(isMobile) {
            getMobileCurrentPosition(map);
        }
        else getCurrentPosition(map);
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
                pmName: '-',
                pmPrice: '-',
                pmOper: '-'
            }
        }

        return {
            type: type,
            data: dataObj
        };
    }

    function setImgOrigin() {
        let imgSrc;

        let type = 'parking';
        let imageSize = {
            kakao: new kakao.maps.Size(70, 77),
            width: 70,
            height: 77
        };
        if(mapSelected === 'pm') {
            type = 'pm';
            let pmType = 'bike';
            pmType = pmType === 'bike' ? 'bike' : 'kick';
            imgSrc = {
                normalOrigin : `/resources/assets/img/${pmType}_off_3x.png`,
                clickOrigin : `/resources/assets/img/${pmType}_on_3x.png`
            }
        } else {
            imageSize.height = 45;
            imgSrc = {
                normalOrigin : '/resources/assets/img/panel_off.png',
                clickOrigin : '/resources/assets/img/panel_on.png'
            }
        }

        return {type: type, imgSrc: imgSrc, imgSize: imageSize}
    }

    function createMarkerImage(markerSize, imageOrigin){
        return new kakao.maps.MarkerImage(
            imageOrigin, // 마커 이미지 URL
            markerSize, // 마커의 크기
        );
    }

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수
    function addMarker(data) {
        log('addMarker', selectedMarker);

        let normalImage;
        let clickImage;

        let imgOrigin = setImgOrigin();

        normalImage = createMarkerImage(imgOrigin.imgSize.kakao, imgOrigin.imgSrc.normalOrigin);
        clickImage = createMarkerImage(imgOrigin.imgSize.kakao, imgOrigin.imgSrc.clickOrigin);

        let marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(data.latitude, data.longitude), // 마커의 위치
            image: !!selectedMarker && selectedMarker.seq === data.parkingSeq ? clickImage : normalImage
        });

        // 마커 객체에 마커아이디와 마커의 기본 이미지를 추가합니다
        marker.normalImage = normalImage;
        
        //클릭한 마커를 기억하기 위해 마커에 seq 추가
        marker.seq = data.parkingSeq;

        // 마커에 click 이벤트를 등록합니다
        kakaoEvent.addListener(marker, 'click', function() {
            // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
            kakao.maps.event.preventMap();

            // 클릭된 마커가 없고, click 마커가 클릭된 마커가 아니면
            // 마커의 이미지를 클릭 이미지로 변경합니다
            if (!selectedMarker || selectedMarker !== marker) {

                // 클릭된 마커 객체가 null이 아니면
                // 클릭된 마커의 이미지를 기본 이미지로 변경하고
                !!selectedMarker && selectedMarker.setImage(selectedMarker.normalImage);

                // 현재 클릭된 마커의 이미지는 클릭 이미지로 변경합니다
                marker.setImage(clickImage);
                if(isMobile) {
                    // 커스텀 오버레이 컨텐츠를 설정합니다
                    let obj = markerInfo(imgOrigin.type, data);
                    log(obj);
                    // webToApp.postMessage(JSON.stringify(obj));
                }
            }

            if (selectedMarker === marker) {
                selectedMarker.setImage(clickImage);
                marker.setImage(normalImage);
                selectedMarker = null;
            } else {
                // 클릭된 마커를 현재 클릭된 마커 객체로 설정합니다
                selectedMarker = marker;
            }

            map.panTo(marker.getPosition());

        });

        marker.setMap(map); // 지도 위에 마커를 표출합니다
        markers.push(marker);  // 배열에 생성된 마커를 추가합니다
    }

    // 마커 커스텀 오버레이
    function addOverlay(data) {
        // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
        kakao.maps.event.preventMap();
        let imgOrigin = setImgOrigin();

        let markerNode = document.createElement('div'); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
        markerNode.className = 'marker_wrap';

        let imgNode = document.createElement('div');
        imgNode.id = 'img_wrap';

        let img = document.createElement('img');
        img.src = imgOrigin.imgSrc.normalOrigin;
        img.width = imgOrigin.imgSize.width;
        img.height = imgOrigin.imgSize.height;
        img.className = 'markerImg';

        imgNode.onclick = function () {

            log(img.src, '!selectedMarker', !!selectedMarker);
            let isClick = img.src.includes('panel_on.png');
            if(isClick && !!selectedMarker) {
                log(selectedMarker, isClick, !!selectedMarker, 'isClick && !selectedMarker')
                selectedMarker = imgOrigin.imgSrc.clickOrigin;
                img.src = imgOrigin.imgSrc.normalOrigin;
            }
            else {
                selectedMarker = imgOrigin.imgSrc.normalOrigin;
                img.src = imgOrigin.imgSrc.clickOrigin;
            }
            map.panTo(new kakao.maps.LatLng(data.latitude, data.longitude));
        };

        imgNode.appendChild(img);

        if(mapSelected === 'parking') {
            let span = document.createElement('span')
            span.className = 'balloon-text';
            span.appendChild(document.createTextNode('현재무료'));
            imgNode.appendChild(span);
        }

        markerNode.appendChild(imgNode);

        let after = document.createElement('div');
        after.className = 'after';
        markerNode.appendChild(after);

        let markerOverlay = new kakao.maps.CustomOverlay({
            zIndex:1,
            position: new kakao.maps.LatLng(data.latitude, data.longitude),
            content: markerNode,
            map: map
        });
        markers.push(markerOverlay);

    }

    async function getsMarker(codes) {
        //기존에 조회된 법정동 코드와 새로운 코드가 다르다면 db 조회
        let result = $.JJAjaxAsync({
            url: _url,
            data: {
                codes: codes
            }
        });

        if (result.success) {
            removeMarker();
            for (const data of result.data) {
                addOverlay(data)
            }
            /*.forEach(function(data){
                // addMarker(data);

            });*/
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
            receiptCnts = result.data.receiptCnts;
        }
    }

    // 초기화
    function initialize() {
        initializeKakao();
        $('#debug').val(gpsLatitude + "," + gpsLongitude + " :: " + (typeof gpsLatitude));
    }

    initialize();

    $('#btnFindMe').on('click', function() {
        if(isMobile) getMobileCurrentPosition(map);
        else getCurrentPosition(map);
    });
    $('#zoomIn').on('click', function() {
        map.setLevel(map.getLevel() - 1);
    });
    $('#zoomOut').on('click', function() {
        map.setLevel(map.getLevel() + 1);
    });

});
