<template>
  <div class="register-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Register</span>
        </div>
      </template>
      <el-form @submit.prevent="handleRegister" :model="registerForm" label-width="80px">
        <el-form-item label="Name">
          <el-input v-model="registerForm.name" required></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="registerForm.email" type="email" required></el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="registerForm.password" type="password" required show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit">Register</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Register',
  setup() {
    const router = useRouter();
    const registerForm = ref({
      name: '',
      email: '',
      password: '',
    });

    const handleRegister = () => {
      console.log('Register:', registerForm.value.name, registerForm.value.email, registerForm.value.password);
      router.push('/login');
    };

    return {
      registerForm,
      handleRegister,
    };
  },
};
</script>

<style scoped>
.register-container {
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