// 路径配置
require.config({
    paths: {
        echarts: '/static/plugins/echarts'
    }
});
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
                subtext: '2015年3月27日',
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
                    magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: true,
            dataZoom: {
                show: true,
                realtime: true,
                start: 80,
                end: 100
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: function () {
                        var list = [];
                        for (var i = 1; i <= 225; i++) {
                            list.push(i);
                        }
                        return list;
                    }()
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
                    data: function () {
                        var list = [];
                        for (var i = 1; i <= 225; i++) {
                            list.push(Math.round(Math.random() * 10));
                        }
                        return list;
                    }()
                }
            ]
        };

        myChart.setOption(option);
    }
);

/*折线1结束*/
// 路径配置

// 使用
require(
    [
        'echarts',
        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
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
        option = {
            title: {
                text: '人员分布？',
                subtext: '2015 年3月27日',
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
                data: ['10%老师', '80%学生', '10%家长'],
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
                            value: 10,
                            name: '10%老师'
                        },
                        {
                            value: 90,
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
                            value: 80,
                            name: '80%学生'
                        },
                        {
                            value: 20,
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
                            value: 10,
                            name: '10%家长'
                        },
                        {
                            value: 90,
                            name: 'invisible',
                            itemStyle: placeHolderStyle
                        }
                    ]
                }
            ]
        };
        myChart.setOption(option);
    }
);
/*饼结束*/
// 路径配置

// 使用
require(
    [
        'echarts',
        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main3'));

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
                subtext: '2015年3月27日',
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
                    data: ['学生', '家长', '老师']
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
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                }
            ]
        };

        myChart.setOption(option);
    }
);
/*统计1结束*/
// 路径配置

// 使用
require(
    [
        'echarts',
        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main4'));

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
                text: '备课统计',
                subtext: '2015年3月27日',
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
                    data: ['云课程', '备课空间', '班级课程', '校本资源', '联盟资源']
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
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                },
                {
                    name: '新增',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                }
            ]
        };

        myChart.setOption(option);
    }
);
/*统计2-1结束*/
// 路径配置

// 使用
require(
    [
        'echarts',
        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main5'));

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
                subtext: '2015年3月27日',
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
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                },
                {
                    name: '新增',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                }
            ]
        };

        myChart.setOption(option);
    }
);
/*统计2-2结束*/
// 路径配置

// 使用
require(
    [
        'echarts',
        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main6'));

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
                subtext: '2015年3月27日',
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
                    data: ['作业', '试卷', '题库']
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
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                },
                {
                    name: '新增',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                },
                {
                    name: '完成',
                    type: 'bar',
                    itemStyle: itemStyle,
                    data: [100, 200, 270, 50, 200, 200, 200, 300]
                }
            ]
        };

        myChart.setOption(option);
    }
);
/*统计3结束*/