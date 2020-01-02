export default {
  getEnv() {
    switch (location.hostname) {
      case "opensds.xiaojukeji.com":
        return "online";

      case "10.89.191.50":
        return "preonline";

      case "10.179.100.222":
        return "stable";

      default:
        return "local";
    }
  },
  getQueryString(url, name) {
    let lets = {};
    url.replace(/[?&]+([^=&]+)=([^&#]*)/gi, (m, key, value) => {
      lets[key] = value;
    });
    return name ? lets[name] : lets;
  },
  setCookie(key, value) {
    let exdate = new Date();
    exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000);
    //字符串拼接cookie
    window.document.cookie = key + "=" + value + ";path=/;expires=" + exdate.toGMTString();
  },
  getCookie(param) {
    let c_param = '';
    if (document.cookie.length > 0) {
      let arr = document.cookie.split('; ');
      for (let i = 0; i < arr.length; i++) {
        let arr2 = arr[i].split('=');
        //判断查找相对应的值
        if (arr2[0] == param) {
          c_param = arr2[1];
        }
      }
      return c_param;
    }
  },
  delCookie(name) {
    if (Array.isArray(name)) {
      for (let i = 0; i < name.length; i++) {
        const element = name[i];
        let exp = new Date();
        exp.setTime(exp.getTime() - 1);
        let cval = this.getCookie(element);
        cval != null ? (document.cookie = element + "=" + cval + ";expires=" + exp.toGMTString()) : false;
      }
    } else {
      let exp = new Date();
      exp.setTime(exp.getTime() - 1);
      let cval = this.getCookie(name);
      cval != null ? (document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString()) : false;
    }
  }
}