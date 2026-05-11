<template>
  <div class="employee-list">
    <div class="page-header">
      <h2>员工管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增员工
      </el-button>
    </div>
    <div class="search-bar">
      <el-select v-model="searchField" placeholder="选择搜索字段" style="width: 140px;" @change="handleAddCondition">
        <el-option label="ID" value="employeeId" />
        <el-option label="姓名" value="employeeName" />
        <el-option label="性别" value="employeeGender" />
        <el-option label="年龄" value="employeeAge" />
        <el-option label="入职日期" value="entryTime" />
        <el-option label="职位" value="staff" />
        <el-option label="备注" value="employeeMessage" />
      </el-select>
      <el-input
        v-model="keyword"
        placeholder="示例: [数值]>25 *[包含]* [前缀]* *[后缀]"
        clearable
        style="width: 280px;"
        @keyup.enter="handleAddCondition"
        @clear="handleClearKeyword"
      />
      <el-button type="primary" @click="handleAddCondition">添加条件</el-button>
      <el-button @click="handleReset">清空</el-button>
      <el-button @click="howToSearch">搜索指南</el-button>

      <!-- 已选条件展示区域 -->
      <div class="selected-conditions" v-if="selectedConditions.length > 0">
        <el-tag
          v-for="(condition, index) in selectedConditions"
          :key="index"
          closable
          @close="handleRemoveCondition(index)"
          class="condition-tag"
        >
          {{ getFieldLabel(condition.field) }}: {{ condition.value }}
        </el-tag>
      </div>
    </div>
    <el-table v-loading="loading" :data="employeeList" stripe style="width: 100%">
      <el-table-column prop="employeeId" label="ID" width="60" />
      <el-table-column prop="employeeName" label="姓名" width="80" />
      <el-table-column prop="employeeGender" label="性别" width="60" />
      <el-table-column prop="employeeAge" label="年龄" width="60" />
      <el-table-column prop="entryTime" label="入职日期" width="120" />
      <el-table-column prop="staff" label="职位" width="100">
        <template #default="{ row }">
          <el-tag :type="getStaffTagType(row.staff)">{{ row.staff }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="employeeMessage" label="备注" />
      <el-table-column label="操作" fixed="right" width="150">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <EmployeeForm ref="employeeFormRef" @success="loadEmployees" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import EmployeeForm from './EmployeeForm.vue'
import { getEmployeeList, deleteEmployee, searchEmployeeByRegex } from '@/api/employee'

const employeeList = ref([])
const keyword = ref('')
const searchField = ref('')
const loading = ref(false)
const employeeFormRef = ref()
const selectedConditions = ref([])

// 字段标签映射
const fieldLabels = {
  employeeId: 'ID',
  employeeName: '姓名',
  employeeGender: '性别',
  employeeAge: '年龄',
  entryTime: '入职日期',
  staff: '职位',
  employeeMessage: '备注'
}

const getFieldLabel = (field) => {
  return fieldLabels[field] || field
}

const getStaffTagType = (staff) => {
  const map = {
    '健身教练': 'primary',
    '助教': 'success',
    '前台': 'warning',
    '保洁': 'info',
    '经理': 'danger'
  }
  return map[staff] || 'info'
}

// 添加搜索条件
const handleAddCondition = () => {
  const field = searchField.value
  const value = keyword.value.trim()

  if (!field) {
    ElMessage.warning('请选择搜索字段')
    return
  }

  if (!value) {
    return
  }

  selectedConditions.value.push({ field, value })

  // 清空输入框但保留选择的字段
  keyword.value = ''
  searchField.value = ''

  // 执行搜索
  loadEmployees()
}

// 删除单个条件
const handleRemoveCondition = (index) => {
  selectedConditions.value.splice(index, 1)
  loadEmployees()
}

// 清空关键词时清空下拉选择
const handleClearKeyword = () => {
  searchField.value = ''
}

// 执行搜索
const loadEmployees = async () => {
  loading.value = true
  try {
    if (selectedConditions.value.length === 0) {
      const res = await getEmployeeList()
      if (res.success) {
        employeeList.value = res.data || []
      }
    } else {
      let resultList = null
      for (const condition of selectedConditions.value) {
        console.log('[DEBUG] 发送 searchEmployeeByRegex:', condition.field, condition.value)
        const res = await searchEmployeeByRegex(condition.field, condition.value)
        if (res.success) {
          const data = res.data || []
          if (resultList === null) {
            resultList = data
          } else {
            resultList = resultList.filter(item1 =>
              data.some(item2 => item2.employeeId === item1.employeeId)
            )
          }
        }
      }
      employeeList.value = resultList || []
    }
  } catch (error) {
    ElMessage.error('获取员工列表失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  keyword.value = ''
  searchField.value = ''
  selectedConditions.value = []
  loadEmployees()
}

const handleAdd = () => {
  employeeFormRef.value?.open()
}

const handleEdit = (row) => {
  employeeFormRef.value?.open(row)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除员工 "${row.employeeName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteEmployee(row.employeeId)
      if (res.success) {
        ElMessage.success('删除成功')
        loadEmployees()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 入职日期正则化搜索
const howToSearch = () => {
  ElMessageBox.alert(
    `
    <div style="line-height: 1.8;">
      <h4 style="margin-top: 0;">正则化搜索使用说明</h4>
      
      <p><strong>支持的搜索模式：</strong></p>
      <ul style="padding-left: 20px;">
        <li><code>[数值]&gt;25</code> - 大于25（适用于年龄等数值字段）</li>
        <li><code>*[包含]*</code> - 包含"包含"文本</li>
        <li><code>[前缀]*</code> - 以"前缀"开头</li>
        <li><code>*[后缀]</code> - 以"后缀"结尾</li>
        <li><code>完整文本</code> - 精确匹配</li>
      </ul>
      
      <p><strong>使用示例：</strong></p>
      <ul style="padding-left: 20px;">
        <li>姓名包含"张"：<code>*张*</code></li>
        <li>职位以前缀"健身"开头：<code>健身*</code></li>
        <li>备注以后缀"优秀"结尾：<code>*优秀</code></li>
        <li>年龄大于等于30：<code>&gt;=30</code></li>
      </ul>
      
      <p><strong>入职日期专用模式：</strong></p>
      <ul style="padding-left: 20px;">
        <li>2024年内入职：<code>2024</code></li>
        <li>2024年6月入职：<code>2024-06</code></li>
        <li>6月入职（字符串）：<code>*-06*</code></li>
        <li>2024年1月1日之后入职：<code>&gt;=2024-01-01</code></li>
        <li>2024上半年入职：<code>between 2024-01,2024-06</code></li>
        <li>精确日期：<code>2024-06-15</code></li>
      </ul>
      
      <p style="color: #909399; font-size: 12px; margin-top: 16px;">
        💡 提示：可以添加多个条件，同时组合搜索，系统会自动取交集
      </p>
    </div>
    `,
    '正则化搜索指南',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '知道了',
      customClass: 'regex-help-dialog'
    }
  )
}

onMounted(() => {
  loadEmployees()
})
</script>

<style scoped>
.employee-list {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #2D3436;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  align-items: center;
}

.selected-conditions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-left: 8px;
}

.condition-tag {
  margin-right: 4px;
}
</style>