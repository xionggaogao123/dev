<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-2
  Time: 下午5:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>复兰科技--管理新闻</title>
    <link rel="stylesheet" href="css/news_manage.css">
    <script type="text/javascript" src="/customizedpage/jinan/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $(".new_button_II").click(function(){
                $(".news_manage_bf").css("display","block");
                $(".news_manage_cg").css("display","block")
            })
        })
        $(function(){
            $(".news_close").click(function(){
                $(".news_manage_bf").css("display","none");
                $(".news_manage_cg").css("display","none")
                $(".content_manage_cg").css("display","none")
            })
        })
        $(function(){
            $(".new_button_V").click(function(){
                $(".news_manage_bf").css("display","block");
                $(".content_manage_cg").css("display","block")
            })
        })
    </script>
</head>
<body>
<div class="manange_ck">
<!--==========================stat-引入头部=============================-->


<!--==========================end-引入头部=============================-->

<!--==========================stat-引入左边导航=============================-->

<!--==========================end-左边导航=============================-->


<!--==========================stat-引入头部广告=============================-->

<!--==========================end-头部广告=============================-->
<!--=======================================栏目管理====================================================-->

    <div class="news_manage_bg  content_manage_bg">
        <div class="news_manage_top">
            <span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;栏目管理</span>
        </div>
        <div class="news_manage_info">
            <dl>
                <dt>
                    <a class="news_top_I news_top_H">栏目管理</a>
                    <a href="manage_news.jsp" class="news_top_II">内容管理</a>
                </dt>
                <dd>
                    <span>栏目列表</span>
                    <button class="new_button_I">&nbsp;&nbsp;&nbsp;&nbsp;删除</button>
                    <button class="new_button_II">+添加栏目</button>
                </dd>
                <dd class="news_manage_info_top">
                    <input type="checkbox" name="">
                    <em>教室开始觉得弗兰卡技的风景啊拉上看到</em>
                    <i>操作</i>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
            </dl>
        </div>
    </div>

</div>













<!--===================================弹出框===========================================-->
<div class="news_manage_bf"></div><!--====================黑色背景===================-->
<div class="news_manage_cg">
    <dl>
        <dt>&nbsp;&nbsp;&nbsp;&nbsp;添加栏目<span class="news_close">x</span></dt>
        <dd>
            <span>栏目名称</span><input type="text">
        </dd>
        <dd>
            <span>栏目目录</span><input type="text">
        </dd>
        <dd>
            <button>提交</button><button>取消</button>
        </dd>
    </dl>
</div>




</body>
</html>
