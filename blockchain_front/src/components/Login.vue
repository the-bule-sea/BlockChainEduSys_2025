<template>
  <div class="login-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>登录</span>
        </div>
      </template>
      <el-form @submit.prevent="handleLogin" :model="loginForm" label-width="80px">
        <!-- 身份选择 -->
        <el-form-item label="身份类型">
          <el-select v-model="loginForm.identityType" placeholder="请选择">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>

        <!-- 条件渲染输入框 -->
        <div v-if="loginForm.identityType === 'ADMIN'">
          <el-form-item label="Email">
            <el-input v-model="loginForm.email" type="email" required />
          </el-form-item>
        </div>

        <div v-else>
          <el-form-item label="身份证号">
            <el-input v-model="loginForm.idCardNum" required />
          </el-form-item>
        </div>

        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" required show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">登录</el-button>
          <el-button @click="$router.push('/register')">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request";

export default {
  name: "Login",
  setup() {
    const router = useRouter();
    const loginForm = ref({
      email: "",
      password: "",
      idCardNum: "",
      identityType: "ADMIN" // 新增身份类型字段 ['ADMIN', 'USER']
    });

    const handleLogin = async () => {
      try {
        const payload = {
          identityType: loginForm.value.identityType,
          email: loginForm.value.email || null,
          idCardNum: loginForm.value.idCardNum || null,
          password: loginForm.value.password
        };
        const response = await request.post("/auth/login", payload);
        if (response.code === "0") {
          console.log("登录成功:", response);
          router.push("/dashboard");
        } else {
          alert(response.msg);
        }
      } catch (error) {
        console.error("登录异常:", error);
        alert("登录失败，请重试");
      }
      // console.log('Login:', loginForm.value.email, loginForm.value.password);
      // router.push('/dashboard');
    };

    return {
      loginForm,
      handleLogin
    };
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>