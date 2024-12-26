import { defineStore } from 'pinia'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

export const useArticleStore = defineStore('article', {
  state: () => ({
    articles: [],
    currentArticle: null,
    totalPages: 0,
    currentPage: 0,
    loading: false,
    error: null
  }),

  actions: {
    async createArticle(article) {
      this.loading = true
      this.error = null
      try {
        console.log('Sending article data:', article)
        const response = await axios.post('/api/articles', article)
        console.log('Create article response:', response.data)
        ElMessage.success('文章发布成功')
        return response.data
      } catch (error) {
        console.error('Create article error details:', error)
        this.error = error.response?.data || '发布失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async getArticle(id) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/articles/${id}`)
        this.currentArticle = response.data
        return response.data
      } catch (error) {
        console.error('Get article error:', error)
        this.error = error.response?.data || '获取文章失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async getAllArticles(page = 0) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/articles?page=${page}&size=10&sort=createdAt,desc`)
        this.articles = response.data.content
        this.totalPages = response.data.totalPages
        this.currentPage = response.data.number
        return response.data
      } catch (error) {
        console.error('Get all articles error:', error)
        this.error = error.response?.data || '获取文章列表失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async getUserArticles(username, page = 0) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/articles/user/${username}?page=${page}&size=10&sort=createdAt,desc`)
        this.articles = response.data.content
        this.totalPages = response.data.totalPages
        this.currentPage = response.data.number
        return response.data
      } catch (error) {
        console.error('Get user articles error:', error)
        this.error = error.response?.data || '获取用户文章失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateArticle(id, article) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/api/articles/${id}`, article)
        ElMessage.success('文章更新成功')
        return response.data
      } catch (error) {
        console.error('Update article error:', error)
        this.error = error.response?.data || '更新文章失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteArticle(id) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/api/articles/${id}`)
        ElMessage.success('文章删除成功')
      } catch (error) {
        console.error('Delete article error:', error)
        this.error = error.response?.data || '删除文章失败'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
}) 