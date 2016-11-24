<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
    <script type="text/javascript" src='/static/js/ajaxfileupload.js'></script>
     <script>
       function sub()
       {
    	   jQuery("#form").submit();
       }
       
       
       function datacheck()
       {
    	   var schoolId=jQuery("#schoolId").val();
    	   var token=jQuery("#token").val();
    	   $.ajaxFileUpload({
               url: '/user/mamager/datacheck.do?schoolId='+schoolId+"&token="+token, 
               type: 'post',
               secureuri: false, //一般设置为false
               fileElementId: 'file', // 上传文件的id、name属性名
               dataType: 'json', //返回值类型，一般设置为json、application/json
               success: function(data, status){  
            	   
            	   if(data.code=="200")
            		{
            		   jQuery("#checkRes").text("检测通过");
            		}
            	   else
            		{
            		   jQuery("#checkRes").text("检测未通过");
            		 }
            	   
            	   jQuery("#checkalert").html(data.message);
            	   
            	   jQuery(".impor-popup,.bg").show();
                 
               },
               error: function(data, status, e){ 
               }
           });
       }
       
       
       function hideDivs()
       {
    	   jQuery(".impor-popup,.bg").hide();
       }
    </script>
    <style type="text/css">
        .impor-popup{
            position: fixed;
            width: 390px;
            height: 225px;
            top: 50%;
            left: 50%;
            margin-top: -112.5px;
            margin-left: -195px;
            background: white;
            z-index: 999999999999999999999;
            font-family: "Microsoft Yahei";
        }
        .popup-SQ-top {
            height: 45px;
            line-height: 45px;
            background: #616161;
            color: white;
        }
        .popup-SQ-top em {
            margin-left: 15px;
            font-style: normal;
        }
        .popup-SQ-top i {
            float: right;
            margin-right: 15px;
            font-style: normal;
        }
        .h-cursor {
            cursor: pointer;
        }
        .impor-popup-bo p{
            padding: 5px 35px;
        }
        /**************************背景***********************/
        .bg
        {
            display: none;
            background-color: #000;
            width: 100%;
            height: 100%;

            left: 0;
            top: 0;
            filter: alpha(opacity=40);
            opacity: 0.4;
            z-index: 1;
            position: fixed !important; /*FF IE7*/
            position: absolute;
            _top: expression(eval(document.compatMode && document.compatMode=='CSS1Compat') ?
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2); /*IE5 IE5.5*/
        }
    </style>
</head>
<body>
 <form id="form" action="/user/mamager/import.do" method="post" enctype="multipart/form-data">
		      选择文件：<input type="file" id="file" name="file"/>
		  <br>
		  <br>
		  <br>
		  
		  <h1>学校logo,如果是新的学校，则增加logo,如果是已有学校，则更新</h1>
		       学校logo: <input type="file" id="logo" name="logo"/>   
		  <br>
		  <br>
		  
		  <h1>如果是新增学校，请填好以下内容</h1>
		       学校类型： <input type="input" id="type" name="type"/>
		      学校名字： <input type="input" id="schoolName" name="schoolName"/>
		      地区ID： <input type="input" id="regid" name="regid"/>
		      初始密码 <input type="input" id="pwd" name="pwd"/>
		
		      
		  <br>
		  <br>
		  <br>
		  <h1>如果是已经存在的学校</h1>
		       学校ID： <input type="input" id="schoolId" name="schoolId"/>
		   
  
  
  
         <br>
		 <br>
		 <br>
		 <h1>请输入TOKEN</h1>
		 TOKEN： <input type="input" id="token" name="token"/>
  
  
        <!-- 
         <input type="button" value="检测" onclick="datacheck()">
          -->
         <input type="button" value="提交" onclick="sub()">
        
        
 </form>
<div class="impor-popup" style="display:none">
    <div class="popup-SQ-top">
        <em>检测</em><i class="h-cursor SQ-X" onclick="hideDivs()">X</i>
    </div>
    <div class="impor-popup-bo">
         <h1 id="checkRes"></h1>
         <p id="checkalert"></p>
    </div>
</div>
<div class="bg">

</div>
</body>

</html>