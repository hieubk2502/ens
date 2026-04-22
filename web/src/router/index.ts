import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    redirect: '/hr/org/structure/info',
    children: [
      {
        path: 'hr',
        name: 'hr',
        meta: { title: 'QUẢN LÝ NHÂN SỰ', module: 'hr' },
        children: [
          // LEVEL 1: Groups in Sidebar
          {
            path: 'org',
            name: 'Organization',
            meta: { title: 'QUẢN LÝ TỔ CHỨC', icon: 'TeamOutlined' },
            children: [
              // LEVEL 2: Items in Sidebar
              {
                path: 'structure',
                name: 'OrgStructure',
                meta: { title: 'Cấu trúc tổ chức' },
                redirect: '/hr/org/structure/info',
                children: [
                  // LEVEL 3: Page Header Context/Views
                  {
                    path: 'info',
                    name: 'OrgInfo',
                    component: () => import('../views/Organization/Structure.vue'),
                    meta: { title: 'Thông tin tổ chức và nhân sự' }
                  },
                  {
                    path: 'chart',
                    name: 'OrgChart',
                    component: () => import('../views/Dashboard.vue'),
                    meta: { title: 'Sơ đồ tổ chức và danh sách nhân sự' }
                  },
                  {
                    path: 'business',
                    name: 'OrgBusiness',
                    component: () => import('../views/Dashboard.vue'),
                    meta: { title: 'Khối ngành nghiệp vụ và danh sách nhân sự' }
                  }
                ]
              },
              {
                path: 'profiles',
                name: 'UserProfiles',
                meta: { title: 'Hồ sơ nhân sự' },
                component: () => import('../views/Dashboard.vue')
              },
              {
                path: 'access',
                name: 'AccessControl',
                meta: { title: 'Quyền truy cập' },
                component: () => import('../views/Dashboard.vue')
              }
            ]
          },
          {
            path: 'okr',
            name: 'OKR',
            meta: { title: 'OKR - KPI TỔ CHỨC', icon: 'CheckCircleOutlined' },
            component: () => import('../views/Dashboard.vue')
          }
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { hideInMenu: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
