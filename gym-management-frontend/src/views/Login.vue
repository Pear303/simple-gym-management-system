<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>
    <div class="login-card">
      <div class="login-header">
        <div class="logo">
          <el-icon :size="32" color="#fff"><Aim /></el-icon>
        </div>
        <h1>健身房管理系统</h1>
        <p>Gym Management System</p>
      </div>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="adminAccount">
          <el-input
            v-model="loginForm.adminAccount"
            placeholder="请输入账号"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="adminPassword">
          <el-input
            v-model="loginForm.adminPassword"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Aim } from '@element-plus/icons-vue'
import { adminLogin } from '@/api/auth'
import { useAdminStore } from '@/store/admin'

const router = useRouter()
const adminStore = useAdminStore()

const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  adminAccount: '',
  adminPassword: ''
})

const rules = {
  adminAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  adminPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 1, message: '密码不能为空', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {

      // 后端返回的是 { success: true, token: "...", adminInfo: {...} }
      const res = await adminLogin(loginForm)
      if (res.success && res.token) {
        adminStore.setToken(res.token)

        if(res.adminInfo){
          adminStore.setAdminInfo(res.adminInfo)
        }

        ElMessage.success('登录成功')

        router.push('/dashboard')

      } else {
        ElMessage.error(res.message || '登录失败')
      }
    } catch (error) {
      ElMessage.error('登录失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FAFAFA;
}

.login-bg {
  display: none;
}

.login-card {
  width: 380px;
  padding: 48px 40px;
  background: #FFFFFF;
  border: 1px solid #E8E8E8;
  position: relative;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 56px;
  height: 56px;
  margin: 0 auto 20px;
  background: #1A1A1A;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-header h1 {
  font-size: 20px;
  font-weight: 500;
  color: #1A1A1A;
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.login-header p {
  font-size: 13px;
  color: #999999;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.login-form {
  margin-top: 0;
}

.login-form :deep(.el-input__wrapper) {
  box-shadow: none;
  border-radius: 2px;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #999;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #1A1A1A;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 2px;
  border-radius: 2px;
  background: #1A1A1A;
  border: none;
  transition: opacity 0.15s ease;
}

.login-btn:hover {
  opacity: 0.85;
}

.login-btn:active {
  opacity: 0.75;
}
</style>