<template>
  <div class="login">
    <h2>Trang Đăng Nhập</h2>
    <p>Chào mừng bạn đến với trang đăng nhập.</p>
    <button @click="login">Nhấn để Đăng nhập (Giả lập)</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const login = () => {
  // Sau khi đăng nhập thành công, chúng ta sẽ chuyển hướng sang Dashboard
  router.push('/dashboard')
}
</script>
