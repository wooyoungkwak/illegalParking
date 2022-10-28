<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/navMapHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">지도 보기</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> ${subTitle} > 지도 보기</li>
				</ol>

				<div class="row">

					<div class="card">
						<div class="card-title">
							<p class="modes">
								<span>
									<label><input type="radio" name="searchIllegalType" value="" checked>전체</label>
									<c:forEach items="${IllegalType.values()}" var="type">
										<label><input type="radio" name="searchIllegalType" value="${type}">${type.value}</label>
									</c:forEach>
								</span>
							</p>
						</div>
						<div class="card-body mt-4">
								<%--							<div class="map_wrap">--%>
							<div id="map"></div>
								<%--							</div>--%>
						</div>
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
		<script src="<%=contextPath%>/resources/js/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script type="application/javascript">
			$(function () {

                // 초기화 함수
                function initializeByWeb() {
                    // 주정차 별 구역 조회
                    $('input:radio[name=searchIllegalType]').change(function () {
                        $.changeIllegalType();
                    });
					$.findMe();
                }

                // 초기화 실행
                initializeByWeb();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>