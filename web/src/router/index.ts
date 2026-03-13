import { createRouter, createWebHistory } from 'vue-router'
import {RouterName} from "@/router/RouterName";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: RouterName.HOME,
      meta: { requiresAuth: true, layout: 'default' },
      component: () => import('@/views/home/HomeView.vue'),
    },
    {
      path: '/dashboard',
      name: RouterName.DASHBOARD,
      meta: { requiresAuth: true, layout: 'default' },
      component: () => import('@/views/dashboard/DashboardView.vue'),
    },
    {
      path: '/login',
      name: RouterName.LOGIN,
      meta: { requiresAuth: false, layout: 'none' },
      component: () => import('@/views/auth/LoginView.vue'),
    },
  ],
})

export default router
