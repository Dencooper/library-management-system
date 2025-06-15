<template>
    <div class="min-h-screen bg-gray-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Header -->
        <div class="flex items-center justify-between mb-8">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">My Borrowing Requests</h1>
            <p class="text-gray-600 mt-2">View the status of your book borrowing requests</p>
          </div>
        </div>
  
        <!-- Status Summary -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <div class="bg-white rounded-lg shadow p-6 flex items-center">
            <div class="rounded-full bg-yellow-100 p-3 mr-4">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">Pending</p>
              <p class="text-2xl font-bold text-gray-900">{{ pendingCount }}</p>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow p-6 flex items-center">
            <div class="rounded-full bg-green-100 p-3 mr-4">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">Accepted</p>
              <p class="text-2xl font-bold text-gray-900">{{ acceptedCount }}</p>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow p-6 flex items-center">
            <div class="rounded-full bg-red-100 p-3 mr-4">
              <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </div>
            <div>
              <p class="text-sm text-gray-600">Rejected</p>
              <p class="text-2xl font-bold text-gray-900">{{ rejectedCount }}</p>
            </div>
          </div>
        </div>
  
        <!-- Filters -->
        <div class="bg-white rounded-lg shadow p-6 mb-8">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Search -->
            <div class="md:col-span-1">
              <label class="block text-sm font-medium text-gray-700 mb-2">Search</label>
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by book title..."
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                @input="filterRequests"
              />
            </div>
  
            <!-- Status Filter -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
              <select
                v-model="selectedStatus"
                @change="filterRequests"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Status</option>
                <option value="PENDING">Pending</option>
                <option value="APPROVED">Accepted</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
  
            <!-- Date Filter -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Date Range</label>
              <select
                v-model="dateFilter"
                @change="filterRequests"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Time</option>
                <option value="today">Today</option>
                <option value="week">This Week</option>
                <option value="month">This Month</option>
              </select>
            </div>
          </div>
        </div>
  
        <!-- Requests List -->
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-lg font-semibold text-gray-900">
              Borrowing Requests ({{ filteredRequests.length }})
            </h2>
          </div>
  
          <!-- Loading -->
          <div v-if="isLoading" class="flex justify-center py-12">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
  
          <!-- Requests List -->
          <div v-else-if="filteredRequests.length > 0" class="divide-y divide-gray-200">
            <div
              v-for="request in filteredRequests"
              :key="request.id"
              class="p-6 hover:bg-gray-50"
            >
              <div class="flex flex-col md:flex-row md:items-center md:justify-between">
                <!-- Request Info -->
                <div class="mb-4 md:mb-0">
                  <div class="flex items-center">
                    <span
                      :class="[
                        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mr-2',
                        getStatusClass(request.status)
                      ]"
                    >
                      {{ request.status }}
                    </span>
                    <h3 class="text-lg font-medium text-gray-900">Request #{{ request.id }}</h3>
                  </div>
                  <p class="text-sm text-gray-500 mt-1">
                    Requested on {{ formatDate(request.requestedAt) }}
                  </p>
                </div>
  
                <!-- Status Info -->
                <div class="flex flex-col items-start md:items-end">
                  <div v-if="request.status === 'APPROVED'" class="text-sm text-green-600">
                    Accepted on {{ formatDate(request.acceptedAt) }}
                  </div>
                  <div v-if="request.status === 'REJECTED'" class="text-sm text-red-600">
                    Rejected on {{ formatDate(request.rejectedAt) }}
                  </div>
                  <button
                    @click="viewRequestDetails(request)"
                    class="mt-2 text-sm text-blue-600 hover:text-blue-800"
                  >
                    View Details
                  </button>
                </div>
              </div>
  
              <!-- Books Preview -->
              <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                <div
                  v-for="book in request.books.slice(0, 3)"
                  :key="book.id"
                  class="flex items-center p-3 bg-gray-50 rounded-lg"
                >
                  <img
                    :src="book.imageUrl || '/placeholder.svg?height=60&width=40'"
                    :alt="book.title"
                    class="w-10 h-14 object-cover rounded mr-3"
                  />
                  <div class="min-w-0">
                    <div class="text-sm font-medium text-gray-900 truncate">{{ book.title }}</div>
                    <div class="text-xs text-gray-500 truncate">{{ book.authorName }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
  
          <!-- No Results -->
          <div v-else class="text-center py-12">
            <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            <h3 class="text-lg font-medium text-gray-900 mb-2">No borrowing requests found</h3>
            <p class="text-gray-600">You haven't made any borrowing requests yet</p>
          </div>
        </div>
      </div>
  
      <!-- Request Details Modal -->
      <div v-if="showDetailsModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeDetailsModal">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
          <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Request Details #{{ selectedRequest?.id }}
              </h3>
              
              <div v-if="selectedRequest" class="space-y-6">
                <!-- Status -->
                <div class="flex items-center">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mr-2',
                      getStatusClass(selectedRequest.status)
                    ]"
                  >
                    {{ selectedRequest.status }}
                  </span>
                </div>
  
                <!-- Timeline -->
                <div class="bg-gray-50 rounded-lg p-4">
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Timeline</h4>
                  <div class="space-y-3">
                    <div class="flex justify-between">
                      <span class="text-sm text-gray-500">Requested:</span>
                      <span class="text-sm font-medium">{{ formatDate(selectedRequest.requestedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.acceptedAt" class="flex justify-between">
                      <span class="text-sm text-gray-500">Accepted:</span>
                      <span class="text-sm font-medium text-green-600">{{ formatDate(selectedRequest.acceptedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.rejectedAt" class="flex justify-between">
                      <span class="text-sm text-gray-500">Rejected:</span>
                      <span class="text-sm font-medium text-red-600">{{ formatDate(selectedRequest.rejectedAt) }}</span>
                    </div>
                  </div>
                </div>
  
                <!-- Books -->
                <div>
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Requested Books</h4>
                  <div class="space-y-2">
                    <div
                      v-for="book in selectedRequest.books"
                      :key="book.id"
                      class="flex items-center p-3 bg-gray-50 rounded-lg"
                    >
                      <img
                        :src="book.imageUrl || '/placeholder.svg?height=60&width=40'"
                        :alt="book.title"
                        class="w-10 h-14 object-cover rounded mr-3"
                      />
                      <div class="flex-1">
                        <div class="font-medium text-gray-900">{{ book.title }}</div>
                        <div class="text-sm text-gray-500">by {{ book.authorName }}</div>
                        <div class="text-xs text-gray-400">ISBN: {{ book.isbn }}</div>
                      </div>
                      <div class="text-sm font-medium text-gray-900">
                        ${{ book.price?.toFixed(2) }}
                      </div>
                    </div>
                  </div>
                </div>
  
                <!-- Description -->
                <div v-if="selectedRequest.description">
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Description</h4>
                  <div class="bg-gray-50 rounded-lg p-4">
                    <p class="text-sm text-gray-700">{{ selectedRequest.description }}</p>
                  </div>
                </div>
  
                <!-- Librarian Notes -->
                <div v-if="selectedRequest.librarianNotes">
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Librarian Notes</h4>
                  <div class="bg-gray-50 rounded-lg p-4">
                    <p class="text-sm text-gray-700">{{ selectedRequest.librarianNotes }}</p>
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
  import { getMyAllBorrowingRequests } from '@/api'
  
  const router = useRouter()
  
  // State
  const borrowingRequests = ref([])
  const isLoading = ref(true)
  const searchQuery = ref('')
  const selectedStatus = ref('')
  const dateFilter = ref('')
  const showDetailsModal = ref(false)
  const selectedRequest = ref(null)
  const message = ref('')
  const errorMessage = ref('')
  
  // Computed
  const filteredRequests = computed(() => {
    let filtered = borrowingRequests.value
  
    // Filter by search query
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      filtered = filtered.filter(request =>
        request.books.some(book => book.title.toLowerCase().includes(query))
      )
    }
  
    // Filter by status
    if (selectedStatus.value) {
      filtered = filtered.filter(request => request.status === selectedStatus.value)
    }
  
    // Filter by date
    if (dateFilter.value) {
      const now = new Date()
      const requestDate = new Date()
      
      filtered = filtered.filter(request => {
        const reqDate = new Date(request.requestedAt)
        
        if (dateFilter.value === 'today') {
          return reqDate.toDateString() === now.toDateString()
        } else if (dateFilter.value === 'week') {
          const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
          return reqDate >= weekAgo
        } else if (dateFilter.value === 'month') {
          const monthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
          return reqDate >= monthAgo
        }
        return true
      })
    }
  
    return filtered.sort((a, b) => new Date(b.requestedAt) - new Date(a.requestedAt))
  })
  
  const pendingCount = computed(() => 
    borrowingRequests.value.filter(r => r.status === 'PENDING').length
  )
  
  const acceptedCount = computed(() => 
    borrowingRequests.value.filter(r => r.status === 'APPROVED').length
  )
  
  const rejectedCount = computed(() => 
    borrowingRequests.value.filter(r => r.status === 'REJECTED').length
  )
  
  // Methods
  const fetchBorrowingRequests = async () => {
    try {
      isLoading.value = true
      const response = await getMyAllBorrowingRequests()
      borrowingRequests.value = response.data.data
    } catch (error) {
      console.error('Error fetching borrowing requests:', error)
      errorMessage.value = 'Failed to load borrowing requests'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isLoading.value = false
    }
  }
  
  const getStatusClass = (status) => {
    const classes = {
      'PENDING': 'bg-yellow-100 text-yellow-800',
      'APPROVED': 'bg-green-100 text-green-800',
      'REJECTED': 'bg-red-100 text-red-800'
    }
    return classes[status] || 'bg-gray-100 text-gray-800'
  }
  
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
  
  const filterRequests = () => {
    // Filtering is handled by computed property
  }
  
  const viewRequestDetails = (request) => {
    selectedRequest.value = request
    showDetailsModal.value = true
  }
  
  const closeDetailsModal = () => {
    showDetailsModal.value = false
    selectedRequest.value = null
  }
  
  // Lifecycle
  onMounted(() => {
    fetchBorrowingRequests()
  })
  </script>
  