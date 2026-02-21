<template>
  <div class="alarm-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="老人姓名">
          <el-input v-model="queryForm.elderName" placeholder="请输入老人姓名" clearable />
        </el-form-item>
        <el-form-item label="告警类型">
          <el-select v-model="queryForm.alarmType" placeholder="请选择" clearable>
            <el-option label="跌倒" value="跌倒" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="血压异常" value="血压异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="紧急呼叫" value="紧急呼叫" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增告警</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="elderPhone" label="联系电话" width="120" />
        <el-table-column label="告警类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getAlarmTypeColor(row.alarmType)">{{ row.alarmType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alarmLevel" label="告警级别" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.alarmLevel === '紧急'" type="danger">紧急</el-tag>
            <el-tag v-else-if="row.alarmLevel === '重要'" type="warning">重要</el-tag>
            <el-tag v-else type="info">一般</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="告警内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="处理状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : 'success'">
              {{ row.status === 0 ? '待处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleBy" label="处理人" width="100" />
        <el-table-column prop="handleTime" label="处理时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="handleHandle(row)">处理</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="老人ID" prop="elderId">
          <el-input v-model="formData.elderId" placeholder="请输入老人ID" />
        </el-form-item>
        <el-form-item label="告警类型" prop="alarmType">
          <el-select v-model="formData.alarmType" placeholder="请选择">
            <el-option label="跌倒" value="跌倒" />
            <el-option label="心率异常" value="心率异常" />
            <el-option label="血压异常" value="血压异常" />
            <el-option label="血糖异常" value="血糖异常" />
            <el-option label="紧急呼叫" value="紧急呼叫" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="告警级别">
          <el-select v-model="formData.alarmLevel" placeholder="请选择">
            <el-option label="紧急" value="紧急" />
            <el-option label="重要" value="重要" />
            <el-option label="一般" value="一般" />
          </el-select>
        </el-form-item>
        <el-form-item label="告警内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="4" placeholder="请输入告警内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="handleDialogVisible" title="处理告警" width="600px">
      <el-form :model="handleForm" :rules="handleRules" ref="handleFormRef" label-width="100px">
        <el-form-item label="处理人" prop="handleBy">
          <el-input v-model="handleForm.handleBy" placeholder="请输入处理人姓名" />
        </el-form-item>
        <el-form-item label="处理备注" prop="handleNotes">
          <el-input v-model="handleForm.handleNotes" type="textarea" :rows="4" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleHandleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAlarmRecords, createAlarmRecord, updateAlarmRecord, deleteAlarmRecord, handleAlarm } from '@/api/alarm'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const handleDialogVisible = ref(false)
const dialogTitle = ref('新增告警')
const formRef = ref()
const handleFormRef = ref()
const tableData = ref([])
const currentRow = ref(null)

const queryForm = reactive({
  elderName: '',
  alarmType: '',
  status: null
})

const pagination = reactive({
  page: 0,
  size: 10,
  total: 0
})

const formData = reactive({
  id: null,
  elderId: null,
  alarmType: '',
  alarmLevel: '',
  content: ''
})

const handleForm = reactive({
  handleBy: '',
  handleNotes: ''
})

const formRules = {
  elderId: [{ required: true, message: '请输入老人ID', trigger: 'blur' }],
  alarmType: [{ required: true, message: '请选择告警类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入告警内容', trigger: 'blur' }]
}

const handleRules = {
  handleBy: [{ required: true, message: '请输入处理人姓名', trigger: 'blur' }]
}

const getAlarmTypeColor = (type) => {
  const colorMap = {
    '跌倒': 'danger',
    '心率异常': 'warning',
    '血压异常': 'warning',
    '血糖异常': 'warning',
    '紧急呼叫': 'danger',
    '其他': 'info'
  }
  return colorMap[type] || 'info'
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      size: pagination.size
    }
    const res = await getAlarmRecords(params)
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
    alarmType: '',
    status: null
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增告警'
  Object.assign(formData, {
    id: null,
    elderId: null,
    alarmType: '',
    alarmLevel: '',
    content: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑告警'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleHandle = (row) => {
  currentRow.value = row
  Object.assign(handleForm, {
    handleBy: '',
    handleNotes: ''
  })
  handleDialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该告警记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAlarmRecord(row.id)
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
          await updateAlarmRecord(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createAlarmRecord(formData)
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

const handleHandleSubmit = async () => {
  await handleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await handleAlarm(currentRow.value.id, handleForm)
        ElMessage.success('处理成功')
        handleDialogVisible.value = false
        handleSearch()
      } catch (error) {
        console.error('处理失败:', error)
      }
    }
  })
}

onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
.alarm-list {
  padding: 20px;
}
</style>
