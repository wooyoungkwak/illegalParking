<%--
  Created by IntelliJ IDEA.
  User: zilet
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn" %>
<% String contextPath = request.getContextPath(); %>

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
        <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">결재목록</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">${subTitle} > 결재목록</li>
                        </ol>
                        <div class="card mb-4 shadow-sm rounded">
                            <div class="card-header">
                                <i class="fas fa-table me-1"></i>
                                주차장 정보
                            </div>
                            <div class="card-body">
                                <form class="row mb-3 g-3">
                                    <input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
                                    <div class="col-1">
                                        <tags:selectTag id="pageSize" title="개수" items="10,25,50" current="${pageSize}"/>
                                    </div>
                                    <div class="col-2"></div>
                                    <div class="col-1">
                                        <tags:filterTag id="filterColumn" enumValues="${ParkingFilterColumn.values()}" column="${filterColumn}"/>
                                    </div>
                                    <div class="col-4">
                                        <tags:searchTag id="searchStr" title="검색"/>
                                    </div>
                                    <div class="col-1"></div>
                                    <div class="col-3">
                                        <tags:sortTag id="orderBy" enumValues="${ParkingOrderColumn.values()}" column="${orderColumn}"/>
                                    </div>
                                </form>
                                <table class="table table-hover table-bordered">
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
                                                <td>${parking.rdnmadr}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}"/>
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
        <script src="<%=contextPath%>/resources/js/calculate/calculateList-scripts.js"></script>
    </stripes:layout-component>

</stripes:layout-render>