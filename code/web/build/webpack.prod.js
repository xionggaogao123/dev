const webpack = require('webpack');

module.exports = {
    entry: {
        './src/main/webapp/static/src/sdk/dist/websdk-1.1.3': './src/main/webapp/static/src/sdk/src/connection',
        './src/main/webapp/static/src/demo/javascript/dist/demo': './src/main/webapp/static/src/demo/javascript/src/entry',
        './src/main/webapp/static/src/webrtc/dist/webrtc-1.0.0': './src/main/webapp/static/src/webrtc/src/webrtc',
    },
    output: {
        path: './',
        publicPath: './',
        filename: '[name].js'
    },
    // devtool: '#eval-cheap-module-source-map',
    resolve: {
        extensions: ['', '.js', '.jsx']
    },
    module: {
        loaders: [
            {
                test: /\.(js|jsx)$/,
                loader: 'babel',
                exclude: /node_modules/,
            },
            {
                test: /\.scss$/,
                loader: 'style!css!sass'
            },
            {
                test: /\.svg|woff|eot|ttf$/,
                loader: require.resolve('file-loader') + '?name=[path][name].[ext]'
            },
            {
                test:/\.css$/,
                loaders: ['style','css']
            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': '"production"'
            }
        }),
        new webpack.optimize.UglifyJsPlugin({
            compressor: {
                warnings: false
            }
        })
    ],
};
