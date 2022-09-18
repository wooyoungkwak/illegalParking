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
                                <div class="row mb-3" >
                                    <div class="col-2">
                                        <select class="form-select" aria-label="Default select example">
                                            <option value="1" selected>10</option>
                                            <option value="2">25</option>
                                            <option value="3">50</option>
                                        </select>
                                    </div>
                                    <div class="col-5">
                                    </div>
                                    <div class="col-5">
                                        <form class="d-flex">
                                            <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                                            <button class="btn btn-outline-success" type="submit">Search</button>
                                        </form>
                                    </div>
                                </div>
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
                                                <td>${parking.prkSeq}</td>
                                                <td>${parking.prkplceNm}</td>
                                                <td>${parking.parkingchrgeInfo}</td>
                                                <td>${parking.operDay}</td>
                                                <td>${parking.weekdayOperOpenHhmm} ~ ${parking.weekdayOperColseHhmm}</td>
                                                <td>${parking.rdnmadr}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <tags:pageTag pageNumber="1" begin="1" end="3" isBeginOver="false" isEndOver="false"/>
<%--                                <div class="row">--%>
<%--                                    <div class="col-5"></div>--%>
<%--                                    <div class="col-5"></div>--%>
<%--                                    <div class="col-2">--%>
<%--                                        <nav aria-label="Page navigation example">--%>
<%--                                            <ul class="pagination">--%>
<%--                                                <li class="page-item"><a class="page-link" href="#">Previous</a></li>--%>
<%--                                                <li class="page-item"><a class="page-link" href="#">1</a></li>--%>
<%--                                                <li class="page-item"><a class="page-link" href="#">2</a></li>--%>
<%--                                                <li class="page-item"><a class="page-link" href="#">3</a></li>--%>
<%--                                                <li class="page-item"><a class="page-link" href="#">Next</a></li>--%>
<%--                                            </ul>--%>
<%--                                        </nav>--%>
<%--                                    </div>--%>
<%--                                </div>--%>

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