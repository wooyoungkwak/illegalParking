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

<stripes:layout-render name="/WEB-INF/views/layout/navMapHtmlLayout.jsp">

    <!-- nav -->
    <stripes:layout-component name="nav">
        <stripes:layout-render name="/WEB-INF/views/layout/component/navLayout.jsp"/>
    </stripes:layout-component>
<style>
  .parking_wrap {position:absolute;bottom:28px;left:-150px;width:300px;}
  .parkinginfo {position:relative;width:100%;border-radius:6px;border: 1px solid #ccc;border-bottom:2px solid #ddd;padding-bottom: 10px;background: #fff;}
  .parkinginfo:nth-of-type(n) {border:0; box-shadow:0px 1px 2px #888;}
  .parkinginfo_wrap .after {content:'';position:relative;margin-left:-12px;left:50%;width:22px;height:12px;background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png')}
  .parkinginfo a, .parkinginfo a:hover, .parkinginfo a:active{color:#fff;text-decoration: none;}
  .parkinginfo a, .parkinginfo span {display: block;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
  .parkinginfo span {margin:5px 5px 0 5px;cursor: default;font-size:13px;}
  .parkinginfo .title {font-weight: bold; font-size:14px;border-radius: 6px 6px 0 0;margin: -1px -1px 0 -1px;padding:10px; color: #fff;background: #d95050;background: #d95050 url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png) no-repeat right 14px center;}
  .parkinginfo .tel {color:#0f7833;}
  .parkinginfo .jibun {color:#999;font-size:11px;margin-top:0;}
</style>
    <!-- side -->
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
                    <div class="col-12">
                        <div id="drawingMap"></div>
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
        <script src="<%=contextPath%>/resources/js/parking/map-scripts.js"></script>
    </stripes:layout-component>

</stripes:layout-render>