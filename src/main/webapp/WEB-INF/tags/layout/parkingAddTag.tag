<%--
  Created by IntelliJ IDEA.
  User: zilet
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<% String contextPath = request.getContextPath(); %>
<%@ attribute name="parkingSeq" type="java.lang.Integer" required="true" %>
<!-- content -->
<main id="parkingDetail">
	<div class="container-fluid px-4">
		<h1 class="mt-4">주차장등록</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 주차장등록</li>
		</ol>
		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header"><i class="fas fa-pen me-1"></i> 직접 입력</div>
			<div class="card-body">
				<div class="row">
					<form class="row">
						<div class="row g-2">
							<div class="col-md-2">
								<label for="prkplceNm" class="form-label">* 주차장명</label>
								<input type="email" class="form-control" id="prkplceNm">
							</div>
							<div class="col-md-1">
								<label for="prkplceSe" class="form-label">* 주차장구분</label>
								<select id="prkplceSe" class="form-select">
									<option value="1">공영</option>
									<option value="2">민영</option>
								</select>
							</div>
							<div class="col-md-1">
								<label for="prkplceType" class="form-label">* 주차장유형</label>
								<input type="text" class="form-control" id="prkplceType" placeholder="예> 노외">
							</div>
							<div class="col-md-2">
								<label for="enforceSe" class="form-label">* 부제시행구분</label>
								<input type="text" class="form-control" id="enforceSe" placeholder="예> 미시행">
							</div>
							<div class="col-md-2">
								<label for="inputManager" class="form-label">* 관리기관명</label>
								<input type="text" class="form-control" id="inputManager" placeholder="예>전라남도 광양시청">
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-2">
								<label for="inputState" class="form-label">운영요일</label>
								<select id="inputState" class="form-select">
									<option selected>평일+토요일+일요일</option>
									<option>평일+토요일</option>
									<option>평일</option>
								</select>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="startTime" title="평일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="endTime" title="평일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="satStartTime" title="토요일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="satEndTime" title="토요일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="sunStartTime" title="일요일시작시간" begin="0" end="23" timeFormat="00" current="0:00"/>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="sunEndTime" title="일요일종료시간" begin="0" end="23" timeFormat="59" current="23:59"/>
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-1">
								<label for="parkingchrgeInfo" class="form-label">* 요금</label>
								<select id="parkingchrgeInfo" class="form-select">
									<option selected>무료</option>
									<option>유료</option>
								</select>
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="basicTime" title="주차기본시간" begin="0" end="5" timeFormat="00" current="1:00"/>
							</div>
							<div class="col-md-2">
								<label for="BasicCharge" class="form-label">주차기본요금</label>
								<input type="text" class="form-control" id="BasicCharge" placeholder="예> 1000원">
							</div>
							<div class="col-md-1">
								<tags:timeSelectTag id="addUnitTime" title="추가단위시간" begin="0" end="5" timeFormat="00" current="1:00"/>
							</div>
							<div class="col-md-2">
								<label for="addUnitCharge" class="form-label">추가단위요금</label>
								<input type="text" class="form-control" id="addUnitCharge" placeholder="예> 1000원">
							</div>
							<div class="col-md-2">
								<label for="dayCmmtkt" class="form-label">1일주차권요금</label>
								<input type="text" class="form-control" id="dayCmmtkt" placeholder="예> 1000원">
							</div>
							<div class="col-md-2">
								<label for="monthCmmtkt" class="form-label">월정기권요금</label>
								<input type="text" class="form-control" id="monthCmmtkt" placeholder="예> 1000원">
							</div>
						</div>

						<div class="row g-2">
							<div class="col-md-2">
								<label for="inputPhone" class="form-label">전화번호</label>
								<input type="text" class="form-control" id="inputPhone" placeholder="XXX-XXX-XXXX">
							</div>
						</div>

						<div class="row g-2">
							<div class="col-12">
								<label for="inputAddress2" class="form-label">소재지도로명 주소</label>
								<input type="text" class="form-control" id="inputAddress2" placeholder="예> 광주광역시 광산구 쌍암동 694-83번지 1층  ">
							</div>
							<div class="col-md-2">
								<label for="inputLat" class="form-label">위도</label>
								<input type="text" class="form-control" id="inputLat" placeholder="XX.XXXXXXXX" disabled>
							</div>
							<div class="col-md-2">
								<label for="inputLot" class="form-label">경도</label>
								<input type="text" class="form-control" id="inputLot" placeholder="XX.XXXXXXXX" disabled>
							</div>
						</div>
						<div class="row g-3"></div>
						<div class="col-1">
							<a id="close" class="btn btn-primary">닫기</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</main>