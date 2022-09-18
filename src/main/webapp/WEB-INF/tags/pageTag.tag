<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageNumber" type="java.lang.Integer" required="true"%>
<%@ attribute name="begin" type="java.lang.Integer" required="true"%>
<%@ attribute name="end" type="java.lang.Integer" required="true"%>
<%@ attribute name="isBeginOver" type="java.lang.Boolean" required="true"%>
<%@ attribute name="isEndOver" type="java.lang.Boolean" required="true"%>

<!-- Pagination-->
<nav aria-label="Pagination">
    <hr class="my-0"/>
    <ul class="pagination justify-content-center my-4" id="pagination">

        <li class="page-item <c:if test="${!isBeginOver}">disabled</c:if>"><a class="page-link" href="#" tabindex="-1" aria-disabled="true"><</a></li>

        <c:forEach begin="1" end="${end}" varStatus="status">
            <li class="page-item <c:if test="${pageNumber == ( begin + ( status.index - 1 )) }">active</c:if>" aria-current="page"><a class="page-link">${begin + ( status.index - 1 )}</a></li>
        </c:forEach>

        <c:choose>
            <c:when test="${isEndOver}">
                <li class="page-item ms-2 me-2">...</li>
                <li class="page-item"><a class="page-link" href="#!">></a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item disabled"><a class="page-link" href="#!">></a></li>
            </c:otherwise>
        </c:choose>

    </ul>
</nav>