/**
 * Created by guojing on 2016/11/14.
 */
define('evaluateManage',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var evaluateManage = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    var evaluateData = {};
    evaluateManage.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        //设置初始页码
        /*if(null != sessionStorage.getItem("evaluatePage")){
            evaluateData.page = sessionStorage.getItem("evaluatePage")||1;
        }else{
            evaluateData.page = 1;
        }
        if(null != sessionStorage.getItem("evaluateName")){
            evaluateData.name = sessionStorage.getItem("evaluateName")||"";
        }else{
            evaluateData.name = "";
        }*/
        evaluateData.page = 1;
        //设置每页数据长度
        evaluateData.pageSize = 12;
        evaluateManage.searchiTreeAppliedData();
        $("#searchBtn").click(function(){
            //设置初始页码
            evaluateData.page = 1;
            evaluateData.name=$.trim($('#name').val());
            evaluateManage.searchiTreeAppliedData();
        });

        $(".new-add").click(function(){
            evaluateManage.searchiTreeData();
            evaluateManage.getGradeAndCategory();
            evaluateManage.getClassInfo();
            $(".bg").show();
            $(".newadd-alert").show();
        });

        $(".alert-next-btn").click(function(){
            var appliedName = $("#appliedName").val();
            if($.trim(appliedName)==""){
                alert("请输入评价名称!");
                return;
            }
            var finishTime = $("#finishTime").val();
            if($.trim(finishTime)==""){
                alert("请输入完成时间!");
                return;
            }
            var treeId = $(".itree-data .alert-tabb-hov").attr("tid")||"";
            if(treeId==""){
                alert("请选择指标体系!");
                return;
            }
            $(".newadd-alert").hide();
            $(".newadd-alertt").show();
        });

        $(".alert-prev-btn").click(function(){
            $(".newadd-alertt").hide();
            $(".newadd-alert").show();
        });

        $(".alert-close").click(function(){
            $("#appliedId").val("");
            $("#appliedName").val("");
            $("#finishTime").val("");
            $("#appliedDescribe").val("");
            $("input[name='iTreeType']").prop("checked","checked");
            $("#evaluatedGroupIds").val("");
            $(".bg").hide();
            $(".newadd-alert").hide();
            $(".newadd-alertt").hide();
        });

        $(".alert-btn-sure").click(function(){
            var evaluatedGroupIdsStr="";
            $(".newadd-alertt-lii div").each(function (i, item) {
                if($(item).hasClass("newadd-alert-img")){
                    if(evaluatedGroupIdsStr==""){
                        evaluatedGroupIdsStr=$(item).attr("cid");
                    }else{
                        evaluatedGroupIdsStr+=","+$(item).attr("cid");
                    }
                }
            });
            if(evaluatedGroupIdsStr==""){
                alert("请选择评价对象!");
                return;
            }
            $("#evaluatedGroupIds").val(evaluatedGroupIdsStr);
            evaluateManage.addITreeApplied();
        });

        $('body').on('click','.newadd-alertt-lii div',function() {
            $(this).toggleClass('newadd-alert-img')
        });

        $('body').on('click','.evaluate-name',function() {
            var id=$(this).parent().attr("id");
            Common.goTo("/evaluate/gotoInterestEvaluate.do?id="+id);
        });

        $('body').on('click','.evaluate-edit',function() {
            var id=$(this).parent().parent().attr("id");
            evaluateManage.getITreeApplied(id);
            $(".bg").show();
            $(".edit-alert").show();
        });

        $(".edit-btn-qx,.edit-alert-close").click(function(){
            $("#eAppliedId").val("");
            $("#eAppliedName").val("");
            $("#eFinishTime").val("");
            $("#eAppliedDescribe").val("");
            $(".bg").hide();
            $(".edit-alert").hide();
        });

        $(".edit-btn-sure").click(function(){
            evaluateManage.editITreeApplied();
        });

        $('body').on('click','.evaluate-del',function() {
            var id=$(this).parent().parent().attr("id");
            evaluateManage.delITreeApplied(id);
        });

        $('body').on('click','.itreeInfo',function(){
            $(this).addClass('alert-tabb-hov').siblings().removeClass('alert-tabb-hov');
        });

        $("input[name='iTreeType']").click(function () {
            evaluateManage.searchiTreeData();
        });


        $('body').on('change', '#term, #grade, #category', function(){
            evaluateManage.getClassInfo();
        });

    };

    evaluateManage.editITreeApplied = function(){
        var editParam={};
        editParam.id=$("#eAppliedId").val();
        editParam.name=$("#eAppliedName").val();
        if($.trim(editParam.name)==""){
            alert("请输入评价名称!");
            return;
        }
        editParam.finishTime=$("#eFinishTime").val();
        if($.trim(editParam.finishTime)==""){
            alert("请输入完成时间!");
            return;
        }
        editParam.describe = $("#eAppliedDescribe").val();
        Common.getPostBodyData('/evaluate/editITreeApplied.do', editParam, function(rep){
            if(rep.code==200){
                $("#eAppliedId").val("");
                $("#eAppliedName").val("");
                $("#eFinishTime").val("");
                $("#eAppliedDescribe").val("");
                alert("修改成功!");
                evaluateManage.searchiTreeAppliedData();
                $(".bg").hide();
                $(".edit-alert").hide();
            }
        });
    };


    evaluateManage.getITreeApplied = function(id){
        var selParam={};
        selParam.id=id;
        Common.getPostData('/evaluate/getITreeApplied.do', selParam, function (rep) {
            if (rep.code == 200) {
                var dto = rep.message;
                $("#eAppliedId").val(dto.id);
                $("#eAppliedName").val(dto.name);
                $("#eFinishTime").val(dto.finishTime);
                $("#eAppliedDescribe").val(dto.describe);
            } else {
                alert(rep.message);
            }
        });
    };

    evaluateManage.delITreeApplied = function(id){
        var delParam={};
        delParam.id=id;
        if(confirm("您确定删除该评价信息吗?")) {
            Common.getPostData('/evaluate/delITreeApplied.do', delParam, function (rep) {
                if (rep.code == 200) {
                    alert(rep.message);
                    evaluateManage.searchiTreeAppliedData();
                } else {
                    alert(rep.message);
                }
            });
        }
    };

    evaluateManage.getGradeAndCategory = function(){
        Common.getData('/myclass/getGradeAndCategory.do',{},function(data){
            var gradeList = data.gradeList;
            var ghtml = '<option value="">全部年级</option>';
            for(var i=0; i<gradeList.length; i++){
                ghtml += '<option value="'+gradeList[i].id+'">'+gradeList[i].name+'</option>';
            }
            $('#grade').html(ghtml);

            var categoryList = data.categoryList;
            var chtml = '<option value="allCategory">全部分类</option>';
            for(var i=0; i<categoryList.length; i++){
                chtml += '<option value="'+categoryList[i].id+'">'+categoryList[i].name+'</option>';
            }
            chtml += '<option value="">未分类</option>';
            $('#category').html(chtml);

            var termList = data.termList;
            var thtml = '';
            for(var i=termList.length-1; i>=0; i--){
                thtml += '<option value="'+termList[i].value+'">'+termList[i].name+'</option>';
            }
            $('#term').html(thtml);

            if(data.role){
                $('#grade').show();
                $('#category').show();
            } else if(data.isManager){
                $('#grade').remove();
                $('#category').remove();
            } else {
                $('#grade').remove();
                $('#category').remove();
            }
        });
    };

    evaluateManage.getClassInfo = function(){
        var selParam={};
        selParam.term=null;
        selParam.gradeId = $('#grade').find('option:selected').val();
        selParam.categoryId = $('#category').find('option:selected').val();
        selParam.termType = $('#term').val();
        Common.getData('/myclass/interestclass.do',selParam,function(rep){
            $('.newadd-alertt-lii').html('');
            var data=rep.rows;
            Common.render({tmpl: $('#j-tmpl3'), data: data, context: '.newadd-alertt-lii'});
        });
    };

    evaluateManage.searchiTreeData = function(){
        var iTreeData = {};
        var owner=0;
        $("input[name='iTreeType']").each(function () {
            if($(this).is(":checked")) {
                owner+=$(this).val();
            }
        });
        if(owner>=3){
            owner=0;
        }
        iTreeData.owner=owner;
        Common.getData('/indicator/getITreeList.do',iTreeData,function(rep){
            $('.itree-data').html('');
            var data=rep.message;
            Common.render({tmpl: $('#j-tmpl2'), data: data, context: '.itree-data'});
        });
    };

    evaluateManage.addITreeApplied = function(){
        var addParam={};
        addParam.name=$("#appliedName").val();
        if($.trim(addParam.name)==""){
            alert("请输入评价名称!");
            return;
        }
        addParam.treeId = $(".itree-data .alert-tabb-hov").attr("tid");
        addParam.termType = $('#term').val();
        addParam.evaluatedGroupIdsStr=$("#evaluatedGroupIds").val();
        addParam.finishTime=$("#finishTime").val();
        if($.trim(addParam.finishTime)==""){
            alert("请输入完成时间!");
            return;
        }
        addParam.describe = $("#appliedDescribe").val();
        Common.getPostBodyData('/evaluate/addITreeApplied.do', addParam, function(rep){
            if(rep.code==200){
                $("#appliedId").val("");
                $("#appliedName").val("");
                $("#finishTime").val("");
                $("#appliedDescribe").val("");
                $("input[name='iTreeType']").prop("checked","checked");
                $("#evaluatedGroupIds").val("");
                alert(rep.message.msg);
                evaluateData.page = 1;
                evaluateData.name="";
                var id=rep.message.id;
                Common.goTo("/evaluate/gotoInterestEvaluate.do?id="+id);
                $(".bg").hide();
                $(".newadd-alertt").hide();
            }
        });
    };

    var option={};
    //查询
    evaluateManage.searchiTreeAppliedData = function(){
        /*sessionStorage.setItem("evaluatePage", evaluateData.page);
        sessionStorage.setItem("evaluateName", evaluateData.name);*/
        Common.getData('/evaluate/getITreeAppliedList.do',evaluateData,function(rep){
            var data=rep.message;
            $('.evaluate-data').html('');
            if(data.rows.length==0){
                evaluateData.page=1;
                if(data.total>0) {
                    evaluateManage.searchiTreeAppliedData();
                }
            }else{
                Common.render({tmpl: $('#j-tmpl'), data: data, context: '.evaluate-data'});
                option.total= data.total;
                option.pagesize= data.pageSize;
                option.currentpage=data.page;
                option.operate=function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function(){
                            if(evaluateData.page!=$(this).text()){
                                evaluateData.page=$(this).text();
                                evaluateManage.searchiTreeAppliedData();
                            }
                        });
                    });
                    $('.first-page').click(function(){
                        if(evaluateData.page!=1) {
                            evaluateData.page = 1;
                            evaluateManage.searchiTreeAppliedData();
                        }
                    });
                    $('.last-page').click(function(){
                        if(evaluateData.page!=totalPage) {
                            evaluateData.page = totalPage;
                            evaluateManage.searchiTreeAppliedData();
                        }
                    })
                };
                Paginator.initPaginator(option);
            }
        });
    };

    module.exports=evaluateManage;
});