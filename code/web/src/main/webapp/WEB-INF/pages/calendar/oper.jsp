<!--========================================编辑====================================-->
<div id="editDIV" class="popup_text_T" style="display:none;z-index:999999" >
    <div class="popup_text_I"></div>
    <div class="popup_text_info_I">
        <div class="popup_text_I_friest">
            <span class="popup_text_II_friest">编辑</span>
        </div>
        <div class="popup_text_KC">
            <span>标题</span>
            <input>
        </div>
        <div class="popup_text_NR">
            <span>内容</span>
            <textarea></textarea>
        </div>
        <div class="popup_text_KS">
            <span>开始时间</span>
            <input>
        </div>
        <div class="popup_text_JS">
            <span>结束时间</span>
            <input>
        </div>

        <div class="popup_text_CF">
            <span>重复</span>
            <select>
                <option>重复</option>
                <option>不重复</option>
            </select>
        </div>
        <div class="popup_text_BC">
            <div class="popup_text_BC_I" onclick="hideAll()">取消</div>
            <div class="popup_text_BC_II" >保存</div>
        </div>
    </div>
</div>
<div style="clear: both"></div>

<!--===========================================新建=====================================================-->
<div id="insertDIV" class="popup_text_T" style="display:none;z-index:999999">
    <div class="popup_text_II"></div>
    <div class="popup_text_XJ">
        <div class="popup_text_I_friest">
            <span id="insertSpan" class="popup_text_II_friest">新建</span>
            <span class="popup_text_II_friestT" onclick="hideAll()">x</span>
        </div>
        <div class="popup_text_XJ_top">
            <input type="radio" id="insert_richeng" name="I" class="popup_top_I" value="2" checked />新建事项
            <input type="radio" id="insert_kecheng" name="I" class="popup_top_II" value="1"   />新建课程
        </div>




        <!--============================新建事项==================================-->
        <div>
            <div class="popup_text_XJ_top_V">
                标题<input id="titleInput" class="popup_text_XJ_top_V_I" type="text">
            </div>
            <div class="popup_text_XJ_top_VI" >
                内容<textarea id="contentArea"></textarea>
            </div>
        </div>

        <!--==========================新建课程==============================================-->
        <div class="popup_text_XJ_top_VII">
            开始时间<input id="bTime" class="popup_text_XJ_top_VII_I" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">&nbsp;&nbsp;&nbsp;&nbsp;
            结束时间<input id="eTime" class="popup_text_XJ_top_VII_II" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
        </div>
        <div class="popup_text_XJ_VIII">
            重复
            <select id="lpSelect" onchange="showLp()">
                <option value="-1">不重复</option>
                <option value="1">每天</option>
				<option value="2">每周</option>
				<option value="3">每月</option>
				<option value="4">每年</option>
            </select>
        </div>
        <!--==========================每年每月每周==============================================-->
        <div id="otherDIV" class="popup_text_XJ_YE"  style="display: none">
            <input id="dayInput1"  class="popup_text_XJ_YE_IIII" type="radio" name="year" value="0">一直持续
            <input id="dayInput2" class="popup_text_XJ_YE_II" type="radio" name="year" value="1" checked >发生<input class="popup_text_XJ_YE_I" id="otherCishu">次后结束
            <input id="dayInput3" class="popup_text_XJ_YE_III" type="radio" name="year" value="2">于
            <input  type="text" id="otherEndInput" style="border:1px solid #C3C3C3;width:120px;height:30px;" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
        </div>
        <!--================================每日===================================================-->
        <div id="dayDIV" style="display: none" >
            <div class="popup_text_XJ_DY" >
                <input id="dayRepeat1" class="popup_text_XJ_DY_III" type="radio" name="day" value="1" checked >每<input id="dayTypeInput" class="popup_text_XJ_DY_I">天重复一次
                <input id="dayRepeat2" class="popup_text_XJ_DY_II" type="radio" name="day" value="0">每个工作日
            </div>
            <div class="popup_text_XJ_JS" >结束</div>
            <div class="popup_text_XJ_DY_V"  >
                <input id="otherRepeat1"  class="popup_text_XJ_DY_V_I" type="radio" name="V" value="0" >一直持续
                <input id="otherRepeat2"  class="popup_text_XJ_DY_V_II" type="radio" name="V" value="1" checked >发生<input id="dayCishu"  class="popup_text_XJ_DY_V_III" type="text">次后
                <input id="otherRepeat3"  class="popup_text_XJ_DY_V_IIII" type="radio" name="V" value="2">于
                
				<input  type="text" id="dayEndInput" style="border:1px solid #C3C3C3;width:120px;height:30px;" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
            </div>
        </div>

        <div class="popup_text_XJ_bottom">
			<span id="deleteBtn" class="popup_text_bottom_I" onclick="showDelete()">删除</span>
            <span id="cancelBtn" class="popup_text_bottom_I" onclick="hideAll()">取消</span>
            <span class="popup_text_botton_II" onclick="insert()">保存</span>
        </div>
    </div>
</div>
<!--=====================================删除课程/日程=======================================-->
<div id="deleteDIV" class="popup_text_T" style="display:none;z-index:999999">
    <div id="popup_text_III"></div>

    <div id="popup_text_info_III" >
        <div class="popup_text_I_friest">
            <span id="real_d_text" class="popup_text_II_friest">删除</span>
        </div>
        <div class="popup_S_I">
                <span id="deleteSapn1">
                    <input id="singleDelete" class="popup_S_V_I" type="radio" name="VV" value="0">
                    <span class="popup_S_V_I_I">您确定要删除<span id="real_day1">2012-12-12</span><span class="popup_SS_II">语文课</span><span class="popup_S_V_I_II"></span><span>吗</span></span>
                </span>
				<br>
                <span id="deleteSapn2">
                    <input id="multiDelete" class="popup_S_V_II" type="radio" name="VV" value="1">
                     <span class="popup_S_V_II_I">您确定要删除<span id="real_day2">2012-12-12</span>当天及后面所有的<span class="popup_SS_II">语文课</span><span class="popup_S_V_I_II"></span><span>吗</span></span>
                </span>
        </div>
        <div class="popup_text_S_BC_I">
            <div class="popup_text_BC_I" onclick="delOper()">确定</div>
            <div class="popup_text_BC_II" onclick="hideAll()">取消</div>
        </div>
    </div>
</div>
</div>