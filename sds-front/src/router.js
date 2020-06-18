import Vue from 'vue'
import Router from 'vue-router'

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
      component: () => import("./views/ApplicationGroup.vue")
    },
    {
      path: "/applyName",
      name: "应用",
      component: () => import("./views/ApplyName.vue")
    },
    {
      path: "/sdsScheme",
      name: "降级预案",
      component: () => import("./views/SdsScheme.vue")
    },
    {
      path: "/demotePoint",
      name: "降级点策略",
      component: () => import("./views/DemotePoint.vue")
    },
    {
      path: "/demotePointResponse",
      name: "降级返回值",
      component: () => import("./views/DemotePointResponse.vue")
    },
    {
      path: "/settingUp",
      name: "设置",
      component: () => import("./views/SettingUp.vue")

    },
    {
      path: "/downgradeChart",
      name: "监控大盘",
      component: () => import("./views/DowngradeChart.vue")
    },
    {
      path: "/",
      name: "首页",
      redirect: "/applicationGroup"
    }
  ]
});
