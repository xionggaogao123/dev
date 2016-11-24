<%--
  Created by IntelliJ IDEA.
  User: Caocui
  Date: 2015/8/4
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div id="kaochang-view" class="hide">
    <h3>考场资源设置</h3>
                        <span class="clearfix class-add">
                            <a href="javascript:void(0)" class="green-btn">添加记录</a>
                        </span>
    <table class="newTable" style="width: 100%;">
        <thead>
        <th>#</th>
        <th>考场号</th>
        <th>考场名称</th>
        <th>座位数</th>
        <th>备注</th>
        <th>操作</th>
        </thead>
        <tbody id="examRoomList">
        </tbody>
    </table>
</div>