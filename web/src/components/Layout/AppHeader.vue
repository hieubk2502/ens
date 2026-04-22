<script setup lang="ts">
import { 
  UserOutlined, 
  BellOutlined,
  SearchOutlined,
  DownOutlined,
  AppstoreOutlined
} from '@ant-design/icons-vue'
import { useLayoutStore } from '../../stores/layout'

const layoutStore = useLayoutStore()

const modules = [
  { 
    key: 'data', 
    title: 'ĐIỀU HÀNH SỐ LIỆU',
    options: ['Báo cáo tổng hợp', 'Chỉ số KPI', 'Phân tích dữ liệu']
  },
  { 
    key: 'management', 
    title: 'QUẢN TRỊ DOANH NGHIỆP',
    options: ['Quản lý dự án', 'Quy trình nghiệp vụ', 'Văn bản nội bộ']
  },
  { 
    key: 'technical', 
    title: 'VẬN HÀNH KỸ THUẬT',
    options: ['Giám sát hạ tầng', 'Bảo trì hệ thống', 'Quản lý tài sản']
  },
  { 
    key: 'hr', 
    title: 'QUẢN LÝ NHÂN SỰ', 
    active: true,
    options: ['Thông tin nhân sự', 'Chính sách thu nhập', 'Đào tạo và phát triển']
  },
]
</script>

<template>
  <a-layout-header class="top-header">
    <div class="header-left">
      <div class="logo">
        <span class="logo-brand">DNP</span>
        <span class="logo-divider">|</span>
        <span class="logo-sub">water</span>
      </div>
      
      <div class="toggle-btn" @click="layoutStore.toggleCollapsed">
        <component :is="layoutStore.collapsed ? 'MenuUnfoldOutlined' : 'MenuFoldOutlined'" />
      </div>
    </div>
    
    <div class="top-menu">
      <template v-for="mod in modules" :key="mod.key">
        <a-dropdown :trigger="['click']">
          <div :class="['top-menu-item', { active: mod.active }]">
            {{ mod.title }}
            <down-outlined style="font-size: 10px; margin-left: 6px" />
          </div>
          <template #overlay>
            <a-menu>
              <a-menu-item v-for="opt in mod.options" :key="opt">
                <a href="#">{{ opt }}</a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
    </div>

    <div class="header-right">
      <a-space size="large">
        <search-outlined class="header-icon" />
        <a-badge count="99+" :offset="[10, 0]">
          <bell-outlined class="header-icon" />
        </a-badge>
        <appstore-outlined class="header-icon" />
        <div class="user-profile">
          <a-avatar size="small" style="background-color: #f56a00">H</a-avatar>
          <span class="username">hieutm</span>
          <down-outlined style="font-size: 10px; color: #888" />
        </div>
      </a-space>
    </div>
  </a-layout-header>
</template>

<style scoped>
.top-header {
  background: #fff;
  padding: 0 16px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e8e8e8;
  z-index: 1001;
  position: sticky;
  top: 0;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  display: flex;
  align-items: center;
  font-family: 'Arial Black', sans-serif;
  cursor: pointer;
}

.logo-brand {
  font-size: 24px;
  color: #103567;
  font-weight: 900;
  letter-spacing: -1px;
}

.logo-divider {
  margin: 0 6px;
  color: #ccc;
  font-weight: normal;
}

.logo-sub {
  font-size: 20px;
  color: #103567;
  font-weight: 300;
}

.toggle-btn {
  font-size: 18px;
  cursor: pointer;
  color: #103567;
  display: flex;
  align-items: center;
}

.top-menu {
  display: flex;
  height: 100%;
  flex: 1;
  margin-left: 20px;
}

.top-menu-item {
  padding: 0 12px;
  cursor: pointer;
  height: 100%;
  display: flex;
  align-items: center;
  font-size: 11px;
  font-weight: 700;
  color: #103567;
  transition: all 0.2s;
  border-bottom: 3px solid transparent;
  white-space: nowrap;
  letter-spacing: 0.5px;
}

.top-menu-item:hover, .top-menu-item.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.header-icon {
  font-size: 18px;
  color: #666;
  cursor: pointer;
}

.header-icon:hover {
  color: #1890ff;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.user-profile:hover {
  background: #f5f5f5;
}

.username {
  font-weight: 500;
  font-size: 13px;
  color: #333;
}
</style>
