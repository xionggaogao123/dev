/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define(['jquery','common','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var chengJi = {},
        Common = require('common');

    var examSelectionData = null;
    var commonData = {};
    var tableData=[];
    var tmpl='';
    var context='';

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * chengJi.init()
     */
    chengJi.init = function(){
        if (GetQueryString("a")!="10000") {


        }

    };
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    chengJi.chartInit = function(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);


    }

//===========================================老师首页=======================================
    chengJi.initTeacherMainPageData = function(userId){
        //查找当前学期
        //Common.getData("/score/getCurrentTerm.do", requestData, function(resp) {
        //    if(resp){
        //        sessionStorage.setItem("schoolYear", resp.currentTerm);
        //    }
        //})
        sessionStorage.setItem("schoolYear", getCurrentTerm());

        //默认展示实际分
        sessionStorage.setItem("hundredSystem",2);
        //切换成百分制
        $("#hundred").on('click', function(){
            $("#reality").attr('class', 'cu');
            $("#hundred").attr('class', 'cur');
            sessionStorage.setItem("hundredSystem",1);
            if(sessionStorage.getItem("fullScore") != 100) {
                chengJi.onExamSelected();
            }
        })
        //切换成实际分
        $("#reality").on('click', function(){
            $("#reality").attr('class', 'cur');
            $("#hundred").attr('class', 'cu');
            sessionStorage.setItem("hundredSystem",2);
            if(sessionStorage.getItem("fullScore") != 100) {
                chengJi.onExamSelected();
            }
        })

        //选中班级
        $('#exam_teacher_select_classsubject').on('change',function(){
            chengJi.onClassSubjectSelected();
        });
        //选中考试
        $('#exam_teacher_select_exam').on('change',function(){
            chengJi.onExamSelected();
        });


        var requestData = {};
        requestData.userId = userId;

        Common.getData('/score/getExamSelection.do',requestData,function(resp){
            examSelectionData = resp;
            var cfg = {};
            cfg.data = resp;
            cfg.context = '#exam_teacher_select_classsubject';
            cfg.tmpl = '#exam_classsubjecttmpl';
            Common.render(cfg);
            chengJi.onClassSubjectSelected();
        });

    }

    function getCurrentTerm(){
        var myDate = new Date();
        var year = myDate.getFullYear();
        var month = myDate.getMonth() + 1;
        var schoolYear;
        if (month < 9 && month >= 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }


    //==========选中班级学科，例如：三年一班（数学）
    chengJi.onClassSubjectSelected = function(){
        var checkText=$("#exam_teacher_select_classsubject").find("option:selected").text();

        if(examSelectionData){
            Common.render({
                tmpl: '#exam_selectexamtmpl',
                data: examSelectionData[checkText],
                context: '#exam_teacher_select_exam',
                overwrite: 1
            });
        }
        sessionStorage.setItem("classId", examSelectionData[checkText].classId);
        sessionStorage.setItem("className", examSelectionData[checkText].className);
        sessionStorage.setItem("subjectId", examSelectionData[checkText].subjectId);
        sessionStorage.setItem("subjectName", examSelectionData[checkText].subjectName);
        sessionStorage.setItem("gradeId", examSelectionData[checkText].gradeId);
        sessionStorage.setItem("gradeName", examSelectionData[checkText].gradeName);
        chengJi.onExamSelected();
    }

    //===========选中考试，例如：期中考试
    chengJi.onExamSelected = function(){
        var examId=$("#exam_teacher_select_exam").find("option:selected").attr('id');
        var examName = $("#exam_teacher_select_exam").find("option:selected").text();
        var classsubject=$("#exam_teacher_select_classsubject").find("option:selected").text();

        var requestData = {};
        requestData.subjectId = sessionStorage.getItem("subjectId");
        requestData.classId = sessionStorage.getItem("classId");
        requestData.examId = examId;

        var examList = examSelectionData[classsubject].examList;
        for(var examidx in examList) {
            if(examList[examidx].examId == examId){
                commonData.schoolYear = examList[examidx].schoolYear;
                commonData.isGrade = examList[examidx].isGrade;
                commonData.classSize = examList[examidx].classSize;
                $("#totaltitle").text(commonData.schoolYear + examSelectionData[classsubject].className +
                examSelectionData[classsubject].subjectName + examName + "成绩");
                sessionStorage.setItem("title",commonData.schoolYear + examSelectionData[classsubject].className + examSelectionData[classsubject].subjectName);
                sessionStorage.setItem("schoolYear", commonData.schoolYear);
            }

        }
        //判断是否为年级考试，并做相应处理
        if(commonData.isGrade == 1) {

            $("#linjjf").show();
            $("#grade").show();
            $("#num2").text(2);
            $("#num3").text(3);
            $("#num4").text(4);
        } else {

            $("#linjjf").hide();
            $("#grade").hide();
            $("#num2").text(1);
            $("#num3").text(2);
            $("#num4").text(3);
        }
        //多年级考试显示班级成绩对比
        if(commonData.classSize > 1) {
            $("#dlbjcjdb").show();
        } else {
            $("#dlbjcjdb").hide();
        }


        //从后台取得本次考试的满分
        var fullScore=100;
        if(requestData.examId && requestData.subjectId) {
            Common.getData('/score/getFullScore.do', requestData, function (resp) {
                sessionStorage.setItem("fullScore", resp.fullScore);
                fullScore = resp.fullScore;
            })
            $(".charts-col").show();
            $("#noExam").hide();
        } else {
            $(".charts-col").hide();
            $("#noExam").show();
        }


        var hundredSystem = sessionStorage.getItem("hundredSystem");//百分制
        //班级成绩对比
        if(requestData.examId && requestData.subjectId) {
            Common.getDataAsync('/score/getClassScoreCompare.do', requestData, function (resp) {


                var rawdata = {};
                rawdata.title =  commonData.schoolYear + examSelectionData[classsubject].gradeName +
                examSelectionData[classsubject].subjectName + "成绩";
                rawdata.exam =  examName;
                rawdata.subject= examSelectionData[classsubject].subjectName;
                rawdata.classlist = resp.classList;
                rawdata.classscore=[];
                if(fullScore != 100 && hundredSystem == 1) {//百分制
                    for( var i in resp.classScore) {
                        rawdata.classscore[i] = Math.round(resp.classScore[i]*100/fullScore);
                    }
                } else {//实际分
                    rawdata.classscore = resp.classScore;
                }

                var chardata = chengJi.buildChartData('exam_bjksdb','',rawdata);
                chengJi.chartInit('cjdbId',chardata);
            });
        }

        //考试成绩分布
        if(requestData.examId && requestData.classId && requestData.subjectId) {
            Common.getDataAsync('/score/getClassScoreDistribution.do', requestData, function (resp) {
                var rawdata = {};
                rawdata.title = "班级成绩分布";
                rawdata.exam =  examSelectionData[classsubject].subjectName + examName;
                rawdata.countlist = resp.countList;
                rawdata.category=[];
                if(fullScore != 100 && hundredSystem == 1) {//百分制
                    rawdata.category[0] = '0-59分';
                    rawdata.category[1] = '60-79分';
                    rawdata.category[2] = '80-89分';
                    rawdata.category[3] = '90-100分';
                } else {//实际分
                    rawdata.category = resp.category;
                }
                var chardata = chengJi.buildChartData('exam_bjcjfb','',rawdata);
                chengJi.chartInit('cjfbId',chardata);
            });
        }

        //成绩统计
        //均分
        if(requestData.examId && requestData.classId && requestData.subjectId) {
            Common.getDataAsync('/score/getClassScoreAverage.do', requestData, function (resp) {
                var data = {};
                if(fullScore != 100 && hundredSystem == 1) {//百分制
                    data.data1 = Math.round(resp.classScore*100/fullScore);
                    data.data2 = Math.round(resp.gradeScore*100/fullScore);
                } else {//实际分
                    data.data1 = resp.classScore;
                    data.data2 = resp.gradeScore;
                }

                data.semester = commonData.schoolYear;
                $("#bjjf").text(data.data1);
                $("#njjf").text(data.data2);

                var chardata = chengJi.buildChartData('cjtj','jf',data);
                chengJi.chartInit('pjfId',chardata);
            });
        }
        //班级成绩
        if(requestData.examId && requestData.classId && requestData.subjectId) {
            Common.getDataAsync('/score/getClassScorePercent.do', requestData, function (resp) {
                var rawdata = {};
                rawdata.data1 = resp.cpr;
                rawdata.data2 = resp.cer;
                rawdata.semester = commonData.schoolYear;
                $("#bjhgl").html(resp.cpr + "<i>%</i>");
                $("#bjyxl").html(resp.cer + "<i>%</i>");
                var chardata = chengJi.buildChartData('cjtj','bjperc',rawdata);
                chengJi.chartInit('bhgId',chardata);

            });
        }
        //年级成绩
        if(requestData.examId && requestData.classId && requestData.subjectId ) {
            if (commonData.isGrade) {
                Common.getDataAsync('/score/getGradeScorePercent.do', requestData, function (resp) {
                    var rawdata = {};
                    rawdata.data1 = resp.gpr;
                    rawdata.data2 = resp.ger;
                    rawdata.semester = commonData.schoolYear;
                    $("#njhgl").html(resp.gpr + "<i>%</i>");
                    $("#njyxl").html(resp.ger + "<i>%</i>");
                    var chardata = chengJi.buildChartData('cjtj', 'njperc', rawdata);
                    chengJi.chartInit('njhgId', chardata);
                });
            }
        }

        //学生列表
        if(requestData.examId && requestData.classId && requestData.subjectId) {
            requestData.fullScore = fullScore;
            requestData.hundred = hundredSystem;
            Common.getDataAsync('/score/getClassScoreList.do', requestData, function (resp) {
                tableData = resp.stuScoreDTOList;
                tmpl = '#exam_scorelisttmpl';
                context = '#scorelist_body';
                chengJi.splitPage(1,10);

            });
        }

    }
//============================================老师学期================================
    chengJi.getTeacherSemesterPageData=function(){

        var requestData = {};
        //查找当前学期
        //Common.getData("/score/getCurrentTerm.do", requestData, function(resp) {
        //    if(resp){
        //        sessionStorage.setItem("schoolYear", resp.currentTerm);
        //    }
        //})
        sessionStorage.setItem("schoolYear", getCurrentTerm());
        //生成标题
        $("#semesterTitle").text(sessionStorage.getItem("schoolYear") + sessionStorage.getItem("className") + "成绩统计分析");
        //班级成绩对比

        requestData.classId = sessionStorage.getItem("classId");
        requestData.subjectId = sessionStorage.getItem("subjectId");
        Common.getDataAsync("/score/getSemesterScoreCompare.do", requestData, function(resp) {
            var rawdata = {};
            rawdata.title=sessionStorage.getItem("schoolYear");
            rawdata.subject=sessionStorage.getItem("subjectName");
            rawdata.classlist=resp.classList;
            rawdata.noramlscorelist=[];
            rawdata.midtermscorelist=[];
            rawdata.endtermscorelist=[];
            for(var i in resp.normalScoreList) {
                if(resp.normalScoreList[i] == null) {
                    rawdata.noramlscorelist[i] = 0;
                } else {
                    rawdata.noramlscorelist[i] = resp.normalScoreList[i];
                }
                if(resp.midtermScoreList[i] == null) {
                    rawdata.midtermscorelist[i] = 0;
                } else {
                    rawdata.midtermscorelist[i] = resp.midtermScoreList[i];
                }
                if(resp.endtermScoreList[i] == null) {
                    rawdata.endtermscorelist[i] = 0;
                } else {
                    rawdata.endtermscorelist[i] = resp.endtermScoreList[i];
                }
            }
            if(null == resp.scoreDTO.usualScore) {
                $("#pscj").text("--");
            } else {
                $("#pscj").text(resp.scoreDTO.usualScore.toFixed(1));
            }
            if(null == resp.scoreDTO.midtermScore) {
                $("#qzcj").text("--");
            } else {
                $("#qzcj").text(resp.scoreDTO.midtermScore.toFixed(1));
            }
            if(null == resp.scoreDTO.finalScore) {
                $("#qmcj").text("--");
            } else {
                $("#qmcj").text(resp.scoreDTO.finalScore.toFixed(1));
            }
            var chardata = chengJi.buildChartData('semester_bjcjdb','',rawdata);
            chengJi.chartInit('cjdbId',chardata);

        })
        requestData.order = 3;
        //成绩统计
        if(requestData.classId && requestData.subjectId) {
            Common.getDataAsync('/score/getSemesterScore.do', requestData, function (resp) {
                //平时成绩
                var rawdata1 = {};
                if(null == resp.avecer) {
                    rawdata1.data1 = 0;
                    rawdata1.data2 = 0;
                } else {
                    rawdata1.data1 = resp.avecpr;
                    rawdata1.data2 = resp.avecer;
                }
                rawdata1.semester = commonData.schoolYear;
                $("#pshgl").html(rawdata1.data1 + "<i>%</i>");
                $("#psyxl").html(rawdata1.data2 + "<i>%</i>");
                var chardata1 = chengJi.buildChartData('cjtj','psjcper',rawdata1);
                chengJi.chartInit('pscjId',chardata1);
                //期中成绩
                var rawdata2 = {};
                if(null == resp.midcpr) {
                    rawdata2.data1 = 0;
                    rawdata2.data2 = 0;
                } else {
                    rawdata2.data1 = resp.midcpr;
                    rawdata2.data2 = resp.midcer;
                }
                rawdata2.semester = commonData.schoolYear;
                $("#qzhgl").html(rawdata2.data1 + "<i>%</i>");
                $("#qzyxl").html(rawdata2.data2 + "<i>%</i>");
                var chardata2 = chengJi.buildChartData('cjtj','midtermper',rawdata2);
                chengJi.chartInit('qzcjId',chardata2);
                //期末成绩
                var rawdata3 = {};
                if(null == resp.endcpr) {
                    rawdata3.data1 = 0;
                    rawdata3.data2 = 0;
                } else {
                    rawdata3.data1 = resp.endcpr;
                    rawdata3.data2 = resp.endcer;
                }
                rawdata3.semester = commonData.schoolYear;
                $("#qmhgl").html(rawdata3.data1 + "<i>%</i>");
                $("#qmyxl").html(rawdata3.data2 + "<i>%</i>");
                var chardata3 = chengJi.buildChartData('cjtj','endtermper',rawdata3);
                chengJi.chartInit('qmcjId',chardata3);
                //学生列表
                tableData = resp.scoreDTOList;
                tmpl = '#subjectScoreListTmpl';
                context = '#scorelist_body';
                chengJi.splitPage(1,10);

            });
        }

        //学生列表,用于排序
        $("#stuListTitle").text(sessionStorage.getItem("schoolYear") + sessionStorage.getItem("className") + sessionStorage.getItem("subjectName") + "成绩");
        sessionStorage.setItem("order",3);
        $("#usualScore").on('click',function(){
            sessionStorage.setItem("order",1);
            chengJi.semesterOrder();
        })
        $("#midScore").on('click',function(){
            sessionStorage.setItem("order",2);
            chengJi.semesterOrder();
        })
        $("#finalScore").on('click',function(){
            sessionStorage.setItem("order",3);
            chengJi.semesterOrder();
        })

    }
    chengJi.semesterOrder = function() {
        var requestData = {};
        requestData.classId = sessionStorage.getItem("classId");
        requestData.subjectId = sessionStorage.getItem("subjectId");
        requestData.order = sessionStorage.getItem("order");
        if(requestData.classId && requestData.subjectId) {

            Common.getDataAsync('/score/getSemesterScoreList.do', requestData, function (resp) {
                //Common.render({
                //    tmpl: '#subjectScoreListTmpl',
                //    data: resp.scoreDTOList,
                //    context: '#scorelist_body',
                //    overwrite: 1
                //});
                tableData = resp.scoreDTOList;
                tmpl = '#subjectScoreListTmpl';
                context = '#scorelist_body';
                chengJi.splitPage(1,10);
            });
        }
    }
//===========================================老师创建考试==============================================
    //创建考试
    chengJi.createClassExam = function() {
        var data = {};
        var name = $("#examName").val();
        var date = $("#examDate").val();
        var fullScore = $("#fullScore").val();
        var failScore = $("#failScore").val();
        var classList=$("input:checkbox:checked").map(function(index,elem) {
                                                                        return $(elem).val();
                                                                        }).get().join(',');

        //var subjectList=$("input[type='radio']:checked").val();
        var subjectList = sessionStorage.getItem("subjectId");
        var temp=/^\d+$/;

        if(!temp.test(fullScore) || !temp.test(failScore)) {
        //if(isNaN(parseInt(fullScore)) || isNaN(parseInt(failScore))) {
            alert("满分、及格分应为整数！");
            return;
        } else if(parseInt(fullScore)<=parseInt(failScore)) {
            alert("满分应大于及格分！");
            return;
        }
        if(name == "" || date == "" || fullScore == "" || failScore == "" || classList == "" || subjectList == null) {
            $("#warningMessage").text("请完善考试信息");
        } else {
            $("#warningMessage").text("");
            data.gradeId = sessionStorage.getItem("gradeId");
            data.name = name;
            data.date = date;
            data.fullScore = fullScore;
            data.failScore = failScore;
            data.classList = classList;
            data.subjectList = subjectList;
            Common.getData('/score/createClassExam.do', data, function (resp) {
                if (resp) {
                    chengJi.getExistExamList();
                }
            })
            $("#examName").val('');
            $("#examDate").val('');
            $("#fullScore").val('');
            $("#failScore").val('');
            $("input:checkbox:checked").map(function(index,elem) {
                $(elem).prop('checked', false);
            });
        }

    }

    //查询已建考试列表
    chengJi.getExistExamList = function() {
        var requestData = {};
        requestData.classId = sessionStorage.getItem("classId");
        requestData.subjectId = sessionStorage.getItem("subjectId");



        if(requestData.classId && requestData.subjectId) {
            requestData.order = sessionStorage.getItem("tIOrder");
            Common.getData('/score/getExamResultList.do', requestData, function (resp) {
                //Common.render({
                //    tmpl: '#examListTmpl',
                //    data: resp.examList,
                //    context: '#existExamList',
                //    overwrite: 1
                //});
                tableData = resp.examList;
                tmpl = '#examListTmpl';
                context =  '#existExamList';
                chengJi.splitPage(1,10);
            });
        }
    }

    //初始化页面
    chengJi.initTeacherInputPage = function() {
        $("#fullScore").change(function(){
            var fullScore = $("#fullScore").val();
            var failScore = fullScore*0.6;
            $("#failScore").val(failScore);
        });
        $("#result_button_BJ").hide();
        $("#teacherInputTitle").text(sessionStorage.getItem("className"));
        var requestData = {};
        //新建考试
        $('#result_button_CJ').on('click',function(){
            chengJi.createClassExam();

        });
        //打分
        $('body').on('click','.inputscore',function(){
            var examId = $(this).attr("examId");
            var examName = $(this).attr("examName");
            sessionStorage.setItem("examId", examId);
            sessionStorage.setItem("examName", examName);
            if (GetQueryString("a")!="10000") {
                Common.goTo("/score/teacher/editscore.do");
            } else {
                Common.goTo("/score/teacher/editscore.do?a=10000");
            }
            //window.location.href="/score/teacher/editscore.do";
        });

        //删除
        $('body').on('click','.delete',function() {
            var isGrade = $(this).attr("isGrade");
            if (isGrade == 1) {
                alert("本考试为年级统一考试，您没有删除权限！如需删除，请联系管理员！")
            } else {
                var msg = "您确定要删除吗？";
                    if (confirm(msg) == true) {
                    var data = {};
                    data.examId = $(this).attr("examId");
                    data.classId = sessionStorage.getItem("classId");
                    data.subjectId = sessionStorage.getItem("subjectId");

                    Common.getData('/score/teacherDeleteExam.do', data, function (resp) {
                        if (resp) {
                            chengJi.getExistExamList();
                        }

                    });
                }
            }
        });

        //编辑回显
        $('body').on('click','.edit',function(){
            var isGrade = $(this).attr("isGrade");
            var flag = false;
            if(isGrade == 0) {
                var msg = "编辑本次考试，将会清除已有的打分记录，确认编辑吗？";
                if (confirm(msg) == true) {
                    flag = true;
                }
            } else {
                flag = true;
            }

            if (flag) {
                $("#result_button_CJ").hide();
                $("#result_button_BJ").show();

                sessionStorage.setItem("isGrade", isGrade);
                var data = {};
                data.examId = $(this).attr("examId");
                sessionStorage.setItem("editExamId", data.examId);
                data.subId = sessionStorage.getItem("subjectId");

                Common.getData('/score/getExamInfo.do', data, function (resp) {
                    $("#examName").val(resp.examName);
                    $("#examDate").val(resp.date);
                    $("#fullScore").val(resp.fullScore);
                    $("#failScore").val(resp.failScore);
                    //var checkBoxAll = $("input[name='class']");
                    var checkBoxAll = $("input:checkbox");
                    var classList = resp.classList;

                    $.each(checkBoxAll, function (j, checkbox) {
                        $(checkbox).prop("checked", false);
                        for (var i in classList) {
                            //获取复选框的value属性
                            var checkValue = $(checkbox).val();
                            if (classList[i] == checkValue) {
                                $(checkbox).prop("checked", true);
                                break;
                            }
                        }
                    })

                });
                if (isGrade == 1) {
                    alert("本考试为年级统一考试，您只能修改满分和及格分！如需修改其他信息，请联系管理员！")
                }
            }
        });

        //编辑考试
        $('#result_button_BJ').on('click',function(){
            var data = {};
            var name = $("#examName").val();
            var date = $("#examDate").val();
            var fullScore = $("#fullScore").val();
            var failScore = $("#failScore").val();
            var classList=$("input:checkbox:checked").map(function(index,elem) {
                return $(elem).val();
            }).get().join(',');
            var subjectList = sessionStorage.getItem("subjectId");
            var temp=/^\d+(\.\d+)?$/;
            if(!temp.test(fullScore) || !temp.test(failScore)) {
                alert("满分、及格分应为数字！");
                return;
            } else if(parseInt(fullScore)<=parseInt(failScore)) {
                alert("满分应大于及格分！");
                return;
            }
            if(name == "" || date == "" || fullScore == "" || failScore == "" || classList == "" || subjectList == null) {
                $("#warningMessage").text("请完善考试信息");
            } else {
                $("#warningMessage").text("");
                data.gradeId = sessionStorage.getItem("gradeId");
                data.name = name;
                data.date = date;
                data.fullScore = fullScore;
                data.failScore = failScore;
                data.classList = classList;
                data.subjectList = subjectList;
                data.examId = sessionStorage.getItem("editExamId");
                data.subjectId = sessionStorage.getItem("subjectId");
                var isGrade = sessionStorage.getItem("isGrade");
                if(isGrade == 1) {
                    Common.getData('/score/teacherEditGradeExam.do', data, function (resp) {
                        if(resp) {
                            chengJi.getExistExamList();
                            $("#result_button_BJ").hide();
                            $("#result_button_CJ").show();
                            alert("编辑成功！")
                        }
                    });
                } else {
                    Common.getData('/score/teacherEditClassExam.do', data, function (resp) {
                        if(resp) {
                            chengJi.getExistExamList();
                            $("#result_button_BJ").hide();
                            $("#result_button_CJ").show();
                            alert("编辑成功！")
                        }
                    });
                }
                $("#examName").val('');
                $("#examDate").val('');
                $("#fullScore").val('');
                $("#failScore").val('');
                $("input:checkbox:checked").map(function(index,elem) {
                    $(elem).prop('checked', false);
                });
            }


        });

        requestData.classId = sessionStorage.getItem("classId");
        requestData.subjectId = sessionStorage.getItem("subjectId");
        requestData.gradeId = sessionStorage.getItem("gradeId");
        //查询班级课程列表
        Common.getData('/score/getClassSubjectSelection.do', requestData, function (resp) {
            Common.render({
                tmpl: '#classListTmpl',
                data: resp,
                context: '#examClassList',
                overwrite: 1
            });

        });
        //取得考试列表
        sessionStorage.setItem("tIOrder", 1);
        $("#examTime").on('click',function(){
            sessionStorage.setItem("tIOrder", 1);
            chengJi.getExistExamList();
        })
        $("#process").on('click',function(){
            sessionStorage.setItem("tIOrder", 2);
            chengJi.getExistExamList();
        })
        chengJi.getExistExamList();

    }

    chengJi.splitPage = function(page,pageSize) {
        var totalNums = tableData.length;//总行数
        var totalPage = Math.ceil(totalNums/pageSize);//总页数
        var begin = (page-1)*pageSize;//页起始位置(包括)
        var end = page*pageSize;//页结束位置(不包括)
        end = end>totalNums?totalNums:end;
        var showData=[];
        for(var i=begin,j=0;i<end;i++,j++) {
            showData[j] = tableData[i];
        }
        Common.render({
            tmpl: tmpl,
            data: showData,
            context: context,
            overwrite: 1
        });
//生成分页工具条
        var pageBar = "第"+page+"页/共"+totalPage+"页"+" ";
        if(page>1){
            pageBar += "<a href=\"javascript:splitPage("+1+","+pageSize+");\">首页</a> ";
        }else{
            pageBar += "首页 ";
        }
        if(page>1){
            pageBar += "<a href=\"javascript:splitPage("+(page-1)+","+pageSize+");\">上一页</a> ";
        }else{
            pageBar += "上一页 ";
        }
        if(page<totalPage){
            pageBar += "<a href=\"javascript:splitPage("+(page+1)+","+pageSize+");\">下一页</a> ";
        }else{
            pageBar += "下一页 ";
        }
        if(page<totalPage){
            pageBar += "<a href=\"javascript:splitPage("+(totalPage)+","+pageSize+");\">尾页</a> ";
        }else{
            pageBar += "尾页 ";
        }
        document.getElementById("page_bar").innerHTML = pageBar;
    }

    var dataStyle = {
        normal: {
            label: {show:false},
            labelLine: {show:false}
        }
    };
    var placeHolderStyle = {
        normal : {
            color: 'rgba(0,0,0,0)',
            label: {show:false},
            labelLine: {show:false}

        },
        emphasis : {
            color: 'rgba(0,0,0,0)'

        }
    };

    chengJi.buildChartData = function(type, subtype,rawdata){
        //班级考试成绩对比
        if(type == 'exam_bjksdb')
        {
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#9bca63'],
                title : {
                    text: rawdata.title,
                    subtext:rawdata.exam,
                    textStyle : {
                        color : 'rgba(75,171,217,0.8)',
                        fontFamily : '微软雅黑',
                        fontSize : 14
                    }
                },
                tooltip: {
                    show: true
                },
                legend: {
                    data:[rawdata.subject]
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                xAxis : [
                    {
                        type : 'category',
                        data : rawdata.classlist,
                        color:['red','white']
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        "name":rawdata.subject,
                        "type":"bar",
                        "data":rawdata.classscore,
                        "markLine":{
                            "data":[
                                // 纵轴，默认
                                {type : 'average', name : '平均值', itemStyle:{normal:{color:'#9bca63'}}},
                            ]
                        }
                    }
                ]
            };
        }

        //班级成绩分布
        if(type == 'exam_bjcjfb'){
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#2ec7c9'],
                title : {
                    text: rawdata.title,
                    subtext: rawdata.exam,
                    textStyle : {
                        color : 'rgba(75,171,217,0.8)',
                        fontFamily : '微软雅黑',
                        fontSize : 14
                    }
                },
                tooltip: {
                    show: true
                },
                legend: {
                    data:['人数']
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                xAxis : [
                    {
                        type : 'category',
                        data : rawdata.category
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        "name":'人数',
                        "type":"bar",
                        "data":rawdata.countlist
                    }
                ]
            };
        }

        //成绩统计
        if(type =='cjtj'){
            var charttitle = '';
            var chardata1 = '';
            var chardata2 = '';
            var max1 = 100;
            var max2 = 100;

            if(subtype=='jf'){
                charttitle = '均分';
                chardata1='班级均分';
                chardata2='年级均分';
            }else if(subtype == 'bjperc'){
                charttitle='班级成绩';
                chardata1='班合格率';
                chardata2='班优秀率';
            }else if(subtype =='njperc'){
                charttitle='年级成绩';
                chardata1='年级合格率';
                chardata2='年级优秀率';
            }else if(subtype=='psjcper'){
                charttitle='平时成绩';
                chardata1='班合格率';
                chardata2='班优秀率';
            }
            else if(subtype=='midtermper'){
                charttitle='期中成绩';
                chardata1='班合格率';
                chardata2='班优秀率';
            }
            else if(subtype=='endtermper'){
                charttitle='期末成绩';
                chardata1='班合格率';
                chardata2='班优秀率';
            }
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#2ec7c9','#b6a2de'],
                title: {
                    text: charttitle,
                    subtext: rawdata.semester,
                    x: 'center',
                    y: 'center',
                    itemGap:15,
                    textStyle : {
                        color : 'rgba(57,156,254,0.8)',
                        fontFamily : '微软雅黑',
                        fontSize : 24,
                        fontWeight : 'bolder'
                    }
                },
                tooltip : {
                    show: true,
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 120,
                    y : 0,
                    itemGap:5,
                    data:[chardata1,chardata2]
                },
                series : [
                    {
                        name:'1',
                        type:'pie',
                        clockWise:false,
                        radius : [94, 112],
                        itemStyle : dataStyle,
                        data:[
                            {
                                value:rawdata.data1,
                                name:chardata1
                            },
                            {
                                value:max1-rawdata.data1,
                                name:'invisible',
                                itemStyle : placeHolderStyle,
                                tooltip: {
                                    show: false
                                }
                            }
                        ]
                    },
                    {
                        name:'2',
                        type:'pie',
                        clockWise:false,
                        radius : [76, 94],
                        itemStyle : dataStyle,
                        data:[
                            {
                                value:rawdata.data2,
                                name:chardata2
                            },
                            {
                                value:max2-rawdata.data2,
                                name:'invisible',
                                itemStyle : placeHolderStyle,
                                tooltip: {
                                    show: false
                                }
                            }
                        ]
                    }
                ]
            }
        }

        //学期成绩班级成绩对比
        if(type == 'semester_bjcjdb'){
            var colorList = [
                '#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
                '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'
            ];

            var charData = {
                title: {
                    text: rawdata.title,
                    subtext: rawdata.subject,
                    subtextStyle: {fontSize: 8},
                    textStyle: {color: '#64b6de', fontSize: 12}
                },
                tooltip: {
                    trigger: 'axis',
                    backgroundColor: 'rgba(255,255,255,0.7)',
                    axisPointer: {
                        type: 'shadow'
                    },
                    formatter: function(params) {
                        // for text color
                        var color = colorList[params[0].dataIndex];
                        var res = '<div style="color:' + color + '">';
                        res += '<strong>' + params[0].name + '成绩</strong>'
                        for (var i = 0, l = params.length; i < l; i++) {
                            res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                        }
                        res += '</div>';
                        return res;
                    }
                },
                legend: {
                    x: 'right',
                    data:['平时成绩','期中成绩','期末成绩']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                grid: {
                    y: 80,
                    y2: 40,
                    x2: 40
                },
                xAxis: [
                    {
                        type: 'category',
                        data: rawdata.classlist
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '平时成绩',
                        type: 'bar',
                        data: rawdata.noramlscorelist
                    },
                    {
                        name: '期中成绩',
                        type: 'bar',
                        data: rawdata.midtermscorelist
                    },
                    {
                        name: '期末成绩',
                        type: 'bar',
                        data: rawdata.endtermscorelist
                    }
                ]
            };
        }


        return charData;
    }




    module.exports = chengJi;


})