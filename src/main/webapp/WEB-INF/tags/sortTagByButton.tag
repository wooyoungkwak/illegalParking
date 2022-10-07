<%@ tag import="org.springframework.data.domain.Sort" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="sortFirst" type="java.lang.String" required="true" %>
<%@ attribute name="sortSecond" type="java.lang.String" required="true" %>
<%@ attribute name="sortThird" type="java.lang.String" required="true" %>
<div class="input-group">
	<span class="input-group-text">정렬</span>
	<a class="btn btn-outline-success" id="sortFirst">${sortFirst}</a>
	<a class="btn btn-outline-danger" id="sortSecond">${sortSecond}</a>
	<a class="btn btn-outline-dark" id="sortThird">${sortThird}</a>
</div>