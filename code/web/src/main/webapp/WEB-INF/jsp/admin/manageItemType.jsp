<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <h2>
        <small>扩展分页带回调函数(表格带分页)</small>
    </h2>
    <form role="form" action="#">
        <div class="form-group">
            <label for="sortNum">排序数字</label>
            <input type="text" class="form-control" id="sortNum" placeholder="请输入排序的数字信息">
            <label for="itemTypeId">培训分类信息Id</label>
            <input type="text" class="form-control" id="itemTypeId" placeholder="情输入培训分类Id">
        </div>
        <button type="submit" onclick="callBackPagination();" class="btn btn-default">查询培训分类数据</button>
    </form>
    <div id="mainContent"></div>
    <div id="callBackPager"></div>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <link rel="stylesheet" href="/static/js/modules/criticism/bootstrap.min.css">
    <script src="/static/js/modules/criticism/jquery-1.11.1.min.js"></script>
    <script src="/static/js/modules/criticism/extendPagination.js"></script>
    <script src="/static/js/modules/criticism/bootstrap.js"></script>
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>

    <%--<script type="text/javascript"--%>
    <%--src="http://api.map.baidu.com/api?v=2.0&ak=TzFCVsUAf4RzyoOdgZ5tB10fASv5Dswy"></script>--%>
    <script type="text/javascript">
        $(function(){
            $('body').on('click','.sortInfo',function(){
                var id=$(this).attr('sortId');
                var sort=Number($('#sortNum').val());
                if (confirm("你确定要更改分类排序的顺序吗？")) {
                    $.ajax({
                        type: "GET",
                        data: {sort:sort},
                        url: '/train/itemType/setSort/'+id,
                        async: false,
                        dataType: "json",
                        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                        success: function (resp) {
                            if(resp.code=="200") {
                                callBackPagination();
                            }
                        }
                    });
                }
            })
        })

        function getItemTypes(itemTypeId){
            $.ajax({
                type: "GET",
                data: {itemTypeId:itemTypeId},
                url: '/train/getItemTypes.do',
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (resp) {
                   if(resp.code=="200") {
                       showData(resp.message);
                   }
                }
            });
        }

        function callBackPagination() {
            var itemTypeId=$('#itemTypeId').val();
            return getItemTypes(itemTypeId);
        }

        function showData(data){
            var html = [];
            html.push(' <table class="table table-hover piece" style="margin-left: 0;">');
            html.push(' <thead><tr><th>分类名称</th><th>分类层级</th><th>分类排序</th><th>操作</th></tr></thead><tbody>');
            for (var i = 0; i <data.length; i++) {
                html.push('<tr><td>');
                html.push(data[i].name);
                html.push('</td>');
                html.push('<td>');
                html.push(data[i].level);
                html.push('</td>');
                html.push('<td>');
                html.push(data[i].sort);
                html.push('</td>');
                html.push('<td style="color: blue;cursor: pointer" class="sortInfo" sortId="'+data[i].id+'">排序</td>');
                html.push('</tr>');
            }
            html.push('</tbody></table>');
            var mainObj = $('#mainContent');
            mainObj.empty();
            mainObj.html(html.join(''));
        }
    </script>
</layout:override>
<%@ include file="_layout.jsp" %>
