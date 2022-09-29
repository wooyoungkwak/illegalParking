<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="begin" type="java.lang.Integer" required="true" %>
<%@ attribute name="end" type="java.lang.Integer" required="true" %>
<%@ attribute name="timeFormat" type="java.lang.Integer" required="true" %>
<%@ attribute name="current" type="java.lang.String" required="false" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>

<label for="${id}" class="form-label">${title}</label>
<select id="${id}" class="form-select" name="${id}" ${option}>
    <c:choose>
        <c:when test="${timeFormat == 0}">
            <c:forEach begin="${begin}" end="${end }" varStatus="status">
                <c:set var="format" value="${status.index}:00"></c:set>
                <option value="${status.index}"
                        <c:if test="${current eq format}">selected</c:if> >${format}</option>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach begin="${begin}" end="${end * ( 60 / timeFormat ) }" varStatus="status">
                <c:set var="format" value="${status.index}:${timeFormat}"></c:set>
                <option value="${status.index}"
                        <c:if test="${current eq format}">selected</c:if> >${format}</option>
            </c:forEach>
        </c:otherwise>

    </c:choose>

</select>