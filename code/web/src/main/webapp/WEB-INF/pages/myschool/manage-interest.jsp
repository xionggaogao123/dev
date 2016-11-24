<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理拓展课</title>
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
    <%--<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>--%>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/manage/manage-interest.js"></script>
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

        .select2-container .select2-choice{
            padding: 0 10px !important;
            height: 35px !important;
            line-height: 34px !important;
        }

        .edit-info-select{
            margin-left: 10px !important;
            margin-top: 0 !important;
        }
        .add-class-student{
            margin-left: 85px !important;
            margin-top: 10px !important;
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
    <div id="content_main" gradeId="${param.gradeId}">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container">
            <div class="main-container">

                <div class="manage-right-container">

                    <div class="class-right-title-bar"
                         style="background-color: #f3f7f3;width: 100%;height: 100px;line-height: 100px;font-size: 14px;display:none;">
                        <span class="title-info class-name">班级名称：</span>
                        <span class="title-info teacher-name">老师：adfa</span>&nbsp;&nbsp;
                        <!--数字人数-->
                        <span style="display: inline-block;line-height: 25px;width: 50px;vertical-align: middle"><span><span
                                class="fnum"></span><span class="snum"></span></span></span>
                    </div>

                    <!--拓展课列表 -->
                    <div class="list-container interest-list" belong="interest" style="overflow: hidden;width: 780px;">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span style="cursor:pointer;" id="category">班级列表 </span>
                        </div>
                        <div id="role" role="${param.role}" userId="${userId}"></div>
                        <c:if test="${param.role == 1}">
                        <div class="list-add-interest"><i class="fa fa-plus"></i> 新建拓展课</div>
                        <div class="list-interest-change"><i class="fa"></i>学生选课去向</div>
                        </c:if>
                        <ul class="list-ul">
                        </ul>
                    </div>

                    <!-- 拓展课各班列表 -->
                    <div class="list-container interest-class-list" belong="interest-class">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span onclick="gotoClassList(${param.role});" style="cursor:pointer;">班级列表 &gt; </span>
                            <span style="cursor:pointer;">学生列表 </span>
                        </div>
                        <div class="list-class-add interest-class-add-student"><i class="fa fa-plus"></i> 添加学生</div>
                        <div style="font-weight: bold;width: 100%;height: 35px;line-height: 30px;font-size: 15px;font-family: Microsoft YaHei;border-bottom: 1px solid #d0d0d0;margin-bottom: 10px;display:none;"
                             class="team-sutdent">
                            <!--选中状态-->
                            <span class="manage-tianjia manage-hover first-class">上半学期</span>
                            <!--为选中状态-->
                            <span class="manage-tianjia">下半学期</span>
                        </div>
                        <ul class="list-ul interest-class-student-list">
                            <li>
                                <div class="list-content">
                                    <span>吴峥</span>
                                    <span>行政班：</span>
                                    <span>数学</span>
                                </div>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                        </ul>
                        <ul class="list-ul second-interest-class-student-list" style="display:none;">
                            <li>
                                <div class="list-content">
                                    <span>吴峥</span>
                                    <span>行政班：</span>
                                    <span>数学</span>
                                </div>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                        </ul>
                    </div>


                    <div class="edit-container">

                        <!-- 添加拓展课学生 -->
                        <div class="edit-info-container add-student">
                            <div class="edit-title-bar">
                                <div>添加学生</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>搜索学生</span>
                                    <select id="gradeList" style="margin-left: 11px;border: 1px solid #aaa;border-radius: 4px;padding: 2px 4px;font-size: 13px;width: 131px;">
                                        <option value="">全部年级</option>
                                    </select>
                                    <select id="classList"  style="margin-left: 3px;border: 1px solid #aaa;border-radius: 4px;padding: 2px 4px;font-size: 13px;width: 131px;">
                                        <option value="">全部班级</option>
                                    </select>
                                    <select class="edit-info-select add-class-student" multiple></select>
                                </div>
                            </div>
                        </div>
                        <!-- 改变拓展课类别 -->
                        <div class="edit-info-container category">
                            <div class="edit-title-bar">
                                <div>改变类别</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <select class="edit-info-select category-select" ></select>
                                </div>
                            </div>
                        </div>
                        <!-- 编辑文才社 -->
                        <div class="edit-info-container edit-interest">
                            <div class="edit-title-bar">
                                <div>编辑拓展课</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group course-type" style="display:none">
                                    <span>课程类型</span>
                                    <input type="radio" name="classtype" checked="checked" id="cltype1" value='1'
                                           onclick="getRadio()">长课
                                    <input type="radio" name="classtype" id="cltype2" value='2' onclick="getRadio()">短课
                                    （<input type="checkbox" id="fteam" disabled><span>上半学期</span>
                                    <input type="checkbox" id="steam" disabled><span>下半学期</span>）
                                </div>
                                <div class="edit-input-group">
                                    <span>班级名称</span>
                                    <input type="text" class="edit-interest-name" maxlength="22">
                                </div>
                                <div class="edit-input-group">
                                    <span>当前学期</span>
                                    <span id="term" maxlength="22" style="margin-left: 10px;font-size: initial;">${term}</span>
                                </div>
                                <div class="edit-input-group">
                                    <span>选课时间</span>
                                    <!-- <input type="datetime-local" class="edit-interest-opentime" placeholder="例如：2014/09/01 07:00:00"> -->
                                    <input class="Wdate" type="text" style="width:145px;height:32px;" id="opentime"
                                           onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                                           value="2000-02-29 01:00:00"/>
                                    <span style="margin-left: 8px;">开放</span>
                                </div>
                                <div class="edit-input-group">
                                    <input class="Wdate" type="text" style="width:145px;height:32px;margin-left: 84px"
                                           id="closetime" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                                           value="2000-02-29 01:00:00"/>
                                    <span style="margin-left: 8px;">关闭</span>
                                </div>
                                <%--<div class="edit-input-group">
                                    <!-- <input type="datetime-local" class="edit-interest-closetime" style="margin-left: 84px;" placeholder="例如：2014/09/07 24:00:00"> -->
                                    <input class="Wdate" type="text" style="width:200px;height:32px;margin-left:84px;"
                                           id="closetime" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                                           value="2000-02-29 01:00:00"/>
                                    <span style="margin-left: 8px;">关闭</span>
                                </div>--%>
                                <div class="edit-input-group show-grade">
                                    <span>从属年级</span>

                                    <div style="width:340px;margin-left: 90px;margin-top:-20px;"
                                         class="edit-subject-selectgrade-interest">
                                        <label class="checkbox-inline">
                                            <input type="checkbox"> 全部
                                        </label>
                                    </div>
                                </div>
                                <div class="edit-input-group">
                                    <span>老师</span>
                                    <select class="edit-info-select add-interest-teacher"></select>
                                </div>
                                <div class="edit-input-group">
                                    <span>科目</span>
                                    <select class="edit-info-select add-class-subject">
                                        <option>语文</option>
                                        <option>数学</option>
                                        <option>英语</option>
                                        <option>物理</option>
                                    </select>
                                </div>
                                <div class="edit-input-group">
                                    <span>从属性别</span>
                                    <%--<div class="edit-sex">--%>
                                        <input type="checkbox" name="interest-sex" value="2" id="sex2" sexid="2"> 全部
                                        <input type="checkbox" name="interest-sex" value="0" id="sex0" sexid="1" style="margin-left: 20px;"> 男
                                        <input type="checkbox" name="interest-sex" value="1" id="sex1" sexid="0" style="margin-left: 20px;"> 女
                                    <%--</div>--%>
                                </div>
                                <div class="edit-input-group">
                                    <span>人数</span>
                                    <input type="number" min="1" class="edit-interest-number">
                                </div>
                                <div class="edit-input-group">
                                    <span>课程介绍</span>
                                    <%--<input type="number" min="2" class="edit-interest-info">--%>
                                    <textarea id="content" style="height:100px"></textarea>
                                </div>
                                <div class="edit-input-group">
                                    <span>上课地点</span>
                                    <input type="text" class="edit-interest-room" maxlength="50">
                                </div>
                                <div class="edit-input-group">
                                    <span style="float:left;margin-right:10px;">上课时间</span>

                                    <div class="edit-select-time">
                                        <ul class="select-time-day">
                                            <li>周一</li>
                                            <li>周二</li>
                                            <li>周三</li>
                                            <li>周四</li>
                                            <li>周五</li>
                                            <li>周六</li>
                                            <li>周日</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="11">1</li>
                                            <li val="21">1</li>
                                            <li val="31">1</li>
                                            <li val="41">1</li>
                                            <li val="51">1</li>
                                            <li val="61">1</li>
                                            <li val="71">1</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="12">2</li>
                                            <li val="22">2</li>
                                            <li val="32">2</li>
                                            <li val="42">2</li>
                                            <li val="52">2</li>
                                            <li val="62">2</li>
                                            <li val="72">2</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="13">3</li>
                                            <li val="23">3</li>
                                            <li val="33">3</li>
                                            <li val="43">3</li>
                                            <li val="53">3</li>
                                            <li val="63">3</li>
                                            <li val="73">3</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="14">4</li>
                                            <li val="24">4</li>
                                            <li val="34">4</li>
                                            <li val="44">4</li>
                                            <li val="54">4</li>
                                            <li val="64">4</li>
                                            <li val="74">4</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="15">5</li>
                                            <li val="25">5</li>
                                            <li val="35">5</li>
                                            <li val="45">5</li>
                                            <li val="55">5</li>
                                            <li val="65">5</li>
                                            <li val="75">5</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="16">6</li>
                                            <li val="26">6</li>
                                            <li val="36">6</li>
                                            <li val="46">6</li>
                                            <li val="56">6</li>
                                            <li val="66">6</li>
                                            <li val="76">6</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="17">7</li>
                                            <li val="27">7</li>
                                            <li val="37">7</li>
                                            <li val="47">7</li>
                                            <li val="57">7</li>
                                            <li val="67">7</li>
                                            <li val="77">7</li>
                                        </ul>
                                        <ul class="select-time-lesson">
                                            <li val="18">8</li>
                                            <li val="28">8</li>
                                            <li val="38">8</li>
                                            <li val="48">8</li>
                                            <li val="58">8</li>
                                            <li val="68">8</li>
                                            <li val="78">8</li>
                                        </ul>
                                    </div>
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
</html>