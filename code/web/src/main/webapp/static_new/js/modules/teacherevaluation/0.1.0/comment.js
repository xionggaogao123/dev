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
    var comment = {},
        Common = require('common');
    var Paginator = require('initPaginator');
    //提交参数
    var commentData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * zhiban.init()
     */
    comment.init = function(){



        //设置初始页码
        commentData.page = 1;
        //设置每页数据长度
        commentData.pageSize = 10;
        comment.selTeacherQualityList(1);

        $(".XZZB-QX").click(function(){
            $('.comments-popupp').hide();
            $('.comments-popup').hide();
        });
        $("#seachQuality").click(function(){
            comment.selTeacherQualityList(1);
        });
        $(".sure-comment").click(function(){
            if ($.trim($('#comment').val()).length == 0 || $.trim($('#comment').val()) == '') {
                alert("领导评价不可为空！");
                return;
            }
            commentData.teacherId = $('#teaId').val();
            commentData.comment = $('#comment').val();
            commentData.score = "";
            comment.addComment(0);
        });

    };
    comment.addComment = function(type) {
        commentData.type = type;
        commentData.team = $('#termlist').val();
        Common.getData("/teachQuality/addComment.do", commentData, function (rep) {
            if(rep.flag) {
                if (type==0) {
                    alert("编辑上级领导评价成功！");
                    $('.comments-popup').hide();
                } else {
                    alert("打分成功！");
                }
                comment.selTeacherQualityList(1);
            } else {
                if (type==0) {
                    alert("编辑上级领导评价失败！");
                } else {
                    alert("打分失败！");
                }
            }
        });
    }
    comment.selTeacherQualityList = function(page) {
        commentData.page = page;
        commentData.term = $('#termlist').val();
        commentData.gradeId = $('#gradelist').val();
        commentData.userName = $('#userName').val();
        Common.getData("/teachQuality/selTeacherQualityList.do", commentData, function (rep) {
            $('#qualityList').html('');
            Common.render({tmpl: $('#quality_templ'), data: rep, context: '#qualityList'});
            $(".comment-red").click(function(){
                $('#comment').html('');
                $('#uname').html('');
                $('#uname').html($(this).attr('tnm'));
                $('#teaId').val($(this).attr('tid'))
                $('#comment').html($(this).attr('com'));
                $('.comments-popup').show();
            });
            $(".comments-cha").click(function(){
                $('#uname2').html('');
                $('#uname2').html($(this).attr('tnm'));
                $('#commentD').html($(this).attr('com'));
                $(".comments-popupp").show();
            });
            $(".score").blur(function() {
                if ($.trim($(this).val()).length == 0 || $.trim($(this).val()) == '') {
                    alert("打分不可为空！");
                    return;
                }
                commentData.teacherId = $(this).parent().attr('tid');
                commentData.comment = "";
                commentData.score = $.trim($(this).val());
                comment.addComment(1);
            });
            var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).off("click");
                        $(this).click(function () {
                            comment.selTeacherQualityList($(this).text());
                        });
                    });
                    $('.first-page').off("click");
                    $('.first-page').click(function () {
                        comment.selTeacherQualityList(1);
                    });
                    $('.last-page').off("click");
                    $('.last-page').click(function () {
                        comment.selTeacherQualityList(totalPage);
                    });
                }
            }
            Paginator.initPaginator(option);
        });
    }




    comment.init();
});