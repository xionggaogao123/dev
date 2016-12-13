define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    require('pagination');
    require('jquery');
    require('birthday');
    var common = require('common');
    var accountSafe = {};
    accountSafe.init = function () {

        $.ms_DatePicker();
    };

    $(function () {
        getInfo();

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

        $('body').on('click', '.edit-img', function () {
            var random = Math.random();
            window.open('/personal/avatarpage.do?uid=' + random + '&uploadtype=head', '图片上传', 'height=253,width=450,top=300,left=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no');
        });
    });

    function getTags(userId) {
        var requestParm = {
            userId: userId
        };
        common.getData('/mate/getUserMateData.do', requestParm, function (resp) {

            alert(JSON.stringify(resp));
        });
    }

    function getUserInfo() {
        common.getData('/forum/loginInfo.do', {}, function (resp) {
            alert(JSON.stringify(resp));
        });
    }

    function getValidateData() {
        common.getData('/account/validateData.do', {}, function (resp) {
            alert(JSON.stringify(resp));
        });
    }

    function getInfo() {
        common.getData('/forum/userCenter/userInfo.do', {}, function (resp) {
            $('.ul-infor .username').text(resp.name);
            $('.ul-infor .nickname').val(resp.nickName);
            $('.ul-infor .avatar').attr('src',resp.avatar);
            $('.sex-div input[name="sex"]').each(function () {
                var value = $(this).attr('value');
                if(value == resp.sex) {
                    $(this).attr('checked',0);
                }
            });

        });
    }

    module.exports = accountSafe;
});