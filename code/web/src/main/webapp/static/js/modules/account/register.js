define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var register = {};

    var cacheKeyId = '';
    var phoneRegisterCheck = {
        userName: false,
        phone: false,
        password: false,
        identifyCode: false
    };

    var emailRegisterCheck = {
        userName: false,
        password: false,
        email: false
    };

    register.init = function () {

    };

    $(function () {
        $('.re-cont .tab span').click(function () {
            $(this).addClass('tab-cur').siblings('.re-cont .tab span').removeClass('tab-cur');
        });

        $('.re-cont .tab span:nth-child(1)').click(function () {
            $('.re-cont .ul1').show();
            $('.re-cont .ul2').hide();
        });

        $('.re-cont .tab span:nth-child(2)').click(function () {
            $('.re-cont .ul2').show();
            $('.re-cont .ul1').hide();
        });

        $("#phone").blur(function () {
            var self = $(this);
            var pattern = /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            if (pattern.test(self.val())) {
                var requestParm = {phone: self.val()};
                common.getData('/account/userPhoneCheck', requestParm, function (resp) {
                    if (resp.message) {
                        self.parent().find('.sp3').text('该手机号已被使用');
                        self.parent().find('.sp3').show();
                        phoneRegisterCheck.phone = false;
                    } else {
                        phoneRegisterCheck.phone = true;
                        self.parent().find('.sp3').hide();
                    }
                });
            } else {
                self.parent().find('.sp3').show();
                phoneRegisterCheck.phone = false;
            }
        });

        $('.ul2 .email-input').blur(function () {
            var self = $(this);
            var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if (pattern.test($(this).val())) {
                var requestParm = {email: $(this).val()};
                common.getData('/account/userEmailCheck', requestParm, function (resp) {
                    if (resp.message) {
                        self.parent().find('.sp3').text('该邮箱已被使用');
                        self.parent().find('.sp3').show();
                        emailRegisterCheck.email = false;
                    } else {
                        self.parent().find('.sp3').hide();
                        emailRegisterCheck.email = true;
                    }
                });
            } else {
                $(this).parent().find('.sp3').text('邮箱不合法');
                $(this).parent().find('.sp3').show();
                emailRegisterCheck.email = false;
            }
        });

        $('.ul1 .password,.ul2 .password').blur(function () {
            var self = $(this);
            if (self.val() == '') {
                self.parent().find('.sp3').text('密码不能为空');
                self.parent().find('.sp3').show();
            } else {
                var pattern = /[a-zA-Z0-9!@#\*\^\$%\(\)-+=_&]{6,20}$/;
                if (!pattern.test(self.val())) {
                    self.parent().find('.sp3').text('密码不符合格式');
                    self.parent().find('.sp3').show();
                } else {
                    self.parent().find('.sp3').hide();
                }
            }
        });

        $('.ul1 .r-password').blur(function () {
            var self = $(this);
            if ($('.ul1 .r-password').val() == $('.ul1 .password').val()) {
                self.parent().find('.sp3').hide();
                phoneRegisterCheck.password = true;
            } else {
                self.parent().find('.sp3').text('两次输入的密码不一致');
                self.parent().find('.sp3').show();
                phoneRegisterCheck.password = false;
            }
        });

        $('.ul2 .r-password').blur(function () {
            var self = $(this);
            var pattern = /[a-zA-Z0-9!@#\*\^\$%\(\)-+=_&]{6,20}$/;
            if (!pattern.test($('.ul2 .password').val())) {
                self.parent().find('.sp3').text('密码不符合格式');
                self.parent().find('.sp3').show();
                return;
            }

            if ($('.ul2 .password').val() == $('.ul2 .r-password').val()) {
                self.parent().find('.sp3').hide();
                emailRegisterCheck.password = true;
            } else {
                self.parent().find('.sp3').text('两次输入的密码不一致');
                self.parent().find('.sp3').show();
            }
        });

        $('.ul1 .user-name').blur(function () {
            var self = $(this);
            if (self.val() != '') {
                self.parent().find('.sp3').hide();
                var requestParm = {userName: self.val()};
                common.getData('/account/userNameCheck', requestParm, function (resp) {
                    if (resp.message) {
                        self.parent().find('.sp3').text('用户名被占用了');
                        self.parent().find('.sp3').show();
                        phoneRegisterCheck.userName = false;
                    } else {
                        phoneRegisterCheck.userName = true;
                    }
                });
            } else {
                self.parent().find('.sp3').text('请输入用户名');
                self.parent().find('.sp3').show();
                phoneRegisterCheck.userName = false;
            }
        });

        $('.ul2 .user-name').blur(function () {
            var self = $(this);
            if (self.val() != '') {
                self.parent().find('.sp3').hide();
                var requestParm = {userName: self.val()};
                common.getData('/account/userNameCheck', requestParm, function (resp) {
                    if (resp.message) {
                        self.parent().find('.sp3').text('用户名被占用了');
                        self.parent().find('.sp3').show();
                        emailRegisterCheck.userName = false;
                    } else {
                        emailRegisterCheck.userName = true;
                    }
                });
            } else {
                self.parent().find('.sp3').text('请输入用户名');
                self.parent().find('.sp3').show();
                emailRegisterCheck.userName = false;
            }
        });

        $('#verifyCode').blur(function () {
            var self = $(this);
            if (self.val() == '') {
                self.parent().find('.sp3').text('请填写验证码');
                self.parent().find('.sp3').show();
                phoneRegisterCheck.identifyCode = false;
            } else {
                self.parent().find('.sp3').hide();
                phoneRegisterCheck.identifyCode = true;
            }
        });

        $('.sendYZM').click(function () {
            if (phoneRegisterCheck.userName && phoneRegisterCheck.password && phoneRegisterCheck.phone) {
                $(this).parent().find('.sp3').hide();
                alert('发送验证码');
                sendVerifyCode($('#phone').val(), $('#verifyCode').val());
            } else {
                $(this).parent().find('.sp3').show();
            }
        });

        $('.ul1 button').click(function () {
            if (!$('.ul1 .argument').is(':checked')) {
                $(this).parent().find('.sp3').text('未勾选社区协议');
                $(this).parent().find('.sp3').show();
                return;
            } else {
                $(this).parent().find('.sp3').hide();
            }

            if (phoneRegisterCheck.userName && phoneRegisterCheck.password && phoneRegisterCheck.phone && phoneRegisterCheck.identifyCode) {
                alert("开始注册");
                var code = $.trim($('#code').val());
                var userName = $.trim($('.ul1 .user-name').val());
                var password = $.trim($('.ul1 .password').val());
                var phone = $.trim($('#phone').val());
                registerUser(code, userName, password, phone, '');
            } else {
                $(this).parent().find('.sp3').text('输入不完整');
                $(this).parent().find('.sp3').show();
            }
        });

        $('.ul2 button').click(function () {
            if (!$('.ul2 .argument').is(':checked')) {
                $(this).parent().find('.sp3').text('未勾选社区协议');
                $(this).parent().find('.sp3').show();
                return;
            } else {
                $(this).parent().find('.sp3').hide();
            }

            if (emailRegisterCheck.email && emailRegisterCheck.password && emailRegisterCheck.userName) {

                $(this).parent().find('.sp3').hide();
                var userName = $.trim($('.ul2 .user-name').val());
                var password = $.trim($('.ul2 .password').val());
                var email = $.trim($('.email-input').val());
                registerUser('', userName, password, '', email);
            } else {
                $(this).parent().find('.sp3').text('输入不完整');
                $(this).parent().find('.sp3').show();
            }
        });

        $('body').on('click', '#imgObj', function () {
            $(this).attr('src', '/verify/verifyCode.do?time=' + new Date());
        });


    });

    function sendVerifyCode(phone, verifyCode) {
        common.getData("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {
            if (resp.code == '200') {

            } else {
                alert(resp.message);
            }

        })
    }

    function registerUser(code, userName, password, phone, email) {
        var requestData = {};
        requestData.cacheKeyId = cacheKeyId;
        requestData.code = code;
        requestData.email = email;
        requestData.userName = userName;
        requestData.passWord = password;
        requestData.phoneNumber = phone;
        common.getPostData('/mall/users.do', requestData, function (resp) {
            if (resp.code == 200) {
                if (resp.type == 1) {
                    $('#successDiv').show();
                    setTimeout(function () {
                        window.location.href = '/mall/entrance.do';
                    }, 8000);
                } else if (resp.type == 2) {
                    var message = resp.message;
                    var item = message.split("$");
                    location.href = '/mall/users/sendEmail.do?email=' + item[0] + '&emailValidateCode=' + item[1];
                }
            } else {
                alert(resp.message);
            }

        });
    }

    module.exports = register;
});