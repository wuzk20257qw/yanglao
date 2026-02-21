<template>
  <div class="layout-container">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h2>养老管理系统</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-icon><User /></el-icon>
              {{ userInfo?.realName || '用户' }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu
            :default-active="activeMenu"
            router
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-menu-item index="/dashboard">
              <el-icon><TrendCharts /></el-icon>
              <span>数据看板</span>
            </el-menu-item>
            <el-menu-item index="/elders">
              <el-icon><User /></el-icon>
              <span>老人管理</span>
            </el-menu-item>
            <el-menu-item index="/beds">
              <el-icon><Grid /></el-icon>
              <span>床位管理</span>
            </el-menu-item>
            <el-menu-item index="/health">
              <el-icon><Monitor /></el-icon>
              <span>健康管理</span>
            </el-menu-item>
            <el-menu-item index="/nursing">
              <el-icon><Notebook /></el-icon>
              <span>护理记录</span>
            </el-menu-item>
            <el-menu-item index="/alarms">
              <el-icon><Warning /></el-icon>
              <span>告警管理</span>
            </el-menu-item>
            <el-menu-item index="/fees">
              <el-icon><Coin /></el-icon>
              <span>费用管理</span>
            </el-menu-item>
            <el-menu-item index="/users">
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/dining">
              <el-icon><Bowl /></el-icon>
              <span>餐饮管理</span>
            </el-menu-item>
            <el-menu-item index="/shifts">
              <el-icon><Clock /></el-icon>
              <span>排班管理</span>
            </el-menu-item>
            <el-menu-item index="/activities">
              <el-icon><Trophy /></el-icon>
              <span>活动管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserInfo, removeUserInfo, removeToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const userInfo = computed(() => getUserInfo())

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    removeToken()
    removeUserInfo()
    ElMessage.success('退出登录成功')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background-color: #545c64;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
}

.el-dropdown-link span {
  margin-left: 5px;
}

.aside {
  background-color: #545c64;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
