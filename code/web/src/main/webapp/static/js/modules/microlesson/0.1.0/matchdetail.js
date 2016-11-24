/* 
 * @Author: Tony
 * @Date:   2015-08-17 11:34:10
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-19 15:25:14
 */

'use strict';
define(['doT', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var matchdetail = {},
        Common = require('common');
    var matchdetailData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiaoyujv_bisaixiangqing.init()
     */
    matchdetail.init = function () {
        if (GetQueryString("a") != "10000") {
            Common.cal('calId');
            Common.leftNavSel();
        }
        matchdetail.sellessonlist();

        $('#uplesson').bind("click", function (event) {
            $(".gay,.att").fadeIn();
        });

        $('.tygry').bind("click", function (event) {
            $(".gay,.att").fadeOut();
        });

        $('#result').bind("click", function (event) {
            var id = $('.b').attr('cid');
            if (GetQueryString("a") != "10000") {
                location.href = '/microlesson/getlessonScoreList.do?matchid=' + id;
            } else {
                location.href = '/microlesson/getlessonScoreList.do?a=10000&matchid=' + id;
            }
        });

        //年级筛选
        $('#seachtype').change(function () {
            matchdetail.sellessonlist();
        });


        $('#edit').bind("click", function (event) {
            var id = $('.b').attr('cid');
            if (GetQueryString("a") != "10000") {
                location.href = '/microlesson/editmatch.do?matchid=' + id;
            } else {
                location.href = '/microlesson/editmatch.do?a=10000&matchid=' + id;
            }

        });

        $('#delmatch').bind("click", function (event) {
            if (confirm("确认删除比赛！")) {
                matchdetailData.matchid = $('.b').attr('cid');
                Common.getData('/microlesson/deletematch.do', matchdetailData, function (rep) {
                    if (rep.result) {
                        if (GetQueryString("a") != "10000") {
                            location.href = '/microlesson/micropage.do';
                        } else {
                            location.href = '/microlesson/micropage.do?a=10000';
                        }
                    } else {
                        alert("删除失败！");
                    }
                });
            }
        });

        $('#downall').bind("click", function (event) {
            var matchid = $('.b').attr('cid');
            location.href = "/microlesson/downloadAll.do?matchid=" + matchid;
            //window.open("/microlesson/downloadAll.do?matchid="+matchid,"_blank");
        });

        $('#typesuer').bind("click", function (event) {
            var lessonname = $("#lessonname").val();
            matchdetailData.name = lessonname;
            if ($.trim($('#lessonname').val()) == '') {
                alert("请输入课程名。");
                return;
            }
            matchdetailData.typeid = $("#seltype").val();
            matchdetailData.matchid = $('.b').attr('cid');
            Common.getData('/microlesson/create.do', matchdetailData, function (rep) {
                if (rep.code == "200") {
                    $(".gay,.att").fadeOut();
                    matchdetail.editLesson(rep.message);
                } else {
                    MessageBox(rep.message, -1);
                }

            });

        });


        //$('#lessonview').bind("click",function(event){
        //    window.open('/microlesson/view.do?lessonId=' + $(this).attr('cid'));
        //
        //});

    };

    matchdetail.editLesson = function (id) {
        var url;
        if (GetQueryString("a") != "10000") {
            url = '/microlesson/edit.do?lessonId=' + id;
        } else {
            url = '/microlesson/edit.do?a=10000&lessonId=' + id;
        }
        location.href = url;
    }

    matchdetail.sellessonlist = function () {
        matchdetailData.typeid = $("#seachtype").val();
        matchdetailData.matchid = matchdetail.getUrlParam('matchid');
        Common.getData('/microlesson/lessonlist.do', matchdetailData, function (rep) {
            $('.tupian').html('');
            Common.render({tmpl: $('#listbiao'), data: rep, context: '.tupian'});
            $('.delete').bind("click", function (event) {
                if (confirm("确认删除课程！")) {
                    matchdetailData.matchid = $('#log').val();
                    matchdetailData.lessonid = $(this).attr('cid');
                    Common.getData('/microlesson/dellesson.do', matchdetailData, function (rep) {
                        matchdetail.sellessonlist();

                    });
                }
            });
        });
    }

    //获取url中的参数
    matchdetail.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }

    ///*参赛课程*/
    //var datalist1 = {
    //    rspCode:200,
    //    rspDesc:'通讯成功',
    //    data:[
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'新建课程'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'安徽省高中化学微课评比'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'安徽省高中化学微课评比'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'新建课程'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'新建课程'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/226x160',
    //            tit:'新建课程'
    //        }
    //    ]
    //};
    //Common.render({tmpl:$('#tupian'),data:datalist1,context:'.tupian'});

    /*点击一次出现蒙版效果开始*/
    $(".Tanchu .LB dd li").click(function (event) {
        $(this).children('.mengban').toggle();
    });
    /*点击一次出现蒙版结束*/
    /*弹出层*/
    $(function () {
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".Tanchu").width() / 2;
        T = $(window).height() / 2 - $(".Tanchu").height() / 2;
        $(".Tanchu").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $(".Look a.xiaozi").click(function (event) {
            $(".gay,.Tanchu").fadeIn();
        });
        $(".Tanchu .Btm .QX").click(function (event) {
            $(".gay,.Tanchu").fadeOut();
        });
    });
    /*弹出层*/
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return unescape(r[2]);
        return null;
    }


    matchdetail.init();
});