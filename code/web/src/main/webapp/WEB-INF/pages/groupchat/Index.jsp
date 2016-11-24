<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="refresh" content="6">
  <title>我的微课-复兰科技 K6KT-快乐课堂</title>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js"></script>
  <script type="text/javascript">
    $(function(){
      $.ajax({
        url:'/groupchat/registChatid.do',
        type:"post",
        data:{},
        success:function(rep){
          $("#pid").html(rep.result);
        },
        error:function(){

        }
      });
    });
  </script>

</head>
<body>
    <p id="pid">aaaa</p>
</body>
</html>