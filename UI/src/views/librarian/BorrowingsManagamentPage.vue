<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Active Borrowings</h1>
          <p class="text-gray-600 mt-2">Track and manage book borrowings and returns</p>
        </div>
        <div class="flex items-center space-x-4">
          <!-- Status Summary -->
          <div class="flex items-center space-x-4 text-sm">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-blue-400 rounded-full mr-2"></div>
              <span>{{ activeCount }} Active</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-red-400 rounded-full mr-2"></div>
              <span>{{ overdueCount }} Overdue</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-green-400 rounded-full mr-2"></div>
              <span>{{ returnedCount }} Returned</span>
            </div>
            <div class="flex items-center">
              <div class="w-3 h-3 bg-red-600 rounded-full mr-2"></div>
              <span>{{ lateCount }} Late</span>
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
              <option value="active">Active</option>
              <option value="overdue">Overdue</option>
              <option value="returned">Returned</option>
              <option value="late">Late</option>
            </select>
          </div>

          <!-- Sort -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Sort By</label>
            <select
              v-model="sortBy"
              @change="sortBorrowings"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="borrowedAt">Borrow Date</option>
              <option value="dueDate">Due Date</option>
              <option value="userName">User Name</option>   
            </select>
          </div>
        </div>
      </div>

      <!-- Borrowings Table -->
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

        <!-- Table -->
        <div v-else-if="filteredBorrowings.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Borrowing Info
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Books
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Dates
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="borrowing in filteredBorrowings"
                :key="borrowing.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">Borrowing #{{ borrowing.id }}</div>
                  <div class="text-sm text-gray-500">{{ borrowing.items.length }} item(s)</div>
                  <div v-if="borrowing.penaltyDescription" class="text-xs text-red-600 mt-1">
                    {{ borrowing.penaltyDescription }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ borrowing.user.fullName }}</div>
                      <div class="text-sm text-gray-500">{{ borrowing.user.email }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="space-y-1">
                    <div
                      v-for="item in borrowing.items.slice(0, 2)"
                      :key="item.id"
                      class="text-sm text-gray-900"
                    >
                      {{ item.bookTitle }}
                      <span class="text-gray-500">({{ item.code }})</span>
                    </div>
                    <div v-if="borrowing.items.length > 2" class="text-sm text-gray-500">
                      +{{ borrowing.items.length - 2 }} more items
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div>
                    <div class="font-medium">Borrowed:</div>
                    <div class="text-gray-500">{{ formatDate(borrowing.borrowedAt) }}</div>
                    <div v-if="borrowing.returnedAt">
                      <div class="font-medium">Returned:</div>
                      <div class="text-gray-500 mt-1">{{ formatDate(borrowing.returnedAt) }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="space-y-1">
                    <span
                      :class="[
                        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                        getBorrowingStatusClass(borrowing)
                      ]"
                    >
                      {{ getBorrowingStatus(borrowing) }}
                    </span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      v-if="!borrowing.returnedAt"
                      @click="processReturn(borrowing)"
                      class="text-green-600 hover:text-green-900"
                    >
                      Return
                    </button>
                    <button
                      @click="viewBorrowingDetails(borrowing)"
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13M0 18h.897c.35 0 .684-.187.897-.53l6.5-6.5c.761-.761 2.015-.761 2.776 0l6.5 6.5c.213.343.547.53.897.53H24c.441 0 .815-.374.815-.815V4.815A.815.815 0 0023.184 4H.815C.374 4 0 .815 0 4.815v13.184c0 .441.374.815.815.815z" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No borrowings found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Borrowing Details Modal -->
    <div v-if="showDetailsModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeDetailsModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-3xl sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              Borrowing Details #{{ selectedBorrowing?.id }}
            </h3>
            
            <div v-if="selectedBorrowing" class="space-y-6">
              <!-- User Info -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Borrower Information</h4>
                <div class="bg-gray-50 rounded-lg p-4">
                  <div class="grid grid-cols-2 gap-4">
                    <div>
                      <span class="text-sm text-gray-500">Name:</span>
                      <div class="font-medium">{{ selectedBorrowing.user.fullName }}</div>
                    </div>
                    <div>
                      <span class="text-sm text-gray-500">Email:</span>
                      <div class="font-medium">{{ selectedBorrowing.user.email }}</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Borrowed Items -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Borrowed Items</h4>
                <div class="space-y-2">
                  <div
                    v-for="item in selectedBorrowing.items"
                    :key="item.id"
                    class="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
                  >
                    <div class="flex-1">
                      <div class="font-medium text-gray-900">{{ item.bookTitle }}</div>
                      <div class="text-sm text-gray-500">Code: {{ item.code }}</div>
                      <div class="text-sm text-gray-500">
                        Borrowed Condition: 
                        <span :class="getConditionClass(item.borrowedCondition)">
                          {{ getConditionLabel(item.borrowedCondition) }}
                        </span>
                      </div>
                      <div v-if="item.returnedCondition" class="text-sm text-gray-500">
                        Returned Condition: 
                        <span :class="getConditionClass(item.returnedCondition)">
                          {{ getConditionLabel(item.returnedCondition) }}
                        </span>
                      </div>
                    </div>
                    <div class="text-sm font-medium text-gray-900">
                      ${{ item.price?.toFixed(2) }}
                    </div>
                  </div>
                </div>
              </div>

              <!-- Timeline -->
              <div>
                <h4 class="text-sm font-medium text-gray-900 mb-2">Timeline</h4>
                <div class="bg-gray-50 rounded-lg p-4">
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
                      <span v-if="!selectedBorrowing.isLate" class="text-sm font-medium text-green-600">{{ formatDate(selectedBorrowing.returnedAt) }}</span>
                      <span v-else class="text-sm font-medium text-red-600">{{ formatDate(selectedBorrowing.returnedAt) }}</span>
                    </div>
                    <div v-if="selectedBorrowing.isLate" class="flex justify-between">
                      <span class="text-sm text-gray-500">Status:</span>
                      <span class="text-sm font-medium text-red-600">Late</span>
                    </div>
                    <div v-if="selectedBorrowing.librarian" class="flex justify-between">
                      <span class="text-sm text-gray-500">Processed by:</span>
                      <span class="text-sm font-medium">{{ selectedBorrowing.librarian.fullName }} - {{ selectedBorrowing.librarian.email }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Penalty Info -->
              <div v-if="selectedBorrowing.penaltyDescription">
                <h4 class="text-sm font-medium text-gray-900 mb-2">Penalty Information</h4>
                <div class="bg-red-50 border border-red-200 rounded-md p-4">
                  <p class="text-sm text-red-700">{{ selectedBorrowing.penaltyDescription }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button
              v-if="!selectedBorrowing?.returnedAt"
              @click="processReturn(selectedBorrowing)"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm"
            >
              Process Return
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

    <!-- Return Processing Modal -->
    <div v-if="showReturnModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeReturnModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-3xl sm:w-full">
          <form @submit.prevent="confirmReturn">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Process Return - Borrowing #{{ returnBorrowing?.id }}
              </h3>
              
              <div class="space-y-6">
                <!-- Late Return Warning -->
                <div v-if="isLateReturn" class="bg-yellow-50 border border-yellow-200 rounded-md p-4">
                  <div class="flex">
                    <svg class="h-5 w-5 text-yellow-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                    </svg>
                    <div>
                      <h4 class="text-sm font-medium text-yellow-800">Late Return Detected</h4>
                      <p class="text-sm text-yellow-700 mt-1">
                        This borrowing is being returned late. A late return penalty will be automatically applied.
                      </p>
                    </div>
                  </div>
                </div>

                <!-- Book Items Condition Check -->
                <div>
                  <h4 class="text-lg font-medium text-gray-900 mb-4">Book Items Condition Assessment</h4>
                  <div class="space-y-4">
                    <div
                      v-for="item in returnBorrowing?.items"
                      :key="item.id"
                      class="border border-gray-200 rounded-lg p-4"
                    >
                      <div class="flex items-start justify-between mb-4">
                        <div class="flex-1">
                          <h5 class="font-medium text-gray-900">{{ item.bookTitle }}</h5>
                          <p class="text-sm text-gray-500">Code: {{ item.code }}</p>
                          <p class="text-sm text-gray-500">
                            Borrowed Condition: 
                            <span :class="getConditionClass(item.borrowedCondition)">
                              {{ getConditionLabel(item.borrowedCondition) }}
                            </span>
                          </p>
                          <p class="text-sm text-gray-500">Price: ${{ item.price?.toFixed(2) }}</p>
                        </div>
                      </div>

                      <div class="space-y-4">
                        <!-- Return Condition -->
                        <div>
                          <label class="block text-sm font-medium text-gray-700 mb-2">
                            Return Condition *
                          </label>
                          <select
                            v-model="returnForm.itemConditions[item.id]"
                            required
                            @change="checkPenaltyRequired(item.id)"
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          >
                            <option value="">Select condition</option>
                            <option value="GOOD">Good - No damage</option>
                            <option value="MINOR_DAMAGE">Minor Damage - Small wear/tear</option>
                            <option value="MAJOR_DAMAGE">Major Damage - Significant damage</option>
                            <option value="LOST">Lost - Book not returned</option>
                          </select>
                        </div>

                        <!-- Penalty Description (only for MAJOR_DAMAGE or LOST) -->
                        <div v-if="needsPenaltyDescription(item.id)">
                          <label class="block text-sm font-medium text-gray-700 mb-2">
                            Penalty Description *
                          </label>
                          <textarea
                            v-model="returnForm.penaltyDescriptions"
                            required
                            rows="3"
                            placeholder="Describe the damage or reason for penalty..."
                            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          ></textarea>
                        </div>

                        <!-- Penalty Warning -->
                        <div v-if="needsPenaltyDescription(item.id)" class="bg-red-50 border border-red-200 rounded-md p-3">
                          <div class="flex">
                            <svg class="h-5 w-5 text-red-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                            </svg>
                            <div>
                              <h6 class="text-sm font-medium text-red-800">Penalty Required</h6>
                              <p class="text-sm text-red-700">
                                A penalty will be automatically calculated and applied for this item based on the condition and damage description.
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="isProcessing || !canProcessReturn"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ isProcessing ? 'Processing...' : 'Confirm Return' }}
              </button>
              <button
                @click="closeReturnModal"
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
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'
import { getAllBorrowings, returnBook } from '@/api'

const router = useRouter()
const route = useRoute()
// State
const borrowings = ref([])
const isLoading = ref(true)
const isProcessing = ref(false)
const searchQuery = ref('')
const selectedStatus = ref('')
const sortBy = ref('borrowedAt')
const showDetailsModal = ref(false)
const showReturnModal = ref(false)
const selectedBorrowing = ref(null)
const returnBorrowing = ref(null)
const message = ref('')
const errorMessage = ref('')

const returnForm = reactive({
  itemConditions: {},
  penaltyDescriptions: ''
})

// Computed
const filteredBorrowings = computed(() => {
  let filtered = borrowings.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(borrowing =>
      borrowing.user.fullName.toLowerCase().includes(query) ||
      borrowing.user.email.toLowerCase().includes(query) ||
      borrowing.items.some(item => item.bookTitle.toLowerCase().includes(query))
    )
  }

  // Filter by status
  if (selectedStatus.value) {
    if (selectedStatus.value === 'active') {
      filtered = filtered.filter(borrowing => !borrowing.returnedAt && !borrowing.isLate)
    } else if (selectedStatus.value === 'overdue') {
      filtered = filtered.filter(borrowing => !borrowing.returnedAt && borrowing.isLate)
    }else if (selectedStatus.value === 'late') {
        filtered = filtered.filter(borrowing => borrowing.returnedAt && borrowing.isLate)
    } else if (selectedStatus.value === 'returned') {
      filtered = filtered.filter(borrowing => borrowing.returnedAt && !borrowing.isLate)
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
    } else if (sortBy.value === 'userName') {
      return a.user.name.localeCompare(b.user.name)
    }
    return 0
  })

  return filtered
})

