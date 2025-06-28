<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">My Penalties</h1>
          <p class="text-gray-600 mt-2">View my penalties detail</p>
        </div>
      </div>

      <!-- Penalties Table -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Table -->
        <div v-else-if="penalties.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Borrowing
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Amount
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Description
                </th>
                <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
                <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="penalty in penalties"
                :key="penalty.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div>
                      <div class="text-sm font-medium text-gray-900">{{ penalty.user.fullName }}</div>
                      <div class="text-sm text-gray-500">{{ penalty.user.email }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-blue-600">
                    Borrowing #{{ penalty.borrowingId }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-lg font-bold text-red-600">
                    ${{ penalty.amount.toFixed(2) }}
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm text-gray-900 max-w-xs truncate">
                    {{ penalty.description }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatDate(penalty.penaltyAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-center">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      penalty.paid ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ penalty.paid ? 'PAID' : 'UNPAID' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      @click="viewPenaltyDetails(penalty)"
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No penalties found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Penalty Details Modal -->
    <div v-if="showDetailsModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeDetailsModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              Penalty Details #{{ selectedPenalty?.id }}
            </h3>
            
            <div v-if="selectedPenalty" class="space-y-4">
              <!-- User Info -->
              <div class="bg-gray-50 rounded-lg p-4">
                <h4 class="text-sm font-medium text-gray-900 mb-2">User Information</h4>
                <div class="space-y-1">
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Name:</span>
                    <span class="text-sm font-medium">{{ selectedPenalty.user.fullName }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Email:</span>
                    <span class="text-sm font-medium">{{ selectedPenalty.user.email }}</span>
                  </div>
                </div>
              </div>

              <!-- Borrowing Info -->
              <div class="bg-blue-50 rounded-lg p-4">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Related Borrowing</h4>
                <div class="space-y-2">
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Borrowing ID:</span>
                    <span class="text-sm font-medium text-blue-600">#{{ selectedPenalty.borrowingd }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Borrowed Date:</span>
                    <span class="text-sm font-medium">{{ formatDate(selectedPenalty.borrowing.borrowedAt) }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Returned Date:</span>
                    <span class="text-sm font-medium">{{ formatDate(selectedPenalty.borrowing.returnedAt) }}</span>
                  </div>
                  <div class="mt-3">
                    <span class="text-sm text-gray-500">Books:</span>
                    <div class="mt-1 space-y-1">
                      <div v-for="item in selectedPenalty.borrowing.items" :key="item.id" class="text-sm">
                        • Title: {{ item.bookTitle }} - Conditions: {{ item.bookItemCondition }} - Price: ${{ item.price.toFixed(2) }}
                        
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Penalty Info -->
              <div class="bg-red-50 rounded-lg p-4">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Penalty Information</h4>
                <div class="space-y-1">
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Amount:</span>
                    <span class="text-lg font-bold text-red-600">${{ selectedPenalty.amount.toFixed(2) }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Date:</span>
                    <span class="text-sm font-medium">{{ formatDate(selectedPenalty.penaltyAt) }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-sm text-gray-500">Status:</span>
                    <span
                      :class="[
                        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                        selectedPenalty.isPaid ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      ]"
                    >
                      {{ selectedPenalty.isPaid ? 'Paid' : 'Unpaid' }}
                    </span>
                  </div>
                </div>
              </div>

              <!-- Description -->
              <div class="bg-gray-50 rounded-lg p-4">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Description</h4>
                <p class="text-sm text-gray-700">{{ selectedPenalty.description }}</p>
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
import { getMyAllPenalties} from '@/api'

// State
const penalties = ref([])
const isLoading = ref(true)
const showDetailsModal = ref(false)
const selectedPenalty = ref(null)
const message = ref('')
const errorMessage = ref('')

// Methods
const fetchPenalties = async () => {
  try {
    isLoading.value = true
    const response = await getMyAllPenalties()
    penalties.value = response.data.data
  } catch (error) {
    console.error('Error fetching penalties:', error)
    errorMessage.value = 'Failed to load penalties'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  }
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


const viewPenaltyDetails = async (penalty) => {
  const response = await getBorrowingById(penalty.borrowingId)
  const borrowing = response.data.data
  selectedPenalty.value = {
    ...penalty,
    borrowing: borrowing,
  }
  showDetailsModal.value = true
}

const closeDetailsModal = () => {
  showDetailsModal.value = false
  selectedPenalty.value = null
}

// Lifecycle
onMounted(() => {
  fetchPenalties()
})
</script>
