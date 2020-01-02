<template>
  <div class="application-group">
    <div class="application-group-search b-wrapper">
     <el-form :inline="true" @submit.native.prevent>
       <el-form-item label="应用组" class="top-btn-left">
         <el-input v-model="applicationGroup" placeholder="请输入关键字进行搜索" @keyup.enter.native="doSearch"></el-input>
       </el-form-item>
       <div class="top-btn-right">
         <el-form-item>
           <el-button type="primary" size="medium" icon="el-icon-search" @click="doSearch">查 找</el-button>
         </el-form-item>
         <el-form-item>
           <el-button type="primary" size="medium" icon="el-icon-plus" @click="addApplicationGroup">新增应用组</el-button>
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
          prop="operatorName"
          label="更新人">
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="创建人">
        </el-table-column>
        <el-table-column
          prop="modifiedTime"
          label="更新时间"
          min-width="100"
          >
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          min-width="100"
          >
        </el-table-column>
        <el-table-column label="操作"  min-width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row.appGroupName, scope.row.operatorId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="application-group-pagination">
        <el-pagination
          background
          layout="prev, next"
          @current-change="currentChangeData"
          :current-page="tableData.pn"
          :total="tableData.total"
          >
        </el-pagination>
      </div>
    </div>
    
    <el-dialog title="新增应用组" :visible.sync="dialogAddApplicationGroup">
      <el-form :inline="true" class="add-application-group" label-position="right" label-width="120px">
        <p class="add-application-group-input">
          <span>应用组名称</span>
          <el-input v-model="applyGroupName"></el-input>
          <el-tooltip class="item-warning" effect="dark" content="注意：只能使用数字、字母、-、_" placement="bottom">
            <i class="el-icon-question"></i>
          </el-tooltip>
        </p>
        <p class="add-application-group-btn dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closeialogAddApplicationGroup">取消</el-button>
            <el-button type="primary" @click="doSubmitAddApplicationGroup">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
    <el-dialog title="修改应用组" :visible.sync="dialogEditApplicationGroup">
      <el-form :inline="true" class="edit-application-group" label-position="right" label-width="120px">
        <p class="edit-application-group-input">
          <span>原应用组名称</span>
          <el-input v-model="editRowData.appGroupName" disabled></el-input>
        </p>
        <p class="edit-application-group-input edit-application-group-input-new">
          <span>新应用组名称</span>
          <el-input v-model="newAppGroupName"></el-input>
          <el-tooltip class="item-warning" effect="dark" content="注意：只能使用数字、字母、-、_" placement="bottom">
            <i class="el-icon-question"></i>
          </el-tooltip>
        </p>
        <p class="edit-application-group-btn dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closedialogEditApplicationGroup">取消</el-button>
            <el-button type="primary" @click="doSubmitEditApplicationGroup">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
  </div>
</template>

