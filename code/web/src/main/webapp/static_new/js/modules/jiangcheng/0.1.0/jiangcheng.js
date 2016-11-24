/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module  奖惩操作
 * @description
 * 奖惩操作模块
 */
/* global Config */




define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var jiangCheng = {},
        Common = require('common');
    			require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }


    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * jiangCheng.dialog(jQuery)
     */

    jiangCheng.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        obj.find('.pop-btn span').click(function(){
            $(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
        });
        $('body').scrollTop(0);
    };
    /**
     * @func localStorage
     * @desc 本地存储
     * @param {string,[string]} str [str] 获取数据[存储本地数据]
     * @example
     * jiangCheng.localStorage()
     */
    jiangCheng.localStorage = function(){
        if(!arguments.length){
            new Error('参数错误，至少需要一个参数！');
            return
        }
        if(arguments.length>2){
            new Error('参数错误，最多需要两个参数！');
            return
        }
        if(arguments.length === 1){
            var data = localStorage.getItem(arguments[0]);
        }
        if(arguments.length === 2){
            localStorage.setItem(arguments[0],arguments[1]);
        }
    };

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    jiangCheng.init = function(){
    	var publicQueryObject = {};
    	var publicPageNo = 1;
    	var publicPageSize = 10;
    	var isFirstPage = true;


        //导出按钮绑定方法
        $("#exportBtn").click(function(){
        	$("#gradeIdForm").val(publicQueryObject.gradeId);
        	$("#classIdForm").val(publicQueryObject.classId);
        	$("#rewardTypeForm").val(publicQueryObject.rewardType);
        	$("#rewardGradeForm").val(publicQueryObject.rewardGrade);
        	$("#studentNameForm").val(publicQueryObject.studentName);
        	$("#exportForm").submit();
        });
        //初始查询权限内所有结果
        getParameterAndQueryReward(1);
        //初始化年级下拉
        var gradesHtml = "<option value='ALL'>全部</option>";
    	for(var i=0;i<grades.length;i++){
    		gradesHtml = gradesHtml +"<option value='" + grades[i].id + "'>" + grades[i].name + "</option>";
    	}
//    	document.getElementById("pageGradeSelect").innerHTML = gradesHtml;
//    	document.getElementById("addGradeSelect").innerHTML = gradesHtml;
    	document.getElementById("pageGradeSelect").innerHTML = gradesHtml;
    	document.getElementById("addGradeSelect").innerHTML = gradesHtml;
    	fillClassSelect("ALL","pageClassSelect",true);
    	fillClassSelect("ALL","addClassSelect",false); 
    	
    	//初始化部门勾选框
    	Common.render({tmpl:$('#addListId'),data:departments,context:'#departmentBox',overwrite:1})
    	Common.render({tmpl:$('#editListId'),data:departments,context:'#editDepartmentBox',overwrite:1})
    	//初始化班级勾选框
    	var classesHtml = "<div class='all'><label><input type='checkbox' id='addAllClasses' value='ALL'>所有班级</label></div>";
    	var editClassesHtml = "<div class='all'><label><input type='checkbox' id='editAllClasses' value='ALL'>所有班级</label></div>";
    	for(var i=0;i<grades.length;i++){
    		classesHtml = classesHtml + "<div class='single clearfix'>";
    		editClassesHtml = editClassesHtml + "<div class='single clearfix'>";
    		classesHtml = classesHtml + "<label class='fclass'><input type='checkbox' id='ADD-" + grades[i].id + "' value='" + grades[i].id + "-ALL'/>" + grades[i].name + "</label>";
    		editClassesHtml = editClassesHtml + "<label class='fclass'><input type='checkbox' id='EDIT-" + grades[i].id + "' value='" + grades[i].id + "-ALL'/>" + grades[i].name + "</label>";
    		classesHtml = classesHtml + "<div class='fclass-list'>";
    		editClassesHtml = editClassesHtml + "<div class='fclass-list'>";
    		for(var j=0;j<classes.length;j++){
    			if(classes[j].gradeId == grades[i].id){
    				classesHtml = classesHtml + "<label><input type='checkbox' value='" + classes[j].id + "'/>" + classes[j].className + "</label>";
    				editClassesHtml = editClassesHtml + "<label><input type='checkbox' id='EDIT-" + classes[j].id + "' value='EDIT-" + classes[j].id +"'/>" + classes[j].className + "</label>";
    			} 
    		} 
    		classesHtml = classesHtml + "</div></div>";
    		editClassesHtml = editClassesHtml + "</div></div>";
    		$("#classBox").html(classesHtml);
    		$("#editClassBox").html(editClassesHtml);
    	}
    	//获取参数并调用查询方法
    	function getParameterAndQueryReward(pageNo){
        	publicQueryObject.gradeId = $("#pageGradeSelect").val();
        	publicQueryObject.classId = $("#pageClassSelect").val();
        	publicQueryObject.rewardType = $("#pageType").val();
        	publicQueryObject.rewardGrade = $("#pageLevelSelect").val();
        	publicQueryObject.studentName = $("#pageStudentName").val()==null || $("#pageStudentName").val()=="" ? "ALL":$("#pageStudentName").val();
        	publicQueryObject.pageNo = pageNo;
        	publicQueryObject.pageSize = publicPageSize;
        	queryRewards();

        }
    	
    	//==========================================================
//    	$(document).delegate("#pageTplId a","click",function(){
//    		queryRewards($(this).attr("data-page"));
//    	});
    	//==========================================================
    	
    	//查询总方法
    	function queryRewards(pageNo){
    		var queryObj = new Object();
    		queryObj.gradeId = publicQueryObject.gradeId;
    		queryObj.classId = publicQueryObject.classId;
    		queryObj.rewardType = publicQueryObject.rewardType;
    		queryObj.rewardGrade = publicQueryObject.rewardGrade;
    		queryObj.studentName = publicQueryObject.studentName;
    		if(pageNo == null){
    			queryObj.pageNo = publicQueryObject.pageNo;
    		}else{
    			publicQueryObject.pageNo = pageNo;
    			queryObj.pageNo = pageNo;
    		}
    		queryObj.pageSize = publicQueryObject.pageSize;
    		
    		// Common.render({tmpl:$('#pageTplId'),data:{cur:20,total:20},context:'.page-links',overwrite:1})

//            Common.page('queryRewards.do',queryObj,function(data){
//            	
//            	Common.render({tmpl:$('#tiaojianId'),data:data.rewardData,context:'#rewardList',overwrite:1})
//            	rewardListCatch = data.rewardData;
//            });
    		
    		
    		$.ajax({
        		type: "POST",
                url: "/reward/queryRewards.do",
                data: queryObj,
                dataType: "json",
                success: function(data){
                	rewardListCatch = data.rewardData;
                	 $('.new-page-links').jqPaginator({
         	            totalPages: Math.ceil(data.pagejson.total/publicPageSize) == 0?1:Math.ceil(data.pagejson.total/publicPageSize),//总页数
         	            visiblePages: 10,//分多少页
         	            currentPage: queryObj.pageNo,//当前页数
         	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
         	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
         	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
         	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
         	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
         	            onPageChange: function (n) { //回调函数
         	            	
         	            	//点击页码的查询
//         	            	alert('当前第' + n + '页'); 
         	            	if(isFirstPage){
         	            		queryRewards(n);
         	            		isFirstPage = false;
         	            	}else{
         	            		isFirstPage = true; 
         	            	}
         	            	
         	                
         	                //这里可以写渲染分页的数据的方法，默认加载当前页面
//         	            	Common.render({tmpl:$('#tiaojianId'),data:data.rewardData,context:'#rewardList',overwrite:1})

         	            }
         	        });
                	Common.render({tmpl:$('#tiaojianId'),data:data.rewardData,context:'#rewardList',overwrite:1});
                }
        	}); 
    	}
    	
    	
    	//绑定新增对话框确认保存按钮
    	$("#confirmAdd").click(function(){
    		var sids = ""; 
    		var checkedStudents = $("input[type=checkbox]:checked",$("#studentBox"));
    		for(var i=0;i<checkedStudents.length;i++){
    			sids = sids + checkedStudents[i].value;
    			if(i != checkedStudents.length - 1){
    				sids = sids + ',';
    			}
    		}
    		var rangeDepartments = "";
    		var checkedDepartments = $("input[type=checkbox]:checked",$("#departmentBox"));
    		for(var i=0;i<checkedDepartments.length;i++){
    			var depCheckDom = checkedDepartments[i];
    			if(depCheckDom.value.indexOf("ALL") == -1){
    				rangeDepartments = rangeDepartments + depCheckDom.value;
    				if(i != checkedDepartments.length - 1){
    					rangeDepartments = rangeDepartments + ",";
    				}
    			}
    		}
    		var rangeClasses = "";
    		var checkedClasses = $("input[type=checkbox]:checked",$("#classBox"));
    		for(var i=0;i<checkedClasses.length;i++){
    			var classCheckDom = checkedClasses[i];
    			if(classCheckDom.value.indexOf("ALL") == -1){
    				rangeClasses = rangeClasses + classCheckDom.value;
    				if(i != checkedClasses.length - 1){
    					rangeClasses = rangeClasses + ",";
    				}
    			}
    		}
    		var rewardObj = new Object();
    		rewardObj.rewardType = $("#addTypeSelect").val();
    		rewardObj.rewardGrade = $("#addLevelSelect").val();
    		rewardObj.rewardDate = $("#addDateInput").val();
    		rewardObj.rewardContent = $("#addRewardContent").val();
    		rewardObj.students = sids;
    		rewardObj.departments = rangeDepartments;
    		rewardObj.classes = rangeClasses;
    		
    		$.ajax({
        		type: "POST",
                url: "/reward/saveReward.do",
                data: rewardObj,
                dataType: "json",
                success: function(data){
                	queryRewards(null);
                }
        	}); 
    	});
    	
    	//绑定编辑对话框确认保存按钮
    	$("#confirmEdit").click(function(){
    		
    		var rangeDepartments = "";
    		var checkedDepartments = $("input[type=checkbox]:checked",$("#editDepartmentBox"));
    		for(var i=0;i<checkedDepartments.length;i++){
    			var depCheckDom = checkedDepartments[i];
    			if(depCheckDom.value.indexOf("ALL") == -1){
    				rangeDepartments = rangeDepartments + depCheckDom.value;
    				if(i != checkedDepartments.length - 1){
    					rangeDepartments = rangeDepartments + ",";
    				}
    			}
    		}
    		var rangeClasses = "";
    		var checkedClasses = $("input[type=checkbox]:checked",$("#editClassBox"));
    		for(var i=0;i<checkedClasses.length;i++){
    			var classCheckDom = checkedClasses[i];
    			if(classCheckDom.value.indexOf("ALL") == -1){
    				rangeClasses = rangeClasses + classCheckDom.value;
    				if(i != checkedClasses.length - 1){
    					rangeClasses = rangeClasses + ",";
    				}
    			}
    		}
    		
    		
    		var rewardObj = new Object();
    		rewardObj.rewardType = $("#editTypeSelect").val();
    		rewardObj.rewardGrade = $("#editLevelSelect").val();
    		rewardObj.rewardDate = $("#editDateInput").val();
    		rewardObj.rewardContent = $("#editRewardContent").val();
    		rewardObj.id = $("#confirmEdit").attr("editRewardId");
    		rewardObj.departments = rangeDepartments;
    		rewardObj.classes = rangeClasses;
    		
    		$.ajax({
        		type: "POST",
                url: "/reward/updateReward.do",
                data: rewardObj,
                dataType: "json",
                success: function(data){
                	queryRewards(null);
                }, 
                error:function(data){
                	alert(1)
                }
        	}); 
    	});
    	
    	
    	

    	//新增按钮方法（TODO：此处应初始化窗口内部数据）
        $('.btn-list .green-btn').click(function(){
        	//这里做了重新加载新增窗口
        	
        	//初始化年级下拉
            var gradesHtml = "<option value='ALL'>全部</option>";
        	for(var i=0;i<grades.length;i++){
        		gradesHtml = gradesHtml +"<option value='" + grades[i].id + "'>" + grades[i].name + "</option>";
        	}
        	document.getElementById("addGradeSelect").innerHTML = gradesHtml;
        	fillClassSelect("ALL","addClassSelect",false); 
        	//初始化部门勾选框
        	Common.render({tmpl:$('#addListId'),data:departments,context:'#departmentBox',overwrite:1})
        	//初始化班级勾选框
        	$("#addRange").find("input").prop("checked",false);
        	//===================================================
//        	var classesHtml = "<div class='all'><label><input type='checkbox' value='ALL'>所有班级</label></div>";
//        	for(var i=0;i<grades.length;i++){
//        		classesHtml = classesHtml + "<div class='single clearfix'>";
//        		classesHtml = classesHtml + "<label class='fclass'><input type='checkbox' id='ADD-" + grades[i].id + "' value='" + grades[i].id + "-ALL'/>" + grades[i].name + "</label>";
//        		classesHtml = classesHtml + "<div class='fclass-list'>";
//        		for(var j=0;j<classes.length;j++){
//        			if(classes[j].gradeId == grades[i].id){
//        				classesHtml = classesHtml + "<label><input type='checkbox' id='ADD-" + classes[j].id + "' value='" + classes[j].id +"'>" + classes[j].className + "</label>";
//        			}
//        		}
//        		classesHtml = classesHtml + "</div></div>";
//        		$("#classBox").html(classesHtml);
//        	}
        	//==================================================
        	//置空新增窗口日期控件
        	$("#addDateInput").val("");
        	//初始化新增窗口type和Level级联下拉
        	$("#addTypeSelect").val("REWARD");
        	fillLevelSelect("REWARD",'addLevelSelect',false);
        	//置空奖惩内容输入框
        	$("#addRewardContent").val("");
        	//全选学生勾选框
        	$("#addClassAllBox").prop("checked",false);
        	//初始化学生勾选控件
        	var gradeId = $("#addGradeSelect").val();
        	var classId = $("#addClassSelect").val();
        	getStudentList(gradeId,classId);
//            jiangCheng.dialog($('#editJiangCheng')); 
            jiangCheng.dialog($('#addJiangCheng'));
            return false;
        });

        
        //查看按钮方法
        $('body').on('click','.view',function(){
            var arr = {};
            for(var i=0;i<rewardListCatch.length;i++){
                if(rewardListCatch[i].id === $(this).attr('data-name')){
                    arr = rewardListCatch[i];
                    break;
                }
            }
            Common.render({tmpl:$('#viewListId'),data:arr,context:'#jiLuJiangCheng .pop-content',overwrite:1})
            jiangCheng.dialog($('#jiLuJiangCheng'));
            return false;
        });
        
        //点击编辑按钮事件方法
        $('body').on('click','.jc-edit',function(){
        	var editObj = {};
            for(var i=0;i<rewardListCatch.length;i++){
                if(rewardListCatch[i].id === $(this).attr('data-name')){
                	editObj = rewardListCatch[i];
                    break;
                }
            }
            $("#editName").html(editObj.name);
            $("#editGrade").html(editObj.nianji);
            $("#editClass").html(editObj.banji);
            $("#editDateInput").val(editObj.date);
            $("#editRewardContent").html(editObj.contents);
            var editType = editObj.type;
            var editLevel = editObj.dengji;
            for(var key in rewardType){
            	if(rewardType[key] == editType){
            		$("#editTypeSelect").val(key);
            		fillLevelSelect(key,'editLevelSelect',false)
            	}
            } 
            $("#editLevelSelect").val(editLevel);
            //此处清空之前的checkbox选中状态
            $("#editJiangCheng").find("input").prop("checked",false);
            
            var departmentArr =  editObj.departments.split(",");
            for(var i=0;i<departmentArr.length;i++){
            	$("#EDIT-" + departmentArr[i] ).prop("checked",true); 
            }
            var classArr = editObj.classes.split(",");
            for(var i=0;i<classArr.length;i++){
            	$("#EDIT-" + classArr[i] ).prop("checked",true); 
            }
            //给确认按钮绑定当前编辑的奖惩id
            $("#confirmEdit").attr("editRewardId",editObj.id);
            jiangCheng.dialog($('#editJiangCheng'));
            return false;
        });
        
        //删除按钮绑定方法
        $('body').on('click','.jc-del',function(){
            if(confirm('确定删除?')){
                var delId = $(this).attr('data-name');
                var delObj = new Object();
                delObj.id = delId;
                $.ajax({
            		type: "POST",
                    url: "/reward/deleteReward.do",
                    data: delObj,
                    dataType: "json",
                    success: function(data){
                    	queryRewards(null);
                    }, 
                    error:function(data){
                    	alert("删除出错，请联系管理员！");
                    }
            	}); 
            }
            return false;
        });
        //查询按钮绑定方法
        function queryBtnFunction(){
        	getParameterAndQueryReward(1);
        }
        $('.jiangcheng-list .green-btn').click(queryBtnFunction);
        
        


    };
 
    (function(){
        jiangCheng.init();
    })()



    module.exports = jiangCheng;
    
    
    //=====================================================================================
    
    //下拉菜单统一绑定事件
    //id="pageGradeSelect" onchange="fillClassSelect(this.value,'pageClassSelect')
    //
    //
    //
    $("#pageGradeSelect").change(function(){fillClassSelect(this.value,'pageClassSelect')});
    $("#pageType").change(function(){fillLevelSelect(this.value,'pageLevelSelect',true)});
    $("#addGradeSelect").change(function(){fillClassSelect(this.value,'addClassSelect',true)});
    $("#addClassSelect").change(function(){getStudentForClassSelected()});
    $("#addTypeSelect").change(function(){fillLevelSelect(this.value,'addLevelSelect',false)});
    $("#editTypeSelect").change(function(){fillLevelSelect(this.value,'editLevelSelect',false)});
     
    
    //全选反选都在这里
    	//新增框学生全选反选
    $("#addClassAllBox").click(function(){
    	$("#studentBox").find("input").prop("checked",$(this).is(':checked'));
    });
    	//新增学生框内若有反选则全校勾选框反选
    $("#studentBox").delegate("input","click",function(){
    	if(!($(this).is(':checked'))){
    		$("#addClassAllBox").prop("checked",false);
    	}
    })
    	//全校勾选框全选反选
    $("#addHoleSchoolCheckbox").click(function(){
    	$("#classBox").find("input").prop("checked",$(this).is(':checked'));
    	$("#addHoleDepartment").find("input").prop("checked",$(this).is(':checked'));
    });
    $("#editHoleSchoolCheckbox").click(function(){
    	$("#editClassBox").find("input").prop("checked",$(this).is(':checked'));
    	$("#editHoleDepartment").find("input").prop("checked",$(this).is(':checked'));
    });
    	//部门全选反选
    $("#editAllDepartment").click(function(){
    	$("#editDepartmentBox").find("input").prop("checked",$(this).is(':checked'));
    	if(!($(this).is(':checked'))){
    		$("#editHoleSchoolCheckbox").prop("checked",false); 
    	}
    });
    $("#addAllDepartment").click(function(){
    	$("#departmentBox").find("input").prop("checked",$(this).is(':checked'));
    	if(!($(this).is(':checked'))){
    		$("#addHoleSchoolCheckbox").prop("checked",false); 
    	}
    });
    $("#departmentBox").delegate("input","click",function(){
    	if(!($(this).is(':checked'))){
    		$("#addAllDepartment").prop("checked",false);
    		$("#addHoleSchoolCheckbox").prop("checked",false); 
    	}
    })
    $("#editDepartmentBox").delegate("input","click",function(){
    	if(!($(this).is(':checked'))){
    		$("#editAllDepartment").prop("checked",false);
    		$("#editHoleSchoolCheckbox").prop("checked",false); 
    	}
    })
    	//班级选全反选
    $("#addAllClasses").click(function(){
    	$("#classBox").find("input").prop("checked",$(this).is(':checked'));
    	if(!($(this).is(':checked'))){
    		$("#addHoleSchoolCheckbox").prop("checked",false); 
    	}
    });
    $("#editAllClasses").click(function(){
    	$("#editClassBox").find("input").prop("checked",$(this).is(':checked'));
    	if(!($(this).is(':checked'))){
    		$("#editHoleSchoolCheckbox").prop("checked",false);  
    	}
    });
    for(var i=0;i<grades.length;i++){
    	var gid = grades[i].id;
    	$("#ADD-" + gid).click(function(){
        	$(this).parent().next().find("input").prop("checked",$(this).is(':checked'));
        	if(!($(this).is(':checked'))){
        		$("#addAllClasses").prop("checked",false);
        		$("#addHoleSchoolCheckbox").prop("checked",false); 
        	}
        });
    	$("#EDIT-" + gid).click(function(){
        	$(this).parent().next().find("input").prop("checked",$(this).is(':checked'));
        	if(!($(this).is(':checked'))){
        		$("#editAllClasses").prop("checked",false);
        		$("#editHoleSchoolCheckbox").prop("checked",false); 
        	}
        });
    } 
    $("#classBox").delegate("input","click",function(){
    	if(!($(this).is(':checked'))){
    		$($(this).parent().parent().parent().find("input").get(0)).prop("checked",false); 
    		$("#addAllClasses").prop("checked",false);
    		$("#addHoleSchoolCheckbox").prop("checked",false); 
    	}
    })
    $("#editClassBox").delegate("input","click",function(){
    	if(!($(this).is(':checked'))){
    		$($(this).parent().parent().parent().find("input").get(0)).prop("checked",false); 
    		$("#editAllClasses").prop("checked",false);
    		$("#editHoleSchoolCheckbox").prop("checked",false); 
    	}
    })
    
    		
    //异步查询学生名单
    function getStudentList(gradeId,classId){
    	$.ajax({
    		type: "GET",
            url: "/reward/queryStudents.do",
            data: {gradeId:gradeId, classId:classId},
            dataType: "json",
            success: function(data){
            	Common.render({tmpl:$('#addListId'),data:data,context:'.student-box',overwrite:1})
            }
    	});
        
    }
    
  //新增中class下拉事件
    function getStudentForClassSelected(){
    	var gradeId = $("#addGradeSelect").val();
     	var classId = $("#addClassSelect").val();
     	getStudentList(gradeId,classId);
    }

    //级联Class下拉选单(grade下拉的selected事件)
    function fillClassSelect(value,targetId,isAdd){ 
    	var isAll = false;
    	if(value == "ALL"){
    		isAll = true;
    	}
    	var classesDom = document.getElementById(targetId);
    	var gradeId = value;
    	$("#" + targetId).html("<option value='ALL'>全部</option>");
    	for(var i=0;i<classes.length;i++){
    		if(gradeId == classes[i].gradeId || isAll){
    			$("#" + targetId).append("<option value='" + classes[i].id + "'>" + classes[i].className + "</option>");
    		}
    	}
    	 if(isAdd){
    		var gradeId = $("#addGradeSelect").val();
         	var classId = $("#addClassSelect").val();
         	getStudentList(gradeId,classId);
    	 }
    }
    //级联Level下拉选单
    function fillLevelSelect(value,targetId,isPage){
    	var levelHtmlStr = "";
    	if(isPage){
    		levelHtmlStr = "<option value='ALL'>全部</option>";
    	}
    	$("#" + targetId).html(levelHtmlStr);
    	if(value != "PUNISHMENT"){
    		for(var i=0;i<rewardLevel.REWARD.length;i++){
    			$("#" + targetId).append("<option value='" + rewardLevel.REWARD[i] + "'>" + rewardLevel.REWARD[i] + "</option>");
    		}
    	}
    	if(value != "REWARD"){
    		for(var i=0;i<rewardLevel.PUNISHMENT.length;i++){
    			$("#" + targetId).append("<option value='" + rewardLevel.PUNISHMENT[i] + "'>" + rewardLevel.PUNISHMENT[i] + "</option>");
    		}
    	}
    }
    
})





