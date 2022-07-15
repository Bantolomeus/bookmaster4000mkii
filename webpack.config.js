const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

module.exports = {
    entry: './client/src/index.js',
    output: {
        filename: "main.js",
        path: path.join(__dirname + '/src/main/resources/public'),
        publicPath: '/'
    },
    mode: process.env.NODE_ENV === "production" ? "production" : "development",
    module: {
        rules: [{
            test: /\.css$/,
            use: [{
                loader: 'style-loader'
            }, {
                loader: 'css-loader'
            }]
        }, { // required for graphics/fonts in css frameworks
            test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'application/font-woff'
                }
            }]
        }, { // required for graphics/fonts in css frameworks
            test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'application/octet-stream'
                }
            }]
        }, { // required for graphics/fonts in css frameworks
            test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'file-loader'
            }]
        }, { // required for graphics/fonts in css frameworks
            test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
            use: [{
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    mimetype: 'image/svg+xml'
                }
            }]
        }]
    },
    devtool: 'source-map',
    devServer: {
        port: 4002,
        proxy: {
            '/': 'http://localhost:8080'
        }
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: './index.html',
            templateContent: '<!DOCTYPE html><html><body> <div id="root"></div> </body></html>',
            meta: {viewport: 'width=device-width, initial-scale=1, shrink-to-fit=no', charset: {charset: "UTF-8"}},
            title: 'Bookmaster 4000 Mark II',
            inject: 'body',
        })
    ]
};

