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
    <div class="groupAddModal_body">
        <div class="row mb-5">
            <div class="col-11 fw-bold"><h3>그룹추가</h3></div>
            <div class="col-1">
                <a class="btn btn-close text-reset" aria-label="Close" id="closeGroupAdd"></a>
            </div>
        </div>

        <form id="groupAddForm">
            <div class="row mb-3">
                <div class="col-4">그룹위치</div>
                <div class="col-8">
                    <select class="form-select" id="locationType" name="locationType">
                        <c:forEach items="${enumValues}" var="enumValue">
                            <option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col-4">그룹명</div>
                <div class="col-8">
                    <input type="text" class="form-control" id="name" name="name">
                </div>
            </div>
        </form>

        <div class="row mb-3">
            <div class="input-group">
                <a class="btn-primary btn" id="createGroupAdd">생성</a>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function (){

        // 그룹 추가 팝업창 초기화
        function groupAddInit() {
            $('#locationType').val("");
            $('#name').val("");
        }

        // 그룹 추가 팝업창 닫기
        $('#closeGroupAdd').on('click', function () {

            groupAddInit();

            $('#modalGroupAdd').hide();
            $('body').css({
                'overflow': 'auto'
            });
        });
    });
</script>