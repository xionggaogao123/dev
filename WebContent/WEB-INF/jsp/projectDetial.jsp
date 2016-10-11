<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
    <head>
    	<title>我的项目清单</title>
        <meta charset="utf-8">
        <link rel="shortcut icon" href="${ctx }/images/page_logo1.png" >
        <script type="text/javascript" src="${ctx }/js/jquery-2.0.3.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
        <script type="text/javascript">
        	var parentDirId = ${project.docsPath};
        	var projectRootDir = ${project.docsPath};
        	var rootArray = [${project.docsPath}];
        	
        	function renameFile(id) {
    			var newName = prompt("请输入新文件名");
    			if(!newName) {
    				alert("请输入新文件名称");
    			} else {
    				$.ajax({
						async : false,
						cache : false,
						dataType : 'json',
						type : 'post',
						url : '${ctx}' + '/file/rename',
						data : {
							fileId : id,
							name : newName
						},
						success : function(data) {
							if(data.code === 0) { // 创建成功
								refushDir(); // 刷新
							}
							
							if(data.code === 1) {
								alert("出错了" + data.msg);
							}
						},
						error : function(arg0, arg1, arg2) {
							alert("出错了");
						}
    				});
    			}
    		}
        	
        	function renameDir(id) {
    			var newName = prompt("请输入新文件夹名");
    			if(!newName) {
    				alert("请输入新文件夹名称");
    			} else {
    				$.ajax({
						async : false,
						cache : false,
						dataType : 'json',
						type : 'post',
						url : '${ctx}' + '/dir/rename',
						data : {
							id : id,
							rename : newName
						},
						success : function(data) {
							if(data.code === 0) { // 创建成功
								refushDir(); // 刷新
							}
							
							if(data.code === 1) {
								alert("出错了" + data.msg);
							}
						},
						error : function(arg0, arg1, arg2) {
							alert("出错了");
						}
    				});
    			}
    		}
        	
        	function showDir(id) {
        		parentDirId = id;
        		rootArray.push(id);
        		console.log(rootArray);
        		refushDir();
        		if(rootArray.length > 1) {
        			$('#up_btn').removeAttr('disabled');
        			return;
        		}
        	}
        	
        	function showUpLevelDir() { // 返回上一级
        		if(rootArray.length === 1) {
        			$('#up_btn').attr('disabled', true);
        			return;
        		}
        		var upLevelId = rootArray.pop();
        		parentDirId = rootArray[rootArray.length - 1];
        		console.log(parentDirId);
        		refushDir();
        	}
        	
        	function removeFile(id) {
        		var toRemove = confirm("确认删除!");
        		if(toRemove) {
        			$.ajax({
						async : false,
						cache : false,
						dataType : 'json',
						type : 'post',
						url : '${ctx}' + '/file/remove',
						data : {
							fileId : id,
						},
						success : function(data) {
							if(data.code === 0) { // 删除成功
								refushDir(); // 刷新
							}
							
							if(data.code === 1) {
								alert("出错了：" + data.msg);
							}
						},
						error : function(arg0, arg1, arg2) {
							alert("出错了");
						}
    				});
        		}
        	}
        	
        	function removeDir(id) {
        		var toRemove = confirm("确认删除!");
        		if(toRemove) {
        			$.ajax({
						async : false,
						cache : false,
						dataType : 'json',
						type : 'post',
						url : '${ctx}' + '/dir/remove',
						data : {
							id : id,
						},
						success : function(data) {
							if(data.code === 0) { // 删除成功
								refushDir(); // 刷新
							}
							
							if(data.code === 1) {
								alert("出错了" + data.msg);
							}
						},
						error : function(arg0, arg1, arg2) {
							alert("出错了");
						}
    				});
        		}
        	}
        	
        	function refushDir() {
        		console.log("refushDIr");
    			$.ajax({
					async : false,
					cache : false,
					dataType : 'json',
					type : 'post',
					url : '${ctx}' + '/dir/refush',
					data : {
						parentDirId : parentDirId
					},
					success : function(data) {
						if(data.code === 0) { // 刷新成功
							$('#dir_list').empty();
							for(var i = 0; i < data.childDir.length; i++) {
								var html = '<li><label onclick="showDir(' + data.childDir[i].id + ')"><img src="${ctx }/images/img_floder.png">';
								html += '<span>' + data.childDir[i].name + '</span>';
								html += '</label><em onclick="renameDir(' + data.childDir[i].id + ')">重命名</em><em onclick="removeDir(' + data.childDir[i].id + ')">删除</em></li>';
								$('#dir_list').append(html);
							}
							
							for(var i = 0; i < data.files.length; i++) {
								var html = '<li><label><img src="${ctx }/images/img_word.png">';
								html += '<span>' + data.files[i].name + '</span>';
								html += '</label><em onclick="window.location.href=\'${ctx}/file/download/' + data.files[i].id +'\'">下载</em><em onclick="renameFile(' + data.files[i].id + ')">重命名</em><em onclick="removeFile(' + data.files[i].id + ')">删除</em></li>';
								$('#dir_list').append(html);
							}
                        		
						}
					},
					error : function(arg0, arg1, arg2) {
						alert("出错了");
					}
				});
    		}
        	
        	
        	$(function() {
        		
        		function select() {
        			console.log("----");
        			$('#f_btn').click();
        		}
        		
        		$('.wind .p1 em,.wind .btn-no').click(function(){
        			$('.wind').fadeOut();
        			$('.bg').fadeOut();
        		});
        		
        		function boxOut() {
        			$('.wind').fadeOut();
        			$('.bg').fadeOut();
        		}
        		
        		$('.dt3 .sp2').click(function() {
        			$('.wind-del').fadeIn();
        			$('.bg').fadeIn();
        		});
        		
        		$('#uploadBtn').click(function() {
        			$('#upload_form input[name=dirID]').val(parentDirId);
        			var formData = new FormData($('#upload_form')[0]);
        			$.ajax({
        				async : false,
        				cache : false,
        				data : formData,
        				type : 'post',
        				dataType : 'json',
        				url : '${ctx}' + '/file/upload',
        				contentType : false,
        				processData : false,
        				success : function(data) {
        					if(data.code === 0) {
        						boxOut();
        						alert("上传成功");
        						refushDir(); // 刷新
        					}
        					if(data.code === 1) {
        						boxOut();
        						alert("上传失败：" + data.msg);
        					}
        				},
        				error : function(arg0, arg1, arg2) {
        					boxOut();
        					alert("出错了");
        				}
        			});
        			$('#f_btn').val("");
        		});
        		
        		$("#add-dir").click(function() {
        			var dirName = prompt("请输入文件夹名","新建文件夹");
        			console.log("new dir name:" + dirName);
        			if(!dirName) {
        				alert("请输入文件夹名称");
        			} else {
        				$.ajax({
							async : false,
							cache : false,
							dataType : 'json',
							type : 'post',
							url : '${ctx}' + '/dir/add',
							data : {
								pid : parentDirId,
								dirName : dirName
							},
							success : function(data) {
								if(data.code === 0) { // 创建成功
									refushDir(); // 刷新
								}
								
								if(data.code === 1) {
									alert("出错了" + data.msg);
								}
							},
							error : function(arg0, arg1, arg2) {
								alert("出错了");
							}
        				});
        			}
        		});
        		
        	});
        </script>
    </head>
    <body>
    	<div class="header">
    		<button onclick="window.location.href='${ctx}/user/logout'">退出</button>
    		<p class="p2">${loginedStaff.name }&nbsp;&nbsp;&nbsp;${loginedStaff.jobTitle }<br>${loginedStaff.loginName }</p>
    		<img class="img2" src="${ctx }/images/img_top_head.png">
    		<img class="img1" src="${ctx }/images/top_logo.png">
    		<p class="p1">复兰项目管理</p>
    	</div>
    	<div class="left-nav">
    		<ul>
    			<li class="li1" onclick="window.location.href='${ctx}/project/new_project'">
    				<div class="li1-img"></div>
    				<p>创建新项目</p>
    			</li>
    			<li class="li2" onclick="window.location.href='${ctx}/project/list'">
    				<div class="li2-imgi"></div>
    				<p class="p-blue">我的项目清单</p>
    			</li>
    			<li class="li3" onclick="window.location.href='${ctx}/staff/list'">
    				<div class="li3-img"></div>
    				<p>公司人员管理</p>
    			</li>
    		</ul>
    	</div>

    	<div class="container">
            <div class="my-list-cont">
                <p class="p-detial-top">
                    <em class="em1" onclick="window.location.href='${ctx}/project/list'">我的项目清单</em>
                    <em class="em2">></em>
                    <em class="em2">${project.projectName }</em>
                </p>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt1">基本信息</p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目名称：</span>
                        <span class="sp2">${project.projectName }</span>
                        <input type="hidden" id="prj_id" value="${project.id }">
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目起止时间：</span>
                        <span><fmt:formatDate value="${project.startDate }"/>至<fmt:formatDate value="${project.endDate }"/></span>
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目编号：</span>
                        <span class="sp2">${project.projectNumber }</span>
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目描述：</span>
                        <span class="sp3">${project.projectDesc }</span>
                    </p>
                </div>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt2">项目团队</p>
                    <p class="p2 detial-cont1-p3">
                        <span class="sp1">
                            <em>负责人</em>
                        </span>
                        <em>${project.projectOwner.name }</em>
                    </p>
                    <c:forEach items="${sdtoMap }" var="map">
                    	<p class="p2">
                        <span class="sp1">
                            <em>${map.key }</em>
                        </span>
                        <c:forEach items="${map.value }" var="list">
                        	<em>${list.name }</em>
                        </c:forEach>
                    	</p>
                    </c:forEach>
                </div>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt3">项目文档
                        <span class="sp1" id="add-dir"><em>+</em>新建文件夹</span>
                        <span class="sp2">上传</span>
                    </p>
                    <p class="p-doc-all">
                        <label>
                        </label>
                        <button id="up_btn" onclick="showUpLevelDir()">上一级</button>
                    </p>
                    <ul class="ul-doc" id="dir_list">
                        <c:if test="${not empty rootDir.childDirs }">
                        	<c:forEach items="${rootDir.childDirs }" var="cDir">
                        		<li>
                        			<label onclick="showDir(${cDir.id})">
                                		<img src="${ctx }/images/img_floder.png">
                                		<span>${cDir.name }</span>
                            		</label>
                            		<em onclick="renameDir(${cDir.id})">重命名</em>
                            		<em onclick="removeDir(${cDir.id})">删除</em>
                            	</li>
                        	</c:forEach>
                        </c:if>
                        <c:if test="${not empty rootDir.files }">
                        	<c:forEach items="${rootDir.files }" var="file">
                        		<li>
                        			<label>
                                		<img src="${ctx }/images/img_word.png">
                                		<span>${file.fileName }</span>
                            		</label>
                            		<em onclick="window.location.href='${ctx}/file/download/${file.id}'">下载</em>
                            		<em onclick="renameFile(${file.id})">重命名</em>
                            		<em onclick="removeFile(${file.id})">删除</em>
                            	</li>
                        	</c:forEach>
                        </c:if>
                    </ul>
                </div>
            </div>
    	</div>
    	
    	<div class="bg"></div>
    	<!-- 上传文件窗口 -->
    	<div class="wind-del wind">
            <p class="p1">上传文件<em></em></p>
            <ul class="ul-infor">
            	<form id="upload_form">
            		<input name="dirID" type="hidden">
                	<li><input type="file" id="f_btn" name="file"></li>
                	<li class="tcenter">
                    	<button type="button" class="btn-ok" id='uploadBtn'>确定</button>
                    	<button class="btn-no">取消</button>
                	</li>
            	</form>
            </ul>
        </div>
    </body>
</html>