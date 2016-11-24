/**
 * xusy 2016-11-09
 * */
define(['common', 'pagination'], function(require, expores, module) {
	var Common = require('common');
	require('pagination');
	$(function(){
		
		var gradeSel = $('#grade');
		var masterFlag = $("#masterFlag").val();
		if(gradeSel[0]) { // 是否获取到grade选择框
			findGrades();
		} else {
			findMasterClazzs();
		}
		
		$('#grade').change(function() {
			findClazzs();
		});
		
		$('.btn-cx').click(function() {
			if(masterFlag == 'true') {
				schoolMasterQuery(1);
			} else {
				clazzMasterQueryInfo(1);
			}
		});
		
		$('.btn-dc').click(function() {
			var exportType = 1;
			if(masterFlag != "true") {
				exportType = 2;
			}
			exportAttendanceInfo(exportType);
		});
		
		$(".table-xg").click(function(){
			$(".bg,.xg-alert").show();
			$('.alert-btn-sure').unbind('click');
		})
		$(".alert-close,.alert-btn-qx").click(function(){
			$(".bg").hide();
			$(".alert-main").parent().hide();
			$('.alert-btn-sure').unbind('click');
		})
	});
	
	function exportAttendanceInfo(exportType) {
		var clazzId = $('#clazz').val();
		var sDate = $('#sDate').val();
		var eDate = $('#eDate').val();
		var stuName = $('#stuName').val();
		var type = $('#type').val();
		var paramsStr = "";
		if(exportType == 1) {
			var gradeId = $('#grade').val();
			paramsStr = '?gradeId=' + gradeId + "&clazzId=" + clazzId 
				+ "&sDate=" + sDate + "&eDate=" + eDate + "&type=" + type 
				+ "&stuName=" + stuName + "&exportType=" + exportType;
		} else if(exportType == 2) {
			paramsStr = "?clazzId=" + clazzId 
			+ "&sDate=" + sDate + "&eDate=" + eDate + "&type=" + type 
			+ "&stuName=" + stuName + "&exportType=" + exportType;
		}
		window.open('/stuAttendance/export.do' + paramsStr);
	}
	
	function schoolMasterQuery(page) {
		var gradeId = $('#grade').val();
		var clazzId = $('#clazz').val();
		var sDate = $('#sDate').val();
		var eDate = $('#eDate').val();
		var stuName = $('#stuName').val();
		var type = $('#type').val();
		var isInit = true;
		$.ajax({
			cache : false,
			async : false,
			data : {
				gradeId : gradeId,
				clazzId : clazzId,
				page : page,
				sDate : sDate,
				eDate : eDate,
				type : type,
				stuName : stuName
			},
			url : '/stuAttendance/showMasterAttendInfo.do',
			type : 'get',
			dataType : 'json',
			success : function(resp) {
				var message = resp.message;
				$('.new-page-links').html("");
				$('#stuAttenInfo').empty();
				if(resp.code == '200' && message.list.length > 0) {
					$(".info-msg").hide();
					Common.render({
	                    tmpl: '#stuAttenInfoTmpl',
	                    data: message.list,
	                    context:'#stuAttenInfo',
	                    overwrite: 1
	                });
					$('.new-page-links').jqPaginator({
	                    totalPages: message.totalPage,//总页数
	                    visiblePages: 10,//分多少页
	                    currentPage: parseInt(page),//当前页数
	                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
	                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
	                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
	                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
	                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
	                    onPageChange: function (n) { //回调函数
	                        if (isInit) {
	                            isInit = false;
	                        } else {
	                        	schoolMasterQuery(n);
	                        }
	                    }
	                });
				} else {
					$(".info-msg").show();
				}
			}
		});
//		Common.getDataAsync('/stuAttendance/showMasterAttendInfo.do', 
//				{
//					gradeId : gradeId,
//					clazzId : clazzId,
//					page : page,
//					sDate : sDate,
//					eDate : eDate,
//					type : type,
//					stuName : stuName
//				}, 
//				function(resp) {
//					var message = resp.message;
//					$('.new-page-links').html("");
//					$('#stuAttenInfo').empty();
//					if(resp.code == '200' && message.list.length > 0) {
//						$(".info-msg").hide();
//						Common.render({
//		                    tmpl: '#stuAttenInfoTmpl',
//		                    data: message.list,
//		                    context:'#stuAttenInfo',
//		                    overwrite: 1
//		                });
//						$('.new-page-links').jqPaginator({
//		                    totalPages: message.totalPage,//总页数
//		                    visiblePages: 10,//分多少页
//		                    currentPage: parseInt(page),//当前页数
//		                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
//		                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
//		                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
//		                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
//		                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
//		                    onPageChange: function (n) { //回调函数
//		                        if (isInit) {
//		                            isInit = false;
//		                        } else {
//		                        	schoolMasterQuery(n);
//		                        }
//		                    }
//		                });
//					} else {
//						$(".info-msg").show();
//					}
//				}
//		);
	}
	
	function clazzMasterQueryInfo(page) {
		var clazzId = $('#clazz').val();
		var sDate = $('#sDate').val();
		var eDate = $('#eDate').val();
		var stuName = $('#stuName').val();
		var type = $('#type').val();
		var isInit = true;
		$.ajax({
			cache : false,
			async : true,
			data : {
				clazzId : clazzId,
				page : page,
				sDate : sDate,
				eDate : eDate,
				type : type,
				stuName : stuName
			},
			type : 'get',
			url : '/stuAttendance/masterQuer.do',
			dataType : 'json',
			success : function(resp) {
				var message = resp.message;
				$('.new-page-links').html("");
				$('#stuAttenInfo').empty();
				if(resp.code == '200' && message.list.length > 0) {
					$('.info-msg').hide();
					Common.render({
	                    tmpl: '#stuAttenInfoTmpl',
	                    data: message.list,
	                    context:'#stuAttenInfo',
	                    overwrite: 1
	                });
					$('.new-page-links').jqPaginator({
	                    totalPages: message.totalPage,//总页数
	                    visiblePages: 10,//分多少页
	                    currentPage: parseInt(page),//当前页数
	                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
	                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
	                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
	                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
	                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
	                    onPageChange: function (n) { //回调函数
	                        if (isInit) {
	                            isInit = false;
	                        } else {
	                        	clazzMasterQueryInfo( n);
	                        }
	                    }
	                });
				} else {
					$('.info-msg').show();
				}
			}
		});
//		Common.getDataAsync('/stuAttendance/masterQuer.do', 
//				{
//					clazzId : clazzId,
//					page : page,
//					sDate : sDate,
//					eDate : eDate,
//					type : type,
//					stuName : stuName
//				}, 
//				function(resp) {
//					var message = resp.message;
//					$('.new-page-links').html("");
//					$('#stuAttenInfo').empty();
//					if(resp.code == '200' && message.list.length > 0) {
//						$('.info-msg').hide();
//						Common.render({
//		                    tmpl: '#stuAttenInfoTmpl',
//		                    data: message.list,
//		                    context:'#stuAttenInfo',
//		                    overwrite: 1
//		                });
//						$('.new-page-links').jqPaginator({
//		                    totalPages: message.totalPage,//总页数
//		                    visiblePages: 10,//分多少页
//		                    currentPage: parseInt(page),//当前页数
//		                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
//		                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
//		                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
//		                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
//		                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
//		                    onPageChange: function (n) { //回调函数
//		                        if (isInit) {
//		                            isInit = false;
//		                        } else {
//		                        	clazzMasterQueryInfo( n);
//		                        }
//		                    }
//		                });
//					} else {
//						$('.info-msg').show();
//					}
//				}
//		);
	}
	
	function findGrades() { // 获取年级列表
		Common.getDataAsync('/myschool/gradelist.do', {}, function(resp) {
			if(resp || resp.rows.length > 0) {
				var gradeList = resp.rows;
				gradeList.unshift({'id':'-1', 'name':'全部年级'});
				Common.render({
                    tmpl: '#gradeTmpl',
                    data: gradeList,
                    context:'#grade',
                    overwrite: 1
                });
				findClazzs();
				schoolMasterQuery(1);
			}
		});
	}
	
	function findClazzs() { // 获取班级列表
		var gradeId = $('#grade').val();
		if(gradeId == "-1") {
			Common.render({
				tmpl : '#clazzTmpl',
				data : [{'id':'-1', 'className':'全部班级', 'classType':0}],
				context : '#clazz',
				overwrite : 1
			});
		} else {
			Common.getDataAsync('/myschool/classlist.do', {gradeid : gradeId}, function(resp) {
				var clazzList = [{'id':'0', 'className':'暂无班级', 'classType':0}];
				if(resp.rows.length > 0) {
					clazzList = resp.rows;
				}
				Common.render({
					tmpl : '#clazzTmpl',
					data : clazzList,
					context : '#clazz',
					overwrite : 1
				});
			});
		}
	}
	
	function findMasterClazzs() { // 获取班主任所在班级列表
		Common.getDataAsync('/stuAttendance/masterClazz.do', {}, function(resp) {
			var clazzList = [{'id':'0', 'className':'暂无班级', 'classType':0}];
			if(resp.message.length > 0) {
				clazzList = resp.message;
			}
			Common.render({
				tmpl : '#clazzTmpl',
				data : clazzList,
				context : '#clazz',
				overwrite : 1
			});
			clazzMasterQueryInfo(1);
		});
	}
 	
})