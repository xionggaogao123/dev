<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>编班规则设置</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/classrule.css" rel="stylesheet"/>
</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">
    <input hidden="hidden" id="xuankeId">

    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>

    <!--col-right-->
    <div class="col-right">
        <div id="main">
            <div class="right-main">
                <ul class="top-nav clearfix">
                    <li class="cur-li">编班规则设置</li>
                </ul>
                <a class="placement-a">&lt;返回教务管理首页</a>
            </div>
            <div class="placement-main">
                <!--===================编班规则设置start=========================-->

                <div class="placement-BB">
                    <div class="placement-BB-top">
                        <em id="term">${term}</em><em id="grade" gid="${gradeId}">${gradeName}</em>
                    </div>
                    <div class="placement-BB-li">
                        <ul>
                            <li id="FD" class="place-cur">1.设置分段</li>
                            <li id="FB">2.学生分班</li>
                            <li id="JS">3.老师和教室</li>
                        </ul>
                    </div>
                    <div class="content">
                        <%--===================分段设置start=======================--%>
                        <div class="placement-SZ" id="placement-FD">
                            <div>
                                <%--<em>未分段班级：<em id="nocnt">0/4</em></em>--%>
                                <button id="autoFenDuan" style="margin-left: 30px;">自动分段</button>
                            </div>
                            <div class="placement-TB">
                                <table>
                                    <thead>
                                    <tr>
                                        <th width="200px">段</th>
                                        <th width="200px">班级</th>
                                        <th width="200px">班级人数</th>
                                        <th width="100px">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="fenduanListCtx"></tbody>
                                </table>
                                <script id="fenduanListTmpl" type="application/template">
                                    {{~it:value:index}}
                                    <tr groupId="{{=value.groupId}}" classId="{{=value.classId}}">
                                        <td>第{{=value.group}}段</td>
                                        <td>{{=value.className}}</td>
                                        <td>{{=value.studentCount}}</td>
                                        <td>调整分段</td>
                                    </tr>
                                    {{~}}
                                </script>
                            </div>
                        </div>
                        <%--====================分段设置end============================--%>
                        <div id="classList" style="display: none">
                            <div class="main1-2">
                                <em>分段</em>
                                <select id="fenduanSelectCtx"></select>
                                <script type="application/template" id="fenduanSelectTmpl">
                                    <option value="-1">全部</option>
                                    {{~it:value:index}}
                                    <option value="{{=value.id}}">第{{=value.group}}段</option>
                                    {{~}}
                                </script>

                                <em>科目</em>
                                <select id="subjectSelectCtx"></select>
                                <script type="application/template" id="subjectListTmpl">
                                    <option value="-1">全部</option>
                                    {{~it:value:index}}
                                    <option value="{{=value.id}}">{{=value.name}}</option>
                                    {{~}}
                                </script>

                                <div class="main1-2btn" id="fenban">自动分班</div>
                            </div>
                            <table class="edit-tab">
                                <thead>
                                <tr class="row1 row">
                                    <th class="col4">分段</th>
                                    <th class="col3">学科</th>
                                    <th class="col2">教学班</th>
                                    <th class="col5">课时</th>
                                    <th class="col5">人数</th>
                                    <th class="col4">学生名单</th>
                                </tr>
                                </thead>
                                <tbody id="courseListCtx"></tbody>
                                <script type="application/template" id="courseListTmpl">
                                    {{~it:value:index}}
                                    <tr class="row2 row">
                                        <td>第{{=value.group}}段</td>
                                        <td>{{=value.subjectName}}</td>
                                        <td>{{=value.courseName}}</td>
                                        <td>{{=value.lessonCount}}</td>
                                        <td>{{=value.count}}</td>
                                        <td>
                                            <em class="tab-check" couId="{{=value.zbCourseId}}"
                                                courseName="{{=value.courseName}}" count="{{=value.count}}">查看</em>
                                        </td>
                                    </tr>
                                    {{~}}
                                </script>
                            </table>
                        </div>
                        <div class="bianbanTip">
                            <p>正在编排，请耐心等待。。。</p>
                        </div>
                        <!--===================学生分班end======================-->
                        <!--===================老师和教室start======================-->
                        <div class="placement-JS" id="placement-JS">
                            <div class="main1-2">
                                <em id="fenduan">分段</em>
                                <select id="fenduanSelectCtx2"></select>
                                <em id="subject">科目</em>
                                <select id="subjectSelectCtx2"></select>
                                <em>类型</em>
                                <select id="courseType" style="width: 110px;">
                                    <option value="1">走班</option>
                                    <option value="2">非走班</option>
                                </select>
                                <div class="main1-2btn" id="setTeaClsRoom" >一键分配老师和教室</div>
                                <div class="main1-2btn" id="fzbSetTeaClsRoom" style="display: none;">一键分配非走班老师</div>
                            </div>
                            <div class="placement-TB">
                                <table class="edit-tab" id="teaClsRmCtx">
                                </table>
                                <script type="application/template" id="teaClsRmTmpl">
                                    <thead>
                                    <tr class="row1 row">
                                        <th class="col2">分段</th>
                                        <th class="col2">教学班</th>
                                        <th class="col5">每周课时</th>
                                        <th class="col3">任课老师</th>
                                        <th class="col3">教室</th>
                                        <th class="col4">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody >
                                    {{~it:value:index}}
                                    <tr class="row2 row">
                                        <td>第{{=value.group}}段</td>
                                        <td>{{=value.courseName}}</td>
                                        <td>{{=value.lessonCount}}</td>
                                        <td>{{=value.teacherName}}</td>
                                        <td>{{=value.classRoom}}</td>
                                        <td>
                                            <em class="edit-set-tea" subId="{{=value.subjectId}}" snm="{{=value.subjectName}}"
                                                couId="{{=value.zbCourseId}}" cn="{{=value.courseName}}" gp="{{=value.group}}"
                                                tid="{{=value.teacherId}}" crid="{{=value.classRoomId}}">设置</em>
                                        </td>
                                    </tr>
                                    {{~}}
                                    </tbody>
                                </script>
                                <script type="application/template" id="feizoubanTeaClsRmTmpl">
                                    <thead>
                                    <tr class="row1">
                                        <td class="col2">行政班</td>
                                        <td class="col2">学科</td>
                                        <td class="col4">每周课时</td>
                                        <td class="col3">任课老师</td>
                                        <td class="col4">操作</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {{~it:value:index}}
                                    <tr>
                                        <td>{{=value.className}}</td>
                                        <td>{{=value.subjectName}}</td>
                                        <td>{{=value.lessonCount}}</td>
                                        <td>{{=value.teacherName}}</td>
                                        <td>
                                            <i class="edit-set-tea" subId="{{=value.subjectId}}" cid="{{=value.classId}}" crid="1"
                                               snm="{{=value.subjectName}}" tid="{{=value.teacherId}}" cn="{{=value.className}}"
                                               cnt="{{=value.lessonCount}}" couid="{{=value.zbCourseId}}">设置</i>
                                        </td>
                                    </tr>
                                    {{~}}
                                    </tbody>
                                </script>
                            </div>
                        </div>
                        <!--===================老师和教室end======================-->
                    </div>
                </div>
                <!--===================编班规则设置end=========================-->
            </div>
        </div>
        <%--查看班级学生--%>
        <div class="right-main2">
            <span class="back-btn">&lt; 返回</span>

            <div class="placement-BB-top">
                <em>${term}</em><em>${gradeName}</em>
            </div>
            <div class="main2-info">
                <i id="courseName"></i>
                <em>人数：</em><i id="stuCount"></i>
            </div>
            <table class="table-stu">
                <thead>
                <tr class="row1">
                    <th>学生姓名</th>
                    <th>性别</th>
                    <th>所属行政班</th>
                </tr>
                </thead>
                <tbody id="studentListCtx"></tbody>
            </table>
            <script type="application/template" id="studentListTmpl">
                {{~it:value:index}}
                <tr>
                    <td>{{=value.studentName}}</td>
                    <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                    <td>{{=value.className}}</td>
                </tr>
                {{~}}
            </script>
        </div>
    </div>
    <!--/.col-right-->

