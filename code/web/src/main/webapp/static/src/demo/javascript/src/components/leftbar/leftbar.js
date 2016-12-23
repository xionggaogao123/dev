var React = require('react');

var Avatar = require('../common/avatar');
var Cate = require('./cate');
var Operations = require('./operations');

var Data = require('../fulaan/Data');

module.exports = React.createClass({

    getInitialState: function () {
        var me = this;

        Demo.selectedCate = 'friends';
        return {
            user: '',
            avatar: ''
        };
    },

    componentDidMount: function () {

        Data.getMineInfo((resp) => {
            Demo.mine = resp;
            this.setState({
                user: resp.nickName,
                avatar: resp.imgUrl
            });
        });

        var strangerId = Demo.getParm('userId');
        var me = this;
        if (strangerId != undefined && strangerId != '' && strangerId != null) {
            Demo.strangerId = strangerId;
            Demo.selectedCate = 'strangers';
            me.props.update('stranger');
        }
    },

    shouldComponentUpdate: function (nextProps, nextState) {
        return nextProps.cur !== Demo.selectedCate;
    },

    updateFriend: function () {
        Demo.selectedCate = 'friends';
        this.props.update('friend');
    },

    updateGroup: function () {
        Demo.selectedCate = 'groups';
        this.props.update('group');
    },

    updateStranger: function () {
        Demo.selectedCate = 'strangers';
        this.props.update('stranger');
    },

    updateChatroom: function () {
        Demo.selectedCate = 'chatrooms';
        this.props.update('chatroom');
    },

    render: function () {
        return (
            <div className='webim-leftbar'>
                <Avatar className='webim-profile-avatar' title={Demo.user} src={this.state.avatar}/>
                <Cate name='friend' update={this.updateFriend} cur={this.props.cur}/>
                <Cate name='group' update={this.updateGroup} cur={this.props.cur}/>
                <Cate name='stranger' update={this.updateStranger} cur={this.props.cur}/>
                <Operations username={Demo.user}/>
            </div>
        );
    }
});
