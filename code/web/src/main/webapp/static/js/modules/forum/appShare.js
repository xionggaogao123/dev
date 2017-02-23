/**
 * Created by admin on 2016/8/17.
 */
/**
 * Created by admin on 2016/6/3.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var appShare = {};

    var postId = $('#postId').val();
    var replyId = $('#replyId').val();

    //分享地址
    appShare.init = function () {
        getPost();
        getFReply();
    }
    $(document).ready(function () {


    })
    function getFReply() {
        var requestData = {};
        requestData.replyId = replyId;
        Common.getData('/forum/getReplyId.do', requestData, function (result) {
            Common.render({
                tmpl: '#hsContTml',
                data: result,
                context: '#hsCont',
                overwrite: 1
            });
            $('#dianzan,.clickzan').click(function () {
                var requestData = {};
                Common.getData("/forum/loginInfo.do?date=" + new Date(), requestData, function (result) {
                    if (result.login == true) {
                        commitDianZan(result.userId);
                    } else {
                        // var url = $(location).attr('href');
                        // var encodedUrl = encodeURI(encodeURI(url));
                        location.href = "/wap/third.do";
                    }
                });
            });
            $('.click-download').click(function () {
                location.href = "/wap/download.do";
            });
        });
    }

    function getPost() {
        var requestData = {};
        requestData.postId = postId;
        Common.getData('/forum/fPostDetail.do', requestData, function (result) {
            Common.render({
                tmpl: '#detailTml',
                data: result,
                context: '#postContent',
                overwrite: 1
            });
            $('#count').html(result[0]["replyCount"]);
        });
    }

    function commitDianZan(userId) {
        var requestData = {};
        requestData.userReply = userId;
        requestData.replyId = replyId;
        Common.getData('/forum/updateReplyBtnZan.do', requestData, function (result) {
            if (result.code == 200) {
                $('#dianzan').attr('src', '/wap/images/red_heart.png');
                var p = $('#din').html();
                var txt = parseInt(p) + 1;
                $('#din').html(txt);
                $('#praise').html("(" + txt + ")");
                $('.clickzan .img1').show();
            }
        });
    }

    module.exports = appShare;
});