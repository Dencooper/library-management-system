<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Borrowing</h1>
          <p class="text-gray-600 mt-2">Create and manage all book borrowings</p>
        </div>
        <button
          @click="showCreateBorrowingModal = true"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Create Borrowing
        </button>
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

    <!-- Create Borrowing Modal -->
    <div v-if="showCreateBorrowingModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeCreateBorrowingModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
          <form @submit.prevent="confirmCreateBorrowing">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                Create New Borrowing
              </h3>
              
              <div class="space-y-6">
                <!-- Librarian Info -->
                <div>
                  <label class="block font-medium text-gray-700 mb-2">Librarian</label>
                  <div class="bg-gray-50 rounded-lg p-4">
                    <div class="grid grid-cols-2 gap-4">
                      <div>
                        <span class="text-sm text-gray-500">Name:</span>
                        <div class="font-medium">{{ authStore.user.fullName }}</div>
                      </div>
                      <div>
                        <span class="text-sm text-gray-500">Email:</span>
                        <div class="font-medium">{{ authStore.user.email }}</div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Email -->
                <div class="space-y-3">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">User Email</label>
                    <div class="flex items-center gap-x-2">
                      <input
                        v-model="createForm.email"
                        type="text"
                        required
                        placeholder="e.g., user@example.com"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                      <button 
                        @click.prevent="searchUser"
                        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
                        Check 
                      </button>
                    </div>
                  </div>
                </div>

                <!-- User Info -->
                <div v-if="user" class="space-y-4">
                  <div
                    class="p-4 rounded-lg shadow-sm"
                    :class="[
                      'bg-gray-50 border',
                      user.isBanned ? 'border-red-500' : 'border-gray-200'
                    ]"
                  >
                    <div class="flex flex-wrap justify-between items-center gap-4">
                      <div class="flex justify-between items-center gap-4 w-full mb-3">
                        <div class="text-gray-900">
                          Name: {{ user.fullName }}
                        </div>
                        <div class="text-gray-900">
                          Phone Number: {{ user.phone || '...' }}
                        </div>
                      </div>
                      <div class="flex w-full">
                        <div class="text-gray-900">
                          Address: {{ user.address || '...' }}
                        </div>
                      </div>
                    </div>
                    <div v-if="user.isBanned" class="mt-2 text-red-600 text-sm font-medium">
                      ⚠️ This user is banned and cannot borrow books
                    </div>
                  </div>
                </div>

                <!-- Book Selection Section -->
                <div class="space-y-4">
                  <label class="block text-sm font-medium text-gray-700">Book Selection</label>
                  
                  <!-- Add Book Form -->
                  <div class="border border-gray-200 rounded-lg p-4 bg-gray-50">
                    <div class="space-y-3">
                      <label class="block text-sm font-medium text-gray-700">Add Book by Code</label>
                      <div class="flex items-center gap-x-2">
                        <input
                          v-model="currentBookCode"
                          type="text"
                          placeholder="e.g., 9786042130523-001"
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          @keyup.enter="addBookItem"
                        />
                        <button 
                          @click.prevent="addBookItem"
                          :disabled="!currentBookCode.trim()"
                          class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
                          Add
                        </button>
                      </div>
                    </div>
                  </div>

                  <!-- Selected Books List -->
                  <div v-if="selectedBooks.length > 0" class="space-y-3">
                    <div class="flex items-center justify-between">
                      <label class="block text-sm font-medium text-gray-700">
                        Selected Books ({{ selectedBooks.length }})
                      </label>
                      <button
                        @click.prevent="clearAllBooks"
                        type="button"
                        class="text-sm text-red-600 hover:text-red-800 bg-transparent border border-red-300 hover:border-red-400 px-3 py-1 rounded-md transition-colors"
                      >
                        Clear All
                      </button>
                    </div>
                    <div class="space-y-2 max-h-60 overflow-y-auto">
                      <div
                        v-for="(book, index) in selectedBooks"
                        :key="book.id"
                        class="flex items-center justify-between p-3 bg-white border border-gray-200 rounded-lg shadow-sm"
                      >
                        <div class="flex-1">
                          <div class="font-medium text-gray-900">{{ book.bookTitle }}</div>
                          <div class="text-sm text-gray-500">Code: {{ book.code }}</div>
                          <div class="flex items-center gap-4 mt-1">
                            <span :class="getConditionClass(book.bookItemCondition)" class="text-sm">
                              {{ getConditionLabel(book.bookItemCondition) }}
                            </span>
                            <span class="text-sm font-medium text-gray-900">
                              ${{ book.price?.toFixed(2) }}
                            </span>
                            <span
                              :class="[
                                'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium',
                                book.available ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                              ]"
                            >
                              {{ book.available ? 'Available' : 'Not Available' }}
                            </span>
                          </div>
                        </div>
                        <button
                          @click="removeBookItem(index)"
                          class="ml-4 p-2 text-red-600 hover:text-red-800 hover:bg-red-50 rounded-full transition-colors"
                        >
                          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                          </svg>
                        </button>
                      </div>
                    </div>
                  </div>

                  <!-- No Books Selected Message -->
                  <div v-else class="text-center py-8 text-gray-500">
                    <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13" />
                    </svg>
                    <p>No books selected. Add books using their codes above.</p>
                  </div>
                </div>

                <!-- Description -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Description
                  </label>
                  <textarea
                    v-model="createForm.description"
                    rows="3"
                    placeholder="Additional notes or instructions..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  ></textarea>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="!canProcessCreate || isCreatingBorrowing"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
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
                        Condition: 
                        <span :class="getConditionClass(item.bookItemCondition)">
                          {{ getConditionLabel(item.bookItemCondition) }}
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
                Process Return - Borrowing #{{ selectedBorrowing?.id }}
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

                <!-- Book Items Condition Check -->
                <div>
                  <h4 class="text-lg font-medium text-gray-900 mb-4">Condition Assessment</h4>
                  <div class="space-y-4">
                    <div
                      v-for="item in selectedBorrowing?.items"
                      :key="item.id"
                      class="border border-gray-200 rounded-lg p-4"
                    >
                      <div class="flex items-start justify-between mb-4">
                        <div class="flex-1">
                          <h5 class="font-medium text-gray-900">{{ item.bookTitle }}</h5>
                          <p class="text-sm text-gray-500">Code: {{ item.code }}</p>
                          <p class="text-sm text-gray-500">
                            Borrowed Condition: 
                            <span :class="getConditionClass(item.bookItemCondition)">
                              {{ getConditionLabel(item.bookItemCondition) }}
                            </span>
                          </p>
                          <p class="text-sm text-gray-500">Price: ${{ item.price?.toFixed(2) }}</p>
                        </div>
                      </div>
                      <div class="space-y-4">
                        <!-- Return Condition -->
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          Return Condition
                        </label>
                        <select
                          v-model="returnForm.itemConditions[item.id]"
                          required
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value="">Select condition</option>
                          <option value="GOOD">Good - No damage</option>
                          <option value="MINOR_DAMAGE">Minor Damage - Small wear/tear</option>
                          <option value="MAJOR_DAMAGE">Major Damage - Significant damage</option>
                          <option value="LOST">Lost - Book not returned</option>
                        </select>
                      </div>
                    </div>
                    <!-- Penalty Warning -->
                    <div v-if="needsPenaltyDescription()" class="bg-red-50 border border-red-200 rounded-md p-3">
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
                      <!-- Penalty Description (only for MAJOR_DAMAGE or LOST) -->
                      <div v-if="needsPenaltyDescription()">
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          Penalty Description
                        </label>
                        <textarea
                          v-model="returnForm.penaltyDescriptions"
                          required
                          rows="3"
                          placeholder="Describe the damage or reason for penalty..."
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        ></textarea>
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
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getAllBorrowings, returnBook, getBookItemByCode, getUserByEmail, createBorrowing } from '@/api'

