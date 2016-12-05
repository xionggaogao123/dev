/*
 * @Author: Tony
 * @Date:   2016-10-24 14:42:16
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-25 10:09:10
 */

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    var communityJoin = {};
    communityJoin.init = function () {

        getMyCommunity();
        //获取热门社区
        getHotCommunity();
    }
    $(document).ready(function () {

        $(".hx-notice").click(function () {
            window.open('/webim/index','_blank');
        });

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('.hd-nav span').click(function() {
            $(this).addClass('hd-green-cur').siblings('.hd-nav span').removeClass('hd-green-cur');
        });

        $('.btn-ok').click(function () {
            var param={};
            var searchId=$('#searchId').val();
            // if(searchId==""||searchId==undefined){
            //     alert("社区Id不能为空！");
            //     return;
            // }
            param.relax=searchId;
            common.getData("/community/search.do",param,function(result){
                if(result.code=="200"){
                    template('#searchCommuntyTmpl','#searchCommunty',result.message);
                    $('.hd-cont-f2').show();
                }else{
                    alert(result.message);
                }
            })
        })

        $('body').on('click','.lookUp',function(){
            judge($(this));
        })

        $('body').on('click','.join',function(){
            var communityId=$(this).attr('communityId');
            var param={};
            param.communityId=communityId;
            common.getData('/community/join.do',param,function(resp){
                if(resp.code=="200"){
                    alert(resp.message);
                    getMyCommunity();
                }else{
                    alert(resp.message);
                }
            })
        })

        $('body').on('mouseleave','.ul-my-com li',function(){
            $('.ul-my-com li .com-hover-card .sp3').removeClass('sp33').addClass('sp-short');
            $('.ul-my-com li .com-hover-card .sp3 em').show();
            $(this).children('.com-hover-card').hide();
        });
        $('body').on('mouseover','.ul-my-com li',function(){
            $(this).children('.com-hover-card').show();
        });

        $('body').on('click','.joinCom',function () {
            var communityId=$(this).attr('cid');
            joinCommunity(communityId);
        })

        $('body').on('click', '.com-set-my-btn', function () {
            window.location.href='/community/communitySet.do';
        })


    })

    function joinCommunity(communityId){
        common.getData('/community/join.do',{communityId:communityId},function(resp){
            if(resp.code=="200"){
                getMyCommunity();
                getHotCommunity();
            }else{
                alert(resp.message);
            }
        })
    }

    function getHotCommunity(){
        common.getData('/community/hotCommunitys.do',{pageSize:9},function (resp) {
            if(resp.code=="200"){
                template('#hotCommunityTmpl','#hotCommunity',resp.message);
            }
        })
    }

    function judge(obj){
        var communityId=obj.attr('communityId');
        var param={};
        param.communityId=communityId;
        common.getData('/community/judge.do',param,function(resp){
            if(resp.code=="200"){
                if(resp.message){
                    window.location.href='/community/communityPublish.do?communityId='+communityId;
                }else{
                    alert("你不是该社区成员，不能查看该社区！");
                }
            }
        })
    }

    function template(tmpl,ctx,data){
        common.render({
            tmpl:tmpl,
            context:ctx,
            data:data,
            overwrite:1
        })
    }
    function getMyCommunity(){
        common.getData("/community/myCommunitys.do",{pageSize:9,platform:"web"},function(result){
            if(result.code="200"){
                if(undefined!=result.message.list){
                    template('#myCommunityTmpl','#myCommunity',result.message.list);
                }else{
                    template('#myCommunityTmpl','#myCommunity',result.message);
                }
            }else{
                alert(result.message);
            }
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

    module.exports = communityJoin;
});