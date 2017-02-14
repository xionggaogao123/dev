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
            <div class="te-df inp1">
                <input  type="text" placeholder="用户名/邮箱/手机号" id="account">
            </div>
        </dd>
        <dd id="user-names" hidden>
        </dd>
        <dd>
            <div class="te-df inp2">
                <input  type="password" id="password" placeholder="请输入登录密码">
            </div>
            <span class="mm-forget" onclick="window.open('/account/findPassword.do')">忘记密码</span>
        </dd>
        <dd>
            <span class="store-DL" id="logIn">登录</span>
        </dd>
        <dd>
            <p class="p-outc">或使用其他账号登录：<i class="i-qq" onclick="redirectQ()"></i><i class="i-wx" onclick="loginWeiXin()"></i></p>
        </dd>
    </dl>
</div>
<!--==========背景============-->
<div class="bg" hidden></div>

<script>
    var isMutilUser = false;

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

        if(isMutilUser) {
            requestData.name = $.trim($("input[name='s-count']:checked").val());
        } else {
            requestData.name = $.trim($('#account').val());
        }
        requestData.pwd = $.trim($('#password').val());
        requestData.verifyCode = "";
        $.post('/user/login.do', requestData, function (resp) {
            if ('200' == resp.code) {

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

    $('#account').bind('input',function () {
        var account = $('#account').val();
        var pattern = /^1(3|4|5|7|8)\d{9}$/;
        isMutilUser = false;
        if(pattern.test(account)) {
            $.ajax({
                url:'/account/listBindUserName.do',
                data:{phone: account},
                success:function (resp) {
                    if(resp.code === '200') {
                        if(resp.message.length > 1) {
                            $('#user-names').show();
                            $('#user-names').empty();
                            $('#user-names').append('<p class="p-ps">该手机已绑定多个账号，请选择要登录的账号：</p>');
                            isMutilUser = true;
                            for(var i=0;i<resp.message.length;i++) {
                                if(i === 0 ) {
                                    $('#user-names').append('<label><input value="'+ resp.message[i] +'" type="radio" name="s-count" checked>'+ resp.message[i]+'</label>');
                                } else {
                                    $('#user-names').append('<label><input value="'+ resp.message[i] +'" type="radio" name="s-count">'+ resp.message[i]+'</label>');
                                }
                            }
                        } else {
                            hideNames();
                        }
                    } else {
                        hideNames();
                    }
                }
            });
        } else {
            hideNames();
        }
    });

    function hideNames() {
        $('#user-names').empty();
        $('#user-names').hide();
    }

    function redirectQ() {
        var currentUrl=encodeURI(encodeURI(location.href));
        window.open('/user/qqlogin.do?currentUrl='+currentUrl, "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
    }

    function loginWeiXin() {
        window.open('/user/wechatlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
    }
</script>
<script type="text/javascript" src="http://www.k6kt.com/static/js/k6kt-sso.js"></script>
