<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<% String contextPath = request.getContextPath(); %>
<%@ attribute name="reportSeq" type="java.lang.Integer" required="true" %>

<!-- content -->
<main id="reportSet">
	<div class="container-fluid px-4">
		<h1 class="mt-4">신고목록</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 신고목록</li>
		</ol>
		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header">
				<i class="fas fa-table me-1"></i>
				불법 주정차 신고 정보
			</div>
			<div class="card-body">
				<div class="row">

					<form class="row" id="data">

						<div class="col-6 g-2">
							<div class="card">
								<div class="card" style="width: 100%;">
									<img src="<%=contextPath%>/2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg" class="card-img-top" alt="...">
									<div class="card-body">
										<h5 class="card-title">Card title</h5>
										<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="col-6 g-2">
							<div class="card">
								<div class="card" style="width: 100%;">
									<img src="..." class="card-img-top" alt="...">
									<div class="card-body">
										<h5 class="card-title">Card title</h5>
										<p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
									</div>
								</div>
							</div>
						</div>
						<div class="col-1">
							<div class="btn-group">
								<c:choose>
									<c:when test="${path.equals('reportSet')}">
										<a type="submit" class="btn btn-primary" id="register">등록</a>
									</c:when>
									<c:otherwise>
										<a type="submit" class="btn btn-primary" id="modify">수정</a>
										<a class="btn btn-outline-success" id="close">닫기</a>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
</main>