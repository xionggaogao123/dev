/**
 * Created by Tony on 2015/1/6.
 */
/**
 * 公用Controller
 */
app.controller('QuestionCtrl', ['$window', '$scope', function ($window, $scope) {
    $.extend($scope, {
        closeWindow: function () {
            $window.close();
        },
        goBack: function (type) {
            var refresh = $window.opener ? $window.opener.refresh : null;
            if (refresh) {
                refresh(type);
            }
            $window.close();
        }
    });
}])
/**
 * 问卷列表controller
 */
    .controller('QuestionListController', ['QuestionService', '$window', '$scope', '$filter', function (service, $w, $scope, $filter) {
        var ctrl = this;
        ctrl.page = {};
        ctrl.currentPage = 1;
        var closeAllPopup = function () {
            for (var i in ctrl.page.content) {
                var item = ctrl.page.content[i];
                item.showUsers = false;
            }
        };
        $scope.$parent.clickBody = closeAllPopup;
        var query = function (number, size) {
            number = number == null ? ctrl.page.number : number;
            size = size == null ? ctrl.page.size : size;
            service.query(number, size).then(function (data) {
                ctrl.page = data;
                //$scope.$parent.getUnread();
            });
        };
        $scope.$parent.queryQuestionnaire = function () {
            query(0, 15);
        };
        var getDaysFromNow = function (dateStr) {
            var date = moment(dateStr, 'YYYYMMDD HH:mm:ss');
            if (!date.isValid()) {
                return false;
            }
            return date.diff(moment(), 'days');
        };
        $.extend($scope, {
            student: userRole == 1 || userRole == 4,
            viewType: 'all',
            isComp: function (actual, expected) {
                return actual == expected || $scope.viewType == 'all';
            },
            remove: function (id) {
                if ($w.confirm('是否删除问卷?')) {
                    service.remove(id).then(function () {
                        query();
                    });
                }
            },
            nameTooLong: function (q) {
                return q.name.lengthInBytes() > 45;
            },
            isUrgent: function (dateStr) {
                var days = getDaysFromNow(dateStr);
                if (days == false) {
                    return false;
                }
                return days >= 0 && days <= 3;
            },
            isExpired: function (dateStr) {
                var days = getDaysFromNow(dateStr);
                if (days == false) {
                    return false;
                }
                return days < 0;
            }
        });
        ctrl.nextPage = function () {
            query(ctrl.page.number + 1, ctrl.page.size);
        };
        ctrl.prevPage = function () {
            query(ctrl.page.number - 1, ctrl.page.size);
        };
        ctrl.pageChanged = function () {
            query(ctrl.currentPage - 1, ctrl.page.size);

        };
        ctrl.toggleUserDialog = function (q) {
            for (var i in ctrl.page.content) {
                var item = ctrl.page.content[i];
                if (item != q) {
                    item.showUsers = false;
                } else {
                    q.showUsers = !q.showUsers;
                }
            }
        };
        ctrl.showNonparts = function (q) {
            /*if(!q.userDialog){
             q.userDialog = new dialog({
             align: 'bottom',
             content: $('#'+ q.id+'-user-dialog')
             });
             }
             q.userDialog.show(evt);*/
            q.showUsers = true;
        };
        ctrl.hideNonparts = function (q) {
            q.showUsers = false;
        };
        ctrl.notify = function (q) {
            var content = prompt('请输入私信内容', '快来参加问卷调查：' + q.name + '吧！');
            if (content) {
                service.notify(content, q.userIds);
            }
        };
        $w.refresh = function (type) {
            switch (type) {
                case 'new':
                    MessageBox('发布问卷成功', 1);
                    break;
                case 'answer':
                    MessageBox('填写问卷成功', 1);
                    break;
            }
            query();
        }
    }])