</div>

<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<div class="bg"></div>

<!--===================编班设置start====================-->
<div class="placement-popup">
    <div class="sports-popup-top">
        <em>老师设置</em>
        <i class="sports-II">X</i>
    </div>
    <div class="place-popup-main">
        <div>2015-2016学年/高二年级</div>
        <dl>
            <dd>
                <em>物理老师</em>

                <div>
                    <input name="" type="checkbox"><span>高二（1）班</span>
                    <input name="" type="checkbox"><span>高二（2）班</span>
                    <input name="" type="checkbox"><span>高二（3）班</span>
                    <input name="" type="checkbox"><span>高二（4）班</span>
                </div>
            </dd>
            <dd class="popup-buttom-s">
                <button class="popup-TT">提交</button>
                <button>取消</button>
            </dd>
        </dl>
    </div>
</div>
<!--===================编班设置end====================-->
<!--设置分段 自动分段 start-->
<div class="section-CUR">
    <div class="section-CUR-top">
        <em>自动分段</em>
        <i class="update-X closeFenDuan">X</i>
    </div>
    <dl>
        <dd>
            <em id="grade2"></em>
        </dd>
        <dd>
            <em>分段数目</em>
            <select class="se1" id="count">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
            </select>
        </dd>
        <%--<dd>
            <em>每段学科教学班数量</em>
            <select class="se2">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
                <option>6</option>
            </select>
        </dd>--%>

        <dd><em class="em-rule">分段规则</em>

            <div class="div-rule">
                <dl class="dl-rule">
                    <dd><input type="radio" name="fd-rule" checked="true" id="duanchoose1">分班时尽量使班级人数最优</dd>
                    <%--<dd>
                        <input type="radio" name="fd-rule" id="duanchoose2">
                        <em>根据班级成绩分段</em>
                    </dd>
                    <dd>
                        <select id="examlist" disabled="disabled">
                        </select>
                        <script type="application/template" id="examlistTempJs">
                            {{~it.data:value:index}}
                            <option value="{{=value.id}}">{{=value.name}}</option>
                            {{~}}
                        </script>
                    </dd>

                    <dd>
                        <a class="section-CK" href="/exam1/list.do?version=5d" id="findCJ" target="_blank">查看成绩情况</a>
                    </dd>--%>
                </dl>
            </div>
        </dd>
        <dd>
            <button class="section-CUR-QR">确认</button>
            <button class="section-CUR-QX closeFenDuan">取消</button>
        </dd>
    </dl>
