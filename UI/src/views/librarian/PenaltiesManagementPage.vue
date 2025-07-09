<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Penalties Management</h1>
          <p class="text-gray-600 mt-2">View and manage late return penalties</p>
        </div>
        <div class="flex items-center space-x-4">
          <!-- Summary Stats -->
          <div class="flex items-center space-x-4 text-sm">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-red-400 rounded-full mr-2"></div>
              <span>{{ unpaidCount }} Unpaid</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-green-400 rounded-full mr-2"></div>
              <span>{{ paidCount }} Paid</span>
            </div>
            <div class="flex items-center">
              <span class="text-gray-600">Total: ${{ totalAmount.toFixed(2) }}</span>
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
              placeholder="Search by user name, borrowing ID, or description..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              @input="filterPenalties"
            />
          </div>

          <!-- Status Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Payment Status</label>
            <select
              v-model="selectedStatus"
              @change="filterPenalties"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Status</option>
              <option value="unpaid">Unpaid</option>
              <option value="paid">Paid</option>
            </select>
          </div>

          <!-- Amount Range -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Amount Range</label>
            <select
              v-model="amountRange"
              @change="filterPenalties"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Amounts</option>
              <option value="0-10">$0 - $10</option>
              <option value="10-25">$10 - $25</option>
              <option value="25-50">$25 - $50</option>
              <option value="50+">$50+</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Penalties Table -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">
            Penalties ({{ filteredPenalties.length }})
          </h2>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Table -->
        <div v-else-if="filteredPenalties.length > 0" class="overflow-x-auto">
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
                v-for="penalty in filteredPenalties"
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
                  <div class="text-sm font-medium text-gray-500">
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
                      @click="editPenalty(penalty)"
                      class="text-green-600 hover:text-green-800"
                    >
                      Edit
                    </button>
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

    <!-- Edit Penalty Modal -->
    <div v-if="showEditModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <form @submit.prevent="savePenalty">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Edit Penalty #{{ penaltyForm.id }}
              </h3>
              
              <div class="space-y-4">
                <!-- Read-only Info -->
                <div class="bg-gray-50 rounded-lg p-4">
                  <div class="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <span class="text-gray-500">User:</span>
                      <div class="font-medium">{{ selectedPenalty?.user.fullName }}</div>
                    </div>
                    <div>
                      <span class="text-gray-500">Borrowing:</span>
                      <div class="font-medium">#{{ selectedPenalty?.borrowingId }}</div>
                    </div>
                    <div>
                      <span class="text-gray-500">Amount:</span>
                      <div class="font-bold text-red-600">${{ selectedPenalty?.amount.toFixed(2) }}</div>
                    </div>
                    <div>
                      <span class="text-gray-500">Date:</span>
                      <div class="font-medium">{{ formatDate(selectedPenalty?.penaltyAt) }}</div>
                    </div>
                  </div>
                </div>

                <!-- Editable Fields -->
                <!-- Description -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                  <textarea
                    v-model="penaltyForm.description"
                    rows="3"
                    required
                    placeholder="Penalty description..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  ></textarea>
                </div>
                <!-- Payment Status -->
                <div>
                  <div>
                    <label class="flex items-center">
                      <input
                        v-model="penaltyForm.isPaid"
                        type="checkbox"
                        class="w-5 h-5 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                      />
                      <span class="ml-3 text-base text-gray-700">Mark as paid</span>
                    </label>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="isSaving"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50"
              >
                {{ isSaving ? 'Saving...' : 'Update' }}
              </button>
              <button
                @click="closeModal"
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
                    <span class="text-sm font-medium text-blue-600">#{{ selectedPenalty.borrowingId }}</span>
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
                      <div
                        v-for="item in selectedPenalty.borrowing.items"
                        :key="item.id"
                        class="grid grid-cols-8 gap-4 text-sm py-1 border-b border-gray-200"
                      >
                        <div class="col-span-3 font-medium">{{ item.bookTitle }}</div>
                        <div class="col-span-3"><span class="font-medium">Condition:</span> {{ item.bookItemCondition }}</div>
                        <div class="col-span-2 text-end"><span class="font-medium">Price:</span> ${{ item.price.toFixed(2) }}</div>
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
              @click="editPenalty(selectedPenalty)"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Edit
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
import { getAllPenalties, getBorrowingById, updatePenalty } from '@/api'

// State
const penalties = ref([])
const isLoading = ref(true)
const isSaving = ref(false)
const showEditModal = ref(false)
const showDetailsModal = ref(false)
const selectedPenalty = ref(null)
const searchQuery = ref('')
const selectedStatus = ref('')
const amountRange = ref('')
const message = ref('')
const errorMessage = ref('')

const penaltyForm = reactive({
  id: null,
  description: '',
  isPaid: false
})

// Computed
const filteredPenalties = computed(() => {
  let filtered = penalties.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(penalty =>
      penalty.user.fullName.toLowerCase().includes(query) ||
      penalty.user.email.toLowerCase().includes(query) ||
      penalty.description?.toLowerCase().includes(query) ||
      penalty.borrowingId.toString().includes(query)
    )
  }

  // Filter by status
  if (selectedStatus.value) {
    if (selectedStatus.value === 'paid') {
      filtered = filtered.filter(penalty => penalty.paid)
    } else if (selectedStatus.value === 'unpaid') {
      filtered = filtered.filter(penalty => !penalty.paid)
    }
  }

  // Filter by amount range
  if (amountRange.value) {
    filtered = filtered.filter(penalty => {
      const amount = penalty.amount
      if (amountRange.value === '0-10') return amount >= 0 && amount <= 10
      if (amountRange.value === '10-25') return amount > 10 && amount <= 25
      if (amountRange.value === '25-50') return amount > 25 && amount <= 50
      if (amountRange.value === '50+') return amount > 50
      return true
    })
  }

  return filtered.sort((a, b) => new Date(b.penaltyAt) - new Date(a.penaltyAt))
})

const unpaidCount = computed(() => 
  penalties.value.filter(p => !p.paid).length
)

const paidCount = computed(() => 
  penalties.value.filter(p => p.paid).length
)

const totalAmount = computed(() => 
  penalties.value.reduce((sum, p) => sum + p.amount, 0)
)

// Methods
const fetchPenalties = async () => {
  try {
    isLoading.value = true
    const response = await getAllPenalties()
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

const editPenalty = (penalty) => {
  selectedPenalty.value = penalty
  penaltyForm.id = penalty.id
  penaltyForm.description = penalty.description
  penaltyForm.isPaid = penalty.paid
  
  showDetailsModal.value = false
  showEditModal.value = true
}

const savePenalty = async () => {
  try {
    isSaving.value = true
    
    const response = await updatePenalty(penaltyForm.id, {
      description: penaltyForm.description,
      isPaid: penaltyForm.isPaid
    })
    const updatedPenalty = response.data.data
    
    const index = penalties.value.findIndex(p => p.id === penaltyForm.id)
    if (index !== -1) {
      penalties.value[index] = {
        ...penalties.value[index],
        ...updatedPenalty,
      }
    }
    
    message.value = 'Penalty updated successfully'
    closeModal()
    setTimeout(() => message.value = '', 3000)
    
  } catch (error) {
    console.error('Error saving penalty:', error)
    errorMessage.value = 'Failed to save penalty'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isSaving.value = false
  }
}

const closeModal = () => {
  showEditModal.value = false
  selectedPenalty.value = null
  
  // Reset form
  penaltyForm.id = null
  penaltyForm.description = ''
  penaltyForm.isPaid = false
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
