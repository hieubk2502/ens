import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLayoutStore = defineStore('layout', () => {
  const collapsed = ref(false)
  const activeModule = ref('hr') // Module đang chọn: 'admin', 'hr', etc.

  function setCollapsed(value: boolean) {
    collapsed.value = value
  }

  function toggleCollapsed() {
    collapsed.value = !collapsed.value
  }

  function setActiveModule(moduleKey: string) {
    activeModule.value = moduleKey
  }

  return { 
    collapsed, 
    activeModule, 
    setCollapsed, 
    toggleCollapsed, 
    setActiveModule 
  }
})
