import request from './index'

export function getEmployeeList(params) {
  return request({
    url: '/employee/list',
    method: 'get',
    params
  })
}

export function searchEmployeeByRegex(field, value) {
  console.log('[API] POST /employee/search/regex', JSON.stringify({ field, value }))
  return request({
    url: '/employee/search/regex',
    method: 'post',
    data: { field, value }
  })
}

export function addEmployee(data) {
  return request({
    url: '/employee/add',
    method: 'post',
    data
  })
}

export function updateEmployee(data) {
  return request({
    url: '/employee/update',
    method: 'put',
    data
  })
}

export function deleteEmployee(id) {
  return request({
    url: `/employee/delete/${id}`,
    method: 'delete'
  })
}