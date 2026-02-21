import request from '@/utils/request'

export function getHealthRecords(params) {
  return request({
    url: '/health-records',
    method: 'get',
    params
  })
}

export function getHealthRecordById(id) {
  return request({
    url: `/health-records/${id}`,
    method: 'get'
  })
}

export function createHealthRecord(data) {
  return request({
    url: '/health-records',
    method: 'post',
    data
  })
}

export function updateHealthRecord(id, data) {
  return request({
    url: `/health-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteHealthRecord(id) {
  return request({
    url: `/health-records/${id}`,
    method: 'delete'
  })
}
