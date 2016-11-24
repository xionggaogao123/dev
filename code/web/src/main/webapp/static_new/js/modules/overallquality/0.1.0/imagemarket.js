/**
 * Created by guojing on 2016/8/25.
 */
define('imagemarket',['jquery','doT','easing','common','imageUpload'],function(require,exports,module){
    /**
     *初始化参数
     */
    var imagemarket = {},
        Common = require('common'),
        imageUpload = require('imageUpload');

    var searchParam={};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * imagemarket.init()
     */
    imagemarket.init = function(){


        $(".xxcs-tab li").click(function(){
            $(".xxcs-tab li").removeClass("yx-active");
            $(this).addClass("yx-active");
            $(".xxcs-content>div").hide();
            var name = $(this).attr("id");
            if(name=="XXXCS"){
                $(".add-goods").show();
                imagemarket.searchOverallQualityGoodsList();
            }else{
                $(".add-goods").hide();
            }
            if(name=="CSGL"){
                $("#selGrade").get(0).selectedIndex = 0;
                $("#checkAll").removeAttr("checked");
                imagemarket.changeGrade();
                imagemarket.checkedAll();
            }
            if(name=="DHJL"){
                imagemarket.changeGoodsHistory();
            }
            $("#" + "tab-" + name).show();
        });


        $(".add-goods").click(function(){
            $("#goodsTitle").text("添加");
            $(".bg").show();
            $(".new-goods-add").show();
        });
        $(".goods-close,.goods-btn-qx").click(function(){
            $("#goodsId").val("");
            $('#picPath').val("");
            $("#qnKey").val("");
            $('#fileList').html("");
            $("#goodsName").val("");
            $("#goodsPrice").val("");
            $("#goodsStock").val("");
            $(".bg").hide();
            $(".new-goods-add").hide();
        });
        $(".goods-btn-sure").click(function(){
            imagemarket.addGoods();
        });
        $('#goodsPrice').keyup(function(event){
            imagemarket.goodsPriceOrStockIsNumber($(this),"商品价格");
        });
        $('#goodsStock').keyup(function(event){
            imagemarket.goodsPriceOrStockIsNumber($(this),"库存量");
        });

        imagemarket.searchOverallQualityGoodsList();

        $("#selGrade").change(function(){
            imagemarket.changeGrade();
        });

        $("#selClass").change(function(){
            imagemarket.searchStuChangeGoodsList();
        });

        $(".pl-agree").click(function(){
            var ids="";
            $("input[name='changeCheck']").each(function () {
                if($(this).is(":checked")) {
                    if(ids==""){
                        ids+=$(this).parent().parent().parent().attr("id");
                    }else{
                        ids+=","+$(this).parent().parent().parent().attr("id");
                    }
                }
            });
            if(ids==""){
                alert("请至少选择一条要同意信息！");
                return;
            }
            imagemarket.batchChangeSure(ids);
        });

        $(".pl-refuse").click(function(){
            var ids="";
            $("input[name='changeCheck']").each(function () {
                if($(this).is(":checked")) {
                    if(ids==""){
                        ids+=$(this).parent().parent().parent().attr("id");
                    }else{
                        ids+=","+$(this).parent().parent().parent().attr("id");
                    }
                }
            });
            if(ids==""){
                alert("请至少选择一条要拒绝信息！");
                return;
            }
            $("#refuseChangeIds").val(ids);
            $(".bg").show();
            $(".refuse-alert").show();
        });

        $(".alert-close,.alert-btn-qx").click(function(){
            $(".bg").hide();
            $(".refuse-alert").hide();
            $("#refuseChangeIds").val("");
            $(".refuse-con").val("");
        });

        $(".alert-btn-sure").click(function(){
            imagemarket.batchChangeRefuse();
        });

        //图片上传
        imageUpload(
            function(successResponse){
                if("200" == successResponse.code){
                    $("#picPath").val(successResponse.picPath);
                    $("#qnKey").val(successResponse.qnKey);
                }
            },
            function(errorResponse){
                if("500" == successResponse.code){
                    alert(errorResponse.data);
                }
            }
        );
    }

    /**
     * 校验成绩是数字
     */
    imagemarket.goodsPriceOrStockIsNumber=function(obj,flag){
        var score=obj.val();
        obj.val(score.replace(".",""));
        var reg = new RegExp("^[0-9]*$");
        if(!reg.test(score.replace(".","").replace(/,/g,""))){
            alert(flag+"请输入正整数数!");
            obj.val('');
            return false;
        }
        return true;
    }

    imagemarket.addGoods = function(){
        var addParam = {};
        addParam.id = $("#goodsId").val();
        addParam.picPath = $('#picPath').val();
        addParam.qnKey=$("#qnKey").val();
        addParam.goodsName = $("#goodsName").val();
        addParam.goodsPrice = $("#goodsPrice").val();
        addParam.goodsStock = $("#goodsStock").val();
        if(addParam.goodsName==""&&$.trim(addParam.goodsName)==""){
            alert("请输入商品名称!");
            return;
        }
        if( addParam.picPath == ''){
            alert("请上传商品图片!");
            return;
        }
        if(addParam.goodsPrice==""){
            alert("请输入商品价格!");
            return;
        }
        if(!imagemarket.goodsPriceOrStockIsNumber($("#goodsPrice"),"商品价格")){
            return;
        }
        if(addParam.goodsStock==""){
            alert("请输入库存量!");
            return;
        }
        if(!imagemarket.goodsPriceOrStockIsNumber($("#goodsStock"),"库存量")){
            return;
        }
        Common.getPostData('/qualityitem/addOrEditGoods.do', addParam, function(rep){
            if(rep.result==true) {
                alert("保存成功!");
                $("#goodsId").val("");
                $('#picPath').val("");
                $("#qnKey").val("");
                $('#fileList').html("");
                $("#goodsName").val("");
                $("#goodsPrice").val("");
                $("#goodsStock").val("");
                imagemarket.searchOverallQualityGoodsList();
                $(".bg").hide();
                $(".new-goods-add").hide();
            }
        });
    }
    imagemarket.searchOverallQualityGoodsList = function () {
        var selParam={};
        Common.getData("/qualityitem/searchOverallQualityGoodsList.do", selParam,function(rep){
            $('#goodsShow').html("");
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '#goodsShow'});
            if(rep.isStudent==true){
                $(".change").click(function(){
                    $(this).parent().prop("class", "yx-nodh");
                    $(this).unbind("click");
                    imagemarket.studentChangeGoods($(this).parent().parent().attr("id"));
                });
            }else{
                $(".edit").click(function(){
                    imagemarket.editOverallQualityGoods($(this).parent().parent().attr("id"));
                });
                $(".del").click(function(){
                    imagemarket.delOverallQualityGoods($(this).parent().parent().attr("id"));
                });
            }
        });
    }

    imagemarket.studentChangeGoods =function(id){
        var seaParam={};
        seaParam.id=id;
        Common.getData("/qualityitem/studentChangeGoods.do", seaParam, function(rep){
            alert(rep.result);
            if(rep.flag){
                $("#totalCount").text(rep.totalCount);
            }
            imagemarket.searchOverallQualityGoodsList();
        });
    }

    imagemarket.editOverallQualityGoods = function (id) {
        var editParam={};
        editParam.id=id;
        Common.getData("/qualityitem/getOverallQualityGoods.do", editParam, function(rep){
            $("#goodsTitle").text("编辑");
            var obj=rep.dto;
            $("#goodsId").val(obj.goodsId);
            $('#picPath').val(obj.picPath);
            $("#qnKey").val(obj.qnKey);
            $('#fileList').html(
                '<div class="file-item thumbnail">' +
                '<img src="'+obj.picPath+'">' +
                '</div>'
            );
            $("#goodsName").val(obj.goodsName);
            $("#goodsPrice").val(obj.goodsPrice);
            $("#goodsStock").val(obj.goodsStock);
            $(".bg").show();
            $(".new-goods-add").show();
        });
    }

    imagemarket.delOverallQualityGoods = function (id) {
        if (confirm('确认删除此条项目信息！')){
            var delParam={};
            delParam.id=id;
            Common.getData("/qualityitem/delOverallQualityGoods.do", delParam, function(rep){
                if(rep.result==true){
                    alert("删除成功!");
                    imagemarket.searchOverallQualityGoodsList();
                }
            });
        }
    }

    imagemarket.changeGrade = function(){
        var selData = {};
        selData.gradeId=$("#selGrade").val();
        Common.getPostData('/qualityitem/getGradeClassValue.do', selData,function(rep){
            var list = rep.classList;
            var html='';
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.className+'</option>';
            });
            $("#selClass").html(html);
            imagemarket.searchStuChangeGoodsList();
        });
    }

    imagemarket.searchStuChangeGoodsList = function(){
        $("#checkAll").removeAttr("checked");
        var selParam = {};
        selParam.gradeId=$("#selGrade").val();
        selParam.classId=$("#selClass").val()||"";
        Common.getPostData('/qualityitem/searchStuChangeGoodsList.do', selParam,function(rep){
            $('#changeGoods').html("");
            if(rep.classId!="") {
                Common.render({tmpl: $('#j-tmpl2'), data: rep, context: '#changeGoods'});
                $(".yx-refuse").click(function () {
                    $("#refuseChangeIds").val($(this).parent().parent().attr("id"));
                    $(".bg").show();
                    $(".refuse-alert").show();
                });
                $(".yx-agree").click(function () {
                    var ids = $(this).parent().parent().attr("id");
                    imagemarket.batchChangeSure(ids);
                });

                $("input[name='changeCheck']").click(function () {
                    if(!$(this).is(":checked")) {
                        $("#checkAll").removeAttr("checked");
                    }
                });
            }
        });
    }

    imagemarket.checkedAll = function() {
        $("#checkAll").removeAttr("checked");
        $('#checkAll').click(function(event) {
            var checkAll = $("#checkAll").is(':checked');
            $("input[name='changeCheck']").each(function () {
                var checked=$(this).is(":checked");
                if(!checkAll){
                    if(checked) {
                        $(this).removeAttr("checked");
                    }
                }
                if(checkAll){
                    if (!checked) {
                        $(this).prop("checked", "checked");
                    }
                }
            });
        });
    }

    imagemarket.batchChangeRefuse = function(){

        var opData={};
        opData.ids = $("#refuseChangeIds").val();
        opData.refuseCon =$(".refuse-con").val();
        opData.operate = "refuse";
        Common.getData('/qualityitem/batchHandleChangeGoods.do', opData,function(rep){
            $(".bg").hide();
            $(".refuse-alert").hide();
            $(".refuse-con").val("");
            if(rep.result=="true") {
                var arr = opData.ids.split(',');
                for (var i in arr) {
                    $("#" + arr[i]).first().find("input").remove();
                    $("#" + arr[i]).find("td:last").html('<a href="javascript:;" class="yx-refused">已拒绝</a>');
                }
            }
        });
    }

    imagemarket.batchChangeSure = function(ids){
        var opData={};
        opData.ids = ids;
        opData.refuseCon = "";
        opData.operate = "sure";
        Common.getData('/qualityitem/batchHandleChangeGoods.do', opData,function(rep){
            if(rep.result=="true"){
                var arr = ids.split(',');
                for(var i in arr){
                    $("#"+arr[i]).first().find("input").remove();
                    $("#"+arr[i]).find("td:last").html('<a href="javascript:;" class="yx-agreed">已同意</a>');
                }
            }
            if(rep.result=="false") {
                alert(rep.mgs);
            }
        });
    }

    imagemarket.changeGoodsHistory = function () {
        var selParam={};
        Common.getPostData('/qualityitem/changeGoodsHistory.do', selParam,function(rep){
            $('#goods-history').html("");
            Common.render({tmpl: $('#j-tmpl3'), data: rep, context: '#goods-history'});
        });
    }
    module.exports=imagemarket;
});