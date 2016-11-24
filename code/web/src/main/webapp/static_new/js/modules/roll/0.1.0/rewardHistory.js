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
    var rewardHistory = {},
        Common = require('common');

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
    rewardHistory.init = function(){


        
        $("#titleId").html(userName + "同学的奖惩记录");
    	if(rewards == null || rewards.length == 0){
    		alert(userName + "同学暂无奖惩记录！");
    	}
    	
    	var listArea = $("#listArea");
    	listArea.empty();
    	for(var i=0;i<rewards.length;i++){
    		var reward = rewards[i];
    		var htmlStr = '<tr>'+
    					  '       <td>' + reward.date + '</td>'+
    					  '       <td>' + reward.type + '</td>'+
    					  '       <td>' + reward.level + '</td>'+
    					  '       <td>' + reward.contents + '</td>'+
    					  '</tr>';
    		listArea.append(htmlStr);			  
    	}
        
    };

    (function(){
        rewardHistory.init();
    })();

    module.exports = rewardHistory;
})