var React = require("react");
var AddFriend = require("./addFriend");
var CreateGroup = require("../group/createGroup");
var JoinPublicGroup = require("../group/joinPublicGroup");
var ShowBlacklist = require("../blacklist/showBlacklist");

var MemberSheet = require("../fulaan/chooseMember");

var LoginOutSheet = require('../fulaan/loginoutSheet');

module.exports = React.createClass({

    getInitialState: function () {
        var me = this;
        return {hide: 'hide'};
    },

    update: function () {
        this.setState({
            hide: this.state.hide ? '' : ' hide'
        });
    },

    addFriends: function () {
        AddFriend.show();
        this.update();
    },

    createGroup: function () {
        CreateGroup.show();
        this.update();
    },

    joinPublicGroup: function () {
        JoinPublicGroup.show();
        this.update();
    },

    showBlacklist: function () {
        ShowBlacklist.show();
        this.update();
    },

    logout: function () {
        LoginOutSheet.show();
        this.setState({
          hide: 'hide'
        });
    },

    inviteMember: function () {
      MemberSheet.show();
      this.update();
    },

    render: function () {
        var className = this.state.hide ? ' ' + this.state.hide : '';
        return (
            <div>
                <i className='webim-operations-icon font xsmaller' onClick={this.update}>M</i>
                <ul className={'webim-operations' + className}>
                    <li onClick={this.addFriends}>
                        <i className='font smallest'>L</i>
                        <span>{Demo.lan.addAFriend}</span>
                    </li>
                    <li onClick={this.inviteMember}>
                        <i className='font smallest'>L</i>
                        <span>创建群聊</span>
                    </li>
                    <li onClick={this.logout}>
                        <i className='font smallest'>Q</i>
                        <span>{Demo.lan.quit}({this.props.username})</span>
                    </li>
                </ul>
            </div>
        );
    }
});
