define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var findPassword = {};
    findPassword.init = function () {

    };

    var check = {
        phone : false,
        verifyCode: false,
        code: false
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

        $('#phone').blur(function () {
            var self = $(this);
            var pattern = /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            if (pattern.test(self.val())) {
                var requestParm = {phone: self.val()};
                common.getData('/account/verifyUserPhone', requestParm, function (resp) {
                    if(resp.code == '200' && resp.message.verify) {
                        self.parent().find('.sp3').hide();
                    } else {
                        self.parent().find('.sp3').text(resp.message.msg);
                        self.parent().find('.sp3').show();
                    }
                });
            } else {
                self.parent().find('.sp3').text('手机号不合法');
                self.parent().find('.sp3').show();
            }
        });

        $('#sendCode').click(function () {
            var phone = $('#phone').val();
            var verifyCode = $('#verifyCode').val();
            common.getData("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {
                alert(JSON.stringify(resp));
                if (resp.code == '200') {

                } else {
                    alert(resp.message);
                }

            })
        });

        $('body').on('click','.next2',function () {
            if (!$('.ul2 .argument').is(':checked')) {
                $(this).parent().find('.sp3').text('未勾选社区协议');
                $(this).parent().find('.sp3').show();
                return;
            } else {
                $(this).parent().find('.sp3').hide();
            }

            if(check.code && check.phone && check.verifyCode) {
                $(this).parent().find('.sp3').hide();
                $('.re-conts').hide();
                $('.re-cont3').show();
                $('.ul-luc li:nth-child(3)').addClass('orali');
            } else {
                $(this).parent().find('.sp3').text('输入不完整');
                $(this).parent().find('.sp3').show();
            }
        });
    });

    module.exports = findPassword;
});