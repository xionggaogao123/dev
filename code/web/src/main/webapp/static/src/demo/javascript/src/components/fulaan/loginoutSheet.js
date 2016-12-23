var React = require('react');
var ReactDOM = require('react-dom');

var componentsNode = document.getElementById('components');
var dom = document.createElement('div');
componentsNode.appendChild(dom);

var LoginOutSheet = React.createClass({

  close: function () {
    typeof this.props.onClose === 'function' && this.props.onClose();
  },

  onSubmit: function () {
    this.close();
  },

  render: function() {
    return (
      <div className="webim-layer">
        <div className="webim-dialog">
          <h3>确定要退出吗?</h3>
          <span className="lg-waing">退出可以直接关掉此页面</span>
          <div className="login-out-btn-div">
            <button onClick={this.onSubmit} className="login-out-btn">确定</button>
          </div>
          <span className='font' onClick={this.close}>A</span>
        </div>
      </div>
    );
  }
});

module.exports = {

  show: function () {
    ReactDOM.render(
      <LoginOutSheet onClose={this.close} />,
      dom
    );
  },

  close: function () {
    ReactDOM.unmountComponentAtNode(dom);
  }
}
