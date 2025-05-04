<template>
  <div class="verify-container">
    <el-card class="verify-card">
      <template #header>
        <strong>学历资料校验</strong>
      </template>

      <div v-if="loading" style="text-align: center">
        <el-icon>
          <Loading />
        </el-icon>正在校验中...
      </div>

      <div v-else-if="verifyResult">
        <el-descriptions title="校验结果" column="1" border>
          <el-descriptions-item label="姓名">{{ verifyResult.degree.name }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ verifyResult.degree.idCardNum }}</el-descriptions-item>
          <el-descriptions-item label="学校">{{ verifyResult.degree.university }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ verifyResult.degree.major }}</el-descriptions-item>
          <el-descriptions-item label="学历">{{ verifyResult.degree.degreeLevel }}</el-descriptions-item>
          <el-descriptions-item label="毕业时间">{{ verifyResult.degree.graduationDate }}</el-descriptions-item>
          <el-descriptions-item label="本地字段哈希">{{ verifyResult.localHash }}</el-descriptions-item>
          <el-descriptions-item label="链上字段哈希">{{ verifyResult.blockchainHash }}</el-descriptions-item>
          <el-descriptions-item label="交易 Hash">{{ verifyResult.degree.txHash }}</el-descriptions-item>
          <el-descriptions-item label="校验状态">
            <el-tag
              :type="verifyResult.valid ? 'success' : 'danger'"
            >{{ verifyResult.valid ? '一致（已验证）' : '不一致（数据可能被篡改）' }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div v-else>
        <el-empty description="暂无校验结果" />
      </div>
    </el-card>
  </div>
</template>
  
  <script setup>
import { ref, onMounted } from "vue";
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import { Loading } from "@element-plus/icons-vue";

const verifyResult = ref(null);
const loading = ref(true);
const user = ref({});

const fetchVerifyData = async () => {
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

    const res = await request.get(`/user/verify/${user.value.id}`);
    if (res.code === "0") {
      verifyResult.value = res.data;
    } else {
      ElMessage.error(res.msg || "校验失败");
      if (Array.isArray(res.data)) {
        ElMessage.error(res.data.join("\n")); // 每个问题一行
      }
    }
  } catch (error) {
    ElMessage.error("请求失败：" + error);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchVerifyData);
</script>
  
  <style scoped>
.verify-container {
  max-width: 800px;
  margin: 40px auto;
}

.verify-container {
  transition: all 0.5s ease;
}
</style>
  