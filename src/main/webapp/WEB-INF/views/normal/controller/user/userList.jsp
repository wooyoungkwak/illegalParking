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
<%@ page import="com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn" %>
<%@ page import="com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType" %>
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
        <main id="userMain">
            <div class="container-fluid px-4">
                <h1 class="mt-4">사용자</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item active">${subTitle} > 관공서</li>
                </ol>
                <div class="card mb-4 shadow-sm rounded">
                    <div class="card-header">
                        <i class="fas fa-table me-1"></i>
                        관공서 정보
                    </div>
                    <div class="card-body">
                        <form class="row mb-3 g-3">
                            <input type="hidden" id="pageNumber" name="pageNumber" value="${pageNumber}"/>
                            <input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
                            <div class="col-2">
                                <a class="btn btn-primary" id="addGovernmentOffice"><i class="fa fa-city"></i> 관공서추가</a>
                            </div>
                            <div class="col-5"></div>
                            <div class="col-1">
                                <tags:filterTag id="filterColumn" enumValues="${UserGovernmentFilterColumn.values()}" column="${filterColumn}"/>
                            </div>
                            <div class="col-4">
                                <tags:searchTag id="searchStr" searchStr="${searchStr}"/>
                                <tags:searchTagWithSelect id="searchStr2" searchStr="${searchStr2}" items="${LocationType.values()}"/>
                            </div>

                        </form>
                        <table class="table table-hover table-bordered" id="userTable">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">지역</th>
                                    <th scope="col">관공서명</th>
                                    <th scope="col">아이디</th>
                                    <th scope="col">관리그룹</th>
                                    <th scope="col">신고접수건</th>
                                    <th scope="col">대기</th>
                                    <th scope="col">미처리</th>
                                    <th scope="col">처리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${userGovernmentDtos}" var="userGovernmentDto" varStatus="status">
                                    <tr>
                                        <td>${userGovernmentDto.userSeq}</td>
                                        <td>${userGovernmentDto.locationType}</td>
                                        <td>${userGovernmentDto.officeName}</td>
                                        <td>${userGovernmentDto.userName}</td>
                                        <td>${userGovernmentDto.groupCount}</td>
                                        <td>${userGovernmentDto.totalCount}</td>
                                        <td>${userGovernmentDto.completeCount}</td>
                                        <td>${userGovernmentDto.exceptionCount}</td>
                                        <td>${userGovernmentDto.penaltyCount}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <tags:pageTag pageNumber="${pageNumber}" isBeginOver="${isBeginOver}" isEndOver="${isEndOver}" totalPages="${totalPages}" items="15,25,50" pageSize="${pageSize}"/>
                    </div>
                </div>
            </div>
        </main>

        <layoutTags:userSetTag/>

        <layoutTags:userAddTag id="modal" enumValues="${LocationType.values()}"/>

    </stripes:layout-component>

    <!-- footer -->
    <stripes:layout-component name="footer">
        <stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript">
        <script src="<%=contextPath%>/resources/js/user/userList-scripts.js"></script>
        <script type="application/javascript">

            $(function () {

                // 검색
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/receiptList?" + $('form').serialize();
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

                function initializeUserSetTag(report) {
                    $.each(report, function (key, value) {
                        if (key.indexOf('receiptStateType') > -1) {
                            if (value === 'OCCUR') {
                                $('#' + key).text("신고발생");
                            } else if (value === 'FORGET') {
                                $('#' + key).text("신고누락");
                            } else if (value === 'EXCEPTION') {
                                $('#' + key).text("신고제외");
                            }
                        } else {
                            $('#' + key).text(value);
                        }
                    });
                }

                function initialize() {

                    $('#orderBy a').on('click', function () {
                        search();
                    });

                    $('#searchStr').next().on('click', function (event) {
                        search();
                    });

                    $('#searchStr2').next().on('click', function (event) {
                        search();
                    });

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

                    $('#pageSize').on("change", function () {
                        $('#pageNumber').val(1);
                        search();
                    });

                    // 신고 등록 표시
                    $('#userTable tbody tr').on('click', function () {

                        // let reportSeqStr = $(this).children("td:eq(0)").find('input').val();
                        // let carNum = $(this).children("td:eq(1)").text();
                        // let receiptSeq = Number.parseInt(reportSeqStr);
                        //
                        // let result = $.JJAjaxAsync({
                        //     url: _contextPath + '/receipt/get',
                        //     data: {
                        //         receiptSeq: receiptSeq
                        //     }
                        // });
                        //
                        // if (result.success) {
                        //     let receipt = result.data;
                        //     initializeReceiptSetTag(receipt);
                        // } else {
                        //     alert("데이터 요청을 실패 하였습니다. ");
                        //     return;
                        // }

                        // initializeReportSetTagTitle(carNum);

                        $('#userMain').hide();
                        $('#userSet').show();
                    });

                    $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
                        searchSelect($(this).val());
                    });

                    $('#userModify').on('click', function () {

                    });

                    $('#userClose').on('click', function () {
                        $('#userMain').show();
                        $('#userSet').hide();
                    });

                    $('#modalClose').on('click', function () {
                        $('#modal').hide();
                        $('body').css({
                            'overflow': 'auto'
                        });
                    });

                    $('#modal').hide();
                    $('#userSet').hide();

                    $('#addGovernmentOffice').on('click', function () {
                        $('#modal').show();
                        $('body').css({
                            'overflow': 'hidden'
                        });
                    });
                }

                $('#searchStr2').hide();
                searchSelect('${filterColumn}');

                initialize();
            });
        </script>
    </stripes:layout-component>

</stripes:layout-render>