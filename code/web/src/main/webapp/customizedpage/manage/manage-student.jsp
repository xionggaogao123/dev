<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
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
    <script type="text/javascript" src="/static/js/manage/manage-student.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript" src="/static/js/manage/school-type.js"></script>

    <script>
        var currentPageID = 5;
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
<%--<%@ include file="../common_new/head.jsp" %>--%>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div id="content_main_container">
    <div id="content_main" classId="${param.classId}">
        <!-- 左侧导航-->
        <%--<%@ include file="../common_new/col-left.jsp" %>--%>
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
                    </div>
                    <!-- 各班列表 -->
                    <div class="list-container pre-class-list" belong="pre-class">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span onclick="gradeList();" style="cursor:pointer;">年级列表 &gt; </span>
                            <span class="list-title-grade-pre" style="cursor:pointer;"
                                  onclick="classInfoList()">一年级</span>
                            <span> &gt; </span>
                            <span class="list-title-class">一年级(1)班</span>
                        </div>
                        <div class="list-class-add class-add-teacher"><i class="fa fa-plus"></i> 添加老师</div>
                        <ul class="list-ul class-teacher-list">
                            <li>
                                <div class="list-content">
                                    <span>吴峥</span>
                                    <span>数学</span>
                                </div>
                                <i class="fa fa-pencil list-edit"></i>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                        </ul>
                        <div class="list-class-add class-add-student"><i class="fa fa-plus"></i> 新建学生</div>
                        <ul class="list-ul class-student-list">
                            <li>
                                <div class="list-content">
                                    <span>王子恒</span>
                                </div>
                                <i class="fa fa-pencil list-edit"></i>
                                <i class="fa fa-trash-o list-delete"></i>
                                <img class="list-change-class" src="/img/K6KT/changeclass.png" title="学生调换班级"/>
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

                        <!-- 添加老师 -->
                        <div class="edit-info-container add-teacher">
                            <div class="edit-title-bar">
                                <div>添加老师</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>搜索老师</span>
                                    <select class="edit-info-select add-class-teacher" id="add-class-teacher"></select>
                                </div>
                                <div class="edit-input-group">
                                    <span>本班任课</span>
                                    <select class="edit-info-select add-class-subject" id="add-class-subject">
                                        <option>语文</option>
                                        <option>数学</option>
                                        <option>英语</option>
                                        <option>物理</option>
                                    </select>

                                </div>
                            </div>
                        </div>
                        <!-- 编辑学生 -->
                        <div class="edit-info-container edit-student">
                            <div class="edit-title-bar">
                                <div>编辑学生</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">

                                <div class="edit-input-group">
                                    <span>学生姓名</span>
                                    <input type="text" class="edit-student-name">
                                </div>
                                <div class="edit-input-group">
                                    <span>性别</span>
                                    <input type="radio" name="sex" checked="checked" value='1'>男
                                    <input type="radio" name="sex" value='0'>女
                                </div>

                                <div class="user-stuuser">
                                </div>
                                <div class="user-paruser">
                                </div>
                                <div class="reset-password-form">
                                    <span class="reset-student-password">还原学生初始密码</span>
                                    <img src="/img/K6KT/yes.png" style="display:none;">
                                    <span class="reset-parent-password" style="margin-left: 10px;">还原家长初始密码</span>
                                    <img src="/img/K6KT/yes.png" style="display:none;">
                                </div>
                            </div>
                        </div>
                        <!-- 调换班级 -->
                        <div class="edit-info-container change-class">
                            <div class="edit-title-bar">
                                <div>调换班级</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>用户名</span>
                                    <span class="change-stu-name">王子恒</span>
                                </div>
                                <div class="edit-input-group">
                                    <span>调换班级</span>
                                    <select class="change-class-select change-class-grade">
                                    </select>
                                    <select class="change-class-select change-class-class">
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

<%--<%@ include file="../common_new/foot.jsp" %>--%>

</body>
<script>


</script>
</html>