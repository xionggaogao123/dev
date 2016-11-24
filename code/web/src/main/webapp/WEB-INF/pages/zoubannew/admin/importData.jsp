<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/10/24
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>3+3走班-导入教学班学生&学号</title>

    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>

<div id="importNumWindow" class="fdset-alert" style="display: block;position: absolute;top:30%;left: 200px;">
    <div class="zb-set-title clearfix">
        <p>导入学号</p>
        <span class="importWindowClose">X</span>
    </div>
    <div style="margin: 5px auto;padding: 10px 35px;">
        <div style="color: red;">导入数据说明：导入数据请严格按照模板，请勿修改表头.</div>
        <form id="importNumForm" method="post" enctype="multipart/form-data" style="margin-top: 20px;">
            <input type="file" id="numFile" name="file"/>
        </form>
        <div class="model-btn">
            <button id="downloadNumTemplate" class="fcset-down" style="margin-top: 20px;">下载模板</button>
            <button id="uploadNumFile" style="margin-top: 20px;">开始导入</button>
        </div>
    </div>
</div>
<div id="importStuWindow" class="fdset-alert" style="display: block;">
    <div class="zb-set-title clearfix">
        <p>导入教学班学生</p>
        <span class="importWindowClose">X</span>
    </div>
    <div style="margin: 5px auto;padding: 10px 35px;">
        <div style="color: red;">导入数据说明：导入数据请严格按照模板，请勿修改表头.</div>
        <form id="importStuForm" method="post" enctype="multipart/form-data" style="margin-top: 20px;">
            <input type="file" id="stusFile" name="file"/>
        </form>
        <div class="model-btn">
            <button id="downloadStusTemplate" class="fcset-down" style="margin-top: 20px;">下载模板</button>
            <button id="uploadStusFile" style="margin-top: 20px;">开始导入</button>
        </div>
    </div>
</div>




<script src="/static_new/js/modules/core/0.1.0/jquery.min.js"></script>
<script src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.form.min.js"></script>
<script src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
<script>
    $('body').on('click', '#downloadNumTemplate', function() {
        downloadNumTemplate();
    });
    $('body').on('click', '#uploadNumFile', function () {
        if ($('#numFile').val() == '') {
            layer.alert('请选择文件');
            return;
        } else {
            importNum();
        }
    });

    /**
     * 下载学生学号模板
     */
    function downloadNumTemplate() {
        window.location.href = '/importZBData/downloadNumTemplate.do';
    }

    /**
     * 导入学生学号
     */
    function importNum() {
        var index = layer.load(2, {shade: [0.3, '#000']});
        $('#importNumForm').ajaxSubmit({
            url: '/importZBData/importStuNum.do',
            success: function (data) {
                layer.close(index);
                layer.alert(data.message);
            },
            error: function (xhr, e) {
                console.log(e);
                layer.close(index);
                layer.alert(e);
            }
        });
    }


    $('body').on('click', '#downloadStusTemplate', function() {
        downloadStuTemplate();
    });

    $('body').on('click', '#uploadStusFile', function () {
        if ($('#stusFile').val() == '') {
            layer.alert('请选择文件');
            return;
        } else {
            importStus();
        }
    });

    /**
     * 下载教学班学生模板
     */
    function downloadStuTemplate() {
        window.location.href = '/importZBData/downloadStuTemplate.do';
    }

    /**
     * 导入教学班学生
     */
    function importStus() {
        var index = layer.load(2, {shade: [0.3, '#000']});

        $('#importStuForm').ajaxSubmit({
            url: '/importZBData/importStus.do',
            success: function (data) {
                layer.close(index);
                layer.alert(data.message);
            },
            error: function (xhr, e) {
                console.log(e);
                layer.close(index);
                layer.alert(e);
            }
        });
    }

</script>
</body>
</html>
