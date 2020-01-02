<template>
  <div id="app">
    <el-container>
      <el-aside :width="'auto'" class="aside-wrapper">
        <Aside :isCollapse="isCollapse"></Aside>
      </el-aside>
      <el-container>
        <el-header :style="'left:' + headerWidth">
          <LayoutHeader></LayoutHeader>
        </el-header>
        <el-main class="main-wrapper" :style="'margin-left:' + mainWidth">
          <LayoutContent>
            <div slot="content">
              <router-view />
            </div>
          </LayoutContent>
        </el-main>
        <!-- <el-footer class="footer-wrapper" :style="'margin-left:' + footerWidth">
          <LayoutFooter></LayoutFooter>
        </el-footer> -->
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss">
  @import "./assets/css/global.css";

  body {
    margin: 0 0px;
    .el-main {
      padding: 15px;
    }
    .main-wrapper {
      margin-top: 100px;
      margin-left: 220px;
      transition: all .3s ease-in-out;
      .main {
        // padding: 30px 20px;
        // background-color: #ffffff;
        // border-radius: 2px;
        // box-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
      }
    }
    .footer-wrapper {
      padding: 0 15px;
      margin-left: 220px;
      transition: all .3s ease-in-out;
      .footer {
        padding: 15px;
        background-color: #ffffff;
        border-radius: 2px;
        box-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
      }
    }
  }
  a {
    text-decoration: none;
  }
</style>
<script>
    import LayoutHeader from '@/components/common/header/header';
    import LayoutFooter from '@/components/common/footer/footer';
    import Aside from '@/components/common/aside/aside';
    import LayoutContent from '@/components/common/content/content';

    export default{
    components: {
      LayoutHeader,
      LayoutFooter,
      Aside,
      LayoutContent
    },
    data() {
      return {
        isCollapse: false,
        headerWidth: '220px',
        mainWidth: '220px',
        footerWidth: '220px'
      }
    },
    watch: {
      "isCollapse": function(newVal, oldVal) {
        if(newVal) {
          this.headerWidth = '64px;'
          this.mainWidth = '64px;'
          this.footerWidth = '64px;'
        } else {
          this.headerWidth = '220px;'
          this.mainWidth = '220px;'
          this.footerWidth = '220px;'
        }
      }
    },
    mounted() {
      this.emitEvent()
    },
    methods: {
      emitEvent() {
        eventHub.$on('changeAside', () => {
          this.isCollapse ? this.isCollapse = false : this.isCollapse = true
        })
      }
    }
  }
</script>
