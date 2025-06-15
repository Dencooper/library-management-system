<template>
    <div class="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div class="sm:mx-auto sm:w-full sm:max-w-md">
        <div class="flex justify-center">
          <svg class="w-12 h-12 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
        </div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Create your account
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Or
          <router-link to="/login" class="font-medium text-blue-600 hover:text-blue-500">
            sign in to your existing account
          </router-link>
        </p>
      </div>
  
      <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form @submit.prevent="handleRegister" class="space-y-6">
            <!-- Full Name -->
            <div>
              <label for="fullName" class="block text-sm font-medium text-gray-700">
                Full Name
              </label>
              <div class="mt-1">
                <input
                  id="fullName"
                  v-model="registerForm.fullName"
                  name="fullName"
                  type="text"
                  autocomplete="name"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="Enter your full name"
                />
              </div>
            </div>
  
            <!-- Email -->
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700">
                Email address
              </label>
              <div class="mt-1">
                <input
                  id="email"
                  v-model="registerForm.email"
                  name="email"
                  type="email"
                  autocomplete="email"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="Enter your email"
                />
              </div>
            </div>
  
            <!-- Phone -->
            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700">
                Phone Number
              </label>
              <div class="mt-1">
                <input
                  id="phone"
                  v-model="registerForm.phone"
                  name="phone"
                  type="tel"
                  autocomplete="tel"
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="Enter your phone number"
                />
              </div>
            </div>
  
            <!-- Address -->
            <div>
              <label for="address" class="block text-sm font-medium text-gray-700">
                Address
              </label>
              <div class="mt-1">
                <textarea
                  id="address"
                  v-model="registerForm.address"
                  name="address"
                  rows="2"
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                  placeholder="Enter your address"
                ></textarea>
              </div>
            </div>
  
            <!-- Password -->
            <div>
              <label for="password" class="block text-sm font-medium text-gray-700">
                Password
              </label>
              <div class="mt-1 relative">
                <input
                  id="password"
                  v-model="registerForm.password"
                  name="password"
                  :type="showPassword ? 'text' : 'password'"
                  autocomplete="new-password"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm pr-10"
                  placeholder="Enter your password"
                />
                <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="absolute inset-y-0 right-0 pr-3 flex items-center"
                >
                  <svg
                    v-if="showPassword"
                    class="h-5 w-5 text-gray-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  <svg
                    v-else
                    class="h-5 w-5 text-gray-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21" />
                  </svg>
                </button>
              </div>
              <!-- Password strength indicator -->
              <div class="mt-2">
                <div class="flex space-x-1">
                  <div
                    v-for="i in 4"
                    :key="i"
                    :class="[
                      'h-1 w-1/4 rounded',
                      getPasswordStrength() >= i ? getPasswordStrengthColor() : 'bg-gray-200'
                    ]"
                  ></div>
                </div>
                <p class="text-xs text-gray-600 mt-1">
                  {{ getPasswordStrengthText() }}
                </p>
              </div>
            </div>
  
            <!-- Confirm Password -->
            <div>
              <label for="confirmPassword" class="block text-sm font-medium text-gray-700">
                Confirm Password
              </label>
              <div class="mt-1 relative">
                <input
                  id="confirmPassword"
                  v-model="registerForm.confirmPassword"
                  name="confirmPassword"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  autocomplete="new-password"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm pr-10"
                  placeholder="Confirm your password"
                />
                <button
                  type="button"
                  @click="showConfirmPassword = !showConfirmPassword"
                  class="absolute inset-y-0 right-0 pr-3 flex items-center"
                >
                  <svg
                    v-if="showConfirmPassword"
                    class="h-5 w-5 text-gray-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  <svg
                    v-else
                    class="h-5 w-5 text-gray-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21" />
                  </svg>
                </button>
              </div>
              <div v-if="registerForm.confirmPassword && !passwordsMatch" class="mt-1">
                <p class="text-xs text-red-600">Passwords do not match</p>
              </div>
            </div>
  
            <!-- Terms and Conditions -->
            <div class="flex items-center">
              <input
                id="terms"
                v-model="registerForm.acceptTerms"
                name="terms"
                type="checkbox"
                required
                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label for="terms" class="ml-2 block text-sm text-gray-900">
                I agree to the
                <a href="#" class="text-blue-600 hover:text-blue-500">Terms and Conditions</a>
                and
                <a href="#" class="text-blue-600 hover:text-blue-500">Privacy Policy</a>
              </label>
            </div>
  
            <!-- Error Message -->
            <div v-if="errorMessage" class="bg-red-50 border border-red-200 rounded-md p-4">
              <div class="flex">
                <svg class="h-5 w-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                </svg>
                <div class="ml-3">
                  <p class="text-sm text-red-800">{{ errorMessage }}</p>
                </div>
              </div>
            </div>
  
            <!-- Success Message -->
            <div v-if="successMessage" class="bg-green-50 border border-green-200 rounded-md p-4">
              <div class="flex">
                <svg class="h-5 w-5 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <div class="ml-3">
                  <p class="text-sm text-green-800">{{ successMessage }}</p>
                </div>
              </div>
            </div>
  
            <!-- Submit Button -->
            <div>
              <button
                type="submit"
                :disabled="authStore.isLoading || !isFormValid"
                class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="authStore.isLoading" class="absolute left-0 inset-y-0 flex items-center pl-3">
                  <svg class="animate-spin h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                </span>
                {{ authStore.isLoading ? 'Creating account...' : 'Create account' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { useAuthStore } from '@/stores/auth'
  
  const router = useRouter()
  const authStore = useAuthStore()
  
  // State
  const showPassword = ref(false)
  const showConfirmPassword = ref(false)
  const errorMessage = ref('')
  const successMessage = ref('')
  
  const registerForm = reactive({
    fullName: '',
    email: '',
    phone: '',
    address: '',
    password: '',
    confirmPassword: '',
    acceptTerms: false
  })
  
  // Computed
  const passwordsMatch = computed(() => {
    return registerForm.password === registerForm.confirmPassword
  })
  
  const isFormValid = computed(() => {
    return registerForm.fullName &&
           registerForm.email &&
           registerForm.password &&
           passwordsMatch.value &&
           registerForm.acceptTerms &&
           getPasswordStrength() >= 2
  })
  
  // Methods
  const getPasswordStrength = () => {
    const password = registerForm.password
    let strength = 0
    
    if (password.length >= 8) strength++
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++
    if (/\d/.test(password)) strength++
    if (/[^a-zA-Z\d]/.test(password)) strength++
    
    return strength
  }
  
  const getPasswordStrengthColor = () => {
    const strength = getPasswordStrength()
    if (strength <= 1) return 'bg-red-500'
    if (strength <= 2) return 'bg-yellow-500'
    if (strength <= 3) return 'bg-blue-500'
    return 'bg-green-500'
  }
  
  const getPasswordStrengthText = () => {
    const strength = getPasswordStrength()
    if (strength <= 1) return 'Weak password'
    if (strength <= 2) return 'Fair password'
    if (strength <= 3) return 'Good password'
    return 'Strong password'
  }
  
  const handleRegister = async () => {
    try {
      errorMessage.value = ''
      successMessage.value = ''
      
      if (!passwordsMatch.value) {
        errorMessage.value = 'Passwords do not match'
        return
      }
      
      const result = await authStore.registerUser({
        fullName: registerForm.fullName,
        email: registerForm.email,
        phone: registerForm.phone,
        address: registerForm.address,
        password: registerForm.password
      })
      
      if (result.success) {
        successMessage.value = 'Account created successfully! Please sign in.'
        
        // Reset form
        Object.keys(registerForm).forEach(key => {
          if (typeof registerForm[key] === 'boolean') {
            registerForm[key] = false
          } else {
            registerForm[key] = ''
          }
        })
        
        // Redirect to login after 2 seconds
        setTimeout(() => {
          router.push('/login')
        }, 2000)
      } else {
        errorMessage.value = result.message || 'Registration failed'
      }
    } catch (error) {
      console.error('Registration error:', error)
      errorMessage.value = 'An unexpected error occurred'
    }
  }
  
  // Lifecycle
  onMounted(() => {
    // Clear any existing messages
    errorMessage.value = ''
    successMessage.value = ''
    
    // If user is already authenticated, redirect them
    if (authStore.isAuthenticated) {
      router.push('/')
    }
  })
  </script>