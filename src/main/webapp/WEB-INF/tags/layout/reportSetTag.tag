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

<!-- content -->
<main id="reportSet">
    <div class="container-fluid px-4">
        <h1 class="mt-4">신고목록</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item active">${subTitle} > 신고목록</li>
        </ol>
        <div class="card mb-2 shadow-sm rounded">
            <div class="card-header">
                <div class="row">
                    <div class="col-9">
                        <i class="fas fa-table"></i>
                        차량 번호 :
                        <span id="carNum" class="title"></span>
                    </div>
                    <div class="col-3 d-flex justify-content-end">
                        <a class="ms-5 btn btn-close" id="close"></a>
                    </div>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-12 d-flex justify-content-between">
                        <div><span class="ms-3" id="reportStateType"></span></div>
                    </div>
                    <div class="col-8 ms-3 mb-3">
                        <div class="col "><label>신고회수 : </label><span class="ms-2" id="overlapCount"></span></div>
                        <div class="col "><label>위치 : </label><span class="ms-2" id="addr"></span></div>
                        <div class="col "><label>신고자 :</label><span class="ms-2" id="name"></span></div>
                        <div class="col "><label>접수시간 : </label><span class="ms-2" id="regDt"></span></div>
                        <div class="col "><label>신고기관 : </label><span class="ms-2" id="GovernmentOfficeName"></span></div>
                    </div>

                    <div class="row ms-3 mb-1">
                        <div class="col-8">
                            <span class="me-lg-2 fw-bold">신고접수 처리내용 작성</span>
                            <a class="btn btn-outline-dark">내용임시저장</a>
                        </div>
                        <div class="col-4">
                            <span class="ms-2 fw-bold">어떻게 처리 할가요?</span>
                        </div>
                    </div>
                    <div class="row ms-3 mb-3">
                        <div class="col-8">
                            <textarea id="note" placeholder="내용을 입력하세요." style="width: 100%; height: 110px; resize: none;"></textarea>
                        </div>
                        <div class="col-2 d-flex justify-content-center">
                            <div class="col btn-group p-2">
                                <button class="btn btn-outline-dark" id="btnException">신고제외</button>
                            </div>
                        </div>
                        <div class="col-2 d-flex justify-content-center">
                            <div class="col btn-group p-2 ps-0">
                                <button class="btn btn-outline-danger" id="btnPenalty">과태료대상</button>
                            </div>
                        </div>
                    </div>

                    <form id="data">
                        <div class="row mb-2 ms-2">
                            <div class="col-6">
                                <div class="card">
                                    <div class="card" style="width: 100%;">
                                        <img id="firstFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
                                        <div class="card-body">
                                            <h5 class="card-title mb-4 fst-italic fw-bold text-danger">1차 신고</h5>
                                            <p class="card-text">신고 위치 : <span id="firstAddr" class="title"></span></p>
                                            <p class="card-text">신고 시간 : <span id="firstRegDt" class="title"></span></p>
                                            <p class="card-text">위치 분석 : <span id="firstIllegalType" class="title"></span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6 m-0">
                                <div class="card ">
                                    <div class="card" style="width: 100%;">
                                        <img id="secondFileName" src="" class="card-img-top" alt="..." style="width: 100%; height: 400px;">
                                        <div class="card-body">
                                            <h5 class="card-title mb-4  fst-italic fw-bold text-danger">2차 신고</h5>
                                            <p class="card-text">신고 위치 : <span id="secondAddr" class="title"></span></p>
                                            <p class="card-text">신고 시간 : <span id="secondRegDt" class="title"></span></p>
                                            <p class="card-text">위치 분석 : <span id="secondIllegalType" class="title"></span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%--						<div class="row ms-2 ">--%>
                        <%--							<div class="col-2 btn-group">--%>
                        <%--								<a type="submit" class="btn btn-primary" id="register">등록</a>--%>
                        <%--								<a class="btn btn-outline-secondary" id="close">닫기</a>--%>
                        <%--							</div>--%>
                        <%--						</div>--%>
                    </form>

                </div>
            </div>

            <div class="card-footer" id="comments"></div>
        </div>
    </div>
</main>