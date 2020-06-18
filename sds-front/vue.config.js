const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "target/static"),
  productionSourceMap: false,
  devServer: {
    port: 8080,
    host: "localhost",
    https: false,
    open: true,
    proxy: {
      "/": {
        target: "http://localhost:8887", //设置调用的接口域名和端口
        changeOrigin: false, //是否跨域
        ws: false,
      }
    }
  },
  configureWebpack: {
    // devtool: "eval-source-map", // https://github.com/vuejs/vue-cli/issues/4572
  }
};