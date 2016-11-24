<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script>
    function aa(){
        window.location.reload();
    }
</script>
<%--<%@ include file="/business/common/right.jsp" %>--%>

<div class="questionList" ng-controller="QuestionListController as ctrl" style="position: relative;top:10px;">

    <div class="questionnaire-list-header">
        <%--<span><button class="returnIndex" onclick="showContent('discuss')"></button></span>--%>
        <span><h1  style="font-size: 20px;padding-left: 20px;padding-top: 5px;">问卷调查</h1></span>
        <a class="questionnaire-btn-new vertical-fix" target="_blank" href="/questionnaire#/new" ng-hide="student">新建问卷</a>
        <span class="selectMe" ng-hide="student">
            <label><input type="radio" name="viewType" ng-model="viewType" value="all">浏览全部</label>
            <label><input type="radio" name="viewType" ng-model="viewType" value="self">我发布的</label>
        </span>

    </div>
    <ul class="questionnaire-ul">
        <li class="questionnaire-ul-thead">
            <span class="listName-thead" style="margin-left: 15px;"> 问卷名称</span>
            <span class="absent-member-thead" ng-if="!student" >参与状态</span>
            <span class="question-publisher-thead">发布人</span>
            <span class="question-deadline">截止时间</span>
            <span class="questionlist-operation-thead">操作</span>
        </li>
        <li ng-repeat="questionnaire in ctrl.page.content | filter:{isOwner:true}:isComp track by questionnaire.id">
            <div
            ${currentUser.role==2?"style='background-position-x:230px;'":""}
            ${currentUser.role==1?"style='background-position-x:230px;'":""}
            <%--style="background-position-x:230px;"--%> class="listForDetail listForInline" ng-class="{'self-questionnaire': questionnaire.isOwner}" ng-switch
                 on="questionnaire.isOwner">
                <span class="questionlistName" style="padding-left: 40px;">
                    {{questionnaire.name}}
                </span>
                <span class="absent-member" ng-if="!student">&nbsp;
                    <a class="underline" ng-if="questionnaire.isOwner && questionnaire.nonpartCount"
                       ng-click="ctrl.toggleUserDialog(questionnaire)" stop-event>{{questionnaire.nonpartCount}}人未参加</a>
                    <div class="absent-popup-container" ng-class="{'pop-left': nameTooLong(questionnaire)}" ng-show="questionnaire.showUsers" stop-event>
                        <div ng-if="questionnaire.isOwner && questionnaire.nonpartCount"
                             class="part-user-container absent-popup">
                            <button ng-click="ctrl.notify(questionnaire)">私信通知</button>
                            <ul class="part-user-list">
                                <li ng-repeat="user in questionnaire.users | limitTo:12">
                                    <!--<img src="{{user.maxImageURL}}">-->
                                    <div>{{user.nickName}}</div>
                                </li>
                            </ul>
                            <a href="/questionnaire#/allRelevant/{{questionnaire.id}}" ng-show="{{questionnaire.nonpartCount}} > 12" class="quest-view-more" target="_blank">查看全部>></a>
                        </div>
                    </div>
                </span>
                <span class="question-publisher">&nbsp;{{questionnaire.publishName}}</span>
                <span class="question-deadline" ng-class="{urgent: isUrgent(questionnaire.endDate)}">{{questionnaire.endDate | deadLine}}&nbsp;</span>
                <button ng-switch-when="true" class="delete" ng-click="remove(questionnaire.id)">删除</button>
                <a ng-switch-when="true" class="scan" target="_blank"
                   href="/questionnaire#/static/{{questionnaire.id}}">查看统计</a>

                <div ng-switch-default ng-switch on="questionnaire.answered || isExpired(questionnaire.endDate)"
                     class="operation-inline" style="position: relative;float:right;">
                    <a ng-switch-when="false" target="_blank" class=" questionnaire-btn-fill vertical-fix" style="margin-top: 7px;"
                       href="/questionnaire#/answer/{{questionnaire.id}}">填写问卷</a>
                    <span class="questionnaire-filled vertical-fix" ng-switch-default>{{questionnaire.answered?'已填写':'已结束'}}</span>
                </div>
            </div>


        </li>
    </ul>

    <div class="questionnaire-pagination-container">
        <pagination boundary-links="true" total-items="ctrl.page.totalElements" items-per-page="ctrl.page.size" ng-model="ctrl.currentPage" ng-change="ctrl.pageChanged()" class="pagination-sm" previous-text="上一页" next-text="下一页" first-text="第一页" last-text="最后一页"></pagination>
    </div>

</div>
