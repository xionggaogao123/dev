<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="r" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <title>拓展课</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript">
        $(function () {
            initFooter();
            getTerms();
            getCategory();
            $('#terms').on('change',function(){
                getInterestClasses(true);
            });
            $('#category').on('change',function(){
                getInterestClasses(false);
            });
            $('.elective_tab').each(function (i) {
                if (i == 0) {
                    $(this).addClass('tab_chosen').css('border-bottom', 'none');
                    $('.loading-img').fadeIn('fast');
                    getInterestClasses(false);
                }
                $(this).click(function () {
                    $('.elective_tab').removeClass('tab_chosen').css('border-bottom', '');
                    $(this).addClass('tab_chosen').css('border-bottom', 'none');
                    $('.header').nextAll().remove();
                    $('.loading-img').fadeIn('fast');
                    if (i == 0) {
                        getInterestClasses(false);
                    } else {
                        getInterestClasses(true);
                    }
                });
            });

            $('body').on('click', '.info', function(){
                $(this).parent().parent().next().slideToggle();
            })

            $('body').on('click', '.select-course', function () {
                var cid = $(this).attr('data-cid');
                var courseType = $(this).attr('courseType');
                var content = $(this).prev().find('.close-time').text();
                var endtime = content.substring(3, content.length);
                if (endtime != '') {
                    endtime = new Date(endtime);
                    curtime = new Date();
                    if (endtime.getTime() < curtime.getTime()) {
                        alert('超出选课时间');
                        getInterestClasses(false);
                    } else {
                        courseOperate(1, cid, courseType);
                    }
                } else {
                    courseOperate(1, cid, courseType);
                }
            });
            $('body').on('click', '.select-first-course', function () {
                var cid = $(this).attr('data-cid');
                var content = $(this).prev().find('.close-time').text();
                var endtime = content.substring(3, content.length);
                if (endtime != '') {
                    endtime = new Date(endtime);
                    curtime = new Date();
                    if (endtime.getTime() < curtime.getTime()) {
                        alert('超出选课时间');
                        getInterestClasses(false);
                    } else {
                        courseOperate(1, cid, 1);
                    }
                } else {
                    courseOperate(1, cid, 1);
                }
            });
            $('body').on('click', '.select-second-course', function () {
                var cid = $(this).attr('data-cid');
                var content = $(this).prev().find('.close-time').text();
                var endtime = content.substring(3, content.length);
                if (endtime != '') {
                    endtime = new Date(endtime);
                    curtime = new Date();
                    if (endtime.getTime() < curtime.getTime()) {
                        alert('超出选课时间');
                        getInterestClasses(false);
                    } else {
                        courseOperate(1, cid, 2);
                    }
                } else {
                    courseOperate(1, cid, 2);
                }
            });
            $('body').on('click', '.quit-course-div', function () {
                if (confirm("确认退课？")) {
                    var cid = $(this).parent().attr('data-cid');
                    var type = $(this).attr('coursetype');
                    courseOperate(2, cid, type);
                }
            });
            $('body').on('click', '.eval-course', function () {
                var termType = $('#terms').val();
                var cid = $(this).parent().attr('data-cid');
                window.open('/myclass/studentScore.do?classId='+cid + '&termType=' + termType);

            });
        });

        $(window).resize(function () {
            initFooter();
        });

        function initFooter() {
            var wheight = window.innerHeight;
            $('#main_content').css('minHeight', wheight - 110 - 40);
        }

        function courseOperate(status, instclassid, type) {
            var url;
            if (status == '1') {
                url = '/myclass/addStuToInterestClass.do';
            } else {
                url = '/myclass/removeStuFromInterestClass.do';
            }
            $('.loading-img').fadeIn('fast');
            $.ajax({
                url: url,
                type: "post",
                dataType: "json",
                async: false,
                data: {
                    classId: instclassid,
                    userId: '${sessionValue.id}',
                    courseType: type
                },
                success: function (data) {
                    $('.header').nextAll().remove();
                    if ($('.elective_tab').eq(0).hasClass('tab_chosen')) {
                        getInterestClasses(false);
                    } else {
                        getInterestClasses(true);
                    }
                    if (data.code == 500) {
                        alert(data.msg);
                    } else {
                        if (status == '1') {
                            alert("选课成功！");
                        } else {
                            alert("退课成功！");
                        }
                    }
                },
                error: function () {
                }
            });
        }

        function getInterestClasses(isMyself) {
            var url;
            var termType = $('#terms').val();
            if (isMyself) {
                url = '/myclass/stuinterestclass.do';
                $('#terms').show();
                $('#category').hide();
            } else {
                url = "/myclass/interestclassofschool.do";
                
                
                var loction=location.href;
                
                if(loction.indexOf("gx_type=1")>0)
                {
                	 url = "/myclass/interestclassofschool.do?gx_type=1";
                }
                
                if(loction.indexOf("gx_type=2")>0)
                {
                	 url = "/myclass/interestclassofschool.do?gx_type=2";
                }
                
                termType = -1;
                $('#terms').hide();
                $('#category').show();
            }
            $.ajax({
                url: url,
                type: "post",
                dataType: "json",
                async: false,
                data: {termType:termType, categoryId:$('#category').val()},
                success: function (data) {
                	
                	$('.loading-img').fadeOut('fast');
                    $('li.content').remove();
                    var rows = data.interestClassInfoList;
                    if (!isMyself && rows.length == 0) {
                        $('#elective_content').show();
//                        $('#main_content').hide();
                    } else {
                        $('#elective_content').hide();
                        $('#main_content').show();
                        for (var i = 0; i < rows.length; i++) {
                            var content = combinContent(rows[i],isMyself);
                            $('#elective_list').append(content);
                        }
                       
                    }
                },
                error: function () {
                }
            });
        }

        function combinContent(row,isMyself) {
            var content = '';
            var opentime = row.openTime;
            var closetime = row.closeTime;
            var coursetype = row.isLongCourse;
            var first = row.firstTerm;
            var second = row.secondTeram;
            var tuike = '';
            if (row.isChoose && (!coursetype) && row.chooseType == 1) {
                tuike = '退课1';
            } else if (row.isChoose && (!coursetype) && row.chooseType == 2) {
                tuike = '退课2';
            } else {
                tuike = '退课';
            }
            if (opentime != null && closetime != null) {
                opentime = opentime.replace('T', ' ').replace(/-/g, '/');
                closetime = closetime.replace('T', ' ').replace(/-/g, '/');
            } else {
                opentime = '';
                closetime = '';
            }
            content += '<li class="content"><div>';
            content += '<span class="title">' + row.className + '</span>';
            content += '<span>' + row.teacherName + '</span>';
            content += '<span style="line-height: 30px;display: inline-block;width: 140px;padding-left: 20px;min-height: 40px;">' + formatClassTime(row.classTime) + '</span>';
            if (!row.isLongCourse && !isMyself) {
                content += '<span style="width:100px;">' + row.fstulist.length + '/' + row.totalStudentCount + ' ' +row.sstulist.length + '/' + row.totalStudentCount+'</span>';
            } else {
                content += '<span style="width:100px;">' + row.studentList.length + '/' + row.totalStudentCount + '</span>';
            }

            content += '<span style="width:180px;text-align: left;"><div class="open-time">开放：' + opentime + '</div>';
            content += '<div class="close-time">关闭：' + closetime + '</div></span>';
            var nb_role = '${sessionValue.userRole}';
            if (isStudent(nb_role)) {
                //兴趣班状态为可用
                if (row.state == 1) {
                    if (row.openState == 1) {
                        //时间未到 不可选
                        content += '<div><span class="class-close">未开放</span><span class="info">课程介绍</span></div>';
                    } else if (row.openState == 3) {
                        if (row.isChoose) {
                            content += '<div><span class="quit-course" data-cid=' + row.id + '><span class="class-close" style="width: 50px">' + tuike + '</span><div class="eval-course">课程评价</div></span><span class="info">课程介绍</span></div>';
                        } else {
                            content += '<div><span class="class-close">已关闭</span><span class="info">课程介绍</span></div>';
                        }
                    } else if (row.openState == 2) {//可选状态
                        //已选
                        if (row.isChoose) {
                            content += '<div><span class="quit-course" data-cid=' + row.id + '><div class="quit-course-div" coursetype='+row.chooseType+'>' + tuike + '</div><div class="eval-course">课程评价</div></span><span class="info">课程介绍</span></div>';
                        } else {
                            if ((row.isLongCourse && row.studentList.length == row.totalStudentCount && row.studentList.length!=0)
                                    ||((row.firstTerm==1 && row.fstulist !=null && row.fstulist.length >= row.totalStudentCount && row.fstulist.length!=0)
                                    && (row.secondTerm==1 && row.sstulist !=null && row.sstulist.length >= row.totalStudentCount && row.sstulist.length!=0))) {
                                content += '<div><span class="class-close">人数已满</span><span class="info">课程介绍</span></div>';
                            } else if (row.timeConflict) {
                                content += '<div><span class="class-close">时间冲突</span><span class="info">课程介绍</span></div>';
                            } else if (row.firstTerm == 1 || row.secondTerm == 1) {
                                if (row.firstTerm == 1 && row.fchoose == 1 && row.fstulist.length < row.totalStudentCount) {
                                    content += '<div><span style="position: relative;left: 0px;width: 70px;overflow: hidden;height: 35px;" class="select-course" data-cid="' + row.id + '" courseType=1 ><div>短课1</div></span>';
                                }
                                if (row.secondTerm == 1 && row.schoose ==1 && row.sstulist.length < row.totalStudentCount) {
                                    content += '<span style="z-index: 9999999999999999999; position: absolute;left: 689px;display: inline-block;width: 70px;      overflow: hidden;      height: 35px;" class="select-course" data-cid="' + row.id + '" courseType=2 ><div>短课2</div></span><span class="info">课程介绍</span></div>';
                                }
                            } else {
                                content += '<div><span class="select-course"  data-cid="' + row.id + '" courseType=0><div>我选</div></span><span class="info">课程介绍</span></div>';
                            }
                        }
                    }
                } else {
                    //兴趣班状态不可用
                    if (row.isChoose) {
//                        content += '<span class="class-close">' + tuike + '</span><span class="info">课程介绍</span>';
                        content += '<div><span class="quit-course" data-cid=' + row.id + '><span class="class-close" style="width: 50px">' + tuike + '</span><div class="eval-course">课程评价</div></span><span class="info">课程介绍</span></div>';
                    } else {
                        content += '<span class="class-close">已关闭</span><span class="info">课程介绍</span>';
                    }
                }
            }
            else if (isParent(nb_role)&&isMyself) {
                //兴趣班状态为可用
                if (row.state == 1) {
                    if (row.openState == 1) {
                        //时间未到 不可选
                        content += '<div><span class="class-close">未开放</span><span class="info">课程介绍</span></div>';
                    } else if (row.openState == 3) {
                        if (row.isChoose) {
                            content += '<div class="eval-course">课程评价</div>';
                        } else {
                            content += '<div><span class="class-close">已关闭</span><span class="info">课程介绍</span></div>';
                        }
                    } else if (row.openState == 2) {//可选状态
                        //已选
                        if (row.isChoose) {
                            content += '<div><span class="quit-course" data-cid=' + row.id + '><div class="eval-course">课程评价</div></span><span class="info">课程介绍</span></div>';
                        }
                    }
                } else {
                    //兴趣班状态不可用
                    if (row.isChoose) {
                        content += '<div><span class="class-close">' + tuike + '</span><span class="info">课程介绍</span></div>';
                    } else {
                        content += '<div><span class="class-close">已关闭</span><span class="info">课程介绍</span></div>';
                    }
                }
            }
            if(row.classContent == "" || row.classContent == null){
                content +='</div><div style="display:none;margin-left: 40px;float: left">该课程暂无介绍</div>'
            } else {
                content +='</div><div style="display:none;margin-left: 40px">'+ row.classContent +'</div>'
            }

            return content;
        }

        function formatClassTime(classTime){
            var week = ['一','二','三','四','五','六','日'];
//            var lis = ['一','二','三','四','五','六','七','八'];
            var str = '';
            for(var i in classTime){
                var ct = classTime[i];
                if('' != ct){
                    var day = ct.substr(0,1);
                    var li = ct.substr(1,1);
                    str += "周" + week[day-1] + "第" + li + "节课；";
                }
            }
            return str;
        }

        function getTerms(){
            $.ajax({
                url: '/interestClassTerms/terms.do',
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (data) {
                    var rows = data.terms;
                    $('#terms').empty();
                    var html = '';
                    for(var i=rows.length-1; i>=0; i--){
                        html += '<option value="'+rows[i].value+'">'+rows[i].name+'</option>'
                    }
                    $('#terms').append(html);
                },
                error: function () {
                }
            });
        }

        function getCategory(){
            $.ajax({
                url: '/myclass/getGradeAndCategory.do',
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (data) {
                    var rows = data.categoryList;
                    $('#category').empty();
                    var html = '<option value="allCategory">全部分类</option>';
                    for(var i=0; i<rows.length; i++){
                        html += '<option value="'+rows[i].id+'">'+rows[i].name+'</option>'
                    }
                    html += '<option value="UN">未分类</option>';
                    $('#category').append(html);
                },
                error: function () {
                }
            });
        }


    </script>
    <style>

        .info {
            cursor: pointer;
            text-decoration:underline;
            float: right!important;
            margin-right: 30px;

        }
        #tab_nav {
            background: rgb(250, 250, 250);
            border-bottom: 2px solid #ff7900;
            padding-bottom: 3px;
        }

        .elective_tab:first-child {
            margin-left: 20px;
        }
        .cont-main-s{
            border-bottom: 2px solid #ff7900;
            height: 52px;
            overflow:visible;
        }
        .cont-main-top{
            overflow: visible;
        }
        .elective_tab {
            display: inline-block;
            width: 105px;
            height: 35px;
            line-height: 35px;
            text-align: center;
            color: #ffa655;
            font-size: 18px;
            cursor: pointer;
            background: #fff;
            margin: 15px 0 0 0;
            border: 2px solid #fff;
            border-bottom: 2px solid  #ff7900;
        }

        #main_content .tab_chosen {
            border: 2px solid #ff7900;
            border-bottom: none;
            background: white;
            margin: 17px 0 0 0;
        }


        .return {
            font-size: 16px;
            float: right;
            background: #f64444;
            color: white;
            padding: 5px 20px;
            margin-top: 20px;
            margin-right: 25px;
            border-radius: 4px;
        }

        #elective {
            background: white;
            overflow: hidden;
        }
		#elective_list li{
			width:100%;
		}
        #elective_list li:hover {
            background: rgb(238, 238, 238);
        }

        #elective_list li.header span {
            font-size: 18px;
            color: #969696;
        }

        #elective_list li.content span {
            font-size: 16px;
            overflow: hidden;
        }

        #elective_list li.content span .open-time,
        #elective_list li.content span .close-time {
            font-size: 13px;
            line-height: normal;
        }

        #elective_list li {
            line-height: 40px;
            float: left;
            border-bottom: 1px solid #ddd;
        }

        #elective_list li span {
            float: left;
            width: 115px;
            text-align: center;
        }

        .quit-course-I div {
            margin-left: 3px;;
            width: 80px;
            margin: 0 auto;
            background-color: #34e083;
            color: #fff;
            line-height: 30px;
            height: 30px;
            margin-top: 5px;
            border-radius: 5px;
            cursor: pointer;
            display: inline;
            padding: 5px 15px;
        }

        .select-course div {
            display: inline-block;
            margin: 0 auto;
            background-color: #34DF83;
            color: #fff;
            line-height: 30px;
            height: 30px;
            margin-top: 5px;
            border-radius: 5px;
            cursor: pointer;
            padding:0 5px;
        }

        .quit-course div {
            margin-right:3px;
            padding:0 9px;
            background-color: #FFA85A;
            color: #fff;
            line-height: 30px;
            height: 30px;
            margin-top: 5px;
            border-radius: 5px;
            cursor: pointer;
            float:left;
        }

        .quit-course-disabled div {
            background-color: #e0e0e0;
            cursor: not-allowed;
            padding:0 9px;
            color: #fff;
            line-height: 30px;
            height: 30px;
            margin-top: 5px;
            border-radius: 5px;
        }

        .time-conflict {
            color: #fbbd00;
        }

        .student-full {
            color: #FD6283;
        }

        .class-close {
            color: #737373;
        }

        .loading-img {
            position: absolute;
            top: 210px;
            left: 55%;
            z-index: 100;
            display: none;
        }
    </style>
