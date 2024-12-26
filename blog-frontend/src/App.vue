<template>
  <el-container class="layout-container">
    <el-header height="60px">
      <div class="nav-container">
        <div class="logo">
          <router-link to="/">博客系统</router-link>
        </div>
        
        <div class="nav-menu">
          <el-menu mode="horizontal" router>
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/articles">文章列表</el-menu-item>
          </el-menu>
        </div>
        
        <div class="nav-right">
          <template v-if="userStore.isLoggedIn">
            <el-button type="primary" @click="$router.push('/write')">写文章</el-button>
            <el-dropdown @command="handleCommand">
              <el-avatar :size="32">
                {{ userStore.user?.username?.charAt(0) }}
              </el-avatar>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button @click="$router.push('/login')">登录</el-button>
            <el-button type="primary" @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    
    <el-container class="main-container">
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  height: 100%;
}

.layout-container {
  height: 100%;
}

.main-container {
  height: calc(100% - 60px);
  background-color: var(--el-bg-color-page);
}

.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo {
  font-size: 1.5em;
  font-weight: bold;
}

.logo a {
  color: var(--el-text-color-primary);
  text-decoration: none;
}

.nav-menu {
  flex: 1;
  margin: 0 20px;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.el-header {
  background-color: white;
  border-bottom: 1px solid var(--el-border-color-light);
  padding: 0 20px;
}
</style>
