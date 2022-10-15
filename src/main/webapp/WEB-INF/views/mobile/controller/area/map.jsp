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
					<div id="map"></div>
					<div class="custom_mapcontrol radius_border">
						<label for="zone"><input type="radio" name="mapSelect" id="zone" checked>불법주차</label>
						<label for="parking"><input type="radio"name="mapSelect" id="parking">주차장</label>
						<label for="pm"><input type="radio"name="mapSelect" id="pm">모빌리티</label>
					</div>
					<!-- 지도 확대, 축소 컨트롤 div 입니다 -->
					<div class="custom_zoomcontrol radius_border">
						<span id="zoomIn"><img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_plus.png" alt="확대"></span>
						<span id="zoomOut"><img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_minus.png" alt="축소"></span>
					</div>

					<div class="custom_btncontrol radius_border">
						<span id="btnFindMe" class="btn"><img src="<%=contextPath%>/resources/assets/img/re_ping.png" alt="내위치"></span>
					</div>

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

			$('#btnFindMe').on('click', function() {
				$.setCurrentPosition();
			});
			$('#zoomIn').on('click', function() {
				$.zoomIn();
			});
			$('#zoomOut').on('click', function() {
				$.zoomOut();
			});

			$('input:radio[name=mapSelect]').on('change', function(event) {
				log(event);
				log(event.target.id);
				if(event.target.id === 'zone'){

				}
				if(event.target.id === 'parking'){

				}
				if(event.target.id === 'pm'){

				}

			});
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