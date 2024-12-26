import axios from 'axios'
import { useUserStore } from '../stores/user'
import router from '../router'
import { ElMessage } from 'element-plus'

const instance = axios.create({
  baseURL: 'http://localhost:8085',
  timeout: 5000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
})

instance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const token = userStore.token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    console.log('Request config:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    })
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  (response) => {
    console.log('Response:', response)
    return response
  },
  (error) => {
    console.error('Error full details:', error)
    console.error('Error config:', error.config)
    console.error('Error message:', error.message)
    
    if (error.response) {
      console.error('Error response:', {
        status: error.response.status,
        data: error.response.data,
        headers: error.response.headers
      })
      
      switch (error.response.status) {
        case 400:
          ElMessage.error(error.response.data || '请求参数错误')
          break
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          const userStore = useUserStore()
          userStore.logout()
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(error.response.data || '服务器错误')
          break
        default:
          ElMessage.error(error.response.data || '未知错误')
      }
    } else if (error.request) {
      console.error('No response received:', error.request)
      ElMessage.error('服务器无响应，请检查后端服务是否正常运行')
    } else {
      console.error('Error details:', error)
      ElMessage.error('请求发送失败')
    }
    
    return Promise.reject(error)
  }
)

export default instance 