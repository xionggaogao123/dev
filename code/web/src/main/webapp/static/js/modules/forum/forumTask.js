/**
 * Created by admin on 2016/6/12.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');

    var post = $('#postValue').val();
    var welfare = $('#welfareValue').val();
    var signIn = $('#signInValue').val();
    var imf = $('#imf').val();
    $(document).ready(function () {

        $('.mission-notice p i').click(function () {
            $('#closedDialog').hide();
        });

        $('#gtoPost').click(function () {
            location.href = "/forum/index.do";
        });

        if (signIn == "true") {
            $('#sign').addClass('btn-pass').removeClass('btn-gray');
            $('#span1').html('任务完成');
        } else {
            $('body').on('click', '#sign', function () {
                $('#closedDialog').show();
            })
        }

        if (post == "true") {
            $('#post').addClass('btn-pass').removeClass('btn-gray');
            $('#span2').html('任务完成');
        } else {
            $('body').on('click', '#post', function () {
                $('#closedDialog').show();
            })
        }

        if (imf == "true") {
            $('#head').addClass('btn-pass').removeClass('btn-gray');
            $('#span3').html('任务完成');
        } else {
            $('body').on('click', '#head', function () {
                location.href = "/forum/userCenter/user.do";
            })
        }

        if (welfare == "true") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('任务完成');
        } else if (welfare == "welfare") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('先签到！');
        } else if (welfare == "welC") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('今天不能抽奖！');
        } else {

            $('body').on('click', '#welfare', function () {
                //window.open('/forum/userCenter/award.do');
                $('#yu').show();
                $('.bg').show();
            });
        }
    })
});