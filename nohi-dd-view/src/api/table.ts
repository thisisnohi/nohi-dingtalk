import request from '@/utils/system/request'
const baseURL: any = import.meta.env.VITE_BASE_API_URL
console.info('baseURL:' + baseURL)

// 获取数据api
export function getData(data: object) {
  return request({
    url: '/table/list',
    method: 'post',
    baseURL: baseURL,
    data
  })
}

// 获取分类数据
export function getCategory(data: object) {
  return request({
    url: '/table/category',
    method: 'post',
    baseURL: '/mock',
    data
  })
}

// 获取树组织数据
export function getTree(data: object) {
  return request({
    url: '/table/tree',
    method: 'post',
    baseURL: '/mock',
    data
  })
}

// 新增
export function add(data: object) {
  return request({
    url: '/table/add',
    method: 'post',
    baseURL: '/mock',
    data
  })
}

// 编辑
export function update(data: object) {
  return request({
    url: '/table/update',
    method: 'post',
    baseURL: '/mock',
    data
  })
}

// 删除
export function del(data: object) {
  return request({
    url: '/table/del',
    method: 'post',
    baseURL: '/mock',
    data
  })
}