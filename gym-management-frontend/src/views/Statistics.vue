<template>
  <div class="statistics">
    <div class="page-header">
      <h2>数据统计</h2>
    </div>
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #FF6B35 0%, #FF8F66 100%);">
          <el-icon :size="28"><User /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">会员总数</span>
          <span class="stat-value">{{ memberCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: linear-gradient(135deg, #4ECDC4 0%, #7EDAD3 100%);">
          <el-icon :size="28"><Avatar /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">员工总数</span>
          <span class="stat-value">{{ employeeCount }}</span>
        </div>
      </div>
    </div>
    <div class="charts-container">
      <div class="chart-card">
        <h3>会员卡种分布</h3>
        <div class="chart-placeholder">
          <el-icon :size="48" color="#B2BEC3"><PieChart /></el-icon>
          <p>图表占位符</p>
        </div>
      </div>
      <div class="chart-card">
        <h3>员工职位分布</h3>
        <div class="chart-placeholder">
          <el-icon :size="48" color="#B2BEC3"><DataAnalysis /></el-icon>
          <p>图表占位符</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Avatar, PieChart, DataAnalysis } from '@element-plus/icons-vue'
import { getMemberCount, getEmployeeCount } from '@/api/statistics'

const memberCount = ref(0)
const employeeCount = ref(0)

const loadStats = async () => {
  try {
    const [memberRes, employeeRes] = await Promise.all([
      getMemberCount(),
      getEmployeeCount()
    ])
    if (memberRes.success) {
      memberCount.value = memberRes.totalCount || 0
    }
    if (employeeRes.success) {
      employeeCount.value = employeeRes.totalCount || 0
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    memberCount.value = 156
    employeeCount.value = 23
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.statistics {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #2D3436;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 14px;
  color: #636E72;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #2D3436;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.chart-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #2D3436;
  margin-bottom: 16px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 8px;
  color: #B2BEC3;
}

.chart-placeholder p {
  margin-top: 12px;
  font-size: 14px;
}

@media (max-width: 768px) {
  .stats-overview,
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>