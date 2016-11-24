<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>济南教育局智慧校园</title>
	<link rel="stylesheet" type="text/css" href="/customizedpage/jinan/style/style.css"/>
	<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
   
    <script type="text/javascript" src="/customizedpage/jinan/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/jinan/js/indexjq.js"></script>
    <script type="text/javascript" src="/customizedpage/jinan/js/iepng.js"></script>
	<!--[if IE 6]>
		<script src="js/iepng.js" type="text/javascript"></script>	
		<script type="text/javascript">
		   EvPNG.fix('div,ul,img,li,input,span,b,h1,h2,h3,h4,a');
		</script>

   <![endif]-->
    <script type="text/javascript">
   $(function(){
   	var name = '${session.currentUser.userName}';
   	
   	if (name==null || name=='') {
   		name = 'test';
   	}
    $('.menu').on('click',function () {
    	window.open('http://121.40.203.77/demo/demo1.jsp?action=create&username=' + name);  
    });
   });
   </script>
</head>
<body>
<!-- top -->
<div class="topbox">
	<div class="top">
		<div class="logo">
			<h1>
				<a href="javascript:void(0);">公司的名字或广告语写在这里</a>
			</h1>
		</div>
		<div class="toplink" style="display:none;">
			<a><span class="menu">微辅导</span></a>
			<span class="search"></span>
			<span class="land"><a href="/">登陆</a></span>
			<span class="register"><a href="/">注册</a></span>
			
		</div>
	</div>
</div>                 
<!-- banner -->
<div class="index-bannerbox">
	<div id="qie">
			<ul id="index-banner">
				<li><img src="/customizedpage/jinan/images/jpg-2.jpg"/></li>
				<li><img src="/customizedpage/jinan/images/jpg-3.jpg"/></li>
				<li><img src="/customizedpage/jinan/images/jpg-4.jpg"/></li>
				<li><img src="/customizedpage/jinan/images/jpg-2.jpg"/></li>
				<li><img src="/customizedpage/jinan/images/jpg-3.jpg"/></li>
			</ul>
		    <a href="javascript:" class="left"></a>
		    <a href="javascript:" class="right"></a>
	</div>			
</div>
<div class="bg-line"></div>
<!-- news-->
<div class="index-newsbox">
	<div class="newsleft">
		<div class="newsleft-top">
			<div class="newsleft-top-dl">
					<dl >
						<dt><img src="/customizedpage/jinan/images/jinan_GK.jpg" /></dt>
						<dd>
								<h5>孙伟常务副省长察看济南高考考点</h5>
								<p>
                                    6月7日上午，省委常委、常务副省长、省招生考试委员会主任孙伟到济南市第一中学考点察看高考工作。孙伟仔细察看了考点外围保障、电子监控中心和考场等情况，听取了全省及济南市高考有关工作汇报，代表省委、省政府向全省广大考务工作者表示亲切问候，向广大考生和家长表达了美好祝愿。他强调，高考关系到千家万户的切身利益，关系到整个社会的和谐稳定。各级各有关部门、单位要紧密围绕“平安高考”目标，再接再厉做好今年的高考招生工作。要在思想上.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
								
						</dd>
				    </dl>
				    <%--<dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic02.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>
				    <dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic03.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>
				    <dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic04.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>
				    <dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic05.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>
				    <dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic06.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>
				    <dl >
						<dt><img src="/business/ypxx/special/jinan/images/pic07.jpg" /></dt>
						<dd>
								<h5>省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</h5>
								<p>
									会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
								</p>
								<a class="btn-more" href="javascript:void(0);"></a>
						</dd>
				    </dl>--%>
			</div>
			<div class="btnnum">
					<span class="btn1"></span>
					<%--<span class="btn2"></span>
					<span class="btn3"></span>
					<span class="btn4"></span>
					<span class="btn5"></span>
					<span class="btn6"></span>
					<span class="btn7"></span>--%>
			</div>
		</div>

		<div class="newsleft-bottom">
			
            <a style="cursor: pointer" onclick="$('#login-bg').fadeIn('fast')"><img src="/customizedpage/jinan/images/jinan_44.png" width="233" height="99" /></a>
            <a href="http://42.121.252.128/"><img src="/customizedpage/jinan/images/jinan_55.png" width="233" height="99" /> </a>
            <a href="http://p.jintizi.com/35%E6%95%99%E7%A0%94%E5%B9%B3%E5%8F%B0-%E9%AB%98%E4%BF%9D%E7%9C%9Fv/#p=首页_未登录_"><img src="/customizedpage/jinan/images/jinan_66.png" width="233" height="99" /></a>
        </div>
	</div>
	<div class="newsright">
		<div class="view"><div class="wsp"><ul>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p> </li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
		</ul>
        <ul>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
		</ul>
        <ul>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
		</ul>
        <ul>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li  class="singular "><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
			<li ><img src="/customizedpage/jinan/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
		</ul></div></div>

	</div>
</div>

<div id="login-bg">
	<div id="login">
		<div id="logincontent">
			<form action="http://www.lqtedu.com/outsite/login.jsp">
				<div class="line" style="line-height:30px; ">
					<div><img src="/img/loginicon.png" /></div>
					<div style="color:rgb(51, 173, 225); font-weight:bold; font-size:14px; margin-left: 10px">登录</div>
					<i class='fa fa-times fa-lg' style="cursor:pointer; float: right; color:rgb(51, 173, 225); line-height: 30px" onclick="$('#login-bg').fadeOut('fast')"></i>
				</div>
				<div class="line">
					<input type="text" placeholder="登录名/身份证号/学籍号" class="username" name="UNAME" />
				</div>
				<div class="line">
					<input type="password" placeholder="密码" class="password" name="UPWD"/>
				</div>
				<div class="line" style="line-height:30px;">
					<input type="submit" value=" 登 录 ">
				</div>
			</form>
		</div>
	</div>
</div>

<!-- footer -->
<div class="bg-line"></div>
<div class="footerbox">
	<div class="footer">
		<div class="host">
			<span>主办：济南教育局 </span><span>技术支持：上海复兰信息科技有限公司</span><span>客服热线：400 820 6735</span>         
		</div>
		<div class="footerlink">
			  <a href="javascript:void(0);">后台管理</a>|
			  <a href="javascript:void(0);">站长中心</a>|
			  <a href="javascript:void(0);">帮助中心</a>
		</div>
	</div>
</div>

	
</body>
</html>