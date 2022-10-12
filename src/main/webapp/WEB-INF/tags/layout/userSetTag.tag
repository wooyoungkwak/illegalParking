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

<%@ attribute name="items" type="java.lang.Object" required="true" %>

<!-- content -->
<main id="userSet">
	<div class="container-fluid px-4">

		<h1 class="mt-4">사용자</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 관공서</li>
		</ol>

		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header">
				<div class="row">
					<div class="col-9">
						<i class="fas fa-table"></i> 관공서 상세 정보
					</div>
					<div class="col-3 d-flex justify-content-end">
						<a class="btn btn-close" id="userClose"></a>
					</div>
				</div>
			</div>
			<%--            <div class="row">--%>
			<%--                <div class="col-11"></div>--%>
			<%--                <div class="col-1">--%>
			<%--                    <a class="ms-5 btn btn-close" id="userClose"></a>--%>
			<%--                </div>--%>
			<%--            </div>--%>

			<form id="userForm">
				<input type="hidden" name="userSeq">
				<div class="row mt-2 mb-2">
					<div class="col-1 d-flex justify-content-lg-end">
						<label class="mt-2">관공서명 : </label>
					</div>
					<div class="col-3">
						<input class="form-control" id="governmentOfficeName" name="name" disabled>
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">지역 : </label></div>
					<div class="col-3">
						<tags:selectTagWithType id="locationType" current="" items="${items}"/>
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">아이디 : </label></div>
					<div class="col-3">
						<input class="form-control" id="userName" name="userName">
					</div>
				</div>
				<div class="row mb-2">
					<div class="col-1 d-flex justify-content-lg-end"><label class="mt-2">패스워드 : </label></div>
					<div class="col-3">
						<input class="form-control" type="text" id="password" name="password">
					</div>
				</div>
			</form>

			<div class="row mb-3">
				<div class="col-4 d-flex justify-content-lg-end">
					<a class="btn btn-primary" id="userModify"><i class="fa fa-cog"></i> 정보수정</a>
				</div>
			</div>

			<div class="row mb-2">
				<div class="col-1 mt-2 d-flex justify-content-lg-center">
					<label>관리 그룹</label>
				</div>
				<div class="col-2 d-flex justify-content-lg-start">
					<a class="btn btn-outline-primary"><i class="fas fa-plus"></i> 그룹추가</a>
				</div>
			</div>

			<nav class="navbar navbar-expand-lg navbar-light bg-light ms-3 me-3 mb-2">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item">
						<a class="nav-link" href="#">나주 A <i class="text-danger fa fa-times"></i></a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#">나주 B <i class="text-danger fa fa-times"></i></a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#">나주 C <i class="text-danger fa fa-times"></i></a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#">나주 D <i class="text-danger fa fa-times"></i></a>
					</li>
				</ul>
			</nav>

			<div class="row">
				<div class="col-8">

				</div>
				<div class="col-6">

				</div>
			</div>
		</div>

	</div>
</main>
<script type="application/javascript">
    $(function () {

        $('#userModify').on('click', function () {
			log($('#userForm').serialize());
        });
    });
</script>

