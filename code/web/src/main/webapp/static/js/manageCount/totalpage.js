
$(function () {
	//var a = document.getElementById('class-search');
	//a.options.add(new Option('全部班级',0));
    var html='<option value="">全校全部班级</option>';
    $("#class-search").html(html);
    changeTimeArea();
    searchTotal();
});

// 路径配置
require.config({
    paths: {
        echarts: '/static/plugins/echarts'
    }
});

function changegreade() {
    var schoolId=$('#schoolid').text();
    var gradeId=$("#grade-search").val();
    var gradeName=$("#grade-search").find("option:selected").text();
    var html='<option value="">'+gradeName+'全部班级</option>';
    if(gradeId!=""){
        $.ajax({
            url:"/manageCount/getGradeClassValue.do",
            data:{
                schoolId:schoolId,
                gradeId:gradeId
            },
            type:"post",
            dataType:"json",
            async:false,
            success:function(data){
                var list = data.classList;
                $.each(list,function(i,item){
                    html+='<option value='+item.id+'>'+item.className+'</option>';
                });
            }
        });
    }
    $("#class-search").html(html);
}

function changeTimeArea(){
    var timeArea=$('#timeArea').val();
    $.ajax({
        url:"/manageCount/getTimeAreaVal.do",
        data:{
            timeArea:timeArea
        },
        type:"post",
        dataType:"json",
        async:false,
        success:function(data){
            $('#dateStart').val(data.dateStart);
            $('#dateEnd').val(data.dateEnd);
        }
    });
}

function searchTotal() {
    MessageBox('图表加载中...', 0);
    //searchVisitor();
    //getpeopletotal();
    //lessontotal();
    schoolTotalData();

}

