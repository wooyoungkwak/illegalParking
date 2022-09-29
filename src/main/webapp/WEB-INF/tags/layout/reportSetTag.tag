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
<%@ attribute name="items" type="java.lang.Object" required="true" %>
<% String contextPath = request.getContextPath(); %>

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
				차량 번호 :
				<span id="firstCarNum" class="title"></span>
			</div>
			<div class="card-body">
				<div class="row">

					<form id="data">
						<div class="row mb-2 ms-2">
							<div class="col-6">
								<div class="card">
									<div class="card" style="width: 100%;">
										<img id="firstFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
										<div class="card-body">
											<h5 class="card-title mb-4 fst-italic fw-bold text-danger">1차 신고</h5>
											<p class="card-text">위반 종류 : <span id="firstIllegalType" class="title"></span></p>
											<p class="card-text">위반 장소 : <span id="firstAddr" class="title"></span></p>
											<p class="card-text">접수 시간 : <span id="firstRegDt" class="title"></span></p>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6 m-0">
								<div class="card ">
									<div class="card" style="width: 100%;">
										<img id="secondFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
										<div class="card-body">
											<h5 class="card-title mb-4  fst-italic fw-bold text-danger">2차 신고</h5>
											<p class="card-text">위반 종류 : <span id="secondIllegalType" class="title"></span></p>
											<p class="card-text">위반 장소 : <span id="secondAddr" class="title"></span></p>
											<p class="card-text">접수 시간 : <span id="secondRegDt" class="title"></span></p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row mb-4 ms-2">
							<div class="col-4 mb-2">
								<tags:typeSelectTag id="setResultType" current="" items="${items}" title="대상:" />
							</div>
							<div class="col-12">
								<tags:inputTag id="note" placeholder="내용을 입력하세요." value="" title="설명:"/>
							</div>
						</div>
						<div class="row ms-2 ">
							<div class="col-2 btn-group">
								<a type="submit" class="btn btn-primary" id="register">등록</a>
								<a class="btn btn-outline-secondary" id="close">닫기</a>
							</div>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
</main>