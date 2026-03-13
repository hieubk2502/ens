<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <header class="nav">
      <div class="brand">
        <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="36" height="36" />
        <span>WebApp</span>
      </div>
      <nav>
        <RouterLink to="/">Trang chủ</RouterLink>
        <RouterLink to="/dashboard">Dashboard</RouterLink>
      </nav>
    </header>

    <main class="content">
      <slot />
    </main>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: grid;
  grid-template-rows: auto 1fr;
  gap: 1.5rem;
}

.nav {
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: #f8fafc;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 700;
  color: #0f172a;
}

nav {
  display: flex;
  gap: 1rem;
}

nav a {
  text-decoration: none;
  color: #0f172a;
  font-weight: 600;
  padding: 0.35rem 0.65rem;
  border-radius: 8px;
  transition: background-color 120ms ease, color 120ms ease;
}

nav a.router-link-active {
  background: #16a34a;
  color: #fff;
}

.content {
  padding: 0 0.5rem 2rem;
}

.logo {
  display: block;
}
</style>
