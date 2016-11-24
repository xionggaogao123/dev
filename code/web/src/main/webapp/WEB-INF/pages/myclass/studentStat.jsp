<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%-- --------------------------------单个学生统计页面---------------------------------------%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript">
        var studentid = '${studentId}';
        function getstudentInfo() {
            var ret = [];
            $.ajax({
                url: "/myclass/finduserinfo.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    userId: studentid
                },
                success: function (data) {
                    ret = data;
                    var content;
                    if (data.petname == null)
                        content = data.userName + '的成长树';
                    else
                        content = data.petname;
                    $('.pet-name').text(content);
                    $(".info p:first").html(data.userName + '<span style="font-size:13px;">学生</span>');
                    $("#student_img").attr('src', data.imgUrl);
                    $(".info p span:nth(2)").text(data.sex == 1 ? "男" : "女");
                    $(".popu div:nth(0) b").text(data.experienceValue);
                    $(".popu div:nth(1) b").text(data.finishCourseNum);
                    $(".have-learned span").eq(1).text(data.finishCourseNum);
                    $(".exp-number>span").text(data.experienceValue);
                    if (data.experienceValue >= 0 && data.experienceValue < 50) {
                        $("#experience").css('background-image', 'url(/img/tree-1.png)');
                    }
                    if (data.experienceValue >= 50) {
                        $("#experience").css('background-image', 'url(/img/egg.png)');
                    }

                }
            });
            return ret;
        }
        function studentScoreList() {
            window.location = "/studentScoreList/" + studentid;
        }
        function getStudentTotallist() {
            $.ajax({
                url: "/myclass/stastu.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    studentId: studentid
                },
                success: function (data) {
                    if (data.rows == null || data.rows.length == 0) {

                    }
                    else {
                        showStudentTotallist(data);
                    }
                }
            });
        }

        function showStudentTotallist(data) {
            var html = '';

            for (var i = 0; i < data.rows.length; i++) {
                var st = data.rows[i];
                var viewType = '';
                var rate=st.correctRate!=null?st.correctRate:'-'
                if (st.isView == true) {
                    viewType = '已看完';
                }else{
                    viewType = '未看过';
                }
                html += '<li class="content"><span class="I">' + st.lessonName + '</span><span class="II">' + viewType + '</span>';
                html += '<span class="III">' + st.doneExerciseCount+'/'+st.totalExerciseCount + '</span><span class="IV">' +rate + '</span></li>';
            }

            var tdiv = $('#fc_student_total ul');
            tdiv.html(html);
        }
        $(function () {
            studentid = $.trim($("body").attr("stuid"));
            $('#classname').text($('#className').val());
            $('#classname').attr('href', $('#classname').attr('href') + '/' + $('#classId').val());
            $('#studentname').text($('#studentName').val());
            $('.loading').show();
            getstudentInfo();
            getPetInfo();
            getStudentTotallist();
            $('.loading').fadeOut(1000);
        });

        function getPetInfo() {
            $.ajax({
                url: '/pet/selectedPet.do',
                success: function (data) {
                    petinfo = data.petInfo;
                    if (petinfo != null) {
                        $("#experience").css('background-image', 'url(' + petinfo.petimage + ')');
                        $('#petid').val(petinfo.id);
                        $('.pet-name').html(petinfo.petname);
                        $('.pet-name').attr('title', petinfo.petname);
                        /* $('#choosenPet').parent('a').attr('href','/petbag'); */
                    } else {
                        $("#experience").css('background-image', 'url(/img/egg.png)');
                    }
                },
                error: function () {
//	        			alert('服务器错误!');
                }

            });
        }

    </script>
    <title>学生统计</title>
    <style>
        #student_info_container {
            padding: 5px 20px;
            background: url(/img/fcteacherbackBig.jpg); /* background-size:100%; */
            overflow: hidden;
        }

        #student_info {
            position: relative;
            margin: 0px auto;
            width: 900px;
            color: white; /* background: url(/img/bgstudent.png); */
            height: 160px;
            overflow: hidden;
            background-repeat: no-repeat;
            background: rgba(0, 0, 0, 0.47);
            filter: progid:DXImageTransform.Microsoft.gradient(startcolorstr=#7F000000, endcolorstr=#7F000000);
        }

        #student_info > * {
            float: left;
        }

        #student_img {
            width: 124px;
            height: 124px;
            margin: 15px 10px;
            border: 3px solid white;
            border-radius: 5px;
        }

        #student_info div.info > p:first-child {
            margin-left: 10px;
            font-size: 24px;
        }

        #student_info div.info > p:first-child a {
            top: 12px;
            position: relative;
            margin-left: 10px;
        }

        #student_info div.info > p:first-child + p {
            margin-top: 30px;
        }

        #student_info div.info {
            margin-top: 20px;
            color: #fff;
            height: 110px;
            text-shadow: 1px 1px 10px #3A3A3A;
        }

        #student_info div.popu > div {
            display: inline-block;
            text-align: center;
            margin: 0 32px;
            float: left;
            overflow: hidden;
        }

        #student_info > div span {
            margin: 0 3px;
        }

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
            background: #ECF5FF;
            border-left: 1px solid #D0D0D0;
            border-right: 1px solid #D0D0D0;
        }

        .ypheader > div:first-child {
            padding: 10px 5px;
            border: 1px solid #D0D0D0;
        }

        #fc_video_list ul {
            border-bottom: none;
        }

        .fc_statistics_list ul span.I {
            text-align: left;
        }

        .fc_statistics_list span.I {
            width: 350px;
            padding-left: 10px;
        }

        .fc_statistics_list span.II {
            width: 200px;
        }

        .fc_statistics_list span.III {
            width: 150px;
        }

        .fc_statistics_list span.IV {
            width: 150px;
        }

        #experience {
            width: 80px;
            height: 140px;
            right: 110px;
            position: absolute;
            top: 0px;
            background-repeat: no-repeat;
            background-position: center;
            background-size: contain;
        }

        .have-learned {
            text-align: center;
            background-color: #AF6731;
            padding: 8px 10px;
            font-size: 19px;
            text-shadow: none;
            max-width: 120px;
        }

        .pet-name {
            overflow: hidden;
            font-size: 16px;
            position: absolute;
            right: 78px;
            bottom: 10px;
            text-shadow: 2px 0px 5px #000;
            width: 190px;
            text-align: center;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .input-name {
            width: 144px;
            font-size: 14px;
            padding: 5px;
            position: absolute;
            right: 90px;
            bottom: 6px;
            display: none;
        }

        .change-name {
            position: absolute;
            bottom: 33px;
            right: 110px;
            color: #00f;
            text-decoration: underline;
            font-size: 15px;
            display: none;
            font-weight: bold;
        }

        .user-exp {
            position: absolute;
            bottom: 0px;
            right: 0px;
        }

        .exp-title {
            position: absolute;
            bottom: 44px;
            right: 23px;
            font-size: 15px;
        }

        .exp-number {
            position: absolute;
            bottom: 23px;
            right: 23px;
            font-size: 14px;
            overflow: hidden;
            text-align: center;
            width: 47px;
        }
    </style>
</head>
<body stuid="${studentId}">
<%@ include file="../common_new/head.jsp" %>

<div id="student_info_container">
    <div id="student_info">
        <img id="student_img" <%-- src="${session.currentUser.middleImageURL}" --%> />

        <div class="info" style="overflow: hidden;">
            <p></p>

            <p class="have-learned">
                <span>已学课程</span>
                <span></span>
            </p>
        </div>
        <div id="experience" style="cursor:pointer;"></div>
        <a href="javascript:;" class="change-name">改名</a>

        <div class="pet-name"></div>
        <input class="input-name" type="text">

        <div id="user-exp" style="cursor:pointer;">
            <img class="user-exp" src="/img/exp.png">
            <span class="exp-title">经验值</span>

            <div class="exp-number">
                <span></span>
            </div>
        </div>
    </div>
</div>
<input hidden="hidden" id="className" name="className"/>
<input hidden="hidden" id="classId" name="classId"/>
<input hidden="hidden" id="studentName" name="studentName"/>

<c:if test="${role:isEducation(sessionValue.userRole)}">
    <div id="nav_to_my" style="background:white;padding:10px 0;">
        <div style="margin:0 auto;width:900px;"><a href="/user">&nbsp;首页&nbsp;</a>><a href="/browser">&nbsp;班级&nbsp;</a>>&nbsp;<a
                id="classname" href="/teacher/class"></a>&nbsp;>&nbsp;<a id="studentname" href="#"></a>&nbsp;</div>
    </div>
</c:if>
<div id="fc_student_total" class="fc_statistics_list ypheader center" style="min-height: 66%;">
    <div>
        <h4>学生统计列表</h4>
    </div>
    <div class="header">
        <span class="I">课程名称</span>|
        <span class="II">课程学习情况</span>|
        <span class="III">做题数/总题数</span>|
        <span class="IV">答题正确率</span>
    </div>
    <div class="header_foot"></div>
    <ul>
    </ul>
</div>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>