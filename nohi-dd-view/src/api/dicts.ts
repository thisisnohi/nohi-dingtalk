const DICT_Y_N = [
  {value: 'Y', label: '是'},
  {value: 'N', label: '否'}
]

const dictData : any =
  {'DICT_Y_N': DICT_Y_N
  }

// 获取数据api
export function initDict(app : any) {
  app.config.globalProperties.$dict = {
    label<T> (type: any, value:T, defaultLabel: T) {
      // console.info('type:' + type + ',value:' + value + ',defaultLabel:' + defaultLabel)
      const dicts = dictData[type];
      if (!dicts) {
        return defaultLabel ? '' : defaultLabel
      }

      if (dicts) {
        for(let item of dicts){
          if (item.value == value) {
            return item.label
          }
        }
      }
      return defaultLabel ? '' : defaultLabel
    }
  }
}
