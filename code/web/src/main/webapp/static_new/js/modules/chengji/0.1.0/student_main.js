define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var Common = require('common');
    var stuMain = {};

    var examSelectionData = {};

    //选择班级
    stuMain.onClassSelected = function() {
        var className=$("#stu_class").find("option:selected").text();
        sessionStorage.setItem("className", className);
        sessionStorage.setItem("classId", examSelectionData[className].classId);
        Common.render({
            tmpl: '#stu_examtmpl',
            data: examSelectionData[className],
            context: '#stu_exam',
            overwrite: 1
        });
        stuMain.onExamSelected();
    }

    //选择考试
    var subList = {};
    stuMain.onExamSelected = function() {
        var examName=$("#stu_exam").find("option:selected").text();
        var examInfo = examSelectionData[sessionStorage.getItem("className")][examName];
        if(examInfo != null){
        subList = examInfo.subjectViewList;
        sessionStorage.setItem("stuName", examInfo.stuName);
        sessionStorage.setItem("schoolYear", examInfo.schoolYear);
        sessionStorage.setItem("examId", examInfo.examId);
        sessionStorage.setItem("examName", examName);
        Common.render({
            tmpl: '#stu_subjecttmpl',
            data: subList,
            context: '#stu_subject',
            overwrite: 1
        });

        stuMain.onSubjectSelected();
        }
    }

    //选择科目
    stuMain.onSubjectSelected = function() {
        var subjectName=$("#stu_subject").find("option:selected").text();
        var subjectId=$("#stu_subject").find("option:selected").attr('id');

        sessionStorage.setItem("subjectName", subjectName);
        sessionStorage.setItem("subjectId", subjectId);

        var requestData = {};
        requestData.classId = sessionStorage.getItem("classId");
        requestData.examId = sessionStorage.getItem("examId");
        requestData.subjectId = sessionStorage.getItem("subjectId");

        //打败了班级、年级百分之多少的同学
        Common.getDataAsync('/score/getClassDefeatRate.do', requestData, function(resp) {
            $("#subject_exam").text(sessionStorage.getItem("subjectName") + sessionStorage.getItem("examName"));
            $("#dbbj").text(resp.classDR + "%");
            $("#dbnj").text(resp.gradeDR + "%");
        })

        //考试成绩，班级均分，年级均分
        Common.getDataAsync('/score/getStuScore.do', requestData, function(resp) {
            if(resp.stuScore == null) {
                resp.stuScore = 0;
            }
            $("#myScore").text(resp.stuScore);
            $("#classScore").text(resp.classScore);
            $("#gradeScore").text(resp.gradeScore);

            var data={};
            data.examName = sessionStorage.getItem("examName");
            data.schoolYear = sessionStorage.getItem("schoolYear");
            data.myScore = resp.stuScore;
            data.classScore = resp.classScore;
            data.gradeScore = resp.gradeScore;
            var charData = buildChartData('score', data);
            chengJi.chartInit('pjfId',charData);

        })
        //班级排名，年级排名
        Common.getDataAsync('/score/getStuRanking.do', requestData, function(resp) {
            $("#classRanking").text(resp.classRanking);
            $("#gradeRanking").text(resp.gradeRanking);
            var data={};
            data.examName = sessionStorage.getItem("examName");
            data.schoolYear = sessionStorage.getItem("schoolYear");
            data.classRanking = resp.classRanking;
            data.gradeRanking = resp.gradeRanking;
            data.classRankingt = '班级排名' + resp.classRanking;
            data.gradeRankingt = '年级排名' + resp.gradeRanking;
            var charData = buildChartData('ranking', data);
            chengJi.chartInit('bhgId',charData);
        })
        //成绩列表
        Common.getDataAsync('/score/getSubScoreList.do', requestData, function(resp) {

            var subNameList = resp.subNameList;
            if(subNameList.length < 3) {
                $("#threeScore").hide();
            } else {
                $("#threeScore").show();

                var data = {};
                var maxScore=0;
                data.stuName = sessionStorage.getItem("stuName");
                data.scoreList = [];
                for (var i in resp.scoreList) {
                    if (resp.scoreList[i] == null) {
                        data.scoreList[i] = 0;
                    } else {
                        data.scoreList[i] = resp.scoreList[i];
                    }
                    if(data.scoreList[i] > maxScore) {
                        maxScore = data.scoreList[i];
                    }
                }

                var txt = '{"indicator":[';
                for (var i = 0; i < subNameList.length; i++) {
                    txt += '{ "text":"' + subNameList[i] + '", "max":' + maxScore + '},';
                }
                txt += '],"radius":70}';
                data.obj = eval("(" + txt + ")");
                var charData = buildChartData('subScoreList', data);
                chengJi.chartInit('njhgId', charData);
                $("#chinese").text(resp.chinese == null ? "--" : resp.chinese);
                $("#math").text(resp.math == null ? "--" : resp.math);
                $("#english").text(resp.english == null ? "--" : resp.english);
            }

        })
    }



    stuMain.initStuMainPage = function(){
        chengJi.init();
        $("#stu_class").on('change',function() {
            stuMain.onClassSelected();
        });
        $("#stu_exam").on('change',function() {
            stuMain.onExamSelected();
        });
        $("#stu_subject").on('change',function() {
            stuMain.onSubjectSelected();
        });


        var data={};
        Common.getData('/score/stuGetExamSelection.do',data,function(resp){
            examSelectionData = resp;
            Common.render({
                tmpl: '#stu_classtmpl',
                data: resp,
                context: '#stu_class'
                //overwrite: 1
            });
            stuMain.onClassSelected();

        });


        //chengJi.chartInit('pjfId',option1);
        //chengJi.chartInit('bhgId',option3);
        //chengJi.chartInit('njhgId',option5);



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

    function buildChartData(type, data) {
        //var charData={};
        if('score' == type) {
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#2ec7c9','#b6a2de'],
                title: {
                    text: data.examName,
                    subtext: data.schoolYear,
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
                    data:['我的考分','班级均分','年级均分']
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
                                value:data.myScore,
                                name:'我的考分'
                            },
                            {
                                value:100,
                                name:'invisible',
                                itemStyle : placeHolderStyle
                            }
                        ]
                    },
                    {
                        name:'2',
                        type:'pie',
                        clockWise:false,
                        radius : [75, 94],
                        itemStyle : dataStyle,
                        data:[
                            {
                                value:data.classScore,
                                name:'班级均分'
                            },
                            {
                                value:100,
                                name:'invisible',
                                itemStyle : placeHolderStyle
                            }
                        ]
                    },
                    {
                        name:'3',
                        type:'pie',
                        clockWise:false,
                        radius : [58, 76],
                        itemStyle : dataStyle,
                        data:[
                            {
                                value:data.gradeScore,
                                name:'年级均分'
                            },
                            {
                                value:100,
                                name:'invisible',
                                itemStyle : placeHolderStyle
                            }
                        ]
                    }
                ]
            }
        } else if('ranking' == type) {
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#2ec7c9','#b6a2de'],
                title: {
                    text: data.examName,
                    subtext: data.schoolYear,
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
                    data:['班级排名','年级排名']
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
                                value:data.classRanking,
                                name:'班级排名'
                            },
                            {
                                value:40,
                                name:'invisible',
                                itemStyle : placeHolderStyle
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
                                value:data.gradeRanking,
                                name:"年级排名"
                            },
                            {
                                value:40,
                                name:'invisible',
                                itemStyle : placeHolderStyle
                            }
                        ]
                    }
                ]
            }
        } else if('subScoreList' == type) {
            var charData = {
                backgroundColor:'#f7f7f7',
                color:['#2ec7c9','#b6a2de','#008acd'],
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    x : 'center',
                    data:[data.stuName]
                },
                toolbox: {
                    show : false,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                polar : [
                    //{
                    //    indicator : [
                    //        {text : '语文', max  : 100},
                    //        {text : '数学', max  : 100},
                    //        {text : '英语', max  : 100},
                    //        {text : '英语', max  : 100},
                    //        {text : '英语', max  : 100},
                    //        {text : '英语', max  : 100}
                    //    ],
                    //    radius : 70
                    //}
                    data.obj
                ],
                series : [
                    {
                        name: '成绩数据',
                        type: 'radar',
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default'
                                }
                            }
                        },
                        data : [
                            {
                                value : data.scoreList,
                                name : data.stuName
                            }
                        ]
                    }
                ]
            }
        }
        return charData;
    }
    module.exports = stuMain;
})