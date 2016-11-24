/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module  报修管理操作
 * @description
 * 报修管理操作
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var bxgl = {},
        Common = require('common');
    			require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }


    bxgl.dialog = function(obj,callback){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        $('body').scrollTop(0);
    };

    bxgl.close = function(obj){
        obj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    };
    
    
    
    
    
    
    
    
    
    
    
    
    
    function getUsers(depId)
    {
    	
    	
    	if(depId)
    	{
    	
		        	$.ajax({
		        		type: "GET",
		                url: "/myschool/department/member/list.do?id="+depId,
		                dataType: "json",
		                success: function(data){
		                	document.getElementById("repairDepartmentUser").innerHTML = "";
		                	for(var i=0;i<data.length;i++){
		                		$("#repairDepartmentUser").append("<option value='" + data[i].value +"'>" + data[i].name +"</option>");
		                	}
		                	
		                }
		        	}); 
    	}
    	
    	
    	

       
    }
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    bxgl.init = function(){


        Common.tab('click',$('.tab-head-new'))
        //评分
        $(document).delegate('.icon-dh','click',function(){
        	$("#evaluationCommitBtn").data("flag",$(this).parent().parent().attr("flag"));
        	$("#pingfenfkId").find("input").prop("checked",false);
            Common.dialog($('#pingfenfkId'));
            return false;
        });
        
        $(document).delegate('#repairDepartment','change',function(){
        	
        	var depid=jQuery("#repairDepartment").val();
        	getUsers(depid);
        	 return false;
        });
        
        
        //查看查询页面报修内容
		$(document).delegate('.content-view','click',function(){
			var rcDetail;
			for(var i=0;i<queryViewCatch.repairData.length;i++){
				if(queryViewCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryViewCatch.repairData[i];
				}
			}
//			$("#repairContentForView").val(rcDetail.repairContent);
			document.getElementById("repairContentForView").innerHTML = rcDetail.repairContent==null?"":rcDetail.repairContent;
            Common.dialog($('#bxcontentId'));
            return false;
        })
        //查看管理页面报修情况
        $(document).delegate('.content-view-manage','click',function(){
			var rcDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryManageCatch.repairData[i];
				}
			}
//			$("#repairContentForView").val(rcDetail.repairContent);
			document.getElementById("repairContentForView").innerHTML = rcDetail.repairContent==null?"":rcDetail.repairContent;
            Common.dialog($('#bxcontentId'));
            return false;
        })
        //查看查询页面报修结果情况
        $(document).delegate('.content-result','click',function(){
			var rcDetail;
			for(var i=0;i<queryViewCatch.repairData.length;i++){
				if(queryViewCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryViewCatch.repairData[i];
				}
			}
			document.getElementById("repairResultForView").innerHTML = rcDetail.repairResult==null?"":rcDetail.repairResult;
            Common.dialog($('#jgcontentId')); 
            return false;
        })
        //查看管理页面报修结果情况
        $(document).delegate('.content-result-manage','click',function(){
			var rcDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryManageCatch.repairData[i];
				}
			}
			document.getElementById("repairResultForView").innerHTML = rcDetail.repairResult==null?"":rcDetail.repairResult;
            Common.dialog($('#jgcontentId')); 
            return false;
        })
        //编辑报修对话框显示方法
        $(document).delegate('.icon-edit','click',function(){
        	var rDetail;
			for(var i=0;i<queryViewCatch.repairData.length;i++){
				if(queryViewCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rDetail = queryViewCatch.repairData[i];
				}
			}
			//初始化内部所有标签内容并填充当前需要编辑的报修条目
			$("#editDeclareRepairPerson").html(userObj.userName);
			$("#editDepartmentSelection").val(rDetail.repairDepartmentId);
			$("#editRepairPlace").val(rDetail.repairPlace);
			$("#editPhone").val(rDetail.phone);
			$("#editRepairType").val(rDetail.repairType);
			$("#editRepairContent").val(rDetail.repairContent);
			$("#editRepairContent").val(rDetail.repairContent);
			$("#editDeclareRepairPerson").data("flag",rDetail.id);
            Common.dialog($('#editbxsqId'));
            return false;
        })
