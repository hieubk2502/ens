# 📐 Architecture Guide - Frontend Project

## 📂 Project Structure (Feature-Based + Layered Architecture)

```
src/
├── core/                    # 🔴 CORE LAYER - Global config & utilities
│   ├── config/             # Environment config
│   ├── constants/          # Global constants
│   ├── types/              # Global types (DTOs)
│   └── utils/              # Global utilities (storage manager, etc)
│
├── shared/                 # 🟡 SHARED LAYER - Reusable across features
│   ├── api/                # HTTP client & API endpoints
│   ├── components/         # UI components dùng chung
│   ├── hooks/              # Composables dùng chung
│   └── utils/              # Utilities dùng chung
│
├── features/               # 🟢 FEATURES - Business domains (độc lập)
│   ├── auth/               # ✅ Feature: Authentication
│   │   ├── models/         # Domain models/entities (User, LoginCredentials, etc)
│   │   ├── repositories/   # Data access interfaces & implementations
│   │   ├── services/       # Business logic
│   │   ├── stores/         # Pinia state management
│   │   ├── components/     # Feature-specific UI components
│   │   ├── views/          # Pages (LoginView.vue)
│   │   └── index.ts        # Public API exports
│   │
│   ├── dashboard/          # ✅ Feature: Dashboard
│   │   ├── models/
│   │   ├── services/
│   │   ├── stores/
│   │   ├── components/
│   │   ├── views/
│   │   └── index.ts
│   │
│   └── home/               # ✅ Feature: Home
│       ├── models/
│       ├── services/
│       ├── stores/
│       ├── components/
│       ├── views/
│       └── index.ts
│
├── layouts/                # Layout templates
├── router/                 # Routing configuration
├── App.vue                 # Root component
└── main.ts                 # Entry point (khởi tạo app)
```

---

## 🏗️ Architecture Layers Explained

### **1. CORE Layer** 🔴
**Trách nhiệm**: Cấu hình toàn cục, types, utilities cho toàn app
- `config/` - Environment config (API base URL, timeouts)
- `constants/` - Global constants & enums
- `types/` - DTOs/Interfaces dùng chung
- `utils/` - Helper functions (StorageManager, etc)

### **2. SHARED Layer** 🟡
**Trách nhiệm**: Code dùng chung cho tất cả features (không phụ thuộc business logic)
- `api/` - HTTP client & API endpoints (giống như @RestController)
- `components/` - Reusable UI components
- `hooks/` - Custom Vue composables
- `utils/` - Shared utility functions

### **3. FEATURES Layer** 🟢
**Trách nhiệm**: Business logic độc lập cho từng tính năng
- Mỗi feature là một **module độc lập**
- Có cấu trúc chuẩn: Models → Repositories → Services → Stores → Components → Views
- Có thể được develop/test riêng biệt

---

## 🔄 Data Flow & Dependency Architecture

```
┌─────────────────────────────────────────────────────────┐
│ PRESENTATION LAYER                                       │
│ ├─ Views (LoginView.vue, DashboardView.vue)            │
│ └─ Components (LoginForm, StatCard)                     │
└────────────────┬────────────────────────────────────────┘
                 │ imports & uses
                 ▼
┌─────────────────────────────────────────────────────────┐
│ STATE MANAGEMENT (Pinia)                                │
│ └─ Stores (useAuthStore, useDashboardStore)            │
│    └─ calls Services & Updates State                    │
└────────────────┬────────────────────────────────────────┘
                 │ dispatches
                 ▼
┌─────────────────────────────────────────────────────────┐
│ BUSINESS LOGIC LAYER                                    │
│ └─ Services (AuthService, DashboardService)            │
│    ├─ Validates input                                   │
│    ├─ Calls repositories                                │
│    └─ Transforms data                                   │
└────────────────┬────────────────────────────────────────┘
                 │ uses
                 ▼
┌─────────────────────────────────────────────────────────┐
│ DATA ACCESS LAYER                                       │
│ ├─ Repository Interface (IAuthRepository)              │
│ └─ Repository Implementation (AuthRepositoryImpl)       │
│    └─ Calls API Client                                  │
└────────────────┬────────────────────────────────────────┘
                 │ uses
                 ▼
┌─────────────────────────────────────────────────────────┐
│ API CLIENT (SHARED)                                     │
│ └─ apiClient (axios wrapper)                            │
│    ├─ Request/Response interceptors                     │
│    └─ Auth token management                             │
└────────────────┬────────────────────────────────────────┘
                 │ HTTP calls
                 ▼
┌─────────────────────────────────────────────────────────┐
│ BACKEND SERVER (API)                                    │
└─────────────────────────────────────────────────────────┘
```

---

## 🔐 Dependency Rules (CRITICAL!)

```
✅ ALLOWED:
- Features → Shared
- Features → Core
- Shared → Core
- Views → Stores
- Stores → Services
- Services → Repositories
- Repositories → API

❌ NOT ALLOWED:
- Shared → Features (sharing is ONE-WAY)
- Core → Features
- Core → Shared
- Features ↔ Features (use shared if need to share)
```

---

## 📚 Feature Example: Auth Feature

### **Step 1: Models** (Data structures)
```typescript
// features/auth/models/index.ts
export interface LoginCredentials {
  email: string
  password: string
}

export interface User {
  email: string
}
```

