import axios , { AxiosError, AxiosRequestConfig, AxiosResponse, AxiosInstance } from 'axios'
import store from '@/store'
import { ElMessage } from 'element-plus'
const baseURL: any = import.meta.env.VITE_BASE_URL

const createDownload = function(blob:any, name:string) {
  const url = window.URL.createObjectURL(new Blob([blob]))
  const $a = document.createElement('a')
  $a.style.display = 'none'
  $a.href = url
  $a.setAttribute('download', name)
  $a.click()
}

const handleBlob = function(blob : any) {
  return new Promise((resolve : Function, reject) => {
    const fileReader = new FileReader()
    fileReader.readAsText(blob)
    fileReader.onload = function(data: any) {
      try {
        const result = JSON.parse(data)
        if (result.flag && result.flag !== 0) {
          reject(result)
        } else {
          resolve()
        }
      } catch (e) {
        // download goes here
        resolve()
      }
    }
  })
}

const service: AxiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 5000
})

// 请求前的统一处理
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // JWT鉴权处理
    if (store.getters['user/token']) {
      config.headers['token'] = store.state.user.token
    }
    return config
  },
  (error: AxiosError) => {
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    if (res instanceof Blob) {
      let filename = 'download'
      for (const key in response.headers) {
        const header = response.headers[key]
        if (header) {
          const header_arr = header.toLowerCase().split('filename=')
          if (header_arr.length > 1) {
            filename = header_arr[1]
            filename = decodeURI(filename)
            break
          }
        }
      }
      return handleBlob(response.data).then((result) => {
        createDownload(res, filename)
      }).catch((result) => {
        return Promise.reject(new Error(result.msg))
      })
    }

    // 返回 0-成功
    if (res.resCode === "0") {
      return res
    } else {
      console.error('res.resCode:' + res.resCode)
      showError(res)
      return Promise.reject(res)
    }
  },
  (error: AxiosError)=> {
    console.log(error) // for debug
    const badMessage: any = error.message || error
    const code = parseInt(badMessage.toString().replace('Error: Request failed with status code ', ''))
    showError({ code, message: badMessage })
    return Promise.reject(error)
  }
)

// 错误处理
function showError(error: any) {
  // token过期，清除本地数据，并跳转至登录页面
  if (error.code === 403) {
    // to re-login
    store.dispatch('user/loginOut')
  } else {
    ElMessage({
      message: error.msg || error.message || '服务异常',
      type: 'error',
      duration: 3 * 1000
    })
  }

}

export default service
