<template>
  <div class="demote-point">
    <div class="demote-point-search b-wrapper">
      <el-row :gutter="24">
        <el-form label-position="top" label-width="80px" size="medium">
          <el-col :span="6">
            <el-form-item label="应用组">
              <el-select v-model="form.applyGroup" placeholder="应用组">
                <p v-for="(item, index) in applyGroupData" :key="index">
                  <el-option :label="item" :value="item"></el-option>
                </p>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="应用名称">
              <el-select v-model="form.appName" placeholder="应用名称" @change="querysdsschemetips">
                <p v-for="(item, index) in appNameData" :key="index">
                  <el-option :label="item" :value="item"></el-option>
                </p>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="降级预案">
              <el-select v-model="form.sdsScheme" placeholder="降级预案">
                <p v-for="(item, index) in sdsSchemeData" :key="index">
                  <el-option :label="item" :value="item"></el-option>
                </p>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="输入搜索">
              <el-input v-model="form.demotePoint" placeholder="输入降级点名称搜索" @keyup.enter.native="doSearch"></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div class="search-btn-wrapper">
        <el-button type="primary" @click="doSearch" icon="el-icon-search">查 找</el-button>
        <el-button type="primary" @click="addDemotePoint" icon="el-icon-plus">新增降级点策略</el-button>
      </div>
    </div>
    <div v-if="!!sdsschemetips" class="strategy-group-tips">
      <el-tag type="danger">{{ sdsschemetips }}</el-tag>
    </div>
    <div class="demote-point-table b-wrapper">
      <el-table
        :data="tableData.data"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="访问量阈值">
                <span>{{ props.row.visitThreshold }}</span>
              </el-form-item>
              <el-form-item label="并发量阈值">
                <span>{{ props.row.concurrentThreshold }}</span>
              </el-form-item>
              <el-form-item label="异常量阈值">
                <span>{{ props.row.exceptionThreshold }}</span>
              </el-form-item>
              <el-form-item label="异常率阈值">
                <span>{{ props.row.exceptionRateThreshold }}</span>
              </el-form-item>
              <el-form-item label="异常率起始阈值">
                <span>{{ props.row.exceptionRateStart }}</span>
              </el-form-item>
              <el-form-item label="超时时间阈值">
                <span>{{ props.row.timeoutThreshold }}</span>
              </el-form-item>
              <el-form-item label="超时量阈值">
                <span>{{ props.row.timeoutCountThreshold }}</span>
              </el-form-item>
              <el-form-item label="每秒生成令牌数">
                <span>{{ props.row.tokenBucketGeneratedTokensInSecond }}</span>
              </el-form-item>
              <el-form-item label="令牌桶容量">
                <span>{{ props.row.tokenBucketSize }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="更新时间">
                <span>{{ props.row.modifiedTime }}</span>
              </el-form-item>
              <el-form-item label="">
                <span></span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          prop="appGroupName"
          label="应用组"
          width="100">
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
          prop="sdsSchemeName"
          label="降级预案"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column
          prop="downgradeRate"
          label="降级比例"
          width="100">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.status == 1 ? '' : 'info'"
              disable-transitions>{{scope.row.status == 1 ? '开启' : '关闭'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="operatorName"
          label="更新人"
          width="100">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>操作时间: {{ scope.row.modifiedTime }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag type="warning" color="#fff">{{scope.row.operatorName || '测试者'}}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="创建人"
          width="100">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>创建时间: {{ scope.row.createTime }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag color="#fff">{{scope.row.creatorName || '创建者'}}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
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

    <el-dialog title="新增降级点策略" :visible.sync="dialogAddDemotePoint">
      <div style="padding-bottom: 20px; line-height: 20px;" v-if="!!sdsschemetips">
        <span style="color: red">{{ sdsschemetips }}</span>
      </div>
      <el-form :inline="true" class="add-apply" label-position="right" label-width="140px">
        <p class="of-application-group">
          <el-form-item label="应用组">
            <el-select v-model="addApplyGroupName" placeholder="应用组">
        <p v-for="(item, index) in applyGroupData" :key="index">
          <el-option :label="item" :value="item"></el-option>
        </p>
        </el-select>
        </el-form-item>
        </p>
        <p class="of-application-group">
          <el-form-item label="应用名称">
            <el-select v-model="addApplyName" placeholder="应用名称" @change="querysdsschemetips">
        <p v-for="(item, index) in someApplyData" :key="index">
          <el-option :label="item" :value="item"></el-option>
        </p>
        </el-select>
        </el-form-item>
        </p>
        <div class="strategy-group">
          <el-form-item label="降级预案">
            <el-select v-model="addSdsScheme">
              <p v-for="(item, index) in addSdsSchemeData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </div>
        <p class="add-apply-name">
          <el-form-item label="降级点名称">
            <el-select v-model="addDemotePointName" filterable allow-create placeholder="降级点">
              <el-option v-for="(item, index) in pointDictlist" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：只能使用字母、数字、-、_" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="访问量阈值">
            <el-input v-model="accessThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的访问量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="并发量阈值">
            <el-input v-model="concurrencyThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：单台的并发量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级比例">
            <el-input v-model="downgradeRatio"></el-input>
          </el-form-item>
          <el-form-item>%</el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：触发策略后被降级的概率，取值[0-100],比如50表示50%的概率被降级掉" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="of-application-group">
          <el-form-item label="状态">
            <el-select v-model="demotePointStatus" placeholder="">
              <el-option label="开启-1" :value="1"></el-option>
              <el-option label="关闭-0" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：0表示不启用, 1表示启用" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <!-- <p class="of-application-group">
          <el-form-item label="所用降级预案">
            <el-select v-model="form.sdsScheme" placeholder="">
              <el-option label="降级预案1" value="1"></el-option>
              <el-option label="降级预案2" value="2"></el-option>
            </el-select>
          </el-form-item>
        </p> -->
        <p class="dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="advancedConfig">高级配置</el-button>
            <el-button type="primary" @click="closeDialogAddDemotePoint">取消</el-button>
            <el-button type="primary" @click="doSubmitAddDemotePoint">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
    <el-dialog title="修改降级点策略" :visible.sync="dialogEditDemotePoint">
      <div style="padding-bottom: 20px; line-height: 20px;" v-if="!!sdsschemetips">
        <span style="color: red">{{ sdsschemetips }}</span>
      </div>
      <el-form :inline="true" class="add-apply"  label-position="right" label-width="140px">
        <p class="of-application-group">
          <el-form-item label="应用组名称">
            <el-select v-model="editRowData.appGroupName" placeholder="" disabled>
              <p v-for="(item, index) in applyGroupData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p>
        <p class="of-application-group">
          <el-form-item label="应用名称">
            <el-select v-model="editRowData.appName" placeholder="" disabled>
              <p v-for="(item, index) in someApplyData" :key="index">
                <el-option :label="item" :value="item"></el-option>
              </p>
            </el-select>
          </el-form-item>
        </p>
        <p>
          <el-form-item label="降级预案">
            <el-select v-model="editRowData.sdsSchemeName" placeholder="">
            <p v-for="(item, index) in editSdsSchemeData" :key="index">
              <el-option :label="item" :value="item"></el-option>
            </p>
            </el-select>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级点名称">
            <el-input v-model="editRowData.point"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：只能使用字母、数字、-、_" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="访问量阈值">
            <el-input v-model="editRowData.visitThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的访问量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="并发量阈值">
            <el-input v-model="editRowData.concurrentThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：单台的并发量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级比例">
            <el-input v-model="editRowData.downgradeRate"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：触发策略后被降级的概率，取值[0-100],比如50表示50%的概率被降级掉" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="of-application-group">
          <el-form-item label="状态">
            <el-select v-model="editRowData.status" placeholder="">
              <el-option label="开启-1" :value="1"></el-option>
              <el-option label="关闭-0" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：0表示不启用, 1表示启用" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="editAdvancedConfig">高级配置</el-button>
            <el-button type="primary" @click="closeDialogEditDemotePoint">取消</el-button>
            <el-button type="primary" @click="doSubmitEditDemotePoint">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
    <el-dialog title="高级配置" :visible.sync="dialogAdvancedConfig">
      <el-form :inline="true" class="edit-apply" label-position="right" label-width="140px">
        <p class="add-apply-name">
          <el-form-item label="每秒生成令牌数">
            <el-input v-model="tokenBucketGeneratedTokensInSecond"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：令牌桶每个1秒能生成多少个令牌，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="令牌桶容量">
            <el-input v-model="tokenBucketSize"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：令牌桶最多能存储多少个令牌，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常量阈值">
            <el-input v-model="exceptionThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的异常量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常率阈值">
            <el-input v-model="exceptionRateThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：取值为[0-100],-1表示不生效,15表示异常率阈值为15%" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常率起始阈值">
            <el-input v-model="exceptionRateStart"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：访问量超过该值，异常率才生效，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="超时时间阈值">
            <el-input v-model="timeoutThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：耗时超过该时间的请求会计入超时计时器中，单位毫秒，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="超时量阈值">
            <el-input v-model="timeoutCountThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的超时量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="压测降级">
            <el-select v-model="pressureTestDowngrade" placeholder="否">
              <el-option label="不进行特殊处理-0" :value="0"></el-option>
              <el-option label="强制降级-1" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="【强制降级】表示只要当前策略生效，哪怕降级比例是0，遇到压测流量都会强制进行降级，【不进行特殊处理】指的是不去识别压测流量" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级延长时间">
            <el-input v-model="delayTime"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：表示降级的持续时间，单位毫秒，最小值为10000，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级延长重试间隔">
            <el-input v-model="retryInterval"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：降级延长时间的重试请求的时间间隔，单位毫秒，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closeDialogAdvancedConfig">基本配置</el-button>
            <el-button type="primary" @click="closeDialogAdvancedConfig">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
    <el-dialog title="高级配置" :visible.sync="dialogEditAdvancedConfig">
      <el-form :inline="true" class="edit-apply" label-position="right" label-width="140px">
        <p class="add-apply-name">
          <el-form-item label="每秒生成令牌数">
            <el-input v-model="editRowData.tokenBucketGeneratedTokensInSecond"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：令牌桶每个1秒能生成多少个令牌，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="令牌桶容量">
            <el-input v-model="editRowData.tokenBucketSize"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：令牌桶最多能存储多少个令牌，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常量阈值">
            <el-input v-model="editRowData.exceptionThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的异常量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常率阈值">
            <el-input v-model="editRowData.exceptionRateThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：取值为[0-100],-1表示不生效,15表示异常率阈值为15%" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="异常率起始阈值">
            <el-input v-model="editRowData.exceptionRateStart"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：访问量超过该值，异常率才生效，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="超时时间阈值">
            <el-input v-model="editRowData.timeoutThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：耗时超过该时间的请求会计入超时计时器中，单位毫秒，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="超时量阈值">
            <el-input v-model="editRowData.timeoutCountThreshold"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：每10秒单台的超时量阈值,-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="压测降级">
            <el-select v-model="editRowData.pressureTestDowngrade" placeholder="否">
              <el-option label="不进行特殊处理-0" :value="0"></el-option>
              <el-option label="强制降级-1" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="【强制降级】表示只要当前策略生效，哪怕降级比例是0，遇到压测流量都会强制进行降级，【不进行特殊处理】指的是不去识别压测流量" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级延长时间">
            <el-input v-model="editRowData.delayTime"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：表示降级的持续时间，单位毫秒，最小值为10000，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="add-apply-name">
          <el-form-item label="降级延长重试间隔">
            <el-input v-model="editRowData.retryInterval"></el-input>
          </el-form-item>
          <el-form-item>
            <el-tooltip class="item-warning" effect="dark" content="注意：降级延长时间的重试请求的时间间隔，单位毫秒，-1表示不生效" placement="bottom">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </el-form-item>
        </p>
        <p class="dialog-footer">
          <el-form-item>
            <el-button type="primary" @click="closeDialogEditAdvancedConfig">基本配置</el-button>
            <el-button type="primary" @click="closeDialogEditAdvancedConfig">确定</el-button>
          </el-form-item>
        </p>
      </el-form>
    </el-dialog>
  </div>
</template>

<style lang="scss">
  .demote-point {
    .demote-point-search {
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
    .demote-point-table .el-form--inline .el-form-item__content {
      width: 300px;
    }
  }
  .strategy-group-tips {
    color: red;
    font-size: 14px;
  }
  .demote-point .top-line {
    height: 1px;
    background: #f6f6f6;
    margin: 0 -20px;
  }
  .demote-point .demote-point-table {
    margin-top: 20px;
    position: relative;
  }
  .application-group-pagination {
    text-align: center;
    margin-top: 20px;
  }
  .demote-point-table {
    position: relative;
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
  .demote-point-search .top-btn-right {
    float: right;
  }
  .demote-point-search .el-button {
    padding: 10px 14px;
  }
  .el-dialog__header {
    padding: 20px;
    border-bottom: 1px solid #dcdfe6;
  }
  .dialog-footer {
    padding-left: 80px;
  }
  .demo-table-expand {
    font-size: 0;
    label {
      width: 130px;
      color: #409EFF;
    }
    .el-form-item {
      margin-right: 0;
      margin-bottom: 0;
      width: 33%;
      text-align: center;
    }
  }
  .el-table__expanded-cell {
    padding: 10px !important;
    background-color: #fafafa;
    &:hover {
      background-color: #f2f5f9 !important;
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
        oldSdsSchemeName: '',
        editNewSdsSchemeName: '',
        editSdsSchemeData: [],
        newSdsSchemeName: '',
        dialogEditAdvancedConfig: false,
        appNameData: [],
        addSdsSchemeData: [],
        addSdsScheme: '',
        demotePointStatus: 1,
        timeoutCountThreshold: '',
        pressureTestDowngrade: 0,
        editConcurrencyThreshold: '',
        retryInterval: '',
        delayTime: '',
        exceptionRateThreshold: '',
        visitGrowthThreshold: '',
        visitGrowthRate: '',
        editDowngradeRatio: '',
        editAccessThreshold: '',
        editDemotePointName: '',
        dialogEditDemotePoint: false,
        timeoutThreshold: '',
        exceptionThreshold: '',
        exceptionRateStart: '',
        tokenBucketGeneratedTokensInSecond: -1,
        tokenBucketSize: -1,
        someApplyData: '',
        downgradeRatio: 100,
        concurrencyThreshold: '',
        accessThreshold: '',
        addDemotePointName: '',
        addApplyName: '',
        addApplyGroupName: '',
        dialogAddDemotePoint: false,
        dialogAdvancedConfig: false,
        sdsSchemeData: [],
        editSdsScheme: '',
        editApplyName: '',
        editRowData: {},
        dialogEditApply: false,
        dialogAddApply: false,
        applicationGroup: "",
        tableData: {
          data: [],
          ps: 8,
          pn: 1,
          total: 1
        },
        applyGroupData: [],
        form: {
          appName: '',
          applyGroup: '',
          sdsScheme: '',
          demotePoint: ''
        },
        showAllExpand: true,
        sdsschemetips: '',
        pointDictlist: []
      }
    },
    created () {
      this.appgroupListall()
    },
    watch: {
      'editRowData.sdsSchemeName' (newVal) {
        this.editNewSdsSchemeName = newVal
      },
      //监听编辑case下面某应用的所有降级预案
      'editRowData' (newVal) {
        this.editRowData.sdsScheme = ''
        this.sdsschemeListall(newVal.appGroupName, newVal.appName).then((res) => {
          if (res.code === 200) {
            this.editSdsSchemeData = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "查询该应用下的所有降级预案失败",
            type: 'warning'
          })
        })
      },
      //监听查询case下的应用组变化
      'form.applyGroup' (newVal, oldVal) {
        this.form.appName = '';
        this.form.sdsScheme = '';
        this.getAppinfoListall(newVal).then((res) => {
          if(res.code === 200) {
            this.appNameData = res.data
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
      //监听查询状态下的应用变化
      'form.appName' (newVal, oldVal) {
        if (!newVal) return;
        this.form.sdsScheme = ''
        this.sdsschemeListall(this.form.applyGroup, newVal).then((res) => {
          if (res.code === 200) {
            this.sdsSchemeData = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "查询该应用下的所有降级预案失败",
            type: 'warning'
          })
        })
      },
      //监听应用组变化 获取该应用组下所有应用
      addApplyGroupName (newVal, oldVal) {
        this.addApplyName = ''
        this.getAppinfoListall(newVal).then((res) => {
          if(res.code === 200) {
            this.someApplyData = res.data
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
      //监听增加策略点case下 获取所有降级预案
      addApplyName (newVal, oldVal) {
        this.addSdsScheme = '',
          this.sdsschemeListall(this.addApplyGroupName, newVal).then((res) => {
            if (res.code === 200) {
              console.log('sdsschemeListall', res.data)
              this.addSdsSchemeData = res.data
            } else {
              this.$message({
                message: res.msg,
                type: 'warning'
              })
            }
          }).catch((err) => {
            this.$message({
              message: "查询所有降级预案失败",
              type: 'warning'
            })
          })
        this.queryPointDictlist(this.addApplyGroupName, newVal)
      }
    },
    methods: {
      querysdsschemetips () {
        Api.querysdsschemetips({
          appGroupName: this.form.applyGroup || this.addApplyGroupName,
          appName: this.form.appName || this.addApplyName
        }).then((res) => {
          if(res.code === 200) {
            this.sdsschemetips = res.data
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "获取应用降级预案失败",
            type: 'warning'
          })
        })
      },
      closeDialogEditDemotePoint () {
        this.dialogEditDemotePoint = false
      },
      closeDialogEditAdvancedConfig () {
        this.dialogEditAdvancedConfig = false
      },
      closeDialogEditAdvancedConfig () {
        this.dialogEditAdvancedConfig = false
      },
      //点击修改case中高级配置按钮
      editAdvancedConfig () {
        this.dialogEditAdvancedConfig = true
      },
      //修改降级点
      doSubmitEditDemotePoint () {
        let params = {
          appGroupName: this.editRowData.appGroupName, //应用组
          appName: this.editRowData.appName,//应用名称
          sdsSchemeName: this.oldSdsSchemeName, //降级预案名称
          newSdsSchemeName: this.editRowData.sdsSchemeName, //新降级预案名
          point: this.editRowData.point, //降级点名称
          visitThreshold: this.editRowData.visitThreshold, //访问量阈值
          concurrentThreshold: this.editRowData.concurrentThreshold, //并发量阈值
          downgradeRate: this.editRowData.downgradeRate, //降级比例
          status: this.editRowData.status, //状态
          visitGrowthRate: this.editRowData.visitGrowthRate, //访问量增长比率
          visitGrowthThreshold: this.editRowData.visitGrowthThreshold,
          exceptionThreshold: this.editRowData.exceptionThreshold, // 异常量阈值
          exceptionRateThreshold: this.editRowData.exceptionRateThreshold, //异常率阈值
          exceptionRateStart: this.editRowData.exceptionRateStart, // 异常率起始阈值
          tokenBucketGeneratedTokensInSecond: this.editRowData.tokenBucketGeneratedTokensInSecond,
          tokenBucketSize: this.editRowData.tokenBucketSize,
          timeoutThreshold: this.editRowData.timeoutThreshold, //超时时间阈值
          timeoutCountThreshold: this.editRowData.timeoutCountThreshold, //超时量阈值
          pressureTestDowngrade: this.editRowData.pressureTestDowngrade, //压测降级
          delayTime: this.editRowData.delayTime, //降级延长时间，单位ms
          retryInterval: this.editRowData.retryInterval //降级延长重试间隔，单位ms
        }
        Api.pointStrategyEdit(params).then((res) => {
          if (res.code === 200) {
            this.$message({
              message: "修改降级点成功",
              type: 'success'
            })
            this.dialogEditDemotePoint = false
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "修改降级点失败",
            type: 'warning'
          })
        })
      },
      //关闭增加降级点的高级设置弹窗
      closeDialogAdvancedConfig () {
        this.dialogAdvancedConfig = false
      },
      //查询某应用组下面的所有应用
      getAppinfoListall (appGroup) {
        let params = {
          appGroupName: appGroup
        }
        return Api.appinfoListall(params)
      },
      advancedConfig () {
        this.dialogAdvancedConfig = true;
      },
      //下一页前一页查询
      currentChangeData(page) {
        this.pointStrategyListpage(page).then((res) => {
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
      //查询所有降级预案
      sdsschemeListall (appGroupName, appName) { // todo
        let params = {
          appGroupName: appGroupName,
          appName: appName
        }
        return Api.sdsschemeListall(params)
      },
      appgroupListall () {
        let params = {
        }
        Api.appgroupListall(params).then((res) => {
          if(res.code === 200) {
            // res.data.forEach(function(item){
            //   item.createTime = TimeFormat.timeFormat(item.createTime)
            //   item.modifiedTime = TimeFormat.timeFormat(item.modifiedTime)
            // })
            console.log('appgroupListall', res.data)
            this.applyGroupData = res.data
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
      //增加降级点
      doSubmitAddDemotePoint() {
        let params = {
          appGroupName: this.addApplyGroupName, //应用组
          appName: this.addApplyName,//应用名称
          sdsSchemeName: this.addSdsScheme, //降级预案名称
          point: this.addDemotePointName, //降级点名称
          visitThreshold: this.accessThreshold, //访问量阈值
          concurrentThreshold: this.concurrencyThreshold, //并发量阈值
          downgradeRate: this.downgradeRatio, //降级比例
          status: this.demotePointStatus, //状态
          visitGrowthRate: this.visitGrowthRate, //访问量增长比率
          visitGrowthThreshold: this.visitGrowthThreshold,
          exceptionThreshold: this.exceptionThreshold, // 异常量阈值
          exceptionRateThreshold: this.exceptionRateThreshold, //异常率阈值
          exceptionRateStart: this.exceptionRateStart, // 异常率起始阈值
          tokenBucketGeneratedTokensInSecond: this.tokenBucketGeneratedTokensInSecond,
          tokenBucketSize: this.tokenBucketSize,
          timeoutThreshold: this.timeoutThreshold, //超时时间阈值
          timeoutCountThreshold: this.timeoutCountThreshold, //超时量阈值
          delayTime: this.delayTime, //降级延长时间，单位ms
          retryInterval: this.retryInterval, //降级延长重试间隔，单位ms
          pressureTestDowngrade: this.pressureTestDowngrade, //压测强制降级
        }
        Api.pointStrategyAdd(params).then((res) => {
          if(res.code === 200) {
            this.$message({
              message: "新增降级点成功",
              type: 'success'
            })
            this.dialogAddDemotePoint = false
          } else {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        }).catch((err) => {
          this.$message({
            message: "新增降级点失败",
            type: 'warning'
          })
        })
        this.dialogAddDemotePoint = false
      },
      closeDialogAddDemotePoint() {
        this.dialogAddDemotePoint = false
      },
      addDemotePoint() {
        this.dialogAddDemotePoint = true
      },
      handleEdit(row) {
        // 展示tips
        this.form.applyGroup = row.appGroupName
        this.form.appName = row.appName
        this.querysdsschemetips()
        this.dialogEditDemotePoint = true
        this.editRowData = row
        this.oldSdsSchemeName = row.sdsSchemeName
      },
      //删除降级点
      handleDelete(rowData) {
        let params = {
          appGroupName: rowData.appGroupName,
          appName: rowData.appName,
          point: rowData.point,
          sdsSchemeName: rowData.sdsSchemeName,
          operatorId: rowData.operatorId
        }
        this.$confirm('确定删除该降级点吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          Api.pointStrategyDelete(params).then((res) => {
            if(res.code === 200) {
              this.$message({
                message: "删除降级点成功",
                type: 'success'
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'warning'
              })
            }
          }).catch((err => {
            console.log('err', err)
            this.$message({
              message: "删除降级点失败",
              type: 'warning'
            })
          }))
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      pointStrategyListpage (page, pageSize) {
        let params = {
          appGroupName: this.form.applyGroup,
          appName: this.form.appName,
          sdsSchemeName: this.form.sdsScheme,
          point: this.form.demotePoint,
          page: page || 1,
          pageSize: pageSize || this.tableData.ps
        }
        return Api.pointStrategyListpage(params)
      },
      //查询应用
      doSearch() {
        this.pointStrategyListpage().then((res) => {
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
            message: "查询策略点失败",
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
    }
  }
</script>
