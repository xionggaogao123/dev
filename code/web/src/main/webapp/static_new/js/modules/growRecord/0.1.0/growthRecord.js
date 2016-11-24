/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  学籍管理操作
 * @description
 * 成长档案
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var growthRecord = {},
        Common = require('common');
    			 require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * rewardHistory.init()
     */
    growthRecord.init = function(){

		if (GetQueryString("a")!="10000") {

		}
         Common.tab('click',$('.grow-tab-head'))
        
       
    	
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
        		if(schoolId ==  school.id || (schoolId=="0" && i==0)){ 
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
        	fillExamSelection(schoolSelection.val(), classSelection.val(), termSelection.val());
        });
        gradeSelection.change(function(){
        	fillAllSelection(schoolSelection.val(),gradeSelection.val());
        	fillExamSelection(schoolSelection.val(), classSelection.val(), termSelection.val());
//        	queryList();
        });
        classSelection.change(function(){
			fillExamSelection(schoolSelection.val(), classSelection.val(), termSelection.val());
        	pageNo = 1;
        	if(canQuery){
        		queryList();
        	}
        });
        
        //=============================================以上是学校-年级-班级下拉=======================================
        //=============================================以 下 是 学 期   - 考 试 下 拉=======================================
        var termSelection = $("#termSelection");
        var examSelection = $("#examSelection");
        //绑定下拉菜单事件
        termSelection.change(function(){
        	fillExamSelection(schoolSelection.val(), classSelection.val(), termSelection.val());
        });
        examSelection.change(function(){
        	pageNo = 1;
        	queryList();
        });
        //填充学期下拉
        function fillTermSelection(){
        	termSelection.html("");
        	for(var i=terms.length-1;i>=0;i--){
        		var term = terms[i];
        		termSelection.append("<option value='" + term + "'>" + term + "</option>");
        	}
        	firstTerm = terms[terms.length-1];
        	fillExamSelection(schoolSelection.val(), classSelection.val(), firstTerm);
        	
        }
        //填充考试下拉
        function fillExamSelection(schoolId, gradeId, term){
        	$.ajax({
         		type: "POST",
                 url: "/growth/getExamList.do",
                 data: {schoolId : schoolId ,gradeId : gradeId, term : term},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == 200){
                 		var datas = data.message;
                 		examSelection.html("");
                 		if(datas.length == 0){
                 			alert("未查询到考试信息，请重新选择！");
                 			canQuery = false;
                 			return;
                 		}else{
                 			canQuery = true;
                 		}
                 		for(var i=0;i<datas.length;i++){
                    		var examItem = datas[i];
                    		examSelection.append("<option value='" + examItem.id + "'>" + examItem.name + "</option>");
                    	}
                 		pageNo = 1;
                 		queryList();
                 	}else{
                 		alert("查询考试下拉列表失败 ！请联系管理员！");
                 	}
                 }, 
                 error:function(data){
                 	alert("查询考试下拉列表失败，请联系管理员！");
                 }
         	});
        }
        
        
        /**
         * 查询方法
         */
        var pageNo = 1;
        var pageSize = 15;
        var isFirstPage = false; 
        var canQuery = true;
        
        
        var tableList = $("#tableList");
        var tableHead = $("#tableHead");
        function queryList(){
        	tableList.empty();
        	tableHead.html('');
        	if(classSelection.val() == "" || examSelection.val() == ""){
        		return;
        	}
        	$.ajax({
         		type: "POST",
                 url: "/growth/queryScoreList.do",
                 data: {classId : classSelection.val() ,examId : examSelection.val(), pageNo : pageNo, pageSize : pageSize},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == 200){
                 		var listData = data.message.list;
                 		var headData = data.message.head;
                 		var pageData = data.message.pagejson;
                 		$('#page_score').jqPaginator({
              	            totalPages: Math.ceil(pageData.total/pageSize) == 0?1:Math.ceil(pageData.total/pageSize),//总页数
              	            visiblePages: 10,//分多少页
              	            currentPage: pageNo,//当前页数
              	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
              	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
              	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
              	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
              	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
              	            onPageChange: function (n) { //回调函数
              	            	if(isFirstPage){
              	            		pageNo = n;
              	            		queryList();
              	            		isFirstPage = false;
              	            	}else{
              	            		isFirstPage = true; 
              	            	}
              	            }
              	        });
                 		//表头
                 		var tableWidth = 250;
                 		var headHtml = '<th width="120">学生姓名</th>';
                 		for(var i=0;i<headData.length;i++){
                 			headHtml = headHtml + '<th width="75">' + headData[i].name + '</th>';
                 			tableWidth = tableWidth + 75;
                 		}
                 		headHtml = headHtml + '<th width="130">操作</th>';
                 		tableHead.html(headHtml);
                 		$('.gray-table').css("width",tableWidth);
                 		//列表内容
                 		tableList.empty();
                 		for(var i=0;i<listData.length;i++){
                 			var subData = listData[i];
                 			var listHtml = '<tr><td>' + subData.studentName + '</td>';
                 			for(var j=0;j<headData.length;j++){
                 				listHtml = listHtml + '<td>' + subData.examScore[headData[j].id].score + '</td>';
                 			}
                 			listHtml = listHtml + '<td><a href="javascript:void(0)" flag="' + subData.studentId + '"  class="orange-btn-new">成绩单</a></td></tr>';
                 			tableList.append(listHtml);
                 		}
                 		
                 		
                 	}else{
                 		alert("查询考试下拉列表失败 ！请联系管理员！");
                 	}
                 }, 
                 error:function(data){
                 	alert("查询考试下拉列表失败，请联系管理员！");
                 }
         	});
        }
        
        
        //跳转前验证
        $('body').on('click','#tableList a',function(){
        	var stuId = $(this).attr("flag");
        	$.ajax({
         		type: "POST",
                 url: "/growth/checkStudent.do",
                 data: {id : stuId},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == 200){
                		 location.href = "/growth/scoreDetail.do?id=" + stuId;
                 	 }else{
                 		alert("该学生不在记录中或无归属班级，无法查询！");
                 	 }
                 }, 
                 error:function(data){
                 	alert("查询失败，请联系管理员！");
                 }
         	});
        });
        
        //初始化下拉和查询
        if(schools.length > 0){
        	fillAllSelection("0","0");
        	fillTermSelection();
//        	queryList();
        }else{
        	alert("未查询到学校数据！请与管理员联系！");
        }
        
        /**
         * 以上是成绩表，以下是素质表
         */
        /**
         * 下拉菜单相关
         */
        var schoolSelection_quality = $("#schoolSelection_quality");
        var gradeSelection_quality = $("#gradeSelection_quality");
        var classSelection_quality = $("#classSelection_quality");
        
        function fillAllSelection_quality(schoolId,gradeId){
        	if(gradeId == "0"){
        		gradeSelection_quality.html("");
        	}
        	classSelection_quality.html("");
        	for(var i=0;i<schools.length;i++){ 
        		var school = schools[i];
        		if(schoolId == "0"){
        			addOption(schoolSelection_quality,school);
        		}
        		if(schoolId ==  school.id || (schoolId=="0" && i==0)){ 
        			var gradeArr = school.grades;
        			for(var j=0;j<gradeArr.length;j++){
        				var grade = gradeArr[j];
        				if(gradeId == "0"){
        					addOption(gradeSelection_quality,grade);
        				}
        				if(gradeId == grade.id || (gradeId == "0" && j == 0)){
        					var classArr = grade.classes;
        					for(var k=0;k<classArr.length;k++){
        						var classObj = classArr[k];
        						addOption(classSelection_quality,classObj);
        					}
        				}
        			}
        		}
        	}
        	pageNo = 1;
        	queryList_quality();
        }
        
        //绑定下拉菜单事件
        schoolSelection_quality.change(function(){
        	fillAllSelection_quality(schoolSelection_quality.val(),"0");
        });
        gradeSelection_quality.change(function(){
        	fillAllSelection_quality(schoolSelection_quality.val(),gradeSelection_quality.val());
        });
        classSelection_quality.change(function(){
        	pageNo = 1;
        	queryList_quality();
        });
        
        /**
         * 查询方法
         */
        var pageNo_quality = 1;
        var isFirstPage_quality = false; 
        
        
        var tableList_quality = $("#tableList_quality");
        function queryList_quality(){
        	tableList_quality.empty();
        	if(classSelection_quality.val() == ""){
        		return;
        	}
        	$.ajax({
         		type: "POST",
                 url: "/growth/queryStudentList.do",
                 data: {classId : classSelection_quality.val(), pageNo : pageNo_quality, pageSize : pageSize},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == 200){
                 		var listData = data.message.list;
                 		var pageData = data.message.pagejson;
                 		$('#page_quality').jqPaginator({
              	            totalPages: Math.ceil(pageData.total/pageSize) == 0?1:Math.ceil(pageData.total/pageSize),//总页数
              	            visiblePages: 10,//分多少页
              	            currentPage: pageNo_quality,//当前页数
              	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
              	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
              	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
              	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
              	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
              	            onPageChange: function (n) { //回调函数
              	            	if(isFirstPage_quality){
              	            		pageNo_quality = n;
              	            		queryList_quality();
              	            		isFirstPage_quality = false;
              	            	}else{
              	            		isFirstPage_quality = true; 
              	            	}
              	            }
              	        });
                 		//列表内容
                 		tableList_quality.empty();
                 		for(var i=0;i<listData.length;i++){
                 			var subData = listData[i];
                 			var listHtml = '<tr><td width="120">' + subData.studentName + '</td>';
                 			listHtml = listHtml + '<td width="130"><a href="javascript:void(0)" flag="' + subData.studentId + '"  class="orange-btn-new">个人详情</a></td></tr>';
                 			tableList_quality.append(listHtml);
                 		}
                 		
                 		
                 	}else{
                 		alert("查询考试下拉列表失败 ！请联系管理员！");
                 	}
                 }, 
                 error:function(data){
                 	alert("查询考试下拉列表失败，请联系管理员！");
                 }
         	});
        }
        
        //跳转前验证
        $('body').on('click','#tableList_quality a',function(){
        	var stuId = $(this).attr("flag");
        	$.ajax({
         		type: "POST",
                 url: "/growth/checkStudent.do",
                 data: {id : stuId},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == 200){
                		 location.href = "/growth/qualityDetail.do?id=" + stuId;
                 	 }else{
                 		alert("该学生不在记录中或无归属班级，无法查询！");
                 	 }
                 }, 
                 error:function(data){
                 	alert("查询失败，请联系管理员！");
                 }
         	});
        });
        
      //初始化下拉和查询
        if(schools.length > 0){
        	fillAllSelection_quality("0","0");
        }else{
        	alert("未查询到学校数据！请与管理员联系！");
        }
        
    };

	function GetQueryString(name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null)return  unescape(r[2]); return null;
	}

    (function(){
    	growthRecord.init();
    })();

    module.exports = growthRecord;
})