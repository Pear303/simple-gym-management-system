<template>
  <el-dialog
    v-model="visible"
    :title="formData.employeeId ? '编辑员工' : '新增员工'"
    width="550px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="90px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="employeeName">
            <el-input v-model="formData.employeeName" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="employeeGender">
            <el-select v-model="formData.employeeGender" placeholder="请选择性别" style="width: 100%">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="年龄" prop="employeeAge">
            <el-input-number v-model="formData.employeeAge" :min="18" :max="70" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入职日期" prop="entryTime">
            <el-date-picker
              v-model="formData.entryTime"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="职位" prop="staff">
            <el-select v-model="formData.staff" placeholder="请选择职位" style="width: 100%">
              <el-option label="健身教练" value="健身教练" />
              <el-option label="助教" value="助教" />
              <el-option label="前台" value="前台" />
              <el-option label="保洁" value="保洁" />
              <el-option label="经理" value="经理" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注" prop="employeeMessage">
        <el-input v-model="formData.employeeMessage" type="textarea" :rows="3" placeholder="请输入备注信息" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { addEmployee, updateEmployee } from '@/api/employee'

const visible = ref(false)
const loading = ref(false)
const formRef = ref()
const isEdit = ref(false)

const formData = reactive({
  employeeId: null,
  employeeName: '',
  employeeGender: '',
  employeeAge: 25,
  entryTime: '',
  staff: '',
  employeeMessage: ''
})

const rules = {
  employeeName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  employeeGender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  entryTime: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ],
  staff: [
    { required: true, message: '请选择职位', trigger: 'change' }
  ]
}

const emit = defineEmits(['success'])

const open = (row = null) => {
  visible.value = true
  if (row) {
    isEdit.value = true
    Object.assign(formData, row)
  } else {
    isEdit.value = false
    Object.assign(formData, {
      employeeId: null,
      employeeName: '',
      employeeGender: '',
      employeeAge: 25,
      entryTime: '',
      staff: '',
      employeeMessage: ''
    })
  }
}

const handleClose = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const res = isEdit.value ? await updateEmployee(formData) : await addEmployee(formData)
      if (res.success) {
        ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
        emit('success')
        handleClose()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      loading.value = false
    }
  })
}

defineExpose({ open })
</script>