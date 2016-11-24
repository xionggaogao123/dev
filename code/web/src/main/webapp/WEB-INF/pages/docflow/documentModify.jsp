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
<body docid="${docId}" type="${type}">
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
                    <c:choose>
                        <c:when test="${type==0}">
                            <li class="cur">
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
                            <li>
                                <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="/docflow/documentList.do?type=0&version=51">公文</a>
                                {{?it.unread>0}}
                        <span>
                            {{=it.unread}}
                        </span>
                                {{?}}
                            </li>
                            <li class="cur">
                                <a href="/docflow/documentList.do?type=1&version=51">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                            <li>
                                <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:otherwise>
                    </c:choose>

                </script>
            </div>

            <div class="tab_main">
                <c:choose>
                    <c:when test="${type==0}">
                        <a href="/docflow/documentList.do?type=0&version=51" class="Bk"> < 返回</a>
                    </c:when>
                    <c:otherwise>
                        <a href="" class="Bk gotoHref"> < 返回</a>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${type==1}">
                        <a href="" class="tab-XG gotoHref">取消</a>
                        <a href="javascript:;" class="tab-XG" id="saveDocument">保存修改</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:;" class="tab-XG" id="savePublishedDoc">保存</a>
                    </c:otherwise>
                </c:choose>

                <input id="authorDepartment" type="hidden" value="">

                <h3><input id="docTitle" value=""></h3>

                <p class="Bt">
                    <span id="departTmpShow">
                        <script type="application/template" id="departTmpJs">
                            <em>部门： {{=it.departmentName}}</em>
                            <em>发布日期： {{=it.time}}</em>
                        </script>
                    </span>
                    <span>
                        <em>
                            发布范围
                            <input disabled="disabled" id="choosedPeople">
                            <button class="tanchu">通讯录</button>
                        </em>
                    </span>
                </p>
                <!-- 正文 -->
                <textarea class="pro-de-ZW" id="contentShow"></textarea>

                <div class="inform-BJ">
                    <script id="container" name="content" type="text/plain">

                    </script>
                    <!-- 配置文件 -->
                    <script type="text/javascript"
                            src="/static/plugins/ueditor/ueditor.config.js"></script>
                    <!-- 编辑器源码文件 -->
                    <script type="text/javascript"
                            src="/static/plugins/ueditor/ueditor.all.js"></script>
                    <!-- 实例化编辑器 -->
                    <style type="text/css">
                        .edui-default .edui-editor {
                            width: 700px !important;
                        }

                        .edui-default .edui-editor-iframeholder {
                            width: 700px !important;
                        }
                    </style>
                    <script type="text/javascript">
                        var ue = UE.getEditor('contentShow',{autoHeightEnabled :false});
                    </script>
                </div>
                <!-- 正文 -->
                <label for="file_attach" style="cursor: pointer;margin-top: 12px;border: 1px solid #DDD;float: left"> <img
                        src="/img/fileattach.png"/> 添加附件
                </label>
                <br>
                <br>
                <img src="/img/loading4.gif" id="fileuploadLoading" style="display: none;"/>

                <div style="width: 0; height: 0; overflow: visible">
                    <input id="file_attach" type="file" name="file" value="添加附件"
                           size="1" style="width: 0; height: 0; opacity: 0">
                </div>

                <div class="Fj Mre-SC">

                </div>
                <script type="application/template" id="fileListJs">
                    {{?it.data.length>0}}
                    <span>附件：</span>
                    {{?}}
                    {{~it.data:value:index}}
                    {{?value.id==-1}}
                    <a href="{{=value.value}}" fn="{{=value.name}}" target="_blank">{{=value.name}}(您上传的)</a>
                    <i class="removeFile" did="{{=value.value}}">x</i>
                    {{??}}
                    <a href="{{=value.value}}" fn="{{=value.name}}" target="_blank">{{=value.name}}({{=value.userName}})</a>
                    <i class="removeFile" did="{{=value.value}}">x</i>
                    {{?}}
                    {{~}}
                </script>
                <c:choose>
                    <c:when test="${type==1}">
                        <div class="tab_AN clearfix">
                            <span>审阅意见</span>

                            <div class="tab_BU">
                                <button class="hs" disabled="disabled">同意</button>
                                <button class="hs" disabled="disabled">驳回</button>
                                <button class="hs" disabled="disabled">废弃</button>
                                <button class="hs" disabled="disabled">转寄</button>
                            </div>
                        </div>
                    </c:when>
                </c:choose>

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
                                <td>{{=value.remark}}</td>
                                <td>{{=value.time}}</td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
                <!-- 弹出层部分 -->
                <div class="gay" style="z-index: 990000001;"></div>
                <!-- 审阅人 -->
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
    seajs.use('documentModify');
</script>
</body>
</html>