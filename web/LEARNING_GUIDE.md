# 🎓 Frontend Architecture - Learning Guide for Backend Engineers

## Từ Backend sang Frontend - Mapping Concepts

Nếu bạn là một **Backend engineer**, đây là cách mapping kiến thức của bạn sang Frontend:

### Backend Java/Spring Concepts → Frontend Vue.js Concepts

| Backend | Frontend | Purpose |
|---------|----------|---------|
| `Entity` / `@Entity` | `models/User.ts` | Data structure |
| `@Repository` Interface | `repositories/IUserRepository.ts` | Contract for data access |
| `@Repository` Implementation | `repositories/UserRepository.ts` | Implement contract with API calls |
| `@Service` | `services/UserService.ts` | Business logic |
| `@Controller` | `views/UserListView.vue` | Handle user interaction |
| `@Autowired` (Dependency Injection) | Constructor parameter | Inject dependencies |
| Database/ORM | `apiClient` | Data persistence (via API) |
| Exception Handling | Try-catch & error state | Error management |
| Validation in Service | Business logic validation | Validate before API call |
| Cache / Redis | Pinia Store | Store state in memory |
| Logging | `console.log`, logger service | Debug & monitoring |

---

## 📖 Practical Examples

### Example 1: Creating a "Users" Feature (CRUD operations)

#### Step 1: Define Models
```typescript
// src/features/users/models/index.ts
export interface User {
  id: string
  name: string
  email: string
  createdAt: string
}

export interface CreateUserRequest {
  name: string
  email: string
  password: string
}

export interface UsersState {
  users: User[]
  selectedUser: User | null
  isLoading: boolean
  error: string | null
}
```

#### Step 2: Define Repository Interface (The contract)
```typescript
// src/features/users/repositories/user.repository.ts
export interface IUserRepository {
  fetchUsers(): Promise<User[]>
  getUserById(id: string): Promise<User>
  createUser(request: CreateUserRequest): Promise<User>
  updateUser(id: string, request: Partial<CreateUserRequest>): Promise<User>
  deleteUser(id: string): Promise<void>
}
```

#### Step 3: Implement Repository (Call API)
```typescript
// src/features/users/repositories/index.ts
import { apiClient } from '@/shared/api/client'
import type { IUserRepository, User, CreateUserRequest } from '@/features/users/repositories/user.repository'

export class UserRepositoryImpl implements IUserRepository {
  async fetchUsers(): Promise<User[]> {
    return apiClient.get<User[]>('/users')
  }

  async getUserById(id: string): Promise<User> {
    return apiClient.get<User>(`/users/${id}`)
  }

  async createUser(request: CreateUserRequest): Promise<User> {
    return apiClient.post<User>('/users', request)
  }

  async updateUser(id: string, request: Partial<CreateUserRequest>): Promise<User> {
    return apiClient.put<User>(`/users/${id}`, request)
  }

  async deleteUser(id: string): Promise<void> {
    return apiClient.delete(`/users/${id}`)
  }
}

export function createUserRepository(): IUserRepository {
  return new UserRepositoryImpl()
}
```

#### Step 4: Create Service (Business logic)
```typescript
// src/features/users/services/index.ts
import type { IUserRepository } from '@/features/users/repositories/user.repository'
import type { User, CreateUserRequest } from '@/features/users/models'

export class UserService {
  constructor(private userRepository: IUserRepository) {}

  /**
   * 🔍 Fetch all users
   * Giống như @GetMapping("/users") trong Spring
   */
  async listUsers(): Promise<User[]> {
    try {
      const users = await this.userRepository.fetchUsers()
      // Format/transform data if needed
      return users.sort((a, b) => a.name.localeCompare(b.name))
    } catch (error) {
      throw new Error(`Failed to fetch users: ${error}`)
    }
  }

  /**
   * 🔍 Get user by ID
   */
  async getUserById(id: string): Promise<User> {
    if (!id) {
      throw new Error('User ID is required')
    }
    return await this.userRepository.getUserById(id)
  }

  /**
   * ✏️ Create new user
   * Giống như @PostMapping("/users") trong Spring
   */
  async createUser(request: CreateUserRequest): Promise<User> {
    // Validation logic (like @Valid & @NotBlank)
    if (!request.name?.trim()) {
      throw new Error('Name is required')
    }
    if (!request.email?.includes('@')) {
      throw new Error('Invalid email format')
    }
    if ((request.password || '').length < 6) {
      throw new Error('Password must be at least 6 characters')
    }

    try {
      return await this.userRepository.createUser(request)
    } catch (error) {
      throw new Error(`Failed to create user: ${error}`)
    }
  }

  /**
   * 🔄 Update user
   * Giống như @PutMapping("/users/{id}") trong Spring
   */
  async updateUser(id: string, request: Partial<CreateUserRequest>): Promise<User> {
    if (!id) {
      throw new Error('User ID is required')
    }

    return await this.userRepository.updateUser(id, request)
  }

  /**
   * 🗑️ Delete user
   * Giống như @DeleteMapping("/users/{id}") trong Spring
   */
  async deleteUser(id: string): Promise<void> {
    if (!id) {
      throw new Error('User ID is required')
    }

    await this.userRepository.deleteUser(id)
  }
}
```

