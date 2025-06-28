<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">My Borrowings</h1>
          <p class="text-gray-600 mt-2">Track your borrowed books and return dates</p>
        </div>
      </div>

      <!-- Status Summary -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="rounded-full bg-red-100 p-3 mr-4">
            <svg class="w-6 h-6   text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-gray-600">Overdue</p>
            <p class="text-2xl font-bold text-gray-900">{{ overdueCount }}</p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="rounded-full bg-green-100 p-3 mr-4">
            <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-gray-600">Returned</p>
            <p class="text-2xl font-bold text-gray-900">{{ returnedCount }}</p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="rounded-full bg-yellow-100 p-3 mr-4">
            <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div>
            <p class="text-sm text-gray-600">Late</p>
            <p class="text-2xl font-bold text-gray-900">{{ lateCount }}</p>
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
              @input="filterBorrowings"
            />
          </div>

          <!-- Status Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
            <select
              v-model="selectedStatus"
              @change="filterBorrowings"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Status</option>
              <option value="overdue">Overdue</option>
              <option value="returned">Returned</option>
              <option value="late">Late</option>
            </select>
          </div>

          <!-- Sort By -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Sort By</label>
            <select
              v-model="sortBy"
              @change="filterBorrowings"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="borrowedAt">Borrow Date</option>
              <option value="returnedAt">Return Date</option>
              <option value="dueDate">Due Date</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Borrowings List -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">
            Borrowings ({{ filteredBorrowings.length }})
          </h2>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Borrowings List -->
        <div v-else-if="filteredBorrowings.length > 0" class="divide-y divide-gray-200">
          <div
            v-for="borrowing in filteredBorrowings"
            :key="borrowing.id"
            class="p-6 hover:bg-gray-50"
          >
            <div class="flex flex-col md:flex-row md:items-center md:justify-between">
              <!-- Borrowing Info -->
              <div class="mb-4 md:mb-0">
                <div class="flex items-center">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mr-2',
                      getBorrowingStatusClass(borrowing)
                    ]"
                  >
                    {{ getBorrowingStatus(borrowing) }}
                  </span>
                  <h3 class="text-lg font-medium text-gray-900">Borrowing #{{ borrowing.id }}</h3>
                </div>
                <p class="text-sm text-gray-500 mt-1">
                  Borrowed on {{ formatDate(borrowing.borrowedAt) }}
                </p>
              </div>

              <!-- Due Date Info -->
              <div class="flex flex-col items-start md:items-end">
                <div :class="getDueDateClass(borrowing)">
                  Due: {{ formatDate(getDueDate(borrowing.borrowedAt)) }}
                </div>
                <div v-if="borrowing.returnedAt" class="text-sm text-green-600">
                  Returned on {{ formatDate(borrowing.returnedAt) }}
                </div>
                <button
                  @click="viewBorrowingDetails(borrowing)"
                  class="mt-2 text-sm text-blue-600 hover:text-blue-800"
                >
                  View Details
                </button>
              </div>
            </div>

            <!-- Books Preview -->
            <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
              <div
                v-for="item in borrowing.items.slice(0, 3)"
                :key="item.id"
                class="flex items-center p-3 bg-gray-50 rounded-lg"
              >
                <div class="min-w-0">
                  <div class="text-sm font-medium text-gray-900 truncate">{{ item.bookTitle }}</div>
                  <div class="text-xs text-gray-500 truncate">Code: {{ item.code }}</div>
                  <div class="text-xs text-gray-500">
                    Condition: 
                    <span :class="getConditionClass(item.bookItemCondition)">
                      {{ item.bookItemCondition }}
                    </span>
                  </div>
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
          <h3 class="text-lg font-medium text-gray-900 mb-2">No borrowings found</h3>
          <p class="text-gray-600">You haven't borrowed any books yet</p>
        </div>
      </div>
    </div>

    <!-- Borrowing Details Modal -->
    <div v-if="showDetailsModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeDetailsModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              Borrowing Details #{{ selectedBorrowing?.id }}
            </h3>
            
            <div v-if="selectedBorrowing" class="space-y-6">
              <!-- Status -->
              <div class="flex items-center">
                <span
                  :class="[
                    'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mr-2',
                    getBorrowingStatusClass(selectedBorrowing)
                  ]"
                >
                  {{ getBorrowingStatus(selectedBorrowing) }}
                </span>
              </div>

              <!-- Timeline -->
              <div class="bg-gray-50 rounded-lg p-4">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Timeline</h4>
                <div class="space-y-3">
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Borrowed:</span>
                    <span class="text-sm font-medium">{{ formatDate(selectedBorrowing.borrowedAt) }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Due Date:</span>
                    <span class="text-sm font-medium" :class="getDueDateClass(selectedBorrowing)">
                      {{ formatDate(getDueDate(selectedBorrowing.borrowedAt)) }}
                    </span>
                  </div>
                  <div v-if="selectedBorrowing.returnedAt" class="flex justify-between">
                    <span class="text-sm text-gray-500">Returned:</span>
                    <span class="text-sm font-medium text-green-600">{{ formatDate(selectedBorrowing.returnedAt) }}</span>
                  </div>
                </div>
              </div>

              <!-- Books -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Borrowed Items</h4>
                <div class="space-y-2">
                  <div
                    v-for="item in selectedBorrowing.items"
                    :key="item.id"
                    class="flex items-center p-3 bg-gray-50 rounded-lg"
                  >
                    <div class="flex-1">
                      <div class="font-medium text-gray-900">{{ item.bookTitle }}</div>
                      <div class="text-sm text-gray-500">Code: {{ item.code }}</div>
                      <div class="text-sm text-gray-500">
                        Condition: 
                        <span :class="getConditionClass(item.bookItemCondition)">
                          {{ item.bookItemCondition }}
                        </span>
                      </div>
                    </div>
                    <div class="text-sm font-medium text-gray-900">
                      ${{ item.price?.toFixed(2) }}
                    </div>
                  </div>
                </div>
              </div>

              <!-- Penalty Info -->
              <div v-if="selectedBorrowing.penaltyDescription">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Penalty Information</h4>
                <div class="bg-red-50 rounded-lg p-4">
                  <p class="text-sm text-red-700">{{ selectedBorrowing.penaltyDescription }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
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
import { getMyAllBorrowings } from '@/api'

// State
const borrowings = ref([])
const isLoading = ref(true)
const searchQuery = ref('')
const selectedStatus = ref('')
const sortBy = ref('borrowedAt')
const showDetailsModal = ref(false)
const selectedBorrowing = ref(null)
const message = ref('')
const errorMessage = ref('')

// Computed
const filteredBorrowings = computed(() => {
  let filtered = borrowings.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(borrowing =>
      borrowing.items.some(item => item.bookTitle.toLowerCase().includes(query))
    )
  }

  // Filter by status
  if (selectedStatus.value) {
    if (selectedStatus.value === 'active') {
      filtered = filtered.filter(borrowing => !borrowing.returnedAt && !borrowing.isLate)
    } else if (selectedStatus.value === 'overdue') {
      filtered = filtered.filter(borrowing => !borrowing.returnedAt && borrowing.isLate)
    } else if (selectedStatus.value === 'returned') {
      filtered = filtered.filter(borrowing => borrowing.returnedAt)
    }
  }

  // Sort
  filtered.sort((a, b) => {
    if (sortBy.value === 'borrowedAt') {
      return new Date(b.borrowedAt) - new Date(a.borrowedAt)
    } else if (sortBy.value === 'dueDate') {
      const dueDateA = getDueDate(a.borrowedAt)
      const dueDateB = getDueDate(b.borrowedAt)
      return new Date(dueDateA) - new Date(dueDateB)
    } else if (sortBy.value === 'returnedAt') {
      if (!a.returnedAt && !b.returnedAt) return 0
      if (!a.returnedAt) return 1
      if (!b.returnedAt) return -1
      return new Date(b.returnedAt) - new Date(a.returnedAt)
    }
    return 0
  })

  return filtered
})

const lateCount = computed(() => 
  borrowings.value.filter(b => b.returnedAt && b.isLate).length
)

const overdueCount = computed(() => 
  borrowings.value.filter(b => !b.returnedAt && b.isLate).length
)

const returnedCount = computed(() => 
  borrowings.value.filter(b => b.returnedAt).length
)

// Methods
const fetchBorrowings = async () => {
  try {
    isLoading.value = true
    const response = await getMyAllBorrowings()
    borrowings.value = response.data.data
    
  } catch (error) {
    console.error('Error fetching borrowings:', error)
    errorMessage.value = 'Failed to load borrowings'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  }
}

const getDueDate = (borrowedAt) => {
  const borrowDate = new Date(borrowedAt)
  const dueDate = new Date(borrowDate)
  dueDate.setDate(dueDate.getDate() + 30) // 30 days loan period
  return dueDate.toISOString()
}

const getDueDateClass = (borrowing) => {
  if (borrowing.returnedAt) return 'text-gray-500'
  
  const dueDate = new Date(getDueDate(borrowing.borrowedAt))
  const now = new Date()
  const daysDiff = Math.ceil((dueDate - now) / (1000 * 60 * 60 * 24))
  
  if (daysDiff < 0) return 'text-red-600' // Overdue
  if (daysDiff <= 3) return 'text-yellow-600' // Due soon
  return 'text-gray-900' // Normal
}

const getBorrowingStatus = (borrowing) => {
  if (borrowing.returnedAt) {
    if(borrowing.isLate) {
      return 'Late'
    } 
    return 'Returned'
  } 
  if (borrowing.isLate) return 'Overdue'
  return 'Active'
}

const getBorrowingStatusClass = (borrowing) => {
  if (borrowing.returnedAt) {
    if (borrowing.isLate) return 'bg-red-100 text-red-800'
    return 'bg-green-100 text-green-800'  
  }
  if (borrowing.isLate) return 'bg-red-100 text-red-800'
  return 'bg-blue-100 text-blue-800'
}

const getConditionClass = (condition) => {
  const classes = {
    'GOOD': 'text-green-600',
    'MINOR_DAMAGE': 'text-yellow-600',
    'MAJOR_DAMAGE': 'text-red-600',
    'LOST': 'text-red-800'
  }
  return classes[condition] || 'text-gray-600'
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

const filterBorrowings = () => {
  // Filtering is handled by computed property
}

const viewBorrowingDetails = (borrowing) => {
  selectedBorrowing.value = borrowing
  showDetailsModal.value = true
}

const closeDetailsModal = () => {
  showDetailsModal.value = false
  selectedBorrowing.value = null
}

// Lifecycle
onMounted(() => {
  fetchBorrowings()
})
</script>
