<script setup lang="ts">
import { computed, ref } from 'vue'
import dayjs from 'dayjs'
import { useCounterStore } from '@/stores/counter'
import { http } from '@/api/http'

const counter = useCounterStore()
const doubled = computed(() => counter.doubleCount)
const serverTime = ref<string>('Chưa gọi')

async function fetchTime() {
  try {
    const { data } = await http.get<{ time: string }>('/time')
    serverTime.value = data?.time ?? 'N/A'
  } catch (error) {
    serverTime.value = 'Lỗi khi gọi API'
  }
}

const today = dayjs().format('DD/MM/YYYY HH:mm')
</script>

<template>
  <section class="page">
    <h1>Trang chủ</h1>
    <p>Vue Router + Pinia + Ant Design Vue + Axios + Dayjs</p>

    <div class="card">
      <p>Giá trị đếm: {{ counter.count }}</p>
      <p>Nhân đôi: {{ doubled }}</p>
      <p>Thời gian hiện tại (local): {{ today }}</p>
      <p>Thời gian từ API: {{ serverTime }}</p>
      <div class="actions">
        <a-button type="primary" @click="counter.increment">Tăng</a-button>
        <a-button @click="counter.reset">Reset</a-button>
        <a-button type="dashed" @click="fetchTime">Gọi API</a-button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.page {
  display: grid;
  gap: 1rem;
}

.card {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
  background: #fff;
}

.actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.75rem;
  flex-wrap: wrap;
}
</style>
