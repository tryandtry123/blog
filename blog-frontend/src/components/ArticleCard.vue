<template>
  <el-card class="article-card">
    <template #header>
      <div class="article-header">
        <router-link :to="{ name: 'article-detail', params: { id: article.id }}">
          <h2>{{ article.title }}</h2>
        </router-link>
        <div class="article-meta">
          <span>{{ article.author.username }}</span>
          <el-divider direction="vertical" />
          <span>{{ formatDate(article.createdAt) }}</span>
        </div>
      </div>
    </template>
    <div class="article-content">
      {{ article.content.substring(0, 200) }}...
    </div>
  </el-card>
</template>

<script setup>
import { formatDate } from '@/utils/format'

defineProps({
  article: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.article-card {
  margin-bottom: 20px;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-header h2 {
  margin: 0;
  font-size: 1.5em;
}

.article-meta {
  color: var(--el-text-color-secondary);
  font-size: 0.9em;
}

.article-content {
  color: var(--el-text-color-regular);
  line-height: 1.6;
}

a {
  text-decoration: none;
  color: var(--el-text-color-primary);
}

a:hover {
  color: var(--el-color-primary);
}
</style> 