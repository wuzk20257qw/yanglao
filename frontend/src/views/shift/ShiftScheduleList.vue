<template>
  <div class="shift-schedule-list">
    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="护士">
          <el-select v-model="queryParams.nurseId" placeholder="选择护士" clearable style="width: 200px">
            <el-option v-for="nurse in nurseList" :key="nurse.id" :label="nurse.username" :value="nurse.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="班次">
          <el-select v-model="queryParams.shiftType" placeholder="选择班次" clearable style="width: 150px">
            <el-option label="早班" value="morning" />
            <el-option label="中班" value="afternoon" />
            <el-option label="晚班" value="night" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="queryParams.shiftDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="取消" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增排班</el-button>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="nurseName" label="护士姓名" width="120" />
        <el-table-column prop="shiftType" label="班次" width="100">
          <template #default="{ row }">
            <span v-if="row.shiftType === 'morning'">早班</span>
            <span v-else-if="row.shiftType === 'afternoon'">中班</span>
            <span v-else-if="row.shiftType === 'night'">晚班</span>
            <span v-else>{{ row.shiftType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="shiftDate" label="排班日期" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="100" />
        <el-table-column prop="endTime" label="结束时间" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '取消' }}
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
        <el-form-item label="护士" prop="nurseId">
          <el-select v-model="formData.nurseId" placeholder="选择护士" style="width: 100%">
            <el-option v-for="nurse in nurseList" :key="nurse.id" :label="nurse.username" :value="nurse.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="班次" prop="shiftType">
          <el-select v-model="formData.shiftType" placeholder="选择班次" style="width: 100%">
            <el-option label="早班" value="morning" />
            <el-option label="中班" value="afternoon" />
            <el-option label="晚班" value="night" />
          </el-select>
        </el-form-item>
        <el-form-item label="排班日期" prop="shiftDate">
          <el-date-picker
            v-model="formData.shiftDate"
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
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="选择状态" style="width: 100%">
            <el-option label="正常" :value="1" />
            <el-option label="取消" :value="0" />
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
import { getShiftSchedules, createShiftSchedule, updateShiftSchedule, deleteShiftSchedule } from '@/api/shift'
import { getUsers } from '@/api/user'

const queryParams = reactive({
  nurseId: null,
  shiftType: '',
  shiftDate: '',
  status: null,
  page: 0,
  size: 10
})

const tableData = ref([])
const nurseList = ref([])
const total = ref(0)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增排班')
const formRef = ref()
const startTime = ref('')
const endTime = ref('')
const formData = reactive({
  id: null,
  nurseId: null,
  shiftType: '',
  shiftDate: '',
  startTime: '',
  endTime: '',
  status: 1,
  notes: ''
})

const formRules = {
  nurseId: [{ required: true, message: '请选择护士', trigger: 'change' }],
  shiftType: [{ required: true, message: '请选择班次', trigger: 'change' }],
  shiftDate: [{ required: true, message: '请选择排班日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const loadNurses = async () => {
  try {
    const { data } = await getUsers()
    nurseList.value = data.content
  } catch (error) {
    console.error('加载护士列表失败', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getShiftSchedules(queryParams)
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
  queryParams.nurseId = null
  queryParams.shiftType = ''
  queryParams.shiftDate = ''
  queryParams.status = null
  queryParams.page = 0
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增排班'
  startTime.value = ''
  endTime.value = ''
  Object.assign(formData, {
    id: null,
    nurseId: null,
    shiftType: '',
    shiftDate: '',
    startTime: '',
    endTime: '',
    status: 1,
    notes: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑排班'
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
      await updateShiftSchedule(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await createShiftSchedule(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条排班记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteShiftSchedule(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  loadNurses()
  loadData()
})
</script>

<style scoped>
.shift-schedule-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 16px;
}
</style>
