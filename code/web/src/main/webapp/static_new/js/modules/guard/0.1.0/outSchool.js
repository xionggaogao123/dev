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

	/**
	 * @func init
	 * @desc 页面初始化
	 * @example jiangCheng.init()
	 */
	guard.init = function() {
//
//
//		Common.tab('click', $('.tab-head-new'))
		
		/**
		 * 公共变量
		 */
		var PUBLIC_PAGE_SIZE = 10;
		var pageNoNow = 1;
		var isInit=true;
		var grade = $('#out-grade option:selected').html();
		var classroom = $('#out-class option:selected').html();
		var queryObjCatch={};
		
		/**
		 * 出校列表
		 */
		function outList(pageNoNow,grade,classroom){
			var skip=(pageNoNow-1)*PUBLIC_PAGE_SIZE;
			
			queryObjCatch.grade=grade;
			queryObjCatch.classroom=classroom;
			queryObjCatch.page=pageNoNow;
			var shuju={};
	    	shuju.skip = skip;
	    	shuju.size = PUBLIC_PAGE_SIZE;
	    	shuju.grade = grade;
	    	shuju.classroom=classroom;
			$.ajax({
				url:"/guard/outList.do",
				type:"post",
				data:shuju,
				datatype:"json",
				success:function(data){
			
//					$("#outSch").html("");
					queryObjCatch.total=data.total;
					queryObjCatch.tt=Math.ceil(data.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.total/PUBLIC_PAGE_SIZE);
					$('.new-page-links:eq(1)').jqPaginator({
          	            totalPages: Math.ceil(data.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.total/PUBLIC_PAGE_SIZE),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: pageNoNow,//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	if(isInit){
          	            		isInit = false;
          	            		outList( n,grade,classroom);
          	            	}else{ 
          	            		isInit = true; 
          	            		return;
          	            	}
          	            }
          	        });
	               	var courseList = data.datas;
	               	
	               		$("#outSch").empty();
	               	var power = data.power;
	               	if(power == 0){
	               		for(var ii=0;ii<courseList.length;ii++){
	                		var course = courseList[ii];
	                		var td=course.ors;
                    		if(td.length>7){
                    			var tt=td.substring(0,7) + "..."
                    		}else{
                    			var tt=td
                    		}
	                		var htmlStr = '  <tr flag="' + course.id + '" >'+
	                		'                    <td>' + course.gd + '</td>'+
	                		'                    <td>' + course.cs + '</td>'+
	                		'                    <td>' + course.sn + '</td>'+
	                		'                    <td>' + course.apt + '</td>'+
	                		'                    <td>' + course.ot + '</td>'+
	                		'                    <td title="'+td+'">' + tt + '</td>'+
	                		'                    <td>'+
	                		'                        <a href="#" class="delete deleteEnter deleteOut" flag="'+course.id+'">删除</a>'+
	                		'                    </td>'+
	                		'                </tr>';
	                		$("#outSch").append(htmlStr);
	                	
	               		} 	
	               	}else{
	               		$("#outSch").empty();
	               		for(var ii=0;ii<courseList.length;ii++){
	                		var course = courseList[ii];
	                		var td=course.ors;
                    		if(td.length>7){
                    			var tt=td.substring(0,7) + "..."
                    		}else{
                    			var tt=td
                    		}
	                		var htmlStr = '  <tr flag="' + course.id + '" >'+
	                		'                    <td>' + course.gd + '</td>'+
	                		'                    <td>' + course.cs + '</td>'+
	                		'                    <td>' + course.sn + '</td>'+
	                		'                    <td>' + course.apt + '</td>'+
	                		'                    <td>' + course.ot + '</td>'+
	                		'                    <td title="'+td+'">' + tt + '</td>'+
	                		'                </tr>';
	                		$("#outSch").append(htmlStr);
	                	
	                	} 	
	               	}
				},
				error:function(){
		   			alert("加载失败！");
		   		}
			});
		}
		
		/**
		 * 根据年级id查询班级信息
		 */
		function findByGrade(gradeId,need){
			$.ajax({
				url:"/guard/findClass.do",
				type:"post",
				data:{"gradeId":gradeId,"need":need},
				datatype:"json",
				success:function(data){
					
					var classList = data.classList;
					var classIds = data.classIds;
					if(need == 0){
						$("#out-class option[Value!='ALL']").remove();
						for(var i=0;i<classList.length;i++){
							var htmlStr = "<option value="+classList[i]+">"+classList[i]+"</option>";
							$("#out-class").append(htmlStr);
						}
					}else{
						$("#outSch-class option[Value!='ALL']").remove();
						for(var i=0;i<classList.length;i++){
							var htmlStr = "<option value="+classIds[i]+">"+classList[i]+"</option>";
							$("#outSch-class").append(htmlStr);
						} 
					}
				}
			});
		}
		
		/**
		 * 根据班级ID查学生信息
		 */
		function findStuInfo(classId){
			$.ajax({
				url:"/guard/findStuInfo.do",
				type:"post",
				data:{"classId":classId},
				datatype:"json",
				success:function(data){
					var stuName = data.stuName;
					$("#outSch-name option[Value!='ALL']").remove();
					for(var i=0;i<stuName.length;i++){
						var htmlStr = "<option value="+stuName[i]+">"+stuName[i]+"</option>";
						$("#outSch-name").append(htmlStr);
					}
				}
			})
		}
		
		/**
		 * 新增下拉框所需数据(根据学校ID查询年级信息)
		 */
		function needInfo(need){
			$.ajax({
				url:"/guard/findByGrade.do",
				type:"post",
				data:{"need":need},
				datatype:"json",
				success:function(data){
					var gradeList = data.gradeList;
					var gradeIds = data .gradeIds;
					if(need == 0){
						$("#out-grade").empty();
						var hrm="<option>全部年级</option>";
						 $("#out-grade").append(hrm);
						for(var i=0;i<gradeList.length;i++){
							var htmlStr = "<option value="+gradeIds[i]+">"+gradeList[i]+"</option>";
							$("#out-grade").append(htmlStr);
						}
					}else{
						for(var i=0;i<gradeList.length;i++){
							var htmlStr = "<option value="+gradeIds[i]+">"+gradeList[i]+"</option>";
							$("#outSch-grade").append(htmlStr);
						}
					}
					
				}
			});
		}
		
		/**
		 * 查询填充下拉菜单
		 */
		$("#out-grade").change(function(){
			
			var gradeId = $(this).val();
			var need = 0;
			findByGrade(gradeId,need);
		})
		
		/**
		 * 查询年级下拉change事件
		 */
		$("#out-grade").change(function(){
			grade = $('#out-grade option:selected').html();
			classroom = $('#out-class option:selected').html();
			if(classroom != "全部班级"){
				$("#out-class option[Value!='ALL']").remove();
				classroom = $('#out-class option:selected').html();
				outList(1,grade,classroom);
			}else{
				outList(1,grade,classroom);
			}
			
		})
		
		/**
		 *查询班级下拉change事件 
		 */
		$("#out-class").change(function(){
			grade = $('#out-grade option:selected').html();
			if(grade != "全部年级"){
				classroom = $('#out-class option:selected').html();
				outList(1,grade,classroom);
			}
		})
		
		
		/**
		 * 新增填充下拉菜单
		 */
		$("#outSch-grade").change(function(){
			var gradeId = $(this).val();
			var need = 1;
			findByGrade(gradeId,need);
		})
		
		/**
		 * 新增下拉班级change事件，填充学生姓名
		 */
		$("#outSch-class").change(function(){
			var classId = $("#outSch-class").val();
			findStuInfo(classId);
		})
		
		/**
		 * 弹窗按钮
		 */
		$('.addOutRecord').on('click', function() {
			Common.dialog($('#outRecordId'));
			var need = 1;
			needInfo(need);
		})
		/**
		 * 添加纪录
		 */
		$("#add-Out").on('click', function() {
			var outTime = $('.Wdate-out').val();
			grade = $('#outSch-grade option:selected').html();
			var classroom1 = $("#outSch-class option:selected").html();
			var studentName = $("#outSch-name").val();
			var approveTeacher = $(".agreeTeacherOuts").val();
			var outReasons = $(".rightAlignReason").val();
				
			if(outTime.trim() == ""){
				alert("请选择时间");
				return false;
			}else if(grade.trim() == "请选择"){
				alert("请选择年级");
				return false;
			}else if(classroom1.trim() == "请选择"){
				alert("请选择班级");
				return false;
			}else if(studentName.trim() == "ALL"){
				alert("请选择学生姓名");
				return false;
			}else if(approveTeacher.trim() == ""){
				alert("请输入批准老师");
				return false;
			}else if(outReasons.trim() == ""){
				alert("请输入事由");
				return false;
			}
			
			var shuju = {};
			shuju.outTime =outTime.trim();
			shuju.grade =grade.trim();
			shuju.classroom =classroom1.trim();
			shuju.studentName =studentName.trim();
			shuju.approveTeacher =approveTeacher.trim();
			shuju.outReasons =outReasons.trim();
			$.ajax({
				url:"/guard/addOut.do",
				type:"post",
				data:shuju,
				datatype:"json",
				success:function(data){
					
					if(confirm("添加成功")){
						grade = $('#out-grade option:selected').html();
						classroom = $('#out-class option:selected').html();
						outList(1,grade,classroom);
						Common.dialog.close($('#outRecordId'));
					}
					$('.Wdate-out').val('');
					$('.agreeTeacherOuts').val('');
					$('.rightAlignReasonOutSch').val('');
					$("#outSch-grade option[Value!='ALL']").remove();
					$("#outSch-class option[Value!='ALL']").remove();
					$("#outSch-name option[Value!='ALL']").remove();
				},
				error:function(){
			   		alert("添加失败！");
			   	}
			});
		})
		/**
		 * 关闭弹窗按钮
		 */
		$('.closePop').on('click', function() {
			Common.dialog.close($('#outRecordId'));
			$('.Wdate-out').val('');
			$('.agreeTeacherOuts').val('');
			$('.rightAlignReasonOutSch').val('');
			$("#outSch-grade option[Value!='ALL']").remove();
			$("#outSch-class option[Value!='ALL']").remove();
			$("#outSch-name option[Value!='ALL']").remove();
			return false;

		})
		
		/**
		 * 顶部点击事件
		 */
		$(".outSchools").on("click",function(){
			
			
		})
		
		/**
		 * 删除方法
		 */
		function delOut(id){
			$.ajax({
				url:"/guard/deleteOut.do",
				type:"post",
				data:{"id":id},
				datatype:"json",
				success:function(data){
					 if(queryObjCatch.page==queryObjCatch.tt){
		           		 if(queryObjCatch.total%PUBLIC_PAGE_SIZE==1 && queryObjCatch.total!=1){
		               		 alert("删除成功");
		               		outList(queryObjCatch.page-1,queryObjCatch.grade,queryObjCatch.classroom);
		               		
		               	 }else{
		               		 alert("删除成功");
		               		outList(queryObjCatch.page,queryObjCatch.grade,queryObjCatch.classroom);
		          	       		
		               	 }
		           	 }else{
		           		 alert("删除成功");
		           		outList(queryObjCatch.page,queryObjCatch.grade,queryObjCatch.classroom);
		           		 
		           	 }
		           	 
				}
			});
		}
		
		/**
		 * 删除按钮点击事件
		 */
		$("#outSch").on('click', '.deleteOut' ,function() {
		if(confirm("确定删除吗？")){
			var id = $(this).closest("tr").attr("flag");
			delOut(id);
		}
		})
		var need = 0;
			needInfo(need);
		outList(1,grade,classroom);
		
	};
	
	(function() {
		guard.init();
		
	})();

})
