<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<% String contextPath = request.getContextPath(); %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="/WEB-INF/views/layout/navHtmlLayout.jsp">

	<!-- nav -->
	<stripes:layout-component name="nav">
		<stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
	</stripes:layout-component>

	<!-- side -->
	<stripes:layout-component name="side">
		<%--		<jsp:include page="side.jsp" flush="true"/>--%>
	</stripes:layout-component>

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="container-fluid px-4">
				<table style="width: 100%; height: 100%;">
					<colgroup>
						<col width="50%"/>
						<col width="50%"/>
					</colgroup>
					<tbody>
					<tr style="height: 200px;">
						<td><p class="fs-4">안녕하세요.</p></td>
						<td></td>
					</tr>
					<tr>
						<td><p class="fs-1">'${officeName}' 입니다.</p></td>
						<td>
							<p class="fs-4">※ 데이터 Ai 자동 분석을 통해 중복 신고, 변동 단속 시간, 오류 신고 등 제외를 통하여 ${totalCount}의 신고 중 ${sendPenaltyCount}건의 신고를 담당 부서에 전달드렸습니다.</p>
						</td>
					</tr>
					<tr>
						<td class="px-3 align-middle">
							<div class="row">
								<div class="col-7 d-flex justify-content-lg-center">
									<div id="pieChart"></div>
								</div>
								<div class="col-4">
									<table class="table border">
										<tr>
											<td class="d-flex justify-content-lg-end">총신고건수</td>
											<td>${totalCount}</td>
										</tr>
										<tr>
											<td class="d-flex justify-content-lg-end">대기</td>
											<td>${completeCount}</td>
										</tr>
										<tr>
											<td class="d-flex justify-content-lg-end">미처리</td>
											<td>${exceptionCount}</td>
										</tr>
										<tr>
											<td class="d-flex justify-content-lg-end">처리</td>
											<td>${penaltyCount}</td>
										</tr>
									</table>
								</div>
							</div>
						</td>
						<td class="px-3 align-middle">
							<div id="barChart" style="height: 300px; width: 100%;"></div>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
		</main>

	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
		<script src="<%=contextPath%>/resources/js/scripts.js"></script>
		<script type="application/javascript">
            $(function () {

                let completeCount = '${completeCount}';
                let exceptionCount = '${exceptionCount}';
                let penaltyCount = '${penaltyCount}';
                let reportCounts = ${reportCounts};
                let receiptCounts = ${receiptCounts};

                // 신고 접수 건수 파이 차트
                $.drawPieChart = function (opt) {
                    let options = {
                        animationEnabled: true,
                        title: {
                            text: "신고 건수"
                        },
                        data: [{
                            type: "doughnut",
                            innerRadius: "40%",
                            showInLegend: true,
                            legendText: "{label}",
                            indexLabel: "{label}",
                            dataPoints: [
                                {label: "대기", y: Number(opt.completeCount)},
                                {label: "미처리", y: Number(opt.exceptionCount)},
                                {label: "처리", y: Number(opt.penaltyCount)}
                            ]
                        }]
                    };

                    function draw() {
                        $("#pieChart").css("height", "400px").css("width", "100%");
                        $("#pieChart").CanvasJSChart(options);
                        $('.canvasjs-chart-credit').hide();
                    };

                    setTimeout(() => {
                        draw();
                    }, 200);
                }

                // 월별 신고 건수
                $.drawBarChart = function (opt) {

                    let reportsDatas = [];
                    let receiptDatas = [];

                    for (let i = 0; i < opt.receiptCounts.length; i++) {
                        reportsDatas.push({label: (i + 1) + "월", y: opt.reportCounts[i]});
                        receiptDatas.push({label: (i + 1) + "월", y: opt.receiptCounts[i]});
                    }

                    var chart = new CanvasJS.Chart("barChart", {
                        animationEnabled: true,
                        title: {
                            text: "신고 발생 / 신고 접수 "
                        },
                        data: [
                            {
                                // 신고 발생 건수
                                type: "column",
                                dataPoints: receiptDatas
                            },
                            {
                                // 신고 접수 건수
                                type: "column",
                                dataPoints: reportsDatas
                            }
                        ]
                    });

                    chart.render();
                }

                // 파이 차트
                $.drawPieChart({
                    completeCount: completeCount,
                    exceptionCount: exceptionCount,
                    penaltyCount: penaltyCount
                });

                // bar 차트
                $.drawBarChart({
                    receiptCounts: receiptCounts,
                    reportCounts: reportCounts
                });
            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>

