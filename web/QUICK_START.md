# 🚀 Quick Start - Login Flow Testing

## ✅ What's Been Implemented

```
✅ Token Storage        → localStorage with StorageManager
✅ Route Guards         → Protect routes & auto-redirect
✅ Auth Initialization  → Restore token on app load
✅ Login View           → Beautiful form with validation
✅ Logout Button        → In header with loading state
✅ Mock API             → Use for testing without backend
✅ Error Handling       → Display errors & clear them
```

---

## 🏃 Get Started (5 minutes)

### **1️⃣ Install Dependencies**
```bash
cd /Users/tranminhhieu/Downloads/ens/web
npm install
```

### **2️⃣ Start Dev Server**
```bash
npm run dev
# Server runs on http://localhost:3000
```

### **3️⃣ Test Login Flow**

Open browser → http://localhost:3000

**You'll see:**
- ⏸️ App loads...
- 🔐 Redirects to /login automatically (no token)
- 📝 Login form appears

**Fill the form:**
```
Email:    test@example.com
Password: password123
```

**Click "Đăng nhập"**
```
✅ Token saved to localStorage
✅ Redirect to home page
✅ User info shows in header
```

### **4️⃣ Test Persistence**

```
Reload page (F5)
↓
Token restored from localStorage
↓
Stay on home page (no re-login)
```

### **5️⃣ Test Logout**

```
Click "🚪 Đăng xuất" button in header
↓
Token cleared from storage
↓
Redirect to /login
↓
Try access /dashboard → redirect to /login again
```

---

## 📋 Complete Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│ 1. App Load (main.ts)                                       │
├─────────────────────────────────────────────────────────────┤
│ ↓ initializeAuth()                                          │
│ ↓ Check localStorage for token                              │
│ ├─ Found: restore & set auth headers                        │
│ └─ Not found: user = unauthenticated                        │
│ ↓ setupAuthGuard()                                          │
│ ↓ Setup route protection                                    │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. Route Check (beforeEach)                                 │
├─────────────────────────────────────────────────────────────┤
│ User tries to access route                                  │
│ ↓ Check route.meta.requiresAuth                             │
│ ├─ YES & no token → Redirect /login                         │
│ ├─ YES & has token → Allow                                  │
│ └─ NO → Allow                                               │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. Login (if no token)                                      │
├─────────────────────────────────────────────────────────────┤
│ LoginView.vue shown                                         │
│ ↓ User fills form                                           │
│ ↓ handleSubmit()                                            │
│ ↓ authStore.login(email, password)                          │
│ ↓ AuthService validates                                     │
│ ↓ Call authRepository.login() (→ API)                       │
│ ↓ Get token back                                            │
│ ↓ Save token to localStorage                                │
│ ↓ Set API auth header                                       │
│ ↓ Update store state                                        │
│ ↓ Redirect to home                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. Access Protected Routes                                  │
├─────────────────────────────────────────────────────────────┤
│ Token in localStorage → Can access /dashboard               │
│ Token in store        → Can access /                        │
│ API calls have auth   → Server knows who we are             │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. Logout                                                   │
├─────────────────────────────────────────────────────────────┤
│ Click logout button                                         │
│ ↓ authStore.logout()                                        │
│ ↓ Clear localStorage                                        │
│ ↓ Clear store.token & store.user                            │
│ ↓ Clear API auth header                                     │
│ ↓ Redirect to /login                                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧪 Testing Checklist

### **Test 1: No Token → Redirect Login**
```
✅ localStorage.clear()
✅ Refresh page
✅ Should redirect to /login
✅ Should see login form
```

### **Test 2: Login with Valid Credentials**
```
✅ Go to /login
✅ Fill: email=test@test.com, password=password
✅ Click login
✅ Wait for loading
✅ See success message
✅ Redirect to home
✅ See user info in header
```

### **Test 3: Check Token in Storage**
```
✅ Open DevTools (F12)
✅ Go to Application → LocalStorage
✅ Look for key: "app_auth_token"
✅ Should have JWT token value
```

