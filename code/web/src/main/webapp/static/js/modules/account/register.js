define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    var register = {};

    var cacheKeyId = '';

    var registerType = 'phone';

    var timer = '';
    var isClick = false;

    var validateCode = false;

    register.init = function () {

    };

    $(function () {
        $('.re-cont .tab span').click(function () {
            $(this).addClass('tab-cur').siblings('.re-cont .tab span').removeClass('tab-cur');
        });

        $('.re-cont .tab span:nth-child(1)').click(function () {
            $('.re-cont .ul1').show();
            $('.re-cont .ul2').hide();
            $('.re-cont .ul3').hide();
        });

        $('.re-cont .tab span:nth-child(2)').click(function () {
            $('.re-cont .ul2').show();
            $('.re-cont .ul1').hide();
            $('.re-cont .ul3').hide();
        });

        $("#phone").blur(function () {
            phoneValid();
        });

        $('#verifyCode').blur(function () {
            verifyCodeValid();
        });

        $('.sendYZM').click(function () {
            var phone = $('#phone').val();
            var requestParm = {
                phone: phone
            };
            common.getData('/account/userPhoneCheck', requestParm, function (resp) {
                if (resp.message) {
                    $('#phone-alert').text('该手机号已被使用');
                    $('#phone-alert').show();
                } else {
                    $('#phone-alert').hide();
                    if (!isClick) {
                        isClick = true;
                        sendVerifyCode($('#phone').val(), $('#verifyCode').val());
                    }
                }
            });
        });

        $('#code').blur(function () {
            codeValid();
        });

        $('.ul1 button').click(function () {
            var code = $.trim($('#code').val());
            var phone = $.trim($('#phone').val());
            if(phoneValid() && validateCode) {
                validate_phone(phone, code);
            }
        });

        $('.ul2 button').click(function () {
            var email = $('#user-email').val();
            var pattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if (pattern.test(email)) {
                var requestParm = {email: email};
                common.getData('/account/userEmailCheck.do', requestParm, function (resp) {
                    if (resp.code === '200') {
                        if (resp.message) {
                            $('#email-alert').text('邮箱已经被注册了');
                            $('#email-alert').show();
                        } else {
                            $('.re-cont .ul1').hide();
                            $('.re-cont .ul2').hide();
                            $('.re-cont .ul3').show();
                            registerType = 'email';
                        }
                    } else {
                        alert(resp.message);
                    }
                });
            } else {
                $('#email-alert').text('邮箱不符合格式');
                $('#email-alert').show();
            }
        });

        $('.ul3 #nick').blur(function () {
            nickNameValid();
        });

        $('.ul3 #password').blur(function () {
            passwordValid();
        });

        $('.ul3 #re-password').blur(function () {
            rePasswordValid();
        });

        $('.ul3 button').click(function () {

            var email = $('#user-email').val();
            var nickName = $('#nick').val();
            var password = $('#password').val();
            var phone = $('#phone').val();
            var code = $('#code').val();

            if(nickNameValid() && passwordValid() && rePasswordValid()) {
                if (registerType === 'phone') {
                    registerUser(code, phone, password, phone, '', nickName);
                } else {
                    registerUser('', email, password, '', email, nickName);
                }
            }
        });

        $('body').on('click', '#imgObj', function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });
    });

    function rePasswordValid() {
        var password = $('#password').val();
        var rePassword = $('#re-password').val();
        if (password === rePassword) {
            return true;
        } else {
            $('#re-password-alert').text('两次密码不一致');
            $('#re-password-alert').show();
        }
        return false;
    }

    function passwordValid() {
        var password = $('.ul3 #password').val();
        var pattern = /^[a-zA-Z0-9!@#*\^$%()-+=_&]{6,20}$/;
        if (!pattern.test(password)) {
            $('#password-alert').text('密码不符合规范');
            $('#password-alert').show();
        } else {
            $('#password-alert').hide();
            return true;
        }
        return false;
    }

    function nickNameValid() {
        var nick = $('.ul3 #nick').val();
        var pattern = /^[a-zA-Z0-9_\u4e00-\u9fa5]{3,20}$/;
        if (!pattern.test(nick)) {
            $('#nick-alert').text('昵称不符合规范');
            $('#nick-alert').show();
        } else {
            $('#nick-alert').hide();
            return true;
        }
        return false;
    }

    function codeValid() {
        if ($('#code').val() == '') {
            $('#code-alert').text('参数不完整');
            $('#code-alert').show();
        } else {
            $('#code-alert').hide();
            return true;
        }
        return false;
    }

    function verifyCodeValid() {
        if ($('#verifyCode').val() == '') {
            $('#verifyCode-alert').text('请填写验证码');
            $('#verifyCode-alert').show();
        } else {
            $('#verifyCode-alert').hide();
            return true;
        }
        return false;
    }

    function phoneValid() {
        var pattern = /^1[3|4|5|7|8][0-9]{9}$/;
        if (pattern.test($('#phone').val())) {
            $('#phone-alert').hide();
            return true;
        } else {
            $('#phone-alert').show();
        }
        return false;
    }

    function sendVerifyCode(phone, verifyCode) {
        common.getData("/mall/users/messages.do", {
            mobile: phone,
            verifyCode: verifyCode
        }, function (resp) {
            if (resp.code == '200') {
                cacheKeyId = resp.cacheKeyId;
                validateCode = true;
                $('.sendYZM').css({
                    "background-color": '#B5B5B5'
                });
                $('.sendYZM').text('60');
                var number = 59;
                timer = window.setInterval(function () {
                    if (number === 0) {
                        clearInterval(timer);
                        $('.sendYZM').css({
                            "background-color": '#FF9F19'
                        });
                        $('.sendYZM').text('发送验证码');
                        isClick = false;
                    } else {
                        $('.sendYZM').text(number--);
                    }
                }, 1000);
            } else {
                alert(resp.message);
                isClick = false;
            }
        });
    }

    function validate_phone(phone, code) {
        common.getData("/account/validatePhone.do", {
            phone: phone,
            code: code,
            cacheKeyId: cacheKeyId
        }, function (resp) {
            alert(JSON.stringify(resp));
            if (resp.code === '200') {
                $('.re-cont .ul1').hide();
                $('.re-cont .ul2').hide();
                $('.re-cont .ul3').show();
                registerType = 'phone';
            }
        });
    }

    var isRegister = false;

    function registerUser(code, userName, password, phone, email, nickName) {
        var requestData = {};
        requestData.cacheKeyId = cacheKeyId;
        requestData.code = code;
        requestData.email = email;
        requestData.userName = userName;
        requestData.passWord = password;
        requestData.phoneNumber = phone;
        requestData.nickName = nickName;
        if (isRegister) {
            return;
        }
        isRegister = true;
        common.postDataAsync('/mall/users.do', requestData, function (resp) {
            if (resp.code == 200) {
                if (resp.type == 1) {
                    $('#successDiv').show();
                    setTimeout(function () {
                        window.location.href = '/';
                    }, 2000);
                } else if (resp.type == 2) {
                    var message = resp.message;
                    var item = message.split("$");
                    location.href = '/mall/users/sendEmail.do?email=' + item[0] + '&emailValidateCode=' + item[1];
                }
            } else {
                alert(resp.message);
                isRegister = false;
            }

        });
    }

    module.exports = register;
});