<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2>欢迎回来，管理员</h2>
      <p>这里是健身房管理系统的概览</p>
    </div>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="24"><User /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">会员总数</span>
          <span class="stat-value">{{ stats.memberTotal }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="24"><Avatar /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">员工总数</span>
          <span class="stat-value">{{ stats.employeeTotal }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="24"><TrendCharts /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">总人数</span>
          <span class="stat-value">{{ stats.total }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="24"><Calendar /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">今日日期</span>
          <span class="stat-value date">{{ currentDate }}</span>
        </div>
      </div>
    </div>
    <div class="quick-actions">
      <h3>快捷操作</h3>
      <div class="actions-grid">
        <router-link to="/members" class="action-item">
          <el-icon :size="24"><UserFilled /></el-icon>
          <span>会员管理</span>
        </router-link>
        <router-link to="/employees" class="action-item">
          <el-icon :size="24"><Avatar /></el-icon>
          <span>员工管理</span>
        </router-link>
        <router-link to="/statistics" class="action-item">
          <el-icon :size="24"><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </router-link>
      </div>
    </div>
  </div>
</template>



<script setup>
import { ref, onMounted, computed } from 'vue'
import { User, Avatar, TrendCharts, Calendar, UserFilled, DataAnalysis } from '@element-plus/icons-vue'
import { getMemberCount, getEmployeeCount } from '@/api/statistics'

const stats = ref({
  memberTotal: 0,
  employeeTotal: 0,
  total: 0
})

const currentDate = computed(() => {
  const now = new Date()
  return `${now.getMonth() + 1}/${now.getDate()}`
})

const fetchStats = async () => {
  try {
    const [memberRes, employeeRes] = await Promise.all([
      getMemberCount(),
      getEmployeeCount()
    ])
    if (memberRes.success) {
      stats.value.memberTotal = memberRes.totalCount || 0
    }
    if (employeeRes.success) {
      stats.value.employeeTotal = employeeRes.totalCount || 0
    }
    stats.value.total = stats.value.memberTotal + stats.value.employeeTotal
  } catch (error) {
    console.error('获取统计数据失败:', error)
    stats.value = {
      memberTotal: 'N/A',
      employeeTotal: 'N/A',
      total: 'N/A'
    }
  }
}

onMounted(() => {
  fetchStats()
})
</script>



<style scoped>
.dashboard {
  max-width: 960px;
  margin: 0 auto;
}

.dashboard-header {
  margin-bottom: 40px;
}

.dashboard-header h2 {
  font-size: 20px;
  font-weight: 500;
  color: #1A1A1A;
  margin-bottom: 4px;
  letter-spacing: -0.2px;
}

.dashboard-header p {
  font-size: 13px;
  color: #999;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2px;
  margin-bottom: 40px;
  background: #E8E8E8;
}

.stat-card {
  background: #FFFFFF;
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  transition: background 0.15s ease;
}

.stat-card:hover {
  background: #FAFAFA;
}

.stat-icon {
  color: #666;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat-value {
  font-size: 28px;
  font-weight: 400;
  color: #1A1A1A;
  letter-spacing: -1px;
}

.stat-value.date {
  font-size: 18px;
  letter-spacing: 0;
}

.quick-actions {
  background: #FFFFFF;
  border: 1px solid #E8E8E8;
  padding: 28px 24px;
}

.quick-actions h3 {
  font-size: 14px;
  font-weight: 500;
  color: #1A1A1A;
  margin-bottom: 20px;
  letter-spacing: -0.2px;
}

.actions-grid {
  display: flex;
  gap: 2px;
}

.action-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 24px;
  background: #FAFAFA;
  text-decoration: none;
  color: #666;
  transition: all 0.15s ease;
  font-size: 13px;
  font-weight: 400;
}

.action-item:hover {
  background: #1A1A1A;
  color: #FFFFFF;
}

@media (max-width: 960px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    flex-direction: column;
  }
}
</style>