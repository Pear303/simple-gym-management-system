<template>
  <div class="app-sidebar">
    <el-menu
      :default-active="activeMenu"
      :collapse="collapse"
      router
      class="sidebar-menu"
      background-color="transparent"
      text-color="#666666"
      active-text-color="#1A1A1A"
    >
      <el-menu-item index="/dashboard">
        <el-icon><Odometer /></el-icon>
        <template #title>仪表盘</template>
      </el-menu-item>
      <el-menu-item index="/members">
        <el-icon><User /></el-icon>
        <template #title>会员管理</template>
      </el-menu-item>
      <el-menu-item index="/employees">
        <el-icon><Avatar /></el-icon>
        <template #title>员工管理</template>
      </el-menu-item>
      <el-menu-item index="/statistics">
        <el-icon><DataAnalysis /></el-icon>
        <template #title>数据统计</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Odometer, User, Avatar, DataAnalysis } from '@element-plus/icons-vue'

defineProps({
  collapse: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.app-sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-menu {
  border-right: none;
  flex: 1;
}

.sidebar-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  font-size: 13px;
  border-radius: 0;
  margin: 0;
  transition: all 0.15s ease;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: rgba(0, 0, 0, 0.04);
  font-weight: 500;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: rgba(0, 0, 0, 0.02);
}
</style>