</div>
<!--设置分段 自动分段 end-->
<!--===================学生分班start====================-->
<div class="placement-popup-FB">
    <div class="sports-popup-top">
        <em>分班设置</em>
        <i class="sports-II closeFenBan">X</i>
    </div>
    <div class="place-popup-main">
        <div>${term}/${gradeName}</div>
        <dl>
            <dd>
                <em>等级考班级人数上限</em>
                <select id="advMax">
                    <option value="35">35</option>
                    <option value="40">40</option>
                    <option value="45">45</option>
                    <option value="50">50</option>
                </select>
            </dd>
            <dd>
                <em>等级考班级人数下限</em>
                <select id="advMin">
                    <option value="15">15</option>
                    <option value="20">20</option>
                    <option value="25">25</option>
                    <option value="30">30</option>
                </select>
            </dd>
            <dd>
                <em>合格考班级人数上限</em>
                <select id="simMax">
                    <option value="35">35</option>
                    <option value="40">40</option>
                    <option value="45">45</option>
                    <option value="50">50</option>
                </select>
            </dd>
            <dd>
                <em>合格考班级人数上限</em>
                <select id="simMin">
                    <option value="15">15</option>
                    <option value="20">20</option>
                    <option value="25">25</option>
                    <option value="30">30</option>
                </select>
            </dd>
            <dd class="popup-buttom-s">
                <button class="submitFenBan">确定</button>
                <button class="closeFenBan">取消</button>
            </dd>
        </dl>
    </div>
</div>
<!--===================学生分班end====================-->

<!--===================老师和教室设置start====================-->
<div class="placement-popup-JS">
    <div class="sports-popup-top">
        <em>老师和教室设置</em>
        <i class="sports-II closeJS">X</i>
    </div>
    <div class="place-popup-main">
        <div>${term}/${gradeName}</div>
        <dl>
            <dd>
                <em>课程</em>
                <span id="cName" courseId="">历史A1</span>
            </dd>
            <dd>
                <em>任课老师</em>
                <select id="teacherCtx"></select>
                <script type="application/template" id="teacherTmpl">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>教室</em>
                <select id="classRoomCtx"></select>
                <script type="application/template" id="classRoomTmpl">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.roomName}}</option>
                    {{~}}
                </script>
            </dd>
            <dd class="popup-buttom-s">
                <button class="popup-TT" id="updateJS">提交</button>
                <button class="closeJS">取消</button>
            </dd>
        </dl>
    </div>
</div>
<!--===================老师和教室设置end====================-->

<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('bianbanrule', function (bianbanrule) {
        bianbanrule.init();
    });
</script>

</body>
</html>
