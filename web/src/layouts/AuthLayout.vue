<template>
  <div class="auth-shell">
    <div class="auth-card">
      <slot />
    </div>
  </div>
</template>

<style scoped>
.auth-shell {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: radial-gradient(circle at 10% 20%, #e0f2fe 0, transparent 25%),
    radial-gradient(circle at 90% 30%, #fef3c7 0, transparent 20%),
    #f8fafc;
  padding: 1rem;
}

.auth-card {
  width: min(420px, 100%);
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 18px 60px rgba(15, 23, 42, 0.12);
}
</style>
