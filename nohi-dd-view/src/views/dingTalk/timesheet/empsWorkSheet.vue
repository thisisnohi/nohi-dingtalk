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
            <el-form-item>
              <el-select v-model="searchParam.queryDeptIds" clearable placeholder="部门" multiple style="width: 98%;">
                <el-option v-for="item in deptOptions" :key="item.key" :label="item.val" :value="item.key" />
              </el-select>
            </el-form-item>
            <el-form-item prop="queryUserNo">
              <el-input v-model="searchParam.queryUserNo" placeholder="员工号/姓名" style="width: 98%;" />
            </el-form-item>
            <el-form-item prop="queryUserNo">
              <el-input v-model="searchParam.queryProjectId" placeholder="项目号/项目名" style="width: 98%;" />
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
            <el-row>
              <el-col :span="16">

              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
      <div class="layout-container-form-search">
        <el-button type="primary" icon="Search" :loading="listLoading" @click="queryBtn">查询</el-button>
        <el-button icon="RefreshLeft" @click="resetBtn">重置</el-button>
        <el-button type="primary" icon="Download" :loading="listLoading" @click="exportSheet">导出</el-button>
      </div>
    </div>
    <div class="layout-container-table">
      <el-table :data="tableData" style="width: 100%" stripe highlight-current-row border>
        <el-table-column align="center" prop="date" label="序号" width="40">
          <template #default="row">
            <span>{{ row.$index+1 }}</span>
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
        >
          <template #default="{row}">
            <el-popover effect="light" trigger="hover" placement="top" width="auto">
              <template #default>
                <p>[{{ row.empName }}][{{ item.id }}]</p>
                <p>{{ showTableTitle(row.dayMap[item.id], item.id) }}</p>
              </template>
              <template #reference>
                <span v-show="showWorkHadr">{{ showTableLabel(row.dayMap[item.id], item.id) }}</span>
              </template>
            </el-popover>

          </template>
        </el-table-column>
        <el-table-column align="center" prop="empName" label="合计" width="80">
          <template #default="{row}">
            <span>{{ row.total }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { empWorkSheetDetail, exportSheet } from '@/api/dingtalk/kaoqing'
import moment from 'moment'
import { Search } from "@element-plus/icons";
import { dtDetpLists } from "@/api/dingtalk/dept";

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
      // 显示工作量
      showWorkHadr: true,
      showDutyTime: true,
      loading: false,
      listLoading: false,
      dialogFlag: false,
      searchParam: {
        queryUserNo: '',
        queryUserName: '',
        datePicker: [moment(new Date()).format('YYYY-MM-') + '01', moment(new Date()).format('YYYY-MM-DD')],
        workDateFrom: moment(new Date()).format('YYYY-MM-DD'),
        workDateTo: moment(new Date()).format('YYYY-MM-DD')
      },
      searchParamRule: {
        datePicker: { type: 'array', required: true, message: '请选择起止日期', trigger: 'change' }
      },
      tableData: [],
      tableColumns: [],
      projectNoFilters: [],
      deptOptions: []
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
  mounted() {
    this.initDeptOption();
  },
  methods: {
    // 初始化部门列表
    initDeptOption() {
      this.deptOptions = []

      let params = {
        data: {}
      }
      dtDetpLists(params)
          .then((res) => {
            res.data.forEach( item => {
              this.deptOptions.push({
                key: item.dtDeptId,
                val: item.dtDeptName,
              })
            })
          })
          .catch((error) => {
          })
    },
    // 重置filter
    initProjectNoFilter(data) {
      // [{ text: '-', value: '-' }, { text: '21003318', value: '21003318' }]
      this.projectNoFilters = []
      const projectNo = []
      if (data && data.length > 0) {
        data.forEach(item => {
          if (projectNo.indexOf(item.projectNo) < 0) {
            projectNo.push('' + item.projectNo)
            this.projectNoFilters.push({ text: '' + item.projectName, value: '' + item.projectNo })
          }
        })
      }
    },
    // 根据日期区间更新表头
    initTableColumns: function() {
      console.info('initTableColumns....')
      const $this = this
      $this.tableColumns = []
      let start = moment(this.searchParam.datePicker[0]).format('YYYY-MM-DD')
      const end = moment(this.searchParam.datePicker[1]).format('YYYY-MM-DD')
      console.info('start:' + start + ',end:' + end)
      while (start <= end) {
        $this.tableColumns.push({
          id: moment(start).format('YYYY-MM-DD'),
          prop: moment(start).format('YYYY-MM-DD'),
          label: moment(start).format('M.D'),
          title: moment(start).format('YYYY-MM-DD') + '_title'
        })
        start = moment(start).add(1, 'days').format('YYYY-MM-DD')
      }
      console.info('start2:' + start + ',end2:' + end)
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
    // 显示单元格
    showTableLabel(item, dayStr) {
      if (!item || !dayStr) {
        return '';
      }
      if (item.workTotal) {
        return item.workTotal
      }
      return ''
    },
    // 显示title
    showTableTitle(item, dayStr) {
      if (!item || !dayStr) {
        return '';
      }
      if (item.title) {
        return item.title
      }
      return ''
    },
    // 导出工时
    exportSheet() {
      const $this = this
      $this.$refs['queryForm'].validate(async valid => {
        if (valid) {
          this.listLoading = true
          try {
            this.searchParam.workDateFrom = moment(this.searchParam.datePicker[0]).format('YYYY-MM-DD')
            this.searchParam.workDateTo = moment(this.searchParam.datePicker[1]).format('YYYY-MM-DD')
            await exportSheet(this.searchParam)
          } catch (e) {
            this.$message.warning(e)
          }
          this.listLoading = false
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
        const { data } = await empWorkSheetDetail(this.searchParam)
        this.tableData = data
        this.initProjectNoFilter(data)
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
        datePicker: [moment(new Date()).format('YYYY-MM-') + '01', moment(new Date()).format('YYYY-MM-DD')],
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
