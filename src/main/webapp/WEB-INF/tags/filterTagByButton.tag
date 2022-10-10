<%@ tag import="org.springframework.data.domain.Sort" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<div class="input-group" id="${id}">
    <input type="hidden" name="${id}" value="${current}">
    <c:forEach var="enumValue" items="${enumValues}" varStatus="status">
        <c:choose>
            <c:when test="${status.count == 1}">
                <a class="btn btn-<c:if test="${!current.equals(enumValue)}">outline-</c:if>success" id="${enumValue}">${enumValue.value}</a>
            </c:when>
            <c:when test="${status.count == 2}">
                <a class="btn btn-<c:if test="${!current.equals(enumValue)}">outline-</c:if>danger" id="${enumValue}">${enumValue.value}</a>
            </c:when>
            <c:otherwise>
                <a class="btn btn-<c:if test="${!current.equals(enumValue)}">outline-</c:if>dark" id="${enumValue}">${enumValue.value}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>

<script type="application/javascript">
    $(function () {
        $('#${id}').find('a').on('click', function () {

            $('#${id}').find('a').each(function () {
                let className = $(this).attr('class');
                if ( className.indexOf("btn-outline-") == -1 ) {
                    $(this).removeClass(className);
                    className = className.replace('btn-','btn-outline-');
                    $(this).addClass(className);
                }
            });

            // 클래스 가져오기
            let className = $(this).attr('class');
            $(this).removeClass(className);
            className = className.replace('btn-outline-', 'btn-' );
            $(this).addClass(className);

            // input 값 넣기
            $('#${id}').find('input').val($(this).attr('id'));
        });
    });
</script>