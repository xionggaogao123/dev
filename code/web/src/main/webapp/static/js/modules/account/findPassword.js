define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');

    var findPassword = {};
    var mobileInit = false;
    var mobile = '';
    findPassword.init = function () {

    };

    var userName = '';

    var cacheKeyId = '';
    var timer = '';
    var isClick = false;

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
            common.getDataAsync("/account/sendVerifyEmail.do", {email: email}, function (resp) {
                if (resp.code == '200') {
                    $('.re-conts .ul2').hide();
                    $('.re-conts .ul21').show();
                    $('.ul21 .sp7 i').text(email);
                } else {
                    alert(resp.message.msg);
                }
            });
        });

        $('#verifyImg,#verifyImg2').click(function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });

        $('.re-btn1').click(function () {
            var verifyCode = $('.verifyCode1').val();
            var name = $('.username').val();
            common.getDataAsync("/account/verifyAccount.do", {name: name, verifyCode: verifyCode}, function (resp) {
                if (resp.code == '200') {
                    $('.re-conts').hide();
                    $('.re-cont2').show();
                    $('.ul-luc li:nth-child(2)').addClass('orali');
                    $('#verifyImg').attr('src', '/verify/verifyCode.do?date' + new Date());
                    if (resp.message.type === 'mobile') {
                        for (var i = 0; i < resp.message.users.length; i++) {
                            if (i == 0) {
                                $('#choose-name').append('<label><input type="radio" value="' + resp.message.users[i].userName + '" name="s-count" checked>' + resp.message.users[i].nickName + '</label>');
                            } else {
                                $('#choose-name').append('<label><input type="radio" value="' + resp.message.users[i].userName + '" name="s-count">' + resp.message.users[i].userName + '</label>');
                            }
                        }
                        $('#choose-name').show();
                        $('#phone').val(resp.message.protectedMobile);
                        mobile = resp.message.mobile;
                        $('#phone').attr("disabled", "disabled");
                        $('.step2 > li').eq(0).find('.sp3').hide();
                        mobileInit = true;
                    } else if (resp.message.type === 'userName') {
                        userName = name;
                        $('#phone').val(resp.message.protectedMobile);
                        $('#phone').attr("disabled", "disabled");
                        mobile = resp.message.mobile;
                        $('#email').val(resp.message.email);
                    } else if (resp.message.type === 'email') {
                        userName = resp.message.userName;
                        $('#email').val(resp.message.email);
                    }
                } else {
                    alert(resp.errorMessage);
                }
            })
        });
        
        var sendCode = $('#sendCode');
        sendCode.click(function () {
            var phone = mobileInit ? mobile : $('#phone').val();
            var verifyCode = $('#verifyCode').val();
            if(!isClick) {
                isClick = true;
                common.getDataAsync("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {
                    if (resp.code == '200') {
                        cacheKeyId = resp.cacheKeyId;
                        sendCode.css({
                            "background-color": '#B5B5B5'
                        });
                        sendCode.text('60');
                        var number = 59;
                        timer = window.setInterval(function () {
                            if (number === 0) {
                                clearInterval(timer);
                                sendCode.css({
                                    "background-color": '#FF9F19'
                                });
                                sendCode.text('发送验证码');
                                isClick = false;
                            } else {
                                sendCode.text(number--);
                            }
                        }, 1000);
                    } else {
                        isClick = false;
                        alert(resp.message);
                    }
                });
            }
        });

        body.on('click', '.next2', function () {
            var code = $('#code').val();
            var requestParm = {
                phone: mobile,
                code: code,
                cacheKeyId: cacheKeyId
            };
            common.getDataAsync("/account/phoneValidate.do", requestParm, function (resp) {
                if (resp.code == '200') {
                    $('.re-conts').hide();
                    $('.re-cont3').show();
                    $('.ul-luc li:nth-child(3)').addClass('orali');
                } else {
                    alert(resp.errorMessage);
                }
            });
        });

        body.on('click', '.re-btn3', function () {
            if(passwordValid && rePasswordValid) {
                var password = $('.step3 .password').val();
                if(typeof(verifyType) == "undefined" ) {
                    var phone = mobileInit ? mobile : $('#phone').val();
                    var code = $('#code').val();
                    userName = mobileInit ? $.trim($("input[name='s-count']:checked").val()) : userName;
                    resetPassword(userName, phone, code, password);
                } else {
                    if(verifyType === 'email') {
                        resetPasswordByEmail(password);
                    }
                }
            }
        });

        var passwordValid = false;
        var rePasswordValid = false;

        $('.step3 .password').blur(function () {
            var self = $(this);
            if (self.val() == '') {
                self.parent().find('.sp3').text('密码不能为空');
                self.parent().find('.sp3').show();
                passwordValid = false;
            } else {
                var pattern = /[a-zA-Z0-9!@#*\^$%()-+=_&]{6,20}$/;
                if (!pattern.test(self.val())) {
                    self.parent().find('.sp3').text('密码不符合格式');
                    self.parent().find('.sp3').show();
                    passwordValid = false;
                } else {
                    self.parent().find('.sp3').hide();
                    passwordValid = true;
                }
            }
        });

        $('.step3 .re-password').blur(function () {
            var self = $(this);
            if ($('.step3 .password').val() == $('.step3 .re-password').val()) {
                self.parent().find('.sp3').hide();
                rePasswordValid = true;
            } else {
                self.parent().find('.sp3').text('两次输入的密码不一致');
                self.parent().find('.sp3').show();
                rePasswordValid = false;
            }
        });

        $('.step4 button').click(function () {
            window.location.href = '/account/login.do';
        });

    });

    function resetPassword(userName, phone, code, password) {
        var requestParm = {
            phone: phone,
            code: code,
            userName: userName,
            password: password,
            cacheKeyId: cacheKeyId
        };
        common.getDataAsync('/account/resetPassword.do', requestParm, function (resp) {
            if(resp.code === '200') {
                setp4();
            } else {
                alert(resp.errorMessage);
            }
        });
    }

    function resetPasswordByEmail(password) {
        var requestParm = {
            password: password,
        };
        common.getDataAsync('/account/resetPasswordByEmail.do', requestParm, function (resp) {
            if(resp.code === '200') {
                setp4();
            } else {
                alert(resp.errorMessage);
            }
        });
    }

    function setp4() {
        $('.re-conts').hide();
        $('.re-cont4').show();
        $('.ul-luc li:nth-child(4)').addClass('orali');
        $('.ul-luc li:nth-child(3)').addClass('orali');
        $('.ul-luc li:nth-child(2)').addClass('orali');
        $('.ul-luc li:nth-child(1)').addClass('orali');
    }

    module.exports = findPassword;
});