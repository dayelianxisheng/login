<template>
  <div class="login-container">
    <!-- 左侧动画区域 -->
    <div class="login-left">
      <div class="animation-container">
        <!-- 这里可以放置动画元素 -->
      </div>
    </div>

    <!-- 右侧登录表单 -->
    <div class="login-right">
      <div class="login-box">
        <div class="login-header">
          <img src="../assets/img.png" alt="logo" class="logo">
          <h1>登录系统</h1>
        </div>

        <el-form @submit.prevent="handleLogin" class="login-form">
          <el-form-item>
            <el-input
              v-model="username"
              placeholder="请输入账号/手机号"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
          </el-form-item>

          <el-button type="primary" @click="handleLogin" class="login-button">
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { User, Lock } from '@element-plus/icons-vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const username = ref('');
const password = ref('');
const rememberPassword = ref(false);
const errorMessage = ref('');
const router = useRouter();

const handleLogin = async () => {
  try {
    const response = await axios.post('/api/users/login', {
      username: username.value,
      password: password.value
    });

    console.log('后端返回的响应:', response.data); // 用于调试

    if (response.data.code === 200) {
      const token = response.data.data;
      localStorage.setItem('token', token);
      localStorage.setItem('username', username.value);

      console.log('登录成功，JWT:', token); // 用于调试

      ElMessage.success('登录成功');
      router.push('/layout/user-management');
    } else {
      ElMessage.error(response.data.message || '登录失败');
    }
  } catch (error) {
    console.error('登录错误:', error);
    ElMessage.error('登录失败');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #1a237e 0%, #000051 100%);
  overflow: hidden;
  margin: 0; /* 确保没有外边距 */
  padding: 0; /* 确保没有内边距 */
}

.login-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.animation-container {
  width: 100%;
  height: 400px;
  background-image: url('../assets/img_1.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: bottom;
}

.login-right {
  width: 500px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-box {
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 60px;
  height: 60px;
  margin-bottom: 20px;
}

h1 {
  color: white;
  font-size: 24px;
  margin: 0;
}

.login-form {
  .el-input {
    background: rgba(255, 255, 255, 0.1);
    border: none;
    height: 50px;

    :deep(.el-input__inner) {
      color: white;
      background: transparent;
      border: 1px solid rgba(255, 255, 255, 0.2);

      &::placeholder {
        color: rgba(255, 255, 255, 0.6);
      }
    }

    :deep(.el-input__prefix) {
      color: rgba(255, 255, 255, 0.6);
    }
  }
}

.el-checkbox {
  color: white;
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
  height: 50px;
  font-size: 16px;
  background: linear-gradient(90deg, #1890ff 0%, #096dd9 100%);
  border: none;

  &:hover {
    opacity: 0.9;
  }
}

.error {
  color: #ff4d4f;
  text-align: center;
  margin-top: 20px;
}
</style>