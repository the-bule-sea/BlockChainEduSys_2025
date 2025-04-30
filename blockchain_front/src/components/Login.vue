<template>
  <div class="login-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>登录</span>
        </div>
      </template>
      <el-form @submit.prevent="handleLogin" :model="loginForm" label-width="80px">
        <el-form-item label="Email">
          <el-input v-model="loginForm.email" type="email" required></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" required show-password></el-input>
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
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';

export default {
  name: 'Login',
  setup() {
    const router = useRouter();
    const loginForm = ref({
      email: '',
      password: '',
    });

    const handleLogin = async() => {
      try {
        const response = await request.post('/auth/login', loginForm.value);
        if(response.code === '0'){
          console.log('登录成功:', response);
          router.push('/dashboard');
        } else{
          alert(response.msg);
        }
      } catch(error){
        console.error('登录异常:', error);
        alert('登录失败，请重试');
      }
      // console.log('Login:', loginForm.value.email, loginForm.value.password);
      // router.push('/dashboard');
    };

    return {
      loginForm,
      handleLogin,
    };
  },
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