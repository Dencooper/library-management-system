<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Browse Books</h1>
          <p class="text-gray-600 mt-2">Discover and explore our book collection</p>
        </div>
        <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
          <div class="flex items-center">
            <svg class="w-5 h-5 text-blue-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <div class="text-sm text-blue-800">
              <div class="font-medium">Need to borrow books?</div>
              <div>Please visit the library or contact a librarian to create a borrowing.</div>
            </div>
          </div>
        </div>
      </div>

        <!-- Search and Filters -->
        <div class="bg-white rounded-lg shadow p-6 mb-8">
          <div class="grid grid-cols-1 md:grid-cols-8 gap-4">
            <!-- Search -->
            <div class="md:col-span-5">
              <div class="relative">
                <input
                  v-model="searchQuery"
                  type="text"
                  placeholder="Search by title, author, or ISBN..."
                  class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  @input="searchBooks"
                />
                <svg class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
            </div>
  
            <!-- Category Filter -->
            <div class="md:col-span-2">
              <select
                v-model="selectedCategory"
                @change="filterBooks"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Categories</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
              
            </div>
  
            <div>
              <select
                v-model="sortBy"
                @change="handleSort"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="title">Sort by Title</option>
                <option value="author">Sort by Author</option>
                <option value="price">Sort by Price</option>
              </select>
            </div>
          </div>
        </div>

      <!-- Books Grid -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
          <p class="text-gray-600">
            Showing {{ filteredBooks.length }} books
            <span v-if="searchQuery"> for "{{ searchQuery }}"</span>
          </p>
          <div class="flex items-center">
            <label class="flex items-center cursor-pointer">
              <input
                type="checkbox"
                v-model="showAvailableOnly"
                @change="handleAvailabilityFilter"
                class="mr-2 h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <span class="text-sm text-gray-700">Available only</span>
            </label>
          </div>
        </div>

        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Books Grid -->
        <div v-else-if="filteredBooks.length > 0" class="p-6">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            <div
              v-for="book in filteredBooks"
              :key="book.id"
              class="group bg-white border border-gray-200 rounded-lg overflow-hidden hover:shadow-md transition-shadow"
            >
              <!-- Book Image -->
              <div class="aspect-w-3 aspect-h-4 bg-gray-200">
                <img
                  :src="book.imageUrl || '/placeholder.svg?height=200&width=140'"
                  :alt="book.title"
                  class="w-full h-72 object-cover rounded-md group-hover:scale-105 transition-transform duration-200"
                />
              </div>

              <!-- Book Info -->
              <div class="p-4">
                <h3 class="truncate text-lg font-semibold text-gray-900 w-full mb-3">
                    {{ book.title }}
                </h3>
                <div class="flex justify-between items-start mb-2">
                  <p class="text-sm text-gray-600 mb-2">by {{ book.author.name }}</p>

                  <span
                    :class="[
                      'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium',
                      book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ book.isAvailable ? 'Available' : 'Unavailable' }}
                  </span>
                </div>

                <!-- Book Details -->
                <div class="space-y-2 mb-3">
                  <div class="flex justify-between items-center">
                    <span class="text-xs text-gray-500">ISBN:</span>
                    <span class="text-xs font-mono text-gray-700">{{ book.isbn }}</span>
                  </div>
                  <div class="flex justify-between items-center">
                    <span class="text-xs text-gray-500">Price:</span>
                    <span class="text-sm font-medium text-gray-900">${{ book.price?.toFixed(2) }}</span>
                  </div>
                 
                </div>

                <!-- Categories -->
                <div class="flex flex-wrap gap-1 mb-3">
                  <span
                      v-for="category in book.categories.slice(0, 2)"
                      :key="category.id"
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                    >
                      {{ category.name }}
                    </span>
                    <span v-if="book.categories.length > 2" class="text-xs text-gray-500">
                      +{{ book.categories.length - 2 }} more
                    </span>
                </div>

                <!-- Action Button -->
                <button
                  @click="viewBookDetail(book)"
                  class="w-full px-3 py-2 mt-3 text-sm font-medium text-blue-600 bg-blue-50 border border-blue-200 rounded-md hover:bg-blue-100 transition-colors"
                >
                  View Details
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- No Results -->
        <div v-else class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No books found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Book Details Modal -->
    <div v-if="showDetailsModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeDetailsModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div v-if="selectedBook" class="space-y-6">
              <!-- Book Header -->
              <div class="flex items-start space-x-4">
                <img
                  :src="selectedBook.imageUrl || '/placeholder.svg?height=200&width=140'"
                  :alt="selectedBook.title"
                  class="w-24 h-32 object-cover rounded-lg"
                />
                <div class="flex-1">
                  <h3 class="text-xl font-bold text-gray-900 mb-2">{{ selectedBook.title }}</h3>
                  <p class="text-lg text-gray-600 mb-2">by {{ selectedbook.author.name }}</p>
                  <div class="flex items-center space-x-4">
                    <span
                      :class="[
                        'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium',
                        selectedBook.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      ]"
                    >
                      {{ selectedBook.isAvailable ? 'Available' : 'Not Available' }}
                    </span>
                    <span class="text-lg font-bold text-gray-900">${{ selectedBook.price?.toFixed(2) }}</span>
                  </div>
                </div>
              </div>

              <!-- Book Details -->
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <h4 class="text-sm font-medium text-gray-900 mb-2">ISBN</h4>
                  <p class="text-sm text-gray-600 font-mono">{{ selectedBook.isbn }}</p>
                </div>
                <div>
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Publication Year</h4>
                  <p class="text-sm text-gray-600">{{ selectedBook.publicationYear || 'N/A' }}</p>
                </div>
              </div>

              <!-- Categories -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Categories</h4>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="category in selectedBook.categoryNames"
                    :key="category"
                    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                  >
                    {{ category }}
                  </span>
                </div>
              </div>

              <!-- Description -->
              <div v-if="selectedBook.description">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Description</h4>
                <p class="text-sm text-gray-600 leading-relaxed">{{ selectedBook.description }}</p>
              </div>

              <!-- Borrowing Notice -->
              <div class="bg-yellow-50 border border-yellow-200 rounded-md p-4">
                <div class="flex">
                  <svg class="h-5 w-5 text-yellow-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                  </svg>
                  <div class="text-sm text-yellow-800">
                    <div class="font-medium">Want to borrow this book?</div>
                    <div>Please visit the library or contact a librarian to create a borrowing for you.</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button
              @click="closeDetailsModal"
              type="button"
              class="w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getAllBooks } from '@/api'
  
  const router = useRouter()
  
  // State
  const books = ref([])
  const isLoading = ref(true)
  const searchQuery = ref('')
  const selectedCategory = ref('')
  const sortBy = ref('title')
  const showAvailableOnly = ref(false)
  const message = ref('')
  const errorMessage = ref('')
  const categories = ref([])
  
  // Computed
  const filteredBooks = computed(() => {
    let result = [...books.value]
  
    // Search filter
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      result = result.filter(book => 
        book.title.toLowerCase().includes(query) || 
        book.author.name.toLowerCase().includes(query) ||
        book.isbn.toLowerCase().includes(query)
      )
    }
    
    // Filter by category
    if (selectedCategory.value) {
      result = result.filter(book => 
        book.categories.some(cat => cat.id === parseInt(selectedCategory.value))
      )
    }
  
      // Availability filter
    if (showAvailableOnly.value) {
      result = result.filter(book => book.isAvailable === true)
    }
    
    // Sort
    switch (sortBy.value) {
      case 'title':
        result.sort((a, b) => a.title.localeCompare(b.title))
        break
      case 'author':
        result.sort((a, b) => a.authorName.localeCompare(b.authorName))
        break
      case 'price':
        result.sort((a, b) => (a.price || 0) - (b.price || 0))
        break
    }
    
    return result
  })
  
  // Methods
  const fetchBooks = async () => {
    try {
      isLoading.value = true
      const response = await getAllBooks()
      books.value = response.data.data
    } catch (error) {
      console.error('Error fetching books:', error)
      errorMessage.value = 'Failed to load books'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isLoading.value = false
    }
  }
  
  const fetchCategories = async () => {
    categories.value = [
      { id: 1, name: "Novel" },
      { id: 2, name: "Science Fiction" },
      { id: 3, name: "Mystery" },
      { id: 4, name: "Horror" },
      { id: 5, name: "Romance" },
      { id: 6, name: "History" },
      { id: 7, name: "Science" },
      { id: 8, name: "Philosophy" },
      { id: 9, name: "Psychology" },
      { id: 10, name: "Economics" },
      { id: 11, name: "Politics" },
      { id: 12, name: "Art" },
      { id: 13, name: "Music" },
      { id: 14, name: "Cuisine" },
      { id: 15, name: "Travel" },
      { id: 16, name: "Sports" },
      { id: 17, name: "Information Technology" },
      { id: 18, name: "Medicine" },
      { id: 19, name: "Religion" },
      { id: 20, name: "Children's Books" }
    ]
  }
  
  const viewBookDetail = (book) => {
    router.push(`/books/${book.id}`)
  }
  
  // Lifecycle
  onMounted(() => {
    fetchBooks()
    fetchCategories()
  })
  </script>
  
  <style scoped>
  .line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  </style>