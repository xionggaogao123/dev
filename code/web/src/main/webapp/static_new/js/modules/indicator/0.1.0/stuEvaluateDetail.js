/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define('stuEvaluateDetail',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var stuEvaluateDetail = {},
        Common = require('common');
    var indiData = {};

    stuEvaluateDetail.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        indiData.parentId="";
        indiData.level=1;
        stuEvaluateDetail.getIndicatorList();

        $('body').on('click', '.em-jia', function(){
            if($(this).hasClass("em-jia")){
                var id = $(this).parent().parent().attr("zhiBiaoId");
                var isOpen = $("tr[zhiBiaoId='"+id+"']").attr("isOpen")||0;
                if(isOpen==0){
                    var level=$(this).parent().parent().attr("level");
                    indiData.parentId=id;
                    indiData.level=parseInt(level)+1;
                    stuEvaluateDetail.getIndicatorList();
                }
                $(this).removeClass("em-jia").addClass("em-jian");
                var childs=$("tr[zhiBiaoParentId='"+id+"']");
                stuEvaluateDetail.showChildZhiBiao(childs);
            }
        });

        $('body').on('click', '.em-jian', function(){
            if($(this).hasClass("em-jian")){
                $(this).removeClass("em-jian").addClass("em-jia");
                var id = $(this).parent().parent().attr("zhiBiaoId");
                var childs=$("tr[zhiBiaoParentId='"+id+"']");
                stuEvaluateDetail.hideChildZhiBiao(childs);
            }
        });

        $('body').on('click','.go-back',function() {
            Common.goTo("/evaluate/evaluateResultInfo.do?appliedId="+$("#appliedId").val());
        });
    };

    stuEvaluateDetail.showChildZhiBiao = function(childs){
        $.each(childs,function(i,item) {
            //$("tr[zhiBiaoId='"+item+"']").show();
            var zhiBiaoId=$(item).attr("zhiBiaoId");
            $("tr[zhiBiaoId='"+zhiBiaoId+"']").show();
            if($(item).find("em").hasClass("em-jian")) {
                var cchilds = $("tr[zhiBiaoParentId='" + zhiBiaoId + "']");
                stuEvaluateDetail.showChildZhiBiao(cchilds);
            }
        });
    };

    stuEvaluateDetail.hideChildZhiBiao = function(childs){
        $.each(childs,function(i,item) {
            var zhiBiaoId=$(item).attr("zhiBiaoId");
            $("tr[zhiBiaoId='"+zhiBiaoId+"']").hide();
            var cchilds=$("tr[zhiBiaoParentId='"+zhiBiaoId+"']");
            stuEvaluateDetail.hideChildZhiBiao(cchilds);
        });
    };

    stuEvaluateDetail.getIndicatorList = function(){
        indiData.appliedId=$("#appliedId").val();
        indiData.activityId=$("#classId").val();
        indiData.termType=$("#termType").val();
        indiData.snapshotId=$("#snapshotId").val();
        indiData.commonToId=$("#commonToId").val();
        indiData.stuState = 2;
        Common.getPostData('/evaluate/getStudentEvaluateList.do', indiData, function(rep){
            if(rep.code==200){
                var dto = rep.message;
                if(indiData.level==1){
                    $('#indicatorTable').html("");
                    Common.render({tmpl: $("#j-tmpl"), data: rep, context: '#indicatorTable'});
                }else{
                    var list = dto.zhiBiaos;
                    var html='';
                    $.each(list,function(i,item){
                        html+='<tr zhiBiaoId="'+item.zhiBiaoId+'" zhiBiaoParentId="'+indiData.parentId+'" level="'+item.level+'" type="'+item.type+'" isOpen="0" class="evaluate-tr evaluate-tr-bl">';
                        html+='<td width="475px;">';
                        var level=parseInt(item.level);
                        if(item.type==1){
                            var marge=35+40*(level-1);
                            html+='<em class="em-jia" style="margin-left: '+marge+'px;"></em>';
                            html+='<input readonly="readonly"  placeholder="'+item.zhiBiaoName+'"/>';
                        }else{
                            var marge1=35+40*(level-1);
                            html+='<input style="margin-left: '+marge1+'px;" readonly="readonly"  placeholder="'+item.zhiBiaoName+'"/>';
                        }
                        html+='</td>';
                        html+='<td>';
                        if(item.scoreType==1){
                            var score = parseInt(item.score);
                            html+='<div class="gradecon" id="Addnewskill_119"><ul class="rev_pro clearfix">';
                            html+='<li>';
                            html+='<input class="fl" type="hidden" style="margin-top:2px;" name="InterviewCommentInfoSub[1]" value="'+item.score+'" />';
                            html+=' <div class="revinp">';
                            html+='            <span class="level">';
                            for(var i=1; i<=5;i++){
                                if(i<=score){
                                    html+='                <i class="level_solid" cjmark=""></i>';
                                }else{
                                    html+='                <i class="level_hollow" cjmark=""></i>';
                                }
                            }
                            html+='             </span>';
                            html+='             </div>';
                            html+='         </li>';
                            html+='     </ul>';
                            html+='     </div>';
                        }
                        html+='</td>';
                        html+='</tr>';
                    });
                    $("tr[zhiBiaoId='"+indiData.parentId+"']").after(html);
                    $("tr[zhiBiaoId='"+indiData.parentId+"']").attr("isOpen",1);
                }
            }
        });
    };


    module.exports=stuEvaluateDetail;
});