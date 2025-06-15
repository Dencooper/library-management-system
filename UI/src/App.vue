<template>
  <div id="app">
    <!-- Header -->
    <Header />
    <!-- Main content -->
    <main>
      <router-view />
    </main>

    <!-- Footer -->
    <Footer />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'

const authStore = useAuthStore()

// Lifecycle
onMounted(async () => {
  // Initialize authentication on app startup
  await authStore.checkAuthOnStartup()
})
</script>