/**
 * Created by guojing on 2016/1/5.
 */
var UEDITOR_HOME_URL = "/static/js/businessactivity/ueditor/";
var UEDITOR_IMG_UPLOAD_URL = "/upload/";

var createInfo={"pics":[],"picNames":[],"phonePics":[],"phonePicNames":[],"docs":[],"docsNames":[]};

$(function(){
    $("#compile_in").css("display","block");

    /*  上传PC端广告图片   ****************************************/
    $('#bannerPic').fileupload({
        url: '/business/addFieryActivityPic.do',
        start: function(e) {
            $('#fileuploadLoading').show();
        },
        done: function(e, data) {
            var info =data.result.message[0];
            createInfo.pics.push(info.fileKey);
            createInfo.picNames.push(info.fileName);
            var html='<div id="'+info.id+'">';
            html+='<img src="/img/notic/inform-complie.png">';
            html+='<div>';
            html+='<span>'+info.fileName+'</span><span class="inform-kb"></span>';
            html+='<span class="inform-YY-SC" onclick="deletePic(\''+info.id+'\',\''+info.fileKey+'\')">删除</span>';
            html+='</div>';
            html+='</div>';

            $(".infrom-TP").append(html);
            $(".infrom-TP").show();
        },
        fail: function (e, data) {

        },
        always: function (e, data) {
            $('#fileuploadLoading').hide();
        }
    });

    /*  上传手机端广告图片   ****************************************/
    $('#bannerPhonePic').fileupload({
        url: '/business/addFieryActivityPic.do',
        start: function(e) {
            $('#fileuploadLoading1').show();
        },
        done: function(e, data) {
            var info =data.result.message[0];
            createInfo.phonePics.push(info.fileKey);
            createInfo.phonePicNames.push(info.fileName);
            var html='<div id="'+info.id+'">';
            html+='<img src="/img/notic/inform-complie.png">';
            html+='<div>';
            html+='<span>'+info.fileName+'</span><span class="inform-kb"></span>';
            html+='<span class="inform-YY-SC" onclick="deletePhonePic(\''+info.id+'\',\''+info.fileKey+'\')">删除</span>';
            html+='</div>';
            html+='</div>';

            $(".infrom-PTP").append(html);
            $(".infrom-PTP").show();
        },
        fail: function (e, data) {

        },
        always: function (e, data) {
            $('#fileuploadLoading1').hide();
        }
    });

    /*  上传附件   ****************************************/
    $('#file_attach').fileupload({
        url: '/commonupload/doc/upload.do',
        start: function(e) {
            $('#fileuploadLoading2').show();
        },
        done: function(e, data) {
            var info =data.result.message[0];
            createInfo.docs.push(info.fileKey);
            createInfo.docsNames.push(info.fileName);
            var html='<div id="'+info.id+'">';
            html+='<img src="/img/notic/inform-complie.png">';
            html+='<div>';
            html+='<span>'+info.fileName+'</span><span class="inform-kb"></span>';
            html+='<span class="inform-YY-SC" onclick="deleteDoc(\''+info.id+'\',\''+info.fileKey+'\')">删除</span>';
            html+='</div>';
            html+='</div>';

            $(".infrom-FJ").append(html);
            $(".infrom-FJ").show();
        },
        fail: function (e, data) {

        },
        always: function (e, data) {
            $('#fileuploadLoading2').hide();
        }
    });

    getEducationList();

    $('.search-edu').click(function(){
        getEducationList();
    });

    $(".addE").click(function(event) {
        addEducation();

    });

    $(".addAllE").click(function(event) {
        addAllEducation();

    });

    $(".delE").click(function(event) {
        delEducation();

    });

    $(".delAllE").click(function(event) {
        delAllEducation();
    });
});

function deletePic(id,filekey)
{
    $("#"+id).remove();
    var index=$.inArray(filekey, createInfo.pics);
    if(index>=0)
    {
        createInfo.pics.splice(index, 1);
        createInfo.picNames.splice(index,1);
    }
}

