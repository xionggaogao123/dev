define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    require('pagination');
    require('jquery');
    require('birthday');
    var common = require('common');
    var accountSafe = {};

    var basicData = {
        tags: [],
        times: []
    };
    accountSafe.init = function () {

        // $.ms_DatePicker();
        getInfo();
        getAllMateData();
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

        $('body').on('click','.bq-tags span,.bq-times span',function () {
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

        $('body').on('click','.btn-no',function () {
            getAllMateData();
        });

        $('body').on('click','.btn-save',function () {
            var tags = '';
            $('.bq-tags span.oracur2').each(function () {
                var code = $(this).attr('value');
                tags += code + ',';
            });

            var times = '';
            $('.bq-times span.oracur2').each(function () {
                var code = $(this).attr('value');
                times += code + ',';
            });
            common.getData('/mate/updateUserTagAndTime.do', {tags: tags,times: times}, function (resp) {
                if(resp.code == '200') {
                    alert('修改成功');
                } else {
                    alert(resp.message);
                }
            });
        });
    });

    function getMyTags() {
        common.getData('/mate/getUserMateData.do', {}, function (resp) {
            alert(JSON.stringify(resp));
            basicData.tags = resp.message.tags;
            basicData.times = resp.message.times;
            update(basicData);
        });
    }

    function update(data) {
        alert(JSON.stringify(data));
        for(var i = 0;i< data.tags.length; i++ ) {
            $('.bq-tags span').each(function () {
                var code = $(this).attr('value');
                if(code == data.tags[i].code) {
                    $(this).addClass('oracur2');
                }
            });
        }

        for(var i=0;i<data.times.length;i++) {
            $('.bq-times span').each(function () {
                var code = $(this).attr('value');
                if(code == data.times[i].code) {
                    $(this).addClass('oracur2');
                }
            });
        }
    }

    function getAllMateData() {
        common.getData('/mate/sortType.do', {}, function (resp) {
            alert(JSON.stringify(resp));
            $('.bq-tags').empty();
            $('.bq-times').empty();
            for(var i=0;i<resp.message.tags.length;i++) {
                $('.bq-tags').append('<span value="'+ resp.message.tags[i].code +'">'+ resp.message.tags[i].data+'</span>');
            }

            for(var i=0;i< resp.message.times.length;i++) {
                $('.bq-times').append('<span value="'+ resp.message.times[i].code +'">'+ resp.message.times[i].data+'</span>');
            }
            getMyTags();
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