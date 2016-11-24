/**
 * Created by admin on 2016/11/17.
 */
/**
 * Created by admin on 2016/11/17.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityUser = {};
    var page = 1;
    var sortType = 2;
    var personId=$('body').attr('personId');
    var applyName=$('body').attr('applyName');
    var zan = 1;
    communityUser.init = function () {
        postList(page, 0);//发帖列表
    };


    $(document).ready(function () {
        $('body').on('click','#apply',function(){
            if($(this).text()=="加玩伴"){
                var that=this;
                var contentValue = "我是"+applyName+",请求加为好友";
                var param = {};
                param.content = contentValue;
                param.personId = personId;
                common.getPostData('/forum/userCenter/applyFriend.do', param, function (resp) {
                    if (resp.code == "200") {
                        var str="<img src=\"/static/images/community/img_add.png\">等回复<em></em>";
                        $(that).html(str);
                        alert("玩伴申请已发出");
                    }
                });
            }
        })
    })








    function postList(page, gtTime) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.classify = -1;
        //requestData.cream=-1;
        requestData.gtTime = gtTime;
        requestData.postSection = "";
        requestData.zan = zan;
        requestData.person = personId;
        common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('#newPage').show();
            $('#newPage').html("");
            $('#theme').show();
            $('#imageTheme').hide();
            if (posts.length > 0) {
                $('#newPage').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
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
                            postList(n, gtTime);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }else{
                $('#imageTheme').show();
                $('#theme').hide();
            }
            template('#postListTml','#postList',posts);

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

    module.exports = communityUser;
});