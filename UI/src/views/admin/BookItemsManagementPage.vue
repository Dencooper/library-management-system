<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Book Items Management</h1>
          <p class="text-gray-600 mt-2">Manage individual book copies and their conditions</p>
        </div>
        <button
          @click="showAddModal = true"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Add Book Item
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
              placeholder="Search by code or book title..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              @input="filterItems"
            />
          </div>

          <!-- Availability Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Availability</label>
            <select
              v-model="selectedAvailability"
              @change="filterItems"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All</option>
              <option value="available">Available</option>
              <option value="borrowed">Borrowed</option>
            </select>
          </div>

          <!-- Condition Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Condition</label>
            <select
              v-model="selectedCondition"
              @change="filterItems"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Select Condition</option>
              <option value="GOOD">Good</option>
              <option value="MINOR_DAMAGE">Minor Damage</option>
              <option value="MAJOR_DAMAGE">Major Damage</option>
              <option value="LOST">Lost</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Book Items Table -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">
            Book Items ({{ filteredItems.length }})
          </h2>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Table -->
        <div v-else-if="filteredItems.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Item Code
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Book Title
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Price
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Condition
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Availability
                </th>
                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="item in filteredItems"
                :key="item.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ item.code }}</div>
                  <div class="text-sm text-gray-500">ID: {{ item.id }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ item.bookTitle }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  ${{ item.price?.toFixed(2) || '0.00' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      getConditionClass(item.bookItemCondition)
                    ]"
                  >
                    {{ getConditionLabel(item.bookItemCondition) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      item.available ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ item.available ? 'Available' : 'Borrowed' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      @click="editItem(item)"
                      class="text-indigo-600 hover:text-indigo-900"
                    >
                      Edit
                    </button>
                    <button
                      @click="deleteItem(item)"
                      class="text-red-600 hover:text-red-900"
                    >
                      Delete
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No book items found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Add/Edit Item Modal -->
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <form @submit.prevent="saveItem">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4">
                {{ showAddModal ? 'Add New Book Item' : 'Edit Book Item' }}
              </h3>
              
              <div class="space-y-4">
                <!-- Book Selection (for Add Modal) -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Select Book *</label>
                  <select
                    v-model="itemForm.bookId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Choose a book</option>
                    <option v-for="book in allBooks" :key="book.id" :value="book.id">
                      {{ book.title }} - {{ book.author.name }} ({{ book.isbn }})
                    </option>
                  </select>
                </div>
                
                <!-- Code -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Item Code *</label>
                  <input
                    v-model="itemForm.code"
                    type="text"
                    required
                    placeholder="e.g., 9786042130523-001"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- Condition -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">Condition *</label>
                  <select
                    v-model="itemForm.bookItemCondition"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select Condition</option>
                    <option value="GOOD">Good</option>
                    <option value="MINOR_DAMAGE">Minor Damage</option>
                    <option value="MAJOR_DAMAGE">Major Damage</option>
                    <option value="LOST">Lost</option>
                  </select>
                </div>

                <!-- Availability -->
                <div>
                  <label class="flex items-center">
                    <input
                      v-model="itemForm.isAvailable"
                      type="checkbox"
                      class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                    />
                    <span class="ml-2 text-sm text-gray-700">Available for borrowing</span>
                  </label>
                </div>
              </div>
            </div>
            
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                :disabled="isSaving"
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50"
              >
                {{ isSaving ? 'Saving...' : (showAddModal ? 'Add Item' : 'Update Item') }}
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
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getAllBookItems, getAllBooks, createBookItem, updateBookItem, deleteBookItem } from '@/api'
import Swal from 'sweetalert2'

const route = useRoute()

// State
const bookItems = ref([])
const allBooks = ref([])
const isLoading = ref(true)
const isSaving = ref(false)
const showAddModal = ref(false)
const showEditModal = ref(false)
const searchQuery = ref('')
const selectedAvailability = ref('')
const selectedCondition = ref('')
const message = ref('')
const errorMessage = ref('')

const itemForm = reactive({
  id: null,
  bookId: '',
  code: '',
  bookTitle: '',
  bookItemCondition: '',
  isAvailable: true
})

// Computed
const filteredItems = computed(() => {
  let filtered = bookItems.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(item =>
      item.code.toLowerCase().includes(query) ||
      item.bookTitle.toLowerCase().includes(query)
    )
  }

  // Filter by availability
  if (selectedAvailability.value) {
    if (selectedAvailability.value === 'available') {
      filtered = filtered.filter(item => item.available)
    } else if (selectedAvailability.value === 'borrowed') {
      filtered = filtered.filter(item => !item.available)
    }
  }

  // Filter by condition
  if (selectedCondition.value) {
    filtered = filtered.filter(item => item.bookItemCondition === selectedCondition.value)
  }

  return filtered
})

// Methods
const fetchBookItems = async () => {
  try {
    isLoading.value = true
    const response = await getAllBookItems()
    bookItems.value = response.data.data
  } catch (error) {
    console.error('Error fetching book items:', error)
    errorMessage.value = 'Failed to load book items'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  } 
}

const fetchAllBooks = async () => {
  try {
    isLoading.value = true
    const response = await getAllBooks()
    allBooks.value = response.data.data
  } catch (error) {
    console.error('Error fetching books:', error)
    errorMessage.value = 'Failed to load books'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  } 
}

const getConditionClass = (condition) => {
    const classes = {
      'GOOD': 'bg-blue-100 text-blue-800',
      'MINOR_DAMAGE': 'bg-orange-100 text-orange-800',
      'MAJOR_DAMAGE': 'bg-red-100 text-red-800',
      'LOST': 'bg-red-100 text-red-800'
    }
    return classes[condition] || 'bg-gray-100 text-gray-800'
  }

const getConditionLabel = (condition) => {
  const labels = {
    'GOOD': 'Good',
    'MINOR_DAMAGE': 'Minor Damage',
    'MAJOR_DAMAGE': 'Major Damage',
    'LOST': 'Lost',
  }
  return labels[condition] || condition
}

const editItem = (item) => {
  itemForm.id = item.id 
  itemForm.bookId = allBooks.value.find(book => book.title === item.bookTitle)?.id || ''
  itemForm.code = item.code
  itemForm.bookItemCondition = item.bookItemCondition
  itemForm.isAvailable = item.available
  
  showEditModal.value = true
}

const deleteItem = async (item) => {
  const result = await Swal.fire({
    title: 'Delete item?',
    text: `Are you sure you want to delete "${item.code}"?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete it!',
    cancelButtonText: 'Cancel'
  })

  if (result.isConfirmed) {
    try {
      await deleteBookItem(item.id, true)
      
      bookItems.value = bookItems.value.filter(i => i.id !== item.id)
      message.value = 'Book item deleted successfully'
      setTimeout(() => message.value = '', 3000)
    } catch (error) {
      console.error('Error deleting book item:', error)
      errorMessage.value = 'Failed to delete book item'
      setTimeout(() => errorMessage.value = '', 3000)
    }
  }
}

const saveItem = async () => {
  try {
    isSaving.value = true
    
    if (showAddModal.value) {
      const response = await createBookItem(itemForm)
      const newItem = response.data.data
      bookItems.value.unshift(newItem)
      message.value = 'Book item added successfully'
    } else {
      const response = await updateBookItem(itemForm.id, itemForm)
      const updatedItem = response.data.data
      const index = bookItems.value.findIndex(i => i.id === itemForm.id)
      if (index !== -1) {
        bookItems.value[index] = { 
          ...bookItems.value[index], 
          ...updatedItem
        }
      }
      message.value = 'Book item updated successfully'
    }
    
    closeModal()
    setTimeout(() => message.value = '', 3000)
    
  } catch (error) {
    console.error('Error saving book item:', error)
    errorMessage.value = 'Failed to save book item'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isSaving.value = false
  }
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  
  // Reset form
  Object.keys(itemForm).forEach(key => {
    if (key === 'id') {
      itemForm[key] = null
    } else if (key === 'available') {
      itemForm[key] = true
    } else {
      itemForm[key] = ''
    }
  })
}

const handleQueryParams = () => {
  const bookTitle = route.query.bookTitle
  if (bookTitle) {
    searchQuery.value = decodeURIComponent(bookTitle)
  }
}

// Watch for route changes
watch(() => route.query, () => {
  handleQueryParams()
}, { immediate: true })

// Lifecycle
onMounted(() => {
  fetchBookItems()
  fetchAllBooks()
  handleQueryParams()
})
</script>
