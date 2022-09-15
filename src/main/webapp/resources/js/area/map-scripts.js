let zoneSeq = [];
let zoneType = [];
let zonePolygon = [];
let zoneArea = [];
let drawingDataTargets = [];
let searchZoneType = '';

// Drawing Manager로 도형을 그릴 지도 div
let drawingMapContainer = document.getElementById('drawingMap');
let drawingMap = {
    center: new kakao.maps.LatLng(35.02035492064902, 126.79383256393594), // 지도의 중심좌표
    level: 3, // 지도의 확대 레벨
    disableDoubleClickZoom: true
};

// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
drawingMap = new kakao.maps.Map(drawingMapContainer, drawingMap)
    , overlays = [] // 지도에 그려진 도형을 담을 배열
    , customOverlay = new kakao.maps.CustomOverlay({})
    , infowindow = new kakao.maps.InfoWindow({removable: true});

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
        strokeColor: '#a2a0a0',
        fillColor: '#FF3333',
        fillOpacity: 0.5,
        hintStrokeStyle: 'dash',
        hintStrokeOpacity: 0.5
    }
};

// 다각형에 마우스오버 이벤트가 발생했을 때 변경할 채우기 옵션입니다
let mouseoverOption = {
    fillColor: '#EFFFED', // 채우기 색깔입니다
    fillOpacity: 0.8 // 채우기 불투명도 입니다
};

// 위에 작성한 옵션으로 Drawing Manager를 생성합니다
let manager = new kakao.maps.drawing.DrawingManager(options);

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

// Drawing Manager에서 데이터를 가져와 도형을 표시할 아래쪽 지도 div
/*
var mapContainer = document.getElementById('map'),
    mapOptions = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };
*/


// 폴리곤 생성 후 새로 그릴 때 생성된 폴리곤 삭제를 위해 manager 데이터 저장
manager.addListener('drawend', function (data) {
    drawingDataTargets.push(data.target);
});

//
$('input:radio[name=searchZoneType]').change(function () {
    searchZoneType = $('input:radio[name=searchZoneType]:checked').val();
    searchZone(searchZoneType);
});

//
function searchZone(typeSeq) {
    removeOverlays();
    zoneInitialize({
        url: $.getContextPath() + '/polygon',
        data: {
            select: SELECT_TYPE_AND_DONG,
            searchZoneType: typeSeq
        }
    });
    drawingPolygon(getPolygonData(), 'load');
}

// 가져오기 버튼을 클릭하면 호출되는 핸들러 함수입니다
// Drawing Manager로 그려진 객체 데이터를 가져와 아래 지도에 표시합니다
function getDataFromDrawingMap() {

    // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
    let data = manager.getData();

    let param = {
        'polygonData': data[kakao.maps.drawing.OverlayType.POLYGON]
        , 'searchZoneType': searchZoneType
    };

    let errorMsg = '실패';
    let callBackFn = function (data) {
        if (param.length === 0) {
            alert('구역을 지정하시기 바랍니다.');
            return false;
        } else {
            // 지도에 가져온 데이터로 도형들을 그립니다
            zoneInitialize(data);

            drawingPolygon(param.polygonData, 'drawing');
            // 생성한 폴리곤 삭제
            removeDrawingOverlays();
        }
    }
    commonAjax("/admin/map/insertPolygon", callBackFn, 'POST', JSON.stringify(param), errorMsg);

}

