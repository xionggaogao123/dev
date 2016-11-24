<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- =========新建群组聊天=================== -->
<!-- <div class="center_main_popup" id="newGroup"> -->
    <div class="center_main_popup_main" id="newGroup" style="z-index: 1111;">
        <!--======================================新建群组聊天弹出框头部===================================================-->
        <div class="center_main_popup_main_top">
            <div class="new_center_main_popup_main_I">
                <span id="popup_title">新建群组</span>
            </div>
            <div class="new_center_main_popup_main_II">
                <input placeholder="请输入群组名称，不超过10个字" id="groupName">
            </div>

            <!--======================================新建群组聊天弹出框底部===================================================-->
            <div class="center_main_popup_main_bottom">
                <div class="center_main_popup_main_bottom_qd" onclick="newChartGroup(this)">确定</div>
                <div class="center_main_popup_main_bottom_qx" onclick="$('#bg').hide();$('#newGroup').hide();$('.seachkeyword').val('');seachkey('');">取消</div>
            </div>
            <!--========================================新建群组聊天中间===================================================================-->
            <div class="center_main_popup_main_LT">
                <div class="new_center_main_popup_main_LT" style="overflow: auto;">
                    <div style="overflow: visible !important;height: 50px;">
                        <img src="/img/fdjing_2.png">
                        <input placeholder="请输入关键字搜索" onkeyup="seachkey($(this).val())" class="seachkeyword">
                    </div>
                    <ul style="overflow: auto;height: 365px;">
                        
                    </ul>
                </div>
                <div class="new_center_main_popup_main_RT">
                    <div class="new_center_main_popup_main_RT_info">
                        <div>
                            <div class="new_center_main_popup_main_RT_I">群组成员</div>
                            <div id="selected-contact-count" class="new_center_main_popup_main_RT_II">0</div>
                            <ul id="select-group-list">
                                
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
<!-- </div> -->
<!-- =========新建群组聊天=================== -->


<!-- =========群组管理=================== -->
<!-- <div class="center_main_popup" id="managerGroup" style="z-index: 1111;"> -->
    <div class="center_main_popup_main" id="managerGroup" style="z-index: 1111;">
        <!--======================================群组管理弹出框头部===================================================-->
        <div class="center_main_popup_main_top">
            <div class="new_center_main_popup_main_I">
                <span id="popup_title_manager">成员管理</span>
            </div>
            <div class="new_center_main_popup_main_II">
                <input placeholder="请输入群组名称，不超过10个字" id="groupName">
            </div>

            <!--======================================群组管理弹出框底部===================================================-->
            <div class="center_main_popup_main_bottom">
                <div class="center_main_popup_main_bottom_qd" onclick="updateGroup(this)">确定</div>
                <div class="center_main_popup_main_bottom_qx" onclick="$('#bg').hide();$('#managerGroup').hide();$('.seachkeyword').val('');seachkey('');">取消</div>
            </div>
            <!--========================================群组管理中间===================================================================-->
            <div class="center_main_popup_main_LT">
                <div class="new_center_main_popup_main_LT" style="overflow: auto;">
                    <div style="overflow: visible !important;height: 50px;">
                        <img src="/img/fdjing_2.png">
                        <input placeholder="请输入关键字搜索" onkeyup="seachkey($(this).val())" class="seachkeyword">
                    </div>
                    <ul style="overflow: auto;height: 370px;margin-top: -5px;">
                        
                    </ul>
                </div>
                <div class="new_center_main_popup_main_RT">
                    <div class="new_center_main_popup_main_RT_info">
                        <div style="overflow: auto;height: 394px;">
                            <div class="new_center_main_popup_main_RT_I">群组成员</div>
                            <div id="selected-contact-count-manager" class="new_center_main_popup_main_RT_II">0</div>
                            <ul id="select-group-list-manager">
                                
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
<!-- </div> -->
<!-- =========群组管理=================== -->


