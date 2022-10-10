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
<%@ attribute name="current" type="java.lang.String" required="false"%>

<!-- content -->
<div class="modal" id="${id}">
	<div class="modal_body">
		<div class="row mb-5">
			<div class="col-11 fw-bold"><h3>관공서추가</h3></div>
			<div class="col-1">
				<a class="btn btn-close" aria-label="Close" id="modalClose"></a>
			</div>
		</div>
		<div class="row mb-3">
			<div class="col-4">관공서명</div>
			<div class="col-8">
				<input type="text" class="form-control" id="name">
			</div>
		</div>
		<div class="row mb-3">
			<div class="col-4">지역</div>
			<div class="col-8">
				<select class="form-select" id="locationType">
					<c:forEach items="${enumValues}" var="enumValue">
						<option value="${enumValue}" <c:if test="${enumValue eq current}">selected</c:if>>${enumValue.value}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row mb-3">
			<div class="col-4">아이디</div>
			<div class="col-8">
				<input type="text" class="form-control" id="username">
			</div>
		</div>
		<div class="row mb-3">
			<div class="col-4">패스워드</div>
			<div class="col-8">
				<input type="text" class="form-control" id="password">
			</div>
		</div>
		<div class="row mb-3">
			<div class="input-group">
				<a class="btn-primary btn">생성</a>
			</div>
		</div>
	</div>
</div>