//**********************************************************************************************************************************
        //上报至教育局对话框的显示方法
        $(document).delegate('.icon-ups','click',function(){
        	var rcDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryManageCatch.repairData[i];
				}
			}
			//初始化内部所有标签内容并填充当前需要编辑的报修条目
			$("#editUpPerson").html(userObj.userName);
			$("#editUpPlace").html(rcDetail.repairPlace);
			$("#editUpPhone").val(rcDetail.phone);
			$("#editUpType").html(rcDetail.repairType);
			$("#editUpContent").html(rcDetail.repairContent);
			$("#editUpPerson").data("flag",rcDetail.id);
			bxgl.dialog($('#editUpId'));
        })
        
        //结果填写按钮绑定事件
        $(document).delegate('.result-view, .icon-result','click',function(){
        	var rDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rDetail = queryManageCatch.repairData[i];
				}
			}
			$("#repairResultSubmitBtn").data("flag",$(this).parent().parent().attr("flag"));
			$("#repairResult").val("");
            Common.dialog($('#bxresultId')); 
            return false;
        })
        //指定维修人员按钮绑定事件
        $(document).delegate('.icon-set','click',function(){
        	var rDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rDetail = queryManageCatch.repairData[i];
				}
			}
			$("#handlerRepairUserCommitBtn").data("flag",$(this).parent().parent().attr("flag"));
			$.ajax({
        		type: "POST",
                url: "/repair/queryMemberByDepartmentId.do",
                data: {departmentId:rDetail.repairDepartmentId},
                dataType: "json",
                success: function(data){
                	document.getElementById("handleRepairUserSelection").innerHTML = "";
                	for(var i=0;i<data.length;i++){
                		$("#handleRepairUserSelection").append("<option value='" + data[i].userId +"'>" + data[i].userName +"</option>");
                	}
                	Common.dialog($('#bxstatusId'));
                }
        	}); 
            
            return false;
        })
        //删除按钮绑定事件
        $(document).delegate('.icon-del','click',function(){
        	if(confirm('确定删除?')){
        		var delId = $(this).parent().parent().attr("flag")
                var delObj = {};
                delObj.id = delId;
                $.ajax({
            		type: "POST",
                    url: "/repair/deleteRepair.do",
                    data: delObj,
                    dataType: "json",
                    success: function(data){
                    	if(data.code == "200"){
                    		alert("删除成功！");
                    		queryForQueryView();
                    	}else if(data.code == "300"){
                    		alert("该报修记录已上报，无法删除！");
                    	}else{
                    		alert("删除出错，请联系管理员！");
                    	}
                    }, 
                    error:function(data){
                    	alert("删除出错，请联系管理员！");
                    }
            	}); 
        	}
                return false;
        });
        
    //报修申请部分
        //页面初始化装载新增报修至部门和编辑报修下拉框
        for(var i=0;i<departments.length;i++){
        	$("#repairDepartment").append("<option value='" + departments[i].id + "'>" + departments[i].name + "</option>");
        	$("#editDepartmentSelection").append("<option value='" + departments[i].id + "'>" + departments[i].name + "</option>");
        	$("#handlerDepartment").append("<option value='" + departments[i].id + "'>" + departments[i].name + "</option>");
        }
    	var depid=jQuery("#repairDepartment").val();
    	getUsers(depid);
        $("#handlerDepartment").append("<option value='"+schoolId+"'>教育局</option>");
        //初始化新增tab页中的报修人
        $("#declareRepairPerson").html(userObj.userName);
//        $("#declareRepairPerson").attr("userId",userObj.userId);
        //绑定报修申请按钮事件
        $("#addRepairBtn").click(function(){
        	var addRepairObj = {};
        	addRepairObj.userId = userObj.userId;
        	addRepairObj.userName = userObj.userName;
        	addRepairObj.repairDepartmentId = $("#repairDepartment").val();
        	addRepairObj.depUser=$("#repairDepartmentUser").val();
        	addRepairObj.repairPlace = $("#repairPlace").val();
        	addRepairObj.phone = $("#phone").val();
        	addRepairObj.repairType = $("#repairType").val();
        	addRepairObj.repairContent = $("#repairContent").val();
        	if($.trim(addRepairObj.repairPlace) == ""){
        		alert("报修地点不能为空！");
        		return;
        	}
        	if($.trim(addRepairObj.phone) == ""){
        		alert("联系电话不能为空！");
        		return;
        	}
        	if($.trim(addRepairObj.repairContent) == ""){
        		alert("报修描述不能为空！");
        		return;
        	}
        	
        	$.ajax({
        		type: "POST",
                url: "/repair/addRepair.do",
                data: addRepairObj,
                dataType: "json",
                success: function(data){
                	alert("报修申请提交成功！");
                	resetAddForm();
                }
        	}); 
        });
        //绑定报修重置按钮事件
        $("#resetAddFromBtn").click(resetAddForm);
        
    //报修查询页面相关
        //初始化表头
        changeQueryListTitle();
        //报修查询参数
        var queryObjForQueryView = {};
        var publicPageNoForQueryView = 1;
    	var publicPageSizeForQueryView = 15;
    	var isFirstPageForQueryView = true;
        //报修查询页面初始查询
