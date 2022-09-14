<%--
  Created by IntelliJ IDEA.
  User: zilet
  Date: 2022-03-02
  Time: 오후 8:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>

<stripes:layout-definition>
    <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
        <!-- Navbar Brand-->
        <a class="navbar-brand ps-3" href="${pageContext.request.contextPath}/home">${mainTitle}</a>

        <!-- Sidebar Toggle-->
        <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>

        <ul id="navMenu" class="navbar-nav ms-auto mt-2 mt-lg-0">
            <li class="nav-item me-5">
                <a class="nav-link" id="navreport" href="${pageContext.request.contextPath}/report">신고</a>
            </li>
            <li class="nav-item me-5">
                <a class="nav-link" id="navarea" href="${pageContext.request.contextPath}/area">불법주정차 구역</a>
            </li>
            <li class="nav-item me-5">
                <a class="nav-link" id="navparking" href="${pageContext.request.contextPath}/parking" tabindex="-1" aria-disabled="true">공영주차장</a>
            </li>
            <li class="nav-item me-5">
                <a class="nav-link" id="navpm" href="${pageContext.request.contextPath}/pm" tabindex="-1" aria-disabled="true">전동 킥보드</a>
            </li>
            <li class="nav-item me-5">
                <a class="nav-link" id="navcalculate" href="${pageContext.request.contextPath}/calculate" tabindex="-1" aria-disabled="true">결제</a>
            </li>
        </ul>

        <!-- Navbar Search-->
            <%--        <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">--%>
            <%--            <div class="input-group">--%>
            <%--                <input class="form-control" type="text" placeholder="Search for..." aria-label="Search for..."--%>
            <%--                       aria-describedby="btnNavbarSearch"/>--%>
            <%--                <button class="btn btn-primary" id="btnNavbarSearch" type="button"><i class="fas fa-search"></i>--%>
            <%--                </button>--%>
            <%--            </div>--%>
            <%--        </form>--%>

        <!-- Navbar-->
        <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown"
                   aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                    <li><a class="dropdown-item" href="#!">Settings</a></li>
                    <li><a class="dropdown-item" href="#!">Activity Log</a></li>
                    <li>
                        <hr class="dropdown-divider"/>
                    </li>
                    <li><a class="dropdown-item" href="#!">Logout</a></li>
                </ul>
            </li>
        </ul>
    </nav>
</stripes:layout-definition>