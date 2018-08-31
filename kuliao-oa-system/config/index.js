'use strict'
// Template version: 1.2.7

const path = require('path')

function loadModule(){
    var proxyTables={};

    //加载模块的代理设置
    let moudules = require('../static/module.json');
    moudules.forEach(function (moudule) {

        if (moudule.path){
            let mouduleProxyUrl =  require('../'+moudule.path+'/dev.js')
            mouduleProxyUrl.proxyTable.forEach(function (ProxyUrl) {
                ProxyUrl.url.forEach(function (url) {
                    let  target= 'http://'+ProxyUrl.ip+':'+ProxyUrl.port;
                    if(proxyTables[url]){
                        console.error(moudule.name+"模块的"+url+'重复设置');
                        console.error('使用代理'+target);
                    }
                    proxyTables[url]={
                        target:target,
                        changeOrigin:true
                    }
                });
            });
        }
    });
    return proxyTables;
}
module.exports = {
    dev: {

        // Paths
        assetsSubDirectory: 'static',
        assetsPublicPath: '/',
        proxyTable: loadModule(),//{
            // '/api':{
            //     target:'http://jsonplaceholder.typicode.com',
            //     changeOrigin:true,
            //     pathRewrite:{
            //         '/api':''
            //     }
            // },
            // '/ms':{
            //     target: 'https://www.easy-mock.com/mock/592501a391470c0ac1fab128',
            //     changeOrigin: true
            // }   本地直接访问/login接口即可 无需跨域
          /*  '/user/!*': {
                target: 'http://172.16.0.84:8081',
                changeOrigin: true
            },
            "/message/!*": {
                //target: 'http://172.20.0.41:8082',
                target: 'http://127.0.0.1:8082',
                changeOrigin: true
            }*/

      //  },

        // Various Dev Server settings
        host: 'localhost', // can be overwritten by process.env.HOST
        port: 8089, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
        autoOpenBrowser: false,
        errorOverlay: true,
        notifyOnErrors: true,
        poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


        /**
         * Source Maps
         */

        // https://webpack.js.org/configuration/devtool/#development
        devtool: 'eval-source-map',

        // If you have problems debugging vue-files in devtools,
        // set this to false - it *may* help
        // https://vue-loader.vuejs.org/en/options.html#cachebusting
        cacheBusting: true,

        // CSS Sourcemaps off by default because relative paths are "buggy"
        // with this option, according to the CSS-Loader README
        // (https://github.com/webpack/css-loader#sourcemaps)
        // In our experience, they generally work as expected,
        // just be aware of this issue when enabling this option.
        cssSourceMap: false,
    },

    build: {
        // Template for index.html
        index: path.resolve(__dirname, '../dist/index.html'),
        // Paths
        assetsRoot: path.resolve(__dirname, '../dist'),
        assetsSubDirectory: 'static',
        assetsPublicPath: './',

        /**
         * Source Maps
         */

        productionSourceMap: false,
        // https://webpack.js.org/configuration/devtool/#production
        devtool: '#source-map',

        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: false,
        productionGzipExtensions: ['js', 'css'],

        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report
    }
}
