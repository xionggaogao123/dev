<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/7/14
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body term="${term}" gradeId="${gradeId}" mode="${mode}">


<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <c:if test="${mode != 0}">
                        <li class="cur" id="ZBK"><a href="javascript:;">走班课</a><em></em></li>
                    </c:if>
                    <li id="FZBK" <c:if test="${mode == 0}">class="cur"</c:if>><a href="javascript:;">非走班课</a><em></em></li>
                    <li id="TYK"><a href="javascript:;">体育课（男女分班）</a><em></em></li>
                    <%--<li id="XXK"><a href="javascript:;" >选修课</a><em></em></li>--%>
                    <li id="FZZB"><a href="javascript:;">分层走班课</a><em></em></li>
                    <li id="DSZ"><a href="javascript:;">单双周课</a><em></em></li>
                </ul>
            </div>

            <div class="tab-main">
                <div class="zouban-title clearfix">
                    <div class="title-left">
                        <h3>${term}<em>${gradeName}</em></h3>
                    </div>
                    <div class="title-right">
                        <a class="back" href="javascript:void(0);">&lt;&nbsp;返回走班教务管理</a>
                    </div>
                </div>
                <!--================走班课start====================-->
                <div id="tab-ZBK" class="tab-ZBK" <c:if test="${mode ==0}">style="display: none;" </c:if>>
                    <div class="ZBK-top">
                        <span style="color: red; font-size: 15px;">已经进行后续步骤操作后，请不要随意添加或删除学科，否则后面步骤都要重新设置！！！</span>
                        <span class="ZBK-II-XZ h-cursor">+新增学科</span>
                    </div>
                    <div class="ZBK-info">
                        <table>
                            <thead>
                            <tr>
                                <th width="187">学科</th>
                                <th width="187">等级考每周课时</th>
                                <th width="187">合格考每周课时</th>
                                <th width="187">操作</th>
                            </tr>
                            </thead>
                            <tbody id="ZB-subjects">
                            </tbody>
                            <script id="ZB-subjectsTmpl" type="text/template">
                                {{ for(var i in it) { }}
                                <tr subConfigId="{{=it[i].subjectConfId}}" subId="{{=it[i].subjectId}}">
                                    <td class="ZBK-col">{{=it[i].subjectName}}</td>
                                    <td class="adv">{{=it[i].advanceTime}}</td>
                                    <td class="sim">{{=it[i].simpleTime}}</td>
                                    <td>
                                        <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                    </td>
                                </tr>
                                {{ } }}
                            </script>
                        </table>
                    </div>
                </div>
                <!--================走班课end====================-->

                <!--================非走班课start=================-->
                <div id="tab-FZBK" class="tab-FZBK" <c:if test="${mode ==0}">style="display: block;" </c:if>>
                    <div class="zouban-addbtn">
                        <ul class="clearfix">
                            <li class="subject-manage zb-active">①学科管理</li>
                            <li class="teacher-set">②老师设置</li>
                        </ul>
                    </div>
                    <div class="fzbk">
                        <!--==============学科管理start=====================-->

                        <div class="FZBK-II" id="tab-FZBK-II">
                            <div class="FZBK-II-top">
                                <span class="FZBK-II-XZ h-cursor">+新增学科</span>
                            </div>
                            <div class="FZBK-info" id="tab-XKGl">
                                <table style="width: 99%">
                                    <thead>
                                    <tr>
                                        <th style="width: 35%">学科</th>
                                        <th style="width: 30%">每周课时数</th>
                                        <th style="width: 35%">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZB-subjects">
                                    </tbody>
                                    <script id="FZB-subjectsTmpl" type="text/template">
                                        {{ for(var i in it) { }}
                                        <tr subConfigId="{{=it[i].subjectConfId}}" subId="{{=it[i].subjectId}}">
                                            <td class="FZBK-col">{{=it[i].subjectName}}</td>
                                            <td class="adv">{{=it[i].advanceTime}}</td>
                                            <td>
                                                <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                            </td>
                                        </tr>
                                        {{ } }}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============学科管理end=====================-->
                        <!--==============老师设置start=====================-->
                        <div class="FZBK-I" id="tab-FZBK-I">
                            <div class="FZBK-sel">
                                <select id="subjectList">
                                    <option>全部学科</option>
                                    <option>语文</option>
                                </select>
                                <script id="subjectListTmpl" type="text/template">
                                    <option value="">全部学科</option>
                                    {{ for(var i in it) { }}
                                    <option value="{{=it[i].subjectId}}">{{=it[i].subjectName}}</option>
                                    {{ } }}
                                </script>
                                <%--<span class="h-cursor">确认</span>--%>
                            </div>
                            <div class="FZBK-daoru">
                                <span class="FZBK-SZ h-cursor" id="FZBKSZ">一键设置</span>
                                <%--<span class="FZBK-XX h-cursor">导入信息</span>--%>
                            </div>
                            <div class="FZBK-info">
                                <table>
                                    <thead>
                                    <tr>
                                        <th width="125">行政班</th>
                                        <th width="120">人数</th>
                                        <th width="130">学科</th>
                                        <th width="125">每周课时</th>
                                        <th width="125">任课老师</th>
                                        <th width="130">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZBCourses">
                                    </tbody>
                                    <script id="FZBCoursesTmpl" type="text/template">
                                        {{~it:value:index}}
                                        <tr>
                                            <td class="FZBK-col className" width="125">{{=value.className}}</td>
                                            <td width="120">{{=value.count}}</td>
                                            <td width="130" class="courseName">{{=value.courseName}}</td>
                                            <td width="125" class="weekTime">{{=value.lessonCount}}</td>
                                            <td width="125">{{=value.teacherName}}</td>
                                            <td width="130">
                                                <em class="h-cursor edit" subId="{{=value.subjectId}}"
                                                    course="{{=value.zbCourseId}}"
                                                    tid="{{=value.teacherId}}">编辑</em>|<em class="h-cursor del" course="{{=value.zbCourseId}}">删除</em>
                                            </td>
                                        </tr>
                                        {{~ }}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============老师设置end=====================-->
                    </div>
                </div>
                <!--================非走班课end=================-->

                <!--================(男女分班)体育课start=================-->
                <div id="tab-TYK" class="tab-TYK">
                    <div class="TYK-main">
                        <span class="TYK-XZ h-cursor">+新增班级</span>
                    </div>
                    <div class="TYK-info">
                        <table>
                            <thead>
                            <tr>
                                <th width="200">行政班级</th>
                                <th width="110">教学班</th>
                                <th width="110">人数</th>
                                <th width="110">老师</th>
                                <th width="110">每周课时</th>
                                <th width="130">操作</th>
                            </tr>
                            </thead>
                            <tbody class="PECourses">
                            <tr>
                                <td width="125" class="TYK-col" rowspan="2"></td>
                                <td width="175"></td>
                                <td width="110"></td>
                                <td width="110"></td>
                                <td width="110" rowspan="2"></td>
                                <td width="130" rowspan="2">
                                    <em class="h-cursor">编辑</em>|<em class="h-cursor">删除</em>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            </tbody>
                            <script id="PECoursesTmpl" type="text/template">
                                {{ for(var i in it) { }}
                                <tr mClassId="{{=it[i].mClassId}}" fClassId="{{=it[i].fClassId}}"
                                    courseName="{{=it[i].groupClassName}}"
                                    MTeacherId="{{=it[i].mTeacherId}}" FTeacherId="{{=it[i].fTeacherId}}"
                                    adminClassId="{{=it[i].adminClassId}}">
                                    <td width="125" class="TYK-col" rowspan="2">{{=it[i].adminClassName}}</td>
                                    <td width="175">{{=it[i].mClassName}}</td>
                                    <td width="110">{{=it[i].mCountStr}}</td>
                                    <td width="110">{{=it[i].mTeacherName}}</td>
                                    <td width="110" rowspan="2" class="lessonCount">{{=it[i].lessonCount}}</td>
                                    <td width="130" rowspan="2">
                                        <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                    </td>
                                </tr>
                                <tr>
                                    <td>{{=it[i].fClassName}}</td>
                                    <td>{{=it[i].fCountStr}}</td>
                                    <td>{{=it[i].fTeacherName}}</td>
                                </tr>
                                {{ } }}
                            </script>
                        </table>
                    </div>
                </div>
                <!--================(男女分班)体育课end=================-->
                <!--================分组走班课start=================-->
                <div class="zb-group" id="tab-FZZB" style="display: none;">
                    <div class="zouban-addbtn-II">
                        <ul class="clearfix">
                            <li class="subject-manage-II v-active">①学科管理</li>
                            <li class="teacher-set-II">②老师设置</li>
                        </ul>
                    </div>
                    <div class="fzbk">
                        <!--==============学科管理start=====================-->

                        <div class="FZBK-II" id="tab-FZZB-I">
                            <div class="FZBK-II-top">
                                <span class="FZZB-II-XZ h-cursor">+新增学科</span>
                            </div>
                            <div class="FZBK-info" id="tab-XKGl-1">
                                <table style="width: 99%">
                                    <thead>
                                    <tr>
                                        <th style="width: 15%">学科</th>
                                        <th style="width: 15%">课时数</th>
                                        <th style="width: 40%">行政班</th>
                                        <th style="width: 15%">分班数</th>
                                        <th style="width: 15%">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZZB-SUBJECT">
                                    </tbody>
                                    <script id="FZZB-subjectsTmpl" type="text/template">
                                        {{ for(var i in it) { }}
                                        <tr subConfigId="{{=it[i].subjectConfId}}" subId="{{=it[i].subjectId}}"
                                            lessonCount="{{=it[i].advanceTime}}" classIds="{{=it[i].classList}}"
                                            classNum="{{=it[i].classNumber}}">
                                            <td style="background:#ececec;">{{=it[i].subjectName}}</td>
                                            <td>{{=it[i].advanceTime}}</td>
                                            <td>{{=it[i].classGroupName}}</td>
                                            <td>{{=it[i].classNumber}}</td>
                                            <td>
                                            <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                            </td>
                                        </tr>
                                        {{ } }}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============学科管理end=====================-->
                        <!--==============老师设置start=====================-->
                        <div class="FZBK-I" id="tab-FZZB-II">
                            <div class="FZBK-sel">
                                <select id="FZZBsubjectList">

                                </select>
                                <%--<span class="h-cursor">确认</span>--%>
                                <script id="FZZBsubjectListTmpl" type="text/template">
                                    <option value="All">全部学科</option>
                                    {{ for(var i in it) { }}
                                    <option value="{{=it[i].subjectId}}">{{=it[i].subjectName}}</option>
                                    {{ } }}
                                </script>
                            </div>
                            <div class="FZBK-daoru">
                                <span class="FZBK-SZ h-cursor" id="FZZBSZl">一键设置</span>
                                <%--<span class="FZBK-XX h-cursor">导入信息</span>--%>
                            </div>
                            <div class="FZBK-info" style="margin-top: 10px;">
                                <table style="width: 750px;">
                                    <thead>
                                    <tr>
                                        <th width="18%">教学班</th>
                                        <th width="12%">课时数</th>
                                        <th width="12%">任课老师</th>
                                        <th width="12%">教室</th>
                                        <th width="12%">学生人数</th>
                                        <th width="20%">操作</th>
                                        <th width="14%">组合操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZZBTeacherList">
                                    </tbody>
                                    <script id="FZZBTeacherListTmpl" type="text/template">
                                        {{ for(var i in it) { }}
                                        <tr zbCourseId="{{=it[i].zbCourseId}}" subId="{{=it[i].subjectId}}"
                                                group="{{=it[i].groupTypeId}}">
                                            <td style="background:#ececec;">{{=it[i].className}}</td>
                                            <td>{{=it[i].lessonCount}}</td>
                                            {{?it[i].teacherName!=null}}
                                            <td>{{=it[i].teacherName}}</td>
                                            {{??}}
                                            <td></td>
                                            {{?}}
                                            {{?it[i].classRoom!=null}}
                                            <td>{{=it[i].classRoom}}</td>
                                            {{??}}
                                            <td></td>
                                            {{?}}
                                            <td>{{=it[i].studentsCount}}</td>
                                            <td>
                                                <em class="h-cursor stu-allot">分配学生</em> |
                                                <em class="h-cursor del-fzzb" courseId="{{=it[i].zbCourseId}}">删除</em>
                                            </td>
                                            {{?i==0||it[i].groupTypeId!=it[i-1].groupTypeId}}
                                            <td rowspan="{{=it[i].classNumber}}">
                                                <em class="h-cursor edit">编辑</em>
                                            </td>
                                            {{?}}
                                        </tr>
                                        {{ } }}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============老师设置end=====================-->
                    </div>
                </div>
                <!--================分组走班课end=================-->
                <!--================单双周课start=================-->
                <div class="dszk-div" id="tab-DSZ" style="display: none;">
                    <div class="zouban-addbtn-I">
                        <ul class="clearfix">
                            <li class="subject-manage-I w-active">①学科管理</li>
                            <li class="teacher-set-I">②老师设置</li>
                        </ul>
                    </div>
                    <div class="fzbk">
                        <!--==============学科管理start=====================-->

                        <div class="FZBK-II" id="tab-FZBK-1">
                            <div class="FZBK-II-top">
                                <span class="DSZ-II-XZ h-cursor">+新增学科</span>
                            </div>
                            <div class="FZBK-info" id="tab-XKGl-I">
                                <table style="width: 99%">
                                    <thead>
                                    <tr>
                                        <th style="width: 50%">学科</th>
                                        <th style="width: 50%">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="DSZK-SUBJECT">
                                    </tbody>
                                    <script id="DSZK-subjectsTmpl" type="text/template">
                                        {{ for(var i in it) { }}
                                        <tr oddEvenId="{{=it[i].oddEvenId}}" subId="{{=it[i].subjectId}}">
                                            <td style="background:#ececec;">{{=it[i].subjectName}}</td>
                                            <td>
                                                <em class="h-cursor del">删除</em>
                                            </td>
                                        </tr>
                                        {{ } }}
                                    </script>

                                </table>
                            </div>
                        </div>
                        <!--==============学科管理end=====================-->
                        <!--==============老师设置start=====================-->
                        <div class="FZBK-I" id="tab-FZBK-2">
                            <div class="FZBK-daoru">
                                <span class="FZBK-SZ h-cursor" id="DSZKSZ">一键设置</span>
                                <%--<span class="FZBK-XX h-cursor">导入信息</span>--%>
                            </div>
                            <div class="FZBK-info" style="margin-top: 50px;">
                                <table style="width: 750px;">
                                    <thead>
                                    <tr>
                                        <th width="25%">行政班</th>
                                        <th width="25%">学科</th>
                                        <th width="25%">老师</th>
                                        <th width="25%">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="zoubanOddEven">
                                    </tbody>
                                    <script id="zoubanOddEvenTmpl" type="text/template">
                                        {{ for(var i in it) { }}
                                        <tr zoubanEvenId="{{=it[i].zoubanOddEvenId}}" subId="{{=it[i].subjectId}}"
                                            teaId="{{=it[i].teacherId}}">
                                            <td style="background: #ececec;">{{=it[i].className}}</td>
                                            <td>{{=it[i].subjectName}}</td>
                                            {{?it[i].teacherName!=null}}
                                            <td>{{=it[i].teacherName}}</td>
                                            {{??}}
                                            <td></td>
                                            {{?}}
                                            <td>
                                                <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                            </td>
                                        </tr>
                                        {{ } }}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============老师设置end=====================-->
                    </div>
                </div>
                <!--================单双周课end=================-->
            </div>

        </div>
    </div>
    <!--/.tab-col右侧-->

