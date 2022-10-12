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
<%@ attribute name="typeValues" type="java.lang.Object[]" required="true" %>

<!-- content -->
<div class="offcanvas offcanvas-end" id="${id}" tabindex="-1" aria-labelledby="offcanvasRightLabel" data-bs-backdrop="false">
	<form id="formAreaSetting" name="formAreaSetting">
		<input type="hidden" id="zoneSeq" name="zoneSeq" value=""/>
		<input type="hidden" id="eventSeq" name="eventSeq" value=""/>

		<div class="offcanvas-header">
			<h5 class="offcanvas-title" id="offcanvasRightLabel">구역설정</h5>
			<button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
		</div>

		<div class="offcanvas-body">

			<div class="card mb-2">
				<div class="card-header">
					불법주정차 구역 타입
				</div>
				<div class="card-body">
					<c:forEach items="${typeValues}" var="typeValue" varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="illegalType" id="zone${status.index}" value="${enumValue}">
							<label class="form-check-label" for="zone${status.index}">${typeValue.value}</label>
						</div>
					</c:forEach>
				</div>
			</div>

			<div class="card mb-2">
				<div class="card-header">
					불법주정차 그룹지정
				</div>
				<div class="card-body">

				</div>
			</div>

			<div class="card mb-2">
				<div class="card-header">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="" id="usedFirst" name="usedFirst">
						<label class="form-check-label" for="usedFirst">
							첫번째 적용시간
						</label>
					</div>
				</div>
				<div class="card-body">
					<div class="row mt-2" id="firstTimeRow">
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="firstStartTime" title="시작" />
						</div>
						<div class="col">~</div>
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="firstEndTime" title="종료"/>
						</div>
					</div>
				</div>
			</div>

			<div class="card">
				<div class="card-header">
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="" id="usedSecond" name="usedSecond">
						<label class="form-check-label" for="usedSecond">
							두번째 적용시간
						</label>
					</div>
				</div>
				<div class="card-body">
					<div class="row mt-2" id="secondTimeRow">
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="secondStartTime" title="시작" />
						</div>
						<div class="col">~</div>
						<div class="col-5">
							<tags:selectTagWithSeperateTime id="secondEndTime" title="종료"/>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="offcanvas-footer">
			<div class="row ms-2">
				<div class="input-group">
					<button type="button" class="btn btn-primary" id="btnModify">등록</button>
					<button type="button" class="btn btn-outline-secondary" data-bs-dismiss="offcanvas">취소</button>
				</div>
			</div>
		</div>
	</form>
</div>