//        queryForQueryView();
        //报修查询缓存
        var queryViewCatch;
        //绑定查询页面学期变更事件，表头标题跟着变化。
//        $("#queryTermType").change(changeQueryListTitle);
        function changeQueryListTitle(){
        	$("#queryTermTypeForList").html("报修明细  " + $("#queryTermType option:selected").text());
        }
        //绑定查询页面查询按钮
        $("#queryBtnForQueryView").click(queryFirstForQueryView);
        function queryFirstForQueryView(){
        	uploadQueryObjForQueryView(1);
        }
        //查询参数组装并查询
        function uploadQueryObjForQueryView(pageNo){
        	queryObjForQueryView.termType = $("#queryTermType").val()==null?"All":$("#queryTermType").val();
        	queryObjForQueryView.repairProcedure = $("#queryRepairProcedure").val() == null ? "All":$("#queryRepairProcedure").val();
        	queryObjForQueryView.pageNo = pageNo;
        	queryObjForQueryView.pageSize = publicPageSizeForQueryView;
        	queryForQueryView(null);
        }
        //报修查询页面查询总方法
        function queryForQueryView(pageNo){
        	var qObj = {};
        	qObj.termType = queryObjForQueryView.termType;
        	qObj.repairProcedure = queryObjForQueryView.repairProcedure;
        	qObj.pageSize = publicPageSizeForQueryView;
        	if(pageNo == null){
        		qObj.pageNo = queryObjForQueryView.pageNo;
        	}else{
        		qObj.pageNo = pageNo;
        	}
        	$.ajax({
        		type: "POST",
                url: "/repair/queryRepairsForView.do",
                data: qObj,
                dataType: "json",
                success: function(data){
                	changeQueryListTitle();
                	queryViewCatch = data;
                	 $('#pageForView').jqPaginator({
          	            totalPages: Math.ceil(data.pagejson.total/publicPageSizeForQueryView) == 0?1:Math.ceil(data.pagejson.total/publicPageSizeForQueryView),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: parseInt(data.pagejson.pageNo),//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	//点击页码的查询
//          	            	alert('当前第' + n + '页'); 
          	            	if(isFirstPageForQueryView){
          	            		queryForQueryView(n);
          	            		isFirstPageForQueryView = false;
          	            	}else{
          	            		isFirstPageForQueryView = true; 
          	            	}
          	            }
          	        });
                	fillQueryList(data.repairData);
                }
        	}); 
        }
        //根据返回的json数据进行拼装表单中内容
        function fillQueryList(repairArr){
//        	$("#queryList").html("<tbody></tbody>");
        	document.getElementById("queryList").innerHTML = "";
        	for(var i=0;i<repairArr.length;i++){
        		var subObj = repairArr[i];
        		var placeName = subObj.repairPlace;
        		if(placeName.length>10){
        			placeName = subObj.repairPlace.substring(0,10)+"...";
        		}
        		var trHtml = "<tr flag='" + subObj.id +"'><td>";
        		trHtml = trHtml + subObj.repairDate + "</td><td>";
        		trHtml = trHtml + subObj.declareRepairPersonName + "</td><td style='cursor:pointer' title="+subObj.repairPlace+">";
        		trHtml = trHtml + placeName + "</td><td>";
        		trHtml = trHtml + "<a href='#' class='content-view'>查看</a>" +"</td><td>";
        		trHtml = trHtml + subObj.repairDepartmentName + "</td><td>";
        		
        		
        		
        		
        		
        		
        		
        		trHtml = trHtml + subObj.repairProcedure + "</td><td>";
        		
        		
        		
        		
        	
        		
        		
        		trHtml = trHtml + ( subObj.solveRepairPersonName==null?"":subObj.solveRepairPersonName ) + "</td><td>";
        		trHtml = trHtml + ( subObj.repairResult==null?"":"<a href='#' class='content-result'>查看</a>" ) + "</td><td>";
        		if(subObj.repariEvaluation == null){
        			trHtml = trHtml + "</td><td>";
        		}else{
        			var starNum = parseInt(subObj.repariEvaluation);
        			for(var j=1;j<6;j++){
        				if(j < starNum + 1){
        					trHtml = trHtml + "<i class='on-xing'></i>";
        				}else{
        					trHtml = trHtml + "<i class='un-xing'></i>";
        				}
        			}
        			trHtml = trHtml + "</td><td>"
        		}
        		if(subObj.repairProcedure == "待处理"){
        			trHtml = trHtml + "<a href='#' class='icon-edit'></a>|<a href='#' class='icon-del'></a>";
        		}
        		if(subObj.repairProcedure == "已完毕" && subObj.repariEvaluation == null){
        			trHtml = trHtml + "<a href='#' class='icon-dh'></a>";
        		}
        		trHtml = trHtml + "</td></tr>"
//        		$("#queryList").append(trHtml);
        		document.getElementById("queryList").innerHTML =  document.getElementById("queryList").innerHTML + trHtml;
//        		$("#VIEW-" + subObj.id).data("repairData",subObj);
//        		$("#VIEW-" + subObj.id).data("repairData","2134134234234234234234");
        	}
        }
        //报修查询页面评分确认按钮（重评按钮去除）
        $("#evaluationCommitBtn").click(function(){
        	var evaluationStar = $('#starBg input[name="score"]:checked ').val();
        	if(evaluationStar == null){
        		alert("请选择评价星级");
        		return;
        	}
//        	alert(evaluationStar);
        	$.ajax({
        		type: "POST",
                url: "/repair/updateEvaluation.do",
                data: {starNum:evaluationStar,id:$("#evaluationCommitBtn").data("flag")},
                dataType: "json",
                success: function(data){
                	queryForQueryView();
                }
        	}); 
        });
        //报修查询页面编辑对话框确认提交按钮事件绑定
        $("#editSubmitRepairBtn").click(function(){
        	var updateRepairObj = {};
        	updateRepairObj.id = $("#editDeclareRepairPerson").data("flag");
        	updateRepairObj.repairDepartmentId = $("#editDepartmentSelection").val();
        	updateRepairObj.repairPlace = $("#editRepairPlace").val();
        	updateRepairObj.phone = $("#editPhone").val();
        	updateRepairObj.repairType = $("#editRepairType").val();
        	updateRepairObj.repairContent = $("#editRepairContent").val();
        	
        	$.ajax({
        		type: "POST",
                url: "/repair/updateRepair.do",
                data: updateRepairObj,
                dataType: "json",
                success: function(data){
                	if(data.code == "200"){
                		alert("修改报修申请成功！");
                    	queryForQueryView();
                	}else if(data.code == "300"){
                		alert("该报修记录已上报，无法修改！");
                	}else{
                		alert("修改出错，请联系管理员！");
                	}
                }
        	}); 
        	
        });
 //*********************************************************************************************************************************
        //上报到教育局关闭按钮绑定事件
        $('#upToEduCloseBtn').click(function(){
        	bxgl.close($("#editUpBtn"));
        });
      //上报教育局弹窗确认按钮时间绑定
        $("#editUpBtn").click(function(){
        	var updateObj = {};
        	updateObj.id = $("#editUpPerson").data("flag");
        	updateObj.manager = $("#editUpPerson").val();
        	updateObj.phone = $("#editUpPhone").val();
        	updateObj.reason = $("#editUpReason").val();
        	updateObj.repairDepartmentName = "教育局";
        	if($.trim(updateObj.phone) == ""){
        		alert("请填写电话号码！");
        		return;
        	}
        	if($.trim(updateObj.reason) == ""){
        		alert("请填写上报原因！");
        		return ;
        	}
        	$.ajax({
        		type: "post",
        		url: "/repair/updateRepairs.do",
        		data: updateObj,
        		dataType: "json",
        		success: function(data){
        			alert("上报成功！");
        			bxgl.close($("#editUpBtn"));
        			$("#editUpReason").val("");
        			queryRepairsForManage();
        		}
        	});
        });
        
        
        
        
        
        
        
   //管理报修页面相关功能和方法
      //初始化表头
		changeListTitle();
		//表头自动变化方法和下拉菜单绑定方法
		function changeListTitle(){
			var titleText = "报修明细  " + $("#handlerTermType option:selected").text() + " " + $("#handlerDepartment option:selected").text();
			$("#manageListTitle").html(titleText);
		}
