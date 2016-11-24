<%--
  Created by IntelliJ IDEA.
  User: Caocui
  Date: 2015/8/4
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div id="chengji-view" class="hide">
    <div class="class-table">
        <div class="class-list">
            <label>年级<select id="scoreGradeList"><c:forEach items="${grades}" var="grade">
                <option value="${grade.id}">${grade.name}</option>
            </c:forEach></select></label>
            <button type="button" class="gray-line-btn" id="scoreListQuery">确定</button>
        </div>
        <table class="newTable" id="scoreGrayTable">
            <thead id="chengjiViewTitle">
            <th width="50">#</th>
            <th>考试名称</th>
            <th width="100">考试日期</th>
            <th width="80">按年级录入</th>
            <th width="80">按班级录入</th>
            <th width="80">按考场录入</th>
            <th width="80">小分录入</th>
            <th width="50">查看</th>
            <th width="80">缺免考设置</th>
            <th width="80">开放设置</th>
            </thead>
            <tbody id="scoreViewExamList">
            </tbody>
        </table>
        <div class="new-page-links" id="score-list-pagination">
        </div>
    </div>
    <div class="class-edit cj-lvru">
        <a href="javascript:void(0)" id="exam-setting-luru-back" class="back-link">&lt; 返回</a>

        <h3 id="score-shouquan-setting">初一期末考试 / 录入授权设置</h3>
                            <span class="clearfix">
                                <a href="javascript:void(0)" id="scrore-setting-batch" class="green-btn">批量设置</a>
                            </span>
        <table class="newTable" style="width: 475px;" id="scoreOpenTimeTable">
            <thead>
            <th>科目</th>
            <th width="80">是否开放</th>
            <th width="100">录入开始时间</th>
            <th width="100">录入结束时间</th>
            <th width="50">设置</th>
            </thead>
            <tbody id="score-input-openlist">
            <tr>
                <td>语文</td>
                <td>开放</td>
                <td>--</td>
                <td>--</td>
                <td><span class="kaifang-set">设置</span></td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="qmkao-view hide">
        <a href="javascript:void(0)" class="back-link">&lt; 返回</a>

        <h3 id="qmkao-title">初一期末考试 / 缺免考设置</h3>

        <div class="class-list">
            <label>科目<select id="qmkao-subject">
            </select></label>
            <label>班级<select id="qmkao-class">
            </select></label>
            <label>显示<select id="qmkao-showtype">
                <option value="all">全部</option>
                <option value="absent">缺考</option>
                <option value="exemption">免考</option>
                <option value="normal">正常</option>
            </select></label>
            <button type="button" class="gray-line-btn" id="qmkao-confirm">确定</button>
        </div>
        <table class="newTable" id="szqmkTable" style="width: 420px;">
            <thead>
            <th width="120px">姓名</th>
            <th width="100px">班级</th>
            <th width="100">缺考</th>
            <th width="100">免考</th>
            </thead>
            <tbody id="qmkao-tboday">
            </tbody>
        </table>
        <div class="new-page-links" id="qmkao-pagination">
        </div>
    </div>
    <div class="class-luru hide">
        <a href="javascript:void(0)" class="back-link">&lt; 返回</a>

        <h3 id="inputByClassTitle">初一期末考试 / 成绩录入</h3>
        <cc:choose>
            <cc:when test="${isAdmin}">
                <table class="table-form" width="540">
                    <tr>
                        <td width="58">班级</td>
                        <td><select id="scoreInputClassAdmin">
                        </select></td>
                    </tr>
                    <tr>
                        <td>科目</td>
                        <td id="scoreInputByClassSubjectAdmin">
                            <button type="button" class="gray-line-btn" id="filterSubjectInClassBtnAdmin">确定</button>
                        </td>
                    </tr>
                </table>
            </cc:when>
            <cc:when test="${isTeacher}">
                <div class="class-list">
                    <label>班级<select id="scoreByClassInputClassTeacher">
                    </select></label>
                    <label>科目<select id="scoreByClassInputSubjectTeacher">
                    </select></label>
                    <button type="button" class="gray-line-btn" id="filterSubjectInClassBtnTeacher">确定</button>
                </div>
            </cc:when>
            <cc:otherwise></cc:otherwise>
        </cc:choose>
        <div class="clearfix btn-list" >
            <a href="javascript:void(0)" class="gray-line-btn" id="exportByClassAtInput">导出</a>
            <a href="javascript:void(0)" class="gray-line-btn daoruBtn" id="importScoreByClass">导入</a>
        </div>
        <div class="gray-table-box">
            <table class="gray-table" >
                <thead id="scoreListTitleByClass">
                </thead>
            </table>
            <div class="gray-table-body">
                <table class="gray-table" >
                    <tbody id="scoreListContentByClass">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="kaochang-luru hide">
        <a href="javascript:void(0)" class="back-link">&lt; 返回</a>

        <h3 id="kaochang-luru-title">初一期末考试 / 成绩录入</h3>
        <cc:choose>
            <cc:when test="${isAdmin}">
                <table class="table-form" width="540">
                    <tr>
                        <td width="58">考场</td>
                        <td><select id="kaochang-luru-roomlist-admin">
                        </select></td>
                    </tr>
                    <tr>
                        <td>科目</td>
                        <td id="kaochang-luru-subjects-admin">
                            <button type="button" class="gray-line-btn" id="kaochang-luru-confirm-admin">确定</button>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </cc:when>
            <cc:when test="${isTeacher}">
                <div class="class-list">
                    <label>考场<select id="kaochang-luru-roomlist-teacher">
                    </select></label>
                    <label>科目<select id="kaochang-luru-subjects-teacher">
                    </select></label>
                    <button type="button" class="gray-line-btn" id="kaochang-luru-confirm-teacher">确定</button>
                </div>
            </cc:when>
            <cc:otherwise></cc:otherwise>
        </cc:choose>
        <div class="clearfix btn-list">
            <a href="javascript:void(0)" class="gray-line-btn" id="kaochang-luru-export">导出</a>
            <a href="javascript:void(0)" class="gray-line-btn daoruBtn" id="kaochang-luru-import">导入</a>
        </div>
        <div class="gray-table-box">
            <table class="gray-table" >
                <thead id="kaochang-luru-titles">
                <th>姓名</th>
                <th>班级</th>
                <th>语文（100）</th>
                <th>英语（100）</th>
                <th>数学（100）</th>
                <th>历史（100）</th>
                </thead>
            </table>
            <div class="gray-table-body">
                <table class="gray-table">
                    <tbody id="kaochang-luru-datalist">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="chakan-luru hide">
        <a href="javascript:void(0)" class="back-link">&lt; 返回</a>

        <h3 id="scoreViewTitle">初一期末考试 / 成绩查看</h3>
        <table class="table-form" >
            <tr>
                <td width="58">班级</td>
                <td id="chakan-score-class">
                </td>
            </tr>
            <tr>
                <td>科目</td>
                <td id="chakan-score-subject">
                </td>
            </tr>
            <tr>
                <td>排序</td>
                <td><select id="chakan-score-orderby">
                    <option value="class" selected>按班级排序</option>
                    <option value="sum">按合计分数排序</option>
                </select></td>
            </tr>
        </table>
        <div class="clearfix btn-list">
            <a href="javascript:void(0)" class="gray-line-btn" id="viewscoreexport">导出</a>
        </div>
        <!--.表头固定的效果-->
        <div class="gray-table-box" style="min-width: 940px;">
            <table class="newTable" >
                <thead id="scoreViewListTitle">
                </thead>
            </table>

            <div class="gray-table-body">
                <table class="newTable">
                    <tbody id="scoreViewListContent">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <!-- 小分设置页面 -->
	<div class="exam-item-score">
		<a href="#" class="back-link">&lt; 返回</a>
        <h3 id="examItemScoreTitle"></h3>
                           
        <table class="newTable" id="examItemScoreConfigTable" style="width: 460px;">
            <thead>
            <th width="160px">科目名称</th>
            <th width="140px">满分</th>
            <th width="160px">小分分数</th>
            </thead>
            <tbody id="examItemScoreList">
            </tbody>
        </table>
        
	</div>
