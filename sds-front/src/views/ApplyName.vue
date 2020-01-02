<template>
  <div class="apply-name">
    <div class="apply-name-search b-wrapper">
     <el-form :inline="true">
       <el-form-item label="应用组">
         <el-select v-model="form.applyGroup" placeholder="">
           <el-option label="全部" value=""></el-option>
           <p v-for="(item, index) in applyGroupData" :key="index">
             <el-option :label="item" :value="item"></el-option>
           </p>
         </el-select>
       </el-form-item>
       <el-form-item label="应用名称">
         <el-input v-model="form.applyName" placeholder="应用名称" @keyup.enter.native="doSearch"></el-input>
       </el-form-item>
       <!-- <el-form-item>
         <el-button type="primary" @click="addApplicationGroup">新增应用组</el-button>
       </el-form-item> -->
       <div class="top-btn-right">
         <el-form-item>
           <el-button type="primary" @click="doSearch" icon="el-icon-search">查 找</el-button>
         </el-form-item>
         <el-form-item>
           <el-button type="primary" @click="addApply" icon="el-icon-plus">新增应用</el-button>
         </el-form-item>
       </div>
     </el-form>
    </div>
    <div class="application-group-table b-wrapper">
      <el-table
        :data="tableData.data"
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
          prop="strategyGroupName"
          label="当前策略组"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="version"
          label="版本">
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
        <el-table-column prop="modifiedTime" label="更新时间" min-width="100" />
        <el-table-column prop="createTime" label="创建时间" min-width="100" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
       <div class="application-group-pagination">
        <el-pagination
          background
          layout="prev, next"
          @current-change="currentChangeData"
          :total="tableData.total"
          :page-size="tableData.ps"
          >
        </el-pagination>
      </div>
    </div>

    <el-dialog title="新增应用" :visible.sync="dialogAddApply">
      <el-form :inline="true" class="add-apply" label-position="right" label-width="120px">
        <p class="of-application-group">
          <el-form-item label="所属应用组">
            <el-select v-model="addApplyGroup">
              <p v-for="(item, index) in applyGroupData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="应 用 名 称">
            <el-input v-model="newAppName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：只能使用数字、字母、-、_" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <!-- <p class="of-application-group">
          <el-form-item label="所用策略组">
            <el-select v-model="addStrategyGroup">
              <p v-for="(item, index) in addStrategyGroupData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p> -->
        <p class="add-application-group-btn dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closeDialogAddApply">取消</el-button>
            <el-button type="primary" @click="doSubmitAddApply">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
    <el-dialog title="修改应用" :visible.sync="dialogEditApply">
      <el-form :inline="true" class="edit-apply" label-position="right" label-width="120px">
        <p class="of-application-group">
          <el-form-item label="所属应用组">
            <el-select v-model="editRowData.appGroupName" placeholder="" disabled>
              <p v-for="(item, index) in applyGroupData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p>
        <p class="edit-apply-name">
          <el-form-item label="应 用 名 称">
            <el-input v-model="editRowData.appName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：只能使用数字、字母、-、_" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="of-application-group">
          <el-form-item label="所用策略组">
            <el-select v-model="editRowData.strategyGroupName" placeholder="">
              <p v-for="(item, index) in strategyGroupData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p>
        <p class="add-application-group-btn dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closeDialogEditApply">取消</el-button>
            <el-button type="primary" @click="doSubmitEditApply">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
  </div>
</template>

<style lang="scss">
  .apply-name {
    .top-line {
      height: 1px;
      background: #f6f6f6;
      margin: 0 -20px;
    }
    .application-group-table {
      margin-top: 20px
    }
    .application-group-pagination {
      text-align: center;
      margin-top: 20px;
    }
    .el-pagination.is-background .el-pager li:not(.disabled).active {
      border: none;
    }
    .el-table--enable-row-transition .el-table__body td {
      text-align: center;
    }
    .el-table tr th {
      text-align: center;
    }
    .apply-name-search .el-button {
      padding: 10px 14px;
    }
    .apply-name-search .top-btn-right {
      float: right;
    }
    .apply-name-search .el-form-item {
      margin-bottom: 0;
    }
    .el-dialog__header {
      padding: 20px;
      border-bottom: 1px solid #dcdfe6;
    }
    .dialog-footer {
      padding-left: 80px;
    }
  }
</style>

