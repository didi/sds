<template>
  <div class="echartDemo">
    <div id="myChart"></div>
  </div>
</template>

<style lang="scss" rel="stylesheet/scss">
  @import "./echartDemo.scss";
</style>

<script type="text/babel">
  export default{
    data() {
      return {
      }
    },
    mounted() {
      const echarts = require('echarts');
      this.showData()
    },
    methods: {
      showData() {
        let myChart = this.$echarts.init(document.getElementById('myChart'));
        myChart.setOption({
            title: {
                text: '测试测试'
            },
            tooltip: {},
            xAxis: {
                data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20]
            }]
        });
      }
    }
  }
</script>
