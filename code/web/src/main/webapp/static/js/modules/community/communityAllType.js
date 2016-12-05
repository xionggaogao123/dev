/**
 * Created by admin on 2016/10/31.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common=require('common');
    var communityAllType = {};
    communityAllType.init = function () {
        getMyCommunity();

        //获取该社区详情列表
        getCommunityDetail();
    }

    $(document).ready(function () {

        $('body').on('click','.spread',function () {
            spread($(this));
        })
        $('body').on('click','.collect',function () {
            collect($(this));
        });

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('body').on('click','.login-mk-btn .d2',function () {
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })
    });

    function  spread(obj) {
        obj.closest('p').css('max-height','140px');
        obj.closest('span').html('<em class="spread">[收起全文]</em>').removeClass('spread').addClass('collect');
    }

    function collect(obj) {
        obj.closest('p').css('max-height','60px');
        obj.closest('span').html('...<em class="spread">[展开全文]</em>').removeClass('collect').addClass('spread');
    }


    //获取该社区详情信息
    function getCommunityDetail(){
        var param={};
        param.pageSize=1;
        common.getData('/community/getAllTypeMessage.do',param,function (resp) {
            if(resp.code=="200"){
                var announcement=resp.message.announcement;
                var activity=resp.message.activity;
                var share= resp.message.share;
                var means=resp.message.means;
                var homework=resp.message.homework;
                var materials=resp.message.materials;
                template('#announcementTmpl','#announcement',announcement);
                template('#activityTmpl','#activity',activity);
                template('#shareTmpl','#share',share);
                template('#meansTmpl','#means',means);
                template('#homeworkTmpl','#homework',homework);
                template('#materialsTmpl','#materials',materials);

                var tempStr="<div class=\"notice-container clearfix com-nothing\">";
                if (announcement.length == 0) {
                    var str=tempStr+"还未发布通知"+"</div>";
                    $('#announcement').append(str);
                }
                if (activity.length == 0) {
                    var str=tempStr+"还未发布活动报名"+"</div>";
                    $('#activity').append(str);
                }
                if (share.length == 0) {
                    var str=tempStr+"还未发布火热分享"+"</div>";
                    $('#share').append(str);
                }
                if (means.length == 0) {
                    var str=tempStr+"还未发布学习用品需求"+"</div>";
                    $('#means').append(str);
                }
                if (homework.length == 0) {
                    var str=tempStr+"还未发布作业"+"</div>";
                    $('#homework').append(str);
                }
                if (materials.length == 0) {
                    var str=tempStr+"还未发布学习资料"+"</div>";
                    $('#materials').append(str);
                }
            }
        })
    }


    //获取我的社区
    function getMyCommunity(){
        common.getData("/community/myCommunitys.do",{platform:"web"},function(result){
            if(result.code="200"){
                template('#myCommunityTmpl','#myCommunity',result.message.list);
            }else{
                alert(result.message);
            }
        })
    }
    //加载模板
    function template(tmpl,ctx,data){
        common.render({
            tmpl:tmpl,
            context:ctx,
            data:data,
            overwrite:1
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

    module.exports = communityAllType;
});