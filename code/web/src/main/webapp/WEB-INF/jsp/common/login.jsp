<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="store-register" style="display: none;">
    <dl>
        <dt>
            <em>登录</em>
        <div>没有账号，<a href="/account/register.do">立即注册</a>
        </div>
        <i id="close">X</i>
        </dt>
        <dd>
            <div hidden id="accountError" class="error">帐户名与密码不匹配，请重新输入</div>
            <h1 class="notice-dl">中国最专业的青少年素质教育社区!</h1>

            <div class="store-MF" hidden>该登录名不存在<a href="/account/register.do">免费注册？</a></div>
        </dd>
        <dd>
            <input class="inp1" type="text" placeholder="用户名/邮箱/手机号" id="account">
        </dd>
        <dd>
            <input class="inp2" type="password" id="password" placeholder="请输入登录密码">
            <span class="mm-forget" onclick="window.open('/account/findPassword.do')">忘记密码</span>
        </dd>
        <dd>
            <span class="store-DL" id="logIn">登录</span>
        </dd>
        <dd>
            <em></em>
        </dd>
    </dl>
</div>
<!--==========背景============-->
<div class="bg" hidden></div>


<script>
    $('body').on('click', '#close', function () {
        $('.store-register').fadeToggle();
        $('.bg').fadeToggle();
    });

    $('#logIn').click(function () {
        logIn();
    });

    $('#password').keydown(function (event) {
        if (event.which == 13) {
            logIn();
        }
    });

    function logIn() {
        $('.store-MF').hide();
        $('.error').hide();

        var requestData = {};
        requestData.name = $.trim($('#account').val());
        requestData.pwd = $.trim($('#password').val());
        requestData.verifyCode = "";
        $.post('/user/login.do', requestData, function (resp) {
            if ('200' == resp.code) {

                try {
                    loginK6ktSso();
                } catch (x) {

                }
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();

                $('#veryCodeSpan').hide();
                var session = resp.message;
                $('#userName').text('欢迎您，' + session.userName);
                $('body').attr('login', 'true');
                if (session.k6kt == 1) {
                    $('#k6kt').show();
                } else {
                    $('#k6kt').hide();
                }
                $('#password').val('');
                window.location.href = window.location.href;
            } else {
                if ('accountError' == resp.message) {
                    $('.store-MF').show();
                } else {
                    $('.error').html(resp.message);
                    $('.error').show();
                }
            }
        })
    }
</script>
<script type="text/javascript" src="http://www.k6kt.com/static/js/k6kt-sso.js"></script>
