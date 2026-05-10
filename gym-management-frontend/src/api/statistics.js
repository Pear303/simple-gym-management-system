import request from './index'

export function getMemberCount() {
  return request({
    url: '/member/total-count',
    method: 'get'
  })
}

export function getEmployeeCount() {
  return request({
    url: '/employee/total-count',
    method: 'get'
  })
}