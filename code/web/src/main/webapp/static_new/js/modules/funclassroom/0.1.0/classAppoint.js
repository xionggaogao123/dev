/**
 *created by xxx on 15/12/17
 */
/**
 *@author xxx
 *@module 功能教室管理
 *@description
 */
/*global config*/
define(['common'], function(require, exports, module) {
	/**
	 *初始化参数
	 */
	var classM = {},
		Common = require('common');
	Array.prototype.remove = function(dx) {
		if (isNaN(dx) || dx > this.length) {
			return false;
		}
		this.splice(dx, 1);

	};
	
	//TODO
	var pageNo = 1;
	var pageSize = 10;
	var rooms;
	var countsTotal;
	var isFirstPage = true;
	
	var classPageNo = 1;
	var classPageSize = 10;
	var classes = {};
	var countClassTotal;
	var isClassFisrtPage = true;
	var classTotalPage;
	var appointTotalPage;

	//DOM加载完成后即调用该方法,获取所有教室数据在页面上显示
	function roomList(roomPageNo){
		var page = {};
		page.skip = (roomPageNo-1)*pageSize;
		page.limit = pageSize;
		$.ajax({
			type: "POST",
			url: "/functionclassroom/classroomList.do",
			data: page,
			resultType: "json",
			success:function(data){
				rooms = data.datas;
				classTotalPage = Math.ceil(data.total/pageSize) == 0?1:Math.ceil(data.total/pageSize),//总页数  
				fillClassroomList(rooms);
				//jqPaginator为分页组件
				$('.new-page-links:eq(1)').jqPaginator({
					totalPages: classTotalPage,
					visiblePages: 5,
					currentPage: roomPageNo,
					first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
		            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
		            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
		            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
		            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
		            onPageChange: function (n) { //回调函数
	  	            	if(isFirstPage){
	  	            		isFirstPage = false;
	  	            		roomList(n);
	  	            	}else{
	  	            		isFirstPage = true; 
	  	            		return;
	  	            	}
	  	            }
				});
			},
			error:function(){
				alert("页面加载失败,请刷新重试!");
			}
		});
	}
	
	//向页面填充教室列表
	function fillClassroomList(classrooms){
		$('#classroomList').html('');
		var Thtml;
		if(classrooms.length==0){
			Thtml = "<tr><td colspan='4' style='border:0'>暂无数据</td></tr>";
			$('.new-page-links:eq(1)').css("display","none");
		}else{
			$('.new-page-links:eq(1)').css("display","block");
			for(var i=0;i<classrooms.length;i++){
				var name = '';
				if (classrooms[i].userList!=null && classrooms[i].userList.length!=0) {
					for (var j=0;j<classrooms[i].userList.length;j++) {
						name += classrooms[i].userList[j].userName + " ";
					}
				}
				Thtml += "<tr flag='"+classrooms[i].id+"'><td>"+classrooms[i].number+"</td><td id='name'>"; //序号
				Thtml += classrooms[i].className+"</td><td>"; //教室名称
				Thtml += name+"</td><td>"; //管理者
				Thtml += "<a href='#' class='abtn lookReserv'>查看</a></td></tr>"; //操作
			}
		}
		$('#classroomList').html(Thtml);
	}

	
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 */
	classM.init = function() {


		//实现表头切换
		Common.tab('click',$('.tab-head-new'));
		
		//调用初始页面方法
		roomList(pageNo);

		$('.count').on('click', function(event) {
			Common.dialog($('#count'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#count'));
		});
		
		//弹出编辑弹框
//		$(document).delegate('.editReserv','click',function(event) {
//			Common.dialog($('#editReserv'));
//			return false;
//		});
		
		//关闭编辑弹框
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#editReserv'));
		});
		
		//关闭编辑弹框
		$('#cancelSaveApp').on('click',function(event){
			Common.dialog.close($('#editReserv'));
		});
		
		//关闭提示弹框,打開預約提示框
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#tip2'));
//			Common.dialog($('#order'));
		});
		
		//关闭提示弹框,打開預約提示框
		$('#getIt').on('click', function(event) {
			Common.dialog.close($('#getIt'));
			Common.dialog($('#order'));
		});
		
		//关闭編輯提示弹框,打開編輯提示框
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#getIt3'));
//			Common.dialog($('#editReserv'));
		});
		
		//关闭提示弹框,打開編輯提示框
		$('#getIt3').on('click', function(event) {
			Common.dialog.close($('#getIt3'));
			Common.dialog($('#editReserv'));
		});
		
		var id; //定义选定教室的id
		var className; //定义教室名称
		//给查看按钮添加事件
		
		$(document).delegate('.lookReserv','click',function(event){
			//获取教室对应id
			id = $(this).parent().parent().attr('flag');
			classes.qqid=id;
	     	className = $(this).parent().prev().prev().html();
	     	$('#lookReserv div:eq(0)').html('<a href="#">教室列表</a>&gt;'+className+'');
			//获取分页数据并发送ajax
			AppointList(id, classPageNo);
			$('#lookReserv').css('display', 'block').siblings().hide();
			return false;
		});
		
		//为什么你不可以用
