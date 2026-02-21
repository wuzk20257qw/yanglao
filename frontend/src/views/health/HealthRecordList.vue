<template>
  <div class="health-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="老人姓名">
          <el-input v-model="queryForm.elderName" placeholder="请输入老人姓名" clearable />
        </el-form-item>
        <el-form-item label="记录类型">
          <el-select v-model="queryForm.recordType" placeholder="请选择" clearable>
            <el-option label="血压" value="血压" />
            <el-option label="血糖" value="血糖" />
            <el-option label="心率" value="心率" />
            <el-option label="体温" value="体温" />
            <el-option label="体重" value="体重" />
            <el-option label="综合" value="综合" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增记录</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="recordType" label="记录类型" width="80" />
        <el-table-column label="血压" width="100">
          <template #default="{ row }">
            <span v-if="row.systolicBP && row.diastolicBP">
              {{ row.systolicBP }}/{{ row.diastolicBP }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="heartRate" label="心率" width="80">
          <template #default="{ row }">
            <span :class="{ 'abnormal': row.heartRate && (row.heartRate < 60 || row.heartRate > 100) }">
              {{ row.heartRate || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="bloodSugar" label="血糖" width="80">
          <template #default="{ row }">
            <span :class="{ 'abnormal': row.bloodSugar && (row.bloodSugar < 3.9 || row.bloodSugar > 6.1) }">
              {{ row.bloodSugar || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="体温" width="80">
          <template #default="{ row }">
            <span :class="{ 'abnormal': row.temperature && (row.temperature < 36.1 || row.temperature > 37.2) }">
              {{ row.temperature || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="体重" width="80" />
        <el-table-column prop="medication" label="用药情况" min-width="150" />
        <el-table-column prop="notes" label="备注" min-width="150" />
        <el-table-column prop="recordDate" label="记录日期" width="110" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
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
        <el-form-item label="老人ID" prop="elderId">
          <el-input v-model="formData.elderId" placeholder="请输入老人ID" />
        </el-form-item>
        <el-form-item label="记录类型" prop="recordType">
          <el-select v-model="formData.recordType" placeholder="请选择">
            <el-option label="血压" value="血压" />
            <el-option label="血糖" value="血糖" />
            <el-option label="心率" value="心率" />
            <el-option label="体温" value="体温" />
            <el-option label="体重" value="体重" />
            <el-option label="综合" value="综合" />
          </el-select>
        </el-form-item>
        <el-form-item label="收缩压">
          <el-input v-model="formData.systolicBP" placeholder="收缩压 (mmHg)" type="number" />
        </el-form-item>
        <el-form-item label="舒张压">
          <el-input v-model="formData.diastolicBP" placeholder="舒张压 (mmHg)" type="number" />
        </el-form-item>
        <el-form-item label="心率">
          <el-input v-model="formData.heartRate" placeholder="心率 (次/分钟)" type="number" />
        </el-form-item>
        <el-form-item label="血糖">
          <el-input v-model="formData.bloodSugar" placeholder="血糖 (mmol/L)" type="number" step="0.1" />
        </el-form-item>
        <el-form-item label="体温">
          <el-input v-model="formData.temperature" placeholder="体温 (℃)" type="number" step="0.1" />
        </el-form-item>
        <el-form-item label="体重">
          <el-input v-model="formData.weight" placeholder="体重 (kg)" type="number" step="0.1" />
        </el-form-item>
        <el-form-item label="用药情况" prop="medication">
          <el-input v-model="formData.medication" type="textarea" :rows="2" placeholder="请输入用药情况" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="formData.notes" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { getHealthRecords, createHealthRecord, updateHealthRecord, deleteHealthRecord } from '@/api/health'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增健康记录')
const formRef = ref()
const tableData = ref([])

const queryForm = reactive({
  elderName: '',
  recordType: ''
})

const pagination = reactive({
  page: 0,
  size: 10,
  total: 0
})

const formData = reactive({
  id: null,
  elderId: null,
  recordType: '',
  systolicBP: null,
  diastolicBP: null,
  heartRate: null,
  bloodSugar: null,
  temperature: null,
  weight: null,
  medication: '',
  notes: ''
})

const formRules = {
  elderId: [{ required: true, message: '请输入老人ID', trigger: 'blur' }],
  recordType: [{ required: true, message: '请选择记录类型', trigger: 'change' }]
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      size: pagination.size
    }
    const res = await getHealthRecords(params)
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
    elderName: '',
    recordType: ''
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增健康记录'
  Object.assign(formData, {
    id: null,
    elderId: null,
    recordType: '',
    systolicBP: null,
    diastolicBP: null,
    heartRate: null,
    bloodSugar: null,
    temperature: null,
    weight: null,
    medication: '',
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑健康记录'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该健康记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteHealthRecord(row.id)
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
          await updateHealthRecord(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createHealthRecord(formData)
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
.health-list {
  padding: 20px;
}
.abnormal {
  color: #f56c6c;
  font-weight: bold;
}
</style>
