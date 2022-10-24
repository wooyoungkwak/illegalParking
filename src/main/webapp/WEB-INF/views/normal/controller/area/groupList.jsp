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
        <script type="application/javascript">
            $(function (){

                // 검색 함수
                function search(pageNumber) {
                    if (pageNumber === undefined) {
                        $('#pageNumber').val("1");
                    } else {
                        $('#pageNumber').val(pageNumber);
                    }
                    location.href = _contextPath + "/groupList?" + $('form').serialize();
                }

                // 검색 입력 방식 선택
                function searchSelect(filterColumn) {
                    if (filterColumn === 'LOCATION') {
                        $('#searchStrGroup').hide();
                        $('#searchStr2Group').show();
                    } else {
                        $('#searchStrGroup').show();
                        $('#searchStr2Group').hide();
                    }
                }

                // groupSetTag 설정
                function initializeGroupSetTag(opt) {
                    $('#groupSeq').val(opt.groupSeq);
                    $('#locationType').val(opt.locationType);
                    $('#name').val(opt.name);

                    let createTr = function (data) {
                        let html = `<tr>`;
                        html += `<td>` + data.pointType + `</td>`;
                        html += `<td>` + data.limitValue + `</td>`;
                        html += `<td>` + data.value + `</td>`;
                        html += `<td>` + data.useValue + `</td>`;
                        html += `<td>` + data.residualValue + `</td>`;
                        html += `<td>` + data.startDate + `</td>`;
                        html += `<td>` + data.stopDate + `</td>`;
                        html += `<td>` + data.finish + `</td>`;
                        html += `</tr>`;
                        return html;
                    }

                    let trs = '';

                    $.each(opt.point, function (index, data) {
                        trs += createTr(data);
                    });

                    $('#pointTable tbody').find('tr').remove();
                    $('#pointTable tbody').append(trs);
                }

                // groupEventTag 설정
                function initializeGroupEventTag(point){
                    let createTr = function (data) {
                        let html = `<tr>`;
                        html += `<td><input type="hidden" value="` + data.pointSeq + `">` + data.pointType + `</td>`;
                        html += `<td>` + data.limitValue + `</td>`;
                        html += `<td>` + data.value + `</td>`;
                        html += `<td>` + data.useValue + `</td>`;
                        html += `<td>` + data.residualValue + `</td>`;
                        html += `<td>` + data.startDate + `</td>`;
                        html += `<td>` + data.stopDate + `</td>`;
                        html += `<td>` + data.finish + `</td>`;
                        html += `</tr>`;
                        return html;
                    }
                    let tr = createTr(point);
                    $('#pointTable tbody').append(tr);
                }

                // 초기화 설정
                function initialize() {



                    // 검색 이벤트 1
                    $('#searchStr').next().on('click', function (event) {
                        search();
                    });

                    // 검색 이벤트 2
                    $('#searchStr2').next().on('click', function (event) {
                        search();
                    });

                    // 페이지 번호 이벤트
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
                        $('#pageSize').val($(this).val());
                        search();
                    });

                    // 불법 주정차 구역의 그룹 테이블 항목 이벤트
                    $('#userTable tbody tr').on('click', function () {

                        let groupSeqStr = $(this).children("td:eq(0)").find('input').val();

                        let locationType = $(this).children("td:eq(0)").text().trim();
                        let name = $(this).children("td:eq(1)").text();

                        let groupSeq = Number.parseInt(groupSeqStr);

                        let result = $.JJAjaxAsync({
                            url: _contextPath + '/point/get',
                            data: {
                                groupSeq: groupSeq
                            }
                        });

                        if (result.success) {
                            let point = result.data;
                            initializeGroupSetTag({
                                groupSeq: groupSeq,
                                point: point,
                                locationType: locationType,
                                name: name
                            });
                        } else {
                            alert(result.msg);
                            return;
                        }

                        $('#groupMain').hide();
                        $('#groupSet').show();
                    });

                    // 필터 변경 이벤트
                    $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
                        searchSelect($(this).val());
                    });

                    // 불법 주정차 구역의 그룹 추가 이벤트
                    $('#createGroupAdd').on('click', function () {
                        let data = $.getData("groupAddForm");

                        if (data.name === '') {
                            alert("그룹명을 입력하세요.");
                            return;
                        }

                        if (confirm("등록 하시겠습니까?")) {
                            let result = $.JJAjaxAsync({
                                url: _contextPath + '/group/set',
                                data: data
                            });

                            if (result.success) {
                                search();
                                $(this).hide();
                            }
                        }
                    });

                    $('#createGroupEvent').on('click', function () {
                        let data = $.getData("groupEventForm");

                        data.groupSeq = $('#groupSeq').val();

                        if (data.isPointLimit == 'on') {
                            data.isPointLimit = true;
                        } else {
                            data.isPointLimit = false;
                        }

                        if (data.isTimeLimit == 'on') {
                            data.isTimeLimit = true;
                        } else {
                            data.isTimeLimit = false;
                        }

                        if (confirm("등록 하시겠습니까?")) {

                            let result = $.JJAjaxAsync({
                                url: _contextPath + '/point/set',
                                data: data
                            });

                            if (result.success) {
                                let point = result.data;
                                initializeGroupEventTag(point);
                                $.closeGroupEvent();
                            } else {
                                alert("등록 실패 하였습니다. " + result.msg);
                            }

                        }
                    });

                    // 그룹 추가 팝업창 열기
                    $('#openGroupAdd').on('click', function () {
                        $('#modalGroupAdd').show();
                        $('body').css({
                            'overflow': 'hidden'
                        });
                    });

                    // 이벤트 추가 팝업창 열기
                    $('#openEventAdd').on('click', function () {
                        let now = new Date();
                        $('#startDate').val($.formatDateYYYYMMDD(now));
                        $('#stopDate').val($.formatDateYYYYMMDD(now));

                        $('#modalGroupEvent').show();
                        $('body').css({
                            'overflow': 'hidden'
                        });
                    });

                    // 그룹 상세 보기 닫기
                    $('#closeGroupSet').on('click', function () {
                        $('#groupMain').show();
                        $('#groupSet').hide();
                    });

                    // 필터에 의한 검색 입력 방식 선택
                    searchSelect('${filterColumn}');

                    // 그룹 설정 / .. / .. / ..  숨김
                    $('#groupSet').hide();
                    $('#modalGroupAdd').hide();
                    $('#modalGroupEvent').hide();
                    $('#searchStr2Group').hide();
                }

                initialize();
            });
        </script>
    </stripes:layout-component>

</stripes:layout-render>