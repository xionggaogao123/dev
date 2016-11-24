/**
 * @author 李伟
 * @module  校园安全
 * @description
 * 校园安全模块
 */
/* global Config */
define('schoolsecurity',['jquery','doT','easing','common','fancybox','experienceScore','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var schoolsecurity = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    /**
     * @func fontNum
     * @desc 输入字数统计
     * @example
     * schoolsecurity.fontNum()
     */
     schoolsecurity.fontNum = function(){
         $(document).keyup(function(){
             var _textarea = $('.txt-info textarea'),
                 text = _textarea.val(),
                 counter = text.length;
             if(140-counter<0){
                 _textarea.val(text.substr(0,140));
                 counter = 140;
             }
             $('.font-num').text('（限'+(140-counter)+'个字）');
         });
     }

    //提交参数
    var schoolSecurityData = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * schoolsecurity.init()
     */
    schoolsecurity.init = function(){

        schoolsecurity.fontNum();


        //设置初始页码
        schoolSecurityData.page = 1;
        //设置每页数据长度
        schoolSecurityData.pageSize = 12;
        //切换查询类型
        schoolsecurity.infoHead();
        //点击提交按钮
        schoolsecurity.publishSchoolSecurity();
        //附件上传
        schoolsecurity.schoolSecurityFileUpload();
        //查询校园安全信息数据
        schoolsecurity.initSchoolSecurityPageData();

        //校长权限全选
        schoolsecurity.checkedAll();
        //校长权限批量删除
        schoolsecurity.batchDelete();
    };

    //切换查询类型
    schoolsecurity.infoHead=function(){
        $('.info-head ul li').click(function(event) {
            $(this).parent('ul').children('li').removeClass('cur');
            $(this).addClass('cur');
            //schoolSecurityData.handleState=$(this).val();
            $("#allCheck").removeAttr("checked");
            schoolSecurityData.page = 1;
            schoolsecurity.initSchoolSecurityPageData();
        });
    }

    /*
     * 发布一条校园安全信息
     * */
    schoolsecurity.publishSchoolSecurity = function() {
        $('.orange-btn').click(function(event){
            var comment_content = $.trim($("#security_content").val());
            var comment_placehd = $("#security_content").attr('placeholder');
            if(comment_content == '' && comment_placehd != '' && comment_placehd != '来说一句'){
                comment_content = comment_placehd;
                $("#security_content").val(comment_placehd);
            }

            if(comment_content == ''){
                alert("请输入内容。");
                return;
            }

            var content = $("#security_content").val().replace(/\n/g, '<br>');

            schoolSecurityData.publishContent=content;

            $('.security-img-delete').hide();
            //var arrayObj = [];
            var flist = '';
            var i = 0;
            if ($(".security-img").length <= 9) {
                $(".security-img").each(function() {
                    var srcPath=$(this).attr('src');
                    flist += srcPath.substring(0,srcPath.indexOf("?")) + ',';
                    //arrayObj[i] = $(this).attr('src');
                    i++;
                    $(this).parent().remove();
                });
            } else {
                alert('上传图片不可超过九张！');
                $('.security-img-delete').show();
                return;
            }
            schoolSecurityData.fileNameAry=flist;
            $('#picuploadLoading').show();
            Common.getData('/schoolSecurity/publishSchoolSecurity.do', schoolSecurityData,function(rep){
                if(rep.resultCode=="2"){
                    var surplusTime=rep.surplusTime;
                    var minute=parseInt(surplusTime/60);
                    var second=surplusTime%60;
                    var msg="";
                    if(minute>0){
                        msg+=minute+"分钟";
                    }
                    if(second>0){
                        msg+=second+"秒";
                    }
                    alert("短时间不能连续发布，请于"+msg+"后再发布!");
                }
                if (rep.score) {
                    scoreManager(rep.scoreMsg, rep.score);
                }
                $("#security_content").val("");
                $('#security_content').attr('placeholder','来说一句');
                $('#picuploadLoading').hide();
                schoolsecurity.initSchoolSecurityPageData();
            });
        });
    }
    /*
     * 上传校园安全附件信息
     * */
     schoolsecurity.schoolSecurityFileUpload = function(id) {
        //点击附件按钮
        $('#image-upload').click(function(event){
            Common.fileUpload('#image-upload','/schoolSecurity/addSchoolSecurityPic.do','#picuploadLoading',function(e,response){
                var result = response.result;
                var rdata = typeof  result == 'string' ? $.parseJSON(result) : result[0] ? $.parseJSON(result[0].documentElement.innerText) : result;
                if (rdata.result) {
                    var url = rdata.path[0];
                    $('#img-container ul').append('<li><a class="fancybox" href="' + url + '"data-fancybox-group="home" title="预览"><img class="security-img" src="' + url +'?imageView/1/h/60/w/60'+ '"></a><i class="security-img-delete"><img src="/static_new/images/tux.jpg"></i></li>');
                    $('#security_content').attr('placeholder', '分享图片');
                    $('.fancybox').fancybox();
                }
            });
        });
    }

    /*
     * 删除上传校园安全附件信息
     * */
    $('body').on('click', '.security-img-delete', function() {
        $(this).parent().remove();
        var file=$("input[class='security-img']");
        var newInput=file.clone().val("");
        file.after(newInput);
        file.remove();
    });

    var option={};
    /*
    * 查询校园安全信息数据
    * */
    schoolsecurity.initSchoolSecurityPageData = function() {
        //获取要查询的校园安全信息处理状态
        var stateFlag;
        if(schoolSecurityData.handleState == $('.info-head ul .cur').val()){
            stateFlag=false;
        }else{
            stateFlag=true;
            schoolSecurityData.handleState = $('.info-head ul .cur').val();
        }
        Common.getData('/schoolSecurity/selSchoolSecurityInfo.do',schoolSecurityData,function(rep){
            $('.sub-info-list').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.sub-info-list'});
            option.total= rep.total;
            option.pagesize= rep.pageSize;
            option.currentpage=rep.page;
            if(stateFlag){
                option.operate=function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function(){
                            if(schoolSecurityData.page!=$(this).text()){
                                schoolSecurityData.page=$(this).text();
                                schoolsecurity.initSchoolSecurityPageData();
                            }
                        });
                    });
                    $('.first-page').click(function(){
                        if(schoolSecurityData.page!=1) {
                            schoolSecurityData.page = 1;
                            schoolsecurity.initSchoolSecurityPageData();
                        }
                    });
                    $('.last-page').click(function(){
                        if(schoolSecurityData.page!=totalPage) {
                            schoolSecurityData.page = totalPage;
                            schoolsecurity.initSchoolSecurityPageData();
                        }
                    })
                }
            }
            Paginator.initPaginator(option);
        });
        /*
         * 绑定处理按钮
         * */
        $('.stips-deal').bind("click",function(event){
            schoolSecurityData.id=$(this).val();
            schoolsecurity.handleSchoolSecurity();
            //schoolsecurity.initSchoolSecurityPageData();
            //处理一条校园安全信息
            $(this).replaceWith('<label class="stips-on"></label>');
        });
        /*
         * 绑定删除按钮
         * */
        $('.icon-del').bind("click",function(event){
            schoolSecurityData.id = $(this).attr('id');
            schoolsecurity.deleteSchoolSecurity();
        });
        if($('.fancybox')!=undefined){
            $('.fancybox').fancybox();
        }
    }


    /*
     * 处理一条校园安全信息
     * */
    schoolsecurity.handleSchoolSecurity = function() {
        Common.getData('/schoolSecurity/handleSchoolSecurity.do', schoolSecurityData,function(){});
    }

    /*
     * 删除一条校园安全信息
     * */
    schoolsecurity.deleteSchoolSecurity = function() {
        if (confirm('确认删除此条校园安全信息！')) {
            //删除一条校园安全信息
            Common.getData('/schoolSecurity/deleteSchoolSecurity.do', schoolSecurityData,function(){}
                /*function(rep){
                    if (rep.score) {
                        scoreManager(rep.scoreMsg, rep.score);
                    }
                }*/
            );
            //查询校园安全信息数据
            schoolsecurity.initSchoolSecurityPageData();
        }
    }

    /*
     * 校长权限全选校园安全信息
     * */
    schoolsecurity.checkedAll = function() {
        $("#allCheck").removeAttr("checked");
        $('#allCheck').click(function(event) {
            var allCheck = $("#allCheck").is(':checked');
            $("input[name='idCheckbox']").each(function () {
                var checked=$(this).is(":checked");
                if(!allCheck){
                    if(checked) {
                        $(this).removeAttr("checked");
                    }
                }
                if(allCheck){
                    if (!checked) {
                        $(this).prop("checked", "checked");
                    }
                }
            });
        });
    }

    /*
     * 校长权限批量删除校园安全信息
     * */
    schoolsecurity.batchDelete = function() {
        $('.batch-del').click(function(event){
            var delIds="";
            $("input[name='idCheckbox']").each(function () {
                if($(this).is(":checked")) {
                    if(delIds==""){
                        delIds+=$(this).val();
                    }else{
                        delIds+=","+$(this).val();
                    }
                }
            });
            if(delIds==""){
                alert("请至少选择一条要删除的校园安全信息！");
                return;
            }
            if (confirm('确认批量删除所选的校园安全信息！')) {
                var delData={};
                delData.delIds=delIds;
                //删除一条校园安全信息
                Common.getData('/schoolSecurity/batchDeleteSchoolSecurity.do', delData,function(){}
                    /*function(rep){
                     if (rep.score) {
                     scoreManager(rep.scoreMsg, rep.score);
                     }
                     }*/
                );
                //查询校园安全信息数据
                schoolsecurity.initSchoolSecurityPageData();
            }
        });
    }

    //schoolsecurity.init();
    module.exports=schoolsecurity;
});
