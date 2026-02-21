import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '数据看板' }
      },
      {
        path: 'elders',
        name: 'Elders',
        component: () => import('@/views/elder/ElderList.vue'),
        meta: { title: '老人管理' }
      },
      {
        path: 'beds',
        name: 'Beds',
        component: () => import('@/views/bed/BedList.vue'),
        meta: { title: '床位管理' }
      },
      {
        path: 'health',
        name: 'Health',
        component: () => import('@/views/health/HealthRecordList.vue'),
        meta: { title: '健康管理' }
      },
      {
        path: 'nursing',
        name: 'Nursing',
        component: () => import('@/views/nursing/NursingRecordList.vue'),
        meta: { title: '护理记录' }
      },
      {
        path: 'alarms',
        name: 'Alarms',
        component: () => import('@/views/alarm/AlarmList.vue'),
        meta: { title: '告警管理' }
      },
      {
        path: 'fees',
        name: 'Fees',
        component: () => import('@/views/fee/FeeRecordList.vue'),
        meta: { title: '费用管理' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/user/UserList.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'dining',
        name: 'Dining',
        component: () => import('@/views/dining/DiningRecordList.vue'),
        meta: { title: '餐饮管理' }
      },
      {
        path: 'shifts',
        name: 'Shifts',
        component: () => import('@/views/shift/ShiftScheduleList.vue'),
        meta: { title: '排班管理' }
      },
      {
        path: 'activities',
        name: 'Activities',
        component: () => import('@/views/activity/ActivityList.vue'),
        meta: { title: '活动管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()

  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
