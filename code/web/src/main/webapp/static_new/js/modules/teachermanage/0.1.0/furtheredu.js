/*
 * @Author: Tony
 * @Date:   2016-02-29 14:24:31
 * @Last Modified by: wang_xinxin
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','pagination'],function(require,exports,module){
	/**
	 *初始化参数
	 */
	var manage = {},
		Common = require('common');
		require('pagination');
	//提交参数
	var manageData = {};
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 * yunying.init()
	 */
	manage.init = function(){


		manageData.page=1;
		$(".addInfo").click(function(){
			manageData.id=$("#userid").val();
			manageData.type=$("#etype").val();
			manageData.name=$("#teacher-name").text();
			manageData.area=$("#area").val();
			manageData.username=$("#realname").val();
			manageData.userrole=$("#userRole").val();
			var ismanage = 0;
			if(document.getElementById("ismanage").checked) {
				ismanage = 1;
			} else {
				ismanage = 0;
			}
			manageData.ismanage=ismanage;
			manageData.sex=$("#sex").val();
			manageData.birth=$("#birth").val();
			manageData.place=$("#place").val();
			manageData.nation=$("#nation").val();
			manageData.card=$("#card").val();
			manageData.cardnumber=$("#cardnum").val();
			manageData.vail=$("#vail").val();
			manageData.maritalstatus=$("#maritalstatus").val();
			manageData.political=$("#political").val();
			manageData.registerplace=$("#registerplace").val();
			manageData.adress=$("#adress").val();
			manageData.nowadress=$("#nowadress").val();
			manageData.zipcode=$("#zipcode").val();
			//manageData.phone=$("#phone").val();
			manageData.contact=$("#contact").val();
			manageData.contactphone=$("#contactphone").val();
			manageData.email=$("#email").val();
			manageData.education=$("#education").val();
			manageData.major=$("#major2").val();
			manageData.deutime=$("#deutime").val();
			manageData.degree=$("#degree2").val();
			manageData.degtime=$("#degtime").val();
			manageData.jobtime=$("#jobtime").val();
			manageData.schooltime=$("#schooltime").val();
			manageData.teachtime=$("#teachtime").val();
			manageData.postiondec =$('#postiondec').val();
			manageData.teachSubject=$("#teachSubject").val();
			manageData.mandarinlevel=$("#mandarinlevel").val();
			//manageData.teacerticode=$("#teacerticode").val();
			manageData.teachernumber=$("#teachernumber").val();

			manageData.organization=$("#organization").val();
			manageData.introduction=$("#introduction").val();
			if($('#name3').val() == ''){
				alert("请输入用户名。");
				return;
			}
			if($('#postiondec').val() == ''){
				alert("请输入职务。");
				return;
			}
			if(manage.getLength($('#postiondec').val()) > 30){
				alert('职务最多15个字符！');
				return;
			}
			if($('#realname').val() == ''){
				alert("请输入姓名。");
				return;
			}

			var tab1 = '';
			$("#addTr").find("tr").each(function(){
				var tdArr = $(this).children();
				tab1 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+','+tdArr.eq(4).html()+';';
			});
			manageData.tab1=tab1;
			var tab2 = '';
			$("#addTr2").find("tr").each(function(){
				var tdArr = $(this).children();
				tab2 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+';';
			});
			manageData.tab2=tab2;
			var tab3 = '';
			$("#addTr3").find("tr").each(function(){
				var tdArr = $(this).children();
				tab3 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+','+tdArr.eq(4).html()+','+tdArr.eq(5).html()+';';
			});
			manageData.tab3=tab3;
			var tab4 = '';
			$("#addTr4").find("tr").each(function(){
				var tdArr = $(this).children();
				tab4 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+';';
			});
			manageData.tab4=tab4;
			var tab5 = '';
			$("#addTr5").find("tr").each(function(){
				var tdArr = $(this).children();
				tab5 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+';';
			});
			manageData.tab5=tab5;
			var tab6 = '';
			$("#addTr6").find("tr").each(function(){
				var tdArr = $(this).children();
				tab6 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+';';
			});
			manageData.tab6=tab6;
			var tab7 = '';
			$("#addTr7").find("tr").each(function(){
				var tdArr = $(this).children();
				tab7 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+','+tdArr.eq(3).html()+';';
			});
			manageData.tab7=tab7;
			var tab8 = '';
			var num = 1;
			$(".table-all-7").find("tr").each(function(){
				var tdArr = $(this).children();
				if ($(".table-all-7").find("tr").length-4>=num) {
					if (num%4==0) {
						tab8 += tdArr.eq(0).html() + ','+tdArr.eq(1).html()+','+tdArr.eq(2).html()+';';
					} else if (num%2==0) {
						tab8 += tdArr.eq(0).html() + ',' + tdArr.eq(1).html() + ',' + tdArr.eq(2).attr('mid') + ',' + tdArr.eq(3).html() +
						',' + tdArr.eq(4).html() + ',' + tdArr.eq(5).html() + ',' ;
					}
				}
				num++;
			});
			manageData.tab8=tab8;
			Common.getPostData('/manage/addOrUpTerInfo.do', manageData,function(rep){
				if (rep.type) {
					if (rep.flg) {
						alert("保存成功！");
						window.location.href = "/manage/teachermanage.do";
					} else {
						alert("保存失败！");
					}
				} else {
					alert(rep.mesg);
				}

			});
		});

		$(".input-search").on("keydown",function(event){
			var browser = navigator.appName ;      //浏览器名称
			var userAgent = navigator.userAgent;     //取得浏览器的userAgent字符串

			var Code = '' ;
			if(browser.indexOf('Internet')>-1)      // IE
				Code = window.event.keyCode ;
			else if(userAgent.indexOf("Firefox")>-1)   // 火狐
				Code = event.which;
			else                     // 其它
				Code = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;

			if (Code == 13)               //可以自行加其它过滤条件
			{
				manageData.page=1;
				manage.getTeacherList();
			}
		});

		$("#addUserImage").click(function(){
			var addObj = {};
			var uid = "55ff6ef30cf25822381484ef";
			addObj.uid=uid;
			jQuery("#uid").val(uid);
			jQuery("#resForm").submit();
		});
		$("#adduserimg").click(function(){
			var addObj = {};
			var uid = "55ff6ef30cf25822381484ef";
			addObj.uid=uid;
			jQuery("#uid").val(uid);
			jQuery("#userimg").submit();
		});
		$('#sel_project_id').change(function(){
			manage.getProjectDetailList();
		});
		$('#sel_score_sort_type').change(function(){
			manage.getProjectDetailList();
		});

		$(".check-lab").click(function(){
			$(".check-o").toggleClass('check-s');
		});
		$(".btn-x-n").click(function(){
			manageData.teacherId ="";
			$(".wind-x-tea").fadeOut();
			$(".wind-set-power").fadeOut();
			$(".wind-x-xm").fadeOut();
			$(".wind-add-xm").fadeOut();
			$(".bg").fadeOut();
		});
		$(".btn-x").click(function(){
			$(this).parent().parent().fadeOut();
			$(".bg").fadeOut();
		});

		$(".main-top2 span").click(function(){
			$(this).addClass("span-top").siblings(".main-top2 span").removeClass("span-top");
		});
		$(".btn-reset-n").click(function(){
			manageData.userId = "";
			manageData.userName = "";
			manageData.jobnum = "";
			$(".wind-mima-reset").fadeOut();
			$(".bg").fadeOut();
		});
		$(".btn-reset-y3").click(function(){
			$(".wind-mima-reset3").fadeOut();
			$(".bg").fadeOut();
		});
		$(".add-tea").click(function(){
			$(".cont-main1").fadeOut();
			$(".cont-main1-1").fadeIn();
			$('.add-title').html('添加老师');
			$('#etype').val(0);
			$('#realname').removeAttr("disabled");
			manage.getProjectList();
		});
		$(".main-top2 .span1").click(function(){
			$(".cont-main1-1").fadeOut();
			$(".cont-main2").fadeOut();
			$(".cont-main3").fadeOut();
			$(".cont-main1").fadeIn();
		});
		$(".main-top2 .span2").click(function(){
			$(".cont-main1-1").fadeOut();
			$(".cont-main1").fadeOut();
			$(".cont-main3").fadeOut();
			$(".cont-main2").fadeIn();
			manage.getProjectList();
			manage.getProjectDetailList();
		});
		$(".main-top2 .span3").click(function(){
			$(".cont-main1-1").fadeOut();
			$(".cont-main1").fadeOut();
			$(".cont-main2").fadeOut();
			$(".cont-main3").fadeIn();
			manage.getProjectList();
		});
		$(".back-jslb").click(function(){
			//$(".cont-main1-1").fadeOut();
			//$(".cont-main1").fadeIn();
			window.location.href = "/manage/teachermanage.do";
		});

		$(".btn-add-xm").click(function(){
			$('#course').val('');
			$('#con').val('');
			$('#score').val(100);
			$(".wind-add-xm").fadeIn();
			$(".bg").fadeIn();
		});
        //$(".img-upload-pic").click(function(){
        //    $(".wind-upload-pic").fadeIn();
        //})
		$(".delproject").click(function(){
			manage.delProject();
		});

		$("#userRole").change(function(){
			 var flag=$(".ismanageclass").attr("flag");
			 if ($(this).val()=="2") {
				 if(flag==1){
					$("#ismanage").prop({"checked":true});
				 }else{
					$("#ismanage").prop({"checked":false});
				 }
				 $('.ismanageclass').show();
			 } else {
				 $("#ismanage").prop({"checked":false});
				 $('.ismanageclass').hide();
			 }
		});

		$("#role").change(function(){
			var flag=$("#isManageDiv").attr("flag");
			if ($(this).val()=="2") {
				if(flag==1){
					$("#ismanage2").prop({"checked":true});
				}else{
					$("#ismanage2").prop({"checked":false});
				}
				$('#isManageDiv').show();
			} else {
				$("#ismanage2").prop({"checked":false});
				$('#isManageDiv').hide();
			}
		});

		// 检查老师用户名重复
		$('#name3').keyup(function() {
			var target = $(this);
			var target4 = $('#teacher-name');
			if ($.trim(target.val()) != '') {
				$.ajax({
					url: '/manage/checkUserName.do',
					type: 'post',
					dataType: 'json',
					data: {
						userName: $.trim(target.val()),
						etype:$('#etype').val(),
						userId:$('#userid').val()
					},
					success: function(data) {
						target4.text("");
						target4.text(data.userName);
					},
					error: function() {
					}
				});
			}
		});

		$(".add1").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#edu1  option:selected').text()+"</td>";
			str+="<td>"+$('#degree1  option:selected').text()+"</td>";
			str+="<td>"+$('#major').val()+"</td>";
			str+="<td>"+$('#time1').val()+"</td>";
			str+="<td>"+$('#open1  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del1'>删除</button></td>";
			str+="</tr>";
			$("#addTr").append(str);
			$('#major').val('');
			$('#time1').val('');
			$(".del1").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del1").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add2").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#time2').val()+"</td>";
			str+="<td>"+$('#job').val()+"</td>";
			str+="<td>"+$('#postion').val()+"</td>";
			str+="<td>"+$('#open2  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del2'>删除</button></td>";
			str+="</tr>";
			$("#addTr2").append(str);
			$('#time2').val('');
			$('#job').val('');
			$('#postion').val('');
			$(".del2").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del2").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add3").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#name').val()+"</td>";
			str+="<td>"+$('#time3').val()+"</td>";
			str+="<td>"+$('#type').val()+"</td>";
			str+="<td>"+$('#level').val()+"</td>";
			str+="<td>"+$('#time4').val()+"</td>";
			str+="<td>"+$('#open3  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del3'>删除</button></td>";
			str+="</tr>";
			$("#addTr3").append(str);
			$('#name').val('');
			$('#time3').val('');
			$('#type').val('');
			$('#level').val('');
			$('#time4').val('');
			$(".del3").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del3").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add4").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#time5').val()+"</td>";
			str+="<td>"+$('#workplace').val()+"</td>";
			str+="<td>"+$('#postion2').val()+"</td>";
			str+="<td>"+$('#open4  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del4'>删除</button></td>";
			str+="</tr>";
			$("#addTr4").append(str);
			$('#time5').val('');
			$('#workplace').val('');
			$('#postion2').val('');
			$(".del4").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del4").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add5").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#time6').val()+"</td>";
			str+="<td>"+$('#workplace2').val()+"</td>";
			str+="<td>"+$('#postion3').val()+"</td>";
			str+="<td>"+$('#open5  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del5'>删除</button></td>";
			str+="</tr>";
			$("#addTr5").append(str);
			$('#time6').val('');
			$('#workplace2').val('');
			$('#postion3').val('');
			$(".del5").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del5").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add6").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#result').val()+"</td>";
			str+="<td>"+$('#level2').val()+"</td>";
			str+="<td>"+$('#time7').val()+"</td>";
			str+="<td>"+$('#open6  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del6'>删除</button></td>";
			str+="</tr>";
			$("#addTr6").append(str);
			$('#result').val('');
			$('#level2').val('');
			$('#time7').val('');
			$(".del6").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del6").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add7").click(function(){
			if(manage.inputCheckOne($(this))==false){
				return;
			}
			var str='';
			str+="<tr>";
			str+="<td>"+$('#time8').val()+"</td>";
			str+="<td>"+$('#certificate').val()+"</td>";
			str+="<td>"+$('#score2').val()+"</td>";
			str+="<td>"+$('#open7  option:selected').text()+"</td>";
			str+="<td><button class='btn-xx del7'>删除</button></td>";
			str+="</tr>";
			$("#addTr7").append(str);
			$('#time8').val('');
			$('#certificate').val('');
			$('#score2').val('');
			$(".del7").click(function(){
				$(this).parent().parent().remove();
			});
		});
		$(".del7").click(function(){
			$(this).parent().parent().remove();
		});
		$(".add8").click(function(){
			if(manage.inputCheckTwo()==false){
				return;
			}
			var str='';
			str+="<tbody><tr>";
			str+="<th class='th1'>时间</th><th class='th2'>培训机构</th><th class='th3'>培训课程</th><th class='th4'>证书</th><th class='th7'>成绩</th><th class='th5'>公开</th><th class='th6'>操作</th></tr><tr>";
			str+="<td>"+$('#value1').val()+"</td>";
			str+="<td>"+$('#value2').val()+"</td>";
			str+="<td mid="+$('#value3 option:selected').attr('mid')+">"+$('#value3 option:selected').text()+"</td>";
			str+="<td>"+$('#value4').val()+"</td>";
			str+="<td>"+$('#value5').val()+"</td>";
			str+="<td rowspan='3'>"+$('#open8 option:selected').text()+"</td>";
			str+="<td rowspan='3'><button class='btn-xx del8'>删除</button></td></tr><tr><th>地点</th><th>培训课时</th><th colspan='3'>培训内容</th></tr><tr>";
			str+="<td>"+$('#value6').val()+"</td>";
			str+="<td>"+$('#value7').val()+"</td>";
			str+="<td  rowspan='3'>"+$('#value8').val()+"</td>";
			str+="</tr></tbody>";
			$("#addTr8").before(str);
			$('#value1').val('');
			$('#value2').val('');
			$('#value4').val('');
			$('#value5').val('');
			$('#value6').val('');
			$('#value7').val('');
			$('#value8').val('');
			$(".del8").click(function(){
				$(this).parent().parent().parent().remove();
			});
		});
		$(".del8").click(function(){
			$(this).parent().parent().parent().remove();
		});

		$(".span-search").click(function(){
			manageData.page=1;
			manage.getTeacherList();
		});

		$(".psure").click(function(){
			if($('#course').val() == ''){
				alert("请输入培训课程。");
				return;
			}
			if($('#con').val() == ''){
				alert("请输入说明。");
				return;
			}
			manageData.course=$('#course').val();
			manageData.content=$('#con').val();
			manageData.score=$('#score').val();
			Common.getPostData('/manage/addProject.do', manageData,function(rep){
					if (rep.flag) {
						alert("添加失败！");
					} else {
						alert("添加成功！");
						$(".wind-add-xm").fadeOut();
						$(".bg").fadeOut();
						manage.getProjectList();
					}
				}
			);
		});

		$(".delteacherinfo").click(function(){
			manage.delTeacherInfo();
		});

		manage.getTeacherList();
	};
	manage.delTeacherInfo=function(){
		manageData.teacherId = $("#delTeacherId").val();
		Common.getData('/manage/delTeacherInfo.do', manageData,function(rep){
			manageData.teacherId ="";
			$(".wind-x-tea").fadeOut();
			$(".bg").fadeOut();
			manageData.page=1;
			manage.getTeacherList();
		});
	}

	manage.getTeacherList=function(){
		manageData.grade = $("#grade").val();
		manageData.customize = $("#customize").val();
		manageData.keyword = $(".input-search").val();
		manageData.type = $("#teachersel").val();
		//设置每页数据长度
		manageData.pageSize = 12;
		Common.getData('/manage/getTeacherList.do', manageData,function(rep){
			$('.users').html('');
			Common.render({tmpl: $('#user_templ'), data: rep, context: '.users'});

			$(".btn-reset").click(function(){
				var uid =$(this).attr("tid");
				var unm =$(this).attr("tnm");
				manageData.userId = uid;
				manageData.userName = unm;
				//var rds = $('#ask-div input[name="tea-radio"]:checked ');
				$(".wind-mima-reset").fadeIn();
				$(".bg").fadeIn();
			});

			$(".btn-edit").click(function(){
				$(".cont-main1").fadeOut();
				$(".cont-main1-1").fadeIn();
				$('#etype').val(1);
				$('.add-title').html('编辑老师');
				$('#realname').attr("disabled","disabled");
				manage.getProjectList();
				manageData.userid=$(this).attr('tid');
				manage.getTeacherDetail();
			});
			$(".btn-set").click(function(){
				$('#teacherName').attr("disabled","");
				$('#teacherNo').attr("disabled","");
				var uid =$(this).attr("tid");
				var unm =$(this).attr("tnm");
				var tnb =$(this).attr("tnb");
				var usr =$(this).attr("usr");
				var ism =$(this).attr("ism");
				$('#teacherName').val(unm);
				$('#teacherNo').val(tnb);
				$("#role").val(usr);
				$("#isManageDiv").attr("flag",ism);
				if(ism!=2){
					$("#isManageDiv").show();
					if(ism==1){
						$("#ismanage2").prop({"checked":true});
					}else{
						$("#ismanage2").prop({"checked":false});
					}
				}else{
					$("#ismanage2").prop({"checked":false});
					$("#isManageDiv").hide();
				}
				$('#teacherName').attr("disabled","disabled");
				$('#teacherNo').attr("disabled","disabled");
				manageData.userid = uid;
				manageData.name = unm;
				manageData.jobnum = tnb;
				$(".wind-set-power").fadeIn();
				$(".bg").fadeIn();
			});

			$(".btn-del").click(function(){
				$("#delTeacherId").val($(this).attr("tid"));
				$(".wind-x-tea").fadeIn();
				$(".bg").fadeIn();
			});
			if(rep.total>0) {
				var totalPage = Math.ceil(rep.total / rep.pageSize) == 0 ? 1 : Math.ceil(rep.total / rep.pageSize);
				$('#pageDiv').jqPaginator({
					totalPages: totalPage,//总页数
					visiblePages: rep.pageSize,//分多少页
					currentPage: rep.page,//当前页数
					first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
					prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
					next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
					last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
					page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
					onPageChange: function (n) { //回调函数
						//点击页码的查询
						//          	            	alert('当前第' + n + '页');
						if (n != manageData.page) {
							manageData.page = n;
							manage.getTeacherList();
						}
					}
				});
			}
		});
	}

	$(".btn-reset-y").click(function(){
		var rds = $("input[name='tea-radio']:checked").val();
		manageData.rds = rds;
		$(".wind-mima-reset").fadeOut();
		$(".wind-mima-reset2").fadeIn();
	});

	$(".btn-reset-y2").click(function(){
		Common.getPostData('/manage/initpwd.do', manageData,function(rep){
			var infomsg="老师："+rep.nm+"，初始密码："+rep.pwd
			$("#resetPwdInfo").text(infomsg);
			manageData.userId = "";
			manageData.userName = "";
			manageData.rds = "";
			$(".wind-mima-reset2").fadeOut();
			$(".wind-mima-reset3").fadeIn();
		});

	});

	$(".surerole").click(function(){
		manageData.role = $('#role option:selected').val();
		if(document.getElementById("ismanage2").checked) {
			manageData.ismanage = 1;
		} else {
			manageData.ismanage = 0;
		}
		Common.getPostData('/manage/updateTeacherRole.do', manageData,function(rep){
			if (rep.flg) {
				alert("设置权限成功！");
				manage.getTeacherList();
			}
			$(".wind-set-power").fadeOut();
			$(".bg").fadeOut();
		});
	});

	$(".btn-reset-n2").click(function(){
		manageData.userId = "";
		manageData.userName = "";
		$(".wind-mima-reset2").fadeOut();
		$(".bg").fadeOut();
	});

	manage.inputCheckOne = function(obj) {
		var reuselt=true;
		var parentObj=obj.parent().parent();
		var titleObj=parentObj.parent().parent().find("tr").first();
		var msg="";
		parentObj.find("input[type='text']").each(function(){
			if($.trim($(this).val())=="") {

				var index = $(this).parent().index();
				var titleName = titleObj.find("th").eq(index).text();
				if (msg!="") {
					msg +=",\n"+ titleName;
				}else{
					msg = titleName;
				}

			}
		});
		if (msg!='') {
			alert(msg+"\n不能为空");
			reuselt= false;
		}
		return reuselt;
	}

	manage.inputCheckTwo = function() {
		var reuselt=true;
		var msg="";
		if($.trim($("#value1").val())==""){
			if (msg!="") {
				msg +=",\n时间" ;
			}else{
				msg = "时间";
			}
		}
		if($.trim($("#value2").val())==""){
			if (msg!="") {
				msg +=",\n培训机构" ;
			}else{
				msg = "培训机构";
			}
		}
		if($.trim($("#value3").val())==""){
			if (msg!="") {
				msg +=",\n培训课程" ;
			}else{
				msg = "培训课程";
			}
		}
		if($.trim($("#value4").val())==""){
			if (msg!="") {
				msg +=",\n证书" ;
			}else{
				msg = "证书";
			}
		}
		if($.trim($("#value5").val())==""){
			if (msg!="") {
				msg +=",\n成绩" ;
			}else{
				msg = "成绩";
			}
		}
		if($.trim($("#value6").val())==""){
			if (msg!="") {
				msg +=",\n地点" ;
			}else{
				msg = "地点";
			}
		}
		if($.trim($("#value7").val())==""){
			if (msg!="") {
				msg +=",\n培训课时" ;
			}else{
				msg = "培训课时";
			}
		}
		if($.trim($("#value8").val())==""){
			if (msg!="") {
				msg +=",\n培训内容" ;
			}else{
				msg = "培训内容";
			}
		}
		if(msg!=""){
			alert(msg+"\n不能为空");
			reuselt= false;
		}
		return reuselt;
	}
	manage.getTeacherDetail = function() {
		Common.getPostData('/manage/getTeacherDetailEdit.do', manageData,function(rep){
			//$("#name3").attr("disabled", true);
			manage.setData(rep.resumeDTO);
			$('.addTr').html('');
			$('.addTr2').html('');
			$('.addTr3').html('');
			$('.addTr4').html('');
			$('.addTr5').html('');
			$('.addTr6').html('');
			$('.addTr7').html('');
			//$('.addTr9').html('');
			Common.render({tmpl: $('#addTr_templ'), data: rep, context: '.addTr'});
			Common.render({tmpl: $('#addTr2_templ'), data: rep, context: '.addTr2'});
			Common.render({tmpl: $('#addTr3_templ'), data: rep, context: '.addTr3'});
			Common.render({tmpl: $('#addTr4_templ'), data: rep, context: '.addTr4'});
			Common.render({tmpl: $('#addTr5_templ'), data: rep, context: '.addTr5'});
			Common.render({tmpl: $('#addTr6_templ'), data: rep, context: '.addTr6'});
			Common.render({tmpl: $('#addTr7_templ'), data: rep, context: '.addTr7'});
			//Common.render({tmpl: $('#addTr9_templ'), data: rep, context: '.addTr9'});
			var content = "";
			if (rep.continueEducationEntryList!=null && rep.continueEducationEntryList.length!=0) {
				for(var i=0;i<rep.continueEducationEntryList.length;i++) {
					var data=rep.continueEducationEntryList[i];
					var fo = "";
					if (data.open==1) {
						fo = '是';
					} else {
						fo = '否';
					}
					content += "<tbody><tr><th class='th1'>时间</th><th class='th2'>培训机构</th><th class='th3'>培训课程</th><th class='th4'>证书</th><th class='th7'>成绩</th><th class='th5'>公开</th><th class='th6'>操作</th></tr><tr> <td>";
					content+=data.time+"</td><td>"+data.institutions+"</td><td  mid='"+data.course+"'>"+data.courseName+"</td><td>"+data.certificate+"</td><td>"+data.record+"</td><td rowspan='3'>"+fo+"</td><td rowspan='3'> <button class='btn-xx del8'>删除</button></td></tr>";
					content+="<tr><th>地点</th><th>培训课时</th><th colspan='3'>培训内容</th></tr><tr><td>"+data.address+"</td><td>"+data.classHour+"</td><td rowspan='3'>"+data.content+"</td></tr></tbody>";
				}
				$("#addTr8").before(content);
			}
			$(".del1,.del2,.del3,.del4,.del5,.del6,.del7").click(function(){
				$(this).parent().parent().remove();
			});
			$(".del8").click(function(){
				$(this).parent().parent().parent().remove();
			});


		});
	}

	manage.setData = function(data) {

		$("#userid").val(data.id);
		$("#name3").val(data.name);
		$("#teacher-name").text(data.name);
		$("#userRole").val(data.userrole);

		/*manageData.userrole=$("#userRole").val();
		var ismanage = 0;
		if(document.getElementById("ismanage").checked) {
			ismanage = 1;
		} else {
			ismanage = 0;
		}*/
        var ism=data.ismanage;
		$(".ismanageclass").attr("flag",ism);
		if(ism!=2){
			$(".ismanageclass").show();
			if(ism==1){
				$("#ismanage").prop({"checked":true});
			}else{
				$("#ismanage").prop({"checked":false});
			}
		}else{
			$("#ismanage").prop({"checked":false});
			$(".ismanageclass").hide();
		}

		$("#area").val(data.area);
		$("#postiondec").val(data.postiondec);
		$("#realname").val(data.username);
		$("#sex").val(data.sex);
		$("#birth").val(data.birth);
		$("#place").val(data.place);
		$("#nation").val(data.national);
		$("#card").val(data.card);
		$("#cardnum").val(data.cardnumber);
		$("#vail").val(data.vail);
		$("#maritalstatus").val(data.maritalstatus);
		$("#political").val(data.political);
		$("#registerplace").val(data.registerplace);
		$("#adress").val(data.adress);
		$("#nowadress").val(data.nowadress);
		$("#zipcode").val(data.zipcode);
		//$("#phone").val(data.phone);
		$("#contact").val(data.contact);
		$("#contactphone").val(data.contactphone);
		$("#email").val(data.email);
		$("#education").val(data.education);
		$("#major2").val(data.major);
		$("#deutime").val(data.deutime);
		$("#degree2").val(data.degree);
		$("#degtime").val(data.degtime);
		$("#jobtime").val(data.jobtime);
		$("#schooltime").val(data.schooltime);
		$("#teachtime").val(data.teachtime);
		$("#teachSubject").val(data.teachSubject);
		$("#mandarinlevel").val(data.mandarinlevel);
		//$("#teacerticode").val(data.teacerticode);
		$("#teachernumber").val(data.teachernumber);
		$("#organization").val(data.organization);
		$("#introduction").val(data.introduction);
	}
	manage.getProjectList = function() {
		Common.getPostData('/manage/getProjectList.do', manageData,function(rep){
			$('.table-xm').html('');
			$('.value3').html('');
			$('.project').html('');
			Common.render({tmpl: $('#xm_templ'), data: rep, context: '.table-xm'});
			Common.render({tmpl: $('#value_templ'), data: rep, context: '.value3'});
			Common.render({tmpl: $('#project_templ'), data: rep, context: '.project'});
			$(".btn-dell").click(function(){
				var con = $(this).attr("mnm");
				manageData.id=$(this).attr("mid");
				$('#showproject').html("确定删除“"+con+"”培训课程吗？");
				$(".wind-x-xm").fadeIn();
				$(".bg").fadeIn();
			});
		});
	}

	manage.getProjectDetailList = function(){
		manageData.projectId = $("#sel_project_id").val();
		manageData.type = $("#sel_score_sort_type").val();
		Common.getPostData('/manage/projectDetailList.do', manageData,function(rep){
			$('.table-group').html('');
			Common.render({tmpl: $('#group_templ'), data: rep, context: '.table-group'});
			$(".btn-check").click(function(){
				var url="/manage/getTeacherDetail.do?userid="+$(this).attr("uid");
				window.open(url);
				//Common.goTo(url);
			});
		});
	}

	manage.delProject = function() {
		Common.getPostData('/manage/delProject.do', manageData,function(rep){
			if (rep.flag) {
				alert("删除失败！");
			} else {
				alert("删除成功！");
				$(".wind-x-xm").fadeOut();
				$(".bg").fadeOut();
				manage.getProjectList();
			}
		});
	}
	function uploadUserPhoto(form) {
//      var courseWareName = $.trim($("#courseWareName").val());
//      $("#courseWareName").val(courseWareName);
//      if (!courseWareName) {
		// MessageBox('请输入课件名。', -1);
		// return;
//      }
		if (!form.elements['pic'].value) {
			MessageBox('请选取课件文件。', -1);
			return;
		}

		MessageBox('上传中', 0);
		fileUpload(form, function(content) {
			var s = '';
			try {
				s = content;
				var json = $.parseJSON(s);
				if (json.code == "200") {
					MessageBox("上传成功!", 1);
				} else {
					MessageBox(json.message, -1);
				}
				form.reset();
				closeDialog(".inside-dialog");
				GetCourseWares();
			} catch (err) {
				if (s.length == 0) {
					MessageBox("上传完毕。", 1);
				} else if (s.length > 50) {
					MessageBox("服务器响应错误。", -1);
				} else {
					MessageBox(s, -1);
				}
			}
		});
	}
	manage.getLength = function(content) {
		var realLength = 0, len = content.length, charCode = -1;
		for (var i = 0; i < len; i++) {
			charCode = content.charCodeAt(i);
			if (charCode >= 0 && charCode <= 128) realLength += 1;
			else realLength += 2;
		}
		return realLength;
	};
	manage.init();
});