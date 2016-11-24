/**
 * Created by guojing on 2015/11/13.
 */
/* global Config */
define('addEducation',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addEducation = {},
        Common = require('common');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * addEducation.init()
     */
    addEducation.init = function(){

        $('#province').change(function(){
            selParam.parentId = $('#province').val();
            selParam.province=$("#province").val();
            selParam.city=$("#city").val();
            selParam.schoolName="";
            //selParam.county=$("#county").val();
            var childId="city";
            addEducation.getChangeRegion(childId);
        });
        $('#city').change(function(){
            /*selParam.parentId = $('#city').val();
            var childId="county";
            addEducation.getChangeRegion(childId);*/
            selParam.schoolName="";
            selParam.province=$("#province").val();
            selParam.city=$("#city").val();
            //selParam.county=$("#county").val();
            addEducation.getSchoolList();
        });
        $('.search-school').click(function(){
            selParam.province="";
            selParam.city="";
            selParam.schoolName=$("#schoolName").val();
            //selParam.county=$("#county").val();
            addEducation.getSchoolList();
        });

        $('.search-user').click(function(){
            selParam.userName=$("#userName").val();
            addEducation.getUserList();
        });
        /*$('#county').change(function(){
            addEducation.getSchoolList();
        });*/

        addEducation.getSchoolList();

        //附件上传
        addEducation.eduLogoFileUpload();

        $(".addS").click(function(event) {
            addEducation.addSchool();

        });

        $(".addAllS").click(function(event) {
            addEducation.addAllSchool();

        });

        $(".delS").click(function(event) {
            addEducation.delSchool();

        });

        $(".delAllS").click(function(event) {
            addEducation.delAllSchool();
        });

        addEducation.getUserList();

        $(".addU").click(function(event) {
            addEducation.addUser();

        });

        $(".addAllU").click(function(event) {
            addEducation.addAllUser();

        });

        $(".delU").click(function(event) {
            addEducation.delUser();

        });

        $(".delAllU").click(function(event) {
            addEducation.delAllUser();
        });

        $(".cancel").click(function(event) {
            var educationLogo = $('#educationLogo').val();
            if(educationLogo!=""){
                var param={};
                param.oldEduLogo=educationLogo;
                Common.getData('/education/delOldEduLogo.do',param,function(rep){
                });
            }
            window.location.href = "/education/eduManageList.do";
        });

        $(".save").click(function(event) {
            addEducation.saveEdu();
        });
    };

    addEducation.saveEdu = function(){
        var eduName=$("#eduName").val();
        if(eduName!="") {
            selParam.educationName = eduName;
            Common.getData('/education/educationNameIsExist.do', selParam, function (rep) {
                if (!rep.isExist) {
                    selParam.province = $('#province').val();
                    selParam.city = $('#city').val();
                    selParam.educationLogo = $('#educationLogo').val();
                    selParam.userIdsStr = $('#userIdsStr').val();
                    selParam.schoolIdsStr = $('#schoolIdsStr').val();
                    Common.getData('/education/addEducation.do', selParam, function (re) {
                        if (re.resultCode == '0') {
                            alert("添加成功!");
                            window.location.href = "/education/eduManageList.do";
                        }
                    });
                }
            });
        } else{
            alert("教育局名称不能为空！");
        }
    }

    addEducation.addUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        var realvalues = [];
        $('#sourceU :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });
        addEducation.handleSelData(source,target,realvalues,"add","user");
    }

    addEducation.addAllUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        addEducation.handleSelAllData(source,target,"add","user");
    }

    addEducation.delUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        var realvalues = [];
        $('#targetU :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });

        addEducation.handleSelData(target,source,realvalues,"del","user");
    }

    addEducation.delAllUser = function(){
        //定义操作对象
        var source=$("#sourceU")[0];
        var target=$("#targetU")[0];
        addEducation.handleSelAllData(target,source,"del","user");
    }

    addEducation.addSchool = function(){
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        var realvalues = [];
        $('#sourceS :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });
        addEducation.handleSelData(source,target,realvalues,"add","school");
    }

    addEducation.delSchool = function(){
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        var realvalues = [];
        $('#targetS :selected').each(function(i, selected) {
            realvalues[i] = $(selected).val();
        });

        addEducation.handleSelData(target,source,realvalues,"del","school");
    }

    addEducation.handleSelData = function (source, target, realvalues, type, name){
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
            addEducation.arrToStrSave(name);
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

    addEducation.addAllSchool = function() {//全部增加
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        addEducation.handleSelAllData(source,target,"add","school");
    }

    addEducation.delAllSchool = function(){//全部删除
        //定义操作对象
        var source=$("#sourceS")[0];
        var target=$("#targetS")[0];
        addEducation.handleSelAllData(target,source,"del","school");
    }

    addEducation.handleSelAllData = function (source, target, type, name){
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
            addEducation.arrToStrSave(name);
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

    addEducation.arrToStrSave = function (name){
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


    addEducation.getChangeRegion = function(childId){
        if(selParam.parentId!=""){
            Common.getData('/education/getRegionList.do',selParam,function(rep){
                var list = rep.regions;
                var html='<option value="">请选择</option>';
                $.each(list,function(i,item){
                    html+='<option value="'+item.id+'">'+item.name+'</option>';
                });
                $("#"+childId).html(html);
                addEducation.getSchoolList();
            });
        }else{
            var html='<option value="">请选择</option>';
            $("#"+childId).html(html);
            addEducation.getSchoolList();
        }
    }

    addEducation.getSchoolList = function(){
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

    addEducation.getUserList = function(){
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

    addEducation.eduLogoFileUpload = function(){
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
                    if(educationLogo!=""){
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
    module.exports=addEducation;
});

