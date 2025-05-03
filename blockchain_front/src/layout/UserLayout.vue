<template>
  <div class="user-layout">
    <!-- 头部导航 -->
    <header class="app-header">
      <div class="logo">区块链学历认证系统</div>
      <nav class="nav-bar">
        <router-link to="/user" class="nav-link">首页</router-link>
        <router-link to="/user/profile" class="nav-link">个人资料</router-link>
        <router-link to="/user/settings" class="nav-link">设置</router-link>
      </nav>
      <div class="user-info">
        <span>欢迎，{{ user.name }}</span>
        <el-button size="small" @click="logout" type="danger">退出</el-button>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = ref(
  localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
)

const logout = () => {
  localStorage.removeItem("user")
  router.push("/login")
}
</script>

<style scoped>
.user-layout {
  font-family: "Segoe UI", sans-serif;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2c3e50;
  color: white;
  padding: 10px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 1.2rem;
  font-weight: bold;
}

.nav-bar {
  display: flex;
  gap: 20px;
}

.nav-link {
  text-decoration: none;
  color: white;
  font-weight: 500;
  transition: opacity 0.3s;
}

.nav-link:hover {
  opacity: 0.7;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  max-width: 1000px;
  margin: 30px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
</style>