<!--========================================群组成员查看==========================================================-->
<!-- <div class="center_main_popup" id="groupMemberView" style="z-index: 1111;"> -->
    <div class="center_main_popup_main" id="groupMemberView" style="z-index: 1111;">
        <!--======================================群组成员查看聊天弹出框头部===================================================-->
        <div class="center_main_popup_main_top">
            <div class="new_center_main_popup_main_SUB">
                <span>群组成员</span>
                <div><i class="fa fa-close fa-2x" onclick="$('#bg').hide();$('#groupMemberView').hide();"></i></div>
            </div>
        </div>
        <!--====================================群组成员查看中间内容========================================================================-->
        <div class="new_center_main_popup_main_SUB_info">
            <ul style="overflow-y: auto;height: 450px;">
                <!--=============================讨论组发起人========================================-->
                <li>
                    <div class="new_center_main_popup_main_SUB_I">
                        <span>发起人</span>
                    </div>
                    <div class="new_center_main_popup_main_SUB_II" id="manager-info">
                        <img src="">
                        <span></span>
                    </div>
                </li>
                <!--=============================讨论组成员========================================-->
                <li>
                    <div class="new_center_main_popup_main_SUB_I">
                        <span>成员<span id="selected-contact-count"></span></span>
                    </div>
                    <ul id="group-member-list-view">
                        
                    </ul>
                </li>

            </ul>
        </div>
    </div>
<!-- </div> -->



<!-- ===============群组聊天弹出框================= -->
<div id="quit-confirm-dialog" class="center_main_popup"  style="z-index: 99999999999;">
<div class="center_main_popup" style="overflow: visible;">
    <div class="center_main_popup_title">
        <span>提示</span>
        <div onclick="$('#quit-confirm-dialog').hide();$('#bg').hide();">x</div>

    </div>
    <div class="center_main_popup_center">
        <img src="images/dayuan_03.png">
        <span id="dialog-msg">你确定要退出这个群?</span>
    </div>
    <div class="center_main_popup_bottom">
        <span class="center_main_popup_bottom_DD" id="confirmQuit" onclick="">确定</span>
        <span class="center_main_popup_bottom_XX" onclick="$('#quit-confirm-dialog').hide();$('#bg').hide();">取消</span>
    </div>
</div>
</div>



<!-- ===============群组文件列表================= -->
<!-- <div id="group-file-list" class="center_main_popup" style="z-index: 1111;"> -->
<div class="center_main_popup_main" id="group-file-list" style="z-index: 1111;">
<!--======================================群组聊天弹出框头部===================================================-->
<div class="center_main_popup_main_top">
    <!-- <div class="center_main_popup_main_I">
        <img src="images/juchi.png">
    </div>
    <div class="center_main_popup_main_II">
        <img src="images/wjjia.png">
    </div>
    <div class="center_main_popup_main_III">
        <span>讨论啊快</span>
    </div> -->
    <div class="center_main_popup_main_IIII">
        <span><i onclick="$('#group-file-list').hide();$('#bg').hide();" class="fa fa-close fa-2x"></i></span>
    </div>
</div>
<!--======================================群组聊天弹出框底部===================================================-->
<div class="center_main_popup_main_bottom">
    
</div>
<!--===================================群组上传统计===============================================================-->
<div class="center_main_popup_main_SX_TJ">
    <div class="center_main_popup_main_SX_I">
        <div class="center_main_popup_main_SX_LT" onclick="$('#group-file-list').hide();$('#bg').hide();">
            返回聊天
        </div>
        <div class="center_main_popup_main_SX_RT">
            <div class="center_main_popup_main_SX_II">共<span id="file-total"></span>个文件</div>
            <labe for="upload-file"><div class="center_main_popup_main_SX_III" onclick="$('#upload-file').trigger('click');">上传</div></labe>
        </div>
        <input type="file" id="upload-file" class="upload-file" multiple="multiple" style="display: none;">
    </div>
</div>
<!--================================群组上传内容=================================-->
<div class="center_main_popup_main_SX_main" style="overflow: auto !important;">
    <div class="center_main_popup_main_SX">
        <ul id="chat-file-list">         

        </ul>
    </div>
</div>
</div>
<!-- </div> -->