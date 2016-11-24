/**
 * Created by lujiang on 15/11/02.
 */
/**
 * @author 陆江
 * @module  区域考试
 * @description
 * 区域考试操作
 */
/* global Config */
define(['common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var newRegional = {},
        Common = require('common');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    newRegional.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        
        $(document).scrollTop(0);
    };
    
    function closeDialog(obj){
        obj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    }
    

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * roll.init()
     */
    newRegional.init = function(){


        Common.tab('click',$('.grow-tab-head'))

        $('.gray-table').on('click','.edit-roll',function(){
        	newRegional.dialog($('#editSchoolRoll'));
            return false;
        });

        $('.gray-table').on('click','.change-roll',function(){
        	newRegional.dialog($('#changeSchoolRoll'));
            return false;
        });
        $('.gray-table').on('click','.look',function(){
        	newRegional.dialog($('#status'));
            return false;
        });
        $('.toEduDepart').on('click','.subTo',function(){
        	newRegional.dialog($('#subStatus'));
            return false;
        });
        $('.gray-table').on('click','.deleteCon',function(){
        	newRegional.dialog($('#delConExam'));
            return false;
        });
        
        //加载初始化年级列表
        getGradeList();
        //年级下拉菜单change事件
        $("#gradeName").on("change",function(){
        	var select_content = $(this).val();
        	
        	$("#examSub").html('');
        	$("#examSub").prev().html('');
        	$("#examSchool").html('');
        	$("#examSchool").prev().html('');
        	loadSubList(select_content);
        	loadSchList(select_content);
        });
     
        
 //年级下拉菜单      
function getGradeList(){
	$.ajax({
		 url: '/regional/loadGradeList.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
        	var datas = data.message;
        	var target = $('#gradeName');
        	var content = '<option value="请选择">' +'---请选择---' + '</option>';
        	target.append(content);
       	 for(var i=0;i<datas.length;i++){
       		var content = '';
            content += '<option value=' + datas[i].type + ' flag="' + datas[i].id + '">' + datas[i].gradeName + '</option>';
            target.append(content);
       	 }
        }
	});
}
//科目列表(参数年级类型)
function loadSubList(gradeType){
	$.ajax({
		url:'/regional/loadSubList.do',
		type:'post',
		dataType:'json',
		data:{'gradeType':gradeType},
		success:function(data){
			var datas = data.message;	
			var content = '<label><input type="checkbox" class="check"  id="subjectAllCheck" checked="true" checked="true" />全选</label>';
			var target = $("#examSub");
			var subjectTable = $("#subjectTable");
			subjectTable.empty();
			$(content).insertBefore('#examSub');
			for(var i=0;i<datas.length;i++){
	       		content = '<label><input type="checkbox" class="check"  value='+ datas[i].subId +' checked="true" />'+ datas[i].subName+'</label>';
	            if(i>0 && i%6==0){
	            	content ='<br><label><input type="checkbox" class="check"  value='+datas[i].subId+'  checked="true" />'+ datas[i].subName+'</label>';
	            }
	            target.append(content);
	            
	            var subjectStr = ' <tr id="subject_' + datas[i].subId + '">'+
	            '          <td class="ddate">' + datas[i].subName + '</td>'+
	            '          <td class="ddate"><input type="text" value="100"></td>'+
	            '          <td class="ddate"><input type="text" value="85"></td>'+
	            '          <td class="ddate"><input type="text" value="60"></td>'+
	            '          <td class="ddate"><input type="text" style="cursor:pointer" onfocus="WdatePicker({minDate:\'%y-%M-%d\',maxDate:\'{%y+100}-%M-%d\'})" class="Wdate dateWidth" /></td>'+
	            '          <td class="ddate"><input type="text" class="timeWidth" value=""></td>'+
	            ' </tr>';
	            subjectTable.append(subjectStr);
	            
	       	 }
		}
	});
}
//学校列表(参数年级类型)
function loadSchList(gradeType){
	$.ajax({
		url:'/regional/loadSchList.do',
		type:'post',
		dataType:'json',
		data:{'gradeType':gradeType},
		success:function(data){
			var datas = data.message;	
			var content = '<label><input type="checkbox" class="check" id="shcoolAllCheck" checked="true" />全选</label>';
			var target = $("#examSchool");
			$(content).insertBefore('#examSchool');
			if(datas.length==0){
				alert('没有与所选年级相匹配的学校');
			}
			for(var i=0;i<datas.length;i++){
				content = '';
	       		content +=' <label><input type="checkbox" class="check"  value="'+ datas[i].schId +'" checked="true" flag="' + datas[i].schName + '"/>'+datas[i].schName+'</label>'; 
	            target.append(content);
	            if(i>0 && i%6==0){
	            	content = '';
	            	content +='<br>'+
	            			  '<label><input type="checkbox" class="check"  value="'+datas[i].schId+'" checked="true" flag="' + datas[i].schName + '"/>'+ datas[i].schName+'</label>';
	            	target.append(content);
	            }
			}
		}
		
	});
}
		//给学校和科目check增加全选反选事件
	// 科目checkbox全选/反选
	$('body').on('click', '#subjectAllCheck', function() {
		$("#examSub").find("input").prop("checked",$(this).is(':checked'));
		if($(this).is(':checked')){
			$('#subjectTable').find('tr').show();
		}else{
			$('#subjectTable').find('tr').hide();
		}
	});
	$('body').on('click', '#examSub input:checkbox', function() {
	  if (!$(this).is(':checked')) {
	      $('#subjectAllCheck').prop('checked', false);
	      $('#subject_' + $(this).val()).hide();
	  }else{
		  $('#subject_' + $(this).val()).show();
	  }
	  
	});
	//学校全反选
	$('body').on('click', '#shcoolAllCheck', function() {
		$("#examSchool").find("input").prop("checked",$(this).is(':checked'));
	});
	$('body').on('click', '#examSchool input:checkbox', function() {
		if (!$(this).is(':checked')) {
		    $('#shcoolAllCheck').prop('checked', false);
		}
	});
	
	//提交新增
	$('#confirmBtn').on('click',function(){
		var dataObj = {};
		dataObj.term = $('.xuenian').html();
		dataObj.examName = $.trim($('#examNmeInput').val());
		dataObj.gradeType = $('#gradeName').val();
		dataObj.gradeName = $('#gradeName option:selected').html();
		dataObj.gradeId = $('#gradeName option:selected').attr("flag");
		dataObj.startTime = $('#startDateInput').val();
		
		// 判断控制
		if("" == dataObj.examName || dataObj.examName == null){
			alert("请填写考试名称");
			return;
		}
		if(dataObj.examName.length > 15){
			alert("考试名称过长！最长15个字！");
			return;
		}
		if(dataObj.gradeType == "请选择" || dataObj.gradeName == null || dataObj.gradeName == "" || dataObj.gradeId == null || dataObj.gradeId == ""){
			alert("请选择考试年级");
			return;
		}
		if(dataObj.startTime == "" || dataObj.startTime == null){
			alert("请填写考试开始日期");
			return ;
		}
		
		var subjectList = $('#examSub input:checked');
		if(subjectList.length==0){
			alert("请选择科目 ");
			return;
		}
		
		var subjectStr = "";
		for(var i=0;i<subjectList.length;i++){
			var subjectCheck = $(subjectList.get(i));
			var subjectId = subjectCheck.val();
			var subjectTableTdList =  $('#subject_' + subjectId).find("td");
			subjectStr = subjectStr + $(subjectTableTdList.get(0)).html();
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + subjectId;
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + $(subjectTableTdList.get(1)).find("input").val();
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + $(subjectTableTdList.get(2)).find("input").val();
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + $(subjectTableTdList.get(3)).find("input").val();
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + $(subjectTableTdList.get(4)).find("input").val();
			subjectStr = subjectStr + "#";
			subjectStr = subjectStr + $(subjectTableTdList.get(5)).find("input").val();
			if(i != subjectList.length - 1){
				subjectStr = subjectStr + "%";
			}
			
			
			//判断分数表格中空值
			var subName = $(subjectTableTdList.get(0)).html();
			var full = parseInt($(subjectTableTdList.get(1)).find("input").val());
			var excellent = parseInt($(subjectTableTdList.get(2)).find("input").val());
			var pass = parseInt($(subjectTableTdList.get(3)).find("input").val());
			var dateStr = $(subjectTableTdList.get(4)).find("input").val();
			var time = $(subjectTableTdList.get(5)).find("input").val();
			
			if(isNaN(full) || full <= 0 || full == null || full == "" ){
				alert("请输入正确的"+subName+"满分数字信息");
				return;
			}
			if(isNaN(excellent) || excellent <= 0 || excellent == null || excellent == "" || excellent >= full){
				alert("请输入正确的"+subName+"优秀分数字信息");
				return;
			}
			if(isNaN(pass) || pass <= 0 || pass == null || pass == "" || pass >= excellent){
				alert("请输入正确的"+subName+"及格分数字信息");
				return;
			}
			if(dateStr == null || dateStr == "" ){
				alert("请选择"+subName+"的考试日期");
				return;
			}
			if($.trim(time) == null || $.trim(time) == "" ){
				alert("请填写"+subName+"的考试时间");
				return;
			}
			
			
			
		}
		dataObj.subjectStr = subjectStr;
		
		var schoolCheckList = $('#examSchool input:checked');
		if(schoolCheckList.length==0){
			alert("请选择学校 ");
			return;
		}
		var schoolStr = "";
		for(var i=0;i<schoolCheckList.length;i++){
			schoolStr = schoolStr + $(schoolCheckList.get(i)).val();
			schoolStr = schoolStr + "#";
			schoolStr = schoolStr + $(schoolCheckList.get(i)).attr("flag");
			if(i != schoolCheckList.length - 1){
				schoolStr = schoolStr + "%";
			}
		}
		dataObj.schoolStr = schoolStr;
		
		
		
		  //新增等待框
        $("#infoWindowH").html("正在保存数据,请等待...");
        newRegional.dialog($('#waitWindow_score'));
		$.ajax({
			url:'/regional/addRegionalExam.do',
			type:'post',
			dataType:'json',
			data:dataObj,
			success:function(data){
				var message = data.message;
				 if (!data) {
                     return false;
                 }
                 if (data.code == 200) {
                	 
                	 $("#overWindowH_score").html(message);
                	 newRegional.dialog($('#overWindow_score'));
                 }else {
                 	$("#overWindowH_score").html(message);
                 	newRegional.dialog($('#overWindow_score'));
                 }
                
			},
		});
	});
	$('#cancelBtn').on('click',function(){
		location.href='/regional/educationList.do';
	});
	$('#overWindowBtn_score').on('click',function(){
		location.href='/regional/educationList.do';
	});
	
//===============================init结尾===================================
    };

    (function(){
    	newRegional.init();
    })();

    module.exports = newRegional;
})
