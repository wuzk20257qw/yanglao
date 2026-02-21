<template>
  <div class="fee-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="老人姓名">
          <el-input v-model="queryForm.elderName" placeholder="请输入老人姓名" clearable />
        </el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="queryForm.feeType" placeholder="请选择" clearable>
            <el-option label="床位费" value="床位费" />
            <el-option label="护理费" value="护理费" />
            <el-option label="餐饮费" value="餐饮费" />
            <el-option label="医疗费" value="医疗费" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="缴费状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="未缴费" :value="0" />
            <el-option label="已缴费" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增费用</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="feeType" label="费用类型" width="100" />
        <el-table-column prop="feeName" label="费用名称" width="120" />
        <el-table-column prop="amount" label="金额" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="period" label="费用周期" width="120" />
        <el-table-column label="缴费状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="payTime" label="缴费时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="handlePay(row)">缴费</el-button>
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
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="formData.feeType" placeholder="请选择">
            <el-option label="床位费" value="床位费" />
            <el-option label="护理费" value="护理费" />
            <el-option label="餐饮费" value="餐饮费" />
            <el-option label="医疗费" value="医疗费" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用名称" prop="feeName">
          <el-input v-model="formData.feeName" placeholder="请输入费用名称" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="formData.amount" placeholder="请输入金额" type="number" step="0.01" />
        </el-form-item>
        <el-form-item label="费用周期">
          <el-input v-model="formData.period" placeholder="如：2024-01" />
        </el-form-item>
        <el-form-item label="备注">
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
import { getFeeRecords, createFeeRecord, updateFeeRecord, deleteFeeRecord, payFee } from '@/api/fee'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增费用')
const formRef = ref()
const tableData = ref([])

const queryForm = reactive({
  elderName: '',
  feeType: '',
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
  feeType: '',
  feeName: '',
  amount: null,
  period: '',
  notes: ''
})

const formRules = {
  elderId: [{ required: true, message: '请输入老人ID', trigger: 'blur' }],
  feeType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  feeName: [{ required: true, message: '请输入费用名称', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }]
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      size: pagination.size
    }
    const res = await getFeeRecords(params)
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
    feeType: '',
    status: null
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增费用'
  Object.assign(formData, {
    id: null,
    elderId: null,
    feeType: '',
    feeName: '',
    amount: null,
    period: '',
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑费用'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handlePay = (row) => {
  ElMessageBox.confirm(`确定要缴纳 ¥${row.amount?.toFixed(2)} 吗？`, '确认缴费', {
    type: 'warning'
  }).then(async () => {
    try {
      await payFee(row.id)
      ElMessage.success('缴费成功')
      handleSearch()
    } catch (error) {
      console.error('缴费失败:', error)
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该费用记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFeeRecord(row.id)
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
          await updateFeeRecord(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createFeeRecord(formData)
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
.fee-list {
  padding: 20px;
}
</style>
