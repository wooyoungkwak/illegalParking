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

            <div class="card-body row">
                <div class="col-3 ms-3"><label>관공서명 : </label></div>
                <div class="col-9"><span id="governmentOfficeName"></span></div>
                <div class="col-3 ms-3"><label>지역 : </label></div>
                <div class="col-9"><span id="locationType"></span></div>
                <div class="col-3 ms-3"><label>아이디 : </label></div>
                <div class="col-9"><span id="userName"></span></div>
                <div class="col-3 ms-3"><label>패스워드 : </label></div>
                <div class="col-9"><span id="password"></span></div>
                <div class="col-12">
                    <div class="input-group">
                        <a class="btn btn-primary" id="userModify"><i class="fas fa-sun"></i> 정보수정</a>
                    </div>
                </div>
                <div class="col-12 ms3"><label>관리 그룹</label></div>
<%--                <div class="col-9"><a class="btn btn-primary"><i class="fas fa-plus"></i>그룹추가</a></div>--%>
            </div>
        </div>

    </div>
</main>


