<template>
  <div class="layout-container">
    <el-row justify="center">
      <img alt="Vue logo" src="@/assets/logo.png" />
      <img alt="Vue logo" src="/assets/logo.png" />
      <img alt="Vue logo" src="./assets/logo.png" />
      <img alt="Vue logo" src="https://alyecs223.xnzn.net/zhst-h5tp-in/lnyst/225116216526639104/cust/c6261432025148659433d8762cbc8792.jpg" />
	  
    </el-row>
    <h1>Welcome to Your App</h1>
    <el-row justify="center">
      <el-button type="primary" @click="start('sj_01')" round>试卷一</el-button>
      <el-button type="primary" @click="start('sj_02')" round>试卷二</el-button>
    </el-row>
  </div>
</template>

<script lang="ts">
import { onBeforeRouteLeave, useRoute, useRouter } from "vue-router"

export default {
  name: "Exam-Index",
  props: {
    msg: String
  },
  setup() {
    // 获取路由器实例
    const router = useRouter()
    // route是响应式对象,可监控器变化
    const route = useRoute()
    let { id } = route.query
    console.info(id)

    // 守卫
    onBeforeRouteLeave((to, from) => {
      console.info(to)
      console.info(from)
      // const answer = window.confirm('是否确定要离开')
      // if (!answer) {
      //   return false
      // }
    })

    // 开始
    const start = (sj_no: string) => {
      console.info('goto start page:' + sj_no)
      router.push({
        path: '/exam/exam-start',
        query: {
          msg: 'from start',
          sj_no: sj_no
        }
      })

    }
    return {
      start,
      backToDash() {
        console.log(route.query)
        router.push('/')
      },
    }
  }
}
</script>
