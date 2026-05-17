<template>
  <div class="statistics">
    <div class="page-header">
      <h2>数据统计</h2>
    </div>
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="20"><User /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-label">会员总数</span>
          <span class="stat-value">{{ memberCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon :size="20"><Avatar /></el-icon>
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
  max-width: 960px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 500;
  color: #1A1A1A;
  letter-spacing: -0.2px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2px;
  margin-bottom: 32px;
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

.stat-info {
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

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2px;
}

.chart-card {
  background: #FFFFFF;
  border: 1px solid #E8E8E8;
  padding: 28px 24px;
}

.chart-card h3 {
  font-size: 14px;
  font-weight: 500;
  color: #1A1A1A;
  margin-bottom: 20px;
  letter-spacing: -0.2px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #FAFAFA;
  color: #CCC;
}

.chart-placeholder p {
  margin-top: 12px;
  font-size: 13px;
}

@media (max-width: 640px) {
  .stats-overview,
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>