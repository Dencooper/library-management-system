<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Borrowing Requests</h1>
          <p class="text-gray-600 mt-2">Review and manage user borrowing requests</p>
        </div>
        <div class="flex items-center space-x-4">
          <!-- Status Summary -->
          <div class="flex items-center space-x-4 text-sm">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-yellow-400 rounded-full mr-2"></div>
              <span>{{ pendingCount }} Pending</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-green-400 rounded-full mr-2"></div>
              <span>{{ approvedCount }} Approved</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-blue-400 rounded-full mr-2"></div>
              <span>{{ borrowingCreatedCount }} Borrowing Created</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-red-400 rounded-full mr-2"></div>
              <span>{{ rejectedCount }} Rejected</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Filters -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-2">Search</label>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search by user name or book title..."
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
              <option value="APPROVED">Approved</option>
              <option value="BORROWING_CREATED">Borrowing Created</option>
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

      <!-- Requests Table -->
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

        <!-- Table -->
        <div v-else-if="filteredRequests.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Request Info
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Books
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Dates
                </th>
                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="request in filteredRequests"
                :key="request.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap w-[200px] max-w-[200px]">
                  <div class="text-sm font-medium text-gray-900">Request #{{ request.id }}</div>
                  <div class="text-sm text-gray-500 truncate">{{ request.description || 'No description' }}</div>
                </td>
                <td class="px-3 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ request.user.fullName }}</div>
                      <div class="text-sm text-gray-500">{{ request.user.email }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="space-y-1">
                    <div
                      v-for="book in request.books.slice(0, 2)"
                      :key="book.id"
                      class="text-sm text-gray-900"
                    >
                      {{ book.title }}
                    </div>
                    <div v-if="request.books.length > 2" class="text-sm text-gray-500">
                      +{{ request.books.length - 2 }} more books
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      getStatusClass(request.status)
                    ]"
                  >
                    {{ getStatusLabel(request.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div>
                    <div class="font-medium">Requested:</div>
                    <div class="text-gray-500">{{ formatDate(request.requestedAt) }}</div>
                    <div v-if="request.approvedAt" class="text-green-600 mt-1">
                      <div class="font-medium">Approved:</div>
                      <div>{{ formatDate(request.approvedAt) }}</div>
                    </div>
                    <div v-if="request.borrowingCreatedAt" class="text-blue-600 mt-1">
                      <div class="font-medium">Borrowing Created:</div>
                      <div>{{ formatDate(request.borrowingCreatedAt) }}</div>
                    </div>
                    <div v-if="request.rejectedAt" class="text-red-600 mt-1">
                      <div class="font-medium">Rejected:</div>
                      <div>{{ formatDate(request.rejectedAt) }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      v-if="request.status === 'PENDING'"
                      @click="approveRequest(request)"
                      class="text-green-600 hover:text-green-900"
                    >
                    Approve
                    </button>
                    <button
                      v-if="request.status === 'PENDING'"
                      @click="rejectRequest(request)"
                      class="text-red-600 hover:text-red-900"
                    >
                      Reject
                    </button>
                    <button
                      v-if="request.status === 'APPROVED'"
                      @click="createBorrowing(request)"
                      class="text-purple-600 hover:text-purple-900 font-medium"
                    >
                      Create
                    </button>
                    <button
                      v-if="request.status === 'BORROWING_CREATED'"
                      @click="viewBorrowing(request.user.email)"
                      class="text-indigo-600 hover:text-indigo-900"
                    >
                      Borrowing
                    </button>
                    <button
                      @click="viewRequestDetails(request)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      View
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- No Results -->
        <div v-else class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No borrowing requests found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Create Borrowing Modal -->
    <div v-if="showCreateBorrowingModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeCreateBorrowingModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <form @submit.prevent="confirmCreateBorrowing">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Create Borrowing - Request #{{ selectedRequestForBorrowing?.id }}
              </h3>
              
              <div v-if="selectedRequestForBorrowing" class="space-y-6">
                <!-- User Info -->
                <div>
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Borrower Information</h4>
                  <div class="bg-gray-50 rounded-lg p-4">
                    <div class="grid grid-cols-2 gap-4">
                      <div>
                        <span class="text-sm text-gray-500">Name:</span>
                        <div class="font-medium">{{ selectedRequestForBorrowing.user.fullName }}</div>
                      </div>
                      <div>
                        <span class="text-sm text-gray-500">Email:</span>
                        <div class="font-medium">{{ selectedRequestForBorrowing.user.email }}</div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Books Selection -->
                <div>
                  <h4 class="text-sm font-medium text-gray-900 mb-2">Select Book Items to Borrow</h4>
                  <div class="space-y-3">
                    <div
                      v-for="book in selectedRequestForBorrowing.books"
                      :key="book.id"
                      class="border border-gray-200 rounded-lg p-4"
                    >
                      <div class="flex items-center justify-between mb-3">
                        <div class="flex items-center">
                          <img
                            :src="book.imageUrl || '/placeholder.svg?height=60&width=40'"
                            :alt="book.title"
                            class="w-10 h-14 object-cover rounded mr-3"
                          />
                          <div>
                            <div class="font-medium text-gray-900">{{ book.title }}</div>
                            <div class="text-sm text-gray-500">by {{ book.author.name }}</div>
                            <div class="text-xs text-gray-400">ISBN: {{ book.isbn }}</div>
                          </div>
                        </div>
                      </div>
                      
                      <!-- Available Items -->
                      <div>
                        <select
                          v-model="borrowingForm.bookItemIds[book.id]"
                          required
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value='' disabled>Select an item</option>
                          <option 
                            v-for="item in getAvailableItemsForBook(book.id)" 
                            :key="item.id" 
                            :value="item.id"
                          >
                            {{ item.code }}
                          </option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="isCreatingBorrowing || !canCreateBorrowing"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-purple-600 text-base font-medium text-white hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ isCreatingBorrowing ? 'Creating...' : 'Create Borrowing' }}
              </button>
              <button
                @click="closeCreateBorrowingModal"
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
              <!-- User Info -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">User Information</h4>
                <div class="bg-gray-50 rounded-lg p-4">
                  <div class="grid grid-cols-2 gap-4">
                    <div>
                      <span class="text-sm text-gray-500">Name:</span>
                      <div class="font-medium">{{ selectedRequest.user.fullName }}</div>
                    </div>
                    <div>
                      <span class="text-sm text-gray-500">Email:</span>
                      <div class="font-medium">{{ selectedRequest.user.email }}</div>
                    </div>
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
                      <div class="text-sm text-gray-500">by {{ book.author.name }}</div>
                      <div class="text-xs text-gray-400">ISBN: {{ book.isbn }}</div>
                    </div>
                    <div class="flex flex-col items-end ml-4 gap-6">
                      <div class="text-sm font-medium text-gray-900">
                        ${{ book.price?.toFixed(2) }}
                      </div>
                      <span
                        :class="[
                          'inline-flex items-center px-2.5 py-0.5 rounded-full text-sm font-medium',
                          book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        ]"
                      >
                        {{ book.isAvailable ? 'Available' : 'Not Available' }}
                      </span>
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

              <!-- Status & Dates -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Status & Timeline</h4>
                <div class="bg-gray-50 rounded-lg p-4">
                  <div class="space-y-2">
                    <div class="flex justify-between">
                      <span class="text-sm text-gray-500">Status:</span>
                      <span
                        :class="[
                          'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                          getStatusClass(selectedRequest.status)
                        ]"
                      >
                        {{ getStatusLabel(selectedRequest.status) }}
                      </span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm text-gray-500">Requested:</span>
                      <span class="text-sm font-medium">{{ formatDate(selectedRequest.requestedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.approvedAt" class="flex justify-between">
                      <span class="text-sm text-gray-500">Approved:</span>
                      <span class="text-sm font-medium text-green-600">{{ formatDate(selectedRequest.approvedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.borrowingCreatedAt" class="flex justify-between">
                      <span class="text-sm text-gray-500">Borrowing Created:</span>
                      <span class="text-sm font-medium text-blue-600">{{ formatDate(selectedRequest.borrowingCreatedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.rejectedAt" class="flex justify-between">
                      <span class="text-sm text-gray-500">Rejected:</span>
                      <span class="text-sm font-medium text-red-600">{{ formatDate(selectedRequest.rejectedAt) }}</span>
                    </div>
                    <div v-if="selectedRequest.borrowingId" class="flex justify-between">
                      <span class="text-sm text-gray-500">Borrowing ID:</span>
                      <span class="text-sm font-medium text-blue-600">#{{ selectedRequest.borrowingId }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button
              v-if="selectedRequest?.status === 'PENDING'"
              @click="approvedRequest(selectedRequest)"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Approve
            </button>
            <button
              v-if="selectedRequest?.status === 'PENDING'"
              @click="rejectRequest(selectedRequest)"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Reject
            </button>
            <button
              v-if="selectedRequest?.status === 'APPROVED'"
              @click="createBorrowing(selectedRequest)"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-purple-600 text-base font-medium text-white hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Create
            </button>
            <button
              @click="closeDetailsModal"
              type="button"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Close
            </button>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllBorrowingRequests, updateBorrowingRequest, getAllBookItems, createBorrowing as createNewBorrowing } from '@/api'
import Swal from 'sweetalert2'
import { useAuthStore } from '@/stores/auth'  

const router = useRouter()
const authStore = useAuthStore()

// State
const borrowingRequests = ref([])
const availableBookItems = ref([])
const isLoading = ref(true)
const isCreatingBorrowing = ref(false)
const searchQuery = ref('')
const selectedStatus = ref('')
const dateFilter = ref('')
const showDetailsModal = ref(false)
const showCreateBorrowingModal = ref(false)
const selectedRequest = ref(null)
const selectedRequestForBorrowing = ref(null)
const message = ref('')
const errorMessage = ref('')

const borrowingForm = reactive({
  bookItemIds: {}
})

// Computed
const filteredRequests = computed(() => {
  let filtered = borrowingRequests.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(request =>
      request.user.fullName.toLowerCase().includes(query) ||
      request.user.email.toLowerCase().includes(query) ||
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

const approvedCount = computed(() => 
  borrowingRequests.value.filter(r => r.status === 'APPROVED').length
)

const borrowingCreatedCount = computed(() => 
  borrowingRequests.value.filter(r => r.status === 'BORROWING_CREATED').length
)

const rejectedCount = computed(() => 
  borrowingRequests.value.filter(r => r.status === 'REJECTED').length
)

const canCreateBorrowing = computed(() => {
  return selectedRequestForBorrowing.value && 
         borrowingForm.bookItemIds[selectedRequestForBorrowing.value.books[0].id]
})

// Methods
const fetchBorrowingRequests = async () => {
  try {
    isLoading.value = true
    const response = await getAllBorrowingRequests()
    borrowingRequests.value = response.data.data
  } catch (error) {
    console.error('Error fetching borrowing requests:', error)
    errorMessage.value = 'Failed to load borrowing requests'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  }
}

const fetchAvailableBookItems = async () => {
  try {
    isLoading.value = true
    const response = await getAllBookItems(true)
    availableBookItems.value = response.data.data
  } catch (error) {
    console.error('Error fetching book items:', error)
    errorMessage.value = 'Failed to load book items'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  } 
}

const getAvailableItemsForBook = (bookId) => {
  return availableBookItems.value.filter(item => 
    item.bookId === bookId
  )
}

const getStatusClass = (status) => {
  const classes = {
    'PENDING': 'bg-yellow-100 text-yellow-800',
    'APPROVED': 'bg-green-100 text-green-800',
    'BORROWING_CREATED': 'bg-blue-100 text-blue-800',
    'REJECTED': 'bg-red-100 text-red-800'
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

const getStatusLabel = (status) => {
  const labels = {
    'PENDING': 'PENDING',
    'APPROVED': 'APPROVED',
    'BORROWING_CREATED': 'BORROWING_CREATED',
    'REJECTED': 'REJECTED'
  }
  return labels[status] || status
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const viewRequestDetails = (request) => {
  selectedRequest.value = request
  showDetailsModal.value = true
}

const approveRequest = async (request) => {
    try {
      const response = await updateBorrowingRequest(request.id, {
        ...request,
        status: 'APPROVED',
        librarianId: authStore.user.id,
      })
      const updatedRequest = response.data.data
      const index = borrowingRequests.value.findIndex(r => r.id === request.id)
      if (index !== -1) {
        borrowingRequests.value[index] = {
          ...borrowingRequests.value[index],
          ...updatedRequest
        }
      }
      message.value = 'Request approved successfully'
      setTimeout(() => message.value = '', 3000)
      closeDetailsModal()
      
    } catch (error) {
      console.error('Error approving request:', error)
      errorMessage.value = 'Failed to approve request'
      setTimeout(() => errorMessage.value = '', 3000)
    }
  }
  
  const rejectRequest = async (request) => {
    Swal.fire({
      title: 'Reject Reason',
      input: 'text',
      inputLabel: 'Please provide a reason for rejection (optional):',
      inputPlaceholder: 'Enter reason here...',
      showCancelButton: true,
      confirmButtonText: 'Submit',
      cancelButtonText: 'Cancel',
    }).then(async (result) => {
      if (result.isConfirmed) {
        const reason = result.value; 
        try {
        const response = await updateBorrowingRequest(request.id, {
          ...request,
          status: 'REJECTED',
          description: reason || request.description,
          })
        const updatedRequest = response.data.data
        const index = borrowingRequests.value.findIndex(r => r.id === request.id)
        if (index !== -1) {
          borrowingRequests.value[index] = {
            ...borrowingRequests.value[index],
            ...updatedRequest
          }
        }
        
        message.value = 'Request rejected successfully'
        setTimeout(() => message.value = '', 3000)
        closeDetailsModal()
    } catch (error) {
      console.error('Error rejecting request:', error)
      errorMessage.value = 'Failed to reject request'
      setTimeout(() => errorMessage.value = '', 3000)
    }
      } else {
        console.log('User cancelled the prompt');
      }
    });
  }

const createBorrowing = async (request) => {
  selectedRequestForBorrowing.value = request
  
  if (availableBookItems.value.length === 0) {
    await fetchAvailableBookItems()
  }
  
  showCreateBorrowingModal.value = true
  closeDetailsModal()
}

const confirmCreateBorrowing = async () => {
  try {
    isCreatingBorrowing.value = true
    
    const bookItemIds = selectedRequestForBorrowing.value.books.map(book => {
      return borrowingForm.bookItemIds[book.id]
    })

    const borrowingData = {
      borrowingRequestId: selectedRequestForBorrowing.value.id,
      bookItemIds
    }

    console.log(borrowingData)

    const response = await createNewBorrowing(borrowingData)
    const borrowing = response.data.data

    const index = borrowingRequests.value.findIndex(r => r.id === selectedRequestForBorrowing.value.id)
    if (index !== -1) {
      borrowingRequests.value[index] = {
        ...borrowingRequests.value[index],
        ...borrowing
      }
    }

    bookItemIds.forEach(itemId => {
      const itemIndex = availableBookItems.value.findIndex(item => item.id === itemId)
      if (itemIndex !== -1) {
        availableBookItems.value[itemIndex].available = false
      }
    })

    message.value = 'Borrowing created successfully'
    setTimeout(() => message.value = '', 3000)
    closeCreateBorrowingModal()

  } catch (error) {
    console.error('Error creating borrowing:', error)
    errorMessage.value = 'Failed to create borrowing'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isCreatingBorrowing.value = false
  }
}

const viewBorrowing = (email) => {
  router.push({ name: 'librarian-borrowings', query: { email } })
}

const closeDetailsModal = () => {
  showDetailsModal.value = false
  selectedRequest.value = null
}

const closeCreateBorrowingModal = () => {
  showCreateBorrowingModal.value = false
  selectedRequestForBorrowing.value = null
  borrowingForm.bookItemIds = {}
}


// Lifecycle
onMounted(() => {
  fetchBorrowingRequests()
})
</script>