// 생성한 그리기 도형 삭제
function removeDrawingOverlays() {
    drawingDataTargets.forEach(function (element) {
        manager.remove(element);
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

//
function fillColorSetting(area) {
    let fillColor;
    if (area.type === 'F') fillColor = '#ffff22';
    else if (area.type === 'Y') fillColor = '#ff6f00';
    else fillColor = '#FF3333';

    return fillColor;
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

    console.log(centroid(area.points));

    // 다각형에 mouseover 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 변경합니다
    // 지역명을 표시하는 커스텀오버레이를 지도위에 표시합니다
    kakao.maps.event.addListener(polygon, 'mouseover', function (mouseEvent) {
        polygon.setOptions(mouseoverOption);
    });

    // 다각형에 mouseout 이벤트를 등록하고 이벤트가 발생하면 폴리곤의 채움색을 원래색으로 변경합니다
    // 커스텀 오버레이를 지도에서 제거합니다
    kakao.maps.event.addListener(polygon, 'mouseout', function () {
        polygon.setOptions(mouseoutOption(area));
    });

    // 다각형에 마우스다운 이벤트를 등록합니다
    var upCount = 0;
    kakao.maps.event.addListener(polygon, 'click', function (mouseEvent) {
        var resultDiv = document.getElementById('result');
        resultDiv.innerHTML = '다각형에 mouseup 이벤트가 발생했습니다!' + (++upCount);
        coordinatesToDongCodeKakaoApi();
        showModal(area.seq);
    });
    overlays.push(polygon);
}

//
function showModal(seq) {
    let result = zoneInitialize({
        url: '/polygon',
        data: {
            'zoneSeq': seq
        }
    });

    $('#polySeq').val(result.zoneSeq);
    let zoneType = result.zoneType;

    $('input:radio[name=zoneType]:input[value="' + zoneType + '"]').prop('checked', true);

    let checkVal = $('input:radio[name="zoneType"]:checked').val();
    timeHideAndShow(checkVal);
    timeSetting(result);

    // alert(result.zoneSeq);0
    $('#areaSettingModal').modal('show');

}

//
function timeHideAndShow(checkVal) {
    if (checkVal === 'Y') { //탄력적 가능일 경우
        $('#timeRow').css('display', 'block');
        $('#startTime, #endTime').attr('disabled', false);
    } else {
        $('#timeRow').css('display', 'none');
        $('#startTime, #endTime').attr('disabled', true);
    }
}

//
function timeSetting(result) {
    let startTime = result.startTime;
    let endTime = result.endTime;
    if (startTime === null || endTime === null) {
        startTime = 9;
        endTime = 18;
    }
    $('#startTime').val(startTime).prop('selected', true);
    $('#endTime').val(endTime).prop('selected', true);
}

// 탄력적 가능 시간 설정
$('input:radio[name=zoneType]').click(function () {
    let checkVal = $('input:radio[name=zoneType]:checked').val();
    timeHideAndShow(checkVal);
});

// 폴리곤 삭제
$('#btnDelete').click(function () {
    let param = {
        'polySeq': $('#polySeq').val()
        , 'searchZoneType': searchZoneType
    };
    if (confirm("삭제하시겠습니까?")) {
        let callBackFn = function (data) {
            zoneInitialize(data);
            drawingPolygon(getPolygonData(), 'load');
            $('#areaSettingModal').modal('hide');
            alert("삭제되었습니다.");
        }
        commonAjax("/admin/map/deletePolygon", callBackFn, "DELETE", JSON.stringify(param), "에러");
    }
});

// 구역 설정
$('#btnUpdate').click(function () {
    if (confirm("설정하시겠습니까?")) {
        let param = $('#formAreaSetting').serializeObject();
        param.searchZoneType = searchZoneType;
        let callBackFn = function (data) {
            zoneInitialize(data);
            drawingPolygon(getPolygonData(), 'load');
            $('#areaSettingModal').modal('hide');
            alert("설정되었습니다.");
        }
        commonAjax("/admin/map/updatePolygon", callBackFn, "PUT", JSON.stringify(param), "에러");
    }
});


/**************************************
 * ****** 폴리곤 내부 포함여부 확인 ******
 **************************************/
kakao.maps.event.addListener(drawingMap, 'click', function (mouseEvent) {
    var latlng = mouseEvent.latLng;
    console.log('click! ' + latlng.toString());
    console.log("x : " + latlng.getLat() + ", y : " + latlng.getLng());
    let p = new Point(latlng.getLng(), latlng.getLat());
    let polys = getPolygonData();
    var len = polys.length;
    for (let i = 0; i < len; i++) {
        let onePolygon = polys[i].points;
        let n = onePolygon.length;
        if (isInside(onePolygon, n, p)) {
            console.log(i + " : Yes");
        } else {
            // console.log(i + " : No");
        }
    }
});

// 지도에 마우스 오른쪽 클릭 이벤트를 등록합니다
// 선을 그리고있는 상태에서 마우스 오른쪽 클릭 이벤트가 발생하면 그리기를 종료합니다
kakao.maps.event.addListener(drawingMap, 'rightclick', function (mouseEvent) {
    // 그리기 중이면 그리기를 취소합니다
    manager.cancel();
});

kakao.maps.event.addListener(drawingMap, 'dblclick', function (mouseEvent) {
    selectOverlay('POLYGON');
});

// 지도가 이동, 확대, 축소로 인해 중심좌표가 변경되면 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트를 등록합니다
/*kakao.maps.event.addListener(drawingMap, 'center_changed', function() {

    // 지도의  레벨을 얻어옵니다
    var level = drawingMap.getLevel();

    if(level > 3) {
        removeOverlays();
    } else {
        pathInBounds();
    }

    // 지도의 중심좌표를 얻어옵니다
    var latlng = drawingMap.getCenter();

    //폴리곤 점들이 다 포함되면 표시되게... 할수있음 ?



    var message = '<p>지도 레벨은 ' + level + ' 이고</p>';
    message += '<p>중심 좌표는 위도 ' + latlng.getLat() + ', 경도 ' + latlng.getLng() + '입니다</p>';

    var resultDiv = document.getElementById('result');
    resultDiv.innerHTML = message;

});*/

/*
kakao.maps.event.addListener(drawingMap, 'drag', function() {
    removeOverlays();
    console.log(overlays);
});
*/

// 중심 좌표나 확대 수준이 변경되면 발생한다.
kakao.maps.event.addListener(drawingMap, 'idle', function () {
    // 지도의  레벨을 얻어옵니다
    var level = drawingMap.getLevel();

    if (level > 5) {
        removeOverlays();
    } else {
        pathInBounds();
    }
})

// 보여지는 맵에 포함된 폴리곤 찾기
function pathInBounds() {
    //맵 구역
    let bounds = drawingMap.getBounds();
    let inBoundsPath = [];

    // 영역정보의 남서쪽 정보를 얻어옵니다
    var swLatLng = bounds.getSouthWest();
    // var swCoords = swLatLng.toCoords();
    // var south = swCoords.getY();
    // var west = swCoords.getX();
    var south = swLatLng.getLat();
    var west = swLatLng.getLng();

    // 영역정보의 북동쪽 정보를 얻어옵니다
    var neLatLng = bounds.getNorthEast();
    // var neCoords = neLatLng.toCoords();
    // var north = neCoords.getY();
    // var east = neCoords.getX();
    var north = neLatLng.getLat();
    var east = neLatLng.getLng();

    // 동, 서, 남, 북 좌표
    console.log(east, west, south, north);

    getPolygonData().filter(function (overlay) {
        let obj = {}, points = [];
        let paths = pointsToPath(overlay.points);
        //console.log(paths);
        paths.forEach(function (element) {
            points.push(bounds.contain(element));
            obj.inBound = points;
        });
        // 맵 안에 포함되어있는지 확인
        if (obj.inBound.some(inBoundPoint => inBoundPoint === true)) {
            obj.overlay = overlay;
            inBoundsPath.push(obj.overlay);
            //console.log(inBoundsPath);
        }
        // console.log(obj.inBound.every(x => x === false));

    });
    drawingPolygon(inBoundsPath, 'load');
    console.log(inBoundsPath);
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

//
function getPolygonData() {
    let areas = [];
    for (let j = 0; j < zonePolygon.length; j++) {
        let pointsPoly = [], obj = {};
        let zonePolygonArr = zonePolygon[j].split(",");
        obj.type = zoneType[j];
        obj.seq = zoneSeq[j];
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
function zoneInitialize(data) {
    let result = $.JJAjax(data);
    if (data.select === undefined) {
        return result;
    }
    zonePolygon = result.zonePolygon;
    zoneSeq = result.zoneSeq;
    zoneType = result.zoneType;
}


// jquery 초기화
$(function () {
    zoneInitialize({
        url: $.getContextPath() + '/polygons',
        data: {
            select: SELECT_ALL
        }
    });

    pathInBounds();
});