</div>

<div class="pop-wrap daoru" id="daoruByClass">
    <div class="pop-title">成绩导入 — 按班级</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importByClassForm" method="post">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="genTemplateByClass">生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadFileByClass" name="scoreData" type="file" accept=".xls,.xlsx">
                </li>
                <li><i class="icon-down"></i></li>
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="beginImportByClassBtn">导入</span><span>取消</span></div>
</div>

<div class="pop-wrap daoru" id="daoruByGrade">
    <div class="pop-title">成绩导入 — 按年级</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importByGradeForm" method="post">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="genTemplateByGrade" style="position: relative;left: 110px;">生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadFileByGrade" name="scoreData" type="file" accept=".xls,.xlsx" style="position: relative;left: 110px;">
                </li>
                <li><i class="icon-down"></i></li>
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="beginImportByGradeBtn">导入</span><span>取消</span></div>
</div>

<div class="pop-wrap daoru" id="daoruByExamId">
    <div class="pop-title">成绩导入 — 按考场</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importByExamForm">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="genTemplateByExamRoom">生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadFileByExamRoom" name="scoreData" type="file" accept=".xls,.xlsx">
                </li>
                <li><i class="icon-down"></i></li>
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="beginImportByExamRoomBtn">导入</span><span>取消</span></div>
</div>


