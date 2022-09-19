<%@ tag import="org.springframework.data.domain.Sort" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="column" type="java.lang.String" %>
<%@ attribute name="direction" type="java.lang.String" %>
<div id="${id}" class="orderby">
<%--    <div class="col-5"></div>--%>
<%--    <div class="col-5"></div>--%>
<%--    <div class="col-2"></div>--%>
    <div class="input-group">
        <select class="form-select">
            <c:forEach items="${enumValues}" var="enumValue">
                <option value="${enumValue}" <c:if test="${enumValue eq column}">selected</c:if>>${enumValue.value}</option>
            </c:forEach>
        </select>
        <select class="form-select">
            <c:forEach items="<%=Sort.Direction.values()%>" var="orderDirection">
                <option value="${orderDirection}" <c:if test="${orderDirection eq direction}">selected</c:if>>
                    <c:choose>
                        <c:when test="${orderDirection eq 'ASC'}">오름차순</c:when>
                        <c:otherwise>내림차순</c:otherwise>
                    </c:choose>
                </option>
            </c:forEach>
        </select>
        <button class="btn btn-outline-success">정렬</button>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var $orderby = $('#${id}.orderby');

        $orderby.find('select').on('change', function () {
            var event = jQuery.Event('change');

            $orderby.trigger(event, [
                $orderby.find('select:first').val(), $orderby.find('select:last').val()
            ]);

            return false;
        });

        $orderby.find('button').on('click', function () {
            var event = jQuery.Event('order');

            $orderby.trigger(event, [
                $orderby.find('select:first').val(), $orderby.find('select:last').val()
            ]);
        });
    });
</script>
