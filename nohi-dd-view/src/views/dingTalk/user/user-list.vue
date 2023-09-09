<template>
  <div class="layout-container">
    <div class="layout-container-form flex space-between">
      <div class="layout-container-form-handle">

      </div>
      <div class="layout-container-form-search">
        <el-input v-model="query.input" :placeholder="$t('message.common.searchTip')" @change="getTableData(true)"></el-input>
        <el-button :icon="Search" class="search-btn" @click="getTableData(true)">{{ $t('message.common.search') }}</el-button>
      </div>
    </div>
    <div class="layout-container-table">
      <el-table
          ref="table"
          :data="tableData"
          style="width: 100%"
          stripe
      >
        <el-table-column prop="dtUserid" label="员工标识" align="center" />
        <el-table-column prop="dtUsername" label="员工姓名" align="center" />
        <el-table-column prop="dtActive" label="是否激活" align="center" >
          <template #default="scope">
            {{ $dict.label('DICT_Y_N', scope.row.dtActive, '') }}
          </template>
        </el-table-column>
        <el-table-column prop="getPriInd" label="是否允许获取数据" align="center" >
          <template #default="row">
            {{ row.getPriInd = 'Y' ? '是' : '否' }}
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
      >
      </el-pagination>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { inject, reactive, ref, watch } from 'vue'
import { Page } from '@/api/page/types'
import { Search } from '@element-plus/icons'
import { userList } from '@/api/dingtalk/user'

// 是否显示分页
const showPage = ref(true)
// 分页参数, 供table使用
const page: Page = reactive({
  totalRecords: 0,
  totalPages: 0,
  pageSize: 10,
  pageIndex: 0,
})
const activeCategory: any = inject('active')
const loading = ref(true)
const tableData = ref([])
const chooseData = ref([])

// 存储搜索用的数据
const query = reactive({
  input: '',
  deptId: activeCategory.value.dtDeptId
})

const handleSelectionChange = (val: []) => {
  chooseData.value = val
}
// 获取表格数据
// params <init> Boolean ，默认为false，用于判断是否需要初始化分页
const getTableData = (init: boolean) => {
  loading.value = true
  if (init) {
    page.pageIndex = 1
  }
  query.deptId = activeCategory.value.dtDeptId
  let params = {
    page: page,
    data: query,
    category: activeCategory.value.id,
  }
  userList(params)
  .then( (res : any) => {
    tableData.value = res.data
    page.totalRecords = Number(res.page.totalRecords)
  })
  .catch(error => {
    tableData.value = []
    page.pageIndex = 1
    page.totalRecords = 0
  })
  .finally(() => {
    loading.value = false
  })
}

watch(activeCategory, (newVal) => {
  getTableData(true)
})
</script>

<style lang="scss" scoped>

</style>
