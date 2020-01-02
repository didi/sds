<template>
  <div class="main">
    <div class="page-container page-component">
      <slot name="content"></slot>
    </div>
  </div>
</template>
<style lang="scss" rel="stylesheet/scss">
  @import "content.scss";
</style>
<script type="text/babel">
  export default{
    name:'v-content',
    props: {
      isCollapse: {
        type: Boolean,
        default() {
          return false;
        }
      }
    },
    data(){
      return {

      }
    },
    components: {},
    mounted() {

    }
  }
</script>
