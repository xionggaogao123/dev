/**
 * Created by fl on 2016/4/27.
 */
define(function(require,exports,module){
    var Common = require('common');
    require('select2');
    var year = calculateSchoolYear();
    var evaluationId = $('body').attr('evid');
    var rootUrl = "/teacherevaluation/" + evaluationId + "/items";
    var teacherGroups;
    var TMPL = '';
    var MODE = -1; //-1为错误码， 1表示打分模式  2表示等级模式
    var MODETYPE = {
        SCOREMODE: 1,
        GRADEMODE: 2
    }


    var ue = UE.getEditor('evidenceEditor',{maximumWords:20000, initialFrameHeight:200});


    $(function(){


        init();
        showRule();
        getTeacherGroups();
        getKaohe();
        availableGroups();

        $('.layout-nav .span1').click(function(){//老师填写个人陈述
            if(!checkPersonalTime()){
                return;
            }
            $('.div-px').hide();
            $('.px1').show();
            getTeacherStatement();
        });
        $('.layout-nav .span2').click(function(){//校长查看个人陈述
            $('.div-px').hide();
            $('.px2').show();
            showFirstTeacherStatement();
        });
        $('.layout-nav .span3').click(function(){//管理员填写实证资料和量化成绩
            if(!checkPersonalTime()){
                return;
            }
            $('.div-px').hide();
            $('.px3').show();
            showFirstTeacherEvidence();
        });
        $('.layout-nav .span4').click(function(){//校长、老师查看实证资料和量化成绩
            $('.div-px').hide();
            $('.px4').show();
            showFirstTeacherEvidenceForLeader();
        });
        //$('.layout-nav .span5').click(function(){//小组互评
        //    if(!checkEvaluationTime()){
        //        return;
        //    }
        //    $('.div-px').hide();
        //    $('.px5').show();
        //    getMyGroupTeachers();
        //});
        //$('.layout-nav .span6').click(function(){//查看小组互评
        //    $('.div-px').hide();
        //    $('.px6').show();
        //    getFirstTeacherHuPingScoreForLeader();
        //});
        $('.layout-nav .span7').click(function(){//考核打分
            if(!checkEvaluationTime()){
                return;
            }
            $('.div-px').hide();
            $('.px7').show();
        });
        $('.layout-nav .span8').click(function(){//查看成绩与排名
            if($('#leader').val()==1 || checkRankingTime()){
                $('.div-px').hide();
                $('.px8a').show();
                getRanking();
            }
        });

        $('#integrity').click(function(){
            window.open("/teacherevaluation/" + evaluationId + "/items/integrity.do")
        })
        //考核已经结束，直接查看成绩与排名
        if(Number($('#timeState').val()) == 1){
            $('.layout-nav .span8').trigger('click');
        }

    })

    function showRule(){
        Common.getDataAsync(rootUrl + "/rule.do", {}, function(resp){
            if(resp.code == '200'){
                $('#rule').html(resp.message);
            }
        })
    }

    function getTimeSetting(){
        var data;
        Common.getData("/teacherevaluation/" + evaluationId + "/config/setting/time.do", {}, function(resp){
            if(resp.code == '200'){
                data = resp.message;
                MODE = data.mode;
            }
        })
        return data;
    }
    var grades = getTimeSetting().gradeSettingDTOs;
    getGrades = function(){
        return grades;
    }

    function checkPersonalTime(){
        var data = getTimeSetting();
        var now = new Date().getTime();
        if(now < data.evaluationTimeBegin || 0 == data.evaluationTimeBegin){//未到打分时间或未设置打分时间可以填写和查看
            return true;
        } else {//到了打分时间，只能查看不能填写
            return true;
        }
    }

    function checkEvaluationTime(){
        var data = getTimeSetting();
        var now = new Date().getTime();
        if(data.evaluationTimeBegin > now || data.evaluationTimeBegin == 0){
            showMsg('考核打分阶段还未开始');
            return false;
        }
        if(data.evaluationTimeEnd + 86400000 < now){
            showMsg('考核打分阶段已经结束');
            return false;
        }
        return true;
    }

    function checkRankingTime(){
        var data = getTimeSetting();
        var now = new Date().getTime();
        if(data.evaluationTimeEnd + 86400000 > now){
            var date = new Date(data.evaluationTimeEnd);
            var string = date.getFullYear() + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日";
            showMsg('当前学年教师评价至'+string+'结束，暂不可查看成绩与排名');
            return false;
        }
        return true;
    }

    function calculateSchoolYear(){
        var schoolYear = '';
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        if(month < 9){
            schoolYear = (year-1) + '-' + year;
        } else {
            schoolYear = year + '-' + (year+1);
        }
        $('.year').text(schoolYear + '学年');
        return schoolYear;
    }

    function getTeacherGroups(){
        Common.getDataAsync("/teacherevaluation/" + evaluationId + "/config/groups.do", {}, function(resp){
            if(resp.code == '200'){
                teacherGroups = resp.message.teacherGroupDTOs;
                Common.render({
                    tmpl: '#groupNamesTmpl',
                    data: teacherGroups,
                    context: '.groupNames',
                    overwrite: 1
                });
                showTeachers(teacherGroups[0].teachers);
                //考核打分没有自己所在小组
                //if($('#member').val() == 1 || $('#leader').val() == 1){
                //    showTeacherGroupsForLeadMember(teacherGroups);
                //}
            }
        })
    }

    function showTeacherGroupsForLeadMember(teacherGroups){
        var data = [];
        var userId = $('#userId').val();
        for(var i=0; i<teacherGroups.length; i++){
            var teachers = teacherGroups[i].teachers;
            var flag = false;
            for(var j=0; j<teachers.length; j++){
                if(teachers[j].id == userId){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                data.push(teacherGroups[i]);
            }
        }
        if(data.length > 0){
            Common.render({
                tmpl: '#groupNamesTmpl',
                data: data,
                context: '.groupNames1',
                overwrite: 1
            });
            showTeachers(data[0].teachers, '.px7 .teachers');
        }
    }

    function showTeachers(data, context, callback){
        if(context == undefined){
            context = '.teachers';
        }
        Common.render({
            tmpl: '#membersTmpl',
            data: data,
            context: context,
            overwrite: 1
        });
        $(context).each(function(i, ele){
            $(ele).children().first().addClass('span-green');
        })
        if(callback != undefined){
            var ele = $(context).children().first();
            callback(ele.attr('id'), ele.text());
        }
    }

    function showTeachersAndState(data, context, callback){
        if(context == undefined){
            context = '.teachers';
        }
        Common.render({
            tmpl: '#membersStateTmpl',
            data: data,
            context: context,
            overwrite: 1
        });
        $(context).each(function(i, ele){
            $(ele).children().first().addClass('span-green');
        })
        if(callback != undefined){
            var ele = $(context).children().first();
            callback(ele.attr('id'), ele.text());
        }
    }

    function getKaohe(){
        Common.getDataAsync("/teacherevaluation/"+evaluationId+"/config/1/elements.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                //Common.render({
                //    tmpl: '#kaoheTmpl',
                //    data: data,
                //    context: '.kaohe',
                //    overwrite: 1
                //});
                Common.render({
                    tmpl: '#elementsTmpl',
                    data: data,
                    context: '#elements',
                    overwrite: 1
                });
                var grades = getGrades();
                if(MODE == MODETYPE.SCOREMODE){
                    for(var i in data){
                        TMPL += '<td>';
                        TMPL += '<table class="score" val="">';
                        TMPL += '<tr>';
                        for(var j=0; j<grades.length; j++) {
                            TMPL += '<td>'+grades[j].name+'</td>';
                        }
                        TMPL += '</tr>';
                        TMPL += '<tr>';
                        for(var j=0; j<grades.length; j++) {
                            TMPL += '<td><div style="width:80px;">';
                            TMPL += '<select class="select">';
                            for(var k=upInt(data[i].score, grades[j].begin/100); k<=downInt(data[i].score, grades[j].end/100); k++) {
                                TMPL += '<option value="' + k + '">' + k + '</option>';
                            }
                            TMPL += '</select>';
                            TMPL += '</div></td>';
                        }
                        TMPL += '</tr>';
                        TMPL += '</table>';
                        TMPL += '</td>';
                    }
                } else if(MODE == MODETYPE.GRADEMODE){
                    for(var i in data){
                        TMPL += '<td>';
                        TMPL += '<table class="score" val="">';
                        TMPL += '<tr>';
                        TMPL += '<td><input type="radio" value="A">A</td>';
                        TMPL += '<td><input type="radio" value="B">B</td>';
                        TMPL += '<td><input type="radio" value="C">C</td>';
                        TMPL += '<td><input type="radio" value="D">D</td>';
                        TMPL += '</tr>';
                        TMPL += '</table>';
                        TMPL += '</td>';
                    }
                }

                Common.render({
                    tmpl: '#kaoheForLeaderTmpl',
                    data: data,
                    context: '.kaoheForLeader',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#kaoheForDetailTmpl',
                    data: data,
                    context: '#kaoheForDetail',
                    overwrite: 1
                });
            }
        })
    }

    //=======================编辑个人陈述页面=====================================

    function getTeacherStatement(){
        var userId = $('#userId').val();
        Common.getDataAsync(rootUrl + "/statements/" + userId + ".do", {}, function(resp){
            if(resp.code == '200'){
                var statement = resp.message.split("<br/>").join('\n');
                $('#teacherStatement').val(statement);
            }
        })

    }

    $('#updateTeacherStatement').click(function(){
        updateTeacherStatement();
    })

    function updateTeacherStatement(){
        var userId = $('#userId').val();
        var statement = $('#teacherStatement').val();
        if($.trim(statement) == ''){
            showMsg('个人陈述为空，请填写完成后再保存');
            return false;
        }
        statement = statement.split("\n").join('<br/>');
        Common.postDataAsync(rootUrl + "/statements/" + userId + ".do", {statement : statement}, function(resp){
            if(resp.code == '200'){
                showMsg('保存成功');
            } else {
                showMsg('保存失败');
            }
        })
    }

    //====================================查看个人陈述================================
    $('body').on('click', '.px2 .teachers span', function(){
        var userId = $(this).attr('id');
        var userName = $(this).text();
        showTeacherStatement(userId, userName);
    })

    function showFirstTeacherStatement(){
        var userId = $('.px2 .teachers span').first().attr('id');
        var userName = $('.px2 .teachers span').first().text();
        showTeacherStatement(userId, userName);
    }

    $('.px2 .groupNames').change(function(){
        var groupId = $(this).val();
        for(var i in teacherGroups){
            if(teacherGroups[i].groupId == groupId){
                showTeachers(teacherGroups[i].teachers, '.px2 .teachers', showTeacherStatement);
            }
        }
    })

    function showTeacherStatement(userId, userName){
        $('.px2 #title').text(userName + '老师的个人陈述');
        Common.getDataAsync(rootUrl + "/statements/" + userId + ".do", {}, function(resp){
            if(resp.code == '200'){
                if(resp.message == ''){
                    resp.message = '该老师还未提交个人陈述';
                }
                $('.px2 #statement').html(resp.message);
            }
        })
    }

    //================================填写实证资料================================

    $('body').on('click', '.px3 .teachers span', function(){
        var teacherId = $(this).attr('id');
        showTeacherEvidence(teacherId);
    })

    $('#saveEvidence').click(function(){
        var teacherId = $('.px3 .teachers span.span-green').attr('id');
        updateTeacherEvidence(teacherId);
    })

    function showFirstTeacherEvidence(){
        var groupId = $('.px3 .groupNames').val();
        var data = getGroupTeachersAndState(groupId, 1);
        showTeachersAndState(data, '.px3 .teachers', showTeacherEvidence);
    }

    $('.px3 .groupNames').change(function(){
        var groupId = $(this).val();
        for(var i in teacherGroups){
            if(teacherGroups[i].groupId == groupId){
                var data = getGroupTeachersAndState(groupId, 1);
                showTeachersAndState(data, '.px3 .teachers', showTeacherEvidence);
            }
        }
    })

    function getGroupTeachersAndState(groupId, type){
        var data;
        Common.getData(rootUrl + "/groupTeachersAndStat.do", {groupId:groupId, type:type}, function(resp){
            if(resp.code == '200'){
                data = resp.message;
            }
        })
        return data;
    }

    function showTeacherEvidence(teacherId){
        Common.getDataAsync(rootUrl + "/evidence/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                ue.ready(function(){
                    ue.setContent(data.evidence);
                })
                if(data.scores.length > 0){
                    $('#lianghua tr select').each(function(index, ele){
                        $(ele).val(data.scores[index].value);
                    })
                } else {
                    $('#lianghua tr select').each(function(index, ele){
                        $(ele).val(0);
                    })
                }
            }
        })
    }

    function updateTeacherEvidence(teacherId){
        var requestData = {};
        requestData.evidence = ue.getContent();
        if($.trim(ue.getContent()) == ''){
            showMsg('实证资料不能为空');
            return false;
        }
        Common.postDataAsync(rootUrl + "/evidence/" + teacherId + ".do", requestData, function(resp){
            if(resp.code == '200'){
                showMsg('保存成功');
                $('.px3 .teachers .span-green').addClass('border-orange');
            } else {
                showMsg('保存失败 ' + resp.message);
            }
        })
    }

    //=================================查看实证资料=============================
    $('body').on('click', '.px4 .teachers span', function(){
        var teacherId = $(this).attr('id');
        showTeacherEvidenceForLeader(teacherId);
    })

    $('.px4 .groupNames').change(function(){
        var groupId = $(this).val();
        for(var i in teacherGroups){
            if(teacherGroups[i].groupId == groupId){
                var data = getGroupTeachersAndState(groupId, 1);
                showTeachersAndState(data, '.px4 .teachers', showTeacherEvidenceForLeader);
            }
        }
    })

    function showFirstTeacherEvidenceForLeader(){
        var teacherId;
        if($('#leader').val() == 1) {
            var groupId = $('.px4 .groupNames').val();
            var data = getGroupTeachersAndState(groupId, 1);
            showTeachersAndState(data, '.px4 .teachers', showTeacherEvidenceForLeader);
        } else {
            teacherId = $('#userId').val();
            showTeacherEvidenceForLeader(teacherId);
        }

    }

    function showTeacherEvidenceForLeader(teacherId){
        Common.getDataAsync(rootUrl + "/evidence/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                if(data.evidence == ''){
                    data.evidence = '该老师暂无实证资料';
                }
                $('#evidenceForLeader').html(data.evidence);
            }
        })
    }

    /*//==========================================组内互评=================================

    $('body').on('click', '.px5 .member2 span', function(){
        var teacherId = $(this).attr('id');
        var data = getTeacherStatementAndEvidenceAndScore(teacherId);
        showTeacherStatementAndEvidence(data);
        showTeacherHuPingScore(data);
    })

    $('#saveHuPingScore').click(function(){
        var hupingscore = formateHuPingScore();
        var teacherId = $('.px5 .member2 .span-green').attr('id');
        Common.postDataAsync(rootUrl + "/huping/" + teacherId + ".do", {huPingScore : hupingscore}, function(resp){
            if(resp.code == '200'){
                showMsg('保存成功');
                $('.px5 .member2 .span-green').addClass('border-orange');
            } else {
                showMsg('保存失败 ' + resp.message);
            }
        })
    })

    function formateHuPingScore(){
        return $('.px5 .kaohe .score').map(function(index,elem) {
            var id = $(elem).parents('tr').attr('id');
            //var score = $(elem).find('select').val();
            var score = $(elem).attr('val');
            return id + "," + score;
        }).get().join(';');
    }

    function getMyGroupTeachers(){
        Common.getDataAsync(rootUrl + "/groupTeachers.do", {}, function(resp){
            if(resp.code == '200'){
                $('.px5 h4').text(resp.message.groupName + "  组内互评");
                Common.render({
                    tmpl: '#membersStateTmpl',
                    data: resp.message.list,
                    context: '#myGroupTeachers',
                    overwrite: 1
                });
                $('#myGroupTeachers span').first().addClass('span-green');
                var teacherId = $('#myGroupTeachers span').first().attr('id');
                var data = getTeacherStatementAndEvidenceAndScore(teacherId);
                showTeacherStatementAndEvidence(data);
                showTeacherHuPingScore(data);
            }

        })
    }

    function getTeacherStatementAndEvidenceAndScore(teacherId){
        var data = null;
        Common.getData(rootUrl + "/statementAndEvidenceAndScore/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                data = resp.message;
            }
        })
        return data;
    }

    function showTeacherStatementAndEvidence(data){
        if(data != null){
            if(data.statement == ''){
                data.statement = '该老师还未提交个人陈述';
            }
            $('.px5 #teacherState').html(data.statement);
            $('.px7 #kh-teachState').html(data.statement);

            if(data.evidence == ''){
                data.evidence = '该老师还未有实证资料';
            }
            $('.px5 #evi').html(data.evidence)
            $('.px7 #kh-evi').html(data.evidence)
        }

    }

    function showTeacherHuPingScore(data){
        if(data.huPingScores.length > 0){
            $('.px5 .kaohe select').each(function(index, ele){
                //$(ele).val(data.huPingScores[index].value);
                var i = Math.floor(index / 4);
                $(ele).select2('val', data.huPingScores[i].value);
                $(ele).parents('.score').attr('val', data.huPingScores[i].value);
            })
        } else {
            $('.px5 .kaohe select').each(function(index, ele){
                //$(ele).val(0);
                $(ele).select2('val', 0);
                $(ele).parents('.score').attr('val', 0);
            })
        }
    }

    //======================查看组内互评================================================

    $('body').on('click', '.px6 .teachers span', function(){
        var teacherId = $(this).attr('id');
        getTeacherHuPingScoreForLeader(teacherId);
    })

    $('.px6 .groupNames').change(function(){
        var groupId = $(this).val();
        for(var i in teacherGroups){
            if(teacherGroups[i].groupId == groupId){
                var data = getGroupTeachersAndState(groupId, 2);
                showTeachersAndState(data, '.px6 .teachers', getTeacherHuPingScoreForLeader);
            }
        }
    })

    function getFirstTeacherHuPingScoreForLeader(){
        var groupId = $('.px6 .groupNames').val();
        var data = getGroupTeachersAndState(groupId, 2);
        showTeachersAndState(data, '.px6 .teachers', getTeacherHuPingScoreForLeader);
    }

    function getTeacherHuPingScoreForLeader(teacherId){
        Common.getDataAsync(rootUrl + "/huping/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#hupingTmpl',
                    data: resp.message,
                    context: '#huping',
                    overwrite: 1
                });
            }
        })
    }*/

    //========================考核打分================================================

    function availableGroups(){
        Common.postDataAsync(rootUrl + "/availableGroups.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                Common.render({
                    tmpl: '#groupNamesTmpl',
                    data: data,
                    context: '.groupNames1',
                    overwrite: 1
                });
                availableTeachers($('.groupNames1').val());
            }
        })
    }

    function availableTeachers(groupId){
        Common.postDataAsync(rootUrl + "/group/" + groupId + "/availableTeachers.do", {}, function(resp){
            if(resp.code == '200'){
                var list = resp.message;
                var data = {list:list, tmpl:TMPL};
                Common.render({
                    tmpl: '#teachersTmpl',
                    data: data,
                    context: '#teachers',
                    overwrite: 1
                });

                if(MODE == MODETYPE.SCOREMODE){
                    showScore(list);
                } else if(MODE == MODETYPE.GRADEMODE){
                    showGrade(list);
                }

            }
        })
    }

    function showScore(list){
        $('.select').select2({
            width: '70px'
        });
        $('.select').change(function(){
            var val = $(this).val();
            $(this).parents('.score').attr("val", val);
            $(this).parents('.score').find('.select').select2('val', val);
        })
        var scores = [];
        for(var i in list){
            var elementScores = list[i].elementScores;
            for(var j in elementScores){
                scores.push(elementScores[j]);
            }
        }

        $('.px7 #teachers select').each(function(index, ele){
            var i = Math.floor(index/4);
            $(ele).select2('val', scores[i].value);
            $(ele).parents('.score').attr('val', scores[i].value);
        })
    }

    function showGrade(list){
        var scores = [];
        for(var i in list){
            var elementScores = list[i].elementScores;
            for(var j in elementScores){
                scores.push(elementScores[j]);
            }
        }
        $('.px7 #teachers .score').each(function(index, ele){
            $(ele).find("input:radio").attr('name', index);
        })
        $('.px7 #teachers .score').each(function(index, ele){
            var grade = scores[i].name;
            $(ele).find("input:radio[value="+grade+"][name="+index+"]").prop('checked','true');
            $(ele).attr('val', grade);
        })
        $('.score input:radio').click(function(){
            var val = $(this).val();
            $(this).parents('.score').attr('val', val)
        })
    }

    //评完提交
    $('#saveKaoHeScore1').click(function(){
        var kaohescore = formateKaoHeScore1();
        if(kaohescore == ''){
            alert('还未有老师报名，不能打分');
            return;
        }
        if(sessionStorage.getItem("passCheck") == 'true'){
            Common.postDataAsync(rootUrl + "/kaoheScore.do", {kaoHeScore : kaohescore, tijiao: 1, mode:MODE}, function(resp){
                if(resp.code == '200'){
                    showMsg('提交成功');
                } else {
                    showMsg('提交失败 ' + resp.message);
                }
            })
        } else {
            alert("还未完成打分，请完成打分后提交")
        }
    })
    function formateKaoHeScore1(){
        sessionStorage.setItem("passCheck", true);
        return $('.px7 #teachers .teacher').map(function(index,elem) {
            var tid = $(elem).attr('tid');
            var scores = '';
            $(elem).find('.score').each(function(i,e){
                if(MODE == MODETYPE.SCOREMODE){
                    var score = Number($(e).attr('val'));
                    if(score == 0){
                        sessionStorage.setItem("passCheck", false);
                    }
                } else if(MODE == MODETYPE.GRADEMODE){
                    var score = $(e).attr('val');
                    if(score == 'Z'){
                        sessionStorage.setItem("passCheck", false);
                    }
                }
                scores += ',' + $(e).attr('val')
            })
            return tid + scores;
        }).get().join(';');
    }

    //临时保存
    $('#saveKaoHeScore0').click(function(){
        var kaohescore = formateKaoHeScore0();
        if(kaohescore == ''){
            alert('还未有老师报名，不能打分');
            return;
        }
        Common.postDataAsync(rootUrl + "/kaoheScore.do", {kaoHeScore : kaohescore, tijiao: 0, mode:MODE}, function(resp){
            if(resp.code == '200'){
                showMsg('保存成功');
            } else {
                showMsg('保存失败 ' + resp.message);
            }
        })
    })
    function formateKaoHeScore0(){
        return $('.px7 #teachers .teacher').map(function(index,elem) {
            var tid = $(elem).attr('tid');
            var scores = '';
            $(elem).find('.score').each(function(i,e){
                scores += ',' + $(e).attr('val')
            })
            return tid + scores;
        }).get().join(';');
    }
    //点击老师姓名显示实证资料和个人陈述
    $('body').on('click', '.ls-name', function(){
        var teacherId = $(this).parents('tr').attr('tid');
        Common.getDataAsync(rootUrl + "/statementAndEvidence/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                var message = resp.message;
                $('#evidence').html(message.evidence=="" ? "暂无实证资料" : message.evidence)
                $('#teacherstatement').html(message.statement=="" ? "暂无陈述" : message.statement)
            } else {
                showMsg('保存失败 ' + resp.message);
            }
        })
        $(".bg").show();
        $(".tea-alert").show();
    })



    //==================================查看成绩与排名==============================

    $('#calculate').click(function(){
        Common.getDataAsync(rootUrl + "/calculateranking.do", {}, function(resp){
            if(resp.code == '200'){
                showMsg("计算标准成绩与最新排名成功");
                getRanking();
            }
        })
    })

    function getRanking(){
        Common.getDataAsync(rootUrl + "/ranking.do", {}, function(resp){
            if(resp.code == '200'){
                var tmpl = "#rankingTmpl1";
                if($('#leader').val() == 1){
                    tmpl = "#rankingTmpl";
                    $('.px8a table thead tr').each(function(index, ele){
                        $(ele).children().eq(3).show();
                        $(ele).children().eq(4).show();
                        $(ele).children().eq(5).show();
                    })
                } else {
                    $('.px8a table thead tr').each(function(index, ele){
                        $(ele).children().eq(3).hide();
                        $(ele).children().eq(4).hide();
                        $(ele).children().eq(5).hide();
                    })
                }
                Common.render({
                    tmpl: tmpl,
                    data: resp.message,
                    context: '#ranking',
                    overwrite: 1
                });
            }
        })

    }
    var teacherId;
    $('body').on('click', '.px8a .check', function(){
        teacherId = $(this).attr('tid');
        Common.getDataAsync(rootUrl + "/elementScores/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;

                var huping = data.huping;
                var html = '<td>组内互评</td>';
                for(var i in huping){
                    html += '<td>'+huping[i].value+'</td>';
                }
                html += '<td><span class="check1 check">详情</span></td>';
                $('#HP').html(html);

                var leader = data.leader;
                var html = '<td>考核小组领导</td>';
                for(var i in leader){
                    html += '<td>'+leader[i].value+'</td>';
                }
                html += '<td><span class="check2 check">详情</span></td>';
                $('#LD').html(html);

                var group = data.group;
                var html = '<td>考核小组成员</td>';
                for(var i in leader){
                    html += '<td>'+group[i].value+'</td>';
                }
                html += '<td><span class="check3 check">详情</span></td>';
                $('#GP').html(html);

                var total = data.total;
                var html = '<td>总分</td>';
                for(var i in total){
                    html += '<td>'+total[i].value+'</td>';
                }
                html += '<td></td>';
                $('#total').html(html);
            }
        })
    })
    $('body').on('click', '.px8b .check1', function(){
        Common.getDataAsync(rootUrl + "/huping/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#hupingTmpl',
                    data: resp.message,
                    context: '#huping1',
                    overwrite: 1
                });
            }
        })
    })
    $('body').on('click', '.px8b .check2', function(){
        Common.getDataAsync(rootUrl + "/leaderScores/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#hupingTmpl',
                    data: resp.message,
                    context: '#leadergroup',
                    overwrite: 1
                });
            }
        })
    })

    $('body').on('click', '.px8b .check3', function(){
        Common.getDataAsync(rootUrl + "/groupScores/" + teacherId + ".do", {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#hupingTmpl',
                    data: resp.message,
                    context: '#leadergroup',
                    overwrite: 1
                });
            }
        })
    })

    //===============================历史考核=======================================
    $('.top-nav li:nth-child(2)').click(function(){
        buildYear();
        getRankingByYear();
    });

    $('#years').change(function(){
        getRankingByYear();
    })

    $('#search').keyup(function(){
        var patternName = $(this).val();
        var patt1=new RegExp(patternName);
        $('#history tr').each(function(index, ele){
            var name = $(ele).children().first().text();
            if(patt1.test(name)){
                $(ele).show();
            } else {
                $(ele).hide();
            }
        })
    })


    function buildYear(){
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var count = 0;
        if(month < 9){
            count = year - 2015;
        } else {
            count = year - 2014;
        }
        var html = '';
        for(var i=count; i>0; i--){
            var ys = (2014+i) + '-' + (2015+i);
            html += '<option value="'+ys+'">' + ys + '</option>';
        }
       $('#years').html(html);

    }

    function getRankingByYear(){
        var year = $('#years').val();
        var url = "/teacherevaluation/" + evaluationId + "/items/ranking.do";
        Common.getDataAsync(url, {}, function(resp){
            if(resp.code == '200'){
                if($('#leader').val()==1){
                    Common.render({
                        tmpl: '#historyTmpl',
                        data: resp.message,
                        context: '#history',
                        overwrite: 1
                    });
                } else {
                    $('.right-cont2 .th4').hide();
                    Common.render({
                        tmpl: '#historyTmpl1',
                        data: resp.message,
                        context: '#history',
                        overwrite: 1
                    });
                }
            }
        })
    }

    //====================================我的成绩==============================
    function myScores(){
        var url = "/teacherevaluation/items/myScores.do";
        Common.getDataAsync(url, {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#myscoresTmpl',
                    data: resp.message,
                    context: '#myscores',
                    overwrite: 1
                });
            }
        })
    }



    //================================================================================
    function init(){
        $('.top-nav li').click(function(){
            $(this).addClass('cur-li').siblings('.top-nav li').removeClass('cur-li');
        });
        $('.top-nav li:nth-child(1)').click(function(){
            $('.right-cont').hide();
            $('.right-cont1').show();
        });
        $('.top-nav li:nth-child(2)').click(function(){
            $('.right-cont').hide();
            $('.right-cont2').show();
        });
        $('.top-nav li:nth-child(3)').click(function(){
            $('.right-cont').hide();
            $('.right-cont3').show();
            myScores();
        });
        $('body').on('click', '.member2 span', function(){
            $(this).addClass('span-green').siblings('.member2 span').removeClass('span-green');
        })

        $('body').on('click', '.px8a .check', function(){
            $('.div-px').hide();
            $('.px8b').show();
        })
        $('body').on('click', '.px8b .check1', function(){
            $('.div-px').hide();
            $('.px8c').show();
        })
        $('body').on('click', '.px8b .check2, .px8b .check3', function(){
            $('.div-px').hide();
            $('.px8d').show();
        })
        $('.em-back').click(function(){
            $('.div-px').hide();
            $('.px-o').show();
            $('.layout-nav span').removeClass('white-hover');
        });
        $('.em-back1').click(function(){
            $('.div-px').hide();
            $('.px-o').show();
            $('.layout-nav span').removeClass('white-hover');
        });
        $('.em-back2').click(function(){
            $('.div-px').hide();
            $('.px8a').show();
        });
        $('.em-back3').click(function(){
            $('.div-px').hide();
            $('.px8b').show();
        });
        $('.layout-nav span').click(function(){
            $(this).addClass('white-hover').siblings('.layout-nav span').removeClass('white-hover');
        });
        $(".alert-close").click(function(){
            $(".bg").hide();
            $(".tea-alert").hide();
        })
    }





});
