define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var Common = require('common');



    (function(){

        //实际分、百分制切换，默认实际分
        //sessionStorage.setItem("hundred",0);
        //$("#reality").on('click', function() {
        //    $("#reality").attr('class','cur');
        //    $("#hundred").attr('class','cu');
        //    sessionStorage.setItem("hundred",0);
        //});
        //$("#hundred").on('click', function() {
        //    $("#hundred").attr('class','cur');
        //    $("#reality").attr('class','cu');
        //    sessionStorage.setItem("hundred",1);
        //});

        $("#stuSemTitle").text(sessionStorage.getItem("schoolYear") + sessionStorage.getItem("className"));
        buildPage();

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


        chengJi.init();
    })()

    function buildPage() {
        var requestData={};
        requestData.classId = sessionStorage.getItem("classId");
        //成绩统计
        Common.getDataAsync('/score/getSemesterStuScore.do',requestData, function(resp) {
            var data={};
            data.stuScoreList = resp.stuScoreList;
            data.gradeScoreList = resp.gradeScoreList;
            var maxScore=0;
            for (var i in data.stuScoreList) {
                if(data.gradeScoreList[i] > maxScore) {
                    maxScore = data.gradeScoreList[i];
                }
                if(data.stuScoreList[i] > maxScore) {
                    maxScore = data.stuScoreList[i];
                }
            }

            var subNameList = resp.subNameList;
            var txt = '{"indicator":[';
            for(var i=0; i<subNameList.length; i++) {
                txt += '{ "text":"' + subNameList[i] + '", "max":' + maxScore + '},';
            }
            txt += '],"radius":130}';
            data.obj = eval ("(" + txt + ")");
            var chartData = bulidChartData('cjtj', data);
            chengJi.chartInit('cjfbId',chartData);

        })
        //个人成绩
        Common.getDataAsync('/score/getAMFScore.do',requestData, function(resp) {
            //var data={};
            //成绩表
            $("#stuScoreTitle").text(sessionStorage.getItem("schoolYear") + sessionStorage.getItem("className") + sessionStorage.getItem("stuName") + "的成绩表");
            Common.render({
                tmpl: '#scoreListTmpl',
                data: resp,
                context: '#scoreList',
                overwrite: 1
            });
            var data = {};
            data.subNameList = resp.subNameList;
            data.aveScoreList = [];
            data.midScoreList = [];
            data.finScoreList = [];
            for(var i in resp.aveScoreList) {
                if(resp.aveScoreList[i] == null) {
                    data.aveScoreList[i] = 0;
                } else {
                    data.aveScoreList[i] = resp.aveScoreList[i];
                }
                if(resp.finScoreList[i] == null) {
                    data.finScoreList[i] = 0;
                } else {
                    data.finScoreList[i] = resp.finScoreList[i];
                }
                if(resp.midScoreList[i] == null) {
                    data.midScoreList[i] = 0;
                } else {
                    data.midScoreList[i] = resp.midScoreList[i];
                }
            }
            var chartData = bulidChartData('grcj', data);
            chengJi.chartInit('cjdbId',chartData);


        })
    }

    function bulidChartData(type, data) {
       if("cjtj" == type) {
           var chartData = {
               /*   title : {
                text: '个人成绩 vs 年级平均',
                subtext: '完全实况球员数据'
                },*/
               tooltip : {
                   trigger: 'axis'
               },
               legend: {
                   x : 'center',
                   data:['个人成绩','年级平均']
               },
               toolbox: {
                   show : false,
                   feature : {
                       mark : {show: false},
                       dataView : {show: false, readOnly: false},
                       restore : {show: false},
                       saveAsImage : {show: false}
                   }
               },
               calculable : true,
               polar : [
                    data.obj

               ],
               series : [
                   {
                       name: '成绩',
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
                               value : data.stuScoreList,
                               name : '个人成绩'
                           },
                           {
                               value : data.gradeScoreList,
                               name : '年级平均'
                           }
                       ]
                   }
               ]
           };
       } else if("grcj" == type) {
           var chartData = {
               tooltip : {
                   trigger: 'axis'
               },
               toolbox: {
                   show : true,
                   feature : {
                       mark : {show: true},
                       dataView : {show: true, readOnly: false},
                       magicType: {show: true, type: [ 'bar']},
                       restore : {show: true},
                       saveAsImage : {show: true}
                   }
               },
               calculable : true,
               legend: {
                   data:[]
               },
               xAxis : [
                   {
                       type : 'category',
                       data : data.subNameList
                   }
               ],
               yAxis : [
                   {
                       type : 'value'
                       /*   name : '水量'*/
                       /*  axisLabel : {
                        formatter: '{value} ml'
                        }*/
                   },
                   {
                       type : 'value'
                       /*  name : '温度'*/
                       /*    axisLabel : {
                        formatter: '{value} °C'
                        }*/
                   },
                   {
                       type : 'value'
                       /*  name : '温度'*/
                       /*    axisLabel : {
                        formatter: '{value} °C'
                        }*/
                   }
               ],
               series : [

                   {
                       name:'平时成绩',
                       type:'bar',
                       data:data.aveScoreList
                   },
                   {
                       name:'期中成绩',
                       type:'bar',
                       data:data.midScoreList
                   },
                   {
                       name:'期末成绩',
                       type:'bar',
                       data:data.finScoreList
                   }
               ]
           };
       }
        return chartData;
    }

})
