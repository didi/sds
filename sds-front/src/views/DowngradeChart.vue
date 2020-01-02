<template>
  <div>
    <div class="demote-point-search b-wrapper">
      <el-form label-position="top" label-width="80px" size="medium">
        <el-col :span="4">
          <el-form-item label="应用组">
            <el-select v-model="form.applyGroup" placeholder="应用组">
              <p v-for="(item, index) in applyGroupList" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="应用名称">
            <el-select v-model="form.applyName" placeholder="应用名称">
              <p v-for="(item, index) in applyName" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="输入搜索">
            <el-input v-model="form.demotePoint" placeholder="输入降级点名称搜索" @keyup.enter.native="doSearch"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div class="search-btn-wrapper">
        <el-col>
          <el-button type="primary" size="medium" icon="el-icon-search" @click="doSearch">
            查 询
          </el-button>
        </el-col>
      </div>
    </div>
    <div class="demote-point-response-table b-wrapper">
      <el-table
        :data="queryResult.result"
        style="width: 100%">
        <el-table-column
          prop="appGroupName"
          label="应用组"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="appName"
          label="应用名称"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="point"
          label="降级点"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="returnValueStr"
          label="降级点返回值"
          :show-overflow-tooltip="true"
          min-width="100">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态">
          <template slot-scope="scope">
            <el-tag color="#fff"> {{scope.row.status === 1 ? '开启' : '关闭'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="operatorName"
          label="更新人">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>更新时间: {{ scope.row.modifiedTime }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag type="warning" color="#fff">{{scope.row.operatorName || '测试者'}}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="创建人">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>创建时间: {{ scope.row.createTime }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag color="#fff">{{scope.row.creatorName || '创建者'}}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="toMonitor(scope.row)">
              统计大盘
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="demote-point-response-pagination">
        <el-pagination
          background
          layout="prev, next"
          @current-change="currentChangeData"
          :total="queryResult.total"
          :page-size="queryResult.ps">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  import Api from "../api/api";

  export default {
    data() {
      console.log(this);
      return {
        form: {
          applyGroup: "",
          appName: "",
          demotePoint: ""
        },
        applyGroupList: ["hasdfasf", "dfasdfas"],
        applyName: ["dfsaf", "sdfs"],
        queryResult: {
          applyDemotePoint: ["afdasfd", "adfasdfa"],
          result: [{
            appGroupName: "我是组名",
            appName: "我是应用",
            point: "我是降级点",
            returnValueStr: "我是降级点返回值",
            status: 1,
            operatorName: "陈鹏志",
            creatorName: "陈鹏志",
            modifiedTime: Date(),
            createTime: Date()
          }],
          total: 100,
          ps: 20
        }
      }
    },
    created() {
      this.getAppGroupList();
    },
    watch: {
      'form.applyGroup': function (n, o) {
        console.log("now=", n, ",origin=", o);
        if (n !== o && n) {
          this.getAppList(n);
        }
      }
    },
    methods: {
      getAppGroupList() {
        Api.appgroupListall().then((res) => {
          if (res.code === 200) {
            this.appInfoList = [];
            this.applyGroupList = ['全部'].concat(res.data);
            this.form.appName = '';
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          console.error(err);
          this.$message({
            message: "查询应用失败",
            type: 'warning'
          })
        })
      },
      getAppList(appGroupName) {
        console.log("getAppList");
        Api.appinfoListall({
          appGroupName
        }).then((res) => {
          if (res.code === 200) {
            this.applyName = ['全部'].concat(res.data)
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          console.error(err);
          this.$message({
            message: "查询该应用组下的所有应用失败",
            type: 'warning'
          })
        })
      },
      doSearch() {
        console.log("doSearch:" + JSON.stringify(this.form))
      },
      currentChangeData() {
        console.log("currentChangeData:" + JSON.stringify(this.form))
      },
      toMonitor(demotePoint) {
        const location = "/#/monitor/" + encodeURI(demotePoint.appGroupName)
          + "/" + encodeURI(demotePoint.appName)
          + "/" + encodeURI(demotePoint.point);
        window.open(location, '_blank');
      }
    }
  }
</script>
