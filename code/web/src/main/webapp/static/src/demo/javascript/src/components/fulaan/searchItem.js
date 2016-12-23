var React = require('react');
var Avatar = require('../common/avatar');

var SearchItem = React.createClass({

  getInitialState: function () {

    return {
      name: this.props.name,
      avatar: this.props.avatar
    };
  },

  componentDidMount: function () {

  },

  shouldComponentUpdate: function (nextProps,nextState) {
    this.setState({
      name: nextProps.name,
      avatar: nextProps.avatar
    })
    return true;
  },

  render: function () {
    return (
      <div className={this.props.show ? 'search-friend-item' : 'search-friend-item hide'}>
        <img className="search-friend-avatar" src={this.props.avatar}/>
        <span className="search-friend-info">{this.props.name}</span>
      </div>
    );
  }
});

module.exports = SearchItem;
