<template>
  <div class="register-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>注册</span>
        </div>
      </template>
      <el-form @submit.prevent="handleRegister" :model="registerForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="registerForm.name" required></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="registerForm.email" type="email" required></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="registerForm.password" type="password" required show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit">注册</el-button>
          <el-button @click="$router.push('/login')">去登录</el-button>
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

    const handleRegister = async() => {
      try {
        const response = await request.post('/auth/register', registerForm.value);
        if(response.code === '0') {
          router.push('/dashbord');
        } else{
          alert(response.msg);
        }
      } catch(error){
        alert('注册失败');
      }
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