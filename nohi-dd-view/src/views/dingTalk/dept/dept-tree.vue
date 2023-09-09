<template>
  <div class="category">
    <div class="header-box">
      <h2>部门</h2>
      <el-input
          v-model="deptName"
          placeholder="部门名称"
          @input="getTreeData(false)"
      ></el-input>
    </div>
    <div class="list system-scrollbar">
      <el-tree
          ref="deptTree"
          class="my-tree"
          :data="deptTreeData"
          :props="defaultProps"
          :expand-on-click-node="false"
          node-key="dtDeptId"
          highlight-current
          default-expand-all
          @node-click="handleNodeClick"
      ></el-tree>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { Ref } from "vue";
import { ref, inject, nextTick } from "vue";
import { getDeptTree } from "@/api/dingtalk/dept"

const deptName = ref('')
const dept: Ref<any|null> = ref(null)
const deptTreeData = ref([]);
const deptTree: Ref<any | null> = ref(null)
// 树结点属性
const defaultProps = {
  children: "children",
  label: "dtDeptName",
};
const active: any = inject("active");
const getTreeData = (init: boolean) => {
  let params = {};
  getDeptTree(params).then((res) => {
    deptTreeData.value = res.data;
    console.info(res.data)
    active.value = res.data[0];
    nextTick(() => {
      dept.value && dept.value.setCurrentKey(active.value.dtDeptId)
    })
  });
};
const handleNodeClick = (row: any) => {
  active.value = row;
};
getTreeData(true);


</script>

<style lang="scss" scoped>
.category {
  background: #fff;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  .header-box {
    padding: 10px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #eee;

    h2 {
      padding: 0;
      margin: 0;
      margin-right: 20px;
      font-size: 14px;
      display: -webkit-box;
      -webkit-line-clamp: 1;
      -webkit-box-orient: vertical;
      overflow: hidden;
      height: 30px;
      line-height: 30px;
    }

    .el-input {
      flex: 1;
    }
  }

  .list {
    flex: 1;
    overflow: auto;
  }

  .my-tree {
    :deep(.el-tree-node__content) {
      height: 36px;
    }

    :deep(.el-tree-node.is-current>.el-tree-node__content) {
      background-color: rgba(64, 158, 255, 0.4);
    }

    :deep(.el-tree-node>.el-tree-node__content) {
      transition: 0.2s;
    }
  }
}
</style>
