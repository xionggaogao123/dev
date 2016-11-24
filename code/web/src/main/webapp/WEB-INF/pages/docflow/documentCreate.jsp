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
    <link href="/static_new/css/docflow/jquery.fs.dropper.min.css"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/docflow/gongwen.css?v=2015041602" rel="stylesheet"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static_new/js/modules/docflow/0.1.0/jquery.fs.dropper.min.js"></script>
    <script type="application/javascript">
        var UEDITOR_HOME_URL = "/static/plugins/ueditor/";
        var UEDITOR_IMG_UPLOAD_URL = "/upload/";
        var ue=new UE.ui.Editor({

        });
        ue.render('ueditor_0');

        function getContentText()
        {
            return ue.getContent();
        }
    </script>
</head>
<body uid="${userName}">
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
    <div class="col-right">
        <!--.tab-col右侧-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul id="unReadCountShow">

                </ul>
                <script type="application/template" id="unReadCountJs">
                    <li>
                        <a href="/docflow/documentList.do?type=0&version=51">公文</a>
                        {{?it.unread>0}}
                        <span>
                            {{=it.unread}}
                        </span>
                        {{?}}
                    </li>
                    <li>
                        <a href="/docflow/documentList.do?type=1&version=51">审阅</a>
                        {{?it.uncheck>0}}
                        <span>{{=it.uncheck}}</span>
                        {{?}}
                    </li>
                    <li class="cur">
                        <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                        {{?it.promote>0}}
                        <span class="Lg">{{=it.promote}}</span>
                        {{?}}
                    </li>
                </script>
            </div>
            <div class="tab_main">
                <div style="margin-bottom: 20px">
                    <a href="/docflow/documentList.do?type=2&version=51" class="Bk"> < 返回</a>
                </div>
                <h3 class="Xq">学期 <span id="termShow">${currentTerm}</span></h3>
                <ul class="BT">
                    <li class="clearfix">
                        <label>标题</label>
                        <input type="text" id="titleShow">
                    </li>
                    <li class="clearfix">
                        <span>撰稿&nbsp;&nbsp; ${userName}</span>
                        <label>部门</label>
                        <select name="" id="departmentShow">
                            <script type="application/template" id="departmentJs">
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                                {{~}}
                            </script>
                        </select>
                    </li>
                    <c:choose>
                        <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)}">
                            <li class="clearfix h_audit">
                                <span>需要审核</span>
                                <input type="checkbox" checked="checked" class="ifCheck">
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="clearfix h_audit" style="display: none">
                                <span>需要审核</span>
                                <input type="checkbox" checked="checked" class="ifCheck">
                            </li>
                        </c:otherwise>
                    </c:choose>

                    <li class="clearfix TS ts1">
                        <label>发布范围</label>
                        <input type="text" disabled="disabled" id="choosedPeople">
                        <button>选择</button>
                    </li>
                    <li class="clearfix TS ts2">
                        <label>审阅人</label>
                        <input type="text" disabled="disabled" id="checkPeople">
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
                        <%--<form action="#" method="GET" class="form demo_form">
                            <div class="upload fs-upload-element fs-upload" data-upload-options="{&quot;action&quot;:&quot;https://formstone.it/extra/upload-target.php&quot;}">
                                <div class="fs-upload-target"></div>
                                &lt;%&ndash;<input class="fs-upload-input" type="file" multiple="">&ndash;%&gt;
                            </div>

                            <div class="filelists">
                                <h5>Complete</h5>
                                <ol class="filelist complete">
                                </ol>
                                <h5>Queued</h5>
                                <ol class="filelist queue">
                                </ol>
                                <span class="cancel_all">Cancel All</span>
                            </div>
                        </form>
                        <script>
                            $(".fs-upload-target").dropper({
                                action: "upload.php"
                            });
                            $(".dropper-dropzone").text("sss");
                            /*$(".fs-upload-target").upload({
                                action: "upload.php"
                            });*/
                        </script>--%>
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
                        <span>部门</span>
                        <ul class="K1 publishShow">

                        </ul>
                        <script type="application/template" id="publishJs">
                            {{~it.data:value:index}}
                            <li class="departmentList" curId="{{=index}}">{{=value.t.value}}</li>
                            <ul>
                                {{~value.list:v:i}}
                                {{?v.choosed==1}}
                                <li class="publishList current" userId="{{=v.idStr}}" name="{{=v.value}}">{{=v.value}}
                                </li>
                                {{??v.choosed==0}}
                                <li class="publishList" userId="{{=v.idStr}}" name="{{=v.value}}">{{=v.value}}</li>
                                {{?}}
                                {{~}}
                            </ul>
                            {{~}}
                        </script>
                    </div>
                </div>
                <!-- 审阅人 -->
                <div class="Tanchu2" style="z-index: 990000009;">
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
<script>
    seajs.use('documentCreate');
</script>
</body>
</html>