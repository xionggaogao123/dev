/**
 * Created by fl on 2015/9/7.
 */
define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var headersemester = {};
    var Common = require('common');
    var tmpl;
    var context;
    var subjectList = {};
    var commonData = {};
    var tableData=[];

    headersemester.init = function(){
        //改变年级
        $("#grade_list").on('change', function () {
            var gradeName = $("#grade_list").find("option:selected").text();
            sessionStorage.setItem("gradeName", gradeName);
            getSubjectList();
            getClassList();
            showData();
        });
        //改变班级
        $("#stu_class").on('change', function () {
            var classId = $("#stu_class").find("option:selected").attr('id');
            sessionStorage.setItem("classId", classId);
            var className = $("#stu_class").find("option:selected").text();
            sessionStorage.setItem("className", className);
            showData();
        });
        //改变科目
        $('body').on('click','.subject', function(){
            $(".semester_grades_F").removeClass('semester_grades_F');
            $(this).addClass('semester_grades_F');
            var subjectId = $(this).attr('subjectId');
            sessionStorage.setItem("subjectId", subjectId);
            var subjectName = $(this).text();
            sessionStorage.setItem("subjectName", subjectName);
            showData();
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
            getSubjectList();
            getClassList();
            showData();
        });

        function getSubjectList() {
            var requestData = {};
            requestData.schoolId = sessionStorage.getItem("schoolId");
            var gradeId = $("#grade_list").find("option:selected").attr("gradeId");
            requestData.gradeId = gradeId;
            Common.getData('/score/getSubjectList.do', requestData, function (resp) {
                if(resp){
                    subjectList = resp.subjectViewList;
                    if(subjectList.length>0) {
                        showSubject(0);
                        $('#subjectList li').first().addClass('semester_grades_F');
                        var subjectId = $('#subjectList li').first().attr("subjectId");
                        sessionStorage.setItem("subjectId", subjectId);
                        sessionStorage.setItem("subjectName",$('#subjectList li').first().text());
                        $('.charts-col').show();
                        $('#noExam').hide();
                    } else {
                        $('.charts-col').hide();
                        $('#noExam').show();
                    }
                }
            });
        }

        function showSubject(index) {
            sessionStorage.setItem("index", index);
            var showSubject = {};
            var size = subjectList.length>3 ? 3 :subjectList.length;
            for(var i=index, j=0; i<index+size; i++, j++) {
                showSubject[j] = subjectList[i];
            }
            Common.render({
                tmpl: '#subjectList_tmpl',
                data: showSubject,
                context: '#subjectList',
                overwrite: 1
            });
        }

        //学科左移
        $('.semester_grades_left').on('click', function(){
            if(sessionStorage.getItem("index")<1)
                return;
            var index = sessionStorage.getItem("index")-1;
            showSubject(index);
        })

        //学科右移
        $('.semester_grades_right').on('click', function(){
            if(sessionStorage.getItem("index")>=subjectList.length-3)
                return;
            var index = sessionStorage.getItem("index")-0+1;
            showSubject(index);
        })

        //查找班级列表
        function getClassList() {
            var requestData = {};
            requestData.schoolId = sessionStorage.getItem("schoolId");
            var gradeId = $("#grade_list").find("option:selected").attr("gradeId");
            var gradeName = $("#grade_list").find("option:selected").text();
            requestData.gradeId = gradeId;
            sessionStorage.setItem("gradeId", gradeId);
            sessionStorage.setItem("gradeName", gradeName);

            Common.getData('/score/getclasslist1.do', requestData, function (resp) {
                Common.render({
                    tmpl: '#stu_classtmpl',
                    data: resp.classList,
                    context: '#stu_class',
                    overwrite: 1
                });
                if(resp.classList!=null){
                    $('.charts-col').show();
                    $('#noExam').hide();
                } else {
                    $('.charts-col').hide();
                    $('#noExam').show();
                }

            });
        }

        function showData(){
            var requestData = {};
            //查找当前学期
            sessionStorage.setItem("schoolYear", getCurrentTerm());


            $("#title").text(sessionStorage.getItem("schoolYear") + sessionStorage.getItem("gradeName") + "成绩分析")
            //班级成绩对比
            requestData.classId = $("#stu_class").find("option:selected").attr('id');
            requestData.subjectId = $(".semester_grades_F").attr('subjectId');
            var className = $("#stu_class").find("option:selected").text();
            if(requestData.classId && requestData.subjectId){
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

                    var chardata = chengJi.buildChartData('semester_bjcjdb','',rawdata);
                    chengJi.chartInit('cjdbId',chardata);
                })
            }
            //各个科目成绩列表
            requestData.gradeId = sessionStorage.getItem("gradeId");
            requestData.schoolId = sessionStorage.getItem("schoolId");
            if(requestData.classId && requestData.gradeId){
                Common.getDataAsync('/score/getSubjectScoreList.do', requestData, function (resp) {
                    Common.render({
                        tmpl: '#scoreDTOList_tmpl',
                        data: resp.scoreDTOList,
                        context: '#scoreDTOList',
                        overwrite: 1
                    });
                    $("#scoreDTOListTitle").text(sessionStorage.getItem("schoolYear") + className + "各科成绩表");
                })
            }

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
                    chengJi.chartInit('pjfId',chardata1);

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
                    chengJi.chartInit('bhgId',chardata2);

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
                    chengJi.chartInit('njhgId',chardata3);
                    //学生列表
                    tableData = resp.scoreDTOList;
                    tmpl = '#subjectScoreListTmpl';
                    context = '#scorelist_body';
                    headersemester.splitPage(1,10);
                    $("#stuListTitle").text(sessionStorage.getItem("schoolYear") + className +
                                            sessionStorage.getItem("subjectName") + "成绩单");
                });
            }
            //各个科目及格率、优秀率
            Common.getDataAsync('/score/getSubjectRateList.do', requestData, function (resp) {
                Common.render({
                    tmpl: '#subjectRateDTOList_tmpl',
                    data: resp.subjectRateDTOList,
                    context: '#subjectRateDTOList',
                    overwrite: 1
                });
                $("#subjectRateDTOListTitle").text(sessionStorage.getItem("schoolYear") + className + "各科成绩表");
            })


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

        sessionStorage.setItem("order",3);
        $("#usualScore").on('click',function(){
            sessionStorage.setItem("order",1);
            headersemester.semesterOrder();
        })
        $("#midScore").on('click',function(){
            sessionStorage.setItem("order",2);
            headersemester.semesterOrder();
        })
        $("#finalScore").on('click',function(){
            sessionStorage.setItem("order",3);
            headersemester.semesterOrder();
        })




        chengJi.init();



    }

    headersemester.semesterOrder = function() {
        var requestData = {};
        requestData.classId = $("#stu_class").find("option:selected").attr('id');
        requestData.subjectId = $(".semester_grades_F").attr('subjectId');
        requestData.order = sessionStorage.getItem("order");
        if(requestData.classId && requestData.subjectId) {

            Common.getData('/score/getSemesterScoreList.do', requestData, function (resp) {
                tableData = resp.scoreDTOList;
                tmpl = '#subjectScoreListTmpl';
                context = '#scorelist_body';
                headersemester.splitPage(1,10);
            });
        }
    }

    headersemester.splitPage = function(page,pageSize) {
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

    module.exports = headersemester;

})