### **Step 2: Repository Interface** (Contract)
```typescript
// features/auth/repositories/auth.repository.ts
export interface IAuthRepository {
  login(credentials: LoginCredentials): Promise<AuthToken>
  logout(): Promise<void>
}
```

### **Step 3: Repository Implementation** (Data access)
```typescript
// features/auth/repositories/index.ts
export class AuthRepositoryImpl implements IAuthRepository {
  async login(credentials: LoginCredentials): Promise<AuthToken> {
    const response = await authApi.login(credentials)
    return { token: response.token }
  }
}
```

### **Step 4: Service** (Business logic)
```typescript
// features/auth/services/index.ts
export class AuthService {
  async login(credentials: LoginCredentials): Promise<string> {
    // Validate input
    if (!credentials.email) throw new Error('Email required')
    
    // Call repo
    const response = await this.authRepository.login(credentials)
    
    // Transform & save
    storageManager.set(TOKEN_STORAGE_KEY, response.token)
    return response.token
  }
}
```

### **Step 5: Store** (State management)
```typescript
// features/auth/stores/index.ts
export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
    user: null,
  }),
  actions: {
    async login(email: string, password: string) {
      const token = await authService.login({ email, password })
      this.token = token
    },
  },
})
```

### **Step 6: View** (UI)
```vue
<!-- features/auth/views/LoginView.vue -->
<script setup lang="ts">
const authStore = useAuthStore()

const onSubmit = async () => {
  await authStore.login(form.email, form.password)
}
</script>
```

---

## 💡 So Sánh với Backend Architecture

| Backend | Frontend |
|---------|----------|
| Entity / Domain Object | `features/*/models/` |
| @Repository Interface | `features/*/repositories/` (Interface) |
| @Repository Impl | `features/*/repositories/` (Impl) |
| @Service | `features/*/services/` |
| State/Cache | `features/*/stores/` (Pinia) |
| @Controller / Endpoint | `features/*/views/` (Pages) |
| DTO | `core/types/` |
| HttpClient/RestTemplate | `shared/api/client.ts` |
| Utils/Helpers | `core/utils/` & `shared/utils/` |

---

## 🎯 Best Practices

### ✅ DO

1. **Keep features independent**
   ```typescript
   // ✅ GOOD - each feature manages itself
   features/auth/
   features/dashboard/
   ```

2. **Use dependency injection**
   ```typescript
   // ✅ GOOD
   export class AuthService {
     constructor(private authRepository: IAuthRepository) {}
   }
   ```

3. **API calls in repositories only**
   ```typescript
   // ✅ GOOD
   // AuthRepositoryImpl → authApi.login()
   
   // ❌ BAD
   // AuthService → authApi.login()
   ```

4. **Business logic in services**
   ```typescript
   // ✅ GOOD - validation in service
   async login(credentials) {
     if (!credentials.email) throw new Error('Email required')
   }
   ```

5. **Export public API from feature**
   ```typescript
   // features/auth/index.ts
   export { useAuthStore } from './stores'
   export type { User, LoginCredentials } from './models'
   ```

### ❌ DON'T

1. **Don't call API from component directly**
   ```typescript
   // ❌ BAD
   const response = await http.post('/login', form)
   ```

2. **Don't share features with each other**
   ```typescript
   // ❌ BAD
   import { useAuthStore } from '@/features/auth'  // from another feature
   ```

3. **Don't put business logic in components**
   ```typescript
   // ❌ BAD
   const onSubmit = async () => {
     if (!form.email) throw new Error('Email required')  // Validation in component
   }
   ```

4. **Don't skip the layers**
   ```typescript
   // ❌ BAD - skipping repository & service
   const store = defineStore('auth', {
     actions: {
       async login(credentials) {
         const response = await authApi.login(credentials)  // Direct API call
       }
     }
   })
   ```

---

## 🚀 Adding a New Feature

### Template: Create a New Feature (e.g., "Products")

```
src/features/products/
├── models/index.ts          # Product, ProductFilter types
├── repositories/
│   ├── product.repository.ts    # IProductRepository interface
│   └── index.ts                 # ProductRepositoryImpl
├── services/index.ts        # ProductService
├── stores/index.ts          # useProductStore
├── components/
│   ├── ProductCard.vue
│   └── ProductFilter.vue
├── views/
│   ├── ProductListView.vue
│   └── ProductDetailView.vue
└── index.ts                 # Public exports
```

1. **Create models** → Define ProductFilter type
2. **Create repository** → Define IProductRepository interface
3. **Implement repository** → Connect to API
4. **Create service** → Add business logic
5. **Create store** → State management
6. **Create components** → UI
7. **Create views** → Pages
8. **Export from index.ts** → Public API
9. **Add routes** → In router/index.ts

---

## 🧪 Testing Strategy

```
Unit tests:
- Services (mock repositories)
- Repositories (mock API client)

Integration tests:
- Stores (with mock repositories)
- Components (with mock stores)

E2E tests:
- Full user flows (login → dashboard)
```

---

## 📝 Quick Checklist

- [ ] Feature has its own folder under `features/`
- [ ] Feature has `models/`, `repositories/`, `services/`, `stores/`, `views/`
- [ ] Component only talks to store
- [ ] Store only talks to service
- [ ] Service only talks to repository
- [ ] Repository only talks to API
- [ ] API calls through `shared/api/`
- [ ] Feature exports public API via `index.ts`
- [ ] No circular dependencies
- [ ] Feature can be tested independently

---

Happy coding! 🎉