/**
 * 新建问卷controller
 */
    .controller('NewQuestCtrl', ['$http', '$scope', '$timeout','$location', 'FileUploader', 'QuestionService', function ($http, $scope, $timeout,$location, FileUploader, service) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/questionnaire/upload1.do',
            autoUpload: true
        });

        uploader.onSuccessItem = function (item, response) {
            $scope.uploading = false;
            $scope.uploadDone = true;
            $timeout(function () {
                $scope.uploadDone = false;
            }, 1000);
            $scope.name = item.file.name.substring(0, item.file.name.lastIndexOf('.'));
            if (response.search(/^<PRE/) >= 0) {
                response = $(response).html();
            }
            $scope.docUrl = response;
            $scope.ie = false;
            if(!!window.ActiveXObject || "ActiveXObject" in window){

                if(response.substr(response.length-3) == 'pdf') {
                    previewSwf(response.substr(0,response.length-3) + 'swf');
                    $scope.ie = true;
                }

            }


        };
        uploader.onBeforeUploadItem = function (item) {
            $scope.uploading = true;
        };
        //========================
        function previewSwf(url){
            var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape(url),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',
                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,
                    localeChain: 'en_US'
                }});
        }

        //==================================
        var ctrl = this;

        $scope.math = Math;
        $.extend($scope, {
            name: '新建问卷',
            endDateOpt: {
                minDate: 0
            },
            batchAnswerSet: 4,
            getAnswers: service.getAnswers,
            addBatch: function(){
                $scope.batchAnswerSet += $scope.batchAnswerSet > 0 ? 1:-1;
            },
            minusBatch: function(){
                $scope.batchAnswerSet += $scope.batchAnswerSet > 0 ? -1:1;
            },
            removeQuestion: function (index) {
                ctrl.answerSheet.splice(index, 1);
            },
            move: function (index, offset) {
                var tmp = ctrl.answerSheet[index];
                var dest = index + offset;
                ctrl.answerSheet[index] = ctrl.answerSheet[dest];
                ctrl.answerSheet[dest] = tmp;
            },
            chooseType: function (index, answer) {
                if(arguments.length == 1){
                    $scope.batchAnswerSet = index;
                }else{
                    ctrl.answerSheet[index] = answer;
                }
            },
            answerAdd: function (index) {
                var answerSet = ctrl.answerSheet[index];
                answerSet = answerSet > 0 ? answerSet + 1 : answerSet - 1;
                ctrl.answerSheet[index] = answerSet;
            },
            answerMinus: function (index) {
                var answerSet = ctrl.answerSheet[index];
                answerSet = answerSet > 0 ? answerSet - 1 : answerSet + 1;
                ctrl.answerSheet[index] = answerSet;
            },
            batchAddToSheet: function(){
                //alert($scope.batchCount);
                for(var i = 0; i < $scope.batchCount; i++){
                    if($scope.batchAnswerSet>10000){
                        var minscoreBatch = parseInt($("input[name='batchMinScore']")[0].value);
                        var maxscoreBatch = parseInt($("input[name='batchMaxScore']")[0].value);

                        $scope.batchAnswerSet = 100000000+minscoreBatch*10000+maxscoreBatch;

                    }
                    ctrl.answerSheet.push($scope.batchAnswerSet);
                }
                ctrl.showBatch = false;
            }
        });
        ctrl.classes = [];
        ctrl.classIds = {};
        ctrl.schoolChecked = true;
        ctrl.respondentCheck = [
            {
                id: 'parent',
                name: '家长',
                checked: false
            }, {
                id: 'student',
                name: '学生',
                checked: false
            }, {
                id: 'teacher',
                name: '老师',
                checked: false
            }, {
                id:'headmaster',
                name:'校领导',
                checked: false
            }
        ];
        ctrl.answerSheet = [
            4
        ];
        ctrl.checkAll = function () {
            //选择全校取消班级
            if (ctrl.schoolChecked) {
                ctrl.classIds = {};
            }
            //选择全部，勾选所有角色
            if (ctrl.allRespondent) {
                for (var i in ctrl.respondentCheck) {
                    var check = ctrl.respondentCheck[i];
                    check.checked = true;
                }
            }
        };
        ctrl.checkChange = function () {
            //取消角色勾选，取消全部
            for (var i in ctrl.respondentCheck) {
                var check = ctrl.respondentCheck[i];
                if (!check.checked) {
                    ctrl.allRespondent = false;
                }
            }
            //勾选班级，取消全校
            ctrl.schoolChecked = $.isEmptyObject(ctrl.classIds);
        };
        ctrl.getAnswers = service.getAnswers;
        ctrl.increaseAnswerSheet = function () {
            ctrl.answerSheet.push(4);
        };
        ctrl.toggleShowBatch = function(){
            if(ctrl.showBatch){
                ctrl.showBatch = false;
            }else{
                ctrl.showBatch = true;
            }
        }
        ctrl.submit = function () {
            var classIds = [];
            for (var classId in ctrl.classIds) {
                if (ctrl.classIds[classId]) {
                    classIds.push(classId);
                }
            }

            //取打分题的分数
            var minscorearray = $("input[name^=minScore]");
            var maxscorearray = $("input[name^=maxScore]");
            for(var sheetidx = 0;sheetidx<ctrl.answerSheet.length;sheetidx++){
                if(ctrl.answerSheet[sheetidx]>10000){
                    var minscore = parseInt(minscorearray[sheetidx].value);
                    var maxscore = parseInt(maxscorearray[sheetidx].value);
                    if(minscore<maxscore && minscore>=0 && maxscore<=9999){
                        ctrl.answerSheet[sheetidx] = 100000000+minscore*10000+maxscore;
                    }
                    else if(minscore<0){
                        alert('打分题最低分要大于等于0');
                        return false;
                    }else if(minscore>=maxscore){
                        alert('打分题最低分要小于等于最高分');
                        return false;
                    }else {
                        alert('打分题分数要在0-9999范围内');
                        return false;
                    }
                }
            }

            var data = {
                name: $scope.name,
                docUrl: $scope.docUrl,
                classIds: classIds,
                answerSheet: ctrl.answerSheet,
                endDate: $scope.endDate
            };
            var respondentFlag = false;
            for (var i in ctrl.respondentCheck) {
                var respondent = ctrl.respondentCheck[i];
                data[respondent.id + 'Respondent'] = respondent.checked;
                if (respondent.checked) {
                    respondentFlag = true;
                }
            }
            if (!$scope.docUrl) {
                alert('请上传文档');
                return false;
            }
            if ($.trim($scope.name) == '') {
                alert('请输入问卷名称');
                return false;
            }
            if (!ctrl.schoolChecked && classIds.length == 0) {
                alert('请选择调查范围');
                return false;
            }
            if (!respondentFlag) {
                alert('请至少选择一种参加人员');
                return false;
            }
            if(!$scope.endDate){
                alert('请选择结束日期');
                return false;
            }
            if (!ctrl.answerSheet.length) {
                alert('请配置问题');
                return false;
            }

            service.add(data).then(function () {
                //$scope.$parent.goBack('new');
                //$location.url('/index1');
                alert('发布问卷成功');
                location.href = '/questionnaire/load.do';
            });
        };
        ctrl.submit2 = function () {
            var classIds = [];
            for (var classId in ctrl.classIds) {
                if (ctrl.classIds[classId]) {
                    classIds.push(classId);
                }
            }

            //取打分题的分数
            var minscorearray = $("input[name^=minScore]");
            var maxscorearray = $("input[name^=maxScore]");
            for(var sheetidx = 0;sheetidx<ctrl.answerSheet.length;sheetidx++){
                if(ctrl.answerSheet[sheetidx]>10000){
                    var minscore = parseInt(minscorearray[sheetidx].value);
                    var maxscore = parseInt(maxscorearray[sheetidx].value);
                    if(minscore<maxscore && minscore>=0 && maxscore<=9999){
                        ctrl.answerSheet[sheetidx] = 100000000+minscore*10000+maxscore;
                    }
                    else if(minscore<0){
                        alert('打分题最低分要大于等于0');
                        return false;
                    }else if(minscore>=maxscore){
                        alert('打分题最低分要小于等于最高分');
                        return false;
                    }else {
                        alert('打分题分数要在0-9999范围内');
                        return false;
                    }
                }
            }

            var data = {
                name: $scope.name,
                docUrl: $scope.docUrl,
                classIds: classIds,
                answerSheet: ctrl.answerSheet,
                endDate: $scope.endDate
            };
            var respondentFlag = false;
            for (var i in ctrl.respondentCheck) {
                var respondent = ctrl.respondentCheck[i];
                data[respondent.id + 'Respondent'] = respondent.checked;
                if (respondent.checked) {
                    respondentFlag = true;
                }
            }
            if (!$scope.docUrl) {
                alert('请上传文档');
                return false;
            }
            if ($.trim($scope.name) == '') {
                alert('请输入问卷名称');
                return false;
            }
            if (!ctrl.schoolChecked && classIds.length == 0) {
                alert('请选择调查范围');
                return false;
            }
            if (!respondentFlag) {
                alert('请至少选择一种参加人员');
                return false;
            }
            if(!$scope.endDate){
                alert('请选择结束日期');
                return false;
            }
            if (!ctrl.answerSheet.length) {
                alert('请配置问题');
                return false;
            }

            service.add2(data).then(function () {
                //$scope.$parent.goBack('new');
                //$location.url('/index1');
                alert('发布问卷成功');
                location.href = '/yunying/quesionload.do';
            });
        };
        ctrl.toggleBatchAdd = function(){
           alert(11);
        }
        var getClasses = function () {
            return $http.get('/questionnaire/classanddepart.do');
        };
        getClasses().then(function (response) {
            ctrl.classes = response.data;
        });
    }])
