/**
 * Created by admin on 2016/12/2.
 */
/**
 * Created by jerry on 2016/10/27.
 *
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    var hotCommunity = {};
    hotCommunity.init = function () {
        getHotCommunity();
    };

    $(document).ready(function () {

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('body').on('mouseleave','.ul-my-com li',function(){
            $('.ul-my-com li .com-hover-card .sp3').removeClass('sp33').addClass('sp-short');
            $('.ul-my-com li .com-hover-card .sp3 em').show();
            $(this).children('.com-hover-card').hide();
        });
        $('body').on('mouseover','.ul-my-com li',function(){
            $(this).children('.com-hover-card').show();
        });

        $('body').on('click','.join',function () {
            var communityId=$(this).attr('cid');
            joinCommunity(communityId);
        })


    });


    function joinCommunity(communityId){
        common.getData('/community/join.do',{communityId:communityId},function(resp){
            if(resp.code=="200"){
                getHotCommunity(page);
            }else{
                alert(resp.message);
            }
        })
    }


    function getHotCommunity() {
        common.getData("/community/hotCommunitys.do", {}, function (result) {
            if (result.code = "200") {
                template('#communityTmpl', '#hotCommunity', result.message);
            } else {
                alert(result.message);
            }
        })
    }


    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    function hx_update() {

        $.ajax({
            url:'/group/offlineMsgCount.do',
            success: function(resp){
                var hx_notice = $('.hx-notice span');
                var offCount = resp.message.offlineCount;

                if(offCount > 0) {
                    $('#hx-icon').removeClass("sp2");
                    $('#hx-icon').addClass('sp1');
                } else {
                    $('#hx-icon').addClass('sp2');
                }
                $('#hx-msg-count').text('您有' + offCount + '条未读消息');
            }
        });
    }

    module.exports = hotCommunity;
});