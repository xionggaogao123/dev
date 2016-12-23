define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    require('pagination');
    require('jquery');
    var common = require('common');
    var accountSafe = {};

    var basicData = {
        tags: [],
        times: []
    };
    var bind = 1;

    var edit_pass_check = {
        password: false,
        n_password: false,
        n_r_password: false
    };

    var edit_phone_check = {
        phone: false,
        code: false
    };

    var thirdBind = {
        qq: false,
        wechat: false
    };

    accountSafe.init = function () {

        getInfo();
        getAllMateData();
        getThirdInfo();
    };

    var cacheKeyId = '';
    var i = 0;

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

            $('.edit-pass .password').val('');
            $('.edit-pass .n-password').val('');
            $('.edit-pass .n-r-password').val('');

            $('.wind-email1 .password').val('');
            $('.wind-email2 input.email-input').val('');

            $('.wind-phone input.code').val('');
            $('.wind-phone input.phone').val('');

            $('.windd .sp3').hide();

            edit_pass_check.password = false;
            edit_pass_check.n_password = false;
            edit_pass_check.n_r_password = false;
        });


        $('.btn-xg-email').click(function () {
            $('.wind-email1').fadeIn();
            $('.bg').fadeIn();
        });

        var body = $('body');

        $('ul.set-left-l li').click(function () {
            var value = $(this).index() + 1;

            for (var i = 1; i <= 4; i++) {
                if (i == value) {
                    $('.set-container .right-' + i).show();
                    continue;
                }
                $('.set-container .right-' + i).hide();
            }

            $('ul.set-left-l li').each(function () {
                $(this).removeClass('li444');
            });

            $(this).addClass('li444');
        });

        body.on('click', '.bq-tags span', function () {
            if ($(this).hasClass('oracur2')) {
                $(this).removeClass('oracur2');
            } else {

                if ($('.bq-tags span.oracur2').length >= 6) {
                    alert("标签不能多于6个");
                    return;
                }
                $(this).addClass('oracur2');
            }
        });

        body.on('click', '.bq-times span', function () {
            if ($(this).hasClass('oracur2')) {
                $(this).removeClass('oracur2');
            } else {
                $(this).addClass('oracur2');
            }
        });

        body.on('click', '.edit-img', function () {
            var random = Math.random();
            window.open('/personal/avatarpage.do?uid=' + random + '&uploadtype=head', '图片上传', 'height=253,width=450,top=300,left=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no');
        });

        body.on('click', '.btn-no', function () {
            getAllMateData();
        });

        body.on('click', '.btn-save', function () {
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
            common.getData('/mate/updateUserTagAndTime.do', {tags: tags, times: times}, function (resp) {
                if (resp.code == '200') {
                    alert('修改成功');
                } else {
                    alert(resp.message);
                }
            });
        });

        body.on('click', '.ul-infor button', function () {
            var requestData = {};
            requestData.nickName = $('.nickname').val();

            if (requestData.nickName.length > 8) {
                alert("昵称最多8个字符");
                return;
            }
            requestData.sex = $('input[type="radio"][name="sex"]:checked').val();
            requestData.year = $('#sel_year option:selected').val();
            requestData.month = $('#sel_month option:selected').val();
            requestData.day = $('#sel_day option:selected').val();
            common.getData('/account/updateNickSexAge.do', requestData, function (resp) {
                if (resp.code == '200') {
                    alert('修改成功');
                } else {
                    alert(resp.message);
                }
            });
        });

        body.on('blur', '.edit-pass .password', function () {
            var self = $(this);
            common.getData('/account/checkUserPassword.do', {password: self.val()}, function (resp) {
                if (resp.code == '200') {
                    $('.verify-pass').hide();
                    edit_pass_check.password = true;
                } else {
                    $('.verify-pass').show();
                    edit_pass_check.password = false;
                }
            });
        });

        body.on('blur', '.edit-pass .n-password', function () {
            var self = $(this);
            var pattern = /[a-zA-Z0-9!@#\*\^\$%\(\)-+=_&]{6,20}$/;
            if (!pattern.test(self.val())) {
                $('.verify-pass-n').show();
                edit_pass_check.n_password = false;
            } else {
                $('.verify-pass-n').hide();
                edit_pass_check.n_password = true;
            }
        });

        body.on('blur', '.edit-pass .n-r-password', function () {
            if ($('.edit-pass .n-password').val() != $('.edit-pass .n-r-password').val()) {
                $('.verify-pass-r').show();
                edit_pass_check.n_r_password = false;
            } else {
                $('.verify-pass-r').hide();
                edit_pass_check.n_r_password = true;
            }
        });

        body.on('click', '.edit-pass .p-btn-ok', function () {
            if (edit_pass_check.password && edit_pass_check.n_password && edit_pass_check.n_r_password) {
                var password = $('.edit-pass input.n-password').val();
                common.getData('/account/changeUserPassword.do', {password: password}, function (resp) {
                    if (resp.code == '200') {
                        alert("修改成功");
                        $('.edit-pass').fadeOut();
                        $('.bg').fadeOut();

                        $('.edit-pass .password').val('');
                        $('.edit-pass .n-password').val('');
                        $('.edit-pass .n-r-password').val('');
                        edit_pass_check.password = false;
                        edit_pass_check.n_password = false;
                        edit_pass_check.n_r_password = false;
                        getInfo();
                    } else {
                        alert(resp.message);
                    }
                });
            } else {
                alert("未填写正确");
            }
        });

        body.on('click', '.wind-email1 p.p-btn-ok', function () {
            var password = $('.wind-email1 .password').val();
            common.getData('/account/checkUserPassword.do', {password: password}, function (resp) {
                if (resp.code == '200') {
                    $('.windd').fadeOut();
                    $('.wind-email2').fadeIn();
                    $('.wind-email1 span.sp3').hide();
                    $('.wind-email1 .password').val('');
                } else {
                    $('.wind-email1 span.sp3').show();
                }
            });
        });


        body.on('click', '.wind-email2 p.p-btn-ok', function () {

            var email_tip = $('.wind-email2 .sp3');
            var email = $('.wind-email2 input.email-input').val();
            var pattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if (!pattern.test(email)) {
                email_tip.text('邮箱格式不正确');
                email_tip.show();
                return;
            } else {
                email_tip.hide();
            }
            common.getData('/account/changeUserEmail.do', {email: email}, function (resp) {
                if (resp.code == '200') {
                    $('.windd').fadeOut();
                    $('.bg').fadeOut();
                    $('.wind-email2').fadeOut();
                    alert("修改成功");
                    getInfo();
                    $('.wind-email2 input.email-input').val('')
                } else {
                    alert(resp.message);
                }
            });
        });

        body.on('blur', '.wind-phone .phone', function () {
            var self = $(this);
            var pattern = /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            if (pattern.test(self.val())) {
                common.getData('/account/checkPhoneCanUse.do', {mobile: self.val()}, function (resp) {
                    if (resp.code == '200') {
                        self.parent().find('.phone-tip').hide();
                        edit_phone_check.phone = true;
                    } else {
                        self.parent().find('.phone-tip').text(resp.message);
                        self.parent().find('.phone-tip').show();
                        edit_phone_check.phone = false;
                    }
                });
            } else {
                self.parent().find('.phone-tip').text('手机号不合法');
                self.parent().find('.phone-tip').show();
                edit_phone_check.phone = false;
            }
        });

        body.on('click', '#sendText', function () {
            var mobile = $('.wind-phone .phone').val();
            if (!edit_phone_check.phone) {
                return;
            }
            common.getData('/mall/users/textMessags.do', {mobile: mobile}, function (resp) {
                if (resp.code == '200') {
                    cacheKeyId = resp.cacheKeyId;
                    edit_phone_check.code = true;
                } else {
                    edit_phone_check.code = false;
                }
            });
        });

        body.on('click', '.wind-phone p.p-btn-ok', function () {
            var code = $('.wind-phone input.code').val();
            var mobile = $('.wind-phone input.phone').val();
            if (edit_phone_check.phone && edit_phone_check.code) {
                var requestData = {
                    mobile: mobile,
                    code: code,
                    cacheKeyId: cacheKeyId
                };
                common.getData('/account/changeUserPhone.do', requestData, function (resp) {
                    alert(JSON.stringify(resp));
                    if (resp.code == '200') {
                        alert(resp.message);
                        $('.windd').fadeOut();
                        $('.bg').fadeOut();
                        $('.wind-phone input.code').val('');
                        $('.wind-phone input.phone').val('');
                        getInfo();
                    } else {
                        alert(resp.message);
                    }
                });
            } else {
                alert("数据填写不正确");
            }
        });

        body.on('click', '.third-qq button', function () {

            if (thirdBind.qq) {
                $('#isRemoveBind').fadeIn();
                $('.bg').fadeIn();
                bind = 2;
            } else {
                window.open('/account/qqBind.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
            }
        });

        body.on('click', '.third-wechat button', function () {
            if (thirdBind.wechat) {
                $('#isRemoveBind').fadeIn();
                $('.bg').fadeIn();
                bind = 1;
            } else {
                window.open('/account/wechatBind.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
            }

        });

        body.on('click', '#isRemoveBind .p-btn-ok', function () {
            removeThirdBind(bind);
        });
    });

    function getMyTags() {
        common.getData('/mate/getUserMateData.do', {}, function (resp) {
            basicData.tags = resp.message.tags;
            basicData.times = resp.message.times;
            update(basicData);
        });
    }

    function update(data) {
        for (i = 0; i < data.tags.length; i++) {
            $('.bq-tags span').each(function () {
                var code = $(this).attr('value');
                if (code == data.tags[i].code) {
                    $(this).addClass('oracur2');
                }
            });
        }

        for (i = 0; i < data.times.length; i++) {
            $('.bq-times span').each(function () {
                var code = $(this).attr('value');
                if (code == data.times[i].code) {
                    $(this).addClass('oracur2');
                }
            });
        }
    }

    function getAllMateData() {
        common.getData('/mate/sortType.do', {}, function (resp) {
            $('.bq-tags').empty();
            $('.bq-times').empty();
            for (i = 0; i < resp.message.tags.length; i++) {
                $('.bq-tags').append('<span value="' + resp.message.tags[i].code + '">' + resp.message.tags[i].data + '</span>');
            }

            for (i = 0; i < resp.message.times.length; i++) {
                $('.bq-times').append('<span value="' + resp.message.times[i].code + '">' + resp.message.times[i].data + '</span>');
            }
            getMyTags();
        });
    }

    function getInfo() {
        common.getData('/forum/userCenter/userInfo.do', {}, function (resp) {
            $('.ul-infor .username').text(resp.name);
            $('.ul-infor .nickname').val(resp.nickName);
            $('.avatar').attr('src', resp.avatar);

            $('.sex-div input[name="sex"]').each(function () {
                var value = $(this).attr('value');
                if (value == resp.sex) {
                    $(this).attr('checked', 0);
                }
            });

            if (resp.phone == null || resp.phone == '') {
                $('#verify-phone').append('<em>未设置</em>');
            } else {
                $('#verify-phone').text(resp.phone);
            }

            if (resp.email == null || resp.email == '') {
                $('#verify-email').append('<em>未设置</em>');
            } else {
                $('#verify-email').text(resp.email);
            }

            $('#sel_year').attr('rel', resp.year);
            $('#sel_month').attr('rel', resp.month);
            $('#sel_day').attr('rel', resp.day);

            $.ms_DatePicker();
        });
    }

    function removeThirdBind(type) {
        common.getData('/account/removeThirdBind', {type: type}, function (resp) {
            if (resp.code == '200') {
                alert("解绑成功");
                window.location.reload();
            } else {
                alert(resp.message);
            }
        });
    }

    function getThirdInfo() {

        common.getData('/account/thirdLoginInfo', {}, function (resp) {

            thirdBind.qq = resp.message.isBindQQ;
            thirdBind.wechat = resp.message.isBindWechat;

            if (resp.message.isBindQQ) {
                $('.third-qq span').removeClass('sp1');
                $('.third-qq span').addClass('sp2');
                $('.third-qq button').removeClass('btn2');
                $('.third-qq button').addClass('btn1');
                $('.third-qq button').text('解除关联');
            } else {
                $('.third-qq span').removeClass('sp2');
                $('.third-qq span').addClass('sp1');
                $('.third-qq button').removeClass('btn1');
                $('.third-qq button').addClass('btn2');
                $('.third-qq button').text('立即关联');
            }

            if (resp.message.isBindWechat) {
                $('.third-wechat span').removeClass('sp1');
                $('.third-wechat span').addClass('sp2');
                $('.third-wechat button').removeClass('btn2');
                $('.third-wechat button').addClass('btn1');
                $('.third-wechat button').text('解除关联');
            } else {
                $('.third-wechat span').removeClass('sp2');
                $('.third-wechat span').addClass('sp1');
                $('.third-third button').removeClass('btn1');
                $('.third-wechat button').addClass('btn2');
                $('.third-wechat button').text('立即关联');
            }
        });

    }

    (function ($) {
        $.extend({
            ms_DatePicker: function (options) {
                var defaults = {
                    YearSelector: "#sel_year",
                    MonthSelector: "#sel_month",
                    DaySelector: "#sel_day",
                    FirstText: "--",
                    FirstValue: 0
                };
                var opts = $.extend({}, defaults, options);
                var $YearSelector = $(opts.YearSelector);
                var $MonthSelector = $(opts.MonthSelector);
                var $DaySelector = $(opts.DaySelector);
                var FirstText = opts.FirstText;
                var FirstValue = opts.FirstValue;

                // 初始化
                var str = "<option value=\"" + FirstValue + "\">" + FirstText + "</option>";
                $YearSelector.html(str);
                $MonthSelector.html(str);
                $DaySelector.html(str);

                // 年份列表
                var yearNow = new Date().getFullYear();
                var yearSel = $YearSelector.attr("rel");
                for (var i = yearNow; i >= 1900; i--) {
                    var sed = yearSel == i ? "selected" : "";
                    var yearStr = "<option value=\"" + i + "\" " + sed + ">" + i + "</option>";
                    $YearSelector.append(yearStr);
                }

                // 月份列表
                var monthSel = $MonthSelector.attr("rel");
                for (var i = 1; i <= 12; i++) {
                    var sed = monthSel == i ? "selected" : "";
                    var monthStr = "<option value=\"" + i + "\" " + sed + ">" + i + "</option>";
                    $MonthSelector.append(monthStr);
                }

                // 日列表(仅当选择了年月)
                function BuildDay() {
                    if ($YearSelector.val() == 0 || $MonthSelector.val() == 0) {
                        // 未选择年份或者月份
                        $DaySelector.html(str);
                    } else {
                        $DaySelector.html(str);
                        var year = parseInt($YearSelector.val());
                        var month = parseInt($MonthSelector.val());
                        var dayCount = 0;
                        switch (month) {
                            case 1:
                            case 3:
                            case 5:
                            case 7:
                            case 8:
                            case 10:
                            case 12:
                                dayCount = 31;
                                break;
                            case 4:
                            case 6:
                            case 9:
                            case 11:
                                dayCount = 30;
                                break;
                            case 2:
                                dayCount = 28;
                                if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
                                    dayCount = 29;
                                }
                                break;
                            default:
                                break;
                        }

                        var daySel = $DaySelector.attr("rel");
                        for (var i = 1; i <= dayCount; i++) {
                            var sed = daySel == i ? "selected" : "";
                            var dayStr = "<option value=\"" + i + "\" " + sed + ">" + i + "</option>";
                            $DaySelector.append(dayStr);
                        }
                    }
                }

                $MonthSelector.change(function () {
                    BuildDay();
                });
                $YearSelector.change(function () {
                    BuildDay();
                });
                if ($DaySelector.attr("rel") != "") {
                    BuildDay();
                }
            }
        });
    })(jQuery);
    module.exports = accountSafe;
});