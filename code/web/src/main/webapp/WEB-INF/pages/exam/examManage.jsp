<%--
  Created by IntelliJ IDEA.
  User: Caocui
  Date: 2015/8/4
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div id="kaoshi-view">
    <div class="class-table">
        <div class="class-list">
            <%--<label>学年<select id="gradeList">--%>
                <%--<c:forEach items="${grades}" var="grade">--%>
                    <%--<option value="${grade.id}">${grade.name}</option>--%>
                <%--</c:forEach>--%>
            <%--</select></label>--%>
            <label>年级<select id="gradeList">
                <c:forEach items="${grades}" var="grade">
                    <option value="${grade.id}">${grade.name}</option>
                </c:forEach>
            </select></label>
            <%--<button type="button" class="gray-line-btn" id="queryByGrade">确定</button>--%>
        </div>
                            <span class="clearfix class-add">
                                <a href="javascript:void(0)" id="newExamInfo" class="green-btn" style="background: #3999d5;border:none;">新建考试</a>
                            </span>
        <table class="newTable" width="100%">
            <thead>
            <th width="50">#</th>
            <th width="100">考试名称</th>
            <th width="80">类型</th>
            <th width="100">考试日期</th>
            <th width="100">考试科目</th>
            <th width="100">小分结构</th>
            <%--<th>说明</th>--%>
            <th width="100">操作</th>
            </thead>
            <tbody id="examList">
            </tbody>
        </table>
        <div class="new-page-links" id="exam-manage-pagination">
        </div>
    </div>
    <div class="class-edit">
        <a href="#" class="back-link">&lt; 返回</a>
        <input type="hidden" id="currDate" value="${currDate}">

        <h3>基本设置</h3>

        <form id="examForm" class="set-form">
            <table class="basic-set">
                <input type="hidden" name="id" id="examId"/>
                <input type="hidden" name="gradeId" id="examGradeId" value="${gradeId}"/>
                <input type="hidden" name="gradeName" id="examGradeName" value="${gradeName}"/>
                <tr>
                    <th width="75">考试名称</td>
                    <th width="75">考试类型</td>
                    <th width="75">考试时间</td>
                </tr>
                <tr>
                    <td><input type="text" name="name" id="examName"/></td>
                    <td><select name="examType" id="examType">
                        <option value="0">期中</option>
                        <option value="1">期末</option>
                        <option value="2">其他</option>
                    </select></td>
                    <td><input type="text" onclick="WdatePicker()" readonly name="date"
                               id="examDate"/>
                    </td>
                </tr>
                <!--
                <tr>
                    <td valign="top">考试科目</td>
                    <td>
                        <label><input type="checkbox" id="selectAllSubject"/>全选</label>

                        <div class="checkbox-list" id="gradeSubjectContainer">
                            <c:forEach var="subject" items="${subjects}">
                                <label><input type="checkbox" class="gradeSubjects"
                                              value="${subject.id}">${subject.name}</label>
                            </c:forEach>
                        </div>
                        <table class="gray-table" width="550">
                            <thead>
                            <th>科目名称</th>
                            <th width="80">满分</th>
                            <th width="80">优秀分</th>
                            <th width="80">及格分</th>
                            <th width="80">低分</th>
                            <th width="100">日期</th>
                            <th width="100">时间</th>
                            </thead>
                            <tbody id="selectedSubjects">
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td valign="top">考试说明</td>
                    <td><textarea id="examRemark" name="remark"></textarea></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <button type="button" class="orange-btn" id="submit-exam-form">确定</button>
                    </td>
                </tr>
                -->
            </table>
            <div class="score-set" id="model">
                <span>模式</span>
                <label><input type="radio" name="type" value="1">普通</label>
                <label><input type="radio" name="type" value="2">3+3</label>
            </div>
            <div class="sub-select">
                <span>选择学科</span>
                <label><input type="checkbox" id="selectAllSubject"/>全选</label>
                <div class="checkbox-list" id="gradeSubjectContainer" style="display: inline-block;">
                    <c:forEach var="subject" items="${subjects}">
                        <label><input type="checkbox" class="gradeSubjects"
                                      value="${subject.id}">${subject.name}</label>
                    </c:forEach>
                </div>
            </div>
            <%--<button class="gray-line-btn" style="margin-top: 15px;">保存</button>--%>
            <div class="sup-set">
                <div class="sup-tab">
                    <ul class="clearfix">
                        <li class="m-cur" id="GJXX"><a href="javascript:;">高级选项</a></li>
                        <%--<li id="YLLSZ"><a href="javascript:;">优良率设置</a></li>--%>
                        <%--<li id="FSDSZ"><a href="javascript:;">分数段设置</a></li>--%>
                    </ul>
                </div>
                <div class="set-main">
                    <div class="sub-1" id="tab-GJXX">
                        <table class="sup-table">
                            <thead>
                            <tr>
                                <th style="width:15%;">学科</th>
                                <th style="width:15%;">总分</th>
                                <th style="width:15%;">优秀分</th>
                                <th style="width:15%;">合格分</th>
                                <th style="width:15%;">低分</th>
                                <th style="width:15%;">日期</th>
                                <th style="width:15%;">时间</th>
                            </tr>
                            </thead>
                            <tbody id="selectedSubjects">
                            <%--<tr>--%>
                            <%--<td>语文</td>--%>
                            <%--<td><input type="input" value="100"></td>--%>
                            <%--<td><input type="input" value="60"></td>--%>
                            <%--<td><input type="input" value="80"></td>--%>
                            <%--<td><input type="input" value="80"></td>--%>
                            <%--</tr>--%>
                            </tbody>
                        </table>
                    </div>
                    <div class="sub-2" id="tab-YLLSZ">
                        <table class="sup-table">
                            <thead>
                            <tr>
                                <th style="width:18%;">学科</th>
                                <th style="width:24%;">优秀率</th>
                                <th style="width:24%;">及格率</th>
                                <th style="width:24%;">低分率</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>语文</td>
                                <td><input type="text" value="大于等于总分90%" style="width:120px;"></td>
                                <td><input type="text" value="大于等于总分90%" style="width:120px;"></td>
                                <td><input type="text" value="大于等于总分90%" style="width:120px;"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="sub-3" id="tab-FSDSZ">
                        <table class="sup-table">
                            <thead>
                            <tr>
                                <th style="width:18%;">分数段1</th>
                                <th style="width:24%;">分数段2</th>
                                <th style="width:24%;">分数段3</th>
                                <th style="width:24%;">分数段4</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><input type="input" value="100" style="width: 40px;border:1px solid #dadada;text-align: center;"></td>
                                <td>0~<input type="input" value="60" style="width: 40px;border:1px solid #dadada;text-align: center;"></td>
                                <td><input type="input" value="60" style="width: 40px;border:1px solid #dadada;text-align: center;">~<input type="input" value="80" style="width: 40px;border:1px solid #dadada;text-align: center;"></td>
                                <td><input type="input" value="80" style="width: 40px;border:1px solid #dadada;text-align: center;">~100</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </form>
        <button class="gray-line-btn" style="margin-top: 30px;" id="submit-exam-form">确认</button>
    </div>
    
    <!-- 小分设置页面 -->
