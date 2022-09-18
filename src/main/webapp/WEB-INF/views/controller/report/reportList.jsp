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
                <h1 class="mt-4">신고목록</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item active">${subTitle} > 신고목록</li>
                </ol>
                <div class="card mb-4 shadow-sm rounded">
                    <div class="card-header">
                        <i class="fas fa-table me-1"></i>
                        불법 주정차 신고 정보
                    </div>
                    <div class="card-body">
                        <form class="row mb-3 g-3">
                            <div class="col-1">
                                <tags:selectTag id="rowNumber" title="개수" items="10,25,50" current="${rowNumber}"/>
                            </div>
                            <div class="col-1">
                                <tags:selectTag id="filter" title="필터" items="위반종류,차량번호,접수시간" current="위반종류"/>
                            </div>
                            <div class="col-1">
                                <tags:selectTag id="sort" title="정렬" items="내림차순,오름차순" current="내림차순"/>
                            </div>
                            <div class="col-2"></div>
                            <div class="col-7">
                                <label for="search" class="form-label">검색</label>
                                <div class="input-group">
                                <input id="search" class="form-control" type="search" placeholder="Search" aria-label="Search">
                                <button class="btn btn-outline-success" type="submit">Search</button>
                                </div>
                            </div>
                        </form>
                        <table class="table table-hover table-bordered" id="reportList">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">위반종류</th>
                                    <th scope="col">차량번호</th>
                                    <th scope="col">접수시간</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="sample" begin="1" end="10" varStatus="status">
                                    <tr>
                                        <td>${sample}</td>
                                        <td>5분주정차</td>
                                        <td>123가1234</td>
                                        <td>2022-01-01</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <tags:pageTag pageNumber="${pageNumber}" begin="${begin}" end="${end}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}"/>
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
        <script src="<%=contextPath%>/resources/js/report/reportList-scripts.js"></script>
    </stripes:layout-component>

</stripes:layout-render>