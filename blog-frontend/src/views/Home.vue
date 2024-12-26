<template>
  <div class="home-container">
    <div class="actions" v-if="userStore.isLoggedIn">
      <el-button type="primary" @click="$router.push('/write')">写文章</el-button>
    </div>

    <div class="articles">
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>

      <div v-else-if="error" class="error">
        {{ error }}
      </div>

      <div v-else-if="articleStore.articles.length === 0" class="no-articles">
        暂无文章
      </div>
      
      <template v-else>
        <el-card v-for="article in articleStore.articles" :key="article.id" class="article-card">
          <template #header>
            <div class="article-header">
              <h2 class="article-title">{{ article.title }}</h2>
            </div>
          </template>
          
          <div class="article-meta">
            <span class="author">作者：{{ article.author?.username || '未知' }}</span>
            <span class="time">发布时间：{{ formatDate(article.createdAt) }}</span>
          </div>
          
          <div class="article-content">
            {{ formatContent(article.content) }}
          </div>

          <div class="article-actions" v-if="userStore.isLoggedIn && article.author?.id === userStore.user?.id">
            <el-button type="primary" link @click="editArticle(article.id)">编辑</el-button>
            <el-button type="danger" link @click="confirmDelete(article.id)">删除</el-button>
          </div>
        </el-card>

        <div class="pagination" v-if="articleStore.totalPages > 1">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="10"
            layout="prev, pager, next"
            :total="articleStore.totalPages * 10"
            @current-change="handlePageChange"
            background
          />
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useArticleStore } from '../stores/article'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const articleStore = useArticleStore()
const userStore = useUserStore()
const currentPage = ref(1)
const loading = ref(false)
const error = ref(null)

const formatDate = (dateString) => {
  if (!dateString) return '未知时间'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatContent = (content) => {
  if (!content) return ''
  return content.length > 200 ? content.substring(0, 200) + '...' : content
}

const handlePageChange = async (page) => {
  try {
    loading.value = true
    error.value = null
    await articleStore.getAllArticles(page - 1)
  } catch (err) {
    error.value = '加载失败，请重试'
    ElMessage.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

const editArticle = (id) => {
  router.push(`/edit/${id}`)
}

const confirmDelete = (id) => {
  ElMessageBox.confirm('确定要删除这篇文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await articleStore.deleteArticle(id)
      await loadArticles()
      ElMessage.success('删除成功')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    }
  })
}

const loadArticles = async () => {
  try {
    loading.value = true
    error.value = null
    await articleStore.getAllArticles()
  } catch (err) {
    console.error('Load articles error:', err)
    error.value = '加载失败，请重试'
    ElMessage.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.home-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.actions {
  margin-bottom: 20px;
  text-align: right;
}

.article-card {
  margin-bottom: 20px;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-title {
  margin: 0;
  font-size: 1.5em;
  color: #303133;
}

.article-meta {
  margin: 10px 0;
  font-size: 14px;
  color: #909399;
}

.article-meta span {
  margin-right: 15px;
}

.article-content {
  margin: 15px 0;
  line-height: 1.6;
  color: #606266;
  white-space: pre-wrap;
}

.article-actions {
  margin-top: 15px;
  text-align: right;
}

.no-articles {
  text-align: center;
  color: #909399;
  padding: 40px 0;
  font-size: 16px;
}

.loading, .error {
  padding: 20px;
  text-align: center;
}

.error {
  color: #f56c6c;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}

.time {
  margin-left: 15px;
  color: #909399;
}

.author {
  color: #409EFF;
}
</style> 