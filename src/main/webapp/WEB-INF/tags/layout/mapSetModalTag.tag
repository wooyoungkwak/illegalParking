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
<!-- content -->
<div class="offcanvas offcanvas-end" id="${id}" tabindex="-1" aria-labelledby="offcanvasRightLabel" data-bs-backdrop="false">
	<form id="formAreaSetting" name="formAreaSetting">
		<input type="hidden" id="zoneSeq" name="zoneSeq" value=""/>
		<div class="offcanvas-header">
			<h5 class="offcanvas-title" id="offcanvasRightLabel">구역설정</h5>
			<button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
		</div>
		<div class="offcanvas-body">
			<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="illegalTypeSeq" id="zone1" value="0">
				<label class="form-check-label" for="zone1">불가</label>
			</div>
			<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="illegalTypeSeq" id="zone2" value="1">
				<label class="form-check-label" for="zone2">5분간 가능</label>
			</div>
			<div class="row" id="timeRow">
				<div class="col">
					<label for="startTime">시작</label>
					<select class="form-select" id="startTime" name="startTime" aria-label="Default select example" disabled>
						<c:forEach begin="1" end="24" varStatus="status">
							<c:choose>
								<c:when test="${status.index < 10}">
									<option value="0${status.index}:00">0${status.index}:00</option>
								</c:when>
								<c:otherwise>
									<option value="${status.index}:00">${status.index}:00</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
				<div class="col">
					<label for="endTime">종료</label>
					<select class="form-select" id="endTime" name="endTime" aria-label="Default select example" disabled>
						<c:forEach begin="1" end="24" varStatus="status">
							<c:choose>
								<c:when test="${status.index < 10}">
									<option value="0${status.index}:00">0${status.index}:00</option>
								</c:when>
								<c:otherwise>
									<option value="${status.index}:00">${status.index}:00</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="offcanvas-footer">
			<%--<button type="button" class="btn btn-danger" id="btnRemove">삭제</button>--%>
			<button type="button" class="btn btn-primary" id="btnModify">확인</button>
			<button type="button" class="btn btn-secondary" data-bs-dismiss="offcanvas">취소</button>
		</div>
	</form>
</div>





<%--
<div class="modal fade" id="${id}" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true" data-backdrop="false">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form id="formAreaSetting" name="formAreaSetting">
				<input type="hidden" id="zoneSeq" name="zoneSeq" value=""/>
				<div class="modal-header">
					<h5 class="modal-title" id="staticBackdropLabel">구역설정</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" name="illegalTypeSeq" id="zone1" value="0">
						<label class="form-check-label" for="zone1">불가</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" name="illegalTypeSeq" id="zone3" value="1">
						<label class="form-check-label" for="zone3">5분간 가능</label>
					</div>
					<div class="row" id="timeRow">
						<div class="col">
							<label for="startTime">시작</label>
							<select class="form-select" id="startTime" name="startTime" aria-label="Default select example" disabled>
								<c:forEach begin="1" end="24" varStatus="status">
									<c:choose>
										<c:when test="${status.index < 10}">
											<option value="0${status.index}:00">0${status.index}:00</option>
										</c:when>
										<c:otherwise>
											<option value="${status.index}:00">${status.index}:00</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
						<div class="col">
							<label for="endTime">종료</label>
							<select class="form-select" id="endTime" name="endTime" aria-label="Default select example" disabled>
								<c:forEach begin="1" end="24" varStatus="status">
									<c:choose>
										<c:when test="${status.index < 10}">
											<option value="0${status.index}:00">0${status.index}:00</option>
										</c:when>
										<c:otherwise>
											<option value="${status.index}:00">${status.index}:00</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger" id="btnRemove">삭제</button>
					<button type="button" class="btn btn-primary" id="btnModify">확인</button>
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
				</div>
			</form>
		</div>
	</div>
</div>--%>
