/**
 * Created by fl on 2016/7/26.
 */
define(function (require, exports, module) {
    var Common = require('common');
    var CONFIG = {};
    var BASEDATA = {
        gradeId: $('body').attr('gid')
    };

    $(function () {
        Common.cal('calId');
        Common.leftNavSel();

        getCurrTerm();
        checkState();

        getStudentTimeTable();
        //getSubjectGroup();


        $('body').on('click', '#exportStu', function() {
            window.location = '/zouban/kebiao/exportStu.do?week=' + BASEDATA.curweek + '&term=' + $('#termShowSp').text();
        });

        $(".tab-head li").click(function () {
            $(".tab-head li").removeClass("cur");
            $(this).addClass("cur");
            $(".tab-main>div").hide();
            var name = $(this).attr("id");
            $("#tab-" + name).show();
        });

        $('.alert-close').click(function () {
            $('.bg').hide();
            $('.xz-mx-alert').hide();
        })

        //=====================教学周==========================
        $("body").on("change", "#termChange", function () {
            if ($("#termChange").val() == 1) {
                showFirstTermWeek();
            } else if ($("#termChange").val() == 2) {
                showSecondTermWeek();
            }
        });
        //显示教学周弹窗
        $('#otherweeks').click(function () {
            showWeekWin();
        })
        //关闭教学周弹窗
        $('#closeweeks').click(function () {
            hideWeekWin()
        })

        $("body").on("change", "#weekSelectCtx", function () {
            changeWeek($(this));
        });


        function changeWeek(node) {
            BASEDATA.term = $('#currTerm').data("currTerm");
            BASEDATA.curweek = Number(node.val());
            $(".weekShow2").text("第" + BASEDATA.curweek + "周");
            $(".termShowSp").text(BASEDATA.term);
            getStudentTimeTable();
            //hideWeekWin();
        }

        function showFirstTermWeek() {
            $(".right0-3").show();
            $(".right0-5").hide();
            $("#firstTermTime").show();
            $("#secondTermTime").hide();
        }

        function showSecondTermWeek() {
            $(".right0-3").hide();
            $(".right0-5").show();
            $("#firstTermTime").hide();
            $("#secondTermTime").show();
        }

        function showWeekWin() {
            $('.col-right0').show();
            $('.bg').show();
            showFirstTermWeek();
        }

        function hideWeekWin() {
            $('.col-right0').hide();
            $('.bg').hide();
        }


    });

    /**
     * 获取当前学年
     */
    function getCurrTerm() {
        Common.getData("/zouban/kebiao/getCurrTerm.do", {}, function (resp) {
            $('#currTerm').data("currTerm", resp.message);
            $('#currTerm').data("currWeek", resp.currWeek);
            $('.termShowSp').text(resp.message);
            $('.weekShow2').text("第" + resp.currWeek + "周");


            BASEDATA.term = resp.message;
            BASEDATA.curweek = resp.currWeek;

            var weekList = [];
            for (var i = 0; i < resp.integers; i++) {
                weekList.push(i + 1);
            }
            //加载周数
            Common.render({
                tmpl: '#weekSelectTmpl',
                data: weekList,
                context: '#weekSelectCtx',
                overwrite: 1
            });

            $('#weekSelectCtx').val(resp.currWeek);
        });
    }

    /**
     * 获取学年列表
     */
    function getTermList() {
        Common.getData("/zouban/common/getTermList.do", {}, function (resp) {
            Common.render({
                tmpl: '#termListTmpl',
                data: resp.termList,
                context: '#termListCtx',
                overwrite: 1
            });
        });
    }

    function getStudentTimeTable() {
        var requestData = {
            week: BASEDATA.curweek
        };

        Common.getData("/zouban/kebiao/getStudentCurrTimeTable.do", requestData, function (resp) {
            if (resp.message == "true") {
                Common.render({
                    tmpl: '#studentTableTmpl',
                    data: resp,
                    context: '#studentTable',
                    overwrite: 1
                });
                $('#msg').hide();
                $('#studentTable').show();
                $('#exportStu').show();
            } else {
                $('#msg').show();
                $('#studentTable').hide();
            }
        });
    }

    function checkState() {
        var requestData = {
            term: $('#currTerm').data("currTerm"),
            gradeId: ""
        }
        var isPass = false;
        Common.getData("/zouban/baseConfig/getState.do", requestData, function (resp) {
            if (resp.state < 5) {//课表未发布
                $('#msg').show();
                $('#studentTable').hide();
            } else {
                $('#msg').hide();
                $('#studentTable').show();
                isPass = true;
            }
        });
        return isPass;
    }

    //学科组合列表
    function getSubjectGroup() {
        var requestData = {term: BASEDATA.term, gradeId: BASEDATA.gradeId};
        Common.getData("/zouban/student/groupList.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert(resp.message + '.<br>请到平台管理-->学科设置关联学科或联系复兰科技！');
            } else {
                Common.render({
                    tmpl: '#subjectGroupsTmpl',
                    data: resp.message.subjectGroupDTOs,
                    context: '#subjectGroupsContext',
                    overwrite: 1
                });
            }
        });
    }


});

