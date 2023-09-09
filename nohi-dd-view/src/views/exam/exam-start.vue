<template>
  <div class="layout-container">
    <el-row justify="center">
      <el-button type="primary" @click="gotoHome()" round>主页</el-button>
    </el-row>
    <div>试卷: {{ shiJuan.sjTitle }}</div>
    <div>进度：{{ shiTi_index }}/{{ shiJuan.stTotal }} &nbsp;&nbsp;&nbsp;&nbsp; 得分：{{ score }}</div>
    <hr />
    <el-row>
      <span class="st_title">{{ shiTi.stTitle }}</span>
    </el-row>
    <el-row>
      <div class="st_detail">{{ shiTi.stDetail }}</div>
    </el-row>
    <el-row>
      <!--      <el-checkbox-group v-if="shiTi.stOptionType == 'checkbox'" v-model="userCheckOpetion">-->
      <!--        <el-checkbox v-for="(item,index) in shiTi.stOptions" :key="index" :label="item.opValue" :value="item.opKey"></el-checkbox>-->
      <!--      </el-checkbox-group>-->
      <el-radio-group v-if="shiTi.stOptionType == 'select'" v-model="userRadioOpetion">
        <el-radio
          v-for="(item, index) in shiTi.stOptions"
          :key="index"
          :label="item.opKey"
        >{{ item.opKey }} ：{{ item.opValue }}</el-radio>
      </el-radio-group>
    </el-row>
    <el-row>
      <div class="st_result" v-show="shoResult">
        <div :class="result">{{ resultMsg }}</div>
      </div>
    </el-row>
    <el-row justify="center">
      <el-button type="primary" @click="pre" round>上一题</el-button>
      <el-button type="primary" @click="next" round>下一题</el-button>
    </el-row>
  </div>
</template>

<script lang="ts">
import { onMounted, watch, reactive, toRefs, getCurrentInstance } from 'vue'
import { onBeforeRouteLeave, useRoute, useRouter } from "vue-router"
import { ElMessage } from 'element-plus'

export default {
  name: "Exam-Start",
  props: {},
  data: function () {
    return {}
  },
  setup() {
    // 获取路由器实例
    const router = useRouter()
    console.info('start .st')
    const route = useRoute()
    let { sj_no } = route.query
    console.info(sj_no)
    if (!sj_no) {
      sj_no = 'sj_01'
    }
    const shiJuanList = getCurrentInstance()?.appContext.config.globalProperties.$shijuan
    const shiTiList = getCurrentInstance()?.appContext.config.globalProperties.$shiti
    console.log(shiJuanList)
    console.log(shiTiList)

    // const {appContext: {config: {globalProperties}}} = getCurrentInstance()
    // console.log(globalProperties.$shijuan)

    const data = reactive({
      shoResult: false,
      result: 'suc',
      resultMsg: '',
      shiJuanList: shiJuanList,
      shiTiList: shiTiList,
      shiJuan_no: 'sj_01',
      shiJuan: {},
      shiTi: {},
      shiTi_index: 1,
      userRadioOpetion: '',
      userCheckOpetion: [],
      resultTotal: [],
      score: 0
    })
    data.shiJuan_no = sj_no

    // 获取试卷
    const getShijuan = (sj_no) => {
      data.shiJuan = {}
      if (!data.shiJuanList) {
        console.info('试卷列表为空')
        return
      }
      const shiJuanList = data.shiJuanList
      for (let i = 0; i < shiJuanList.length; i++) {
        const item = shiJuanList[i]
        if (item.sjNo == sj_no) {
          data.shiJuan = item
          break
        }
      }
    }
    const getShiTi = (sj_no, st_index) => {
      data.shiTi = {}
      if (!data.shiTiList) {
        console.info('试题列表为空')
        return
      }
      const shiTiList = data.shiTiList
      for (let i = 0; i < shiTiList.length; i++) {
        const item = shiTiList[i]
        if (item.sjNo == sj_no && item.stIndex == st_index) {
          console.info(item.stOptions)
          data.shiTi = item
          break
        }
      }
      data.shiTi_index = st_index
    }
    onMounted(() => {
      console.log("3-组件挂载到页面之后执行-----onMounted()")
      getShijuan(data.shiJuan_no)
      getShiTi(data.shiJuan_no, data.shiTi_index)
    })

    watch(
      () => data.userRadioOpetion,
      () => {
        console.log('userRadioOpetion:' + data.userRadioOpetion + ',data.shiTi.stAnswer:' + data.shiTi.stAnswer)
        if (!data.userRadioOpetion) {
          return
        }
        if (data.userRadioOpetion == data.shiTi.stAnswer) {
          data.resultMsg = '答对了，正确答案:' + data.shiTi.stAnswer
          data.result = 'suc'
        } else {
          data.resultMsg = '答错了，正确答案:' + data.shiTi.stAnswer
          data.result = 'error'
        }
        data.shoResult = true

        // 保存结果
        saveResult()
      }
    )
    // 保存结果
    const saveResult = () => {
      let shiTiResult
      for (let i = 0; i < data.resultTotal.length; i++) {
        const item = data.resultTotal[i]
        if (item.shiTi_index == data.shiTi_index) {
          shiTiResult = item
          break
        }
      }
      if (!shiTiResult) {
        // 试题结果对象
        shiTiResult = {
          shiTi_index: data.shiTi_index,
          result: data.result,
          shiTi: data.shiTi
        }
        data.resultTotal.push(shiTiResult)
      } else {
        shiTiResult.result = data.result
      }
      // 计算得分
      calResult()
    }

    // 计算结果
    const calResult = () => {
      let score = 0
      for (let i = 0; i < data.resultTotal.length; i++) {
        const item = data.resultTotal[i]
        const shiTi = item.shiTi
        if (item.result == 'suc') {
          score += shiTi.stScore
        }
      }
      data.score = score
    }

    // 监管路由变化
    watch(
      () => route.query,
      (query) => {
        console.log(query)
      }
    )
    // 守卫
    onBeforeRouteLeave((to, from) => {
      console.info(to)
      console.info(from)
      const answer = window.confirm('是否确定要离开')
      if (!answer) {
        return false
      }
    })

    const clear = () => {
      data.shoResult = false
      data.result = ''
      data.resultMsg = ''
      data.userRadioOpetion = ''
    }
    console.info('============setup over===============')
    return {
      ...toRefs(data), getShiTi, getShijuan,
      start: function () {
        console.info('goto start page')
      },
      gotoHome() {
        router.push('/exam')
      },
      // 上一题
      pre() {
        clear()
        console.info('pre sj_no:' + data.shiJuan_no + ',st_index:' + data.shiTi_index)
        getShiTi(data.shiJuan_no, data.shiTi_index - 1)
      },
      // 下一题
      next() {
        if (!data.userRadioOpetion) {
          ElMessage.error('请选择一个答案')
          return
        }
        clear()
        console.info('next sj_no:' + data.shiJuan_no + ',st_index:' + data.shiTi_index)
        getShiTi(data.shiJuan_no, data.shiTi_index + 1)
      }
    }
  }
}
</script>
