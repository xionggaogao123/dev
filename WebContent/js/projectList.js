/*
* @Author: Tony
* @Date:   2016-09-23 11:17:26
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-23 16:16:07
*/

'use strict';
var context; // 获取上下文路径
var totalPage; // 总页数
$(document).ready(function(){
	context = $('#ctx').val();
	totalPage = $('#totalPage').val();
	//$('.ul-company-member li .sp1 em').click(function())
	
//	if(parseInt(totalPage) > 0) {
//		$('#list').jqPaginator({
//			totalPages: parseInt(totalPage),
//			visiblePages: 5,
//			currentPage: 1,
//			first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
//			prev: '<li class="prev"><a href="javascript:void(0);">上一页<\/a><\/li>',
//			next: '<li class="next"><a href="javascript:void(0);">下一页<\/a><\/li>',
//			last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
//			page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
//			onPageChange: function (num, type) {
//				console.log(num + "--")
//				listMember(num, context);
//			}
//		});
//	}
	$('#searchBtn').click(function() {
		var status = $('#prjStatus').val();
		console.log("status:" + status);
		listMember(1, status, context);
	});
	listMember(1, -1, context);
});


function listMember(page, status, ctx) {
	if(!ctx)
		ctx = "";
	var isInit = true;
	$.ajax({ 
		cache : false,
		async : false,
		url : ctx + '/project/show',
		dataType : 'json',
		type : 'post',
		data : {
			page : page || 1,
			status : status || -1
		},
		success : function(data) {
			$("#pro_list").empty();
			$('#list').empty();
			if(data.code === 1) { // 成功获取数据
				$('#list').jqPaginator({
					totalPages: data.totalPage,
					visiblePages: 5,
					currentPage: 1,
					first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
					prev: '<li class="prev"><a href="javascript:void(0);">上一页<\/a><\/li>',
					next: '<li class="next"><a href="javascript:void(0);">下一页<\/a><\/li>',
					last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
					page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
					onPageChange: function (num, type) {
						if(isInit) {
							isInit = false
						} else {
							console.log(num + "--")
							listMember(num, status, context);
						}
					}
				});
				for(var i = 0; i < data.results.length; i++) {
					var html = '<li><span class="sp1"><em onclick="window.location.href=\'' + ctx + '/project/' + data.results[i].id + '/detail\'">';
					html += data.results[i].projectName + '</em></span><span class="sp2"><img src="' + ctx + '/images/img_black_man.png">';
					var createdDate = new Date(data.results[i].createdDate);
					var dateStr = createdDate.getFullYear() + "-" + (createdDate.getMonth() + 1) + "-" + createdDate.getDate();
					html += data.results[i].ownerName + '</span><span class="sp2"><img src="' + ctx + '/images/img_black_man.png">' + data.results[i].createrName + '</span>'
					html += '<span class="sp3">' + dateStr + '</span></li>';
					$("#pro_list").append(html);
				}
			}
			if(data.code === 0) { // 未获取到数据
				
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log(jqXHR + textStatus + errorThrown);
		}
	});
}