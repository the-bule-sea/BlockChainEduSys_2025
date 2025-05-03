<template>
  <div class="dashboard-container">
    <h2 class="title">欢迎回来，{{ user.name }}</h2>
    <p class="subtitle">您已成功登录用户中心</p>

    <!-- 学历信息 -->
    <transition name="fade">
      <el-card v-if="degree" class="degree-card">
        <template #header>
          <div class="card-header">
            <strong>学历信息</strong>
          </div>
        </template>
        <div class="info-row"><label>姓名：</label>{{ degree.name }}</div>
        <div class="info-row"><label>身份证号：</label>{{ degree.idCardNum }}</div>
        <div class="info-row"><label>学校：</label>{{ degree.university }}</div>
        <div class="info-row"><label>专业：</label>{{ degree.major }}</div>
        <div class="info-row"><label>学历：</label>{{ degree.degreeLevel }}</div>
        <div class="info-row"><label>毕业时间：</label>{{ degree.graduationDate }}</div>
      </el-card>
    </transition>

    <el-button type="danger" @click="logout" icon="el-icon-switch-button">退出登录</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request";

const router = useRouter();
const user = ref({});
const degree = ref(null);

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const storedUser = localStorage.getItem("user");
    if (!storedUser) {
      alert("请先登录");
      router.push("/login");
      return;
    }
    user.value = JSON.parse(storedUser);

    if (!user.value.id) {
      console.error("用户ID未定义");
      return;
    }

    const res = await request.get(`/user/degree/${user.value.id}`);
    if (res.code === "0") {
      degree.value = res.data;
    }
  } catch (error) {
    console.error("获取学历信息失败:", error);
  }
};

onMounted(async () => {
  await fetchUserInfo();
});

const logout = () => {
  localStorage.removeItem("user");
  router.push("/login");
};
</script>

<style scoped>
.dashboard-container {
  text-align: center;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.degree-card {
  max-width: 600px;
  margin: 0 auto 30px auto;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.5s ease;
}

.card-header {
  font-weight: bold;
  font-size: 18px;
}

.info-row {
  margin: 10px 0;
  font-size: 15px;
  text-align: left;
}

.info-row label {
  font-weight: bold;
  color: #333;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>