const React = require("react");
const Chat = require('./chat/chat');
const Data = require('./fulaan/Data');

module.exports = React.createClass({
    getInitialState: function () {
        return {
            chat: true,
            loadingStatus: false,
            loadingMsg: '正在登录'
        };
    },

    componentDidMount: function () {
        Data.getMineInfo((resp) => {
            Demo.conn.open({
                apiUrl: 'http://a1.easemob.com',
                user: resp.id,
                pwd: '123456',
                appKey: 'fulan#fulanmall'
            });
        });
    },

    update: function (state) {
        this.setState({
            chat: state.chat,
            loadingStatus: state.loadingStatus,
            loadingMsg: state.loadingMsg,
            content: state.content,
            status: state.status
        });
    },

    loading: function (status, msg) {
        msg = msg || Demo.lan.loading;
        this.setState({loadingStatus: status, loadingMsg: msg});
    },

    render: function () {
        const props = {};
        props.rosterChange = this.props.rosterChange;
        props.groupChange = this.props.groupChange;
        props.chatroomChange = this.props.chatroomChange;
        return (
            <div>
                <div className={'webim' + (WebIM.config.isWindowSDK ? ' webim_isWindowSDK' : '')}>
                    <Chat show={this.state.chat} {...this.props} update={this.update}
                          loading={this.loading} {...props} />
                </div>
            </div>
        );
    }
});
