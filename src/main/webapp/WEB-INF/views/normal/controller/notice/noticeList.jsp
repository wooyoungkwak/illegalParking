<%--
  Created by IntelliJ IDEA.
  User:
  Date: 2022-10-17
  Time: 오전 8:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layoutTags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType" %>
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
		<main id="noticeMain">
			<div class="container-fluid px-4">
				<h1 class="mt-4">공지사항</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active">${subTitle} > 공지사항</li>
				</ol>
				<div class="card mb-4 shadow-sm rounded">
					<div class="card-header">
						<i class="fas fa-table me-1"></i>
						공지 사항 정보
					</div>
					<div class="card-body">
						<form class="row mb-3">
							<input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
							<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
							<div class="col-7 d-flex justify-content-lg-start">
								<a class="btn btn-primary" id="write">
									<i class="fas fa-pen me-1"></i> 글작성
								</a>
							</div>
							<div class="col-1">
								<tags:filterTag id="filterColumn" enumValues="${NoticeFilterColumn.values()}" column="${filterColumn}"/>
							</div>
							<div class="col-4">
								<tags:searchTag id="searchStr" searchStr="${searchStr}"/>
								<tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${NoticeType.values()}"/>
							</div>
						</form>
						<table class="table table-hover">
							<thead>
							<tr>
								<th scope="col" class="text-center" style="width: 5%;">분류</th>
								<th scope="col" class="text-center" style="width: 25%;">제목</th>
								<th scope="col" class="text-center" style="width: 50%;">내 용</th>
								<th scope="col" class="text-center" style="width: 10%;">등록일</th>
								<th scope="col" class="text-center" style="width: 10%;">등록자</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${notices}" var="notice" varStatus="status">
								<tr>
									<td class="text-center">${notice.noticeType.value}</td>
									<td class="text-center">${notice.subject}</td>
									<td>${notice.content}</td>
									<td class="text-center">
										<fmt:parseDate value="${notice.regDt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both" />
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td class="text-center">${notice.user.name}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}" isRegister="false"/>
					</div>
				</div>
			</div>
		</main>

		<layoutTags:noticeSetTag items="${NoticeType.values()}"/>


	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/notice/noticeList-scripts.js"></script>
		<script type="application/javascript">
			$(function (){

                // 검색 입력 방식 선택 함수
                function searchSelect(filterColumn) {
                    if (filterColumn === 'NOTICETYPE') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

                // 초기화
                function initialize() {

                    // 검색 이벤트
                    $('#searchStr').next().on('click', function (event) {
                        $.search();
                    });

                    // 검색 이벤트
                    $('#searchStr2').next().on('click', function (event) {
                        $.search();
                    });

                    // 필터 변경 이벤트
                    $('#filterColumn').find('select').on('change', function (){
                        searchSelect($(this).val());
					});

					$('#write').on('click', function (){
						$.openNoticeSet();
					});

                    // 필터에 의한 검색 입력 방식 선택
                    searchSelect('${filterColumn}');

                    //
                    $('#noticeSet').hide();

                }

                initialize();

			});
		</script>
	</stripes:layout-component>

</stripes:layout-render>