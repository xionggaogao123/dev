<%--
  Created by IntelliJ IDEA.
  User: Caocui
  Date: 2015/8/4
  Time: 14:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div id="dengji-view" class="hide">
    <h3>全校默认等级设置</h3>

    <div class="clearfix"><a href="#" class="green-btn">添加记录</a></div>
    <table class="newTable" style="width: 500px;">
        <thead>
        <th>#</th>
        <th>等级名称</th>
        <th>分数范围（换算成百分制）</th>
        <th>操作</th>
        </thead>
        <tbody id="levelList">
        <tr>
            <td style="width:40px">1</td>
            <td><input class="inputOfTable"/></td>
            <td><input class="inputOfTable"/><span>%<span></td>
            <td><a href="#" class="icon-del"></a></td>
        </tr>
        </tbody>
    </table>
</div>
