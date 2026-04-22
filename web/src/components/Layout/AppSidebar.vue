<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import * as Icons from '@ant-design/icons-vue'
import { useLayoutStore } from '../../stores/layout'

const router = useRouter()
const route = useRoute()
const layoutStore = useLayoutStore()

const selectedKeys = ref<string[]>([])
const openKeys = ref<string[]>([])

// Sync menu with current route
watch(() => route.path, (newPath) => {
  const matched = route.matched
  if (matched.length >= 3) {
    // Level 1: matched[2]
    // Level 2: matched[3]
    if (matched[2]) openKeys.value = [matched[2].name as string]
    if (matched[3]) selectedKeys.value = [matched[3].name as string]
  }
}, { immediate: true })

const getMenuItems = (routes: any[]) => {
  const result: any[] = []
  
  // Find the 'hr' module root
  const hrModule = routes.find(r => r.path === '/')?.children?.find((r: any) => r.path === 'hr')
  if (!hrModule || !hrModule.children) return result

  hrModule.children.forEach((l1: any) => {
    if (l1.meta?.hideInMenu) return

    const item: any = {
      key: l1.name as string,
      title: l1.meta?.title || l1.name,
      icon: l1.meta?.icon ? (Icons as any)[l1.meta.icon] : null,
    }

    if (l1.children && l1.children.length > 0) {
      item.children = l1.children.map((l2: any) => ({
        key: l2.name as string,
        title: l2.meta?.title || l2.name,
      }))
    }

    result.push(item)
  })

  return result
}

const menuData = computed(() => getMenuItems(router.options.routes))

const handleMenuClick = ({ key }: any) => {
  router.push({ name: key })
}
</script>

<template>
  <a-layout-sider 
    :collapsed="layoutStore.collapsed" 
    :trigger="null" 
    collapsible 
    class="side-nav"
    width="260"
  >
    <div class="sidebar-header" v-if="!layoutStore.collapsed">
      <Icons.HomeFilled style="margin-right: 8px" />
      <span>DASHBOARD</span>
    </div>
    
    <a-menu
      v-model:selectedKeys="selectedKeys"
      v-model:openKeys="openKeys"
      theme="dark"
      mode="inline"
      class="side-menu"
      @click="handleMenuClick"
    >
      <template v-for="item in menuData">
        <a-sub-menu v-if="item.children" :key="item.key">
          <template #title>
            <span>
              <component :is="item.icon" v-if="item.icon" />
              <span>{{ item.title }}</span>
            </span>
          </template>
          <a-menu-item v-for="sub in item.children" :key="sub.key">
            {{ sub.title }}
          </a-menu-item>
        </a-sub-menu>

        <a-menu-item v-else :key="item.key">
          <component :is="item.icon" v-if="item.icon" />
          <span>{{ item.title }}</span>
        </a-menu-item>
      </template>
    </a-menu>
  </a-layout-sider>
</template>

<style scoped>
.side-nav {
  background: #103567 !important;
  min-height: calc(100vh - 56px);
  box-shadow: 2px 0 8px rgba(0,0,0,0.15);
}

.sidebar-header {
  padding: 16px 24px;
  color: white;
  font-weight: bold;
  font-size: 14px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  margin-bottom: 8px;
}

.side-menu {
  background: transparent !important;
  border: none;
}

:deep(.ant-menu-dark.ant-menu-inline .ant-menu-sub) {
  background: #0d2a52 !important;
}

:deep(.ant-menu-item) {
  height: 48px !important;
  line-height: 48px !important;
  margin: 4px 0 !important;
  font-size: 13px !important;
  font-weight: 500 !important;
}

:deep(.ant-menu-item-selected) {
  background-color: #1890ff !important;
  color: white !important;
}

:deep(.ant-menu-submenu-title) {
  height: 48px !important;
  line-height: 48px !important;
  font-size: 13px !important;
  font-weight: 600 !important;
  color: rgba(255,255,255,0.85) !important;
}

:deep(.ant-menu-item:hover) {
  color: white !important;
}

:deep(.ant-menu-submenu-arrow) {
  color: rgba(255,255,255,0.5) !important;
}
</style>
