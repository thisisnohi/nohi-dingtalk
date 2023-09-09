import requestTx from '@/utils/system/request-tx'

// 获取数据api
export function dtDetpLists(data: object) {
    return requestTx({
      url: '/dtDetp/lists',
      method: 'post',
      data
    })
  }

export function getDeptTree(data: object) {
  return requestTx({
    url: '/dtDetp/tree',
    method: 'post',
    data
  })
}
