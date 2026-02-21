import request from '@/utils/request'

export function getBeds(params) {
  return request({
    url: '/beds',
    method: 'get',
    params
  })
}

export function getBedById(id) {
  return request({
    url: `/beds/${id}`,
    method: 'get'
  })
}

export function createBed(data) {
  return request({
    url: '/beds',
    method: 'post',
    data
  })
}

export function updateBed(id, data) {
  return request({
    url: `/beds/${id}`,
    method: 'put',
    data
  })
}

export function deleteBed(id) {
  return request({
    url: `/beds/${id}`,
    method: 'delete'
  })
}

export function updateBedStatus(id, status) {
  return request({
    url: `/beds/${id}/status`,
    method: 'put',
    params: { status }
  })
}
