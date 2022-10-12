<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String contextPath = request.getContextPath(); %>

<stripes:layout-render name="/WEB-INF/views/layout/mobileMapHtmlLayout.jsp">

	<!-- content -->
	<stripes:layout-component name="contents">
		<main>
			<div class="row">
				<div class="map_wrap">
					<div id="drawingMap"></div>
				</div>
			</div>
		</main>
	</stripes:layout-component>

	<!-- footer -->
	<stripes:layout-component name="footer">
		<stripes:layout-render name="/WEB-INF/views/layout/component/footerLayout.jsp"/>
	</stripes:layout-component>

	<!-- javascript -->
	<stripes:layout-component name="javascript">
		<script src="<%=contextPath%>/resources/js/area/mapCommon-scripts.js"></script>
		<script src="<%=contextPath%>/resources/js/area/map-scripts.js"></script>
		<script type="application/javascript">
            // INTERFACE : APP TO WEB
            function appToGps(x, y) {
                $('#debug').val(x + "," + y + " :: " + (typeof x));
            }

            $(function () {
                // INTERFACE : WEB TO APP
                // TYPE : parking / pm
                // DATA (parking) : pkName ( 주차장이름 ) / pkAddr(주소) / pkPrice(요금) / pkOper(운영 - 유료/무료) / pkCount(주차장면수) / pkPhone(주차장 전화번호)
                // DATA (pm) : pmName ( 킥보드 이름 ) / pmPrice(요금) / pmOper(운영 - 유료/무료)
                $('#webToApp').on('click', function (event) {
                    let obj = {
                        type: 'parking',
                        data: {
                            pkName: '1',
                            pkAddr: '2',
                            pkPrice: '3',
                            pkOper: '4',
                            pkCount: '5',
                            pkPhone: '6'
                        }
                    };

                    webToApp.postMessage(JSON.stringify(obj));
                });
            });
		</script>
	</stripes:layout-component>

</stripes:layout-render>