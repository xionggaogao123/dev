<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-2
  Time: 下午8:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>复兰科技--管理新闻</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news_manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>

    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/manage/manage-news.js"></script>
    <script type="text/javascript" src="/static/js/ajaxfileupload.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script src="http://www.w3cschool.cc/try/angularjs/1.2.5/angular.min.js"></script>
    <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>


    <script type="application/javascript">


    </script>
    <style type="text/css">
        #ueditor_0 {
            width: 412px !important;
            height: 200px !important;
        }
    </style>

</head>
<body>
<!--==========================stat-引入头部=============================-->

<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>
<!--==========================end-引入头部=============================-->

<!--=====================================内容管理================================================-->

<div class="news_manage_bf"></div>

<div class="manange_cg">


    <!--==========================stat-引入左边导航=============================-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--==========================end-左边导航=============================-->


    <!--==========================stat-引入头部广告=============================-->
    <%@ include file="../common/right.jsp" %>
    <!--==========================end-头部广告=============================-->
    <div class="content_manage_bg">
        <div class="news_manage_top">
            <span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;内容管理</span>
        </div>
        <div class="news_manage_info">
            <dl>
                <dt>
                    <a href="/myschool/managenewscolumn?version=85&tag=5" class="news_top_I">栏目管理</a>
                    <a class="news_top_II news_top_H">内容管理</a>
                </dt>
                <dd>
                    <span>内容列表</span>
                    <button class="new_button_I">&nbsp;&nbsp;&nbsp;&nbsp;删除</button>
                    <button class="new_button_V">+发布内容</button>

                </dd>
                <dd class="content_manage_br">
                    <div class="content_manage_left">筛选</div>
                    <div class="content_manage_right">
                        <span id="newsColumnSearch">栏目</span>
                        <select class="newsColumnSearchItem">

                            <option>2</option>
                            <option>3</option>
                        </select>
                        <span>标题</span>
                        <input type="text" id="newsTitleSearch" name="">
                        <button onclick="searchNews()">搜索</button>
                    </div>
                </dd>
                <dd class="content_manage_info_top">
                    <input class="newsTitle" type="checkbox" name="newsTitle">

                    <p>标题</p>
                    <em>栏目</em>
                    <i>时间</i>
                    <i>操作</i>
                </dd>
            </dl>
        </div>
        <div class="page-paginator">
            <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
            <span class="last-page">尾页</span>

        </div>


    </div>
    <!--===================================弹出框===========================================-->
    <%--<div class="news_manage_bf"></div>--%>
    <!--====================黑色背景===================-->
    <div class="content_manage_cg">
        <dl>
            <dt>&nbsp;&nbsp;&nbsp;&nbsp;发布内容和编辑内容<span class="news_close news_close_btn">x</span></dt>
            <dd>
                <span>*栏目</span>
                <select class="choose_column">


                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                </select>
            </dd>
            <dd>
                <span>*标题</span><input class="content_title" type="text">


            </dd>
            <dd class="content_manage_II">
                <span> </span><input type="checkbox" class="content_manage_in">

                <p>置顶(置顶内容会在首页显示，置顶内容请上传缩略图)</p>
            </dd>
            <dd>
                <span>缩略图</span>

                <!--======================上传图片==========================-->
                <img src="" width="75px;" id="img_preview" class="upload_img" height="75px;"
                     style="display: none;float:left;margin-left: 20px;">
                <i id="img-x">x</i>
                <label class="content_manage_bt" id="upload-blog-img" for="image-upload" style="cursor:pointer;">
                    <span class="content_manage_bt">上传</span>
                </label>

                <div class="size-zero" style="width: 0;height: 0;">
                    <input type="file" name="image-upload" id="image-upload" accept="image/*"
                           multiple="multiple"/>
                </div>

                <%--<span>缩略图</span>

                <!--======================上传图片==========================-->
                <img src="" width="75px;" height="75px;" style="display: none;margin-left: 20px;">

                <button class="content_manage_bt">上传</button>--%>

            </dd>
            <dd>
                <span>描述</span><textarea class="content_manage_te"></textarea>
            </dd>
            <dd>
                <span>*正文</span>
                <textarea id="ueditor" class="content_manage_ter" name="content" style="overflow-y: auto"></textarea>
                <%--<textarea class="content_manage_ter"></textarea>--%>


            </dd>
            <dd>
                <span></span>
                <button class="content_manage_btt new_publish_btn">发布</button>
                <button class="content_manage_btt news_close_btn">取消</button>
            </dd>
        </dl>
    </div>

    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.parse.js"></script>
    <script type="text/javascript" src="/static/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style type="text/css">
        .edui-default .edui-editor {
            width: 414px !important;
        }

        .edui-default .edui-editor-iframeholder {
            width: 414px !important;
        }
    </style>
    <script type="application/javascript">
        var ue = UE.getEditor('ueditor',{autoHeightEnabled :false});
    </script>

    <!--预览窗口-->
    <div class="preview-wrapper">
        <div id="preview-container" style="overflow: auto !important;">
            <div style="text-align: center;position: fixed;width: 970px;">
                <span id="title" class="ellipsis" title="previewTitle"
                      style="font: 600 20px 'Microsoft YaHei';margin: 0 16px;height: 47px;line-height: 60px;max-width: 740px;"></span>
                <span id="newsdate"></span>
                <span onclick="closePreview()" class="close-preview">关闭</span>
                <hr style="background: #5D9E20;height: 3px;">
            </div>
            <div id="content" class="preview-content"></div>
        </div>


    </div>
    </div>
    <%@ include file="../common_new/foot.jsp" %>

</body>
</html>
