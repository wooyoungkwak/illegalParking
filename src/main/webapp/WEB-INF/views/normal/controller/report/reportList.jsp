<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
  접수 목록
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
							<c:forEach var="report" items="${reports}" varStatus="status">
								<tr>
									<td class="text-center" >
										<input type="hidden" value="${report.reportSeq}" id="reportSeq">
										${report.name}
									</td class="text-center">
									<td class="text-center">${report.carNum}</td>
									<td>
											${report.addr}
									</td>
									<td class="text-center" >
										<fmt:parseDate value="${report.regDt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both" />
										<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td class="text-center" ></td>
									<td class="text-center" >${report.reportStateType.value}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="10,25,50" pageSize="${pageSize}"/>
					</div>
				</div>
			</div>
		</main>

		<layoutTags:reportSetTag/>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/report/reportList-scripts.js"></script>
		<script type="application/javascript">
			$(function () {

                // 검색
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/reportList?" + $('form').serialize();
                }

                // 신고 접수 설정 초기화 함수
                function initializeReportSetTag(report) {
                    $.each(report, function (key, value) {
                        if (key.indexOf('receiptStateType') > -1) {
                            if (value === 'COMPLETE') {
                                $('#' + key).text("신고접수");
                            } else if (value === 'PENALTY')  {
                                $('#' + key).text("신고제외");
                            } else if (value === 'EXCEPTION') {
                                $('#' + key).text("과태료대상");
                            }
                        } else if (key.indexOf("firstFileName") > -1) {
                            $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/image/" + value));
                        } else if (key.indexOf("secondFileName") > -1) {
                            $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/image/" + value));
                        } else if (key === 'firstIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
                        } else if (key === 'secondIllegalType') {
                            if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                            else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
                        } else if (key === 'note') {
                            $('#' + key).val(value === null ? "" : value);
                        } else if( key === 'regDt' || key === 'firstRegDt' || key === 'secondRegDt' ) {
                            $('#' + key).text(value.replace('T', ' '));
                        } else if (key === 'resultType') {
                            if (value === undefined || value === 'WAIT') {
                                $('#register').show();
                            } else {
                                $('#register').hide();
                                $('#setResultType').attr("disabled", true);
                                $('#note').attr("disabled", true);
                            }
                            $('#setResultType').val(value);
                        } else {
                            $('#' + key).text(value);
                        }
                    });

                }

                // 신고 접수 설정 타이틀 설정 함수
                function initializeReportSetTagTitle(carNum) {
                    $('#reportSetTitle').text(carNum);
                }

                // 검색 입력 방식 선택
                function searchSelect(filterColumn) {
                    if (filterColumn === 'RESULT') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

				// 초기화 함수
                function initialize() {

                    // 검색 이벤트 1
                    $('#searchStr').next().on('click', function (event) {
                        search();
                    });

                    // 검색 이벤트 2
                    $('#searchStr2').next().on('click', function (event) {
                        search();
                    });

                    // 필터 변경 이벤트
                    $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
                        searchSelect($(this).val());
                    });

                    // 페이지 번호 검색 이벤트
                    $('#pagination').find("li").on('click', function () {
                        let ul = $(this).parent();
                        let totalSize = ul.children("li").length;
                        if (totalSize <= 3) {
                            return;
                        }
                        let pageNumber;
                        if ($(this).text() === "<") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            if (pageNumber == 1) return;
                            pageNumber = pageNumber - 1;

                        } else if ($(this).text() === ">") {
                            pageNumber = Number.parseInt(ul.children('.active').text());
                            let myLocation = $(this).index();
                            let activeLocation = ul.children('.active').index();
                            if (activeLocation == (myLocation - 1)) {
                                return;
                            }
                            pageNumber = pageNumber + 1;
                        } else {
                            pageNumber = Number.parseInt($(this).text());
                        }

                        search(pageNumber);
                    });

                    // 페이지 사이즈 변경 이벤트
                    $('#paginationSize').on("change", function () {
                        $('#pageNumber').val($(this).val());
                        search();
                    });

                    // 신고 등록 표시
                    $('#reportTable tbody tr').on('click', function () {

                        let reportSeqStr = $(this).children("td:eq(0)").find('input').val();
                        let carNum = $(this).children("td:eq(3)").text();

                        let reportSeq = Number.parseInt(reportSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/report/get',
                            data: {
                                reportSeq: reportSeq
                            }
                        });

                        if (result.success) {
                            let report = result.data;
                            initializeReportSetTag(report);
                        } else {
                            alert("데이터 요청을 실패 하였습니다. ");
                            return;
                        }

                        initializeReportSetTagTitle(carNum);

                        $('#reportMain').hide();
                        $('#reportSet').show();
                    });

                    // 신고 접수 등록 이벤트
                    $('#register').on('click', function () {

                        let data = $.getData('data');
                        if (data.setResultType === 'WAIT') {
                            alert("결과를 선택 하세요.");
                            return;
                        }

                        data.reportSeq = reportSeq;
                        data.userSeq = _userSeq;

                        if (confirm("등록 하시겠습니까?")) {
                            $.JJAjaxSync({
                                url: _contextPath + '/set',
                                data: data,
                                success: function (data) {
                                    if (data.success) {
                                        alert("등록 되었습니다.");
                                        search();
                                    } else {
                                        alert("등록 실패 하였습니다.");
                                    }
                                },
                                err: function (code) {
                                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                                }
                            });
                        }
                    });

                    // 신고 접수 등록 설정 닫기 이벤트
                    $('#close').on('click', function () {
                        $('#reportMain').show();
                        $('#reportSet').hide();
                    });

                    // 검색 방식 선택
                    searchSelect();

                    // 신고 등록 화면 감추기
                    $('#reportSet').hide();
                }

                // 초기화
                initialize();

            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>