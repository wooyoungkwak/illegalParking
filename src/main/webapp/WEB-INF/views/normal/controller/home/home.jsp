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
                                {label: "대기", y: opt.completeCount},
                                {label: "미처리", y: opt.exceptionCount},
                                {label: "처리", y: opt.penaltyCount}
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

                $.drawPieChart({
                    completeCount: completeCount,
                    exceptionCount: exceptionCount,
                    penaltyCount: penaltyCount
                });

                // 월별 신고 건수
                $.drawBarChart = function (opt) {
                    var chart = new CanvasJS.Chart("barChart", {
                        animationEnabled: true,
                        title: {
                            text: "신고 발생 / 신고 접수 "
                        },
                        data: [
                            {
                                // 신고 발생 건수
                                type: "column",
                                dataPoints: [
                                    {label: "1월", y: 71},
                                    {label: "2월", y: 55},
                                    {label: "3월", y: 50},
                                    {label: "4월", y: 65},
                                    {label: "5월", y: 95},
                                    {label: "6월", y: 68},
                                    {label: "7월", y: 28},
                                    {label: "8월", y: 34},
                                    {label: "9월", y: 14},
                                    {label: "10월", y: 14},
                                    {label: "11월", y: 14},
                                    {label: "12월", y: 14}
                                ]
                            },
                            {
                                // 신고 접수
                                type: "column",
                                dataPoints: [
                                    {label: "1월", y: 31},
                                    {label: "2월", y: 25},
                                    {label: "3월", y: 20},
                                    {label: "4월", y: 25},
                                    {label: "5월", y: 55},
                                    {label: "6월", y: 38},
                                    {label: "7월", y: 28},
                                    {label: "8월", y: 24},
                                    {label: "9월", y: 14},
                                    {label: "10월", y: 14},
                                    {label: "11월", y: 14},
                                    {label: "12월", y: 14}
                                ]
                            }
                        ]
                    });

                    chart.render();
                }

                $.drawBarChart();
            });
        </script>
    </stripes:layout-component>

</stripes:layout-render>