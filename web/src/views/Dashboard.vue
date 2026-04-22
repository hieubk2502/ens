<template>
  <div class="dashboard">
    <h2>Trang Dashboard</h2>
    <p>Chúc mừng! Bạn đã đăng nhập thành công vào hệ thống.</p>
    <button @click="logout">Đăng xuất</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  router.push('/login')
}
</script>
