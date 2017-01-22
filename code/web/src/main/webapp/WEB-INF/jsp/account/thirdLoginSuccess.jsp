<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>第三方登录</title>
    <script type="text/javascript" src="/static/js/modules/newIndex/jquery-1.11.1.js"></script>
</head>

<script>
    var openerUrl = window.opener.location.href;

    <c:if test="${bindSuccess == -1 }">
    if(openerUrl.indexOf('login') !== -1) {
        window.opener.location.href = '/';
        window.close();
    } else {
        window.opener.location.reload();
        window.close();
    }
    </c:if>

    <c:if test="${bindSuccess == 0 }">
        alert("绑定失败，该qq号已经被别人绑定了");
        window.close();
    </c:if>

    <c:if test="${bindSuccess == 1 }" >
        alert("绑定成功");
        window.opener.location.reload();
        window.close();
    </c:if>

    <c:if test="${bindSuccess == null}">
        window.location.href = '/';
    </c:if>


</script>
<body>

</body>
</html>
