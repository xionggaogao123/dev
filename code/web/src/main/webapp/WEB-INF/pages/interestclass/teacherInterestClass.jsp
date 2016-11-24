<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%--------------------------兴趣班老师页面----------------------------------------------%>
<html>
<head>
    <title>我的班级-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <%--<link rel="stylesheet" type="text/css" href="/css/dialog.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="/css/style.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="/css/flippedclassroom.css"/>--%>
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>

    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script src="/static/js/sharedpart.js" type="text/javascript"></script>
    <script src="/static/js/common/role.js" type="text/javascript"></script>
    <style type="text/css">
        #fc_class_container {
            margin: 0 auto;
            position: relative;
            left: 10px;
            float: left;
            width:790px;
            padding: 70px 0 0 0;
        }

        #fc_class_container h4 {
            font-size: 17px;
            border-left: 4px solid #ff7e00;
            padding-left: 10px;
            margin:22px 0;
        }
        #fc_class_container select{
            width: 125px;
            height: 32px;
            margin-right: 10px;
            vertical-align: top;
        }
        #fc_class_container #term{
            width: 250px;;
        }

        #fc_class_list {
            clear: both;
            transition-duration: 1.5s;
            transition-property: height;
        }

        .fc_class_detail > div {
            display: inline-block;
            float: left;
            margin-bottom: 40px;
            border: 1px solid #D0D0D0;
            /*border-bottom: none;*/
            border-radius: 3px;
            padding-top: 15px;;
        }

        .fc_class_detail > div > * {
            float: left;
        }

        .fc_class_detail > div > div {
            text-align: center;
            background: white;
            border: 1px solid #D0D0D0;
        }

        .fc_class_detail > div > div > div {
            margin: 5px 0;
            padding: 0 14px;
            float: left;
            min-width: 80px;
        }

        .fc_class_detail > div > div > div > span:first-child {
            margin-top: 20px;
            display: block;
        }

        .fc_class_detail > div > div > div > span:first-child + span {
            margin-bottom: 20px;
        }

        .fc_class_nav {
            height: 40px;
            line-height: 40px;
            position: absolute;
            top: 20px;
            left: 0;
            border-bottom: 1px solid #ccc;
            width: 100%;
        }

        .fc_class_nav > li {
            float: left;
            background: #fda616;
            color: #fff;
            text-align: center;
            width: 120px;
            font-size: 14px;
            margin-right: 3px;
            border: 1px solid #fda616;
            border-bottom: 1px solid #ccc;
            height: 39px;
            cursor: pointer;
        }

        .fc_class_nav .fc_cur{
            background: #fff;
            color: #fda616;
            border: 1px solid #ccc;
            border-bottom: 1px solid #fff;
        }

        select{
            width: 110px;
            border: #D3d3d3 1px solid !important;
            border-radius: 2px;
            height: 25px;
            padding: 0 5px;
            margin-bottom: 15px !important;
        }

        .fc_button{
            display: inline-block;
            width: 94px;
            text-align: center;
            background: #fda616;
            color: #fff;
            height: 32px;
            cursor: pointer;
        }
        #content_main_container{
            width: 1100px;
            margin: 0 auto;
        }
    </style>

    <script type="text/javascript">
        $(document).ready(function(){
            $('body').on('click','.fc_class_nav li',function(){
                $(this).addClass('fc_cur').siblings('.fc_class_nav li').removeClass('fc_cur');
            });

            $('#exportData').click(function(){
                window.open("/myclass/exportFinalExcel.do?termType=" + $('#term').val())
            })

            $('#attendanceState').click(function(){
                window.open("/myclass/headMasterCheckAttendance.do")
            })

            $('#iclass').click(function(){
                $('#term').show();
                $('#grade').show();
                $('#category').show();
                $('#attendanceState').show();
                $('#exportData').show();
                getClassInfo();
            })

            $('#aclass').click(function(){
                $('#term').hide();
                $('#grade').hide();
                $('#category').hide();
                $('#attendanceState').hide();
                $('#exportData').hide();
                getClassesByMaster();
            })
        });

        var currentPageID = 2;
        var currentPage = 1;
        var nb_role = '${sessionValue.userRole}';
        var type = 0;
        function getClassInfo() {
            type = $.trim($("body").attr("interesttype"));
            var term = $('#term').val();
            var ret = [];
            var gradeId = $('#grade').find('option:selected').val();
            var categoryId = $('#category').find('option:selected').val();
            var termType = $('#term').val();
            $.ajax({
                url: "/myclass/interestclass.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    type: type,
                    term: null,
                    gradeId: gradeId,
                    categoryId: categoryId,
                    termType: termType
                },
                success: function (data) {
                    if (data.rows.length == 0) {
                        $('#interest-info').show();
                        $('#interest-all-info').show();
//                        if (type == 2) {
//                            $('#interest-info').show();
//                            $('#interest-all-info').show();
//                        } else {
//                            $('#interest-all-info').hide();
//                        }

                    } else {
                        if (type == 2) {
                            $('#interest-info').hide();
                            $('#interest-all-info').show();
                        } else {
                            $('#interest-all-info').hide();
                        }
                        ret = data;
                    }
                    showclass(data);
                },
                complete: function () {
                    $('.fc_class').bind('click', function (e) {
                        window.open($(this).attr('href'));
                    });
                    $('.t_score').bind('click', function (e) {
                        e.stopPropagation();
                        window.open($(this).attr('href'));
                    });
                }
            });
            return ret;
        }

        function showclass(data) {
            var html = '';

            if (data.rows) {
                for (var i = 0; i < data.rows.length; i++) {
                    var cla = data.rows[i];
                    var href = '/myclass/interstat/' + cla.id + '/' + $('#term').val();
                    var shref1 = "/myclass/finalcomment/" + cla.id+ '/' + $('#term').val();
                    var shref2 = "/myclass/finalcomment/" + cla.id+ '/' + $('#term').val();
                    html += '<div class="fc_class_detail"><div class="fc_class" style="cursor:pointer;width:770px;" href="' + href + '">' +
                    '<div style="background: url(/img/activity/homepage-xuesheng.png);background-repeat:no-repeat;width: 90px;height: 87px;border: none"></div>' +
                    '<div style="float: left;border: none"><div style="width: 175px;padding: 0px;"><span class="blue">';
                    html += '班级名称</span><span title="'+cla.className+'" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 100%;display: inline-block;">' + cla.className + '</span></div><div style="width: 130px;padding: 0px;"><span class="blue">任课老师</span><span title="'+cla.teacherName+'" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 100%;display: inline-block;">';
                    html += (cla.teacherName == '' ? '暂无' : cla.teacherName) + '</span></div>';
                    html += '<div style=""><span class="blue">班级学生数</span>';
                    html += '<span>' + cla.studentList.length + '</span></div>';
                    html += '<div style=";"><span class="blue">课时数</span>';
                    html += '<span>' + cla.lessonCount + '节</span></div>';
                    if ((( isHeadMaster(nb_role) || isK6ktHelper(nb_role)) && isAdmin(nb_role)) || isTeacher(nb_role) || isSubjectLeader(nb_role)) {
                        if (cla.coursetype == 2) {
                            html += '<div class="t_score" href="' + shref1 + '"><span style="width: 94px;height: 32px;background: #3999d4;color: #fff;line-height: 30px;border-radius: 4px;">课程评价1</span></div>';
                            html += '<div class="t_score" href="' + shref2 + '"><span style="width: 94px;height: 32px;background: #3999d4;color: #fff;line-height: 30px;border-radius: 4px;">课程评价2</span></div>';
                        } else {
                            html += '<div class="t_score" href="' + shref1 + '"><span style="width: 94px;height: 32px;background: #3999d4;color: #fff;line-height: 30px;border-radius: 4px;">课程总评</span></div>';
                        }
                    }
                    html += '<p class="fix"></p></div><div class="fix"></div></div><div class="fix"></div></div>';

                }
            }
            var tdiv = $('#fc_class_list');
            tdiv.html(html);
        }

        function getGradeAndCategory(){
            $.ajax({
                url: "/myclass/getGradeAndCategory.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {

                },
                success: function (data) {
                    var gradeList = data.gradeList;
                    var html = '';
                    for(var i=0; i<gradeList.length; i++){
                        html += '<option value="'+gradeList[i].id+'">'+gradeList[i].name+'</option>';
                    }
                    $('#grade').append(html);

                    var categoryList = data.categoryList;
                    html = '';
                    for(var i=0; i<categoryList.length; i++){
                        html += '<option value="'+categoryList[i].id+'">'+categoryList[i].name+'</option>';
                    }
                    html += '<option value="">未分类</option>';
                    $('#category').append(html);

                    var termList = data.termList;
                    html = '';
                    for(var i=termList.length-1; i>=0; i--){
                        html += '<option value="'+termList[i].value+'">'+termList[i].name+'</option>';
                    }
                    $('#term').append(html);

                    if(data.role){
                        $('#grade').show();
                        $('#category').show();
                        $('.header').show();
                    } else if(data.isManager){
                        $('#grade').remove();
                        $('#category').remove();
                        $('.header').show();
                    } else {
                        $('#grade').remove();
                        $('#category').remove();
                        $('.header').remove();
                    }

                },
                complete: function () {

                }
            });
        }

        function getClassesByMaster(){
            $.ajax({
                url: "/myclass/findClassByMasterId.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {

                },
                success: function (data) {
                    console.log(data);
                    showaclass(data);
                },
                complete: function () {
                    $('.fc_class').bind('click', function (e) {
                        var cnm = $(this).parents('.fc_class_detail').attr('cnm');
                        sessionStorage.setItem("cnm", cnm);
                        window.open($(this).attr('href'));
                    });
                    $('.t_score').bind('click', function (e) {
                        e.stopPropagation();
                        var cnm = $(this).parents('.fc_class_detail').attr('cnm');
                        sessionStorage.setItem("cnm", cnm);
                        window.open($(this).attr('href'));
                    });
                }
            });
        }

        function showaclass(data) {
            var html = '';

            if (data.message) {
                for (var i = 0; i < data.message.length; i++) {
                    var cla = data.message[i];
                    var href = '/myclass/classMasterCheckAttendance.do?cid=' + cla.cId;
                    html += '<div class="fc_class_detail" cnm="'+cla.cNm+'"><div class="fc_class" style="cursor:pointer;width:770px;" href="' + href + '">' +
                    '<div style="background: url(/img/activity/homepage-xuesheng.png);background-repeat:no-repeat;width: 90px;height: 87px;border: none"></div>' +
                    '<div style="float: left;border: none"><div style="width: 175px;padding: 0px;"><span class="blue">';
                    html += '行政班</span><span title="'+cla.cNm+'" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 100%;display: inline-block;">' + cla.cNm + '</span></div><div style="width: 130px;padding: 0px;"><span class="blue">班主任</span><span title="'+cla.teacherName+'" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 100%;display: inline-block;">';
                    html += (cla.mst == '' ? '暂无' : cla.mst) + '</span></div>';
                    html += '<div style=""><span class="blue">班级学生数</span>';
                    html += '<span>' + cla.sNo + '</span></div>';
                    html += '<div class="t_score" href="' + href + '"><span style="width: 94px;height: 32px;background: #3999d4;color: #fff;line-height: 30px;border-radius: 4px;">课程详情</span></div>';
                    html += '<p class="fix"></p></div><div class="fix"></div></div><div class="fix"></div></div>';

                }
            }
            var tdiv = $('#fc_class_list');
            tdiv.html(html);
        }



        $(function () {
            $('.loading').show();
            getGradeAndCategory();
            getClassInfo();
            $('.loading').fadeOut(1000);
            $('#term').on('change',function(){
                getClassInfo();
            });

            $('body').on('change', '#grade, #category', function(){
                getClassInfo();
            })
        });
    </script>
