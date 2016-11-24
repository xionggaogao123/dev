<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/10/16
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>走班选课</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/zouban/zouban.css?v=2015041602" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/zouban/dypeizhi.css">
    <link href="/static_new/css/zouban/expand.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static_new/js/modules/calendar/0.1.0/WdatePicker.js"></script>

</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>

    <div class="col-right">
        <input hidden="hidden" id="xuankeid">
        <input type="hidden" id="mode" value="${mode}">

        <div class="tab-col">
            <div class="tab-top clearfix">
                <ul>
                    <c:choose>
                        <c:when test="${mode=='index'}"><%--晋元--%>
                            <li class="cur tab-zouban" index="1"><a href="javascript:;">走班课</a></li>
                            <li class="tab-Nzouban" index="2"><a href="javascript:;">非走班课</a></li>
                            <li class="tab-Xzouban" index="3"><a href="javascript:;">小走班课</a></li>
                            <li class="tab-Ozouban" index="5"><a href="javascript:;">其他课</a></li>
                        </c:when>
                        <c:when test="${mode=='zhuZhouIndex'}"><%--株洲--%>
                            <li class="cur tab-zouban" index="1"><a href="javascript:;">基础设置</a></li>
                            <li class="tab-Nzouban" index="2"><a href="javascript:;">非走班课</a></li>
                            <li class="tab-Pzouban" index="6"><a href="javascript:;">选修课</a></li>
                        </c:when>
                        <c:otherwise><%--格致&长征--%>
                            <li class="cur tab-zouban" index="1"><a href="javascript:;">走班课</a></li>
                            <li class="tab-Nzouban" index="2"><a href="javascript:;">非走班课</a></li>
                            <li class="cur-li" index="4"><a href="javascript:;">体育课</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
                <a class="backUrl tab-back" href="javascript:;">&lt;返回教务管理首页</a>
            </div>
            <!--====================走班课程设置 start=====================-->
            <div class="tab-curriculum">
                <dl>
                    <dd>
                        <span id="team" style="padding-left:38px;">${term}</span>
                        <em></em>
                        <span id="gradeShow" gid="${gradeId}">${gradeName}</span>
                    </dd>
                    <dd>
                        <em>开放时间</em><i>
                        <span id="startdt">2015-11-11</span>
                        <span>～</span>
                        <span id="enddt">2016-11-11</span>
                    </i>
                        <em>状态</em><i id="state"></i>
                    </dd>
                    <dd style="margin-left: 350px;">
                        <button class="shezhi-CUR-CS">设置选课时间</button>
                        <c:choose>
                            <c:when test="${mode!='zhuZhouIndex'}">
                                <button class="update-CUR-TJ">添加课程</button>
                            </c:when>
                        </c:choose>
                        <button class="BON GS" id="xkgs">公示</button>
                        <button class="QXGS" id="xkqxgs">取消公示</button>
                        <button class="BON GB" id="xkgb">公布</button>
                        <button class="QXGB" id="xkqxgb">取消公布</button>
                    </dd>
                    <c:choose>
                    <c:when test="${mode!='zhuZhouIndex'}">
                    <dd>
                        </c:when>
                        <c:otherwise>
                    <dd style="display: none">
                        </c:otherwise>
                        </c:choose>
                        <table id="subjectlist">
                        </table>
                        <script type="application/template" id="subjectConfTempJs">
                            <tr>
                                <th>科目</th>
                                <th>合格考每周课时</th>
                                <th>等级考每周课时</th>
                                <th>是否分层教学</th>
                                <th>说明</th>
                                <th>操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.subjectName}}</td>
                                <td>{{=value.simpleTime}}</td>
                                <td>{{=value.advanceTime}}</td>
                                <td>{{=value.fenceng}}</td>
                                <td>{{=value.explain}}</td>
                                <td>
                                    <span class="curri-update sub_update"
                                          scid="{{=value.subjectConfId}}">编辑&nbsp;|</span>
                                    <span class="curri-update sub_delete" scid="{{=value.subjectConfId}}">删除</span>
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>

                </dl>
            </div>
            <!--====================走班课程设置 end=====================-->
            <!--====================非走班课程设置 start==================-->
            <div class="tab-Ncurriculum">
                <dl>
                    <dd>
                        <em></em>
                        <span id="team">${term}</span>
                        <em></em>
                        <span id="gradeShow" gid="${gradeId}">${gradeName}</span>
                        <button class="tab-Ncurriculum-update">添加课程</button>
                    </dd>

                    <dd>
                        <table id="subjectlist2">
                        </table>
                        <script type="application/template" id="subjectConfTempJs2">
                            <tr>
                                <th class="th-subj">科目</th>
                                <th class="th-clat">每周课时</th>
                                <th class="th-set">操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td class="tab-Ncurriculum-I">{{=value.subjectName}}</td>
                                <td class="tab-Ncurriculum-II">{{=value.advanceTime}}</td>
                                <td class="tab-Ncurriculum-III">
                                    <em class="noupdate" scid="{{=value.subjectConfId}}">编辑</em>|
                                    <em class="nodelete" scid="{{=value.subjectConfId}}">删除</em>
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>
                </dl>
            </div>
            <!--====================非走班课程设置 end==================-->

            <div class="tab-interestClass">
                <dl>
                    <dd>
                        <em></em>
                        <span id="team1">${term}</span>
                        <em></em>
                        <span id="gradeShow1" gid="${gradeId}">${gradeName}</span>
                        <button class="tab-interest-update" style="float: right;margin-right: 10px;">添加课程</button>
                        <button class="tab-interest-setGroup" style="float: right">设置单元</button>

                    </dd>

                    <dd>
                        <table id="interestCourseShow"></table>
                        <script type="application/template" id="interestClassJs">
                            <tr>
                                <th>课程名</th>
                                <th>学科</th>
                                <th>任课老师</th>
                                <th>上课地点</th>
                                <th>面向对象</th>
                                <th>上课时间</th>
                                <th>人数上限</th>
                                <th>操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.courseName}}</td>
                                <td>{{=value.subjectName}}</td>
                                <td>{{=value.teacherName}}</td>
                                <td>{{=value.classRoom}}</td>
                                <td>{{?value.groupName==""}}
                                    全年级
                                    {{??}}
                                    {{=value.groupName}}
                                    {{?}}
                                </td>
                                <td>{{~value.pointEntrylist:v:i}}
                                    星期{{=v.x}}第{{=v.y}}节
                                    {{~}}
                                </td>
                                <td>{{=value.max}}</td>
                                <td>
                                    <em class="editInterest" cid="{{=value.zbCourseId}}">编辑</em>|
                                    <em class="deleteInterest" cid="{{=value.zbCourseId}}">删除</em>
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>
                </dl>
            </div>
            <div class="dypeizhi_mian clearfix">
                <a href="#" class="dypeizhi_back">< 返回</a>
                <button class="dypeizhi_add">添加</button>
                <button class="dypeizhi_edit">编辑</button>

                <table>
                    <thead>
                    <tr>
                        <th>单元序号</th>
                        <th>单元名称</th>
                        <th>关联班级</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="duanTableShow">

                    </tbody>
                </table>
                <script type="application/template" id="duanTableJs">
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=index+1}}</td>
                        <td>{{=value.fenDuanName}}</td>
                        <td>{{~value.classNames:v:i}}
                            {{=v}}、
                            {{~}}
                        </td>
                        <td><a class="deleteDuan" did="{{=value.fenDuanId}}">删除</a></td>
                    </tr>
                    {{~}}
                </script>
            </div>


            <!--===================体育课start=========================-->
            <div class="sports-S">
                <div class="sports-top">
                    <em id="term">${term}</em>
                    <em id="PEGrade" gid="${gradeId}">${gradeName}</em>
                    <span id="addPE">添加课程</span>
                </div>
                <div class="sports-top">
                    <em>体育课每周课时</em>
                    <select id="lessonCount">
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3" selected>3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                </div>
                <div class="sports-D">
                    <table>
                        <thead>
                        <tr>
                            <th width="220">行政班级</th>
                            <th width="110">教学班</th>
                            <th width="100">人数</th>
                            <th width="100">任课老师</th>
                            <th width="100">每周课时</th>
                            <th width="100">操作</th>
                        </tr>
                        </thead>
                        <tbody id="PECourseListCtx"></tbody>
                    </table>
                    <script id="PECourseListTmpl" type="application/template">
                        {{~it:value:index}}
                        <tr mClassId="{{=value.mClassId}}" fClassId="{{=value.fClassId}}"
                            mTeacherId="{{=value.mTeacherId}}" fTeacherId="{{=value.fTeacherId}}"
                            adminClassId="{{=value.adminClassId}}" groupClassName="{{=value.groupClassName}}">
                            <td rowspan="2">{{=value.adminClassName}}</td>
                            <td>{{=value.mClassName}}</td>
                            <td>{{=value.mCountStr}}</td>
                            <td>{{=value.mTeacherName}}</td>
                            <td rowspan="2">{{=value.lessonCount}}</td>
                            <td rowspan="2">
                                <em class="sports-BJ">编辑</em>|
                                <em class="delete">删除</em>
                            </td>
                        </tr>
                        <tr>
                            <td>{{=value.fClassName}}</td>
                            <td>{{=value.fCountStr}}</td>
                            <td>{{=value.fTeacherName}}</td>
                        </tr>
                        {{~}}
                    </script>
                </div>
            </div>
            <!--===================体育课end=========================-->
        </div>
    </div>
    <!--/.col-right-->
