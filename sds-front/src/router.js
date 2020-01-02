import Vue from 'vue'
import Router from 'vue-router'
import ApplicationGroup from './views/ApplicationGroup.vue'
import ApplyName from './views/ApplyName.vue'
import StrategyGroup from './views/StrategyGroup.vue'
import DemotePoint from './views/DemotePoint.vue'
import DemotePointResponse from "./views/DemotePointResponse.vue";
import SettingUp from './views/SettingUp.vue'
import DowngradeChart from './views/DowngradeChart.vue'

Vue.use(Router);

export default new Router({
  // mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/about",
      name: "关于我们",
      component: () => import("./views/About.vue")
    },
    {
      path: "/applicationGroup",
      name: "应用组",
      component: ApplicationGroup
    },
    {
      path: "/applyName",
      name: "应用",
      component: ApplyName
    },
    {
      path: "/strategyGroup",
      name: "策略组",
      component: StrategyGroup
    },
    {
      path: "/demotePoint",
      name: "降级点策略",
      component: DemotePoint
    },
    {
      path: "/demotePointResponse",
      name: "降级返回值",
      component: DemotePointResponse
    },
    {
      path: "/settingUp",
      name: "设置",
      component: SettingUp
    },
    {
      path: "/downgradeChart",
      name: "监控大盘",
      component: DowngradeChart
    },
    {
      path: "/",
      name: "首页",
      redirect: "/applicationGroup"
    }
  ]
});
