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
       <span>版本:</span><span><input type="text" id="version"></span>
       <br/>
       <span>描述:</span><span><textarea id="description"  rows="10"></textarea></span>
       <br/>
       <span>app名称:</span><span><input type="text" id="appName"></span>
       <br/>
       <span>下载路径:</span><span><input type="text" id="url"></span>
       <br/>
       <span id="submit" onclick="submit()" style="cursor: pointer;color: #5db75d">提交</span>
   </div>
   <script type="text/javascript">
       function submit() {
          alert("你好");
       }
   </script>
   <script type="text/javascript"
           src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
   <script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
   <script>
       //上传图片
       $('#image-upload').fileupload({
           url: '/community/images.do',
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
</body>
</html>
