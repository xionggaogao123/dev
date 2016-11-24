<%--
  
                       _oo0oo_
                      o8888888o
                      88" . "88
                      (| -_- |)
                      0\  =  /0
                    ___/`---'\___
                  .' \\|     |// '.
                 / \\|||  :  |||// \
                / _||||| -:- |||||- \
               |   | \\\  -  /// |   |
               | \_|  ''\---/''  |_/ |
               \  .-\__  '-'  ___/-. /
             ___'. .'  /--.--\  `. .'___
          ."" '<  `.___\_<|>_/___.' >' "".
         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
         \  \ `_.   \_ __\ /__ _/   .-` /  /
     =====`-.____`.___ \_____/___.-`___.-'=====
                       `=---='


     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

               佛祖保佑         永无BUG



  User: Tony
  Date: 2014/10/31
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/css/tree.css"/>
    <script type="text/javascript" src='/js/jquery-1.11.1.min.js'></script>
    <script src="/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script>
        $(function () {
            $.fn.zTree.init($("#teacherDirUl"), {
                async: {
                    enable: true,
                    url: '/directory/viewTeacherDirs.do',
                    otherParam: {
                        teacherId: 113
                    }
                },
                data: {
                    simpleData: {
                        enable: true,
                        pIdKey: 'parent',
                        rootPId: 0
                    }
                }
            });
            $('#course-container').load('/lesson/allTeacherLessons.do', {
                teacherId: 113
            });
        });
    </script>
</head>
<body>
<div>
    <ul id="teacherDirUl" class="ztree dir-tree"></ul>
    <div>
        <div>免费开放课程</div>
        <div id="course-container"></div>
    </div>
</div>
</body>
</html>
