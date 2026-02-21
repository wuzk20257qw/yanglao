import request from '@/utils/request'

export function getNursingRecords(params) {
  return request({
    url: '/nursing-records',
    method: 'get',
    params
  })
}

export function getNursingRecordById(id) {
  return request({
    url: `/nursing-records/${id}`,
    method: 'get'
  })
}

export function createNursingRecord(data) {
  return request({
    url: '/nursing-records',
    method: 'post',
    data
  })
}

export function updateNursingRecord(id, data) {
  return request({
    url: `/nursing-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteNursingRecord(id) {
  return request({
    url: `/nursing-records/${id}`,
    method: 'delete'
  })
}
