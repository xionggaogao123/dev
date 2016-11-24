<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/reset.css" rel="stylesheet" />
    <!-- jquery ui styles -->
    <link href="/static/css/jquery-ui.min.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="/static/css/jiangcheng/jiangcheng.css?v=2015071201" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>

    <!--#head-->
    <%@ include file="../common_new/head.jsp" %>
    
 
    <!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">
        <!-- 录课神器 -->
        <div class="movie">
        <img src="images/movie.png" height="26" width="43" alt="">
                            录课神器
        </div>
        <!-- 录课神器 -->

        <!--.col-left-->
        <div class="col-left">

            <!--.user-info-->
            <div class="user-info">
                  <img src="${sessionValue.midAvatar}" />
                  <em>${sessionValue.userName}111</em>
        <span>经验值${sessionValue.experience}</span>
            </div>
            <!--/.user-info-->
            <!--.left-nav左侧导航-->
            <%@ include file="../common_new/col-left.jsp" %>
            <!--/.left-nav左侧导航-->
            <!--.wcal-->
            <div class="left-cal" id="calId"></div>
            <!--/.wcal-->


        </div>
        <!--/.col-left-->

        <!--.col-right-->
        <div class="col-right">

            <!--.banner-info-->
            <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
            <!--/.banner-info-->

            <!--.txt-info-->
            <div class="txt-info">
                <div class="txt-head">
                    <h3>学生奖惩记录</h3>
                </div>
                <div class="jiangcheng-list">

                    <dl>
                        <dt>查询</dt>
                        <dd class="clearfix">
                            <form>
                            <label>
                                学期
                                <select>
                                    <option value="2014第一学期">2014第一学期</option>
                                    <option value="2014第二学期">2014第二学期</option>
                                </select>
                            </label>
                            <label>
                                年级
                                <select>
                                    <option value="初一">初一</option>
                                    <option value="初二">初二</option>
                                </select>
                            </label>
                            <label>
                                班级
                                <select>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                </select>
                            </label>
                            <label>
                                类型
                                <select>
                                    <option value="表彰奖励">表彰奖励</option>
                                    <option value="违纪违规">违纪违规</option>
                                </select>
                            </label>
                            <label>
                                等级
                                <select>
                                    <option value="一等奖">一等奖</option>
                                    <option value="二等奖">二等奖</option>
                                    <option value="三等奖">三等奖</option>
                                    <option value="特等奖">特等奖</option>
                                    <option value="口头警告">口头警告</option>
                                    <option value="记过">记过</option>
                                    <option value="通报批评">通报批评</option>
                                </select>
                            </label>
                            <label>学生姓名<input type="text" /></label>
                            <button type="button" class="green-btn">查询</button>
                            </form>
                        </dd>
                    </dl>

                    <div class="jiangcheng-table">
                        <h4>列表</h4>
                        <div class="btn-list">
                            <button class="green-btn">添加</button>
                            <button class="btn">导出</button>
                        </div>
                        <table>
                            <thead>
                                <th width="40">#</th>
                                <th width="80">姓名</th>
                                <th width="80">年级</th>
                                <th width="60">班级</th>
                                <th width="90">类型</th>
                                <th width="90">等级</th>
                                <th width="80">内容</th>
                                <th width="110">奖惩日期</th>
                                <th>操作</th>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>


                </div>
            </div>
            <!--/.txt-info-->



        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

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
    <!--#foot-->


    <div class="pop-wrap" id="editJiangCheng">
        <div class="pop-title">编辑奖惩记录</div>
        <div class="pop-content">
            <div class="pop-list clearfix">
                <dl class="item">
                    <dt>姓名</dt>
                    <dd>2015第二学期</dd>
                </dl>
                <dl class="item">
                    <dt>年级</dt>
                    <dd>初一</dd>
                </dl>
                <dl class="item">
                    <dt>班级</dt>
                    <dd>1</dd>
                </dl>
            </div>
            <div class="pop-list clearfix">
                <dl class="item">
                    <dt>类型</dt>
                    <dd >
                    <select class="item-input">
                        <option value ="表彰奖励">表彰奖励</option>
                        <option value ="表彰奖励1">表彰奖励1</option>
                        <option value="表彰奖励2">表彰奖励2</option>
                        <option value="表彰奖励3">表彰奖励3</option>
                    </select> 
                    </dd>
                </dl>
                <dl class="item">
                    <dt>日期</dt>
                    <dd>
                    <select class="item-input">
                    </select> </dd>
                </dl>
                <dl class="item">
                    <dt>等级</dt>
                    <dd>
                        <select class="item-input">
                        <option value ="二等奖">二等奖</option>
                        <option value ="一等奖">一等奖</option>
                        <option value="三等奖">三等奖</option>
                        <option value="四等奖">四等奖</option>
                    </select> 
                    </dd>
                </dl>
            </div>
            <div class="pop-list clearfix">
                <dl class="enter-content">
                    <dt>内容</dt>
                    <dd><textarea></textarea></dd>
                </dl>
            </div>
            <div class="pop-fanwei">
                <h5>公示范围</h5>
                <dl class="fanwei-list clearfix">
                    <dt>部门</dt>
                    <dd>
                        <div class="single"><label><input type="checkbox">全校</span></div>
                        <div class="single">
                            <label><input type="checkbox">所有部门</label>
                            <label><input type="checkbox">教务处</label>
                            <label><input type="checkbox">教导处</label>
                            <label><input type="checkbox">校办</label>
                            <label><input type="checkbox">教务处</label>
                            <label><input type="checkbox">团委</label>
                            <label><input type="checkbox">其他</label>
                        </div>
                    </dd>
                </dl>
                <dl class="fanwei-list clearfix">
                    <dt>班级</dt>
                    <dd>
                        <div class="all"><input type="checkbox">所有班级</div>
                        <div class="single">
                            <label><input type="checkbox">初一</label>
                            <label><input type="checkbox">一班</label>
                            <label><input type="checkbox">二班</label>
                            <label><input type="checkbox">三班</label>
                            <label><input type="checkbox">四班</label>
                        </div>
                        <div class="single">
                            <label><input type="checkbox">初二</label>
                            <label><input type="checkbox">一班</label>
                            <label><input type="checkbox">二班</label>
                            <label><input type="checkbox">三班</label>
                            <label><input type="checkbox">四班</label>
                            <label><input type="checkbox">五班</label>
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
        <div class="pop-btn"><span class="active">确定</span><span>取消</span></div>
    </div>

    <div class="pop-wrap" id="jiLuJiangCheng">
        <div class="pop-title">记录详情</div>
        <div class="pop-content">

        </div>
        <div class="pop-btn"><span>关闭</span></div>
    </div>

    <div class="pop-wrap" id="addJiangCheng">
        <div class="pop-title">添加奖惩记录</div>
        <div class="pop-content">
            <div class="pop-list clearfix">
                <dl class="item">
                    <dt>年级</dt>
                    <dd >
                        <select class="item-input">
                            <option value="初一">初一</option>
                            <option value="初二">初二</option>
                        </select>
                    </dd>
                </dl>
                <dl class="item">
                    <dt>班级</dt>
                    <dd >
                        <select class="item-input">
                            <option value="1">1</option>
                            <option value="2">2</option>
                        </select>
                    </dd>
                </dl>
                <dl class="item">
                    <dt>类型</dt>
                    <dd >
                        <select class="item-input">
                            <option value="表彰奖励">表彰奖励</option>
                            <option value="违纪违规">违纪违规</option>
                        </select>
                    </dd>
                </dl>
            </div>
            <div class="pop-list clearfix">
                <dl class="item">
                    <dt>等级</dt>
                    <dd>
                        <select class="item-input">
                            <option value="一等奖">一等奖</option>
                            <option value="二等奖">二等奖</option>
                            <option value="三等奖">三等奖</option>
                            <option value="特等奖">特等奖</option>
                            <option value="口头警告">口头警告</option>
                            <option value="记过">记过</option>
                            <option value="通报批评">通报批评</option>
                        </select>
                    </dd>
                </dl>
                 <dl class="item">
                    <dt>日期</dt>
                    <dd>
                        <input type="text" id="wdate" />
                    </dd>
                </dl>
            </div>
            <div class="xuan-student">
                <div class="student-title clearfix">
                    <span>选择学生</span>
                    <label><input type="checkbox">全选</label>
                </div>
                <div class="student-box clearfix">


                </div>
            </div>
            <div class="pop-list clearfix">
                <dl class="enter-content">
                    <dt>内容</dt>
                    <dd><textarea></textarea></dd>
                </dl>
            </div>
            <div class="pop-fanwei">
                <h5>公示范围</h5>
                <dl class="fanwei-list clearfix">
                    <dt>部门</dt>
                    <dd>
                        <div class="single"><label><input type="checkbox">全校</label></div>
                        <div class="single">
                            <label><input type="checkbox" value="所有部门">所有部门</label>
                            <label><input type="checkbox" value="教务处">教务处</label>
                            <label><input type="checkbox" value="德育处">德育处</label>
                            <label><input type="checkbox" value="教导处">教导处</label>
                            <label><input type="checkbox" value="其他">其他</label>
                            <label><input type="checkbox" value="保卫处">保卫处</label>
                            <label><input type="checkbox" value="校办">校办</label>
                            <label><input type="checkbox" value="学工处">学工处</label>
                        </div>
                    </dd>
                </dl>
                <dl class="fanwei-list clearfix">
                    <dt>班级</dt>
                    <dd>
                        <div class="all"><label><input type="checkbox">所有班级</label></div>
                        <div class="single">
                            <label><input type="checkbox" value="初一">初一</label>
                            <label><input type="checkbox" value="一班">一班</label>
                            <label><input type="checkbox" value="二班">二班</label>
                        </div>
                        <div class="single">
                            <label><input type="checkbox" value="初二">初二</label>
                            <label><input type="checkbox" value="一班">一班</label>
                            <label><input type="checkbox" value="二班">二班</label>
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
        <div class="pop-btn"><span class="active">确定</span><span>取消</span></div>
    </div>

    <div class="bg-dialog"></div>

    <script type="text/template" id="tiaojianId">
        {{~it:value:index}}
        <tr>
            <td>{{=index+1}}</td>
            <td>{{=value.name}}</td>
            <td>{{=value.nianji}}</td>
            <td>{{=value.banji}}</td>
            <td>{{=value.type}}</td>
            <td>{{=value.dengji}}</td>
            <td><a href="#" class="view" data-name="{{=value.name}}">查看</a></td>
            <td>{{=value.date}}</td>
            <td><a href="#" class="jc-edit" data-name="{{=value.name}}"></a>|<a href="#" class="jc-del" data-name="{{=value.name}}"></a></td>
        </tr>
        {{~}}
    </script>

    <script type="text/template" id="viewListId">
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>姓名</dt>
                <dd class="item-input">{{=it.name}}</dd>
            </dl>
            <dl class="item">
                <dt>年级</dt>
                <dd class="item-input">{{=it.nianji}}</dd>
            </dl>
            <dl class="item">
                <dt>班级</dt>
                <dd class="item-input">{{=it.banji}}</dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>类型</dt>
                <dd class="item-input">{{=it.type}}</dd>
            </dl>
            <dl class="item">
                <dt>等级</dt>
                <dd class="item-input">{{=it.dengji}}</dd>
            </dl>
            <dl class="item">
                <dt>日期</dt>
                <dd class="item-input">{{=it.date}}</dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="enter-content">
                <dt>内容</dt>
                <dd><textarea readonly>{{=it.contents}}</textarea></dd>
            </dl>
        </div>
    </script>

    <script type="text/template" id="addListId">
        {{~it:value:index}}
        <label><input type="checkbox" value="{{=value.name}}">{{=value.name}} </label>
        {{~}}

    </script>

    <script type="text/template" id="editListId">

        <label><input type="checkbox">韩梅梅 </label>

    </script>



    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('jiangcheng');
    </script>
</body>
</html>