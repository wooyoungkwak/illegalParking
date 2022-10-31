<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/mobileMapHtmlLayout.jsp">
	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="col">
				<div class="map_wrap">
					<div id="map"></div>

					<div class="map-control alarm-control" role="group">
						<label class="alarm-mode-button">
							<input type="checkbox"/>
							<span class="onOff-switch"></span>
						</label>
					</div>

					<div class="map-control border border-2 bg-white shadow-sm btn-group rounded-pill" role="group">

						<label for="zone" class="mapType btn btn-dark rounded-pill fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="zone" autocomplete="off" checked>불법주차</label>

						<label for="parking" class="mapType btn btn-white fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="parking" autocomplete="off">주차장</label>

						<label for="pm" class="mapType btn btn-white fw-bold"><input type="radio" class="btn-check" name="mapSelect" id="pm" autocomplete="off">모빌리티</label>
					</div>

						<%--<div class="custom-btn-control">
						</div>--%>
					<!-- 지도 확대, 축소 컨트롤 div 입니다 -->
					<div class="custom-control">
						<span id="btnFindMe"><img src="<%=contextPath%>/resources/assets/img/ping.png" alt="내위치"></span>
						<span id="zoomIn"><img src="<%=contextPath%>/resources/assets/img/plus.png" alt="확대"></span>
						<span id="zoomOut"><img src="<%=contextPath%>/resources/assets/img/minus.png" alt="축소"></span>
					</div>

					<div class="msg-bar rounded-pill" id="msgBar">
						<p class="fw-semibold">GPS 설정중...</p>
					</div>

				</div>
					<%--<div class="test"> <input type="text" id="debug" value=""><input type="text" id="test" value=""> </div>--%>

			</div>
		</main>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script type="application/javascript">
            // INTERFACE : APP TO WEB
            function appToGps(x, y) {
                $.gpsPoint(x, y);
                let position = new kakao.maps.LatLng(gpsLatitude, gpsLongitude);
                myLocMarker.setPosition(position);
				areaOfCurrentLocation();
                if (isFirst) {
                    $.initializeMove(position);
                }
                isFirst = false;
            }

			function areaOfCurrentLocation (){
				let p = new Point(gpsLongitude,gpsLatitude);
				let len = $.polygons.length;
				let illegalType;
				for (let i = 0; i < len; i++) {
					let points = [];
					$.polygons[i].points.forEach(function (overlay) {
						let x = overlay.x, y = overlay.y;
						points.push(new Point(x, y));
					})
					illegalType = $.polygons[i].type;
					let onePolygon = points;
					let n = onePolygon.length;
					let divMsgBar = $('.msg-bar');
					let msg;
					let color;
					if (isInside(onePolygon, n, p)) {
						if(illegalType === 'ILLEGAL') {
							msg = '현재 구역은 불법주정차 구역입니다.';
							color = '#E63636B5'
						} else if (illegalType === 'FIVE_MINUTE') {
							msg = '현재 구역은 5분 주차 가능한 구역입니다.';
							color = '#FF9443B5'
						}

						divMsgBar.find('p').text(msg);
						divMsgBar.css('background-color', color);
						break;
					} else {
						msg = '현재 구역은 주차 가능한 구역입니다.';
						color = '#FFC527B5'
						divMsgBar.find('p').text(msg);
						divMsgBar.css('background-color', color);
					}
				}
			}

            $(function () {


                // 라디오버튼 change 이벤트 설정
                function handleRadioButton(event) {
                    let mapType = $('.mapType');
                    if ($.isMobile) webToApp.postMessage(JSON.stringify('click'));
                    for (const type of mapType) {
                        type.classList.remove("btn-dark");
                        type.classList.remove("rounded-pill");
                        type.classList.add("btn-white");
                    }

                    event.currentTarget.classList.remove("btn-white");
                    event.currentTarget.classList.add("btn-dark");
                    event.currentTarget.classList.add("rounded-pill");
                }

                // app 초기화
                function initializeByMobile() {
                    // 불법주차 / 주차장 / 모빌리티 변경 이벤트
                    $('input:radio[name=mapSelect]').change(function (event) {
                        $.loading(true);
                        if ($.isMobile) {
                            $.CENTER_LATITUDE = gpsLatitude;
                            $.CENTER_LONGITUDE = gpsLongitude;
                            // map.panTo(new kakao.maps.LatLng(CENTER_LATITUDE, CENTER_LONGITUDE));
                        }
                        let mapType = $('.mapType');
                        for (const type of mapType) {
                            type.addEventListener("change", handleRadioButton);
                        }
                        $.mapSelected = event.target.id;
                        $.changeMapType();
                        $.loading(false);
                    });

                    // 내위치 찾기 이벤트
                    $('#btnFindMe').on('click', function () {
                        $.findMe();
                    });

                    // 맵의 줌인 이벤트
                    $('#zoomIn').on('click', function () {
                        let level = $.getLevel('in');
                        if (level < 4) {
                            $.processDongCodesBounds();
                        }
                    });

                    // 맵의 줌아웃 이벤트
                    $('#zoomOut').on('click', function () {
                        let level = $.getLevel('out');
                        if (level <= 3) {
                            $.processDongCodesBounds();
                        } else {
                            if ($.mapSelected === 'zone') $.removeOverlays();
						}
                    });

                    $.findMe();
                    $.loading(false);

                }

                initializeByMobile();
            });

		</script>
	</stripes:layout-component>

</stripes:layout-render>