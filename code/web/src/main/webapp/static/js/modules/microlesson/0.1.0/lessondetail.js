/*
 * @Author: Tony
 * @Date:   2015-08-17 14:52:59
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-17 15:57:45
 */

'use strict';
define(['doT', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var lessondetail = {},
        Common = require('common');
    var lessondetailData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * matchdetail.init()
     */
    lessondetail.init = function () {
        if (GetQueryString("a") != "10000") {
            Common.cal('calId');
            Common.leftNavSel();
        }
        $('#submitbut').bind("click", function (event) {
            var comment = $("#comment").val();
            lessondetailData.comment = comment;
            if ($.trim(comment) == '') {
                alert("请点评！");
                return;
            }
            var scoretype = '';
            var names = $(".lbdf span");
            var scores = $(".lbdf select");
            if (names != '' && names.length != 0) {
                for (var i = 0; i < names.length; i++) {
                    scoretype += names[i].innerHTML + ';' + scores[i].value + ',';
                }
            } else {
                lessondetailData.score = $("#socrelist").val();
            }
            lessondetailData.scoretypelist = scoretype;
            lessondetailData.lessonid = $('#lessonid').val();
            Common.getData('/microlesson/addScoreInfo.do', lessondetailData, function (rep) {
                if (rep.result) {
                    alert("点评成功！");
                    $('.lbdf').hide();
                    //$('.peoscore').empty();
                    Common.render({tmpl: $('#fenshu'), data: rep, context: '.peoscore'});
                    //$('#yourscore').text(rep.score+'分');
                    $('#sccontent').text($("#comment").val());
                    $('.lbdp').show();

                } else {
                    alert(rep.mesg);
                }

            });

        });

        $('#dellesson').bind("click", function (event) {
            if (confirm("确认删除课程")) {
                lessondetailData.matchid = $('#matchid').val();
                lessondetailData.lessonid = $('#lessonid').val();
                Common.getData('/microlesson/dellesson.do', lessondetailData, function (rep) {
                    if (GetQueryString("a") != "10000") {
                        location.href = "/microlesson/matchDetailInfo.do?matchid=" + $('#matchid').val();
                    } else {
                        location.href = "/microlesson/matchDetailInfo.do?a=10000&matchid=" + $('#matchid').val();
                    }
                });
            }

        });


    };
    ///*打分详情*/
    //var datalist1 = {
    //    rspCode:200,
    //    rspDesc:'通讯成功',
    //    data:[
    //        {
    //            namer:'张帆：100分',
    //            contentmore:'这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好。'
    //        },
    //        {
    //            namer:'张帆：100分',
    //            contentmore:'这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好。'
    //        },
    //        {
    //            namer:'张帆：100分',
    //            contentmore:'这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好，这门课很好。'
    //        }
    //    ]
    //};
    //Common.render({tmpl:$('#fenshu'),data:datalist1,context:'.fenshu'});
    /*打分详情*/
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return unescape(r[2]);
        return null;
    }


    lessondetail.init();
});