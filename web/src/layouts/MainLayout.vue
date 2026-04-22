<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppHeader from '../components/Layout/AppHeader.vue'
import AppSidebar from '../components/Layout/AppSidebar.vue'
import AppFooter from '../components/Layout/AppFooter.vue'
import { DownOutlined, TeamOutlined, SearchOutlined, PlusOutlined, ExportOutlined, FullscreenOutlined, ReloadOutlined } from '@ant-design/icons-vue'

const route = useRoute()
const router = useRouter()

// Get hierarchy breadcrumbs
const breadcrumbs = computed(() => {
  return route.matched
    .filter(m => m.meta && m.meta.title)
    .map(m => ({ title: m.meta.title as string, path: m.path }))
})

// Current L3 siblings for the selector
const siblings = computed(() => {
  const parent = route.matched[route.matched.length - 2]
  if (parent && parent.children) {
    return parent.children.map((c: any) => ({
      name: c.name,
      title: c.meta?.title || c.name
    }))
  }
  return []
})

const handleContextChange = ({ key }: any) => {
  router.push({ name: key })
}
</script>

<template>
  <a-layout class="enterprise-layout">
    <AppHeader />

    <a-layout class="body-layout">
      <AppSidebar />

      <a-layout-content class="main-content">
        <div class="content-wrapper">
          <!-- PAGE HEADER -->
          <div class="page-header">
            <div class="header-top">
              <a-breadcrumb separator=">">
                <a-breadcrumb-item>Trang chủ</a-breadcrumb-item>
                <a-breadcrumb-item v-for="bc in breadcrumbs.slice(1)" :key="bc.path">
                  {{ bc.title }}
                </a-breadcrumb-item>
              </a-breadcrumb>
              
              <div class="context-selector">
                <team-outlined style="margin-right: 8px; color: #103567; font-size: 18px" />
                <a-dropdown :trigger="['click']">
                  <div class="selector-box">
                    <span>{{ route.meta.title }}</span>
                    <down-outlined style="font-size: 10px; margin-left: 12px" />
                  </div>
                  <template #overlay>
                    <a-menu @click="handleContextChange" :selectedKeys="[route.name as string]">
                      <a-menu-item v-for="sib in siblings" :key="sib.name">
                        {{ sib.title }}
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </div>
            </div>
            
            <h2 class="page-title">{{ route.meta.title?.toString().toUpperCase() }}</h2>
          </div>

          <div class="content-body">
             <RouterView />
          </div>
        </div>

        <AppFooter />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style scoped>
.enterprise-layout {
  min-height: 100vh;
}

.body-layout {
  display: flex;
}

.main-content {
  background: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  padding: 12px 24px;
}

.page-header {
  margin-bottom: 20px;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.context-selector {
  display: flex;
  align-items: center;
}

.selector-box {
  background: #fff;
  border: 1px solid #d9d9d9;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #333;
}

.selector-box:hover {
  border-color: #1890ff;
}

.page-title {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin: 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #ddd;
}

.content-body {
  background: #fff;
  padding: 24px;
  min-height: calc(100vh - 250px);
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

:deep(.ant-breadcrumb) {
  font-size: 12px;
  color: #888;
}

:deep(.ant-breadcrumb-link) {
  color: #888 !important;
}

:deep(.ant-breadcrumb-separator) {
  color: #ccc;
}
</style>
