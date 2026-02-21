<template>
  <div class="elder-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="护理等级">
          <el-select v-model="queryForm.nursingLevel" placeholder="请选择" clearable>
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="在院" :value="1" />
            <el-option label="出院" :value="2" />
            <el-option label="转院" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="60">
          <template #default="{ row }">
            {{ row.gender === 0 ? '女' : '男' }}
          </template>
        </el-table-column>
        <el-table-column label="年龄" width="60">
          <template #default="{ row }">
            {{ calculateAge(row.birthDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="nursingLevel" label="护理等级" width="80" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column label="入院天数" width="80">
          <template #default="{ row }">
            {{ calculateAdmissionDays(row.admissionDate) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :value="0">女</el-radio>
            <el-radio :value="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="formData.birthDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入家庭住址" />
        </el-form-item>
        <el-form-item label="护理等级" prop="nursingLevel">
          <el-select v-model="formData.nursingLevel" placeholder="请选择">
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
          </el-select>
        </el-form-item>
        <el-form-item label="入院日期" prop="admissionDate">
          <el-date-picker
            v-model="formData.admissionDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="紧急联系人" prop="emergencyContact">
          <el-input v-model="formData.emergencyContact" placeholder="请输入紧急联系人" />
        </el-form-item>
        <el-form-item label="紧急联系电话" prop="emergencyPhone">
          <el-input v-model="formData.emergencyPhone" placeholder="请输入紧急联系电话" />
        </el-form-item>
        <el-form-item label="健康状况" prop="healthStatus">
          <el-input
            v-model="formData.healthStatus"
            type="textarea"
            :rows="3"
            placeholder='如：{"diseases":["高血压"]}'
          />
        </el-form-item>
        <el-form-item label="过敏史" prop="allergies">
          <el-input
            v-model="formData.allergies"
            type="textarea"
            :rows="3"
            placeholder='如：{"drugAllergies":["青霉素"]}'
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getElders, createElder, updateElder, deleteElder } from '@/api/elder'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增老人')
const formRef = ref()
const tableData = ref([])

const queryForm = reactive({
  name: '',
  nursingLevel: '',
  status: null
})

const pagination = reactive({
  page: 0,
  size: 10,
  total: 0
})

const formData = reactive({
  id: null,
  name: '',
  gender: null,
  birthDate: '',
  idCard: '',
  phone: '',
  address: '',
  nursingLevel: '',
  admissionDate: '',
  emergencyContact: '',
  emergencyPhone: '',
  healthStatus: '',
  allergies: ''
})

const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }]
}

const calculateAge = (birthDate) => {
  if (!birthDate) return '-'
  const birth = new Date(birthDate)
  const now = new Date()
  return now.getFullYear() - birth.getFullYear()
}

const calculateAdmissionDays = (admissionDate) => {
  if (!admissionDate) return '-'
  const admission = new Date(admissionDate)
  const now = new Date()
  const diff = Math.floor((now - admission) / (1000 * 60 * 60 * 24))
  return diff
}

const getStatusText = (status) => {
  const statusMap = { 1: '在院', 2: '出院', 3: '转院' }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = { 1: 'success', 2: 'info', 3: 'warning' }
  return typeMap[status] || ''
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      size: pagination.size
    }
    const res = await getElders(params)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('查询失败:', error)
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  Object.assign(queryForm, {
    name: '',
    nursingLevel: '',
    status: null
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增老人'
  Object.assign(formData, {
    id: null,
    name: '',
    gender: null,
    birthDate: '',
    idCard: '',
    phone: '',
    address: '',
    nursingLevel: '',
    admissionDate: '',
    emergencyContact: '',
    emergencyPhone: '',
    healthStatus: '',
    allergies: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑老人'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该老人档案吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteElder(row.id)
      ElMessage.success('删除成功')
      handleSearch()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (formData.id) {
          await updateElder(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createElder(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        handleSearch()
      } catch (error) {
        console.error('提交失败:', error)
      }
    }
  })
}

onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
.elder-list {
  padding: 20px;
}
</style>
