<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>公文流转</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <%--<link href="/static_new/css/docflow/reset.css?v=1" rel="stylesheet"/>--%>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/docflow/gongwen.css?v=2015041602" rel="stylesheet"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <style>
        .homepage-right-top,.col-left{display: none;}
    </style>
</head>
<body docid="${docId}" uid="${userName}">
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%--<%@ include file="../common_new/head-cloud.jsp" %>--%>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
    <%--<c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--广告-->
    <div class="col-right" style="width: 1000px;float: none;">
        <!--.tab-col右侧-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul id="unReadCountShow">

                </ul>
                <script type="application/template" id="unReadCountJs">
                    <li>
                        <a href="/docflow/documentList.do?type=0&a=10000&version=51">公文</a>
                        {{?it.unread>0}}
                        <span>
                            {{=it.unread}}
                        </span>
                        {{?}}
                    </li>
                    <li class="cur">
                        <a href="/docflow/documentList.do?type=1&a=10000&version=51">审阅</a>
                        {{?it.uncheck>0}}
                        <span>{{=it.uncheck}}</span>
                        {{?}}
                    </li>
                    <li>
                        <a href="/docflow/documentList.do?type=2&a=10000&version=51">我的公文</a>
                        {{?it.promote>0}}
                        <span class="Lg">{{=it.promote}}</span>
                        {{?}}
                    </li>
                </script>
            </div>
            <div class="tab_main">
                <a href="/docflow/documentList.do?type=1&a=10000&version=51" class="Bk"> < 返回</a>
                <a href="/docflow/documentModify.do?docId={{=it.id}}&type=1&a=10000&version=51" id="goToHref" class="tab-XG">修改原文</a>

                <h3 id="titleShow" style="text-align: center"></h3>

                <p class="Bt">
                    <script type="application/template" id="departmentJs">
                        <em>部门： {{=it.departmentName}}</em>
                        <em>发布日期： {{=it.time}}</em>
                    </script>
                </p>
                <!-- 正文 -->
                <ul class="Zw">
                    <li id="contentShow"></li>
                </ul>
                <!-- 正文 -->
                <div class="Fj" id="Fj_show">
                    <script type="application/template" id="fileJs">
                        {{?it.data.length>0}}
                        <span>附件：</span>
                        {{?}}
                        {{~it.data:value:index}}
                        <a href="{{=value.value}}" fn="{{=value.name}}" target="_blank">{{=value.name}}({{=value.userName}})</a>
                        {{~}}
                    </script>
                </div>
                <div class="tab_AN clearfix">
                    <span>审阅意见</span>

                    <div class="tab_BU">
                        <button id="TY" class="tab_HV">同意</button>
                        <button id="BH">驳回</button>
                        <c:choose>
                            <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)}">
                                <button id="FB" class="head_JS">结束流程并发布</button>
                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>

                        <button id="FQ">废弃</button>
                        <button id="ZJ">转寄</button>
                    </div>
                </div>

                <%--审核部分的附件列表--%>
                <script type="application/template" id="fileListJs">
                    {{?it.data.length}}
                    <span>附件：</span>
                    {{?}}
                    {{~it.data:value:index}}
                    <a href="{{=value.path}}" fn="{{=value.name}}" target="_blank">{{=value.name}}({{=value.userName}})</a>
                    <i class="removeFile" did="{{=value.path}}">x</i>
                    {{~}}
                </script>
                <!--=======================start同意时弹出===========================-->
                <div class="tab-TY" id="tab-TY" style="display: block">
                    <span>下一位审阅人</span>
                    <input type="text" class="checkPeople_TY" disabled="disabled">
                    <button class="tab_TX" id="txl_TY">通讯录</button>
                    <span class="tab-BZ">备注</span>

                    <textarea id="TY_reason"></textarea>
                    <label class="tab_TJ" for="TY_fj" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;">
                        <img
                                src="/img/fileattach.png"/> 添加附件
                    </label>

                    <div style="width: 0; height: 0; overflow: visible">
                        <input id="TY_fj" type="file" name="file" value="添加附件"
                               size="1" style="width: 0; height: 0; opacity: 0">
                    </div>
                    <div id="fileListShow_TY">
                    </div>
                    <div class="tab_bottom">
                        <button class="tab_TY" id="TY_btn">同意并转发</button>
                    </div>
                </div>
                <!--=======================end同意时弹出===========================-->
                <!--=======================start驳回时弹出===========================-->
                <div class="tab-TY" id="tab-BH" style="display: none">
                    <span>历史审阅人</span>
                    <select id="BH_select">
                        <script type="application/template" id="historyManJs">
                            {{~it.data:value:index}}
                                    {{?index==it.data.length-1}}
                            <option value="{{=value.userId}}/{{=value.departmentId}}" selected="selected">
                                <span>{{=index+1}}</span>
                                <span>{{=value.userName}}</span>
                                <span>{{=value.departmentName}}</span>
                            </option>
                                    {{??}}
                            <option value="{{=value.userId}}/{{=value.departmentId}}">
                                <span>{{=index+1}}</span>
                                <span>{{=value.userName}}</span>
                                <span>{{=value.departmentName}}</span>
                            </option>
                                    {{?}}
                            {{~}}
                        </script>
                    </select>
                    <span class="tab-BZ">驳回原因</span>

                    <textarea id="BH_reason"></textarea>
                    <label class="tab_TJ" for="BH_fj" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;">
                        <img
                                src="/img/fileattach.png"/> 添加附件
                    </label>

                    <div style="width: 0; height: 0; overflow: visible">
                        <input id="BH_fj" type="file" name="file" value="添加附件"
                               size="1" style="width: 0; height: 0; opacity: 0">
                    </div>
                    <div id="fileListShow_BH">
                    </div>
                    <div class="tab_bottom">
                        <button class="tab_TY" id="BH_btn">驳回</button>
                    </div>
                </div>
                <!--=======================end驳回时弹出===========================-->
                <!--=======================start结束流程并发布时弹出===========================-->
                <div class="tab-TY" id="tab-FB" style="display: none">
                    <span class="tab-BZ">备注</span>

                    <textarea id="FB_reason"></textarea>
                    <label class="tab_TJ" for="FB_fj" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;">
                        <img
                                src="/img/fileattach.png"/> 添加附件
                    </label>

                    <div style="width: 0; height: 0; overflow: visible">
                        <input id="FB_fj" type="file" name="file" value="添加附件"
                               size="1" style="width: 0; height: 0; opacity: 0">
                    </div>
                    <div id="fileListShow_FB">
                    </div>
                    <div class="tab_bottom">
                        <button class="tab_TY head_JS" id="FB_btn">结束流程并发布</button>
                    </div>
                </div>
                <!--=======================end结束流程并发布时弹出===========================-->
                <!--=======================start废弃时弹出===========================-->
                <div class="tab-TY" id="tab-FQ" style="display: none">
                    <span class="tab-BZ">作废原因</span>

                    <textarea id="FQ_reason"></textarea>
                    <label class="tab_TJ" for="FQ_fj" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;">
                        <img
                                src="/img/fileattach.png"/> 添加附件
                    </label>

                    <div style="width: 0; height: 0; overflow: visible">
                        <input id="FQ_fj" type="file" name="file" value="添加附件"
                               size="1" style="width: 0; height: 0; opacity: 0">
                    </div>
                    <div id="fileListShow_FQ">
                    </div>
                    <div class="tab_bottom">
                        <button class="tab_TY" id="FQ_btn">作废</button>
                    </div>
                </div>
                <!--=======================end废弃时弹出===========================-->
                <!--=======================start转寄时弹出===========================-->
                <div class="tab-TY" id="tab-ZJ" style="display: none">
                    <span>下一位接收人</span>
                    <input type="text" class="checkPeople_ZJ" disabled="disabled">
                    <button class="tab_TX" id="txl_ZJ">通讯录</button>
                    <span class="tab-BZ">转寄原因</span>

                    <textarea id="ZJ_reason"></textarea>
                    <label class="tab_TJ" for="ZJ_fj" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;">
                        <img
                                src="/img/fileattach.png"/> 添加附件
                    </label>

                    <div style="width: 0; height: 0; overflow: visible">
                        <input id="ZJ_fj" type="file" name="file" value="添加附件"
                               size="1" style="width: 0; height: 0; opacity: 0">
                    </div>
                    <div id="fileListShow_ZJ">
                    </div>
                    <div class="tab_bottom">
                        <button class="tab_TY" id="ZJ_btn">转寄</button>
                    </div>
                </div>
                <!--=======================end转寄时弹出===========================-->
                <div class="tab_LS">
                    <span>审阅历史</span>
                    <table id="checkHistoryShow">
                        <script type="application/template" id="checkHistoryJs">
                            <tr>
                                <th>操作人</th>
                                <th>操作人部门</th>
                                <th>审批意见</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.userName}}</td>
                                <td>{{=value.departmentName}}</td>
                                <td>{{=value.opinionDesc}}</td>
                                <td style="text-align: left">{{=value.remark}}</td>
                                <td>{{=value.time}}</td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
                <!-- 弹出层部分 -->
                <div class="gay"></div>
                <!-- 审阅人 -->
                <div class="Tanchu2">
                    <p class="clearfix">选择审阅人
                        <span class="qx2">取消</span>
                        <span class="QD2">确定</span>
                    </p>

                    <div class="xuanren">
                        <p class="Hm">
                            <em id="choosedMan">未选择</em>
                        </p>

                        <div class="clearfix">
                            <ul>
                                <h4>类别</h4>
                                <li class="current group1" tag="dep">部门</li>
                                <%--<li class="group1" tag="sub">学科</li> 暂时不用，后期根据需求决定--%>
                            </ul>
                            <ol></ol>
                            <ul class="K1 K1show">

                            </ul>
                            <script type="application/template" id="K1JS">
                                <h4>{{=it.type}}</h4>
                                {{~it.data:value:index}}
                                <li class="group2" tag="{{=it.type}}" index="{{=index}}" dep="{{=value.t.idStr}}">
                                    {{=value.t.value}}
                                </li>
                                {{~}}
                            </script>
                            <ol></ol>
                            <ul class="K2 K2show">
                            </ul>
                            <script type="application/template" id="K2JS">
                                <h4>人员</h4>
                                {{~it.data:value:index}}
                                {{?value.choosed==1}}
                                <li class="group3 current" userId="{{=value.idStr}}" name="{{=value.value}}">
                                    {{=value.value}}
                                </li>
                                {{??value.choosed==0}}
                                <li class="group3" userId="{{=value.idStr}}" name="{{=value.value}}">{{=value.value}}
                                </li>
                                {{?}}
                                {{~}}
                            </script>
                        </div>
                    </div>
                </div>
                <!-- 弹出层部分 -->

            </div>
        </div>
        <!--/.tab-col右侧-->
    </div>
    <!--/.col-right-->
</div>
<!--/#content-->
<!--/#content-->
<div class="popup-head" style="display:none">

</div>
<div class="popup-info" style="display:none">
    <dl>
        <dt>
            <em>提醒</em>
        </dt>
        <dd>
            <em></em>
        </dd>
        <dd>
            <em class="popup-op">确定要撤销改公文吗？</em>
        </dd>
        <dd>
            <em>
                <button class="he_qd">确定</button>
                <button class="he_qx">取消</button>
            </em>
        </dd>
    </dl>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<%----%>
<script>
    seajs.use('documentCheck');
</script>
</body>
</html>