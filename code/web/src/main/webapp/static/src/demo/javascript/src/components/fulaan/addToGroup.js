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

var AddToGroup = React.createClass({

  getInitialState: function() {
    return {
      members: this.props.members,
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
    var isSelected = false;
    _.forEach(this.state.selectItems,(item) => {
      if(item === id) {
        isSelected = true;
        return false;
    }});
    if(!isSelected){
      this.state.selectItems.push(id);
      this.setState({
        selectCount: this.state.selectCount + 1
      });
    }
  },

  unSelected: function(id) {
    _.forEach(this.state.selectItems,(item) => {
      if(item === id){
        _.pull(this.state.selectItems,id);
        this.setState({
          selectCount: this.state.selectCount - 1
        });
        return false;
      }
    });
  },

  onSubmit: function(){
    this.props.onChoose(this.state.selectItems);
    typeof this.props.onClose === 'function' && this.props.onClose();
  },

  render: function() {
    var items = [];

    _.forEach(this.state.members,(member) => {
      var isSelected = false;
      _.forEach(this.state.selectItems,(item) => {
        if(item === member.id){
          isSelected = true;
          return false;
        }
      });
      var name = member.nickName === '' ? member.userName:member.nickName;
      items.push(<Item id={member.id} name={name} avatar={member.avator}
        select={this.selected}
        isSelect={isSelected}
        unSelect={this.unSelected}/>);
    });
    return (
      <div className="webim-layer">
        <div className="webim-dialog">
          <h3>选择好友</h3>
          <div className="go-container">
            <div className="go-content">
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

  show: function (choosed,members){
    ReactDOM.render(
        <AddToGroup onClose={this.close}
        onChoose={choosed}
        members={members}/>,
        dom
    );
  },

  close: function () {
      ReactDOM.unmountComponentAtNode(dom);
  }
}
