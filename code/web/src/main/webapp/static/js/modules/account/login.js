define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var login = {};

    login.init = function () {

        $(document).keypress(function(e) {
            if(e.which == 13) {
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
    });

    function loginWeb() {
        var userName = $('input.username').val();
        var password = $('input.password').val();
        if (password == '' || userName == '') {
            return;
        }
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
    }

    module.exports = login;
});