<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/10/12
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Title</title>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-browser.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery.qqFace.js"></script>
    <script type="text/javascript" src="/static/js/jquery.qrcode.min.js"></script>
    <style>
        .spa-upload {
            display: inline-block;
            width: 64px;
            height: 21px;
            text-align: center;
            border: 1px solid #28CF7B;
            color: #28CF7B;
            margin-right: 10px;
            cursor: pointer;
            line-height: 21px;
            font-size: 13px;
        }
    </style>
</head>
<body>
<div style="margin-left: 300px;margin-top: 100px;">
    <span>app标识Id:</span><span><input type="text" id="id"></span>
    <br/>
    <span>app包名:</span><span><input type="text" id="appPackageName"></span>
    <br/>
    <span>logo:</span><span><div>
       <img id="imageUrl" src="/static/images/community/upload.png">
       <p>
           <label for="image-upload"><span class="spa-upload">上传图片</span>你可以上传JPG、GIF、或PNG格式的文件，文件大小不超过2M</label>
       </p>
       <input type="file" name="image-upload" id="image-upload" accept="image/*" size="1"
              hidden="hidden"/>
       </div></span>
    <br/>
    <span>类型:</span><span><input type="text" id="type"></span>
    <br/>
    <span>大小:</span><span><input type="text" id="size"></span>
    <br/>
    <span>apk的大小:</span><span><input type="text" id="appSize"></span>
    <br/>
    <span>版本:</span><span><input type="text" id="versionName"></span>
    <br/>
    <span>版本code:</span><span><input type="text" id="versionCode"></span>
    <br/>
    <span>描述:</span><span><textarea id="description" rows="10"></textarea></span>
    <br/>
    <span>app名称:</span><span><input type="text" id="appName"></span>
    <br/>
    <span>下载路径:</span><span><input type="text" id="url"></span>
    <br/>
    <span>展示图片:</span><span><div>
       <div id="appendImage"></div>
       <p>
           <label for="upload-image"><span class="spa-upload">上传图片</span>你可以上传JPG、GIF、或PNG格式的文件，文件大小不超过2M</label>
       </p>
       <input type="file" name="upload-image" id="upload-image" accept="image/*" size="1"
              hidden="hidden"/>
       </div></span>
    <span id="submit" onclick="submit()" style="cursor: pointer;color: #5db75d">提交</span>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <span>社区名称:</span><span><input type="text" id="communityName"></span>
    <span id="submitCommunity" onclick="submitCommunity()" style="cursor: pointer;color: #5db75d">设置老的用户为家长用户</span>
    <br/>
    <br/>
    <br/>
    <br/>
    <span>用户名:</span><span><input type="text" id="userName"></span>
    <span>昵称:</span><span><input type="text" id="nickName"></span>
    <span>默认号码:</span><span><input type="text" value="18817366135" id="phoneNumber"></span>
    <span>设置角色:</span><span><input type="text" value="0" id="newRole"></span>
    <span id="registerUser" onclick="registerUser()" style="cursor: pointer;color: #5db75d">注册账户</span>
    <br/>
    <br/>
    <br/>
    <label class="btn import-btn">导入
        <input style="display: none;" id="uploadImportTemplate" type="file" multiple="multiple" name="importTemplate" type="file" accept=".apk">
    </label>
    <%--<div id="qrcode"></div>--%>
    <%--<script type="text/javascript">--%>
        <%--$('#qrcode').qrcode({--%>
            <%--render: "canvas", //也可以替换为table--%>
            <%--width: 100,--%>
            <%--height: 100,--%>
            <%--text: "http://www.baidu.com"--%>
        <%--});--%>
    <%--</script>--%>
</div>
<script type="text/javascript">

    function registerUser() {
        var userName = $.trim($('#userName').val());
        if (null == userName || "" == userName) {
            alert("用户名不能为空!");
            return;
        }
        var phoneNumber = $.trim($('#phoneNumber').val());
        if (null == phoneNumber || "" == phoneNumber) {
            alert("手机号码不能为空!");
            return;
        }
        var newRole = $.trim($('#newRole').val());
        if (null == newRole || "" == newRole) {
            alert("角色不能为空!");
            return;
        }
        var nickName = $.trim($('#nickName').val());
        if (null == nickName || "" == nickName) {
            alert("昵称不能为空!");
            return;
        }
        var param = {};
        param.userName = userName;
        param.phoneNumber = phoneNumber;
        param.newRole = newRole;
        param.nickName = nickName;
        var url = "/web/user/registerUser.do"
        $.ajax({
            type: "GET",
            data: param,
            url: url,
            async: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                alert(resp.message);
            }
        });
    }

    function submitCommunity() {
        var url = "/web/community/setOldUserData.do";
        var data = {};
        data.communityName = $.trim($('#communityName').val());
        $.ajax({
            type: "GET",
            data: data,
            url: url,
            async: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                alert("设置老用户数据为家长成功!");
            }
        });
    }
    function submit() {
        var message = {};
        message.id = $.trim($('#id').val());
        message.appPackageName = $.trim($('#appPackageName').val());
        message.logo = $.trim($('#imageUrl').attr('src'));
        message.type = $.trim($('#type').val());
        message.size = $.trim($('#size').val());
        message.versionName = $.trim($('#versionName').val());
        message.description = $.trim($('#description').val());
        message.appName = $.trim($('#appName').val());
        message.url = $.trim($('#url').val());
        message.versionCode = $.trim($('#versionCode').val());
        message.appSize = $.trim($('#appSize').val());
        var imageList = new Array();
        $('.attachItem').each(function (i) {
            var url = $(this).attr('src');
            var item = {};
            item.url = url;
            item.flnm = "文件" + i;
            item.uploadUserId = "";
            item.time = "";
            imageList.push(item);
        })
        message.imageList = imageList;
        var url = "/web/appMarket/saveAppDetail.do";
        $.ajax({
            type: "POST",
            url: url,
            async: false,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(message),
            success: function (result) {
                alert("保存成功!");
            }
        });
    }
</script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
<script>
    $('#upload-image').fileupload({
        url: '/web/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                var image = response.result.message[0].path;
                var imageStr = "<img class=\"attachItem\" src=\"" + image + "\" width='100px' height='100px'>";
                $('#appendImage').append(imageStr);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {

        }
    });
    //上传图片
    $('#image-upload').fileupload({
        url: '/web/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                var image = response.result.message[0].path;
                $('#imageUrl').attr('src', image);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {

        }
    });
</script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/appmarket/0.1.0/appMarketIndex.js');
</script>
</body>
</html>
