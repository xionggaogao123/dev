<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<%--------------------------------------------------------老师给班级学生经验值加分页面------------------------------------%>
<html>
<head>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>老师评分</title>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css"/>
<script type="text/javascript" src='/static/js/jquery.min.js'></script>
<script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" ></script>
<script type="text/javascript" src="/static/js/validate-form.js" ></script>
<link href="/static_new/css/reset.css" rel="stylesheet" />
<style stype="text/css">
    .popu {
        top: 150px;
        left: 228px;
        position: absolute;
        font-size: 15px;
    }

    .popu p {
        font-weight: bold;
    }

    .popu b {
        margin-top: 10px;
        display: inline-block;
    }

    .header_foot {
        height: 20px;
        background: rgb(235, 238, 247);
        border-bottom: 1px solid #d0d0d0;
    }

    .fc_statistics_list span {
        padding-right: 10px;
        padding-left: 10px;
        width: 69px;
        overflow: hidden;
    }

    .fc_statistics_list input {
        padding: 3px 10px;
        width: 73px;
        overflow: hidden;
        vertical-align: top;
        position: relative;
        top: 10px;
    }

    .fc_statistics_list .header span {
        border-left: 1px solid #d0d0d0;
    }

    .fc_statistics_list > * {
        white-space: nowrap;
    }

    .fc_statistics_list ul > * {
        float: left;
    }

    .Ifocus {
        border: 1px solid #cccccc;
        border-radius: 4px;
    }

    .student-score {
        margin: 0 auto;
    }

    .students-score {
        margin: 0 auto;
    }

    .content span {
        min-width: 205px;
    }

    .content span button {
        float: left;
        border: 1px solid #9C9C9C;
        border-radius: 4px;
        background: #FFFFFF;
        display: inline-block;
        width: 37px;
        height: 21px;
        vertical-align: middle;
        line-height: 21px;
        color: #565656;
        font: 600 14px 'Microsoft YaHei';
    }

    .content {
        width: 100% !important;
    }

    button.good-selected {
        background: #ff9d1d !important;
        color: #fff !important;
        border: none !important;
    }

    button.bad-selected {
        background: #76b1da !important;
        color: #fff !important;
        border: none !important;
    }

    button.better-selected {
        background: #fd2b2b !important;
        color: #fff !important;
        border: none !important;
    }

    .score-btn {
        background: rgb(255, 126, 0);
        padding: 10px 20px;
        color: white;
        border-radius: 5px;
        font-size: 15px;
    }

    .score-btn:hover {
        background: #ff7900;
        padding: 11px 21px;
    }

    .part-score li {
        border: none !important;
        height: 35px !important;
        margin-top: 5px !important;
        color: #777979;
        font: 600 15px 'Microsoft YaHei';
        min-width: 205px;
        text-align: center;
    }
</style>

