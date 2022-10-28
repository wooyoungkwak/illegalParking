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

<stripes:layout-render name="/WEB-INF/views/layout/htmlLayout.jsp">

    <!-- content -->
    <stripes:layout-component name="contents">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-5">
                    <div class="card shadow-lg border-0 rounded-lg mt-5">
                        <div class="card-header"><h3 class="text-center font-weight-light my-4">Login</h3></div>
                        <div class="card-body">

                            <form method="post" id="FormLogin" action="/loginProcess">
                                <div class="form-floating mb-3">
                                    <input class="form-control" name="email" id="email" placeholder="user" value=""/>
                                    <label for="email">아이디</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <input class="form-control" name="password" id="password" type="password" value=""/>
                                    <label for="password">패스워드</label>
                                </div>
                                <div class="form-check mb-3">
                                    <input class="form-check-input" id="saveId" type="checkbox" value=""/>
                                    <label class="form-check-label" for="saveId">아이디 저장</label>
                                </div>
                                <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <a class="small" href="password"></a>
                                    <a class="btn btn-primary" id="BtnLogin">로그인</a>
                                </div>
                            </form>

                        </div>
                        <div class="card-footer text-center py-3">
                            <div class="small">문의 : 061-930-7071</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </stripes:layout-component>

    <!-- javascript -->
    <stripes:layout-component name="javascript">
        <script type="text/javascript">

            //쿠키 저장하는 함수
            function set_cookie(name, value, unixTime) {
                var date = new Date();
                date.setTime(date.getTime() + unixTime);
                document.cookie = encodeURIComponent(name) + '=' + encodeURIComponent(value) + ';expires=' + date.toUTCString() + ';path=/';
            }

            //쿠키 값 가져오는 함수
            function get_cookie(name) {
                var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
                return value? value[2] : null;
            }

            $.cookie = function (key, val){
                if (val == undefined) {
                    return get_cookie(key);
                }
                set_cookie(key, val, "");
            }

            $(function () {
                let idName = "email";
                function saveId(){
                    if ( $('#saveId').is(":checked") ) {
                        $.cookie(idName,$('#email').val() );
                    }
                }

                // 로그인
                function login(){
                    if ( $('#email').val() === '' ){
                        alert("아이디를 입력하세요.");
                        return;
                    }
                    if ($('#password').val() === '') {
                        alert("패스워드를 입력하세요.");
                        return;
                    }

                    saveId();

                    $form.submit();
                }

                let $form = $('#FormLogin');
                let $btnLogin = $('#BtnLogin');

                $btnLogin.on('click', function () {
                    login();
                });

                $('#password').on('keydown', function (e) {
                    if (e.key == 'Enter') { // Enter key
                        login();
                    }
                });

                let url = location.href;
                let path = url.split('?');
                if (path.length > 1) {
                    let state = path[1].split('=')[1];

                    if (state == 'fail') {
                        alert('인증 실패 하였습니다.');
                    }
                    location.href = path[0];
                }

                if ( $.cookie(idName) !== undefined ){
                    $('#email').val($.cookie(idName));
                }

            });
        </script>

    </stripes:layout-component>

</stripes:layout-render>