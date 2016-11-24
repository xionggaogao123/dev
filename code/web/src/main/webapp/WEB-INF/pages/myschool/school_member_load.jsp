<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
     <script>
       function sub()
       {
    	   if($('#check').is(':checked'))
    	   {
    		   jQuery("#type").val(1);
    	   }
    	   else
    	   {
    		   jQuery("#type").val(0);
    	   }
    	   jQuery("#form").submit();
       }
    </script>
</head>
<body>
 <form id="form" action="/user/mamager/down/school/members.do" method="post" enctype="multipart/form-data">
		   
		  <br>
		  <br>
		  <br>
		      学校名称关键字： <input type="input" id="schName" name="schName"/>
		      学校初始密码： <input type="input" id="initPwd" name="initPwd"/>
		           <input type="hidden" id="type" name="type"/> 
                                 是否导出毕业班：<input type="checkbox" id="check" name="check"  checked="checked"/>
         <input type="button" value="提交" onclick="sub()">
        
        
 </form>
</body>

</html>