const route = useRoute()
const authStore = useAuthStore()

// State
const borrowings = ref([])
const selectedBooks = ref([]) // Changed from bookItem to selectedBooks array
const user = ref(null)
const isLoading = ref(true)
const isProcessing = ref(false)
const searchQuery = ref('')
const selectedStatus = ref('')
const sortBy = ref('borrowedAt')
const showCreateBorrowingModal = ref(false)
const showDetailsModal = ref(false)
const showReturnModal = ref(false)
const selectedBorrowing = ref(null)
const message = ref('')
const errorMessage = ref('')
const isCreatingBorrowing = ref(false)
const currentBookCode = ref('') // New field for current book code input

const createForm = reactive({
  email: '',
  description: ''
})

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
    } else if (selectedStatus.value === 'late') {
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

const isLateReturn = computed(() => {
  if (!selectedBorrowing.value) return false
  const dueDate = getDueDate(selectedBorrowing.value.borrowedAt)
  const now = new Date()
  return now > new Date(dueDate)
})

const canProcessCreate = computed(() => {
  if (!user.value) return false
  if (user.value.isBanned) return false
  if (selectedBooks.value.length === 0) return false
  
  // Check if all selected books are available
  const allAvailable = selectedBooks.value.every(book => book.available)
  return allAvailable
})

const canProcessReturn = computed(() => {
  if (!selectedBorrowing.value) return false

  const allConditionsSelected = selectedBorrowing.value.items.every(item => {
    return returnForm.itemConditions[item.id]
  })

  if (!allConditionsSelected) return false

  if( needsPenaltyDescription() && !returnForm.penaltyDescriptions.trim()) {
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

const searchUser = async () => {
  user.value = null
  try {
    const response = await getUserByEmail(createForm.email)
    user.value = response.data.data
  } catch (error) {
    errorMessage.value = 'No user found with that email'
    setTimeout(() => errorMessage.value = '', 3000)
    return
  }
}

const addBookItem = async () => {
  if (!currentBookCode.value.trim()) return

  try {
    // Check if book is already selected
    const isAlreadySelected = selectedBooks.value.some(book => book.code === currentBookCode.value.trim())
    if (isAlreadySelected) {
      errorMessage.value = 'This book is already selected'
      setTimeout(() => errorMessage.value = '', 3000)
      return
    }

    const response = await getBookItemByCode(currentBookCode.value.trim())
    const bookItem = response.data.data

    if (!bookItem.available) {
      errorMessage.value = 'This book is not available for borrowing'
      setTimeout(() => errorMessage.value = '', 3000)
      return
    }

    selectedBooks.value.push(bookItem)
    currentBookCode.value = '' // Clear the input
    
    message.value = 'Book added successfully'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    errorMessage.value = 'No matching or available book found with that code'
    setTimeout(() => errorMessage.value = '', 3000)
  }
}

const removeBookItem = (index) => {
  selectedBooks.value.splice(index, 1)
}

const clearAllBooks = () => {
  selectedBooks.value = []
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
    if (borrowing.isLate) {
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

const needsPenaltyDescription = () => {
 return selectedBorrowing.value?.items.some(item => {
    const condition = returnForm.itemConditions[item.id]
    return condition === 'MAJOR_DAMAGE' || condition === 'LOST'
  }) || false
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
  selectedBorrowing.value = borrowing
  showReturnModal.value = true
}

const confirmCreateBorrowing = async () => {
  try {
    isCreatingBorrowing.value = true
    const borrowingData = {
      email: createForm.email,
      bookItemIds: selectedBooks.value.map(book => book.id), 
      description: createForm.description || null
    }

    const response = await createBorrowing(borrowingData)
    
    // Add the new borrowing to the list
    borrowings.value.unshift(response.data.data)
    
    message.value = 'Borrowing created successfully!'
    setTimeout(() => message.value = '', 3000)
    
    closeCreateBorrowingModal()
    
    // Refresh the borrowings list
    await fetchBorrowings()
  } catch (error) {
    console.error('Error creating borrowing:', error)
    errorMessage.value = error.response?.data?.message || 'Failed to create borrowing'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isCreatingBorrowing.value = false
  }
}

const confirmReturn = async () => {
  try {
    isProcessing.value = true
    const returnData = {
      borrowingId: selectedBorrowing.value.id,
      description: returnForm.penaltyDescriptions
          ? returnForm.penaltyDescriptions.trim()
          : null,
      items: selectedBorrowing.value.items.map(item => ({
        bookItemId: item.id,
        bookItemCondition: returnForm.itemConditions[item.id],
      }))
    }

    const response = await returnBook(returnData)
    const data = response.data.data
    const index = borrowings.value.findIndex(b => b.id === selectedBorrowing.value.id)
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

const closeCreateBorrowingModal = () => {
  showCreateBorrowingModal.value = false
  selectedBooks.value = [] // Clear selected books
  user.value = null
  createForm.email = ''
  createForm.description = ''
  currentBookCode.value = ''
}

const closeDetailsModal = () => {
  showDetailsModal.value = false
  selectedBorrowing.value = null
}

const closeReturnModal = () => {
  showReturnModal.value = false
  selectedBorrowing.value = null
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