</head>
<body style="font-family:Microsoft YaHei;background-color: #ffffff">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<div style="background: #ffffff">
<div style="width: 1000px;margin: 0 auto;background-color: white;overflow: hidden;">
    <%@ include file="../common_new/col-left.jsp" %>
    <img src="/img/loading.gif" class='loading-img'>

    <div id="main_content" style="margin-left: 10px;;width:770px;position:relative;left: 20px;min-resolution: 437px;overflow: hidden;">
        <div class="cont-main-top"><%--<div id="tab_nav">--%>
            <%--<a href="/user"><span class="return">返回</span></a>--%>
        <%--</div>--%>
            <div class="cont-main-s">
        <span class="elective_tab" style="left:25px;font-weight: bold">选课列表</span>
        <span class="elective_tab" style="left:131px;font-weight: bold">我的选课</span>
            </div>
        <select id="terms" class="terms-I">
            <option value="9">sss</option>
        </select>
            <select id="category" class="terms-I">
                <option value="allCategory">全部分类</option>
            </select>
            </div>

        <div id="elective">
            <ul id="elective_list">
                <li class="header">
                    <span>课程名称</span>
                    <span>老师姓名</span>
                    <span>上课时间</span>
                    <span style="width:100px;">人数</span>
                    <span style="width:200px;">时间</span>
                    <span>操作</span>
                </li>
            </ul>
        </div>
    </div>
    <div style="font-size: 16px;color: #000000;font-family: 'Microsoft YaHei';text-align: center;margin-top: 5em;overflow: hidden; display:none;position: absolute;top:20%;left:48%;"
         id="elective_content">
        <span>您的学校暂时没有推出选课，请耐心等候!</span>
    </div>
</div>
</div>
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
</body>
</html>
