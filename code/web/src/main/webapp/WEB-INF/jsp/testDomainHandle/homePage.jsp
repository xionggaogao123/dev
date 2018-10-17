<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>七牛-测试域名替换处理</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript">
        function domainUpdate() {
            var model={};
            model.dbName = $("#dbName").val();
            model.tableName = $("#tableName").val();
            model.selField = $("#fieldName").val();
            model.childField = $("#childFieldName").val();
            $.ajax({
                type: "post",
                data:model,
                url: '/testDomainHandle/handleTableTestDomain.do',
                async: false,
                cache : false,
                dataType: "json",
                traditional :true,
                success:function(data){
                    if(data.code==200){
                        alert(data.message);
                    }else{
                        alert(data.message);
                    }
                }
            });
        }
    </script>
</head>
<body>
    dbName：<input id="dbName" value="k6kt_new"><br><br>
    tableName：<input id="tableName" value=""><br><br>
    fieldName ：<input id="fieldName" value=""><br><br>
    childFieldName : <input id="childFieldName" value=""><br><br>
    <button id="update" onclick="domainUpdate()">修改</button>
</body>
</html>
