<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<% String contextPath = request.getContextPath(); %>
<%@ page import="com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn" %>
<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main id="parkingTable">
			<div class="container-fluid px-4">
				<h1 class="mt-4">주차장목록</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 주차장목록</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						주차장 정보
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-1"></div>
							<div class="col-2"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${ParkingFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}" />
							</div>
							<div class="col-1"></div>
							<div class="col-3">
								<tags:sortTag id="orderBy" enumValues="${ParkingOrderColumn.values()}" column="${orderColumn}" direction="${orderDirection}"/>
							</div>
						</form>
						<table class="table table-hover table-bordered" id="tableList">
							<thead>
							<tr>
								<th scope="col">#</th>
								<th scope="col">주차장명</th>
								<th scope="col">요금</th>
								<th scope="col">운행요일</th>
								<th scope="col">평일시간</th>
								<th scope="col">주소</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="parking" items="${parkings}" varStatus="status">
								<tr>
									<td>${parking.parkingSeq}</td>
									<td>${parking.prkplceNm}</td>
									<td>${parking.parkingchrgeInfo}</td>
									<td>${parking.operDay}</td>
									<td>${parking.weekdayOperOpenHhmm} ~ ${parking.weekdayOperColseHhmm}</td>
									<td>
										<c:choose>
											<c:when test="${ parking.rdnmadr == ''}">${parking.lnmadr}</c:when>
											<c:otherwise>${parking.rdnmadr}</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}"/>
					</div>
				</div>
			</div>
		</main>
		
		<layoutTags:parkingAddTag parkingSeq="1" path="parkingList"/>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/parking/parkingList-scripts.js"></script>
	</stripes:layout-component>

</stripes:layout-render>