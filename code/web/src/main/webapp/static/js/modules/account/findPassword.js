define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var findPassword = {};
    findPassword.init = function () {

    };

    $(function () {
        $('.tab span:nth-child(1)').click(function () {
            $(this).addClass('tab-cur').siblings('.tab span').removeClass('tab-cur');
            $('.re-cont .ul1').show().siblings('.re-cont ul').hide();
        });

        $('.tab span:nth-child(2)').click(function () {
            $(this).addClass('tab-cur').siblings('.tab span').removeClass('tab-cur');
            $('.re-cont .ul2').show().siblings('.re-cont ul').hide();
        });

        $('.re-btn2').click(function () {
            $('.re-conts').hide();
            $('.re-cont3').show();
            $('.ul-luc li:nth-child(3)').addClass('orali');
        });

        $('.re-btn3').click(function () {
            $('.re-conts').hide();
            $('.re-cont4').show();
            $('.ul-luc li:nth-child(4)').addClass('orali');
        });
        $('.re-btn-email').click(function () {
            $('.re-conts .ul2').hide();
            $('.re-conts .ul21').show();
        });

        $('#verifyImg').click(function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });

        $('#verifyImg2').click(function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });

        $('.re-btn1').click(function () {
            var verifyCode = $('.verifyCode1').val();
            var name = $('.username').val();
            common.getData("/account/verifyCodeWithName.do", {name: name, verifyCode: verifyCode}, function (resp) {
                alert(JSON.stringify(resp));
                if (resp.code == '200') {
                    $('.re-conts').hide();
                    $('.re-cont2').show();
                    $('.ul-luc li:nth-child(2)').addClass('orali');

                } else {
                    alert(resp.message);
                }

            })
        });
    });

    module.exports = findPassword;
});