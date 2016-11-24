<%--
  Created by IntelliJ IDEA.
  User: Caocui
  Date: 2015/8/4
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div id="kaosheng-view" class="hide">
    <div class="class-table">
        <div class="class-list">
            <label>年级<select id="arrange-exam-grade">
                <c:forEach items="${grades}" var="grade">
                    <option value="${grade.id}">${grade.name}</option>
                </c:forEach>
            </select></label>
            <button type="button" class="gray-line-btn" id="reoladByGradeBtn">确定</button>
        </div>
        <table class="newTable" width="100%">
            <thead>
            <th width="50">#</th>
            <th>考试名称</th>
            <th width="100">日期</th>
            <th width="80">编排状态</th>
            <th width="80">锁定</th>
            <th width="150">操作</th>
            </thead>
            <tbody id="arrange-exam-tbody">
            </tbody>
        </table>
        <div class="new-page-links" id="arrange-exam-pagination">
        </div>
    </div>
    <div class="class-edit kaosheng-edit" id="kaosheng-view-container">
        <a href="javascript:void(0)" class="back-link">&lt; 返回</a>

        <h3 id="kaosheng-view-title">初一期末考试 / 考生安排查看</h3>

        <div class="kaosheng-tit">
                            <span>
                                <label><input type="radio" name="apview" value="examroom"/>按考场查看</label>
                                <label><input type="radio" name="apview" value="class"/>按班级查看</label>
                            </span>
                            <span>
                                <label id="examroom-select" style="display: none">考场<select id="examroom-select-field">
                                </select></label>
                                <label id="examroom-class" style="display: none">班级<select id="examroom-class-field">
                                </select></label>
                                <button type="button" class="gray-line-btn" id="kaosheng-view-confirm">确定</button>
                            </span>
        </div>
                        <span class="clearfix">
                            <a href="javascript:void(0)" id="kaosheng-view-export-seat"
                               class="gray-line-btn">按标签导出</a>
                            <a href="javascript:void(0)" id="kaosheng-view-export"
                                                  class="gray-line-btn">普通导出</a>
                        </span>


        <div class="sub-kaosheng-list">
            <table class="gray-table">
                <thead id="kaosheng-view-arranged-title">
                <tr id="kaosheng-view-arranged-title-tr">
                    <th width="120"><div class="div120">班级</div></th>
                    <th width="120"><div class="div120">考场名称</div></th>
                    <th width="120"><div class="div120">考场编号</div></th>
                    <th width="120"><div class="div120">考号</div></th>
                    <th width="100"><div class="div120">考生姓名</div></th>
                </tr>
                </thead>
                <tbody id="kaosheng-view-arranged-list">
                </tbody>
            </table>
        </div>
    </div>
    <div class="class-edit paikaochang">
        <a href="javscript:void(0)" class="back-link">&lt; 返回</a>

        <div class="paikc-tit clearfix">
            <span>使用说明：</span>
            <ol>
                <li>1. 选择要启用考场资源(总容量应大于考生总人数)</li>
                <li>2. 点击编排按钮进行考生安排</li>
                <li>3. 未排考生为 0 时，编排成功</li>
                <li>4. 点击“查看”查看考生编排情况</li>
            </ol>
        </div>
        <h3 id="paikaochang-title">初一 / 初一期末考试 排考场</h3>
        <table class="paikc-opt">
            <tr>
                <td width="105" id="paikaochang-arranged-detail-grade">初一年级</td>
                <td width="176">考生总人数：<span id="paikaochang-arranged-detail-sumstu">100</span></td>
                <td width="148">已排考生：<span id="paikaochang-arranged-detail-arrangedstu">0</span></td>
                <td>未排考生：<span id="paikaochang-arranged-detail-unarrstu">0</span></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>已启用考场的总容量：<span id="paikaochang-arranged-detail-examcap">0</span></td>
                <td>编排状态：<span class="color-org" id="paikaochang-arranged-status">未完成</span></td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <div class="paikc-list clearfix">
            <div class="paikc-list-left">
                <h4>选择考场资源</h4>
                <table class="newTable" style="width: 100%;">
                    <thead>
                    <tr>
                        <th>考场号</th>
                        <th>考场名称</th>
                        <th>座位数</th>
                        <th>启用</th>
                    </tr>
                    </thead>
                    <tbody id="paikaochang-arranged-examroomlist">
                    </tbody>
                </table>
            </div>
                                <span class="paikc-list-opt">
                                    <em></em>
                                    <em></em>
                                </span>

            <div class="paikc-list-right">
                <h4 id="paikaochang-arranged-detail-examname">初一期末考试</h4>

                <div class="sub-btn-list">
                    <a href="javascript:void(0)" class="gray-line-btn"
                       id="paikaochang-arranged-detail-viewbtn">查看</a>
                    <a href="javascript:void(0)" class="gray-line-btn start-bianpai paikaochang-arranged-detail-begin"
                       id="paikaochang-arranged-detail-begin" ty="0">普通编排</a>
                    <a href="javascript:void(0)" class="gray-line-btn start-bianpai paikaochang-arranged-detail-begin"
                       id="paikaochang-arranged-detail-begin-score" ty="1">按成绩编排</a>
                </div>
                <table class="newTable" style="width: 100%;">
                    <thead>
                    <th>考场号</th>
                    <th>考场名称</th>
                    <th>座位数</th>
                    <th>饱和情况</th>
                    <th>移除</th>
                    </thead>
                    <tbody id="paikaochang-arranged-selectedexamroom">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- 提示框 -->
<div class="pop-wrap" id="waitWindow">
    <div class="pop-content">
        <h3 class="infoWindowH" id="waitWindowInfo">正在排考场,请等待...</h3>
    </div>
</div>

<div class="pop-wrap" id="overWindow">
    <div class="pop-content">
        <h3 class="infoWindowH" id="overWindowH">考场安排完毕！</h3>
        <h3 class="infoWindowH" id="overWindowHT">请点击“查看”了解考场名单。</h3>
    </div>
    <div class="pop-btn"><span class="active" id="overWindowBtn">确定</span></div>
</div>
