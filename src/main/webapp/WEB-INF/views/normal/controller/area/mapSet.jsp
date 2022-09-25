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
<%@ taglib prefix="layoutTags" tagdir="/WEB-INF/tags/layout" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/navMapHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">지도 보기</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> ${subTitle} > 지도 보기</li>
				</ol>

				<div class="row">

					<div class="map_wrap">
						<div id="drawingMap"></div>
						<p class="modes">
							<button onclick="selectOverlay('POLYGON')">구역추가</button>
							<button onclick="getDataFromDrawingMap()">저장</button>
							<span>
								<label><input type="radio" name="searchIllegalTypeSeq" value="" checked>전체</label>
								<label><input type="radio" name="searchIllegalTypeSeq" value="0">불법주정차</label>
								<label><input type="radio" name="searchIllegalTypeSeq" value="1">5분주정차</label>
							</span>
						</p>
					</div>

					<p id="result"></p>

					<layoutTags:mapSetModalTag id="areaSettingModal" />

				</div>
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
		<script src="<%=contextPath%>/resources/js/area/mapSet-scripts.js"></script>
	</stripes:layout-component>

</stripes:layout-render>