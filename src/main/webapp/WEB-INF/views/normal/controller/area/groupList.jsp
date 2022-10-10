<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.point.enums.PointType" %>
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
        <main id="groupMain">
            <div class="container-fluid px-4">
                <h1 class="mt-4">불법주정차 구역</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item active">${subTitle} > 그룹관리</li>
                </ol>
                <div class="card mb-4 shadow-sm rounded">
                    <div class="card-header">
                        <i class="fas fa-table me-1"></i>
                        그룹관리
                    </div>
                    <div class="card-body">
                        <form class="row mb-3 g-3">
                            <input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
                            <input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
                            <div class="col-2">
                                <a class="btn btn-primary" id="openGroupAdd"><i class="fa fa-bookmark"></i> 그룹추가</a>
                            </div>
                            <div class="col-5"></div>
                            <div class="col-1">
                                <tags:filterTag id="filterColumn" enumValues="${GroupFilterColumn.values()}" column="${filterColumn}"/>
                            </div>
                            <div class="col-4">
                                <tags:searchTag id="searchStr" searchStr="${searchStr}"/>
                                <tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${LocationType.values()}"/>
                            </div>

                        </form>
                        <table class="table table-hover table-bordered" id="userTable">
                            <thead>
                                <tr>
                                    <th scope="col">그룹위치</th>
                                    <th scope="col">그룹명</th>
                                    <th scope="col">포함구역</th>
                                    <th scope="col">설정내용</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${illegalGroupDtos}" var="illegalGroupDto" varStatus="status">
                                    <tr>
                                        <td>
                                            <input type="hidden" value="${illegalGroupDto.groupSeq}">
                                            ${illegalGroupDto.locationType.value}
                                        </td>
                                        <td>${illegalGroupDto.name}</td>
                                        <td>${illegalGroupDto.groupSize}</td>
                                        <td>${illegalGroupDto.note}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="15,30,50" pageSize="${pageSize}"/>
                    </div>
                </div>
            </div>
        </main>

        <layoutTags:groupSetTag enumValues="${LocationType.values()}"/>

        <layoutTags:groupAddTag id="modalGroupAdd" enumValues="${LocationType.values()}"/>

        <layoutTags:groupEventAddTag id="modalGroupEvent" enumValues="${PointType.values()}"/>

    </stripes:layout-component>

    <!-- footer -->
    <stripes:layout-component name="footer">
        <stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript">
        <script src="<%=contextPath%>/resources/js/area/groupList-scripts.js"></script>
    </stripes:layout-component>

</stripes:layout-render>