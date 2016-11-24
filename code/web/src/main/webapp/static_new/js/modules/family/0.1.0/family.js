/**
 * Created by XXX on 15/9/15.
 */
/**
 * @author XXX
 * @module  添加家庭成员
 * @description
 * 添加家庭成员
 */
/* global Config */
define(['common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var family = {},
        Common = require('common');

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

    family.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
//        obj.find('.pop-btn span').click(function(){
//            $(this).closest('.pop-wrap').hide();
//            $('.bg-dialog').hide();
//        });
        $('body').scrollTop(0);
    };

    function closeDialog($targetDialogObj){
    	$targetDialogObj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    }

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    family.init = function(){
		if (GetQueryString("a")!="10000") {


		}
        Common.tab('click',$('.grow-tab-head'))
        
        //关闭按钮绑定事件
        $('.closeBtnClass').on('click',function(){
        	closeDialog($(this));
        });

        $('.addMember').on('click',function(){
        	clearMemberWindow("add");
        	family.dialog($('#addFalmilyWindow'));
            return false;
        });
        $('body').on('click','.edit-family',function(){
        	clearMemberWindow("edit");
        	$.ajax({
         		type: "POST",
                 url: "/registration/queryFamilyMemberDetail.do",
                 data: {id : $(this).closest('tr').attr("flag")},
                 dataType: "json",
                 success: function(data){
                	 $("#member_save_btn_edit").attr("flag",data.id);
                	 
                	 $("#memberName_edit").val(data.memberName);
                 	 $("#memberRelation_edit").val(data.memberRelation);
                 	 $("#memberRace_edit").val(data.memberRace);
                 	 $("#memberNationality_edit").val(data.memberNationality);
                 	 $("#memberSex_edit").val(data.memberSex);
                 	 $("#memberBirthday_edit").val(data.memberBirthday);
                 	 $("#memberEducation_edit").val(data.memberEducation);
                 	 $("#memberWork_edit").val(data.memberWork);
                 	 $("#memberPolitics_edit").val(data.memberPolitics);
                 	 $("#memberHealth_edit").val(data.memberHealth);
                 	 $("#memberAddressNow_edit").val(data.memberAddressNow);
                 	 $("#memberAddressRegistration_edit").val(data.memberAddressRegistration);
                 	 $("#memberPhone_edit").val(data.memberPhone);
                 	 $("#memberEmail_edit").val(data.memberEmail);
                	 
                	 family.dialog($('#editFalmilyWindow'));
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        	
        	return false;
        });
        $('.addResume').on('click',function(){
        	clearResumeWindow('add');
        	family.dialog($('#addStudyHistoryWindow'));
            return false;
        });
        $('body').on('click','.edit-resume',function(){ 
        	clearResumeWindow('edit');
        	$.ajax({
         		type: "POST",
                 url: "/registration/queryResumeDetail.do",
                 data: {id : $(this).closest('tr').attr("flag")},
                 dataType: "json",
                 success: function(data){
                	 $("#resume_save_btn_edit").attr("flag",data.id);
                	 
                	$("#resumeSD_edit").val(data.startDate);
                 	$("#resumeED_edit").val(data.endDate);
                 	$("#entrance_edit").val(data.entranceType);
                 	$("#studyType_edit").val(data.studyType);
                 	$("#studyCompany_edit").val(data.studyUnit);
                 	$("#resumePostscript_edit").val(data.postScript);
                	 
                 	family.dialog($('#editStudyHistoryWindow'));
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        	return false;
        });
        

        $('.content-view').on('click',function(){
        	family.dialog($('#bxcontentId'));
            return false;
        })

        $('.icon-edit').on('click',function(){
        	family.dialog($('#editbxsqId'));
            return false;
        })

        $('.result-view, .icon-result').on('click',function(){
        	family.dialog($('#bxresultId'));
            return false;
        })

        $('.icon-set').on('click',function(){
        	family.dialog($('#bxstatusId'));
            return false;
        })
        
        /**
         * 公共变量
         */
        var addTypeStr = "add";
        var editTypeStr = "edit";
        
        /**
    	 * 以下代码为家庭成员
    	 */
        //清空家庭成员窗口方法（根据传入参数指定清空哪个窗口）
        function clearMemberWindow(str){
        	$("#memberName_" + str).val("");
        	$("#memberRelation_" + str).val("父亲");
        	$("#memberRace_" + str).val("汉族");
        	$("#memberNationality_" + str).val("中国");
        	$("#memberSex_" + str).val("1");
        	$("#memberBirthday_" + str).val("");
        	$("#memberEducation_" + str).val("小学");
        	$("#memberWork_" + str).val("公务员");
        	$("#memberPolitics_" + str).val("群众");
        	$("#memberHealth_" + str).val("健康");
        	$("#memberAddressNow_" + str).val("");
        	$("#memberAddressRegistration_" + str).val("");
        	$("#memberPhone_" + str).val("");
        	$("#memberEmail_" + str).val("");
        }
        function uploadObjectForMember(targetObj,str){
        	targetObj.memberName = $.trim($("#memberName_" + str).val());
        	targetObj.memberRelation = $("#memberRelation_" + str).val();
        	targetObj.memberRace = $("#memberRace_" + str).val();
        	targetObj.memberNationality = $("#memberNationality_" + str).val();
        	targetObj.memberSex = $("#memberSex_" + str).val();
        	targetObj.memberBirthday = $("#memberBirthday_" + str).val();
        	targetObj.memberEducation = $("#memberEducation_" + str).val();
        	targetObj.memberWork = $("#memberWork_" + str).val();
        	targetObj.memberPolitics = $("#memberPolitics_" + str).val();
        	targetObj.memberHealth = $("#memberHealth_" + str).val();
        	targetObj.memberAddressNow = $("#memberAddressNow_" + str).val();
        	targetObj.memberAddressRegistration = $("#memberAddressRegistration_" + str).val();
        	targetObj.memberPhone = $("#memberPhone_" + str).val();
        	targetObj.memberEmail = $("#memberEmail_" + str).val();
        	if(targetObj.memberName == ""){
        		return "请填写姓名！";
        	}
        	if(targetObj.memberBirthday == ""){
        		return "请填填写生日！";
        	}
        	return "";	
        	
        }
        //新增家庭成员窗口保存方法
        $("#member_save_btn_add").click(function(){
        	var addObj = {};
        	var alertStr = uploadObjectForMember(addObj,addTypeStr);
        	if(alertStr != ""){
        		alert(alertStr);
        		return;
        	}
        	addObj.userId = PUBLIC_USER_ID;
        	$.ajax({
         		type: "POST",
                 url: "/registration/addFamilyMember.do",
                 data: addObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog($("#addFalmilyWindow"));
                	 memberList = data;
                	 fillMemberList();
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        //编辑家庭成员信息
        $("#member_save_btn_edit").click(function(){
        	var editObj = {};
        	var alertStr = uploadObjectForMember(editObj,editTypeStr);
        	if(alertStr != ""){
        		alert(alertStr);
        		return;
        	}
        	editObj.id = $(this).attr("flag");
        	editObj.userId = PUBLIC_USER_ID;
        	$.ajax({
         		type: "POST",
                 url: "/registration/editFamilyMember.do",
                 data: editObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog($("#editFalmilyWindow"));
                	 memberList = data;
                	 fillMemberList();
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        //查询家庭成员列表
        var memberListArea = $("#memberListArea");
        function fillMemberList(){
        	memberListArea.empty();
        	for(var i=0;i<memberList.length;i++){
        		var member = memberList[i];
        		var htmlStr = '				<tr flag="' + member.id + '">'+
        		'                                <td>' + member.name + '</td>'+
        		'                                <td>' + member.relation + '</td>'+
        		'                                <td>' + member.race + '</td>'+
        		'                                <td>' + member.birthday + '</td>'+
        		'                                <td>' + member.sex + '</td>'+
        		'                                <td class="address">' + member.address + '</td>'+
        		'                                <td>' + member.phone + '</td>'+
        		'                                <td>';
        		if(familyCanEdit){
        			htmlStr = htmlStr + '                <a href="#" class="orange-btn-new edit-family">编辑</a>'+
            		'                                    <a href="#" class="grey-btn-delete del-family">删除</a>';
        		}else{
        			htmlStr = htmlStr + '                <a href="#" class="orange-btn-new edit-family">查看</a>';
        		}
        		htmlStr = htmlStr +'        	</td>'+
        		'                            </tr>';
        		memberListArea.append(htmlStr);			  
        	}
        }
        fillMemberList();
        //删除家庭成员
        $('body').on('click','.del-family',function(){
        	if(confirm("删除后不能恢复！确定删除吗？")){
        		$.ajax({
             		type: "POST",
                     url: "/registration/delFamilyMember.do",
                     data: {id : $(this).closest('tr').attr("flag")},
                     dataType: "json",
                     success: function(data){
                    	 alert("操作成功！");
                    	 memberList = data;
                    	 fillMemberList();
                     }, 
                     error:function(data){
                     	alert("系统错误，请联系管理员！");
                     }
             	});
        	}
        });
        
        /**
         * 以下代码为学习简历
         */
        //清空学习简历窗口的方法（根据传入参数指定清空哪个窗口）
        function clearResumeWindow(str){
        	$("#resumeSD_" + str).val("");
        	$("#resumeED_" + str).val("");
        	$("#entrance_" + str).val("统一招生考试");
        	$("#studyType_" + str).val("走读");
        	$("#studyCompany_" + str).val("");
        	$("#resumePostscript_" + str).val("");
        }
        //组装数据
        function uploadObjectForResume(targetObj,str){
        	targetObj.resumeSD = $("#resumeSD_" + str).val();
        	targetObj.resumeED = $("#resumeED_" + str).val();
        	targetObj.entrance = $("#entrance_" + str).val();
        	targetObj.studyType = $("#studyType_" + str).val();
        	targetObj.studyCompany = $.trim($("#studyCompany_" + str).val());
        	targetObj.resumePostscript = $.trim($("#resumePostscript_" + str).val());
        	if(targetObj.resumeSD == "" || targetObj.resumeED == ""){
        		return "请填写起止日期！";
        	}
        	if(targetObj.memberBirthday == ""){
        		return "请填写生日！";
        	}
        	if(targetObj.studyCompany == ""){
        		return "请填写学习单位！";
        	}
        	return "";	
        	
        }
      //新增学习简历窗口保存方法
        $("#resume_save_btn_add").click(function(){
        	var addObj = {};
        	var alertStr = uploadObjectForResume(addObj,addTypeStr);
        	if(alertStr != ""){
        		alert(alertStr);
        		return;
        	}
        	addObj.userId = PUBLIC_USER_ID;
        	$.ajax({
         		type: "POST",
                 url: "/registration/addStudyResume.do",
                 data: addObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog($("#addStudyHistoryWindow"));
                	 resumeList = data;
                	 fillResumeList();
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        //编辑学习简历窗口保存方法
        $("#resume_save_btn_edit").click(function(){
        	var editObj = {};
        	var alertStr = uploadObjectForResume(editObj,editTypeStr);
        	if(alertStr != ""){
        		alert(alertStr);
        		return;
        	}
        	editObj.id = $(this).attr("flag");
        	editObj.userId = PUBLIC_USER_ID;
        	$.ajax({
         		type: "POST",
                 url: "/registration/editLearningResume.do",
                 data: editObj,
                 dataType: "json",
                 success: function(data){
                	 alert("保存成功！");
                	 closeDialog($("#editStudyHistoryWindow")); 
                	 resumeList = data;
                	 fillResumeList();
                 }, 
                 error:function(data){
                 	alert("系统错误，请联系管理员！");
                 }
         	}); 
        });
        //查询家庭成员列表
        var resumeListArea = $("#resumeListArea");
        function fillResumeList(){
        	resumeListArea.empty();
        	for(var i=0;i<resumeList.length;i++){
        		var resume = resumeList[i];
        		var htmlStr = '			<tr flag="' + resume.id + '">'+
        		'                            <td>' + resume.startDate + '</td>'+
        		'                            <td>' + resume.endDate + '</td>'+
        		'                            <td>' + resume.entranceType + '</td>'+
        		'                            <td>' + resume.studyType + '</td>'+
        		'                            <td class="resumeSch">' + resume.studyUnit + '</td>'+
        		'                            <td>' + resume.postScript + '</td>'+
        		'                            <td>';
        		if(resumeCanEdit){
        			htmlStr = htmlStr + '                <a href="#" class="orange-btn-new edit-resume">编辑</a>'+
            		'                                    <a href="#" class="grey-btn-delete del-resume">删除</a>';
        		}else{
        			htmlStr = htmlStr + '                <a href="#" class="orange-btn-new edit-resume">查看</a>';
        		}
        		htmlStr = htmlStr + '        </td>'+
        		'                        </tr>';
        		resumeListArea.append(htmlStr);			  
        	}
        }
        fillResumeList();
        //删除学习简历
        $('body').on('click','.del-resume',function(){
        	if(confirm("删除后不能恢复！确定删除吗？")){
        		$.ajax({
             		type: "POST",
                     url: "/registration/delLearningResume.do",
                     data: {id : $(this).closest('tr').attr("flag")},
                     dataType: "json",
                     success: function(data){
                    	 alert("操作成功！");
                    	 resumeList = data;
                    	 fillResumeList();
                     }, 
                     error:function(data){
                     	alert("系统错误，请联系管理员！");
                     }
             	});
        	}
        });
        /**
         * 以下代码为奖惩记录
         */
//    	if(rewards == null || rewards.length == 0){
//    		alert(userName + "同学暂无奖惩记录！");
//    	}
    	
    	var rewardListArea = $("#rewardListArea");
    	function fillRewardList(){
    		rewardListArea.empty();
        	for(var i=0;i<rewardList.length;i++){
        		var reward = rewardList[i];
        		var htmlStr = '<tr>'+
        					  '       <td>' + reward.date + '</td>'+
        					  '       <td>' + reward.type + '</td>'+
        					  '       <td>' + reward.level + '</td>'+
        					  '       <td>' + reward.contents + '</td>'+
        					  '</tr>';
        		rewardListArea.append(htmlStr);			  
        	}
    	}
    	fillRewardList();
        
    	
        

    };
	function GetQueryString(name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r!=null)return  unescape(r[2]); return null;
	}
    (function(){
        family.init();
    })();

    module.exports = family;
})