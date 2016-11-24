 /**
 * Created by guojing on 2015/4/15.
 */
$(function (){
    changeTimeArea();
    changeRole();
    submitFun();
});

// 路径配置
require.config({
    paths: {
        echarts: '/static/plugins/echarts'
    }
});

function changeRole(){
   var role=$("#role").val();
    $.ajax({
        url:"/manageCount/getRoleFunValue.do",
        data:"role="+role,
        type:"post",
        async:false,
        dataType:"json",
        success:function(data){
            var list = data.roleFuns;
            var html="";
            $.each(list,function(i,item){
                html+='<option value="'+item.key+'">'+item.value+'</option>';
            });
            $("#funId").html(html);
        }
    });
}

function changeGrade(){
    var schoolId=$("#schoolId").val();
    var gradeId=$("#gradeId").val();
    var gradeName=$("#gradeId").find("option:selected").text();
    if(gradeId!=""){
        $.ajax({
            url:"/manageCount/getGradeClassValue.do",
            data:{
                schoolId:schoolId,
                gradeId:gradeId
            },
            type:"post",
            dataType:"json",
            success:function(data){
                var list = data.classList;
                var html='<option value="">'+gradeName+'全部班级</option>';
                $.each(list,function(i,item){
                    html+='<option value='+item.id+'>'+item.className+'</option>';
                });
                $("#classId").html(html);
            }
        });
    }else{
        var html='<option value="">'+gradeName+'全部班级</option>';
        $("#classId").html(html);
    }
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

function submitFun(){
    MessageBox('图表加载中...', 0);
    titleChange();
    getSchoolRankingList();
    seachUseTimeDist();
    getPagingQueryList(1,10);
}

function titleChange(){
    var funName=$("#funId").find("option:selected").text();
    var html1="<span>1</span>&nbsp;&nbsp;&nbsp; "+funName+"排行榜";
    $("#title1").html(html1);
    $("#title11").text(funName);
    $("#title113").text(funName+"量");
    var html2="<span>2</span>&nbsp;&nbsp;&nbsp; "+funName+"统计";
    $("#title2").html(html2);

    var role=$("#role").val();
    var roleText=$("#role").find("option:selected").text();
    if(role==2||roleText=="老师"){
        $("#divTitle1").text(funName+"(老师)统计");
        $("#spanTitle1").text("老师姓名");
    }else if(role==1||roleText=="学生"){
        $("#spanTitle1").text("学生姓名");
        $("#divTitle1").text(funName+"(学生)统计");
    }else{
        $("#spanTitle1").text("家长姓名");
        $("#divTitle1").text(funName+"(家长)统计");
    }
    var spanTitle2=funName.substring(0,funName.length-1)+"总数";
    $("#spanTitle2").text(spanTitle2);

}

function getSchoolRankingList(){
    var schoolId=$("#schoolId").val();
    var gradeId=$("#gradeId").val();
    var role=$("#role").val();
    var classId=$("#classId").val();
    var funId=$("#funId").val();
    var dateStart=$("#dateStart").val();
    var dateEnd=$("#dateEnd").val();
    $.ajax({
        url:"/manageCount/rankingList.do",
        data:{
            schoolId:schoolId,
            gradeId:gradeId,
            dateStart:dateStart,
            dateEnd:dateEnd,
            role:role,
            classId:classId,
            funId:funId,
            size:10
        },
        type:"post",
        dataType:"json",
        success:function(data){
            var list = data.list;
            var html="";
            $.each(list,function(i,item){
                var number="";
                if(i==0){
                    number="第一名";
                }
                if(i==1){
                    number="第二名";
                }
                if(i==2){
                    number="第三名";
                }
                if(i==3){
                    number="第四名";
                }
                if(i==4){
                    number="第五名";
                }
                if(i==5){
                    number="第六名";
                }
                if(i==6){
                    number="第七名";
                }
                if(i==7){
                    number="第八名";
                }
                if(i==8){
                    number="第九名";
                }
                if(i==9){
                    number="第十名";
                }
                html+='<div class="td" style="cursor: pointer;" onclick="gotoDetail(\''+item.userId+'\',\''+item.name+'\')">'+
                    '<span>'+number+'</span>'+
                    '<span>'+item.name+'</span>'+
                    '<span>'+item.newCountTotal+'</span></div>';
            });
            $("#mainTdiv1").html(html);
        }
    });
}

function seachUseTimeDist() {
    var schoolId=$("#schoolId").val();
    var gradeId=$("#gradeId").val();
    var role=$("#role").val();
    var classId=$("#classId").val();
    var funId=$("#funId").val();
    var dateStart=$("#dateStart").val();
    var dateEnd=$("#dateEnd").val();
    $.ajax({
        url: "/manageCount/useTimeDistMap.do",
        type: "post",
        dataType: "json",
        async: true,
        traditional: true,
        data: {
            schoolId:schoolId,
            gradeId:gradeId,
            dateStart:dateStart,
            dateEnd:dateEnd,
            role:role,
            classId:classId,
            funId:funId
        },
        success: function (data) {
            var time_area=[];
            var people_count=[];
            for(var i in data.list) {
                time_area[i]=data.list[i].createTime;
                people_count[i]=data.list[i].newCountTotal;
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
        },
        complete: function () {
            ClosePromptDialog();
        }
    });
}


var currentPage = 1;
function getPagingQueryList(page, size){
    var schoolId=$("#schoolId").val();
    var gradeId=$("#gradeId").val();
    var role=$("#role").val();
    var classId=$("#classId").val();
    var funId=$("#funId").val();
    var dateStart=$("#dateStart").val();
    var dateEnd=$("#dateEnd").val();
    $.ajax({
        url:'/manageCount/pagingQuery.do',
        data:{
            'page':page,
            'size': size,
            schoolId:schoolId,
            gradeId:gradeId,
            dateStart:dateStart,
            dateEnd:dateEnd,
            role:role,
            classId:classId,
            funId:funId
        },
        type:'post',
        async:false,
        dataType:'json',
        success: function(data){
            var html="";
            $.each(data.content,function(i,item){
                html+='<div style="cursor: pointer;" class="td" onclick="gotoDetail(\''+item.userId+'\',\''+item.name+'\')">'+
                '<span>'+item.name+'</span>'+
                '<span>'+item.newCountTotal+'</span></div>';
            });
            $("#mainTdiv2").html(html);
            var total = data.totalElements;
            var to = Math.ceil(total / size);
            var totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetTotalPages(totalPages);
            } else {
                resetTotalCurrPages(page, totalPages);
            }
            if (data.totalElements > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
            }
        }
    });
}

function resetTotalPages(totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "&lt;";
                case "next":
                    return "&gt;";
                case "last":
                    return "末页"+page;
                case "page":
                    return  page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            getPagingQueryList(page, 10);
        }
    });
}

function resetTotalCurrPages(page,totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: page,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "&lt;";
                case "next":
                    return "&gt;";
                case "last":
                    return "末页"+page;
                case "page":
                    return  page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            getPagingQueryList(page, 10);
        }
    });
}

function gotoDetail(id,name){
    var schoolId=$("#schoolId").val();
    var role=$("#role").val();
    var funId=$("#funId").val();
    var dateStart=$("#dateStart").val();
    var dateEnd=$("#dateEnd").val();
    var param="schoolId="+schoolId+"&operateUserId="+id+"&userName="+name+"&role="+role+"&funId="+funId+"&dateStart="+dateStart+"&dateEnd="+dateEnd;
    window.location.href="/manageCount/functionUsePage.do?"+param;
}