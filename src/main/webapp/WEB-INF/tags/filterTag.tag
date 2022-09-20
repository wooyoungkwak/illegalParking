<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="enumValues" type="java.lang.Object[]" required="true" %>
<%@ attribute name="column" type="java.lang.String" %>
<%@ attribute name="direction" type="java.lang.String" %>
<div id="${id}" class="filterColumn">
	<div class="input-group">
		<select class="form-select" name="filterColumn">
			<c:forEach items="${enumValues}" var="enumValue">
				<option value="${enumValue}" <c:if test="${enumValue eq column}">selected</c:if>>${enumValue.value}</option>
			</c:forEach>
		</select>
	</div>
</div>