/*
* @Author: Tony
* @Date:   2016-09-23 11:17:26
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-23 11:30:23
*/

'use strict';

function chooseGroup() {
	var ids = [];
	$('.wind-addmember input[type=checkbox]').each(function() {
		if($(this).prop("checked")) {
			ids.push($(this).val());
		}
	});
	console.log(ids);
	$('.wind').fadeOut();
	$('.bg').fadeOut();
	
}

function showMsgBox(info) {
	$('.wind-del').fadeIn();
	$('.bg').fadeIn();
	$('#info').text(info);
}

function hideMsgBox() {
	$('.wind-del').fadeOut();
	$('.bg').fadeOut();
}

function check() {
	var prjName = $('input[name=projectName]').val();
	console.log('prjname:' + prjName)
	if(!prjName) {
		showMsgBox('请输入项目名称');
		return false;
	}
	
	var prjNum = $('input[name=projectNumber]').val();
	console.log('prjNum:' + prjNum)
	if(!prjNum) {
		showMsgBox('请输入项目编号');
		return false;
	}
	
	var startDate = $('input[name=startDate]').val();
	var endDate = $('input[name=endDate]').val();
	if(!startDate || !endDate) {
		showMsgBox('请输入项目起止时间');
		return false;
	}
	
	var owner = $('option:selected').val();
	console.log("owner:" + owner);
	if(owner === '-1') {
		showMsgBox('请选择项目负责人');
		return false;
	}
	
	var memsAcc = 0;
	$('input[name=staffs]').each(function() {
		if($(this).prop("checked")) {
			memsAcc++;
		}
	});
	console.log(memsAcc);
	if(memsAcc <= 0) {
		showMsgBox('请选择项目成员');
		return false;
	}
	
	var desc = $('textarea[name=projectDesc]').val();
	console.log("desc:" + desc)
	if(!desc) {
		showMsgBox('请输入项目描述');
		return false;
	}
	
	return true;
}

$(document).ready(function(){
	$('.ul-new li .sp3').click(function(){
		$(this).toggleClass('trang1')
		$('.member-list').slideToggle();
	});
	$('.member-list>p').click(function(){
		$(this).children('em').toggleClass('trang2');
		$(this).next('.ul-list-detial').slideToggle();
	});
	
	$('.item-conts .p-memb span em').click(function(){
		$(this).parent().hide();
	});
	$('.wind .p1 em,.wind .btn-no').click(function(){
		$('.wind').fadeOut();
		$('.bg').fadeOut();
	});
	
	$('.wind-del .btn-ok').click(function() {
		hideMsgBox();
	});
	
	$('.btn-addm').click(function(){
		$('.wind-addmember').fadeIn();
		$('.bg').fadeIn();
	});
	
	$(function() {
		$("#startDate").datetimepicker({
			format : 'yyyy-mm-dd',
			autoclose : true, // 自动关闭
			startView : 4, // 开始视图
			minView : 2, // 最详细视图
			todayBtn : true, // 显示今日按钮
			todayHighlight : true ,// 今日高亮
			language : 'zh-CN'
		});
	});
	
	$(function() {
		$("#endDate").datetimepicker({
			format : 'yyyy-mm-dd',
			autoclose : true, // 自动关闭
			startView : 4, // 开始视图
			minView : 2, // 最详细视图
			todayBtn : true, // 显示今日按钮
			todayHighlight : true ,// 今日高亮
			language : 'zh-CN'
		});
	});
})
