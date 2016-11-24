/**
 * Created by fl on 2016/4/22.
 */
define(function(require,exports,module){
    require('rome');
    require('ajaxfileupload');
    var Common = require('common');
    var year = calculateSchoolYear();
    var evaluationId = $('body').attr('evid');
    var rootUrl = "/teacherevaluation/" + evaluationId + "/config";
    var TYPE = {
        LEADER: 1,
        MEMBER: 2,
        TEACHER : 3
    }

    var ELETYPE = {
        ELEMENT: 1,
        LIANGHUA: 2
    }

    $(function(){


        init();

        $('.top-nav li:nth-child(1)').click(function(){
            $('.right-cont').hide();
            $('.right-cont1').show();
            getMemberGroup();
        });
        $('.top-nav li:nth-child(2)').click(function(){
            $('.right-cont').hide();
            $('.right-cont2').show();
            getProportion();
        });
        $('.top-nav li:nth-child(3)').click(function(){
            $('.right-cont').hide();
            $('.right-cont3').show();
            getElements(ELETYPE.ELEMENT);
        });
        $('.top-nav li:nth-child(4)').click(function(){
            $('.right-cont').hide();
            $('.right-cont4').show();
            getElements(ELETYPE.LIANGHUA);
        });
        $('.top-nav li:nth-child(5)').click(function(){
            $('.right-cont').hide();
            $('.right-cont5').show();
            getSettings();
        });
        $('.top-nav li:nth-child(6)').click(function(){
            $('.right-cont').hide();
            $('.right-cont6').show();
            getSettings();
        });
        $('.top-nav li:nth-child(7)').click(function(){
            $('.right-cont').hide();
            $('.right-cont7').show();
            getSettings();
        });
        $('.top-nav li:nth-child(8)').click(function(){
            $('.right-cont').hide();
            $('.right-cont8').show();
            getSettings();
        });
        getMemberGroup();

    })

    function isBegin(){
        var flag = false;
        Common.getData(rootUrl + "/setting.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                var now = new Date().getTime();
                if(data.evaluationTimeBegin != 0 && data.evaluationTimeBegin < now){
                    flag = true;
                }
            }
        })
        return flag;
    }

    function checkUpdateState(){ //返回true表示可以更改  false表示不可以更改
        var flag = false;
        if(isBegin()){
            var beginWaring = "本学年教师评价已开始，禁止改动基础设置，如若强行更改，则已打分的考核打分全部作废且不可恢复，是否强行更改？";
            if(confirm(beginWaring)){
                Common.getPostData("/teacherevaluation/" + evaluationId + "/items/restScores.do", {}, function(resp){
                    if(resp.code == '200'){
                        flag = true;
                    } else {
                        flag = false;
                    }
                })
                return flag;
            } else {
                return false;
            }

        } else {
            return true;
        }

    }
