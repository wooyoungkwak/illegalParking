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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType" %>

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
		<main id="reportMain">
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
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-3">
								<tags:filterTagByButton id="reportStateType" current="${reportStateType}" enumValues="${ReportStateType.values()}"/>
							</div>

							<div class="col-4"></div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${ReportFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
								<tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${ResultType.values()}"/>
							</div>
						</form>
						<table class="table table-hover table-bordered" id="reportTable">
							<thead>
							<tr>
								<th scope="col" class="text-center" style="width: 10%">신고자</th>
								<th scope="col" class="text-center" style="width: 10%">차량번호</th>
								<th scope="col" class="text-center" style="width: 40%">위치</th>
								<th scope="col" class="text-center" style="width: 20%">신고일시</th>
								<th scope="col" class="text-center" style="width: 10%">중복회수</th>
								<th scope="col" class="text-center" style="width: 10%">상태</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="receipt" items="${receipts}" varStatus="status">
								<tr>
									<td class="text-center" >
										<input type="hidden" value="${receipt.receiptSeq}" id="reportSeq">
										${receipt.name}
									</td class="text-center">
									<td class="text-center">${receipt.carNum}</td>
									<td>${receipt.addr}</td>
									<td class="text-center" >
										<fmt:parseDate value="${receipt.regDt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both" />
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td class="text-center" >${receipt.overlapCount}</td>
									<td class="text-center" >${receipt.receiptStateType.value}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}"/>
					</div>
				</div>
			</div>
		</main>

		<layoutTags:receiptSetTag/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/report/receiptList-scripts.js"></script>
		<script type="application/javascript">
            if ('${filterColumn}' === 'RESULT') {
                $('#searchStrGroup').hide();
                $('#searchStr2Group').show();
            } else {
                $('#searchStrGroup').show();
                $('#searchStr2Group').hide();
            }
		</script>
	</stripes:layout-component>

</stripes:layout-render>