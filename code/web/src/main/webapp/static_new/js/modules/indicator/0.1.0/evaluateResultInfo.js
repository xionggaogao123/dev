/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define('evaluateRInfo',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var evaluateRInfo = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    var selStuParam={};

    evaluateRInfo.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        evaluateRInfo.getInterestClassList();
        //设置初始页码
        selStuParam.page = 1;
        //设置每页数据长度
        selStuParam.pageSize = 20;
        var classObj=$(".evaluate-tab .li-cur");
        $("#classId").val(classObj.attr("claId"));
        evaluateRInfo.getStudentList();
        $('body').on('click', '.evaluate-tab li', function(){
            $(this).addClass("li-cur").siblings().removeClass("li-cur");
            $("#classId").val($(this).attr("claId"));
            evaluateRInfo.getStudentList();
        });

        $("#searchBtn").click(function(){
            //设置初始页码
            selStuParam.page = 1;
            evaluateRInfo.getStudentList();
        });

        $('body').on('click','.evaluate-name',function() {
            var id=$(this).attr("id");
            window.open("/evaluate/studentEvaluateDetail.do?id="+id);
            //Common.goTo();
        });

        $('body').on('click','.go-back',function() {
            Common.goTo("/evaluate/evaluateResult.do?index=6&version=5");
        });
    };

    evaluateRInfo.getInterestClassList = function(){
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

    var option={};
    /**
     * 查询学生信息列表
     */
    evaluateRInfo.getStudentList = function(){
        selStuParam.appliedId=$("#appliedId").val();
        selStuParam.activityId=$("#classId").val();
        if(selStuParam.activityId==""){
            alert("请选择兴趣班!");
            return;
        }
        selStuParam.name=$.trim($('#name').val())
        selStuParam.termType=$("#termType").val();
        selStuParam.stuState = $(".evaluate-pro-top").find(".pro-cur").val();
        Common.getPostData('/evaluate/getStudentResultList.do', selStuParam, function(rep){
            if(rep.code=200){
                var data = rep.message;
                if(selStuParam.name!=""&&data.total==0){
                    alert("用户信息没有查到!");
                }else{
                    $('#studentList').html("");
                    Common.render({tmpl: $("#j-tmpl2"), data: data, context: '#studentList'});
                    option.total= data.total;
                    option.pagesize= data.pageSize;
                    option.currentpage=data.page;
                    option.operate=function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).click(function(){
                                if(selStuParam.page!=$(this).text()){
                                    selStuParam.page=$(this).text();
                                    evaluateRInfo.getStudentList();
                                }
                            });
                        });
                        $('.first-page').click(function(){
                            if(selStuParam.page!=1) {
                                selStuParam.page = 1;
                                evaluateRInfo.getStudentList();
                            }
                        });
                        $('.last-page').click(function(){
                            if(selStuParam.page!=totalPage) {
                                selStuParam.page = totalPage;
                                evaluateRInfo.getStudentList();
                            }
                        })
                    };
                    Paginator.initPaginator(option);
                }
            }
        });
    };

    module.exports=evaluateRInfo;
});