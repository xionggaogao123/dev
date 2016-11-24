/**
 *
 */
function loadFriends() {
    jQuery("#recipient").val("");
    jQuery("#messageSpan").empty();
    $.ajax({
        type: "GET",
        url: "/friendcircle/listAll.do",
        success: function (msg) {
            try {
                for (i = 0; i < msg.length; i++) {
                    var friendHtml = '<span style="font-family: Microsoft Yahei;font-size: 14px;line-height: 30px;cursor:pointer;display:block;text-align: center;" onclick="AddMsgReceiver(\'' + msg[i].userName + '\',\'' +
                        msg[i].id + '\',\'' + msg[i].imgUrl +
                        '\')" style="cursor: pointer; line-height:30px;" class="studentsList">' +
                        msg[i].userName + '</span>';
                    jQuery("#messageSpan").append(friendHtml);
                }
            } catch (x) {
            }
            ;
        }
    });

    var sd = $("#contactDiv");
    $("#contactDiv").show();
}

function hideFriendsDIV() {
    jQuery("#contactDiv").hide();
}

function SendPM() {
    var recipient = $.trim($('#recipient').val());
    var pm = $.trim($('#pm-content').val());

    if (recipient == '' || pm == '') {
        alert("请输入联系人和私信内容。");
        return;
    }
    MessageBox('发送中...', 0);
    $.ajax({
        url: "/letter/add.do",
        type: "post",
        data: {
            'recipient': recipient,
            'message': pm
        },
        success: function (data) {
            if (data.status == 'error') {
                MessageBox(data.errorMessage, -1);
            } else {
                closeDialog1("#sendPM");
                // $('#recipient').val(recipient);
                // $('#pm-content').val('');
                MessageBox(data.message, 1);
                GetPMList();
            }
        },
        error: function (e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
}

function actInvit() {
    $.ajax({
        url: "/activity/invite.do",
        type: "post",
        data: {
            'actId': jQuery("#act_Id").val(),
            'guestIds': jQuery("#guestIds").val(),
            'msg': jQuery("#pm-content").val()
        },
        success: function (data) {
            hideFriendsDIV();
        }
    });
}

function AddMsgReceiver(username, id, img) {
    var s = $.trim($('#recipient').val());
    s = s.replace(/^;+|;+$/gm, '');

    var reg = new RegExp('(^|;|\\s)' + username + '($|;|\\s)');
    if (reg.test(s)) {
        return;
    }
    if (s != '') s += ';';
    $('#recipient').val(s + username);

    if (jQuery("#guestIds").val().indexOf(id) < 0) {
        jQuery("#guestIds").val(jQuery("#guestIds").val() + "," + id);
        var inviteFriend = jQuery(".inviteFriend").length;
        if (inviteFriend > 0) {
            var html = '<dl id="dl_' + id + '"><dt><img width="36" height="36" alt="" src="' +
                img + '"></dt><dd>' + username +
                '</dd><a class="del_btn" href="javascript:deleteFriend(\'' + id +
                '\',\'' + username + '\');"></a></dl>';
            jQuery(".inviteFriend").prepend(html);
        }
    }
}


function deleteFriend(id, name) {
    jQuery("#dl_" + id).remove();
    var names = $('#recipient').val();
    $('#recipient').val(names.replace(name, ""));
    var ids = jQuery("#guestIds").val();
    jQuery("#guestIds").val(ids.replace(id, ""));
}
				
				