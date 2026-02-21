import { defineStore } from 'pinia'
import { login as loginApi, getCurrentUser } from '@/api/auth'
import { setToken, removeToken, setUserInfo, removeUserInfo } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token
  },

  actions: {
    async login(loginForm) {
      const res = await loginApi(loginForm)
      this.token = res.data.token
      this.userInfo = res.data.userInfo

      setToken(res.data.token)
      setUserInfo(res.data.userInfo)
    },

    async getUserInfo() {
      const res = await getCurrentUser()
      this.userInfo = res.data
      setUserInfo(res.data)
    },

    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
      removeUserInfo()
    }
  }
})
