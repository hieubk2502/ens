import { defineStore } from 'pinia'
import { http } from '@/api/http'

type LoginPayload = {
  email: string
  password: string
}

type UserInfo = {
  email: string
}

type LoginResponse = {
  token: string
  user?: UserInfo
}

const TOKEN_KEY = 'auth_token'

function setAuthHeader(token?: string | null) {
  if (token) {
    http.defaults.headers.common.Authorization = `Bearer ${token}`
  } else {
    delete http.defaults.headers.common.Authorization
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: (typeof localStorage !== 'undefined' && localStorage.getItem(TOKEN_KEY)) || null as string | null,
    user: null as UserInfo | null,
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token),
  },
  actions: {
    async login(payload: LoginPayload) {
      const mock = import.meta.env.VITE_ENABLE_MOCK === 'true'
      if (mock) {
        this.token = 'mock-token'
        this.user = { email: payload.email }
        this.persistToken()
        return
      }

      const { data } = await http.post<LoginResponse>('/login', payload)
      if (!data?.token) throw new Error('Token không hợp lệ')
      this.token = data.token
      this.user = data.user ?? { email: payload.email }
      this.persistToken()
    },
    logout() {
      this.token = null
      this.user = null
      this.persistToken()
    },
    persistToken() {
      setAuthHeader(this.token)
      if (typeof localStorage !== 'undefined') {
        if (this.token) {
          localStorage.setItem(TOKEN_KEY, this.token)
        } else {
          localStorage.removeItem(TOKEN_KEY)
        }
      }
    },
  },
})

// set initial header
setAuthHeader((typeof localStorage !== 'undefined' && localStorage.getItem(TOKEN_KEY)) || undefined)
