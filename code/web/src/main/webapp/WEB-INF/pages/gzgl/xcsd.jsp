<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/6/16
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-拓展课评价</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="css/expand.css?v=2015041602" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="css/gongziguanli.css">
  <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
  <script type="text/javascript" src="js/gzxmgl.js"></script>
</head>
<body>
<!--#head-->
<div id="head">

  <div class="sub-head clearfix">
    <a href="#" class="logo"></a>
    <div class="head-right">
      <a href="javascript:;" class="user-msg"><i>14</i>私信</a>
                <span class="user-cener">
                    <a href="javascript:;">个人中心</a>
                    <ul style="display:none;">
                      <li><a href="javascript:;">我的私信</a></li>
                      <li><a href="javascript:;">个人设置</a></li>
                      <li><a href="javascript:;">用户手册</a></li>
                      <li><a href="javascript:;">我的好友</a></li>
                    </ul>
                </span>
      <span>欢迎您，siri [ <a href="javascript:;">退出</a> ]</span>
    </div>
  </div>
</div>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <div class="col-left">

    <!--.user-info-->
    <div class="user-info">
      <img src="http://placehold.it/116x116" />
      <em>王源</em>
      <span>课程23&nbsp;&nbsp;经验值339</span>
    </div>
    <!--/.user-info-->

    <!--.left-nav左侧导航-->
    <ul class="left-nav">
      <li class="er current">
        <i class="iconfont">&#xe67b;</i>
        <span>管理统计</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>统计首页</dt></a>
        <a href="javascript:;"><dt>功能使用</dt></a>
      </dl>
      <li class="er">
        <i class="iconfont">&#xf0084;</i>
        <span>成绩分析</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>分析首页</dt></a>
        <a href="javascript:;"><dt>功能使用</dt></a>
      </dl>
      <li class="er">
        <i class="iconfont">&#xf003a;</i>
        <span>家校互动</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>微校园</dt></a>
        <a href="javascript:;"><dt>微家园</dt></a>
        <a href="javascript:;"><dt>通知</dt></a>
      </dl>
      <li class="er">
        <i class="iconfont">&#xe674;</i>
        <span>学习中心</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>微课@翻转课堂</dt></a>
        <a href="javascript:;"><dt>作业</dt></a>
        <a href="javascript:;"><dt>考试</dt></a>
        <a href="javascript:;"><dt>题库/组卷</dt></a>
      </dl>
      <li class="er">
        <i class="iconfont">&#xe6d4;</i>
        <span>电子超市</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>电子超市首页</dt></a>
        <a href="javascript:;"><dt>个人帐户</dt></a>
      </dl>
      <li id="pre">
        <i class="iconfont">&#xe614;</i>
        <span>我的班级</span>
      </li>
      <li id="pre">
        <i class="iconfont">&#xe631;</i>
        <span>好友圈</span>
      </li>
      <li class="er">
        <i class="iconfont">&#xf00aa;</i>
        <span>我的学校</span>
        <s class="iconfont">&#xe61c;</s>
      </li>
      <dl>
        <a href="javascript:;"><dt>班级</dt></a>
        <a href="javascript:;"><dt>老师</dt></a>
        <a href="javascript:;"><dt>管理学科</dt></a>
        <a href="javascript:;"><dt>管理老师</dt></a>
        <a href="javascript:;"><dt>管理校园</dt></a>
        <a href="javascript:;"><dt>管理拓展课</dt></a>
      </dl>
      <li id="pre">
        <i class="iconfont">&#xe663;</i>
        <span>群组交流</span>
      </li>
      <li id="pre">
        <i class="iconfont">&#xe6c3;</i>
        <span>投票选举</span>
      </li>
      <li id="pre">
        <i class="iconfont">&#x349b;</i>
        <span>调查问卷</span>
      </li>
      <li id="pre">
        <i class="iconfont">&#xe68a;</i>
        <span>火热活动</span>
      </li>
      <li id="pre">
        <i class="iconfont">&#xe65a;</i>
        <span>校园安全</span>
      </li>
    </ul>
    <!--/.left-nav左侧导航-->

    <!--.wcal-->
    <div class="left-cal" id="calId"></div>
    <!--/.wcal-->

    <!--.orange-col-->
    <div class="orange-col">
      <div class="col-head">
        <h3>同学排行</h3>
      </div>
      <ul class="col-main paihan">
        <li class="clearfix">
          <img src="http://placehold.it/45x45" />
          <span>xinxin</span>
          <em>经验值<i>238</i></em>
        </li>
        <li class="clearfix">
          <img src="http://placehold.it/45x45 " />
          <span>xinxin</span>
          <em>经验值<i>238</i></em>
        </li>
      </ul>
    </div>
    <!--/.orange-col-->

  </div>
  <!--/.col-left-->

  <!--.col-right-->
  <div class="col-right">

    <!-- 广告部分 -->
    <div class="banner-info clearfix">
      <div class="banner-info-left">
        <ul>
          <li>
            <a href="javascript:;">
              <img src="images/homepage-4.jpg"/>
            </a>
          </li>
          <li>
            <a href="javascript:;">
              <img src="images/homepage-10.jpg"/>
            </a>
          </li>
          <li>
            <a href="javascript:;">
              <img src="images/homepage-11.jpg"/>
            </a>
          </li>
          <li>
            <a href="javascript:;">
              <img src="images/homepage-12.jpg"/>
            </a>
          </li>
          <li>
            <a href="javascript:;">
              <img src="images/homepage-13.jpg"/>
            </a>
          </li>
        </ul>
        <ol>
          <li class="current">1</li>
          <li>2</li>
          <li>3</li>
          <li>4</li>
          <li>5</li>
        </ol>
      </div>
      <div class="banner-info-right">
        <a href="javascript:;">
          <img src="images/elephant.png" alt="">
        </a>
      </div>
    </div>
    <!-- 广告部分 -->
    <div class="gzgl-con">
      <div class="gzgl-nav clearfix">
        <ul>
          <li class="gzgl-active"><a href="javascript:;">薪酬管理</a><em></em></li>
          <li><a href="javascript:;">我的工资条</a><em></em></li>
          <li><a href="javascript:;">工资统计</a><em></em></li>
        </ul>
      </div>
      <div class="gzgl-main">
        <div class="gzgl-title clearfix">
          <p>&lt;返回薪酬管理</p>
          <span><em>＋</em>新增项目</span>
        </div>
        <div class="gzgl-table">
          <h2 class="gzgl-table-title">工资项目管理列表</h2>
          <table class="gzxmgl-table">
            <tr>
              <th style="width:25%;">序号</th>
              <th style="width:25%;">项目</th>
              <th style="width:25%;">类型</th>
              <th style="width:25%;">操作</th>
            </tr>

            <tr>
              <td style="background:#ececec;">1</td>
              <td>公积金</td>
              <td>发款</td>
              <td>
                <a class="gzxmgl-edit" href="javascript:;">编辑</a>
                <a class="gzxmgl-del" href="javascript:;">删除</a>
              </td>
            </tr>
            <tr>
              <td style="background:#ececec;">2</td>
              <td>课时费</td>
              <td>发款</td>
              <td>
                <a class="gzxmgl-edit" href="javascript:;">编辑</a>
                <a class="gzxmgl-del" href="javascript:;">删除</a>
              </td>
            </tr>
            <tr>
              <td style="background:#ececec;">3</td>
              <td>工伤险</td>
              <td>扣款</td>
              <td>
                <a class="gzxmgl-edit" href="javascript:;">编辑</a>
                <a class="gzxmgl-del" href="javascript:;">删除</a>
              </td>
            </tr>
            <tr>
              <td style="background:#ececec;">4</td>
              <td>失业险</td>
              <td>扣款</td>
              <td>
                <a class="gzxmgl-edit" href="javascript:;">编辑</a>
                <a class="gzxmgl-del" href="javascript:;">删除</a>
              </td>
            </tr>
            <tr>
              <td style="background:#ececec;">5</td>
              <td>养老险</td>
              <td>扣款</td>
              <td>
                <a class="gzxmgl-edit" href="javascript:;">编辑</a>
                <a class="gzxmgl-del" href="javascript:;">删除</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
    <div class="gzxmgl-alert">
      <div class="gzxmgl-alert-title clearfix">
        <p>工资项目编辑</p>
        <span class="gzxmgl-close">X</span>
      </div>
      <div class="gzxmgl-alert-main">
        <span>项目名称</span>
        <input type="text" placeholder="公积金" style="width:220px;">
        <br>
        <br>
        <span>类型</span>
        <label><input type="radio" name="gzgl-type">发款</label>
        <label><input type="radio" name="gzgl-type">扣款</label>

        <div class="gzxmgl-alert-btn">
          <button class="gzxmgl-sure">确定</button>
          <button class="gzxmgl-qx">取消</button>
        </div>
      </div>
    </div>

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
<div class="gzgl-meng"></div>
<!--#foot-->
<div id="foot">
  <div class="sub-foot clearfix">
    <p>版权所有：上海复兰信息科技有限公司 www.fulaan-tech.com 沪ICP备14004857号 </p>
    <ul>
      <li><a href="#">关于我们</a></li><li>|</li>
      <li><a href="#">联系我们</a></li><li>|</li>
      <li><a href="#">服务条款</a></li><li>|</li>
      <li><a href="#">隐私保护</a></li><li>|</li>
      <li><a href="#" class="qq-online"></a></li>
    </ul>
  </div>
</div>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="js/sea.js"></script>
<!-- Custom js -->
<script src="js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('expand');
</script>
</body>
</html>
