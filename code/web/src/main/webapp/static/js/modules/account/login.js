define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var login = {};
    var isMutilUser = false;

    login.init = function () {

        $(document).keypress(function (e) {
            if (e.which == 13) {
                loginWeb();
            }
        });
    };

    $(function () {

        $('.ul1 .forget-password').click(function () {
            window.location.href = '/account/findPassword';
        });

        $('input.username').blur(function () {
            if ($(this).val() == '') {
                $(this).parent().find('.sp3').show();
            } else {
                $(this).parent().find('.sp3').hide();
            }
        });

        $('input.password').blur(function () {
            if ($(this).val() == '') {
                $(this).parent().find('.sp3').show();
            } else {
                $(this).parent().find('.sp3').hide();
            }
        });


        $('.ul1 button').click(function () {
            loginWeb();
        });

        $('.ul1 li .sp-qq').click(function () {
            window.open('/user/qqlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        });

        $('.ul1 li .sp-wx').click(function () {
            window.open('/user/wechatlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        });

        $('#account').bind('input',function () {
            var account = $('#account').val();
            var pattern = /^1(3|4|5|7|8)\d{9}$/;
            isMutilUser = false;
            if (pattern.test(account)) {
                $.ajax({
                    url: '/account/listBindUserName.do',
                    data: {phone: account},
                    success: function (resp) {
                        if (resp.code === '200') {
                            if (resp.message.length > 1) {
                                $('#user-names').show();
                                $('#user-names').empty();
                                $('#user-names').append('<p style="color:#FF4918;">该手机已绑定多个账号，请选择要登录的账号：</p>');
                                isMutilUser = true;
                                for (var i = 0; i < resp.message.length; i++) {
                                    if(i === 0 ) {
                                        $('#user-names').append('<label><input value="' + resp.message[i] + '" type="radio" name="s-count" checked>' + resp.message[i] + '</label>');
                                    } else {
                                        $('#user-names').append('<label><input value="' + resp.message[i] + '" type="radio" name="s-count">' + resp.message[i] + '</label>');
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

    });

    function hideNames() {
        $('#user-names').empty();
        $('#user-names').hide();
    }

    function loginWeb() {
        var userName = $('input.username').val();
        var password = $('input.password').val();
        if (password == '' || userName == '') {
            return;
        }
        var requestData = {};
        if(isMutilUser) {
            requestData.name = $.trim($("input[name='s-count']:checked").val());
        } else {
            requestData.name = $.trim($('#account').val());
        }
        requestData.pwd = $.trim(password);
        requestData.verifyCode = "";
        $.post('/user/login.do', requestData, function (resp) {
            if (resp.code == '200') {
                window.location.href = '/';
            } else {
                alert(resp.message);
            }
        });
    }

    module.exports = login;
});