/**
 * Created by jerry on 2016/10/27.
 *
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communitySet = {};
    var page=1;
    communitySet.init = function () {
        getHotCommunity();
        getMyCommunity(page);
    };

    $(document).ready(function () {

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('body').on('click', '#cancel', function () {
            $('.wind-com-edit').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.btn-save', function () {

            saveOperation();
        });

        $('body').on('click', '.quit', function () {
            var communityId = $(this).attr('communidyId');
            quitCommunity(communityId);
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

        $('body').on('click','.sp-edit',function(){
            var cmId=$(this).attr('cmId');
            common.getData('/community/'+cmId,{},function(resp){
                if(resp.code=="200"){
                    $('.wind-com-edit').data('id',cmId);
                    $('#communityName').val(resp.message.name);
                    $('#communityLogo').attr('src',resp.message.logo);
                    $('#communityDesc').next().val(resp.message.desc);
                    $('#selectOpen').val(resp.message.open);
                    $('.wind-com-edit').fadeIn();
                    $('.bg').fadeIn();
                }
            })

        })

        $('body').on('change','#selectOpen',function () {
            selectShow($(this));
        })


    });

    function selectShow(obj){
        if(obj.val()==0){
            $('#xt1').show();
            $('#xt2').hide();
        }else{
            $('#xt1').hide();
            $('#xt2').show();
        }
    }

    function joinCommunity(communityId){
        common.getData('/community/join.do',{communityId:communityId},function(resp){
            if(resp.code=="200"){
                getMyCommunity(page);
                getHotCommunity();
            }else{
                alert(resp.message);
            }
        })
    }


    //获取我的社区
    function getHotCommunity() {
        common.getData("/community/hotCommunitys.do", {}, function (result) {
            if (result.code = "200") {
                template('#communityTmpl', '#hotCommunity', result.message);

            } else {
                alert(result.message);
            }
        })
    }


    function getMyCommunity(page) {
        var isInit = true;
        var requestData = {};
        requestData.page=page;
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/community/myCommunitys.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('#myPage').html("");
                if (resp.code == "200") {
                    var resultData = resp.message.list;
                    $('#userCount').text(resp.message.count);
                    if (resultData.length > 0) {
                        $('#myPage').jqPaginator({
                            totalPages: Math.ceil(resp.message.count / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / resp.message.pageSize),//总页数
                            visiblePages: 3,//分多少页
                            currentPage: parseInt(page),//当前页数
                            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                            onPageChange: function (n) { //回调函数
                                if (isInit) {
                                    isInit = false;
                                } else {
                                    getMyCommunity(n);
                                    $('body,html').animate({scrollTop: 0}, 20);
                                }
                            }
                        });
                    }
                    template('#myCommunityTmpl','#myCommunity',resultData);
                } else {
                    alert(resp.message);
                }
            }
        });
        // common.getData("/community/myCommunitys.do", {}, function (result) {
        //     if (result.code = "200") {
        //         template('#myCommunityTmpl', '#myCommunity', result.message);
        //     } else {
        //         alert(result.message);
        //     }
        // })
    }


    function quitCommunity(communtiyId) {
        var data =  {
            communityId:communtiyId
        };

        common.getData("/community/quit.do",data, function (result) {

            if (result.code = "200") {
                alert("退出成功");
                getMyCommunity(page);
                getHotCommunity();
            } else {
                alert(result.message);
            }
        });
    }


    function validateCommunityInfo(){

        var communityName=$('#communityName').val();
        var communityDes=$('#communityDesc').next().val();
        if(communityName==""||communityName==undefined){
            alert("社区名称不能为空！");
            return false;
        }

        if(communityName.length>15){
            alert("社区名称不能超过15个字！");
            return false;
        }

        if(communityDes==""||communityDes==undefined){
            alert("社区简介不能为空！");
            return false;
        }
        if(communityDes.length>100){
            alert("社区简介字数不能超过100！");
            return false;
        }


        return true;

    }

    function saveOperation() {
        if(validateCommunityInfo()){
            if(judgeCreate($('#communityName').val(),$('.wind-com-edit').data('id'))){
                var param={};
                param.logo=$('#communityLogo').attr('src');
                param.desc=$('#communityDesc').next().val();
                param.name=$('#communityName').val();
                param.open=$('#selectOpen').val();
                param.communityId=$('.wind-com-edit').data('id');
                common.getData('/community/update.do',param,function(resp){
                        getMyCommunity(page);
                        $('.wind-com-edit').fadeOut();
                        $('.bg').fadeOut();
                        alert("保存成功！");
                })
            }
        }

    }

    function judgeCreate(communityName,id){
        var flag=true;
        //先判断社区名称是否更改
        common.getData('/community/judgeCommunityName.do',{communityName:communityName,id:id},function(resp){
             if(resp.message){
                 flag=true;
             }else{
                 flag=false;
             }
        })
        if(!flag){
            flag=false;
            common.getData('/community/judgeCreate.do',{communityName:communityName},function(resp){
                if(resp.message){
                    flag=false;
                    alert("该社区名称已存在！");
                }
            })
            return flag;
        }else{
            return flag;
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

    module.exports = communitySet;
});