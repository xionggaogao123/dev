/**
 * Created by guojing on 2016/11/11.
 */
define('indicatorTree',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var iTree = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    var iTreeData = {};
    iTree.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        //设置初始页码
        /*if(null != sessionStorage.getItem("itreePage")){
            iTreeData.page = sessionStorage.getItem("itreePage")||1;
        }else{
            iTreeData.page = 1;
        }
        if(null != sessionStorage.getItem("itreeName")){
            iTreeData.name = sessionStorage.getItem("itreeName")||"";
        }else{
            iTreeData.name = "";
        }*/
        iTreeData.page = 1;
        //设置每页数据长度
        iTreeData.pageSize = 12;
        iTree.searchiTreeData();
        $("#searchBtn").click(function(){
            //设置初始页码
            iTreeData.page = 1;
            iTreeData.name=$.trim($('#name').val());
            iTree.searchiTreeData();
        });

        $(".new-add").click(function(){
            $("#newadd-title").text("新建指标体系");
            $(".bg").show();
            $(".newadd-alert").show();
        });

        $(".alert-btn-sure").click(function(){
            iTree.addOrEditITree();
        });

        $(".alert-close,.alert-btn-qx").click(function(){
            $("#treeId").val("");
            $("#treeName").val("");
            $("#treeDescribe").val("");
            $(".bg").hide();
            $(".newadd-alert").hide();
        });

        $('body').on('click','.itree-name',function() {
            var id=$(this).parent().attr("id");
            Common.goTo("/indicator/indicatorManage.do?treeId="+id);
        });

        $('body').on('click','.itree-edit',function() {
            var id=$(this).parent().parent().attr("id");
            iTree.getITreeById(id);
        });
        $('body').on('click','.itree-del',function() {
            var id=$(this).parent().parent().attr("id");
            iTree.deleteITree(id);
        });
    };

    iTree.getITreeById = function(id){
        var selParam={};
        selParam.id=id;
        Common.getPostData('/indicator/getIndicatorTree.do', selParam,function(rep){
            if(rep.code==200){
                var dto=rep.message;
                $("#treeId").val(dto.id);
                $("#treeName").val(dto.name);
                $("#treeDescribe").val(dto.describe);
                $("#newadd-title").text("编辑指标体系");
                $(".bg").show();
                $(".newadd-alert").show();
            }
        });
    };

    iTree.deleteITree = function(id){
        var delParam={};
        delParam.id=id;
        if(confirm("您确定删除该指标体系信息吗?")){
            Common.getPostData('/indicator/delIndicatorTree.do', delParam,function(rep){
                if(rep.code==200){
                    alert(rep.message);
                    iTree.searchiTreeData();
                }else{
                    alert(rep.message);
                }
            });
        }
    };

    iTree.addOrEditITree = function(){
        var addParam={};
        addParam.id=$("#treeId").val();
        addParam.name=$("#treeName").val();
        if($.trim(addParam.name)==""){
            alert("请输入指标体系名称!");
            return;
        }
        addParam.describe = $("#treeDescribe").val();
        Common.getPostBodyData('/indicator/addOrEditITree.do', addParam, function(rep){
            var json = eval('(' + rep + ')');
            if(json.code==200){
                $("#treeId").val("");
                $("#treeName").val("");
                $("#treeDescribe").val("");
                iTreeData.page = 1;
                iTreeData.name="";
                var dto=json.message.dto;
                if(addParam.id!=""){
                    alert("指标体系修改成功!");
                    iTree.searchiTreeData();
                }else{
                    Common.goTo("/indicator/indicatorManage.do?treeId="+dto.id);
                }
                $(".bg").hide();
                $(".newadd-alert").hide();
            }else{
                alert(json.message);
            }
        });
    };

    var option={};
    //查询
    iTree.searchiTreeData = function(){
        /*sessionStorage.setItem("itreePage", iTreeData.page);
        sessionStorage.setItem("itreeName", iTreeData.name);*/
        Common.getData('/indicator/getITreePageList.do',iTreeData,function(rep){
            var data=rep.message;
            $('.itree-data').html('');
            if(data.rows.length==0){
                iTreeData.page=1;
                if(data.total>0){
                    iTree.searchiTreeData();
                }
            }else {
                Common.render({tmpl: $('#j-tmpl'), data: data, context: '.itree-data'});
                option.total = data.total;
                option.pagesize = data.pageSize;
                option.currentpage = data.page;
                option.operate = function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function () {
                            if (iTreeData.page != $(this).text()) {
                                iTreeData.page = $(this).text();
                                iTree.searchiTreeData();
                            }
                        });
                    });
                    $('.first-page').click(function () {
                        if (iTreeData.page != 1) {
                            iTreeData.page = 1;
                            iTree.searchiTreeData();
                        }
                    });
                    $('.last-page').click(function () {
                        if (iTreeData.page != totalPage) {
                            iTreeData.page = totalPage;
                            iTree.searchiTreeData();
                        }
                    })
                };
                Paginator.initPaginator(option);
            }
        });
    };
    module.exports=iTree;
});