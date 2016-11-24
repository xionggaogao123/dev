/**
 * Created by Tony on 2015/1/6.
 */
app.factory('QuestionService', ['$http', '$q', '$window', function ($http, $q, $window) {
    /**
     * 计算未参加的人数
     * @param q 问卷信息
     */
    var calcNonparts = function (q) {
        /*var partCount = 0;
        if (q.respondents != null) {
            partCount = Object.keys(q.respondents).length;
        }
        q.nonpartUsers = [];
         if (q.userIds) {
            for (var i in q.users) {
                var user = q.users[i];
                if (q.respondents == null || q.respondents[user.id] == null) {
                    q.nonpartUsers.push(user);
                }
            }
        }
         q.nonpartCount = q.nonpartUsers.length;*/
        q.nonpartCount = q.userIds ? q.userIds.length : 0;
    };
    /**
     * 计算问卷是否自己发布的，是否已填写
     * @param q 问卷信息
     */
    var calcOwner = function (q) {
        q.isOwner = userId == q.publisher;
        if (!q.isOwner) {
            var respondents = q.respondents;
            q.answered = respondents != null && respondents[userId] != null;
        }
    };
    /**
     * 初始化问卷信息列表
     * @param list 问卷列表
     */
    var wrapList = function (list) {
        for (var i in list) {
            var q = list[i];
            calcNonparts(q);
            calcOwner(q);
        }
    };
    var getAnswers = function (length) {
        if (length < 0) {
            length = -length;
        }
        var answers = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
        return answers.slice(0, length);
    };
    /**
     * 统计数据计算
     * @param q 问卷信息
     */
    var staticAnswers = function (q) {
        var answerSheet = q.answerSheet,
            respondents = q.respondents,
            answers = [],
            answerRatios = [];
        for (var i in answerSheet) {
            var question = answerSheet[i];
            var answerCount = [];
            var ratios = [];
            if (question != 0 && question<10000) {
                //选择题
                var qCount = question < 0 ? -question : question;
                for (var j = 0; j < qCount; j++) {
                    answerCount[j] = 0;
                    ratios[j] = 0;
                }
            }
            answers.push(answerCount);
            answerRatios.push(ratios);
        }
        if (respondents) {
            for (var userId in respondents) {
                var userAnswers = respondents[userId];
                for (var i in answerSheet) {
                    var answerCount = answers[i],
                        question = answerSheet[i],
                        answer = userAnswers[i];
                    if (question > 0 && question<10000) {
                        //单选
                        answerCount[answer]++;
                    } else if (question < 0) {
                        //多选
                        for (var j in answer) {
                            answerCount[answer[j]]++;
                        }
                    } else {
                        //问答题
                        answerCount.push(answer);
                    }
                }
            }
            for (var i in answers) {
                var answerCount = answers[i],
                    question = answerSheet[i],
                    ratios = answerRatios[i];
                if (question != 0) {
                    var max = 0;
                    for (var j in answerCount) {
                        var count = answerCount[j];
                        if (max < count) {
                            max = count;
                        }
                    }
                    for (var j in answerCount) {
                        var count = answerCount[j];
                        ratios[j] = (count / max);
                    }
                }
            }
        }
        q.answers = answers;
        q.answerRatios = answerRatios;
    };
    /**
     * 缓存问卷的全部未参加人员ID
     */
    var nonPartIds = {};
    var getNonPartIds = function (qId) {
        var nonPartIdsOfQ = nonPartIds[qId];
        if (!nonPartIdsOfQ) {
            return $http.get('/questionnaire/nonParts.do', {
                params: {
                    id: qId
                }
            }).then(function (response) {
                nonPartIds[qId] = response.data;
                return nonPartIds[qId];
            });
        } else {
            return nonPartIdsOfQ;
        }
    };
    return {
        query: function (number, size) {
            return $http.get('/questionnaire/list.do',
                {
                    params: {
                        page: number,
                        size: size
                    }
                }).then(function (response) {
                    var data = response.data;
                    wrapList(data.content);
                    return data;
                });
        },
        remove: function (id) {
            return $http.post('/questionnaire/delete.do', null,
                {
                    params: {
                        id: id
                    }
                });
        },
        statistic: function (id) {
            return $http.get('/questionnaire/stat.do', {
                params: {
                    id: id
                }
            }).then(function (response) {
                var data = response.data;
                calcNonparts(data);
                staticAnswers(data);
                return data;
            });
        },
        getAnswers: getAnswers,
        add: function (data) {
            return $http.post('/questionnaire/new.do', {}, {
                params: data
            }).then(function (response) {
                return response.data;
            });
        },
        add2: function (data) {
            return $http.post('/yunying/new.do', {}, {
                params: data
            }).then(function (response) {
                return response.data;
            });
        },
        view: function (id) {
            return $http.get('/questionnaire/view.do', {
                params: {
                    id: id
                }
            }).then(function (response) {
                return response.data;
            });
        },
        answer: function (id, answers) {
            return $http.post('/questionnaire/answer.do', {
                id: id,
                answers: answers
            });
        },
        getUnread: function () {
            return $http.get('/questionnaire/unread.do').then(function (response) {
                return response.data;
            });
        },
        notify: function (content, userIds) {
            return $http.post('/questionnaire/notify.do', {}, {
                params: {
                    content: content,
                    userIds: userIds
                }
            }).then(function (response) {
                return response.data;
            });
        },
        getNonParts: function (qId, page, size) {
            return $q.when(getNonPartIds(qId))
                .then(function (nonPartIdsOfQ) {
                    //分页
                    var end = (page + 1) * size;
                    var total = nonPartIdsOfQ.length;
                    return nonPartIdsOfQ.slice(end - size, end > total ? total : end);
                }).then(function (ids) {
                    //获取用户信息
                    return $http.get('/users.do', {
                        params: {
                            userIds: ids
                        }
                    }).then(function (response) {
                        return response.data;
                    });
                }).then(function(content){
                    return {
                        content: content,
                        totalElements: nonPartIds[qId].length
                    };
                });
        },
        getNonPartsId: function(qId){
            return getNonPartIds(qId);
        },
        exportStats: function (qId, type, charts) {
            var form = $('<form>', {
                target: '_blank',
                method: 'POST',
                action: 'questionnaire/export.do'
            }).appendTo('body');
            form.append($('<input>', {
                name: 'id',
                value: qId
            })).append($('<input>', {
                name: 'type',
                value: type
            }));
            $.each(charts, function (i, chart) {
                form.append($('<input>', {
                    name: 'chart',
                    value: chart
                }));
            });
            form.submit();
            form.remove();
        }
    };
}]);