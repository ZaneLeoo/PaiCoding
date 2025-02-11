import { defineStore } from 'pinia'
import Cookies from 'js-cookie'

export const useUserStore = defineStore('user', {
  state: () => ({
    isLogin: false, // 登录状态
    avatar: ''
  }),
  actions: {
    login(avatar) {
      this.isLogin = true
      this.avatar = avatar
    },
    logout() {
      this.isLogin = false
      Cookies.remove('p-session')
    }
  }
})