//======================================人员分组页面js========================================
    var groupId = '';
    var isEditTeacherGroup = false;
    getMemberGroup();
    //考核小组领导 编辑
    $('.bj-khxz').click(function(){
        getTeachers(TYPE.LEADER);
        isEditTeacherGroup = false;
    });
    //考核小组成员  编辑
    $('.bj-khdx').click(function(){
        getTeachers(TYPE.MEMBER);
        isEditTeacherGroup = false;
    });
    //老师小组  编辑
    $('body').on('click', '.bj-xzry', function(){
        groupId = $(this).attr('id');
        getTeachers(TYPE.TEACHER, groupId);
        isEditTeacherGroup = true;
    })

    $('body').on('click', '.delete-xzry', function(){
        var msg = "确认删除本小组？";
        if(confirm(msg)){
            groupId = $(this).attr('id');
            Common.postDataAsync(rootUrl + "/groups/teacherGroups/" + groupId + "/deletion.do", {}, function(resp){
                if(resp.code == '200'){
                    getMemberGroup();
                } else {
                    showMsg('删除失败');
                }
            })
        }
    })

    $('body').on('click', '.ul-tea li', function(){
        if($(this).hasClass('onfocus')){
            $(this).removeClass('onfocus');
        } else {
            $(this).addClass('onfocus');
        }

    })

    $('body').on('click', '.addTeacherGroup', function(){
        Common.postDataAsync(rootUrl + "/groups/teacherGroups.do", {}, function (resp) {
            if (resp.code == '200') {
                getMemberGroup();
            } else {
                showMsg('添加失败');
            }
        })
    })

    $('body').on('click', '#addTeacherToLeaders', function(){
        var teacherIds = $("#leadersCandidate .onfocus").map(function (index, elem) {
            return $(elem).attr('id');
        }).get().join(',');
        Common.postDataAsync(rootUrl + "/" + TYPE.LEADER + "/members.do", {teacherIds: teacherIds}, function (resp) {
            showTeachers(resp, TYPE.LEADER);
            getMemberGroup();
        })
    })

    $('body').on('click', '#leadersExistence i', function(){
        var teacherId = $(this).parents('li').attr('id');
        Common.postDataAsync(rootUrl + "/" + TYPE.LEADER + "/members/" + teacherId + "/deletion.do", {}, function (resp) {
            showTeachers(resp, TYPE.LEADER);
            getMemberGroup();
        })
    })

    $('body').on('click', '#addTeacherToMembers', function(){
        var teacherIds = $("#membersCandidate .onfocus").map(function (index, elem) {
            return $(elem).attr('id');
        }).get().join(',');
        Common.postDataAsync(rootUrl + "/" + TYPE.MEMBER + "/members.do", {teacherIds: teacherIds}, function (resp) {
            showTeachers(resp, TYPE.MEMBER);
            getMemberGroup();
        })
    })

    $('body').on('click', '#membersExistence i', function(){
        var teacherId = $(this).parents('li').attr('id');
        Common.postDataAsync(rootUrl + "/" + TYPE.MEMBER + "/members/" + teacherId + "/deletion.do", {}, function (resp) {
            showTeachers(resp, TYPE.MEMBER);
            getMemberGroup();
        })
    })

    $('body').on('click', '#addTeacherToTeacherGroup', function(){
        var teacherIds = $("#teachersCandidate .onfocus").map(function (index, elem) {
            return $(elem).attr('id');
        }).get().join(',');
        Common.postDataAsync(rootUrl + "/" + TYPE.TEACHER + "/members.do", {
            teacherIds: teacherIds,
            groupId: groupId
        }, function (resp) {
            $('#allStuNo').val(Number($('#allStuNo').val()) + $("#teachersCandidate .onfocus").length);
            showTeachers(resp, TYPE.TEACHER);
            getMemberGroup();
        })
    })

    $('body').on('click', '#teachersExistence i', function(){
        var teacherId = $(this).parents('li').attr('id');
        Common.postDataAsync(rootUrl + "/" + TYPE.TEACHER + "/groups/" + groupId + "/members/" + teacherId + "/deletion.do", {}, function (resp) {
            $('#allStuNo').val(Number($('#allStuNo').val()) - 1);
            showTeachers(resp, TYPE.TEACHER);
            getMemberGroup();
        })
    })

    $('.btn-ok').click(function(){
        if(isEditTeacherGroup){
            var requestData = {};
            requestData.name = $('#teacherGroupName').val();
            requestData.num = $('#num').val()=="" || Number($('#num').val())==0 ? $('#allStuNo').val() : $('#num').val();
            requestData.lnum = $('#lnum').val()==""  || Number($('#num').val())==0 ? $('#allStuNo').val() : $('#lnum').val();
            Common.postDataAsync(rootUrl + "/groups/teacherGroups/" + groupId + ".do?", requestData, function(resp){
                getMemberGroup();
            })
        }
    })

    $('.search').keyup(function(){
        var patternName = $(this).val();
        var patt1=new RegExp(patternName);
        $(this).siblings('ul').find('li').each(function(index, ele){
            var name = $(ele).text();
            if(patt1.test(name)){
                $(ele).show();
            } else {
                $(ele).hide();
            }
        })
    })

    $('#num, #lnum').keyup(function(){
        var num = Number($(this).val());
        var teacherNum = $('#teachersExistence').find('li').size();
        if(num > teacherNum){
            $(this).val(teacherNum);
        }
    })


    function getMemberGroup(){
        Common.getDataAsync(rootUrl + "/groups.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                Common.render({
                    tmpl: '#leadersTmpl',
                    data: data.leaders,
                    context: '#leaders',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#membersTmpl',
                    data: data.members,
                    context: '#members',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#teacherGroupDTOsTmpl',
                    data: data.teacherGroupDTOs,
                    context: '#teacherGroupDTOs',
                    overwrite: 1
                });
            } else {
                showMsg('获取分组信息失败');
            }
        })
    }

    function getTeachers(type, groupId){
        var requestData = {};
        if(type == TYPE.TEACHER){
            requestData.groupId = groupId;
        }
        Common.getDataAsync(rootUrl + "/" + type + "/members.do", requestData, function(resp){
            showTeachers(resp, type, true);
        })
    }

    function showTeachers(resp, type, reset){
        if(resp.code == '200'){
            var data = resp.message;
            if(type == TYPE.TEACHER && reset){
                $('#teacherGroupName').val(data.groupName);
                $('#allStuNo').val(data.allStuNo);
                $('#num').val(data.num);
                $('#lnum').val(data.lnum);
            }
            var pre = '';
            if(type == 1){
                pre = '#leaders';
            } else if(type == 2){
                pre = '#members';
            } else {
                pre = '#teachers';
            }
            Common.render({
                tmpl: pre + 'CandidateTmpl',
                data: data.candidate,
                context: pre + 'Candidate',
                overwrite: 1
            });
            Common.render({
                tmpl: pre + 'ExistenceTmpl',
                data: data.existence,
                context: pre + 'Existence',
                overwrite: 1
            });
        } else {
            showMsg('获取成员信息失败');
        }
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

    //下载老师模板
    $('#exportTeachers').click(function(){
        //Common.getDataAsync(rootUrl + "/exportTeacher.do", {}, )
        window.open("/teacherevaluation/" + evaluationId + "/config/exportTeacher.do");
    })

    $('button.import').on('click',function() {
        var parent = $(this).parent().parent();
        if ($("#file").val() == "") {
            showMsg("请先选择上传文件");
        } else {
            showMsg("上传中...");
            $.ajaxFileUpload
            (
                {
                    url: "/teacherevaluation/" + evaluationId + "/config/importMemberGroup.do", //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'file', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json

                    success: function (data, status)  //服务器成功响应处理函数
                    {
                        if (data == false) {
                            showMsg("上传失败！");
                        } else {
                            showMsg("上传成功！");
                            parent.fadeOut();
                            $('.bg').fadeOut();
                            getMemberGroup();
                        }
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                        showMsg("上传失败！");
                    }
                }
            )
        }

    })
    //======================================评分比重页面js========================================
    var proportionDTO = {};
    $('.pro').blur(function(){
        //$('#leadGroupPro').val(Number($('#leaderPro').val()) + Number($('#groupPro').val()));
        $('.right-cont2 .span3').text(Number($('#huPingPro').val()) + Number($('#leaderPro').val()) + Number($('#groupPro').val()) + "%");
    })

    $('#saveProportion').click(function(){
        setProPortion();
    })
    function getProportion(){
        Common.getDataAsync(rootUrl + "/proportion.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                $('#huPingPro').val(data.huPingPro);
                //$('#liangHuaPro').val(data.liangHuaPro);
                $('#leadGroupPro').val(data.leadGroupPro);
                $('#leaderPro').val(data.leaderPro);
                $('#groupPro').val(data.groupPro);

                $('#leaderMax').val(data.leaderMax);
                $('#leaderMin').val(data.leaderMin);
                $('#groupMax').val(data.groupMax);
                $('#groupMin').val(data.groupMin);
                $('#huPingMax').val(data.huPingMax);
                $('#huPingMin').val(data.huPingMin);
                $('.right-cont2 .span3').text(Number(data.huPingPro) + Number(data.leadGroupPro) + "%");
                proportionDTO = data;
            } else {
                showMsg('获取评分比重失败');
            }
        })
    }

    function setProPortion(){
        if(buildDTO()) {
            $.ajax({
                url: rootUrl + "/proportion.do",
                type: 'post',
                data: JSON.stringify(proportionDTO),
                contentType: 'application/json',
                success: function (resp) {
                    if(resp.code == '200'){
                        showMsg('保存成功');
                    } else {
                        showMsg(resp.message);
                    }
                },
                error: function (e) {
                    showMsg('保存失败');
                }
            });

        }
    }

    function buildDTO(){
        proportionDTO.huPingPro = $('#huPingPro').val();
        proportionDTO.leadGroupPro = Number($('#leaderPro').val()) + Number($('#groupPro').val());
        proportionDTO.leaderPro = $('#leaderPro').val();
        proportionDTO.groupPro = $('#groupPro').val();
        proportionDTO.leaderMax = $('#leaderMax').val();
        proportionDTO.leaderMin = $('#leaderMin').val();
        proportionDTO.groupMax = $('#groupMax').val();
        proportionDTO.groupMin = $('#groupMin').val();
        proportionDTO.huPingMax = $('#huPingMax').val();
        proportionDTO.huPingMin = $('#huPingMin').val();

        var pro = Number(proportionDTO.huPingPro)  + Number(proportionDTO.leadGroupPro);
        if(pro > 100){
            showMsg('评分比重之和超过100%，请修改后重新提交');
            return false;
        } else if(pro < 100) {
            showMsg('评分比重之和不足100%，请修改后重新提交');
            return false;
        }
        if(Number(proportionDTO.leadGroupPro) != Number(proportionDTO.leaderPro) + Number(proportionDTO.groupPro)){
            showMsg('评分小组比重应为校长&党支书和其他评分组成员比重之和，请修改后重新提交');
            return false;
        }
        return true;
    }

    //======================================考核要素页面js========================================
    var elementType = '';
    var elementId = '';
    function getElements(type){
        elementType = type;
        Common.getDataAsync(rootUrl + "/" + type + "/elements.do", {}, function(resp){
            if(resp.code == '200'){
                showElements(resp.message, elementType);
            } else {
                showMsg('获取列表失败');
            }
        })
    }

    function showElements(data, type){
        var pre = type == ELETYPE.ELEMENT ? '#elements' : '#lianghua';
        Common.render({
            tmpl: pre + 'Tmpl',
            data: data,
            context: pre,
            overwrite: 1
        });
    }

    /**
     * 添加考核要素
     */
    $('#addElement').click(function(){
        $('.wind-khnr1').fadeIn();
        $('.bg').fadeIn();
        elementId = '';
    })

    /**
     * 编辑考核要素
     */
    $('body').on('click', '.bj-khnr', function(){
        $('.wind-khnr1').fadeIn();
        $('.bg').fadeIn();
        elementId = $(this).parents('.div-yq').attr('id');
        $('#elementname').val($(this).prev().prev().text());
        $('#elementscore').val($(this).prev().text());
    })

    /**
     * 添加量化成绩
     */
    $('#addLiangHua').click(function(){
        $('.wind-lhcj').fadeIn();
        $('.bg').fadeIn();
        elementId = '';
    })

    /**
     * 编辑量化成绩
     */
    $('body').on('click', '.bj-lhcj', function(){
        $('.wind-lhcj').fadeIn();
        $('.bg').fadeIn();
        elementId = $(this).parents('.div-yq').attr('id');
        $('#lianghuaname').val($(this).prev().prev().text());
        $('#lianghuascore').val($(this).prev().text());
    })


    /**
     * 删除考核要素或量化成绩
     */
    $('body').on('click', '.btn-del-element', function(){
        if(checkUpdateState()) {
            var msg = "确认删除？";
            if (confirm(msg)) {
                elementId = $(this).parents('.div-yq').attr('id');
                var url = rootUrl + "/" + elementType + "/elements/" + elementId + "/deletion.do";
                Common.postDataAsync(url, {}, function (resp) {
                    if (resp.code == '200') {
                        showElements(resp.message, elementType);
                    } else {
                        showMsg('删除失败');
                    }
                })
            }
        }
    })


    $('#upsetElement').click(function(){
        var requestData = {};
        requestData.name = $('#elementname').val();
        requestData.score = $('#elementscore').val();
        upsetElement(requestData);
    })

    $('#upsetLiangHua').click(function(){
        var requestData = {};
        requestData.name = $('#lianghuaname').val();
        requestData.score = $('#lianghuascore').val();
        upsetElement(requestData);
    })

    function upsetElement(requestData){
        if(checkUpdateState()) {
            var url = "";
            if (elementId == '') {
                url = rootUrl + "/" + elementType + "/elements.do";
            } else {
                url = rootUrl + "/" + elementType + "/elements/" + elementId + ".do";
            }
            Common.postDataAsync(url, requestData, function (resp) {
                if (resp.code == '200') {
                    showElements(resp.message, elementType);
                } else {
                    showMsg('保存失败');
                }
            })
        }
    }
    //===============================考核要素之考核内容js===================================================
    var contentId = '';
    /**
     * 显示考核内容
     */
    $('body').on('click', '.right-cont3 .div-yq span', function(){
        elementId = $(this).parents('.div-yq').attr('id');
        var url = rootUrl + "/" + elementType + "/elements/" + elementId + "/contents.do";
        Common.getDataAsync(url, {}, function (resp) {
            if (resp.code == '200') {
                showContents(resp.message);
            } else {
                showMsg('获取内容失败');
            }
        })
    })

    function showContents(data){
        Common.render({
            tmpl: '#contentsTmpl',
            data: data,
            context: '#contents',
            overwrite: 1
        });
    }

    /**
     * 添加考核内容
     */
    $('#addContentToElement').click(function(){
        $('.wind-khnr2').fadeIn();
        $('.bg').fadeIn();
        contentId = '';
    })

    /**
     * 编辑考核内容
     */
    $('body').on('click', '.btn-bj3s', function(){
        $('.wind-khnr2').fadeIn();
        $('.bg').fadeIn();
        contentId = $(this).parents('.div-yq').attr('id');
        $('#ele-content').val($(this).prev().text());
    })

    /**
     * 删除考核内容
     */
    $('body').on('click', '.btn-del-content', function(){
        var msg = "确认删除？";
        if(confirm(msg)) {
            contentId = $(this).parents('.div-yq').attr('id');
            var url = rootUrl + "/" + elementType + "/elements/" + elementId + "/contents/" + contentId + "/deletion.do";
            Common.postDataAsync(url, {}, function (resp) {
                if (resp.code == '200') {
                    showContents(resp.message);
                } else {
                    showMsg('删除失败');
                }
            })
        }
    })

    $('#upsetContent').click(function(){
        var value = $.trim($('#ele-content').val());
        if(value == ''){
            showMsg('内容不能为空');
            return false;
        }
        upsetContent(value);
    })

    function upsetContent(value){
        var url = "";
        if(contentId == ''){
            url = rootUrl + "/" + elementType + "/elements/" + elementId + "/contents.do";
        } else {
            url = rootUrl + "/" + elementType + "/elements/" + elementId + "/contents/"+ contentId +".do";
        }
        Common.postDataAsync(url, {value : value}, function(resp){
            if(resp.code == '200'){
                showContents(resp.message);
            } else {
                showMsg('保存失败');
            }
        })
    }
    //=======================================等第设置js===========================================

    function getSettings(){
        Common.getDataAsync(rootUrl + "/setting.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                showTime(data);
                showRule(data);
                showGrades(data);
                showMode(data);
            }
        })
    }

    function showGrades(data){
        Common.render({
            tmpl: '#gradesTmpl',
            data: data.gradeSettingDTOs,
            context: '#grades',
            overwrite: 1
        });
        gradesCss();
    }

    function gradesCss(){
        $('.right-cont5 table tr:nth-child(1) div').css({'border':'1px solid #ff0000','box-shadow':'0 0 5px #ff0000 inset'});
        $('.right-cont5 table tr:nth-child(2) div').css({'border':'1px solid #ff9900','box-shadow':'0 0 5px #ff9900 inset'});
        $('.right-cont5 table tr:nth-child(3) div').css({'border':'1px solid #ffff00','box-shadow':'0 0 5px #ffff00 inset'});
        $('.right-cont5 table tr:nth-child(4) div').css({'border':'1px solid #00ff00','box-shadow':'0 0 5px #00ff00 inset'});
        $('.right-cont5 table tr:nth-child(5) div').css({'border':'1px solid #00ffff','box-shadow':'0 0 5px #00ffff inset'});
        $('.right-cont5 table tr:nth-child(6) div').css({'border':'1px solid #4a86e8','box-shadow':'0 0 5px #4a86e8 inset'});
        $('.right-cont5 table tr:nth-child(7) div').css({'border':'1px solid #0000ff','box-shadow':'0 0 5px #0000ff inset'});
        $('.right-cont5 table tr:nth-child(8) div').css({'border':'1px solid #9900ff','box-shadow':'0 0 5px #9900ff inset'});
        $('.right-cont5 table tr:nth-child(9) div').css({'border':'1px solid #ff00ff','box-shadow':'0 0 5px #ff00ff inset'});
        $('.right-cont5 table tr:nth-child(10) div').css({'border':'1px solid #980000','box-shadow':'0 0 5px #980000 inset'});
        $('.right-cont5 table tr:nth-child(11) div').css({'border':'1px solid #000000','box-shadow':'0 0 5px #000000 inset'});
        $('.right-cont5 table tr:nth-child(12) div').css({'border':'1px solid #434343','box-shadow':'0 0 5px #434343 inset'});
    }

    $('#appendGrade').click(function(){
        var html = '<tr> <td class="td1"> <input type="text" class="input1" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"> </td> <td class="td2"> ' +
            '<input type="text" class="input2" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>' +
            '<input type="text" class="input3" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')">' +
            ' </td> <td> <button class="btn-x1">删除</button> </td> </tr>';
        $('#gradeTable').append(html);
    })
    var gradeSettings = [];
    $('#updateGrades').click(function(){
        var parent = $(this).parent().parent();
        if(checkGradeData()){
            $.ajax({
                url: rootUrl + "/setting/grade.do",
                type: 'post',
                data: JSON.stringify(gradeSettings),
                contentType: 'application/json',
                success: function (resp) {
                    if(resp.code == '200'){
                        showGrades({gradeSettingDTOs : gradeSettings});
                        showMsg('保存成功');
                        parent.fadeOut();
                        $('.bg').fadeOut();
                    } else {
                        showMsg('保存失败');
                    }
                },
                error: function (e) {
                    showMsg('保存失败');
                }
            });
        }
    })

    function checkGradeData(){
        var isPass = true;
        gradeSettings = [];
        $('#gradeTable tr').each(function(index, ele){
            if(index >= 1){
                var name = $(ele).find('.input1').val();
                var end = $(ele).find('.input2').val();
                var begin = $(ele).find('.input3').val();
                if($.trim(name) == ''){
                    showMsg('第' + index + '行等第名称为空，请重新设置后保存');
                    isPass = false;
                    return false;
                }
                if(Number(end) > 100){
                    showMsg('第' + index + '行分数区间结束分数应小于100，请重新设置后保存');
                    isPass = false;
                    return false;
                }
                if(Number(begin) < 0){
                    showMsg('第' + index + '行分数区间开始分数应大于0，请重新设置后保存');
                    isPass = false;
                    return false;
                }
                if(Number(end) <= Number(begin)){
                    showMsg('第' + index + '行分数区间开始分数应小于结束分数，请重新设置后保存');
                    isPass = false;
                    return false;
                }
                var gradeSetting = {};
                gradeSetting.name = name;
                gradeSetting.begin = Number(begin);
                gradeSetting.end = Number(end);
                gradeSettings.push(gradeSetting);
            }
        })
        return isPass;
    }

    //=======================================考核模式js============================================
    function showMode(data){
        $("input:radio[name='eva-style'][value='"+data.mode+"']").prop('checked','true');
        $('.grade').each(function(index, ele){
            var grade = data.modeGrades[index];
            $(ele).val(grade.value)
        })
    }

    $('#saveMode').click(function(){
        if(checkUpdateState()){
            var mode = $("input[name='eva-style']:checked").val();
            if(mode == null){
                alert("请选择模式");
                return false;
            }
            var flag = true;
            var grades = 'A,0;B,0;C,0;D,0';
            if(2 == mode){//等级
                grades = $('.grade').map(function(index,ele) {
                    if('' == $(ele).val()){
                        flag = false;
                    }
                    return $(ele).attr("gn") + "," + $(ele).val();
                }).get().join(';');
            }
            if(flag){
                Common.postDataAsync(rootUrl + "/setting/mode.do", {grades: grades, mode: mode}, function(resp){
                    if(resp.code == '200'){
                        showMsg('保存成功');
                    } else {
                        showMsg('保存失败');
                    }
                })
            } else {
                alert("请填写完整等级对应的分数")
            }
        }


    })
    //=======================================评分时间js============================================

    function showTime(data){
        rome(evaluationTimeBegin, {initialValue: convertLongToString(data.evaluationTimeBegin),time: false, dateValidator: rome.val.beforeEq(evaluationTimeEnd)});
        rome(evaluationTimeEnd, {initialValue: convertLongToString(data.evaluationTimeEnd),time: false, dateValidator: rome.val.afterEq(evaluationTimeBegin)});
    }

    function convertLongToString(long){
        if(Number(long) != 0) {
            var date = new Date(long);
            var string = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            return string;
        }
        return "";
    }

    $('#saveTime').click(function(){
        if(checkTime()){
            var requestData = {};
            requestData.evaluationTimeBegin = $('#evaluationTimeBegin').val();
            requestData.evaluationTimeEnd = $('#evaluationTimeEnd').val();
            Common.postDataAsync(rootUrl + "/setting/time.do", requestData, function(resp){
                if(resp.code == '200'){
                    showMsg('保存成功');
                } else {
                    showMsg('保存失败');
                }
            })
        }
    })

    function checkTime(){
        if($('#evaluationTimeBegin').val() == '' | $('#evaluationTimeEnd').val() == '' ){
            showMsg("还有时间未填写，请填写完整后再保存");
            return false;
        }
        return true;
    }

    //=====================================评比规则js=============================

    function showRule(data){
        $('#rule').val(data.rule.split("<br/>").join('\n'));
    }

    $('#saveRule').click(function(){
        var rule = $('#rule').val();
        rule = rule.split("\n").join('<br/>');
        Common.postDataAsync(rootUrl + "/setting/rule.do", {rule : rule}, function(resp){
            if(resp.code == '200'){
                showMsg('保存成功');
            } else {
                showMsg('保存失败');
            }
        })
    })


});





