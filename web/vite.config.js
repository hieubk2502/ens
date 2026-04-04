import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    // Port dev server
    port: process.env.VITE_PORT ? parseInt(process.env.VITE_PORT) : 3000,
    // Auto open browser khi chạy
    open: false,
    // Host (localhost hoặc 0.0.0.0)
    host: true,
  },
  preview: {
    // Port preview server (build)
    port: 4173,
  },
  test: {
    environment: 'jsdom',
    globals: true,
  },
})
