var React = require("react");
var ReactDOM = require('react-dom');
var SearchItem = require('../fulaan/searchItem');
var Data = require('../fulaan/Data');

var componentsNode = document.getElementById('components');
var dom = document.createElement('div');
componentsNode.appendChild(dom);

var UI = require('../common/webim-demo');
var Button = UI.Button;
var Input = UI.Input;

var AddMember = React.createClass({

    getInitialState: function () {

      return {
        searchResult: {},
        showSearchItem: false
      };
    },
    addMember: function () {

        var value = this.refs.input.refs.input.value;

        if (!value) {
            Demo.api.NotifyError(Demo.lan.username + Demo.lan.notEmpty);
            return;
        }
        if (value == Demo.user) {
            Demo.api.NotifyError(Demo.lan.addFriendSelfInvalid);
            this.close();
            return;
        }
        if (Demo.roster[value] == 1) {
            Demo.api.NotifyError(value + ' ' + Demo.lan.addFriendRepeat);
            this.close();
            return;
        }

        Data.addFriend(this.state.searchResult.id,(resp) => {

          if(resp.code == 200){
            alert('您的好友请求已发送');
            this.close();
          }
        });
        // if (WebIM.config.isWindowSDK) {
        //     WebIM.doQuery('{"type":"addFriend","to":"' + value + '","message":"' + Demo.user + Demo.lan.request + '"}',
        //         function success(str) {
        //             alert(Demo.lan.contact_added);
        //         },
        //         function failure(errCode, errMessage) {
        //             Demo.api.NotifyError('addMember:' + errCode);
        //         });
        // } else {
        //     Demo.conn.subscribe({
        //         to: value,
        //         message: Demo.user + Demo.lan.request
        //     });
        // }
        // this.close();
    },

    close: function () {
        typeof this.props.onClose === 'function' && this.props.onClose();
    },

    searchFriend: function () {
      var value = this.refs.input.refs.input.value;

      if (!value) {
          Demo.api.NotifyError(Demo.lan.username + Demo.lan.notEmpty);
          return;
      }
      if (value == Demo.user) {
          Demo.api.NotifyError(Demo.lan.addFriendSelfInvalid);
          this.close();
          return;
      }
      Data.searchFriend(value,(resp) => {
        this.setState({
          searchResult: resp.data.message,
          showSearchItem: true
        });
      });

    },

    render: function () {
        return (
            <div className='webim-friend-options'>
                <div ref='layer' className='webim-layer'></div>
                <div className='webim-dialog'>
                    <h3>{Demo.lan.addAFriend}</h3>
                    <div ref='content'>
                        <Input defaultFocus='true' ref='input' placeholder={Demo.lan.username}/>
                        <Button text='搜索' onClick={this.searchFriend} className='webim-dialog-button2' />
                    </div>
                    <SearchItem  name={this.state.searchResult.nickName} avatar={this.state.searchResult.avator} show={this.state.showSearchItem} />
                    <Button text={Demo.lan.add} onClick={this.addMember} className='webim-dialog-button'/>
                    <span className='font' onClick={this.close}>A</span>
                </div>
            </div>
        );
    }
});

module.exports = {
    show: function () {
        ReactDOM.render(
            <AddMember onClose={this.close}/>,
            dom
        );
    },

    close: function () {
        ReactDOM.unmountComponentAtNode(dom);
    }
};
