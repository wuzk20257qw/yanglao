import request from '@/utils/request'

export function getFeeRecords(params) {
  return request({
    url: '/fees',
    method: 'get',
    params
  })
}

export function getFeeRecordById(id) {
  return request({
    url: `/fees/${id}`,
    method: 'get'
  })
}

export function createFeeRecord(data) {
  return request({
    url: '/fees',
    method: 'post',
    data
  })
}

export function updateFeeRecord(id, data) {
  return request({
    url: `/fees/${id}`,
    method: 'put',
    data
  })
}

export function deleteFeeRecord(id) {
  return request({
    url: `/fees/${id}`,
    method: 'delete'
  })
}

export function payFee(id) {
  return request({
    url: `/fees/${id}/pay`,
    method: 'post'
  })
}

export function getElderFees(elderId) {
  return request({
    url: `/fees/elder/${elderId}`,
    method: 'get'
  })
}

export function getFeeTypes() {
  return request({
    url: '/fees/types',
    method: 'get'
  })
}
