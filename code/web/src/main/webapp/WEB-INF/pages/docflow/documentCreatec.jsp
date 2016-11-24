<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
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
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/docflow/gongwen.css?v=2015041602" rel="stylesheet"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>


</head>
<body uid="${userName}">
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%--<%@ include file="../common_new/col-left.jsp" %>--%>
    <input type="hidden" id="eduId" value="${eduId}">
    <input type="hidden" id="eduName" value="${eduName}">
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
    <div class="col-right" style="width:1000px;">
        <!--.tab-col右侧-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul id="unReadCountShow">
                    <li class="cur">
                        <a href="#">发布公文</a>
                    </li>
                </ul>
            </div>
            <div class="tab_main">
                <div style="margin-bottom: 20px">
                    <a href="/docflow/documentList.do?type=2&version=51&a=10000" class="Bk"> < 返回</a>
                </div>
                <h3 class="Xq">学期 <span id="termShow">${currentTerm}</span></h3>
                <ul class="BT">
                    <li class="clearfix">
                        <label>标题</label>
                        <input type="text" id="titleShow">
                    </li>
                    <li class="clearfix">
                        <span>撰稿&nbsp;&nbsp; ${eduName}</span>

                    </li>

                    <li class="clearfix TS ts1">
                        <label>发布范围</label>
                        <input type="text" disabled="disabled" id="choosedPeople">
                        <button>选择</button>
                    </li>

                </ul>
                <div class="CJ clearfix">
                    <label>正文</label>

                    <div class="Yc">
                        <textarea id="contentShow"></textarea>

                        <div class="inform-BJ">
                            <!-- 配置文件 -->
                            <script type="text/javascript"
                                    src="/static/plugins/ueditor/ueditor.config.js"></script>
                            <!-- 编辑器源码文件 -->
                            <script type="text/javascript"
                                    src="/static/plugins/ueditor/ueditor.all.js"></script>
                            <style type="text/css">
                                .edui-default .edui-editor {
                                    width: 606px !important;
                                }

                                .edui-default .edui-editor-iframeholder {
                                    width: 606px !important;
                                }
                            </style>
                            <!-- 实例化编辑器 -->
                            <script type="text/javascript">
                                var ue = UE.getEditor('contentShow',{autoHeightEnabled :false});
                            </script>
                        </div>

                        <label for="file_attach" style="cursor: pointer; margin-top: 12px;border: 1px solid #DDD;"> <img
                                src="/img/fileattach.png"/> 添加附件
                        </label>

                        <div style="width: 0; height: 0; overflow: visible">
                            <input id="file_attach" type="file" name="file" value="添加附件"
                                   size="1" style="width: 0; height: 0; opacity: 0">
                        </div>
                        <img src="/img/loading4.gif" id="fileuploadLoading" style="display: none;"/>
                        <br/>
                        <br/>

                        <div id="fileListShow">
                        </div>

                        <script type="application/template" id="fileListJs">
                            {{?it.data.length>0}}
                            <span>附件：</span>
                            {{?}}
                            {{~it.data:value:index}}

                            <a href="{{=value.path}}" fn="{{=value.name}}" target="_blank">{{=value.name}}(我上传的)</a>
                            <i class="removeFile" did="{{=value.path}}">x</i>
                            {{~}}
                        </script>
                    </div>
                </div>
                <div class="TJ">
                    <button class="submitDocument">提交</button>
                    <button class="qX" id="quit">取消</button>
                </div>
                <!-- 弹出层部分 -->
                <div class="gay" style="z-index: 990000001;"></div>
                <!-- 发布范围 -->
                <div class="Tanchu1" style="z-index: 990000009;">
                    <p class="clearfix">发布范围
                        <span class="qx1">取消</span>
                        <span class="QD1">确定</span>
                    </p>

                    <div class="xuanren">
                        <p class="Hm" id="Hm">
                            <em>未选择</em>
                        </p>
                        <script type="application/template" id="choosedPeopleJs">
                            {{~it.data:value:index}}
                            <em>@{{=value.name}}</em>
                            {{~}}
                        </script>
                        <span>学校</span>
                        <ul class="K1 publishShow">

                        </ul>
                        <script type="application/template" id="publishJs">
                            {{~it.data:v:index}}
                                {{?v.choosed==1}}
                                <li class="publishList current" userId="{{=v.id}}" name="{{=v.name}}">{{=v.name}}
                                </li>
                                {{??v.choosed==0}}
                                <li class="publishList" userId="{{=v.id}}" name="{{=v.name}}">{{=v.name}}</li>
                                {{?}}
                                {{~}}
                        </script>
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
<div class="popup-head" style="display:none;z-index: 990000001;">

</div>
<div class="popup-info" style="display:none;z-index: 990000009;">
    <dl>
        <dt>
            <em>提醒</em>
        </dt>
        <dd>
            <em></em>
        </dd>
        <dd>
            <em class="popup-op">确定要撤销该公文吗？</em>
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
<script>
    seajs.use('documentCreateEdu');
</script>
</body>
</html>