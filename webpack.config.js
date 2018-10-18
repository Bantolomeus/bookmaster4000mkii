const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

module.exports = {
    // This is the "main" file which should include all other modules
    entry: {app: './client/main.js'},
    // Where should the compiled file go?
    output: {
        path: path.join(__dirname + '/src/main/resources/public'),
        publicPath: '/',
        filename: 'bundle.js'
    },
    module: {
        rules: [{
            test: /\.html$/,
            loader: 'html-loader'
        }, { // will enable bootstrap.css to be bundled
            test: /\.css$/,
            use: [{
                loader: 'style-loader'
            }, {
                loader: 'css-loader'
            }]
        }, {
            test: /\.less$/,
            use: [{
                loader: 'style-loader' // creates style nodes from JS strings
            }, {
                loader: 'css-loader' // translates CSS into CommonJS: interprets @import and url() like import/require()
            }, {
                loader: 'less-loader' // compiles Less to CSS
            }]
        }, {
            test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'application/font-woff'
                }
            }]
        }, {
            test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'application/octet-stream'
                }
            }]
        }, {
            test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'file-loader'
            }]
        }, {
            test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'image/svg+xml'
                }
            }]
        }, {
            test: /\.js$/,
            exclude: /node_modules/,
            use: {
                loader: 'babel-loader',
                options: {
                    presets: ['babel-preset-es2015'],
                    cacheDirectory: true // see docs how to specify which directory for transpiled src
                }
            }
        }]
    },
    devtool: 'source-map',
    devServer: {
        port: 3000,
        proxy: {
            '/': 'http://localhost:8080'
        }
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './client/index.html',
            filename: './index.html'
        })
    ]
};

