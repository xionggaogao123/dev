/**
 * Created by fl on 2015/8/26.
 */

define(function(require, exports, module) {
    var statPage={};
    var Common = require('common');
    require('pagination');

    statPage.init = function() {
        getUsers(1)
    }

    function getUsers(page){
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 50;
        var id = $('body').attr('id');
        Common.getDataAsync("/questionnaire/"+id+"/users.do", requestData, function(resp){
            var data = resp;
            var isInit = true;
            $('.new-page-links').html("");
            if(data.userList.length > 0){
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(data.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getUsers(n);
                        }
                    }
                });
            }



            Common.render({
                tmpl: '#submitInfoTmpl',
                data: resp.userList,
                context: '#submitInfo',
                overwrite: 1
            });
        })
    }

    statPage.sendLetters = function(id){
        $.ajax({
            type: "GET",
            data:{id: id},
            url: '/questionnaire/sendLetters.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            beforeSend:function(){
                $('.letter').text("私信发送中...")
            },
            success: function(resp){
                if(resp.code == '200'){
                    alert("发送成功")
                    $('.letter').text("发送成功")
                } else {
                    alert("发送失败")
                    $('.letter').text("发送私信")
                }
            }
        });

    }





    module.exports = statPage;
});