function deletePhonePic(id,filekey)
{
    $("#"+id).remove();
    var index=$.inArray(filekey, createInfo.phonePics);
    if(index>=0)
    {
        createInfo.phonePics.splice(index, 1);
        createInfo.phonePicNames.splice(index,1);
    }
}

function deleteDoc(id,filekey)
{
    $("#"+id).remove();

    var index=$.inArray(filekey, createInfo.docs);
    if(index>=0)
    {
        createInfo.docs.splice(index, 1);
        createInfo.docsNames.splice(index,1);
    }
}


function submit()
{
    var title=$("#title_input").val();
    if(!title || title.length>50)
    {
        alert("标题内容非空，并且不超过50字");
        return;
    }
    $("#title").val(title);
    var checkRole=0;
    $('input[name="role"]:checked').each(function(){
        checkRole+=parseInt($(this).val());
    });
    $("#checkRole").val(checkRole);

    var takeEffect=1;
    $('input[name="effect"]:checked').each(function(){
        takeEffect=parseInt($(this).val());
    });
    $("#takeEffect").val(takeEffect);

    $("#content").val(getContent());
    if(createInfo.pics.length<2){
        $("#picFile").val(createInfo.pics[0]);
        $("#picName").val(createInfo.picNames[0]);
    }else{
        alert("火热活动的PC端广告图片只能是一张！");
        return;
    }

    if(createInfo.phonePics.length<2){
        $("#phonePicFile").val(createInfo.phonePics[0]);
        $("#phonePicName").val(createInfo.phonePicNames[0]);
    }else{
        alert("火热活动的手机端广告图片只能是一张！");
        return;
    }

    $("#docFile").val(getDocFile());
    $("#docNames").val(getDocNames());


    var beginTime=$("#bTime").val();
    var endTime=$("#eTime").val();

    $("#beginTime").val(beginTime);
    $("#endTime").val(endTime);
    $("#form").submit();
}

function getDocFile()
{
    var fs="";
    for(var f in createInfo.docs )
    {
        fs=fs+createInfo.docs[f]+",";
    }
    return fs;
}

function getDocNames()
{
    var fs="";
    for(var f in createInfo.docsNames )
    {
        fs=fs+createInfo.docsNames[f]+"|";
    }
    return fs;
}

function cancel()
{
    $(".inform-popup-I").show();
}

function goIndex()
{
    location.href="/business/fieryactivitylist.do";
}

function getEducationList(){
    var selParam={};
    selParam.eduIdsStr=$('#eduIdsStr').val();
    selParam.eduName=$("#eduName").val();
    $.ajax({
        url:"/education/getEducationList.do",
        data:selParam,
        type:"post",
        async:false,
        dataType:"json",
        success:function(data){
            var list = data;
            var html="";
            $.each(list,function(i,item){
                html+='<option value="'+item.id+'">'+item.educationName+'</option>';
            });
            $("#sourceE").html(html);
        }
    });
}

function addEducation(){
    //定义操作对象
    var source=$("#sourceE")[0];
    var target=$("#targetE")[0];
    var realvalues = [];
    $('#sourceE :selected').each(function(i, selected) {
        realvalues[i] = $(selected).val();
    });
    handleSelData(source,target,realvalues,"add");
}

function delEducation(){
    //定义操作对象
    var source=$("#sourceE")[0];
    var target=$("#targetE")[0];
    var realvalues = [];
    $('#targetE :selected').each(function(i, selected) {
        realvalues[i] = $(selected).val();
    });

    handleSelData(target,source,realvalues,"del");
}

function handleSelData(source, target, realvalues, type){
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
        arrToStrSave();
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

function addAllEducation() {//全部增加
    //定义操作对象
    var source=$("#sourceE")[0];
    var target=$("#targetE")[0];
    handleSelAllData(source,target,"add");
}

function delAllEducation(){//全部删除
    //定义操作对象
    var source=$("#sourceE")[0];
    var target=$("#targetE")[0];
    handleSelAllData(target,source,"del");
}

function handleSelAllData(source, target, type){
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
        arrToStrSave();
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

function arrToStrSave(){
    var sobj;
    var tobj;
    var realvalues = [];
    sobj=$("#targetE")[0];
    tobj=$("#eduIdsStr");
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