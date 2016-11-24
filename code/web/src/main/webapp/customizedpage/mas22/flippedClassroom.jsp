<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>复兰科技 K6KT-快乐课堂</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/customizedpage/mas22/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/customizedpage/mas22/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
		<script>
		$(function(){
			$('.item-tab').click(function(){
				$('.item-tab').removeClass('selected');
				$(this).addClass('selected');
				$('.subject-teacher').hide();
				var currentId = $(this).attr('id');
				$('#show-'+currentId).show();
			});
		});
		</script>
        <style>
        .item-tab{width:83px;*width:81px;text-align:center;height:35px;font-size:14px;line-height:33px;border:1px solid #d2d2d2;float:left;cursor: pointer;border-right:0;background:#f1f1f1;}
		.selected{border-top:4px solid #3aa21d;background:#fff;border-bottom:0;border-radius: 3px;}
		.subject-teacher{display:none;width:auto;min-height:76px;}
		.current-show{display:block;}
		.teachers-info {width:auto;padding-left: 0px;overflow:hidden;}
		.subject-teacher{margin-left:0;border-top:0;border-top-right-radius:0px;border-top-left-radius:0px;}
		.names-pre{font-size:14px;line-height:32px;padding: 12px 7px 12px 37px;width: 994px;*width:940px;}
        </style>
    </head>
    <body>
    <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
		<div id="intro-player">
			<div id="player_div"></div>
			<span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span></div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/customizedpage/mas22/mas22_logo.png">
                    <a class="login-btn" href="javascript:;">登 录</a>
                    <input class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'>忘记密码？</a>
                        <a class="username-error">用户名不存在</a>
                    </div>
                </div>
            </div>
            <div class="main-content-container">
                <div class='content-container'>
                    <img class="text-1" src="/img/K6KT/main-page/text-1.png" />
                    <img class="text-2" src="/img/K6KT/main-page/text-2.png" />

                    <a onclick="playMovie()">
                        <div class="player-hand">
                            <div></div>
                        </div>
                    </a>
                    <div class="teacher-apply" onclick="go2appuse()">
                        教师申请试用
                    </div>
                    <div class="app-link">
                        <img src="/img/K6KT/iphone.png" />
                        <div>
                            <div>手机客户端 APP</div>
                            <div>
                                <span><img src="/img/K6KT/ios.png" /></span>
                                <span>|</span>
                                <span><img src="/img/K6KT/android.png" /></span>
                                <span><a href="/mobile">点击下载 ></a></span>
                            </div>
                        </div>
                    </div>

                    <div class="monitor-div">
                        <div class="carousel nivoSlider" id='slider'>
                            <img src="/img/K6KT/main-page/screen-1.png"/>
                            <img src="/img/K6KT/main-page/screen-2.png"/>
                        </div>
                    </div>
                </div>
            </div>
            <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
                <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
            </div>
            <div class="shcool-container" style="display: none;">
	            <div class="shcool-about">
	            	<div>
						<ul>
	            		<li class="item-tab selected" id="sub1">高中语文</li>
	            		<li class="item-tab" id="sub2">高中数学</li>
	            		<li class="item-tab" id="sub3">高中英语</li>
	            		<li class="item-tab" id="sub4">高中物理</li>
	            		<li class="item-tab" id="sub5">高中化学</li>
	            		<li class="item-tab" id="sub6">高中生物</li>
	            		<li class="item-tab" id="sub7">高中政治</li>
	            		<li class="item-tab" id="sub8">高中历史</li>
	            		<li class="item-tab" id="sub9">高中地理</li>
	            		<li class="item-tab" id="sub10">信息通用</li>
	            		<li class="item-tab" id="sub11">体育健康</li>
	            		<li class="item-tab" id="sub12" style="border-right:1px solid #d2d2d2;">音乐美术</li>
						</ul>
	            	</div>
	            	<div class="teachers-info">
	            		<dl id="show-sub1" class="subject-teacher current-show">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/422">陈  静</a>    <a href="/masTeacher/358">丁  飞</a>    <a href="/masTeacher/417">董文静</a>    <a href="/masTeacher/483">黄晓明</a>    <a href="/masTeacher/359">蒋  莉</a>    <a href="/masTeacher/418">蒋学艳</a>    <a href="/masTeacher/357">林兴华</a>    <a href="/masTeacher/1435">刘爱和</a>    <a href="/masTeacher/478">倪泽燕</a>    <a href="/masTeacher/361">牛红梅</a>    <a href="/masTeacher/480">邵良红</a>    <a href="/masTeacher/481">宋云霞</a>    <a href="/masTeacher/415">汤许璐</a>
<a href="/masTeacher/356">陶华奖</a>    <a href="/masTeacher/416">魏志军</a>    <a href="/masTeacher/360">吴中杰</a>    <a href="/masTeacher/363">闫瑞侠</a>    <a href="/masTeacher/484">张  琴</a>    <a href="/masTeacher/362">张  卫</a>    <a href="/masTeacher/479">张尔钢</a>    <a href="/masTeacher/420">张炎平</a>    <a href="/masTeacher/421">章习友</a>    <a href="/masTeacher/482">邹  璞</a>    <a href="/masTeacher/419">祖先华</a></pre>
							</dd>
	            		</dl>
	            		<dl id="show-sub2" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/364">蔡丛林</a>    <a href="/masTeacher/491">陈  瑶</a>    <a href="/masTeacher/527">付红燕</a>    <a href="/masTeacher/370">顾  鹏</a>    <a href="/masTeacher/487">何  鸣</a>    <a href="/masTeacher/369">胡学平</a>    <a href="/masTeacher/429">华  斐</a>    江宏传    <a href="/masTeacher/371">蒋淮颖</a>    <a href="/masTeacher/492">金诚岩</a>    <a href="/masTeacher/427">刘宝明</a>    <a href="/masTeacher/431">刘国庆</a>    <a href="/masTeacher/490">刘义峰</a>
<a href="/masTeacher/367">钱阳开</a>    <a href="/masTeacher/430">沈  斌</a>    <a href="/masTeacher/423">孙  滨</a>    <a href="/masTeacher/373">汤  伟</a>    <a href="/masTeacher/365">汪  冰</a>    <a href="/masTeacher/485">王  辉</a>    <a href="/masTeacher/428">王露露</a>    <a href="/masTeacher/486">王荣善</a>    <a href="/masTeacher/366">王引德</a>    <a href="/masTeacher/488">吴太龙</a>    <a href="/masTeacher/425">吴宜平</a>    <a href="/masTeacher/368">徐晓蓉</a>    <a href="/masTeacher/426">杨可友</a>
<a href="/masTeacher/489">于  楷</a>    <a href="/masTeacher/372">俞含林</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub3" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/496">蔡  瑞</a>    <a href="/masTeacher/377">陈  胜</a>    <a href="/masTeacher/375">陈贵萍</a>    <a href="/masTeacher/499">程  娟</a>    段  莹    <a href="/masTeacher/493">郭  庆</a>    <a href="/masTeacher/500">胡邦英</a>    <a href="/masTeacher/438">季  锋</a>    <a href="/masTeacher/374">李淑玫</a>    <a href="/masTeacher/435">娄珊珊</a>    <a href="/masTeacher/497">鲁  露</a>    <a href="/masTeacher/439">马文莉</a>    <a href="/masTeacher/378">邵承龙</a>
<a href="/masTeacher/381">汪建军</a>    王  慧    <a href="/masTeacher/376">王传云</a>    <a href="/masTeacher/432">王世芳</a>    <a href="/masTeacher/495">吴  强</a>    <a href="/masTeacher/437">徐  月</a>    <a href="/masTeacher/379">徐莹侠</a>    <a href="/masTeacher/436">薛  况</a>    <a href="/masTeacher/498">杨  军</a>    <a href="/masTeacher/380">杨益民</a>    <a href="/masTeacher/494">章若华</a>    <a href="/masTeacher/433">赵培辰</a>    <a href="/masTeacher/434">朱  栎</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub4" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/502">陈玉和</a>    <a href="/masTeacher/445">高  飞</a>    <a href="/masTeacher/384">金  靖</a>    <a href="/masTeacher/443">赖永宏</a>    <a href="/masTeacher/441">李国凤</a>    <a href="/masTeacher/444">彭山忠</a>    <a href="/masTeacher/442">汪忠淼</a>    <a href="/masTeacher/501">王  进</a>    <a href="/masTeacher/503">吴  莎</a>    <a href="/masTeacher/504">吴慧峰</a>    <a href="/masTeacher/386">谢霖菁</a>    <a href="/masTeacher/382">杨炳康</a>    <a href="/masTeacher/440">张  虹</a>
<a href="/masTeacher/385">张  力</a>    <a href="/masTeacher/383">张健宝</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub5" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/507">顾  巍</a>    <a href="/masTeacher/91">后勇军</a>    <a href="/masTeacher/448">黄  芳</a>    <a href="/masTeacher/508">李  琳</a>    <a href="/masTeacher/447">李  旭</a>    <a href="/masTeacher/389">彭粹明</a>    <a href="/masTeacher/450">钱广玉</a>    <a href="/masTeacher/506">钱其保</a>    <a href="/masTeacher/505">石  焱</a>    <a href="/masTeacher/452">王  瑶</a>    <a href="/masTeacher/449">曾加南</a>    <a href="/masTeacher/451">张德海</a>    <a href="/masTeacher/509">张永群</a>
<a href="/masTeacher/387">郑贤平</a>    郑永报    <a href="/masTeacher/390">周春花</a>    <a href="/masTeacher/388">周美华</a></pre>
                        </dd>
	            		</dl>
	            		<dl id="show-sub6" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/391">胡明静</a>    <a href="/masTeacher/454">姜文芳</a>    <a href="/masTeacher/512">阚晓彤</a>    <a href="/masTeacher/392">李  蓓</a>    <a href="/masTeacher/393">李晓庆</a>    <a href="/masTeacher/511">孙东林</a>    <a href="/masTeacher/453">汪  丽</a>    <a href="/masTeacher/455">吴冬霞</a>    <a href="/masTeacher/456">吴潇楚</a>    <a href="/masTeacher/510">姚卫国</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub7" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre" ><a href="/masTeacher/514">陈  云</a>    <a href="/masTeacher/513">董  成</a>    <a href="/masTeacher/459">刘  焱</a>    <a href="/masTeacher/395">聂  焱</a>    <a href="/masTeacher/515">陶  俊</a>    <a href="/masTeacher/394">王  亮</a>    <a href="/masTeacher/458">王庆发</a>    <a href="/masTeacher/397">吴成宏</a>    <a href="/masTeacher/457">夏海波</a>    <a href="/masTeacher/460">薛广林</a>    <a href="/masTeacher/396">张  秀</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub8" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre" ><a href="/masTeacher/399">崔序芳</a>    <a href="/masTeacher/400">李  宏</a>    <a href="/masTeacher/517">梁晓星</a>    <a href="/masTeacher/398">刘和洪</a>    <a href="/masTeacher/462">鹿  秀</a>    <a href="/masTeacher/463">王  茜</a>    <a href="/masTeacher/464">徐仁龙</a>    <a href="/masTeacher/518">张  昕</a>    <a href="/masTeacher/516">张为学</a>    <a href="/masTeacher/461">周桃正</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub9" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/502">曹  捷</a>    <a href="/masTeacher/520">程东生</a>    <a href="/masTeacher/467">董圣亮</a>    <a href="/masTeacher/522">范章婷</a>    <a href="/masTeacher/519">刘  娟</a>    <a href="/masTeacher/465">吕红叶</a>    <a href="/masTeacher/468">钱民民</a>    <a href="/masTeacher/521">任  玲</a>    <a href="/masTeacher/466">王  舜</a>    <a href="/masTeacher/403">许全香</a>   <a href="/masTeacher/401">杨谋明</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub10" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/474">陈梦琳</a>    <a href="/masTeacher/475">刘  洋</a>    <a href="/masTeacher/411">邱承进</a>    <a href="/masTeacher/412">沈宏松</a>    <a href="/masTeacher/476">汪宗荣</a>    <a href="/masTeacher/477">袁时煌</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub11" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/524">陈国宏</a>    <a href="/masTeacher/408">丁方银</a>    <a href="/masTeacher/472">丁少宏</a>    <a href="/masTeacher/414">高琦璐</a>    <a href="/masTeacher/406">黄明虎</a>    <a href="/masTeacher/407">刘新继</a>    <a href="/masTeacher/471">陆克宏</a>    <a href="/masTeacher/525">宋洪涛</a>    <a href="/masTeacher/413">王  忠</a>    <a href="/masTeacher/523">徐少宏</a>    <a href="/masTeacher/526">袁  新</a>    <a href="/masTeacher/470">张  洁</a></pre>
	            			</dd>
	            		</dl>
	            		<dl id="show-sub12" class="subject-teacher">
	            			<dd class="teacher-name">
	            				<pre class="names-pre"><a href="/masTeacher/410">高宏修</a>    <a href="/masTeacher/410">李善忠</a>    <a href="/masTeacher/409">唐  云</a>    <a href="/masTeacher/404">王  珺</a>    <a href="/masTeacher/469">姚旭峰</a>    <a href="/masTeacher/473">仲  磊</a></pre>
	            			</dd>
	            		</dl>
	            	</div>
	            </div>
	         </div>
        </div>
        <!-- 页尾 -->
    <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
    <script>
        function go2appuse(){
            window.location.href="/customizedpage/application.jsp";
        }
    </script>
</html>