const activeCount = computed(() => 
  borrowings.value.filter(b => !b.returnedAt && !b.isLate).length
)

const overdueCount = computed(() => 
  borrowings.value.filter(b => !b.returnedAt && b.isLate).length
)

const returnedCount = computed(() => 
  borrowings.value.filter(b => b.returnedAt && !b.isLate).length
)

const lateCount = computed(() => 
  borrowings.value.filter(b => b.returnedAt && b.isLate).length
)

const isLateReturn = computed(() => {
  if (!returnBorrowing.value) return false
  const dueDate = getDueDate(returnBorrowing.value.borrowedAt)
  const now = new Date()
  return now > new Date(dueDate)
})

const canProcessReturn = computed(() => {
  if (!returnBorrowing.value) return false

  const allConditionsSelected = returnBorrowing.value.items.every(item => {
    return returnForm.itemConditions[item.id]
  })

  if (!allConditionsSelected) return false

  const anyNeedsPenalty = returnBorrowing.value.items.some(item =>
    needsPenaltyDescription(item.id)
  )

  if (anyNeedsPenalty && !returnForm.penaltyDescriptions.trim()) {
    return false
  }

  return true
})

// Methods
const fetchBorrowings = async () => {
  try {
    isLoading.value = true
    const response = await getAllBorrowings()
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

const getConditionLabel = (condition) => {
  const labels = {
    'GOOD': 'Good',
    'MINOR_DAMAGE': 'Minor Damage',
    'MAJOR_DAMAGE': 'Major Damage',
    'LOST': 'Lost'
  }
  return labels[condition] || condition
}

const needsPenaltyDescription = (itemId) => {
  const condition = returnForm.itemConditions[itemId]
  return condition === 'MAJOR_DAMAGE' || condition === 'LOST'
}

const checkPenaltyRequired = () => {
  if (!returnBorrowing.value?.items.some(item => needsPenaltyDescription(item.id))) {
    returnForm.penaltyDescriptions = ''
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

const viewBorrowingDetails = (borrowing) => {
  selectedBorrowing.value = borrowing
  showDetailsModal.value = true
}

const processReturn = (borrowing) => {
  returnBorrowing.value = borrowing
  returnForm.itemConditions = {}
  returnForm.penaltyDescriptions = '';
  
  showReturnModal.value = true
  closeDetailsModal()
}

const confirmReturn = async () => {
  try {
    isProcessing.value = true

    const returnData = {
      id: returnBorrowing.value.id,
      penaltyDescription: returnForm.penaltyDescriptions
          ? returnForm.penaltyDescriptions.trim()
          : null,
      items: returnBorrowing.value.items.map(item => ({
        bookItemId: item.bookId,
        bookItemCondition: returnForm.itemConditions[item.id],
      }))
    }

    const response = await returnBook(returnData)
    const data = response.data.data

    const index = borrowings.value.findIndex(b => b.id === returnBorrowing.value.id)
    if (index !== -1) {
      borrowings.value[index] = {
        ...borrowings.value[index],
        ...data
      }

      borrowings.value[index].items.forEach(item => {
        item.available = true
        item.returnedCondition = returnForm.itemConditions[item.id]
      })
    }

    message.value = 'Return processed successfully. Penalties will be handled by the system if applicable.'
    setTimeout(() => (message.value = ''), 5000)
    closeReturnModal()
  } catch (error) {
    console.error('Error processing return:', error)
    errorMessage.value = 'Failed to process return'
    setTimeout(() => (errorMessage.value = ''), 3000)
  } finally {
    isProcessing.value = false
  }
}

const closeDetailsModal = () => {
  showDetailsModal.value = false
  selectedBorrowing.value = null
}

const closeReturnModal = () => {
  showReturnModal.value = false
  returnBorrowing.value = null
  returnForm.itemConditions = {}
  returnForm.penaltyDescriptions = ''
}

const handleQueryParams = () => {
    const email = route.query.email
    if (email) {
      searchQuery.value = decodeURIComponent(email)
    }
  }

  // Watch for route changes
  watch(() => route.query, () => {
    handleQueryParams()
  }, { immediate: true })
  
  // Lifecycle
  onMounted(() => {
    fetchBorrowings()
    handleQueryParams()
  })
</script>
