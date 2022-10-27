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
						<p class="fw-semibold">현재 지역은 주정차 금지 지역입니다.</p>
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
		<script src="<%=contextPath%>/resources/js/area/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script type="application/javascript">
            // INTERFACE : APP TO WEB
            function appToGps(x, y) {
                $.gpsPoint(x, y);
            }
		</script>
	</stripes:layout-component>

</stripes:layout-render>