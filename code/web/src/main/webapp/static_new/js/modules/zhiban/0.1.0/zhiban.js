/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','initPaginator','role'],function(require,exports,module){
	/**
	 *初始化参数
	 */
	var zhiban = {},
		Common = require('common'),
	Paginator = require('initPaginator');
	require('fileupload');
	var someFileFailed = false;
	//提交参数
	var zhibanData = {};

	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 * zhiban.init()
	 */
	zhiban.init = function(){
		var nb_role = $('#role').val();
		Common.cal('calId');
		Common.leftNavSel();
		//设置初始页码
		zhibanData.page = 1;
		//设置每页数据长度
		zhibanData.pageSize = 12;
		zhiban.getNowFormatDate();
		//if (isK6ktHelper(nb_role) || isHeadMaster(nb_role)) {
		//	$("#tab-ZBJH").show();
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
		zhiban.selDutySetInfo(6);
			//zhiban.selDutyInfo(1);
			//zhiban.myDutyInfo();
		//} else {
			//$("#tab-ZBJH").hide();
			//zhiban.getNowFormatDate();
			//$(".LWDZB-I").show();
			//$(".LWDZB-II").hide();
			//zhiban.myDutyInfo(5);
			//$("#tab-MYZB").show();
		//}
		$("input:radio[name='zhiban-time']").change(function (){
			var tp = $("input[name='zhiban-time']:checked").val();
			zhibanData.type = tp;
			zhibanData.num = 0;
			//if (tp==1) {
			//	if ($('#min1').val()!='') {
			//		zhibanData.num = $('#min1').val();
			//	}
			//} else if(tp==2) {
				if ($('#min2').val()!='') {
					zhibanData.num = $('#min2').val();
				}
			//}
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			Common.getPostData('/duty/updDutySetTime.do', zhibanData,function(rep){
				if (rep.flag) {
					alert("修改成功！");
				} else {
					alert("修改失败！");
				}
			});
		});
		$(".ZBSM-edit").click(function(){
			zhiban.selDutySetInfo(6);
			$(".popup-ZBSM").show();
			$(".bg").show();

		})
		$(".SQ-X").click(function(){
			$(".popup-ZBSM").hide();
			$(".bg").hide();
		})
		$("#seach-myDuty").click(function(){
			zhiban.selMyDutyHistory(1);
		});
		$(".sure-log").click(function(){
			window.location.href="/duty/exportDutyLog.do?startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val()+"&name="+$('#name').val();
		});
		$("#sure-ip").click(function(){
			zhiban.updateDutySetIp();
		});
		$(".zb-jhgl-look").click(function(){
			zhiban.selAllDutyProject();
			$('#week').val($('#nowWeek').val());
			$('#year3').val($('#nowYear3').val());
			zhiban.selDutyUserSarlaryList(4,1);
			$(".zhiban-jhgl").hide();
			$(".zb-lookjl").show();
		});
		zhiban.fileUpload();
		$(".zb-jhgl-school").click(function(){
			//zhiban.selDutyUserSarlaryList(1);
			$(".zhiban-jhgl").hide();
			$(".zb-jl-tj").show();
		});
		$("#sure-explain").click(function(){
			zhiban.addDutyExplain();
		});

		$(".sure-shift").click(function(){
			zhibanData.dutyShiftId = $('#dsid').val();
			zhibanData.type = 1;
			zhibanData.userId=$('#dutyUser2').val();
			Common.getPostData('/duty/updDutyShiftInfo.do', zhibanData,function(rep) {
				if (rep.flag) {
					alert("替换成功！");
					$(".zhiban-meng").hide();
					$(".zb-set-alert").hide();
					zhiban.selShiftCheckList();
				} else {
					alert("替换失败！");
				}
			});
		});
		$(".sure3").click(function(){
			zhiban.selDutyUserSarlaryList(1,1);
		});
		$(".lastWeek").click(function(){
			if (parseInt($('#week').val())-1<=0) {
				$('#week').val(zhiban.getNumOfWeeks(parseInt($('#year3').val())-1)+1);
				$('#year3').val(parseInt($('#year3').val())-1);
			} else {
				$('#week').val(parseInt($('#week').val())-1);
			}
			zhiban.selDutyUserSarlaryList(3,1);
		});
		$("#lastWeek2").click(function(){
			if (parseInt($('#week').val())-1<=0) {
				$('#week').val(zhiban.getNumOfWeeks(parseInt($('#year3').val())-1)+1);
				$('#year3').val(parseInt($('#year3').val())-1);
			} else {
				$('#week').val(parseInt($('#week').val())-1);
			}
			zhiban.selMyDutyHistory(2);
		});
		$("#thisWeek2").click(function(){
			$('#week').val($('#nowWeek').val());
			$('#year3').val($('#nowYear3').val());
			zhiban.selMyDutyHistory(2);
		});
		$("#nextWeek2").click(function(){
			if (parseInt($('#week').val())>=zhiban.getNumOfWeeks($('#year3').val())+1) {
				$('#week').val(1);
				$('#year3').val(parseInt($('#year3').val())+1);
			} else {
				$('#week').val(parseInt($('#week').val())+1);
			}
			zhiban.selMyDutyHistory(2);
		});
		$(".thisWeek").click(function(){
			$('#week').val($('#nowWeek').val());
			$('#year3').val($('#nowYear3').val());
			zhiban.selDutyUserSarlaryList(4,1);
		});
		$(".nextWeek").click(function(){
			if (parseInt($('#week').val())>=zhiban.getNumOfWeeks($('#year3').val())+1) {
				$('#week').val(1);
				$('#year3').val(parseInt($('#year3').val())+1);
			} else {
				$('#week').val(parseInt($('#week').val())+1);
			}
			zhiban.selDutyUserSarlaryList(5,1);
		});
		$(".export-selDutyUser").click(function(){
			window.location.href="/duty/exportDutyInfoList.do?startTime="+$("#bTime").val()+"&endTime="+$("#eTime").val()+"&name="+$('#username').val();
		});

		$(".zhiban-back").click(function(){
			$(".zhiban-jhgl").show();
			$(".zb-lookjl").hide();
			$('.zb-jl-tj').hide();
		});

		$(".LWDJB-CK").click(function(){
			$('#week').val($('#nowWeek').val());
			$('#year3').val($('#nowYear3').val());
			zhiban.selMyDutyHistory(2);
			$(".LWDZB-I").hide();
			$(".LWDZB-II").show();
		});
		$(".LWDZB-II a").click(function(){
			$(".LWDZB-I").show();
			$(".LWDZB-II").hide();
		});
		$(".sure1").click(function(){
			zhiban.selDutyUserSarlaryList(2,1);
		});
		$(".export-duty1").click(function(){
			zhiban.exportDutyUserSarlaryList();
		});
		$(".zhiban-daochu").click(function(){
			window.location.href="/duty/exportShiftCheckList.do?startDate="+$("#bTime3").val()+"&endDate="+$("#sTime3").val()+"&name="+$('#username3').val();
		});
		$(".JBSQ-daochu").click(function(){
			window.location.href="/duty/exportMySarlaryList.do?year="+$("#year").val()+"&month="+$("#month").val();
		});

		$(".sure2").click(function(){
			zhiban.selShiftCheckList();
		});
		$("#search-salary").click(function(){
			zhiban.selMySarlaryList();
		});
		$(".submit-log").click(function(){
			zhiban.addDutyLog();
		});
		$("#project1").click(function(){
			zhibanData.dutyProjectId = $("#project1").val();
			Common.getPostData('/duty/selProjectByOrgId.do', zhibanData,function(rep) {
				$('#project2').empty();
				var sel = '';
				if (rep.dutyProject!=null && rep.dutyProject.length!=0) {
					for (var i=0;i<rep.dutyProject.length;i++) {
						sel += "<option value="+rep.dutyProject[i].id+">"+rep.dutyProject[i].content+"</option>";
					}
				}
				$('#project2').append(sel);
			});
		});
		$(".SQ-X").click(function(){
			$(".popup-HB").hide();
			$(".bg").hide();
			$(".popup-ZBJL").hide();
		});
		$(".HB-QX").click(function(){
			$(".popup-HB").hide();
			$(".bg").hide();
			$(".popup-ZBJL").hide();
		});

		//$('#min1').blur(function (){
		//	zhiban.updateNum(1,$('#min1').val());
		//});
		$('#min2').blur(function (){
			var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/
			if (!re.test($('#min2').val()))
			{
				alert("请输入数字");
				$('#min2').val("");
				return false;
			} else {
				zhiban.updateNum(1,$('#min2').val());
			}
		});
		$(".shift-submit").click(function(){
			zhibanData.dutyId = $('#dutyId2').val();
			zhibanData.cause = $('#cause').val();
			zhibanData.timeDesc = $('#time').html();
			Common.getPostData('/duty/addDutyShiftInfo.do', zhibanData,function(rep) {
				if(rep.flag) {
					alert("申请换班成功！");
					zhiban.myDutyInfo();
					$(".popup-HB").hide();
					$(".bg").hide();
				} else {
					alert("申请换班失败！");
				}
			});
		});
		$(".sure-plan").click(function(){
			zhibanData.modelId = $('#plan-model').val();
			Common.getPostData('/duty/useModel.do', zhibanData,function(rep) {
				if(rep.flag) {
					alert("模板使用成功！");
					zhibanData.week=$('#week').val();
					zhibanData.year=$('#year3').val();
					zhiban.selDutyInfo(1);
					$(".zhiban-meng").hide();
					$(".zb-plan-alert").hide();
				} else {
					alert("模板使用失败！");
				}
			});
		});
		$(".zb-jhgl-use").click(function(){
			if ($('#week').val()>$('#nowWeek').val()) {
				Common.getPostData('/duty/selDutyModelList.do', zhibanData,function(rep) {
					$('#plan-model').html('');
					Common.render({tmpl:$('#plan-model_templ'),data:rep,context:'#plan-model'});
				});
				$(".zhiban-meng").show();
				$(".zb-plan-alert").show();
			} else {
				alert("本周包括本周前都不可以使用模板！");
			}

		});
		$(".zb-jhgl-save").click(function(){
			$(".zhiban-meng").show();
			$(".zb-model-alert").show();
		});
		$(".zb-jhgl-manage").click(function(){
			zhiban.selDutyModelList();
			$(".JBSQ-II").show();
			$(".zhiban-jhgl").hide();
		});
		$(".zb-jhgl-sz").click(function(){
			if (parseInt($('#week').val())-1<=0) {
				$('#week').val(zhiban.getNumOfWeeks(parseInt($('#year3').val())-1)+1);
				$('#year3').val(parseInt($('#year3').val())-1);
			} else {
				$('#week').val(parseInt($('#week').val())-1);
			}
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			zhiban.selDutyInfo(1);
		});
		$(".zb-jhgl-zz").click(function(){
			$('#week').val($('#nowWeek').val());
			$('#year3').val($('#nowYear3').val());
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			zhiban.selDutyInfo(1);
		});
		$(".zb-jhgl-xz").click(function(){
			if (parseInt($('#week').val())>=zhiban.getNumOfWeeks($('#year3').val())+1) {
				$('#week').val(1);
				$('#year3').val(parseInt($('#year3').val())+1);
			} else {
				$('#week').val(parseInt($('#week').val())+1);
			}
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			zhiban.selDutyInfo(1);
		});
		$(".JBSQ-II-back").click(function(){
			$(".JBSQ-II").hide();
			$(".zhiban-jhgl").show();
		});

		$('.sure-model').click(function(){
			zhibanData.modelName = $.trim($('#modelname').val());
			zhibanData.modelId = $('#modelId').val();
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			if ($('#modelname').val().length == 0 || $.trim($('#modelname').val()) == '') {
				alert("模板名称不可为空！");
				return;
			}
			if ($('#modelId').val()=='') {
				Common.getPostData('/duty/addModel.do', zhibanData,function(rep) {
					if (rep.flag) {
						alert("新增成功！");
						$('#modelname').val('');
						$('#modelId').val('');
						$(".zb-model-alert").hide();
						$(".zhiban-meng").hide();
					} else {
						alert(rep.mesg);
					}
				});
			} else {
				Common.getPostData('/duty/updateDutyModel.do', zhibanData,function(rep) {
					if (rep.flag) {
						alert("修改成功！");
						$('#modelname').val('');
						$('#modelId').val('');
						$(".zb-model-alert").hide();
						$(".zhiban-meng").hide();
						zhiban.selDutyModelList();
					} else {
						alert(rep.mesg);
					}
				});
			}


		});
		$(".sure-duty").click(function(){
			zhibanData.dutyProject = $('#project2').val();
			//zhibanData.type = $('#dt-type').val();
			var uid = '';
			$('.dtuser a').each(function(i) {
				uid += $('.dtuser a').eq(i).attr('uid') + ',';
			});
			zhibanData.users = uid;
			zhibanData.index = $('#index').val();
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			Common.getPostData('/duty/addOrUpdDutyUserInfo.do', zhibanData,function(rep){
				if (rep.flag) {
					alert("修改成功！");
					$('.zb-ren-alert').hide();
					$(".zhiban-meng").hide();
					zhibanData.week=$('#week').val();
					zhibanData.year=$('#year3').val();
					zhiban.selDutyInfo(1);
				} else {
					alert("修改失败！");
				}
			});
		});

		$(".zb-set-close,.zb-btn-qx").click(function(){
			$(".zhiban-meng").hide();
			$(".zb-set-alert").hide();
			$(".zb-model-alert").hide();
			$(".zb-plan-alert").hide();

		})
		$(".SQ-X,.XZZB-QX").click(function(){
			$(".popup-XZZB").hide();
			$(".zhiban-meng").hide();
		});
		$(".add-users").click(function(){
			$('#poptype').val(1);
			$(".popup-XZZB").show();
			$(".zhiban-meng").show();
		});

		$(".add-project").click(function(){
			$('#conn').val('');
			$('#projectlist').val('');
			$(".zhiban-meng").show();
			$(".zb-set-newadd").show();
		})
		$(".sure-project").click(function(){
			if ($.trim($('#conn').val())=='') {
				alert("值班项目不能为空！");
				return;
			}
			zhibanData.dutyId = $('#dutyId').val();
			zhibanData.orgDutyProjectId = $('#projectlist').val();
			zhibanData.content = $('#conn').val();
			zhibanData.dutyProjectId = $('#dutyProjectId').val();
			Common.getPostData('/duty/addOrUdpDutyProject.do', zhibanData,function(rep){
				if(rep.flag) {
					if (rep.count==0) {
						if ($('#dutyProjectId').val()=='') {
							alert("新增成功！");
						} else {
							alert("修改成功！");
						}
						$(".zhiban-meng").hide();
						$(".zb-set-newadd").hide();
						zhiban.selDutySetInfo(3);
					} else {
						alert("值班项目不能重复！");
					}

				} else {
					alert("新增失败！");
				}
			});
		});
		$(".zb-set-close,.zb-btn-qx").click(function(){
			$(".zhiban-meng").hide();
			$(".zb-set-newadd").hide();
		})
		$(".zb-set-close,.zb-btn-qx").click(function(){
			$(".zhiban-meng").hide();
			$(".zb-ren-alert").hide();
		});
		$(".XZZB-TJ").click(function(){
			if ($('#poptype').val()==1) {
				var bol = false;
				$('.zb-set-ren img').each(function(i) {
					if ($('.zb-set-ren img').eq(i).attr('uid')==$('#dutyUser').val()){
						bol = true;
					}
				});
				if (bol) {
					alert("该人员已存在！");
				} else {
					zhibanData.userId = $('#dutyUser').val();
					zhibanData.week=$('#week').val();
					zhibanData.year=$('#year3').val();
					Common.getPostData('/duty/addDutyUser.do', zhibanData,function(rep){
						if (rep.flag) {
							alert("添加成功！");
							$(".popup-XZZB").hide();
							$(".zhiban-meng").hide();
							zhiban.selDutySetInfo(1);
						} else {
							alert("添加失败！");
						}
					});
				}
			} else {
				var flg = false;
				$('.dtuser a').each(function(i) {
					if($('.dtuser a').eq(i).attr('uid')==$('#dutyUser').val()) {
						flg = true;
					}
				});
				if (!flg) {
					$('.dtuser').append("<a uid="+$('#dutyUser').val()+">"+$("#dutyUser option:selected").text()+"<em class='zb-set-del ur-del'>X</em></a>");
					$('.popup-XZZB').hide();
					$(".ur-del").click(function(){
						$(this).parent().remove();
					});
				} else {
					alert('该值班人员已存在！');
				}
			}
		});
		$(".tab-head li").click(function(){
			$(".tab-head li").removeClass("cur");
			$(this).addClass("cur");
			$(".tab-WDZB>div").hide();
			var name = $(this).attr("id");
			if (name=='ZBJH') {
				zhibanData.week=$('#nowWeek').val();
				zhibanData.year=$('#nowYear3').val();
				$('#week').val($('#nowWeek').val());
				$('#year3').val($('#nowYear3').val());
				zhiban.selDutyInfo(1);
			} else if (name=='ZBXCGL') {
				zhiban.selAllDutyProject();
				zhiban.selDutyUserSarlaryList(2,1);
			} else if (name=='HBSH') {
				zhiban.selShiftCheckList();
			} else if (name=='ZBXC') {
				zhiban.selMySarlaryList();
			} else if (name=='ZBSM') {
				zhiban.selDutySetInfo(6);
			} else if (name=='ZBSZ') {
				zhiban.selDutySetInfo(1);
				zhiban.dutyUser();
				$(".zhiban-set-title li").removeClass("zb-active");
				$('#ZBREN').addClass("zb-active");
				$(".zb-set>div").hide();
				$("#tab-ZBREN").show();
			} else if (name=='MYZB') {
				zhiban.getNowFormatDate();
				$(".LWDZB-I").show();
				$(".LWDZB-II").hide();
				zhiban.myDutyInfo(5);
			}
			$("#" + "tab-" + name).show();
		})
		$(".zhiban-set-title li").click(function(){
			$(".zhiban-set-title li").removeClass("zb-active");
			$(this).addClass("zb-active")
			$(".zb-set>div").hide();
			var $name = $(this).attr("id");
			if ($name=='ZBREN') {
				zhiban.selDutySetInfo(1);
			} else if ($name=='ZBSD') {
				zhiban.selDutySetInfo(2);
			} else if ($name=='ZBXM') {
				zhiban.selDutySetInfo(3);
			} else if ($name=='ZBTIME') {
				zhiban.selDutySetInfo(4);
			} else if ($name=='ZBIP') {
				zhiban.selDutySetInfo(5);
			}

			$("#" + "tab-" + $name).show();
		});

		$(".zbsdsd").click(function(){
			var str = "<li class='set-sd-only'><input class='set-sd-type' id='set-nm'><input type='text' class='zb-input' id='zb-starttime' onfocus=\"WdatePicker({dateFmt:'HH:mm'})\" class='Wdate Wdate-enter'><em> - </em>" +
				"<input type='text' class='zb-input' onfocus=\"WdatePicker({dateFmt:'HH:mm'})\" id='zb-endtime' class='Wdate Wdate-enter'><div class='zb-set-cz'><a class='zb-set-save zb-set-sd-save'>保存</a><i> | </i><a class='zb-set-del zb-set-sd-del'>删除</a></div></li>";
			$('.zbsdsd_ul').append(str);
			zhiban.saveTime();
			$(".zb-set-sd-del").click(function(){
				$(this).parent().parent().remove();
			});

		});
	};

	zhiban.selMySarlaryList = function() {
		zhibanData.year= $('#year').val();
		zhibanData.month= $('#month').val();
		Common.getPostData('/duty/selMySarlaryList.do', zhibanData,function(rep) {
			$('.salary-list').html('');
			if (rep.rows!=null) {
				Common.render({tmpl:$('#salary-list_templ'),data:rep,context:'.salary-list'});
			}
		});
	}
	zhiban.selAllDutyProject = function() {
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
		Common.getPostData('/duty/selAllDutyProject.do', zhibanData,function(rep){
			$('#projectid').empty();
			var sel2 = '<option value="">全部</option>';
			if (rep.rows!=null && rep.rows.length!=0) {
				for (var i=0;i<rep.rows.length;i++) {
					sel2 += "<option value="+rep.rows[i].id+">"+rep.rows[i].content+"</option>";
				}
			}
			$('#projectid').append(sel2);
			//$('#projectid2').empty();
			//var sel = '<option value="">全部</option>';
			//if (rep.rows!=null && rep.rows.length!=0) {
			//	for (var i=0;i<rep.rows.length;i++) {
			//		sel += "<option value="+rep.rows[i].id+">"+rep.rows[i].content+"</option>";
			//	}
			//}
			//$('#projectid2').append(sel);
		});
	}
	zhiban.selMyDutyHistory = function(type) {
		zhibanData.type = type;
		zhibanData.week=$('#week').val();
		zhibanData.curYear=$('#year3').val();
		zhibanData.year = $('#year2').val();
		zhibanData.month = $('#month2').val();
		Common.getPostData('/duty/selMyDutyHistory.do', zhibanData,function(rep){
			$('.my-duty').html('');
			Common.render({tmpl:$('#my-duty_templ'),data:rep,context:'.my-duty'});
		});
	}
	zhiban.addDutyLog = function() {
		zhibanData.dutyUserId = $('#dutyUserId').val();
		zhibanData.log = $('#dutylog').val();
		var flist = '';
		var fname = '';
		$(".popup-bottom-LAB label").each(function() {
			var srcPath=$(this).attr('path');
			var nm = $(this).attr('nm');
			flist += srcPath + ',';
			fname += nm+ ',';
		});
		zhibanData.filepath = flist;
		zhibanData.realName = fname;
		Common.getPostData('/duty/addDutyLog.do', zhibanData,function(rep) {
			if (rep.flag) {
				alert("修改值班记录成功！");
				zhiban.myDutyInfo();
				$(".popup-ZBJL").hide();
				$(".bg").hide();
			} else {
				alert("修改值班记录失败！");
			}
		});
	}
	zhiban.selShiftCheckList = function() {
		zhibanData.startDate = $('#bTime3').val();
		zhibanData.endDate = $('#sTime3').val();
		zhibanData.name = $('#username3').val();
		Common.getPostData('/duty/selShiftCheckList.do', zhibanData,function(rep) {
			$('.zhiban-table').html('');
			Common.render({tmpl:$('#zhiban-table_templ'),data:rep,context:'.zhiban-table'});
			$(".zb-hb-xq").click(function(){
				$('#cause2').html($(this).attr('cus'));
				$('#username5').html($(this).attr('duser'));
				$('#project5').html($(this).attr('dtp'));
				$('#timeduan2').html($(this).attr('dtduan'));
				$('#time2').html($(this).attr('dtd'));
				$(".request-huanban2").show();
				$(".bg").show();
			});
			$(".tongguo").click(function(){
				$('#cause4').html($(this).attr('cus'));
				$('#username4').html($(this).attr('duser'));
				$('#project4').html($(this).attr('dtp'));
				$('#dateDesc4').html($(this).attr('dtd'));
				$('#date4').html($(this).attr('dt'));
				$('#dsid').val($(this).attr('dsid'));
				var userId = $(this).attr('duid');
				//zhiban.dutyUser();
				zhibanData.type=1;
				Common.getPostData('/duty/selDutySetInfo.do', zhibanData,function(rep){
					$('#dutyUser2').empty();
					var sel = '';
					if (rep.userlist!=null && rep.userlist.length!=0) {
						for (var i=0;i<rep.userlist.length;i++) {
							if (rep.userlist[i].id!=userId) {
								sel += "<option value="+rep.userlist[i].id+">"+rep.userlist[i].userName+"</option>";
							}
						}
					}
					$('#dutyUser2').append(sel);
				});
				$(".zhiban-meng").show();
				$(".zb-set-alert").show();
			});
			$(".bohui").click(function(){
				if (confirm('是否确认驳回！')) {
					zhibanData.dutyShiftId = $(this).attr("dsid");
					zhibanData.type = 2;
					zhibanData.userId = "";
					Common.getPostData('/duty/updDutyShiftInfo.do', zhibanData, function (rep) {
						if (rep.flag) {
							alert("驳回成功！");
							zhiban.selShiftCheckList();
						} else {
							alert("驳回失败！");
						}
					});
				}
			});


		});
	}
	zhiban.exportDutyUserSarlaryList = function() {
		//zhibanData.startTime = $('#sTime2').val();
		//zhibanData.endTime = $('#bTime2').val();
		//zhibanData.dutyProjectId = $('#projectid2').val();
		//zhibanData.name = $('#userName2').val();
		window.location.href="/duty/exportDutyUserSarlaryList.do?startTime="+$("#sTime2").val()+"&endTime="+$("#bTime2").val()+"&name="+$('#userName2').val();
	}
	zhiban.selDutyUserSarlaryList = function(type,page) {
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
		zhibanData.page = page;
		if (type==1) {
			zhibanData.type = 0;
			zhibanData.startTime = $('#bTime').val();
			zhibanData.endTime = $('#eTime').val();
			//zhibanData.dutyProjectId = $('#projectids').val();
			zhibanData.name = $('#username').val();
		} else if (type==2) {
			zhibanData.type = 0;
			zhibanData.startTime = $('#sTime2').val();
			zhibanData.endTime = $('#bTime2').val();
			zhibanData.name = $('#userName2').val();
		} else if (type==4) {
			zhibanData.week=$('#nowWeek').val();
			zhibanData.year=$('#nowYear3').val();
			zhibanData.type = 1;
			zhibanData.startTime = "";
			zhibanData.endTime = "";
			zhibanData.name = "";
		} else {
			zhibanData.type = 1;
			zhibanData.startTime = "";
			zhibanData.endTime = "";
			zhibanData.name = "";
		}
		Common.getPostData('/duty/selDutyUserSarlaryList.do', zhibanData,function(rep) {
			if (type==2) {
				$('.zb-table-pay').html('');
				if (rep.rows!=null && rep.rows.length!=0) {
					Common.render({tmpl:$('#zb-table-pay_templ'),data:rep,context:'.zb-table-pay'});
				}
				$('.zb-pay-num').blur(function (){
					zhibanData.dutyUserId = $(this).parent().parent().attr('puid');
					zhibanData.sarlay = $(this).val();
					var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/
						if (!re.test($(this).val()))
						{
							alert("请输入数字");
							$(this).val("");
							return false;
						}
					Common.getPostData('/duty/updateSarlary.do', zhibanData,function(rep) {
						if (rep.flag) {
							alert("薪酬修改成功！");
						} else {
							alert("薪酬修改失败！");
						}
					});
				});
			} else if (type==1 || type==3 || type==4 || type==5) {
				$('.zb-jl-table').html('');
				if (rep.rows!=null && rep.rows.length!=0) {
					Common.render({tmpl: $('#zb-jl-table_templ'), data: rep, context: '.zb-jl-table'});
				}
				$('.view').click(function() {
					var url = $(this).attr("pth");
					var urlarg = url.split(".")[url.split(".").length-1];
					if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
						window.open("http://ow365.cn/?i=9666&furl="+url);
					} else {
						window.open(url);
					}
				});
			}
			var option = {
				total: rep.total,
				pagesize: 12,
				currentpage: rep.page,
				operate: function (totalPage) {
					$('.page-index span').each(function () {
						$(this).off("click");
						$(this).one("click",function () {
							zhiban.selDutyUserSarlaryList(type,$(this).text());
						});
					});
					$('.first-page').off("click");
					$('.first-page').one("click",function () {
						zhiban.selDutyUserSarlaryList(type,1);
					});
					$('.last-page').off("click");
					$('.last-page').one("click",function () {
						zhiban.selDutyUserSarlaryList(type,totalPage);
					});
				}
			};
			Paginator.initPaginator(option);

		});
	}
	zhiban.selDutyModelList = function() {
		Common.getPostData('/duty/selDutyModelList.do', zhibanData,function(rep) {
			$('#model-table').html('');
			Common.render({tmpl:$('#model-table_templ'),data:rep,context:'#model-table'});
			$(".del-model").click(function(){
				if (confirm("确定删除？")){
					zhibanData.modelId = $(this).attr('mid');
					Common.getPostData('/duty/delDutyModel.do', zhibanData,function(rep) {
						if (rep.flag) {
							alert("删除成功！");
							zhiban.selDutyModelList();
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
				$(".zb-model-alert").show();
			});
		});
	}
	zhiban.myDutyInfo = function(type) {
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
		Common.getPostData('/duty/selMyDutyInfo.do', zhibanData,function(rep){
			$('#myduty-list').html('');
			Common.render({tmpl:$('#myduty-list_templ'),data:rep,context:'#myduty-list'});
			$(".LWDJB-HB").click(function(){
				$('#time').html($(this).attr('dtd'));
				$('#timeduan').html($(this).attr('dtduan'));
				$('#project').html($(this).attr('dtp'));
				$('#dutyId2').val($(this).attr('duid'));
				$('#cause').val('');
				zhibanData.dutyId = $(this).attr('duid');
				Common.getPostData('/duty/selDutyShiftDetail.do', zhibanData,function(rep){
					$('#cause').html(rep.cause);
				});
				$(".request-huanban").show();
				$(".bg").show();
			});
			$(".LWDJB-QD").click(function(){
				zhiban.checkInOut(1,$(this).attr('duid'));
			});
			$(".LWDJB-QT").click(function(){
				zhiban.checkInOut(2,$(this).attr('duid'));
			});
			$(".LWDJB-TX").click(function(){
				$('#project6').html($(this).attr("tdp"));
				$('#timeDuan6').html($(this).attr("tda"));
				$('#timeDesc6').html($(this).attr("tde"));
				$('#dutyUserId').val($(this).attr("duid"));
				zhibanData.dutyUserId = $(this).attr("duid");
				Common.getPostData('/duty/selLocalIp.do', zhibanData,function(rep){
					$('#localip').html(rep.ip);
					$('#dutylog').text(rep.content);
					if (rep.files!=null && rep.files.length!=0) {
						//sessionStorage.setItem("realname",rep.filePath);
						//sessionStorage.setItem("filename",rep.realName);
						$(".popup-bottom-LAB").html('');
						for (var i=0;i<rep.files.length;i++) {
							var uf = '<label style="padding-left: 50px;" path="'+rep.files[i].path+'" nm="'+rep.files[i].name+'">'+rep.files[i].name+'<a class="yulan" style="padding-left:10px;" pth='+rep.files[i].path+'>预览</a>'+'<span  class="h-cursor del-file">删除</span></label>';
							$(".popup-bottom-LAB").append(uf);
						}
						$('.yulan').click(function() {
							var url = $(this).attr("pth");
							var urlarg = url.split(".")[url.split(".").length-1];
							if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
								window.open("http://ow365.cn/?i=9666&furl="+url);
							} else {
								window.open(url);
							}
						});
						$(".del-file").click(function(){
							//sessionStorage.clear();
							$(this).parent().remove();
						});
					}
					//$('#localip').html(rep.rows.ip);
				});
				$(".popup-ZBJL").show();
				$(".bg").show();
			});
		});
	}
	zhiban.checkInOut = function(type,id) {
		zhibanData.type = type;
		zhibanData.dutyId = id;
		Common.getPostData('/duty/checkInOut.do', zhibanData,function(rep){
			if(rep.flag) {
				if (type==1) {
					alert("签到成功！");
				} else {
					alert("签退成功！");
				}
				zhiban.myDutyInfo();
			} else {
				if (type==1) {
					alert(rep.mesg);
				}  else{
					alert("签退失败！");
				}
			}
		});
	}
	zhiban.selDutyInfo = function(type) {
		Common.getPostData('/duty/selDutyList.do', zhibanData,function(rep){
			if (type==1) {
				if (rep.flag==0) {
					$('.zb-jhgl-table').html('');
					Common.render({tmpl:$('#zb-jhgl-table_templ'),data:rep,context:'.zb-jhgl-table'});
					if ($('#year3').val()<=$('#nowYear3').val() && $('#week').val()<$('#nowWeek').val()) {
						$('.zb-jhgl-add').hide();
						$('.zb-td-edit').hide();
					} else {
						$('.zb-jhgl-add').show();
						$('.zb-td-edit').show();
					}
					$(".zb-td-edit").click(function(){
						zhibanData.type=2;
						zhibanData.num = $(this).attr('idx');
						$('#index').val($(this).attr('idx'));
						$('#dt-type').val(2);
						zhibanData.week=$('#week').val();
						zhibanData.year=$('#year3').val();
						Common.getPostData('/duty/selSingleDuty.do', zhibanData,function(rep){
							$('#info1').html(rep.time);
							$('#info2').html(rep.desc);
							$('#project1').empty();
							var sel2 = '';
							if (rep.orgDutyProject!=null && rep.orgDutyProject.length!=0) {
								for (var i=0;i<rep.orgDutyProject.length;i++) {
									sel2 += "<option value="+rep.orgDutyProject[i].id+">"+rep.orgDutyProject[i].content+"</option>";
								}
							}
							$('#project1').append(sel2);
							$('#project2').empty();
							var sel = '';
							if (rep.dutyProject!=null && rep.dutyProject.length!=0) {
								for (var i=0;i<rep.dutyProject.length;i++) {
									sel += "<option value="+rep.dutyProject[i].id+">"+rep.dutyProject[i].content+"</option>";
								}
							}
							$('#project2').append(sel);
							$('.dtuser').empty();
							$('.dtuser').append("<a class='zb-ren-add' uid=''>&nbsp;</a>");
							if (rep.users!=null && rep.users.length>0) {
								for (var i=0;i<rep.users.length;i++) {
									$('.dtuser').append("<a uid="+rep.users[i].id+">"+rep.users[i].userName+"<em class='zb-set-del ur-del'>X</em></a>");
								}
								$(".ur-del").click(function(){
									$(this).parent().remove();
								});
							}
						});
						$(".zb-ren-add").click(function(){
							zhibanData.type=1;
							Common.getPostData('/duty/selDutySetInfo.do', zhibanData,function(rep){
								$('#dutyUser').empty();
								var sel = '';
								if (rep.userlist!=null && rep.userlist.length!=0) {
									for (var i=0;i<rep.userlist.length;i++) {
										sel += "<option value="+rep.userlist[i].id+">"+rep.userlist[i].userName+"</option>";
									}
								}
								$('#dutyUser').append(sel);
							});
							$('#poptype').val(2);
							$(".popup-XZZB").show();
						});
						$(".zhiban-meng").show();
						$(".zb-ren-alert").show();
					});
					$(".zb-jhgl-add").click(function(){
						zhibanData.type=1;
						zhibanData.num = $(this).attr('idx');
						$('#index').val($(this).attr('idx'));
						$('#dt-type').val(1);
						Common.getPostData('/duty/selSingleDuty.do', zhibanData,function(rep){
							$('#project1').empty();
							var sel2 = '';
							if (rep.orgDutyProject!=null && rep.orgDutyProject.length!=0) {
								for (var i=0;i<rep.orgDutyProject.length;i++) {
									sel2 += "<option value="+rep.orgDutyProject[i].id+">"+rep.orgDutyProject[i].content+"</option>";
								}
							}
							$('#project1').append(sel2);
							$('#project2').empty();
							var sel = '';
							if (rep.dutyProject!=null && rep.dutyProject.length!=0) {
								for (var i=0;i<rep.dutyProject.length;i++) {
									sel += "<option value="+rep.dutyProject[i].id+">"+rep.dutyProject[i].content+"</option>";
								}
							}
							$('#project2').append(sel);
							$('.dtuser').empty();
							$('.dtuser').append("<a class='zb-ren-add' uid=''>&nbsp;</a>");
							$('#info1').html(rep.time);
							$('#info2').html(rep.desc);
						});
						$(".zhiban-meng").show();
						$(".zb-ren-alert").show();
						$(".zb-ren-add").click(function(){
							//zhiban.dutyUser();
							zhibanData.type=1;
							Common.getPostData('/duty/selDutySetInfo.do', zhibanData,function(rep){
								$('#dutyUser').empty();
								var sel = '';
								if (rep.userlist!=null && rep.userlist.length!=0) {
									for (var i=0;i<rep.userlist.length;i++) {
										sel += "<option value="+rep.userlist[i].id+">"+rep.userlist[i].userName+"</option>";
									}
								}
								$('#dutyUser').append(sel);
							});
							$('#poptype').val(2);
							$(".popup-XZZB").show();
						});
					});
				} else {
					$('#week').val(parseInt($('#week').val())+1);
					alert("已经最前一周！");
				}

			}
		});
	}
	zhiban.updateNum = function(type,num) {
		if (type==$("input[name='zhiban-time']:checked").val()) {
			zhibanData.type = type;
			zhibanData.num = num;
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			Common.getPostData('/duty/updDutySetTime.do', zhibanData,function(rep){
				if (rep.flag) {
					alert("修改成功！");
				} else {
					alert("修改失败！");
				}
			});
		}
	}

	zhiban.updateDutySetIp = function() {
		zhibanData.ip = $('#ipset').val();
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
			Common.getPostData('/duty/updateDutySetIp.do', zhibanData,function(rep){
				if (rep.flag) {
					alert("保存成功！");
				} else {
					alert("保存失败！");
				}
			});
	}
	zhiban.dutyUser = function() {
		Common.getPostData('/duty/selDutyUsers.do', zhibanData,function(rep){
			$('#dutyUser').html('');
			Common.render({tmpl:$('#dutyUser_templ'),data:rep,context:'#dutyUser'});
			$('#dutyUser2').html('');
			Common.render({tmpl:$('#dutyUser2_templ'),data:rep,context:'#dutyUser2'});
		});
	}
	zhiban.selDutySetInfo = function(idx) {
		zhibanData.type=idx;
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
		Common.getPostData('/duty/selDutySetInfo.do', zhibanData,function(rep){
			$('#dutyId').val(rep.dutyId);
			if (idx==1) {
				$('.zb-set-ren').html('');
				Common.render({tmpl:$('#user_templ'),data:rep,context:'.zb-set-ren'});
				if ($('#week').val()<$('#nowWeek').val()) {
					$('.add-users').hide();
					$('.user-del').hide();
				} else {
					$('.add-users').show();
					$('.user-del').show();
				}
				zhiban.dutyUser();
				$(".user-del").click(function(){
					if (confirm("确定删除？")) {
						zhibanData.userId = $(this).attr('uid');
						zhibanData.week=$('#week').val();
						zhibanData.year=$('#year3').val();
						Common.getPostData('/duty/missDutyUser.do', zhibanData, function (rep) {
							if (rep.flag) {
								alert("删除成功！");
								zhiban.selDutySetInfo(1);
							} else {
								alert("删除失败！");
							}
						});
					}
				});
			} else if (idx==2) {
				$('.zbsdsd_ul').html('');
				Common.render({tmpl:$('#zbsdsd_ul_templ'),data:rep,context:'.zbsdsd_ul'});
				if ($('#week').val()<$('#nowWeek').val()) {
					$('.zbsdsd').hide();
					$('.zb-set-cz').hide();
				} else {
					$('.zbsdsd').show();
					$('.zb-set-cz').show();
				}
				$(".zb-set-sd-edit").click(function(){
					//$(this).parent().parent().find('input').get(0).value;
					//$(this).parent().parent().find('input').get(0).disabled='';
					var dom = $(this).parent().parent().find('input');
					var dom = $(this).parent().parent().find('input');
					dom.each(function(i) {
						dom.get(i).disabled='';
					});
					$(this).html("保存");
					$(this).removeClass("zb-set-sd-edit");
					$(this).addClass("zb-set-sd-save");
					zhiban.saveTime();
				});
				$(".zb-set-sd-del").click(function(){
					if (confirm("删除会导致值班列表删除，确定删除？")) {
						zhibanData.dutyTimeId = $(this).parent().attr("did");
						zhibanData.week=$('#week').val();
						zhibanData.year=$('#year3').val();
						Common.getPostData('/duty/delDutyTimeInfo.do', zhibanData, function (rep) {
							if (rep.flag) {
								alert("删除成功！");
								zhiban.selDutySetInfo(2);
							} else {
								alert("删除失败！");
							}
						});
					}
				});
			} else if (idx==4) {
				if ($('#week').val()<$('#nowWeek').val()) {
					$('.zb-time-txt').attr("readonly",true);
					$("[name='zhiban-time']").attr("readonly",true);
				} else {
					$('.zb-time-txt').attr("readonly",false);
					$("[name='zhiban-time']").attr("readonly",false);
				}
				var n = rep.type-1;
				$("input[name=zhiban-time]:eq("+n+")").attr("checked",'checked');
				if (rep.type==1) {
					$('#min2').val(rep.num);
				}
			} else if (idx==5) {
				if ($('#week').val()<$('#nowWeek').val()) {
					$('#sure-ip').hide();
				} else {
					$('#sure-ip').show();
				}
				$('#ipset').val(rep.ip);
			} else if (idx==6) {
				$('#dutyExplain').html(rep.explain);
				$('#explain').val(rep.explain);
			} else if(idx==3) {
				$('.zb-set-xm').html('');
				Common.render({tmpl:$('#zb-set-xm_templ'),data:rep,context:'.zb-set-xm'});
				if ($('#week').val()<$('#nowWeek').val()) {
					$('.add-project').hide();
					$('.project-dis').hide();

				} else {
					$('.add-project').show();
					$('.project-dis').show();
				}
				zhibanData.week=$('#week').val();
				zhibanData.year=$('#year3').val();
				Common.getPostData('/duty/selProjectList.do', zhibanData,function(rep){
					$('#projectlist').html('');
					Common.render({tmpl:$('#projectlist_templ'),data:rep,context:'#projectlist'});
				});
				$(".pro-edit").click(function(){
					$('#projectlist').val($(this).attr('dpoid'));
					$('#dutyProjectId').val($(this).attr('dpid'));
					$('#conn').val($(this).attr('dpcn'));
					$(".zhiban-meng").show();
					$(".zb-set-newadd").show();
				});
				$(".pro-del").click(function(){
					if (confirm("确定删除？")) {
						zhibanData.dutyProjectId = $(this).attr("dpid");
						zhibanData.dutyOrgProjectId = $(this).attr("dpoid");
						Common.getPostData('/duty/delDutyProject.do', zhibanData, function (rep) {
							if (rep.flag) {
								alert("删除成功！");
								zhiban.selDutySetInfo(3);
							} else {
								alert("删除失败！");
							}
						});
					}
				});
			}
		});
	}

	zhiban.saveTime = function() {
		$(".zb-set-sd-save").click(function(){
			zhibanData.dutyId = $('#dutyId').val();
			var dom = $(this).parent().parent().find('input');
			dom.each(function(i) {
				if (i==0) {
					zhibanData.timeDesc = dom.get(i).value;
				} else if (i==1) {
					zhibanData.startTime = dom.get(i).value;
				} else if (i==2) {
					zhibanData.endTime = dom.get(i).value;
				}
			});
			zhibanData.dutyTimeId = $(this).parent().attr('did');
			zhibanData.week=$('#week').val();
			zhibanData.year=$('#year3').val();
			Common.getPostData('/duty/addOrUdpDutyTimeInfo.do', zhibanData,function(rep){
				if (rep.flag) {
					alert("修改成功！");
					zhiban.selDutySetInfo(2);
				} else {
					alert("修改失败！");
				}
			});
		});
	}
	/*
	 * 上传校园安全附件信息
	 * */
	zhiban.fileUpload = function(id) {
		//上传附件
		$('#file_attach').fileupload({
			url: '/duty/uploadattach.do',
			start: function(e) {
				$('#fileuploadLoading').show();
			},
			done: function(e, data) {
				if (data.dataType == 'iframe ') {
					var response = $( 'pre', data.result ).text();
				} else {
					var response = data.result;
				}
				try {
					var ob = response;
					if (ob.uploadType != 1) {
						alert("附件上传失败！");
					}
					//sessionStorage.setItem("realname",ob.vurl);
					//sessionStorage.setItem("filename",ob.name);
					//$(".popup-bottom-LAB").html('');
					var uf = '<label style="padding-left: 50px;" path="'+ob.vurl+'" nm="'+ob.name+'">'+ob.name+'<a class="yulan" style="padding-left:10px;" pth='+ob.vurl+'>预览</a>'+'<span  class="h-cursor del-file">删除</span></label>';
					$(".popup-bottom-LAB").append(uf);
					$('.yulan').click(function() {
						var url = $(this).attr("pth");
						var urlarg = url.split(".")[url.split(".").length-1];
						if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
							window.open("http://ow365.cn/?i=9666&furl="+url);
						} else {
							window.open(url);
						}
					});
					$(".del-file").click(function(){
						//sessionStorage.clear();
						$(this).parent().remove();
					});
				} catch (err) {
				}
			},
			fail: function (e, data) {

			},
			always: function (e, data) {
				$('#fileuploadLoading').hide();
			}
		});
	}
	zhiban.addDutyExplain = function() {
		if ($('#explain').val().length == 0 || $('#explain').val() == '') {
			alert("值班说明不可为空！");
			return;
		}
		zhibanData.week=$('#week').val();
		zhibanData.year=$('#year3').val();
		zhibanData.explain = $('#explain').val();
		Common.getPostData('/duty/addDutyExplain.do', zhibanData,function(rep){
			if (rep.flag) {
				alert("编辑成功！");
				zhiban.selDutySetInfo(6);
				$('.popup-ZBSM').hide();
				$(".bg").hide();
			} else {
				alert("编辑失败！");
			}
		});
	}
	zhiban.getNowFormatDate = function() {
		var date = new Date();
		var seperator1 = "/";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		$('#month').val(month);
		$('#month2').val(month);
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
			+ " " + date.getHours() + seperator2 + date.getMinutes()
			+ seperator2 + date.getSeconds();
		$('#curtime').html(currentdate);
	}

	zhiban.theWeek = function() {
		var totalDays = 0;
		var now = new Date();
		var years = now.getYear()
		if (years < 1000)
			years += 1900
		var days = new Array(12);
		days[0] = 31;
		days[2] = 31;
		days[3] = 30;
		days[4] = 31;
		days[5] = 30;
		days[6] = 31;
		days[7] = 31;
		days[8] = 30;
		days[9] = 31;
		days[10] = 30;
		days[11] = 31;

		//判断是否为闰年，针对2月的天数进行计算
		if (Math.round(now.getYear() / 4) == now.getYear() / 4) {
			days[1] = 29
		} else {
			days[1] = 28
		}

		if (now.getMonth() == 0) {
			totalDays = totalDays + now.getDate();
		} else {
			var curMonth = now.getMonth();
			for (var count = 1; count <= curMonth; count++) {
				totalDays = totalDays + days[count - 1];
			}
			totalDays = totalDays + now.getDate();
		}
		//得到第几周
		var week = Math.round(totalDays / 7);
		return week;
	}
	zhiban.getNumOfWeeks = function(year){
		var d=new Date(year,0,1);
		var yt=( ( year%4==0 && year%100!=0) || year%400==0)?366:365;
		return Math.ceil((yt-d.getDay())/7.0);
	}

	zhiban.init();
});