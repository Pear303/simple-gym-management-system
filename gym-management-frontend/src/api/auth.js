import request from './index'

export function adminLogin(data) {
  return request({
    url: '/admin-login',
    method: 'post',
    data
  })
}

export function getAdminMainPage() {
  return request({
    url: '/admin-main-page',
    method: 'post'
  })
}