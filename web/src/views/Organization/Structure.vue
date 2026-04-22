<template>
  <div class="org-structure">
    <!-- FILTER SECTION -->
    <a-collapse v-model:activeKey="activeFilter" ghost>
      <a-collapse-panel key="1">
        <template #header>
          <span class="section-title">
            <down-outlined v-if="activeFilter.includes('1')" />
            <right-outlined v-else />
            Lọc tìm kiếm
          </span>
        </template>
        
        <div class="filter-form">
          <a-row :gutter="24">
            <a-col :span="8">
              <div class="form-label">Tên đơn vị</div>
              <a-input placeholder="Nhập tên đơn vị" />
            </a-col>
            <a-col :span="8">
              <div class="form-label">Khối nghiệp vụ</div>
              <a-select placeholder="Chọn khối nghiệp vụ" style="width: 100%">
                <a-select-option value="1">Khối vận hành</a-select-option>
                <a-select-option value="2">Khối hỗ trợ</a-select-option>
              </a-select>
            </a-col>
            <a-col :span="8">
              <div class="form-label">Loại đơn vị</div>
              <a-select placeholder="Chọn loại đơn vị" style="width: 100%">
                <a-select-option value="1">Tổng công ty</a-select-option>
                <a-select-option value="2">Công ty thành viên</a-select-option>
              </a-select>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-top: 16px">
            <a-col :span="8">
              <div class="form-label">Địa điểm chấm công linh hoạt</div>
              <a-select placeholder="Chọn giá trị" style="width: 100%" />
            </a-col>
            <a-col :span="16" style="display: flex; justify-content: flex-end; align-items: flex-end">
              <a-space>
                <a-button>Bỏ lọc</a-button>
                <a-button type="primary" class="btn-search">
                  <search-outlined /> Tìm kiếm
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </div>
      </a-collapse-panel>
    </a-collapse>

    <a-divider />

    <!-- RESULTS SECTION -->
    <div class="results-header">
      <span class="section-title">
        <down-outlined /> Kết quả tìm kiếm
      </span>
      <div class="header-actions">
        <a-input-search placeholder="Tìm kiếm nhanh..." style="width: 200px; margin-right: 16px" />
        <a-space>
          <a-button type="primary" class="btn-add">
            <plus-outlined /> Thêm mới
          </a-button>
          <a-button>
            <file-excel-outlined /> Xuất Excel
          </a-button>
          <a-button type="text">
            <fullscreen-outlined />
          </a-button>
          <a-button type="text">
            <reload-outlined />
          </a-button>
        </a-space>
      </div>
    </div>

    <a-table 
      :columns="columns" 
      :data-source="data" 
      :pagination="false"
      class="custom-table"
      size="middle"
    >
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.key === 'name'">
          <span :style="{ paddingLeft: (record.level_num.split('.').length - 1) * 20 + 'px' }">
            {{ record.level_num }}. {{ text }}
          </span>
        </template>
        <template v-if="column.key === 'action'">
          <a-space :size="12">
            <eye-outlined class="action-icon" />
            <edit-outlined class="action-icon blue" />
            <delete-outlined class="action-icon red" />
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  DownOutlined, 
  RightOutlined, 
  SearchOutlined, 
  PlusOutlined, 
  FileExcelOutlined, 
  FullscreenOutlined, 
  ReloadOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue'

const activeFilter = ref(['1'])

const columns = [
  { title: 'Tên đơn vị', dataIndex: 'name', key: 'name' },
  { title: 'Loại đơn vị', dataIndex: 'type', key: 'type' },
  { title: 'Cấp đơn vị', dataIndex: 'level', key: 'level' },
  { title: 'Khối nghiệp vụ', dataIndex: 'group', key: 'group' },
  { title: 'Trưởng đơn vị', dataIndex: 'leader', key: 'leader' },
  { title: 'Hành động', key: 'action', align: 'center' },
]

const data = [
  { 
    key: '1', 
    level_num: '1',
    name: 'Dnp water', 
    type: 'Tổng công ty', 
    level: 'VP HDQT v...', 
    group: 'Hỗ trợ quản trị', 
    leader: 'Phạm Loan Đào 123' 
  },
  { 
    key: '2', 
    level_num: '1.1',
    name: 'Dnpw: ctcp cấp thoát nước bình phước', 
    type: 'Công ty thành viên', 
    level: 'Công ty cấ...', 
    group: 'Hỗ trợ quản trị', 
    leader: 'Lương Thị Hằng' 
  },
  { 
    key: '3', 
    level_num: '1.2',
    name: 'CTCP Cấp thoát nước Phú Thọ', 
    type: 'Công ty thành viên', 
    level: 'VP HDQT v...', 
    group: 'Khối vận hành', 
    leader: 'Trần Đức Anh' 
  },
]
</script>

<style scoped>
.org-structure {
  padding: 0;
}

.section-title {
  font-weight: 700;
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-form {
  padding: 8px 12px;
}

.form-label {
  font-size: 12px;
  font-weight: 600;
  color: #666;
  margin-bottom: 4px;
}

.btn-search {
  background-color: #103567;
  border-color: #103567;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  align-items: center;
}

.btn-add {
  background-color: #103567;
  border-color: #103567;
}

.custom-table :deep(.ant-table-thead > tr > th) {
  background: #f8f9fa;
  font-weight: 700;
  font-size: 13px;
  color: #333;
}

.action-icon {
  font-size: 16px;
  cursor: pointer;
  color: #888;
}

.action-icon.blue { color: #1890ff; }
.action-icon.red { color: #ff4d4f; }

:deep(.ant-collapse-header) {
  padding: 0 !important;
}

:deep(.ant-collapse-content-box) {
  padding: 16px 0 0 0 !important;
}
</style>
