/*
* @Author: Tony
* @Date:   2016-09-23 11:17:26
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-23 16:05:18
*/

'use strict';
var optionMemberId; // 定义全局变量用于保存要执行操作人员的id
var contextGloabl; // 全局 变量，项目上下文路径
$(document).ready(function(){
	
	contextGloabl = $("#ctx").val();
	
	$('.tab-member .th6 .sp1').click(function(){
		$('.wind-edit').fadeIn();
		$('.bg').fadeIn();
	});
	
	$(".p-member-top span").click(function() {
		$('.wind-new').fadeIn();
		$('.bg').fadeIn();
	});
	
	$('.wind .p1 em,.wind .btn-no').click(function(){
		$('.wind').fadeOut();
		$('.bg').fadeOut();
		optionMemberId = null;
	});
	
	$('#info-ok').click(function() {
		$('.wind').fadeOut();
		$('.bg').fadeOut();
	});
	
	$('#deleteBtn').click(function() { // 删除人员
		$.ajax({ 
			cache : false,
			async : false,
			url : contextGloabl + '/staff/remove',
			dataType : 'json',
			type : 'post',
			data : {
				id : optionMemberId
			},
			success : function(data) {
				if(data.code === 0) {
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					showMsgBox("删除成功！");
					setTimeout(function() {
						location.reload(true);
					}, 500);
					optionMemberId = null;
				}
				if(data.code === 1) {
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					showMsgBox("删除失败:" + data.msg);
					optionMemberId = null;
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
		        console.log(jqXHR + textStatus + errorThrown);
		    }
		});
	});
	
	$("#updateBtn").click(function() {
		var staffNum = $("#ed_staffNum").val();
		var name = $("#ed_name").val();
		var gender = $("#ed_gender input[type=radio]:checked").val();
		var departmentId = $("#ed_depart select").val();
		var sub_department = $("#ed_sub_depart select").val();
		//console.log("sd" + sub_department)
		var jobTitle = $("#ed_jbTitle").val();
		var isPrjOwner = $("#ed_is_owner input[type=radio]:checked").val();
		$.ajax({
			cache : false,
			async : false,
			url : contextGloabl + '/staff/update',
			type : 'post',
			dataType : 'json',
			data : {
				id : optionMemberId,
				jobNumber : staffNum,
				name : name,
				gender : gender,
				departmentId : departmentId,
				subDepartment : sub_department,
				jobTitle : jobTitle,
				isOwner : isPrjOwner
			},
			success : function(data) {
				if(data.code === 0) { // 更新成功
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					showMsgBox("更新成功！");
					setTimeout(function() {
						location.reload(true);
					}, 500);
					optionMemberId = null;
				}
				if(data.code === 1) { // 更新失败
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					optionMemberId = null;
					showMsgBox("更新失败：" + data.msg);
				}
			},
			error : function(arg0, arg1, arg2) {
				hideAll();
				showMsgBox("出错了");
			}
		});
	});

	$("#addBtn").click(function() { // 增加新员工
		var jobNum = $("#new_staffNum").val();
		var name = $("#new_name").val();
		var loginName = $("#new_login_name").val();
		var pwd = $("#new_pwd").val();
		var gender = $("#new_gender input[type=radio]:checked").val();
		var departmentId = $("#new_depart select").val();
		var subDepartment = $("#new_sub_depart select").val();
		var jobTitle = $("#new_jbTitle").val();
		var isPrjOwner = $("#new_is_owner input[type=radio]:checked").val();
		$.ajax({
			async : false,
			cache : false,
			type : 'post',
			data : {
				jobNumber : jobNum,
				name : name,
				gender : gender,
				loginName : loginName,
				password : pwd,
				'department.id' : departmentId,
				'subDepartment.id' : subDepartment,
				jobTitle : jobTitle,
				isPrjOwner : isPrjOwner
			},
			dateType : 'json',
			url : contextGloabl + '/staff/save',
			success : function(data) {
				if(data.code === 0) { // 添加成功
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					showMsgBox("更新成功！");
					setTimeout(function() {
						location.reload(true);
					}, 500);
				}
				
				if(data.code === 1) { // 添加失败
					$('.wind').fadeOut();
					$('.bg').fadeOut();
					showMsgBox("添加失败：" + data.msg);
				}
			},
			error : function(arg0, arg1, arg2) {
				$('.wind').fadeOut();
				$('.bg').fadeOut();
				showMsgBox("出错了！");
			}
		});
	});
	
});

function hideAll() {
	$('.wind').fadeOut();
	$('.bg').fadeOut();
}

function showMsgBox(msg) {
	$('.wind-msg').fadeIn();
	$('.bg').fadeIn();
	$('#msg').text(msg);
}

function getMemInfo() {
	$.ajax({
		cache : false,
		async : false,
		url : contextGloabl + '/staff/'+ optionMemberId + '/info',
		type : 'post',
		dataType : 'json',
		success : function(data) {
			$("#ed_staffNum").val(data.jobNumber);
			$("#ed_name").val(data.name);
			$("#ed_gender input").prop("checked", false);
			if(data.gender) {
				$("#ed_gender input[type=radio][value=" + data.gender + "]").prop("checked","checked");
			}
			$("#ed_depart option[value=" + data.department + "]").prop("selected", true);
			$("#ed_depart option").trigger("change");
			$("#ed_sub_depart option[value=" + data.subDepartment + "]").prop("selected", true);
			$("#ed_jbTitle").val(data.jobTitle);
			
			$("#ed_is_owner input[type=radio]").prop("checked", false);
			if(data.isPrjOwner) {
				$("#ed_is_owner input[type=radio][value=" + data.isPrjOwner + "]").prop("checked","checked");
			}
		},
		error : function(arg0, arg1, arg2) {
			hideAll();
			showMsgBox("出错了");
			return;
		}
	});
}

function editMember(id) {
	
	optionMemberId = id;
	getMemInfo();
	$('.wind-edit').fadeIn();
	$('.bg').fadeIn();
}

function deleteMember(id) {
	
	optionMemberId = id;
	$('.wind-del-mem').fadeIn();
	$('.bg').fadeIn();
}

function listMember(page, ctx) {
	if(!ctx)
		ctx = "";
	$.ajax({ 
		cache : false,
		async : false,
		url : ctx + '/staff/show',
		dataType : 'json',
		type : 'post',
		data : {
			pageNum : page || 1
		},
		success : function(data) {
			if(data.code === 1) { // 成功获取数据
				$("#member").empty();
				for(var i = 0; i < data.results.length; i++) {
					var html = '<tr><td class="th1">' + data.results[i].jobNumber + '</td><td class="th2">' + data.results[i].name + '</td><td class="th3">';
					if(data.results[i].gender === "1") {
						html += '<img src="' + ctx + '/images/img_man.png"></td>';
					} else {
						html += '<img src="' + ctx + '/images/img_women.png"></td>';
					}
					html += '<td class="th4">' + data.results[i].department;
					html += '</td><td class="th5">' + data.results[i].jobTitle + '</td><td class="th6"><span class="sp1" onclick="editMember(' + data.results[i].id + ')"></span><span class="sp2" onclick="deleteMember(' + data.results[i].id +')"></span></td></tr>';
					$("#member").append(html);
				}
			}
			if(data.code === 0) { // 未获取到数据
				hideAll();
				showMsgBox("出错了：" + data.msg);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log(jqXHR + textStatus + errorThrown);
		}
	});
}

function pagenation(totalPage, ctx) {
	$('#list').jqPaginator({
		totalPages: totalPage,
		visiblePages: 5,
		currentPage: 1,
		first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
		prev: '<li class="prev"><a href="javascript:void(0);">上一页<\/a><\/li>',
		next: '<li class="next"><a href="javascript:void(0);">下一页<\/a><\/li>',
		last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
		page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
		onPageChange: function (num, type) {
			listMember(num, ctx);
		}
	});
}