/**
 * Created by fl on 2016/8/24.
 */
define(['common','pagination','echarts'],function(require,exports,module){

    var Common = require('common');
    require('pagination');
    $(function() {


        getGrades();


        $('#grades').change(function(){//切换年级
            getExams();
        })
        $('#exams').change(function(){//切换考试
            getScoreAnalysis();
        })
        //点击总分分析时判断是否能总分分析
        $('#ZFFX').click(function(){
            checkZongFenState();
        })

        $('#ysw').click(function(){
            var examId = $('#exams').val();
            getZongfen(examId, 1)
        })
        $('#all').click(function(){
            var examId = $('#exams').val();
            getZongfen(examId, 2)
        })

        $('body').on('click', '.sub-nav li', function(){
            $(".sub-nav li").removeClass("s-cur");
            $(this).addClass("s-cur");
            var examId = $('#exams').val();
            var subjectId = $(this).attr('sid');
            $('#subjectName').text($(this).find('a').text());
            getSubjectSummary(examId, subjectId)
            getStudentSubjectScore(examId, subjectId, 1)
        })

        $('#3lv1fen').click(function(){
            window.open('/scoreanalysis/exportResult/3lv1fen.do?examId=' + $('#exams').val())
        })

        $('#subjectsRanking').click(function(){
            window.open('/scoreanalysis/exportResult/subjectsRanking.do?examId=' + $('#exams').val())
        })

        $('#zongbiao').click(function(){
            window.open('/scoreanalysis/exportResult/zongbiao.do?examId=' + $('#exams').val())
        })

    })

    function getGrades(){
        Common.getDataAsync('/myschool/gradelist.do', {}, function(resp){
            Common.render({
                tmpl: '#gradesTmpl',
                data: resp.rows,
                context:'#grades',
                overwrite: 1
            });
            getExams();
        })
    }

    function getExams(){
        var gradeId = $('#grades').val();
        Common.getDataAsync('/exam1/loadExam.do', {gradeId: gradeId, page: 1}, function(resp){
            var data = resp.message.list;
            if(data.length > 0){
                $('#info').show();
                $('#warning').hide();
                Common.render({
                    tmpl: '#examsTmpl',
                    data: data,
                    context:'#exams',
                    overwrite: 1
                });
                getScoreAnalysis();
            } else {
                $('#warning').show();
                $('#info').hide();
                data = [{id:'', name:'暂无考试'}];
                Common.render({
                    tmpl: '#examsTmpl',
                    data: data,
                    context:'#exams',
                    overwrite: 1
                });
            }

        })
    }

    function getScoreAnalysis(){
        var examId = $('#exams').val();
        getScoreSummaryByExamId(examId)
        getZongfen(examId, 1)
        getStudentZongfen(examId, 1)
        getExamSubjects(examId);
    }

    function getScoreSummaryByExamId(examId){
        Common.getDataAsync('/scoreanalysis/summary.do', {examId: examId, type: 2}, function(resp){
            if(resp.code == '200'){
                one(resp.message)
                two(resp.message)
                Common.render({
                    tmpl: '#gradesummaryTmpl',
                    data: resp.message,
                    context:'#gradesummary',
                    overwrite: 1
                });
            }

        })
    }

    function checkZongFenState(){
        var examId = $('#exams').val();
        Common.getDataAsync('/scoreanalysis/zoufenstate.do', {examId : examId}, function(resp){
            var data = resp.message;
            if(data.ysw){
                $('.less-inf').hide();
                $('.all-main').show();
                data.all ? $('#all').show() : $('#all').hide();
            } else {
                $('.less-inf').show();
                $('.all-main').hide();
            }
        })
    }

    function getZongfen(examId, type){
        var url = type == 1 ? '/scoreanalysis/yswzongfen.do' : '/scoreanalysis/zongfen.do';
        Common.getDataAsync(url, {examId : examId}, function(resp){
            three(resp.message)
            Common.render({
                tmpl: '#zongfenTmpl',
                data: resp.message,
                context:'#zongfen',
                overwrite: 1
            });
            Common.render({
                tmpl: '#zongfenTmpl1',
                data: resp.message,
                context:'#zongfen1',
                overwrite: 1
            });
        })
    }

    function getStudentZongfen(examId, page){
        var isInit = true;
        var pageSize = 20;
        Common.getDataAsync('/scoreanalysis/studentzongfen.do', {examId : examId, page: page, pageSize: pageSize}, function(resp){
            var message = resp.message;
            $('.per-analyse .new-page-links').html("");
            if(message.list.length > 0) {
                //分页方法
                $('.per-analyse .new-page-links').jqPaginator({
                    totalPages: Math.ceil(message.count / message.pageSize) == 0 ? 1 : Math.ceil(message.count / message.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getStudentZongfen(examId, n);
                        }
                    }
                });
            }

            Common.render({
                tmpl: '#stuzongfenTmpl',
                data: message.list,
                context:'#stuzongfen',
                overwrite: 1
            });
        })
    }

    function getExamSubjects(examId){
        Common.getDataAsync('/scoreanalysis/examsubjects.do', {examId: examId}, function(resp){
            Common.render({
                tmpl: '#examsubjectTmpl',
                data: resp.message,
                context:'#examsubject',
                overwrite: 1
            });
            $('#examsubject li').first().addClass('s-cur');
            var subjectId = $('#examsubject li').first().attr('sid');
            getSubjectSummary(examId, subjectId);
            getStudentSubjectScore(examId, subjectId, 1);
        })
    }

    function getSubjectSummary(examId, subjectId){
        Common.getDataAsync('/scoreanalysis/summary.do', {examId: examId, subjectId: subjectId, type: 1}, function(resp){
            Common.render({
                tmpl: '#subjectSummaryTmpl',
                data: resp.message,
                context:'#subjectSummary',
                overwrite: 1
            });
            Common.render({
                tmpl: '#subjectSummaryTmpl1',
                data: resp.message,
                context:'#subjectSummary1',
                overwrite: 1
            });
            four(resp.message)
            six(resp.message)
        })
    }

    function getStudentSubjectScore(examId, subjectId, page){
        var isInit = true;
        var pageSize = 20;
        Common.getDataAsync('/scoreanalysis/stuSubScores.do', {examId : examId, subjectId: subjectId, page: page, pageSize: pageSize}, function(resp){
            var message = resp.message;
            $('.stu-main .new-page-links').html("");
            if(message.list.length > 0) {
                //分页方法
                $('.stu-main .new-page-links').jqPaginator({
                    totalPages: Math.ceil(message.count / message.pageSize) == 0 ? 1 : Math.ceil(message.count / message.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getStudentSubjectScore(examId, subjectId, n);
                        }
                    }
                });
            }

            Common.render({
                tmpl: '#subjectScoreTmpl',
                data: message.list,
                context:'#subjectScore',
                overwrite: 1
            });
        })
    }

    function one(data) {
        var subjects = [];
        var youxiu = [];
        var hege = [];
        var buhege = [];
        for(var i in data){
            subjects[i] = data[i].subjectName;
            youxiu[i] = data[i].youXiuCount;
            hege[i] = data[i].heGeCount - data[i].youXiuCount;
            buhege[i] = data[i].count - data[i].heGeCount;
        }
        var option = {
            backgroundColor:'#ccc',
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['优秀人数', '合格人数', '不合格人数']
            },
            toolbox: {
                show: false,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            yAxis: [
                {
                    type: 'value'
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: subjects
                }
            ],
            series: [
                {
                    name: '优秀人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: youxiu
                },
                {
                    name: '合格人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: hege
                },
                {
                    name: '不合格人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: buhege
                },

            ]
        };

        chartInit("chart-one", option);
    }

    function two(data) {
        var subjects = [];
        var max = [];
        var avg = [];
        var min = [];
        for(var i in data){
            subjects[i] = data[i].subjectName;
            max[i] = data[i].max;
            avg[i] = data[i].avg;
            min[i] = data[i].min;
        }

        option = {
            backgroundColor:'#ccc',
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['最高分', '最低分', '平均分']
            },
            toolbox: {
                show: false,
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            xAxis: [
                {
                    type: 'category',
                    data: subjects
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '最高分',
                    type: 'bar',
                    data: max,
                    //markPoint: {
                    //    data: [
                    //        {type: 'max', name: '最大值'},
                    //        {type: 'min', name: '最小值'}
                    //    ]
                    //},
                    markLine: {
                        data: [
                            {type: 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name: '最低分',
                    type: 'bar',
                    data: min,
                    //markPoint: {
                    //    data: [
                    //        {name: '年最高', value: 182.2, xAxis: 7, yAxis: 183},
                    //        {name: '年最低', value: 2.3, xAxis: 11, yAxis: 3}
                    //    ]
                    //},
                    markLine: {
                        data: [
                            {type: 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name: '平均分',
                    type: 'bar',
                    data: avg,
                    //markPoint: {
                    //    data: [
                    //        {name: '年最高', value: 182.2, xAxis: 7, yAxis: 183},
                    //        {name: '年最低', value: 2.3, xAxis: 11, yAxis: 3}
                    //    ]
                    //},
                    markLine: {
                        data: [
                            {type: 'average', name: '平均值'}
                        ]
                    }
                },
            ]
        };


        chartInit("chart-two", option);
    }

    function three(data) {

        var classes = [];
        var count_10 = [];
        var count_50 = [];
        var count_100 = [];
        var count_200 = [];
        for(var i in data){
            classes[i] = data[i].className;
            count_10[i] = data[i].count_10;
            count_50[i] = data[i].count_50 - data[i].count_10;
            count_100[i] = data[i].count_100 - data[i].count_50;
            count_200[i] = data[i].count_200 - data[i].count_100;
        }
        var option = {
            backgroundColor:'#ccc',
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['前100~200名人数', '前100~50名人数', '前50~10名人数', '前10名人数']
            },
            toolbox: {
                show: false,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            yAxis: [
                {
                    type: 'value'
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: classes
                }
            ],
            series: [
                {
                    name: '前100~200名人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: count_200
                },
                {
                    name: '前100~50名人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: count_100
                },
                {
                    name: '前50~10名人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: count_50
                },
                {
                    name: '前10名人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: count_10
                },

            ]
        };

        chartInit("chart-three", option);
    }

    function four(data) {
        var classes = [];
        var youxiu = [];
        var hege = [];
        var difen = [];
        for(var i in data){
            classes[i] = data[i].className;
            youxiu[i] = data[i].youXiuCount;
            hege[i] = data[i].heGeCount;
            difen[i] = data[i].diFenCount;
        }

        var option = {
            backgroundColor:'#ccc',
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['优秀人数', '合格人数', '低分人数']
            },
            toolbox: {
                show: false,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            yAxis: [
                {
                    type: 'value'
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: classes,
                    axisLabel: {
                        interval: 0,
                        rotate: 37,
                        margin: 10,
                        textStyle: {
                            color: "#333"
                        }
                    }
                }
            ],
            series: [
                {
                    name: '优秀人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: youxiu
                },
                {
                    name: '合格人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: hege
                },
                {
                    name: '低分人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: difen
                },

            ]
        };

        chartInit("chart-four", option);
    }

    function six(data) {
        var classes = [];
        var avg = [];
        for(var i in data){
            classes[i] = data[i].className;
            avg[i] = data[i].avg;
        }
        var option = {
            backgroundColor:'#ccc',
            title: {
                text: $('#subjectName').text() + '平均分'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['班级平均分数']
            },
            toolbox: {
                show: false,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            xAxis: [
                {
                    type: 'category',
                    data: classes
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '班级平均分数',
                    type: 'bar',
                    data: avg,
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                            {type: 'min', name: '最小值'}
                        ]
                    },
                    markLine: {
                        data: [
                            {type: 'average', name: '平均值'}
                        ]
                    }
                },
            ]
        };

        chartInit("chart-six", option);
    }

    function five(data) {
        var option = {
            backgroundColor:'#ccc',
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['优秀人数', '合格人数', '低分人数']
            },
            toolbox: {
                show: false,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: false,
            yAxis: [
                {
                    type: 'value'
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: ['1班', '1班', '1班', '1班', '1班', '1班', '1班'],
                    axisLabel: {
                        interval: 0
                    }
                }
            ],
            series: [
                {
                    name: '优秀人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: [3, 3, 3, 4, 3, 5, 2]
                },
                {
                    name: '合格人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: [120, 132, 101, 134, 90, 130, 110]
                },
                {
                    name: '低分人数',
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {normal: {label: {show: true, position: 'insideRight'}}},
                    data: [22, 18, 19, 23, 29, 33, 31]
                },

            ]
        };
        chartInit("chart-five", option);
    }

    function chartInit(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);

    }


    $(function(){
        $(".tab-head li").click(function(){
            $(".tab-head li").removeClass("cur");
            $(this).addClass("cur");
            $(".tab-main>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $(".sub-tab li").click(function(){
            $(".sub-tab li").removeClass("s-cur");
            $(this).addClass("s-cur");
            //$(".sub-main>table").hide();
            //var name = $(this).attr("id");
            //$("#" + "tab-" + name).show();
        })
        $(".rank-tab li").click(function(){
            $(".rank-tab li").removeClass("v-cur");
            $(this).addClass("v-cur");
            $(".rank-main>table").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $(".one-tab li").click(function(){
            $(".one-tab li").removeClass("m-cur");
            $(this).addClass("m-cur");
            $(".one-main>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $(".all-tab li").click(function(){
            $(".all-tab li").removeClass("m-cur");
            $(this).addClass("m-cur");
            $(".all-main>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })

        $(".grade-tab li").click(function(){
            $(".grade-tab li").removeClass("s-cur");
            $(this).addClass("s-cur");
            $(".grade-main>table").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $(".stu-tab li").click(function(){
            $(".stu-tab li").removeClass("s-cur");
            $(this).addClass("s-cur");
            $(".stu-main>table").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
    })



});