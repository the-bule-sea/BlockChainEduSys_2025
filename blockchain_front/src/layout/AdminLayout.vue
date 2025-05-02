<template>
  <div>
    <!-- 使用 el-container 布局，水平排列 header 和主体部分 -->
    <el-container style="height: 100vh;">
      <!-- header 部分 -->
      <el-header style="background-color: lightslategray; height: 60px; line-height: 60px;">
        <span style="font-size: 20px; color: white">后台界面</span>
        <el-dropdown style="float: right; height: 60px; line-height: 60px;">
          <span class="el-dropdown-link" style="color: white;">
            当前用户：{{ user.name }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>
                <div @click="logout">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!-- 使用 el-container 分为左侧 (aside) 和右侧 (main) -->
      <el-container style="flex: 1; display: flex;">
        <!-- 左侧菜单栏 -->
        <el-aside style="background-color: #545c64; width: 250px;">
          <el-scrollbar>
            <el-menu
              :default-active="$route.path"
              router
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose"
              background-color="#545c64"
              text-color="#fff"
              active-text-color="#ffd04b"
            >
              <el-menu-item index="/admin">
                <span>首页</span>
              </el-menu-item>
              <el-sub-menu index="2">
                <template #title>
                  <span>用户管理</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="/admin/test">测试信息</el-menu-item>
                  <el-menu-item index="/admin/test">测试信息</el-menu-item>
                  <el-menu-item index="/admin/test">测试信息</el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <!-- 右侧主内容区域 -->
        <el-main style="flex: 1;">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const user = ref(
  localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
)

const logout = () => {
  console.log('退出登录test')
  localStorage.clear() // 清除 token
  router.push('/login')
}

// 可选方法占位
const handleOpen = (key, keyPath) => {
  console.log('Menu opened:', key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log('Menu closed:', key, keyPath)
}
</script>

<style scoped>
.el-menu {
  border-right: none !important;
}
</style>