<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import DefaultLayout from './layouts/DefaultLayout.vue'

const route = useRoute()
const layoutComponent = computed(() => {
  switch (route.meta.layout) {
    case 'none':
      return null
    default:
      return DefaultLayout
  }
})
</script>

<template>
  <component v-if="layoutComponent" :is="layoutComponent">
    <RouterView />
  </component>
  <RouterView v-else />
</template>
