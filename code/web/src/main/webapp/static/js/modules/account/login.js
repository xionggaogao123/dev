define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var login = {};

    var check = {
        userName: false,
        password: false
    };
    login.init = function () {

    };

    $(function () {

        $('.ul1 .forget-password').click(function () {
            window.location.href = '/account/findPassword';
        });

        $('input.username').blur(function () {
            if ($('input.username').val() == '') {
                $(this).parent().find('.sp3').show();
                check.userName = false;
            } else {
                $(this).parent().find('.sp3').hide();
                check.userName = true;
            }
        });

        $('input.password').blur(function () {
            check.password = $(this).val() != '';
        });


        $('.ul1 button').click(function () {

            if (!check.userName || !check.password) {
                return;
            }
            var userName = $('input.username').val();
            var password = $('input.password').val();
            var requestData = {};
            requestData.name = $.trim(userName);
            requestData.pwd = $.trim(password);
            requestData.verifyCode = "";
            $.post('/user/login.do', requestData, function (resp) {

                if (resp.code == '200') {
                    window.location.href = '/';
                } else {
                    alert(resp.message);
                }
            });
        });

        $('.ul1 li .sp-qq').click(function () {
            window.open('/user/qqlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        });

        $('.ul1 li .sp-wx').click(function () {
            window.open('/user/wechatlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        });
    });

    module.exports = login;
});