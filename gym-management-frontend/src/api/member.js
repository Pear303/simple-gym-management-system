import request from './index'

export function getMemberList(params) {
  return request({
    url: '/member/list',
    method: 'get',
    params
  })
}

export function addMember(data) {
  return request({
    url: '/member/add',
    method: 'post',
    data
  })
}

export function updateMember(data) {
  return request({
    url: '/member/update',
    method: 'put',
    data
  })
}

export function deleteMember(id) {
  return request({
    url: `/member/delete/${id}`,
    method: 'delete'
  })
}

export function searchMemberById(id) {
  return request({
    url: `/member/search/id/${id}`,
    method: 'get'
  })
}