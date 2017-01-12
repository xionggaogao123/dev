define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var register = {};

    var cacheKeyId = '';

    var validatePhone = false;
    var validateImageYZM = false;
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
            var self = $(this);
            var pattern = /(^(([0+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            if (pattern.test(self.val())) {
                var requestParm = {phone: self.val()};
                common.getData('/account/userPhoneCheck', requestParm, function (resp) {
                    if (resp.message) {
                        $('#phone-alert').text('该手机号已被使用');
                        $('#phone-alert').show();
                        validatePhone = false;
                    } else {
                        validatePhone = true;
                        self.parent().find('.sp3').hide();
                    }
                });
            } else {
                $('#phone-alert').show();
                validatePhone = false;
            }
        });

        $('#verifyCode').blur(function () {
            var self = $(this);
            if (self.val() == '') {
                self.parent().find('.sp3').text('请填写验证码');
                self.parent().find('.sp3').show();
                validateImageYZM = false;
            } else {
                self.parent().find('.sp3').hide();
                validateImageYZM = true;
            }
        });

        $('.sendYZM').click(function () {
            if (validatePhone && validateImageYZM) {
                $(this).parent().find('.sp3').hide();
                sendVerifyCode($('#phone').val(), $('#verifyCode').val());
            } else {
                $(this).parent().find('.sp3').show();
            }
        });

        $('#code').blur(function () {
            if ($(this).val() == '') {
                $(this).parent().find('.sp3').text('参数不完整');
                $(this).parent().find('.sp3').show();
            } else {
                $(this).parent().find('.sp3').hide();
            }
        });

        $('.ul1 button').click(function () {
            if (validatePhone && validateCode) {
                var code = $.trim($('#code').val());
                var phone = $.trim($('#phone').val());
                validate_phone(phone,code);
            } else {
                $(this).parent().find('.sp3').text('输入不完整');
                $(this).parent().find('.sp3').show();
            }
        });

        $('.ul2 button').click(function () {
            var email = $('#user-email').val();
            var pattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if(pattern.test(email)) {
                var requestParm = {email: email};
                common.getData('/account/userEmailCheck.do',requestParm,function (resp) {
                    if(resp.code === '200') {
                        if(resp.message) {
                            $('#email-alert').text('邮箱已经被注册了');
                            $('#email-alert').show();
                        } else {
                            $('.re-cont .ul1').hide();
                            $('.re-cont .ul2').hide();
                            $('.re-cont .ul3').show();
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
            var nick = $(this).val();
            var pattern = /[a-zA-Z0-9_\u4e00-\u9fa5]{3,20}$/;
            if(!pattern.test(nick)) {
                $('#nick-alert').text('昵称不符合规范');
                $('#nick-alert').show();
            } else {
                $('#nick-alert').hide();
            }
        });

        $('.ul3 #password').blur(function () {
            var password = $(this).val();
            var pattern = /[a-zA-Z0-9!@#*\^$%()-+=_&]{6,20}$/;
            if(!pattern.test(password)) {
                $('#password-alert').text('密码不符合规范');
                $('#password-alert').show();
            } else {
                $('#password-alert').hide();
            }
        });

        $('.ul3 #re-password').blur(function () {
            var password = $('#password').val();
            var rePassword = $('#re-password').val();
            if(password === rePassword) {

            } else {
                $('#re-password-alert').text('两次密码不一致');
                $('#re-password-alert').show();
            }

        });

        $('.ul3 button').click(function () {
            var email = $('#user-email').val();
            var nickName = $('#nick').val();
            var password = $('#password').val();
            registerUser('',email,password,'',email,nickName);
        });

        $('body').on('click', '#imgObj', function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });


    });

    function sendVerifyCode(phone, verifyCode) {
        common.getData("/mall/users/messages.do", {
            mobile: phone,
            verifyCode: verifyCode
        }, function (resp) {
            if (resp.code == '200') {
                cacheKeyId = resp.cacheKeyId;
                validateCode = true;
            } else {
                alert(resp.message);
                validateCode = false;
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
        });
    }

    var isRegister = false;

    function registerUser(code, userName, password, phone, email,nickName) {
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
        common.getPostData('/mall/users.do', requestData, function (resp) {
            if (resp.code == 200) {
                if (resp.type == 1) {
                    $('#successDiv').show();
                    setTimeout(function () {
                        window.location.href = '/';
                    }, 8000);
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