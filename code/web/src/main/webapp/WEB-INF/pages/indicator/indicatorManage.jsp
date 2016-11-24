<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-学生评价系统</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
	<link href="/static_new/css/indicator/indicator.css?v=2016102001" rel="stylesheet" />
	<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

<!--.col-left-->
<%@ include file="../common_new/col-left.jsp" %>
<!--/.col-left-->

<!--.col-right-->
<div class="col-right">

    <!--.banner-info-->
    <!--  
    <img src="http://placehold.it/770x100" class="banner-info" />
    -->
    <!--/.banner-info-->

    <div class="grow-col">
		<!--.tab-col右侧-->
		<div class="tab-col">
			<div class="grow-col-head clearfix">
				<h3 class="grow-col-stu">学生评价系统</h3><span class="go-back">&lt;返回</span>
				<input id="treeId" type="hidden" value="${dto.id}">
			</div>
			<div class="itree-info">
				<span>指标体系名称:</span><em>${dto.name}</em><span>创建时间:</span><em>${dto.createDate}</em>
				<br>
				<span>指标体系说明:</span><br>
				<p>
					${dto.describe}
				</p>
			</div>
			<div class="main" id="main1">
				<c:if test="${dto.isHandle}">
                <div class="tab-main-right">
					<span>新建指标</span>
                </div>
				</c:if>
				<ul class="dl-bg"></ul>
				<script type="text/template" id="j-tmpl">
					{{ if(it.message.length>0){ }}
					{{ for (var i = 0, l = it.message.length; i < l; i++) { }}
					{{var obj=it.message[i];}}
					{{ if(obj.level==1){}}
					<li class="dl-f" edit="0" >
					{{ } }}
						<dl>
							<dt tid="{{=obj.id}}" zid="{{=obj.zid}}" level="{{=obj.level}}" type="{{=obj.type}}" isOpen="0" title="{{=obj.name}}">
								{{ if(obj.level<6){}}
									<em class="em-jia"></em>
									<input readonly="readonly"  placeholder="{{=obj.name}}" value="{{=obj.name}}"/>
									{{ if(obj.isHandle){}}
										<label class="lab-add">添加子指标</label>
									{{ } }}
								{{ }else{ }}
									<input readonly="readonly"  placeholder="{{=obj.name}}" value="{{=obj.name}}"/>
								{{ } }}
								{{ if(obj.isHandle){}}
									{{ if(obj.level<6){}}
										<label>|</label>
									{{ } }}
									<label class="lab-del">删除</label><label>|</label><label class="lab-rel">编辑</label>
								{{}}}
								<%--<img class="sel-edit sel-edit-f" width="22" height="22" src="/static_new/images/indicator/del-edit.jpg">--%>
								<dl id="{{=obj.id}}_dl" class="dl-sel dl-none">
								</dl>
							</dt>
						</dl>
						<%--<dt tid="{{=obj.id}}" level="{{=obj.level}}" type="{{=obj.type}}">
							<input readonly="readonly" class="dl-inp" placeholder="{{=obj.name}}"/>
							{{if(obj.isHandle){ }}
							<label class="lab-dell">X</label>
							<img class="sel-edit sel-edit-f" width="22" height="22" src="/static_new/images/indicator/del-edit.jpg">
							{{}}}
						</dt>--%>
					{{ if(obj.level==1){}}
						<%--<div class="li-right">
							<img class="sel-edit" width="22" height="22" src="/static_new/images/indicator/del-edit.jpg">
						</div>--%>
					</li>
					{{ } }}
					{{}}}
					{{}}}
				</script>
			</div>
			<%--<c:if test="${dto.isHandle}">
				<div class="itree-com">
					<div class="itree-com-bo">
						<span class="com-BC">保存</span>
					</div>
				</div>
			</c:if>--%>
		</div>
	</div>
<!--/.col-right-->

</div>
<!--/#content-->
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %> 
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
	seajs.use('indicator',function(indicator){
		indicator.init();
	});

</script>
</body>
</html>