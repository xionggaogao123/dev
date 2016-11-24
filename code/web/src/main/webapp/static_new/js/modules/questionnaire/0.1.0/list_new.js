/**
 * Created by fl on 2015/11/23.
 */


define(['common','pagination','/static/plugins/angular-1.2.28/angular.min.js'],function(require,exports,module){
    var Common = require('common');
    require('pagination');
    var onlyMyself = 0;
    var currentpage = 1;

    (function(){/*

        */

        sessionStorage.setItem("userId",$('#title').attr('userId'));

        getQuestionnaireList(1);

        $('input:radio').change(function(){
            onlyMyself = $('input:radio:checked').val();
            getQuestionnaireList(1);
        })

        $('body').on('click','.review',function(){
            sessionStorage.setItem('quesId',$(this).attr('qid'));
            window.open("/questionnaire/review.do");
        })

        $('body').on('click', '.public', function(){
            updateIsPublic($(this), 1);
        })

        $('body').on('click', '.quitpublic', function(){
            updateIsPublic($(this), 0);
        })


    })()

    function getQuestionnaireList(page){
        currentpage = page;
        var isInit = true;
        var requestData = {};
        var date = new Date();
        requestData.page = page-1;
        requestData.size = 15;
        requestData.onlyMyself = onlyMyself;
        Common.getData('/questionnaire/list.do?time='+ date.getTime(), requestData,function(resp){
            var datas = resp.content;
            $('.new-page-links').html("");
            if(datas.length > 0) {
                //分页方法
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.totalElements / requestData.size) == 0 ? 1 : Math.ceil(resp.totalElements / requestData.size),//总页数
                    visiblePages: 10,//分多少页
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
                            getQuestionnaireList(n);
                        }
                    }
                });
            }

            Common.render({
                tmpl: '#list_tmpl',
                data: resp.content,
                context: '#list',
                overwrite: 1
            })
        })
        remove();
    }

    function remove(){
        $('.remove').on('click',function(){
            var msg = '是否删除问卷?';
            if(confirm(msg)== true){
                var id = $(this).attr('id');
                //alert(id);
                Common.getData('/questionnaire/delete.do',{id:id}, function(resp){

                })
                getQuestionnaireList(currentpage);
            }

        })
    }

    function updateIsPublic($ele,isPublic){
        var qId = $ele.attr("qid");
        Common.getData('/questionnaire/updateIsPublic.do',{qId:qId, isPublic:isPublic}, function(resp){
            if(resp.code == "500"){
                alert("操作失败");
            } else {
                if(isPublic == 1){
                    $ele.removeClass('public').addClass('quitpublic').text("取消公开");
                } else {
                    $ele.removeClass('quitpublic').addClass('public').text("公开");
                }
            }
        })
    }
});
