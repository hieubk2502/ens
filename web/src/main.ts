import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Nhập file cấu hình router

const app = createApp(App)

app.use(router) // Bảo Vue biết rằng chúng ta sẽ dùng Router này

app.mount('#app')
