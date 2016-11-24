/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define('evaluate',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var evaluate = {},
        Common = require('common');
    var indiData = {};

    evaluate.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        evaluate.getInterestClassList();

        var classObj=$(".evaluate-tab .li-cur");
        $("#classId").val(classObj.attr("classId"));
        evaluate.getStudentList();

        $('body').on('click', '.evaluate-tab li', function(){
            $(this).addClass("li-cur").siblings().removeClass("li-cur");
            $("#classId").val($(this).attr("classId"));
            evaluate.getStudentList();
        });

        $(".evaluate-pro-top li").click(function(){
            $(this).addClass("pro-cur").siblings().removeClass("pro-cur");
            evaluate.getStudentList();
            var stuState = $(".evaluate-pro-top").find(".pro-cur").val();
            $("#describe").val("");
        });

        $('body').on('click', '.evaluate-pro-info li', function(){
            var stuState = $(".evaluate-pro-top").find(".pro-cur").val();
            if(stuState==1){
                if($(this).hasClass("evaluate-im")){
                    $(this).removeClass("evaluate-im");
                }else{
                    $(this).addClass("evaluate-im");
                }
            }else{
                $("#describe").val("");
                if($(this).hasClass("evaluate-im")){
                    $(this).removeClass("evaluate-im");
                    $('#indicatorTable').html("");
                }else{
                    $(this).addClass("evaluate-im").siblings().removeClass("evaluate-im");
                    indiData.commonToId=$(this).attr("stu-id");
                    indiData.parentId="";
                    indiData.level=1;
                    //evaluate.getStudentEvaluateList();
                    evaluate.getIndicatorList();
                }
            }
        });

        /*   $(".te-stack").click(function(){
         if($(this).hasClass("te-stack")){
         $(this).removeClass("te-stack").addClass("te-stack-h")
         }else{
         $(this).removeClass("te-stack-h").addClass("te-stack")
         }
         });*/
        /*  $('body').on('mouseover','td', function(){
         var s =$(this).find("ul");
         var n=s.children();
         var input=s.siblings("input");
         clearAll = function () {
         for (var i = 0; i < n.length; i++) {
         n[i].className = '';
         }
         }
         for (var i = 0; i < n.length; i++) {
         n[i].onclick = function () {
         var q = this.getAttribute("rel");
         clearAll();
         input.val(q);
         for (var i = 0; i < q; i++) {
         n[i].className = 'on';
         }
         }
         n[i].onmouseover = function () {
         var q = this.getAttribute("rel");
         clearAll();
         for (var i = 0; i < q; i++) {
         n[i].className = 'on';
         }
         }
         n[i].onmouseout = function () {
         clearAll();
         for (var i = 0; i < input.val(); i++) {
         n[i].className = 'on';
         }
         }
         }
         });*/

        //点星星
        $('body').on('mouseover','i[cjmark]',function(){
            var num = $(this).index();
            var pmark = $(this).parents('.revinp');
            var mark = pmark.prevAll('input');
            //var val = mark.val();
            //if(mark.prop('checked')) return false;

            var list = $(this).parent().find('i');
            for(var i=0;i<=num;i++){
                list.eq(i).attr('class','level_solid');
            }
            for(var i=num+1,len=list.length-1;i<=len;i++){
                list.eq(i).attr('class','level_hollow');
            }
            //$(this).parent().next().html(degree[num+1]);

            /* if(val != 0){
             for(var i=0;i<=val;i++){
             list.eq(i).attr('class','level_solid');
             }
             }else{
             for(var i=0;i<=list.length-1;i++){
             list.eq(i).attr('class','level_hollow');
             }
             } */

        });

        $('body').on('mouseout','i[cjmark]',function(){
            var num = $(this).index();
            var pmark = $(this).parents('.revinp');
            var mark = pmark.prevAll('input');
            var val = parseInt(mark.val());
            //if(mark.prop('checked')) return false;

            var list = $(this).parent().find('i');
            //alert(list.length);
            if(val != 0){
                for(var i=0;i<=val;i++){
                    list.eq(i).attr('class','level_solid');
                }
                //alert(val);
                for(var i=val,len=list.length-1;i<=len;i++){
                    list.eq(i).attr('class','level_hollow');
                }
                //$(this).parent().next().html(degree[val]);
            }else{
                for(var i=0;i<=list.length-1;i++){
                    list.eq(i).attr('class','level_hollow');
                }
                //$(this).parent().next().html("未评分");
            }
        });

        //点击星星
        $('body').on('click','i[cjmark]',function(){
            var num = $(this).index();
            var pmark = $(this).parents('.revinp');
            var mark = pmark.prevAll('input');
            mark.val(num+1);

        });
        $('body').on('click','.gradcon-c',function(){
            var num = $(this).index();
            var pmark = $(this).parents('.revinp');
            var mark = pmark.prevAll('input');
            $(this).parent().siblings("input").val(0);
            $(this).siblings(".level").find("i").attr('class','level_hollow');

        });

        indiData.parentId="";
        indiData.level=1;
        evaluate.getIndicatorList();
        $('body').on('click', '.em-jia', function(){
            if($(this).hasClass("em-jia")){
                var id = $(this).parent().parent().attr("zhiBiaoId");
                var isOpen = $("tr[zhiBiaoId='"+id+"']").attr("isOpen")||0;
                if(isOpen==0){
                    var level=$(this).parent().parent().attr("level");
                    indiData.parentId=id;
                    indiData.level=parseInt(level)+1;
                    evaluate.getIndicatorList();
                }
                $(this).removeClass("em-jia").addClass("em-jian");
                var childs=$("tr[zhiBiaoParentId='"+id+"']");
                evaluate.showChildZhiBiao(childs);
            }
        });

        $('body').on('click', '.em-jian', function(){
            if($(this).hasClass("em-jian")){
                $(this).removeClass("em-jian").addClass("em-jia");
                var id = $(this).parent().parent().attr("zhiBiaoId");
                var childs=$("tr[zhiBiaoParentId='"+id+"']");
                evaluate.hideChildZhiBiao(childs);
            }
        });

        $(".com-BC").click(function(){
            evaluate.saveEvaluate();
        });

        $('body').on('click','.com-QX, .go-back',function() {
            Common.goTo("/evaluate/evaluateManage.do?index=6&version=4");
        });
    };

    evaluate.getInterestClassList = function(){
        var selParam={};
        selParam.id=$("#appliedId").val();
        Common.getPostData('/evaluate/getInterestClassList.do', selParam, function(rep){
            if(rep.code=200){
                $('#classList').html("");
                var lenth = rep.message.length*184;
                $('#classList').css('width',lenth);
                Common.render({tmpl: $("#j-tmpl3"), data: rep, context: '#classList'});

            }
        });
    }

    evaluate.showChildZhiBiao = function(childs){
        $.each(childs,function(i,item) {
            //$("tr[zhiBiaoId='"+item+"']").show();
            var zhiBiaoId=$(item).attr("zhiBiaoId");
            $("tr[zhiBiaoId='"+zhiBiaoId+"']").show();
            if($(item).find("em").hasClass("em-jian")) {
                var cchilds = $("tr[zhiBiaoParentId='" + zhiBiaoId + "']");
                evaluate.showChildZhiBiao(cchilds);
            }
        });
    };

    evaluate.hideChildZhiBiao = function(childs){
        $.each(childs,function(i,item) {
            var zhiBiaoId=$(item).attr("zhiBiaoId");
            $("tr[zhiBiaoId='"+zhiBiaoId+"']").hide();
            var cchilds=$("tr[zhiBiaoParentId='"+zhiBiaoId+"']");
            evaluate.hideChildZhiBiao(cchilds);
        });
    };

    /**
     * 查询学生信息列表
     */
    evaluate.getStudentList = function(){
        var selStuParam={};
        selStuParam.appliedId=$("#appliedId").val();
        selStuParam.activityId=$("#classId").val();
        if(selStuParam.activityId==""){
            alert("请选择兴趣班!");
            return;
        }
        selStuParam.termType=$("#termType").val();
        selStuParam.stuState = $(".evaluate-pro-top").find(".pro-cur").val();
        Common.getPostData('/evaluate/getStudentList.do', selStuParam, function(rep){
            if(rep.code=200){
                $('#studentList').html("");
                var lenth = rep.message.length*95;
                $('#studentList').css('width',lenth);
                Common.render({tmpl: $("#j-tmpl2"), data: rep, context: '#studentList'});
                indiData.parentId="";
                indiData.level=1;
                if(selStuParam.stuState==1){
                    evaluate.getIndicatorList();
                }else{
                    $('#indicatorTable').html("");
                }
            }
        });
    };

    /**
     * 保存
     */
    evaluate.saveEvaluate = function(){
        var url="";
        var saveDate={};
        var stuState = $(".evaluate-pro-top").find(".pro-cur").val();
        var activityId=$("#classId").val();
        var termType=$("#termType").val();
        var describe=$("#describe").val();
        var appliedId=$("#appliedId").val();
        var snapshotId=$("#snapshotId").val();
        saveDate.id=$("#evaluateId").val();
        saveDate.activityId=activityId;
        saveDate.termType=termType;
        saveDate.describe=describe;
        saveDate.appliedId=appliedId;
        saveDate.snapshotId=snapshotId;
        var evaScores = [];
        var commonToIdsStr="";
        $("#studentList li").each(function (i, item) {
            if($(item).hasClass("evaluate-im")){
                if(commonToIdsStr==""){
                    commonToIdsStr=$(item).attr("stu-id");
                }else{
                    commonToIdsStr+=","+$(item).attr("stu-id");
                }
            }
        });
        if(commonToIdsStr==""){
            alert("请选择学生!");
            return;
        }
        saveDate.commonToIdsStr=commonToIdsStr;
        if(stuState==1){
            url="/evaluate/addEvaluate.do";
        }else{
            url="/evaluate/updEvaluate.do";
        }
        $('#indicatorTable tr').each(function (i, tr) {
            var score = $(tr).find(".fl").val();
            var zhiBiaoId = $(tr).attr("zhiBiaoId");
            //var zhiBiaoParentId = $(tr).attr("zhiBiaoParentId");
            //var level = $(tr).attr("level");
            //var type = $(tr).attr("type");
            var evaScore = {
                'zhiBiaoId': zhiBiaoId,
                'scoreType':1,
                'score':score
            };
            evaScores.push(evaScore);
        });
        saveDate.zhiBiaos=evaScores;
        $.ajax({
            url: url,
            type: 'post',
            data: JSON.stringify(saveDate),
            contentType: 'application/json',
            success: function (result) {
                if (result.code=200) {
                    alert(result.message);
                    evaluate.getStudentList();
                    $("#describe").val("");
                    //setTimeout(function() { location.href = '/evaluate/evaluateManage.do?index=6&version=4'; }, 1000)
                } else {
                    alert(result.message);
                }
            }
        });

    };
    //evaluate.getStudentEvaluateList
    evaluate.getIndicatorList = function(){
        indiData.appliedId=$("#appliedId").val();
        indiData.activityId=$("#classId").val();
        indiData.termType=$("#termType").val();
        indiData.snapshotId=$("#snapshotId").val();
        indiData.stuState = $(".evaluate-pro-top").find(".pro-cur").val();
        Common.getPostData('/evaluate/getStudentEvaluateList.do', indiData, function(rep){
            if(rep.code==200){
                var dto = rep.message;
                $("#evaluateId").val(dto.id);
                $("#describe").val(dto.describe);
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
                            html+='            <!-- <span style="display: none" class="revgrade">未评分</span>-->';
                            html+='             <span class="gradcon-c">重新打分</span>';
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

    /**
     * 查询指标信息列表
     */
    /*evaluate.getIndicatorList = function(){
     Common.getPostData('/growth/getIndicatorList.do', indiData,function(rep){
     if(rep.code==200){
     if(indiData.level==1){
     $('#indicatorTable').html("");
     Common.render({tmpl: $("#j-tmpl"), data: rep, context: '#indicatorTable'});
     }else{
     var list = rep.message;
     var html='';
     $.each(list,function(i,item){
     html+='<tr zhiBiaoId="'+item.id+'" zhiBiaoParentId="'+indiData.parentId+'" level="'+item.level+'" type="'+item.type;
     if(item.type==1){
     html+='" isOpen="0" class="evaluate-tr evaluate-tr-bl">';
     html+='<td width="475px;">';
     html+='<em class="em-jia"></em>';
     }else{
     html+='" class="evaluate-trr evaluate-trr-bl">';
     html+='<td width="475px;">';
     }
     html+='<input readonly="readonly"  placeholder="'+item.name+'"/>';
     html+='</td>';
     html+='<td>';
     html+='<div class="gradecon" id="Addnewskill_119"><ul class="rev_pro clearfix">';
     html+='<li>';
     html+='<input class="fl" type="hidden" style="margin-top:2px;" name="InterviewCommentInfoSub[1]" value="0" />';
     html+=' <div class="revinp">';
     html+='            <span class="level">';
     html+='                <i class="level_hollow" cjmark=""></i>';
     html+='                <i class="level_hollow" cjmark=""></i>';
     html+='                <i class="level_hollow" cjmark=""></i>';
     html+='                <i class="level_hollow" cjmark=""></i>';
     html+='                 <i class="level_hollow" cjmark=""></i>';
     html+='             </span>';
     html+='            <!-- <span style="display: none" class="revgrade">未评分</span>-->';
     html+='             <span class="gradcon-c">重新打分</span>';
     html+='             </div>';
     html+='         </li>';
     html+='     </ul>';
     html+='     </div>';
     html+='</td>';
     html+='</tr>';
     });
     $("tr[zhiBiaoId='"+indiData.parentId+"']").after(html);
     $("tr[zhiBiaoId='"+indiData.parentId+"']").attr("isOpen",1);
     }
     }
     });
     };*/

    module.exports=evaluate;
});