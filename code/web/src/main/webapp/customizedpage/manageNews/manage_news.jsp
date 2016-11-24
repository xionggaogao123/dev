<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-2
  Time: 下午8:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>复兰科技--管理新闻</title>
    <link rel="stylesheet" href="css/news_manage.css">
    <script type="text/javascript" src="/customizedpage/jinan/js/jquery-1.11.1.min.js"></script>
    <script type="application/javascript">
        $(function(){
            $(".news_close").click(function(){
                $(".news_manage_bf").css("display","none");
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

<!--=====================================内容管理================================================-->
<div class="manange_cg">
    <!--==========================stat-引入头部=============================-->


    <!--==========================end-引入头部=============================-->

    <!--==========================stat-引入左边导航=============================-->

    <!--==========================end-左边导航=============================-->


    <!--==========================stat-引入头部广告=============================-->

    <!--==========================end-头部广告=============================-->
    <div class="manange_ck">
        <div class="content_manage_bg">
            <div class="news_manage_top">
                <span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;内容管理</span>
            </div>
            <div class="news_manage_info">
                <dl>
                    <dt>
                        <a href="manage_content.jsp" class="news_top_I">栏目管理</a>
                        <a class="news_top_II news_top_H">内容管理</a>
                    </dt>
                    <dd>
                        <span>内容列表</span>
                        <button class="new_button_I">&nbsp;&nbsp;&nbsp;&nbsp;删除</button>
                        <button class="new_button_III">排序</button>
                        <button class="new_button_V">+添加栏目</button>
                    </dd>
                    <dd class="content_manage_br">
                        <div class="content_manage_left">筛选</div>
                        <div class="content_manage_right">
                            <span>选择</span>
                            <select>
                                <option>2</option>
                                <option>3</option>
                            </select>
                            <span>标题</span>
                            <input type="text" name="">
                            <button>搜索</button>
                        </div>
                    </dd>
                    <dd class="content_manage_info_top">
                        <input type="checkbox" name="">
                        <strong>排序</strong>
                        <p>标题</p>
                        <em>栏目</em>
                        <i>时间</i>
                        <i>操作</i>
                    </dd>
                    <dd class="content_manage_info_middle">
                        <input type="checkbox" name="">
                        <strong>1</strong>
                        <p><span class="content_manage_info_sp">【置顶】</span>垃圾费拉科技是地方了看见爱上了对方就爱上对方</p>
                        <em>新闻</em>
                        <i>2015/07/01/12:55</i>
                        <button>编辑</button>
                        <button class="news_manage_button">删除</button>
                    </dd>
                    <dd class="content_manage_info_middle">
                        <input type="checkbox" name="">
                        <strong>1</strong>
                        <p><span class="content_manage_info_sp">【置顶】</span>垃圾费拉科技是地方了看见爱上了对方就爱上对方</p>
                        <em>新闻</em>
                        <i>2015/07/01/12:55</i>
                        <button>编辑</button>
                        <button class="news_manage_button">删除</button>
                    </dd>
                    <dd class="content_manage_info_middle">
                        <input type="checkbox" name="">
                        <strong>1</strong>
                        <p><span class="content_manage_info_sp">【置顶】</span>垃圾费拉科技是地方了看见爱上了对方就爱上对方</p>
                        <em>新闻</em>
                        <i>2015/07/01/12:55</i>
                        <button>编辑</button>
                        <button class="news_manage_button">删除</button>
                    </dd>
                    <dd class="content_manage_info_middle">
                        <input type="checkbox" name="">
                        <strong>2</strong>
                        <p>标题</p>
                        <em>栏目</em>
                        <i>时间</i>
                        <button>编辑</button>
                        <button class="news_manage_button">删除</button>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<!--===================================弹出框===========================================-->
<div class="news_manage_bf"></div><!--====================黑色背景===================-->
<div class="content_manage_cg">
    <dl>
        <dt>&nbsp;&nbsp;&nbsp;&nbsp;发布内容和编辑内容<span class="news_close">x</span></dt>
        <dd>
            <span>*栏目</span>
            <select>
                <option>1</option>
                <option>2</option>
                <option>3</option>
            </select>
        </dd>
        <dd>
            <span>*标题</span><input type="text">
        </dd>
        <dd class="content_manage_II">
            <span> </span><input type="checkbox" class="content_manage_in" ><p>置顶(置顶内容会在首页显示，置顶内容请上传缩略图)</p>
        </dd>
        <dd>
            <span>缩略图</span><input type="text"><button class="content_manage_bt">上传</button><button class="content_manage_bt">预览</button>
        </dd>
        <dd>
            <span>描述</span><textarea class="content_manage_te"></textarea>
        </dd>
        <dd>
            <span>*正文</span><textarea class="content_manage_ter"></textarea>
        </dd>
        <dd>
            <span></span><button class="content_manage_btt">发布</button><button class="content_manage_btt">取消</button>
        </dd>
    </dl>
</div>
</body>
</html>