<div class="pop-wrap" id="inputauthset">
    <div class="pop-title">单科设置</div>
    <div class="pop-content">
        <h3>语文 — 录入授权设置</h3>
        <table class="gray-table" >
            <thead>
            <th>科目名称</th>
            <th>满分</th>
            <th>日期</th>
            <th>星期</th>
            <th>时间</th>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div class="pop-btn"><span class="active">关闭</span></div>
</div>

<div class="pop-wrap" id="lurusetId">
    <form id="lurusetIdForm">
        <div class="pop-title">单科设置</div>
        <input name="id" type="hidden" id="lurusetIdForm-Id">
        <input name="examId" type="hidden" id="lurusetIdForm-examId">

        <div class="pop-content">
            <h3 id="lurusetId-title">语文 — 录入授权设置</h3>
            <table>
                <tr>
                    <td>是否开放</td>
                    <td>
                        <label><input name="openStatus" type="radio" value="1">开放</label>
                        <label><input name="openStatus" type="radio" value="0">关闭</label>
                    </td>
                </tr>
                <tr>
                    <td>开放时间</td>
                    <td><input type="text" name="openBeginTime" id="lurusetIdForm-begin" readonly
                               onclick="WdatePicker({minDate:'${currDate}'})"></td>
                </tr>
                <tr>
                    <td>结束时间</td>
                    <td><input type="text" name="openEndTime" id="lurusetIdForm-end" readonly
                               onclick="WdatePicker({minDate:'${currDate}'})"></td>
                </tr>
            </table>
        </div>
    </form>
    <div class="pop-btn"><span class="active" id="lurusetIdForm-confirm">确定</span><span>取消</span></div>
</div>
<div class="pop-wrap" id="piliangsetId">
    <div class="pop-title">批量设置</div>
    <form id="piliangsetIdForm">
        <input name="examId" type="hidden" id="piliangsetIdForm-examId">

        <div class="pop-content">
            <table>
                <tr>
                    <td>是否开放</td>
                    <td>
                        <label><input name="openStatus" type="radio" value="1">开放</label>
                        <label><input name="openStatus" type="radio" value="0">关闭</label>
                    </td>
                </tr>
                <tr>
                    <td>开放时间</td>
                    <td><input type="text" name="openBeginTime" id="piliangsetIdForm-begin" readonly
                               onclick="WdatePicker({minDate:'${currDate}'})"></td>
                </tr>
                <tr>
                    <td>结束时间</td>
                    <td><input type="text" name="openEndTime" id="piliangsetIdForm-end" readonly
                               onclick="WdatePicker({minDate:'${currDate}'})"></td>
                </tr>
            </table>
        </div>
    </form>
    <div class="pop-btn"><span class="active" id="piliangsetIdForm-confirm">确定</span><span>取消</span></div>
</div>



<div class="pop-wrap daoru" id="importExamItemScoreWindow">
    <div class="pop-title">小分分数导入</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importExamItemForm" method="post">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="exportExamItemScoreModelBtn">生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadExamItemScoreInput" name="examItemData" type="file" accept=".xls,.xlsx">
                </li>
                
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="importExamItemScoreBtn">导入</span><span id="cancelImportExamItenScore">取消</span></div>
</div>

<!-- 导出用表单 -->
	<form action="/examItemScore/exportExamItemScoreModel.do" method="post" id="exportExamItemScoreModelForm" style="display:none" >
		<input type="text" id="eId" name="examId" value=""/>
		<input type="text" id="sId" name="subjectId" value=""/>
	</form>
	
	<!-- 提示框 -->
<div class="pop-wrap" id="waitWindow_score">
    <div class="pop-content">
        <h3 class="infoWindowH" >正在导入小分成绩,请等待...</h3>
    </div>
</div>

<div class="pop-wrap" id="overWindow_score">
    <div class="pop-content">
        <h3 class="infoWindowH" id="overWindowH_score">小分成绩导入完毕！</h3>
    </div>
    <div class="pop-btn"><span class="active" id="overWindowBtn_score">确定</span></div>
</div>