//		$("#handlerTermType").change(changeListTitle);
//		$("#handlerDepartment").change(changeListTitle);
        //管理报修页面查询缓存
        var queryManageCatch;
        //管理报修页面查询参数缓存
        var queryObjForQueryManage = {};
        var publicPageNoForQueryManage = 1;
    	var publicPageSizeForQueryManage = 15;
    	var isFirstPageForQueryManage = true;
        //管理报修页面查询方法
        $("#queryHandlerListBtn").click(queryFirstForManage);
        function queryFirstForManage(){
        	uploadQueryObjForQueryManage(1);
        }
        //查询参数组装并查询
        function uploadQueryObjForQueryManage(pageNo){
        	queryObjForQueryManage.termType = $("#handlerTermType").val()==null?"All":$("#handlerTermType").val();
        	queryObjForQueryManage.repairDepartmentId = $("#handlerDepartment").val() == null ? "All":$("#handlerDepartment").val();
        	queryObjForQueryManage.repairProcedure = $("#handlerRepairProcedure").val() == null ? "All":$("#handlerRepairProcedure").val();
        	queryObjForQueryManage.pageNo = pageNo;
        	queryObjForQueryManage.pageSize = publicPageSizeForQueryManage;
        	queryRepairsForManage(null);
        }
        //管理报修页面查询方法
        function queryRepairsForManage(pageNo){
        	var qObj = {};
        	qObj.termType = queryObjForQueryManage.termType ;
        	qObj.repairDepartmentId = queryObjForQueryManage.repairDepartmentId;
        	qObj.repairProcedure = queryObjForQueryManage.repairProcedure;
        	qObj.pageSize = queryObjForQueryManage.pageSize;
        	if(pageNo == null){
        		qObj.pageNo = queryObjForQueryManage.pageNo;
        	}else{
        		qObj.pageNo = pageNo;
        	}
        	
        	$.ajax({
        		type: "POST",
                url: "/repair/queryRepairsForManage.do",
                data: qObj,
                dataType: "json", 
                success: function(data){
                	changeListTitle();
                	queryManageCatch = data;
                	$('#pageForManage').jqPaginator({
          	            totalPages: Math.ceil(data.pagejson.total/publicPageSizeForQueryManage) == 0?1:Math.ceil(data.pagejson.total/publicPageSizeForQueryManage),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: parseInt(data.pagejson.pageNo),//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	//点击页码的查询
//          	            	alert('当前第' + n + '页'); 
          	            	if(isFirstPageForQueryManage){
          	            		queryRepairsForManage(n);
          	            		isFirstPageForQueryManage = false;
          	            	}else{
          	            		isFirstPageForQueryManage = true; 
          	            	}
          	            }
          	        });
                	fillQueryManageList(data.repairData);
                }
        	}); 
        }
        
        
        //为管理报修页面表单组装内容
        function fillQueryManageList(repairArr){
//        	$("#queryList").html("<tbody></tbody>");
        	document.getElementById("handlerList").innerHTML = "";
        	for(var i=0;i<repairArr.length;i++){
        		var subObj = repairArr[i];
        		var placeName = subObj.repairPlace;
        		if(placeName.length>10){
        			placeName = subObj.repairPlace.substring(0,10)+"...";
        		}
        		var trHtml = "<tr flag='" + subObj.id +"'><td>";//id
        		trHtml = trHtml + subObj.repairDate + "</td><td>";//报修日期
        		trHtml = trHtml + subObj.declareRepairPersonName + "</td><td style='cursor:pointer' title="+subObj.repairPlace+">";//报修人
        		trHtml = trHtml + placeName + "</td><td>";//报修地点
        		trHtml = trHtml + "<a href='#' class='content-view-manage'>查看</a>" +"</td><td>";//内容查看
        		trHtml = trHtml + subObj.repairDepartmentName + "</td><td>";//维修部门
        		
        		

        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		if(subObj.repairProcedure == "已完毕"){
        			trHtml = trHtml + subObj.repairProcedure + "</td><td>";
        		}else if(subObj.repairProcedure == "待处理" || (subObj.isReport == 1 && subObj.isResolve == 0)){
        			trHtml = trHtml + "待处理" + "</td><td>";
        		}else if((subObj.repairProcedure == "已受理" && subObj.isReport == 0) || (subObj.isReport == 1 && subObj.isResolve == 1)){
        			trHtml = trHtml + "已受理" + "</td><td>";
        		}
        		
        
        		trHtml = trHtml + ( subObj.solveRepairPersonName==null?"":subObj.solveRepairPersonName ) + "</td><td>";
        		trHtml = trHtml + ( subObj.repairResult==null?"":"<a href='#' class='content-result-manage'>查看</a>" ) + "</td><td>";
        		if(subObj.repariEvaluation == null){
        			trHtml = trHtml + "</td><td>";
        		}else{
        			var starNum = parseInt(subObj.repariEvaluation);
        			for(var j=1;j<6;j++){
        				if(j < starNum + 1){
        					trHtml = trHtml + "<i class='on-xing'></i>";
        				}else{
        					trHtml = trHtml + "<i class='un-xing'></i>";
        				}
        			}
        			trHtml = trHtml + "</td><td>"
        		}
        		//操作
        		if(subObj.repairProcedure == "已完毕" || subObj.isReport == 1){
        			trHtml = trHtml + "";
        		}else if(subObj.repairProcedure == "待处理" && isAdmin){
        			trHtml = trHtml + "<a href='#' class='icon-set'>";
        		}else if(subObj.isReport == 0 && subObj.repairProcedure == "已受理" && subObj.repariEvaluation == null){
        			trHtml = trHtml + "<a href='#' class='icon-result'>";
        		}
        		trHtml = trHtml + "</td>";
        		//上报
        		if(isAdmin){
        			if(subObj.repairProcedure == "已完毕" && subObj.isReport == 1){
            			trHtml = trHtml + "<td>已上报</td></tr>";
            		}else if(subObj.isReport == 0 && (subObj.repairProcedure == "待处理" ||subObj.repairProcedure == "已受理") ){
            			trHtml = trHtml + "<td>"+"<a href='#' class='icon-ups'>"+"</td></tr>";
            		}else if(subObj.repairProcedure == "已完毕" && subObj.isReport == 0){
            			trHtml = trHtml + "<td></td></tr>";
            		}else if(subObj.repairProcedure == "已受理" && subObj.isReport == 1 && (subObj.isResolve == 0 || subObj.isResolve==1)){
            			trHtml = trHtml + "<td>已上报</td></tr>";
            		}
        			
        		}else{
        			trHtml = trHtml + "</tr>";
        		}
        		document.getElementById("handlerList").innerHTML =  document.getElementById("handlerList").innerHTML + trHtml;
        	}
        }
        
        //管理报修页面分配维修人员确认按钮绑定事件
        $("#handlerRepairUserCommitBtn").click(function(){
        	$.ajax({
        		type: "POST",
                url: "/repair/deliverRepairMission.do",
                data: {workerId:$("#handleRepairUserSelection").val(),repairId:$(this).data("flag")},
                dataType: "json",
                success: function(data){
                	alert("报修任务分派成功！");
                	queryRepairsForManage();
                }
        	}); 
        });
        //管理报修页面填写维修结果
        $("#repairResultSubmitBtn").click(function(){
        	$.ajax({
        		type: "POST",
                url: "/repair/commitRepairResult.do",
                data: {resultContent:$("#repairResult").val(),repairId:$("#repairResultSubmitBtn").data("flag")},
                dataType: "json",
                success: function(data){
                	alert("维修结果提交成功！");
                	queryRepairsForManage();
                }
        	}); 
        });

        
//======init方法后括号==============  
    };
  //======init方法后括号==============
    
    
    //重置新增表单方法
    function resetAddForm(){
    	$("#repairDepartment").val($($("#repairDepartment").find("option").get(0)).val());
    	$("#repairPlace").val("");
    	$("#phone").val("");
    	$("#repairType").val($($("#repairType").find("option").get(0)).val());
    	$("#repairContent").val("");
    }
    
    
    
    
    (function(){
        bxgl.init();
    })();

    module.exports = bxgl;
})
