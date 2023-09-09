<template>
  <div class="layout-container">
    <div class="layout-container-form flex space-between" v-loading="loading">
      <div class="layout-container-form-handle">
        <div class="layout-container-form-search">
          <el-form
              ref="queryForm"
              :model="searchParam"
              :rules="searchParamRule"
              :inline="true"
              label-width="120px"
              @keyup.enter.native="queryBtn"
          >
            <el-form-item prop="queryUserNo">
              <el-input v-model="searchParam.queryUserNo" placeholder="员工号" style="width: 98%;"/>
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
          </el-form>
        </div>
      </div>
      <div class="layout-container-form-search">
        <el-button type="primary" :icon="Search" :loading="listLoading" @click="queryBtn">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetBtn">重置</el-button>
      </div>
    </div>
    <div class="layout-container-table">
      <el-table :data="tableData" style="width: 100%" highlight-current-row border>
        <el-table-column align="center" prop="date" label="序号" width="100">
          <template #default="row">
            <span>{{ row.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="projectNo" label="项目名" width="150">
          <template #default="{row}">
            <span>{{ row.projectNo }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="projectName" label="项目名" width="150">
          <template #default="{row}">
            <span>{{ row.projectName }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="empNo" label="员工号" width="80">
          <template #default="{row}">
            <span>{{ row.empNo }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="empName" label="员工名" width="80">
          <template #default="{row}">
            <span>{{ row.empName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(item) in tableColumns"
            :key="item.id"
            :prop="item.prop"
            :label="item.label"
            :title="item.title"
            align="center"
            width="60"
            show-overflow-tooltip
        />
      </el-table>
    </div>
  </div>
</template>

<script>
import { empWorkSheet } from '@/api/dingtalk/kaoqing'
import moment from 'moment'
import { Search } from "@element-plus/icons";

export default {
  computed: {
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
  components: {},
  data() {
    return {
      loading: false,
      listLoading: false,
      dialogFlag: false,
      searchParam: {
        queryUserNo: '',
        queryUserName: '',
        datePicker: [moment(new Date()).format('YYYY-MM-DD'), moment(new Date()).format('YYYY-MM-DD')],
        workDateFrom: moment(new Date()).format('YYYY-MM-DD'),
        workDateTo: moment(new Date()).format('YYYY-MM-DD')
      },
      searchParamRule: {
        datePicker: { type: 'array', required: true, message: '请选择起始日期', trigger: 'change' }
      },
      tableData: [],
      tableColumns: [],
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
      ]
    }
  },
  watch: {
    'searchParam.datePicker': function(val) {
      console.info(val)
    },
    'searchParam.workDateFrom': function(val) {
      console.info(val)
    },
    'searchParam.workDateTo': function(val) {
      console.info(val)
    }
  },
  methods: {
    // 过滤项目
    filterTag(value, row) {
      return row.projectNo === value
    },
    // 根据日期区间更新表头
    initTableColumns: function() {
      console.info('initTableColumns....')
      const $this = this
      $this.tableColumns = []
      let start = this.searchParam.datePicker[0]
      const end = this.searchParam.datePicker[1]
      while (start <= end) {
        $this.tableColumns.push({
          id: moment(start).format('YYYY-MM-DD'),
          prop: moment(start).format('YYYY-MM-DD'),
          label: moment(start).format('M.D'),
          title: moment(start).format('YYYY-MM-DD') + '_title'
        })
        start = moment(start).add(1, 'days')
      }
      console.info($this.tableColumns)
    },
    // 查询
    queryBtn() {
      const $this = this
      $this.$refs['queryForm'].validate(async valid => {
        if (valid) {
          $this.initTableColumns()
          $this.getList()
        }
      })
    },
    baseStatPassedChanged() {
      this.queryBtn()
    },
    async getList() {
      console.info(this.searchParam)
      console.info('datePicker:' + this.searchParam.datePicker)
      this.listLoading = true
      try {
        this.searchParam.workDateFrom = moment(this.searchParam.datePicker[0]).format('YYYY-MM-DD')
        this.searchParam.workDateTo = moment(this.searchParam.datePicker[1]).format('YYYY-MM-DD')
        const { data } = await empWorkSheet(this.searchParam)
        this.tableData = data
      } catch (e) {
        this.$message.warning(e)
      }
      this.listLoading = false
    },
    // 重置
    resetBtn() {
      // this.$refs.queryForm.resetFields()
      this.listLoading = false
      this.searchParam = {
        queryTimeResult: [],
        queryLocationResult: [],
        workDateFrom: moment(new Date()).format('YYYY-MM-DD'),
        workDateTo: moment(new Date()).format('YYYY-MM-DD')
      }
      this.datePicker = [moment(new Date()).format('YYYY-MM-DD'), moment(new Date()).format('YYYY-MM-DD')]
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