<script type='text/javascript'>
    var currentPageID = 2;
    $(function () {
        $("#addScore").click(function () {
            if (confirm('是否确认保存？')) {
                var data = getScoreJson();
                if (data) {
                    addTeacherScore(data);
                }
            }
        });

        getClassStatistics();

        $('.student-score').scroll(function (event) {
            $('.part-score').scrollLeft($(this).scrollLeft());
        });
    });

    function getClassStatistics() {
        $('#fc_teacher_score ul').find('li.content').remove();
        $.ajax({
            url: "/myclass/statstus.do",
            type: "get",
            dataType: "json",
            async: false,
            data: {
                classId: '${param.classId}'
            },
            success: function (data) {
                if (data.totalList == null || data.totalList.length == 0) {

                }
                else {
                    showStuListOfScore(data);
                }
            },
            complete: function () {
                $('#fc_teacher_score li.content input').bind('focus', function () {
                    $(this).addClass('Ifocus');
                }).bind('blur', function () {
                    $(this).removeClass('Ifocus');
                }).number(false);

                if (document.body.clientWidth > 1585) {
                    $('.header_foot').css('width', '1586px');
                }
                else {
                    $('#fc_teacher_score ul').css('width', document.body.clientWidth + 'px');
                    $('.header_foot').css('width', $('#fc_teacher_score ul')[0].scrollWidth + 'px');
                }
                var pheight = document.documentElement.clientHeight - 314;
                $('.student-score').css('height', pheight + 'px');
            }
        });
    }

    function showStuListOfScore(data) {
        var target = $('#fc_teacher_score .student-score');
        var html = '';
        if (data.totalList) {
            for (var i = 0; i < data.totalList.length; i++) {
                html = "";
                var stu = data.totalList[i];
                var bg = (i % 2 == 0) ? 'white' : '#f3f8f8';
                html += '<li class="content" style="background:' + bg + ';" belong="' + stu.studentId + '"><span style="width:126px;"><img src="' + stu.imageURL + '" style="width:46px;height:46px;margin-right:10px;"/>' + stu.userName + '</span>';
                html += '<span style="width:205px;"><button class="better perfmc">优异</button> <button class="good-selected perfmc">较好</button><button class="bad perfmc">欠佳</button></span><span class="thisScore">1</span><span>' + stu.experienceValue + '</span>';
                html += '</li>';
                target.append(html);
            }
        }
    }

    function getScoreJson() {
        var listItem = [];
        var postData = [];
        $("li.header span").each(function () {
            if ($(this).attr('tid')) {
                listItem.push($(this).attr('tid'));
            }
        });

        $('.student-score li.content').each(function (j) {
            postData.push({userId: $(this).attr('belong'), score: $(this).children('span:nth(2)').text()});
        });
        return JSON.stringify(postData);
    }

    function addTeacherScore(data) {
        $('.loading').show();
        $.ajax({
            url: "/myclass/saveexp.do",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                classId: '${param.classId}',
                scoreData: data
            },
            success: function (data) {
                location.href='/teacher/class';
                <%--location.href = '/myclass/addexp/'+'${requestScope.classId}';--%>
            },
            complete: function () {
                $('.loading').fadeOut(1000);
            }
        });
    }
    window.onresize = function (e) {
        if (document.body.clientWidth > 1585) {
            /* $('#fc_teacher_score ul').css('width','1585px');
             $('.header_foot').css('width','1585px'); */
        }
        else {
            $('#fc_teacher_score ul').css('width', document.body.clientWidth + 'px');
            $('.header_foot').css('width', $('#fc_teacher_score ul')[0].scrollWidth + 'px');
        }
        var pheight = document.documentElement.clientHeight - 314;
        $('.student-score').css('height', pheight + 'px');
    }
    $(function () {
        $('.perfmc').click(function () {
            $(this).siblings().each(function () {
                if ($(this).prop('className').indexOf('good-selected') >= 0) {
                    $(this).removeClass('good-selected').addClass('good');
                } else if ($(this).prop('className').indexOf('better-selected') >= 0) {
                    $(this).removeClass('better-selected').addClass('better');
                } else if ($(this).prop('className').indexOf('bad-selected') >= 0) {
                    $(this).removeClass('bad-selected').addClass('bad');
                }
            });

            var currtClass = $(this).prop("className");
            var score = 1;
            if (currtClass.indexOf('good') >= 0) {
                $(this).addClass('good-selected');
                score = 1;
            } else if (currtClass.indexOf('better') >= 0) {
                $(this).addClass('better-selected');
                score = 2;
            } else if (currtClass.indexOf('bad') >= 0) {
                $(this).addClass('bad-selected');
                score = 0;
            }
            $(this).parent().next().text(score);
        });
    });
</script>
</head>
<body style="font-family:Microsoft YaHei;">
<%@ include file="../common_new/head.jsp" %>
<div id="fc_teacher_score" class="fc_statistics_list ypheader" style="overflow:hidden;width:900px;margin: 0 auto;">
    <ul class="part-score" style="border-bottom:none;height:36px;margin:0 auto;border-top:none;overflow:hidden;">
        <li>学生姓名</li>
        <li>在校表现</li>
        <li>得分</li>
        <li style="width:273px;">本学年累计评分<label style="color:#ccc;font-weight:400;">(每年8月31日24:00清空)</label></li>
    </ul>
    <!-- <ul class="student-score">
       <li class="content">

       </li>
    </ul> -->
    <ul class="student-score">
        <li class="content">

        </li>
    </ul>
    <div style="margin:0 auto;max-width:1400px;height: 80px;margin-top:20px;padding:0 20px;overflow:hidden;">
        <a href="/teacher/class" class="score-btn" style="margin-right: 635px;"><span
                style="position:relative;top:7px;">返回</span></a>
        <a id="addScore" class="score-btn" style="margin-right:10px;"><span style="position:relative;top:7px;">保存</span></a>
    </div>
</div>

<%@ include file="../common_new/foot.jsp" %>
</body>
</html>