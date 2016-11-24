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
    var projectManage = {},
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
    projectManage.init = function(){


        $('.proManage').on('click','.addProjectRadio',function(){
        	$('#newProjectName').val('');
            Common.dialog($('#addRadio'));
            return false;
        });
        //编辑弹窗
        $('.project').on('click','.orange-btn-new',function(){
        	$('#editProjectName').val($(this).parent().prev().html());
        	$('#editConfirmBtn').attr('flag',$(this).parent().parent().attr('flag'));
            Common.dialog($('#editRadio'));
            return false;
        });
        $('#addRadio').on('click','.depic',function(){
            Common.dialog.close($('#addRadio'));
            return false;
        });
        $('#editRadio').on('click','.depic',function(){
            Common.dialog.close($('#editRadio'));
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
        $('#newConfirmBtn').on('click',function(){
        	var projectName = $.trim($('#newProjectName').val());
        	if(projectName == ""){
        		alert("评价指标不能为空！请填写完整！");
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/quality/addProject.do",
                data: {name : projectName},
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
        $('#editConfirmBtn').on('click',function(){
        	var projectName = $.trim($('#editProjectName').val());
        	var targetId = $(this).attr("flag");
        	if(projectName == ""){
        		alert("评价指标不能为空！请填写完整！");
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/quality/editProject.do",
                data: {name : projectName,id : targetId},
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
                    url: "/quality/delProject.do",
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
      //重置
        $('body').on('click','.resetAll',function(){
        	if(confirm("重置后将清除本学期所有素质教育评价和班主任评价，且不可恢复,是否确认！？")){
        		$.ajax({
            		type: "POST",
                    url: "/growth/resetAll.do",
                    data: {},
                    dataType: "json",
                    success: function(data){
                    	if(data.code == 200){
                    		alert("重置成功！");
                    		location.reload();
                    	}else{
                    		alert("重置失败 ！请联系管理员");
                    	}
                    }
            	});
        	}
        	
        	
        });
        
        //跳转到子项目1231231
        $('body').on('click','.projectDetail',function(){
        	var targetId = $(this).parent().attr("flag"); 
        	location.href = "/quality/subList.do?parentId=" + targetId; 
        })

//=============================================init结束======================================================

    };

    (function(){
    	projectManage.init();
    })();

    module.exports = projectManage;
})