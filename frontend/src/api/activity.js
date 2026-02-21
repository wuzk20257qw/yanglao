import request from '@/utils/request'

export const getActivities = (params) => {
  return request({
    url: '/activities',
    method: 'get',
    params
  })
}

export const getActivityById = (id) => {
  return request({
    url: `/activities/${id}`,
    method: 'get'
  })
}

export const createActivity = (data) => {
  return request({
    url: '/activities',
    method: 'post',
    data
  })
}

export const updateActivity = (id, data) => {
  return request({
    url: `/activities/${id}`,
    method: 'put',
    data
  })
}

export const deleteActivity = (id) => {
  return request({
    url: `/activities/${id}`,
    method: 'delete'
  })
}

export const getEnrollments = (params) => {
  return request({
    url: '/activity-enrollments',
    method: 'get',
    params
  })
}

export const enrollActivity = (data) => {
  return request({
    url: '/activity-enrollments',
    method: 'post',
    data
  })
}

export const cancelEnrollment = (id) => {
  return request({
    url: `/activity-enrollments/${id}/cancel`,
    method: 'put'
  })
}

export const deleteEnrollment = (id) => {
  return request({
    url: `/activity-enrollments/${id}`,
    method: 'delete'
  })
}
