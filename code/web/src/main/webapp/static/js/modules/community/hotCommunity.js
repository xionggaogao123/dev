/**
 * Created by admin on 2016/12/2.
 */
/**
 * Created by jerry on 2016/10/27.
 *
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var hotCommunity = {};
    var page=1;
    hotCommunity.init = function () {
        getHotCommunity(page,"");
    };

    $(document).ready(function () {

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('body').on('click', '#cancel', function () {
            $('.wind-com-edit').fadeOut();
            $('.bg').fadeOut();
        });


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

        $('body').on('click','#lastId',function () {
            var lastId=$(this).data('lastId');
            getHotCommunity(2,lastId);
        })


    });


    function joinCommunity(communityId){
        common.getData('/community/join.do',{communityId:communityId},function(resp){
            if(resp.code=="200"){
                getHotCommunity(page,"");
            }else{
                alert(resp.message);
            }
        })
    }


    function getHotCommunity(page,lastId) {
        common.getData("/community/hotCommunitys.do", {page:page,lastId:lastId}, function (result) {
            if (result.code = "200") {
                $('#lastId').data('lastId',result.message[result.message.length-1].id);
                if(page==1){
                    template('#communityTmpl', '#hotCommunity', result.message);
                }else{
                    loadData(result.message);
                }
            } else {
                alert(result.message);
            }
        })
    }

    function loadData(list){
        for(var i in list){
            var str="<li><a><img src=\""+list[i].logo+"\"></a> <p>"+list[i].name+"</p> " +
            "<div class=\"com-hover-card clearfix\"> " +
            "<div class=\"clearfix\"> " +
            "<img src=\""+list[i].logo+"\"><span></span> " +
            "<span class=\"sp1\">"+list[i].name+"</span> " +
            "<span class=\"sp2\">社区ID："+list[i].searchId+"</span> " +
            "<span class=\"sp-short sp3\">社区简介：<em>...[详细]</em>"+list[i].desc+"</span></div><p>" +
            "<button class=\"join\" cid=\""+list[i].id+"\">+加入社区</button></p>" +
            "<div class=\"train-f\"> " +
            "<div class=\"down-train\"></div>" +
            "</div> </div> </li>";
            $('#hotCommunity').append(str);
        }
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