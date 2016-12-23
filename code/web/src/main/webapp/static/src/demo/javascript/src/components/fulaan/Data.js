var axios = require('axios');

exports.getGroupInfo = function(emChatId) {
  if(emChatId !== undefined) {
    axios.get('/group/' + emChatId)
      .then(function (resp){
        //alert(JSON.stringify(resp));
      })
      .catch(function (error) {
        // alert(error.toString());
      });
  }
}

exports.getFriendList = function(success) {
  axios.get('/friend/getFriends')
    .then(function (resp) {
      success(resp);
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.searchFriend = function (userName,success) {
  axios.get('/friend/search?relax=' +userName)
    .then(function (resp) {
      success(resp);
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.addFriend = function (userId,success) {
  axios.get('/friend/apply?personId=' + userId + '&content=haha')
    .then(function (resp) {
      success(resp.data);
      console.log("申请好友");
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.getMineInfo = function (success) {
  axios.get('/user/info')
    .then(function (resp) {
      success(resp.data);
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.getEmGroups = function (emChatIds,success) {
  axios.get('/group/listEmchat?emChatIds=' + emChatIds)
    .then(function (resp) {
      success(resp.data);
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.getGroupMembers = function (emChatId,success) {
  axios.get('/group/members?emChatId=' + emChatId)
    .then(function (resp) {
      success(resp.data);
    })
    .catch(function (error) {
      // console.log(error.toString());
    });
}

exports.quitGroup = function (emChatId,success) {
  axios.get('/group/quit?emChatId=' + emChatId)
    .then(function (resp) {
      success(resp.data);
    })
    .catch(function (error) {
      // console.log(error.toString());
    })
}

exports.createGroup = function (userIds,success,fail) {
  axios.get('/group/create?userIds=' + userIds)
    .then(function (resp) {
      // console.log(resp);
      success(resp.data);
    })
    .catch(function (error) {
      fail(error);
    });
}

exports.addMemberToGroup = function (emChatId,userIds,success,fail) {
  axios.get('/group/inviteMember?emChatId=' + emChatId + '&userIds=' + userIds)
    .then(function (resp) {
      // console.log(resp);
      success(resp.data);
    })
    .catch((error) => {
      fail(error);
    });
}

exports.getOnList = function (emChatId,success,fail) {
  axios.get('/group/getOnJoin?emChatId=' + emChatId)
    .then(function (resp) {
      success(resp.data);
    })
    .catch((resp) => {
      fail(error);
    });
}

exports.deleteFriend = function (userIds,success,fail) {
  axios.get('/friend/delete?userIds=' + userIds)
    .then(function (resp) {
      success(resp.data);
    })
    .catch(function(error) {
      fail(error);
    });
}

exports.getUserInfo = function (userId,success) {
  axios.get('/user/userInfo?userId=' + userId)
  .then(function (resp) {
    success(resp.data);
  })
  .catch(function (error) {
    // console.log(error.toString());
  });
}

exports.getUserInfos = function (userIds,success) {

  alert(JSON.stringify(userIds) + 'getUserInfos');
  axios.get('/user/userInfos?userIds=' + userIds)
  .then(function (resp) {
    success(resp.data);
  })
  .catch(function (error) {
    // console.log(error.toString());
  });
}
