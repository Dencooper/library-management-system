import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getMe } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  // State
  const accessToken = ref(localStorage.getItem('access_token') || null)
  const user = ref(null)
  const isLoading = ref(false)

  // Getters
  const isAuthenticated = computed(() => !!accessToken.value && !!user.value)

  // Role checking helpers
  const hasRole = (requiredRole) => {
    if (!user.value || !user.value.role) return false
    return user.value.role === requiredRole
  }

  const isUser = computed(() => hasRole('USER'))
  const isLibrarian = computed(() => hasRole('LIBRARIAN'))
  const isAdmin = computed(() => hasRole('ADMIN'))

  // Actions
  const loginUser = async (credentials) => {
    try {
      isLoading.value = true
      const response = await login(credentials)
      
      // Response structure: { access_token, account: { id, email } }
      const { access_token, account } = response.data.data
      
      // Store token
      accessToken.value = access_token
      localStorage.setItem('access_token', access_token)
      
      // Get full user data
      await fetchCurrentUser()
      
      return { success: true }
    } catch (error) {
      console.error('Login error:', error)
      
      // Clear any stored token on login failure
      accessToken.value = null
      localStorage.removeItem('access_token')
      
      return { 
        success: false, 
        message: error.response?.data?.message || 'Login failed' 
      }
    } finally {
      isLoading.value = false
    }
  }

  const registerUser = async (userData) => {
    try {
      isLoading.value = true
      const response = await register(userData)
      
      return { success: true, message: 'Registration successful' }
    } catch (error) {
      console.error('Registration error:', error)
      return { 
        success: false, 
        message: error.response?.data?.message || 'Registration failed' 
      }
    } finally {
      isLoading.value = false
    }
  }

  const fetchCurrentUser = async () => {
    try {
      if (!accessToken.value) return
      
      const response = await getMe()
      
      // Response structure: { id, fullName, email, phone, address, role }
      user.value = response.data.data
      console.log(user.value);
    } catch (error) {
      console.error('Fetch user error:', error)
      // If token is invalid, logout
      if (error.response?.status === 401 || error.response?.status === 403) {
        logout()
      }
    }
  }

  const logout = () => {
    accessToken.value = null
    user.value = null
    localStorage.removeItem('access_token')
  }

  const initializeAuth = async () => {
    if (accessToken.value) {
      await fetchCurrentUser()
    }
  }

  const checkAuthOnStartup = async () => {
    if (accessToken.value) {
      await fetchCurrentUser()
    }
  }

  const updateUser = (userData) => {
    user.value = { ...user.value, ...userData }
  }

  return {
    // State
    accessToken,
    user,
    isLoading,
    
    // Getters
    isAuthenticated,
    isUser,
    isLibrarian,
    isAdmin,
    
    // Actions
    loginUser,
    registerUser,
    fetchCurrentUser,
    logout,
    initializeAuth,
    checkAuthOnStartup,
    updateUser,
    hasRole
  }
})