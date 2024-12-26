<template>
  <div class="edit-container">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="3" animated />
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <el-form v-else :model="articleForm" :rules="rules" ref="articleFormRef">
      <el-form-item prop="title">
        <el-input
          v-model="articleForm.title"
          placeholder="请输入文章标题"
          size="large"
          clearable
        />
      </el-form-item>
      
      <el-form-item prop="content">
        <el-input
          v-model="articleForm.content"
          type="textarea"
          :rows="15"
          placeholder="请输入文章内容"
          resize="none"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitArticle" :loading="submitting">
          保存修改
        </el-button>
        <el-button @click="$router.push('/')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useArticleStore } from '../stores/article'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const articleStore = useArticleStore()
const userStore = useUserStore()
const articleFormRef = ref(null)
const loading = ref(false)
const submitting = ref(false)
const error = ref(null)

const articleForm = ref({
  title: '',
  content: ''
})

const rules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在2到100个字符之间', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ]
}

const loadArticle = async () => {
  const id = route.params.id
  if (!id) {
    error.value = '文章ID不存在'
    return
  }

  try {
    loading.value = true
    const article = await articleStore.getArticle(id)
    
    // 检查是否是文章作者
    if (article.author.id !== userStore.user?.id) {
      ElMessage.error('没有权限编辑此文章')
      router.push('/')
      return
    }

    articleForm.value.title = article.title
    articleForm.value.content = article.content
  } catch (err) {
    console.error('Load article error:', err)
    error.value = '加载文章失败'
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

const submitArticle = async () => {
  if (!articleFormRef.value) return
  
  try {
    await articleFormRef.value.validate()
    submitting.value = true
    
    // 检查用户是否登录
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    const id = route.params.id
    await articleStore.updateArticle(id, articleForm.value)
    ElMessage.success('文章更新成功')
    router.push('/')
  } catch (error) {
    console.error('Update article error:', error)
    if (error.response) {
      ElMessage.error(error.response.data || '更新失败，请重试')
    } else if (error.request) {
      ElMessage.error('服务器无响应，请检查网络连接')
    } else {
      ElMessage.error('更新失败：' + error.message)
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  // 检查用户是否登录
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  loadArticle()
})
</script>

<style scoped>
.edit-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
}

.loading, .error {
  padding: 20px;
  text-align: center;
}

.error {
  color: #f56c6c;
}
</style> 