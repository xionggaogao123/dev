<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/3/29
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品分类简介</title>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/mall/uploadVideo.css" rel="stylesheet">
</head>
<body>

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <%--列表--%>
            <div class="container">
                <div class="col-md-3"></div>
                <div class="col-md-6 border">
                    <div style="margin-top: 20px">
                        <button class="btn btn-add" id="add">新增</button>
                    </div>
                    <script id="listJS" type="application/template">
                        {{~it:value:index}}
                        <li class="list-group-item">
                            <span>名称：{{=value.name}}</span>
                            <a href="#" style="position:absolute;left:80%;"
                               onclick="videoDetail('{{=value.id}}')">详情/编辑</a>
                        </li>
                        {{~}}
                    </script>
                    <div style="margin-top: 20px">
                        <ul id="listCtx" class="list-group"></ul>
                    </div>
                    <div class="new-page-links"></div>
                </div>
                <div class="col-md-3"></div>
            </div>
            <%--新增--%>
            <div class="addVideo container">
                <div class="row">
                    <form role="form">
                        <input type="hidden" id="id" name="id">
                        <div>
                            <div class="col-md-3">
                                <label for="name">名称</label>
                            </div>
                            <div class="col-md-9">
                                <input id="name" type="text" name="name" class="form-control">
                            </div>
                        </div>
                        <div>
                            <div class="col-md-3">
                                <label for="title">title</label>
                            </div>
                            <div class="col-md-9">
                                <input id="title" type="text" name="title" class="form-control">
                            </div>
                        </div>
                        <div>
                            <div class="col-md-3">
                                <label for="category">商品分类</label>
                            </div>
                            <div class="col-md-9">
                                <script id="categoryJS" type="application/template">
                                    {{~it:value:index}}
                                    <option value="{{=value.id}}">{{=value.name}}</option>
                                    {{~}}
                                </script>
                                <select id="category" name="category" class="form-control" required="required"></select>
                            </div>
                        </div>
                        <div>
                            <div class="col-md-3">
                                <label for="text">文案</label>
                            </div>
                            <div class="col-md-9">
                    <textarea id="text" name="text" class="form-control"
                              style="resize:none;height:150px;"></textarea>
                            </div>
                        </div>
                        <input type="hidden" id="videoId" name="videoId" value="">
                        <input type="hidden" id="videoImageUrl" name="videoImageUrl" value="">
                        <input type="hidden" id="imageUrl" name="imageUrl" value="">
                    </form>
                </div>
                <div style="margin-top:10px;">
                    <div class="row">
                        <div class="col-md-3">
                            <label for="video">上传视频</label>
                        </div>
                        <div class="col-md-9">
                            <input id="video" name="video" type="file" value="上传视频"
                                   size="1" style="height: 20px; opacity: 0.8;"
                                   accept="video/*">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3">
                            <label for="image">上传图片</label>
                        </div>
                        <div class="col-md-9">
                            <input id="image" name="image" type="file" value="上传图片"
                                   size="1" style="height: 20px; opacity: 0.8;"
                                   accept="image/*">
                        </div>
                    </div>
                </div>
                <div class="text-center" style="margin-top:50px;">
                    <button id="submit" type="button" class="btn btn-primary">提交并返回</button>
                    <button id="cancel" type="button" class="btn btn-primary">取消</button>
                </div>
            </div>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>

<script>
    seajs.use('uploadVideo', function (uploadVideo) {
        uploadVideo.init(1);
    });
    //上传视频
    $('#video').fileupload({
        url: '/commonupload/video.do',
        paramName: 'Filedata',
        formData: {'type': 'categoryVideo'},
        done: function (e, response) {
            if (response.result.code != '500') {
                alert("上传成功！");
                var videoId = response.result.videoInfo.id;
                var videoImg = response.result.videoInfo.imageUrl;
                if (videoImg == "") {//设置视频默认图片
                    videoImg = "/img/K6KT/main-page/store-st.png";
                }
                $('#videoId').val(videoId);
                $('#videoImageUrl').val(videoImg);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {
            $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
        }
    });
    //上传图片
    $('#image').fileupload({
        url: '/mall/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                alert("上传成功！");
                var image = response.result.message[0].path;
                $('#imageUrl').val(image);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {
            $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
        }
    });
</script>

</body>
</html>
