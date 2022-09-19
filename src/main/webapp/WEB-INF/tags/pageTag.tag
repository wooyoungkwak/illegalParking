<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageNumber" type="java.lang.Integer" required="true" %>
<%@ attribute name="begin" type="java.lang.Integer" required="true" %>
<%@ attribute name="end" type="java.lang.Integer" required="false" %>
<%@ attribute name="isBeginOver" type="java.lang.Boolean" required="true" %>
<%@ attribute name="isEndOver" type="java.lang.Boolean" required="true" %>

<!-- Pagination-->
<nav aria-label="Pagination">
	<hr class="my-0"/>
	<ul class="pagination justify-content-center my-4" id="pagination">
		<li class="page-item"><a class="page-link"><</a></li>
		<c:if test="${isBeginOver}">
			<li class="page-item ms-2 me-2">...</li>
		</c:if>

		<c:forEach begin="1" end="3" varStatus="status">
			<li class="page-item <c:if test="${pageNumber == ( begin + ( status.index - 1 )) }">active</c:if>" aria-current="page"><a class="page-link">${begin + ( status.index - 1 )}</a></li>
		</c:forEach>

		<c:if test="${isEndOver}">
			<li class="page-item ms-2 me-2">...</li>
		</c:if>
		<li class="page-item"><a class="page-link">></a></li>
	</ul>
</nav>