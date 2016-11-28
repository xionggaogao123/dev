/*
 * @Author: Tony
 * @Date:   2016-10-24 14:42:16
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-25 10:38:32
 */

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common=require('common');
    var communityCreate = {};
    communityCreate.init = function () {


        getMyCommunity();
        //获取热门社区
        getHotCommunity();
    }

    $(document).ready(function () {

        $('.hd-nav span').click(function () {
            $(this).addClass('hd-green-cur').siblings('.hd-nav span').removeClass('hd-green-cur');
        });
        $('.com-cre-infor p span i').click(function(){
            $(this).next('em').toggleClass('erw-em1').toggleClass('erw-em2');

                var id = $('#zk').text();
                if (id.indexOf("展开") > -1) {
                    $('#zk').text('收起');
                } else if (id.indexOf('收起') > -1) {
                    $('#zk').text('展开');
                }
        });

        hx_update();

        setInterval(hx_update,1000 * 60);


        $('body').on('click', '.com-set-my-btn', function () {
            window.location.href='/community/communitySet.do';
        })

        // $('body').on('blur','#communityName',function () {
        //     if($(this).val()!=""){
        //         judgeCreate($(this).val());
        //     }
        // })

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

        $('.btn-ok').click(function () {
            //先验证信息 
            if(validateCommunityInfo()){
                if(judgeCreate($('#communityName').val())){
                    var param={};
                    param.logo=$('#imageUrl').attr('src');
                    param.desc=$('#communityDes').val();
                    param.name=$('#communityName').val();
                    param.open=$('#selectOpen').val();
                    common.getData('/community/create.do',param,function(resp){
                        if(resp.code=="200"){
                            $('#image').attr('src',resp.message.logo);
                            $('#comm_name').text(resp.message.name);
                            $('.sq-nm').html(resp.message.name);
                            $('#comm_id').text("社区ID:"+resp.message.searchId);
                            $('#QRcode').attr('src',resp.message.qrUrl);
                            $('.com-l1').hide();
                            $('.com-l2').show();
                            getMyCommunity();
                            getHotCommunity();
                        }else{
                            alert(resp.message);
                            return;
                        }

                    })
                }


            }
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

    function judgeCreate(communityName){
        var flag=true;
        common.getData('/community/judgeCreate.do',{communityName:communityName},function(resp){
            if(resp.message){
                flag=false;
                alert("该社区名称已存在！");
            }
        })
        return flag;
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
        common.getData("/community/myCommunitys.do",{},function(result){
            if(result.code="200"){
                template('#myCommunityTmpl','#myCommunity',result.message);
            }else{
                alert(result.message);
            }
        })
    }

    /**
     * 获取热门社区
     */
    function getHotCommunity(){
        common.getData('/community/hotCommunitys.do',{},function (resp) {
            if(resp.code=="200"){
                template('#hotCommunityTmpl','#hotCommunity',resp.message);
            }
        })
    }

    function validateCommunityInfo(){

        var communityName=$('#communityName').val();
        var communityDes=$('#communityDes').val();
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
        if(communityDes.length>150){
            alert("社区简介字数不能超过150！");
            return false;
        }


        return true;

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


    module.exports = communityCreate;
});