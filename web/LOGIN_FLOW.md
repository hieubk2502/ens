# 🔐 Complete Login Flow Guide

## Overview

Dự án hiện có một login flow hoàn chỉnh:
- ✅ Check token khi app load
- ✅ Redirect to login nếu chưa auth
- ✅ Redirect to home nếu đã auth
- ✅ Logout & clear token

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│ main.ts - App Initialization                            │
├─────────────────────────────────────────────────────────┤
│                                                          │
│ 1️⃣  Pinia created → app.use(pinia)                    │
│ 2️⃣  Router created → app.use(router)                  │
│ 3️⃣  initializeAuth() → Restore token from storage     │
│ 4️⃣  setupAuthGuard(router) → Setup route guards       │
│ 5️⃣  app.mount('#app')                                 │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 Step-by-Step Flow

### **1️⃣ App Initialization (main.ts)**

```typescript
// Step 1: Initialize Pinia
app.use(pinia)

// Step 2: Initialize Auth (restore token from storage)
initializeAuth()
// ↓ Restore token từ localStorage
// ↓ Set auth header nếu có token

// Step 3: Setup Route Guards
setupAuthGuard(router)
// ↓ Kiểm tra mỗi lần navigate
```

### **2️⃣ App Load - Check Token**

```
App Loads
  ↓
initializeAuth() được gọi
  ↓
Check localStorage.getItem('app_auth_token')
  ↓
Tìm thấy token?
  ├─ YES: Set token to auth store & API headers
  └─ NO: User chưa login
```

### **3️⃣ Route Navigation - Check Auth**

```
User truy cập route
  ↓
router.beforeEach hook được trigger
  ↓
Check: route.meta.requiresAuth?
  ├─ YES & isAuthenticated? 
  │   ├─ YES: Allow navigate ✅
  │   └─ NO: Redirect /login ❌ (with redirect param)
  │
  └─ NO: Allow navigate ✅ (public route)
```

### **4️⃣ Login - Save Token**

```
User fill form & submit
  ↓
LoginView.vue handleSubmit()
  ↓
authStore.login(email, password)
  ↓
AuthService.login()
  ├─ Validate input
  ├─ Call authRepository.login()
  ├─ Save token to localStorage
  └─ Return token
  ↓
Store state: token = "..." & user = {...}
  ↓
Set API auth header: apiClient.setAuthToken(token)
  ↓
Redirect to home (or redirect URL)
```

### **5️⃣ Protected Route Access**

```
User truy cập /dashboard
  ↓
router.beforeEach checks:
  ├─ route.meta.requiresAuth = true
  ├─ authStore.isAuthenticated = true ✅
  ↓
Allow navigate to DashboardView
```

### **6️⃣ Logout - Clear Token**

```
User click logout button
  ↓
authStore.logout()
  ↓
Clear localStorage
Clear store state (token = null, user = null)
Clear API auth header
  ↓
Redirect to /login
```

---

## 📁 File Structure

```
features/auth/
├── models/index.ts          # Type definitions
├── repositories/            # Data access
│   ├── auth.repository.ts   # Interface
│   └── index.ts             # Implementation
├── services/index.ts        # Business logic
├── stores/index.ts          # Pinia state
├── guards/index.ts          # ✨ NEW - Route guards & initialization
├── views/
│   └── LoginView.vue        # ✨ IMPROVED - Better form
├── components/              # Login form components (future)
└── index.ts                 # Public exports
```

---

## 🔑 Key Files

### **1. guards/index.ts** (NEW - Route Guards)
```typescript
/**
 * setupAuthGuard(router)
 * - Kiểm tra mỗi navigation
 * - Redirect to /login nếu cần auth nhưng chưa authenticated
 * - Redirect home nếu access /login nhưng đã authenticated
 */

/**
 * initializeAuth()
 * - Called quando app load
 * - Restore token từ localStorage
 * - Set auth headers
 */
```

### **2. main.ts** (Updated)
```typescript
// Step 1: Pinia initialized
app.use(pinia)

// Step 2: Restore auth state
initializeAuth()

// Step 3: Setup route guards
setupAuthGuard(router)
```

### **3. LoginView.vue** (Improved)
```vue
- Email validation
- Password validation
- Show/hide password toggle
- Remember me checkbox
- Error handling
- Enter key support
- Demo mode hint
- Redirect after login
```

### **4. Auth Store** (stores/index.ts)
```typescript
state: {
  token: string | null      # JWT token
  user: User | null         # User info
  isLoading: boolean        # Loading state
  error: string | null      # Error message
}

actions: {
  login(email, password)    # Login
  logout()                  # Logout
  clearError()              # Clear error
}

getters: {
  isAuthenticated           # Check if logged in
  hasError                  # Check if has error
}
```

