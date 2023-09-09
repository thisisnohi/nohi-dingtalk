<!--suppress ALL -->
<script setup lang="ts">
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons";
import { dtDetpLists } from "@/api/dingtalk/dept";

let loading = ref(false)

// 部门信息
interface DeptInfo {
  id: string
  dtDeptId: string
  dtDeptName: string
  enDeptNo: string
  dtParDeptNo: string
  dtParDeptName: string
  deptUserNum: number
}

const tableData = ref([]);
const tableRowClassName = ({
                             row,
                             rowIndex,
                           }: {
  row: DeptInfo
  rowIndex: number
}) => {
  if (rowIndex === 1) {
    return 'warning-row'
  } else if (rowIndex === 3) {
    return 'success-row'
  }
  return ''
}

// 查询条件
const query = reactive({
  deptId: '',
  deptName: ''
})

// 分页信息
const pageInfo = reactive({
  totalRecords: 0,
  totalPages: 0,
  pageSize: 10,
  pageIndex: 0,
});

const getTableData = (init: boolean) => {
  loading.value = true
  if (init) {
    pageInfo.pageIndex = 1
  }
  let params = {
    page: pageInfo,
    data: query
  }
  dtDetpLists(params)
      .then((res) => {
        console.log(res)
        tableData.value = res.data
        console.info(tableData)
        pageInfo.totalRecords = Number(res.page.totalRecords)
        pageInfo.pageSize = Number(res.page.pageSize)
        pageInfo.pageIndex = Number(res.page.pageIndex)
        pageInfo.totalPages = Number(res.page.totalPages)
      })
      .catch((error) => {
        tableData.value = []
        pageInfo.pageIndex = 1
        pageInfo.totalRecords = 0
      })
      .finally(() => {
        loading.value = false
      })
  // dtDetpLists
};

</script>

<template>
  <div class="layout-container">
    <div class="layout-container-form flex space-between" v-loading="loading">
      <div class="layout-container-form-handle">
        <div class="layout-container-form-search">
          <el-form :inline="true" :model="query" class="demo-form-inline">
            <el-form-item label="部门编号">
              <el-input v-model="query.deptId" placeholder="部门编号" clearable/>
            </el-form-item>
            <el-form-item label="部门名称">
              <el-input v-model="query.deptName" placeholder="部门名称" clearable/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="layout-container-form-search">
        <el-button type="primary" :icon="Search" @click="getTableData()">查询</el-button>
      </div>
    </div>
    <div class="layout-container-table">
      <el-table
          :data="tableData"
          style="width: 100%"
          stripe
      >
        <el-table-column prop="dtDeptId" label="部门ID" align="center"  style="width: 20%"/>
        <el-table-column prop="dtDeptName" label="部门名称" align="center" style="width: 20%"/>
        <el-table-column prop="dtParDeptId" label="父部门ID" align="center"  style="width: 20%"/>
        <el-table-column prop="dtParDeptName" label="父部门名称" align="center" style="width: 20%"/>
        <el-table-column prop="deptUserNum" label="员工数" align="center" style="width: 20%"/>
      </el-table>
      <el-pagination background layout="prev, pager, next" :total="pageInfo.totalRecords"/>
    </div>
  </div>
</template>

<style>
.el-table .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

.el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>
