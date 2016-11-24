/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var teaCheck = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    //提交参数
    var teaCheckData = {};
    /*上传附件开始*/
    var files = [];
    var type = 0;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * zhiban.init()
     */
    teaCheck.init = function(){
        //设置初始页码
        teaCheckData.page = 1;
        //设置每页数据长度
        teaCheckData.pageSize = 12;
        teaCheck.selSubTeacherList(1);
        $(".check-con").prev(".plan-title").children(".path-root").click(function(){
            $(".checkxq-con,.next-path").hide();
            $(".check-con").show();
        });
        $(".plan-search").click(function(){
            teaCheck.selSubTeacherList(1);
        });
    };

    teaCheck.selSubTeacherList = function(page) {
        teaCheckData.term = $('#termlist').val();
        teaCheckData.userName = $('#name').val();
        teaCheckData.page = page;
        Common.getData('/teach/subject/users.do',teaCheckData,function(rep){
            //if (rep.code==200) {
                $('.checklist').html('');
                Common.render({tmpl: $('#checklist_templ'), data: rep, context: '.checklist'});
            $(".table-check").click(function(){
                teaCheckData.term = $('#termlist').val();
                teaCheckData.subjectId = $(this).attr('sbid');
                teaCheckData.userId = $(this).attr('uid');
                $('#teaname').text($(this).attr('nm'));
                teaCheck.selSingleTeacherCheck();
            });
            //}
            var option = {
                total: rep.total,
                pagesize: 12,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).off("click");
                        $(this).one("click",function () {
                            teaCheck.selSubTeacherList($(this).text());
                        });
                    });
                    $('.first-page').off("click");
                    $('.first-page').one("click",function () {
                        teaCheck.selSubTeacherList(1);
                    });
                    $('.last-page').off("click");
                    $('.last-page').one("click",function () {
                        teaCheck.selSubTeacherList(totalPage);
                    });
                }
            };
            Paginator.initPaginator(option);
        });
    }

    //更新评分项的值
    teaCheck.selSingleTeacherCheck = function() {
        Common.getData('/teach/selSingleTeacherCheck.do',teaCheckData,function(rep){
            $(".checkxq-con,.next-path").show();
            $(".check-con").hide();
            $('.teacheck').html('');
            $('#checktime').text(rep.time);
            $('#checkId').val(rep.id);
            Common.render({tmpl: $('#teacheck_templ'), data: rep, context: '.teacheck'});
            $(".check-count").blur(function(){
                teaCheckData.type = 1;
                teaCheckData.value = $(this).val();
                teaCheckData.projectName = $(this).parent().parent().attr('tcnm');
                teaCheck.updateProjectValue();
            });
            $(".check-quality").blur(function(){
                teaCheckData.type = 2;
                teaCheckData.value = $(this).val();
                teaCheckData.projectName = $(this).parent().parent().attr('tcnm');
                teaCheck.updateProjectValue();
            });
            $(".check-score").blur(function(){
                teaCheckData.type = 3;
                teaCheckData.value = $(this).val();
                teaCheckData.projectName = $(this).parent().parent().attr('tcnm');
                teaCheck.updateProjectValue();
            });
        });
    }

    teaCheck.updateProjectValue = function() {
        teaCheckData.id = $('#checkId').val();
        Common.getData('/teach/updateProjectValue.do',teaCheckData,function(rep){
            alert(rep.message);
        });
    }
    teaCheck.init();
});