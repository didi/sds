<template>
  <div class="demote-point-response">
     <div class="demote-point-response-search b-wrapper">
      <el-row :gutter="24">
        <el-form ref="form" :model="searchForm" label-position="top" label-width="80px" size="medium">
            <el-col :span="6">
              <el-form-item label="应用组">
                <el-select v-model="searchForm.appGroupName" placeholder="应用组">
                  <el-option v-for="(item, index) in applyGroupList" :key="index" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="应用">
                <el-select v-model="searchForm.appName" placeholder="应用">
                  <el-option v-for="(item, index) in appInfoList" :key="index" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="降级点">
                <el-select v-model="searchForm.point" placeholder="降级点" filterable allow-create clearable>
                  <el-option v-for="(item, index) in pointDictlist" :key="index" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="降级返回值">
                  <el-input v-model="searchForm.returnValueStr" placeholder="降级返回值"></el-input>
              </el-form-item>
            </el-col>
        </el-form>
      </el-row>
      <div class="search-btn-wrapper">
        <el-button type="primary" size="medium" icon="el-icon-search" @click="querypointreturnvalue(1)"> 查 询 </el-button>
        <el-button type="primary" size="medium" icon="el-icon-plus" @click="dialogAddPointResponse = true"> 新 增 </el-button>
      </div>
    </div>
    <div class="demote-point-response-table b-wrapper">
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
            <el-tag color="#fff"> {{scope.row.status == 1 ? '开启' : '关闭'}}</el-tag>
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
              @click="updatePointRes(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="deletpointreturnvalue(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="demote-point-response-pagination">
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
     <el-dialog title="新增降低点返回值" :visible.sync="dialogAddPointResponse">
       <el-form ref="form" :model="addPointResForm" label-width="100px" size="medium">
          <el-form-item label="应用组">
            <el-select v-model="addPointResForm.appGroupName" placeholder="应用组">
              <el-option v-for="(item, index) in applyGroupList" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="应用">
            <el-select v-model="addPointResForm.appName" placeholder="应用">
              <el-option v-for="(item, index) in appInfoList" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="降级点">
            <el-select v-model="addPointResForm.point" filterable allow-create clearable placeholder="降级点">
              <el-option v-for="(item, index) in pointDictlist" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="addPointResForm.status" placeholder="状态">
              <el-option label="关闭" value="0"></el-option>
              <el-option label="开启" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="降级返回值">
            <p style="color: red"> 注意：输入 JSON 格式</p>
            <el-input type="textarea" rows="3" v-model="addPointResForm.returnValueStr"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addpointreturnvalue">确 定</el-button>
            <el-button @click="dialogAddPointResponse = false">取 消</el-button>
          </el-form-item>
        </el-form>
     </el-dialog>
     <el-dialog title="更新降低点返回值" :visible.sync="dialogEditPointResponse">
       <el-form ref="form" :model="editPointResForm" label-width="100px" size="medium">
          <el-form-item label="应用组">
            <el-select v-model="editPointResForm.appGroupName" placeholder="应用组">
              <el-option v-for="(item, index) in applyGroupList" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="应用">
            <el-select v-model="editPointResForm.appName" placeholder="应用">
              <el-option v-for="(item, index) in appInfoList" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="降级点">
            <el-select v-model="editPointResForm.point" filterable allow-create clearable placeholder="降级点">
              <el-option v-for="(item, index) in pointDictlist" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="editPointResForm.status" placeholder="状态">
              <el-option label="关闭" value="0"></el-option>
              <el-option label="开启" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="降级返回值">
            <p style="color: red"> 注意：输入 JSON 格式</p>
            <el-input type="textarea" rows="3" v-model="editPointResForm.returnValueStr"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updatePointReturnvalue">确 定</el-button>
            <el-button @click="dialogEditPointResponse = false">取 消</el-button>
          </el-form-item>
        </el-form>
     </el-dialog>
  </div>
 
</template>

<style lang="scss">
  .demote-point-response {
    .demote-point-response-search {
      .el-row {
        margin-bottom: 20px;
        &:last-child {
          margin-bottom: 0;
        }
      }
      .el-select {
        width: 100%;
      }

      .el-form-item__label {
        line-height: 1;
      }

      .el-form-item {
        margin-bottom: 0;
      }
    }
    .demote-point-response-table {
      margin-top: 20px;
    }
    .demote-point-response-pagination {
      text-align: center;
      margin-top: 20px;
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
        searchForm: {
          appGroupName: '',
          appName: '',
          point: '',
          returnValueStr: ''
        },
        addPointResForm: {
          appGroupName: '',
          appName: '',
          point: '',
          returnValueStr: '',
          status: '0'
        },
        editPointResForm: {
        },
        applyGroupList: [],
        appInfoList: [],
        pointDictlist: [],
        dialogAddPointResponse: false,
        dialogEditPointResponse: false,
        tableData: {
          data: [],
          ps: 8,
          pn: 1,
          total: null
        },
        editPointResFormShowStatus: '关闭'
      }
    },
    created () {
      this.getAppGroupList()
      this.querypointreturnvalue(1)
    },
    computed: {
    },
    watch: {
      'searchForm.appGroupName': function(n, o) {
        if( n !== o ) {
          this.getAppInfoList(this.searchForm.appGroupName)
          this.searchForm.appName = ''
          this.searchForm.point = ''
        }
      },
      'searchForm.appName': function(n, o) {
        if( n !== o && n ) {
          this.queryPointDictlist(this.searchForm.appGroupName, this.searchForm.appName)
          this.searchForm.point = ''
        }
      },
      'addPointResForm.appGroupName': function(n, o) {
        if( n !== o ) {
          this.getAppInfoList(this.addPointResForm.appGroupName)
          this.addPointResForm.appName = ''
          this.addPointResForm.point = ''
        }
      },
      'addPointResForm.appName': function(n, o) {
        if( n !== o && n ) {
          this.queryPointDictlist(this.addPointResForm.appGroupName, this.addPointResForm.appName)
          this.addPointResForm.point = ''
        }
      },
    },
    methods: {
      //下一页前一页查询
      currentChangeData(page) {
        this.querypointreturnvalue(page)
      },
      getAppGroupList() {
        Api.appgroupListall().then((res) => {
          if(res.code === 200) {
            this.appInfoList = []
            this.applyGroupList = ['全部'].concat(res.data)
            this.searchForm.appName = ''
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
      getAppInfoList (appGroupName) {
        Api.appinfoListall({
          appGroupName
        }).then((res) => {
          if(res.code === 200) {
            this.appInfoList = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "查询该应用组下的所有应用失败",
            type: 'warning'
          })
        })
      },
      queryPointDictlist (appGroupName, appName) {
        Api.queryPointDictlist({
          appGroupName,
          appName
        }).then((res) => {
          if(res.code === 200) {
           this.pointDictlist = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "获取降点失败",
            type: 'warning'
          })
        })
      },
      addpointreturnvalue () {
        Api.addpointreturnvalue({
          appGroupName: this.addPointResForm.appGroupName,
          appName: this.addPointResForm.appName,
          point:  this.addPointResForm.point,
          returnValueStr: this.addPointResForm.returnValueStr,
          status: this.addPointResForm.status
        }).then((res) => {
          if(res.code === 200) {
            this.$message({
              message: "新增降级返回值成功",
              type: 'warning'
            })
            this.dialogAddPointResponse = false;
            this.querypointreturnvalue(1)
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "新增降级返回值失败",
            type: 'warning'
          })
        })
      },
      querypointreturnvalue (page) {
        this.searchForm.appGroupName === '全部' ? this.searchForm.appGroupName = '' : false;
        Api.querypointreturnvalue({
          appGroupName: this.searchForm.appGroupName,
          appName: this.searchForm.appName,
          point: this.searchForm.point,
          returnValueStr: this.searchForm.returnValueStr,
          page: page,
          pageSize: this.tableData.ps
        }).then((res) => {
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
            message: "获取降级点返回值失败",
            type: 'warning'
          })
        })
      },
      updatePointRes(row) {
        this.dialogEditPointResponse = true
        this.editPointResForm = JSON.parse(JSON.stringify(row))
        this.editPointResForm.status == 0 ? this.editPointResForm.status = '关闭' : false
        this.editPointResForm.status == 1 ? this.editPointResForm.status = '开启' : false
      },
      updatePointReturnvalue() {
        Api.editpointreturnvalue({
          appGroupName: this.editPointResForm.appGroupName,
          appName: this.editPointResForm.appName,
          point:  this.editPointResForm.point,
          returnValueStr: this.editPointResForm.returnValueStr,
          status: this.editPointResForm.status
        }).then((res) => {
          if(res.code === 200) {
            this.$message({
              message: "更新降级返回值成功",
              type: 'warning'
            })
            this.dialogEditPointResponse = false;
            this.querypointreturnvalue(1)
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "更新降级返回值失败",
            type: 'warning'
          })
        })
      },
      deletpointreturnvalue(row) {
        this.$confirm('确定删除该降级点返回值吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          Api.deletpointreturnvalue({
            appGroupName: row.appGroupName,
            appName: row.appName,
            point: row.point
          }).then((res) => {
            if(res.code === 200) {
              this.$message({
                message: "删除降级点返回值成功",
                type: 'warning'
              })
              this.querypointreturnvalue(1)
            } else {
              this.$message({
                message: res.msg,
                type: 'warning'
              })
            }
          }).catch((err) => {
            this.$message({
              message: "删除降级点返回值失败",
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
    }
  }
</script>
