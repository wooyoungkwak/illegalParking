<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String contextPath = request.getContextPath(); %>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>${title}</title>
    <link href="<%=contextPath%>/resources/css/styles.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@latest/dist/style.css" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="<%=contextPath%>/resources/js/scripts.js"></script>

    <script type="application/javascript">
        // contextPath 구하기
        $.getContextPath = function () {
            let hostIndex = location.href.indexOf(location.host) + location.host.length;
            return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
        }

        let SELECT_ALL = "all";
        let SELECT_TYPE= "type";
        let SELECT_DONG = "dong";
        let SELECT_TYPE_AND_DONG = "typeAndDong";
        const log = console.log;
        const _contextPath = $.getContextPath();

        let _userSeq = '${userSeq}';
        let _userName = '${userName}';

        $(function () {
            let paths = location.pathname.split("/");
            let size = paths.length;

            $('#navMenu').find('a').removeClass("active");
            $('#layoutSidenav_nav').find('a').removeClass("active");

            if(location.pathname === '/home'){
                $('#sidebarToggle').hide();
            } else {
                $('#nav' + _contextPath.replace("/","")).addClass("active");
            }

            $('#side_' + paths[size-1]).addClass("active");

            // Toggle the side navigation
            const sidebarToggle = document.body.querySelector('#sidebarToggle');
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', event => {
                    event.preventDefault();
                    document.body.classList.toggle('sb-sidenav-toggled');
                    localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
                });
            }

            $('#back').on('click', function (){
                window.history.back();
            })
        });
    </script>
</head>