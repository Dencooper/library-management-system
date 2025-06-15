<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Borrow Books</h1>
          <p class="text-gray-600 mt-2">Search and select books to borrow</p>
        </div>
        <!-- Selected Books Counter -->
        <div class="flex items-center space-x-4">
          <div class="bg-blue-100 text-blue-800 px-4 py-2 rounded-lg">
            <span class="font-medium">Selected: {{ selectedBooks.length }}/3</span>
          </div>
          <button
            v-if="selectedBooks.length > 0"
            @click="showRequestModal = true"
            class="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors flex items-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            Create Request
          </button>
        </div>
      </div>

      <!-- Selected Books Summary -->
      <div v-if="selectedBooks.length > 0" class="bg-white rounded-lg shadow p-6 mb-8">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Selected Books for Borrowing</h3>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div
            v-for="book in selectedBooks"
            :key="book.id"
            class="flex items-center p-3 bg-green-50 border border-green-200 rounded-lg"
          >
            <img
              :src="book.imageUrl || '/placeholder.svg?height=60&width=40'"
              :alt="book.title"
              class="w-10 h-14 object-cover rounded mr-3"
            />
            <div class="flex-1 min-w-0">
              <div class="text-sm font-medium text-gray-900 truncate">{{ book.title }}</div>
              <div class="text-xs text-gray-500 truncate">{{ book.authorName }}</div>
            </div>
            <button
              @click="removeFromSelection(book)"
              class="ml-2 text-red-600 hover:text-red-800"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Search and Filters -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <div class="grid grid-cols-1 md:grid-cols-6 gap-4">
          <!-- Search -->
          <div class="md:col-span-4">
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
          <div>
            <select
              v-model="selectedCategory"
              @change="filterBooks"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Categories</option>
              <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
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

      <!-- Results count -->
      <div class="mb-6 flex items-center justify-between">
        <p class="text-gray-600">
          Showing {{ filteredBooks.length }} books
          <span v-if="searchQuery"> for "{{ searchQuery }}"</span>
          <span v-if="selectedCategory"> in "{{ selectedCategory }}"</span>
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

      <!-- Books Grid -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
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
              class="bg-white border border-gray-200 rounded-lg overflow-hidden hover:shadow-md transition-shadow"
              :class="{ 'ring-2 ring-green-500': isBookSelected(book) }"
            >
              <!-- Book Image -->
              <div class="aspect-w-3 aspect-h-4 bg-gray-200">
                <img
                  :src="book.imageUrl || '/placeholder.svg?height=200&width=140'"
                  :alt="book.title"
                  class="w-full h-64 object-cover"
                />
              </div>

              <!-- Book Info -->
              <div class="p-4">
                <h4 class="text-lg font-semibold text-gray-900 line-clamp-2">{{ book.title }}</h4>
                <p class="text-sm text-gray-600 mb-2">by {{ book.author.name }}</p>
                
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

                <!-- Availability Status -->
                <div class="flex items-center justify-between mb-3">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ book.isAvailable ? 'Available' : 'Not Available' }}
                  </span>
                  <span class="text-sm font-medium text-gray-900">${{ book.price?.toFixed(2) }}</span>
                </div>

                <!-- Action Buttons -->
                <div class="flex space-x-2">
                  <button
                    @click="viewBookDetail(book)"
                    class="flex-1 px-3 py-2 text-sm font-medium text-blue-600 bg-blue-50 border border-blue-200 rounded-md hover:bg-blue-100 transition-colors"
                  >
                    View Details
                  </button>
                  <button
                    v-if="book.isAvailable && !isBookSelected(book)"
                    @click="addToSelection(book)"
                    :disabled="selectedBooks.length >= 3"
                    class="flex-1 px-3 py-2 text-sm font-medium text-white bg-green-600 border border-transparent rounded-md hover:bg-green-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
                  >
                    {{ selectedBooks.length >= 3 ? 'Max Reached' : 'Select' }}
                  </button>
                  <button
                    v-else-if="isBookSelected(book)"
                    @click="removeFromSelection(book)"
                    class="flex-1 px-3 py-2 text-sm font-medium text-red-600 bg-red-50 border border-red-200 rounded-md hover:bg-red-100 transition-colors"
                  >
                    Remove
                  </button>
                  <button
                    v-else
                    disabled
                    class="flex-1 px-3 py-2 text-sm font-medium text-gray-400 bg-gray-100 border border-gray-200 rounded-md cursor-not-allowed"
                  >
                    Unavailable
                  </button>
                </div>
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

    <!-- Create Borrowing Request Modal -->
    <div v-if="showRequestModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeRequestModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <form @submit.prevent="submitBorrowingRequest">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Create Borrowing Request
              </h3>
              
              <!-- Selected Books Review -->
              <div class="mb-6">
                <h4 class="text-sm font-medium text-gray-900 mb-3">Selected Books ({{ selectedBooks.length }})</h4>
                <div class="space-y-3">
                  <div
                    v-for="book in selectedBooks"
                    :key="book.id"
                    class="flex items-center p-3 bg-gray-50 rounded-lg"
                  >
                    <img
                      :src="book.imageUrl || '/placeholder.svg?height=60&width=40'"
                      :alt="book.title"
                      class="w-12 h-16 object-cover rounded mr-4"
                    />
                    <div class="flex-1">
                      <div class="text-sm font-medium text-gray-900">{{ book.title }}</div>
                      <div class="text-sm text-gray-500">by {{ book.authorName }}</div>
                      <div class="text-xs text-gray-400">ISBN: {{ book.isbn }}</div>
                    </div>
                    <div class="text-sm font-medium text-gray-900">
                      ${{ book.price?.toFixed(2) }}
                    </div>
                  </div>
                </div>
              </div>

              <!-- Request Description -->
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  Description (Optional)
                </label>
                <textarea
                  v-model="requestDescription"
                  rows="3"
                  placeholder="Add any additional notes for your borrowing request..."
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                ></textarea>
              </div>

              <!-- Terms and Conditions -->
              <div class="mb-4">
                <label class="flex items-start">
                  <input
                    v-model="agreeToTerms"
                    type="checkbox"
                    required
                    class="mt-1 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  />
                  <span class="ml-2 text-sm text-gray-700">
                    I agree to the library's borrowing terms and conditions. I understand that books must be returned within 30 days and late fees may apply.
                  </span>
                </label>
              </div>

              <!-- Summary -->
              <div class="bg-blue-50 rounded-lg p-4">
                <div class="flex justify-between items-center">
                  <span class="text-sm font-medium text-blue-900">Total Books:</span>
                  <span class="text-sm font-bold text-blue-900">{{ selectedBooks.length }}</span>
                </div>
                <div class="flex justify-between items-center mt-1">
                  <span class="text-sm font-medium text-blue-900">Loan Period:</span>
                  <span class="text-sm font-bold text-blue-900">30 days</span>
                </div>
                <div class="flex justify-between items-center mt-1">
                  <span class="text-sm font-medium text-blue-900">Due Date:</span>
                  <span class="text-sm font-bold text-blue-900">{{ getDueDate() }}</span>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="isSubmitting || !agreeToTerms"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ isSubmitting ? 'Submitting...' : 'Submit Request' }}
              </button>
              <button
                @click="closeRequestModal"
                type="button"
                class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Success/Error Messages -->
    <div v-if="message" class="fixed top-4 right-4 z-50">
      <div class="bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg">
        {{ message }}
      </div>
    </div>

    <div v-if="errorMessage" class="fixed top-4 right-4 z-50">
      <div class="bg-red-500 text-white px-6 py-3 rounded-lg shadow-lg">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllBooks, createBorrowingRequest } from '@/api'

