<template>
  <div class="activity-list">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="活动类型">
          <el-select v-model="queryParams.activityType" placeholder="选择活动类型" clearable style="width: 150px">
            <el-option label="健身活动" value="fitness" />
            <el-option label="文娱活动" value="entertainment" />
            <el-option label="教育讲座" value="education" />
            <el-option label="外出活动" value="outing" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动日期">
          <el-date-picker
            v-model="queryParams.activityDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增活动</el-button>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="name" label="活动名称" width="180" />
        <el-table-column prop="activityType" label="活动类型" width="100">
          <template #default="{ row }">
            <span v-if="row.activityType === 'fitness'">健身活动</span>
            <span v-else-if="row.activityType === 'entertainment'">文娱活动</span>
            <span v-else-if="row.activityType === 'education'">教育讲座</span>
            <span v-else-if="row.activityType === 'outing'">外出活动</span>
            <span v-else>{{ row.activityType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="activityDate" label="活动日期" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="80" />
        <el-table-column prop="endTime" label="结束时间" width="80" />
        <el-table-column prop="location" label="地点" width="120" />
        <el-table-column label="报名人数" width="100">
          <template #default="{ row }">
            {{ row.currentParticipants }} / {{ row.maxParticipants || '不限' }}
          </template>
        </el-table-column>
        <el-table-column prop="feeAmount" label="费用" width="80">
          <template #default="{ row }">
            {{ row.feeAmount ? `¥${row.feeAmount}` : '免费' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '进行中' : '已结束' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEnrollments(row)">报名</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动类型" prop="activityType">
          <el-select v-model="formData.activityType" placeholder="选择活动类型" style="width: 100%">
            <el-option label="健身活动" value="fitness" />
            <el-option label="文娱活动" value="entertainment" />
            <el-option label="教育讲座" value="education" />
            <el-option label="外出活动" value="outing" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入活动描述" />
        </el-form-item>
        <el-form-item label="活动日期" prop="activityDate">
          <el-date-picker
            v-model="formData.activityDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="startTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 100%"
            @change="formData.startTime = $event"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="endTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 100%"
            @change="formData.endTime = $event"
          />
        </el-form-item>
        <el-form-item label="活动地点">
          <el-input v-model="formData.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="最大人数">
          <el-input-number v-model="formData.maxParticipants" :min="0" placeholder="不限则留空" style="width: 100%" />
        </el-form-item>
        <el-form-item label="活动费用">
          <el-input-number v-model="formData.feeAmount" :min="0" :precision="2" placeholder="免费则留空" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="选择状态" style="width: 100%">
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="enrollmentDialogVisible" title="活动报名" width="800px">
      <el-button type="primary" @click="handleAddEnrollment" style="margin-bottom: 16px">新增报名</el-button>
      <el-table :data="enrollmentData" border stripe v-loading="enrollmentLoading">
        <el-table-column prop="elderName" label="老人姓名" width="120" />
        <el-table-column prop="enrollmentTime" label="报名时间" width="180" />
        <el-table-column prop="notes" label="备注" min-width="150" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已报名' : '已取消' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button v-if="row.status === 1" link type="danger" size="small" @click="handleCancelEnrollment(row)">取消报名</el-button>
            <el-button link type="danger" size="small" @click="handleDeleteEnrollment(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="enrollmentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="enrollDialogVisible" title="新增报名" width="500px">
      <el-form :model="enrollFormData" :rules="enrollFormRules" ref="enrollFormRef" label-width="100px">
        <el-form-item label="老人" prop="elderId">
          <el-select v-model="enrollFormData.elderId" placeholder="选择老人" style="width: 100%">
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name" :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="enrollFormData.notes" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="enrollDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEnrollSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getActivities,
  createActivity,
  updateActivity,
  deleteActivity,
  getEnrollments,
  enrollActivity,
  cancelEnrollment,
  deleteEnrollment
} from '@/api/activity'
import { getElders } from '@/api/elder'

const queryParams = reactive({
  activityType: '',
  activityDate: '',
  status: null,
  page: 0,
  size: 10
})

const tableData = ref([])
const elderList = ref([])
const total = ref(0)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增活动')
const formRef = ref()
const startTime = ref('')
const endTime = ref('')
const formData = reactive({
  id: null,
  name: '',
  description: '',
  activityType: '',
  activityDate: '',
  startTime: '',
  endTime: '',
  location: '',
  maxParticipants: null,
  feeAmount: null,
  status: 1
})

const formRules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  activityType: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  activityDate: [{ required: true, message: '请选择活动日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const enrollmentDialogVisible = ref(false)
const enrollmentLoading = ref(false)
const enrollmentData = ref([])
const currentActivityId = ref(null)

const enrollDialogVisible = ref(false)
const enrollFormRef = ref()
const enrollFormData = reactive({
  activityId: null,
  elderId: null,
  notes: ''
})

const enrollFormRules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }]
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
    const { data } = await getActivities(queryParams)
    tableData.value = data.content
    total.value = data.totalElements
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadEnrollments = async (activityId) => {
  enrollmentLoading.value = true
  try {
    const { data } = await getEnrollments({ activityId })
    enrollmentData.value = data.content
  } catch (error) {
    ElMessage.error('加载报名数据失败')
  } finally {
    enrollmentLoading.value = false
  }
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  queryParams.activityType = ''
  queryParams.activityDate = ''
  queryParams.status = null
  queryParams.page = 0
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增活动'
  startTime.value = ''
  endTime.value = ''
  Object.assign(formData, {
    id: null,
    name: '',
    description: '',
    activityType: '',
    activityDate: '',
    startTime: '',
    endTime: '',
    location: '',
    maxParticipants: null,
    feeAmount: null,
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  startTime.value = row.startTime || ''
  endTime.value = row.endTime || ''
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    formData.startTime = startTime.value
    formData.endTime = endTime.value
    if (formData.id) {
      await updateActivity(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createActivity(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这个活动吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteActivity(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleEnrollments = (row) => {
  currentActivityId.value = row.id
  enrollmentDialogVisible.value = true
  loadEnrollments(row.id)
}

const handleAddEnrollment = () => {
  enrollFormData.activityId = currentActivityId.value
  enrollFormData.elderId = null
  enrollFormData.notes = ''
  enrollDialogVisible.value = true
}

const handleEnrollSubmit = async () => {
  await enrollFormRef.value.validate()
  try {
    await enrollActivity(enrollFormData)
    ElMessage.success('报名成功')
    enrollDialogVisible.value = false
    loadEnrollments(currentActivityId.value)
    loadData()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '报名失败')
  }
}

const handleCancelEnrollment = (row) => {
  ElMessageBox.confirm('确定要取消这个报名吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelEnrollment(row.id)
      ElMessage.success('取消成功')
      loadEnrollments(currentActivityId.value)
      loadData()
    } catch (error) {
      ElMessage.error('取消失败')
    }
  })
}

const handleDeleteEnrollment = (row) => {
  ElMessageBox.confirm('确定要删除这条报名记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteEnrollment(row.id)
      ElMessage.success('删除成功')
      loadEnrollments(currentActivityId.value)
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
.activity-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 16px;
}
</style>
