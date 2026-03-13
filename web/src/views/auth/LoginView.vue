<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { useAuthStore } from '@/stores/auth'

type LoginForm = {
  email: string
  password: string
}

const form = reactive<LoginForm>({
  email: '',
  password: '',
})

const loading = ref(false)
const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const onSubmit = async () => {
  loading.value = true
  try {
    await auth.login(form)
    message.success('Đăng nhập thành công')
    const redirect = (route.query.redirect as string) || '/dashboard'
    router.push(redirect)
  } catch (error: unknown) {
    const msg = error instanceof Error ? error.message : 'Đăng nhập thất bại'
    message.error(msg)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page">
    <div class="card">
      <h1 class="title">Đăng nhập</h1>
      <p class="subtitle">Nhập thông tin để tiếp tục</p>

      <a-form layout="vertical" @submit.prevent="onSubmit">
        <a-form-item label="Email" name="email" :rules="[{ required: true, type: 'email', message: 'Email không hợp lệ' }]">
          <a-input v-model:value="form.email" placeholder="you@example.com" autocomplete="email" />
        </a-form-item>

        <a-form-item label="Mật khẩu" name="password" :rules="[{ required: true, message: 'Nhập mật khẩu' }]">
          <a-input-password v-model:value="form.password" placeholder="••••••••" autocomplete="current-password" />
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" block :loading="loading">Đăng nhập</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: radial-gradient(circle at 10% 20%, #e0f2fe 0, transparent 25%),
    radial-gradient(circle at 90% 30%, #fef3c7 0, transparent 20%),
    #f8fafc;
  padding: 1rem;
}

.card {
  width: min(420px, 100%);
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 18px 60px rgba(15, 23, 42, 0.12);
  display: grid;
  gap: 0.5rem;
}

.title {
  margin: 0;
}

.subtitle {
  color: #475569;
  margin: 0 0 0.75rem;
}
</style>
