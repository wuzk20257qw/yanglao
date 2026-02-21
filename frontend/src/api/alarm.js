import request from '@/utils/request'

export function getAlarmRecords(params) {
  return request({
    url: '/alarm-records',
    method: 'get',
    params
  })
}

export function getAlarmRecordById(id) {
  return request({
    url: `/alarm-records/${id}`,
    method: 'get'
  })
}

export function createAlarmRecord(data) {
  return request({
    url: '/alarm-records',
    method: 'post',
    data
  })
}

export function updateAlarmRecord(id, data) {
  return request({
    url: `/alarm-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteAlarmRecord(id) {
  return request({
    url: `/alarm-records/${id}`,
    method: 'delete'
  })
}

export function handleAlarm(id, data) {
  return request({
    url: `/alarm-records/${id}/handle`,
    method: 'post',
    data
  })
}
