import request from '@/utils/request'

export const getShiftSchedules = (params) => {
  return request({
    url: '/shift-schedules',
    method: 'get',
    params
  })
}

export const getShiftScheduleById = (id) => {
  return request({
    url: `/shift-schedules/${id}`,
    method: 'get'
  })
}

export const createShiftSchedule = (data) => {
  return request({
    url: '/shift-schedules',
    method: 'post',
    data
  })
}

export const updateShiftSchedule = (id, data) => {
  return request({
    url: `/shift-schedules/${id}`,
    method: 'put',
    data
  })
}

export const deleteShiftSchedule = (id) => {
  return request({
    url: `/shift-schedules/${id}`,
    method: 'delete'
  })
}
