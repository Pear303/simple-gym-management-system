<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2>欢迎回来，管理员</h2>
      <p>这里是健身房管理系统的概览</p>
    </div>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF6B35 0%, #FF8F66 100%);">
          <el-icon :size="32"><User /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">会员总数</span>
          <span class="stat-value">{{ stats.memberTotal }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #4ECDC4 0%, #7EDAD3 100%);">
          <el-icon :size="32"><Avatar /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">员工总数</span>
          <span class="stat-value">{{ stats.employeeTotal }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #74B9FF 0%, #98C1FF 100%);">
          <el-icon :size="32"><TrendCharts /></el-icon>
        </div>
        <div class="stat-content">
          <span class="stat-label">总人数</span>
          <span class="stat-value">{{ stats.total }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FDCB6E 0%, #FED476 100%);">
          <el-icon :size="32"><Calendar /></el-icon>
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
  max-width: 1200px;
  margin: 0 auto;
}

.dashboard-header {
  margin-bottom: 32px;
}

.dashboard-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #2D3436;
  margin-bottom: 8px;
}

.dashboard-header p {
  font-size: 14px;
  color: #636E72;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 14px;
  color: #636E72;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #2D3436;
}

.stat-value.date {
  font-size: 18px;
}

.quick-actions {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.quick-actions h3 {
  font-size: 16px;
  font-weight: 600;
  color: #2D3436;
  margin-bottom: 20px;
}

.actions-grid {
  display: flex;
  gap: 16px;
}

.action-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
  text-decoration: none;
  color: #2D3436;
  transition: all 0.3s;
}

.action-item:hover {
  background: linear-gradient(135deg, rgba(255, 107, 53, 0.1) 0%, rgba(255, 107, 53, 0.05) 100%);
  color: #FF6B35;
}

.action-item span {
  font-size: 14px;
  font-weight: 500;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    flex-direction: column;
  }
}
</style>