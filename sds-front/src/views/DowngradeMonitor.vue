<template>
  <div>
    <div class="b-wrapper">
      <span>应用组: {{ meta.appGroupName }}    </span>
      <span>应用名称: {{ meta.appName }}    </span>
      <span>降级点名称: {{ meta.point }}    </span>
    </div>
    <el-row>
      <v-chart :options="all"/>
      <v-chart :options="visit"/>
      <v-chart :options="concurrent"/>
      <v-chart :options="exception"/>
      <v-chart :options="timeout"/>
      <v-chart :options="downgrade"/>
    </el-row>
  </div>
</template>

<script>
  import MonitorChart from "../components/home/monitor/MonitorChart";
  import ECharts from 'vue-echarts'
  import 'echarts/lib/chart/line'
  import timeFormat from "../utils/timeFormat";
  import Api from "../api/api";

  export default {
    components: {
      'v-chart': ECharts,
      MonitorChart,
    },
    data() {
      console.log("data");
      const initData = this.getInitData(['访问量统计', '并发统计', '异常统计', '超时统计', '降级统计']);
      const metaData = this.monitorMeta();
      return {
        meta: {
          ...metaData,
        },
        monitorData: {
          visitList: [],
          concurrentList: [],
          exceptionList: [],
          timeoutList: [],
          downgradeList: [],
        },
        all: this.renderChart(initData, "应用统计"),
        visit: this.renderChart([initData[0]]),
        concurrent: this.renderChart([initData[1]]),
        exception: this.renderChart([initData[2]]),
        timeout: this.renderChart([initData[3]]),
        downgrade: this.renderChart([initData[4]]),
      }
    },
    mounted() {
      console.log("mounted");
      document.title = this.meta.appName + "-" + this.meta.point;

      this.loadData();
    },
    computed: {},
    methods: {
      // 从url获取
      monitorMeta() {
        const element = window.location.hash.substr('#/monitor/'.length).split('/');
        console.log(element);
        const appGroupName = decodeURI(element[0]);
        const appName = decodeURI(element[1]);
        const point = decodeURI(element[2]);
        return {
          appGroupName,
          appName,
          point
        }
      },
      // 计算初始化X轴时间戳
      getInitData(titles) {
        const result = [];
        titles.forEach(title => {
          const data = [];
          const current = new Date().getTime();
          for (let i = -24 * 60; i <= 0; i++) {
            data.push({
              timestamp: timeFormat.utcTimeFormat(current + i * 60 * 1000),
              value: 0
            })
          }
          result.push({
            title,
            data,
          });
        });
        return result;
      },
      // 载入数据
      loadData() {
        const current = new Date().getTime();
        const params = {
          appGroupName: this.meta.appGroupName,
          appName: this.meta.appName,
          point: this.meta.point,
          startTime: current - 24 * 60 * 60 * 1000,
          endTime: current
        };
        console.log(params);
        Api.queryDashboardMain(params).then((res) => {
          if (res.code === 200) {
            this.monitorData.visitList = res.data.visitList.map(item => {
              return {
                timestamp: timeFormat.utcTimeFormat(item.timestamp),
                value: item.value
              }
            });
            this.monitorData.concurrentList = res.data.concurrentList.map(item => {
              return {
                timestamp: timeFormat.utcTimeFormat(item.timestamp),
                value: item.value
              }
            });
            this.monitorData.exceptionList = res.data.exceptionList.map(item => {
              return {
                timestamp: timeFormat.utcTimeFormat(item.timestamp),
                value: item.value
              }
            });
            this.monitorData.timeoutList = res.data.timeoutList.map(item => {
              return {
                timestamp: timeFormat.utcTimeFormat(item.timestamp),
                value: item.value
              }
            });
            this.monitorData.downgradeList = res.data.downgradeList.map(item => {
              return {
                timestamp: timeFormat.utcTimeFormat(item.timestamp),
                value: item.value
              }
            });
            this.refreshChart();

          } else {
            console.error("请求失败", JSON.stringify(res))
          }
        }).catch((err) => {
          console.error(err);
        })
      },
      refreshChart() {
        this.all = this.renderChart([{
          title: '访问量统计',
          data: this.monitorData.visitList
        }, {
          title: '并发统计',
          data: this.monitorData.concurrentList
        }, {
          title: '异常统计',
          data: this.monitorData.exceptionList
        }, {
          title: '超时统计',
          data: this.monitorData.timeoutList
        }, {
          title: '降级统计',
          data: this.monitorData.downgradeList
        }]);
        this.visit = this.renderChart([{
          title: '访问量统计',
          data: this.monitorData.visitList
        }]);
        this.concurrent = this.renderChart([{
          title: '并发统计',
          data: this.monitorData.concurrentList
        }]);
        this.exception = this.renderChart([{
          title: '异常统计',
          data: this.monitorData.exceptionList
        }]);
        this.timeout = this.renderChart([{
          title: '超时统计',
          data: this.monitorData.timeoutList
        }]);
        this.downgrade = this.renderChart([{
          title: '降级统计',
          data: this.monitorData.downgradeList
        }]);
      },
      // 创建绘制图表数据
      renderChart(dataList, title) {
        const lineCommon = {
          title: {
            text: title ? title : dataList[0].title
          },
          tooltip: {
            trigger: 'axis'
          },
          yAxis: {
            splitLine: {
              show: false
            }
          },
          legend: {
            data: dataList.map(item => item.title)
          },
          markLine: {
            silent: true,
            data:
              [{
                yAxis: 50
              }, {
                yAxis: 100
              }, {
                yAxis: 150
              }, {
                yAxis: 200
              }, {
                yAxis: 300
              }]
          }
        };
        const seriesCommon = {
          type: 'line',
        };
        const offset = parseInt(dataList[0].data.length * 11 / 12 + '', 10);
        return {
          ...lineCommon,
          xAxis: {
            data: dataList[0].data.map(function (item) {
              return item.timestamp;
            })
          },
          dataZoom: [{
            startValue: dataList[0].data[offset].timestamp
          }, {
            type: 'inside'
          }],
          series: dataList.map(function (item) {
            return {
              ...seriesCommon,
              name: item.title,
              data: item.data.map(it => it.value),
            }
          })
        }
      },
      // 关闭浏览器页面
      close() {
        window.close()
      }
    }
  }
</script>
