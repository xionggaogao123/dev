/*
* @Author: Tony
* @Date:   2016-11-30 10:36:33
* @Last Modified by:   Tony
* @Last Modified time: 2016-12-01 15:40:02
*/

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');

    var trainDetail = {};
    var instituteId=$('body').attr('instituteId');
    var page=1;
    var itemName=$('body').attr('itemName');
    trainDetail.init = function () {
        getTopItems();

        getTrainComment(page);

        getInstitute();
    };

    $(document).ready(function () {
        $('body').on('click','.train-nav span',function(){
            $('.train-nav span').removeClass('cur1');
            $(this).addClass('cur1');
        });
        $('body').on('click','.d1f span',function(){
            $('.d1f span').removeClass('cur2');
            $(this).addClass('cur2');
        });
    	$('body').on('click','.d2f span',function(){
    		$('.d2f span').removeClass('cur2');
    		$(this).addClass('cur2');
    	});
    	$('body').on('click','.lesson-menu span',function(){
    		$(this).addClass('cur3').siblings('span').removeClass('cur3');
    	});
        $('body').on('click','.train-detail .nav span',function(){
            $(this).addClass('cur4').siblings('span').removeClass('cur4');
        });
        $('body').on('click','.train-detail .nav .sp1',function(){
            $('.new-page-links').hide();
            $('.train-infor').show();
            $('.train-pj').hide();
        });
        $('body').on('click','.train-detail .nav .sp2',function(){
            $('.new-page-links').show();
            $('.train-infor').hide();
            $('.train-pj').show();
        })

        $('body').on('click','#submit',function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        submitTrainComment();
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            })
        })
    })


    //提交点评
    function submitTrainComment(){
        var score=getScore();
        if(score==0){
            alert("请先星级评分");
            return false;
        }
        var comment=$('#comment').val();
        if(comment.length>100){
            alert("评论字数不能超过100!");
            return false;
        }
        var param={};
        param.instituteId=instituteId;
        param.score=getScore();
        param.comment=comment;
        common.getData('/train/addTrainComment.do',param,function (resp) {
            if(resp.code=="200"){
                getTrainComment(page);
                $('#comment').val("");
                getInstitute();
                alert(resp.message);
            }else{
                alert(resp.message);
            }
        })


    }

    function getScore(){
        var score=0;
        $('#p-star img').each(function(){
            if($(this).attr('src')=="/static/images/train/star_golden.png"){
                score++;
            }
        })
        return score;
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

    function getTrainComment(page) {
        var isInit = true;
        var requestData = {};
        requestData.page=page;
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/train/getTrainComments/'+instituteId,
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('.new-page-links').html("");
                if (resp.code == "200") {
                    $('.train-detail').find('.sp2').find('em').html("("+resp.message.count+")");
                    var resultData = resp.message.list;
                    if (resultData.length > 0) {
                        $('.new-page-links').jqPaginator({
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
                                    getTrainComment(n);
                                    $('body,html').animate({scrollTop: 0}, 20);
                                }
                            }
                        });
                    }
                    template('#trainCommentTmpl','#trainComment',resultData);
                } else {
                    alert(resp.message);
                }
            }
        });
    }

    function getTopItems() {
        common.getData('/train/getItemTypes.do',{level:1},function(resp){
            if(resp.code=="200"){
                var list=resp.message;
                var str="";
                for(var i in list){
                    $('#trainTop').data(list[i].name,list[i].id);
                    str+="<span name=\""+list[i].name+"\">"+list[i].name+"</span>";
                }
                $('#trainTop').append(str);
                $('#trainTop').find('span[name='+itemName+']').addClass('cur1');
            }
        })
    }

    function getInstitute(){

        var requestData = {};
        requestData.itemType=$('#trainTop').data($('#trainTop').find('.cur1').text());
        requestData.sortType=1;
        requestData.pageSize=4;
        common.getData('/train/getInstitutes.do',requestData,function(resp){
             if(resp.code=="200"){
               template('#instituteTmpl','#institute',resp.message.list);
             }
        })
    }

    module.exports = trainDetail;
});