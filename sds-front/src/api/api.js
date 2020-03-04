import fetch from "../utils/fetch";
import store from "../store";

// 根据环境设置API地址
const apiHost = "";

// 合并公共参数
const _fetch = {
  paramFn: (url, params) => {
    let commonParams = {};
    if (url.indexOf("add") > -1 || url.indexOf("clone") > -1) {
      commonParams = {
        creatorName: store.getters["user/userInfo"].username_zh,
        creatorEmail: store.getters["user/userInfo"].email
      };
    } else {
      commonParams = {
        operatorName: store.getters["user/userInfo"].username_zh,
        operatorEmail: store.getters["user/userInfo"].email
      };
    }
    return Object.assign(commonParams, params);
  },
  get: (url, params) => {
    return fetch.get(url, _fetch.paramFn(url, params));
  },
  post: (url, params) => {
    return fetch.post(url, _fetch.paramFn(url, params));
  }
};

// 接口定义
export default {
  testGet: params => {
    return _fetch.post(apiHost + "/sds/appgroup/listpage", params);
  },
  appgroupAdd: params => {
    return _fetch.post(apiHost + "/sds/appgroup/add", params);
  },
  appgroupEdit: params => {
    return _fetch.post(apiHost + "/sds/appgroup/edit", params);
  },
  appgroupDelete: params => {
    return _fetch.post(apiHost + "/sds/appgroup/delete", params);
  },
  //分页查询应用组
  appgroupListpage: params => {
    return _fetch.post(apiHost + "/sds/appgroup/listpage", params);
  },
  //（下拉列表）查询所有应用组
  appgroupListall: params => {
    return _fetch.post(apiHost + "/sds/appgroup/listall", params);
  },
  //查询应用组下面的所有应用
  appinfoListall: params => {
    return _fetch.post(apiHost + "/sds/appinfo/listall", params);
  },
  //分页查询应用
  appinfoListpage: params => {
    return _fetch.post(apiHost + "/sds/appinfo/listpage", params);
  },
  //新增应用
  appinfoAdd: params => {
    return _fetch.post(apiHost + "/sds/appinfo/add", params);
  },
  //编辑应用
  appinfoEdit: params => {
    return _fetch.post(apiHost + "/sds/appinfo/edit", params);
  },
  //删除应用
  appinfoDelete: params => {
    return _fetch.post(apiHost + "/sds/appinfo/delete", params);
  },
  //查询所有降级预案（降级预案下拉列表使用）
  sdsschemeListall: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/listall", params);
  },
  //降级预案分页查询
  sdsschemeListpage: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/listpage", params);
  },
  //新增降级预案
  sdsschemeAdd: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/add", params);
  },
  //修改降级预案
  sdsschemeEdit: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/edit", params);
  },
  //克隆降级预案
  sdsschemeClone: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/clone", params);
  },
  //删除降级预案
  deleteStrategygroup: params => {
    return _fetch.post(apiHost + "/sds/sdsscheme/delete", params);
  },
  //新增降级点策略
  pointStrategyAdd: params => {
    return _fetch.post(apiHost + "/sds/pointstrategy/add", params);
  },
  //修改降级点策略
  pointStrategyEdit: params => {
    return _fetch.post(apiHost + "/sds/pointstrategy/edit", params);
  },
  //删除降级点策略
  pointStrategyDelete: params => {
    return _fetch.post(apiHost + "/sds/pointstrategy/delete", params);
  },
  //分页查询降级点策略
  pointStrategyListpage: params => {
    return _fetch.post(apiHost + "/sds/pointstrategy/listpage", params);
  },
  // 查询应用当前生效降级预案
  querysdsschemetips: params => {
    return _fetch.post(
      apiHost + "/sds/pointstrategy/querysdsschemetips ",
      params
    );
  },
  // 查询
  querypointreturnvalue: params => {
    return _fetch.post(apiHost + "/sds/pointreturnvalue/listpage", params);
  },
  // /sds/pointreturnvalue/add
  addpointreturnvalue: params => {
    return _fetch.post(apiHost + "/sds/pointreturnvalue/add", params);
  },
  // /sds/pointdict/querylist
  queryPointDictlist: params => {
    return _fetch.post(apiHost + "/sds/pointdict/querylist", params);
  },
  editpointreturnvalue: params => {
    return _fetch.post(apiHost + "/sds/pointreturnvalue/edit", params);
  },
  deletpointreturnvalue: params => {
    return _fetch.post(apiHost + "/sds/pointreturnvalue/delete", params);
  },

  // 监控和告警相关
  queryDashboardMain: params => {
    return _fetch.post(apiHost + '/sds/dashboard/main', params);
  },
  queryPoint: params => {
    return _fetch.post(apiHost + '/sds/dashboard/listpage', params);
  }
};