function init(){
    $('.btn-dr').click(function(){
        $('.wind-in').fadeIn();
        $('.bg').fadeIn();
    })
    $('.wind .wind-top em').click(function(){
        $(this).parent().parent().fadeOut();
        $('.bg').fadeOut();
    });
    $('.wind .btn-no, .wind .btn-ok').click(function(){
        $(this).parent().parent().fadeOut();
        $('.bg').fadeOut();
    });
    $('.top-nav li').click(function(){
        $(this).addClass('cur-li').siblings('.top-nav li').removeClass('cur-li');
    });
    $('.bj-khxz').click(function(){
        $('.wind-khxz').fadeIn();
        $('.bg').fadeIn();
    });
    $('.bj-khdx').click(function(){
        $('.wind-khry').fadeIn();
        $('.bg').fadeIn();
    });
    $('.bj-lhcj').click(function(){
        $('.wind-lhcj').fadeIn();
        $('.bg').fadeIn();
    });
    $('body').on('click', '.bj-xzry', function(){
        $('.wind-xzry').fadeIn();
        $('.bg').fadeIn();
    });
    $('.ul-tea li i').click(function(){
        $(this).parent().fadeOut();
    });
    $('body').on('click', '.right-cont3 .div-yq span', function(){
        $('.right-cont3').hide();
        $('.right-cont3s').show();
    })
    $('.back-cont3').click(function(){
        $('.right-cont3s').hide();
        $('.right-cont3').show();
    });
    $('body').on('click', '.btn-x1', function(){
        $(this).parent().parent().remove();
    })
    $('.bj-ddsz').click(function(){
        $('.wind-ddsz').fadeIn();
        $('.bg').fadeIn();
    });

    $('.member2 span').click(function(){
        $(this).addClass('span-green').siblings('.member2 span').removeClass('span-green');
    });
    $('.confirm .no').click(function(){
        $(this).parent().fadeOut();
        $('.bg').fadeOut();
    });
    
}

