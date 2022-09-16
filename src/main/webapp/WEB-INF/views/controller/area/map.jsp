<%--
  Created by IntelliJ IDEA.
  User: zilet
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/navMapHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<stripes:layout-component name="side">
		<jsp:include page="side.jsp" flush="true"/>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<h1 class="mt-4">지도 보기</h1>
				<ol class="breadcrumb mb-4">
					<li class="breadcrumb-item active"> ${subTitle} > 지도 보기</li>
				</ol>

				<div class="row">

					<div class="map_wrap">
						<div id="drawingMap"></div>
						<p class="modes">
							<button onclick="selectOverlay('POLYGON')">구역추가</button>
							<button onclick="getDataFromDrawingMap()">저장</button>
							<span>
								<label><input type="radio" name="searchTypeSeq" value="" checked>전체</label>
								<label><input type="radio" name="searchTypeSeq" value="1">주정차 불가</label>
								<label><input type="radio" name="searchTypeSeq" value="2">탄력적 가능</label>
								<label><input type="radio" name="searchTypeSeq" value="3">5분간 가능</label>
							</span>
						</p>
					</div>

					<p id="result"></p>

					<div class="modal fade" id="areaSettingModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
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
											<input class="form-check-input" type="radio" name="typeSeq" id="zone1" value="1">
											<label class="form-check-label" for="zone1">불가</label>
										</div>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="typeSeq" id="zone2" value="2">
											<label class="form-check-label" for="zone2">탄력적 가능</label>
										</div>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="typeSeq" id="zone3" value="3">
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
										<button type="button" class="btn btn-danger" id="btnDelete">삭제</button>
										<button type="button" class="btn btn-primary" id="btnUpdate">확인</button>
										<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
		</main>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=55cc3405408d4cfbef37c3a93a5422c4&libraries=services,clusterer,drawing"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/mapCommon-scripts.js"></script>
	</stripes:layout-component>

</stripes:layout-render>