</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<div class="bg"></div>
<!--=================非走班老师设置弹窗框start===================-->
<div class="popup-SZ">
    <div class="popup-SZ-top">
        <em>设置</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-SZ-main">
        <dl>
            <dd>
                <h2>高二一班<label>语文</label></h2>
            </dd>
            <dd>
                <em>任课老师</em>
                <select id="teachers">
                </select>
                <script id="teachersTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>每周课时</em>
                <select id="weekTime">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
        </dl>
    </div>
    <p style="margin-top: 10px;text-align: center;color: red;">PS：修改课时将清空已排课程！！</p>
    <div class="popup-SZ-bottom" style="padding-bottom: 30px;">
        <span class="SZ-TJ h-cursor" id="SZTJ">确定</span>
        <span class="SZ-QX h-cursor">取消</span>
    </div>
</div>
<!--=================非走班老师设置弹窗框end===================-->
<!--=================单双周老师设置弹窗框start===================-->
<div class="DSZ-SZ">
    <div class="popup-SZ-top">
        <em>编辑老师</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-SZ-main">
        <dl>
            <dd>
                <em>单周老师</em>
                <select id="DANZHOU-LIST">
                    <option></option>
                </select>
                <br/>
                <br/>
                <em>双周老师</em>
                <select id="SHUANZHOU-LIST">
                    <option></option>
                </select>
                <script id="DANZHOUTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                    {{ } }}
                </script>
                <script id="SHUANZHOUTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                    {{ } }}
                </script>
            </dd>
        </dl>
    </div>
    <div class="popup-SZ-bottom" style="padding-bottom: 30px;">
        <span class="SZ-TJ h-cursor" id="DSZKTJ">确定</span>
        <span class="SZ-QX h-cursor">取消</span>
    </div>
