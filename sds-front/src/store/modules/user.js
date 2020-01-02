import Login from '../../api/login'
import commonApi from '../../api/common'
import utils from '../../utils/utils'
// initial state
const state = {
  userInfo: {},
  ticket: ''
}
// getters
const getters = {
  userInfo: (state, getters) => {
    return state.userInfo
  },
  ticket: (state, getters) => {
    return state.ticket
  }
}

// actions
const actions = {
  login ({commit}) {
    let code = utils.getQueryString(location.search, 'code');
    code ? actions.checkCode({commit}, code) : (location.href = `${Login.loginInitData.ssoApiHost}/sso/login?app_id=${Login.loginInitData.appId}&jumpto=${commonApi.currentLocation}&version=${commonApi.appVersion}`);
  },
  logout ({commit}) {
    utils.delCookie(['userInfo', 'ticket'])
    commit('clearLoginInfo')
    location.href = `${Login.loginInitData.ssoApiHost}/ldap/logout?app_id=${Login.loginInitData.appId}&jumpto=${commonApi.currentLocation}`
  },
  checkCode({commit}, code) {
    commit('setTicket', code)
    Login.checkCode({code}).then(data => {
      if(data.code === 200) {
        actions.checkTicket({commit}, data.data.data.ticket)
      } else {
        // console.log('校验code失败', data.data)
      }
    })
  },
  checkTicket({commit}, ticket) {
    Login.checkTicket({ticket}).then(data => {
      if(data.code === 200) {
        actions.getUserInfo({commit}, ticket)
      } else {
        // console.log('校验ticket失败', data.data)
      }
    })
  },
  getUserInfo({commit}, ticket) {
    Login.getUserInfo({ticket}).then(data => {
      if(data.code === 200) {
        commit('setUser', data.data.data)
        location.search.indexOf('jumpto') > -1 ? location.href = location.origin + '/' : false;
      } else {
        // console.log('获取信息失败', data.data)
      }
    })
  }
}

// mutations
const mutations = {
  setUser (state, userInfo) {
    let info = JSON.parse(JSON.stringify(userInfo));
    info.username_zh = encodeURI(info.username_zh);
    utils.setCookie('userInfo', JSON.stringify(info))
    state.userInfo = userInfo
  },
  setTicket (state, ticket) {
    utils.setCookie('ticket', ticket)
    state.ticket = ticket
  },
  clearLoginInfo(state) {
    state.userInfo = {}
    state.ticket = ''
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
