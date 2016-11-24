define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var Common = require('common');
    var stuHistory = {};
    var tableData = [];
    var tmpl = '';
    var context = '';

    stuHistory.initPage = function(){
        chengJi.init();

        $("#schoolYearSelect").on('change',function() {
            onSelected();
        });
        $("#subjectSelect").on('change',function() {
            onSelected();
        });

        sessionStorage.setItem("order", 0);
        $("#score").on("click", function(){
            sessionStorage.setItem("order", 1);
            scoreList();
        })
        $("#classScore").on("click", function(){
            sessionStorage.setItem("order", 2);
            scoreList();
        })
        $("#classRanking").on("click", function(){
            sessionStorage.setItem("order", 3);
            scoreList();
        })
        var requestData = {};
        requestData.classId = sessionStorage.getItem("classId");
        //选择列表
        Common.getData('/score/getSemesterSelection.do',requestData, function(resp) {
            Common.render({
                tmpl: '#schoolYearSelectTmpl',
                data: resp.schoolYear,
                context: '#schoolYearSelect',
                overwrite: 1
            });
            Common.render({
                tmpl: '#subjectSelectTmpl',
                data: resp.subjectViewList,
                context: '#subjectSelect',
                overwrite: 1
            });
            onSelected();
        });

        function onSelected() {
            var schoolYear = $("#schoolYearSelect").find("option:selected").text();
            var subjectId = $("#subjectSelect").find("option:selected").attr('value');
            var subjectName = $("#subjectSelect").find("option:selected").text();
            requestData.schoolYear = schoolYear;
            requestData.subjectId = subjectId;
            requestData.order = sessionStorage.getItem("order");
            Common.getData('/score/getHistorySubjectScore.do',requestData, function(resp) {
                //成绩表
                //Common.render({
                //    tmpl: '#examListTmpl',
                //    data: resp.subExamList,
                //    context: '#examList',
                //    overwrite: 1
                //});
                tableData = resp.subExamList;
                tmpl = '#examListTmpl';
                context = '#examList';
                stuHistory.splitPage(1,12);

                //图表
                var stuScoreList = [];
                var classScoreList = [];
                for(var i in resp.stuScoreList) {
                    if(resp.stuScoreList[i] == null) {
                        stuScoreList[i] = 0;
                    } else {
                        stuScoreList[i] = resp.stuScoreList[i];
                    }
                    if(resp.classScoreList[i] == null) {
                        classScoreList[i] = 0;
                    } else {
                        classScoreList[i] = resp.classScoreList[i];
                    }
                }

                var option = {
                    title : {
                        text: schoolYear + subjectName + '考试汇总',
                        subtext: subjectName + '考试',
                        subtextStyle: {fontSize: 8},
                        textStyle: {color: '#64b6de', fontSize: 12}
                    },
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data:['个人成绩','班级均分']
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
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            boundaryGap : false,
                            data : resp.examNameList
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value} 分'
                            }
                        }
                    ],
                    series : [
                        {
                            name:'个人成绩',
                            type:'line',
                            data:stuScoreList,
                            markPoint : {
                                data : [
                                    {type : 'max', name: '最大值'},
                                    {type : 'min', name: '最小值'}
                                ]
                            },
                            markLine : {
                                data : [
                                    {type : 'average', name: '平均值'}
                                ]
                            }
                        },
                        {
                            name:'班级均分',
                            type:'line',
                            data:classScoreList,
                            markPoint : {
                                data : [
                                    {type : 'max', name: '最大值'},
                                    {type : 'min', name: '最小值'}
                                ]
                            },
                            markLine : {
                                data : [
                                    {type : 'average', name : '平均值'}
                                ]
                            }
                        }
                    ]
                };

                chengJi.chartInit('cjdbId',option);



            });



        }

        function scoreList() {
            requestData.order = sessionStorage.getItem("order");
            Common.getData('/score/getHistorySubjectScore.do',requestData, function(resp) {
                //成绩表
                //Common.render({
                //    tmpl: '#examListTmpl',
                //    data: resp.subExamList,
                //    context: '#examList',
                //    overwrite: 1
                //});
                tableData = resp.subExamList;
                tmpl = '#examListTmpl';
                context = '#examList';
                stuHistory.splitPage(1,12);
            });
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


    };

    stuHistory.splitPage = function(page,pageSize) {
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

    module.exports = stuHistory;

})
