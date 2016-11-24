<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" href="/static/css/inform.css">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news.css"/>

    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/manage/manage-class.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript" src="/static/js/manage/school-type.js"></script>

    <script>
        var currentPageID = 5;
        $(function () {
            var gradeName = getUrlParam("gradeName");
            $(".list-title-grade").text(decodeURI(gradeName));
        });

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        }
    </script>
    <style type="text/css">

        .reset-teacher-password {
            padding: 7px 0px;
            background-color: #FF5555;
            margin-left: 150px;
            width: 130px;
            font-size: 14px;
            color: #fff;
            font-weight: bold;
            text-align: center;
            border-radius: 3px;
            cursor: pointer;
        }

        .Wdate {
            border: #999 1px solid;
            height: 20px;
            background: #fff url(/img/datePicker.gif) no-repeat right;
        }

        .Wdate::-ms-clear {
            display: none;
        }

        .WdateFmtErr {
            font-weight: bold;
            color: red;
        }
    </style>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div id="content_main_container">
    <div id="content_main" gradeId="${param.gradeId}" gradeName="${param.gradeName}">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container">
            <div class="main-container">
                <div class="manage-left-container" style="display: none;">
                    <%--<img class="manager-img" src="${sessionValue.avatar}">--%>

                    <div class="manager-name">${sessionValue.userName}</div>
                    <ul class="manage-select">
                        <li id="tag3" data-target="class">
                            <div>管理班级</div>
                        </li>
                    </ul>

                </div>
                <div class="manage-right-container">
                    <div class="right-title-bar">
                        <div style="padding-left: 20px;margin-top:-10px;">
                            <div style="font-size: 30px;display: inline">
                                <span class="title-school-year"></span>
                            </div>
                        <span style="background-color: #ff7b00;cursor: pointer;border-radius: 4px;color: white;width: 130px;display: inline-block;text-align: center;position: relative;top:-5px;display:none;"
                              class="start-new-year">
                            开始新学期
                        </span>
                        </div>
                        <span class="title-info">学校名称：</span>
                        <span class="title-school-name">XX小学</span>
                        <span class="title-info">学段：</span>
                        <span class="title-school-level">小学</span>
                        <i class="fa fa-pencil title-edit"></i>

                        <div class="title-resetpassword">设置新建用户默认密码</div>
                        <div id="compile-but" class="title-resetselectpwd">密码重置</div>
                    </div>

                    <!-- 添加重置密码人员 -->
                    <div id="bg" class="bg"></div>
                    <div class="contacts-main" style="display: none;">
                        <div class="contacts-top">
                            <span class="contacts-top-left">选择重置密码人员</span>
                                <span class="contacts-top-right-I">
                                    <button class="contacts-top-right" onclick="sureUserCancel()">取消</button>
                                    <button class="contacts-top-right-II" onclick="sureUser()">确定</button>
                                </span>
                        </div>
                        <div class="contacts-IN">
                            <input id="selectUser1">
                        </div>

                        <div class="contacts-middle">
                            <ul>
                                <li id="xiaoyuan_div_li" class="contacts-bottom-H" onclick="userChoose('xiaoyuan_div')">校园</li>
                                <li id="dep_div_li"  onclick="userChoose('dep_div')">部门</li>
                                <%--<li id="teacher_div_li" onclick="userChoose('teacher_div')">老师</li>--%>
                                <li id="student_div_li" onclick="userChoose('student_div')">学生</li>
                                <li id="parent_div_li" onclick="userChoose('parent_div')">家长</li>
                                <li id="teacher2_div_li"  onclick="userChoose('teacher2_div')">老师</li>
                            </ul>
                        </div>

                        <div id="xiaoyuan_div">
                            <div class="contacts-bottom">
                                <div class="contacts-bottom-text">
                                    <ul id="ul">
                                        <li class="contacts-odd" onclick="selectAll(1)">
                                            <div class="comtacts-sv" name="comtacts-sv"></div> <span>全校老师</span>
                                        </li>

                                        <li class="contacts-even" onclick="selectAll(2)">
                                            <div class="comtacts-sv" name="comtacts-sv"></div> <span>全校同学</span>
                                        </li>

                                        <li class="contacts-even" onclick="selectAll(3)">
                                            <div class="comtacts-sv" name="comtacts-sv"></div> <span>全校师生</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div id="dep_div" style="display: none">
                            <div class="contacts-bottom-nav">
                                <ul>
                                    <li>部门</li>
                                </ul>
                            </div>
                            <div class="contacts-bottom">
                                <div id="dep_user_group" class="contacts-bottom-text"></div>
                            </div>
                        </div>

                        <div id="teacher_div" style="display: none">
                            <div class="contacts-pl" style="display: none;">
                                <input placeholder="输入关键字检索" onchange="searchName('teacher_user_group',this)" >
                            </div>
                            <div class="contacts-bottom-nav">
                                <ul>
                                    <li onclick="getusets(1,1)">学科</li>
                                    <li onclick="getusets(2,1)">班级</li>
                                    <li onclick="getusets(3,1)">班主任</li>
                                </ul>
                            </div>
                            <div class="contacts-bottom">
                                <div id="teacher_user_group" class="contacts-bottom-text"></div>
                            </div>
                        </div>
                        <div id="student_div" style="display: none">
                            <div class="contacts-pl" style="display: none;">
                                <input placeholder="输入关键字检索" >
                            </div>
                            <div class="contacts-bottom-nav">
                                <ul>
                                    <li>班级</li>
                                </ul>
                            </div>
                            <div class="contacts-bottom">
                                <div id="student_user_group" class="contacts-bottom-text"></div>
                            </div>
                        </div>

                        <div id="teacher2_div" style="display: none">
                            <div class="contacts-pl" style="display: none;">
                                <input placeholder="输入关键字检索" >
                            </div>

                            <div class="contacts-bottom">
                                <div id="teacher2_user_group" class="contacts-bottom-text"></div>
                            </div>
                        </div>

                        <div id="parent_div" style="display: none">
                            <div class="contacts-pl" style="display: none;">
                                <input placeholder="输入关键字检索" >
                            </div>
                            <div class="contacts-bottom-nav">
                                <ul>
                                    <li>班级</li>
                                </ul>
                            </div>
                            <div class="contacts-bottom">
                                <div id="parent_user_group" class="contacts-bottom-text"></div>
                            </div>
                        </div>
                    </div>


                    <!-- 班级列表 -->
                    <div class="list-container class-list" belong="class" style="overflow: hidden;">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span onclick="gradeList();" style="cursor:pointer;">年级列表 &gt; </span>
                            <span id="list-title-grade" class="list-title-grade">${param.gradeName}</span>
                        </div>
                        <div class="list-add-class"><i class="fa fa-plus"></i> 新建班级</div>
                        <div class="batch-export" style="display:none"><i class="fa"></i> 批量导入人员</div>
                        <ul class="list-ul">
                            <li>
                                <div class="list-content">
                                    <span>一年级1班</span>
                                    <span>班主任：</span>
                                    <span class="grade-manager-name">吴峥</span>
                                </div>
                                <i class="fa fa-pencil list-edit"></i>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                        </ul>
                    </div>


                    <div class="edit-container">
                        <!-- 设置默认密码 -->
                        <div class="edit-info-container edit-password">
                            <div class="edit-title-bar">
                                <div>设置默认密码</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>密码</span>
                                    <input type="text" class="edit-password-input">
                                </div>
                            </div>
                        </div>
                        <!-- 学校信息 -->
                        <div class="edit-info-container edit-school">
                            <div class="edit-title-bar">
                                <div>学校信息</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>学校名称</span>
                                    <input type="text">
                                </div>
                                <div class="edit-input-group">
                                    <span style="float:left;margin-right:10px;">学段</span>

                                    <div style="width:280px;">
                                        <label class="checkbox-inline">
                                            <input type="checkbox"> 小学
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox"> 初中
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox"> 高中
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 编辑班级 -->
                        <div class="edit-info-container edit-class">
                            <div class="edit-title-bar">
                                <div>编辑班级</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>班级名称</span>
                                    <span class="edit-class-grade" style="margin-left:10px;">一年级</span>
                                    <input type="text" class="edit-class-number">
                                </div>
                                <div class="edit-input-group">
                                    <span>班主任</span>
                                    <select class="edit-info-select edit-class-teacher">
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="edit-commit-btn-container">
                            <div class="edit-commit-btn">确定</div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- right end-->
        </div>
    </div>
</div>

<%@ include file="../common_new/foot.jsp" %>

</body>
<script>


</script>
</html>