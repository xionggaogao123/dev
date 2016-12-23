var React = require("react");
var Item = require('./contactItem');
var Data = require('../fulaan/Data');
var _ = require('lodash');

module.exports = React.createClass({

  getInitialState: function () {
    var me = this;

    return {
      src: 'demo/images/group_user.png',
      friends:[],
      groups:this.props.groupList
    };
  },

  update: function (id) {
    this.props.updateNode(id);
  },

  onscroll: function () {
    var scrollTop = this.refs.contactContainer.scrollTop;
    var scollTopNum = scrollTop / 60;
    if ((scrollTop / 60 + 10) == this.props[Demo.selectedCate].length) {
      this.props.getChatroom();
    }
  },

  componentDidMount: function() {

    Data.getFriendList((resp) => {
      Demo.friendsList = resp.data.message;
      this.setState({friends:resp.data.message});
    });

  },

  render: function () {
    var f = [],
    g = [],
    s = [];

    var me = this;
    _.forEach(this.state.friends,(friend,index) => {
      var name = friend.nickName == ''?friend.userName:friend.nickName;
      f.push(<Item id={friend.id} cate='friends' key={index} username={name}
      update={me.update} cur={me.props.curNode}
      avatar={friend.avator}/>);
    });

    _.forEach(this.props.groupList,(group,index) => {
      g.push(<Item id={group.emChatId} cate='groups' key={index}
      update={me.update} cur={me.props.curNode}
      src={me.state.src}
      avatar={group.headImage}
      username={group.name}
      name={group.name} />);
    });

    _.forEach(this.props.strangerList,(stranger,index) => {
      s.push(<Item id={stranger.userId} cate='strangers' key={index}
      username={stranger.userName} update={me.update} cur={me.props.curNode}
      avatar={stranger.avator}/>);
    });

    return (
      <div ref='contactContainer' className='webim-contact-wrapper' onScroll={this.onscroll}>
      <div className={this.props.cur === 'friend' ? '' : ' hide'}>{f}</div>
      <div className={this.props.cur === 'group' ? '' : ' hide'}>{g}</div>
      <div className={this.props.cur === 'stranger' ? '' : ' hide'}>{s}</div>
      </div>
    );
  }
});
