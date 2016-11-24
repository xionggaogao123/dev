/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define(['jquery','doT','easing','common','fancybox','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var lessonclass = {},
        Common = require('common');
    var lessonclassData = {};
    lessonclass.init = function() {


        lessonclass.initPic();
        lessonclass.lessonActiviness();
        $("#xuesheng").click(function(event) {
            $(this).addClass("sta-cur").siblings().removeClass("sta-cur");
            $('.sta-xingwei').hide();
            $('.sta-kaoshi').hide();
            $('.sta-dati').hide();
            $(".lanclass-hov-dati").hide();
            $('.sta-ketang').hide();
            lessonclassData.type = 3;
            lessonclassData.times = $('#stucnt').val();
            lessonclass.getLessonUploadFileList();
        });
        $("#ketang").click(function(event) {
            $(this).addClass("sta-cur").siblings().removeClass("sta-cur");
            $('.sta-xingwei').hide();
            $('.sta-kaoshi').hide();
            $('.sta-dati').hide();
            $(".lanclass-hov-dati").hide();
            $('.sta-xuesheng').hide();
            lessonclassData.type = 1;
            lessonclassData.times = 0;
            lessonclass.getLessonUploadFileList();
        });
        $("#xingwei").click(function(event) {
            $(this).addClass("sta-cur").siblings().removeClass("sta-cur");
            $('.sta-kaoshi').hide();
            $('.sta-dati').hide();
            $(".lanclass-hov-dati").hide();
            $('.sta-ketang').hide();
            $('.sta-xuesheng').hide();
            lessonclass.lessonActiviness();
        });
        $("#dati").click(function(event) {
            $(this).addClass("sta-cur").siblings().removeClass("sta-cur");
            $('.sta-xingwei').hide();
            $('.sta-kaoshi').hide();
            $('.sta-ketang').hide();
            $('.sta-xuesheng').hide();
            $('#answer').html($('#answertime').find("option:selected").text()+"详情");
            $('#answer2').html($('#answertime').find("option:selected").text());
            lessonclassData.classId = $('#classid').val();
            lessonclassData.times = $('#answertime').val();
            lessonclass.getStuQuickAnswerTextList();
        });
        $("#kaoshi").click(function(event) {
            $(this).addClass("sta-cur").siblings().removeClass("sta-cur");
            $('.sta-xingwei').hide();
            $('#examdec').html($('#examlist').find("option:selected").text()+"试卷详情");
            $('.sta-dati').hide();
            $(".lanclass-hov-dati").hide();
            $('.sta-ketang').hide();
            $('.sta-xuesheng').hide();
            lessonclassData.classId = $('#classid').val();
            lessonclassData.times = $('#examlist').val();
            lessonclass.getExamList();
        });
        $("#dtms").click(function(event) {
            $(this).addClass("kaoshi-cur").siblings().removeClass("kaoshi-cur");
            $('.sta-zjms').hide();
            $('.sta-dtms').show();
            lessonclassData.times = $('#examlist').val();
            lessonclass.getExamQuestionNumberList();
            $("#examlist").css("display", "none");
            lessonclassData.classId = $('#classid').val();
            lessonclassData.number = $('#sigleExamlist').val();
            lessonclass.getSingleExamList();
        });
        $("#zjms").click(function(event) {
            $(this).addClass("kaoshi-cur").siblings().removeClass("kaoshi-cur");
            $('.sta-zjms').show();
            $('.sta-dtms').hide();
            $("#examlist").css("display", "block");
            $("#sigleExamlist").css("display", "none");
            lessonclassData.classId = $('#classid').val();
            lessonclassData.times = $('#examlist').val();
            lessonclass.getExamList();
        });
        $('#stucnt').on('change',function(){
            lessonclassData.type = 3;
            lessonclassData.times = $('#stucnt').val();
            lessonclass.getLessonUploadFileList();
        });
        $('#answertime').on('change',function(){
            $('#answer').html($('#answertime').find("option:selected").text()+"详情");
            $('#answer2').html($('#answertime').find("option:selected").text());
            lessonclassData.classId = $('#classid').val();
            lessonclassData.times = $('#answertime').val();
            lessonclass.getStuQuickAnswerTextList();
        });
        $('#examlist').on('change',function(){
            lessonclassData.classId = $('#classid').val();
            lessonclassData.times = $('#examlist').val();
            lessonclass.getExamList();
        });
        $('#sigleExamlist').on('change',function(){
            lessonclassData.times = $('#examlist').val();
            lessonclassData.classId = $('#classid').val();
            lessonclassData.number = $('#sigleExamlist').val();
            lessonclass.getSingleExamList();
        });

    };

    lessonclass.getExamQuestionNumberList = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        Common.getPostData('/interactLesson/getExamQuestionNumberList.do', lessonclassData,function(rep) {
            if(rep.length>0){
                $("#sigleExamlist").css("display", "block");
                $('.sigleExam').html('');
                Common.render({tmpl: $('#sigleExam_templ'), data: rep, context: '.sigleExam'});
            }else{
                $("#sigleExamlist").css("display", "none");
            }
        });
    }

    lessonclass.chartInit=function(id,opt)
    {
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    };

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

    lessonclass.buildChartData = function(type,rep,index){
        var charData = {};
        if (type=='xwtj') {
            var stuName=[];
            var scoreTotal=[];
            if (rep.scoreTotal!=null && rep.scoreTotal.length!=0) {
                for (var i=0;i<rep.scoreTotal.length;i++) {
                    stuName[i] = rep.scoreTotal[i].stuName;
                    scoreTotal[i] = rep.scoreTotal[i].scoreTotal;
                }
            }
            charData = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: function (params) {

                        var res = '<div>';
                        res += '<strong>' + params[0].name + '</strong>'
                        for (var i = 0, l = params.length; i < l; i++) {
                            res += '<br/>' + params[i].seriesName + ' : ' + params[i].value
                        }
                        res += '</div>';
                        return res;

                        //return params[0].name + '<br/>';
                           // + params[0].seriesName + ' : ' + params[0].value + '<br/>'
                            //+ params[1].seriesName + ' : ' + (params[1].value + params[0].value);
                    }
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: false},
                        dataView: {show: false, readOnly: false},
                        restore: {show: false},
                        saveAsImage: {show: false}
                    }
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: stuName
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        boundaryGap: [0, 0.1]
                    }
                ],
                series: [
                    {
                        name: '活跃度',
                        type: 'bar',
                        stack: 'sum',
                        barCategoryGap: '50%',
                        itemStyle: {
                            normal: {
                                color: 'tomato',
                                barBorderColor: 'tomato',
                                barBorderWidth: 6,
                                barBorderRadius: 0,
                                label: {
                                    show: true, position: 'insideTop'
                                }
                            }
                        },
                        data: scoreTotal
                    },
                    /*{
                        name: 'Forecast',
                        type: 'bar',
                        stack: 'sum',
                        itemStyle: {
                            normal: {
                                *//* color: '#fff',*//*
                                barBorderColor: 'tomato',
                                barBorderWidth: 6,
                                barBorderRadius: 0,
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: function (params) {
                                        for (var i = 0, l = charData.xAxis[0].data.length; i < l; i++) {
                                            if (charData.xAxis[0].data[i] == params.name) {
                                                return charData.series[0].data[i] + params.value;
                                            }
                                        }
                                    },
                                    textStyle: {
                                        color: 'tomato'
                                    }
                                }
                            }
                        },
                        data: []
                    }*/
                ]
            };
        }
        //else if(type=='bjtj') {
        //    charData = {
        //        title: {
        //            x: 'center',
        //            /* text: 'ECharts例子个数统计',
        //             subtext: 'Rainbow bar example',*/
        //            link: 'http://echarts.baidu.com/doc/example.html'
        //        },
        //        tooltip: {
        //            trigger: 'item'
        //        },
        //        toolbox: {
        //            show: true,
        //            feature: {
        //                dataView: {show: false, readOnly: false},
        //                restore: {show: false},
        //                saveAsImage: {show: false}
        //            }
        //        },
        //        calculable: true,
        //        grid: {
        //            borderWidth: 0,
        //            y: 80,
        //            y2: 60
        //        },
        //        xAxis: [
        //            {
        //                type: 'category',
        //                show: true,
        //                data: ['', '', '优秀', '', '', '良好', '', '', '不及格', '', '']
        //                //data: ['', '', '优秀'+"("+rep.excellentRate+"%)", '', '', '良好'+"("+rep.goodRate+"%)", '', '', '不及格'+"("+rep.failureRate+"%)", '', '']
        //            }
        //        ],
        //        yAxis: [
        //            {
        //                type: 'value',
        //                show: true
        //            }
        //        ],
        //        series: [
        //            {
        //                /* name: 'ECharts例子个数统计',*/
        //                type: 'bar',
        //                itemStyle: {
        //                    normal: {
        //                        color: function (params) {
        //                            // build a color map as your need.
        //                            var colorList = [
        //                                '#C1232B', '#B5C334', '#f0c49c', '#E87C25', '#27727B',
        //                                '#f096aa', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
        //                                '#D7504B', '#C6E579', '#10baf5', '#F0805A', '#26C0C0'
        //                            ];
        //                            return colorList[params.dataIndex]
        //                        },
        //                        label: {
        //                            show: true,
        //                            position: 'top',
        //                            formatter: '{b}\n{c}'
        //                        }
        //                    }
        //                },
        //                data: [, , rep.excellentNum, , , rep.goodNum, , , rep.failureNum, ,],
        //                markPoint: {
        //                    tooltip: {
        //                        trigger: 'item',
        //                        backgroundColor: 'rgba(0,0,0,0)',
        //                        formatter: function (params) {
        //                            return '<img src="'
        //                                + params.data.symbol.replace('image://', '')
        //                                + '"/>';
        //                        }
        //                    },
        //                    data: [
        //                        {xAxis: 0, y: 350, name: 'Line', symbolSize: 20, symbol: 'image://../asset/ico/折线图.png'},
        //                        {xAxis: 1, y: 350, name: 'Bar', symbolSize: 20, symbol: 'image://../asset/ico/柱状图.png'},
        //                        {xAxis: 2, y: 350, name: 'Scatter', symbolSize: 20, symbol: 'image://../asset/ico/散点图.png'},
        //                        {xAxis: 3, y: 350, name: 'K', symbolSize: 20, symbol: 'image://../asset/ico/K线图.png'},
        //                        {xAxis: 4, y: 350, name: 'Pie', symbolSize: 20, symbol: 'image://../asset/ico/饼状图.png'},
        //                        {xAxis: 5, y: 350, name: 'Radar', symbolSize: 20, symbol: 'image://../asset/ico/雷达图.png'},
        //                        {xAxis: 6, y: 350, name: 'Chord', symbolSize: 20, symbol: 'image://../asset/ico/和弦图.png'},
        //                        {xAxis: 7, y: 350, name: 'Force', symbolSize: 20, symbol: 'image://../asset/ico/力导向图.png'},
        //                        {xAxis: 8, y: 350, name: 'Map', symbolSize: 20, symbol: 'image://../asset/ico/地图.png'},
        //                        {xAxis: 9, y: 350, name: 'Gauge', symbolSize: 20, symbol: 'image://../asset/ico/仪表盘.png'},
        //                        {xAxis: 10, y: 350, name: 'Funnel', symbolSize: 20, symbol: 'image://../asset/ico/漏斗图.png'},
        //                    ]
        //                }
        //            }
        //        ]
        //    };
        //}
        else if (type=='kstj') {
            var name=[];
            var count=[];
            if (index==1) {
                name[0] = "优秀"+"("+rep.excellentRate+"%)";
                name[1] = "良好"+"("+rep.goodRate+"%)";
                name[2] = "不及格"+"("+rep.failureRate+"%)";
                count[0]=rep.excellentNum;
                count[1]=rep.goodNum;
                count[2]=rep.failureNum;
            } else {
                if (rep!=null && rep.length!=0) {
                    for (var i=0;i<rep.length;i++) {
                        name[i] = rep[i].answerDes+"("+rep[i].rate+")";
                        count[i] = rep[i].count;
                    }
                }
            }

            charData = {
                title: {
                    x: 'center',
                    /* text: 'ECharts例子个数统计',
                     subtext: 'Rainbow bar example',*/
                    link: 'http://echarts.baidu.com/doc/example.html'
                },
                tooltip: {
                    trigger: 'item'
                },
                toolbox: {
                    show: true,
                    feature: {
                        dataView: {show: false, readOnly: false},
                        restore: {show: false},
                        saveAsImage: {show: false}
                    }
                },
                calculable: true,
                grid: {
                    borderWidth: 0,
                    y: 80,
                    y2: 60
                },
                xAxis: [
                    {
                        type: 'category',
                        show: true,
                        data: name
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        show: true
                    }
                ],
                series: [
                    {
                        /* name: 'ECharts例子个数统计',*/
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                color: function (params) {
                                    // build a color map as your need.
                                    var colorList = [
                                        '#C1232B', '#B5C334', '#f0c49c', '#E87C25', '#27727B',
                                        '#f096aa', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
                                        '#D7504B', '#C6E579', '#10baf5', '#F0805A', '#26C0C0'
                                    ];
                                    return colorList[params.dataIndex]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{b}\n{c}'
                                }
                            }
                        },
                        data: count,
                        markPoint: {
                            tooltip: {
                                trigger: 'item',
                                backgroundColor: 'rgba(0,0,0,0)',
                                formatter: function (params) {
                                    return '<img src="'
                                        + params.data.symbol.replace('image://', '')
                                        + '"/>';
                                }
                            },
                            data: [
                            ]
                        }
                    }
                ]
            };
        }
        return charData;
    }

    lessonclass.getSingleExamList = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        Common.getPostData('/interactLesson/getSingleQuestionList.do', lessonclassData,function(rep) {
            if(rep.stuExamDetail.length>0) {
                $('.sta-dtms').show();
                $(".lanclass-hov").hide();
                $('#singlequestion').html($('#sigleExamlist').find("option:selected").text() + "(正确答案:" + rep.answer + ")");
                $('.singleAnswer').html('');
                Common.render({tmpl: $('#singleAnswer_templ'), data: rep.stuExamDetail, context: '.singleAnswer'});
                //if (rep.format!='subjective') {
                var chardata = lessonclass.buildChartData('kstj', rep.answerCount, 0);
                lessonclass.chartInit('djms', chardata);
                if($('.fancybox')!=undefined){
                    $('.fancybox').fancybox();
                }
            }else{
                $('.sta-dtms').hide();
                $(".lanclass-hov").show();
            }
        });
    }
    lessonclass.getExamList = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        Common.getPostData('/interactLesson/getExamList.do', lessonclassData,function(rep) {
            if(rep.hiddenEcharts=="N") {
                $('.sta-kaoshi').show();
                $(".lanclass-hov").hide();
                var chardata = lessonclass.buildChartData('kstj', rep.scoreClassify,1);
                lessonclass.chartInit('bjtj', chardata);
                $('.stuexamlist').html('');
                Common.render({tmpl: $('#stuexamlist_templ'), data: rep.examList, context: '.stuexamlist'});

                if($('.fancybox')!=undefined){
                    $('.fancybox').fancybox();
                }
            }else{
                $('.sta-kaoshi').hide();
                $(".lanclass-hov").show();
            }

        });
    }
    lessonclass.getStuQuickAnswerTextList = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        Common.getPostData('/interactLesson/getStuQuickAnswerTextList.do', lessonclassData,function(rep){
            $('.sta-dati').show();
            if(rep.hiddenEcharts=="N") {
                $('.dati-data').show();
                $(".lanclass-hov").hide();
                $(".lanclass-hov-dati").hide();
                var chardata = lessonclass.buildChartData('kstj', rep.answerCount, 0);
                lessonclass.chartInit('awto', chardata);
                $('.answertotal').html('');
                Common.render({tmpl: $('#answertotal_templ'), data: rep.stuQuickAnswer, context: '.answertotal'});
            }else{
                $('.dati-data').hide();
                $(".lanclass-hov").hide();
                $(".lanclass-hov-dati").show();
            }
        });
    }
    lessonclass.getLessonUploadFileList = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        Common.getPostData('/interactLesson/lessonUploadFile.do', lessonclassData,function(rep){
            if(rep.length>0){
                $(".lanclass-hov").hide();
                if (lessonclassData.type==3) {
                    $('.sta-xuesheng').show();
                    $('.stufilelist').html('');
                    Common.render({tmpl: $('#stufilelist_templ'), data: rep, context: '.stufilelist'});
                } else {
                    $('.sta-ketang').show();
                    $('.techfilelist').html('');
                    Common.render({tmpl: $('#techfilelist_templ'), data: rep, context: '.techfilelist'});
                }
                if($('.fancybox')!=undefined){
                    $('.fancybox').fancybox();
                }
            }else{
                if (lessonclassData.type==3) {
                    $('.sta-xuesheng').hide();
                } else {
                    $('.sta-ketang').hide();
                }
                $(".lanclass-hov").show();
            }
        });

    }

    lessonclass.lessonActiviness = function() {
        lessonclassData.lessonId = $('#lessonid').val();
        lessonclassData.classId = $('#classid').val();
        Common.getPostData('/interactLesson/lessonActiviness.do', lessonclassData,function(rep){
            $('.questiontotal').html('');

            if(rep.hiddenEcharts=="N"){
                $('.sta-xingwei').show();
                $(".lanclass-hov").hide();
                if(rep.hiddenTable=="N") {
                    Common.render({tmpl: $('#questiontotal_templ'), data: rep.countTimes, context: '.questiontotal'});
                }else{
                    $("#sta-QD").hide();
                }
                var chardata = lessonclass.buildChartData('xwtj',rep,0);
                lessonclass.chartInit('xwtj',chardata);
            }else{
                $('.sta-xingwei').hide();
                $(".lanclass-hov").show();
            }
        });
    }
    lessonclass.initPic = function() {
        if ($('.video-thumb').length <= 0) {
            $('#videoplayer-div').hide();
        } else {
            video_thumb_list = $('.video-thumb').get();
            playVideo(0);
        }
    }

    lessonclass.init();
});