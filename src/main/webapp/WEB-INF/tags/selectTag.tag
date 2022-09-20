<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="current" type="java.lang.String" required="true" %>
<%@ attribute name="option" type="java.lang.String" required="false" %>
<%@ attribute name="items" type="java.lang.Object" required="true" %>

<%--<label for="${id}" class="form-label">${title}</label>--%>
<select id="${id}" class="form-select" name="${id}" ${option}>
    <c:forEach var="item" items="${items}" varStatus="status">
        <c:set var="format" value="${item}"></c:set>
        <option value="${format}" <c:if test="${current eq format}">selected</c:if> >${format}</option>
    </c:forEach>
</select>