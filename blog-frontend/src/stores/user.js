import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from '../utils/axios'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))

  const register = async (userData) => {
    try {
      const { confirmPassword, ...registerData } = userData
      const { data } = await axios.post('/api/auth/register', registerData)
      return data
    } catch (error) {
      console.error('Registration failed:', error)
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message)
      }
      throw error
    }
  }

  const login = async (credentials) => {
    try {
      const { data } = await axios.post('/api/auth/login', credentials)
      token.value = data.token
      user.value = data.user
      localStorage.setItem('token', data.token)
      return data
    } catch (error) {
      console.error('Login failed:', error)
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message)
      }
      throw error
    }
  }

  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  const isLoggedIn = computed(() => !!token.value)

  return {
    user,
    token,
    register,
    login,
    logout,
    isLoggedIn
  }
}) 