import request from '@/utils/request'

export function getElders(params) {
  return request({
    url: '/elders',
    method: 'get',
    params
  })
}

export function getElderById(id) {
  return request({
    url: `/elders/${id}`,
    method: 'get'
  })
}

export function createElder(data) {
  return request({
    url: '/elders',
    method: 'post',
    data
  })
}

export function updateElder(id, data) {
  return request({
    url: `/elders/${id}`,
    method: 'put',
    data
  })
}

export function deleteElder(id) {
  return request({
    url: `/elders/${id}`,
    method: 'delete'
  })
}

export function assignBed(elderId, bedId) {
  return request({
    url: `/elders/${elderId}/assign-bed`,
    method: 'post',
    params: { bedId }
  })
}

export function releaseBed(elderId) {
  return request({
    url: `/elders/${elderId}/release-bed`,
    method: 'post'
  })
}

export function getElderStats() {
  return request({
    url: '/elders/stats',
    method: 'get'
  })
}