### **Test 4: Reload Page → Token Restored**
```
✅ After login, refresh page (F5)
✅ Should NOT redirect to /login
✅ Should stay on home page
✅ User info still shows in header
✅ Token still in localStorage
```

### **Test 5: Access Protected Route**
```
✅ While logged in
✅ Go to /dashboard
✅ Should load dashboard (not redirect)
✅ Headers should have Authorization
```

### **Test 6: Logout**
```
✅ Click logout button in header
✅ See "Đã đăng xuất" message
✅ Redirect to /login
✅ localStorage["app_auth_token"] should be gone
✅ Try access /dashboard
✅ Should redirect to /login again
```

### **Test 7: Try Login Page While Logged In**
```
✅ Login successfully
✅ Go to /login
✅ Should redirect to home (not show login form)
```

### **Test 8: Error Handling**
```
✅ Go to /login
✅ Submit without email
✅ Should show error: "Email không được để trống"
✅ Submit with invalid email
✅ Should show error: "Email không hợp lệ"
✅ Submit without password
✅ Should show error: "Mật khẩu không được để trển"
```

---

## 🔑 Key Commands

```bash
# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Type check
npm run typecheck

# Lint code
npm run lint

# Run tests
npm run test
```

---

## 📁 Important Files

| File | Purpose |
|------|---------|
| `src/main.ts` | App initialization + auth setup |
| `src/features/auth/guards/index.ts` | Route guards |
| `src/features/auth/views/LoginView.vue` | Login form |
| `src/features/auth/stores/index.ts` | Auth state |
| `src/features/auth/services/index.ts` | Business logic |
| `src/shared/api/client.ts` | HTTP client |
| `src/core/utils/storage.ts` | Storage manager |
| `src/layouts/DefaultLayout.vue` | Main layout (with logout) |

---

## 🐛 Debugging

### **Enable Debug Logs**

In `src/features/auth/guards/index.ts`, uncomment:
```typescript
console.log(`Navigating to ${to.path}, requiresAuth: ${requiresAuth}...`)
```

### **Check Token in Console**
```javascript
// In browser console:
localStorage.getItem('app_auth_token')
```

### **Check Store State**
```javascript
// Install Vue DevTools browser extension
// Then access Pinia store in DevTools → Pinia tab
```

### **Check Network Requests**
```
F12 → Network tab → Check Authorization header
```

---

## 🚨 Common Issues

### **Issue: Always redirects to /login**
```
Solution:
1. Check if token in localStorage: 
   localStorage.getItem('app_auth_token')
2. Check auth store: 
   authStore.isAuthenticated (should be true)
3. Clear localStorage & re-login:
   localStorage.clear()
4. Check console for errors
```

### **Issue: Can't logout**
```
Solution:
1. Check layout using logout button (DefaultLayout.vue)
2. Check authStore.logout() implementation
3. Check localStorage cleared:
   localStorage.getItem('app_auth_token') === null
4. Check redirect happened
```

### **Issue: Port 3000 already in use**
```
Solution:
1. Change port in .env.development:
   VITE_PORT=3001
2. Or kill process on port:
   # macOS/Linux:
   lsof -ti:3000 | xargs kill -9
   # Windows:
   netstat -ano | findstr :3000
```

---

## 📖 Documentation

- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Full architecture guide
- **[LOGIN_FLOW.md](./LOGIN_FLOW.md)** - Detailed login flow
- **[LEARNING_GUIDE.md](./LEARNING_GUIDE.md)** - For backend engineers

---

## 🎯 Next Steps

1. ✅ Test login flow locally
2. ✅ Test token persistence
3. ✅ Test logout
4. 🔄 Connect to real backend API
5. 🔄 Implement refresh token
6. 🔄 Add 2FA support
7. 🔄 Add password reset

---

Happy coding! 🎉

**Questions?** Check the docs or enable debug logs!
