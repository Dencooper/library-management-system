<template>
    <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
      <div class="sm:flex sm:items-start">
        <div class="mt-3 text-center sm:mt-0 sm:text-left w-full">
          <h3 class="text-lg leading-6 font-medium text-gray-900 mb-4">
            Create New Account
          </h3>
          
          <form @submit.prevent="handleRegister" class="space-y-4">
            <div>
              <label for="fullName" class="block text-sm font-medium text-gray-700">Full Name</label>
              <input
                type="text"
                id="fullName"
                v-model="form.fullName"
                required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter your full name"
              />
            </div>
            
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
              <input
                type="email"
                id="email"
                v-model="form.email"
                required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter your email"
              />
            </div>
  
            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700">Phone</label>
              <input
                type="tel"
                id="phone"
                v-model="form.phone"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter your phone number"
              />
            </div>
  
            <div>
              <label for="address" class="block text-sm font-medium text-gray-700">Address</label>
              <textarea
                id="address"
                v-model="form.address"
                rows="2"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter your address"
              ></textarea>
            </div>
            
            <div>
              <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
              <input
                type="password"
                id="password"
                v-model="form.password"
                required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Enter your password"
              />
            </div>
  
            <div>
              <label for="confirmPassword" class="block text-sm font-medium text-gray-700">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                v-model="form.confirmPassword"
                required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="Confirm your password"
              />
            </div>
  
            <div v-if="errorMessage" class="text-red-600 text-sm">
              {{ errorMessage }}
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
      <button
        type="submit"
        @click="handleRegister"
        :disabled="authStore.isLoading"
        class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50"
      >
        <span v-if="authStore.isLoading" class="mr-2">
          <svg class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
        </span>
        {{ authStore.isLoading ? 'Creating...' : 'Create Account' }}
      </button>
      <button
        type="button"
        @click="$emit('close')"
        class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
      >
        Cancel
      </button>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import { useAuthStore } from '@/stores/auth'
  
  const emit = defineEmits(['close', 'success'])
  const authStore = useAuthStore()
  
  const form = reactive({
    fullName: '',
    email: '',
    phone: '',
    address: '',
    password: '',
    confirmPassword: ''
  })
  
  const errorMessage = ref('')
  
  const handleRegister = async () => {
    errorMessage.value = ''
    
    // Validate passwords match
    if (form.password !== form.confirmPassword) {
      errorMessage.value = 'Passwords do not match'
      return
    }
    
    const { confirmPassword, ...registerData } = form
    const result = await authStore.registerUser(registerData)
    
    if (result.success) {
      emit('success')
    } else {
      errorMessage.value = result.message
    }
  }
  </script>