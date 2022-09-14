<%--
  Created by IntelliJ IDEA.
  User: zilet
  Date: 2022-03-02
  Time: 오후 8:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>

<stripes:layout-definition>
    <!DOCTYPE html>
    <html>
        <%@ include file="/WEB-INF/views/reference/head.jsp"%>

        <body>

            <stripes:layout-component name="nav" />

            <div id="layoutSidenav">

                <stripes:layout-component name="side"/>

                <div id="layoutSidenav_content">

                    <stripes:layout-component name="contents"/>

                    <footer class="py-4 bg-light mt-auto">
                        <div class="container-fluid px-4">
                            <div class="d-flex align-items-center justify-content-between small">
                                <div class="text-muted">Copyright &copy; Your Website 2021</div>
                                <div>
                                    <a href="#">Privacy Policy</a>
                                    &middot;
                                    <a href="#">Terms &amp; Conditions</a>
                                </div>
                            </div>
                        </div>
                    </footer>
                </div>
            </div>

            <stripes:layout-component name="javascript"/>

        </body>
    </html>
</stripes:layout-definition>