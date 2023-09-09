import * as ElementPlusIconsVue from '@element-plus/icons-vue'

/**
 * 初始化 icon
 * @param app
 */
export function initIcon(app: any) {
  console.info('====initIcon=====')
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    console.info('regIcon:' + key)
    app.component(key, component)
  }
}

/**
 * 初始化 icon
 * @param app
 */
// export function initElementPlus(app: any) {
//
// }