/**
 * 查看统计Controller
 */
    .controller('StaticCtrl', ['$routeParams', '$scope', 'QuestionService', function ($routeParams, $scope, service) {
        var qId = $routeParams.qId;

        function previewSwf(url){
            var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape(url),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',
                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,
                    localeChain: 'en_US'
                }});
        }

        $scope.math = Math;
        $scope.getavgscore =  function (scorearray) {
            var scoretotal = 0;
            for(var iidx=0;iidx<scorearray.length;iidx++){
                scoretotal+=scorearray[iidx];
            }
            if(scorearray.length==0){
                return "无人答题";
            }
            var avgscore = scoretotal/scorearray.length;
            return avgscore.toFixed(2);
        };
        service.statistic(qId).then(function (data) {
            $.extend($scope, data);
            var response= $scope.docUrl;
            $scope.ie = false;
            if(!!window.ActiveXObject || "ActiveXObject" in window){

                if(response.substr(response.length-3) == 'pdf') {
                    previewSwf(response.substr(0,response.length-3) + 'swf');
                    $scope.ie = true;
                }
            }
            // 设置数据
            $.each(data.answerSheet, function (i, answerSet) {


                var chartOpt = {
                    notMerge: true,
                    grid: {
                        x: '10%',
                        y: '10%',
                        x2: '10%',
                        y2: '10%',
                        borderWidth: 0
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        show: false,
                        data: ['答案统计']
                    },
                    xAxis: [
                        {
                            type: 'value',
                            boundaryGap: [0, 0.01],
                            splitLine: {
                                show: false
                            },
                            axisLabel: {
                                show: false
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'category',
                            data: [],
                            splitLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                margin: 10
                            }
                        }
                    ],
                    series: [
                        {
                            name: '答案统计',
                            type: 'bar',
                            data: [],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        distance: 50,
                                        position: 'insideRight'
                                    }
                                }
                            }
                        }
                    ]
                };
                var pieOpt = {
                    notMerge: true,
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b}<br/>{c}人, {d}%'
                    },
                    legend: {
                        show: true,
                        orient: 'vertical',
                        x: 'left',
                        data: []
                    },
                    series: [
                        {
                            name: '选项',
                            type: 'pie',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: false
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            },
                            data: []
                        }
                    ]
                };
                $.each(service.getAnswers(answerSet), function (j, answer) {
                    chartOpt.yAxis[0].data.push(answer);
                    chartOpt.series[0].data.push(data.answers[i][j]);
                    pieOpt.legend.data.push(answer);
                    pieOpt.series[0].data.push({value: data.answers[i][j], name: answer});
                });
                if (answerSet > 0) {
                    $scope.chartTypes[i] = 'pie';
                }
                $scope.chartOpts[i] = chartOpt;
                $scope.pieOpts[i] = pieOpt;
            });
        });
        $.extend($scope, {
            getAnswers: service.getAnswers,
            toggleExpand: function (answers) {
                answers.expanded = !answers.expanded;
            },
            getRatio: function (i, j) {
                return $scope.answerRatios[i][j] * 100 + '%';
            },
            chartOpts: [],
            pieOpts: [],
            chartTypes: [],
            chartObjs: [],
            exportCharts: function (type) {
                service.exportStats(qId, type, $.map($scope.chartObjs, function (chart) {
                    if(chart)
                        return chart.getDataURL();
                }));
            }
        });

    }])
