define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var findPassword = {};
    findPassword.init = function () {

    };

    var phoneVerifyCheck = {
        phone : false,
        code: false
    };

    var resetPasswordCheck = {
        password : false,
        rePassword: false
    };

    var cacheKeyId = '';

    $(function () {
        
        var body = $('body');
        $('.tab span:nth-child(1)').click(function () {
            $(this).addClass('tab-cur').siblings('.tab span').removeClass('tab-cur');
            $('.re-cont .ul1').show().siblings('.re-cont ul').hide();
        });

        $('.tab span:nth-child(2)').click(function () {
            $(this).addClass('tab-cur').siblings('.tab span').removeClass('tab-cur');
            $('.re-cont .ul2').show().siblings('.re-cont ul').hide();
        });
        
        $('.re-btn-email').click(function () {

            var email = $('#email').val();
            common.getData("/account/verifyUserEmail.do", {email: email}, function (resp) {
                if (resp.code == '200') {
                    $('.re-conts .ul2').hide();
                    $('.re-conts .ul21').show();
                    $('.ul21 .sp7 i').text(email);
                } else {
                    alert(resp.message.msg);
                }

            });
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
                if (resp.code == '200') {
                    $('.re-conts').hide();
                    $('.re-cont2').show();
                    $('.ul-luc li:nth-child(2)').addClass('orali');
                    $('#verifyImg').attr('src','/verify/verifyCode.do?date'+ new Date());
                } else {
                    alert(resp.message);
                }
            })
        });

        $('#phone').blur(function () {
            var self = $(this);
            var pattern = /^1[3|4|5|7|8][0-9]{9}$/;
            if (pattern.test(self.val())) {
                var requestParm = {phone: self.val()};
                common.getData('/account/verifyUserPhone', requestParm, function (resp) {
                    if(resp.code == '200' && resp.message.verify) {
                        self.parent().find('.sp3').hide();
                        phoneVerifyCheck.phone = true;
                    } else {
                        self.parent().find('.sp3').text(resp.message.msg);
                        self.parent().find('.sp3').show();
                        phoneVerifyCheck.phone = false;
                    }
                });
            } else {
                self.parent().find('.sp3').text('手机号不合法');
                self.parent().find('.sp3').show();
                phoneVerifyCheck.phone = false;
            }
        });

        $('#sendCode').click(function () {
            var phone = $('#phone').val();
            var verifyCode = $('#verifyCode').val();
            if(!phoneVerifyCheck.phone) {
                return;
            }
            var self = $(this);
            self.css('color','#aaa');
            common.getData("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {
                if (resp.code == '200') {
                    phoneVerifyCheck.code = true;
                    cacheKeyId = resp.cacheKeyId;
                } else {
                    alert(resp.message);
                    phoneVerifyCheck.code = false;
                }

            })
        });

        body.on('click','.next2',function () {
            if (!$('.next2-argument').is(':checked')) {
                $(this).parent().find('.sp3').text('未勾选社区协议');
                $(this).parent().find('.sp3').show();
                return;
            } else {
                $(this).parent().find('.sp3').hide();
            }

            if(phoneVerifyCheck.code && phoneVerifyCheck.phone ) {
                $(this).parent().find('.sp3').hide();

                var phone = $('#phone').val();
                var code = $('#code').val();

                var requestParm = {
                    phone: phone,
                    code: code,
                    cacheKeyId: cacheKeyId
                };
                common.getData("/account/phoneValidate.do", requestParm, function (resp) {
                    if (resp.code == '200') {
                        $('.re-conts').hide();
                        $('.re-cont3').show();
                        $('.ul-luc li:nth-child(3)').addClass('orali');
                    } else {
                        alert(resp.message);
                    }

                });

            } else {
                $(this).parent().find('.sp3').text('输入不完整');
                $(this).parent().find('.sp3').show();
            }
        });

        body.on('click','.re-btn3',function () {
            var password = $('#reset-password').val();
            if(!resetPasswordCheck.password || !resetPasswordCheck.rePassword ) {
                return;
            }
            common.getData("/account/resetPassword.do", {password: password}, function (resp) {
                if (resp.code == '200') {
                    $('.re-conts').hide();
                    $('.re-cont4').show();
                    $('.ul-luc li:nth-child(4)').addClass('orali');
                } else {
                    alert(resp.message);
                }
            });
        });

        $('.step3 .password').blur(function () {
            var self = $(this);
            if (self.val() == '') {
                self.parent().find('.sp3').text('密码不能为空');
                self.parent().find('.sp3').show();
                resetPasswordCheck.password = false;
            } else {
                var pattern = /[a-zA-Z0-9!@#*\^$%()-+=_&]{6,20}$/;
                if (!pattern.test(self.val())) {
                    self.parent().find('.sp3').text('密码不符合格式');
                    self.parent().find('.sp3').show();
                    resetPasswordCheck.password = false;
                } else {
                    self.parent().find('.sp3').hide();
                    resetPasswordCheck.password = true;
                }
            }
        });

        $('.step3 .re-password').blur(function () {
            var self = $(this);
            if ($('.step3 .password').val() == $('.step3 .re-password').val()) {
                self.parent().find('.sp3').hide();
                resetPasswordCheck.rePassword = true;
            } else {
                self.parent().find('.sp3').text('两次输入的密码不一致');
                self.parent().find('.sp3').show();
                resetPasswordCheck.rePassword = false;
            }
        });

        $('.step4 button').click(function () {
            window.location.href = '/account/login.do';
        });

        $('.receiveEmail').click(function () {

        });

    });

    module.exports = findPassword;
});