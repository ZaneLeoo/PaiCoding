import axios from 'axios'
import { BASE_URL } from '@/http/URL'

axios.defaults.baseURL = BASE_URL
axios.defaults.withCredentials = true

export function doGet(url, params, type) {
  const responseType = type ? type : 'json'
  return axios({
    method: 'get',
    url: url,
    params: params,
    responseType: responseType,
    headers: {
      'Content-Type': 'application/json'
    },
    withCredentials: true // 确保发送请求时包含凭证
  })
}

export function doPost(url, data) {
  return axios({
    method: 'post',
    url: url,
    data: data,
    headers: {
      'Content-Type': 'application/json'
    },
    withCredentials: true // 确保发送请求时包含凭证
  })
}

export function doFilePost(url, data) {
  return axios({
    method: 'post',
    url: url,
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    withCredentials: true // 确保发送请求时包含凭证
  })
}

export function doLoginPost(url, data) {
  return axios({
    method: 'post',
    url: url,
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function doPut(url, data) {
  return axios({
    method: 'put',
    url: url,
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function doDelete(url, params) {
  return axios({
    method: 'delete',
    url: url,
    params: params,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}
