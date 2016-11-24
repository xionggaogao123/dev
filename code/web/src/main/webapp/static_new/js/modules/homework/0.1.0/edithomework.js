/**
 * Created by fl on 2015/8/19.
 */
/**
 * Created by fl on 2015/8/17.
 */
define(['jquery','doT','easing','common'],function(require, exports, module) {
    var Common = require('common');
    var editHW={};

    editHW.init = function() {

        //录音


        var foo = true;
        $(".a12").on('click', function(){
            showflash('#recordercontainer1');
        })



        function showflash(container) {
            var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
            mc.setAttribute("id", "recorderApp");
            mc.setAttribute("name", "recorderApp");

            mc.addVariable("uploadAction", "/homework/uploadvoice.do");
            mc.addVariable("fileName", "audio");
            mc.addVariable("recordTime", 10 * 60 * 1000);
            mc.addVariable("appName", "recorderApp");
            mc.write("myContent");
            if (foo) {
                $(container).append($('#recorder'));
                $('#recorder .sanjiao').show();
                $("#myContent").show();
                foo = false;
            } else {
                $("#myContent").hide();
                $('#recorder .sanjiao').hide();
                foo = true;
            }
        }

        //获取班级列表
        var requestData = {};
        Common.getData('/homework/teacher/section.do', requestData, function (resp) {
            Common.render({
                tmpl: '#classListTmpl',
                data: resp.classSubjectList,
                context: '#classList',
                overwrite: 1
            });
        });

        var requestData = {};
        requestData.hwid = sessionStorage.getItem("homeworkId");
        Common.getData('/homework/detail.do', requestData, function (resp) {
            $("#course-name").val(resp.title);
            $("#content").val(resp.content);
            if(resp.pg == 1) {
                $("#pg").prop("checked", true);
            }

            //回显类型
            var radioAll = $("input[name='k']:radio");
            var type = resp.type;
            $.each(radioAll, function (j, radio) {
                var radioValue = $(radio).val();
                if (type == radioValue) {
                    $(radio).prop("checked", true);
                }

            })

            //回显班级
            var checkBoxAll = $("input.basic-NN:checkbox");
            var classList = resp.classIdList;
            $.each(checkBoxAll, function (j, checkbox) {
                $(checkbox).prop("checked", false);
                for (var i in classList) {
                    //获取复选框的value属性
                    var checkValue = $(checkbox).val();
                    if (classList[i] == checkValue) {
                        $(checkbox).prop("checked", true);
                        break;
                    }
                }
            })

            //回显课时
            for(var i in resp.classLessonIndex){
                var ele = resp.classLessonIndex[i];
                var radioAll = $("input[name="+ele.field+"]:radio");
                $.each(radioAll, function (j, radio) {
                    var radioValue = $(radio).val();
                    if (ele.value == radioValue) {
                        $(radio).prop("checked", true);
                    }

                })
            }

            //voice
            if(resp.voiceFile.length>0){
                if( $('.voice').length > 0 ){
                    $('.voice').remove();
                }
                if($('#reply_section').length > 0){
                    var voiceContainer =  $('#reply_section');
                }
                //如果是老师提交语音
                else if($('#submit_section').length > 0){
                    var voiceContainer =  $('#submit_section');
                }

                newVoice(resp.voiceFile[0].value).appendTo(voiceContainer);
                //FWRecorder.hide();
                $('.sanjiao').hide();
                $("div[id^=myContent]").hide();
            }
            //alert(resp.lessonId);
            sessionStorage.setItem("lessonId", resp.lessonId);

        });


        //回显课程相关
        var requestData = {};
        requestData.lessonId = sessionStorage.getItem("lessonId");


        //保存
        $("#savehomework").on('click', function(){
            //保存作业相关
            var requestData = {};
            requestData.homeworkId = sessionStorage.getItem("homeworkId");
            requestData.title = $("#course-name").val();
            if(requestData.title == ""){
                alert("请填写作业标题！");
                return;
            }
            if(requestData.title.length > 50){
                alert("标题应不多于50个字符！");
                return;
            }
            requestData.content = $("#content").val();
            if(requestData.content.length > 500){
                alert("内容应不多于500个字符！");
                return;
            }
            var classIdList=$("input.basic-NN:checkbox:checked").map(function(index,elem) {
                var index = $(elem).siblings("input:radio:checked").val();
                if(index == null){
                    index = 0;
                }
                return "0," + $(elem).val() + "," + index;
            }).get().join(';');
            var subjectList=$("input.basic-NN:checkbox:checked").map(function(index,elem) {
                return $(elem).attr("subject");
            }).get().join(',');
            requestData.classIdList = classIdList;
            requestData.subjectList = subjectList;
            if($('.voice').length > 0){
                requestData.voicefile = $('.voice').attr('url');
            }
            //requestData.type = $("input[name='k']:radio:checked").val();
            requestData.type = sessionStorage.getItem('qianhoutype')=='keqian' ? 0 : 1;
            if(requestData.classIdList==""){
                alert("请选择班级！")
                return;
            }
            if(requestData.type==null){
                alert("请选择类型！")
                return;
            }
            requestData.pg = 0;
            if($("#pg").prop("checked")){
                requestData.pg = 1;
            }
            Common.getPostData('/homework/edit.do', requestData, function (resp) {
                //if(sessionStorage.getItem("newHW") == 1){
                    //scoreManager("发送作业", 3);
                //}
                //保存课程相关
                UpdateFZCourseInfoForHomework("homework");
            });

        })

        //配置练习
        $("#conf").on('click', function(){
            var exeid = $(this).attr("value");
            var lessonId = sessionStorage.getItem("lessonId");
            var homeworkId = sessionStorage.getItem("homeworkId");
            configdDtk(exeid,lessonId,homeworkId);
        })

    }

    //配置练习
    function configdDtk(exeid, lesson, homework) {
        $.ajax({
            url: '/exam/canconf.do',
            data: {
                id: exeid
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.code=='200') {
                    window.location.href = '/exam/view.do?id=' + exeid+ '&lesson=' + lesson + '&homework=' + homework + '&tty=' + data.message;
                } else {
                    alert("亲，该试卷已有学生答题啦,不能再配置了哟！");
                    return;
                }
            }

        });

    }

    function newVoice(url) {
        return $('<p><a class="voice" onclick="playVoice(\'' + url + '\');" url="' + url + '"><img src="/img/yuyin.png" style="width:160px;height:22px;">播放</a></p>').hover(function () {
            if ($(this).find('img[src="/img/dustbin.png"]').length == 0) {
                $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

            } else {
                $(this).find('img[src="/img/dustbin.png"]').show();
            }
        }, function () {
            $(this).find('img[src="/img/dustbin.png"]').hide();
        });
    }

    module.exports = editHW;
});
