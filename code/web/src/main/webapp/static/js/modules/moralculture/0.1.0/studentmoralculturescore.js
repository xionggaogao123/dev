/*
 * @Author: Tony
 * @Date:   2015-07-02 09:50:39
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-02 10:54:19
 */
define(['jquery', 'chengji', 'common'], function (require, exports, module) {
    require('jquery');
    var chengJi = require('chengji'),
        Common = require('common');
    (function () {
        var dataStyle = {
                normal: {
                    label: {show: false},
                    labelLine: {show: false}
                }
            },
            placeHolderStyle = {
                normal: {
                    color: 'rgba(0,0,0,0)',
                    label: {show: false},
                    labelLine: {show: false}
                },
                emphasis: {
                    color: 'rgba(0,0,0,0)'
                }
            };
        selPersonalScore();

        $("#semesterId").change(function () {
            selPersonalScore();
        });

        $("#studentId").change(function () {
            selPersonalScore();
        });
    })();

    function selPersonalScore() {
        var selData = {};
        selData.semesterId = $("#semesterId").val();
        selData.userId = $("#studentId").val();
        Common.getData('/moralCultureScore/selPersonalVsClassAvgScore.do', selData, function (resp) {
            var indicator = new Array();
            var personalScoreData = new Array();
            var avgScoreData = new Array();
            for (var i = 0; i < resp.list.length; i++) {
                var obj = resp.list[i];
                var childNode = {text: obj.moralCultureName, max: 100};
                indicator.push(childNode);
                var personalScore = resp.personalScore;
                if (personalScore != null && personalScore != "") {
                    var obj2 = personalScore[i];
                    if (obj2.projectScore != "") {
                        personalScoreData.push(obj2.projectScore);
                    } else {
                        personalScoreData.push(0);
                    }
                }

                var avgScore = resp.avgScore;
                if (avgScore != null && avgScore != "") {
                    var obj3 = avgScore[i];
                    if (obj3.projectScore != "") {
                        avgScoreData.push(obj3.projectScore);
                    } else {
                        avgScoreData.push(0);
                    }
                }
            }
            var option = {
                backgroundColor: '#f7f7f7',
                color: ['#e11e54', '#b9d3c7'],
                title: {
                    text: '个人成绩vs班级均分',
                    subtext: $("#semesterId").find("option:selected").text(),
                    textStyle: {
                        color: 'rgba(75,171,217,0.8)',
                        fontFamily: '微软雅黑',
                        fontSize: 18
                    }
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    orient: 'vertical',
                    x: 'right',
                    y: 'bottom',
                    data: ['个人成绩', '班级均分']
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                polar: [
                    {
                        indicator: indicator
                    }
                ],
                calculable: true,
                series: [
                    {
                        name: '个人成绩vs班级均分',
                        type: 'radar',

                        data: [
                            {
                                value: personalScoreData,
                                name: '个人成绩'
                            },
                            {
                                value: avgScoreData,
                                name: '班级均分'
                            }
                        ]
                    }
                ]
            };
            chengJi.chartInit('gvsb', option);
        });
    }

    chengJi.init();
});
