/**
 * Created by yan on 2015/3/10.
 */


function initLaunchActivity(){
    $("#nope").focus(function(e) {
        var $selectValue=$("#nope").val();
        if($selectValue!=""){
            $(".autocompleter-list").addClass("autocompleter-list-border");
        }
        $(this).addClass("nope-border");

    }).blur(function(e) {
        $(".autocompleter-list").removeClass("autocompleter-list-border");
        $(this).removeClass("nope-border");
    });;

    $(".input").focus(function(e) {
        $(this).addClass("nope-border");
    }).blur(function(e) {
        $(this).removeClass("nope-border");
    });;

    //所有文本框获取失去焦点事件
    $("#num,#address,.contentArea_l textarea,.contentArea_l select").each(function(e) {
        var thisValue=$(this).val();
        if(thisValue!=""){
            $(this).val(thisValue);
        }else{
            $(this).val("");
        }
        $(this).focus(function(e) {
            $(this).addClass("nope-border").val("");
        }).blur(function(e) {
            var curValue = $(this).val();
            if(curValue!=""){
                $(this).val(curValue).removeClass("nope-border");
            }else{
                $(this).val(thisValue).removeClass("nope-border");
            }
        });;
    });
}


function addImage()
{
    var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false;
    if(ie){
        document.getElementById("file").click();
        document.getElementById("filename").value=document.getElementById("file").value;
    }else{
        var a=document.createEvent("MouseEvents");
        a.initEvent("click", true, true);
        document.getElementById("file").dispatchEvent(a);
    }
}

function ajaxFileUpload() {
    $.ajaxFileUpload
    (
        {
            url: '/activity/uploadPic.do',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'text',
            success: function (data, status)  //服务器成功响应处理函数
            {
                if(data!="500")
                {
                    var k=data.indexOf("<div");
                    if(k>0) data=data.substring(0,k);
                    jQuery("#add_image").hide();
                    jQuery("#upload_img,#replace_a").show();
                    jQuery("#upload_img").attr("src",data);
                }
                else
                {
                    alert("请正确上传活动图片");
                }
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                alert("服务器繁忙，请稍后再试");
            }
        }
    )
    return false;
}


function addActivity() {

    var name = jQuery("#nope").val();
    if (!name || (name.length > 10)) {
        alert("活动名称非空并且不超过10个字符");
        return;
    }

    var beginTimeStr;
    var endTimeStr;
    try {
        beginTimeStr = jQuery("#dt").val().replace(/-/g, "/") + ":00"
        endTimeStr = jQuery("#ivi").val().replace(/-/g, "/") + ":00"
        if(beginTimeStr=="开始时间：:00"){
            alert("请输入活动开始时间");
            return
        }
        if(endTimeStr=="结束时间：:00"){
            alert("请输入活动结束时间");
            return
        }

        var beginTime = new Date(beginTimeStr);
        var endTime = new Date(endTimeStr);

        if (isNaN(beginTime.getTime()) || isNaN(endTime.getTime())) {
            alert("请正确输入活动时间");
            return;
        }
        if (endTime.getTime() < beginTime.getTime()) {
            alert("您发起的活动'结束时间'早于'开始时间'");
            return;
        }
    } catch (x) {
        alert("活动时间选择有误！");
        return;
    }


    var address = jQuery("#address").val();
    if (!address) {
        alert("活动地址不能为空");
        return;
    }


    var def = jQuery("#defArea").val();
    if (!def || def.length > 300) {
        alert("活动说明非空并且不超过300字");
        return;
    }
    var visible = jQuery("#pref_noapply").val();
    var joinCount = jQuery("#num").val();
    if (!isInt(joinCount)) {
        alert("人数为整数，请正确输入");
        return;
    }

    var iamge = jQuery("#upload_img").attr("src");

    var act = {
        name: name,
        //eventStartDate: beginTimeStr.replace("/", "").replace("/", ""),
        eventStartDate: beginTimeStr,
        //eventEndDate: endTimeStr.replace("/", "").replace("/", ""),
        eventEndDate: endTimeStr,
        location: address,
        description: def,
        visible: visible,
        memberCount: joinCount,
        coverImage: iamge
    };
    var activityData = {act: act, guestIds: jQuery("#guestIds").val(), message: $.trim($('#pm-content').val())};

    $.ajax({
        url: '/activity/promote.do',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(activityData),
        success: function (result) {
            location.href = "/activity/view.do?actId=" + result.act.id;
        }
    });
}




    //example.js
    function toggler () {
        if (picker.destroyed) {
            picker.restore();
        } else {
            picker.destroy();
        }
        toggle.innerHTML = picker.destroyed ? 'Restore <code>rome</code> instance!' : 'Destroy <code>rome</code> instance!';
    }

    function dateKit(){
        var moment = rome.moment;
        rome(dt);
        rome(ivi);
       /* rome(ivp, { initialValue: '2014-12-08 08:36' });
        rome(sm, { weekStart: 1 });
        rome(d, { time: false });
        rome(t, { date: false });
        rome(mms, { monthsInCalendar: 2 });

        var picker = rome(ind);

        if (toggle.addEventListener) {
            toggle.addEventListener('click', toggler);
        } else if (toggle.attachEvent) {
            toggle.attachEvent('onclick', toggler);
        } else {
            toggle.onclick = toggler;
        }



        rome(mm, { min: '2013-12-30', max: '2014-10-01' });
        rome(mmt, { min: '2014-04-30 19:45', max: '2014-09-01 08:30' });

        rome(iwe, {
            dateValidator: function (d) {
                return moment(d).day() !== 6;
            }
        });

        rome(win, {
            dateValidator: function (d) {
                var m = moment(d);
                var y = m.year();
                var f = 'MM-DD';
                var start = moment('12-21', f).year(y).startOf('day');
                var end = moment('03-19', f).year(y).endOf('day');
                return m.isBefore(start) && m.isAfter(end);
            }
        });

        rome(tim, {
            timeValidator: function (d) {
                var m = moment(d);
                var start = m.clone().hour(12).minute(59).second(59);
                var end = m.clone().hour(18).minute(0).second(1);
                return m.isAfter(start) && m.isBefore(end);
            }
        });

        rome(inl).on('data', function (value) {
            inlv.innerText = inlv.textContent = value;
        });

        rome(left, {
            time: false,
            dateValidator: rome.val.beforeEq(right)
        });

        rome(right, {
            time: false,
            dateValidator: rome.val.afterEq(left)
        });

        rome(leftInline, {
            time: false,
            dateValidator: rome.val.beforeEq(rightInline)
        });

        rome(rightInline, {
            time: false,
            dateValidator: rome.val.afterEq(leftInline)
        });

        rome(exa, {
            dateValidator: rome.val.except('2014-08-01')
        });

        rome(exb, {
            dateValidator: rome.val.except('2014-08-02', '2014-08-06')
        });

        rome(exc, {
            dateValidator: rome.val.except(['2014-08-04', '2014-08-09'])
        });

        rome(exd, {
            dateValidator: rome.val.except([['2014-08-03', '2014-08-07'], '2014-08-15'])
        });

        rome(exe, {
            dateValidator: rome.val.only([
                ['2014-08-01', '2014-08-15'], '2014-08-22'
            ])
        });

        rome(exf, {
            dateValidator: rome.val.except([exb, exd, '2014-08-15'])
        });*/
}