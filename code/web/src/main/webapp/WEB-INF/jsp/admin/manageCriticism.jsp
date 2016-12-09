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
            <%--<label for="totalCount">总数</label>--%>
            <%--<input type="text" class="form-control" id="totalCount" placeholder="请输入总数">--%>
            <%--<label for="showCount">展示选项数</label>--%>
            <%--<input type="text" class="form-control" id="showCount" placeholder="请输入分页栏展示数(默认10)">--%>
            <label for="limit">每页显示数据量</label>
            <input type="text" class="form-control" id="limit" placeholder="请输入每页显示数据量(默认10)">
            <label for="limit">培训机构Id</label>
            <input type="text" class="form-control" id="instituteId" placeholder="情输入评论对应的机构Id">
        </div>
        <button type="submit" onclick="callBackPagination();" class="btn btn-default">提交</button>
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
            $('body').on('click','.delInfo',function(){
                var id=$(this).attr('delId');
                $.ajax({
                    type: "GET",
                    data: {id:id},
                    url: '/train/removeCriticism.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200") {
                            callBackPagination();
                        }
                    }
                });
            })
        })
        function callBackPagination() {
            var limit = Number($('#limit').val()) || 10;
            var totalCount=createTable(1, limit, totalCount);
            $('#callBackPager').extendPagination({
                totalCount: totalCount,
                showCount: 0,
                limit: limit,
                callback: function (curr, limit, totalCount) {
                    createTable(curr, limit, totalCount);
                }
            });
        }

        function geCriticisms(currPage,limit,instituteId){
            var totalCount=0;
            $.ajax({
                type: "GET",
                data: {pageSize:limit,page:currPage},
                url: '/train/getTrainComments/'+instituteId,
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (resp) {
                   if(resp.code=="200") {
                       showData(currPage,limit,resp.message.count,resp.message.list);
                       totalCount=resp.message.count;
                   }
                }
            });
            return totalCount;
        }

        function createTable(currPage, limit, total) {
            var instituteId=$('#instituteId').val();
            return geCriticisms(currPage,limit,instituteId);
        }

        function showData(currPage, limit, total, data){
            var html = [], showNum = limit;
            if (total - (currPage * limit) < 0) showNum = total - ((currPage - 1) * limit);
            html.push(' <table class="table table-hover piece" style="margin-left: 0;">');
            html.push(' <caption>悬停表格(' + total + ')</caption>');
            html.push(' <thead><tr><th>评论人昵称</th><th>评论人积分</th><th>评论内容</th><th>操作</th></tr></thead><tbody>');
            for (var i = 0; i <data.length; i++) {
                html.push('<tr><td>');
                html.push(data[i].nickName);
                html.push('</td>');
                html.push('<td>');
                html.push(data[i].score);
                html.push('</td>');
                html.push('<td>');
                html.push(data[i].comment);
                html.push('</td>');
                html.push('<td style="color: blue;cursor: pointer" class="delInfo" delId="'+data[i].id+'">删除</td>');
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
