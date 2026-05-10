<template>
  <div class="app-header">
    <div class="header-left">
      <el-button
        :icon="isMobile ? Fold : (isCollapse ? Expand : Fold)"
        circle
        @click="$emit('toggle')"
        class="toggle-btn"
      />
      <span class="breadcrumb" v-if="!isMobile">健身房管理系统</span>
      <span class="breadcrumb-mobile" v-else>健身房管理</span>
    </div>
    <div class="header-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-icon :size="20"><User /></el-icon>
          <span class="username" v-if="!isMobile">管理员</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { User, SwitchButton, Fold, Expand } from '@element-plus/icons-vue'
import { useAdminStore } from '@/store/admin'

defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  },
  isMobile: {
    type: Boolean,
    default: false
  }
})

defineEmits(['toggle'])

const router = useRouter()
const adminStore = useAdminStore()

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      adminStore.logout()
      router.push('/login')
    } catch {
    }
  }
}
</script>

<style scoped>
.app-header {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #fff;
}

.toggle-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.5);
}

.breadcrumb {
  color: #fff;
  font-size: 16px;
  font-weight: 500;
}

.breadcrumb-mobile {
  color: #fff;
  font-size: 14px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.1);
}

.username {
  font-size: 14px;
}
</style>