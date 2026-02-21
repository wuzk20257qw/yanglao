import request from '@/utils/request'

export const getDiningRecords = (params) => {
  return request({
    url: '/dining-records',
    method: 'get',
    params
  })
}

export const getDiningRecordById = (id) => {
  return request({
    url: `/dining-records/${id}`,
    method: 'get'
  })
}

export const createDiningRecord = (data) => {
  return request({
    url: '/dining-records',
    method: 'post',
    data
  })
}

export const updateDiningRecord = (id, data) => {
  return request({
    url: `/dining-records/${id}`,
    method: 'put',
    data
  })
}

export const deleteDiningRecord = (id) => {
  return request({
    url: `/dining-records/${id}`,
    method: 'delete'
  })
}
