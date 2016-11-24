define(function(require,exports,module) {
    var expressTemplate = {};
    require('jquery');
    require('doT');
    require('pagination');
    Common = require('common');

    PAGE = 1;//当前页数

    expressTemplate.init = function(page){
        var url = "/mall/admin/expTempList.do";
        Common.getData(url,{page:page,pageSize:20},function(data){
            $('#expTempListCtx').empty();
            initTemplate();
            Common.render({tmpl:$('#expTempListTemp'),data:data.tempList,context:'#expTempListCtx'});
            paginator(data);
        });
    }


    /**
     * 分页
     * */
    paginator = function(data){
        var isInit = true;
        $('.new-page-links').html("");
        if(data.tempList.length > 0){
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n){ //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        expressTemplate.init(n);
                    }
                    PAGE = n;
                }
            });
        }
    }

    initTemplate = function(){
        $('.expTemp').hide();
        $('#_id').val(null);
        $('#name').val(null);
        $('tbody').empty();
        $(".bg").hide();
    }


    expressTemplate.provinceList=[
        {zn:"BJ",znm:"北京",choose:0,cur:0},
        {zn:"TJ",znm:"天津",choose:0,cur:0},
        {zn:"HE",znm:"河北",choose:0,cur:0},
        {zn:"SX",znm:"山西",choose:0,cur:0},
        {zn:"NM",znm:"内蒙古",choose:0,cur:0},
        {zn:"LN",znm:"辽宁",choose:0,cur:0},
        {zn:"JL",znm:"吉林",choose:0,cur:0},
        {zn:"HL",znm:"黑龙江",choose:0,cur:0},
        {zn:"SH",znm:"上海",choose:0,cur:0},
        {zn:"JS",znm:"江苏",choose:0,cur:0},
        {zn:"ZJ",znm:"浙江",choose:0,cur:0},
        {zn:"AH",znm:"安徽",choose:0,cur:0},
        {zn:"FJ",znm:"福建",choose:0,cur:0},
        {zn:"JX",znm:"江西",choose:0,cur:0},
        {zn:"SD",znm:"山东",choose:0,cur:0},
        {zn:"HA",znm:"河南",choose:0,cur:0},
        {zn:"HB",znm:"湖北",choose:0,cur:0},
        {zn:"HN",znm:"湖南",choose:0,cur:0},
        {zn:"GD",znm:"广东",choose:0,cur:0},
        {zn:"GX",znm:"广西",choose:0,cur:0},
        {zn:"HI",znm:"海南",choose:0,cur:0},
        {zn:"CQ",znm:"重庆",choose:0,cur:0},
        {zn:"SC",znm:"四川",choose:0,cur:0},
        {zn:"GZ",znm:"贵州",choose:0,cur:0},
        {zn:"YN",znm:"云南",choose:0,cur:0},
        {zn:"XZ",znm:"西藏",choose:0,cur:0},
        {zn:"SN",znm:"陕西",choose:0,cur:0},
        {zn:"GS",znm:"甘肃",choose:0,cur:0},
        {zn:"QH",znm:"青海",choose:0,cur:0},
        {zn:"NX",znm:"宁夏",choose:0,cur:0},
        {zn:"XJ",znm:"新疆",choose:0,cur:0},
        {zn:"HK",znm:"香港",choose:0,cur:0},
        {zn:"MO",znm:"澳门",choose:0,cur:0},
        {zn:"TW",znm:"台湾",choose:0,cur:0}
    ];
    /**
     *
     * @param otherList 其他行已选择的省
     * @param thisList 当前行已经选择的省
     */
    initProvinceList = function(otherList,thisList){
        for(province in expressTemplate.provinceList){
            expressTemplate.provinceList[province].choose = 0;
            expressTemplate.provinceList[province].cur = 0;
        }
        for(var i=0;i<expressTemplate.provinceList.length;i++){
            for(var j=0;j<otherList.length;j++){
                if(expressTemplate.provinceList[i].zn==otherList[j]){
                    expressTemplate.provinceList[i].choose=1;
                }
            }
            for(var j=0;j<thisList.length;j++){
                if(expressTemplate.provinceList[i].zn==thisList[j]){
                    expressTemplate.provinceList[i].cur=1;
                }
            }
        }
        $("#addWin").show();
        $(".bg").show();
        $('#provinceList').empty();

        Common.render({tmpl:$('#provinceTempJs'),data:expressTemplate.provinceList,context:'#provinceList'});
    };

    getZoneName = function(zoneNoList){
        var zoneNameList = [];
        var provinceList = expressTemplate.provinceList;
        for(znIndex in zoneNoList){
            for(znmIndex in provinceList){
                if(zoneNoList[znIndex] == provinceList[znmIndex].zn){
                    zoneNameList.push(provinceList[znmIndex].znm);
                }
            }
        }
        return zoneNameList.join("、");
    }


    /**
     * 新增|编辑模板
     * */
    expTemp = function(id){
        var data = {};
        if(id){
            $('#_id').val(id);

            var url = '/mall/admin/expTempDetail.do';
            var queryParam = {id:id};
            Common.getPostData(url,queryParam,function(rep){
                data.id = rep.id;
                data.name = rep.name;
                data.details = rep.details;
            });
        }

        if(data != null && data.name != null){
            $('#name').val(data.name);
        }
        if(data == null || data.details == null){
            data.details = [];
        }

        Common.render({tmpl:$('#tbodyTmpl'),data:data.details,context:'tbody'});
        $('.expTemp').show();
        $('.bg').show();
    }

    //新增模板
    addTemp = function(){
        expTemp();
    }

    //编辑模板
    editTemp = function(id){
        expTemp(id);
    }

    //删除模板
    removeTemp = function(id){
        if(confirm('确定要删除该模板？')){
            var url = '/mall/admin/removeExpTemplate.do';
            Common.getPostData(url,{id:id},function(data){
                if(data.code == '200'){
                    expressTemplate.init(PAGE);
                }
            });
        }
    }

    //check
    check = function(){
        if(!$('#name').val()){
            alert("模板名称不能为空！");
            return false;
        }
        if($('tr').length == 1){
            alert("模板不能为空！");
            return false;
        }

        return true;
    }

    $(document).ready(function(){

        $("table").find("input").attr("maxlength",6)
            .keyup(function(){this.value=this.value.replace(/\D/gi,"")});

        $(".hide-x").click(function(){
            $("#addWin").hide();
        });
        $("#save").click(function(){
            $("#addWin").hide();
            //$(".bg").hide();
            var choosed=[];
            $("input:checkbox[name=check_name]:checked").each(function(i){
                choosed.push($(this).val());
                var zn=$(this).val();
                var znm="";
                for(var i=0;i<expressTemplate.provinceList.length;i++){
                    if(expressTemplate.provinceList[i].zn==zn){
                        znm=expressTemplate.provinceList[i].znm;
                        break;
                    }
                }
                choosed.push({zn:zn,znm:znm});
            });
        });

        $('#name').blur(function(){
            if(!$(this).val()){
                $(this).addClass('input-border');
            }
        });
        $('#name').focus(function(){
            $(this).removeClass('input-border');
        });


        //新增行
        $("body").on("click","#addRow",function(){
            var row = '<tr>'
                +'<td><span></span><input type="hidden" name="zoneName">'
                +'<input type="hidden" name="zoneNo"><a href="#" class="editAddr">编辑</a></td>'
                +'<td><input name="firstPrice" type="text" maxlength="5" required onkeyup="if(isNaN(value))execCommand(\'undo\')" onafterpaste="if(isNaN(value))execCommand(\'undo\')"></td>'
                +'<td><input name="addOnePrice" type="text" maxlength="5" required onkeyup="if(isNaN(value))execCommand(\'undo\')" onafterpaste="if(isNaN(value))execCommand(\'undo\')"></td>'
                +'<td><a href="#" class="deleteRow" >删除</a></td>'
                +'</tr>';
            $('tbody').append(row);
        });

        //删除行
        $("body").on("click",".deleteRow",function(){
            $(this).parents('tr').remove();
        });

        targetRow = null;

        //编辑送货地区
        $("body").on("click",".editAddr",function(){
            var otherList = [];//其他行已选择的省
            var thisList = $(this).siblings("input[name='zoneNo']").val().split(",");//当前行已选择的省

            $(this).parent("td").parent("tr").siblings().each(function(){
                var l = $(this).children("td").children("input[name='zoneNo']").val().split(",");
                for(var i = 0;i < l.length;i++){
                    otherList.push(l[i]);
                }
            });


            initProvinceList(otherList,thisList);
            targetRow = $(this);
        });

        //确定送货地区
        $("body").on("click","#save",function(){
            var addrList = [];
            $("#provinceList").find("input:checkbox[name^='check']:checked").each(function(){
                addrList.push($(this).val());
            });
            targetRow.siblings("span").text(getZoneName(addrList));
            targetRow.siblings("input[name='zoneName']").val(getZoneName(addrList));
            targetRow.siblings("input[name='zoneNo']").val(addrList);
        });

        //提交
        $("body").on("click","#submit",function(){
            if(check()){
                var id = $("#_id").val();
                var name = $("#name").val();
                var details = [];
                $("tbody").children("tr").each(function(){
                    var prvcInfo = {};
                    prvcInfo.zoneNo = $(this).find("input[name='zoneNo']").val();
                    prvcInfo.zoneName = $(this).find("input[name='zoneName']").val();
                    prvcInfo.firstPrice = $(this).find("input[name='firstPrice']").val();
                    prvcInfo.addOnePrice = $(this).find("input[name='addOnePrice']").val();
                    details.push(prvcInfo);
                });

                var template = {id:id,name:name,details:details};
                var url = '/mall/admin/saveExpTemplate.do';
                $.ajax({
                    url: url,
                    type: 'post',
                    contentType: "application/json",
                    data: JSON.stringify(template),
                    success: function (rep) {
                        if (rep.code == "200") {
                            expressTemplate.init(PAGE);
                        }
                    }
                });
            }

        });

        //取消
        $("body").on("click","#cancel",function(){
            initTemplate();
        });

    });

     module.exports = expressTemplate;
});

