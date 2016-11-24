/**
 *@author huanxiaolei@ycode.cn
 *@module 功能教室管理
 *@description
 */
/*global config*/
define(['common'], function(require, exports, module) {
	/**
	 *初始化参数
	 */
	var classManage = {},
		Common = require('common');
	Array.prototype.remove = function(dx) {
		if (isNaN(dx) || dx > this.length) {
			return false;
		}
		this.splice(dx, 1);

	};
	//公共变量
	var PUBLIC_CLASSROOMID;
	var COUNT_CLASSROOMID;
	var CURPAGE;
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 */
	classManage.init = function() {


		Common.tab('click',$('.tab-head-new'));
		function toTop(){
			$(document).scrollTop(0);	
		};
		$(".zb-ren-add").click(function(event) {
			$('.pop-wrapp').show();
		});
		$(".change-user").click(function(event) {
			$('.pop-wrapp').hide();
		});
		$(".sure-user").click(function(event) {
			var flg = false;
			if ($('#poptype').val()==1) {
				$('.cruser2 a').each(function (i) {
					if ($('.cruser2 a').eq(i).attr('uid') == $('#manager').val()) {
						flg = true;
					}
				});
			} else {
				$('.cruser a').each(function (i) {
					if ($('.cruser a').eq(i).attr('uid') == $('#manager').val()) {
						flg = true;
					}
				});
			}
			if (!flg) {
				if ($('#poptype').val()==1) {
					$('.cruser2').append("<a class='manage-ren-alert' uid=" + $('#manager').val() + ">" + $("#manager option:selected").text() + "<em class='zb-set-del ur-del'>X</em></a>");
				} else {
					$('.cruser').append("<a class='manage-ren-alert' uid=" + $('#manager').val() + ">" + $("#manager option:selected").text() + "<em class='zb-set-del ur-del'>X</em></a>");
				}
				$('.pop-wrapp').hide();
				$(".ur-del").click(function(){
					$(this).parent().remove();
				});
			} else {
				alert('该管理者已存在！');
			}
		});

		 var isInit = true;
		 var pageSize = 5;
		 var pageSizes = 10;
		//初始化加载列表
		queryFunClassRoomList();
		//加载功能教室管理员
		loadManage();
		//功能教室列表
		function queryFunClassRoomList(pageNo){
			var queryObj = {};
			queryObj.pageNo = pageNo==null?1:pageNo;
	    	queryObj.pageSize = pageSizes;
			$.ajax({
				type: 'post',
				url: "/functionclassroom/funClassRoomList.do",
				data: queryObj,
			    dataType: 'json',
			    success:function(data){
			    	var subData = data.datas;
			    	var $classRoomList = $("#classRoomList");
			    	$("#classRoomList tr:gt(0)").remove();
			    	if(subData.length==0){
			    		$classRoomList.append("<tr><td colspan='4'>暂无数据</td></tr>");
			    		return;
			    	}
			    	for(var i=0;i<subData.length;i++){
						var name = '';
						if (subData[i].userList!=null && subData[i].userList.length!=0) {
							for (var j=0;j<subData[i].userList.length;j++) {
								name += subData[i].userList[j].userName + " ";
							}
						}
			    		 var trHtml =      ' <tr>';
				    	 trHtml = trHtml + '   <td>'+ subData[i].number +'</td>';
				    	 trHtml = trHtml + '   <td>'+ subData[i].classRoomName +'</td>';
				    	 trHtml = trHtml + '   <td>'+ name +'</td>';
				    	 trHtml = trHtml + '   <td flag="'+ subData[i].id +'"><a class="abtn editClass">编辑</a><a class="abtn count mydel">统计</a>';
				    	 trHtml = trHtml + '   <a class="del">删除</a></td>';
				    	 trHtml = trHtml + ' </tr>';
				    	 $classRoomList.append(trHtml);
			    	}
			    	//分页
			    	$('#fun-clasroom-list').jqPaginator({
						totalPages:  Math.ceil(data.total/pageSizes) == 0?1:Math.ceil(data.total/pageSizes),//总页数  
						visiblePages: 5,
						currentPage: data.cur,
						first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
			            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
			            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
			            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
			            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
			            onPageChange: function (n) { //回调函数
			            	CURPAGE = n;
		  	            	if(isInit){
		  	            		queryFunClassRoomList(n);
		  	            		isInit = false;
		  	            	}else{
		  	            		isInit = true; 
		  	            		return;
		  	            	}
		  	            }
					});
			    }
			});
		};
		//功能教室编辑
		$("#classRoomList").on("click",".editClass",function(){
			$('#poptype').val(1);
			var classRoomId = $(this).parent().attr("flag");
			PUBLIC_CLASSROOMID = $(this).parent().attr("flag");
			$.ajax({
				type: 'post',
				url: "/functionclassroom/detialFunClassRoom.do",
				data: {'classRoomId':classRoomId},
				dataType: 'json',
				success:function(data){
					if(data.length==0){
						return;
					}
					$("#editNum").val(data.number);
					$("#editClassName").val(data.classRoomName);
//					$("#editManager option:selected").attr("selected","");
//					$("#editManager option[value='"+data.adminId+"']").attr("selected","selected");
					$('.cruser2').empty();
					$('.cruser2').append("<a class='manage-ren-alert zb-ren-add' uid=''></a>");
					if (data.userList!=null && data.userList.length!=0) {
						for (var i=0;i<data.userList.length;i++) {
							$('.cruser2').append("<a class='manage-ren-alert' uid="+data.userList[i].id+">"+data.userList[i].userName+"<em class='zb-set-del ur-del'>X</em></a>");
							$(".ur-del").click(function(){
								$(this).parent().remove();
							});
						}
					}
					$(".zb-ren-add").click(function(event) {
						$('.pop-wrapp').show();
					});
					toTop();
					Common.dialog($('#editClass'));
					return false;
				 }
			});
		});
		//编辑功能教室关闭按钮
		$('#editClass').on('click','.closePop,#editCancel', function(event) {
			Common.dialog.close($('#editClass'));
		});
		//编辑功能教室确定按钮
		$("#editDone").on("click",function(){
			var Obj = {};
			Obj.classRoomId = PUBLIC_CLASSROOMID;
		    Obj.number = $("#editNum").val();
			Obj.classRoomName = $("#editClassName").val().trim();
		    //Obj.administratorId = $("#editManager").val();
    		//Obj.administratorName = $("#editManager").find("option:selected").html();
			var uid = '';
			$('.cruser2 a').each(function(i) {
				uid += $('.cruser2 a').eq(i).attr('uid') + ',';
			});
			Obj.users = uid;
    		if(isNull(Obj)){//空值判断
    			$.ajax({
    				type: 'post',
    				url: "/functionclassroom/updateFunctionClassRoom.do",
    				data: Obj,
    			    dataType: 'json',
    			    success:function(data){
    			    	if(data.code==200){
    			    		alert("编辑成功！");
    			    		Common.dialog.close($('#editClass'));
    			    		queryFunClassRoomList(CURPAGE);
    			    	}else{
    			    		alert(data.message);
    			    	}
    			    },
    			    error:function(){
    			    	alert("编辑失败！");
    			    }
    			});
    		}
		});
		//功能教室删除
		$("#classRoomList").on("click",".del",function(){
			if(confirm("确定删除?")){
				var classRoomId = $(this).parent().attr("flag");
				$.ajax({
					type: 'post',
					url: "/functionclassroom/delFunClassRoom.do",
					data: {'classRoomId':classRoomId},
				    dataType: 'json',
				    success:function(data){
				    	if(data.code==200){
				    		alert("删除成功！");
				    		var index = $("#classRoomList").find("tr:last").index();
				    		if(index-1==0&&CURPAGE>1){
				    			queryFunClassRoomList(CURPAGE-1);
				    		}else{
				    			queryFunClassRoomList(CURPAGE);
				    		}
				    	}
				    },
				    error:function(){
				    	alert("删除失败！");
				    }
				});
			}
		});
		//添加功能教室
		$('.addClass').on('click', function() {
			Common.dialog($('#addClass'));
			$('#poptype').val(0);
			$('.cruser').empty();
			$('.cruser').append("<a class='manage-ren-alert zb-ren-add' uid=''></a>");
			$(".zb-ren-add").click(function(event) {
				$('.pop-wrapp').show();
			});
			toTop();
			//清空管理员下拉列表选择
			return false;
		});
		//添加功能教室关闭按钮
		$('#addClass').on('click','.closePop,#addCancel', function(event) {
			Common.dialog.close($('#addClass'));
			clearAddClassText();
		});
		//加载功能教室管理员
		function loadManage(){
			$.ajax({
				type: 'post',
				url: "/functionclassroom/loadmanages.do",
				data: {},
			    dataType: 'json',
			    success:function(data){
			    	var $manager = $('#manager');
			    	var $editManager = $('#editManager');
			    	for(var i=0;i<data.length;i++){
			    		var optHtml = "<option value='"+data[i].id+"'>"+data[i].userName+"</option>";
			    		$manager.append(optHtml);
			    		$editManager.append(optHtml);
			    	}
			    }
			});
		};
		//添加功能教室确定按钮
		$("#addDone").on("click",function(){
			var Objs = {};
			Objs.number = $("#number").val();
			Objs.classRoomName = $("#className").val().trim();
			//Objs.administratorId = $("#manager").val();
			//Objs.administratorName = $("#manager").find("option:selected").html();
			var uid = '';
			$('.cruser a').each(function(i) {
				uid += $('.cruser a').eq(i).attr('uid') + ',';
			});
			Objs.users = uid;
			if(isNull(Objs)){//空值判断
				$.ajax({
					type: 'post',
					url: "/functionclassroom/addFunctionClassRoom.do",
					data: Objs,
				    dataType: 'json',
				    success:function(data){
				    	if(data.code==200){
				    		alert("添加成功！");
				    		Common.dialog.close($('#addClass'));
				    		queryFunClassRoomList(CURPAGE);
							clearAddClassText();
				    	}else{
				    		alert(data.message);
				    	}
				    },
				    error:function(){
				    	alert("添加失败！");
				    }
				});
			}
		});
		//判断空值
		function isNull(Obj){
			if(Obj.number==""){
				alert("请填写序号！");
				return false;
			}
			if(isNaN(Obj.number)){
				alert("序号请填写数字！");
				return false;
			}
			if(Obj.number<0){
				alert("请填写正确的序号！");
				return false;
			}
			if(Obj.classRoomName==""){
				alert("请填写功能教室名称！");
				return false;
			}
			if(Obj.users==''){
				alert("请选择委托管理者！");
				return false;
			}

			return true;
		}
		//去除填写内容
		function clearAddClassText(){
			 $("#number").val("");
			 $("#className").val("");
			 //$("#manager").val("ALL");
		};
		 //功能教室统计按钮
		$('#classRoomList').on('click','.count',function(event) {
			var classRoomId = $(this).parent().attr("flag");
			COUNT_CLASSROOMID = $(this).parent().attr("flag");
			//加载学期
			loadTerm(classRoomId);
			var classRoomName = $(this).parent().parent().find("td:eq(1)").text();
			$("#count").find(".pop-title span:first").html(classRoomName + "预约详情");
			//加载列表
			var term = $("#term").val();
			queryAndFillList(term,classRoomId);
		});
		
	   
	    //根据功能教室的ID和学期查询并填充列表
	    function queryAndFillList(term,crId,pageNo){
	    	var queryObj = {};
	    	queryObj.term = term;
	    	queryObj.classRoomId = crId;
	    	queryObj.pageNo = pageNo==null?1:pageNo;
	    	queryObj.pageSize = pageSize;
	    	
	    	$.ajax({
				type: 'post',
				url: "/functionclassroom/queryappointmentList.do",
				data: queryObj,
			    dataType: 'json',
			    success:function(data){
			    	var subData = data.datas;
			    	var $appointDetial = $("#appointDetial");
			    	$("#appointDetial tr:gt(0)").remove();
			    	if(subData.length>0){
			    		for(var j=0;j<subData.length;j++){
			    			var reason = subData[j].reasons;
			    			if(reason.length>10){
			    				var subReason = reason.substring(0,10)+"..";
			    			}else{
			    				var subReason = reason;
			    			}
				    		var trHtml =      " <tr>";
					    	trHtml = trHtml + "   <td>" + subData[j].startTime + "</td>";
					    	trHtml = trHtml + "   <td>" + subData[j].endTime + "</td>";
					    	trHtml = trHtml + "   <td>" + subData[j].user + "</td>";
					    	trHtml = trHtml + "   <td class='hand' title='" + subData[j].reasons + "'>" + subReason + "</td>";
					    	trHtml = trHtml + " </tr>";
					    	$appointDetial.append(trHtml);
				    	}
			    	}
			    	//填充完数据再弹框
			    	Common.dialog($('#count'));
			    	toTop();
			    	//分页
			    	$('#count-detial').jqPaginator({
						totalPages:  Math.ceil(data.total/pageSize) == 0?1:Math.ceil(data.total/pageSize),//总页数  
						visiblePages: 5,
						currentPage: data.cur,
						first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
			            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
			            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
			            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
			            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
			            onPageChange: function (n) { //回调函数
		  	            	if(isInit){
		  	            		queryAndFillList(term,crId,n);
		  	            		isInit = false;
		  	            	}else{
		  	            		isInit = true; 
		  	            		return;
		  	            	}
		  	            }
					});
			    },
			    error:function(){
			    	alert("数据加载失败！");
			    }
			});
	    };
	 
		//加载学期
		function loadTerm(classId){
			$.ajax({
				type: 'post',
				url: "/functionclassroom/getSemester.do",
				data: {'classRoomId':classId},
				async : false,
			    dataType: 'json',
			    success:function(data){
			    	var $term = $("#term");
			    	$term.html("");
			    	for(var i=0;i<data.length;i++){
			    		var optHtml = "<option value='"+data[i]+"'>"+data[i]+"</option>";
				    	$term.append(optHtml);
			    	}
			    }
			});
		};
		//功能教室统计关闭按钮
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#count'));
			$("#appointDetial tr:gt(0)").remove();
		});
		//学期下拉change事件
		$("#term").change(function(){
			queryAndFillList($(this).val(),COUNT_CLASSROOMID);
		});
		
	};
	
	(function() {
		classManage.init();
	})();

	module.exports = classManage;

});
