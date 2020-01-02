import utils from '../utils/utils';

// 根据环境设置SSO地址
const ssoApiHost = (() => {
  switch (utils.getEnv()) {
    case "online":
      return 'http://mis.diditaxi.com.cn/auth';

    default:
      return 'http://mis-test.diditaxi.com.cn/auth';
  }
})();
const ssoHost = (() => {
  switch (utils.getEnv()) {
    case "online":
      return 'https://star.xiaojukeji.com/data/ssologinweb.node';

    default:
      return 'http://page-daily.kuaidadi.com/data/ssologinweb.node';
  }
})();
const appId = (() => {
  switch (utils.getEnv()) {
    case "online":
      return 2260;

    case "preonline":
      return 2519;

    case "stable":
      return 2260;

    case "local":
      return 2518;
  }
})();

// 配置公有Ajax
const CommonAjax = (url, type, params) => {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: url,
      type: type,
      data: params,
      success: (data) => {
        resolve(data)
      },
      error: (err) => {
        reject(err)
      }
    })
  })
}

export default {
  checkCode: (params) => {
    return CommonAjax(ssoHost, 'POST', Object.assign({
      apiName: 'checkCode'
    }, params))
  },
  checkTicket: (params) => {
    return CommonAjax(ssoHost, 'POST', Object.assign({
      apiName: 'checkTicket'
    }, params))
  },
  getUserInfo: (params) => {
    return CommonAjax(ssoHost, 'POST', Object.assign({
      apiName: 'getUserInfo'
    }, params))
  },
  loginInitData: {
    ssoApiHost,
    ssoHost,
    appId
  }
}