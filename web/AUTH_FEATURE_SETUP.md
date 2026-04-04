# 📋 Auth Feature - Setup Complete

## ✅ Files Created/Updated

### 1. **API Endpoints** (`src/shared/api/auth.ts`)
   - Định nghĩa tất cả auth endpoints
   - Methods: `loginV1()`, `logout()`, `getCurrentUser()`, `refreshToken()`, `register()`

### 2. **HTTP Client** (`src/shared/api/client.ts`)
   - ✅ Đã có sẵn
   - Axios wrapper với interceptors
   - Auto thêm Authorization header

### 3. **Models** (`src/features/auth/models/index.ts`)
   - `LoginV1Request` - Input params cho login
   - `LoginV1Response` - Response từ API (token, refreshToken, expiresIn, tokenType)
   - `User` - User info (id, email, name)

### 4. **Repository Interface** (`src/features/auth/repositories/iAuthRepo.ts`)
   - `IAuthRepository` interface
   - Methods: `loginV1()`, `logout()`, `getCurrentUser()`

### 5. **Repository Implementation** (`src/features/auth/repositories/index.ts`)
   - `AuthRepositoryImpl` - implement IAuthRepository
   - Gọi authApi methods
   - Quản lý tokens trong localStorage
   - Helper methods: `isAuthenticated()`, `getAccessToken()`, `getRefreshToken()`

### 6. **Service/Business Logic** (`src/features/auth/services/index.ts`)
   - `AuthService` class
   - Methods: `login()`, `logout()`, `getCurrentUser()`, `isAuthenticated()`, `getAccessToken()`
   - Validate input trước khi gửi API
   - Error handling

### 7. **Pinia Store** (`src/features/auth/stores/index.ts`)
   - `useAuthStore` - State management
   - State: `token`, `refreshToken`, `user`, `isLoading`, `error`
   - Actions: `login()`, `logout()`, `fetchCurrentUser()`, `clearError()`
   - Getters: `isAuthenticated()`, `getToken()`, `getUser()`, `getError()`

### 8. **Login Form Component** (`src/features/auth/components/LoginForm.vue`)
   - Form với 3 fields: email, password, opt
   - Input validation
   - Error message display
   - Loading state
   - Emit `success` event khi login thành công

### 9. **Login View** (`src/features/auth/views/LoginView.vue`)
   - Full page login screen
   - Includes LoginForm component
   - Beautiful gradient background
   - Navigate to home (`/`) sau khi login thành công

### 10. **Feature Exports** (`src/features/auth/index.ts`)
   - Export public API: stores, services, repositories, types, components, views

### 11. **Router** (`src/router/index.ts`)
   - Route `/login` → `LoginView.vue`
   - Route `/` → home (protected route)
   - Meta: `requiresAuth`, `title`

---

## 🔄 Data Flow

```
LoginForm.vue
    ↓ (user submits form)
useAuthStore.login()
    ↓ (calls)
AuthService.login()
    ↓ (calls)
AuthRepositoryImpl.loginV1()
    ↓ (calls)
authApi.loginV1()
    ↓ (calls)
apiClient.post('/api/auth/login')
    ↓ (HTTP request)
Backend API
    ↓ (returns token)
AuthRepositoryImpl (saves token to localStorage)
    ↓
authStore.token = response.accessToken
authStore.user = current user info
    ↓
LoginView.vue (@success event)
    ↓
router.push({ name: 'home' })
```

---

## 🚀 How to Use

### 1. Login
```typescript
import { useAuthStore } from '@/features/auth'

const authStore = useAuthStore()
await authStore.login(email, password, opt)
```

### 2. Check if Authenticated
```typescript
if (authStore.isAuthenticated()) {
  // User is logged in
}
```

### 3. Get Current User
```typescript
const user = authStore.user
console.log(user.email, user.name)
```

### 4. Logout
```typescript
await authStore.logout()
```

### 5. Get Access Token
```typescript
const token = authStore.token
```

---

## 📝 Environment Setup

Make sure you have `.env` file with:
```
VITE_API_BASE_URL=http://localhost:3000
```

---

## ✔️ Next Steps

1. Test login with your backend API
2. Add route guards for protected routes
3. Add JWT token refresh logic
4. Add "Remember me" functionality
5. Add password reset flow
6. Add user registration feature

---

Tất cả sẵn sàng! 🎉
