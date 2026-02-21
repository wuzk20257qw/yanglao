<template>
  <div class="dining-record-list">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="老人">
          <el-select v-model="queryParams.elderId" placeholder="选择老人" clearable style="width: 200px">
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name" :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="餐次">
          <el-select v-model="queryParams.mealType" placeholder="选择餐次" clearable style="width: 150px">
            <el-option label="早餐" value="breakfast" />
            <el-option label="午餐" value="lunch" />
            <el-option label="晚餐" value="dinner" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="未用餐" :value="0" />
            <el-option label="已用餐" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增餐饮记录</el-button>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="elderName" label="老人姓名" width="120" />
        <el-table-column prop="mealType" label="餐次" width="100">
          <template #default="{ row }">
            <span v-if="row.mealType === 'breakfast'">早餐</span>
            <span v-else-if="row.mealType === 'lunch'">午餐</span>
            <span v-else-if="row.mealType === 'dinner'">晚餐</span>
            <span v-else>{{ row.mealType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="mealName" label="餐品名称" width="150" />
        <el-table-column prop="nutritionLevel" label="营养级别" width="100" />
        <el-table-column prop="diningDate" label="用餐日期" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已用餐' : '未用餐' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" min-width="150" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
        style="margin-top: 16px; text-align: right"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="老人" prop="elderId">
          <el-select v-model="formData.elderId" placeholder="选择老人" style="width: 100%">
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name" :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="餐次" prop="mealType">
          <el-select v-model="formData.mealType" placeholder="选择餐次" style="width: 100%">
            <el-option label="早餐" value="breakfast" />
            <el-option label="午餐" value="lunch" />
            <el-option label="晚餐" value="dinner" />
          </el-select>
        </el-form-item>
        <el-form-item label="餐品名称" prop="mealName">
          <el-input v-model="formData.mealName" placeholder="请输入餐品名称" />
        </el-form-item>
        <el-form-item label="营养级别" prop="nutritionLevel">
          <el-select v-model="formData.nutritionLevel" placeholder="选择营养级别" style="width: 100%">
            <el-option label="普通餐" value="normal" />
            <el-option label="软食" value="soft" />
            <el-option label="半流质" value="semi-liquid" />
            <el-option label="流质" value="liquid" />
            <el-option label="低盐" value="low-salt" />
            <el-option label="低糖" value="low-sugar" />
          </el-select>
        </el-form-item>
        <el-form-item label="用餐日期" prop="diningDate">
          <el-date-picker
            v-model="formData.diningDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="选择状态" style="width: 100%">
            <el-option label="未用餐" :value="0" />
            <el-option label="已用餐" :value="1" />
          </el-select>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDiningRecords, createDiningRecord, updateDiningRecord, deleteDiningRecord } from '@/api/dining'
import { getElders } from '@/api/elder'

const queryParams = reactive({
  elderId: null,
  mealType: '',
  status: null,
  page: 0,
  size: 10
})

const tableData = ref([])
const elderList = ref([])
const total = ref(0)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增餐饮记录')
const formRef = ref()
const formData = reactive({
  id: null,
  elderId: null,
  mealType: '',
  mealName: '',
  nutritionLevel: '',
  diningDate: '',
  status: 0,
  notes: ''
})

const formRules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  mealType: [{ required: true, message: '请选择餐次', trigger: 'change' }],
  mealName: [{ required: true, message: '请输入餐品名称', trigger: 'blur' }],
  diningDate: [{ required: true, message: '请选择用餐日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const loadElders = async () => {
  try {
    const { data } = await getElders()
    elderList.value = data.content
  } catch (error) {
    console.error('加载老人列表失败', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getDiningRecords(queryParams)
    tableData.value = data.content
    total.value = data.totalElements
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  queryParams.elderId = null
  queryParams.mealType = ''
  queryParams.status = null
  queryParams.page = 0
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增餐饮记录'
  Object.assign(formData, {
    id: null,
    elderId: null,
    mealType: '',
    mealName: '',
    nutritionLevel: '',
    diningDate: '',
    status: 0,
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑餐饮记录'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (formData.id) {
      await updateDiningRecord(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createDiningRecord(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDiningRecord(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  loadElders()
  loadData()
})
</script>

<style scoped>
.dining-record-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 16px;
}
</style>