<style lang="scss">
  .application-group {
    .application-group-table {
      margin-top: 20px;
    }
    .application-group-search .el-form-item {
      margin-right: 25px;
      margin-bottom: 0;
    }
    .application-group-search .top-btn-left {
      float: left;
    }
    .application-group-search .top-btn-right {
      float: right;
    }
    .application-group-pagination {
      text-align: center;
      margin-top: 20px;
    }
    .el-pagination.is-background .el-pager li:not(.disabled).active {
      border: none;
    }
    .add-application-group .add-application-group-input {
      padding-left: 20px
    }
    .add-application-group .add-application-group-input .el-input {
      width: 40%;
      margin-left: 20px;
      margin-right: 20px;
    }
    .add-application-group .add-application-group-input .el-input .el-input__inner {
      height: 35px;
    }
    .add-application-group .add-application-group-btn {
      margin-top: 40px;
    }
    //修改弹框样式
    .edit-application-group .edit-application-group-input {
      padding-left: 20px
    }
    .edit-application-group .edit-application-group-input .el-input {
      width: 40%;
      margin-left: 20px;
      margin-right: 20px;
    }
    .edit-application-group .edit-application-group-input .el-input .el-input__inner {
      height: 35px;
    }
    .edit-application-group .edit-application-group-input-new {
      margin-top: 15px;
    }
    .edit-application-group .edit-application-group-btn {
      margin-top: 40px;
      padding-left: 20px;
    }
    .el-table td {
      padding: 8px 0;
    }
    .el-input__inner {
      height: 34px;
      line-height: 34px;
    }
    .application-group-search .el-button {
      padding: 10px 14px;
    }
    .el-dialog__header {
      padding: 20px;
      border-bottom: 1px solid #dcdfe6;
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
        tableData: {
          data: [],
          ps: 8,
          pn: 1,
          total: null
        },
        editRowData: {},
        newAppGroupName: '',
        applicationGroup: '',
        dialogAddApplicationGroup: false,
        applyGroupName: '',
        dialogEditApplicationGroup: false
      }
    },
    created() {
    },
    mounted() {
      this.doSearch()
    },
    methods: {
      //编辑应用组
      doSubmitEditApplicationGroup() {
        let params = {
          appGroupName: this.editRowData.appGroupName,
          newAppGroupName: this.newAppGroupName
        }
        Api.appgroupEdit(params).then((res) => {
          if (res.code === 200) {
            this.$message({
              message: '修改应用组成功',
              type: 'success'
            })
            this.doSearch()
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "修改应用组失败",
            type: 'warning'
          })
        })
        this.closedialogEditApplicationGroup()
      },
      closedialogEditApplicationGroup() {
        this.dialogEditApplicationGroup = false
      },
      handleEdit(rowData) {
        this.editRowData = rowData
        this.dialogEditApplicationGroup = true
      },
      //删除应用组
      handleDelete(appGroupName, operatorId) {
        let params = {
          appGroupName: appGroupName,
          operatorId: operatorId
        }
        this.$confirm('确定删除该应用组吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          Api.appgroupDelete(params).then((res) => {
            if (res.code === 200) {
              this.$message({
                message: '删除应用组成功',
                type: 'success'
              })
              this.doSearch()
            } else {
              this.$message({
                message: res.msg,
                type: 'warning'
              })
            }
          }).catch((err) => {
            this.$message({
              message: "删除应用组失败",
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
      currentChangeData(page) {
        console.log('page', page)
        console.log('this.tableData.total', this.tableData.total)
        this.appGroupListpage(page).then((res) => {
          if(res.code === 200) {
            res.data.forEach(function(item){
              item.createTime = TimeFormat.utcTimeFormat(item.createTime);
              item.modifiedTime = TimeFormat.utcTimeFormat(item.modifiedTime);
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
            message: "分页查询应用组失败",
            type: 'warning'
          })
        })
      },
      appGroupListpage(page, pageSize) {
        let params = {
          appGroupName: this.applicationGroup,
          page: page || 1,
          pageSize: pageSize || this.tableData.ps
        }
        return Api.appgroupListpage(params)
      },
      addApplicationGroup() {
        this.dialogAddApplicationGroup = true
      },
      //查询应用组
      doSearch() {
        this.appGroupListpage().then((res) => {
          if(res.code == 200) {
            res.data.forEach(function(item){
              item.createTime = TimeFormat.utcTimeFormat(item.createTime)
              item.modifiedTime = TimeFormat.utcTimeFormat(item.modifiedTime)
            })
            this.tableData.data = res.data
            console.log('this.tableData.total', this.tableData.total)
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
            message: "查询应用组失败",
            type: 'warning'
          })
        })
      },
      //新增应用组
      doSubmitAddApplicationGroup() {
        let params = {
          appGroupName: this.applyGroupName
        }
        Api.appgroupAdd(params).then((res) => {
          if (res.code === 200) {
            this.$message({
              message: '新增成功',
              type: 'success'
            })
            this.doSearch()
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "新增失败",
            type: 'warning'
          })
        })
        this.closeialogAddApplicationGroup()
      },
      closeialogAddApplicationGroup() {
        this.dialogAddApplicationGroup = false
      }
    }
  }
</script>
