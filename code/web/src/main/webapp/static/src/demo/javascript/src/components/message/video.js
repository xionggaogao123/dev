const React = require("react");
const ReactDOM = require('react-dom');
const Avatar = require('../common/avatar');


const VideoMsg = React.createClass({

    componentDidMount: function () {
        const me = this;

        const options = {url: me.props.value};

        options.onFileDownloadComplete = function (response) {
            me.refs.video.src = WebIM.utils.parseDownloadResponse.call(Demo.conn, response);
        };

        options.onFileDownloadError = function () {
        };

        options.headers = {
            'Accept': 'audio/mp4'
        };

        WebIM.utils.download.call(Demo.conn, options);

    },

    render: function () {
        const icon = this.props.className === 'left' ? 'H' : 'I';
        return (
            <div className={'rel pointer ' + this.props.className}>
                <Avatar src={this.props.src} className={this.props.className + ' small'}/>
                <p className={this.props.className}>{this.props.name} {this.props.time}</p>
                <div className="clearfix">
                    <div className='webim-msg-value'>
                        <span className='webim-msg-icon font'>{icon}</span>
                        <div className='webim-video-msg'>
                            <video id={this.props.id} ref='video' controls/>
                        </div>
                    </div>
                    <div className={"webim-msg-error " + (this.props.error ? ' ' : 'hide')}>
                        <span className='webim-file-icon font smaller red' title={this.props.errorText}>k</span>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = function (options, sentByMe) {
    const props = {
        src: options.avatar || 'demo/images/default.png',
        time: options.time || new Date().toLocaleString(),
        value: options.value || '',
        name: options.name,
        length: options.length || '',
        id: options.id,
        error: options.error,
        errorText: options.errorText
    };

    if(sentByMe === true) {
      props.src = Demo.mine.imgUrl
    } else {
      props.src = options.ext.avatar,
      props.name = options.ext.nickName
    }

    const node = document.createElement('div');
    node.className = 'webim-msg-container rel';
    options.wrapper.appendChild(node);

    Demo.api.scrollIntoView(node);

    return ReactDOM.render(
        <VideoMsg {...props} className={sentByMe ? 'right' : 'left'}/>,
        node
    );
};