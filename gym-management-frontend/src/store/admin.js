import { defineStore } from 'pinia'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    adminInfo: JSON.parse(localStorage.getItem('adminInfo')) || null
  }),

  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)

      console.log('Token 已设置:', token)
    },
    setAdminInfo(adminInfo) {
      this.adminInfo = adminInfo
      localStorage.setItem('adminInfo', JSON.stringify(adminInfo))
      
      console.log('AdminInfo 已设置:', adminInfo)
    },
    logout() {
      this.token = ''
      this.adminInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('adminInfo')
    }
  }
})
