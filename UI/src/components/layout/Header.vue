<template>
  <header class="bg-white shadow-sm border-b border-gray-200">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- Logo -->
        <div class="flex items-center">
          <router-link to="/" class="flex items-center">
            <svg class="h-8 w-8 text-blue-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
            <span class="text-xl font-bold text-gray-900">Library System</span>
          </router-link>
        </div>

        <!-- Navigation -->
        <nav class="hidden md:flex space-x-8">
          <!-- User navigation -->
          <template v-if="authStore.hasRole('USER')">
            <router-link to="/" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Home
            </router-link>
            <router-link to="/borrow" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Borrow Books
            </router-link>
            <router-link to="/my-borrowing-requests" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              My Requests
            </router-link>
            <router-link to="/my-borrowings" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              My Borrowings
            </router-link>
            <router-link to="/penalties" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Penalties
            </router-link>
          </template>
          
          <!-- Librarian navigation -->
          <template v-if="authStore.hasRole('LIBRARIAN')">
            <router-link to="/librarian/dashboard" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Dashboard
            </router-link>
            <router-link to="/librarian/borrowing-requests" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Requests
            </router-link>
            <router-link to="/librarian/borrowings" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Borrowings
            </router-link>
            <router-link to="/librarian/penalties" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Penalties
            </router-link>
            <router-link to="/librarian/books" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Books
            </router-link>
            <router-link to="/librarian/book-items" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Book Items
            </router-link>
            <router-link to="/librarian/shelves" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Shelves
            </router-link>
          </template>
          
          <!-- Admin navigation -->
          <template v-if="authStore.hasRole('ADMIN')">
            <router-link to="/admin/dashboard" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Dashboard
            </router-link>
            <router-link to="/admin/books" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Books
            </router-link>
            <router-link to="/admin/book-items" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Book Items
            </router-link>
            <router-link to="/admin/shelves" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Shelves
            </router-link>
            <router-link to="/admin/borrowing-requests" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Requests
            </router-link>
            <router-link to="/admin/borrowings" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Borrowings
            </router-link>
            <router-link to="/admin/penalties" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Penalties
            </router-link>
            <router-link to="/admin/users" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              User Management
            </router-link>
            <router-link to="/admin/accounts" class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium">
              Account Management
            </router-link>
          </template>
        </nav>

        <!-- User menu -->
        <div class="flex items-center space-x-4">
          <!-- User dropdown -->
          <div v-if="authStore.isAuthenticated" class="relative">
            <button
              @click="showUserMenu = !showUserMenu"
              class="flex items-center text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <div class="h-8 w-8 rounded-full bg-gray-300 flex items-center justify-center">
                <span class="text-sm font-medium text-gray-700">
                  {{ authStore.user?.fullName?.charAt(0).toUpperCase() }}
                </span>
              </div>
            </button>

            <!-- Dropdown menu -->
            <div
              v-if="showUserMenu"
              @click.away="showUserMenu = false"
              class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50"
            >
              <div class="px-4 py-2 text-sm text-gray-700 border-b">
                <div class="font-medium">{{ authStore.user?.fullName }}</div>
                <div class="text-gray-500">{{ authStore.user?.email }}</div>
              </div>
              <router-link
                to="/profile"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                @click="showUserMenu = false"
              >
                Profile
              </router-link>
              <router-link
                to="/settings"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                @click="showUserMenu = false"
              >
                Settings
              </router-link>
              <button
                @click="logout"
                class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
              >
                Sign out
              </button>
            </div>
          </div>

          <!-- Login/Register buttons -->
          <div v-else class="flex items-center space-x-4">
            <router-link
              to="/login"
              class="text-gray-900 hover:text-blue-600 px-3 py-2 text-sm font-medium"
            >
              Login
            </router-link>
            <router-link
              to="/register"
              class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium"
            >
              Register
            </router-link>
          </div>
        </div>

        <!-- Mobile menu button -->
        <div class="md:hidden">
          <button
            @click="showMobileMenu = !showMobileMenu"
            class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
          >
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path v-if="!showMobileMenu" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Mobile menu -->
      <div v-if="showMobileMenu" class="md:hidden">
        <div class="px-2 pt-2 pb-3 space-y-1 sm:px-3 border-t border-gray-200">
          <!-- Public navigation -->
          <router-link to="/" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
            Home
          </router-link>
          <router-link to="/books" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
            Browse Books
          </router-link>

          <!-- User navigation -->
          <template v-if="authStore.hasRole('USER')">
            <router-link to="/borrow" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Borrow Books
            </router-link>
            <router-link to="/my-borrowing-requests" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              My Requests
            </router-link>
            <router-link to="/my-borrowings" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              My Borrowings
            </router-link>
            <router-link to="/penalties" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Penalties
            </router-link>
          </template>
          
          <!-- Librarian navigation -->
          <template v-if="authStore.hasRole('LIBRARIAN')">
            <router-link to="/librarian/dashboard" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Dashboard
            </router-link>
            <router-link to="/librarian/books" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Books
            </router-link>
            <router-link to="/librarian/book-items" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Book Items
            </router-link>
            <router-link to="/librarian/shelves" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Shelves
            </router-link>
            <router-link to="/librarian/borrowing-requests" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Requests
            </router-link>
            <router-link to="/librarian/borrowings" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Borrowings
            </router-link>
            <router-link to="/librarian/penalties" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Penalties
            </router-link>
          </template>
          
          <!-- Admin navigation -->
          <template v-if="authStore.hasRole('ADMIN')">
            <router-link to="/admin/dashboard" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Dashboard
            </router-link>
            <router-link to="/admin/books" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Books
            </router-link>
            <router-link to="/admin/book-items" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Book Items
            </router-link>
            <router-link to="/admin/shelves" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Shelves
            </router-link>
            <router-link to="/admin/borrowing-requests" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Requests
            </router-link>
            <router-link to="/admin/borrowings" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Borrowings
            </router-link>
            <router-link to="/admin/penalties" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Penalties
            </router-link>
            <router-link to="/admin/users" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              User Management
            </router-link>
            <router-link to="/admin/accounts" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Account Management
            </router-link>
          </template>

          <!-- Mobile auth buttons -->
          <div v-if="!authStore.isAuthenticated" class="pt-4 border-t border-gray-200">
            <router-link to="/login" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Login
            </router-link>
            <router-link to="/register" class="block px-3 py-2 text-base font-medium text-gray-900 hover:text-blue-600">
              Register
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const showUserMenu = ref(false)
const showMobileMenu = ref(false)

const logout = async () => {
  await authStore.logout()
  showUserMenu.value = false
  router.push('/')
}
</script>
