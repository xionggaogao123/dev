define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');

    (function(){


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

        var option = {
            backgroundColor:'#f7f7f7',
            color:['#9bca63'],
            title : {
                text: '2014-2015学年第一学期三年级语文成绩',
                subtext:'期末成绩',
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
                data:['语文']
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
                    data : ["1班","2班","3班","4班","5班","6班"]
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    "name":'语文',
                    "type":"bar",
                    "data":[30, 70, 90, 50, 100, 80],
                    "markLine":{
                        "data":[
                            // 纵轴，默认
                            {type : 'average', name : '平均值', itemStyle:{normal:{color:'#9bca63'}}},
                        ]
                    }
                }
            ]
        };



        var option2 = {
            backgroundColor:'#f3f3f3,',
            color:['#56cfc5','#93e1e0'],
            title : {
                text: '成绩分布',
                subtext: '三年级一班'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['语文']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : ['0','30','60','70','80','90','100']
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
               /* {
                    name:'成交',
                    type:'line',
                    smooth:true,
                    itemStyle: {normal: {areaStyle: {type: 'default'}}},
                    data:[10, 12, 21, 54, 260, 830, 710]
                },*/
                {
                    name:'成绩',
                    type:'line',
                    smooth:true,
                    itemStyle: {normal: {areaStyle: {type: 'default'}}},
                    data:[0, 60, 0, 91, 70, 30, 10]
                },
              /*  {
                    name:'意向',
                    type:'line',
                    smooth:true,
                    itemStyle: {normal: {areaStyle: {type: 'default'}}},
                    data:[1320, 1132, 601, 234, 120, 90, 20]
                }*/
            ]
        };

        var option3 = {
            backgroundColor:'#f7f7f7',
            color:['#2ec7c9','#b6a2de'],
            title: {
                text: '期末考试',
                subtext: '2014-2015学年上学期',
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
                data:['班级排名12','年级排名98']
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
                            value:68,
                            name:'班级排名12'
                        },
                        {
                            value:32,
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
                            value:29,
                            name:'年级排名98'
                        },
                        {
                            value:71,
                            name:'invisible',
                            itemStyle : placeHolderStyle
                        }
                    ]
                }
            ]
        }

        chengJi.init();
        chengJi.chartInit('cjdbId',option);
        chengJi.chartInit('cjfbId',option2);
        chengJi.chartInit('pjfId',option3);
        chengJi.chartInit('bhgId',option3);
        chengJi.chartInit('njhgId',option3);



    })()

})
