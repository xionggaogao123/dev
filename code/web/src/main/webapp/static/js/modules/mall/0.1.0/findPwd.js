/**
 * Created by admin on 2016/5/19.
 */
define(function (require, exports, module) {
    var findPwd = {};
    var common = require('common');

    var un = "";
    var phone = "";
    var cacheKeyId = "";


    findPwd.init = function () {
        changeCodeImg();
    }

    $(document).ready(function () {

        $('.account-HQ').click(function () {
            if (getVerificationCode()) {
                settime($('.account-HQ'));
            }
        })

        $('body').on('click', '#imgCode,#imgCode1,#imgCode2 ', function () {
            changeCodeImg();
        })

        $('body').on('blur', '#newPwdAgain', function () {
            checkPwd();
        })

        $('body').on('blur', '#newPwd', function () {
            if ($("#newPwdAgain").val() != "") {
                checkPwd();
            }
        })


        $('#firstCheck').click(function () {
            firstStepCheck();
        })

        $('#secondCheck').click(function () {
            secondStepCheck();
        })

        $('#thirdCheck').click(function () {
            thirdStepCheck();
        })

        $('.verify-HQ').click(function () {
            if (getVerificationCode()) {
                settime($('.verify-HQ'));
            }
        })

        $('#btn').click(function () {
            window.open('/mall/index.do');
        })

    })

    function firstStepCheck() {
        var url = "/mall/findUser/userCheck.do";
        common.getData(url, {"username": $("#userName").val(), "vCode": $("#vCode").val()}, function (rep) {
            if (rep.code == "200") {
                un = $("#userName").val();
                uty = rep.message;
                $("#stepFind_1").hide();
                $("#stepFind_2").show();
                $("#user_Name").text(un);
                phone = rep.phone;
                $("#phone").text(rep.phone);
                changeCodeImg();
            }
            else {
                alert(rep.message);
            }
        });
    }


    function secondStepCheck() {
        var code = $("#checkCode").val();
        var requestData = {};
        requestData.userName = un;
        requestData.cacheKeyId = cacheKeyId;
        requestData.code = code;
        requestData.phoneNumber = $("#phone").text();
        var url = "/mall/findUser/validate.do";
        common.getPostData(url, requestData, function (rep) {
            if (rep.code == "200") {
                $("#stepFind_2").hide();
                changeCodeImg();
                $("#stepFind_3").show();
            }
            else {
                alert(rep.message);
            }
        });
    }


    function thirdStepCheck() {
        var newPwd = $("#newPwd").val();

        var newPwdAgain = $("#newPwdAgain").val();

        var vodeCheck = $("#vodeCheck").val();

        if (newPwd == "") {
            $('#abl').show();
            return;
        }
        if (newPwdAgain == "") {
            $('#abll').show();
            return;
        }
        if (vodeCheck == "") {
            $('#vImage').show();
            return;
        }
        var data = {
            "username": un,
            "pwd": $("#newPwd").val(),
            "pwdAgain": $("#newPwdAgain").val(),
            "vCode": $("#vodeCheck").val()
        };

        var url = "/mall/findUser/resetPwd.do";
        common.getData(url, data, function (rep) {
            if (rep.code == "200") {
                $("#stepFind_3").hide();
                $("#stepFind_4").show();
            }
            else {
                alert(rep.message);
            }
        });
    }


    function getVerificationCode() {
        phone = $('#phone').text();
        var verifyCode = $('#vCode1').val();
        if (verifyCode == '') {
            alert("验证码不能为空");
            return false;
        }

        var flag = false;
        common.getData("/mall/users/messages.do", {mobile: phone, verifyCode: verifyCode}, function (resp) {

            if (resp.code == "200") {
                cacheKeyId = resp.cacheKeyId;
                flag = true;
            }
            else {
                flag = false;
                alert(resp.message);
            }
        })
        return flag;
    }

    var countdown = 60;

    function settime(obj) {
        if (countdown == 0) {
            obj.attr("disabled", false);
            obj.text("免费获取验证码");
            countdown = 60;
            return;
        } else {
            obj.attr("disabled", true);
            obj.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function () {
                settime(obj)
            }
            , 1000)
    }

    function checkPwd() {
        var pwd = $("#newPwd").val();
        var pwdAgain = $("#newPwdAgain").val();

        if (pwd != pwdAgain) {
            alert("两次输入不一致");
            return;
        }
    }

    function changeCodeImg() {
        var src = "/verify/verifyCode.do";
        var src = chgUrl(src);
        var imgSrc = $("#imgCode");
        imgSrc.attr("src", src);

        var imgSrc1 = $("#imgCode1");
        imgSrc1.attr("src", src);
        $("#imgCode2").attr("src", src);
    }

    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        url = url.substring(0, 21);
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }

    module.exports = findPwd;
});