
'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var dorm = {},
        Common = require('common');
    var Paginator = require('initPaginator');
    //提交参数
    var dormData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * dorm.init()
     */
    dorm.init = function(){


        //设置初始页码
        dormData.page = 1;
        //设置每页数据长度
        dormData.pageSize = 12;
        dorm.selDormitoryEntryList();
        $(".dorm-newly").click(function(){
            $('#areaName').val('');
            $('#remark').val('');
            $('#dormId').val('');
            $("#dorm-new").show();
            $(".bg").show();
        })
        $('.dorm-QX').click(function(){
            $("#dorm-new").hide();
            $(".bg").hide();
            $("#dorm-QC").hide();
            $("#dorm-QCC").hide();
            $("#dorm-newW").hide();
            $("#dorm-QCCC").hide();
            $("#dorm-newWw").hide();
            $("#dorm-QSQC").hide();
            $("#dorm-QSRZ").hide();
            $("#dorm-QSTZ").hide();
            $(".dorm-clean").hide();
        })
        $(".dorm-dell").click(function(){
            $("#dorm-QCC").show();
            $(".bg").show();
        })
        $(".dorm-delll").click(function(){
            $("#dorm-QCCC").show();
            $(".bg").show();
        })
        $(".dorm-editt").click(function(){
            $("#dorm-newW").show();
            $(".bg").show();
        })
        $(".loop-add").click(function(){
            $('#loopName').val('');
            $('#remark2').val('');
            $('#loopId').val('');
            $("#dorm-newW").show();
            $(".bg").show();
        })

        $(".room-add").click(function(){
            $('#roomName').val('');
            $('#remark3').val('');
            $('#bedNum').val('1');
            $('input:radio[name=na]')[0].checked = true;
            $('#roomId').val('');
            $("#dorm-newWw").show();
            $(".bg").show();
        })
        $(".dorm-RZGL").click(function(){
            $("#dorm-GL").hide();
            $("#dorm-check").show();
            $('.check-right').show();
            $(".head-dorm").append("&gt;<a class='cinManage'>入住管理</a>");
            $(".cinManage").click(function(){
                dorm.clickTab();
            });
            $(".dormManage").click(function(){
                dorm.hideOther();
            });
            dorm.selDormManageListInfo();
        })
        $(".check-QC").click(function(){
            $("#dorm-QSQC").show();
            $(".bg").show();
        })
        $(".check-TZ").click(function(){
            $("#dorm-QSTZ").show();
            $(".bg").show();
        })
        $(".dorm-RZTJ").click(function(){
            $("#dorm-check").hide();
            $("#dorm-RZMD").show();
            $('.check-right').hide();
            $(".head-dorm").append("&gt;<a class='loopManage'>入住名单</a>");
            $(".cinManage").click(function(){
                dorm.clickTab();
            });
            dorm.selRoomUserList(1);
        });
        $(".cleanAll").click(function(){
            $('#reson').val('');
            $(".dorm-clean").show();
            $(".bg").show();
        });
        $('.dorm-QCTJ').click(function(){
            $("#dorm-check").hide();
            $("#dorm-QCMD").show();
            $('.check-right').hide();
            $(".head-dorm").append("&gt;<a class='loopManage'>迁出名单</a>");
            $(".cinManage").click(function(){
                dorm.clickTab();
            });
            $(".dormManage").click(function(){
                //dorm.clickTab();
            });

            dorm.selMoveUserList(1);
        })
        $('.sure-area').click(function(){
            dorm.addOrUpdDormitoryEntry();
        });
        $('#seach7').click(function(){
            dorm.selMoveUserList(1);
        });
        $('#seach8').click(function(){
            dormData.page = 1;
            dorm.selRoomUserList(1);
        });
        $('.sure-loop').click(function(){
            dorm.addOrUpdLoopEntry();
        });
        $('.sure-room').click(function(){
            dorm.addOrUpdRoomEntry();
        });
        $('.sure-roomUser').click(function(){
            dorm.addRoomUserInfo();
        });
        $('.sure-EditUser').click(function(){
            dorm.updateUserRoom();
        });
        $('.sure-clean').click(function(){
            dorm.cleanRoomUserInfo();
        });

        $('#seachRooms').click(function(){
            dorm.selDormManageListInfo();
        });

        $('.sure-qianchu').click(function(){
            dormData.roomId = $('#roomId4').val();
            dormData.bedNum = $('#bedNum4').val();
            dormData.cause = $('#cause4').val();
            if ($('#cause4').val().length == 0 || $.trim($('#cause4').val()) == '') {
                alert("迁出原因不可为空！");
                return;
            }
            dorm.moveUserInfo();
        });

        $('.sure-del-dorm2').click(function(){
            dormData.dormId = $('#dormId2').val();
            dorm.deleteDormitoryInfo();
        });
        $('.sure-del-loop').click(function(){
            dormData.loopId = $('#loopId2').val();
            dorm.deleteLoopInfo();
        });
        $('.sure-del-room').click(function(){
            dormData.roomId = $('#roomId2').val();
            dorm.deleteRoomInfo();
        });
        $('#dormlist').change(function(){
            var did = $(this).val();
            if (did!='') {
                dormData.dormId = did;
                dorm.selLoopEntryList(2,did);
            }
        });
        $('#gradeList').change(function(){
            dorm.selGradeClassUserInfo(1);
        });
        $('#classList').change(function(){
            dorm.selGradeClassUserInfo(2);
        });
        $('#dormList6').change(function(){
            dormData.dormId = $('#dormList6').val();
            dormData.sex = $('#sex5').val();
            dorm.selRoomOptionInfo(1);
        });
        $('#loopList6').change(function(){
            dormData.loopId = $('#loopList6').val();
            dormData.sex = $('#sex5').val();
            dorm.selRoomOptionInfo(2);
        });
        $('#roomList6').change(function(){
            dormData.roomId = $('#roomList6').val();
            dormData.sex = $('#sex5').val();
            dorm.selRoomOptionInfo(3);
        });
        $('#dormList7').change(function(){
            dormData.dormId = $('#dormList7').val();
            dorm.selLoopEntryList(2,$('#dormList7').val());
        });
        $('#dormList8').change(function(){
            dormData.dormId = $('#dormList8').val();
            dorm.selLoopEntryList(2,$('#dormList8').val());
        });
        $('#loopList7').change(function(){
            dormData.loopId = $('#loopList7').val();
            dorm.selRoomEntryList(2);
        });
        $('#loopList8').change(function(){
            dormData.loopId = $('#loopList8').val();
            if ($('#loopList8').val()=='') {
                dormData.dormId = $('#dormList8').val();
                dorm.selLoopEntryList(2,$('#dormList8').val());
            } else  {
                dorm.selRoomEntryList(2);
            }
        });
        $('#dormlist2').change(function(){
            var did = $(this).val();
            if (did!='') {
                dormData.dormId = did;
                dorm.selLoopEntryList(2,did);
            }
        });

    };
    dorm.selDormitoryEntryList = function() {
        Common.getData('/dormManage/selDormitoryEntryList.do',dormData,function(rep){
            $('.dormList').html('');
            Common.render({tmpl:$('#dormList_templ'),data:rep,context:'.dormList'});
            if (rep.rows.length!=0) {
                $('#dormlist').empty();
                for (var i=0;i<rep.rows.length;i++) {
                    $('#dormlist').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                }
                $('#dormList7').empty();
                //$('#dormList7').append("<option value=''>全部</option>");
                for (var i=0;i<rep.rows.length;i++) {
                    $('#dormList7').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                }
                $('#dormList8').empty();
                //$('#dormList8').append("<option value=''>全部</option>");
                for (var i=0;i<rep.rows.length;i++) {
                    $('#dormList8').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                }
                dormData.dormId = $('#dormlist').val();
                dorm.selLoopEntryList(2,$('#dormlist').val());
            }

            $('.del-dorm').click(function(){
                $('#dormId2').val($(this).parent().attr('did'));
                $("#dorm-QC").show();
                $(".bg").show();
            });
            $(".edit-dorm").click(function(){
                $('#areaName').val($(this).attr('nm'));
                $('#remark').val($(this).attr('rmk'));
                $('#dormId').val($(this).parent().attr('did'));
                $("#dorm-new").show();
                $(".bg").show();
            });
            $(".dorm-detail").click(function(){
                $(".head-dorm").append("<a class='loopnm' did="+$(this).parent().attr('did')+">&gt;"+$(this).attr('nm')+"</a>");
                $(".dormManage").click(function(){
                    dorm.hideOther();
                });
                dormData.dormId = $(this).parent().attr('did');
                dorm.selLoopEntryList(1);
                $("#dorm-SS").show();
                $("#dorm-GL").hide();
                $("#dorm-SSL").hide();
            });
        });
    }
    dorm.selRoomEntryList = function(type) {
        Common.getData('/dormManage/selRoomEntryList.do', dormData, function (rep) {
            if (type==1) {
                $('.roomList').html('');
                Common.render({tmpl: $('#roomList_templ'), data: rep, context: '.roomList'});
                $('.del-room').click(function(){
                    $('#roomId2').val($(this).parent().attr('rid'));
                    $("#dorm-QCCC").show();
                    $(".bg").show();
                });
                $('.edit-room').click(function(){
                    $('#roomName').val($(this).attr('nm'));
                    $('#remark3').val($(this).attr('rmk'));
                    $('#bedNum').val($(this).attr('bn'));
                    if ($(this).attr('rty')==1) {
                        $('input:radio[name=na]')[0].checked = true;
                    } else  {
                        $('input:radio[name=na]')[1].checked = true;
                    }
                    $('#roomId').val($(this).parent().attr('rid'));
                    $("#dorm-newWw").show();
                    $(".bg").show();
                });
                //$(".dorm-XQ").click(function(){
                //    dormData.loopId = $(this).parent().attr('lid');
                //    dorm.selLoopEntryList();
                //    $("#dorm-SS").hide();
                //    $("#dorm-GL").hide();
                //    $("#dorm-SSL").show();
                //});
            } else {
                $('#roomList7').empty();
                $('#roomList7').append("<option value=''>全部</option>");
                for (var i=0;i<rep.rows.length;i++) {
                    $('#roomList7').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                }
                $('#roomList8').empty();
                $('#roomList8').append("<option value=''>全部</option>");
                for (var i=0;i<rep.rows.length;i++) {
                    $('#roomList8').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                }
            }
        });
    }
    dorm.selLoopEntryList = function(type,dormId) {
        Common.getData('/dormManage/selLoopEntryList.do', dormData, function (rep) {
            if (type==2) {
                if (rep.rows!=null && rep.rows.length!=0) {
                    $('#looplist').empty();
                    $('#looplist').append("<option value=''>全部</option>");
                    for (var i=0;i<rep.rows.length;i++) {
                        $('#looplist').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                    }
                    $('#loopList7').empty();
                    $('#loopList7').append("<option value=''>全部</option>");
                    for (var i=0;i<rep.rows.length;i++) {
                        $('#loopList7').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                    }
                    $('#loopList8').empty();
                    $('#loopList8').append("<option value=''>全部</option>");
                    for (var i=0;i<rep.rows.length;i++) {
                        $('#loopList8').append("<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>");
                    }
                    dormData.loopId = '';
                    dormData.dormId = dormId;
                    dorm.selRoomEntryList(2);
                }
            } else {
                $('.loopList').html('');
                Common.render({tmpl: $('#loopList_templ'), data: rep, context: '.loopList'});
                $('.del-loop').click(function(){
                    $('#loopId2').val($(this).parent().attr('lid'));
                    $("#dorm-QCC").show();
                    $(".bg").show();
                });
                $('.edit-loop').click(function(){
                    $('#loopName').val($(this).attr('nm'));
                    $('#remark2').val($(this).attr('rmk'));
                    $('#loopId').val($(this).parent().attr('lid'));
                    $("#dorm-newW").show();
                    $(".bg").show();
                });
                $(".dorm-XQ").click(function(){
                    $(".head-dorm").append("<a class='roomManage'>&gt;"+$(this).attr('nm')+"</a>");
                    $(".loopnm").click(function(){
                        var len = $('.head-dorm a').length;
                        $('.head-dorm a').each(function (i) {
                            if (i==len-1) {
                                $(this).remove();
                            }
                        });
                        dormData.dormId = $(this).attr('did');
                        dorm.selLoopEntryList(1);
                        $("#dorm-SS").show();
                        $("#dorm-GL").hide();
                        $("#dorm-SSL").hide();
                    });
                    dormData.loopId = $(this).parent().attr('lid');
                    dorm.selRoomEntryList(1);
                    $("#dorm-SS").hide();
                    $("#dorm-GL").hide();
                    $("#dorm-SSL").show();
                });
            }
        });
    }
    dorm.deleteLoopInfo = function() {
        Common.getData('/dormManage/deleteLoopInfo.do',dormData,function(rep){
            if (rep.flag) {
                alert("删除成功！");
                $("#dorm-QCC").hide();
                $(".bg").hide();
                dorm.selLoopEntryList(1);
            } else {
                alert("删除失败！");
            }
        });
    }
    dorm.deleteRoomInfo = function() {
        Common.getData('/dormManage/deleteRoomInfo.do',dormData,function(rep){
            if (rep.flag) {
                alert("删除成功！");
                $("#dorm-QCCC").hide();
                $(".bg").hide();
                dorm.selRoomEntryList(1);
            } else {
                alert("删除失败！");
            }
        });
    }
    dorm.deleteDormitoryInfo = function() {
        Common.getData('/dormManage/deleteDormitoryInfo.do',dormData,function(rep){
            if (rep.flag) {
                alert("删除成功！");
                $("#dorm-QC").hide();
                $(".bg").hide();
                dorm.selDormitoryEntryList();
            } else {
                alert("删除失败！");
            }
        });
    }
    dorm.addOrUpdRoomEntry = function() {
        var name = $.trim($('#roomName').val());
        var remark = $.trim($('#remark3').val());
        if (name == '') {
            alert("请输入房间号！");
            return;
        }
        if (remark=='') {
            alert("请输入备注！");
            return;
        }
        if (dorm.getLength(name) > 10) {
            alert('房间号最多发10个字符！');
            return;
        }
        if (dorm.getLength(remark) > 80) {
            alert('备注最多发80个字符！');
            return;
        }
        dormData.name = name;
        dormData.remark = remark;
        dormData.bedNum = $('#bedNum').val();
        dormData.roomType = $('#sex input[name="na"]:checked ').val();
        if ($('#roomId').val()=='') {
            Common.getData('/dormManage/addRoomInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("添加成功！");
                    dorm.selRoomEntryList(1);
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("添加失败！");
                }
            });
        } else {
            dormData.roomId=$('#roomId').val();
            Common.getData('/dormManage/updateRoomInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("更新成功！");
                    dorm.selRoomEntryList(1);
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("更新失败！");
                }
            });
        }
    }
    dorm.addOrUpdLoopEntry = function() {
        var name = $.trim($('#loopName').val());
        var remark = $.trim($('#remark2').val());
        if (name == '') {
            alert("请输入楼层名称！");
            return;
        }
        if (remark=='') {
            alert("请输入备注！");
            return;
        }
        if (dorm.getLength(name) > 10) {
            alert('楼层名称最多发10个字符！');
            return;
        }
        if (dorm.getLength(remark) > 80) {
            alert('备注最多发80个字符！');
            return;
        }
        dormData.name = name;
        dormData.remark = remark;
        if ($('#loopId').val()=='') {
            Common.getData('/dormManage/addLoopInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("添加成功！");
                    dorm.selLoopEntryList(1);
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("添加失败！");
                }
            });
        } else {
            dormData.loopId=$('#loopId').val();
            Common.getData('/dormManage/updateLoopInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("更新成功！");
                    dorm.selLoopEntryList(1);
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("更新失败！");
                }
            });
        }
    }
    dorm.addOrUpdDormitoryEntry = function() {
        var name = $.trim($('#areaName').val());
        var remark = $.trim($('#remark').val());
        if (name == '') {
            alert("请输入分区名称！");
            return;
        }
        if (remark=='') {
            alert("请输入备注！");
            return;
        }
        if (dorm.getLength(name) > 10) {
            alert('分区名称最多发10个字符！');
            return;
        }
        if (dorm.getLength(remark) > 80) {
            alert('备注最多发80个字符！');
            return;
        }
        dormData.name = name;
        dormData.remark = remark;
        if ($('#dormId').val()=='') {
            Common.getData('/dormManage/addDormitoryInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("添加成功！");
                    dorm.selDormitoryEntryList();
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("添加失败！");
                }
            });
        } else {
            dormData.dormId=$('#dormId').val();
            Common.getData('/dormManage/updateDormitoryInfo.do',dormData,function(rep){
                if (rep.flag) {
                    alert("更新成功！");
                    dorm.selDormitoryEntryList();
                    $('.dorm-new').hide();
                    $(".bg").hide();
                } else {
                    alert("更新失败！");
                }
            });
        }

    }
    dorm.getLength = function(content) {
        var realLength = 0, len = content.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = content.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        return realLength;
    };

    dorm.selDormManageListInfo = function() {
        dormData.dormId = $('#dormlist').val();
        dormData.loopId = $('#looplist').val();
        dormData.sex = $('#sexlist').val();
        Common.getData('/dormManage/selDormManage.do',dormData,function(rep){
            $('#dormNm').html($('#dormlist').find("option:selected").text());
            $('#loopCnt').text(rep.loopCnt);
            $('#roomCnt').text(rep.roomCnt);
            $('#cin').text(rep.cin);
            $('#cbed').text(rep.cbed);
            $('#croom').text(rep.croom);
            $('.roomUsers').html('');
            if (rep.roomList!=null && rep.roomList.length!=0) {
                Common.render({tmpl:$('#roomUsers_templ'),data:rep,context:'.roomUsers'});
            }
            $(".check-RZZ").click(function(){
                dormData.roomId = $(this).attr('roomId');
                dormData.bedNum = $(this).attr('bnum');
                $('#roomId3').val($(this).attr('roomId'));
                dorm.selBedOptionInfo(dormData.bedNum);
                $("#dorm-QSRZ").show();
                $(".bg").show();
            });
            $(".check-TZ-I").click(function(){
                $('#userName5').text($(this).parent().attr('unm'));
                $('#bedNum5').text($(this).parent().attr('bnum'));
                $('#dormName5').text($('#dormNm').text());
                $('#loopName5').text($(this).parent().attr('lnm'));
                $('#roomName5').text($(this).parent().attr('rnm'));
                $('#sex5').val($(this).parent().attr('sex'));
                $('#orgRoomId').val($(this).parent().attr('roomId'));
                $('#orgBedNum').val($(this).parent().attr('bnum'));
                dormData.sex = $(this).parent().attr('sex');
                dorm.selRoomOptionInfo(0);
                $("#dorm-QSTZ").show();
                $(".bg").show();
            });
            $(".check-TZ-II").click(function(){
                $('#userName').text($(this).parent().attr('unm'));
                $('#roomId4').val($(this).parent().attr('roomId'));
                $('#bedNum4').val($(this).parent().attr('bnum'));
                $("#dorm-QSQC").show();
                $(".bg").show();
            });

        });
    }

    dorm.selBedOptionInfo = function(num) {
        Common.getData('/dormManage/selBedOptionInfo.do',dormData,function(rep){
            if (rep.gradeList!=null && rep.gradeList.length!=0) {
                $('#gradeList').empty();
                for (var i=0;i<rep.gradeList.length;i++) {
                    $('#gradeList').append("<option value="+rep.gradeList[i].id+">"+rep.gradeList[i].name+"</option>");
                }
                $('#classList').empty();
                for (var i=0;i<rep.classList.length;i++) {
                    $('#classList').append("<option value="+rep.classList[i].id+">"+rep.classList[i].className+"</option>");
                }
                $('#userList').empty();
                for (var i=0;i<rep.userList.length;i++) {
                    $('#userList').append("<option value="+rep.userList[i].id+">"+rep.userList[i].userName+"</option>");
                }
                $('#bedlist').empty();
                for (var i=0;i<rep.nums.length;i++) {
                    $('#bedlist').append("<option value="+rep.nums[i]+">"+rep.nums[i]+"</option>");
                }
                $('#dormName2').text(rep.dormName);
                $('#loopName2').text(rep.loopName);
                $('#roomName2').text(rep.roomName);
                $('#bedlist').val(num);
            }
        });
    }
    dorm.selGradeClassUserInfo = function(type) {
        dormData.gradeId = $('#gradeList').val();
        dormData.classId = $('#classList').val();
        dormData.roomId = $('#roomId3').val();
        dormData.type = type;
        Common.getData('/dormManage/selGradeClassUserInfo.do',dormData,function(rep){
            if (type==1) {
                $('#classList').empty();
                for (var i=0;i<rep.classList.length;i++) {
                    $('#classList').append("<option value="+rep.classList[i].id+">"+rep.classList[i].className+"</option>");
                }
            }
            $('#userList').empty();
            for (var i=0;i<rep.userList.length;i++) {
                $('#userList').append("<option value="+rep.userList[i].id+">"+rep.userList[i].userName+"</option>");
            }
        });
    }
    //人员入住
    dorm.addRoomUserInfo = function() {
        dormData.roomId = $('#roomId3').val();
        dormData.userId = $('#userList').val();
        dormData.bedNum = $('#bedlist').val();
        if ($('#userList').val()==null||$('#userList').val()=='') {
            alert("人员不能为空！");
            return;
        }
        Common.getData('/dormManage/addRoomUserInfo.do',dormData,function(rep){
            if(rep.flag) {
                alert("人员入住成功！");
                $("#dorm-QSRZ").hide();
                $(".bg").hide();
                dorm.selDormManageListInfo();
            } else {
                alert("人员入住失败！");
            }
        });
    }
    //迁出人员
    dorm.moveUserInfo = function() {
        Common.getData('/dormManage/moveUserInfo.do',dormData,function(rep){
            if(rep.flag) {
                alert("人员迁出成功！");
                $("#dorm-QSQC").hide();
                $(".bg").hide();
                dorm.selDormManageListInfo();
            } else {
                alert("人员迁出失败！");
            }
        });
    }
    //调整宿舍
    dorm.updateUserRoom = function() {
        if ($('#roomList6').val()==null) {
            alert("该楼层已满，不能调整！");
            return;
        }
        if ($('#bedList6').val()==null) {
            alert("该房间已满，不能调整！");
            return;
        }
        dormData.orgRoomId = $('#orgRoomId').val();
        dormData.orgBedNum = $('#orgBedNum').val();
        dormData.roomId = $('#roomList6').val();
        dormData.bedNum = $('#bedList6').val();
        Common.getData('/dormManage/updateUserRoom.do',dormData,function(rep){
            if(rep.flag) {
                alert("宿舍调整成功！");
                $("#dorm-QSTZ").hide();
                $('.bg').hide();
                dorm.selDormManageListInfo();
            } else {
                alert("宿舍调整失败！");
            }
        });
    }
    //迁出名单列表
    dorm.selMoveUserList = function(page) {
        dormData.page = page;
        dormData.dormId = $('#dormList7').val();
        dormData.loopId = $('#loopList7').val();
        dormData.roomId = $('#roomList7').val();
        dormData.userName = $('#userName7').val();
        Common.getData('/dormManage/selMoveUserList.do',dormData,function(rep){
            $('.moveUser').html('');
            Common.render({tmpl: $('#moveUser_templ'), data: rep, context: '.moveUser'});
            var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).off("click");
                        $(this).click(function () {
                            dorm.selMoveUserList($(this).text());
                        });
                    });
                    $('.first-page').off("click");
                    $('.first-page').click(function () {
                        dorm.selMoveUserList(1);
                    });
                    $('.last-page').off("click");
                    $('.last-page').click(function () {
                        dorm.selMoveUserList(totalPage);
                    });
                }
            }
            Paginator.initPaginator(option);
        });
    }
    // 分页初始化
    dorm.initPaginator2=function (option) {
        var totalPage = '';
        $('.page-paginator2').show();
        $('.page-index2').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        dorm.buildPaginator2(totalPage, option.currentpage);
        option.operate(totalPage);
    }
    // 分页初始化
    dorm.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        dorm.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    dorm.buildPaginator =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else {
                $('.page-index').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    }

    dorm.buildPaginator2 =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index2').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index2').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index2').append('<i>···</i>');
            } else {
                $('.page-index2').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index2').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index2').append('<span>' + i + '</span>');
                }
            }
        }
    }
    //住宿名单列表
    dorm.selRoomUserList = function(page) {
        dormData.page = page;
        dormData.dormId = $('#dormList8').val();
        dormData.loopId = $('#loopList8').val();
        dormData.roomId = $('#roomList8').val();
        dormData.userName = $('#userName8').val();
        Common.getData('/dormManage/selRoomUserList.do',dormData,function(rep){
            $('.roomUser').html('');
            Common.render({tmpl: $('#roomUser_templ'), data: rep, context: '.roomUser'});
            var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).off("click");
                        $(this).click(function () {
                            dorm.selRoomUserList($(this).text());
                        });
                    });
                    $('.first-page').off("click");
                    $('.first-page').click(function () {
                        dorm.selRoomUserList(1);
                    });
                    $('.last-page').off("click");
                    $('.last-page').click(function () {
                        dorm.selRoomUserList(totalPage);
                    });
                }
            }
            Paginator.initPaginator(option);
        });
    }
    //清空数据
    dorm.cleanRoomUserInfo = function() {
        if ($.trim($('#reson').val()) == '') {
            alert("请输入清空原因！");
            return;
        }
        dormData.remark = $.trim($('#reson').val());
        Common.getData('/dormManage/cleanRoomUserInfo.do',dormData,function(rep){
            if (rep.flag) {
                alert("清空成功！");
                dorm.selDormManageListInfo();
                $('.dorm-clean').hide();
                $(".bg").hide();
            } else {
                alert("清空失败！");
            }
        });
    }
    dorm.selRoomOptionInfo = function(type) {
        dormData.type = type;
        Common.getData('/dormManage/selRoomOptionInfo.do',dormData,function(rep){
            if (type==0) {
                $('#dormList6').empty();
                if (rep.dormList!=null && rep.dormList.length!=0) {
                    for (var i=0;i<rep.dormList.length;i++) {
                        $('#dormList6').append("<option value="+rep.dormList[i].id+">"+rep.dormList[i].name+"</option>");
                    }
                }
            }
            if (type==0 || type==1) {
                $('#loopList6').empty();
                if (rep.loopList!=null && rep.loopList.length!=0) {
                    for (var i=0;i<rep.loopList.length;i++) {
                        $('#loopList6').append("<option value="+rep.loopList[i].id+">"+rep.loopList[i].name+"</option>");
                    }
                }

            }
            if (type==0||type==1||type==2) {
                $('#roomList6').empty();
                if (rep.roomList!=null && rep.roomList.length!=0) {
                    for (var i=0;i<rep.roomList.length;i++) {
                        $('#roomList6').append("<option value="+rep.roomList[i].id+">"+rep.roomList[i].name+"</option>");
                    }
                }
            }
            $('#bedList6').empty();
            if (rep.bedNums!=null && rep.bedNums.length!=0) {
                for (var i=0;i<rep.bedNums.length;i++) {
                    $('#bedList6').append("<option value="+rep.bedNums[i]+">"+rep.bedNums[i]+"</option>");
                }
            }
        });
    }
    dorm.clickTab = function() {
        $("#dorm-GL").hide();
        $("#dorm-check").show();
        $('.check-right').show();
        $("#dorm-QCMD").hide();
        $("#dorm-RZMD").hide();
        $(".head-dorm").empty();
        $(".head-dorm").append("<a class='dormManage'>宿舍管理</a>&gt;<a class='cinManage'>入住管理</a>");
        dorm.selDormManageListInfo();
        $(".dormManage").click(function(){
            dorm.hideOther();
        });
    }
    dorm.hideOther = function() {
        $("#dorm-GL").show();
        $(".check-right").hide();
        $("#dorm-check").hide();
        $("#dorm-QCMD").hide();
        $("#dorm-RZMD").hide();
        $("#dorm-SS").hide();
        $("#dorm-SSL").hide();
        $(".head-dorm").empty();
        $(".head-dorm").append("<a class='dormManage'>宿舍管理</a>");
        dorm.selDormitoryEntryList();
    }
    dorm.init();
});