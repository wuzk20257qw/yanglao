import request from '@/utils/request'

export function getFeeRecords(params) {
  return request({
    url: '/fee-records',
    method: 'get',
    params
  })
}

export function getFeeRecordById(id) {
  return request({
    url: `/fee-records/${id}`,
    method: 'get'
  })
}

export function createFeeRecord(data) {
  return request({
    url: '/fee-records',
    method: 'post',
    data
  })
}

export function updateFeeRecord(id, data) {
  return request({
    url: `/fee-records/${id}`,
    method: 'put',
    data
  })
}

export function deleteFeeRecord(id) {
  return request({
    url: `/fee-records/${id}`,
    method: 'delete'
  })
}

export function payFee(id) {
  return request({
    url: `/fee-records/${id}/pay`,
    method: 'post'
  })
}

export function getElderFees(elderId) {
  return request({
    url: `/fee-records/elder/${elderId}`,
    method: 'get'
  })
}

export function getFeeTypes() {
  return request({
    url: '/fee-types',
    method: 'get'
  })
}
