define(function (require, exports, module) {
    user = {};
    require('jquery');
    require('pagination');
    require('birthday');
    Common = require('common');
    var cacheKeyId = '';
    var countdown = 60;

    user.init = function () {
        init();
        getUserInfo();
    }

    $(document).ready(function () {

        $(".account-TJ").click(function () {
            Validate();
        });
        $('.jf-my').click(function () {
            $('.ehm1').hide();
            $('.ehm2').show();
            $(this).addClass('ebusiness-main-HOV').siblings('.ebusiness-main-left dl span').removeClass('ebusiness-main-HOV');
        });
        $('.ebusiness-info ul li').click(function () {
            $(this).addClass('ebusiness-info-li').siblings('.ebusiness-info ul li').removeClass('ebusiness-info-li');
        });
        $('.jf-li1').click(function () {
            $('.jf1').show();
            $('.jf2').hide();
        });
        $('.jf-li2').click(function () {
            $('.jf2').show();
            $('.jf1').hide();
        });

        $(".account-TJJ").click(function () {

            $(this).parent().parent().parent().hide();
            $(this).parent().parent().parent().next().show();
        });

        $('#verifyImg').click(function () {
            chgUrl();
        });

        //获取手机验证码
        $('.account-HQ').click(function () {
            if (getVerificationCode()) {
                settime($('.account-HQ'));
            }
        })

        $('body').on('blur', '#newrpassword', function () {
            checkPwd();
        });

        $('.new-page-links').jqPaginator({
            totalPages: 10,
            visiblePages: 10,//分多少页
            currentPage: 2,//当前页数
            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
            onPageChange: function (n) { //回调函数
                // alert(n);
            }
        });

    })

    function checkPwd() {
        var pwd = $("#newpassword").val();
        var pwdAgain = $("#newrpassword").val();

        if (pwd != pwdAgain) {
            $("#comparePwd").show();
            return;
        }
    }

    function Validate() {
        var code = $.trim($('.register-YZ').val());
        var requestData = {};
        requestData.userName = $('#name').text();
        requestData.cacheKeyId = cacheKeyId;
        requestData.code = code;
        requestData.phoneNumber = $.trim($('#phone').val());
        Common.getPostData('/mall/userCenter/validateUser.do', requestData, function (resp) {
            if (resp.code == 200) {

                $(".account-TJ").parent().parent().parent().hide();
                $(".account-TJ").parent().parent().parent().next().show();
            } else {
                alert(rep.message);
            }
        })

    }

    function wrong(ele, i) {
        ele.addClass('register-wrong').removeClass('register-right');
        $('#' + ele.attr('id') + i).show().siblings('span').hide();
        return false;
    }

    function right(ele, i) {
        ele.addClass('register-right').removeClass('register-wrong');
        $('#' + ele.attr('id') + i).show().siblings('span').hide();
        return true;
    }


    //设置发送请求时间
    function settime(val) {
        if (countdown == 0) {
            val.attr("disabled", false);
            val.text("点击获取");
            countdown = 60;
            return true;
        } else {
            val.attr("disabled", true);
            val.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function () {
            settime(val)
        }, 1000)
    }


    //获取手机验证码
    function getVerificationCode() {
        if ($('#verifyCode').val() == '') {
            var ele = $('#verifyCode');
            return wrong(ele, 2);
        }
        var phone = $('#phone').val();
        var verifyCode = $('#verifyCode').val();
        chgUrl();
        var flag = false;
        Common.getData("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {
            if (resp.code == '200') {
                cacheKeyId = resp.cacheKeyId;
                $('#YZ1').show();
                flag = true;
            } else {
                flag = false;
                alert(resp.message);
            }

        })
        return flag;
    }

    //修改图片验证码
    function chgUrl() {
        var imgSrc = $("#verifyImg");
        var url = imgSrc.attr("src");

        var timestamp = (new Date()).valueOf();
        url = url.substring(0, 21);
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        imgSrc.attr("src", url);
    }

    function init() {
        $('#accountSafeDiv').hide();
        $('#account-AQ').hide();

        $(function () {//生日插件
            $.ms_DatePicker({
                YearSelector: ".sel_year",
                MonthSelector: ".sel_month",
                DaySelector: ".sel_day"
            });
        });

        $('body').on('click', '#baseInfo', function () {
            $(this).addClass('ebusiness-info-li');
            $('#accountSafe').removeClass('ebusiness-info-li');
            $('#baseInfoDiv').show();
            $('#accountSafeDiv').hide();
            $('#account-AQ').hide();
        });

        $('body').on('click', '#accountSafe', function () {
            $(this).addClass('ebusiness-info-li');
            $('#baseInfo').removeClass('ebusiness-info-li');
            $('#baseInfoDiv').hide();
            $('#accountSafeDiv').show();
            $('#account-AQ').show();
        });

        $('body').on('click', '.btn-edit-head', function () {
            var random = Math.random();
            window.open('/personal/avatarpage.do?uid=' + random + '&uploadtype=head', '图片上传', 'height=253,width=450,top=300,left=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no');
        });

        $('#account').submit(function () {
            return validatePassword();
        });

        $('#changeSub').click(function () {
            var requestData = {};
            requestData.nickName = $('input[name="nickName"]').val();
            if ($('input[name="nickName"]').val() == "") {
                alert("昵称不能为空");
                return;
            }
            var nickName = $.trim($('input[name="nickName"]').val());
            if (nickName.length > 8) {
                alert("昵称长度不能超过8个字");
                return;
            }
            var log = false;
            var reDa = {};
            reDa.nickName = $('input[name="nickName"]').val();
            Common.getData('/forum/userCenter/existUserInfo.do', reDa, function (resp) {
                if (resp) {
                    alert('该昵称已经有了');
                    log = true;
                }
            })
            if (log) {
                return;
            }

            requestData.sex = $('input[name="sex"]:checked').attr('value');
            requestData.year = $('#sel_year option:selected').val();
            requestData.month = $('#sel_month option:selected').val();
            requestData.day = $('#sel_day option:selected').val();
            Common.getPostData('/forum/userCenter/updateUserInfo.do', requestData, function (resp) {
                if (resp.code == 200) {
                    alert(resp.message);
                    location.href = "/forum/userCenter/user.do";
                } else {
                    alert(resp.message);
                }
            })
        });

    }

    function getUserInfo() {
        var url = '/forum/userCenter/userInfo.do';
        Common.getData(url, {}, function (resp) {
            $('#name').text(resp.name);
            $('input[name="nickName"]').val(resp.nickName);
            if (resp.sex == 0) {
                $('input[name="sex"][value="0"]').attr('checked', 'checked');
            } else {
                $('input[name="sex"][value="1"]').attr('checked', 'checked');
            }
            $('.sel_year').attr('rel', resp.year);
            $('.sel_month').attr('rel', resp.month);
            $('.sel_day').attr('rel', resp.day);
            $('#phone').val(resp.phone);
        });
    }


    function validatePassword() {
        if (validateForm({
                loginpassword: {required: true, maxLength: 20},
                newpassword: {required: true, maxLength: 20},
                newrpassword: {required: true, maxLength: 20, confirm: "newpassword"}
            }, {con: $('.account-tb tr:lt(3)').find('td:last'), msg: ['登录密码', '新密码', '确认新密码']})) {
            if (checkPassword()) {
                //发送ajax请求
                $.ajax({
                    type: "POST",
                    url: "/mall/userCenter/modifyPassword.do",
                    data: {
                        password: $('#newpassword').val()
                    },
                    async: false,
                    success: function (data) {
                        alert(data.message);
                        if ($.trim(data.code) == "200") {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                $('#loginpassword').val('');
                $('#newpassword').val('');
                $('#newrpassword').val('');
                return false;
            } else {
                alert("您输入的登陆密码有误，请重新输入！");

                $('#loginpassword').val('');
                $('#newpassword').val('');
                $('#newrpassword').val('');
                return false;
            }
        }


        $('#loginpassword').val('');
        $('#newpassword').val('');
        $('#newrpassword').val('');
        return false;
    }

    //验证用户名密码是否正确
    function checkPassword() {
        var flag = false;
        $.ajax({
            type: "POST",
            url: "/personal/checkpass.do",
            data: {password: $('#loginpassword').val()},
            async: false,
            success: function (data) {
                if ($.trim(data['repeat']) == $.trim('yes')) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
        return flag;
    }

    module.exports = user;
});