const router = useRouter()

// State
const books = ref([])
const selectedBooks = ref([])
const isLoading = ref(true)
const isSubmitting = ref(false)
const showRequestModal = ref(false)
const searchQuery = ref('')
const selectedCategory = ref('')
const sortBy = ref('title')
const showAvailableOnly = ref(false)
const requestDescription = ref('')
const agreeToTerms = ref(false)
const message = ref('')
const errorMessage = ref('')

// Computed
const categories = computed(() => {
  const allCategories = new Set()
  books.value.forEach(book => {
    if (book.categoryNames && Array.isArray(book.categoryNames)) {
      book.categoryNames.forEach(category => allCategories.add(category))
    }
  })
  return Array.from(allCategories).sort()
})

// Computed
const filteredBooks = computed(() => {
  let result = [...books.value]

  // Search filter
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(book => 
      book.title.toLowerCase().includes(query) || 
      book.authorName.toLowerCase().includes(query) ||
      book.isbn.toLowerCase().includes(query)
    )
  }
  
  // Category filter
  if (selectedCategory.value) {
    result = result.filter(book => 
      book.categoryNames && book.categoryNames.includes(selectedCategory.value)
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

const isBookSelected = (book) => {
  return selectedBooks.value.some(selected => selected.id === book.id)
}

const addToSelection = (book) => {
  if (selectedBooks.value.length >= 3) {
    errorMessage.value = 'You can only select up to 3 books per request'
    setTimeout(() => errorMessage.value = '', 3000)
    return
  }
  
  if (!isBookSelected(book)) {
    selectedBooks.value.push(book)
    message.value = `"${book.title}" added to selection`
    setTimeout(() => message.value = '', 2000)
  }
}

const removeFromSelection = (book) => {
  selectedBooks.value = selectedBooks.value.filter(selected => selected.id !== book.id)
  message.value = `"${book.title}" removed from selection`
  setTimeout(() => message.value = '', 2000)
}

const viewBookDetail = (book) => {
  router.push(`/book/${book.id}`)
}

const getDueDate = () => {
  const dueDate = new Date()
  dueDate.setDate(dueDate.getDate() + 30)
  return dueDate.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const submitBorrowingRequest = async () => {
  try {
    isSubmitting.value = true
    
    const requestData = {
      bookIds: selectedBooks.value.map(book => ( book.id))
    }
    
    await createBorrowingRequest(requestData)

    message.value = `Borrowing request submitted successfully for ${selectedBooks.value.length} book(s)!`
    
    // Reset form
    selectedBooks.value = []
    requestDescription.value = ''
    agreeToTerms.value = false
    closeRequestModal()
    
    setTimeout(() => {
      message.value = ''
      router.push({ name: 'my-borrows' })
    }, 3000)
    
  } catch (error) {
    console.error('Error submitting borrowing request:', error)
    errorMessage.value = 'Failed to submit borrowing request. Please try again.'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isSubmitting.value = false
  }
}

const closeRequestModal = () => {
  showRequestModal.value = false
}

// Lifecycle
onMounted(() => {
  fetchBooks()
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
