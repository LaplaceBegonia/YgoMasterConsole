const webpack = require('webpack')
module.exports = {
    publicPath: './',
    lintOnSave: true,
    configureWebpack: {
        //关闭 webpack 的性能提示
        performance: {
            hints:false
        }
    },
    devServer: {
        host: 'localhost',//本地地址
        port: '8081',//端口号
        hot: true,//热更新自动刷新
        open: true,//自动打开
        overlay: {  //当出现编译错误或警告时，在浏览器中显示全屏覆盖。只显示错误信息不提示警告情况限制是
          warning: false,
          error: true
        },
        // proxy: {
        //   "/api": {
        //     target: '',//代理地址 凡是使用/api
        //     changeOrigin: true,//允许跨域请求
        //     secure: false,
        //     pathRewrite: { //重写路径 替换请求地址中的指定路径
        //       ['^/api']: '/api' //将请求地址中的api替换
        //     }
        //   }
        // }
      }
}
