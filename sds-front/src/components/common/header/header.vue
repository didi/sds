<template>
  <div class="header-box">
    <div class="header-controller">
      <ul class="hd-ct-left">
        <li @click="changeAside">
          <a class="iconfont adSys-outdent" href="javascript:;" v-if="iconRemember ==='left'"></a>
          <a class="iconfont adSys-indent" href="javascript:;" v-if="iconRemember ==='right'"></a>
        </li>
        <li>
          <a class="iconfont adSys-reload" href="javascript:;" @click="reloadPage()"></a>
        </li>
        <li>
          <a class="iconfont" href="javascript:;">
            <input class="search-input" type="text" placeholder="搜索...">
          </a>
        </li>
      </ul>
      <ul class="hd-ct-right">
        <li>
          <el-dropdown>
            <span class="el-dropdown-link">
              <span class="env-tag"> {{ env ? env : "测试环境" }}</span>
              <a href="javascript:;">
                <a class="iconfont adSys-user user-li" href="javascript:;"></a>
                <span class="user-name">{{ userInfo.username_zh || '登录' }}</span>
                <a class="iconfont adSys-down user-li" href="javascript:;"></a>
              </a>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>
                <template>
                  <div @click="logout">
                    退出登录
                  </div>
                </template>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </li>
      </ul>
    </div>
    <div class="bread-crumb">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{$route.name}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
  </div>
</template>
<style lang="scss" rel="stylesheet/scss">
  @import "./header.scss";
</style>
<script type="text/babel">
  import store from '../../../store'
  import utils from "../../../utils/utils";

  export default {
    props: {
      isCollapse: {
        type: Boolean,
        default() {
          return false;
        }
      }
    },
    data() {
      return {
        iconRemember: 'left'
      }
    },
    computed: {
      env() {
        switch (utils.getEnv()) {
          case "local":
            return "";
          case "stable":
            return "";
          case "preonline":
            return "";
          case "online":
            return ""
        }
      },
      userInfo() {
        return store.state.user.userInfo
      }
    },
    mounted() {
    },
    methods: {
      changeAside() {
        if (this.iconRemember === 'left') {
          this.iconRemember = 'right'
        } else {
          this.iconRemember = 'left'
        }
        eventHub.$emit('changeAside')
      },
      reloadPage() {
        this.reload();
      },
      logout() {
        store.dispatch('user/logout')
      }
    }
  }
</script>
