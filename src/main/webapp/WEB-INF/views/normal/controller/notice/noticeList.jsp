<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-10-17
  Time: 오전 8:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn" %>
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
		<main id="noticeMain">
			<div class="container-fluid px-4">
				<h1 class="mt-4">공지사항</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 공지사항</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						공지 사항 정보
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${NoticeFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
							</div>
						</form>
						<table class="table table-hover">
							<thead>
							<tr>
								<th scope="col" class="text-center" style="width: 5%;">#</th>
								<th scope="col" class="text-center" style="width: 25%;">제목</th>
								<th scope="col" class="text-center" style="width: 50%;">내 용</th>
								<th scope="col" class="text-center" style="width: 10%;">등록자</th>
								<th scope="col" class="text-center" style="width: 10%;">등록일</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach begin="1" end="12" varStatus="status">
								<tr>
									<td class="text-center">${status.index}</td>
									<td class="text-center">제목 ${status.index}.....</td>
									<td>공지사항 내용 ${status.index}.....</td>
									<td class="text-center">관리자</td>
									<td class="text-center">2022-10-01</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}" isRegister="true"/>
					</div>
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
		<script src="<%=contextPath%>/resources/js/scripts.js"></script>
	</stripes:layout-component>

</stripes:layout-render>