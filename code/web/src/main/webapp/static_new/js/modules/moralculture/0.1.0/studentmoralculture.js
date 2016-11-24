/*
 * @Author: Tony
 * @Date:   2015-07-02 11:55:29
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-07 09:10:36
 */
define('studentmoralculture',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('easing');
    var studentmoralculture = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * studentmoralculture.init()
     */
    studentmoralculture.init = function(){


        //获取用户数据
        studentmoralculture.getUserData();
        //校验成绩是数字
        studentmoralculture.keyupScore();
        //保存学生录入的德育成绩信息
        studentmoralculture.saveStudentMoralCultureInfo();
        $("#semesterId").change(function(){
            studentmoralculture.getUserData();
        });

        $("#moralCultureScore").click(function ( ){
            var semesterId=$("#semesterId").val();
            window.location.href="/moralCultureScore/moralCultureScorePage.do?semesterId="+semesterId;
        });

    };
    /**
     * 获取用户数据
     */
    studentmoralculture.getUserData=function(){
        var parentRole=$("#parentRole").val();
        $("input[name='id']").each(function(){
            if(parentRole!="false"){
                $(this).next().text("");
            }else{
                $(this).next().val("");
            }
        });
        var selData = {};
        selData.semesterId = $("#semesterId").val();
        Common.getPostData('/moralCultureScore/selPersonalMoralCultureScore.do', selData,function(rep){
                var personalScore=rep.personalScore;
                var parentRole=$("#parentRole").val();
                if (personalScore != null && personalScore != "") {
                    for (var i = 0; i < personalScore.length; i++) {
                        var obj = personalScore[i];
                        $("input[name='id']").each(function(){
                            if (obj.projectId == $(this).val()) {

                                if(parentRole!="false"){

                                    $(this).next().text(obj.projectScore);
                                }else{
                                    $(this).next().val(obj.projectScore);
                                }
                            }
                        });
                    }
                }
            }
        );
    }
    /**
     * 校验成绩是数字
     */
    studentmoralculture.scoreIsNumber=function(obj,flag){
        var score=obj.val();
        var moralCultureName=obj.parent().find('p').text().replace("：","");
        var reg = new RegExp("^[0-9]*$");
        if(!reg.test(score.replace(".","").replace(/,/g,""))){
            //msg+="适用利率请输入数字!\n";
            alert(moralCultureName+"成绩请输入数字!");
            obj.val('');
            return false;
        }else{
            if(100>=score&&score>=0){
                if(score.indexOf('.')>0){
                    if(score.substring(0,score.indexOf('.')).length>2){
                        alert(moralCultureName+"成绩带小数，正数不能超过两位！");
                        obj.val(score.substring(0,3));
                        return false;
                    }
                    if(score.substring(score.indexOf('.')+1).length>1){
                        alert(moralCultureName+"成绩带小数，小数不能超过一位！");
                        obj.val(score.substring(0,score.indexOf('.'))+score.substring(score.indexOf('.'),score.indexOf('.')+2));
                        return false;
                    }
                    if(flag==2){
                        if(score.substring(score.indexOf('.')+1).length<1){
                            alert(moralCultureName+"成绩录入错误！");
                            obj.val(score.substring(0,score.indexOf('.')));
                            return false;
                        }
                    }
                }else{
                    if(score.length>3){
                        alert(moralCultureName+"成绩不能超过三位！");
                        obj.val(score.substring(0,3));
                        return false;
                    }
                }
            }else{
                if(score>100){
                    obj.val(score.substring(0,2));
                }else{
                    obj.val("");
                }
                alert(moralCultureName+"成绩数值范围错误！");
                return false;
            }
        }
        return true;
    }

    /**
     * 录入成绩
     */
    studentmoralculture.keyupScore=function(){
        $('.score').keyup(function(event){
            studentmoralculture.scoreIsNumber($(this),1);
        });
    }

    /**
     *保存学生录入的德育成绩信息
     */
    studentmoralculture.saveStudentMoralCultureInfo=function(){
        $('.que').click(function(event){
            var saveData = {};
            var ids=new Array();
            $("input[name='id']").each(function(){
                ids.push($(this).val());
            });
            saveData.semesterId=$("#semesterId").val();
            saveData.ids=ids;
            var scores=new Array();
            var total=0;
            var flag=true;
            $("input[name='score']").each(function(){
                if($(this).val()!=""&&$(this).val()!=null){
                    total++;
                    if(!studentmoralculture.scoreIsNumber($(this),2)){
                        flag=false;
                        return;
                    }
                }
                scores.push($(this).val());
            });
            if(!flag){
                return;
            }
            if(total==0){
                alert("请至少输入一项德育成绩！");
                return;
            }
            saveData.scores=scores;
            Common.getPostData('/moralCultureScore/addOrEditMoralCultureScoreInfo.do', saveData,function(rep){
                    //查询德育项目信息数据
                    //moralculturemanage.initMoralCultureManageData();
                    if(rep.result){
                        alert("保存成功！");
                    }
                    location.reload();
                }
            );

        });
    }

    studentmoralculture.init();
});