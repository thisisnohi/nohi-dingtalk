<template>
  <div class="layout-container">
    <div class="layout-container-form flex-direction-column" v-loading="listLoading">
      <el-form ref="queryForm" class="text-left" :model="searchParam" :rules="searchParamRule" :inline="true" label-width="120px" @keyup.enter.native="queryBtn">
        <el-form-item prop="queryUserNo">
          <el-input v-model="searchParam.queryUserId" placeholder="员工号" style="width: 98%;" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchParam.queryUserName" placeholder="员工名" style="width: 98%;" />
        </el-form-item>
        <el-form-item prop="datePicker">
          <el-date-picker
              v-model="searchParam.datePicker"
              type="daterange"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchParam.queryTimeResult" clearable placeholder="打卡结果" multiple style="width: 98%;">
            <el-option v-for="item in timeResultOptions" :key="item.key" :label="item.val" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchParam.queryLocationResult" clearable placeholder="定位结果" multiple style="width: 98%;">
            <el-option v-for="item in locationResultOptions" :key="item.key" :label="item.val" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div class="form-item text-left">
        <el-button type="primary" :icon="Search" :loading="listLoading" @click="queryBtn">查询</el-button>
        <el-button :icon="RefreshLeft" @click="resetBtn">重置</el-button>
        <el-button :icon="Refresh" :loading="syncLoading" @click="refreshSearchFormKaoQing">刷新考勤数据</el-button>
        <el-button :icon="Refresh" :loading="syncLoading" @click="refreshSearchFormApr">刷新审批数据</el-button>
      </div>
    </div>
    <div class="layout-container-table">

      <el-table :data="tableData" style="width: 100%" stripe highlight-current-row border>
        <el-table-column fixed="left" align="center" label="操作" width="50">
          <template #default="scope">
            <i class="el-icon" title="刷新审批数据" style="vertical-align: middle;" @click="refreshApr(scope.$index, scope.row)">
              <Refresh />
            </i>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="date" label="序号" width="50">
          <template #default="row">
            <span>{{ row.$index+1 }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="enUserNo" label="员工号" width="150">
          <template #default="{row}">
            <span>{{ row.dtUserId }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="dtUserName" label="员工名" width="80">
          <template #default="{row}">
            <span>{{ row.dtUserName }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="workDate" label="工作日" width="100">
          <template #default="{row}">
            {{ $moment(row.workDate).format('YYYY-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="checkTypeCn" label="考勤类型" width="80">
          <template #default="{row}">
            <span>{{ row.checkTypeCn }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="baseCheckTime" label="基准时间" width="80">
          <template #default="{row}">
            {{ $moment(row.baseCheckTime).format('HH:mm') }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="usercheckTime" label="实际打卡时间" width="110">
          <template #default="{row}">
            {{ $moment(row.usercheckTime).format('MM-DD HH:mm') }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="timeResultCn" label="打卡结果" width="80">
          <template #default="{row}">
            <span>{{ row.timeResultCn }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="locationResultCn" label="定位结果" width="80">
          <template #default="{row}">
            <span>{{ row.locationResultCn }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="sourceTypeCn" label="数据来源" width="80">
          <template #default="{row}">
            <span>{{ row.sourceTypeCn }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="locationMethod" label="定位方式" width="80">
          <template #default="{row}">
            <span>{{ row.locationMethod }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="userAddress" label="打卡地址" width="350">
          <template #default="{row}">
            <span>{{ row.userAddress }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="aprTitle" label="外勤备注" width="100">
          <template #default="{row}">
            <span>{{ row.outsideRemark }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="aprTitle" label="请假标题" width="100">
          <template #default="{row}">
            <span>{{ row.aprTitle }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="aprStatusCn" label="请求审批状态" width="100">
          <template #default="{row}">
            <span>{{ row.aprStatusCn }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="aprResultCn" label="请假审批结果" width="100">
          <template #default="{row}">
            <span>{{ row.aprResultCn }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          v-if="showPage"
          v-model:current-page="page.pageIndex"
          class="system-page"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.totalRecords"
          :page-size="page.pageSize"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { dingTalkPageList, synUserKq, syncProcInfos } from '@/api/dingtalk/kaoqing'
import { Refresh, RefreshLeft, Search } from "@element-plus/icons"
export default {
  components: { Refresh },
  computed: {
    Refresh() {
      return Refresh
    },
    RefreshLeft() {
      return RefreshLeft
    },
    Search() {
      return Search
    }
  },
  provide() {
    return {
      closeDialog: this.closeDialog,
      queryBtn: this.queryBtn
    }
  },
  data() {
    return {
      listLoading: false,
      showPage: true,
      pageSizes: { type: Array, default: [10, 20, 50, 100] },
      pageLayout: { type: String, default: "total, sizes, prev, pager, next, jumper" }, // 分页需要显示的东西，默认全部
      syncLoading: false,
      mainPage: true,
      downPage: false,
      dialogFlag: false,
      recruitId: '',
      rowIndex: '',
      rowObj: {},
      recruitDetailTabName: 'baseInfo',
      searchParam: {
        queryTimeResult: [],
        queryLocationResult: [],
        queryUserId: '',
        queryUserNo: '',
        queryUserName: '',
        datePicker: [this.$moment(new Date()).format('YYYY-MM-') + '01', this.$moment(new Date()).format('YYYY-MM-DD')],
        workDateFrom: this.$moment(new Date()).format('YYYY-MM-DD'),
        workDateTo: this.$moment(new Date()).format('YYYY-MM-DD')
      },
      searchParamRule: {
        datePicker: { type: 'array', required: true, message: '请选择起止日期', trigger: 'change' }
      },
      tableData: [],
      timeResultOptions: [
        { key: 'Normal', val: '正常' },
        { key: 'Early', val: '早退' },
        { key: 'Late', val: '迟到' },
        { key: 'SeriousLate', val: '严重迟到' },
        { key: 'Absenteeism', val: '旷工迟到' },
        { key: 'NotSigned', val: '未打卡' }
      ],
      locationResultOptions: [
        { key: 'Normal', val: '范围内' },
        { key: 'Outside', val: '范围外' },
        { key: 'NotSigned', val: '未打卡' }
      ],
      page: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecords: 0
      },
      timer: null
    }
  },
  async created() {
    this.getList()
  },
  methods: {
    // 分页相关：监听页码切换事件
    handleCurrentChange  (val)  {
      if (this.timer) {
        this.page.index = 1
      } else {
        this.page.index = val
        this.getList()
      }
    },
    // 分页相关：监听单页显示数量切换事件
    handleSizeChange(val){
      this.timer = 'work'
      setTimeout(() => {
        this.timer = null
      }, 100)
      this.page.pageSize = val
      this.getList()
    },
    // 查询
    queryBtn() {
      const $this = this
      $this.$refs['queryForm'].validate(async valid => {
        if (valid) {
          $this.page.pageIndex = 1
          $this.getList()
          $this.downPage = false
        }
      })
    },
    baseStatPassedChanged() {
      this.queryBtn()
    },
    async getList() {
      this.listLoading = true
      try {
        this.searchParam.workDateFrom = this.$moment(this.searchParam.datePicker[0]).format('YYYY-MM-DD')
        this.searchParam.workDateTo = this.$moment(this.searchParam.datePicker[1]).format('YYYY-MM-DD')
        const { data, page } = await dingTalkPageList(this.page, this.searchParam)
        console.info(data)
        this.tableData = data
        this.page = page
      } catch (e) {
        console.error(e)
        this.listLoading = false
        this.$message.warning(e)
      }
      this.listLoading = false
    },
    // 重置
    resetBtn() {
      // this.$refs.queryForm.resetFields()
      this.listLoading = false
      this.syncLoading = false
      this.searchParam = {
        queryTimeResult: [],
        queryLocationResult: [],
        datePicker: [this.$moment(new Date()).format('YYYY-MM-') + '01', this.$moment(new Date()).format('YYYY-MM-DD')],
        workDateFrom: this.$moment(new Date()).format('YYYY-MM-DD'),
        workDateTo: this.$moment(new Date()).format('YYYY-MM-DD')
      }
    },
    closeDialog() {
      this.dialogFlag = false
    },
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex
    },
    changeRowData(rowIdex, data) {
      this.tableData.splice(rowIdex, 1, data.data)
    },
    reloadTable(val) {
      if (val) {
        this.getList()
      }
    },
    // 刷新审批信息
    async refreshApr(index, row) {
      const $this = this
      console.info('刷新:' + index)
      // syncProcInfos
      const param = {
        queryProcInstId: row.procInstId
      }
      this.syncLoading = true
      await syncProcInfos(param).then(res => {
        $this.$message.success('更新成功!!')
        this.getList()
      }).catch(function(err) {
        $this.$message.warning(err)
      }).finally(() => {
        this.syncLoading = false
      })
    },
    // 刷新查询表单条件下考勤
    async refreshSearchFormKaoQing() {
      const $this = this
      const param = {
        queryUserId: $this.searchParam.queryUserId,
        queryUserName: $this.searchParam.queryUserName,
        workDateFrom: $this.searchParam.workDateFrom,
        workDateTo: $this.searchParam.workDateTo
      }
      this.syncLoading = true
      await synUserKq(param).then(res => {
        $this.$message.success('更新成功')
        this.getList()
      }).catch(function(err) {
        $this.$message.warning(err)
      }).finally(() => {
        this.syncLoading = false
      })
      this.syncLoading = false
    },
    // 刷新查询表单条件下的审批数据
    async refreshSearchFormApr() {
      const $this = this
      const param = {
        queryUserId: $this.searchParam.queryUserId,
        queryUserName: $this.searchParam.queryUserName,
        workDateFrom: $this.searchParam.workDateFrom,
        workDateTo: $this.searchParam.workDateTo
      }
      this.syncLoading = true
      await syncProcInfos(param).then(res => {
        $this.$message.success('更新成功!!')
        this.getList()
      }).catch(function(err) {
        $this.$message.warning(err)
      }).finally(() => {
        this.syncLoading = false
      })
    },
    // 下载
    download() {

    }
  }
}
</script>
<style scoped>
  .el-col > div, .el-col input {
    width: 98%;
  }
  .el-col .el-input {
    width: 98%;
  }
  .el-select__tags {
    white-space: nowrap;
    overflow: hidden;
  }
</style>