/**
 * 填写问卷Controller
 */
    .controller('AnswerCtrl', ['$scope', '$routeParams', 'QuestionService', function ($scope, $routeParams, service) {
        var qId = $routeParams.qId;
        service.view(qId).then(function (data) {
            $.extend($scope, data);
            var response= $scope.docUrl;
            $scope.ie = false;
            if(!!window.ActiveXObject || "ActiveXObject" in window){

                if(response.substr(response.length-3) == 'pdf') {
                    previewSwf(response.substr(0,response.length-3) + 'swf');
                    $scope.ie = true;
                }
            }
        });

        function previewSwf(url){
            var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape(url),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',
                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,
                    localeChain: 'en_US'
                }});
        }

        $scope.math = Math;
        $scope.buildScoreValueArray =  function (min,max) {
            var retarray = [];
            for(var iidx = min;iidx<=max;iidx++){
                retarray.push(iidx);
            }
            return retarray;
        };
        $.extend($scope, {
            answers: [],
            getAnswers: service.getAnswers,
            submit: function () {
                var answers = $scope.answers;
                for (var i in $scope.answerSheet) {
                    var question = $scope.answerSheet[i];
                    if (question < 0) {
                        //多选处理
                        var answerObj = answers[i];
                        var answer = [];
                        for (var a in answerObj) {
                            if (answerObj[a]) {
                                answer.push(a);
                            }
                        }
                        answers[i] = answer;
                    }
                    //打分处理
                    if(question>10000){
                        answers[i] =$("#score"+i).val();
                    }
                }
                service.answer(qId, answers).then(function () {
                    //$scope.$parent.goBack('answer');
                    alert('填写成功');
                    location.href = '/questionnaire/load.do';
                });
            }
        });
    }])
    .controller('FillStatusCtrl',['$scope','$routeParams','QuestionService',function($scope,$routeParams,service){
        var ctrl = this;
        var qId = $routeParams.qId;
        $scope.size = 48;
        $scope.pageNumber = 1;
        var query = function(){service.getNonParts(qId,$scope.pageNumber-1,$scope.size).then(function(data){
            angular.extend($scope, data);
        });}
        $scope.pageChanged = function(){
            query();
        }
        query();
        $scope.notify = function () {
            service.view(qId)
                .then(function(questionnaire){
                    var name = questionnaire.name;
                    var userIds = service.getNonPartsId(qId);
                    var content = prompt('请输入私信内容', '快来参加问卷调查：' + name + '吧！');
                    if (content) {
                        service.notify(content, userIds);
                    }
                });
        };
    }]);