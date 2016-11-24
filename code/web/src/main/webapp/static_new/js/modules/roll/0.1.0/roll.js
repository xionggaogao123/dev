/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  学籍管理操作
 * @description
 * 学籍管理操作
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var roll = {},
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

    roll.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
//        obj.find('.pop-btn span').click(function(){
//            $(this).closest('.pop-wrap').hide();
//            $('.bg-dialog').hide();
//        });
        $('body').scrollTop(0);
    };
    
    function closeDialog(targetDialogObj){
    	$(targetDialogObj).closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    }
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * roll.init()
     */
    roll.init = function(){
		if (GetQueryString("a")!="10000") {


		}

        //基础信息弹窗
        $('.gray-table').on('click','.edit-roll',function(){
        	$.ajax({
         		type: "POST",
                 url: "/registration/queryBaseInfo.do",
                 data: {userId : $(this).parent().parent().attr("flag")},
                 dataType: "json",
                 success: function(data){
                	 //放置ID在隐藏域
                	 $("#baseInfoIdInput").val(data.id);
                	 //初始化相关信息
                	 $("#baseUserName").val(data.userName);
                	 $("#baseStudentNumber").val(data.studentNumber);
                	 $("#baseGrade").html($("#gradeSelection").html());
                	 $("#baseGrade").val($("#gradeSelection").val());
                	 $("#baseClass").html($("#classSelection").html());
                	 $("#baseClass").val($("#classSelection").val());
                	 $("#baseSex").val(data.sex);
                	 $("#baseBirthday").val(data.birthday);
                	 $("#baseRace").val(data.race);
                	 $("#baseHealth").val(data.health);
                	 $("#baseAddressNow").val(data.addressNow);
                	 $("#baseAddressR").val(data.addressR);
                	 $("#basePhone").val(data.phone);
                	 $("#baseEmail").val(data.email); 
                	 
                	 roll.dialog($('#editSchoolRoll'));
                 }, 
                 error:function(data){
                 	alert("查询出错，请联系管理员！");
                 }
         	}); 
            return false;
        });
        //绑定基本信息关闭按钮
        $("#baseCloseBtn").click(function(){
        	closeDialog(this);
        });
        //基本信息更新
        $('body').on('click','#baseConfirmBtn',function(){
        	if($.trim($("#baseUserName").val()) == ""){
        		alert("姓名不能为空，请填写完整再确认！");
        		return;
        	}
        	var thisDialogObj = this;
        	var updateObj = {};
        	updateObj.id = $("#baseInfoIdInput").val();
        	updateObj.userName =  $("#baseUserName").val();;
        	updateObj.studentNumber = $("#baseStudentNumber").val();
        	updateObj.classId = $("#baseClass").val();
        	updateObj.sex = $("#baseSex").val();;
        	updateObj.birthday = $("#baseBirthday").val();
        	updateObj.race = $("#baseRace").val();
        	updateObj.health = $("#baseHealth").val();
        	updateObj.addressNow = $("#baseAddressNow").val();
        	updateObj.addressR = $("#baseAddressR").val();
        	updateObj.phone = $("#basePhone").val();
        	updateObj.email = $("#baseEmail").val();
        	updateObj.preClassId = classSelection.val();
        	if(!confirm("确认修改？")){
        		return;
        	}
        	$.ajax({
         		type: "POST",
                 url: "/registration/updateBaseInfo.do",
                 data: updateObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog(thisDialogObj);
                	if(keywordCatch == null){
	            			queryList();
	            	}else{
	            			queryList(keywordCatch);
	            	}
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        
        $("#baseGrade").change(function(){
        	$("#baseClass").html("");
        	for(var i=0;i<schools.length;i++){ 
        		var school = schools[i];
        		if(schoolSelection.val() ==  school.id){ 
        			var gradeArr = school.grades;
        			for(var j=0;j<gradeArr.length;j++){
        				var grade = gradeArr[j];
        				if($(this).val() == grade.id){
        					var classArr = grade.classes;
        					for(var k=0;k<classArr.length;k++){
        						var classObj = classArr[k];
        						addOption($("#baseClass"),classObj);
        					}
        				}
        			}
        		}
        	}
        });

        //学籍变更弹窗
        $('.gray-table').on('click','.change-roll',function(){
        	$.ajax({
         		type: "POST",
                 url: "/registration/queryRegistration.do",
                 data: {userId : $(this).parent().parent().attr("flag")},
                 dataType: "json",
                 success: function(data){
                	 //放置ID在隐藏域
                	 $("#RInfoIdInput").val(data.id);
                	 //填充
                	 $("#preRName").val(data.userName);
                	 $("#preRStudentNumber").val(data.studentNumber);
                	 $("#preRGrade").html($("#gradeSelection").html());
                	 $("#preRGrade").val($("#gradeSelection").val());
                	 $("#preRClass").html($("#classSelection").html());
                	 $("#preRClass").val($("#classSelection").val());
                	 $("#preRSchool").val(data.newSchool);
                	 $("#preRDate").val(data.changeDate);
                	 $("#preRContent").val(data.changePostscript);
                	 //初始化编辑内容
                	 if(rCanEdit == "true"){
                		 $("#newRGrade").html($("#gradeSelection").html());
                    	 $("#newRGrade").val($("#gradeSelection").val());
                    	 $("#newRClass").html($("#classSelection").html());
                    	 $("#newRClass").val($("#classSelection").val());
                    	 
                    	 $("#newRDate").val("");
                    	 $("#newRStudentNumber").val("");
                    	 $("#newRSchool").val("");
                    	 $("#newRContent").val("");
                	 }
                	 roll.dialog($('#changeSchoolRoll'));  
                 }, 
                 error:function(data){
                 	alert("查询出错，请联系管理员！");
                 }
         	});
            return false;
        });
        
        $("#newRGrade").change(function(){
        	$("#newRClass").html("");
        	for(var i=0;i<schools.length;i++){ 
        		var school = schools[i];
        		if(schoolSelection.val() ==  school.id){ 
        			var gradeArr = school.grades;
        			for(var j=0;j<gradeArr.length;j++){
        				var grade = gradeArr[j];
        				if($(this).val() == grade.id){
        					var classArr = grade.classes;
        					for(var k=0;k<classArr.length;k++){
        						var classObj = classArr[k];
        						addOption($("#newRClass"),classObj);
        					}
        				}
        			}
        		}
        	}
        });
        //学籍变动窗口绑定关闭
        $("#RCloseDialogBtn").click(function(){
        	 closeDialog(this);
        });
      //学籍信息更新
        $('body').on('click','#RCommitBtn',function(){
        	if($.trim($("#newRDate").val()) == ""){
        		alert("变更日期不能为空，请填写完整再确认！");
        		return;
        	}
        	var thisDialogObj = this;
        	var updateObj = {};
        	updateObj.id = $("#RInfoIdInput").val();
        	updateObj.newRDate =  $("#newRDate").val();;
        	updateObj.newRStudentNumber = $("#newRStudentNumber").val();
        	updateObj.newRClass = $("#newRClass").val();
        	updateObj.newRSchool = $("#newRSchool").val();;
        	updateObj.newRContent = $("#newRContent").val();
        	updateObj.preClassId = classSelection.val();
        	if(!confirm("确认修改？")){ 
        		return;
        	}
        	$.ajax({
         		type: "POST",
                 url: "/registration/updateRegistration.do",
                 data: updateObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog(thisDialogObj);
                	if(keywordCatch == null){
	            			queryList();
	            	}else{
	            			queryList(keywordCatch);
	            	}
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        
        
        
        /**
         * 下拉菜单相关
         */
        var schoolSelection = $("#schoolSelection");
        var gradeSelection = $("#gradeSelection");
        var classSelection = $("#classSelection");
        //页面初始化下拉菜单
        function addOption(selDom,dataObj){
        	selDom.append('<option value="' + dataObj.id + '">' + dataObj.name + '</option>');
        }
        
        function fillAllSelection(schoolId,gradeId){
        	if(gradeId == "0"){
        		gradeSelection.html("");
        	}
        	classSelection.html("");
        	for(var i=0;i<schools.length;i++){ 
        		var school = schools[i];
        		if(schoolId == "0"){
        			addOption(schoolSelection,school);
        		}
        		if(schoolId ==  school.id || (schoolId=="0"&&i==0)){ 
        			var gradeArr = school.grades;
        			for(var j=0;j<gradeArr.length;j++){
        				var grade = gradeArr[j];
        				if(gradeId == "0"){
        					addOption(gradeSelection,grade);
        				}
        				if(gradeId == grade.id || (gradeId == "0" && j == 0)){
        					var classArr = grade.classes;
        					for(var k=0;k<classArr.length;k++){
        						var classObj = classArr[k];
        						addOption(classSelection,classObj);
        					}
        				}
        			}
        			
        			
        		}
        	}
        }
        
        //绑定下拉菜单事件
        schoolSelection.change(function(){
        	fillAllSelection(schoolSelection.val(),"0");
        	pageNo = 1;
        	$("#keywordInput").val("");
        	queryList();
        });
        gradeSelection.change(function(){
        	fillAllSelection(schoolSelection.val(),gradeSelection.val());
        	pageNo = 1;
        	$("#keywordInput").val("");
        	queryList();
        });
        classSelection.change(function(){
        	pageNo = 1;
        	$("#keywordInput").val("");
        	queryList();
        });
        /**
         * 查询相关
         */
        var listArea = $("#listArea");
        var pageNo = 1;
        var pageSize = 30;
        var isFirstPage = false; 
        var keywordCatch = "";
        
        function queryList(keyword){
        	if(classSelection.html() == ""){
        		return;
        	}
        	var queryObj = {};
        	queryObj.classId = classSelection.val();
        	keywordCatch = keyword;
        	queryObj.keyword = keywordCatch;
        	queryObj.pageSize = isStudent?10000:pageSize;
        	queryObj.pageNo = pageNo;
        	 $.ajax({
         		type: "POST",
                 url: "/registration/queryList.do",
                 data: queryObj,
                 dataType: "json",
                 success: function(data){
                	 if(isStudent){
                		 uploadAndFillListArea(data.datas);
                		 return;
                	 }
                	 $('.new-page-links').jqPaginator({
          	            totalPages: Math.ceil(data.pagejson.total/pageSize) == 0?1:Math.ceil(data.pagejson.total/pageSize),//总页数
          	            visiblePages: 10,//分多少页
          	            currentPage: queryObj.pageNo,//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	if(isFirstPage){
          	            		pageNo = n;
          	            		if(keywordCatch == null){
          	            			queryList();
          	            		}else{
          	            			queryList(keywordCatch);
          	            		}
          	            		isFirstPage = false;
          	            	}else{
          	            		isFirstPage = true; 
          	            	}
          	            }
          	        });
                 	uploadAndFillListArea(data.datas);
                 }, 
                 error:function(data){
                 	alert("查询出错，请联系管理员！");
                 }
         	}); 
        	
        }
        
        function uploadAndFillListArea(dataSrc){
        	listArea.empty();
        	//如果是学生，则过滤
        	if(isStudent){
        		dataSrc = filterMyself(dataSrc);
        	}
        	var gradeAndClass = gradeSelection.find("option:selected").text() + " / " + classSelection.find("option:selected").text();
        	for(var i=0;i<dataSrc.length;i++){
				var url;
        		var dataObj = dataSrc[i];
				if (GetQueryString("a")!="10000") {
					url = '<a href="/registration/rewardHistory.do?userId=' + dataObj.id + '" class="orange-btn-new">学籍信息</a>';
				} else {
					url = '<a href="/registration/rewardHistory.do?a=10000&userId=' + dataObj.id + '&a=10000" class="blue-btn orange-btn-new">学籍信息</a>';
				}
        		var htmlStr = '		<tr flag="' + dataObj.id + '">'+
        		'                        <td>' + dataObj.userName + '</td>'+ 
        		'                        <td>' + ((dataObj.registerNum == null || dataObj.registerNum == "")?"--":dataObj.registerNum) + '</td>'+
        		'                        <td>' + ((dataObj.studentNum == null || dataObj.studentNum == "")?"--":dataObj.studentNum) + '</td>'+
        		'                        <td>' + gradeAndClass + '</td>'+ 
        		'                        <td>'+
        		'                            <a href="#" class="orange-btn-new blue-btn change-roll">学籍变动</a>'+
        		'                            <a href="#" class="orange-btn-new blue-btn edit-roll">基本信息</a>'+
        		url+
        		'                        </td>'+ 
        		'                    </tr>';
        		
        		listArea.append(htmlStr);
        	}
        	
        }
        
        function filterMyself(dataSrc){
        	var result = new Array();
        	for(var i=0;i<dataSrc.length;i++){
        		var dataObj = dataSrc[i];
        		if(dataObj.id == PUBLIC_USER_ID){
        			result.push(dataObj);
        			break;
        		}
        	}
        	return result;
        }
        
        //模糊查询按钮绑定
        $("#searchBtn").click(function(){
        	var text = $("#keywordInput").val();
        	pageNo = 1;
        	queryList(text);
        });
        
        
        
        //初始化下拉和查询
        if(schools.length > 0){
        	if(!isStudent){
        		fillAllSelection("0","0");
        	}
        	queryList();
        }else{
        	alert("未查询到学校数据！请与管理员联系！");
        }
        
        
        
        /*$('.kaifang-set').on('click',function(){
            Common.dialog($('#lurusetId'),function(obj){
                $(obj).find('.pop-btn span.active').on('click',function(){
                    alert('关闭了。。。');
                    Common.dialog.close($(obj));
                })
            });
        });*/
        





    };
	function GetQueryString(name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null)return  unescape(r[2]); return null;
	}
    (function(){
        roll.init();
    })();

    module.exports = roll;
})