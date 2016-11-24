/**
 * Created by guojing on 2015/11/13.
 */
/* global Config */
define('editEducation',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var editEducation = {},
        Common = require('common');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * editEducation.init()
     */
    editEducation.init = function(){

        $('#province').change(function(){
            selParam.parentId = $('#province').val();
            selParam.province=$("#province").val();
            selParam.city=$("#city").val();
            selParam.schoolName="";
            //selParam.county=$("#county").val();
            var childId="city";
            editEducation.getChangeRegion(childId);
        });
        $('#city').change(function(){
            /*selParam.parentId = $('#city').val();
             var childId="county";
             addEducation.getChangeRegion(childId);*/
            selParam.schoolName="";
            selParam.province=$("#province").val();
            selParam.city=$("#city").val();
            //selParam.county=$("#county").val();
            editEducation.getSchoolList();
        });
        $('.search-school').click(function(){
            selParam.province="";
            selParam.city="";
            selParam.schoolName=$("#schoolName").val();
            //selParam.county=$("#county").val();
            editEducation.getSchoolList();
        });

        $('.search-user').click(function(){
            selParam.userName=$("#userName").val();
            editEducation.getUserList();
        });
        /*$('#county').change(function(){
            editEducation.getSchoolList();
        });*/
        editEducation.loadData();

        $(".addS").click(function(event) {
            editEducation.addSchool();

        });

        $(".addAllS").click(function(event) {
            editEducation.addAllSchool();

        });

        $(".delS").click(function(event) {
            editEducation.delSchool();

        });

        $(".delAllS").click(function(event) {
            editEducation.delAllSchool();
        });

        $(".addU").click(function(event) {
            editEducation.addUser();

        });

        $(".addAllU").click(function(event) {
            editEducation.addAllUser();

        });

        $(".delU").click(function(event) {
            editEducation.delUser();

        });

        $(".delAllU").click(function(event) {
            editEducation.delAllUser();
        });

        $(".cancel").click(function(event) {
            var educationLogo = $('#educationLogo').val();
            var oldEduLogo = $('#oldEduLogo').val();
            if(educationLogo!=oldEduLogo){
                var param={};
                param.oldEduLogo=educationLogo;
                Common.getData('/education/delOldEduLogo.do',param,function(rep){
                });
            }
            window.location.href = "/education/eduManageList.do";
        });

        $(".save").click(function(event) {
            editEducation.saveEdu();
        });
    };
    editEducation.loadData = function (){
        selParam.province=$("#province").val();
        selParam.city=$("#city").val();
        editEducation.getSchoolList();
        //附件上传
        editEducation.eduLogoFileUpload();

        editEducation.getUserList();
    }


    editEducation.saveEdu = function(){
        var eduName=$("#eduName").val();
        if(eduName!="") {
            selParam.educationName = eduName;
            selParam.id = $('#id').val();
            Common.getData('/education/educationNameIsExist.do', selParam, function (rep) {
                if (!rep.isExist) {
                    editEducation.arrToStrSave("user");
                    editEducation.arrToStrSave("school");
                    selParam.province = $('#province').val();
                    selParam.city = $('#city').val();
                    selParam.educationLogo = $('#educationLogo').val();
                    selParam.userIdsStr = $('#userIdsStr').val();
                    selParam.schoolIdsStr = $('#schoolIdsStr').val();
                    Common.getData('/education/editEducation.do', selParam, function (re) {
                        if (re.resultCode == '0') {
                            alert("修改成功!");
                            var educationLogo = $('#educationLogo').val();
                            var oldEduLogo = $('#oldEduLogo').val();
                            if(educationLogo!=oldEduLogo){
                                var param={};
                                param.oldEduLogo=oldEduLogo;
                                Common.getData('/education/delOldEduLogo.do',param,function(rep){

                                });
                            }
                            window.location.href = "/education/eduManageList.do";
                        }
                    });
                }
            });
        } else{
            alert("教育局名称不能为空！");
        }
    }

    editEducation.addUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        var realvalues = [];
        $('#sourceU :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });
        editEducation.handleSelData(source,target,realvalues,"add");
    }

    editEducation.addAllUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        editEducation.handleSelAllData(source,target,"add");
    }

    editEducation.delUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        var realvalues = [];
        $('#targetU :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });

        editEducation.handleSelData(target,source,realvalues,"del");
    }

    editEducation.delAllUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        editEducation.handleSelAllData(target,source,"del");
    }

    editEducation.addSchool = function(){
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        var realvalues = [];
        $('#sourceS :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });
        editEducation.handleSelData(source,target,realvalues,"add");
    }

    editEducation.delSchool = function(){
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        var realvalues = [];
        $('#targetS :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });

        editEducation.handleSelData(target,source,realvalues,"del");
    }

    editEducation.handleSelData = function (source, target, realvalues, type){
        var sourceLength=source.length;
        //所选择对象的索引
        var selectedItem = source.selectedIndex;
        if(selectedItem>=0) //如果选择对象的索引大于0
        {
            for (var i = sourceLength-1; i>=0; i--)
            {
                for (var x = 0; x < realvalues.length; x++)
                {
                    if (source.options[i].value == realvalues[x])
                    {
                        var selectedText = source.options[i].text; //所选择对象的索引的名称
                        var selectedValue = source.options[i].value; //所选择对象的索引的值
                        source.options[i].remove() ;
                        realvalues.splice(x,1);
                        //源对象所有的记录数
                        var targetLength=target.length;
                        //增加源框中的数据
                        var newoption = new Option(selectedText, selectedValue, false, false);
                        target.options[targetLength] = newoption;
                    }
                }
            }
        }
        else
        {
            if(type=="add"){
                alert("请选定栏目再添加！");
            }
            if(type=="del"){
                alert("请选定栏目再删除！");
            }

        }
    }

    editEducation.addAllSchool = function()  //全部增加
    {
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        editEducation.handleSelAllData(source,target,"add");
    }


    editEducation.delAllSchool = function(){//全部删除
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        editEducation.handleSelAllData(target,source,"del");
    }

    editEducation.handleSelAllData = function (source, target, type){
        //目标对象所有的记录数
        var sourceLength = source.length-1;
        if(sourceLength>=0) //如果选择对象的索引大于0
        {
            for (var i = sourceLength; i >=0; i--)
            {
                var selectedText = source.options[i].text; //所选择对象的索引的名称
                var selectedValue = source.options[i].value; //所选择对象的索引的值
                source.options[i].remove() ;
                //源对象所有的记录数
                var targetLength=target.length;
                //增加源框中的数据
                var newoption = new Option(selectedText, selectedValue, false, false);
                target.options[targetLength] = newoption;
            }
        }
        else
        {
            if(type=="add"){
                alert("已经没有可增加对象！");
            }
            if(type=="del"){
                alert("已经没有可移除对象！");
            }

        }
    }


    editEducation.arrToStrSave = function (name){
        var sobj;
        var tobj;
        var realvalues = [];
        if(name=="user"){
            sobj=$("#targetU")[0];
            tobj=$("#userIdsStr");
        }
        if(name=="school"){
            sobj=$("#targetS")[0];
            tobj=$("#schoolIdsStr");
        }

        var length = sobj.length-1;
        if(length>=0) //如果选择对象的索引大于0
        {
            for (var i = length; i >=0; i--)
            {
                var value = sobj.options[i].value; //所选择对象的索引的值
                realvalues.push(value);
            }
        }
        tobj.val(realvalues.join(","));
    }

    editEducation.getChangeRegion = function(childId){
        if(selParam.parentId!=""){
            Common.getData('/education/getRegionList.do',selParam,function(rep){
                var list = rep.regions;
                var html='<option value="">请选择</option>';
                $.each(list,function(i,item){
                    html+='<option value="'+item.id+'">'+item.name+'</option>';
                });
                $("#"+childId).html(html);
                editEducation.getSchoolList();
            });
        }else{
            var html='<option value="">请选择</option>';
            $("#"+childId).html(html);
            editEducation.getSchoolList();
        }
    }

    editEducation.getSchoolList = function(){
        selParam.schoolIdsStr=$('#schoolIdsStr').val();
        Common.getData('/education/getSchoolList.do',selParam,function(rep){
            var list = rep;
            var html='';
            $.each(list,function(i,item){
                html+='<option value="'+item.schoolId+'">'+item.schoolName+'</option>';
            });
            $("#sourceS").html(html);
        });
    }

    editEducation.getUserList = function(){
        selParam.userIdsStr=$('#userIdsStr').val();
        Common.getData('/education/getEduUserList.do',selParam,function(rep){
            var list = rep;
            var html='';
            $.each(list,function(i,item){
                html+='<option value="'+item.id+'">'+item.name+'</option>';
            });
            $("#sourceU").html(html);
        });
    }

    editEducation.eduLogoFileUpload = function(){
        //点击附件按钮
        $('#eduLogoPic').click(function(event){
            Common.fileUpload('#eduLogoPic','/education/addEduLogoPic.do','#picuploadLoading',function(e,response){
                var result = response.result;
                var rdata = typeof  result == 'string' ? $.parseJSON(result) : result[0] ? $.parseJSON(result[0].documentElement.innerText) : result;
                if (rdata.result) {
                    var url = rdata.path[0];
                    var html='<img src="' + url + '" style="width: 170px;height:35px;">';
                    $('#logoImg').html(html);
                    var educationLogo = $('#educationLogo').val();
                    var oldEduLogo = $('#oldEduLogo').val();
                    if(educationLogo!=oldEduLogo){
                        var param={};
                        param.oldEduLogo=educationLogo;
                        Common.getData('/education/delOldEduLogo.do',param,function(rep){
                        });
                    }
                    $("#educationLogo").val(url);
                }
            });
        });
    }

    module.exports=editEducation;
});