</div>
<!--=================单双周老师设置弹窗框end===================-->
<!--================分组走班学科管理编辑弹窗框start===================-->
<div class="FZZB-edit popup-addFZZB" style="height:auto;">
    <div class="popup-XKGL-top">
        <em>学科编辑</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <dd>
                <em>学科</em>
                <select class="subjects" id="fzzbSubject">
                    <%--<option>劳技</option>--%>
                </select>
            </dd>
            <dd>
                <em>课时数</em>
                <select class="lessonNo-DJ" id="lessonNo">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
            <dd>
                <em>行政班</em>
                <div class="input-wrap classesWrap" id="classesWrap">
                </div>
                <script id="classesWrapTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <label><input type="checkbox" value="{{=it[i].id}}" name="classesWrap">{{=it[i].className}}</label>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>分班数</em>
                <select class="lessonNo-DJ" id="fenban">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
        </dl>
    </div>
    <div class="popup-XKGL-bottom" style="padding-bottom: 30px;">
        <p id="fzzbTips1" style="display: block;margin-top: 10px;text-align: center;color: red;">PS:减少课时将清除已有配置</p>
        <span class="XKGL-TJ h-cursor" id="addFzzb">确定</span>
        <span class="XKGL-QX h-cursor" >取消</span>
    </div>
