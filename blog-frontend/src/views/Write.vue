<template>
  <div class="write-container">
    <el-form :model="articleForm" :rules="rules" ref="articleFormRef">
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
        <el-button type="primary" @click="submitArticle" :loading="loading">
          发布文章
        </el-button>
        <el-button @click="$router.push('/')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useArticleStore } from '../stores/article'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const articleStore = useArticleStore()
const userStore = useUserStore()
const articleFormRef = ref(null)
const loading = ref(false)

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

onMounted(() => {
  // 检查用户是否登录
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
})

const submitArticle = async () => {
  if (!articleFormRef.value) return
  
  try {
    await articleFormRef.value.validate()
    loading.value = true
    
    // 检查用户是否登录
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    console.log('Submitting article:', articleForm.value)
    const result = await articleStore.createArticle(articleForm.value)
    console.log('Article creation result:', result)
    ElMessage.success('文章发布成功')
    router.push('/')
  } catch (error) {
    console.error('Submit article error:', error)
    if (error.response) {
      ElMessage.error(error.response.data || '发布失败，请重试')
    } else if (error.request) {
      ElMessage.error('服务器无响应，请检查网络连接')
    } else {
      ElMessage.error('发布失败：' + error.message)
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.write-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
}
</style> 