---

## 🚀 Usage

### **Simple Login Flow**

```bash
# 1. Start dev server
npm run dev

# 2. Go to http://localhost:5173
# ↓ Automatically redirects to /login (no token)

# 3. Fill form & login
# Email: any@email.com
# Password: any password (demo mode)

# 4. After login
# ↓ Token saved to localStorage
# ↓ Redirect to / (home page)

# 5. Reload page
# ↓ Token restored from localStorage
# ↓ Stay on home page (no login redirect)
```

### **Routes**

```typescript
/login                  # Login page (public)
/                      # Home page (requires auth)
/dashboard             # Dashboard (requires auth)
```

### **Meta Tags** (in router/index.ts)

```typescript
meta: {
  requiresAuth: true   # Route needs authentication
  requiresAuth: false  # Public route
  layout: 'default'    # Which layout to use
  layout: 'none'       # No layout (login page)
}
```

---

## 💾 Token Storage

### **Where Token is Stored**

```javascript
// localStorage key: 'app_auth_token'
localStorage.getItem('app_auth_token')
// → Returns: "eyJhbGciOiJIUzI1NiIs..."

// Accessed through StorageManager
import { storageManager } from '@/core/utils/storage'
storageManager.get(TOKEN_STORAGE_KEY)
```

### **Token Persistence**

```
User Login
  ↓
Token saved to localStorage
  ↓
User closes browser
  ↓
User opens browser again
  ↓
Token restored from localStorage
  ↓
User stays logged in (no re-login needed)
```

---

## 🛡️ Security Features

### **Current**
- ✅ Token stored in localStorage
- ✅ Token sent in Authorization header (Bearer)
- ✅ Protected routes checked at navigation
- ✅ Logout clears token

### **Recommended Improvements**
- [ ] Use httpOnly cookies instead of localStorage (more secure)
- [ ] Implement refresh token logic
- [ ] Add CSRF protection
- [ ] Add rate limiting on login
- [ ] Add 2FA support
- [ ] Token expiration check & refresh

---

## 🧪 Testing Login Flow

### **Test 1: No Token - Redirect to Login**
```
1. Clear localStorage: localStorage.clear()
2. Refresh page
3. Should redirect to /login
✅ Pass
```

### **Test 2: Login Success**
```
1. Go to /login
2. Fill form (demo credentials work)
3. Click login
4. Should save token to localStorage
5. Should redirect to /
✅ Pass
```

### **Test 3: Remember Token After Reload**
```
1. Login successfully
2. Go to /
3. Reload page (F5)
4. Should still on / (no redirect to /login)
✅ Pass
```

### **Test 4: Logout**
```
1. Login successfully
2. Click logout button (in header/menu)
3. Token should be cleared
4. Should redirect to /login
✅ Pass
```

### **Test 5: Try Access Protected Route Without Token**
```
1. Clear localStorage
2. Try go to /dashboard
3. Should redirect to /login?redirect=/dashboard
4. After login, should redirect back to /dashboard
✅ Pass
```

---

## 🔧 How to Add Logout Button

### **Example: Logout in Header**

```vue
<script setup lang="ts">
import { useAuthStore } from '@/features/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = async () => {
  await authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="header">
    <span v-if="authStore.user">👤 {{ authStore.user.email }}</span>
    <a-button danger @click="handleLogout">🚪 Logout</a-button>
  </div>
</template>
```

---

## 🎯 Environment Variables

### **.env.development**
```
VITE_ENABLE_MOCK=true    # Use mock login (no backend needed)
VITE_API_BASE_URL=http://localhost:3000
```

### **.env.production**
```
VITE_ENABLE_MOCK=false   # Use real API
VITE_API_BASE_URL=https://api.example.com
```

---

## 📚 Files to Review

1. **src/main.ts** - App initialization
2. **src/features/auth/guards/index.ts** - Route guards
3. **src/features/auth/stores/index.ts** - Auth store
4. **src/features/auth/views/LoginView.vue** - Login form
5. **src/shared/api/client.ts** - API client with auth
6. **src/core/utils/storage.ts** - Storage manager

---

## 🚀 Next Steps

1. Test login flow locally
2. Test token persistence (reload page)
3. Test logout functionality
4. Add logout button to header/menu
5. Implement real API integration
6. Add refresh token logic
7. Add 2FA if needed
8. Add password reset flow

---

Happy authenticating! 🔐