</head>
<body interesttype="${type}">
<%@ include file="../common_new/head.jsp" %>
<jsp:include page="/infoBanner.do"/>
<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <%@ include file="../common/right.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <div id="fc_class_container">
                <ul class="fc_class_nav">
                    <li class="fc_cur" id="iclass">兴趣班</li>
                    <c:if test="${isMaster == 1}">
                    <li id="aclass">行政班</li>
                    </c:if>
                </ul>
                <select id="term">
                    <%--<option>2015-2016学年第一学期</option>--%>
                    <%--<option>2014-2015学年第二学期</option>--%>
                    <%--<option>2014-2015学年第一学期</option>--%>
                </select>
                <hr style="background-color:#ff7e00;margin-top:10px;width:770px;float: left;">
                <select id="grade" hidden>
                    <option value="">全部年级</option>
                </select>
                <select id="category" hidden>
                    <option value="allCategory">全部分类</option>
                </select>
                <input type="button" value="拓展课状态" class="fc_button header" id="attendanceState" style="display: none">
                <input type="button" value="导出数据" class="fc_button header" id="exportData" style="display: none">
                <div id="fc_class_list">
                </div>
            </div>
            <%--<div style="font-size: 14px;color: #000000;font-family: 'Microsoft YaHei';margin-top: 5em;color: rgb(255, 138, 0);font-size:16px; display:none; overflow: hidden;"--%>
                 <%--id="interest-info">--%>
                <%--<span style="margin-left:220px;"> 您还未开设“才艺社/拓展课”，加油哦！</span>--%>
            <%--</div>--%>
            <div style="font-size: 14px;color: #000000;font-family: 'Microsoft YaHei';display:none; width:100%;margin-top:15em;overflow: hidden;"
                 id="interest-all-info">
                <span style="margin-left:52px;"> 如果您要开设拓展课供同学们选课，请让您的校领导或管理员用户进入“我的学校”下面的功能“管理拓展课”。</span><br>
                <span style="margin-left:140px;">拓展课开设完毕后，学生将在此版块根据设定的“选课规则”进行自主选课。</span>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>