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
define(['common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addProject = {},
        Common = require('common');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    Common.dialogClose = function(obj,callback){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();

        obj.find('.closePop span').click(function(){
            Common.dialog.close($(this));
        })
        $(document).scrollTop(0);
    };

    Common.dialog.close = function(obj){
        $(obj).closest('.pop-wrap').hide();
        $('.bg-dialog').hide();

    };

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * roll.init()
     */
    addProject.init = function(){


        $('.proManage').on('click','.addProjectRadio',function(){
        	$('#newProjectName').val('');
            Common.dialog($('#addRadio'));
            return false;
        });
        //编辑弹窗
        $('.project').on('click','.orange-btn-new',function(){
        	$('#editSubProjectName').val($(this).attr("myname"));
        	$('#editSubProjectRequirement').val($(this).attr("myrequirement"));
        	$('#editSubConfirmBtn').attr('flag',$(this).parent().parent().attr('flag'));
            Common.dialog($('#editSubProj'));
            return false;
        });
        $('#addSubProj').on('click','.depic',function(){
        	$('#newSubProjectName').val('');
        	$('#newSubProjectRequirement').val('');
            Common.dialog.close($('#addSubProj'));
            return false;
        });
        $('#editSubProj').on('click','.depic',function(){
            Common.dialog.close($('#editSubProj'));
            return false;
        });
        $('.proMana').on('click','.addSubProject',function(){
            Common.dialog($('#addSubProj'));
            return false;
        });
        $('#addSubProj').on('click','.depic',function(){
            Common.dialog.close($('#addSubProj'));
            return false;
        });
        
   //自己的东西
        //新增
        $('#newSubConfirmBtn').on('click',function(){
        	var projectName = $.trim($('#newSubProjectName').val());
        	var projectRequirement = $.trim($('#newSubProjectRequirement').val());
        	if(projectName == ""){
        		alert("评价指标不能为空！请填写完整！");
        		return;
        	}
        	if(projectRequirement == ""){
        		alert("表现要求不能为空！请填写完整！");
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/quality/addSubProject.do",
                data: {name : projectName , requirement : projectRequirement , parentId : PUBLIC_PARENT_ID},
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		alert("新增成功！");
                		location.reload();
                	}else{
                		alert("新增失败 ！请联系管理员");
                	}
                	
                }
        	});
        });
        
        //编辑
        $('#editSubConfirmBtn').on('click',function(){
        	var projectName = $.trim($('#editSubProjectName').val());
        	var projectRequirement = $.trim($('#editSubProjectRequirement').val());
        	var targetId = $(this).attr("flag");
        	if(projectName == ""){
        		alert("评价指标不能为空！请填写完整！");
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/quality/editSubProject.do",
                data: {name : projectName,requirement : projectRequirement,id : targetId},
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		alert("编辑成功！");
                		location.reload();
                	}else{
                		alert("编辑失败 ！请联系管理员");
                	}
                	
                }
        	});
        });
        
      //删除
        $('body').on('click','.grey-btn-delete',function(){
        	if(confirm("删除后不可恢复,是否确认！？")){
        		var targetId = $(this).parent().parent().attr("flag");
        		$.ajax({
            		type: "POST",
                    url: "/quality/delSubProject.do",
                    data: {id : targetId},
                    dataType: "json",
                    success: function(data){
                    	if(data.code == 200){
                    		alert("删除成功！");
                    		location.reload();
                    	}else{
                    		alert("删除失败 ！请联系管理员");
                    	}
                    }
            	});
        	}
        	
        	
        });
        

//=============================================init结束======================================================

    };

    (function(){
    	addProject.init();
    })();

    module.exports = addProject;
})