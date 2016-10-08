/*
* @Author: Tony
* @Date:   2016-09-23 11:17:26
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-23 11:30:23
*/

'use strict';

$(document).ready(function(){
	$('.ul-new li .sp3').click(function(){
		$(this).toggleClass('trang1')
		$('.member-list').slideToggle();
	});
	$('.member-list>p').click(function(){
		$(this).children('em').toggleClass('trang2');
		$(this).next('.ul-list-detial').slideToggle();
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
