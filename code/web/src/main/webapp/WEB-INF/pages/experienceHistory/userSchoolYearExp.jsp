<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理学年积分-复兰科技 K6KT-快乐课堂</title>
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
    <script>
        function userSchoolYearExp()
        {
            $.ajax({
                url: "/user/userSchoolYearExpImport.do",
                type: "post",
                success: function (data) {
                    if(data){
                        alert("操作完成");
                    }
                }
            });
        }
    </script>
</head>
<body>
<form id="form">
    <input type="button" value="将老师打分从积分记录提取到学年积分表中" onclick="userSchoolYearExp()">
</form>
</body>

</html>