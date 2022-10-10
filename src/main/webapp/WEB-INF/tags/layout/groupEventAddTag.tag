<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<% String contextPath = request.getContextPath(); %>

<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>

<!-- content -->
<div class="modal" id="${id}">
    <div class="groupEventModal_body">
        <div class="row mb-5">
            <div class="col-11 fw-bold"><h3>이벤트추가</h3></div>
            <div class="col-1">
                <a class="btn btn-close text-reset" aria-label="Close" id="closeGroupEvent"></a>
            </div>
        </div>
        <form id="groupEventForm">
            <div class="row mb-3">
                <div class="col-4"><label class="mt-2">분류</label></div>
                <div class="col-8">
                    <select class="form-select" id="pointType" name="pointType">
                        <c:forEach items="${enumValues}" var="enumValue">
                            <option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col-4"><label class="mt-2">제한포인트</label></div>
                <div class="col-8">
                    <input type="text" class="form-control" id="limitValue" name="limitValue">
                    <input class="form-check-input" type="checkbox" id="isPointLimit" name="isPointLimit">
                    <label class="form-check-label" for="isPointLimit">
                        제한없음
                    </label>
                </div>

            </div>
            <div class="row mb-3">
                <div class="col-4"><label class="mt-2">제공포인트</label></div>
                <div class="col-6">
                    <input type="text" class="form-control" id="value" name="value" >
                </div>
            </div>
            <div class="row mb-1">
                <div class="col-4"><label class="mt-2">시작일자</label></div>
                <div class="col-8">
                    <input type="date" class="form-control" id="startDate" name="startDate">
                </div>
            </div>
            <div class="row mb-3">
                <div class="col-4"><label class="mt-2">종료일자</label></div>
                <div class="col-8">
                    <input type="date" class="form-control" id="stopDate" name="stopDate">
                    <input class="form-check-input" type="checkbox" id="isTimeLimit" name="isTimeLimit">
                    <label class="form-check-label" for="isTimeLimit">
                        제한없음
                    </label>
                </div>
            </div>
        </form>

        <div class="row mb-3">
            <div class="input-group">
                <a class="btn-primary btn" id="createGroupEvent">생성</a>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function (){

        // 그룹 이벤트 팝업창 초기화
        function groupEventAddInit() {
            $('#pointType').val("");
            $('#limit').val("");
            $('#value').val("");
            $('#startDate').val("");
            $('#endDate').val("");
        }

        // 그룹 이벤트 팝업창 닫기
        $('#closeGroupEvent').on('click', function () {
            groupEventAddInit();

            $('#modalGroupEvent').hide();
            $('body').css({
                'overflow': 'auto'
            });
        });
    });
</script>