/**
 * Created by Niu Xin on 15/4/22.
 */

(function () {
    var app = angular.module('exam-index', ['ui.bootstrap']);

    app.directive('getClasses', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                if (typeof scope.classList == 'undefined') {
                    scope.classList = [];
                }
                scope.classList.push({
                    id: attrs.value,
                    name: attrs.title
                });
            }
        }
    });

    app.factory('ExamData', ['$http', function ($http) {
        return {
            page: 1,
            pageSize: 12,
            total: 0,
            examList: [],
            loaded: false,
            
            
            redirect:function()
            {
            	
            },
            loadPage: function () {
                var _this = this;
                _this.loaded = false;
                $http({
                    url: "/exam/teacher/list.do",
                    params: {skip: (_this.page - 1) * _this.pageSize, limit: _this.pageSize},
                    method: 'POST',
                    cache: false
                }).success(function (response) {
                    _this.examList = response.list;
                    _this.total = response.count;
                })['finally'](function () {
                    _this.loaded = true;
                });
            },
            remove: function (examId) {
                for (var i in this.examList) {
                    var exam = this.examList[i];
                    if (exam.id == examId) {
                        this.examList.splice(i, 1);
                    }
                }
            },
            pushClass: function (examId, classId) {
                var exam;
                for (var i in this.examList) {
                    if (this.examList[i].id == examId) {
                        exam = this.examList[i];
                        break;
                    }
                }
                if (exam != null) {
                    for (var i in exam.classes) {
                        var classInfo = exam.classes[i];
                        if (classInfo.idStr == classId) {
                            exam.classes.splice(i, 1);
                            exam.alreadClasses.push(classInfo);
                        }
                    }
                }
            }
        }
    }]);

    app.controller('ExamUploadCtrl', ['$scope', 'ExamData', function ($scope, examData) {
        $scope.submitWord = function () {
            var k = checkForm();

            if (k) {
                if ($("#answerWord1").val() == null || $.trim($("#answerWord1").val()) == "") {
                    $("#answerWord1").remove();
                }
                var idss = '';
                var ids = $("input[name='pushClass']");
                for (var i = 0; i < ids.length; i++) {
                    var classObject = ids[i];
                    if ($(classObject).prop("checked")) {
                        idss += $(classObject).val() + ',';
                    }
                }
                idss = idss.substring(0, idss.length - 1);
                $("#classIds").val(idss);
                //$("#submitWord").submit();
                MessageBox('文档解析中......，请您耐心等待:）', 0);
                fileUpload($("#submitWord").get(0), function (response) {
                	//alert(response);
                	//alert(message);
                    MessageBox('上传完毕。即将进入配置页面', 1);
                    window.location.href='/exam/view/'+response;
                   // examData.page = 1;
                   // examData.loadPage();
                })
            }
        };
    }]);

    app.controller('ExamListCtrl', ['$scope', '$http', '$window', 'ExamData', function ($scope, $http, $window, examData) {

        $scope.examData = examData;

        //TODO
        $scope.correctPaper = function (paperId) {
            $window.location.href = '/exam/answer/stat/list.do?id=' + paperId;
        };

        //TODO 暂无检查接口
        $scope.configPaper = function (paperId) {
        	
        	 $http({
                 url: '/exam/canconf.do',
                 params: {
                     id: paperId
                 },
                 method: 'POST'
             }).success(function (response) {
                 if (response.code == 200) {
                	 $window.location.href = '/exam/view/' + paperId;
                 } else {
                	 jQuery("#btn_"+paperId).hide();
                     alert("已经有学生答题，不能再次配置了");
                 }
             }).error(function (e) {
                 MessageBox("服务器错误。", -1);
             });
        };

        //删除试卷
        $scope.deletePaper = function (paperId) {
            var key = confirm("确定删除吗？");
            if (!key) return;
            $http({
                url: '/exam/delete.do',
                params: {
                    id: paperId
                },
                method: 'POST'
            }).success(function (response) {
                if (response.code == 200) {
                    MessageBox("删除成功！", 1);
                    examData.remove(paperId);
                } else {
                    MessageBox(response.message, -1);
                }
            }).error(function (e) {
                MessageBox("删除失败：服务器错误。", -1);
            });
        };

        //推送班级
        $scope.pushPaper = function (paperId, classId) {
            $http({
                url: '/exam/class/add.do',
                params: {
                    id: paperId,
                    classId: classId
                },
                method: 'POST'
            }).success(function (response) {
                if (response.code == 200) {
                    MessageBox("推送成功！", 1);
                    examData.pushClass(paperId, classId);
                } else {
                    MessageBox(response.message, -1);
                }
            }).error(function (e) {
                MessageBox("推送失败：服务器错误。", -1);
            });
        }

    }]);

    app.controller('ExamListPaginationCtrl', ['$scope', 'ExamData', function ($scope, examData) {
        $scope.examData = examData;
        examData.loadPage();
    }]);

})();


function checkClass() {
    var inputs = $("input[name='pushClass']");
    for (var i = 0; i < inputs.length; i++) {
        var k = $(inputs[i]).prop('checked')
        if (k) return true;
    }
    return false;

}

function checkForm() {
    var flag = checkClass();
    if (!flag) {
        $("#className").css("display", "block");
        return;
    } else {
        $("#className").css("display", "none");
    }
    var practiseName = $("#practiseName").val();
    
    
    var questWord = $("#questWord").val();
    var answerWord = $("#answerWord").val();
    if (practiseName == null || $.trim(practiseName) == "" ||practiseName.length>20) {
        alert("试卷名字不为空 并且不超过20个字符");
        return false;
    } else if (questWord == null || $.trim(questWord) == "") {
        alert("请上传试卷");
        return false;
    } else if (suffix(questWord) != ".doc" && suffix(questWord) != ".docx") {
        alert("只能上传word文档(格式为doc或docx)");
        return false;
    } else if ($.trim(answerWord) != "") {
        if (suffix(answerWord) != ".doc" && suffix(answerWord) != ".docx") {
            alert("只能上传word文档(格式为doc或docx)");
            return false;
        }
    }
    return true;

}

function suffix(str) {
    var k = str.lastIndexOf(".");
    return str.substring(k, str.length);
}