<template>
    <div class="min-h-screen bg-gray-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Header -->
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-900">Return Books</h1>
          <p class="text-gray-600 mt-2">Return your borrowed books to the library</p>
        </div>
  
        <!-- Return Methods -->
        <div class="bg-white rounded-lg shadow-lg p-6 mb-8">
          <h2 class="text-xl font-semibold text-gray-900 mb-4">Return Methods</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- Scan QR Code -->
            <div class="border border-gray-200 rounded-lg p-4">
              <div class="flex items-center mb-3">
                <svg class="w-6 h-6 text-blue-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 16h4.01M12 8h4.01M16 4h4.01M4 4h4.01M4 8h4.01M4 12h4.01M4 16h4.01" />
                </svg>
                <h3 class="font-medium text-gray-900">Scan QR Code</h3>
              </div>
              <p class="text-gray-600 text-sm mb-4">Scan the QR code on your book to return it quickly</p>
              <button
                @click="startQRScanner"
                class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
              >
                Start QR Scanner
              </button>
            </div>
  
            <!-- Manual Selection -->
            <div class="border border-gray-200 rounded-lg p-4">
              <div class="flex items-center mb-3">
                <svg class="w-6 h-6 text-green-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                </svg>
                <h3 class="font-medium text-gray-900">Manual Selection</h3>
              </div>
              <p class="text-gray-600 text-sm mb-4">Select books from your borrowed list to return</p>
              <button
                @click="showManualReturn = true"
                class="w-full px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
              >
                Select Books
              </button>
            </div>
          </div>
        </div>
  
        <!-- QR Scanner -->
        <div v-if="showQRScanner" class="bg-white rounded-lg shadow-lg p-6 mb-8">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-xl font-semibold text-gray-900">QR Code Scanner</h2>
            <button
              @click="stopQRScanner"
              class="text-gray-500 hover:text-gray-700"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <div class="bg-gray-100 rounded-lg p-8 text-center">
            <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 16h4.01M12 8h4.01M16 4h4.01M4 4h4.01M4 8h4.01M4 12h4.01M4 16h4.01" />
            </svg>
            <p class="text-gray-600">Point your camera at the QR code on the book</p>
            <p class="text-sm text-gray-500 mt-2">QR Scanner would be implemented here</p>
          </div>
        </div>
  
        <!-- Manual Return -->
        <div v-if="showManualReturn" class="bg-white rounded-lg shadow-lg p-6 mb-8">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-semibold text-gray-900">Select Books to Return</h2>
            <button
              @click="showManualReturn = false"
              class="text-gray-500 hover:text-gray-700"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
  
          <!-- Loading -->
          <div v-if="isLoading" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          </div>
  
          <!-- No borrowed books -->
          <div v-else-if="borrowedBooks.length === 0" class="text-center py-8">
            <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
            <h3 class="text-lg font-medium text-gray-900 mb-2">No books to return</h3>
            <p class="text-gray-600">You don't have any borrowed books to return</p>
          </div>
  
          <!-- Books list -->
          <div v-else class="space-y-4">
            <div
              v-for="borrow in borrowedBooks"
              :key="borrow.id"
              class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
            >
              <div class="flex items-center">
                <!-- Checkbox -->
                <input
                  v-model="selectedBooks"
                  :value="borrow.id"
                  type="checkbox"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded mr-4"
                />
  
                <!-- Book Cover -->
                <div class="w-12 h-16 bg-gradient-to-br from-blue-100 to-purple-100 rounded flex items-center justify-center mr-4">
                  <svg class="w-6 h-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                  </svg>
                </div>
  
                <!-- Book Info -->
                <div class="flex-1">
                  <h3 class="font-semibold text-gray-900">{{ borrow.book.title }}</h3>
                  <p class="text-gray-600 text-sm">by {{ borrow.book.author }}</p>
                  <div class="flex items-center space-x-4 text-xs text-gray-500 mt-1">
                    <span>Borrowed: {{ formatDate(borrow.borrowDate) }}</span>
                    <span>Due: {{ formatDate(borrow.dueDate) }}</span>
                    <span
                      :class="[
                        'px-2 py-1 rounded-full',
                        isOverdue(borrow.dueDate) ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
                      ]"
                    >
                      {{ isOverdue(borrow.dueDate) ? 'Overdue' : 'Active' }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
  
            <!-- Return Button -->
            <div v-if="selectedBooks.length > 0" class="pt-4 border-t">
              <div class="flex items-center justify-between">
                <p class="text-sm text-gray-600">
                  {{ selectedBooks.length }} book(s) selected for return
                </p>
                <button
                  @click="returnSelectedBooks"
                  :disabled="isReturning"
                  class="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50 transition-colors"
                >
                  {{ isReturning ? 'Returning...' : 'Return Selected Books' }}
                </button>
              </div>
            </div>
          </div>
        </div>
  
        <!-- Recent Returns -->
        <div v-if="recentReturns.length > 0" class="bg-white rounded-lg shadow-lg p-6">
          <h2 class="text-xl font-semibold text-gray-900 mb-4">Recent Returns</h2>
          <div class="space-y-3">
            <div
              v-for="returnItem in recentReturns"
              :key="returnItem.id"
              class="flex items-center justify-between p-3 bg-green-50 border border-green-200 rounded-lg"
            >
              <div class="flex items-center">
                <svg class="w-5 h-5 text-green-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <div>
                  <p class="font-medium text-gray-900">{{ returnItem.book.title }}</p>
                  <p class="text-sm text-gray-600">Returned on {{ formatDate(returnItem.returnDate) }}</p>
                </div>
              </div>
              <span class="text-sm text-green-700 font-medium">Returned</span>
            </div>
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
  import { ref, onMounted } from 'vue'
  
  // State
  const showQRScanner = ref(false)
  const showManualReturn = ref(false)
  const borrowedBooks = ref([])
  const selectedBooks = ref([])
  const recentReturns = ref([])
  const isLoading = ref(false)
  const isReturning = ref(false)
  const message = ref('')
  const errorMessage = ref('')
  
  // Methods
  const fetchBorrowedBooks = async () => {
    try {
      isLoading.value = true
      
      // Add your API call here
      // const response = await getCurrentBorrows()
      
      // Mock data for now
      borrowedBooks.value = [
        {
          id: 1,
          book: {
            id: 1,
            title: "The Great Gatsby",
            author: "F. Scott Fitzgerald"
          },
          borrowDate: "2024-01-15",
          dueDate: "2024-02-15"
        },
        {
          id: 2,
          book: {
            id: 2,
            title: "To Kill a Mockingbird",
            author: "Harper Lee"
          },
          borrowDate: "2024-01-20",
          dueDate: "2024-01-25" // Overdue
        }
      ]
      
    } catch (error) {
      console.error('Error fetching borrowed books:', error)
      errorMessage.value = 'Failed to load borrowed books'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isLoading.value = false
    }
  }
  
  const startQRScanner = () => {
    showQRScanner.value = true
    showManualReturn.value = false
    // Here you would initialize the QR scanner
  }
  
  const stopQRScanner = () => {
    showQRScanner.value = false
    // Here you would stop the QR scanner
  }
  
  const returnSelectedBooks = async () => {
    try {
      isReturning.value = true
      
      // Add your return books API call here
      // await returnBooksAPI(selectedBooks.value)
      
      // Move returned books to recent returns
      const returnedBooks = borrowedBooks.value.filter(borrow => 
        selectedBooks.value.includes(borrow.id)
      )
      
      returnedBooks.forEach(borrow => {
        recentReturns.value.unshift({
          ...borrow,
          returnDate: new Date().toISOString().split('T')[0]
        })
      })
      
      // Remove returned books from borrowed list
      borrowedBooks.value = borrowedBooks.value.filter(borrow => 
        !selectedBooks.value.includes(borrow.id)
      )
      
      selectedBooks.value = []
      showManualReturn.value = false
      
      message.value = `Successfully returned ${returnedBooks.length} book(s)`
      setTimeout(() => message.value = '', 3000)
      
    } catch (error) {
      console.error('Error returning books:', error)
      errorMessage.value = 'Failed to return books'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isReturning.value = false
    }
  }
  
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    })
  }
  
  const isOverdue = (dueDateString) => {
    const dueDate = new Date(dueDateString)
    const today = new Date()
    return dueDate < today
  }
  
  // Lifecycle
  onMounted(() => {
    fetchBorrowedBooks()
  })
  </script>