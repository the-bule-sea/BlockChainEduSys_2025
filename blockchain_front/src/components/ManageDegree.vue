<template>
  <div>
    <!-- 查询区域 -->
    <div>
      <el-input v-model="params.name" placeholder="请输入姓名" style="width: 150px" />
      <el-input
        v-model="params.idCardNum"
        placeholder="请输入身份证号"
        style="width: 180px; margin-left: 10px"
      />
      <el-button type="primary" @click="findBySearch" style="margin-left: 10px">查询</el-button>
      <el-button type="warning" @click="reset">清空</el-button>
      <el-button type="success" @click="add">新增</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" style="width: 100%; margin-top: 20px">
      <!-- <el-table-column prop="id" label="ID" width="60px" /> -->
      <el-table-column prop="name" label="姓名" width="80px" />
      <el-table-column prop="idCardNum" label="身份证号" width="180px" />
      <el-table-column prop="university" label="学校" />
      <el-table-column prop="major" label="专业" />
      <el-table-column prop="degreeLevel" label="学历" />
      <el-table-column prop="graduationDate" label="毕业时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" @click="edit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
            <template #reference>
              <el-button type="danger" style="margin-left: 10px">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="block" style="margin-top: 20px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="params.pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="params.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <!-- 弹窗 -->
    <el-dialog title="学历信息" v-model="dialogFormVisible" width="40%">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCardNum"/>
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form.university"/>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.major"/>
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="form.degreeLevel" placeholder="请选择学历">
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
            <el-option label="博士" value="博士" />
          </el-select>
        </el-form-item>
        <el-form-item label="毕业时间">
          <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {ElMessage, ElLoading} from "element-plus";
import request from "@/utils/request";

const params = ref({
  name: "",
  idCardNum: "",
  pageNum: 1,
  pageSize: 5
});

const tableData = ref([]);
const total = ref(0);
const dialogFormVisible = ref(false);
const form = ref({});

// 加载数据
const load = async () => {
  // console.log(params.value);
  const response = await request.post("/admin/degree/page", params.value);
  if (response.code === "0") {
    tableData.value = response.data.content;
    console.log(response.data);
    total.value = response.data.totalElements;
  }
};

onMounted(async () => {
  await load()
})

// 查询
const findBySearch = () => {
  params.value.pageNum = 1;
  load();
};

// 清空
const reset = () => {
  params.value = { name: "", idCardNum: "", pageNum: 1, pageSize: 5 };
  load();
};

// 新增
const add = () => {
  form.value = {};
  dialogFormVisible.value = true;
};

// 编辑
const edit = row => {
  form.value = { ...row };
  dialogFormVisible.value = true;
};

// 提交
const submit = async () => {
  try {
    // 显示加载动画
    const loadingInstance = ElLoading.service({
      fullscreen: true,
      text: '正在上链，请稍候...',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    // 伪随机延迟，模拟上链耗时（1.5s ~ 3s）
    const delay = Math.floor(Math.random() * 1500) + 1500;
    await new Promise(resolve => setTimeout(resolve, delay));

    // 发起真实请求
    const res = await request.post('/admin/degree', form.value);

    // const res = null;
    // 关闭加载动画
    loadingInstance.close();

    if (res.code === '0') {
      dialogFormVisible.value = false;
      await load();
      ElMessage.success('上链成功，数据已提交');
    } else {
      ElMessage.error(res.msg || '提交失败');
    }
  } catch (error) {
    // 出错也关闭加载动画
    ElLoading.service({}).close();
    console.error('提交失败:', error);
    ElMessage.error('提交失败，请重试');
  }
}

// 删除
const del = async (id) => {
  try {
    const res = await request.delete(`/admin/degree/${id}`)
    if (res.code === '0') {
      await load()
      ElMessage.success('删除成功')
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败，请重试')
  }
}


// 分页
const handleSizeChange = size => {
  params.value.pageSize = size;
  load();
};
const handleCurrentChange = page => {
  params.value.pageNum = page;
  load();
};
</script>