<script>
    /* eslint-disable */
    import Api from '../api/api'
    import TimeFormat from '../utils/timeFormat'

    export default {
    data () {
      return {
        addApplyGroupData: [],
        applyGroupData: [],
        addStrategyGroup: '',
        addApplyGroup: '',
        newAppName: '',
        addStrategyGroupData: [],
        oldAppName: '',
        strategyGroupData: [],
        editStrategyGroup: '',
        editRowData: {},
        dialogEditApply: false,
        dialogAddApply: false,
        applicationGroup: "",
        tableData: {
          data: [],
          ps: 8,
          pn: 1,
          total: null
        },
        form: {
          applyName: '',
          applyGroup: '',
          strategyGroup: ''
        }
      }
    },
    watch: {
      'form.applyName' (newVal) {
        let params = {
          appGroupName: this.form.applyName,
          appName: newVal
        }
        Api.strategygroupListall(params).then((res) => {
          if (res.code === 200) {
            this.addStrategyGroupData = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "分页查询应用失败",
            type: 'warning'
          })
        })
      }
    },
    created () {
      this.appgroupListall()
      this.currentChangeData()
    },
    methods: {
      //下一页前一页查询
      currentChangeData(page) {
        this.applyListpage(page).then((res) => {
          if(res.code === 200) {
            res.data.forEach(function(item){
              item.createTime = TimeFormat.timeFormat(item.createTime)
              item.modifiedTime = TimeFormat.timeFormat(item.modifiedTime)
            })
            this.tableData.data = res.data
            if (this.tableData.data.length < this.tableData.ps) {
              this.tableData.total = page * this.tableData.ps
            }
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "分页查询应用失败",
            type: 'warning'
          })
        })
      },
      //修改应用
      doSubmitEditApply () {
        let params = {
          appGroupName: this.editRowData.appGroupName,
          appName: this.oldAppName,
          newAppName: this.editRowData.appName,
          newStrategyGroupName: this.editRowData.strategyGroupName,
          operatorId: this.editRowData.operatorId
        }
        Api.appinfoEdit(params).then((res) => {
          if(res.code === 200) {
            this.$message({
              message: "修改应用成功",
              type: 'success'
            })
            this.dialogEditApply = false
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          console.log('err', err)
          this.$message({
            message: "编辑应用失败",
            type: 'warning'
          })
        })
      },
      closeDialogEditApply () {
        this.dialogEditApply = false
      },
      //所有应用组
      appgroupListall () {
        let params = {
        }
        Api.appgroupListall(params).then((res) => {
          if(res.code === 200) {
            this.applyGroupData = res.data
            // this.doSearchCreated(this.applyGroupData[0] || '')
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "查询应用失败",
            type: 'warning'
          })
        })
      },
      //增加应用
      doSubmitAddApply() {
        let params = {
          appGroupName: this.addApplyGroup,
          appName: this.newAppName
        }
        Api.appinfoAdd(params).then((res) => {
          if(res.code === 200) {
            this.$message({
              message: "新增应用成功",
              type: 'success'
            })
            this.closeDialogAddApply()
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "新增应用失败",
            type: 'warning'
          })
        })
      },
      closeDialogAddApply() {
        this.dialogAddApply = false
      },
      addApply() {
        this.dialogAddApply = true
      },
      handleEdit(row) {
        this.dialogEditApply = true
        this.editRowData = row
        this.oldAppName = row.appName || ''
        let params = {
          appGroupName: this.editRowData.appGroupName,
          appName: this.editRowData.appName
        }
        Api.strategygroupListall(params).then((res) => {
          if (res.code === 200) {
            console.log('strategygroupListall', res.data)
            this.strategyGroupData = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "分页查询应用失败",
            type: 'warning'
          })
        })
      },
      //删除应用
      handleDelete(rowData) {
        let params = {
          appGroupName: rowData.appGroupName,
          appName: rowData.appName,
          operatorId: rowData.operatorId
        }
        this.$confirm('确定删除该应用吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          Api.appinfoDelete(params).then((res) => {
            if(res.code === 200) {
              this.$message({
                message: "删除应用成功",
                type: 'success'
              })
              this.appgroupListall()
            } else {
              this.$message({
                message: res.msg,
                type: 'warning'
              })
            }
          }).catch((err) => {
            console.log('err', err)
            this.$message({
              message: "删除应用失败",
              type: 'warning'
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      applyListpage(page, pageSize, appGroupName) {
        let params = {
          appGroupName: appGroupName || this.form.applyGroup,
          appName: this.form.applyName,
          page: page || 1,
          pageSize: pageSize || this.tableData.ps
        }
        return Api.appinfoListpage(params)
      },
      //查询应用
      doSearch() {
         this.applyListpage().then((res) => {
          if(res.code === 200) {
            res.data.forEach(function(item){
              item.createTime = TimeFormat.timeFormat(item.createTime)
              item.modifiedTime = TimeFormat.timeFormat(item.modifiedTime)
            })
            this.tableData.data = res.data
            if (this.tableData.data.length < this.tableData.ps) {
              this.tableData.total = this.tableData.data.length
            } else {
              this.tableData.total = 100
            }
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "查询应用失败",
            type: 'warning'
          })
        })
      }
    }
  }
</script>
