/* 
 * @Author: Tony
 * @Date:   2015-11-12 10:47:01
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-11-18 10:42:04
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var copy = $('#copy').html();
    var pdfUrl = '';

    (function () {

        getMyClassAndDepart();
        trigger();

        $('.btn-tjtm').click(function () {
            var copy1 = $('#forcopy').html();
            $('#forcopy').append(copy1);
            trigger();
            formateIndex();
        })

        $('body').on('click', '.iadd', function () {
            var span = $(this).prev().prev();
            var index = span.attr('index');
            span.text(getOptions(index - 0 + 1));
            span.attr('index', index - 0 + 1)
        })

        $('body').on('click', '.ijian', function () {
            var span = $(this).prev();
            var index = span.attr('index');
            span.text(getOptions(index - 1));
            span.attr('index', index - 1)
        })

        $('body').on('click', '#confirm', function () {
            var num = $('#number').val();
            if (num <= 0) {
                alert("题目个数应大于零");
                return false;
            }
            var copy1 = $('#copy').html();
            for (var i = 0; i < num; i++) {
                $('#forcopy').append(copy1);
            }
            trigger();
            formateIndex();
            $(".div-pltj").fadeToggle("fast");
        })

        $('body').on('click', "input[name='role']:checkbox", function () {
            var role = $(this).attr('id');
            var c = $(this).prop('checked');
            if ('allRole' == role) {
                $("input[name='role']:checkbox").each(function (index, elem) {
                    $(elem).prop('checked', c);
                })
            } else {
                if (false == c) {
                    $('#allRole').prop('checked', false);
                }
            }

        })

        $('body').on('click', "input[name='classanddepart']:checkbox", function () {
            var classId = $(this).attr('classid');
            var c = $(this).prop('checked');
            if (null != classId && true == c) {
                $('#allClass').prop('checked', false);
            } else if (null == classId && true == c) {
                $("input[name='classanddepart']:checkbox").each(function (index, elem) {
                    if (0 != index) {
                        $(elem).prop('checked', false);
                    }
                })
            }
        })

        $('body').on('blur', '.min', function () {
            $(this).prop('value', 3);
            //alert($(this).val());
            //alert($(this).attr('value'));
        })


        $('body').on('click', '#publish', function () {
            var questionnaireDTO = {};
            if ('' == pdfUrl) {
                alert("请上传文件");
                return false;
            }
            var qName = $('#qName').val();
            if ('' == qName) {
                alert("请输入问卷名称");
                return false;
            }
            var qDate = $('#qDate').val();
            if ('' == qDate) {
                alert("请输入截止日期");
                return false;
            }
            var endDate = qDate.replace(/-/g, '');
            questionnaireDTO.name = qName;
            questionnaireDTO.endDate = endDate;
            questionnaireDTO.teacherRespondent = $('#teacher').prop('checked');
            questionnaireDTO.studentRespondent = $('#student').prop('checked');
            questionnaireDTO.parentRespondent = $('#parent').prop('checked');
            var classIds = [];
            $("input[name='classanddepart']:checkbox:checked").each(function (index, elem) {
                classIds[index] = $(elem).attr('classid');
            })
            //questionnaireDTO.classIds = classIds;
            questionnaireDTO.classList = classIds.join(',');
            questionnaireDTO.docUrl = pdfUrl;

            var answerSheet = [];
            $('#forcopy').children().each(function (index, elem) {
                var name = $(elem).find('.gretd').text();
                if ("单选" == name) {
                    var num = $(elem).find('.abcd').attr('index');
                    answerSheet[index] = parseInt(num);
                } else if ("多选" == name) {
                    var num = $(elem).find('.abcd').attr('index');
                    answerSheet[index] = 0 - num;
                } else if ("问答" == name) {
                    answerSheet[index] = 0;
                } else if ("打分" == name) {
                    var min = $(elem).find('.min').val();
                    var max = $(elem).find('.max').val();
                    if (min < 0 || max < 0) {
                        alert("打分范围不能小于0");
                        return false;
                    }
                    if (min > 9999 || max > 9999) {
                        alert("打分范围不能大于9999");
                        return false;
                    }
                    if (min >= max) {
                        alert("最低分应小于最高分");
                        return false;
                    }
                    var str = '1';
                    var obj = {};
                    obj.value = min;
                    funFormat(obj, 4);
                    str += obj.value;
                    obj.value = max;
                    funFormat(obj, 4);
                    str += obj.value;
                    answerSheet[index] = str;
                }
            })
            questionnaireDTO.answerSheetList = answerSheet.join(',');
            //questionnaireDTO.answerSheet = answerSheet;

            Common.getData('/questionnaire/new1.do', questionnaireDTO, function (resp) {
                alert("ss");
            })


        })


        /*批量添加*/
        $(".div-pltj .em-dax , .div-pltj .em-dux").click(function () {
            $(".div-pltj tr:nth-child(3)").show();
            $(".div-pltj tr:nth-child(4)").hide();
            $(".div-pltj tr:nth-child(5)").hide();
            $('#copy').find('.tr3').show();
            $('#copy').find('.tr4').hide();
            $('#copy').find('.tr5').hide();
        });


        $(".div-pltj .em-wd").click(function () {
            $(".div-pltj tr:nth-child(3)").hide();
            $(".div-pltj tr:nth-child(4)").hide();
            $(".div-pltj tr:nth-child(5)").hide();
            $('#copy').find('.tr3').hide();
            $('#copy').find('.tr4').hide();
            $('#copy').find('.tr5').hide();
        });

        $(".div-pltj .em-df").click(function () {
            $(".div-pltj tr:nth-child(3)").hide();
            $(".div-pltj tr:nth-child(4)").show();
            $(".div-pltj tr:nth-child(5)").show();
            $('#copy').find('.tr3').hide();
            $('#copy').find('.tr4').show();
            $('#copy').find('.tr5').show();
        });

        $(".btn-pltj").click(
            function () {
                $(".div-pltj").fadeToggle("fast");
            });

        function CloseWin() //这个不会提示是否关闭浏览器
        {
            window.opener = null;
            //window.opener=top;
            window.open("", "_self");
            window.close();
        }

        $('#file_attach').fileupload({
            url: '/questionnaire/upload1.do',
            start: function (e) {
                $('.uploading').show();
            },
            done: function (e, data) {
                pdfUrl = "/upload/questionnaire/" + data.result.url + ".pdf";
                showPdf(pdfUrl);
                $('#qName').val(data.result.name);

            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('.uploading').hide();
                $('.uploaded').show();
                setTimeout(function () {
                    $('.uploaded').hide();
                }, 2000)
            }
        });

        $('#file_attach1').fileupload({
            url: '/questionnaire/upload1.do',
            start: function (e) {
                $('.uploading').show();
            },
            done: function (e, data) {
                pdfUrl = "/upload/questionnaire/" + data.result.url + ".pdf";
                showPdf(pdfUrl);
                $('#qName').val(data.result.name);

            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('.uploading').hide();
                $('.uploaded').show();
                setTimeout(function () {
                    $('.uploaded').hide();
                }, 2000)
            }
        });

    })()


    function getMyClassAndDepart() {
        Common.getData('/questionnaire/classanddepart.do', {}, function (resp) {
            Common.render({
                tmpl: '#classanddepart_tmpl',
                data: resp,
                context: '#classanddepart',
                overwrite: 1
            })
        })
    }

    function showPdf(url) {
        $('.n-cont2').hide();
        new PDFObject({url: url}).embed('pdf');
        $('#pdf').show();
    }

    function trigger() {
        $(".n-cont3a-4 td em, .div-pltj td em").click(function () {
            var cl = $(this).attr('class');
            $(this).addClass("gretd").siblings("em").removeClass("gretd")
            $('#copy').find('.' + cl).addClass("gretd").siblings("em").removeClass("gretd")
        });

        $(".n-cont3a-4 .em-dax , .n-cont3a-4 .em-dux").click(function () {
            $(this).parents('.n-cont3a-4').find('.tr3').show();
            $(this).parents('.n-cont3a-4').find('.tr4').hide();
            $(this).parents('.n-cont3a-4').find('.tr5').hide();
            //$(this).parent().parent().next().show();
            //$(this).parent().parent().next().next().hide();
            //$(this).parent().parent().next().next().next().hide();
            //$(".n-cont3a-4 tr:nth-child(3)").show();
            //$(".n-cont3a-4 tr:nth-child(4)").hide();
            //$(".n-cont3a-4 tr:nth-child(5)").hide();
        });


        $(".n-cont3a-4 .em-wd").click(function () {
            $(this).parents('.n-cont3a-4').find('.tr3').hide();
            $(this).parents('.n-cont3a-4').find('.tr4').hide();
            $(this).parents('.n-cont3a-4').find('.tr5').hide();
            //$(this).parent().parent().next().hide();
            //$(this).parent().parent().next().next().hide();
            //$(this).parent().parent().next().next().next().hide();
            //$(".n-cont3a-4 tr:nth-child(3)").hide();
            //$(".n-cont3a-4 tr:nth-child(4)").hide();
            //$(".n-cont3a-4 tr:nth-child(5)").hide();
        });

        $(".n-cont3a-4 .em-df").click(function () {
            $(this).parents('.n-cont3a-4').find('.tr3').hide();
            $(this).parents('.n-cont3a-4').find('.tr4').show();
            $(this).parents('.n-cont3a-4').find('.tr5').show();
            //$(this).parent().parent().next().hide();
            //$(this).parent().parent().next().next().show();
            //$(this).parent().parent().next().next().next().show();
            //$(".n-cont3a-4 tr:nth-child(3)").hide();
            //$(".n-cont3a-4 tr:nth-child(4)").show();
            //$(".n-cont3a-4 tr:nth-child(5)").show();
        });

    }

    function getOptions(index) {
        if (index > 26) {
            index = 26;
        }
        var all = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
        var options = '';
        for (var i = 0; i < index; i++) {
            options += all[i] + '；';
        }
        return options;
    }

    function formateIndex() {
        $('#forcopy').children().each(function (index, elem) {
            $(elem).find('.index').text(index + 1);
        })
    }


    function funFormat(obj, intBit) {
        var val = obj.value;
        var str = "";
        var i = 0;
        if (val != "") {
            for (i = 0; i < intBit; i++)
                str = str + "0";
            val = str + val;
            val = val.substring(val.length - intBit, val.length);
            obj.value = val;
            return true;
        }
        return false;
    }

});


