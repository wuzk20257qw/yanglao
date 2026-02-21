<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="(stat, index) in statsData" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="24"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>床位使用情况</span>
          </template>
          <el-progress type="dashboard" :percentage="bedUsagePercentage" :color="bedUsageColor">
            <template #default="{ percentage }">
              <span class="percentage-value">{{ percentage }}%</span>
              <span class="percentage-label">床位利用率</span>
            </template>
          </el-progress>
          <div class="bed-info" style="margin-top: 20px">
            <el-row>
              <el-col :span="12">
                <div class="info-item">
                  <div class="info-label">已用床位</div>
                  <div class="info-value">{{ stats.occupiedBedCount }}</div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="info-item">
                  <div class="info-label">空闲床位</div>
                  <div class="info-value">{{ stats.bedCount - stats.occupiedBedCount }}</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>告警处理情况</span>
          </template>
          <el-row>
            <el-col :span="12">
              <div class="alarm-item pending">
                <div class="alarm-label">待处理告警</div>
                <div class="alarm-value">{{ stats.pendingAlarmCount }}</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="alarm-item handled">
                <div class="alarm-label">已处理告警</div>
                <div class="alarm-value">{{ stats.handledAlarmCount }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>健康记录</span>
          </template>
          <div class="record-count">{{ stats.healthRecordCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>护理记录</span>
          </template>
          <div class="record-count">{{ stats.nursingRecordCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>餐饮记录</span>
          </template>
          <div class="record-count">{{ stats.diningRecordCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>活动数量</span>
          </template>
          <div class="record-count">{{ stats.activityCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>排班数量</span>
          </template>
          <div class="record-count">{{ stats.shiftScheduleCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>费用记录</span>
          </template>
          <div class="record-count">{{ stats.feeRecordCount }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { User, Grid, TrendCharts, ChatLineSquare, Warning, Coin, Bowl, Clock, Trophy } from '@element-plus/icons-vue'
import { getDashboardStats } from '@/api/dashboard'

const stats = reactive({
  elderCount: 0,
  bedCount: 0,
  occupiedBedCount: 0,
  healthRecordCount: 0,
  nursingRecordCount: 0,
  pendingAlarmCount: 0,
  handledAlarmCount: 0,
  feeRecordCount: 0,
  diningRecordCount: 0,
  shiftScheduleCount: 0,
  activityCount: 0,
  userCount: 0
})

const statsData = computed(() => [
  {
    label: '老人总数',
    value: stats.elderCount,
    icon: User,
    color: '#409EFF'
  },
  {
    label: '床位总数',
    value: stats.bedCount,
    icon: Grid,
    color: '#67C23A'
  },
  {
    label: '用户数量',
    value: stats.userCount,
    icon: TrendCharts,
    color: '#E6A23C'
  },
  {
    label: '活动数量',
    value: stats.activityCount,
    icon: Trophy,
    color: '#F56C6C'
  }
])

const bedUsagePercentage = computed(() => {
  if (stats.bedCount === 0) return 0
  return Math.round((stats.occupiedBedCount / stats.bedCount) * 100)
})

const bedUsageColor = computed(() => {
  const percentage = bedUsagePercentage.value
  if (percentage >= 90) return '#F56C6C'
  if (percentage >= 70) return '#E6A23C'
  return '#67C23A'
})

const loadData = async () => {
  try {
    const { data } = await getDashboardStats()
    Object.assign(stats, data)
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.percentage-value {
  display: block;
  font-size: 28px;
  font-weight: bold;
}

.percentage-label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.bed-info {
  padding: 0 20px;
}

.info-item {
  text-align: center;
  padding: 10px 0;
}

.info-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.info-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.alarm-item {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
}

.alarm-item.pending {
  background-color: #FEF0F0;
}

.alarm-item.handled {
  background-color: #F0F9FF;
}

.alarm-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.alarm-value {
  font-size: 32px;
  font-weight: bold;
}

.alarm-item.pending .alarm-value {
  color: #F56C6C;
}

.alarm-item.handled .alarm-value {
  color: #409EFF;
}

.record-count {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  padding: 20px 0;
}
</style>
