var React = require("react");


module.exports = React.createClass({

    getInitialState: function () {
        return null;
    },

    render: function () {
        var className = this.props.className ? ' ' + this.props.className : '';
        return (
            <div className={'webim-avatar-icon' + className}>
                <img className='w100' src={this.props.src} title={this.props.title} />
            </div>
        );
    }
});
