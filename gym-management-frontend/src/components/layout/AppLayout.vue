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
  background: #F5F5F5;
  border-right: 1px solid #E8E8E8;
  transition: width 0.2s ease;
  overflow: hidden;
  z-index: 100;
}

.sidebar-mobile {
  position: fixed;
  left: 0;
  top: 56px;
  height: calc(100vh - 56px);
}

.header {
  background: #1A1A1A;
  display: flex;
  align-items: center;
  padding: 0 16px;
  height: 56px;
}

.main-content {
  background: #FAFAFA;
  padding: 32px;
  min-height: calc(100vh - 56px);
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
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