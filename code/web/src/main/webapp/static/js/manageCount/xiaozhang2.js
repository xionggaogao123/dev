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