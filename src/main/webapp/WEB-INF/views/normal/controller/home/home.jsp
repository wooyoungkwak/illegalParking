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
<%--		<jsp:include page="side.jsp" flush="true"/>--%>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<div class="row">
					<div class="col-2">
						<div class="mt-4 mb-5"><br /></div>
						<div class="mt-4 mb-5"><br /></div>
						<div class="row">
							<div class="col-xl-12 col-md-12">
								<div class="card text-black mb-4">
									<div class="card-body fw-bold">금일 신고 건수</div>
									<div class="card-footer d-flex align-items-center justify-content-between">
										<a class="small text-white stretched-link" href="#"></a>
										<div class="text-danger fw-bold fst-italic me-2"><span>50건</span></div>
									</div>
								</div>
							</div>
							<div class="col-xl-12 col-md-12">
								<div class="card text-dark mb-4">
									<div class="card-body fw-bold">10월 신고 건수</div>
									<div class="card-footer d-flex align-items-center justify-content-between">
										<a class="small text-white stretched-link" href="#"></a>
										<div class="text-danger fw-bold fst-italic me-2"><span>50건</span></div>
									</div>
								</div>
							</div>
							<div class="col-xl-12 col-md-12">
								<div class="card text-dark mb-4">
									<div class="card-body fw-bold">2022년 신고 건수</div>
									<div class="card-footer d-flex align-items-center justify-content-between">
										<a class="small text-white stretched-link" href="#"></a>
										<div class="text-danger fw-bold fst-italic me-2"><span>50건</span></div>
									</div>
								</div>
							</div>
							<div class="col-xl-12 col-md-12">
								<div class="card text-dark mb-4">
									<div class="card-body fw-bold">2022년도 신고 누락 건수</div>
									<div class="card-footer d-flex align-items-center justify-content-between">
										<a class="small text-white stretched-link" href="#"></a>
										<div class="text-primary fw-bold fst-italic me-2"><span>50건</span></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-9">
						<div class="row mt-5 mb-4">
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
								<c:forEach begin="1" end="12" varStatus="status">
									<tr>
										<td class="text-center">${status.index}</td>
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