<div class="exam-item">
		<a href="#" class="back-link">&lt; 返回</a>
        <h3 id="examItemTitle"></h3>
                           
        <table class="newTable" id="examItemConfigTable" style="width: 490px;">
            <thead>
            <th width="120px">科目名称</th>
            <th width="100px">满分</th>
            <th width="120px">小分结构</th>
            <th width="150px">小分结构查看</th>
            </thead>
            <tbody id="examItemList">
            </tbody>
        </table>
        
</div>
</div>


<div class="pop-wrap" id="viewKaoshiId">
    <div class="pop-title">考试科目查看</div>
    <div class="pop-content">
        <h3 id="detailExamSubjectTitle">初一期末考试</h3>
        <table class="newTable" width="100%">
            <thead>
            <th>科目名称</th>
            <th width="60">满分</th>
            <th width="80">日期</th>
            <th width="80">星期</th>
            <th width="100">时间</th>
            </thead>
            <tbody id="detailExamSubjectList">
            </tbody>
        </table>
    </div>
    <div class="pop-btn"><span class="active">关闭</span></div>
</div>

<div class="pop-wrap daoru" id="importExamItemWindow">
    <div class="pop-title">小分结构导入</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importExamItemForm" method="post">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="exportExamItemModelBtn">生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li><i class="fomartImg"></i></li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadExamItemInput" name="examItemData" type="file" accept=".xls,.xlsx">
                </li>
                
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="importExamItemBtn">导入</span><span id="cancelImportExamItem">取消</span></div>
</div>

<div class="pop-wrap hz-pop hide" id="examItemDetail">
		<div class="pop-title">小分结构详情</div>
        <div class="pop-content" id="examItemDetailContent">
             <ul id="treeDemo" class="ztree"></ul>
        </div>
        <div class="pop-btn"><span id="examItemDetailCloseBtn">关闭</span></div>
</div>



<!-- 导出用表单 -->
	<form action="/examItem/exportExamItemModel.do" method="post" id="exportExamItemModelForm" style="display:none" >
		<input type="text" id="eIdForItem" name="examId" value=""/>
		<input type="text" id="sIdForItem" name="subjectId" value=""/>	
	</form>