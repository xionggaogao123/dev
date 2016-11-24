/*
 * @Author: Tony
 * @Date:   2015-08-14 16:02:24
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-14 17:13:56
 */

'use strict';
define(['doT','common'],function(require,exports,module){
	/**
	 *初始化参数
	 */
	var Common = require('common');
	var gzglData = {};
	var gzgl = {
		saveSalaryItem: function (id, itemName, newText) {
			$.ajax({
				url: "/wages/update.do",
				type: "post",
				datatype: "json",
				data: {id: id, itemName: itemName, m: newText},
				success: function (data) {
					if (data.code == 200) {
						var sal = data.message;
						var tds = $("td", $("#" + id));
						tds.eq(tds.length - sal.debitCount-4).text(sal.ssStr);
						tds.eq(tds.length - 3).text(sal.msStr);
						tds.eq(tds.length - 2).text(sal.asStr);
					} else {
						alert(data.message);
					}
				}
			});
		},
		retrieveSalary: function () {
			gzgl.wagelist();
		},
		filterDataByQuery: function () {
			gzgl.wagelist();
		},
		getSalaryTimes: function (year, month) {
			$.ajax({
				url: "/wages/getSalaryTimes.do",
				type: "get",
				datatype: "json",
				data: {year: year, month: month},
				success: function (data) {
					if (data.code == 200) {
						gzgl.buildSalaryNumber(data.message);
					} else {
						alert(data.message);
					}
				}
			});
		},
		buildSalaryNumber: function (data) {
			$("#salaryNumber").empty();
			var times;
			for (var i = 0, len = data.length; i < len; i++) {
				times = data[i];
				$("#salaryNumber").append("<option value='" + times + "'>" + times + "</option>");
			}
			if (data.length > 0) {
				$("#salaryNumber").val(times);
				gzgl.retrieveSalary();
			} else {
				$("#salaryNumber").append("<option value='0'>全部</option>");
			}
		},
		changeSalaryName: function (oldtext, newtext) {
			var year = $("#salaryYear").val();
			var month = $("#salaryMonth").val();
			var number = $("#salaryNumber").val();
			$.ajax({
				url: "renameSalary.do",
				type: "post",
				datatype: "json",
				data: {year: year, month: month, num: number, name: newtext},
				success: function (data) {
					if (data.code == 200) {
						$('#salary-list-title').text(newtext);
					} else {
						$('#salary-list-title').text(oldtext);
						alert(data.message);
					}
				},
				error: function () {
					$('#salary-list-title').text(oldtext);
				}
			});
		}
	};

	Array.prototype.remove = function (dx) {
		if (isNaN(dx) || dx > this.length) {
			return false;
		}
		this.splice(dx, 1);
	}
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 * jiaoyujv_start.init()
	 */
	gzgl.init = function(){


		//设置初始页码
		gzglData.page = 1;
		//设置每页数据长度
		gzglData.pageSize = 12;
		Common.render({
			tmpl: $('#yearListTemp'),
			data: yearList,
			context: '#salaryYear',
			overwrite: 1
		});
		$('#salaryMonth').change(function () {
			var year = $("#salaryYear").val();
			var month = $("#salaryMonth").val();
			gzgl.getSalaryTimes(year, month);
		});
		$("#genSalaryTemplate").click(function () {
			var $form = $("#genTemplateForm");
			$("[name=year]", $form).val($("#salaryYear").val());
			$("[name=month]", $form).val($("#salaryMonth").val());
			$("[name=num]", $form).val($("#salaryNumber").val());
			$form.attr("action", "/wages/template.do").submit();
		});
		$(".hand-XC").click(function () {
			var $form = $("#genTemplateForm");
			$("[name=year]", $form).val($("#salaryYear").val());
			$("[name=month]", $form).val($("#salaryMonth").val());
			$("[name=num]", $form).val($("#salaryNumber").val());
			$form.attr("action", "/wages/template.do").submit();
		});
		$('#salaryData').change(function(){
			$.ajaxFileUpload({
				url:'/salary/import.do',
				secureuri: false, //是否需要安全协议，一般设置为false
				fileElementId: 'salaryData', //文件上传域的ID
				dataType: 'json', //返回值类型 一般设置为json
				param: {},
				success: function(data,status){
					if (data.code != 200) {
						alert(data.message);
					} else {
						alert("工资导入成功");
						gzgl.retrieveSalary();
						$(".gzgl-meng").hide();
						$(".gzgl-gly-alert").hide();
					}
				},
				error: function (data, status, e)//服务器响应失败处理函数
				{
					alert("工资导入失败");
				}
			});
			return false;
		});
		$('#seachSalary').click(function () {
			$("#salaryHeaderBox").empty();
			$("#salaryBodyBox").empty();
			$("#salaryUserList").empty();
			gzgl.retrieveSalary();
		});
		$('.hand-dr').click(function () {
			$(".gzgl-meng").hide();
			$(".gzgl-gly-alert").hide();
		});
		$('body').on('click', '.editableTd', function () {
			var td = $(this);
			var text = td.text();
			var txt = $("<input type='text' maxlength='10' style='border:none;width:60px;background: #5db85c;'>").val(text);
			txt.on("blur", function () {
				var newText = $(this).val();
				var reg = /^\d+(\.\d+)?$/;
				var itemName = $(this).parent('.editableTd').attr('id');
				var id = $(this).parent('.editableTd').parent('tr').attr('id');
				$(this).remove();
				if (newText.match(reg)) {
					td.text(newText);
					gzgl.saveSalaryItem(id, itemName, newText);
				} else {
					td.text(text);
				}
			});
			td.text("");
			td.append(txt);
			txt.focus();
		});
		$('body').on('click', '.editableTd2', function () {
			var td = $(this);
			var text = td.text();
			var txt = $("<input type='text' maxlength='10' style='border:none;width:60px;background: #5db85c;'>").val(text);
			txt.on("blur", function () {
				var newText = $(this).val();
				//var reg = /^\d+(\.\d+)?$/;
				//var itemName = $(this).parent('.editableTd2').attr('id');
				var id = $(this).parent('.editableTd2').parent('tr').attr('id');
				$(this).remove();
				//if (newText.match(reg)) {
				//td.text(newText);
				$.ajax({
					url: "/wages/updateRemark.do",
					type: "post",
					datatype: "json",
					data: {id: id, m: newText},
					success: function (data) {
						if (data.code == 200) {
							td.text(newText);
						} else {
							alert(data.message);
							td.text("");
						}
					}
				});
					//gzgl.saveSalaryItem(id, itemName, newText);
				//} else {
				//	td.text(text);
				//}
			});
			td.text("");
			td.append(txt);
			txt.focus();
		});
		$("#salaryYear").val(currYear);
		$("#salaryMonth").val(currMonth);
		var year = $("#salaryYear").val();
		var month = $("#salaryMonth").val();
		this.getSalaryTimes(year, month);

		$('#deleteSalaryTable').click(function () {
			if (confirm('确定删除本次的工资单数据?')) {
				var year = $("#salaryYear").val();
				var month = $("#salaryMonth").val();
				var number = $("#salaryNumber").val();
				$.ajax({
					url: "/wages/deleteSalary.do",
					type: "post",
					datatype: "json",
					data: {year: year, month: month, num: number},
					success: function (data) {
						if (data.code == 200) {
							$('#theadTotal').hide();
							$("#salaryHeaderBox").empty();
							$("#salaryBodyBox").empty();
							$("#salaryUserList").empty();
							$("#salaryNumber").children("[value=" + number + "]").remove();
							if ($("#salaryNumber").children().length == 0) {
								$("#salaryNumber").append("<option value='0'>全部</option>");
							} else {
								$("#salaryNumber").val($("#salaryNumber").children(":last").attr("value"));
								gzgl.retrieveSalary();
							}
						} else {
							alert(data.message);
						}
					}
				});
			}
		});

		$("#gzglxm").click(function(){
			$(".tab2").show();
			$(".tab1").hide();
			gzgl.itemList();
		});
		$(".gzxmgl-sure").click(function(){
			if ($("#itemName").val() == null || $.trim($("#itemName").val()) == "") {
				$("#itemName").focus();
				alert("请输入项目名称！");
				return;
			}
			if ($.trim($("#itemName").val()).length>40) {
				$("#itemName").focus();
				alert("项目名称长度不超过40！");
				return;
			}
			gzglData.itemName = $.trim($("#itemName").val());
			gzglData.type = $("input[name='gzgl-type']:checked").val();
			if ($("#type").val()==1) {
				gzgl.itemAdd();
			} else if($("#type").val()==2) {
				gzglData.id=$('#salaryId').val();
				gzgl.itemUpdate();
			}

		});

		$("#back").click(function(){
			$(".tab2").hide();
			$(".tab1").show();
		});

		$("#addprojcet").click(function(){
			$('#itemName').val('');
			$('#type').val(1);
			var selects = document.getElementsByName("gzgl-type");
			for (var i=0; i<selects.length; i++){
				if (selects[i].value=="发款") {
					selects[i].checked= true;
					break;
				}
			}
			$(".gzgl-meng").show();
			$(".gzxmgl-alert").show();
		});
		$(".gzgl-zhibiao").click(function(){
			$("#salaryHeaderBox").empty();
			$("#salaryBodyBox").empty();
			$("#salaryUserList").empty();
			var year = $("#salaryYear").val();
			var month = $("#salaryMonth").val();
			$.ajax({
				url: "/wages/tabulate.do",
				type: "post",
				datatype: "json",
				data: {year: year, month: month},
				success: function (data) {
					if (data.code == 200) {
						gzgl.buildSalaryNumber(data.message);
						//$(".gzgl-meng").show();
						//$(".gzgl-gly-alert").show();
					} else {
						alert(data.message);
					}
				}
			});
		});
		$(".gzxmgl-qx,.gzgl-close").click(function(){
			$(".gzgl-meng").hide();
			$(".gzgl-gly-alert").hide();
		});
		$(".gzxmgl-qx,.gzxmgl-close").click(function(){
			$(".gzgl-meng").hide();
			$(".gzxmgl-alert").hide();
		});

	};
	gzgl.itemList = function() {
		Common.getData('/wages/itemList.do',gzglData,function(rep){
			$('.gzxmgl-table').html('');
			Common.render({tmpl: $('#gzxmgl_templ'), data: rep, context: '.gzxmgl-table'});
			$(".gzxmgl-edit").click(function(){
				$('#type').val(2);
				$('#itemName').val($(this).parent().parent().children().eq(1).text());
				$('#salaryId').val($(this).attr('vid'));
				var selects = document.getElementsByName("gzgl-type");
				for (var i=0; i<selects.length; i++){
					if (selects[i].value==$(this).parent().parent().children().eq(2).text()) {
						selects[i].checked= true;
						break;
					}
				}
				$(".gzgl-meng").show();
				$(".gzxmgl-alert").show();
			});
			$('.gzxmgl-del').bind('click', function () {
				if (confirm('确认删除此工资项目！')) {
					gzglData.id=$(this).attr('vid');
					gzgl.itemDelete();
				}
			});
		});
	}
	gzgl.itemAdd = function() {
		Common.getPostData('/wages/itemAdd.do',gzglData,function(rep){
			if (!rep) {
				alert("添加失败！");
			}
			if (rep.code == "200") {
				gzgl.itemList();
				$(".gzgl-meng").hide();
				$(".gzxmgl-alert").hide();
			} else {
				alert(rep.message);
			}
		});
	}
	gzgl.itemDelete = function() {
		Common.getPostData('/wages/itemDelete.do',gzglData,function(rep){
			if (!rep) {
				alert("删除失败！");
			}
			if (rep.code == "200") {
				gzgl.itemList();
			} else {
				alert(rep.message);
			}
		});
	}

	gzgl.itemUpdate = function() {
		Common.getPostData('/wages/itemUpdate.do',gzglData,function(rep){
			if (!rep) {
				alert("修改失败！");
			}
			if (rep.code == "200") {
				gzgl.itemList();
				$(".gzgl-meng").hide();
				$(".gzxmgl-alert").hide();
			} else {
				alert(rep.message);
			}
		});
	}

// 分页初始化
	gzgl.initPaginator=function (option) {
		var totalPage = '';
		$('.page-paginator').show();
		$('.page-index').empty();
		if (option.total % option.pagesize == 0) {
			totalPage = option.total / option.pagesize;
		} else {
			totalPage = parseInt(option.total / option.pagesize) + 1;
		}
		gzgl.buildPaginator(totalPage, option.currentpage);
		option.operate(totalPage);
	}

	gzgl.buildPaginator =function (totalPage, currentPage) {
		if (totalPage > 5) {
			if (currentPage < 4) {
				for (var i = 1; i <= 5; i++) {
					if (i == currentPage) {
						$('.page-index').append('<span class="active">' + i + '</span>');
					} else {
						$('.page-index').append('<span>' + i + '</span>');
					}
				}
				$('.page-index').append('<i>···</i>');
			} else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
				$('.page-index').append('<i>···</i>');
				for (var i = currentPage - 2; i <= currentPage + 2; i++) {
					if (i == currentPage) {
						$('.page-index').append('<span class="active">' + i + '</span>');
					} else {
						$('.page-index').append('<span>' + i + '</span>');
					}
				}
				$('.page-index').append('<i>···</i>');
			} else {
				$('.page-index').append('<i>···</i>');
				for (var i = totalPage - 4; i <= totalPage; i++) {
					if (i == currentPage) {
						$('.page-index').append('<span class="active">' + i + '</span>');
					} else {
						$('.page-index').append('<span>' + i + '</span>');
					}
				}
			}
		} else {
			for (var i = 1; i <= totalPage; i++) {
				if (i == currentPage) {
					$('.page-index').append('<span class="active">' + i + '</span>');
				} else {
					$('.page-index').append('<span>' + i + '</span>');
				}
			}
		}
	}
	gzgl.wagelist = function() {
		var year = $("#salaryYear").val();
		var month = $("#salaryMonth").val();
		var number = $("#salaryNumber").val();
		$.ajax({
			url: "/wages/list.do",
			type: "post",
			datatype: "json",
			data: {year: year, month: month, num: number,name: $.trim($('#username').val()),page:gzglData.page,pageSize:gzglData.pageSize},
			success: function (data) {
				$('#createTime').html("");
				$('#theadTotal').hide();
					if (data.salarylist.length > 0) {
						var item = [];
						var moneys = data.salarylist[0].money;
						var time = data.salarylist[0].createTime;
						$('#createTime').html(time);
						for (var i = 0; i < moneys.length; i++) {
							//if (filter != "0" && moneys[i].itemName != filter) {
							//	continue;
							//} else {
								item.push({id: "", itemName: moneys[i].itemName});
							//}
						}
						var salaryData = {};
						salaryData.salary = data.salarylist;
						salaryData.item = item;
						Common.render({
							tmpl: $('#salaryHeaderTemp'),
							data: data.salarylist[0].salaryItem,
							context: '#salaryHeaderBox',
							overwrite: 1,
							callback:function(){
								$('#salaryTableId table').width($('#salaryHeaderBox').find('th').length*120);
							}
						});
						Common.render({
							tmpl: $('#salaryListTemp'),
							data: salaryData,
							context: '#salaryBodyBox',
							overwrite: 1
						});
						if (salaryData!=null&&salaryData.salary!=null&&salaryData.salary.length!=0) {
							$('#theadTotal').show();
						} else {
							$('#theadTotal').hide();
						}
						Common.render({
							tmpl: $('#salaryUserListTemp'),
							data: salaryData,
							context: '#salaryUserList',
							overwrite: 1
						});
					}
					var option = {
						total: data.total,
						pagesize: data.pageSize,
						currentpage: data.page,
						operate: function (totalPage) {
							$('.page-index span').each(function () {
								$(this).click(function () {
									gzglData.page = $(this).text();
									gzgl.wagelist();
								})
							});
							$('.first-page').click(function () {
								gzglData.page = 1;
								gzgl.wagelist();
							});
							$('.last-page').click(function () {
								gzglData.page = totalPage;
								gzgl.wagelist();
							})
						}
					}
					gzgl.initPaginator(option);
			}
		});
	}
	gzgl.init();
});