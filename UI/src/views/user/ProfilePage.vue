<template>
    <div class="min-h-screen bg-gray-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Header -->
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-900">My Profile</h1>
          <p class="text-gray-600 mt-2">Manage your account information and preferences</p>
        </div>
  
        <!-- Profile Card -->
        <div class="bg-white rounded-lg shadow-lg overflow-hidden">
          <!-- Profile Header -->
          <div class="bg-gradient-to-r from-blue-500 to-purple-600 px-6 py-8">
            <div class="flex items-center">
              <div class="w-20 h-20 bg-white rounded-full flex items-center justify-center">
                <svg class="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <div class="ml-6">
                <h2 class="text-2xl font-bold text-white">{{ user?.fullName || 'User Name' }}</h2>
                <p class="text-blue-100">{{ user?.email }}</p>
                <span class="inline-block mt-2 px-3 py-1 bg-white bg-opacity-20 text-white text-sm rounded-full">
                  {{ user?.role || 'USER' }}
                </span>
              </div>
            </div>
          </div>
  
          <!-- Profile Content -->
          <div class="p-6">
            <!-- Edit Form -->
            <form @submit.prevent="updateProfile" class="space-y-6">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Full Name -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Full Name</label>
                  <input
                    v-model="profileForm.fullName"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    :disabled="!isEditing"
                  />
                </div>
  
                <!-- Email -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                  <input
                    v-model="profileForm.email"
                    type="email"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50"
                    disabled
                  />
                </div>
  
                <!-- Phone -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Phone</label>
                  <input
                    v-model="profileForm.phone"
                    type="tel"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    :disabled="!isEditing"
                  />
                </div>
  
                <!-- Address -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Address</label>
                  <input
                    v-model="profileForm.address"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    :disabled="!isEditing"
                  />
                </div>
              </div>
  
              <!-- Action Buttons -->
              <div class="flex justify-end space-x-4 pt-6 border-t">
                <button
                  v-if="!isEditing"
                  @click="startEditing"
                  type="button"
                  class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                >
                  Edit Profile
                </button>
                
                <template v-else>
                  <button
                    @click="cancelEditing"
                    type="button"
                    class="px-6 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    :disabled="isUpdating"
                    class="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50 transition-colors"
                  >
                    {{ isUpdating ? 'Saving...' : 'Save Changes' }}
                  </button>
                </template>
              </div>
            </form>
          </div>
        </div>
  
        <!-- Statistics Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
          <div class="bg-white rounded-lg shadow p-6">
            <div class="flex items-center">
              <div class="p-3 bg-blue-100 rounded-full">
                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm font-medium text-gray-600">Books Borrowed</p>
                <p class="text-2xl font-bold text-gray-900">{{ stats.totalBorrowed }}</p>
              </div>
            </div>
          </div>
  
          <div class="bg-white rounded-lg shadow p-6">
            <div class="flex items-center">
              <div class="p-3 bg-green-100 rounded-full">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm font-medium text-gray-600">Books Returned</p>
                <p class="text-2xl font-bold text-gray-900">{{ stats.totalReturned }}</p>
              </div>
            </div>
          </div>
  
          <div class="bg-white rounded-lg shadow p-6">
            <div class="flex items-center">
              <div class="p-3 bg-yellow-100 rounded-full">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm font-medium text-gray-600">Currently Borrowed</p>
                <p class="text-2xl font-bold text-gray-900">{{ stats.currentlyBorrowed }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue'
  import { useAuthStore } from '@/stores/auth'
  
  const authStore = useAuthStore()
  
  // State
  const isEditing = ref(false)
  const isUpdating = ref(false)
  const user = ref(authStore.user)
  
  const profileForm = reactive({
    fullName: '',
    email: '',
    phone: '',
    address: ''
  })
  
  const stats = reactive({
    totalBorrowed: 0,
    totalReturned: 0,
    currentlyBorrowed: 0
  })
  
  // Methods
  const initializeForm = () => {
    if (user.value) {
      profileForm.fullName = user.value.fullName || ''
      profileForm.email = user.value.email || ''
      profileForm.phone = user.value.phone || ''
      profileForm.address = user.value.address || ''
    }
  }
  
  const startEditing = () => {
    isEditing.value = true
  }
  
  const cancelEditing = () => {
    isEditing.value = false
    initializeForm() // Reset form
  }
  
  const updateProfile = async () => {
    try {
      isUpdating.value = true
      
      // Add your update profile API call here
      // await updateUserProfile(profileForm)
      
      // Update auth store
      authStore.updateUser(profileForm)
      user.value = authStore.user
      
      isEditing.value = false
      
      // Show success message
      console.log('Profile updated successfully')
      
    } catch (error) {
      console.error('Error updating profile:', error)
    } finally {
      isUpdating.value = false
    }
  }
  
  const fetchUserStats = async () => {
    try {
      // Add your API call to fetch user statistics
      // const response = await getUserStats()
      // stats.totalBorrowed = response.data.totalBorrowed
      // stats.totalReturned = response.data.totalReturned
      // stats.currentlyBorrowed = response.data.currentlyBorrowed
      
      // Mock data for now
      stats.totalBorrowed = 15
      stats.totalReturned = 12
      stats.currentlyBorrowed = 3
    } catch (error) {
      console.error('Error fetching user stats:', error)
    }
  }
  
  // Lifecycle
  onMounted(() => {
    initializeForm()
    fetchUserStats()
  })
  </script>