#### Step 5: Create Pinia Store (State management)
```typescript
// src/features/users/stores/index.ts
// Giống như trong backend, nhưng lưu state ở client-side
// Thay vì lưu vào database, ta lưu vào memory (store state)

import { defineStore } from 'pinia'
import type { UsersState } from '@/features/users/models'
import { UserService } from '@/features/users/services'
import { createUserRepository } from '@/features/users/repositories'

const userService = new UserService(createUserRepository())

export const useUsersStore = defineStore('users', {
  state: (): UsersState => ({
    users: [],
    selectedUser: null,
    isLoading: false,
    error: null,
  }),

  getters: {
    /**
     * Computed getters - like @Transient methods
     */
    userCount(): number {
      return this.users.length
    },

    getUsersByName: (state) => (name: string) => {
      return state.users.filter(u => u.name.toLowerCase().includes(name.toLowerCase()))
    },
  },

  actions: {
    /**
     * Load all users
     * Như gọi userService.listUsers() và lưu kết quả vào state
     */
    async loadUsers() {
      this.isLoading = true
      this.error = null

      try {
        this.users = await userService.listUsers()
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to load users'
        this.users = []
      } finally {
        this.isLoading = false
      }
    },

    /**
     * Load single user by ID
     */
    async loadUserById(id: string) {
      this.isLoading = true
      this.error = null

      try {
        this.selectedUser = await userService.getUserById(id)
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to load user'
        this.selectedUser = null
      } finally {
        this.isLoading = false
      }
    },

    /**
     * Create new user
     */
    async createUser(name: string, email: string, password: string) {
      this.error = null

      try {
        const newUser = await userService.createUser({ name, email, password })
        this.users.push(newUser)
        return newUser
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to create user'
        throw error
      }
    },

    /**
     * Update user
     */
    async updateUser(id: string, updates: { name?: string; email?: string }) {
      this.error = null

      try {
        const updated = await userService.updateUser(id, updates)
        const index = this.users.findIndex(u => u.id === id)
        if (index !== -1) {
          this.users[index] = updated
        }
        if (this.selectedUser?.id === id) {
          this.selectedUser = updated
        }
        return updated
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to update user'
        throw error
      }
    },

    /**
     * Delete user
     */
    async deleteUser(id: string) {
      this.error = null

      try {
        await userService.deleteUser(id)
        this.users = this.users.filter(u => u.id !== id)
        if (this.selectedUser?.id === id) {
          this.selectedUser = null
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to delete user'
        throw error
      }
    },

    /**
     * Clear error
     */
    clearError() {
      this.error = null
    },
  },
})
```

#### Step 6: Create Components (UI building blocks)
```vue
<!-- src/features/users/components/UserCard.vue -->
<script setup lang="ts">
import type { User } from '@/features/users/models'

interface Props {
  user: User
}

const props = defineProps<Props>()

const emit = defineEmits<{
  edit: [id: string]
  delete: [id: string]
}>()
</script>

<template>
  <a-card class="user-card">
    <template #title>{{ user.name }}</template>
    <p>📧 {{ user.email }}</p>
    <p>📅 {{ user.createdAt }}</p>
    
    <template #actions>
      <a-button @click="emit('edit', user.id)">Edit</a-button>
      <a-button danger @click="emit('delete', user.id)">Delete</a-button>
    </template>
  </a-card>
</template>

<style scoped>
.user-card {
  margin-bottom: 16px;
}
</style>
```

