<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/9/18
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>教师评价-管理员</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/evaluation.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>

<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="SZGL" class="cur"><a href="javascript:;">实证资料管理</a><em></em></li>
                    <li id="SZTS"><a href="javascript:;">实证资料推送</a><em></em></li>
                </ul>
            </div>

            <div class="tab-main">
                <!--==============================实证资料管理start==================================-->
                <div class="szzl-manage" id="tab-SZGL">
                    <table class="newTable">
                        <thead>
                        <tr>
                            <th style="width:33%;">教师姓名</th>
                            <th style="width:33%;">实证资料状态</th>
                            <th style="width:34%;">操作</th>
                        </tr>
                        </thead>
                        <tbody id="items">
                        <%--<tr>--%>
                            <%--<td style="background:#ececec">张三丰</td>--%>
                            <%--<td><span class="typing-will">待录入</span></td>--%>
                            <%--<td class="td-span">--%>
                                <%--<span class="eva-edit">编辑</span>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        </tbody>
                        <script id="itemsTmpl" type="text/template">
                            {{~ it:value:index }}
                            <tr tid="{{=value.teacherId}}">
                                <td style="background:#ececec">{{=value.teacherName}}</td>
                                {{? value.eviState == 1 }}
                                <td><span class="typing-ago">已录</span></td>
                                {{??}}
                                <td><span class="typing-will">待录入</span></td>
                                {{?}}
                                <td class="td-span">
                                    <span class="eva-edit">编辑</span>
                                </td>
                            </tr>
                            {{~ }}
                        </script>
                    </table>
                    <div class="new-page-links"></div>
                </div>
                <!--==============================实证资料管理end==================================-->
                <!--==============================实证资料推送start==================================-->
                <div class="szzl-push" id="tab-SZTS" style="display: none">
                        <span class="push-obj">选择需要推送的考核：
                            <select id="evals">
                                <option>请选择考核项目</option>
                            </select>
                            <script id="evalsTmpl" type="text/template">
                                <option value="none">请选择考核项目</option>
                                {{ for(var i in it) { }}
                                <option value="{{=it[i].id}}">{{=it[i].name}}</option>
                                {{ } }}
                            </script>
                        </span>

                    <div class="push-main" style="display: none">
                        <div class="push-all">
                            <label><input type="checkbox" id="allCheck" class="grey">全选</label>
                            <button id="allPush">批量推送</button>
                        </div>
                        <table class="push-table">
                            <thead>
                            <tr>
                                <th style="width:8%;">&nbsp;</th>
                                <th style="width:38%;">教师姓名</th>
                                <th style="width:21%;">实证资料状态</th>
                                <th style="width:33%;">操作</th>
                            </tr>
                            </thead>
                            <tbody id="teachers">
                            <%--<tr>--%>
                                <%--<td><input type="checkbox"></td>--%>
                                <%--<td>张三丰</td>--%>
                                <%--<td><span class="typing-ago">已录</span></td>--%>
                                <%--<td>--%>
                                    <%--<div class="td-div">--%>
                                        <%--<span class="push-look">查看</span>--%>
                                        <%--<i>|</i>--%>
                                        <%--<span class="push-push">推送</span>--%>
                                    <%--</div>--%>
                                <%--</td>--%>
                            <%--</tr>--%>
                            </tbody>
                            <script id="teachersTmpl" type="text/template">
                                {{~ it:value:index }}
                                <tr tid="{{=value.teacherId}}">
                                    <td><input type="checkbox"></td>
                                    <td class="t-name">{{=value.teacherName}}</td>
                                    {{? value.eviState == 1 }}
                                    <td><span class="typing-ago">已录</span></td>
                                    {{??}}
                                    <td><span class="typing-will">待录入</span></td>
                                    {{?}}
                                    <td>
                                        <div class="td-div">
                                            <span class="push-edit">编辑</span>
                                            <i>|</i>
                                            <span class="push-push">推送</span>
                                        </div>
                                    </td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                </div>
                <!--==============================实证资料推送end==================================-->
            </div>
        </div>
        <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<div class="szzl-edit">
    <div class="alert-title clearfix">
        <p>实证资料编辑</p>
        <span class="alert-close">x</span>
    </div>
    <div class="alert-main">
        <span class="name-wrap" id="teacherName">赵灿</span>
        <textarea id="evidence"></textarea>
    </div>
    <div class="alert-btn">
        <button class="btn-save2" id="saveEvidence">保存</button>
        <button class="btn-qx">取消</button>
    </div>
</div>
<div class="push-sure">
    <div class="alert-title clearfix">
        <p>推送</p>
        <span class="alert-close">x</span>
    </div>
    <div class="alert-main">
        <span class="push-span">确定将<em>张三丰</em>的实证资料推送到本次考核</span>

        <div class="alert-btn">
            <button class="btn-sure">确定</button>
            <button class="btn-qx">取消</button>
        </div>
    </div>
</div>
<div class="push-fail">
    <div class="alert-title clearfix">
        <p>提示</p>
        <span class="alert-close">x</span>
    </div>
    <div class="alert-main">
        <div class="img-wrap-f"></div>
        <span class="push-inf">当前没有进行中的考核可供推送</span>

        <div class="alert-btn">
            <button class="btn-know" style="margin:0px;">我知道了</button>
        </div>
    </div>
</div>
</div>
</div>
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/manager');
</script>

</body>
</html>
