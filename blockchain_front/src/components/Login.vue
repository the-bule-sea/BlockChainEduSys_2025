<template>
  <div class="login-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Login</span>
        </div>
      </template>
      <el-form @submit.prevent="handleLogin" :model="loginForm" label-width="80px">
        <el-form-item label="Email">
          <el-input v-model="loginForm.email" type="email" required></el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="loginForm.password" type="password" required show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit">Login</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  setup() {
    const router = useRouter();
    const loginForm = ref({
      email: '',
      password: '',
    });

    const handleLogin = () => {
      console.log('Login:', loginForm.value.email, loginForm.value.password);
      router.push('/dashboard');
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