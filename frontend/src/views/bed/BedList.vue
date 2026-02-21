<template>
  <div class="bed-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="床位号">
          <el-input v-model="queryForm.bedNo" placeholder="请输入床位号" clearable />
        </el-form-item>
        <el-form-item label="楼层">
          <el-select v-model="queryForm.floor" placeholder="请选择" clearable>
            <el-option v-for="f in [1,2,3,4,5]" :key="f" :label="f + '楼'" :value="f" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="空闲" :value="0" />
            <el-option label="已占用" :value="1" />
            <el-option label="维护中" :value="2" />
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
        <el-table-column prop="bedNo" label="床位号" width="100" />
        <el-table-column label="楼层" width="80">
          <template #default="{ row }">
            {{ row.floor }}楼
          </template>
        </el-table-column>
        <el-table-column prop="area" label="区域" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="占用老人" width="120">
          <template #default="{ row }">
            {{ row.elderName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 1" type="warning" size="small" @click="handleRelease(row)">释放</el-button>
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
        <el-form-item label="床位号" prop="bedNo">
          <el-input v-model="formData.bedNo" placeholder="如：101" />
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-select v-model="formData.floor" placeholder="请选择">
            <el-option v-for="f in [1,2,3,4,5]" :key="f" :label="f + '楼'" :value="f" />
          </el-select>
        </el-form-item>
        <el-form-item label="区域" prop="area">
          <el-input v-model="formData.area" placeholder="如：A区" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择">
            <el-option label="空闲" :value="0" />
            <el-option label="已占用" :value="1" />
            <el-option label="维护中" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { getBeds, createBed, updateBed, deleteBed, updateBedStatus } from '@/api/bed'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增床位')
const formRef = ref()
const tableData = ref([])

const queryForm = reactive({
  bedNo: '',
  floor: null,
  status: null
})

const pagination = reactive({
  page: 0,
  size: 10,
  total: 0
})

const formData = reactive({
  id: null,
  bedNo: '',
  floor: null,
  area: '',
  status: 0,
  remark: ''
})

const formRules = {
  bedNo: [{ required: true, message: '请输入床位号', trigger: 'blur' }],
  floor: [{ required: true, message: '请选择楼层', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getStatusText = (status) => {
  const statusMap = { 0: '空闲', 1: '已占用', 2: '维护中' }
  return statusMap[status] || '-'
}

const getStatusType = (status) => {
  const typeMap = { 0: 'success', 1: 'warning', 2: 'danger' }
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
    const res = await getBeds(params)
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
    bedNo: '',
    floor: null,
    status: null
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增床位'
  Object.assign(formData, {
    id: null,
    bedNo: '',
    floor: null,
    area: '',
    status: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑床位'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleRelease = (row) => {
  ElMessageBox.confirm('确定要释放该床位吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await updateBedStatus(row.id, 0)
      ElMessage.success('释放成功')
      handleSearch()
    } catch (error) {
      console.error('释放失败:', error)
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该床位吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBed(row.id)
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
          await updateBed(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createBed(formData)
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
.bed-list {
  padding: 20px;
}
</style>
