var React = require('react');

var chooseMember = require('./chooseMember');
var Data = require('./Data');
var _ = require('lodash');

var AddToGroup = require('./addToGroup');

var GroupMember = React.createClass({

  getInitialState: function () {
    return {
      members:[],
      show: this.props.show
    };
  },

  componentDidMount: function () {

  },

  shouldComponentUpdate: function (nextProps,nextState) {
    return true;
  },

  componentWillReceiveProps:function(nextProps) {
    this.setState({
      show:nextProps.show
    });
  },

  addMember: function() {
    var me = this;
    Data.getOnList(this.props.emChatId,(resp) => {
      me.props.onClose();
      me.setState({
        show: false
      });
      AddToGroup.show((data) => {
        var userIds = '';
        _.forEach(data,(id) => {
          userIds += id +',';
        });
        Data.addMemberToGroup(this.props.emChatId,userIds,(resp) => {
          if(resp.code == 200) {
            alert('添加成功');
          }
        });

      },resp.message);
    });
  },

  cancelMember: function() {
    alert('cancelMember');
  },

  render: function () {

    var memberItems = [];

    _.forEach(this.props.members,(member) => {
      memberItems.push(<div className="gm-container">
        <div>
          <img src={member.avator}/>
        </div>
        <div>
          <span>{member.nickName}</span>
        </div>
      </div>);
    });

    memberItems.push(<div className="gm-container" onClick={this.addMember}>
      <img src='/static/dist/webim/demo/images/plus.png'/>
    </div>);

    // memberItems.push(<div className="gm-container" onClick={this.cancelMember}>
    //   <img src='/static/dist/webim/demo/images/camel.png' />
    // </div>);

    return (
      <div className={this.state.show ? "webim-group-memeber" : "webim-group-memeber hide"}>
        {memberItems}
      </div>
    );
  }

});

module.exports = GroupMember;
