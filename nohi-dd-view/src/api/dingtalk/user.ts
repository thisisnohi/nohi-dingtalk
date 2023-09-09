import requestTx from '@/utils/system/request-tx'

// 获取数据api
export function userList(data: object) {
    return requestTx({
      url: '/dd-mp/dtUser/lists',
      method: 'post',
      baseURL: '/',
      data
    })
  }