</div>
<!--/#content-->
<!--=======================================弹出层背景 start========================================-->
<div class="bg"></div>
<!--=======================================弹出层背景 end========================================-->


<!--/添加编辑课程弹出框 start-->
<div class="update-CUR">
    <div class="update-CUR-top">
        <em>添加/编辑课程</em>
        <i class="update-X">X</i>
    </div>
    <dl>
        <dt>
            <span id="term1">${term}</span>
            <span>/</span>
            <span id="grade1">${gradeName}</span>
        </dt>
        <dd>
            <em>科目名称</em>
            <select id="subjects">
            </select>
            <script type="application/template" id="subjectTempJs">
                {{~it.data:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <div>
                <em>考试方式</em>
                <input type="checkbox" class="curri-CH" id="sipchk">
                <i>合格考</i><br>
            </div>
            <div>
                <em></em>
                <i>合格考每周课时</i>
                <select id="sipinput" disabled>
                    <option value="0" selected>0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </div>
            <div>
                <em></em>
                <input type="checkbox" class="curri-CH" id="advchk">
                <i>等级考</i>
            </div>
            <div>
                <em></em>
                <i>等级考每周课时</i>
                <select id="advinput" disabled>
                    <option value="0" selected>0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </div>
            <div>
                <em></em>
                <i>是否分层教学</i>
                <input type="radio" class="curri-RA" name="fenceng" value="1" id="fenceng1">
                <i>分层教学</i>
                <input type="radio" class="curri-RA" name="fenceng" value="0" id="fenceng2" checked>
                <i>不分层教学</i>
            </div>
        </dd>
        <dd>
            <em>说明</em>
            <textarea id="explain">

            </textarea>
        </dd>
        <dd>
        <span>
            <button class="curri-TJ_ONE">提交</button>
            <button class="curri-QX">取消</button>
        </span>
        </dd>
    </dl>
</div>
<!--/添加编辑课程 end-->
<!--设置参数 start-->
<div class="shezhi-CUR">
    <div class="update-CUR-top">
        <em>设置参数</em>
        <i class="update-X">X</i>
    </div>
    <dl>
        <dt>选课开放时间</dt>
        <dd>
            <em>开放时间</em>
            <input class="Wdate" type="text" style="width:171px;height:32px;" id="opentime"
                   onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                   value=""/>
        </dd>
        <dd>
            <em>结束时间</em>
            <input class="Wdate" type="text" style="width:171px;height:32px;" id="endtime"
                   onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                   value=""/>
        </dd>
        <dd>
            <span>
                <button class="curri-TJ_ZERO">提交</button>
                <button class="curri-QX">取消</button>
            </span>
        </dd>
    </dl>
</div>
<!--设置参数 end-->
<!--确认公示 start-->
<div class="gongshi-CUR">
    <div class="gongshi-CUR-top">
        <em>确认公示</em>
        <i class="update-X">X</i>
    </div>
    <div class="gongshi-CUR-info">
        公示后，课程设置和相关参数将被锁定并且不可修改，全校老师将看到走班课程。
    </div>
    <div class="gongshi-CUR-buttom">
        <button class="curri-TJ gongshi_TJ">确定</button>
        <button class="curri-QX">取消</button>
    </div>
</div>
<!--确认公示 end-->
<!--确认发布 start-->
<div class="fabu-CUR">
    <div class="fabu-CUR-top">
        <em>确认发布</em>
        <i class="update-X">X</i>
    </div>
    <div class="fabu-CUR-info">
        发布后，课程设置将被锁定，不可修改，改年级学生将会看到走班选修课程和相关参数。
    </div>
    <div class="fabu-CUR-buttom">
        <button class="curri-TJ fabu_TJ">确定</button>
        <button class="curri-QX">取消</button>
    </div>
</div>
<!--确认发布 end-->
<!--提示 start-->
<div class="tishi-CUR">
    <div class="tishi-CUR-top">
        <em>提示</em>
        <i class="update-X">X</i>
    </div>
    <div class="tishi-CUR-info">
        走班课程还未公示，请先公示走班课程和相关参数。
    </div>
    <div class="tishi-CUR-buttom">
        <button class="curri-TJ ifgongsi">确定</button>
        <button class="curri-QX">取消</button>
    </div>
</div>
<!--提示 end-->
<!--非走班选课添加/编辑课程 start-->
<div class="Nupdate-CUR">
    <div class="Nupdate-CUR-top">
        <em>提示</em>
        <i class="update-X">X</i>
    </div>
    <dl>
        <dt>
            <em id="term2">${term}</em>/
            <em id="grade2">${gradeName}</em>
        </dt>
        <dd>
            <em>科目名称</em>
            <select id="subjects2">
            </select>
            <script type="application/template" id="subjectTempJs2">
                {{~it.data:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <em>每周课时</em>
            <select id="nuadvtm">
                <option value="0" selected>0</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
            </select>
        </dd>
        <dd>
            <span>
                <button class="curri-TJ_THREE">提交</button>
                <button class="curri-QX">取消</button>
            </span>
        </dd>
    </dl>
</div>
<!--非走班选课添加/编辑课程 end-->

<div class="dypeizhi_alert">
    <div class="dypeizhi_alert_title clearfix">
        <span>单元修改</span>
        <em class="dypeizhi_alert_close">X</em>
    </div>
    <div class="dypeizhi_alert_mian">
        <div id="editFenDuanshow">

        </div>
        <script type="application/template" id="editFenDuanJs">
            {{~it.data:value:index}}
            <div class="dypeizhi_alert_item">
                <span>单元名称：</span><input class="dypeizhi_alert_text editDuanName" did="{{=value.fenDuanId}}" type="text"
                                         value="{{=value.fenDuanName}}">

                <div class="dypeizhi_alert_box">
                    {{~it.class:v1:index1}}
                    <lable>
                        <input type="checkbox" did="{{=value.fenDuanId}}" class="editClassCheck" value="{{=v1.id}}"
                               {{~value.classIds:c:index2}}
                               {{?c==v1.id}}
                               checked
                               {{?}}
                               {{~}}
                                >{{=v1.className}}
                    </lable>
                    {{~}}
                </div>
            </div>
            {{~}}
        </script>
        <div class="dypeizhi_alert_foot">
            <button id="editSave">确定</button>
            <button id="editCancel">取消</button>
        </div>
    </div>
</div>
<div class="dypeizhi_alert2">
    <div class="dypeizhi_alert_title clearfix">
        <span>单元添加</span>
        <em class="dypeizhi_alert_close">X</em>
    </div>
    <div class="dypeizhi_alert_mian">
        <div class="dypeizhi_alert_item">
            <span>单元名称：</span><input class="dypeizhi_alert_text" id="inpName" type="text">

            <div class="dypeizhi_alert_box" id="classList"></div>
            <script type="application/template" id="classTempJs">
                {{~it.data:value:index}}
                <label class="label">
                    <input name="classDuanInp" type="checkbox" value="{{=value.id}}"
                           class="checkbox radioBtns">{{=value.className}}
                </label>
                {{~}}
            </script>
            <div class="dypeizhi_alert_foot">
                <button id="addSave">确定</button>
                <button id="addCancel">取消</button>
            </div>
        </div>
    </div>
</div>

<%--体育课设置--%>
<div class="sports-popup" mClassId="" fClassId="">
    <div class="sports-popup-top">
        <em>体育课设置</em>
        <i class="sports-II PEClose">X</i>
    </div>
    <div class="sports-popup-main">
        <div>${term}/${gradeName}</div>
        <dl>
            <dd>
                <em>班级名称</em><input type="text" id="courseName" style="border: solid 1px #aaa;" value="">
            </dd>
            <dd>
                <em>关联班级</em>

                <div id="classListCtx"></div>
                <script id="classListTmpl" type="application/template">
                    {{~it:value:index}}
                    <input name="classId" type="checkbox" class="popup-IN" value="{{=value.id}}">
                    <span>{{=value.className}}</span>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>男生班老师</em>
                <select id="teacherMCtx"></select>
                <script id="teacherTmpl" type="application/template">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>女生班老师</em>
                <select id="teacherFCtx"></select>
            </dd>
            <dd class="popup-buttom-s">
                <button class="popup-TT">提交</button>
                <button class="PEClose">取消</button>
            </dd>
        </dl>
    </div>
</div>

<!--==================新的弹出框==================-->
<%--<div class="zouban">
    <div class="zouban-top">
        <em>选修课</em><i class="zouban-dl">X</i>
    </div>
    <div class="zouban-info">
        <dl>
            <dt class="title-show">${term}/${gradeName}</dt>
            <dd>
                <em>课程名</em><input id="courseName">
            </dd>
            <dd>
                <em>所属学科</em>
                <select id="subjectListShow"></select>
            </dd>
            <dd>
                <em>任课老师</em>
                <select id="teacherShow">
                    <option></option>
                    <option></option>
                </select>
                <script type="application/template" id="teacherTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.teacherId}}">{{=value.teacherName}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>面向全年级</em>
                <label class="label"><input type="checkbox" class="checkbox" id="allClass">是</label>
            </dd>
            <dd class="clearfix" id="show-duan">
                <em style="float: left">面向单元</em>

                <div id="duanList">

                </div>
                <script type="application/template" id="duanTempJs">
                    {{~it.data:value:index}}
                    <label class="label"><input name="classInp" type="radio" value="{{=value.fenDuanId}}"
                                                class="checkbox radioBtns">{{=value.fenDuanName}}</label>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>上课教室</em>
                <select id="classroomShow">
                    <option></option>
                    <option></option>
                </select>
                <script type="application/template" id="classroomTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.id}}">{{=value.roomName}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>人数上限</em><input id="maxPeople" onkeyup="this.value=this.value.replace(/\D/g,'')"
                                    onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </dd>
            <dd>
                <em>上课时间</em>
                <select class="zouban-I">
                    <option value="1">星期一</option>
                    <option value="2">星期二</option>
                    <option value="3">星期三</option>
                    <option value="4">星期四</option>
                    <option value="5">星期五</option>
                    <option value="6">星期六</option>
                    <option value="7">星期日</option>
                </select>
                <select class="zouban-II">
                    <option value="1">第一节</option>
                    <option value="2">第二节</option>
                    <option value="3">第三节</option>
                    <option value="4">第四节</option>
                    <option value="5">第五节</option>
                    <option value="6">第六节</option>
                    <option value="7">第七节</option>
                    <option value="8">第八节</option>
                    <option value="9">第九节</option>
                    <option value="10">第十节</option>
                </select>
            </dd>
            <dd class="zouban-bottom">
                <span class="zouban-QD">确定</span><span class="zouban-QX">取消</span>
            </dd>
        </dl>
    </div>
</div>--%>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('lessoninstall');
</script>
</body>
</html>
