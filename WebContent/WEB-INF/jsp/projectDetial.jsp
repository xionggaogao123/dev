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
        	var logDates = [];
        	var dateIndex = 0;
        	
        	function showMsgBox(msg) {
        		$('.wind-msg').fadeIn();
        		$('.bg').fadeIn();
        		$('#msg').text(msg);
        	}
        	
        	function hideMsgBox() {
        		$('.wind-msg').fadeOut();
        		$('.bg').fadeOut();
        	}
        	
        	function hideAll() {
        		$('.wind').fadeOut();
                $('.bg').fadeOut();
        	}
        	
        	function showDelBox(functionType, arg) {
        		$('.wind-remove').fadeIn();
        		$('.bg').fadeIn();
        		$('#to_remove').unbind('click');
        		if(functionType === 1) {
        			$("#to_remove").bind('click', function() {
        				$.ajax({
    						async : false,
    						cache : false,
    						dataType : 'json',
    						type : 'post',
    						url : '${ctx}' + '/dir/remove',
    						data : {
    							id : arg,
    						},
    						success : function(data) {
    							if(data.code === 0) { // 删除成功
    								hideAll();
    								refushDir(); // 刷新
    							}
    							
    							if(data.code === 1) {
    								showMsgBox("出错了：" + data.msg);
    							}
    						},
    						error : function(arg0, arg1, arg2) {
    							showMsgBox("出错了");
    						}
        				});
        			});
        		} else if(functionType === 2) {
        			$('#to_remove').bind('click', function() {
        				$.ajax({
    						async : false,
    						cache : false,
    						dataType : 'json',
    						type : 'post',
    						url : '${ctx}' + '/file/remove',
    						data : {
    							fileId : arg,
    						},
    						success : function(data) {
    							if(data.code === 0) { // 删除成功
    								refushDir(); // 刷新
    								hideAll();
    							}
    							
    							if(data.code === 1) {
    								showMsgBox("出错了：" + data.msg);
    							}
    						},
    						error : function(arg0, arg1, arg2) {
    							showMsgBox("出错了");
    						}
        				});
        			});
        		}
        	}
        	
        	function showInputBox(title, name, functionType, arg) {
        		$('.wind-input').fadeIn();
        		$('.bg').fadeIn();
        		$('.wind-input .p1').text(title);
        		$('.wind-input .sp1').text(name);
        		$('.wind-input .inp1').val("");
        		$("#to_excute").unbind('click');
        		if(functionType === 1) {
        			console.log("111")
        			$("#to_excute").bind('click', function() {
        				console.log("3333");
        				var dirName = $('.wind-input .inp1').val();
                        if(!dirName) {
                        	hideAll();
                        	showMsgBox("请输入文件夹名称");
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
                                        hideAll();
                                    }
                                    
                                    if(data.code === 1) {
                                    	hideAll();
                                    	showMsgBox("出错了：" + data.msg);
                                    }
                                },
                                error : function(arg0, arg1, arg2) {
                                	hideAll();
                                	showMsgBox("出错了");
                                }
                            });
                        }
                        $("#to_excute").unbind('click');
        			});
        		} else if(functionType === 2) {
        			console.log("arg:" + arg);
        			$("#to_excute").bind('click', function() {
        				var newName = $('.wind-input .inp1').val();
            			if(!newName) {
            				hideAll();
            				showMsgBox("请输入新文件夹名称");
            			} else {
            				$.ajax({
        						async : false,
        						cache : false,
        						dataType : 'json',
        						type : 'post',
        						url : '${ctx}' + '/dir/rename',
        						data : {
        							id : arg,
        							rename : newName
        						},
        						success : function(data) {
        							if(data.code === 0) { // 创建成功
        								refushDir(); // 刷新
        								hideAll();
        							}
        							
        							if(data.code === 1) {
        								hideAll();
        								showMsgBox("出错了：" + data.msg);
        							}
        						},
        						error : function(arg0, arg1, arg2) {
        							hideAll();
        							showMsgBox("出错了");
        						}
            				});
            			}
            			$("#to_excute").unbind('click');
        			});
        		} else if(functionType === 3) {
        			$('#to_excute').bind('click', function() {
        				var newName = $('.wind-input .inp1').val();
            			if(!newName) {
            				hideAll();
            				showMsgBox("请输入新文件名称");
            			} else {
            				$.ajax({
        						async : false,
        						cache : false,
        						dataType : 'json',
        						type : 'post',
        						url : '${ctx}' + '/file/rename',
        						data : {
        							fileId : arg,
        							name : newName
        						},
        						success : function(data) {
        							if(data.code === 0) { // 创建成功
        								hideAll();
        								refushDir(); // 刷新
        							}
        							
        							if(data.code === 1) {
        								hideAll();
        								showMsgBox("出错了：" + data.msg);
        							}
        						},
        						error : function(arg0, arg1, arg2) {
        							hideAll();
        							showMsgBox("出错了");
        						}
            				});
            			}
            			$("#to_excute").unbind('click');
        			});
        		}
        	}
        	
        	function hideAndGetInput() {
        		$('.wind-input').fadeOut();
        		$('.bg').fadeOut();
        	}
        	
        	function renameFile(id) {
        		showInputBox('请输入新文件名', '新文件名', 3, id);
    		}
        	
        	function renameDir(id) {
        		showInputBox('请输入新文件夹名', '新文件夹名', 2 , id);
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
        		showDelBox(2, id);
        	}
        	
        	function removeDir(id) {
        		showDelBox(1, id);
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
						showMsgBox("出错了");
					}
				});
    		}
        	
        	function editGroup() {
        		$('.wind-addmember').fadeIn();
        		$('.bg').fadeIn();
        		
        		var ids = [];
        		
        		$('#prj_grp em.group').each(function() {
        			ids.push($(this).attr("sId"));
        		});
        		
        		$('.wind-addmember input[type=checkbox]').each(function() {
        			$(this).prop("checked", false);
        			var value = $(this).val();
        			if(ids.indexOf(value) != -1) {
        				$(this).prop("checked", "checked");
        			}
        		});
        	}
        	
        	function LogBox() {
        		$('.wind-log .inp1').val("")
        		$('.wind-log').fadeIn();
        		$('.bg').fadeIn();
        	}
        	
        	function LogInfo() {
        		//getPrjLogInfo(0);
        		//$('.wind-log-tb').fadeIn();
        		//$('.bg').fadeIn();
        		getLogDates();
        		if(logDates.length > 0) {
        			getLogsByDate(0);
        		}
        	}
        	
        	function getLogDates() {
        		if(true) {
        			$.ajax({
        				async : false,
        				cache : false,
        				type : 'post',
        				dataType : 'json',
        				url : '${ctx}' + '/project/log_dates',
        				data : {
        					prjId : ${project.id},
        				},
        				success : function(data) {
        					if(data.length > 0) {
        						logDates = data;
        						console.log(logDates);
        					} else {
        						showMsgBox("没有相关日志信息");
        					}
        				},
        				error : function(arg0, arg1, arg2) {
        					showMsgBox("出错了");
        				}
        			});
        		} 
        	}
        	
        	function getLogsByDate(index) {
        		
        		$.ajax({
        			async : false,
        			cache : false,
        			type : 'post',
        			dataType : 'json',
        			url : '${ctx}' + '/project/logInDate',
        			data : {
        				date : logDates[index],
        				prjId : ${project.id}
        			},
        			success : function(data) {
        				if(data.info.code === 0) {
        					dateIndex = index;
        					if(index <= 0) {
		        				console.log("-----");
		        				$('.wind-log-tb .prev').css('visibility', 'hidden');
		        			} else {
		        				$('.wind-log-tb .prev').css('visibility', 'visible');
		        			}
		        			if(index >= logDates.length - 1) {
		        				$('.wind-log-tb .next').css('visibility', 'hidden');
		        			} else {
		        				$('.wind-log-tb .next').css('visibility', 'visible');
		        			}
		        			
		        			$('#log_list').empty();
		        			var logList = data.data;
		        			var html = '';
		        			for(var i = 0; i < logList.length; i++) {
		        				html += '<tr><td width="100px">' + logList[i].createdUserName + '</td>';
		        				html += '<td width="200px">' + logList[i].createDate + '</td>';
		        				html += '<td width="370px">' + logList[i].logInfo + '</td></tr>';
		        			}
		        			
		        			$('#log_list').append(html);
		        			
		        			$('.wind-log-tb').fadeIn();
		        			$('.bg').fadeIn();
        				} else {
        					hideAll();
    						showMsgBox("出错了：" + data.info.msg);
        				}
        			},
        			error : function(arg0, arg1, arg2) {
        				showMsgBox("出错了")
        			}
        		});
        	}
        	
        	function getPrjLogInfo(index) {
        		logIndex = index;
        		$.ajax({
        			async : false,
        			cache : false,
        			type : 'post',
        			dataType : 'json',
        			url : '${ctx}' + '/project/logs',
        			data : {
        				prjId : ${project.id},
        				index : index
        			},
        			success : function(data) {
        				if(data.info.code === 0) {
        					var count = data.count;
        					if(count > 0 && data.data) {
        						$('#cuser').val(data.data.createdUserName);
        						var createDate = new Date(data.data.createdTime);
        						var dateStr = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();
        						$('#cdate').val(dateStr);
        						$('.wind-show-log textarea').val(data.data.logInfo);
        						$('.wind-show-log').fadeIn();
        		        		$('.bg').fadeIn();
        		        		if(index <= 0) {
        		        			console.log("-----");
        		        			$('.wind-show-log .prev').css('visibility', 'hidden');
        		        		} else {
        		        			$('.wind-show-log .prev').css('visibility', 'visible');
        		        		}
        		        		if(index + 1 >= count) {
        		        			$('.wind-show-log .next').css('visibility', 'hidden');
        		        		} else {
        		        			$('.wind-show-log .next').css('visibility', 'visible');
        		        		}
        					} else {
        						hideAll();
        						showMsgBox("还没有日志");
        					}
        				} else if(data.info.code === 1) {
        					hideAll();
    						showMsgBox("出错了：" + data.info.msg);
        				}
        			},
        			error : function() {
        				hideAll();
						showMsgBox("出错了");
        			}
        		});
        	}
        	
        	function addPrjLog() {
        		var log = $.trim($('.wind-log .inp1').val());
				if(!log || log.length <= 0) {
					hideAll();
					showMsgBox("请输入内容");
					return;
				}
        		$.ajax({
        			async : false,
        			cache : false,
        			dataType : 'json',
        			type : 'post',
        			url : '${ctx}' + '/project/add_log',
        			data : {
        				logInfo : log,
        				prjId : ${project.id}
        			},
        			success : function(data) {
        				if(data.code === 0) {
        					hideAll();
        					showMsgBox("更新成功");
        				} else if(data.code === 1) {
        					hideAll();
        					showMsgBox("出错了：" + data.msg);
        				}
        			},
        			error : function(arg0, arg1, arg2) {
        				hideAll();
        				showMsgBox("出错了");
        			}
        		});
        	}
        	
        	function updateMems(ids) {
        		
        		var memberIdList = [];
        		
        		$('.wind-addmember input[type=checkbox]').each(function() {
        			if($(this).prop("checked")) {
        				memberIdList.push(parseInt($(this).val()));
        			}
        		});
        		
        		$.ajax({
        			async : false,
        			cache : false,
        			dataType : 'json',
        			type : 'post',
        			data : {
        				prjId : '${project.id}',
        				ids : memberIdList
        			},
        			url : '${ctx}' + '/project/updateGroup',
        			success : function(data) {
        				var code;
        				var msg;
        				if(!data.info) {
        					code = data.code;
        					msg = data.msg;
        				} else {
        					code = data.info.code;
        					msg = data.info.msg;
        				}
        				if(code === 0) {
        					var html = "";
        					var memberDatas = data.data;
        					for(var i = 0; i <memberDatas.length; i++) {
        						html += '<p class="p2"><span class="sp1"><em>';
        						html += memberDatas[i][0].department + '</em></span>';
        						for(var j = 0; j < memberDatas[i].length; j++) {
        							html += '<em class="group" sid="' + memberDatas[i][j].id + '">' + memberDatas[i][j].name + '</em>';
        						}
        						html += '</p>';
        					}
        					$('#prj_grp').empty().append(html);
        				} else if(code === 1) {
        					showMsgBox("出错了：" + msg);
        				}
        				$('.wind-addmember').fadeOut();
                		$('.bg').fadeOut();
        			},
        			error : function(arg0, arg1, arg2) {
        				showMsgBox("出错了");
        				$('.wind-addmember').fadeOut();
                		$('.bg').fadeOut();
        			}
        		});
        	}
        	
        	function addDir() {
        		console.log("-------")
        		var dirName = $('.wind-input .inp1').val();
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
            }
        	
        	function showChgStatusBox() {
        		$('.wind-prj-status').fadeIn();
        		$('.bg').fadeIn();
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
        		
        		$('.wind-msg .btn-ok').click(function() {
        			$('.wind').fadeOut();
        			$('.bg').fadeOut();
        		});
        		
        		function boxOut() {
        			$('.wind').fadeOut();
        			$('.bg').fadeOut();
        		}
        		
        		$('.dt3 .sp2').click(function() {
        			$('.wind-del.wind-upload').fadeIn();
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
        						showMsgBox("上传成功");
        						refushDir(); // 刷新
        					}
        					if(data.code === 1) {
        						boxOut();
        						showMsgBox("上传失败：" + data.msg);
        					}
        				},
        				error : function(arg0, arg1, arg2) {
        					boxOut();
        					showMsgBox("出错了");
        				}
        			});
        			$('#f_btn').val("");
        		});
        		
        		$("#add-dir").click(function() {
        			showInputBox('请输入文件夹名', '文件夹名', 1);
        		});
        		
        		$('.wind-prj-status .btn-ok').click(function() {
					var status = $('#status_li select').val();
					var statusText = $('#status_li option:selected').text();
					console.log("status:" + status + statusText);
        			$.ajax({
        				async : false,
        				cache : false,
        				dataType : 'json',
        				type : 'post',
        				url : '${ctx}/project/chgStatus',
        				data : {
        					prjId : ${project.id},
        					status : status
        				}, 
        				success : function(data) {
        					if(data.code === 0) {
        						hideAll();
        						$('.p-detial-top .sp1').text("状态：" + statusText);
        						$('.p-detial-top span').removeClass();
        						$('.p-detial-top span').addClass("sp1 color-" + status);
            					showMsgBox("更新成功");
        					} if(data.code === 1) {
        						hideAll();
            					showMsgBox("出错了：" + data.msg);
        					}
        				}, 
        				error : function(arg0, arg1, arg2) {
        					hideAll();
            				showMsgBox("出错了");
        				}
        			});
        		});
        		
        		$('.wind-log-tb .prev').click(function() {
        			getLogsByDate(--dateIndex);
            	});
            	
            	$('.wind-log-tb .next').click(function() {
            		getLogsByDate(++dateIndex);
            	});
        		
        	});
        </script>
    </head>
    <body>
    	<div class="header">
    		<button onclick="window.location.href='${ctx}/user/logout'">退出</button>
    		<button onclick="window.location.href='${ctx}/static/ch-pw.html'" class="ch-pw">修改密码</button>
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
                    <span class="sp1 color-${status.code }" <c:if test="${loginedStaff.isPrjOwner eq '1' or loginedStaff.isPrjOwner eq '2' }">onclick="showChgStatusBox()" style="cursor: pointer;"</c:if>>状态：${status.status}</span>
                </p>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt1">基本信息
                    	<span class="sp1" onclick="LogBox()"><em>+</em>添加项目日志</span>
                    	<span class="sp1" onclick="LogInfo()"><em>●</em>查看项目日志</span>
                    </p>
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
                    <p class="detial-item-tit dt2">项目团队
                    	<span class="sp1" onclick="editGroup()"><em>+</em>添加新成员</span>
                    </p>
                    <p class="p2 detial-cont1-p3">
                        <span class="sp1">
                            <em>负责人</em>
                        </span>
                        <em>${project.projectOwner.name }</em>
                    </p>
                    <p class="p2 detial-cont1-p3">
                        <span class="sp1">
                            <em>创建人</em>
                        </span>
                        <em>${project.projectCreater.name }</em>
                    </p>
                    <div id="prj_grp">
                    	<c:forEach items="${sdtoMap }" var="map">
                    		<p class="p2">
                        	<span class="sp1">
                            	<em>${map.key }</em>
                        	</span>
                        	<c:forEach items="${map.value }" var="list">
                        		<em class="group" sId="${list.id }">${list.name }</em>
                        	</c:forEach>
                    		</p>
                    	</c:forEach>
                    </div>
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
    	<div class="wind-del wind-upload wind">
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
        
        <!-- 删除确认窗口 -->
        <div class="wind-del wind-remove wind">
            <p class="p1">提示<em></em></p>
            <ul class="ul-infor">
                <li>确认删除？</li>
                <li class="tcenter">
                    <button class="btn-ok" id="to_remove">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
        
        <!-- 信息提示窗口 -->
        <div class="wind-del wind-msg wind">
            <p class="p1">提示<em></em></p>
            <ul class="ul-infor">
                <li id="msg"></li>
                <li class="tcenter">
                    <button class="btn-ok">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
        
        <!-- 内容框 -->
        <div class="wind-edit wind-input wind">
            <p class="p1"><em></em></p>
            <ul class="ul-infor">
                <li>
                    <span class="sp1"></span>
                    <input type="text" class="inp1">
                </li>
                <li class="tcenter">
                    <button class="btn-ok" id="to_excute">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
        <!-- 添加成员窗口 -->
    	<div class="wind-addmember wind" style="top: 50%">
    		<p class="p1">添加成员<em></em></p>
    		<ul class="ul-fl">
    			<c:forEach items="${allStaffMap }" var="map">
    				<li class="clearfix">
    					<span>${map.key }</span>
	    				<div class="clearfix p-wind-cont">
	    					<c:forEach items="${map.value }" var="sDto">
	    						<div class="div-per-cont">
		    						<label>
		    							<input value=${sDto.id } type="checkbox">${sDto.name }
		    						</label>
	    							<p class="p-infor">
	    								<span>职务：${sDto.jobTitle }</span>
	    								<span>工号：${sDto.jobNumber }</span>
	    							</p>
    							</div>
    				        </c:forEach>
	    				</div>
    				</li>
    			</c:forEach>
    			<li style="text-align: center">
    				<button class="btn-ok" onclick="updateMems()">确定</button>
    				<button class="btn-no">取消</button>
    			</li>
    		</ul>
    	</div>
    	
    	<!-- 添加日志 -->
        <div class="wind-edit wind-log wind" style="height: 225px">
            <p class="p1">添加项目日志<em></em></p>
            <ul class="ul-infor">
                <li>
                <!-- style="margin: 11px 0px 0px; height: 103px; width: 389px;" -->
                    <textarea type="text" class="inp1"></textarea>
                </li>
                <li class="tcenter">
                    <button class="btn-ok" onclick="addPrjLog()">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
    	
    	<!-- 查看日志 -->
        <div class="wind-edit wind-show-log wind" style="height: 380px">
            <p class="p1">项目日志信息<em></em></p>
            <ul class="ul-infor">
            	<li>
            		<span class="sp1">创建人</span>
                    <input type="text" id="cuser" class="inp2" readonly>
            	</li>
            	<li>
            		<span class="sp1">创建日期</span>
                    <input type="text" id="cdate" class="inp2" readonly>
            	</li>
                <li>
                    <span class="sp1"></span>
                    <textarea type="text" class="inp1" readonly></textarea>
                </li>
                <li class="tcenter">
                    <button class="btn-log prev">上一条</button>
                    <button class="btn-log next">下一条</button>
                </li>
            </ul>
        </div>
        
        <!-- 查看日志列表 -->
        <div class="wind-edit wind-log-tb wind" style="height: 380px">
            <p class="p1">项目日志信息<em></em></p>
            <div style="height: 390px;">
        		<table>
            		<thead>
                		<tr>
                    		<th width="100px">姓名</th>
                    		<th width="200px">时间</th>
                    		<th width="370px">日志</th>
                		</tr>
            		</thead>
            		<tbody id="log_list">
            		</tbody>
        		</table>
   			</div>
            <div class="tcenter">
                    <button class="btn-log prev">上一页</button>
                    <button class="btn-log next">下一页</button>
            </div>
        </div>
    	
    	<!-- 修改项目状态 -->
    	<div class="wind-edit wind-prj-status wind">
            <p class="p1">修改项目状态<em></em></p>
            <ul class="ul-infor">
                <li id="status_li">
                    <span class="sp1">项目状态</span>
                    <select id="prj_status">
                    	<c:forEach items="${statusVO }" var="vo">
                    		<option value="${vo.value }"<c:if test="${vo.value == project.projectStatus }">selected</c:if>>${vo.label }</option>
                    	</c:forEach>
                    </select>
                </li>
                <li class="tcenter">
                    <button class="btn-ok">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
    	
    </body>
</html>