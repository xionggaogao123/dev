/*
 * @Author: Alex
 * @Date:   2016-08-10 14:24:31
 * @Last Modified by:   Alex
 * @Last Modified time: 2016-08-10 14:24:31
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox'],function(require,exports,module){
	/**
	 *初始化参数
	 */
	var meet = {},
		Common = require('common');
	var someFileFailed = false;
	//提交参数
	var meetData = {};

	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 * homepage.init()
	 */
	meet.init = function(){


		meet.uploadFile();
		//meet.getUserList();
		meet.selMyMeetingList(0);
		//设置初始页码
		meetData.page = 1;
		//设置每页数据长度
		meetData.pageSize = 12;
		//$(".zb-set-close,.alert-btn-qx").click(function(){
		//	$(".zhiban-meng").hide();
		//	$(".sphw-alert").hide();
		//	$(".newhw-alert").hide();
		//})
		$(".hyandhd-new").click(function(){
			meet.cleanInput();
			meet.setNoDisabledInput();
			$('#meetId2').val('');
			$(".newhw-alert").show();
			$(".hyandhd-meeting").hide();
		})
		$(".meeting-CH-top i").click(function(){
			$(".meeting-CH").hide();
			$(".meeting-MB").hide();
			$('.zhiban-meng').hide();
		});
		$(".hw-back").click(function(){
			$(".newhw-alert").hide();
			$(".newhw-alert2").hide();
			if($('#mtype').val()==1) {
				$(".gly-index").show();
			} else {
				$(".hyandhd-meeting").show();
			}

		})
		$(".model-manage").click(function(){
			$(".model-tea-manage").show();
			$(".hyandhd-meeting").hide();
			meet.selModelList(2);
		})
		$(".qx-shenhe").click(function(){
			$(".sphw-alert").hide();
			$('.zhiban-meng').hide();
		});
		$(".shenhe-close").click(function(){
			$(".sphw-alert").hide();
			$('.zhiban-meng').hide();
		});
		$(".hyandhd-back").click(function(){
			$(".model-tea-manage").hide();
			$(".hyandhd-meeting").show();
		})
		$(".hyandhd-new").click(function(){
			$(".newhw-mettings").show();
			$(".hyandhd-meeting").hide();
			$('#meet-name').text("新建会务/ 活动");
			meet.selModelList(1);
		})
		$("#model-sel").change(function (){
			meetData.meetId=$('#model-sel').val();
			if ($('#model-sel').val()=='') {
				meet.cleanInput();
			} else {
				meet.selMeetDetail(1);
			}

		});
		$("body").on("click", ".departmentList", function () {
			var curId = $(this).attr("curId");
			var userlist = departmentAndSubjectUser.department;
			var departmentUser = userlist[curId].list;
			//点击部门选择全部人
			if (!$(this).hasClass("current")) {//未选择
				$(this).addClass("current");
				for (var j = 0; j < departmentUser.length; j++) {
					var ifHave = false;
					for (var i = 0; i < choosedPeople.data.length; i++) {
						if (choosedPeople.data[i].id == departmentUser[j].idStr) {
							ifHave = true;
							break;
						}
					}
					if (!ifHave) {
						choosedPeople.data.push({"id": departmentUser[j].idStr, "name": departmentUser[j].value});
						$(".publishList[userId='" + departmentUser[j].idStr + "']").addClass('current');
					}
				}
				meet.choosedPeopleShow();
			}
			else {
				$(this).removeClass("current");

				for (var i = 0; i < choosedPeople.data.length; i++) {
					var ifHave = false;
					for (var j = 0; j < departmentUser.length; j++) {
						if (choosedPeople.data[i].id == departmentUser[j].idStr) {
							ifHave = true;
							break;
						}
					}
					if (ifHave) {
						choosedPeople.data.splice(i,1);
						i--;
						$(".publishList[userId='" + departmentUser[j].idStr + "']").removeClass('current');
					}
				}
				meet.choosedPeopleShow();
			}
		});
		//发布范围选择人
		$("body").on("click", ".publishList", function () {
			var id = $(this).attr("userId");
			var name = $(this).attr("name");
			if ($(this).hasClass("current")) {
				//同一个人的都去除样式
				meet.removePeople(id);
				$(".publishList[userId='" + id + "']").removeClass("current");
			}
			else {
				meet.addPeople(id, name);
				$(".publishList[userId='" + id + "']").addClass('current');
			}
		});
		$(".tab-head li").click(function(){
			$(".tab-head li").removeClass("cur");
			$(this).addClass("cur");
			$(".tab-main>div").hide();
			var name = $(this).attr("id");
			if (name=='MYHD') {
				$('#mtype').val(0);
				meet.selMyMeetingList(0);
			} else if (name=='HWSP') {
				$('#mtype').val(1);
				meet.selMyMeetingList(1);
			} else if (name=='HYCD') {
				meet.selMeetLogList();
			}
			$("#" + "tab-" + name).show();
		})
		$(".meeting-ul li").click(function(){
			$(".meeting-ul li").removeClass("meeting-active");
			$(this).addClass("meeting-active")
			$(".li-con>div").hide();
			var $name = $(this).attr("id");
			$("#" + "tab-" + $name).show();
		});

		$(".save-meeting").click(function(){
			meet.addMeetingInfo(0);
		});
		$(".save-meeting-model").click(function(){
			if ($.trim($('#meetname').val())=='') {
				alert("请输入名称。");
				return;
			}
			if ($('#datetime').val()==''||$('#starttime').val()==''||$('#endtime').val()=='') {
				alert("请输入起止时间。");
				return;
			}
			if ($.trim($('#cause').val())=='') {
				alert("请输入会议事由。");
				return;
			}
			$(".meeting-MB").show();
			$('.zhiban-meng').show();
		});
		$(".qx-xj-meeting").click(function(){
			$(".hyandhd-meeting").show();
			$(".newhw-alert").hide();
		});
		$(".meeting-rad").click(function(){
			//$('.Tanchu1').show();
			$('.zhiban-meng').show();
			//meet.getOnlyDepartmentList();
			$(".meeting-CH").show();
		})
		$(".qx1").click(function (event) {
			$(".Tanchu1").hide();
			meet.showPublishOnMain();
			$('.zhiban-meng').hide();

		});
		$(".QD1").click(function (event) {
			baseChoosedPeople = {data: []};
			for (var i = 0; i < choosedPeople.data.length; i++) {
				baseChoosedPeople.data.push(choosedPeople.data[i]);
			}
			meet.showPublishOnMain();
			$(".Tanchu1").hide();
			$('.zhiban-meng').hide();

		});

		$(".meeting-CH-QX").click(function(){
			$(".meeting-CH").hide();
			$(".meeting-MB").hide();
			$('.zhiban-meng').hide();
		});
		$(".close-model").click(function(){
			$(".meeting-MB").hide();
			$('.zhiban-meng').hide();
		});
		$("#submit-meeting3").click(function(){
			meet.selMyMeetingList(0);
		});
		$("#submit-meeting2").click(function(){
			meet.selMeetLogList();
		});
		$("#submit-meeting").click(function(){
			meet.selMyMeetingList(1);
		});
		$(".submit-shenhe").click(function(){
			meetData.meetId = $('#meetId').val();
			meetData.flag = $("input[name='agree']:checked").val();;
			meetData.appUserId = $('#appUser').val();
			meetData.remark = $('#remark').val();
			meet.submitShenHe();
		});


		$(".sure-model").click(function(){
			if ($.trim($('#modelname').val())=='') {
				alert("模板名称必须输入！");
				return;
			}
			if ($('#modelId').val()!='') {
				meet.updateMeetModel();
			} else {
				meet.addMeetingInfo(1);
			}

		});

		$(".add-user").click(function(){
			var flg = false;
			$('.meeting-dv span').each(function(i) {
				if($('.meeting-dv span').eq(i).attr('uid')==$('#userlist').val()) {
					flg = true;
				}
			});
			if (!flg) {
				$('.meeting-dv').append("<span class='meeting-ren' uid=" + $('#userlist').val() + ">" + $("#userlist option:selected").text() + "<em class='close-user'>X</em></span>");
				$('.meeting-CH').hide();
				$('.zhiban-meng').hide();
				$('.close-user').click(function() {
				$(this).parent().remove();
				});
			} else {
				alert('该人员已存在！');
			}
		});

	}
	meet.submitShenHe = function() {
		Common.getPostData('/meeting/submitShenHe.do',meetData,function(rep){
			if (rep.flag) {
				alert("审核成功！");
				$(".zhiban-meng").hide();
				$(".sphw-alert").hide();
				meet.selMyMeetingList(1);
			} else {
				alert("审核失败！");
			}
		});
	}
	meet.selMeetDetail = function(type) {
		if (meetData.meetId!=''&& meetData.meetId!=null) {
			Common.getPostData('/meeting/selMeetingDetail.do',meetData,function(rep){
				if (rep.rows!=null) {
					if (type==1) {
						if (rep.rows.type==0) {
							$('input:radio[name=m-type]')[0].checked = true;
						} else if (rep.rows.type==1) {
							$('input:radio[name=m-type]')[1].checked = true;
						}
						$('#meetname').val(rep.rows.name);
						$('#datetime').val(rep.rows.meetDate);
						$('#starttime').val(rep.rows.startTime);
						$('#endtime').val(rep.rows.endTime);
						$('#cause').val(rep.rows.cause);
						$('#process').text(rep.rows.process);
						$('#order').val(rep.rows.order);
						$('#issue').val(rep.rows.issue);
						$('#approvalUserId').val(rep.rows.approvalUserId);
						$('.meeting-dv').empty();
						$('.meeting-dv').append("<span class='meeting-rad' uid=''></span>");
						$(".meeting-rad").click(function(){
							//$('.Tanchu1').show();
							$('.zhiban-meng').show();
							//meet.getOnlyDepartmentList();
							$(".meeting-CH").show();
						})
						if(rep.rows.userlist!=null && rep.rows.userlist.length!=0) {
							for(var i=0;i<rep.rows.userlist.length;i++) {
								$('.meeting-dv').append("<span class='meeting-ren' uid=" + rep.rows.userlist[i].id + ">" + rep.rows.userlist[i].userName + "<em class='close-user'>X</em></span>");
								$('.close-user').click(function() {
									$(this).parent().remove();
								});
							}
						}
						$('.upload-mlist').empty();
						if (rep.rows.coursewareList!=null && rep.rows.coursewareList.length!=0) {
							for(var i=0;i<rep.rows.coursewareList.length;i++) {
								var list = rep.rows.coursewareList[i];
								$('.upload-mlist').append("<li vurl="+list.value+" vnm="+list.type+"><span>"+list.type+"</span><div class='meeting-a'><a href='javascript:;'>删除</a></div></li>");
								$(".meeting-a").click(function(event) {
									$(this).parent().remove();
								});
							}
						}
					} else if (type==2) {
						if (rep.rows.type==0) {
							$('input:radio[name=m-type2]')[0].checked = true;
						} else if (rep.rows.type==1) {
							$('input:radio[name=m-type2]')[1].checked = true;
						}
						$('#meetname2').val(rep.rows.name);
						$('#datetime2').val(rep.rows.meetDate);
						$('#starttime2').val(rep.rows.startTime);
						$('#endtime2').val(rep.rows.endTime);
						$('#cause2').val(rep.rows.cause);
						$('#process2').text(rep.rows.process);
						$('#order2').val(rep.rows.order);
						$('#issue2').val(rep.rows.issue);
						$('#approvalUserId2').val(rep.rows.approvalUserId);
						$('.meeting-dv2').empty();
						if(rep.rows.userlist!=null && rep.rows.userlist.length!=0) {
							for(var i=0;i<rep.rows.userlist.length;i++) {
								$('.meeting-dv2').append("<span class='meeting-ren' uid=" + rep.rows.userlist[i].id + ">" + rep.rows.userlist[i].userName + "<em class='close-user'>X</em></span>");
							}
						}
						$('.upload-mlist2').empty();
						if (rep.rows.coursewareList!=null && rep.rows.coursewareList.length!=0) {
							for(var i=0;i<rep.rows.coursewareList.length;i++) {
								var list = rep.rows.coursewareList[i];
								var href = list.value;
								var fileKey = href.substring(href.lastIndexOf('/') + 1);
								var fileName = list.type;
								href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
								$('.upload-mlist2').append("<li vurl="+list.value+" vnm="+list.type+"><span>"+list.type+"</span><div class='meeting-a'><a class='yulan' pth="+ list.value +">预览</a><i>|</i><a href="+href+" fnm="+fileName+" target='_blank'>下载</a></div></li>");
								$('.yulan').click(function() {
									var url = $(this).attr("pth");
									var urlarg = url.split(".")[url.split(".").length-1];
									if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
										window.open("http://ow365.cn/?i=9666&furl="+url);
									} else {
										window.open(url);
									}
								});
							}
						}
					} else {
						$('#username').text(rep.rows.userName);
						$('#name').text(rep.rows.name);
						if (rep.rows.type==0) {
							$('#meettype').text("会议");
						} else {
							$('#meettype').text("活动");
						}

						$('#time').text(rep.rows.meetDate + " " + rep.rows.startTime + "-" + rep.rows.endTime);
						$('#meetcause').text(rep.rows.cause);
						$('#meetId').val(rep.rows.id);
						$('.alert-legend-con').empty();
						$('.alert-legend-con').append("<span>提交申请</span>");
						if (rep.rows.sheHeUserList!=null && rep.rows.sheHeUserList.length!=0) {
							for(var i=0;i<rep.rows.sheHeUserList.length;i++) {
								var user = rep.rows.sheHeUserList[i];
								$('.alert-legend-con').append("<i></i><span>"+user.userName+"<em class='alert-step-result alert-result-agree'>同意</em></span>");
							}
						}
						$('.alert-legend-con').append("<i></i><span>我<em class='alert-step-result alert-result-spz'>审批中</em></span>");
					}

				}
			});
		} else {

		}

	}

	meet.addMeetingInfo = function(type) {
		var startTime = $('#starttime').val();
		var endTime = $('#endtime').val();
		if ($.trim($('#meetname').val())=='') {
			alert("请输入名称。");
			return;
		}
		if ($('#datetime').val()==''||startTime==''||endTime=='') {
			alert("请输入起止时间。");
			return;
		}
		if ($.trim($('#cause').val())=='') {
			alert("请输入会议事由。");
			return;
		}
		var startTimeArray = startTime.split(":");
		var endTimeArray = endTime.split(":");
		var startDate = new Date(startTimeArray[0],startTimeArray[1]);
		var endDate = new Date(endTimeArray[0],endTimeArray[1]);
		var start = startDate.getTime();
		var end = endDate.getTime();

		if (start >= end) {
			alert('开始时间不能大于结束时间。');
			return ;
		}
		var uid='';
		var cnt = 0;
		$('.meeting-dv span').each(function(i) {
			uid += $('.meeting-dv span').eq(i).attr('uid') + ',';
			cnt +=1;
		});
		if (cnt<=2) {
			alert('参会人员必须有两个及两个以上。');
			return ;
		}
		meetData.name=$.trim($('#meetname').val());
		meetData.type=$("input[name='m-type']:checked").val();
		meetData.startTime=$('#datetime').val()+" "+$('#starttime').val();
		meetData.endTime=$('#datetime').val()+" "+$('#endtime').val();
		meetData.cause=$.trim($('#cause').val());
		meetData.process=$('#process').val();
		meetData.order=$('#order').val();
		meetData.issue=$('#issue').val();
		meetData.approvalUserId=$('#approvalUserId').val();
		meetData.modelType = type;
		meetData.users = uid;
		meetData.modelName = $('#modelname').val();
		var fnm = '';
		var furl = '';
		$('.upload-mlist li').each(function(i) {
			fnm += $(this).attr('vnm') + ',';
			furl += $(this).attr('vurl') + ',';
		});
		meetData.filenameAry=fnm;
		meetData.pathAry=furl;
		meetData.id = $('#meetId2').val();
		Common.getPostData('/meeting/addMeetingInfo.do',meetData,function(rep){
			if (rep.flag) {
				if (type==1) {
					if (rep.count==0) {
						alert("模板新建成功！");
						$('#modelname').val('');
						$(".meeting-MB").hide();
						$('.zhiban-meng').hide();
					} else {
						alert("模板名称不能重复！");
					}

				} else {
					if (meetData.id!='') {
						alert("更新成功！");
					} else {
						alert("新建成功！");
					}
				}
				meet.selMyMeetingList(0);
				$(".newhw-alert").hide();
				$(".hyandhd-meeting").show();
			} else {
				alert("修改失败！");
			}
		});
	}
	meet.cleanInput = function() {
		$('#meetname').val('');
		$('#datetime').val('');
		$('#starttime').val('');
		$('#endtime').val('');
		$('#model-sel').val('');
		$('#cause').val('');
		$('#process').val('');
		$('#order').val('');
		$('#issue').val('');
		$('#approvalUserId').val('');
		//$("#m-type:checked").attr("checked",false);
		//$("input[id=m-type][value=0]").attr("checked",true);
		$('.meeting-dv').empty();
		$('.meeting-dv').append("<span class='meeting-rad' uid=''></span><span class='meeting-ren' uid="+userId+">"+ nb_username +"</span>");
		$(".meeting-rad").click(function(){
			//$('.Tanchu1').show();
			$('.zhiban-meng').show();
			//meet.getOnlyDepartmentList();
			$(".meeting-CH").show();
		})
		$('.upload-mlist').empty();

	}
	meet.selMyMeetingList = function(type) {
		meetData.index=type;
		if(type==0) {
			meetData.startTime=$('#stime3').val();
			meetData.endTime=$('#etime3').val();
			meetData.type=$('#status3').val();
			meetData.keyword=$('#keyword3').val();
		} else if (type==1) {
			meetData.startTime=$('#stime').val();
			meetData.endTime=$('#etime').val();
			meetData.type=$('#status').val();
			meetData.keyword=$('#keyword').val();
		}
		Common.getPostData('/meeting/selMyMeetingList.do',meetData,function(rep){
			if (type==0) {
				$('#meeting-table').html('');
				Common.render({tmpl:$('#meeting-table_templ'),data:rep,context:'#meeting-table'});
				$(".meeting-join").click(function(){
					var meet = $(this).attr('mid');
					meetData.meetId = meet;
					Common.getPostData('/meeting/checkMeetingStatus.do',meetData,function(rep){
						if (rep.flag) {
							window.location.href = "/meeting/meetingDetail.do?meetId="+meet;
						} else {
							alert("您已退出会议，不允许进入！");
						}
					});
				});
				$(".detail-meeting").click(function(){
					meetData.meetId = $(this).attr('mid');
					meet.selMeetDetail(2);
					$(".newhw-alert2").show();
					$(".hyandhd-meeting").hide();
				});
				$(".edit-meeting").click(function(){
					meet.selModelList(1);
					meetData.meetId = $(this).attr('mid');
					$('#meetId2').val($(this).attr('mid'));
					meet.selMeetDetail(1);
					$('#meet-name').text("编辑会务/ 活动");
					$(".newhw-alert").show();
					$(".hyandhd-meeting").hide();
				});
				$(".meeting-ck").click(function(){
					window.location.href = "/meeting/meetingLog.do?meetId="+$(this).attr('mid');
				});
				$(".meeting-tj").click(function(){
					meet.selModelList(1);
					meetData.meetId = $(this).attr('mid');
					$('#meetId2').val($(this).attr('mid'));
					meet.selMeetDetail(1);
					$('#meet-name').text("编辑会务/ 活动");
					meet.setDisabledInput();
					$(".newhw-alert").show();
					$(".hyandhd-meeting").hide();
				});
			} else {
				$('#meeting-table1').html('');
				Common.render({tmpl:$('#meeting-table_templ1'),data:rep,context:'#meeting-table1'});
				$(".shehe-detail").click(function(){
					meetData.meetId = $(this).attr('mid');
					meet.selMeetDetail(2);
					$(".newhw-alert2").show();
					$(".gly-index").hide();
				});
				$(".hyandhd-sp").click(function(){
					meetData.meetId = $(this).attr('mid');
					meet.selMeetDetail(3);
					$(".zhiban-meng").show();
					$(".sphw-alert").show();
				});

			}
		});
	}

	meet.setDisabledInput = function() {
		$('#meetname').attr("disabled",true);
		//document.getElementsByName("m-type").attr("disabled",true);
		$('#datetime').attr("disabled",true);
		$('#starttime').attr("disabled",true);
		$('#endtime').attr("disabled",true);
		$('#model-sel').attr("disabled",true);
		$('#cause').attr("disabled",true);
		$('#process').attr("disabled",true);
		$('#order').attr("disabled",true);
		$('#issue').attr("disabled",true);
		$('#approvalUserId').attr("disabled",true);
	}

	meet.setNoDisabledInput = function() {
		$('#meetname').attr("disabled",false);
		//document.getElementsByName("m-type").attr("disabled",true);
		$('#datetime').attr("disabled",false);
		$('#starttime').attr("disabled",false);
		$('#endtime').attr("disabled",false);
		$('#model-sel').attr("disabled",false);
		$('#cause').attr("disabled",false);
		$('#process').attr("disabled",false);
		$('#order').attr("disabled",false);
		$('#issue').attr("disabled",false);
		$('#approvalUserId').attr("disabled",false);
	}
	meet.selMeetLogList = function() {
		meetData.startTime=$('#stime2').val();
		meetData.endTime=$('#etime2').val();
		//meetData.type=$('#status2').val();
		meetData.keyword=$('#keyword2').val();
		Common.getPostData('/meeting/selMeetLogList.do',meetData,function(rep){
			$('#meeting-table2').html('');
			Common.render({tmpl:$('#meeting-table_templ2'),data:rep,context:'#meeting-table2'});
			$(".log-detail").click(function(){
				window.location.href = "/meeting/meetingLog.do?meetId="+$(this).attr('mid');
			});
			$(".del-log").click(function(){
				if (confirm("确定要删除该会议存档吗？")) {
					meetData.id = $(this).attr('id');
					meet.delMeetLog();
				}
			});
		});
	}
	meet.delMeetLog = function() {
		Common.getPostData('/meeting/delMeetLog.do',meetData,function(rep){
			if (rep.flag) {
				alert("删除成功！");
				meet.selMeetLogList();
			} else {
				alert("删除失败！");
			}
		});
	}
	meet.selModelList = function(type) {
		Common.getPostData('/meeting/selMeetingModelList.do',meetData,function(rep){
			if (type==1) {
				$('#model-sel').empty();
				var sel = '<option value="">请选择</option>';
				if (rep.rows!=null && rep.rows.length!=0) {
					for (var i=0;i<rep.rows.length;i++) {
						sel += "<option value="+rep.rows[i].id+">"+rep.rows[i].modelName+"</option>";
					}
				}
				$('#model-sel').append(sel);
			} else if (type==2) {
				$('#model-table').html('');
				Common.render({tmpl:$('#model-table_templ'),data:rep,context:'#model-table'});
				$(".del-model").click(function(){
					if (confirm("确定删除？")){
						meetData.meetId = $(this).attr('mid');
						Common.getData('/meeting/delMeetingInfo.do', meetData,function(rep) {
							if (rep.flag) {
								alert("删除成功！");
								meet.selModelList(2);
							} else {
								alert("删除失败！");
							}
						});
					}
				});
				$(".edit-model").click(function(){
					$('#modelname').val($(this).attr("mnm"));
					$('#modelId').val($(this).attr("mid"));
					$(".zhiban-meng").show();
					$(".meeting-MB").show();
					//$(".zb-model-alert").show();
				});
			}

		});
	}
	meet.updateMeetModel = function() {
		meetData.modelName = $('#modelname').val();
		meetData.meetId = $('#modelId').val();
		Common.getPostData('/meeting/updateMeetModel.do',meetData,function(rep){
			if (rep.flag) {
				if (rep.count==0) {
					alert("修改成功！");
					$(".meeting-MB").hide();
					$('.zhiban-meng').hide();
					$('#modelname').val('');
					$('#modelId').val('');
					meet.selModelList(2);
				} else {
					alert("模板名称不能重复！");
				}

			} else {
				alert("修改失败！");
			}
		});
	}

	meet.uploadFile = function() {
		$('#file_upload').uploadify({
			'swf': "/static/plugins/uploadify/uploadify.swf",
			'uploader': '/meeting/file/upload.do',
			'method': 'post',
			'buttonText': '',
			'fileTypeDesc': '',
			'fileSizeLimit': '300MB',
			'fileTypeExts': '*.*',
			'multi': true,
			'fileObjName': 'Filedata',
			'onUploadSuccess': function (file, response, result) {
				try {
					var json = $.parseJSON(response);
					if (json.flg) {
						$('.upload-mlist').append("<li vurl="+json.vurl+" vnm="+json.name+"><span>"+json.name+"</span><div class='meeting-a'><a href='javascript:;'>删除</a></div></li>");
					} else {
						someFileFailed = true;
					}
					/*关闭图片*/
					$(".meeting-a").click(function(event) {
						$(this).parent().remove();
					});
				} catch (err) {
				}
			},
			'onQueueComplete': uploadComplete,
			'onUploadError': function (file, errorCode, errorMsg, errorString) {
				//MessageBox("服务器响应错误。", -1);
				someFileFailed = true;
			}
		});
	}
	function uploadComplete(queueData) {
		if (someFileFailed) {
			MessageBox("部分视频上传失败，请重试。", -1)
			someFileFailed = false;
		}
	}
	var departmentAndSubjectUser = {
		"department": [],
		"subject": []
	};
	/*发布范围选择开始*/
	var choosedPeople = {data: []};
	var baseChoosedPeople = {data: []};
	//获取发布范围显示数据
	meet.getOnlyDepartmentList = function() {
		choosedPeople = {data: []};
		for (var i = 0; i < baseChoosedPeople.data.length; i++) {
			choosedPeople.data.push(baseChoosedPeople.data[i]);
		}
		var userlist = departmentAndSubjectUser.department;
		//初始化是否被选中
		for (var i = 0; i < userlist.length; i++) {
			for (var j = 0; j < userlist[i].list.length; j++) {
				if (meet.checkIfChoosed(userlist[i].list[j].idStr)) {
					userlist[i].list[j].choosed = 1;
				}
				else {
					userlist[i].list[j].choosed = 0;
				}
			}
		}
		$(".publishShow").empty();
		Common.render({tmpl: $('#publishJs'), data: {data: userlist}, context: '.publishShow'});
		meet.choosedPeopleShow();
	}

	//判断该user是否已经被选择
	meet.checkIfChoosed = function(id) {
		for (var i = 0; i < choosedPeople.data.length; i++) {
			if (choosedPeople.data[i].id == id) {
				return true;
			}
		}
		return false;
	}

	//添加选择的好友----发布范围
	meet.addPeople = function(id, name) {
		for (var i = 0; i < choosedPeople.data.length; i++) {
			if (choosedPeople.data[i].id == id) {
				return;
			}
		}
		choosedPeople.data.push({"id": id, "name": name});
		meet.choosedPeopleShow();
	}

	//删除选择的好友
	meet.removePeople = function(id) {
		for (var i = 0; i < choosedPeople.data.length; i++) {
			if (choosedPeople.data[i].id == id) {
				choosedPeople.data.splice(i, 1);
				meet.choosedPeopleShow();
				return;
			}
		}
	}

	//显示已经选择的好友
	meet.choosedPeopleShow = function() {
		var list = choosedPeople.data;
		if (list.length == 0) {
			$("#Hm").empty();
			$("#Hm").append("<em>未选择</em>");
			return;
		}
		$("#Hm").empty();
		Common.render({tmpl: $('#choosedPeopleJs'), data: {data: list}, context: '#Hm'});
	}

	meet.showPublishOnMain = function() {
		var allPeople = "";
		for (var i = 0; i < baseChoosedPeople.data.length; i++) {
			if (i < baseChoosedPeople.data.length - 1) {
				allPeople += baseChoosedPeople.data[i].name + ",";
			}
			else {
				allPeople += baseChoosedPeople.data[i].name;
			}
		}
		$("#choosedPeople").val(allPeople);
	}

	meet.getUserList = function () {
		$.ajax(
			{
				url: "/meeting/getdepartmentusers.do",
				type: "get",
				dataType: "json",
				success: function (data) {
					departmentAndSubjectUser.department = data["department"];
					departmentAndSubjectUser.subject = data["subject"];
				}
			}
		);
	};

	/*发布范围结束*/
	meet.init();
});