</div>
<!--=================分组走班学科管理编辑弹窗框end===================-->
<!--=================分组走班老师设置编辑弹窗框start===================-->
<div class="FZZB-SZ" style="height: auto;">
    <div class="popup-SZ-top">
        <em>教学班老师编辑</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-SZ-main">
        <script id="FZZBSZTmpl" type="text/template">
            {{ for(var i in it) { }}
            <dd >
                <label><input class="FzzbCourseId" value="{{=it[i].zoubanCourseId}}" style="width: 100px" type="hidden"></label>
                <label>名称<input class="FzzbClassName" value="{{=it[i].className}}" style="width: 100px"></label>
                <label>老师
                <select class="tte" id="{{=it[i].teId}}">
                    <option>张三丰</option>
                </select>
                </label>
                <label>教室
                <select class="ttt" id="{{=it[i].crId}}">
                    <option>张三丰</option>
                </select>
                </label>
            </dd>
            {{ } }}
        </script>
        <script id="tteTmpl" type="text/template">
            {{ for(var i in it) { }}
            <option value="{{=it[i].id}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <script id="tttTmpl" type="text/template">
            {{ for(var i in it) { }}
            <option value="{{=it[i].id}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <dl class="w-dl" id="FZZBSZ">

        </dl>
    </div>
    <div class="popup-SZ-bottom" style="padding-bottom: 30px;margin-top: 30px;">
        <p id="fzzbTips2" style="display: block;margin-top: 10px;text-align: center;color: red;">PS:修改教室或者老师会清除已有配置</p>
        <span class="SZ-TJ h-cursor" id="SZCONFIRM">确定</span>
        <span class="SZ-QX h-cursor">取消</span>
    </div>
</div>
<!--=================分组走班老师设置编辑弹窗框end===================-->
<!--=================分组走班老师设置调整学生弹窗框start===================-->
<div class="allot-alert popup-FZZB" style="height: auto;">
    <div class="popup-SZ-top">
        <em>分配学生</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="alert-main">
        <div class="allot-title">
            <script id="AllocClassInfoTmpl" type="text/template">
                {{ for(var i in it) { }}
                <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                {{ } }}
            </script>
            <select id="AllocClassInfo">
                <%--<option>1班</option>--%>
            </select>
            <span>选定学生</span>
        </div>
        <div class="allot-main clearfix">
            <div class="allot-left">
                <ul class="left-ul" id="ClassStudentInfo">

                </ul>
                <script id="ClassStudentInfoTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <li class="classInfo" stuId="{{=it[i].id}}"><span>{{=it[i].name}}</span><em>√</em></li>
                    {{ } }}
                </script>
            </div>
            <div class="allot-mid">
                <img src="../static_new/images/allot-left.png" style="margin:120px 0 40px 15px;">
                <img src="../static_new/images/allot-right.png" style="margin-left: 15px;">
            </div>
            <div class="allot-right">
                <ul class="right-ul" id="TeachStudentInfo">
                </ul>
                <script id="TeachStudentInfoTmpl" type="text/template">
                {{ for(var i in it) { }}
                <li class="teachInfo" stuId="{{=it[i].id}}"><span>{{=it[i].name}}</span><em>√</em></li>
                {{ } }}
                </script>
            </div>
        </div>
    </div>
    <div class="popup-SZ-bottom" style="padding-bottom: 30px;margin-top: 30px;">
        <span class="SZ-TJ h-cursor" id="fenpei">确定</span>
        <span class="SZ-QX h-cursor">取消</span>
    </div>
</div>
<!--=================分组走班老师设置调整学生弹窗框end===================-->
<!--=================体育课设置弹窗框start===================-->
<div class="popup-TYK">
    <div class="popup-TYK-top">
        <em>体育课设置</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-TYK-main">
        <dl>
            <dd>
                <em>体育课名称</em><input id="courseName">
            </dd>
            <dd>
                <em>关联行政班级</em>

                <div class="classes">
                </div>
                <script id="classTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <span>
              <input class="popup-TYK-IN" type="checkbox" value="{{=it[i].id}}"><label>{{=it[i].className}}</label>
          </span>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>男生班老师</em>

                <div id="MPETeachers"></div>
                <script id="MPETeachersTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <span>
              <input class="popup-TYK-IN" type="radio" name="teacherMName"
                     value="{{=it[i].id}}"><label>{{=it[i].name}}</label>
          </span>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>女生班老师</em>

                <div id="FPETeachers">
                </div>
                <script id="FPETeachersTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <span>
                        <input class="popup-TYK-IN" type="radio" name="teacherFName" value="{{=it[i].id}}">
                        <label>{{=it[i].name}}</label>
                    </span>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>每周课时数</em>
                <select id="lessonCount">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
        </dl>
    </div>
    <div class="popup-TYK-bottom">
        <span class="TYK-TJ h-cursor">确定</span>
        <span class="TYK-QX h-cursor">取消</span>
    </div>
</div>
<!--=================体育课设置弹窗框end===================-->

<!--=================非走班弹窗框start===================-->
<div class="popup-XKGL">
    <div class="popup-XKGL-top">
        <em>学科管理</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <div>${term}<em>${gradeName}</em></div>
            <dd>
                <em>学科</em>
                <select class="subjects">
                </select>
            </dd>
            <dd>
                <em>每周课时数</em>
                <select class="lessonNo-DJ">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
        </dl>
    </div>
    <p id="fzbTips" style="display: none;margin-top: 10px;text-align: center;color: red;">PS：修改课时将清空已排课程！！</p>
    <div class="popup-XKGL-bottom">
        <span class="XKGL-TJ h-cursor" id="addFzb">确定</span>
        <span class="XKGL-QX h-cursor">取消</span>
    </div>
</div>
<!--=================非走班弹窗框end===================-->
<!--=================单双周课新增学科弹窗框start===================-->
<div class="DSZ-XKGL" style="height: 280px;">
    <div class="popup-XKGL-top">
        <em>新增单双周课</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <dd>
                <em>单周学科</em>
                <select class="subjects" id="danzhou">
                    <%--<option>劳技</option>--%>
                </select>
            </dd>
            <dd>
                <em>双周学科</em>
                <select class="subjects" id="shuanzhou">
                    <%--<option>劳技</option>--%>
                </select>
            </dd>
        </dl>
    </div>
    <div class="popup-XKGL-bottom">
        <span class="XKGL-TJ h-cursor" id="addDSZK">确定</span>
        <span class="XKGL-QX h-cursor">取消</span>
    </div>
</div>
<!--=================单双周课新增学科弹窗框end===================-->
<!--=================走班弹窗框start===================-->
<div class="popup-XKGL-ZB" style="height: auto;">
    <div class="popup-XKGL-top">
        <em>学科管理</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <div>${term}<em>${gradeName}</em></div>
            <dd>
                <em>学科</em>
                <select class="subjects">
                    <option>语文</option>
                </select>
                <script id="subjectTmpl" type="text/template">
                    {{ for(var i in it) { }}
                    <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                    {{ } }}
                </script>
            </dd>
            <dd>
                <em>等级考课时数</em>
                <select class="lessonNo-DJ">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
            <dd>
                <em>合格考课时数</em>
                <select class="lessonNo-HG">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </dd>
        </dl>
    </div>
    <p id="zbTips" style="display: none;margin-top: 10px;text-align: center;color: red;">PS:修改课时将清空已排课程</p>
    <div class="popup-XKGL-bottom" style="padding-bottom:30px;">
        <span class="XKGL-ZB-TJ h-cursor">确定</span>
        <span class="XKGL-QX h-cursor">取消</span>
    </div>
</div>
<!--=================走班弹窗框end===================-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    var base = {
        term: '${term}',
        gradeId: '${gradeId}',
        gradeName: '${gradeName}',
        mode: ${mode},
        xuankeId: '${xuankeId}'
    }
    seajs.use('/static_new/js/modules/zouban/2.0/subjectConfig.js');
</script>

</body>
</html>
