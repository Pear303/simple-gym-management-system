<template>
  <el-container class="app-layout">
    <el-aside
      :width="isCollapse ? '64px' : '180px'"
      class="sidebar"
      :class="{ 'sidebar-mobile': isMobile }"
    >
      <AppSidebar :collapse="isCollapse" />
    </el-aside>
    <el-container>
      <el-header class="header">
        <AppHeader @toggle="toggleCollapse" :is-mobile="isMobile" @menu-click="showMobileMenu = !showMobileMenu" />
      </el-header>
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <el-drawer v-model="showMobileMenu" direction="ltr" size="220px" :show-close="false" v-if="isMobile">
      <AppSidebar :collapse="false" />
    </el-drawer>
  </el-container>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import AppHeader from './AppHeader.vue'
import AppSidebar from './AppSidebar.vue'

const isCollapse = ref(false)
const isMobile = ref(false)
const showMobileMenu = ref(false)

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value) {
    isCollapse.value = true
  }
}

const toggleCollapse = () => {
  if (isMobile.value) {
    showMobileMenu.value = true
  } else {
    isCollapse.value = !isCollapse.value
  }
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.app-layout {
  height: 100vh;
}

.sidebar {
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  transition: width 0.3s ease;
  overflow: hidden;
  z-index: 100;
}

.sidebar-mobile {
  position: fixed;
  left: 0;
  top: 60px;
  height: calc(100vh - 60px);
}

.header {
  background: #2D3436;
  display: flex;
  align-items: center;
  padding: 0 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.main-content {
  background: #F8F9FA;
  padding: 24px;
  min-height: calc(100vh - 60px);
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }
}
</style>