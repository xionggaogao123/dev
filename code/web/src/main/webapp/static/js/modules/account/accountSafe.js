define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var accountSafe = {};
    accountSafe.init = function () {

    };

    $(function () {
        $('.btn-xg-psw').click(function () {
            $('.wind-psw').fadeIn();
            $('.bg').fadeIn();
        });
        $('.btn-xg-phone').click(function () {
            $('.wind-phone').fadeIn();
            $('.bg').fadeIn();
        });
        $('.windd .p1 em').click(function () {
            $('.windd').fadeOut();
            $('.bg').fadeOut();
        });
        $('.btn-xg-email').click(function () {
            $('.wind-email1').fadeIn();
            $('.bg').fadeIn();
        });
        $('.wind-email1 .p-btn-ok').click(function () {
            $('.windd').fadeOut();
            $('.wind-email2').fadeIn();
        });

        $('ul.set-left li').click(function () {
            var value = $(this).index() + 1;

            for (var i = 1; i <= 4; i++) {
                if (i == value) {
                    $('.set-container .right-' + i).show();
                    continue;
                }
                $('.set-container .right-' + i).hide();
            }

            $('ul.set-left li').each(function () {
                $(this).removeClass('li444');
            });

            $(this).addClass('li444');
        });

        $('.bq-list span').click(function () {
            if ($(this).hasClass('oracur2')) {
                $(this).removeClass('oracur2');
            } else {
                $(this).addClass('oracur2');
            }
        });
    });

    module.exports = accountSafe;
});