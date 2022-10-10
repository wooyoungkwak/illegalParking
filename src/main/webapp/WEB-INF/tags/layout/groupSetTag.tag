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
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>
<% String contextPath = request.getContextPath(); %>

<!-- content -->
<main id="groupSet">
    <input type="hidden" id="groupSeq">
    <div class="container-fluid px-4">
        <h1 class="mt-4">신고목록</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item active">${subTitle} > 그룹관리</li>
        </ol>
        <div class="card mb-2 shadow-sm rounded">

            <div class="card-header">
                <div class="row">
                    <div class="col-9">
                        <i class="fas fa-table"></i>
                        그룹설정
                    </div>
                    <div class="col-3 d-flex justify-content-end">
                        <a class="ms-5 btn btn-close" id="closeGroupSet"></a>
                    </div>
                </div>
            </div>

            <div class="card-body">
                <div class="row mb-2">
                    <div class="col-2">
                        <label class="ms-5">그룹위치</label>
                    </div>
                    <div class="col-2">
                        <select class="form-select" id="locationType" disabled>
                            <c:forEach items="${enumValues}" var="enumValue">
                                <option value="${enumValue.value}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row mb-5">
                    <div class="col-2">
                        <label class="ms-5">그룹명</label>
                    </div>
                    <div class="col-2">
                        <input type="text" class="form-control" id="name" disabled>
                    </div>

                </div>
                <div class="row mb-2">
                    <div class="col-12 d-flex justify-content-end">
                        <a class="btn btn-primary" id="openEventAdd"><i class="fas fa-plus"></i> 이벤트추가</a>
                    </div>
                </div>
                <table class="table table-bordered" id="pointTable">
                    <thead>
                        <tr>
                            <th scope="col">분류</th>
                            <th scope="col">제한포인트</th>
                            <th scope="col">제공포인트</th>
                            <th scope="col">누적사용량</th>
                            <th scope="col">남은포인트</th>
                            <th scope="col">시작일자</th>
                            <th scope="col">종료일자</th>
                            <th scope="col">마감기준</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>포인트제공</td>
                            <td>500,000</td>
                            <td>5,000</td>
                            <td>105,000</td>
                            <td>395,000</td>
                            <td>2022-11-20</td>
                            <td>2022-12-30</td>
                            <td>소진시</td>
                        </tr>
                        <tr>
                            <td>포인트제공</td>
                            <td>-</td>
                            <td>1,000</td>
                            <td>328,000</td>
                            <td>-</td>
                            <td>2022-09-01</td>
                            <td>2022-09-30</td>
                            <td>모두</td>
                        </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</main>



