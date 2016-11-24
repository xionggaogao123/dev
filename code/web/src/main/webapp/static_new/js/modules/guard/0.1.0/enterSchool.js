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
	var queryObjCatch={};
	var PUBLIC_PAGE_SIZE =10;
	var grade="全部年级";
	var classroom="全部班级";
	/**
	 * 列表页面
	 */
	
	/*
	 * 获取年级下来菜单
	 */
	function selectGuard(){
		 $.ajax({
            url: "/guard/findByGrade.do",
            type: "post",
            datatype: "json",
            data: {"need":0},
            success: function (data) {
            	var gradeIds = data .gradeIds; 
            	var gradeList=data.gradeList;
            	$("#stuInGuardSelect").empty(); 
           	 var hrm="<option value='ALL'>全部年级</option>";
           	 $("#stuInGuardSelect").append(hrm);
           	 for(var i=0;i<gradeList.length;i++){
           		var htmlStr = "<option value="+gradeIds[i]+" >"+gradeList[i]+"</option>";
					$("#stuInGuardSelect").append(htmlStr);
				}
           	$("#enterSchoolLists").empty(); 
           	queryList(1,grade ,classroom);
            }
            
         });
    }
	/*
	 * 获取班级下拉菜单
	 */
	function selectClass(gradeId){
		 $.ajax({
           url: "/guard/findClass.do",
           type: "post",
           datatype: "json",
           data: {"need":0,"gradeId":gradeId},
           success: function (data) {
           var classList = data.classList;
       	 $("#stuInClassSelect").empty(); 
       	 var hrm="<option value='ALL'>全部班级</option>";
       	 $("#stuInClassSelect").append(hrm);
			for(var i=0;i<classList.length;i++){
				var htmlStr = "<option >"+classList[i]+"</option>";
				$("#stuInClassSelect").append(htmlStr);
			}
          	 
           }
           
        });
   }
	
	
	/*
	 * Guard change事件
	 */
	
	
	$("#stuInGuardSelect").change(function(){
		var gradeId = $(this).val();
        queryObjCatch.grade=$('#stuInGuardSelect option:selected').html()
		selectClass(gradeId);
		queryList(1, queryObjCatch.grade ,classroom);
	})
	
	/*
	 * class change事件
	 */
	
	$("#stuInClassSelect").change(function(){
		queryObjCatch.classroom=$('#stuInClassSelect option:selected').html()
		queryList(1, queryObjCatch.grade ,queryObjCatch.classroom);
	})
	
	
	
	var isInit = true;
	function queryList(pageNo,grade,classroom){
		var skip=(pageNo-1)*PUBLIC_PAGE_SIZE;
		queryObjCatch.grade=grade;
		queryObjCatch.classroom=classroom;
		queryObjCatch.page=pageNo;
		
    	
    		$.ajax({
        		type: "POST",
                url: "/guard/filterEnterList.do",   
                data: {"grade":grade,"classroom":classroom,"skip" :skip,"size":PUBLIC_PAGE_SIZE},
                dataType: "json",
                success: function(data){
                	queryObjCatch.total=data.total;
                	queryObjCatch.tt=Math.ceil(data.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.total/PUBLIC_PAGE_SIZE);
                	$('.new-page-links:eq(0)').jqPaginator({
          	            totalPages: Math.ceil(data.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.total/PUBLIC_PAGE_SIZE),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage:pageNo ,//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	if(isInit){
          	            		isInit = false;
          	            		queryList(n,grade,classroom);
          	            	}else{ 
          	            		isInit = true; 
          	            		return;
          	            	}
          	            }
          	        });
                	var courseList = data.datas;
                	var power = data.power;
                	if(power==0){
                		$("#enterSchoolLists").empty();
                		for(var ii=0;ii<courseList.length;ii++){
                    		var course = courseList[ii];
                    		var td=course.ers;
                    		if(td.length>9){
                    			var tt=td.substring(0,9) + "..."
                    		}else{
                    			var tt=td
                    		}
                    	

                    		var htmlStr = '  <tr flag="' + course.id + '" >'+
                    		'                    <td>' + course.gd + '</td>'+
                    		'                    <td>' + course.cr + '</td>'+
                    		'                    <td>' + course.sn + '</td>'+
                    		'                    <td>' + course.et + '</td>'+
                    		'                    <td title="'+td+'">' +tt+ '</td>'+
                    		'                    <td>'+
                    		'                        <a href="#" class="deleteEnter" >删除</a>'+
                    		'                    </td>'+
                    		'                </tr>';
                    		$("#enterSchoolLists").append(htmlStr);
                    	
                    }
                	}else{
                		$("#enterSchoolLists").empty();
                		for(var ii=0;ii<courseList.length;ii++){
                    		var course = courseList[ii];
                    		var td=course.ers;
                    		if(td.length>9){
                    			var tt=td.substring(0,9) + "..."
                    		}else{
                    			var tt=td
                    		}
                    		var htmlStr = '  <tr flag="' + course.id + '" >'+
                    		'                    <td>' + course.gd + '</td>'+
                    		'                    <td>' + course.cr + '</td>'+
                    		'                    <td>' + course.sn + '</td>'+
                    		'                    <td>' + course.et + '</td>'+
                    		'                    <td title="'+td+'">' +tt+ '</td>'+
                    		'                </tr>';
                    		$("#enterSchoolLists").append(htmlStr);
                    	
                    }
                	}
                }
        	});
    		}
	
	/**
	 * 新增页面
	 */
	
	
	/*
	 * 获取年级下来菜单
	 */
	function selectAddGuard(){
		 $.ajax({
            url: "/guard/findByGrade.do",
            type: "post",
            datatype: "json",
            data: {"need":0},
            success: function (data) {
            	var gradeIds = data.gradeIds; 
            	var gradeList=data.gradeList;
            	$("#inRecordIdGrade").empty(); 
           	 var hrm="<option>请选择</option>";
           	 $("#inRecordIdGrade").append(hrm);
           	 for(var i=0;i<gradeList.length;i++){
           		var htmlStr = "<option value="+gradeIds[i]+" >"+gradeList[i]+"</option>";
					$("#inRecordIdGrade").append(htmlStr);
				}
           	 
            }
            
         });
    }
	/*
	 * 获取班级下拉菜单
	 */
	function selectAddClass(gradeId){
		 $.ajax({
           url: "/guard/findClass.do",
           type: "post",
           datatype: "json",
           data: {"need":0,"gradeId":gradeId},
           success: function (data) {
           var classIds = data.classIds;
           var classList = data.classList;
       	 $("#inRecordIdClass").empty(); 
       	 var hrm="<option >请选择</option>";
       	 $("#inRecordIdClass").append(hrm);
			for(var i=0;i<classList.length;i++){
				var htmlStr = "<option value="+classIds[i]+">"+classList[i]+"</option>";
				$("#inRecordIdClass").append(htmlStr);
			}
          	 
           }
           
        });
   }
	
	/*
	 * 获取班级下学生的下拉菜单
	 */
	function selectAddName(classId){
		 $.ajax({
           url: "/guard/findStuInfo.do",
           type: "post",
           datatype: "json",
           data: {"classId":classId},
           success: function (data) {
        	  
        	   var classList=data.stuName;
          
       	 $("#inRecordIdName").empty(); 
       	 var hrm="<option >请选择</option>";
       	 $("#inRecordIdName").append(hrm);
			for(var i=0;i<classList.length;i++){
				var htmlStr = "<option>"+classList[i]+"</option>";
				$("#inRecordIdName").append(htmlStr);
			}
          	 
           }
           
        });
   }
	
	/*
	 * Guard change事件
	 */
	
	
	$("#inRecordIdGrade").change(function(){
		var gradeId =$(this).val();
		selectAddClass(gradeId);
	})
	
	/*
	 * class change事件
	 */
	
	$("#inRecordIdClass").change(function(){
		var classId =$(this).val();
		selectAddName(classId);
	})
	
	/**
	 * 新增
	 */
	
	function addEnterSchool(entryTime,grade,classroom,studentName,entryReasons){
		 $.ajax({
	           url: "/guard/addEnter.do",
	           type: "post",
	           datatype: "json",
	           data: {"entryTime":entryTime,"grade":grade,"classroom":classroom,"studentName":studentName,"entryReasons":entryReasons},
	           success: function (data) {
	            alert("保存成功");
	            Common.dialog.close($('#inRecordId'));
	            queryList(1,"全部年级","全部班级");
	           $(".Wdate-enter").val('');
	    		$('#inRecordIdGrade').empty();
	    		 $("#inRecordIdClass").empty();
	    		 $("#inRecordIdName").empty();
	    		$("#inRecordIdTextarea").val('');

	           }
	           
	        });
	}
	
	$('#inRecordIdSave').on('click',function(){ 
		var entryTime=$(".Wdate-enter").val();
		var grade = $('#inRecordIdGrade option:selected').html();
		var classroom = $("#inRecordIdClass option:selected").html();
		var studentName = $("#inRecordIdName option:selected").html();
		var entryReasons = $("#inRecordIdTextarea").val();
		if(entryTime==""){
			alert("请添加时间信息！");
		}else if(grade==""||grade=="请选择"){
			alert("请添年级信息！");
		}else if(classroom==""||classroom=="请选择"){
			alert("请添班级信息！");
		}else if(studentName==""||studentName=="请选择"){
			alert("请添学生信息！");
		}else if(entryReasons==""){
			alert("请添事由信息！");
		}else{
			addEnterSchool(entryTime,grade,classroom,studentName,entryReasons);
		}
		
	});
	/*
	 * 删除
	 */
	
	function del(targetId){
		 $.ajax({
            url: "/guard/deleteEnter.do",
            type: "post",
            datatype: "json",
            data: {"id":targetId },
            success: function (data) {
           	 if(queryObjCatch.page==queryObjCatch.tt){
           		 if(queryObjCatch.total%PUBLIC_PAGE_SIZE==1 && queryObjCatch.total!=1){
               		 alert("删除成功");
               		 queryList(queryObjCatch.page-1,queryObjCatch.grade,queryObjCatch.classroom);
               		
               	 }else{
               		 alert("删除成功");
          	       		queryList(queryObjCatch.page,queryObjCatch.grade,queryObjCatch.classroom);
          	       		
               	 }
           	 }else{
           		 alert("删除成功");
       	       	queryList(queryObjCatch.page,queryObjCatch.grade,queryObjCatch.classroom);
           		 
           	 }
           	 
           	 
            }
         
        });
	}
	/*
	 * 删除
	 */
	$('#enterSchoolLists').on('click','.deleteEnter',function(){
		if(confirm('确定删除吗?')){
			var targetId = $(this).closest("tr").attr("flag");
			del(targetId);
		}
		
	});
	
	
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example jiangCheng.init()
	 */
	guard.init = function() {


		Common.tab('click', $('.tab-head-new'))
		$('.addInRecord').on('click', function() {
			Common.dialog($('#inRecordId'));
			selectAddGuard();
			return false;
		})
		$('.closePop').on('click', function() {
			Common.dialog.close($('#inRecordId'));
			$(".Wdate-enter").val('');
			$('#inRecordIdGrade').empty();
   		    $("#inRecordIdClass").empty();
   		    $("#inRecordIdName").empty();
			$("#inRecordIdTextarea").val('');
			return false;

		})
		
	};
	
	
	
	
	guard.init();
	selectGuard();
	
})