#### Step 7: Create Views (Pages)
```vue
<!-- src/features/users/views/UsersListView.vue -->
<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUsersStore } from '@/features/users/stores'
import UserCard from '@/features/users/components/UserCard.vue'

const router = useRouter()
const usersStore = useUsersStore()
const showCreateForm = ref(false)
const newUserName = ref('')
const newUserEmail = ref('')
const newUserPassword = ref('')

onMounted(() => {
  usersStore.loadUsers()
})

const handleCreate = async () => {
  try {
    await usersStore.createUser(newUserName.value, newUserEmail.value, newUserPassword.value)
    // Clear form
    newUserName.value = ''
    newUserEmail.value = ''
    newUserPassword.value = ''
    showCreateForm.value = false
  } catch (error) {
    // Error is in store.error
  }
}

const handleEdit = (id: string) => {
  router.push(`/users/${id}/edit`)
}

const handleDelete = async (id: string) => {
  if (confirm('Are you sure?')) {
    await usersStore.deleteUser(id)
  }
}
</script>

<template>
  <div class="users-container">
    <h1>👥 Users Management</h1>

    <!-- Error alert -->
    <a-alert
      v-if="usersStore.error"
      :message="usersStore.error"
      type="error"
      closable
      @close="usersStore.clearError"
      style="margin-bottom: 16px"
    />

    <!-- Create button -->
    <a-button type="primary" @click="showCreateForm = true" style="margin-bottom: 16px">
      ➕ Add User
    </a-button>

    <!-- Create form modal -->
    <a-modal v-model:visible="showCreateForm" title="Create User" @ok="handleCreate">
      <a-form layout="vertical">
        <a-form-item label="Name">
          <a-input v-model:value="newUserName" placeholder="Full name" />
        </a-form-item>
        <a-form-item label="Email">
          <a-input v-model:value="newUserEmail" type="email" placeholder="user@example.com" />
        </a-form-item>
        <a-form-item label="Password">
          <a-input-password v-model:value="newUserPassword" placeholder="Password" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Loading spinner -->
    <a-spin :spinning="usersStore.isLoading">
      <!-- Users list -->
      <div v-if="usersStore.users.length > 0" class="users-grid">
        <UserCard
          v-for="user in usersStore.users"
          :key="user.id"
          :user="user"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </div>
      <a-empty v-else description="No users found" />
    </a-spin>
  </div>
</template>

<style scoped>
.users-container {
  padding: 24px;
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}
</style>
```

#### Step 8: Export from feature
```typescript
// src/features/users/index.ts
export { useUsersStore } from '@/features/users/stores'
export type { User, CreateUserRequest, UsersState } from '@/features/users/models'
export { UserService } from '@/features/users/services'
```

#### Step 9: Update Router
```typescript
// src/router/index.ts
import { useUsersStore } from '@/features/users'

const routes = [
  // ... other routes
  {
    path: '/users',
    name: 'UsersList',
    meta: { requiresAuth: true },
    component: () => import('@/features/users/views/UsersListView.vue'),
  },
  {
    path: '/users/:id/edit',
    name: 'UserEdit',
    meta: { requiresAuth: true },
    component: () => import('@/features/users/views/UserEditView.vue'),
  },
]
```

---

## 🌊 Data Flow Visualization

```
User fills form in Component
        ↓
Component calls Store Action
        ↓
usersStore.createUser(name, email, password)
        ↓
Store calls Service
        ↓
userService.createUser({ name, email, password })
        ↓
Service validates input
        ↓
Service calls Repository
        ↓
userRepository.createUser({ name, email, password })
        ↓
Repository calls API Client
        ↓
apiClient.post('/users', { name, email, password })
        ↓
HTTP POST request to Backend
        ↓
Backend processes & returns User object
        ↓
Repository returns User
        ↓
Service returns User
        ↓
Store saves User to state.users array
        ↓
Component reactively updates (UI re-renders)
        ↓
User sees new user in list
```

---

## 🎯 Key Takeaways

1. **Features are self-contained modules** - mỗi feature là một standalone application
2. **Layered architecture** - clear separation of concerns
3. **Dependency injection** - loose coupling, easy to test
4. **API in repository only** - single point of API calls
5. **Business logic in service** - reusable, testable
6. **State in store** - pinia manages the state
7. **Dumb components** - components only display & emit events

---

## 📚 Further Learning

- Read [ARCHITECTURE.md](./ARCHITECTURE.md) for full architecture details
- Compare with your backend architecture
- Practice by adding new features following this pattern
- Write unit tests for services & repositories

Happy learning! 🚀
