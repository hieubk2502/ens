# 🔌 Port Configuration Guide

## Current Setup

Dự án đã được cấu hình để dễ dàng thay đổi port.

## 📋 Default Ports

```
Development Server: 5173 (mặc định)
Preview Server:     4173
```

## 🚀 Cách Chạy

### **Cách 1: Port Mặc Định (5173)**
```bash
npm run dev
# → http://localhost:5173
```

### **Cách 2: Thay Đổi Port qua .env.development**

1. Mở file `.env.development`
2. Thay đổi `VITE_PORT=5173` thành port bạn muốn
   ```bash
   # Port 3000
   VITE_PORT=3000
   
   # Port 8080
   VITE_PORT=8080
   ```
3. Chạy `npm run dev`

### **Cách 3: Port qua Command Line**
```bash
# Port 3000
VITE_PORT=3000 npm run dev

# Port 8000
VITE_PORT=8000 npm run dev

# macOS/Linux
export VITE_PORT=3000 && npm run dev
```

### **Cách 4: Port qua Vite Command**
```bash
vite --port 3000
```

## 🔧 Cấu Hình trong vite.config.js

```javascript
server: {
  port: process.env.VITE_PORT ? parseInt(process.env.VITE_PORT) : 3000,
  open: false,        // Tự động mở browser
  host: true,         // 0.0.0.0 (accessible from network)
},
```

## 📝 Các Options Khác

### **Tự động mở browser**
```javascript
// vite.config.js
server: {
  open: true,  // Tự động mở http://localhost:5173
  // hoặc specific URL
  open: 'http://localhost:3000/dashboard'
}
```

### **Cho phép network access**
```javascript
server: {
  host: '0.0.0.0',  // Accessible từ IP khác (không chỉ localhost)
  port: 5173,
}
```

### **Allow CORS**
```javascript
server: {
  cors: true,
  // hoặc specific origins
  cors: {
    origin: ['http://localhost:3000', 'https://example.com'],
  }
}
```

## 🧪 Test Port

```bash
# Kiểm tra port đang dùng
# macOS
lsof -i :5173

# Linux
netstat -tlnp | grep 5173

# Windows
netstat -ano | findstr :5173
```

## ⚠️ Conflicts

Nếu port bị dùng (Address already in use):

```bash
# Giảm port (5173 → 5174, 5175...)
npm run dev
# Vite sẽ tự động chọn port tiếp theo

# Hoặc kill process trên port
# macOS/Linux
kill -9 $(lsof -t -i:5173)

# Windows
netstat -ano | findstr :5173
taskkill /PID <PID> /F
```

## 🎯 Recommended Setup

```bash
# .env.development
VITE_PORT=5173        # Development

# .env.production  
# (không cần config port - production sẽ deploy lên server)
```

---

**Tóm tắt:**
- ✅ Default port: `5173`
- ✅ Change qua `VITE_PORT` env variable
- ✅ Hoặc change trong `vite.config.js`
- ✅ Hoặc pass `--port` flag

Chạy ngay:
```bash
npm run dev
```
