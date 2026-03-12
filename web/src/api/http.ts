import axios from 'axios'

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/',
  timeout: 10_000,
})

http.interceptors.request.use((config) => {
  // Attach auth token here if needed
  return config
})

http.interceptors.response.use(
  (response) => response.data,
  (error) => {
    // Centralize error handling/logging
    return Promise.reject(error)
  },
)
