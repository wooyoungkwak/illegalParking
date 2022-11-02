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
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType" %>
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

					<div class="card">
						<div class="card-title">
							<div class="row mt-2 ms-2 p-0">
								<div class="col-6 mt-2 align-middle">
									<input class="form-check-input" type="radio" name="searchIllegalType" id="type_all" value="" checked>
									<label class="form-check-label" for="type_all">전체</label>
									<c:forEach items="${IllegalType.values()}" var="type">
										<input class="form-check-input" type="radio" name="searchIllegalType" value="${type}" id="type_${type}">
										<label class="form-check-label" for="type_${type}">${type.value}</label>
									</c:forEach>
								</div>
								<div class="col-6 d-flex justify-content-end">
									<div class="me-3">
										<a class="btn btn-sm btn-outline-success" id="btnAddOverlay">구역추가</a>
										<a class="btn btn-sm btn-outline-dark" id="btnModifyOverlay">구역수정</a>
										<a class="btn btn-sm btn-outline-primary" id="btnSet">저장</a>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body mt-0 pt-0">
								<%--							<div class="map_wrap">--%>
							<div id="drawingMap"></div>
								<%--							</div>--%>
						</div>
					</div>

					<p class="text-danger fs-10 m-0 mt-1 mb-1" id="postscript"> <i class="fas fa-exclamation-triangle"></i> 20m : 1레벨, 50m : 3레벨, 3레벨 이하 구역이 표시. 현재 [ <span class="text-primary fw-bold" id="mapLevel">3레벨</span> ]</p>

					<layoutTags:mapSetModalTag id="areaSettingModal" typeValues="${IllegalType.values()}" enumValues="${LocationType.values()}" current=""/>

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
		<script src="<%=contextPath%>/resources/js/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/mapSet-scripts.js"></script>
	</stripes:layout-component>

</stripes:layout-render>