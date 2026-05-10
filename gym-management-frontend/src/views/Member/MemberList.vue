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
      <el-input
        v-model="searchKeyword"
        placeholder="待实现"
        clearable
        @clear="loadMembers"
        style="width: 300px;"
        @keyup.enter="loadMembers"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="loadMembers">搜索</el-button>
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
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import MemberForm from './MemberForm.vue'
import { getMemberList, deleteMember, searchMemberById } from '@/api/member'

const memberList = ref([])
const searchKeyword = ref('')
const searchMode = ref('keyword')
const loading = ref(false)
const memberFormRef = ref()

// 切换搜索模式时清空列表
watch(searchMode, () => {
  memberList.value = []
  searchKeyword.value = ''
})

const getCardClassName = (type) => {
  const map = { 1: '月卡', 2: '季卡', 3: '年卡' }
  return map[type] || '未知'
}

const getCardTagType = (type) => {
  const map = { 1: 'warning', 2: 'success', 3: 'primary' }
  return map[type] || 'info'
}

const loadMembers = async () => {
  if (searchMode.value === 'keyword') {
    loading.value = true
    try {
      const res = await getMemberList({ keyword: searchKeyword.value })
      if (res.success) {
        memberList.value = res.data || []
      }
    } catch (error) {
      ElMessage.error('获取会员列表失败')
    } finally {
      loading.value = false
    }
    return
  }

  const keyword = searchKeyword.value.trim()
  if (!keyword) {
    ElMessage.warning('请输入会员ID')
    return
  }

  const id = parseInt(keyword)
  if (isNaN(id)) {
    ElMessage.warning('请输入有效的数字ID')
    return
  }

  loading.value = true
  try {
    const res = await searchMemberById(id)
    if (res.success && res.data) {
      memberList.value = [res.data]
    } else {
      memberList.value = []
      ElMessage.warning(res.message || '未找到该会员')
    }
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
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
}
</style>