function schoolTotalData(){
    $.ajax({
        url: "/manageCount/schooltotaldata.do",
        data: {
            'gradeId': $("#grade-search").val(),
            'classId': $('#class-search').val(),
            'schoolid': $('#schoolid').text(),
            'dateStart': $('#dateStart').val(),
            'dateEnd': $('#dateEnd').val()
        },
        type: "post",
        dataType: "json",
        async: false,
        traditional: true,
        success: function (data) {
            // 基于准备好的dom，初始化echarts图表
            var rawdata = {};

            rawdata.title_text = "访问分析";
            rawdata.title_subtext = $("#currTime").val();
            rawdata.time_area=[];
            rawdata.people_count=[];
            var acList=data.acList;
            for(var i in acList) {
                rawdata.time_area[i]=acList[i].createTime;
                rawdata.people_count[i]=acList[i].newCountTotal;
            }
            // 使用
            require(
                [
                    'echarts',
                    'echarts/chart/line',// 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var option;
                    var myChart = ec.init(document.getElementById('main1'));
                    option = {
                        title: {
                            text:  rawdata.title_text,
                            subtext: rawdata.title_subtext,
                            subtextStyle: {fontSize: 8},
                            textStyle: {color: '#64b6de', fontSize: 12}

                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['访问人数分布']
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                mark: {show: true},
                                dataZoom: {show: true},
                                dataView: {show: true},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        dataZoom: {
                            show: true,
                            realtime: true,
                            start: 0,
                            end: 100
                        },
                        xAxis: [
                            {
                                type: 'category',
                                boundaryGap: false,
                                data: rawdata.time_area
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                axisLabel: {
                                    formatter: '{value} 人'
                                }
                            }
                        ],
                        series: [
                            {
                                name: '访问人数分布',
                                type: 'line',
                                symbol: 'none',
                                data: rawdata.people_count
                            }
                        ]
                    };
                    myChart.setOption(option);
                }
            );

            //buildChartData('main1','gltj_fwfx','',rawdata);

            var count = data.tcount + data.scount + data.pcount;
            var teacher="";
            var student="";
            var parent="";
            if (count==0) {
                teacher=0;
                student=0;
                parent=0;
                $('#teacount').html('0('+teacher+'%)');
                $('#stucount').html('0('+student+'%)');
                $('#partcount').html('0('+parent+'%)');
            } else {
                teacher=Math.round(data.tcount * 100 / count);
                student=Math.round(data.scount * 100 / count);
                parent=Math.round(data.pcount * 100 / count);
                $('#teacount').html(data.tcount + '(' + teacher + '%)');
                $('#stucount').html(data.scount + '(' + student + '%)');
                $('#partcount').html(data.pcount + '(' + parent + '%)');
            }

            rawdata.teacher=teacher;
            rawdata.student=student;
            rawdata.parent=parent;
            buildChartData('main2','gltj_fwtj','ryfb',rawdata);

            rawdata.scount=data.scount;
            rawdata.pcount=data.pcount;
            rawdata.tcount=data.tcount;
            buildChartData('main3','gltj_fwtj','rysftj',rawdata);

            $('#lescount').html('总:' + data.teaLessonCount);
            $('#clscount').html('总:' + data.teaClassCount);
            $('#cldcount').html('总:' + data.stuCloudCount);
            $('#sclscount').html('总:' + data.stuClassCount);
            $('#hwcount').html('总:' + data.homeworkCount);
            $('#pcount').html('总:' + data.paperCount);

            rawdata.teaLessonCount=data.teaLessonCount;
            rawdata.teaClassCount=data.teaClassCount;
            buildChartData('main4','gltj_bkzy','lsbktj',rawdata);

            rawdata.stuCloudCount=data.stuCloudCount;
            rawdata.stuClassCount=data.stuClassCount;
            buildChartData('main5','gltj_bkzy','xsgktj',rawdata);

            rawdata.homeworkCount=data.homeworkCount;
            rawdata.paperCount=data.paperCount;
            buildChartData('main6','gltj_bkzy','zytj',rawdata);
        },
        complete: function () {
            ClosePromptDialog();
        }
    });

}

function buildChartData(objId,type, subtype, rawdata){

    require(
        [
            'echarts',
            'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
            'echarts/chart/bar'
        ],
        function (ec) {
            var myChart = ec.init(document.getElementById(objId));
            var charData;
            if(type == 'gltj_fwfx') {
                charData = {
                    title: {
                        text: rawdata.title_text,
                        subtext: rawdata.title_subtext,
                        subtextStyle: {fontSize: 8},
                        textStyle: {color: '#64b6de', fontSize: 12}

                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['访问人数分布']
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataZoom: {show: true},
                            dataView: {show: true},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    calculable: true,
                    dataZoom: {
                        show: true,
                        realtime: true,
                        start: 0,
                        end: 100
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: rawdata.time_area
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} 人'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '访问人数分布',
                            type: 'line',
                            symbol: 'none',
                            data: rawdata.people_count
                        }
                    ]
                };
            }

            if(type == 'gltj_fwtj'){
                if(subtype=='ryfb'){
                    var dataStyle = {
                        normal: {
                            label: {show: false},
                            labelLine: {show: false}
                        }
                    };
                    var placeHolderStyle = {
                        normal: {
                            color: 'rgba(0,0,0,0)',
                            label: {show: false},
                            labelLine: {show: false}
                        },
                        emphasis: {
                            color: 'rgba(0,0,0,0)'
                        }
                    };
                    charData = {
                        title: {
                            text: '人员分布？',
                            subtext: rawdata.title_subtext,
                            sublink: 'http://e.weibo.com/1341556070/AhQXtjbqh',
                            x: 'center',
                            y: 'center',
                            itemGap: 8,
                            textStyle: {
                                color: 'rgba(30,144,255,0.8)',
                                fontFamily: '微软雅黑',
                                fontSize: 19,
                                fontWeight: 'bolder'
                            }
                        },
                        tooltip: {
                            show: false,
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            x: 90,
                            y: 10,
                            itemGap: 14,
                            data: [ rawdata.teacher+ '%老师', rawdata.student + '%学生', rawdata.parent + '%家长'],
                            textStyle: {fontSize: 12},
                            itemWidth: 0,
                            itemHeight: 0
                        },
                        toolbox: {
                            show: false,
                            feature: {
                                mark: {show: true},
                                dataView: {show: true, readOnly: false},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        series: [
                            {
                                name: '1',
                                type: 'pie',
                                clockWise: false,
                                radius: [65, 80],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: rawdata.teacher,
                                        name: rawdata.teacher + '%老师'
                                    },
                                    {
                                        value: 100 - rawdata.teacher,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            },
                            {
                                name: '2',
                                type: 'pie',
                                clockWise: false,
                                radius: [50, 65],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: rawdata.student,
                                        name: rawdata.student + '%学生'
                                    },
                                    {
                                        value: 100 - rawdata.student,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            },
                            {
                                name: '3',
                                type: 'pie',
                                clockWise: false,
                                radius: [35, 50],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: rawdata.parent,
                                        name: rawdata.parent + '%家长'
                                    },
                                    {
                                        value: 100 - rawdata.parent,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            }
                        ]
                    };
                }
                if(subtype=='rysftj'){
                    var zrColor = require('zrender/tool/color');
                    var colorList = [
                        '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
                        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
                    ];
                    var itemStyle = {
                        normal: {
                            color: function (params) {
                                if (params.dataIndex < 0) {
                                    // for legend
                                    return zrColor.lift(
                                        colorList[colorList.length - 1], params.seriesIndex * 0.1
                                    );
                                }
                                else {
                                    // for bar
                                    return zrColor.lift(
                                        colorList[params.dataIndex], params.seriesIndex * 0.1
                                    );
                                }
                            }
                        }
                    };
                    charData = {
                        title: {
                            text: '访问人员身份统计',
                            subtext: rawdata.title_subtext,
                            subtextStyle: {fontSize: 8},
                            textStyle: {color: '#64b6de', fontSize: 12},
                            sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
                        },
                        tooltip: {
                            trigger: 'axis',
                            backgroundColor: 'rgba(255,255,255,0.7)',
                            axisPointer: {
                                type: 'shadow'
                            },
                            formatter: function (params) {
                                // for text color
                                var color = colorList[params[0].dataIndex];
                                var res = '<div style="color:' + color + '">';
                                res += '<strong>' + params[0].name + '</strong>'
                                for (var i = 0, l = params.length; i < l; i++) {
                                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                                }
                                res += '</div>';
                                return res;
                            }
                        },
                        legend: {
                            x: 'right',
                            data: ['人数']
                        },
                        toolbox: {
                            show: false,
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
                            x: 50,
                            y: 50,
                            y2: 40,
                            x2: 40
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['老师','学生', '家长']
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        series: [

                            {
                                name: '人数',
                                type: 'bar',
                                itemStyle: itemStyle,
                                data: [ rawdata.tcount,rawdata.scount, rawdata.pcount]
                            }
                        ]
                    };
                }
            }

            if(type =='gltj_bkzy'){
                var zrColor = require('zrender/tool/color');
                var colorList = [
                    '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
                    '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
                ];
                var itemStyle = {
                    normal: {
                        color: function (params) {
                            if (params.dataIndex < 0) {
                                // for legend
                                return zrColor.lift(
                                    colorList[colorList.length - 1], params.seriesIndex * 0.1
                                );
                            }
                            else {
                                // for bar
                                return zrColor.lift(
                                    colorList[params.dataIndex], params.seriesIndex * 0.1
                                );
                            }
                        }
                    }
                };
                var charttitle = '';
                var legendList=[];
                legendList[0]='总数';
                legendList[1]='新增';
                var xAxisList=[];
                var seriesList=[];
                if(subtype=='lsbktj'){
                    charttitle= '备课统计';
                    xAxisList[0]='备课空间';
                    xAxisList[1]='班级课程';
                    seriesList[0]=rawdata.teaLessonCount;
                    seriesList[1]=rawdata.teaClassCount;
                }
                if(subtype=='xsgktj'){
                    charttitle= '观看统计';
                    xAxisList[0]='云课程';
                    xAxisList[1]='班级课程';
                    seriesList[0]=rawdata.stuCloudCount;
                    seriesList[1]=rawdata.stuClassCount;
                }
                if(subtype=='zytj'){
                    charttitle= '资源统计';
                    legendList[2]='完成';
                    xAxisList[0]='作业';
                    xAxisList[1]='试卷';
                    seriesList[0]=rawdata.homeworkCount;
                    seriesList[1]=rawdata.paperCount;
                }
                charData = {
                    title: {
                        text: charttitle,
                        subtext: rawdata.title_subtext,
                        subtextStyle: {fontSize: 8},
                        textStyle: {color: '#64b6de', fontSize: 12},
                        sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
                    },
                    tooltip: {
                        trigger: 'axis',
                        backgroundColor: 'rgba(255,255,255,0.7)',
                        axisPointer: {
                            type: 'shadow'
                        },
                        formatter: function (params) {
                            // for text color
                            var color = colorList[params[0].dataIndex];
                            var res = '<div style="color:' + color + '">';
                            res += '<strong>' + params[0].name + '</strong>'
                            for (var i = 0, l = params.length; i < l; i++) {
                                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                            }
                            res += '</div>';
                            return res;
                        }
                    },
                    legend: {
                        x: 'right',
                        data: legendList
                    },
                    toolbox: {
                        show: false,
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
                        x: 50,
                        y: 50,
                        y2: 40,
                        x2: 40
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: xAxisList
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '总数',
                            type: 'bar',
                            itemStyle: itemStyle,
                            data: seriesList
                        }
                    ]
                };
            }

            myChart.setOption(charData);
        }

    );
}

/*function searchVisitor() {
    $.ajax({
        url: "/manageCount/accessAnalysis.do",
        data: {
            'gradeId': $("#grade-search").val(),
            'classId': $('#class-search').val(),
            'schoolid': $('#schoolid').text(),
            'dateStart': $('#dateStart').val(),
            'dateEnd': $('#dateEnd').val()
        },
        type: "post",
        dataType: "json",
        async: false,
        traditional: true,
        success: function (data) {
            var time_area=[];
            var people_count=[];
            for(var i in data) {
                time_area[i]=data[i].createTime;
                people_count[i]=data[i].newCountTotal;
            }
            // 使用
            require(
                [
                    'echarts',
                    'echarts/chart/line',// 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var option;
                    var myChart = ec.init(document.getElementById('main1'));
                    option = {
                        title: {
                            text: '访问分析',
                            subtext: $("#currTime").val(),
                            subtextStyle: {fontSize: 8},
                            textStyle: {color: '#64b6de', fontSize: 12}

                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['访问人数分布']
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                mark: {show: true},
                                dataZoom: {show: true},
                                dataView: {show: true},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        dataZoom: {
                            show: true,
                            realtime: true,
                            start: 0,
                            end: 100
                        },
                        xAxis: [
                            {
                                type: 'category',
                                boundaryGap: false,
                                data: time_area
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                axisLabel: {
                                    formatter: '{value} 人'
                                }
                            }
                        ],
                        series: [
                            {
                                name: '访问人数分布',
                                type: 'line',
                                symbol: 'none',
                                data: people_count
                            }
                        ]
                    };
                    myChart.setOption(option);
                }
            );
        }
    });
}
function getpeopletotal() {
    $.ajax({
        url: "/manageCount/peopletotal.do",
        type: "post",
        dataType: "json",
        async: true,
        traditional: true,
        data: {
            'gradeId': $("#grade-search").val(),
            'classId': $('#class-search').val(),
            'schoolid': $('#schoolid').text(),
            'dateStart': $('#dateStart').val(),
            'dateEnd': $('#dateEnd').val()
        },
        success: function (data) {
            var count = data.tcount + data.scount + data.pcount;
            var teacher="";
            var student="";
            var parent="";
            if (count==0) {
                teacher=0;
                student=0;
                parent=0;
                $('#teacount').html('0('+teacher+'%)');
                $('#stucount').html('0('+student+'%)');
                $('#partcount').html('0('+parent+'%)');
            } else {
                teacher=Math.round(data.tcount * 100 / count);
                student=Math.round(data.scount * 100 / count);
                parent=Math.round(data.pcount * 100 / count);
                $('#teacount').html(data.tcount + '(' + teacher + '%)');
                $('#stucount').html(data.scount + '(' + student + '%)');
                $('#partcount').html(data.pcount + '(' + parent + '%)');
            }
            require(
                [
                    'echarts',
                    'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
                    'echarts/chart/bar'
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('main2'));
                    var dataStyle = {
                        normal: {
                            label: {show: false},
                            labelLine: {show: false}
                        }
                    };
                    var placeHolderStyle = {
                        normal: {
                            color: 'rgba(0,0,0,0)',
                            label: {show: false},
                            labelLine: {show: false}
                        },
                        emphasis: {
                            color: 'rgba(0,0,0,0)'
                        }
                    };
                    var option = {
                        title: {
                            text: '人员分布？',
                            subtext: $("#currTime").val(),
                            sublink: 'http://e.weibo.com/1341556070/AhQXtjbqh',
                            x: 'center',
                            y: 'center',
                            itemGap: 8,
                            textStyle: {
                                color: 'rgba(30,144,255,0.8)',
                                fontFamily: '微软雅黑',
                                fontSize: 19,
                                fontWeight: 'bolder'
                            }
                        },
                        tooltip: {
                            show: false,
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            x: 90,
                            y: 10,
                            itemGap: 14,
                            data: [ teacher+ '%老师', student + '%学生', parent + '%家长'],
                            textStyle: {fontSize: 12},
                            itemWidth: 0,
                            itemHeight: 0
                        },
                        toolbox: {
                            show: false,
                            feature: {
                                mark: {show: true},
                                dataView: {show: true, readOnly: false},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        series: [
                            {
                                name: '1',
                                type: 'pie',
                                clockWise: false,
                                radius: [65, 80],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: teacher,
                                        name: teacher + '%老师'
                                    },
                                    {
                                        value: 100 - teacher,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            },
                            {
                                name: '2',
                                type: 'pie',
                                clockWise: false,
                                radius: [50, 65],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: student,
                                        name: student + '%学生'
                                    },
                                    {
                                        value: 100 - student,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            },
                            {
                                name: '3',
                                type: 'pie',
                                clockWise: false,
                                radius: [35, 50],
                                itemStyle: dataStyle,
                                data: [
                                    {
                                        value: parent,
                                        name: parent + '%家长'
                                    },
                                    {
                                        value: 100 - parent,
                                        name: 'invisible',
                                        itemStyle: placeHolderStyle
                                    }
                                ]
                            }
                        ]
                    };
                    myChart.setOption(option);

                    // 基于准备好的dom，初始化echarts图表
                    var myChart2 = ec.init(document.getElementById('main3'));

                    var zrColor = require('zrender/tool/color');
                    var colorList = [
                        '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
                        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
                    ];
                    var itemStyle = {
                        normal: {
                            color: function (params) {
                                if (params.dataIndex < 0) {
                                    // for legend
                                    return zrColor.lift(
                                        colorList[colorList.length - 1], params.seriesIndex * 0.1
                                    );
                                }
                                else {
                                    // for bar
                                    return zrColor.lift(
                                        colorList[params.dataIndex], params.seriesIndex * 0.1
                                    );
                                }
                            }
                        }
                    };
                    option = {
                        title: {
                            text: '访问人员身份统计',
                            subtext: $("#currTime").val(),
                            subtextStyle: {fontSize: 8},
                            textStyle: {color: '#64b6de', fontSize: 12},
                            sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
                        },
                        tooltip: {
                            trigger: 'axis',
                            backgroundColor: 'rgba(255,255,255,0.7)',
                            axisPointer: {
                                type: 'shadow'
                            },
                            formatter: function (params) {
                                // for text color
                                var color = colorList[params[0].dataIndex];
                                var res = '<div style="color:' + color + '">';
                                res += '<strong>' + params[0].name + '</strong>'
                                for (var i = 0, l = params.length; i < l; i++) {
                                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                                }
                                res += '</div>';
                                return res;
                            }
                        },
                        legend: {
                            x: 'right',
                            data: ['人数']
                        },
                        toolbox: {
                            show: false,
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
                            x: 50,
                            y: 50,
                            y2: 40,
                            x2: 40
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: ['老师', '学生', '家长']
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        series: [

                            {
                                name: '人数',
                                type: 'bar',
                                itemStyle: itemStyle,
                                data: [data.tcount, data.scount, data.pcount]
                            }
                        ]
                    };

                    myChart2.setOption(option);
                }
            );


        },
        complete: function () {

        }
    });

}
function lessontotal() {
	 $.ajax({
        url: "/manageCount/lessonCount.do",
        type: "post",
        dataType: "json",
        async: true,
        traditional: true,
        data: {
            'gradeId': $("#grade-search").val(),
            'classId': $('#class-search').val(),
            'schoolid': $('#schoolid').text(),
            'dateStart': $('#dateStart').val(),
            'dateEnd': $('#dateEnd').val()
        },
        success: function (data) {
        	$('#lescount').html('总:' + data.teaLessonCount);
        	$('#clscount').html('总:' + data.teaClassCount);
        	$('#cldcount').html('总:' + data.stuCloudCount);
        	$('#sclscount').html('总:' + data.stuClassCount);
        	$('#hwcount').html('总:' + data.homeworkCount);
        	$('#pcount').html('总:' + data.paperCount);
            // 使用
    require(
    [
        'echarts',
        'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
    	'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main4'));

        var zrColor = require('zrender/tool/color');
        var colorList = [
            '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
            '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
        ];
        var itemStyle = {
            normal: {
                color: function (params) {
                    if (params.dataIndex < 0) {
                        // for legend
                        return zrColor.lift(
                            colorList[colorList.length - 1], params.seriesIndex * 0.1
                        );
                    }
                    else {
                        // for bar
                        return zrColor.lift(
                            colorList[params.dataIndex], params.seriesIndex * 0.1
                        );
                    }
                }
            }
        };
        var option;
        option = {
            title: {
                text: '备课统计',
                subtext: $("#currTime").val(),
                subtextStyle: {fontSize: 8},
                textStyle: {color: '#64b6de', fontSize: 12},
                sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
            },
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(255,255,255,0.7)',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    // for text color
                    var color = colorList[params[0].dataIndex];
                    var res = '<div style="color:' + color + '">';
                    res += '<strong>' + params[0].name + '</strong>'
                    for (var i = 0, l = params.length; i < l; i++) {
                        res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                    }
                    res += '</div>';
                    return res;
                }
            },
            legend: {
                x: 'right',
                data: ['总数', '新增']
            },
            toolbox: {
                show: false,
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
                x: 50,
                y: 50,
                y2: 40,
                x2: 40
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['备课空间', '班级课程']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [

                {
                    name: '总数',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [data.teaLessonCount, data.teaClassCount]
                }
            ]
        };

        myChart.setOption(option);
        
        // 基于准备好的dom，初始化echarts图表
        var myChart5 = ec.init(document.getElementById('main5'));

        var zrColor = require('zrender/tool/color');
        var colorList = [
            '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
            '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
        ];
        var itemStyle = {
            normal: {
                color: function (params) {
                    if (params.dataIndex < 0) {
                        // for legend
                        return zrColor.lift(
                            colorList[colorList.length - 1], params.seriesIndex * 0.1
                        );
                    }
                    else {
                        // for bar
                        return zrColor.lift(
                            colorList[params.dataIndex], params.seriesIndex * 0.1
                        );
                    }
                }
            }
        };
        option = {
            title: {
                text: '观看统计',
                subtext: $("#currTime").val(),
                subtextStyle: {fontSize: 8},
                textStyle: {color: '#64b6de', fontSize: 12},
                sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
            },
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(255,255,255,0.7)',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    // for text color
                    var color = colorList[params[0].dataIndex];
                    var res = '<div style="color:' + color + '">';
                    res += '<strong>' + params[0].name + '</strong>'
                    for (var i = 0, l = params.length; i < l; i++) {
                        res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                    }
                    res += '</div>';
                    return res;
                }
            },
            legend: {
                x: 'right',
                data: ['总数', '新增']
            },
            toolbox: {
                show: false,
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
                x: 50,
                y: 50,
                y2: 40,
                x2: 40
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['云课程', '班级课程']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [

                {
                    name: '总数',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [data.stuCloudCount, data.stuClassCount]
                }
            ]
        };

        myChart5.setOption(option);
        
        // 基于准备好的dom，初始化echarts图表
        var myChart6 = ec.init(document.getElementById('main6'));

        var zrColor = require('zrender/tool/color');
        var colorList = [
            '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
            '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
        ];
        var itemStyle = {
            normal: {
                color: function (params) {
                    if (params.dataIndex < 0) {
                        // for legend
                        return zrColor.lift(
                            colorList[colorList.length - 1], params.seriesIndex * 0.1
                        );
                    }
                    else {
                        // for bar
                        return zrColor.lift(
                            colorList[params.dataIndex], params.seriesIndex * 0.1
                        );
                    }
                }
            }
        };
        option = {
            title: {
                text: '资源统计',
                subtext: $("#currTime").val(),
                subtextStyle: {fontSize: 8},
                textStyle: {color: '#64b6de', fontSize: 12},
                sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
            },
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(255,255,255,0.7)',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    // for text color
                    var color = colorList[params[0].dataIndex];
                    var res = '<div style="color:' + color + '">';
                    res += '<strong>' + params[0].name + '</strong>'
                    for (var i = 0, l = params.length; i < l; i++) {
                        res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                    }
                    res += '</div>';
                    return res;
                }
            },
            legend: {
                x: 'right',
                data: ['总数', '新增', '完成']
            },
            toolbox: {
                show: false,
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
                x: 50,
                y: 50,
                y2: 40,
                x2: 40
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['作业', '试卷']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [

                {
                    name: '总数',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [data.homeworkCount, data.paperCount]
                }
            ]
        };

        myChart6.setOption(option);
    });
        },
        complete: function () {

        }
    });
}*/
