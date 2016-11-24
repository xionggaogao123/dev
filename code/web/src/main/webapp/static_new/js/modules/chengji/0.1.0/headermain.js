/**
 * Created by fl on 2015/9/6.
 */
define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var Common = require('common');
    var headermain = {};
    var tmpl;
    var context;


    headermain.init = function(){
        var examSelectionData = {};

        $("#grade_list").on('change', function () {
            getClassList();
        });
        $("#stu_class").on('change', function () {
            onClassSelected();
        });
        $("#stu_exam").on('change', function () {
            onExamSelected();
        });
        $("#stu_subject").on('change', function () {
            onSubjectSelected();
        });

        //默认展示实际分
        sessionStorage.setItem("hundredSystem",2);
        //切换成百分制
        $("#hundred").on('click', function(){
            $("#reality").attr('class', 'cu');
            $("#hundred").attr('class', 'cur');
            sessionStorage.setItem("hundredSystem",1);
            if(sessionStorage.getItem("fullScore") != 100) {
                onExamSelected();
            }
        })
        //切换成实际分
        $("#reality").on('click', function(){
            $("#reality").attr('class', 'cur');
            $("#hundred").attr('class', 'cu');
            sessionStorage.setItem("hundredSystem",2);
            if(sessionStorage.getItem("fullScore") != 100) {
                onExamSelected();
            }
        })

        //查找学校年级列表
        var requestData = {};
        requestData.schoolId = sessionStorage.getItem("schoolId");
        Common.getData('/myschool/gradelist/edu.do', requestData, function (resp) {
            Common.render({
                tmpl: '#grade_list_tmpl',
                data: resp.rows,
                context: '#grade_list',
                overwrite: 1
            });
            getClassList();
        });
        //查找班级列表,考试列表，科目列表
        function getClassList() {
            var requestData = {};
            requestData.schoolId = sessionStorage.getItem("schoolId");
            var gradeId = $("#grade_list").find("option:selected").attr("gradeId");
            var gradeName = $("#grade_list").find("option:selected").text();
            requestData.gradeId = gradeId;
            sessionStorage.setItem("gradeId", gradeId);
            sessionStorage.setItem("gradeName", gradeName);

            Common.getDataAsync('/score/getclasslist.do', requestData, function (resp) {
                examSelectionData = resp;
                Common.render({
                    tmpl: '#stu_classtmpl',
                    data: resp,
                    context: '#stu_class',
                    overwrite: 1
                });
                onClassSelected();

            });
        }

        //选择班级
        function onClassSelected() {
            var className = $("#stu_class").find("option:selected").text();
            sessionStorage.setItem("className", className);

            var data = {};
            if (className != "") {
                data = examSelectionData[className];
                sessionStorage.setItem("classId", examSelectionData[className].classId);
            }
            Common.render({
                tmpl: '#stu_examtmpl',
                data: data,
                context: '#stu_exam',
                overwrite: 1
            });
            onExamSelected();
        }

        //选择考试
        var subList = {};

        function onExamSelected() {
            var examName = $("#stu_exam").find("option:selected").text();
            if (examName != "") {
                var examInfo = examSelectionData[sessionStorage.getItem("className")][examName];
                subList = examInfo.subjectViewList;
                sessionStorage.setItem("schoolYear", examInfo.schoolYear);
                sessionStorage.setItem("examId", examInfo.examId);
                sessionStorage.setItem("examName", examName);
                $(".charts-col").show();
                $("#noExam").hide();
            } else {
                subList = {};
                $(".charts-col").hide();
                $("#noExam").show();
            }

            Common.render({
                tmpl: '#stu_subjecttmpl',
                data: subList,
                context: '#stu_subject',
                overwrite: 1
            });

            onSubjectSelected();
        }

        //选择科目
        function onSubjectSelected() {
            var subjectName = $("#stu_subject").find("option:selected").text();
            var subjectId = $("#stu_subject").find("option:selected").attr('id');

            //生成标题
            $("#totaltitle").text(
                sessionStorage.getItem("gradeName") +
                sessionStorage.getItem("className") +
                subjectName +
                sessionStorage.getItem("examName"));

            var requestData = {};
            requestData.examId = sessionStorage.getItem("examId");
            requestData.subjectId = subjectId;
            requestData.classId = sessionStorage.getItem("classId");

            //从后台取得本次考试的满分
            var fullScore = 100;
            if (requestData.examId && requestData.subjectId) {
                Common.getDataAsync('/score/getFullScore.do', requestData, function (resp) {
                    sessionStorage.setItem("fullScore", resp.fullScore);
                    fullScore = resp.fullScore;
                })

            }

            var hundredSystem = sessionStorage.getItem("hundredSystem");
            //班级成绩对比
            if (requestData.examId && requestData.subjectId) {
                Common.getDataAsync('/score/getClassScoreCompare.do', requestData, function (resp) {


                    var rawdata = {};
                    rawdata.title = sessionStorage.getItem("schoolYear") + sessionStorage.getItem("gradeName") + subjectName + "成绩";
                    rawdata.exam = sessionStorage.getItem("examName");
                    rawdata.subject = subjectName;
                    rawdata.classlist = resp.classList;
                    rawdata.classscore = [];
                    if (fullScore != 100 && hundredSystem == 1) {//百分制
                        for (var i in resp.classScore) {
                            rawdata.classscore[i] = Math.round(resp.classScore[i] * 100 / fullScore);
                        }
                    } else {//实际分
                        rawdata.classscore = resp.classScore;
                    }

                    var chardata = chengJi.buildChartData('exam_bjksdb', '', rawdata);
                    chengJi.chartInit('cjdbId', chardata);
                });
            }

            //考试成绩分布
            if (requestData.examId && requestData.classId && requestData.subjectId) {
                Common.getDataAsync('/score/getClassScoreDistribution.do', requestData, function (resp) {
                    var rawdata = {};
                    rawdata.title = "班级成绩分布";
                    rawdata.exam = subjectName + sessionStorage.getItem("examName");
                    rawdata.countlist = resp.countList;
                    rawdata.category = [];
                    if (fullScore != 100 && hundredSystem == 1) {//百分制
                        rawdata.category[0] = '0-59分';
                        rawdata.category[1] = '60-79分';
                        rawdata.category[2] = '80-89分';
                        rawdata.category[3] = '90-100分';
                    } else {//实际分
                        rawdata.category = resp.category;
                    }
                    var chardata = chengJi.buildChartData('exam_bjcjfb', '', rawdata);
                    chengJi.chartInit('cjfbId', chardata);
                });
            }

            //成绩统计
            //均分
            if (requestData.examId && requestData.classId && requestData.subjectId) {
                Common.getDataAsync('/score/getClassScoreAverage.do', requestData, function (resp) {
                    var data = {};
                    if (fullScore != 100 && hundredSystem == 1) {//百分制
                        data.data1 = Math.round(resp.classScore * 100 / fullScore);
                        data.data2 = Math.round(resp.gradeScore * 100 / fullScore);
                    } else {//实际分
                        data.data1 = resp.classScore;
                        data.data2 = resp.gradeScore;
                    }

                    data.semester = sessionStorage.getItem("schoolYear");
                    $("#bjjf").text(data.data1);
                    $("#njjf").text(data.data2);

                    var chardata = chengJi.buildChartData('cjtj', 'jf', data);
                    chengJi.chartInit('pjfId', chardata);
                });
            }
            //班级成绩
            if (requestData.examId && requestData.classId && requestData.subjectId) {
                Common.getDataAsync('/score/getClassScorePercent.do', requestData, function (resp) {
                    var rawdata = {};
                    rawdata.data1 = resp.cpr;
                    rawdata.data2 = resp.cer;
                    rawdata.semester = sessionStorage.getItem("schoolYear");
                    $("#bjhgl").html(resp.cpr + "<i>%</i>");
                    $("#bjyxl").html(resp.cer + "<i>%</i>");
                    var chardata = chengJi.buildChartData('cjtj', 'bjperc', rawdata);
                    chengJi.chartInit('bhgId', chardata);

                });
            }
            //年级成绩
            if (requestData.examId && requestData.classId && requestData.subjectId) {
                //if (commonData.isGrade) {
                Common.getDataAsync('/score/getGradeScorePercent.do', requestData, function (resp) {
                    var rawdata = {};
                    rawdata.data1 = resp.gpr;
                    rawdata.data2 = resp.ger;
                    rawdata.semester = sessionStorage.getItem("schoolYear");
                    $("#njhgl").html(resp.gpr + "<i>%</i>");
                    $("#njyxl").html(resp.ger + "<i>%</i>");
                    var chardata = chengJi.buildChartData('cjtj', 'njperc', rawdata);
                    chengJi.chartInit('njhgId', chardata);
                });
                //}
            }

            //学生列表
            if (requestData.examId && requestData.classId && requestData.subjectId) {
                requestData.fullScore = fullScore;
                requestData.hundred = hundredSystem;
                Common.getDataAsync('/score/getClassScoreList.do', requestData, function (resp) {
                    tableData = resp.stuScoreDTOList;
                    tmpl = '#exam_scorelisttmpl';
                    context = '#scorelist_body';
                    headermain.splitPage(1, 10);

                });
            }


        }

        chengJi.init();

    }

    headermain.splitPage = function(page,pageSize){
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

    module.exports = headermain;

})
