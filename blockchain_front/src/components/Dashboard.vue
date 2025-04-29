<template>
  <div class="dashboard-container">
    <h2>Welcome to the Dashboard</h2>
    <p>You have successfully logged in.</p>
    <button @click="logout">Logout</button>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: 'Dashboard',
  setup() {
    const router = useRouter();

    const logout = () => {
      // 这里可以添加登出逻辑
      console.log('Logout');
      router.push('/login');
    };

    return {
      logout,
    };
  },
};
</script>

<style scoped>
.dashboard-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
</style>