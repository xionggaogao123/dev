define([ 'common', 'ajaxfileupload', 'treeview', 'pagination' ], function(
		require, exports, module) {
	/**
	 * 初始化参数
	 */
	var guard = {}, Common = require('common');
	require('treeview');
	require('ajaxfileupload');
	require('pagination');
	Array.prototype.remove = function(dx) {
		if (isNaN(dx) || dx > this.length) {
			return false;
		}
		this.splice(dx, 1);
	}
	//定义对象
	var queryObjCatch={};
	var power;
	var visitors;
	var pageSizeForVisit = 10;
	var isFirstPageSizeForVisit = true;
	var pageNoNow = 1;
	//定义请求访客列表方法
	function visitList(pageNoNow){
		var skip=(pageNoNow-1)*pageSizeForVisit;
		queryObjCatch.page=pageNoNow;
		var page = {};
		page.skip = skip;
		page.size = pageSizeForVisit;
		$.ajax({
			type: "POST",
			url: "/guard/visitList.do",
			data: page,
			dataType: "json",
			success: function(data){
				queryObjCatch.total=data.total;
            	queryObjCatch.tt=Math.ceil(data.total/pageSizeForVisit) == 0?1:Math.ceil(data.total/pageSizeForVisit);
				//jqPaginator为分页组件
            	$('.new-page-links:eq(2)').jqPaginator({
      	            totalPages: Math.ceil(data.total/pageSizeForVisit) == 0?1:Math.ceil(data.total/pageSizeForVisit),//总页数  
      	            visiblePages: 10,//分多少页
      	            currentPage:pageNoNow ,//当前页数
      	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
      	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
      	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
      	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
      	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
      	            onPageChange: function (n) { //回调函数
      	            	//点击页码的查询
//      	            	alert('当前第' + n + '页'); 
      	            	if(isFirstPageSizeForVisit){
      	            		isFirstPageSizeForVisit = false;
      	            		visitList(n);
      	            	}else{
      	            		isFirstPageSizeForVisit = true; 
      	            		return;
      	            	}
      	            }
      	        });
            	visitors = data.datas;
            	power = data.power;
            	fillVisitList(data.datas);
            	fillDeptList(data.depts);
			}
		});
	}
	/**
	 * 在页面上显示访客列表
	 */
	function fillVisitList(visitorArr){
		$('#visit').html('');
		for(var i=0; i<visitorArr.length; i++){
			var visitor = visitorArr[i];
			//事由内容过长时省略显示
			var td=visitor.rs;
    		if(td.length>5){
    			var tt=td.substring(0,5) + "..."
    		}else{
    			var tt=td
    		}
    		
    		//单位内容过长时省略显示
    		var company = visitor.cp;
    		if(company.length>5){
    			var company2 = company.substring(0,5) + "...";
    		}else{
    			var company2 = company;
    		}

			var trHtml = "<tr flag='"+visitor.id+"'><td>";//id
			trHtml += visitor.vnam+"</td><td>"; //姓名
			trHtml += visitor.gd==1?"男":"女"; //性别
			trHtml += "</td><td class='leave' title='"+company+"'>";
			trHtml += company2+"</td><td>"; //单位
			trHtml += visitor.te+"</td><td>"; //电话
			trHtml += visitor.vt+"</td><td>"; //登访时间
			trHtml += visitor.obj+"</td><td class='leave' title='"+td+"'>"; //对象
			trHtml += tt+"</td><td>"; //事由
			trHtml += "<a href='#' class='lookIn'>查看</a>";
			if(power=='1'){
				trHtml += "</td></tr>";
			}else{
				trHtml += "<a href='#' class='delIn'>删除</a></td></tr>";//删除
			}
			$('#visit').html($('#visit').html()+trHtml);
		}
	}
	
	/**
	 * 将部门名称添加到下拉列表中
	 */
	function fillDeptList(depts){
		var arr = $('#addDept');
		arr.html("");
		for(var i=0;i<depts.length;i++){
			arr.append("<option '"+depts[i]+"'>"+depts[i]+"</option>");
		}
	}

	/**
	 * @func init
	 * @desc 页面初始化
	 * @example jiangCheng.init()
	 */
	guard.init = function() {
	/*-----------init方法开始--------------*/
		
//
//
		//实现表头切换
//		Common.tab('click', $('.tab-head-new'))
		//实现访问登记添加弹框
		$('.addInviteRecord').on('click', function() {
			Common.dialog($('#inviteRecordId'));
			return false;
		})
		//关闭访问登记添加弹框
		$('.closePop').on('click', function() {
			Common.dialog.close($('#inviteRecordId'));
			clearVisitor();
			return false;
		})
		//实现访问登记查看弹框
		$('.lookIn').on('click', function() {
			Common.dialog($('.lookInId'));
			return false;
		})
		//关闭访问登记查看弹框
		$('.closePop').on('click', function() {
			Common.dialog.close($('.lookInId'));
			return false;
		})
		
		$('#seeSure').on('click', function() {
			Common.dialog.close($('#seeSure'));
			return false;
		})
		
		//TODO
		//登访数据列表
		visitList(1);
		//给添加按钮定义点击事件 
		$(document).delegate('#saveVisitor','click',function(){
			var visitor = {};
			visitor.visitTime = $('#visitTime').val()==null?"":$('#visitTime').val().trim();
			visitor.visitorName = $('#visitorName').val()==null?"":$('#visitorName').val().trim();
			visitor.gender = $('#visitorGender').val()==null?"":$('#visitorGender').val().trim();
			visitor.company = $('#company').val()==null?"":$('#company').val().trim();
			visitor.telephone = $('#telephone').val()==null?"":$('#telephone').val().trim();
			visitor.numOfPeople = $('#numOfPeople').val()==null?"":$('#numOfPeople').val().trim();
			visitor.papers = $('#papers').val()==null?"":$('#papers').val().trim();
			visitor.reasons = $('#visitReasons').val()==null?"":$('#visitReasons').val().trim();
			visitor.department = $('#addDept').val()==null?"":$('#addDept').val().trim();
			visitor.object = $('#object').val()==null?"":$('#object').val().trim();
			var flag = valuesIsFull(visitor);//判断弹窗内容是否填满
			if(flag){
				addVisitor(visitor);//调用方法,向数据库添加数据
				Common.dialog.close($('#saveVisitor'));//关闭弹窗
				clearVisitor();  //清空弹窗内容
				visitList(1);
			}
		});
		
		//判断添加信息是否全部填写
		function valuesIsFull(visitor){
//			var Regx = /(^[0-9]$)/;
			if(visitor.visitTime==""){
				alert('请选择时间');
				return false;
			}else if(visitor.visitorName==""){
				alert('请输入姓名');
				return false;
			}else if(visitor.company==''){
				alert('请输入单位');
				return false;
			}else if(visitor.telephone==''){
				alert('请输入电话');
				return false;
			}else if(isNaN($('#telephone').val().trim())){
				alert('电话号码请填数字');
				return false;
			}else if(visitor.telephone.length>13){
				alert('电话号码位数太长,请重新输入!');
				
				return false;
			}else if(visitor.reasons==''){
				alert('请输入事由');
				return false;
			}else if(visitor.object==''){
				alert('请输入对象');
				return false;
			}else if(isNaN($('#numOfPeople').val().trim())){
				alert('人数请填写数字');
				return false;
			}
			return true;
		}
		
		//向数据库添加数据
		function addVisitor(visitor){
			$.ajax({
				type: "POST",
				url: "/guard/addVisit.do",
				data: visitor,
				success: function(){
					alert('添加成功');
				}
			});
		}
		
		//清除添加页面中的数据
		function clearVisitor(){
			$('#visitTime').val("");
			$('#visitorName').val("");
			$('#visitorGender').val("");
			$('#company').val("");
			$('#telephone').val("");
			$('#numOfPeople').val("");
			$('#papers').val("");
			$('#visitReasons').val("");
			$('#addDept').val("");
			$('#object').val("");
		}
		
		//给删除按钮定义点击事件
		$(document).delegate('.delIn','click',function(){
			if(confirm("确定删除？")){
				var id = $(this).parent().parent().attr('flag');
				var visitor = {};
				visitor.id = id;
				$.ajax({
					type: "POST",
					url: "/guard/deleteVisit.do",
					data: visitor,
					success: function(){
						 if(queryObjCatch.page==queryObjCatch.tt){
			           		 if(queryObjCatch.total%pageSizeForVisit==1 && queryObjCatch.total!=1){
			               		 alert("删除成功");
			               		visitList(queryObjCatch.page-1);
			               		
			               	 }else{
			               		 alert("删除成功");
			               		visitList(queryObjCatch.page);
			          	       		
			               	 }
			           	 }else{
			           		 alert("删除成功");
			           		visitList(queryObjCatch.page);
			           		 
			           	 }
					}
				});
			}
		});
		
		//给查看按钮添加事件
		$(document).delegate('.lookIn','click',function(){
			var id = $(this).parent().parent().attr('flag');
			var visitor = {};
			visitor.id = id;
			$.ajax({
				type: "POST",
				url: "/guard/visitorDetail.do",
				data: visitor,
				resultType: "json",
				success: function(data){
					$('#seeTime').val(data.visitTime);
					$('#seeName').val(data.visitorName);
					$('#seeGender').val(data.gender==1?"男":"女");
					$('#seePapers').val(data.papers);
					$('#seeCompany').val(data.company);
					$('#seeReasons').val(data.reasons);
					$('#seeDepartment').val(data.department);
					$('#seeObject').val(data.object);
					$('#seeTelephone').val(data.telephone);
					$('#seeNumOfPeople').val(data.numOfPeople);
					Common.dialog($('.lookInId'));
				}
			});
		});
		
		
		
		
	/*-----------init方法结束--------------*/
	};
	/*-----------init方法结束--------------*/
	

	(function() {
		guard.init();
	})();

})