//		$('.lookReserv').on('click',function(event) {
//			$('#lookReserv').css('display', 'block').siblings().hide();
//			return false;
//		});

		//弹出预约弹框
		$('.order').on('click', function(event) {
			Common.dialog($('#order'));
			return false;
		});
		//关闭预约弹框
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#order'));
			//清空弹框内容
			clearSaveWindow();
		});
		
		//关闭预约弹框
		$('#cancelSave').on('click', function(event) {
			Common.dialog.close($('#cancelSave'));
			//清空弹框内容
			clearSaveWindow();
		});

		//给"教室列表"添加事件
		$('.gray-table-body').on('click', '.index a', function(event) {
			$(this).parents('.gray-table-body').hide().siblings().css('display', 'block');			
		});
		
		//给教室列表"到该页"按钮添加事件
		$('#currentPage').on('click',function(){
			var goPage = $(this).prev().val();
			if((!isNaN(goPage))&&(goPage<=classTotalPage)&&(goPage>0)){
				roomList(parseInt(goPage));
			}
		});
		
		//给预约列表"到该页"按钮添加事件
		$('#goThisPage').on('click',function(){
			var goPage = $(this).prev().val();
			if((!isNaN(goPage))&&(appointTotalPage>=goPage)&&(goPage>0)){
				AppointList(id,parseInt(goPage));
			}
		});
		
		//显示预约列表方法
		function AppointList(id,classPageNo){
			var classPage = {};
			classPage.skip = (classPageNo-1)*classPageSize;
			classPage.limit = classPageSize;
			classPage.cid = id;
			$.ajax({
				type: "POST",
				url: "/functionclassroom/appointmentList.do",
				data: classPage,
				returnType: "json",
				success: function(data){
					classes.datas = data.datas;
					classes.total = data.total;
					appointTotalPage = Math.ceil(data.total/classPageSize) == 0?1:Math.ceil(data.total/classPageSize);
					fillClassList(classes.datas);
					//jqPaginator为分页组件
					$('.new-page-links:eq(2)').jqPaginator({
						totalPages: Math.ceil(data.total/classPageSize) == 0?1:Math.ceil(data.total/classPageSize),//总页数  
						visiblePages: 5,
						currentPage: classPageNo,
						first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
			            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
			            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
			            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
			            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
			            onPageChange: function (n) { //回调函数
			            	classes.pageNo = n;	
		  	            	if(isClassFisrtPage){
		  	            		isClassFisrtPage = false;
		  	            		AppointList(id,n);
		  	            	}else{
		  	            		isClassFisrtPage = true; 
		  	            		return;
		  	            	}
		  	            }
					});
				}
//				error: function(){
//					alert('error');
//				}
			});
		}
		
		//填充预约列表数据
		function fillClassList(arr){
			var Thtml;
			$('#AppointList').html('');
			if(arr.length==0){
				Thtml = "<tr><td style='border:0' colspan='4'>暂无数据</td></tr>";
				$('.new-page-links:eq(2)').css("display","none");
			}else{
				$('.new-page-links:eq(2)').css("display","block");
				for(var i=0;i<arr.length;i++){
					var reaSort;
					if(arr[i].reasons.length>10){
						reaSort = arr[i].reasons.substring(0,10)+"...";
					}else{
						reaSort = arr[i].reasons;
					}
					Thtml += "<tr flag="+arr[i].id+"><td>";
					Thtml += arr[i].startTime+"</td><td>"; //起始时间
					Thtml += arr[i].endTime+"</td><td>"; //结束时间
					Thtml += arr[i].user+"</td><td style='cursor:pointer' title='"+arr[i].reasons+"'>"; //使用者
					Thtml += reaSort+"</td></tr>"; //使用说明
					//Thtml += "<a href='#' class='abtn editReserv'>编辑</a><a href='#' class='del2 del'>删除</a></td>"; //操作
				}
			}
			$('#AppointList').html(Thtml);
		}
		
		//给添加弹窗"确定"按钮添加事件
		$('#saveAppoint').on('click',function(){
			//获取所有要传递的值
			var e = {};
			e.cid = id;
			e.className = className;
			e.startTime = $('#startTime').val()==null?"":$('#startTime').val().trim();
			e.endTime = $('#endTime').val()==null?"":$('#endTime').val().trim();
			e.reasons = $('#reasons').val()==null?"":$('#reasons').val().trim();
			if(isFull(e)){
				$.ajax({
					type: "POST",
					url: "/functionclassroom/putAppInfo.do",
					returnType: "json",
					success: function(data){
						e.uid = data.id;
						e.sid = data.sid;
						e.userName = data.userName; //数据获取完毕
						
						//发送ajax
						$.ajax({
							type: "POST",
							url: "/functionclassroom/saveAppoint.do",
							returnType: "json",
							data: e,
							success: function(data){
								if(data.flag==0){
									alert("添加失败!");
								}else if(data.flag==1){
									alert("添加成功!");
									//刷新页面
									AppointList(id,classes.pageNo);
									//关闭窗口
									Common.dialog.close($('#order'));
									//清空添加弹框内容
									clearSaveWindow();
								}else if(data.flag==2){
									Common.dialog($('#tip2'));
									$('#tipCount').html(data.datas.length);
									var tHtml;
									$('#tipBody').html('');
									for(var i=0;i<data.datas.length;i++){
										var reaSort;
										if(data.datas[i].reasons.length>13){
											reaSort = data.datas[i].reasons.substring(0,13)+"...";
										}else{
											reaSort = data.datas[i].reasons;
										}
										tHtml += "<tr><td class='atime'>"+data.datas[i].startTime+"</td><td class='atime'>"; //起始时间
										tHtml += data.datas[i].endTime+"</td><td class='auser'>"; //结束时间
										tHtml += data.datas[i].user+"</td><td class='leave' title='"+data.datas[i].reasons+"'>"; //用户名
										tHtml += reaSort+"</td></tr>"; //使用事由
									}
									$('#tipBody').html(tHtml);
								}
							}
						});
					}
				});
				
			}
		});
		
		function clearSaveWindow(){
			$('#startTime').val('');
			$('#endTime').val('');
			$('#reasons').val('');
		}
		
		//判断页面输入信息是否符合要求
		function isFull(e){
			if(e.startTime==""){
				alert("请选择开始时间!");
				return false;
			}else if(e.endTime==""){
				alert("请选择结束时间!");
				return false;
			}else if(e.startTime>e.endTime){
				alert("您选择的时间不正确!");
				return false;
			}else if(e.reasons==""){
				alert("请输入使用事由!");
				return false;
			}else{
				return true;
			}
		}
		
		//给删除按钮添加事件
		$(document).delegate('.del2','click',function(){
			var aid = $(this).parent().parent().attr('flag');
			if(confirm("确定删除该条预约?")){
				$.ajax({
					type: "POST",
					url: "/functionclassroom/deleteAppoint.do",
					data: {"id":aid},
					returnType: "json",
					success: function(){
							alert("已删除");
							//刷新页面
							if(appointTotalPage==classes.pageNo){
								if(classes.total%classPageSize==1){
									AppointList(id,classes.pageNo-1);
								}else{
									AppointList(id,classes.pageNo);
								}
							}else{
								AppointList(id,classes.pageNo);
							}
					}
				});
			}
		});
		
		var aid; //定义预约的id
		//给编辑按钮添加事件
		$(document).delegate('.editReserv','click',function(){
			//获取原先数据
			aid = $(this).parent().parent().attr('flag');
			var e = {};
			$.ajax({
				type: "post",
				url: "/functionclassroom/getAppoint.do",
				data: {"id":aid},
				returnType: "json",
				success: function(data){
					e.userName = data.userName;
					e.startTime = data.startTime;
					e.endTime = data.endTime;
					e.reasons = data.reasons;
					//将获取的数据放入弹框中
					fillEditWindow(e)
					//弹出编辑弹窗
					Common.dialog($('#editReserv'));
				}
			});
		});
		
		//将获取的数据放入弹框中
		function fillEditWindow(e){
			$('#eUser').val(e.userName);
			$('#eStartTime').val(e.startTime);
			$('#eEndTime').val(e.endTime);
			$('#eReasons').val(e.reasons);
		}
		
		//保存编辑的预约信息
		$(document).delegate('#sureSaveApp','click',function(){
			var e = {};
			e.startTime = $('#eStartTime').val();
			e.endTime = $('#eEndTime').val();
			e.reasons = $('#eReasons').val();
			e.id = aid;
			if(isFull(e)){
				$.ajax({
					type: "POST",
					url: "/functionclassroom/updateAppoint2.do",
					data: e,
					returnType: "json",
					success: function(data){
						if(data.flag==0){
							alert("更新失敗!");
						}else if(data.flag==1){
							alert("更新成功!");
							//关闭弹窗
							Common.dialog.close($('#editReserv'));
							//刷新页面
							AppointList(id,classes.pageNo);
						}else if(data.flag==2){
							Common.dialog($('#tip3'));
							$('#tipCount3').html(data.datas.length);
							var tHtml;
							$('#tipBody3').html('');
							for(var i=0;i<data.datas.length;i++){
								var reaSort;
								if(data.datas[i].reasons.length>13){
									reaSort = data.datas[i].reasons.substring(0,13)+"...";
								}else{
									reaSort = data.datas[i].reasons;
								}
								tHtml += "<tr><td class='atime'>"+data.datas[i].startTime+"</td><td class='atime'>"; //起始时间
								tHtml += data.datas[i].endTime+"</td><td class='auser'>"; //结束时间
								tHtml += data.datas[i].user+"</td><td class='leave' title='"+data.datas[i].reasons+"'>"; //用户名
								tHtml += reaSort+"</td></tr>"; //使用事由
							}
							$('#tipBody3').html(tHtml);
						}
					}
				});
			}
		})
		 $("#classReservation123").click(function(){
			 roomList(pageNo);
			 AppointList(id,classes.pageNo);
			 if(classes.qqid!=null){
				$.ajax({
					type: "POST",
					url: "/functionclassroom/findClassRoomName.do",
					data: {"classRoomId":classes.qqid},
					dataType: "json",
					success: function(data){
						$('#lookReserv div:eq(0)').html('<a href="#">教室列表</a>&gt;<span>'+data+'</span>');
					}
				});
			 }
 	    })
		
		$(document).delegate('#st','click',function(){
			Common.dialog($('#tip2'));
		})
		
		
	/*===========init方法结束===========*/
	};
	/*===========init方法结束===========*/	
	(function() {
		classM.init();
	})();

	module.exports = classM;

});
