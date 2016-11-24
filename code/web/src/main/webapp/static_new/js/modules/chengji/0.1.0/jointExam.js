/**
 * Created by fl on 2015/10/26.
 */

define(['common','pagination','echarts'],function(require,exports,module){

    var Common = require('common');
    require('pagination');
    var totalScore;
    var examName;
    var jointExamId = sessionStorage.getItem("jointExamId");

    (function(){

        //
        //

        $(".top-1").click(function() {
            $(".main-1").show();
            $(".main-2").hide();
            $(".main-3").hide();
            $(".main-1-2").hide();
            $(".main-4").hide();
        });
        $(".top-4").click(function() {
            $(".main-4").show();
            $(".main-2").hide();
            $(".main-3").hide();
            $(".main-1").hide();
            $(".main-1-2").hide();
            var requestData = {};
            requestData.jointExamId = jointExamId;
            Common.getData("/score/getSubjectDistribution.do", requestData, function(resp){
                if(resp.code == '200'){
                    showSubjectDistribution(resp.message);
                } else {
                    alert(resp.message);
                }
            })
        });
        $('body').on('click', '.wind-ddsz .btn-x1', function(){
            $(this).parent().parent().remove();
        })
        $('.wind-ddsz .ddtitle button').click(function(){
            newGrade();
        });
        $('.wind-ddsz .wind-top em,.wind-ddsz .btn-no').click(function() {
            $(this).parent().parent().fadeOut();
            $('.bg').fadeOut();
        });
        $('.main-4 .tab1 thead button').click(function() {
            echoGrades();
            $('.wind-ddsz').fadeIn();
            $('.bg').fadeIn();
        })

        $(".top-2").click(function() {
            $(".main-2").show();
            $(".main-1").hide();
            $(".main-3").hide();
            $(".main-1-2").hide();
            $(".main-4").hide();
            var requestData = {};
            requestData.jointExamId = jointExamId;
            Common.getData("/score/getScoreDistribution.do", requestData, function(resp){
                showScoreDistribution(resp);
            })
        });

        $(".top-3").click(function() {
            $(".main-3").show();
            $(".main-1").hide();
            $(".main-2").hide();
            $(".main-1-2").hide();
            $(".main-4").hide();
        });

        $(".main-top .li").click(function(){
            $(this).addClass("top-cur").siblings(".li").removeClass("top-cur")
        });

        $(".detial-btn").click(function(){
            $(".main-1-2").show();
            $(".main-1").hide();
        });

        $(".back").click(function(){
            $(".main-1").show();
            $(".main-1-2").hide();
        });

        $(".btn-QX").click(function() {
            quitDialog();
        });

        $(".main2-1-set").click(function() {
            $(".wind-score").show();
            $(".bg").show();
        });
        //添加
        $(".btn-TJ").click(function() {
            var ks = $("#KS").val().trim();
            var js = $("#JS").val().trim();
            if(ks == '' && js == ''){
                alert("开始分数和结束分数至少填写一个！");
                return;
            }
            if(ks == ''){
                ks = 0;
            }
            if(js == ''){
                js = totalScore;
            }
            var temp=/^\d+$/;

            if(!temp.test(ks) || !temp.test(js)) {
                alert("开始分数、结束分数应为整数！");
                return;
            } else if(parseInt(js)<=parseInt(ks)) {
                alert("开始分数应小于结束分数！");
                return;
            } else if(parseInt(js)>totalScore){
                alert("结束分数应小于满分！");
                return;
            }
            $("#ksjs").show();
            $(".wind-4").children().last().append('<tr> <td>' + ks + '</td> <td>-</td> <td>' + js + '</td> </tr>');

        });
        //确定
        $(".btn-QD").click(function() {
            var beginList = $(".wind-4").children().children().map(function(index,element){
                if(index != 0){
                    return $(element).children().first().text();
                }
            }).get().join(',');
            var endList = $(".wind-4").children().children().map(function(index,element){
                if(index != 0){
                    return $(element).children().last().text();
                }
            }).get().join(',');
            var requestData = {};
            requestData.beginList = beginList;
            requestData.endList = endList;
            requestData.jointExamId = jointExamId;
            if(beginList != '' && endList != ''){
                Common.getData("/score/setScoreSection.do", requestData, function(resp){
                    showScoreDistribution(resp);
                })
            }

            quitDialog();

        });

        $('.btn-ok').click(function(){
            setGrade();
        })

        getExamSummary();





    })()

    function getExamSummary(){
        var date = new Date();
        var requestData = {};
        requestData.jointExamId = jointExamId;
        Common.getData("/score/getExamSummary.do?time="+date.getTime(), requestData, function(resp){
            var examInfo = resp.regionalExam;
            if(examInfo.rankFlag == 0){
                if(confirm("有新的数据未排名，是否排名？") == true){
                    //去排名
                    Common.getData("/score/rankScore.do", requestData,function(data){
                        if(data.code == "200"){
                            alert("排名成功！");
                            getExamSummary();
                        } else {
                            alert("排名失败！");
                        }
                    })
                }
            } else {
                totalScore = resp.totalScore;
                examName = examInfo.name;
                $('#title0').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name);
                $('#title1').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "各科排名");
                $('#title2').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name);
                $('#title3').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "考试详情");
                $('#title4').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name);
                $('#title5').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "成绩人数分布");
                $('#title6').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "成绩列表");
                $('#title7').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "统计表");
                $('#title8').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name);
                $('#title9').html(examInfo.term + examInfo.gradeName + "&nbsp;" + examInfo.name + "汇总表");
                $('#title10').html(examInfo.term);
                $('#totalscore').text("满分" + totalScore + "分");

                //展示学科
                Common.render({
                    tmpl: '#subject_tmpl',
                    data: resp.subs,
                    context: '#subject',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#subject_tmpl1',
                    data: resp.subs,
                    context: '#subject1',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#subject_tmpl2',
                    data: resp.subs,
                    context: '#subject2',
                    overwrite: 1
                });
                //展示学校排名
                Common.render({
                    tmpl: '#school_rank_tmpl',
                    data: resp.examSummaryDTOList,
                    context: '#school_rank',
                    overwrite: 1
                });
                //展示考试详情
                Common.render({
                    tmpl: '#summary_tmpl',
                    data: resp.examSummaryDTOList,
                    context: '#summary',
                    overwrite: 1
                });
                //展示总平均
                Common.render({
                    tmpl: '#totalsummary_tmpl',
                    data: resp.summary,
                    context: '#totalsummary',
                    overwrite: 1
                });
                if(resp.examSummaryDTOList){
                    showExamSummaryChart(resp.examSummaryDTOList);
                }
                getAreaStuScore(1);
            }


        })
    }

    //====================绘制图表 ===============================
    function showExamSummaryChart(data){
        var schools = [];
        var subjects = ["综合"];
        var series = [];
        for(var i in data){
            schools[i] = data[i].schoolName;
            var item = {};
            item.name=data[i].schoolName;
            item.type='line';
            item.smooth =true;
            item.data=[data[i].compositeRanking];
            var sub = data[i].subjectDetailsDTOList;
            for(var j=0; j<sub.length;j++){
                item.data[j+1] = sub[j].compositeRanking;
            }
            series[i] = item;

        }
        var it = data[0].subjectDetailsDTOList;
        for(var j=0; j<it.length;j++){
            subjects[j+1] = it[j].subjectName;
        }
        var option = {
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:schools
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: false, type: ['line', 'bar', 'stack', 'tiled']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : subjects
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : series
        };
        chartInit('pgfId',option);
    }

    function showScoreDistributionChart(data){
        var schools = [];
        var sections = [];
        var series = [];
        for(var i in data){
            schools[0] = data[i].schoolName;
            var item = {};
            item.name=data[i].schoolName;
            item.type='line';
            item.smooth =true;
            item.itemStyle= {normal: {areaStyle: {type: 'default'}}};
            item.data=[];
            var sub = data[i].scoreSection;
            for(var j=0; j<sub.length;j++){
                item.data[j] = sub[j].num;
            }
            series[0] = item;
        }
        var it = data[0].scoreSection;
        for(var j=0; j<it.length;j++){
            sections[j] = it[j].beginScore + '-' + it[j].endScore;
        }
        var option1 = {
            title : {
                text: examName,
                subtext: '分数段'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:schools
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: false, type: ['line', 'bar', 'stack', 'tiled']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : sections
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : series
        };
        chartInit('pgfIt',option1);
    }

    function chartInit(id,opt)
    {
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    }



    //================================绘制图表结束==========================================

    
    function getAreaStuScore(page){
        var isInit = true;
        var requestData = {};
        requestData.areaExamId =  jointExamId;
        requestData.page = page;
        requestData.pageSize = 10;
        Common.getData("/score/getAreaStudentScore.do", requestData, function(resp){
            var datas = resp.rows;
            $('.new-page-links').html("");
            if(datas.length > 0) {
                //分页方法
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.total / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.total / requestData.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(resp.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getAreaStuScore(n);
                        }
                    }
                });
            }
            Common.render({
                tmpl: '#areaStuScore_tmpl',
                data: resp.rows,
                context: '#areaStuScore',
                overwrite: 1
            });
        })
    }

    function quitDialog(){
        $(".wind-score").hide();
        $(".bg").hide();
        $("#ksjs").hide();
        $(".wind-4").children().children().each(function(index,element){
            if(index != 0){
                element.remove();
            }
        })
    }

    function showScoreDistribution(resp){
        if(resp.length == 0){
            $('.main2-3').hide();
            alert("请先设置分数段");
            return;
        }
        $('.main2-3').show();
        //标题
        Common.render({
            tmpl: '#distriTitle_tmpl',
            data: resp[0].scoreSection,
            context: '#distriTitle',
            overwrite: 1
        });
        Common.render({
            tmpl: '#distriTitle_tmpl1',
            data: resp[0].scoreSection,
            context: '#distriTitle1',
            overwrite: 1
        });
        //内容
        Common.render({
            tmpl: '#distriBody_tmpl',
            data: resp,
            context: '#distriBody',
            overwrite: 1
        });
        showScoreDistributionChart(resp);
    }

    function showSubjectDistribution(data){
        Common.render({
            tmpl: '#showGrades_tmpl',
            data: data[0].grades,
            context: '#showGrades',
            overwrite: 1
        });
        setCss();
        Common.render({
            tmpl: '#showGrades_tmpl1',
            data: data[0].grades,
            context: '#showGrades1',
            overwrite: 1
        });
        Common.render({
            tmpl: '#subjectDistribution_tmpl',
            data: data,
            context: '#subjectDistribution',
            overwrite: 1
        });
    }

    function newGrade(){
        var html = '<tr><td class="td1"><input type="text" value=""></td><td class="td2">' +
            '<input type="text" value="" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')">' +
            '<i>≥</i><span>%</span></td><td><button class="btn-x1">删除</button></td></tr>';
        $('#grades').append(html);
    }

    function setGrade(){
        var trs = $('#grades').find('tr');
        if(trs.length <= 1){
            alert("请至少设置一个等第");
        } else {
            var names = '';
            var percents = '';
            var prePer = 100;//上一个等级百分比
            var flag = true;//分数是否递减
            trs.each(function(index, ele){
                if(index > 0 && flag){
                    var per = $(ele).find('input').last().val();
                    if(per >= prePer){
                        alert('第' + index + '个等级的分数应该小于上一个等级');
                        flag = false;
                    }
                    prePer = per;
                    names += $(ele).find('input').first().val() + ',';
                    percents += per + ',';
                }
            })
            if(!flag){
                return;
            }
            var requestData = {};
            requestData.names = names;
            requestData.percents = percents;
            requestData.jointExamId = jointExamId;
            Common.getDataAsync('/score/setGradeSetting.do', requestData, function(resp){
                if(resp.code == '200'){
                    showSubjectDistribution(resp.message)
                    $('.wind-ddsz').fadeOut();
                    $('.bg').fadeOut();
                    getAreaStuScore(1);
                } else {
                    alert(resp.message);
                }
            })
        }
    }

    function setCss(){
        $('.main-4 table tbody tr:nth-child(1) div').css({'border':'1px solid #ff0000','box-shadow':'0 0 5px #ff0000 inset'});
        $('.main-4 table tbody tr:nth-child(2) div').css({'border':'1px solid #ff9900','box-shadow':'0 0 5px #ff9900 inset'});
        $('.main-4 table tbody tr:nth-child(3) div').css({'border':'1px solid #ffff00','box-shadow':'0 0 5px #ffff00 inset'});
        $('.main-4 table tbody tr:nth-child(4) div').css({'border':'1px solid #00ff00','box-shadow':'0 0 5px #00ff00 inset'});
        $('.main-4 table tbody tr:nth-child(5) div').css({'border':'1px solid #00ffff','box-shadow':'0 0 5px #00ffff inset'});
        $('.main-4 table tbody tr:nth-child(6) div').css({'border':'1px solid #4a86e8','box-shadow':'0 0 5px #4a86e8 inset'});
        $('.main-4 table tbody tr:nth-child(7) div').css({'border':'1px solid #0000ff','box-shadow':'0 0 5px #0000ff inset'});
        $('.main-4 table tbody tr:nth-child(8) div').css({'border':'1px solid #9900ff','box-shadow':'0 0 5px #9900ff inset'});
        $('.main-4 table tbody tr:nth-child(9) div').css({'border':'1px solid #ff00ff','box-shadow':'0 0 5px #ff00ff inset'});
        $('.main-4 table tbody tr:nth-child(10) div').css({'border':'1px solid #980000','box-shadow':'0 0 5px #980000 inset'});
        $('.main-4 table tbody tr:nth-child(11) div').css({'border':'1px solid #000000','box-shadow':'0 0 5px #000000 inset'});
        $('.main-4 table tbody tr:nth-child(12) div').css({'border':'1px solid #434343','box-shadow':'0 0 5px #434343 inset'});
    }

    function echoGrades(){
        var length = $('#showGrades tr').length;
        if(length <= 0){
            return false;
        }
        var html = '<tr><th class="th1"></th><th class="th2"></th><th></th></tr>';
        $('#showGrades tr').each(function(index, ele){
            html += formateHtml(ele);
        })

        $('#grades').empty();
        $('#grades').append(html);
    }

    function formateHtml(ele){
        var name = $(ele).find('div').text();
        var value = $(ele).children().last().text();
        value = value.substring(1, value.length-1);

        var html = '';
        html += '<tr><td class="td1"><input type="text" value="' + name + '"></td><td class="td2">';
        html += '<input type="text" value="' + value + '" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"><i>≥</i><span>%</span>';
        html += '</td><td><button class="btn-x1">删除</button></td></tr>';

        return html;
    }





});


