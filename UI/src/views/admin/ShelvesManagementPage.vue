<template>
    <div class="min-h-screen bg-gray-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Header -->
        <div class="flex items-center justify-between mb-8">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">Shelf Management</h1>
            <p class="text-gray-600 mt-2">Organize and manage library shelf locations</p>
          </div>
          <button
            @click="showAddModal = true"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Add New Shelf
          </button>
        </div>
  
        <!-- Search and Filters -->
        <div class="bg-white rounded-lg shadow p-6 mb-8">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Search -->
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">Search Shelves</label>
              <div class="relative">
                <input
                  v-model="searchQuery"
                  type="text"
                  placeholder="Search by code or location..."
                  class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  @input="filterShelves"
                />
                <svg class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
            </div>
  
            <!-- Sort -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Sort By</label>
              <select
                v-model="sortBy"
                @change="sortShelves"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="code">Shelf Code</option>
                <option value="location">Location</option>
                <option value="createdAt">Created Date</option>
              </select>
            </div>
          </div>
        </div>
  
        <!-- Shelves Grid -->
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-lg font-semibold text-gray-900">
              Shelves ({{ filteredShelves.length }})
            </h2>
          </div>
  
          <!-- Loading -->
          <div v-if="isLoading" class="flex justify-center py-12">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
  
          <!-- Grid View -->
          <div v-else-if="filteredShelves.length > 0" class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              <div
                v-for="shelf in filteredShelves"
                :key="shelf.id"
                class="bg-gray-50 rounded-lg p-6 border border-gray-200 hover:shadow-md transition-shadow"
              >
                <!-- Shelf Icon -->
                <div class="flex items-center justify-center w-12 h-12 bg-blue-100 rounded-lg mb-4">
                  <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                  </svg>
                </div>
  
                <!-- Shelf Info -->
                <div class="mb-4">
                  <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ shelf.code }}</h3>
                  <p class="text-sm text-gray-600 mb-2">{{ shelf.location }}</p>
                  <div class="text-xs text-gray-500">
                    <p>Created: {{ formatDate(shelf.createdAt) }}</p>
                    <p v-if="shelf.updatedAt !== shelf.createdAt">
                      Updated: {{ shelf.updatedAt == null ? 'N/A' : formatDate(shelf.updatedAt) }}
                    </p>
                  </div>
                </div>
  
                <!-- Actions -->
                <div class="flex items-center justify-between">
                  <div class="flex space-x-2">
                    <button
                      @click="editShelf(shelf)"
                      class="text-indigo-600 hover:text-indigo-900 text-sm font-medium"
                    >
                      Edit
                    </button>
                    
                  </div>
                  <button
                      @click="deleteShelf(shelf)"
                      class="text-red-600 hover:text-red-900 text-sm font-medium"
                    >
                      Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
  
          <!-- No Results -->
          <div v-else class="text-center py-12">
            <svg class="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
            </svg>
            <h3 class="text-lg font-medium text-gray-900 mb-2">No shelves found</h3>
            <p class="text-gray-600">Try adjusting your search criteria</p>
          </div>
        </div>
      </div>
  
      <!-- Add/Edit Shelf Modal -->
      <div v-if="showAddModal || showEditModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
          <div @click.stop class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <form @submit.prevent="saveShelf">
              <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                <h3 class="text-lg font-medium text-gray-900 mb-4">
                  {{ showAddModal ? 'Add New Shelf' : 'Edit Shelf' }}
                </h3>
                
                <div class="space-y-4">
                  <!-- Shelf Code -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Shelf Code</label>
                    <input
                      v-model="shelfForm.code"
                      type="text"
                      required
                      placeholder="e.g., A1-01, B2-15"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <p class="text-xs text-gray-500 mt-1">Use format: Section-Row (e.g., A1-01)</p>
                  </div>
  
                  <!-- Location -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Location</label>
                    <input
                      v-model="shelfForm.location"
                      type="text"
                      required
                      placeholder="e.g., Floor 1, Section A, Row 1"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <p class="text-xs text-gray-500 mt-1">Detailed location description</p>
                  </div>
                </div>
              </div>
              
              <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                <button
                  type="submit"
                  :disabled="isSaving"
                  class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50"
                >
                  {{ isSaving ? 'Saving...' : (showAddModal ? 'Add Shelf' : 'Update Shelf') }}
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
  import { ref, reactive, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getAllShelves, createShelf, updateShelf, updateShelfStatus } from '@/api'
  
  import Swal from 'sweetalert2'

  const router = useRouter()
  
  // State
  const shelves = ref([])
  const isLoading = ref(true)
  const isSaving = ref(false)
  const showAddModal = ref(false)
  const showEditModal = ref(false)
  const searchQuery = ref('')
  const sortBy = ref('code')
  const message = ref('')
  const errorMessage = ref('')
  
  const shelfForm = reactive({
    id: null,
    code: '',
    location: ''
  })
  
  // Computed
  const filteredShelves = computed(() => {
    let filtered = shelves.value
  
    // Filter by search query
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      filtered = filtered.filter(shelf =>
        shelf.code.toLowerCase().includes(query) ||
        shelf.location.toLowerCase().includes(query)
      )
    }
  
    // Sort
    filtered.sort((a, b) => {
      if (sortBy.value === 'code') {
        return a.code.localeCompare(b.code)
      } else if (sortBy.value === 'location') {
        return a.location.localeCompare(b.location)
      } else if (sortBy.value === 'createdAt') {
        return new Date(b.createdAt) - new Date(a.createdAt)
      }
      return 0
    })
  
    return filtered
  })
  
  // Methods
  const fetchShelves = async () => {
    try {
      isLoading.value = true
      const response = await getAllShelves()
      shelves.value = response.data.data
    } catch (error) {
      console.error('Error fetching shelves:', error)
      errorMessage.value = 'Failed to load shelves'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isLoading.value = false
    }
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    })
  }

  
  const editShelf = (shelf) => {
    shelfForm.id = shelf.id
    shelfForm.code = shelf.code
    shelfForm.location = shelf.location
    
    showEditModal.value = true
  }
  
  const deleteShelf = async (shelf) => {
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
        await updateShelfStatus(shelf.id, true)
        shelves.value = shelves.value.filter(s => s.id !== shelf.id)
        message.value = 'Shelf deleted successfully'
        setTimeout(() => message.value = '', 3000)
      } catch (error) {
        console.error('Error deleting shelf:', error)
        errorMessage.value = 'Failed to delete shelf'
        setTimeout(() => errorMessage.value = '', 3000)
      }
    }
  }
  
  const saveShelf = async () => {
    try {
      isSaving.value = true
      
      if (showAddModal.value) {
        const response = await createShelf(shelfForm)
        const newShelf = response.data.data
        shelves.value.unshift(newShelf)
        message.value = 'Shelf added successfully'
      } else {
        const response = await updateShelf(shelfForm.id, shelfForm)
        const updatedShelf = response.data.data
        const index = shelves.value.findIndex(s => s.id === shelfForm.id)
        if (index !== -1) {
          shelves.value[index] = { 
            ...shelves.value[index], 
            ...updatedShelf
          }
        }
        message.value = 'Shelf updated successfully'
      }
      
      closeModal()
      setTimeout(() => message.value = '', 3000)
      
    } catch (error) {
      console.error('Error saving shelf:', error)
      errorMessage.value = 'Failed to save shelf'
      setTimeout(() => errorMessage.value = '', 3000)
    } finally {
      isSaving.value = false
    }
  }
  
  const closeModal = () => {
    showAddModal.value = false
    showEditModal.value = false
    
    // Reset form
    Object.keys(shelfForm).forEach(key => {
      shelfForm[key] = key === 'id' ? null : ''
    })
  }
  
  // Lifecycle
  onMounted(() => {
    fetchShelves()
  })
  </script>
  