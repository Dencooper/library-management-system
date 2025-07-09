<template>
  <div class="min-h-screen bg-gray-100">

    <!-- Header with Back Button -->
    <header>
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center">
            <button @click="goHome" class="mr-4 text-gray-600 hover:text-gray-900 flex items-center">
              <svg class="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
              Home
            </button>
            <button @click="goBack" class="mr-4 text-gray-600 hover:text-gray-900 flex items-center">
              <svg class="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
              </svg>
              Books
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Loading -->
    <div v-if="loading" class="flex justify-center items-center min-h-96">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
    </div>

    <!-- Book Not Found -->
    <div v-else-if="!book" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 text-center">
      <div class="text-gray-400 text-6xl mb-4">📚</div>
      <h1 class="text-2xl font-bold text-gray-900 mb-4">Book Not Found</h1>
      <p class="text-gray-600 mb-8">The book you're looking for doesn't exist or has been removed.</p>
      <button @click="goBack" class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors">
        Go Back to Library
      </button>
    </div>

    <!-- Book Detail -->
    <div v-else class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Book Image -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg shadow-lg p-6">
            <img
              :src="book.imageUrl || '/placeholder.svg?height=500&width=350'"
              :alt="book.title"
              class="w-full h-auto rounded-lg shadow-md"
            />
          </div>
        </div>

        <!-- Book Information -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Title and Author -->
          <div class="bg-white rounded-lg shadow-lg p-6">
            <h1 class="text-4xl font-bold text-gray-900 mb-4">{{ book.title }}</h1>
            <div class="flex items-center text-xl text-gray-600 mb-4">
              <svg class="h-6 w-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              <span>by {{ book.author.name }}</span>
            </div>

            <!-- Categories -->
            <div class="flex flex-wrap gap-2 mb-6">
              <span
                v-for="category in book.categories"
                :key="category.id"
                class="inline-block bg-blue-100 text-blue-800 text-sm px-3 py-1 rounded-full"
              >
                {{ category.name }}
              </span>
            </div>

            <!-- Book Details -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-4">
              <div class="space-y-3">
                <div class="flex items-center">
                  <span class="font-semibold text-gray-700 w-20">ISBN:</span>
                  <span class="text-gray-600 font-mono">{{ book.isbn }}</span>
                </div>
                <div class="flex items-center">
                  <span class="font-semibold text-gray-700 w-20">Price:</span>
                  <span class="text-2xl font-bold text-green-600">${{ book.price?.toFixed(2) || 'N/A' }}</span>
                </div>
              </div>
              <div class="space-y-3">
                <div class="flex items-center">
                  <span class="font-semibold text-gray-700 w-24">Status:</span>
                  <span 
                    v-if="book.isAvailable"
                    class="inline-block bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full"
                  >
                    Available
                  </span>
                  <span 
                    v-else
                    class="inline-block bg-red-100 text-red-800 text-xs px-2 py-1 rounded-full"
                  >
                    Unavailable
                  </span>
                </div>
                <div class="flex items-center">
                  <span class="font-semibold text-gray-700 w-24">Location:</span>
                  <span class="text-gray-600">{{ book.shelf.location || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Book Description -->
          <div class="bg-white rounded-lg shadow-lg p-6">
            <h2 class="text-2xl font-bold text-gray-900 mb-4">Description</h2>
            <p class="text-gray-700 leading-relaxed mb-4">
              This is an excellent book that provides comprehensive coverage of its subject matter. 
              The author {{ book.author.name }} presents the material in an engaging and accessible way, 
              making it suitable for both beginners and advanced readers.
            </p>
            <p class="text-gray-700 leading-relaxed">
              With its clear explanations and practical examples, this book serves as an invaluable 
              resource for anyone interested in {{ book.categoryNames?.[0]?.toLowerCase() || 'this topic' }}.
            </p>
          </div>

          <!-- Availability Information -->
          <div class="bg-white rounded-lg shadow-lg p-6">
            <h2 class="text-2xl font-bold text-gray-900 mb-4">Availability</h2>
            <div class="space-y-4">
              <div v-if="bookItems.length === 0" class="flex items-center justify-between p-4 bg-red-50 rounded-lg">
                <div class="flex items-center">
                  <div class="w-4 h-4 bg-red-500 rounded-full mr-3"></div>
                  <span class="font-medium text-red-800">Currently unavailable for borrowing</span>
                </div>
              </div>

              <div  v-else class="flex items-center justify-between p-4 bg-green-50 rounded-lg">
                <div class="flex items-center">
                  <div class="w-4 h-4 bg-green-500 rounded-full mr-3"></div>
                  <span class="font-medium text-green-800">Available for borrowing</span>
                </div>
                <span class="text-green-700 font-semibold">{{ bookItems.length }} copies available</span>

              </div>

              <div v-if="bookItems.length > 0" class="text-sm text-center text-gray-600">
                <div class="font-medium">Want to borrow this book?</div>
                <div>Please visit the library or contact a librarian to create a borrowing for you.</div>
              </div>
              
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-gray-600">
                <div>
                  <h4 class="font-semibold text-gray-800 mb-2">Borrowing Information:</h4>
                  <ul class="space-y-1">
                    <li>• Maximum loan period: 30 days</li>
                    <li>• Penalty fee: ${{ book.price }} per book</li>
                  </ul>
                </div>
                <div>
                  <h4 class="font-semibold text-gray-800 mb-2">Location Details:</h4>
                  <ul class="space-y-1">
                    <li>• Shelf code: {{ book.shelf.code }}</li>
                    <li>• Section: {{ book.categoryNames?.[0] || 'General' }}</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          

        </div>
      </div>
      <!-- Related Books -->
      <div class="mt-12">
        <h2 class="text-2xl font-bold text-gray-900 mb-6">Related Books</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          <div
            v-for="relatedBook in relatedBooks"
            :key="relatedBook.id"
            @click="viewBook(relatedBook)"
            class="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow cursor-pointer group"
          >
            <div class="p-4">
              <img
                :src="relatedBook.imageUrl || '/placeholder.svg?height=200&width=140'"
                :alt="relatedBook.title"
                class="w-full h-64 object-cover rounded-md mb-3 group-hover:scale-105 transition-transform duration-200"
              />
              <h3 class="text-lg font-semibold line-clamp-2 group-hover:text-blue-600 mb-2">
                {{ relatedBook.title }}
              </h3>
              <p class="text-sm text-gray-600 mb-2">{{ relatedBook.author.name }}</p>
              <button class="w-full bg-blue-100 text-blue-800 py-2 px-3 rounded text-sm hover:bg-blue-200 transition-colors">
                View Details
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Success/Error Messages -->
    <div v-if="message" class="fixed bottom-4 right-4 z-50">
      <div class="bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg">
        {{ message }}
      </div>
    </div>

    <div v-if="errorMessage" class="fixed bottom-4 right-4 z-50">
      <div class="bg-red-500 text-white px-6 py-3 rounded-lg shadow-lg">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBook, getBookItemsByBook, getAllBooks } from '../api'

// Router instances
const route = useRoute()
const router = useRouter()

// State
const loading = ref(true)
const book = ref(null)
const bookItems = ref([])
const relatedBooks = ref([])
const message = ref('')
const errorMessage = ref('')
// Load related books
const loadRelatedBooks = async () => {
  try {
    const response = await getAllBooks()
    relatedBooks.value = response.data.data.filter(
      b =>
        b.id !== book.value.id &&
        b.categories.some(cat =>
          book.value.categories.some(selected =>
            selected.name === cat.name
          )
        )
    ).slice(0, 4)
  } catch (error) {
    console.error('Error loading related books:', error)
    relatedBooks.value = []
  }
}

// Methods
const loadBook = async () => {
  try {
    const bookId = route.params.id
    const response = await getBook(bookId)
    book.value = response.data.data
  } catch (error) {
    console.error('Error loading book:', error)
    book.value = null
  } finally {
    loading.value = false
  }
}

const loadBookItems = async () => {
  try {
    const bookId = route.params.id
    const response = await getBookItemsByBook(bookId, true)
    bookItems.value = response.data.data
  } catch (error) {
    console.error('Error loading book:', error)
    bookItems.value = null
  } finally {
    loading.value = false
  }
}

const viewBook = (book) => {
  router.push(`/books/${book.id}`)
}

watch(() => route.params.id, () => {
  loadBook()
  loadBookItems()
  loadRelatedBooks()
})

const goBack = () => {
  router.push('/books')
}

const goHome = () => {
  router.push('/')
}

onMounted(() => {
  loadBook()
  loadBookItems()
  loadRelatedBooks()
})
</script>