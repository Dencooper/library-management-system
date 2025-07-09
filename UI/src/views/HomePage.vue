<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Hero Section -->
    <div class="bg-gradient-to-r from-blue-600 to-purple-700">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
        <div class="text-center">
          <h1 class="text-4xl md:text-6xl font-bold text-white mb-6">
            Welcome to Our Library
          </h1>
          <p class="text-xl text-blue-100 mb-8 max-w-3xl mx-auto">
            Discover thousands of books, manage your reading journey, and explore the world of knowledge at your fingertips.
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link
              v-if="authStore.isAuthenticated"
              to="/books"
              class="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Discover Books
            </router-link>
            <router-link
              v-else
              to="/login"
              class="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Get Started
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div class="py-20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-3xl font-bold text-gray-900 mb-4">Why Choose Our Library?</h2>
          <p class="text-gray-600 max-w-2xl mx-auto">
            Experience the future of library management with our comprehensive digital platform
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="text-center p-6">
            <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">Easy Search</h3>
            <p class="text-gray-600">Find books quickly with our advanced search and filtering system</p>
          </div>

          <div class="text-center p-6">
            <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">Track Borrows</h3>
            <p class="text-gray-600">Keep track of your borrowed books and due dates effortlessly</p>
          </div>

          <div class="text-center p-6">
            <div class="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg class="w-8 h-8 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">Digital Management</h3>
            <p class="text-gray-600">Manage your library experience entirely online</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Stats Cards -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-gray-200 rounded-lg shadow-sm p-6 text-center">
          <div class="text-3xl mb-2">📚</div>
          <h3 class="text-2xl font-bold text-gray-900">{{ books }}</h3>
          <p class="text-gray-600">Books</p>
        </div>
        <div class="bg-gray-200 rounded-lg shadow-sm p-6 text-center">
          <div class="text-3xl mb-2">👥</div>
          <h3 class="text-2xl font-bold text-gray-900">{{ members }}</h3>
          <p class="text-gray-600">Users</p>
        </div>
        <div class="bg-gray-200 rounded-lg shadow-sm p-6 text-center">
          <div class="text-3xl mb-2">📖</div>
          <h3 class="text-2xl font-bold text-gray-900">{{ borrowings }}</h3>
          <p class="text-gray-600">Borrowings</p>
        </div>
      </div>
    </div>
    

    <!-- Books Preview Section -->
    <div class="bg-white py-20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-gray-900 mb-4">Featured Books</h2>
          <p class="text-gray-600">Discover some of our most popular titles</p>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Books Grid -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-6">
          <div
            v-for="book in featuredBooks"
            :key="book.id"
            @click="viewBook(book.id)"
            class="bg-white rounded-lg shadow-md hover:shadow-lg transition-all duration-200 cursor-pointer group"
          >
            <div class="p-4">
              <div class="relative mb-4">
                <img
                  :src="book.imageUrl || '/placeholder.svg?height=240&width=160'"
                  :alt="book.title"
                  class="w-full h-72 object-cover rounded-md group-hover:scale-105 transition-transform duration-200"
                />
                <div class="absolute top-2 right-2">
                  <span class="inline-block bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">
                    Available
                  </span>
                </div>
                <div class="absolute top-2 left-2">
                  <span
                    v-for="category in Array.from(book.categories).slice(0, 1)"
                    :key="category"
                    class="inline-block bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full"
                  >
                    {{ category.name }}
                  </span>
                </div>
              </div>
              <div class="space-y-2">
                <h3 class="font-semibold text-sm line-clamp-2 group-hover:text-blue-600 transition-colors">
                  {{ book.title }}
                </h3>
                <p class="text-sm text-gray-600 flex items-center">
                  <svg class="h-3 w-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                  {{ book.author.name }}
                </p>
                <p class="text-xs text-gray-500">ISBN: {{ book.isbn }}</p>
                <div class="flex items-center justify-between pt-2">
                  <span class="font-bold text-lg text-green-600">${{ book.price?.toFixed(2) || 'N/A' }}</span>
                  <button class="text-blue-600 hover:text-blue-800 text-sm font-medium">
                    View Details →
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-center mt-12">
          <router-link
            to="/books"
            class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            View All Books
            <svg class="ml-2 w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
            </svg>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getAllBooks, getUserQuantity, getBorrowingQuantity } from '@/api'

const router = useRouter()
const authStore = useAuthStore()

// State
const featuredBooks = ref([])
const bookItems = ref([])
const members = ref(0)
const books = ref(0)
const borrowings = ref(0)
const isLoading = ref(true)

// Methods
const fetchFeaturedBooks = async () => {
  try {
    isLoading.value = true
    const response = await getAllBooks()
    // Get first 5 books as featured
    featuredBooks.value = response.data.data.slice(0, 4)
    books.value = response.data.data.length
  } catch (error) {
    console.error('Error fetching featured books:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchUserQuantity = async () => {
  try {
    isLoading.value = true
    const response = await getUserQuantity()
    members.value = response.data.data
  } catch (error) {
    console.error('Error fetching user quantity:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchBorrowingQuantity = async () => {
  try {
    isLoading.value = true
    const response = await getBorrowingQuantity()
    borrowings.value = response.data.data
  } catch (error) {
    console.error('Error fetching user quantity:', error)
  } finally {
    isLoading.value = false
  }
}

const viewBook = (bookId) => {
  router.push({ name: 'BookDetail', params: { id: bookId } })
}

// Lifecycle
onMounted(() => {
  fetchFeaturedBooks()
  fetchUserQuantity()
  fetchBorrowingQuantity()
})
</script>