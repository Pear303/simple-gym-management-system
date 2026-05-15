<template>
  <div class="member-list">
    <div class="page-header">
      <h2>会员管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增会员
      </el-button>
    </div>
    <div class="search-bar">
      <el-select v-model="searchField" placeholder="选择搜索字段" style="width: 140px;" @change="handleAddCondition">
        <el-option label="ID" value="memberId" />
        <el-option label="姓名" value="memberName" />
        <el-option label="性别" value="memberGender" />
        <el-option label="年龄" value="memberAge" />
        <el-option label="电话" value="memberPhone" />
        <el-option label="身高" value="memberHeight" />
        <el-option label="体重" value="memberweight" />
        <el-option label="卡种（可扩充）" value="cardClass" />
        <el-option label="到期时间（可扩充）" value="cardTime" />
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
    <el-table v-loading="loading" :data="memberList" stripe style="width: 100%">
      <el-table-column prop="memberId" label="ID" width="60" />
      <el-table-column prop="memberName" label="姓名" width="80" />
      <el-table-column prop="memberGender" label="性别" width="60" />
      <el-table-column prop="memberAge" label="年龄" width="60" />
      <el-table-column prop="memberPhone" label="电话" width="120" />
      <el-table-column prop="memberHeight" label="身高" width="80" />
      <el-table-column prop="memberweight" label="体重" width="80" />
      <el-table-column prop="cardClass" label="卡种" width="100">
        <template #default="{ row }">
          <el-tag :type="getCardTagType(row.cardClass)">{{ getCardClassName(row.cardClass) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="cardTime" label="到期时间" width="120" />
      <el-table-column label="操作" fixed="right" width="150">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <MemberForm ref="memberFormRef" @success="loadMembers" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import MemberForm from './MemberForm.vue'
import { getMemberList, deleteMember, searchMemberByRegex } from '@/api/member'

const memberList = ref([])
const keyword = ref('')
const searchField = ref('')
const loading = ref(false)
const memberFormRef = ref()
const selectedConditions = ref([])

// 字段标签映射
const fieldLabels = {
  memberId: 'ID',
  memberName: '姓名',
  memberGender: '性别',
  memberAge: '年龄',
  memberPhone: '电话',
  memberHeight: '身高',
  memberweight: '体重',
  cardClass: '卡种',
  cardTime: '到期时间'
}

const getFieldLabel = (field) => {
  return fieldLabels[field] || field
}

const getCardClassName = (type) => {
  const map = { 1: '月卡', 2: '季卡', 3: '年卡' }
  return map[type] || '未知'
}

const getCardTagType = (type) => {
  const map = { 1: 'warning', 2: 'success', 3: 'primary' }
  return map[type] || 'info'
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
  loadMembers()
}

// 删除单个条件
const handleRemoveCondition = (index) => {
  selectedConditions.value.splice(index, 1)
  loadMembers()
}

// 清空关键词时清空下拉选择
const handleClearKeyword = () => {
  searchField.value = ''
}

// 执行搜索
const loadMembers = async () => {
  loading.value = true
  try {
    if (selectedConditions.value.length === 0) {
      const res = await getMemberList()
      if (res.success) {
        memberList.value = res.data || []
      }
    } else {
      let resultList = null
      for (const condition of selectedConditions.value) {
        console.log('[DEBUG] 发送 searchMemberByRegex:', condition.field, condition.value)
        const res = await searchMemberByRegex(condition.field, condition.value)
        if (res.success) {
          const data = res.data || []
          if (resultList === null) {
            resultList = data
          } else {
            resultList = resultList.filter(item1 =>
              data.some(item2 => item2.memberId === item1.memberId)
            )
          }
        }
      }
      memberList.value = resultList || []
    }
  } catch (error) {
    ElMessage.error('获取会员列表失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  keyword.value = ''
  searchField.value = ''
  selectedConditions.value = []
  loadMembers()
}

const handleAdd = () => {
  memberFormRef.value?.open()
}

const handleEdit = (row) => {
  memberFormRef.value?.open(row)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除会员 "${row.memberName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteMember(row.memberId)
      if (res.success) {
        ElMessage.success('删除成功')
        loadMembers()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 如何使用正则化搜索
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
        <li>电话以"138"开头：<code>138*</code></li>
        <li>体重小于60：<code>&lt;60</code></li>
        <li>卡种为月卡：<code>1</code></li>
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
  loadMembers()
})
</script>

<style scoped>
.member-list {
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