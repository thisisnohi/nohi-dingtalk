import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import { baidu } from './utils/system/statistics'
import 'element-plus/theme-chalk/display.css' // 引入基于断点的隐藏类
import 'element-plus/dist/index.css'
import 'normalize.css' // css初始化
import './assets/style/common.scss' // 公共css
import './theme/modules/chinese/index.scss'
import App from './App.vue'
import store from './store'
import router from './router'
import { getAuthRoutes } from './router/permission'
import i18n from './locale'
import moment from 'moment'
import { initDict } from "./api/dicts";
import { initIcon } from "./init";
import print from 'vue3-print-nb';

// 试卷、试题
import { shijuan } from "../mock/exam/shijuan";
import { shiti } from "../mock/exam/shiti";

if (import.meta.env.MODE !== "development") {
  // 非开发环境调用百度统计
  baidu();
}

/** 权限路由处理主方法 */
getAuthRoutes().then(() => {
  const app = createApp(App)
  app.use(ElementPlus, {size: store.state.app.elementSize})
  app.use(store)
  app.use(router)
  app.use(i18n)
  app.use(print);
  // app.config.performance = true
  app.mount('#app')

// 注册moment
  app.config.globalProperties.$moment = moment
// 初始化数据字典
// @ts-ignore
  initDict(app);
  initIcon(app);

  const unuser = (obj: any) => {
    if (typeof obj == "undefined") {
      console.info("undifine");
    }
  }

  const getShijuan = new Promise((resolve, reject) => {
    unuser(reject);
    // createApp(App).config.globalProperties.$shijuan = shijuan
    app.config.globalProperties.$shijuan = shijuan;
    resolve(true);
  });
  const getShiti = new Promise((resolve, reject) => {
    unuser(reject);
    // createApp(App).config.globalProperties.$shiti = shiti
    app.config.globalProperties.$shiti = shiti;
    resolve(true);
  });
  unuser(getShijuan);
  unuser(getShiti);
})
