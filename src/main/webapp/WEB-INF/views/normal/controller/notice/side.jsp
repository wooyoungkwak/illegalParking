<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-09-07
  Time: 오전 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="layoutSidenav_nav">
	<nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
		<div class="sb-sidenav-menu">
			<div class="nav">
				<div class="sb-sidenav-menu-heading">${subTitle}</div>
				<a class="nav-link active" href="${pageContext.request.contextPath}/noticeList" id="side_noticeList">
					<div class="sb-nav-link-icon"><i class="fas fa-table"></i></div>
					공지 사항
				</a>
			</div>
		</div>
	</nav>
</div>