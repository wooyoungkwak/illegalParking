<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side">
		<%--        <jsp:include page="side.jsp" flush="true"/>--%>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">Dashboard</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">Dashboard</li>
				</ol>
				<div class="row">
					<div class="col-xl-3 col-md-6">
						<div class="card bg-primary text-white mb-4">
							<div class="card-body">금일 신고 건수</div>
							<div class="card-footer d-flex align-items-center justify-content-between">
								<a class="small text-white stretched-link" href="#">View Details</a>
								<div class="small text-white"><i class="fas fa-angle-right"></i></div>
							</div>
						</div>
					</div>
					<div class="col-xl-3 col-md-6">
						<div class="card bg-warning text-white mb-4">
							<div class="card-body">10월 신고 건수</div>
							<div class="card-footer d-flex align-items-center justify-content-between">
								<a class="small text-white stretched-link" href="#">View Details</a>
								<div class="small text-white"><i class="fas fa-angle-right"></i></div>
							</div>
						</div>
					</div>
					<div class="col-xl-3 col-md-6">
						<div class="card bg-success text-white mb-4">
							<div class="card-body">2022년 신고 건수</div>
							<div class="card-footer d-flex align-items-center justify-content-between">
								<a class="small text-white stretched-link" href="#">View Details</a>
								<div class="small text-white"><i class="fas fa-angle-right"></i></div>
							</div>
						</div>
					</div>
					<div class="col-xl-3 col-md-6">
						<div class="card bg-danger text-white mb-4">
							<div class="card-body">2022년도 신고 누락 건수</div>
							<div class="card-footer d-flex align-items-center justify-content-between">
								<a class="small text-white stretched-link" href="#">View Details</a>
								<div class="small text-white"><i class="fas fa-angle-right"></i></div>
							</div>
						</div>
					</div>
				</div>

				<div class="row mt-5 mb-5">
					<div class="col-4"></div>
					<div class="col-4 border-bottom text-center"><p class="h1 fw-bold">공지사항</p></div>
					<div class="col-4"></div>
				</div>
				<div class="row">
					<table class="table table-hover">
						<thead>
						<tr>
							<th scope="col" class="text-center" style="width: 5%;">#</th>
							<th scope="col" class="text-center" style="width: 75%;">내 용</th>
							<th scope="col" class="text-center" style="width: 10%;">등록자</th>
							<th scope="col" class="text-center" style="width: 10%;">등록일</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach begin="1" end="20" varStatus="status">
							<tr>
								<td class="text-center">${status.index}</td>
								<td>공지사항 내용 ${status.index}.....</td>
								<td class="text-center">관리자</td>
								<td class="text-center">2022-10-01</td>
							</tr>
						</c:forEach>
						</tbody>
						<tfoot>
						<tr>
							<th scope="col" class="text-center" style="width: 5%;">#</th>
							<th scope="col" class="text-center" style="width: 75%;">내 용</th>
							<th scope="col" class="text-center" style="width: 10%;">등록자</th>
							<th scope="col" class="text-center" style="width: 10%;">등록일</th>
						</tr>
						</tfoot>
					</table>
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