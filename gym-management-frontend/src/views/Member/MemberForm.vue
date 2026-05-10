<template>
  <el-dialog
    v-model="visible"
    :title="formData.memberId ? '编辑会员' : '新增会员'"
    width="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="密码" prop="memberPassword">
            <el-input v-model="formData.memberPassword" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="memberName">
            <el-input v-model="formData.memberName" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="memberGender">
            <el-select v-model="formData.memberGender" placeholder="请选择性别" style="width: 100%">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="memberAge">
            <el-input-number v-model="formData.memberAge" :min="1" :max="150" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="电话" prop="memberPhone">
            <el-input v-model="formData.memberPhone" placeholder="请输入电话" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="卡种" prop="cardClass">
            <el-select v-model="formData.cardClass" placeholder="请选择卡种" style="width: 100%">
              <el-option label="月卡" :value="1" />
              <el-option label="季卡" :value="2" />
              <el-option label="年卡" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身高(cm)" prop="memberHeight">
            <el-input-number v-model="formData.memberHeight" :min="50" :max="250" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="体重(kg)" prop="memberweight">
            <el-input-number v-model="formData.memberweight" :min="20" :max="300" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="到期时间" prop="cardTime">
            <el-date-picker
              v-model="formData.cardTime"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
      </el-row>
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
import { addMember, updateMember } from '@/api/member'

const visible = ref(false)
const loading = ref(false)
const formRef = ref()
const isEdit = ref(false)

const formData = reactive({
  memberId: null,
  memberPassword: '',
  memberName: '',
  memberGender: '',
  memberAge: 18,
  memberHeight: 170,
  memberweight: 70,
  memberPhone: '',
  cardClass: 1,
  cardTime: ''
})

const rules = {
  memberPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  memberName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  memberGender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  memberAge: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  memberPhone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
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
      memberId: null,
      memberPassword: '',
      memberName: '',
      memberGender: '',
      memberAge: 18,
      memberHeight: 170,
      memberweight: 70,
      memberPhone: '',
      cardClass: 1,
      cardTime: ''
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
      const res = isEdit.value ? await updateMember(formData) : await addMember(formData)
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