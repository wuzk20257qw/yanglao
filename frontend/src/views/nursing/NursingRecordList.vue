<template>
  <div class="nursing-list">
    <el-card>
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="老人姓名">
          <el-input v-model="queryForm.elderName" placeholder="请输入老人姓名" clearable />
        </el-form-item>
        <el-form-item label="护理类型">
          <el-select v-model="queryForm.nursingType" placeholder="请选择" clearable>
            <el-option label="日常护理" value="日常护理" />
            <el-option label="医疗护理" value="医疗护理" />
            <el-option label="康复护理" value="康复护理" />
            <el-option label="心理护理" value="心理护理" />
            <el-option label="饮食护理" value="饮食护理" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理等级">
          <el-select v-model="queryForm.nursingLevel" placeholder="请选择" clearable>
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
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
        <el-table-column prop="nursingType" label="护理类型" width="100" />
        <el-table-column prop="nursingLevel" label="护理等级" width="80" />
        <el-table-column prop="content" label="护理内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="notes" label="备注" min-width="150" show-overflow-tooltip />
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
        <el-form-item label="护理类型" prop="nursingType">
          <el-select v-model="formData.nursingType" placeholder="请选择">
            <el-option label="日常护理" value="日常护理" />
            <el-option label="医疗护理" value="医疗护理" />
            <el-option label="康复护理" value="康复护理" />
            <el-option label="心理护理" value="心理护理" />
            <el-option label="饮食护理" value="饮食护理" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理等级">
          <el-select v-model="formData.nursingLevel" placeholder="请选择">
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
          </el-select>
        </el-form-item>
        <el-form-item label="护理内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="4" placeholder="请输入护理内容" />
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
import { getNursingRecords, createNursingRecord, updateNursingRecord, deleteNursingRecord } from '@/api/nursing'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增护理记录')
const formRef = ref()
const tableData = ref([])

const queryForm = reactive({
  elderName: '',
  nursingType: '',
  nursingLevel: ''
})

const pagination = reactive({
  page: 0,
  size: 10,
  total: 0
})

const formData = reactive({
  id: null,
  elderId: null,
  nursingType: '',
  nursingLevel: '',
  content: '',
  notes: ''
})

const formRules = {
  elderId: [{ required: true, message: '请输入老人ID', trigger: 'blur' }],
  nursingType: [{ required: true, message: '请选择护理类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入护理内容', trigger: 'blur' }]
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      page: pagination.page,
      size: pagination.size
    }
    const res = await getNursingRecords(params)
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
    nursingType: '',
    nursingLevel: ''
  })
  pagination.page = 0
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增护理记录'
  Object.assign(formData, {
    id: null,
    elderId: null,
    nursingType: '',
    nursingLevel: '',
    content: '',
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑护理记录'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该护理记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteNursingRecord(row.id)
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
          await updateNursingRecord(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createNursingRecord(formData)
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
.nursing-list {
  padding: 20px;
}
</style>
