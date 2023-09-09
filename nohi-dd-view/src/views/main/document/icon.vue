<template>
  <div class="layout-container">
    <h1>icon</h1>
    <i class="sfont system-home">
      sfont system-home
    </i>
    <div style="width: 80%;margin: 10px auto;">
      <el-button :icon="Search">刷新审批数据</el-button>
      <el-icon style="vertical-align: middle">
        <Search />
      </el-icon>
    </div>
    <ul class="demo-icon-list">
      <li class="icon-item"  v-for="(key, index) in icons" @click="copy(key.iconName)">
        <span class="demo-svg-icon">
          <el-icon :size="size">
            <component :is="key.iconName" class="myIcon"></component>
          </el-icon>
          <span class="icon-name" >{{key.iconName}}</span>
        </span>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { Ref, ref } from 'vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { ElMessage } from "element-plus/es";
import { Search } from "@element-plus/icons";

const $this : any = this
interface IconType {
  iconName: String,
  component: Object
}

const icons: Ref<IconType[]> = ref([])
const type: Ref<any> = ref()
type.value = "primary"
const size = ref()
size.value = 20
const init = () => {
  let arry = []
  let index = 0
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    index++
    arry.push({
      iconName: key,
      component: component
    })
  }
  icons.value = arry
}
// 初始化
init()

const copy = (name : string) => {
  let url = name;
  let oInput = document.createElement('input');
  oInput.value = url;
  document.body.appendChild(oInput);
  oInput.select(); // 选择对象;
  document.execCommand("Copy"); // 执行浏览器复制命令
  ElMessage({
    message: '复制成功',
    type: 'success'
  })
  oInput.remove()
}

</script>
<style>
.demo-icon-list{
  width: 80%;
  margin: 0 auto;
  list-style: none;
  padding: 0!important;
  border-top: 1px solid ;
  border-left: 1px solid;
  border-radius: 4px;
  display: grid;
  grid-template-columns: repeat(7,1fr);
}
.demo-icon-list .icon-item {
  text-align: center;
  color: var(--el-text-color-regular);
  height: 90px;
  border-right: 1px solid;
  border-bottom: 1px solid;
  transition: background-color var(--el-transition-duration);
}
.demo-icon-list .icon-item .demo-svg-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  cursor: pointer;
}
.demo-icon-list .icon-item .demo-svg-icon .icon-name{
  margin-top: 8px;
  font-size: 13px;
}
.demo-icon-list .icon-item:hover {
  background-color: var(--el-border-color-extra-light);
  color: var(--system-header-breadcrumb-text-color);
}
</style>
