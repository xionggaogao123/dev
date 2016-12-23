var React = require('react');
var ReactDOM = require('react-dom');

var componentsNode = document.getElementById('components');
var dom = document.createElement('div');
componentsNode.appendChild(dom);

var Avatar = require('../common/avatar');
var _ = require('lodash');
var Data = require('../fulaan/Data');

var Item = React.createClass({

  getInitialState: function() {
    return {
      isSelect: this.props.isSelect
    }
  },

  shouldComponentUpdate: function(){
    return true;
  },

  componentWillReceiveProps: function(nextProps) {
    console.log('componentWillReceiveProps' +this.state.isSelect );
  },

  select: function() {
    if(!this.state.isSelect){
      this.props.select(this.props.id);
    } else {
      this.props.unSelect(this.props.id);
    }
    this.setState({
      isSelect:!this.state.isSelect
    });
  },

  render: function() {
    console.log(this.state.isSelected);
    return (
      <div onClick={this.select} className={this.state.isSelect ? 'cm-container-selected' : 'cm-container'}>
        <div></div>
        <input type="radio" checked={this.state.isSelect ? "checked" : ''}/>
        <img src={this.props.avatar} />
        <h4>{this.props.name}</h4>
        <div></div>
      </div>
    );
  }
});

var MemberSheet = React.createClass({

  getInitialState: function() {
    return {
      members: [],
      selectItems:[],
      selectCount: 0
    };
  },

  getDefaultProps: function() {
    return {
      isOpen: false,
    };
  },

  componentWillReceiveProps: function(nextProps) {

  },

  componentDidMount: function() {

  },

  close: function() {
    typeof this.props.onClose === 'function' && this.props.onClose();
  },

  selected: function(id) {
    for(var i=0;i<this.state.selectItems.length;i++){
      if(this.state.selectItems[i] === id){
        return;
      }
    }
    this.state.selectItems.push(id);
    this.setState({
      selectCount: this.state.selectCount + 1
    });
  },

  unSelected: function(id) {
    var isSelect = false;
    for(var i=0;i<this.state.selectItems.length;i++){
      if(this.state.selectItems[i] === id){
        isSelect = true;
      }
    }

    if(isSelect){
      _.pull(this.state.selectItems,id);
      this.setState({
        selectCount: this.state.selectCount - 1
      });
    }

  },

  onSubmit: function(){
    var userIds = '';
    _.forEach(this.state.selectItems,(name,index) => {
      userIds += name + ',';
    });

    Data.createGroup(userIds,(resp) => {
      alert("创建成功");
    },(error) => {
      alert("创建失败");
    });
    typeof this.props.onClose === 'function' && this.props.onClose();
  },

  render: function() {

    var container = {
      height: '400px',
      backgroundColor: 'green',
      width: 'auto',
      visibility: 'hidden',
      overflowY: 'auto',
    };

    var content = {
      visibility:'visible'
    }

    var items = [];
    for(var i=0;i < Demo.friendsList.length;i++){
      var isSelected = false;
      for(var j=0;j < this.state.selectItems.length;j++){
        if(this.state.selectItems[j] === Demo.friendsList[i].id){
          isSelected = true;
        }
      }
      var name = Demo.friendsList[i].nickName ==='' ?Demo.friendsList[i].userName:Demo.friendsList[i].nickName;
      items.push(<Item id={Demo.friendsList[i].id} name={name} avatar={Demo.friendsList[i].avator}
        select={this.selected}
        isSelect={isSelected}
        unSelect={this.unSelected}/>);
      }
      return (
        <div className="webim-layer">
          <div className="webim-dialog">
            <h3>选择好友</h3>
            <div style={container}>
              <div style={content}>
                {items}
              </div>
            </div>
            <button onClick={this.onSubmit} id='cmBtn'>确定({this.state.selectCount})</button>
            <span className='font' onClick={this.close}>A</span>
          </div>
        </div>
      );
    }
  });

  module.exports = {

    show: function (choosed){
      ReactDOM.render(
        <MemberSheet onClose={this.close}
          onChoose={choosed}/>,
          dom
        );
      },

      close: function () {
        ReactDOM.unmountComponentAtNode(dom);
      }
    }
