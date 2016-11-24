/**
 * Created by admin on 2016/8/18.
 */
/**
 * Created by admin on 2016/8/17.
 */
/**
 * Created by admin on 2016/6/3.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    require('tou');
    var imageApp = {};

    var replyId = $('#replyId').val();

    //分享地址

    imageApp.init = function () {
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

            TouchSlide({
                slideCell: "#slideBoxindexb",
                autoPlay: false,
                mainCell: ".bd ul",
                effect: "leftLoop"
            });
            //var index=$('#index').val();
            //$("#hsCont li").eq(index).show();
        });
    }


    module.exports = imageApp;
});