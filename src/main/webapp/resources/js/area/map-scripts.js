$(function () {
    let zoneSeqs = [];
    let zoneTypes = [];
    let zonePolygons = [];
    let zoneAreas = [];

    let beforeCodes = [];

    let CENTER_LATITUDE = 35.02035492064902;
    let CENTER_LONGITUDE = 126.79383256393594;

    //지도를 담을 영역의 DOM 레퍼런스
    let container = document.getElementById('map');

    let map;

    let overlays = [] // 지도에 그려진 도형을 담을 배열
    let kakaoEvent = kakao.maps.event;

    // 다각형에 마우스오버 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    let mouseoverOption = {
        fillColor: '#EFFFED', // 채우기 색깔입니다
        fillOpacity: 0.8 // 채우기 불투명도 입니다
    };

    let polygonStyle = {
        "draggable": true, // 그린 후 드래그가 가능하도록 설정합니다
        "removable": true, // 그린 후 삭제 할 수 있도록 x 버튼이 표시됩니다
        "editable": true, // 그린 후 수정할 수 있도록 설정합니다
        "strokeColor": "#330000", // 외곽선 색
        "strokeWeight": 2,
        "strokeStyle": "solid", //그리기중 마우스를 따라다니는 보조선의 선 스타일
        "strokeOpacity": 1, // 그리기중 마우스를 따라다니는 보조선의 투명도
        "fillColor": "#000000", // 채우기 색
        "fillOpacity": 0.5 // 채우기색 투명도
    };
    // 다각형에 마우스아웃 이벤트가 발생했을 때 변경할 채우기 옵션입니다
    function mouseoutOption(area) {
        return {
            fillColor: fillColorSetting(area), // 채우기 색깔입니다
            fillOpacity: 0.5
        } // 채우기 불투명도 입니다
    }

    let searchIllegalTypeSeq = '';
    // 주정차 별 구역 조회
    $('input:radio[name=searchIllegalTypeSeq]').change(async function () {
        $('#areaSettingModal').offcanvas('hide');
        searchIllegalTypeSeq = $('input:radio[name=searchIllegalTypeSeq]:checked').val();
        log(searchIllegalTypeSeq);
        removeOverlays();
        await getsZone();
        await drawingPolygon(getZonesInBounds(), 'load');
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

    // 보여지는 맵에 포함된 폴리곤 찾기
    function getZonesInBounds() {
        //맵 구역
        let bounds = map.getBounds();
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
                showModal(area);
            }
        });
        overlays.push(polygon);
    }

    // 카카오 초기화
    function initializeKakao() {
        let options = {
            center: new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE), // 지도의 중심좌표
            level: 3, // 지도의 확대 레벨
        };

        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        map = new kakao.maps.Map(container, options);

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
                    } else {
                        // $('#areaSettingModal').offcanvas('hide');
                        // log(i + " : No");
                    }
                }
            }
        });

        // 중심 좌표나 확대 수준이 변경되면 발생한다.
        setKakaoEvent({
            target: map,
            event: 'idle',
            func: async function () {
                $('#areaSettingModal').offcanvas('hide');
                // 지도의  레벨을 얻어옵니다
                let level = map.getLevel();

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
        let codes = await getDongCodesBounds(map);
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

        if (result.success) {
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
