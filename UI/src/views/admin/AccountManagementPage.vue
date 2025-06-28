<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">User Management</h1>
          <p class="text-gray-600 mt-2">Manage library users and their information</p>
        </div>
        <button
          @click="showAddModal = true"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Add New User
        </button>
      </div>

      <!-- Search and Filters -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-2">Search Users</label>
            <div class="relative">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by name, email, or phone..."
                class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                @input="filterUsers"
              />
              <svg class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>

          <!-- User Type Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">User Type</label>
            <select
              v-model="selectedUserType"
              @change="filterUsers"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Types</option>
              <option value="STUDENT">Student</option>
              <option value="FACULTY">Faculty</option>
              <option value="STAFF">Staff</option>
              <option value="EXTERNAL">External</option>
            </select>
          </div>

          <!-- Status Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
            <select
              v-model="selectedStatus"
              @change="filterUsers"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Status</option>
              <option value="ACTIVE">Active</option>
              <option value="SUSPENDED">Suspended</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Users Table -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">
            Users ({{ filteredUsers.length }})
          </h2>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Users Table -->
        <div v-else-if="filteredUsers.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Contact Info
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Activity
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Registered
                </th>
                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="user in filteredUsers"
                :key="user.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <div class="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                        <span class="text-sm font-medium text-blue-600">{{ user.name.charAt(0).toUpperCase() }}</span>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ user.name }}</div>
                      <div class="text-sm text-gray-500">ID: {{ user.id }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm text-gray-900">{{ user.email }}</div>
                  <div class="text-sm text-gray-500">{{ user.phone || 'No phone' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      getUserTypeClass(user.userType)
                    ]"
                  >
                    {{ user.userType }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      getStatusClass(user.status)
                    ]"
                  >
                    {{ user.status }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm text-gray-900">{{ user.borrowingCount }} borrowings</div>
                  <div class="text-sm text-gray-500">{{ user.activeBorrowings }} active</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ formatDate(user.registeredAt) }}</div>
                  <div class="text-sm text-gray-500">{{ formatDate(user.lastActivity) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end space-x-2">
                    <button
                      @click="viewUser(user)"
                      class="text-blue-600 hover:text-blue-900 bg-blue-50 hover:bg-blue-100 px-2 py-1 rounded text-xs font-medium transition-colors"
                    >
                      View
                    </button>
                    <button
                      @click="editUser(user)"
                      class="text-indigo-600 hover:text-indigo-900 bg-indigo-50 hover:bg-indigo-100 px-2 py-1 rounded text-xs font-medium transition-colors"
                    >
                      Edit
                    </button>
                    <button
                      @click="toggleUserStatus(user)"
                      :class="[
                        'px-2 py-1 rounded text-xs font-medium transition-colors',
                        user.status === 'ACTIVE' 
                          ? 'text-red-600 hover:text-red-900 bg-red-50 hover:bg-red-100' 
                          : 'text-green-600 hover:text-green-900 bg-green-50 hover:bg-green-100'
                      ]"
                    >
                      {{ user.status === 'ACTIVE' ? 'Suspend' : 'Activate' }}
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No users found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Add User Modal -->
    <div v-if="showAddModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
          <form @submit.prevent="saveUser">
            <!-- Modal Header -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-xl font-semibold text-white">
                  Add New User
                </h3>
                <button
                  @click="closeModal"
                  type="button"
                  class="text-white hover:text-gray-200 transition-colors"
                >
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>

            <!-- Modal Body -->
            <div class="bg-white px-6 py-6">
              <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <!-- Left Column - Personal Information -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                      Personal Information
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- Full Name -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Full Name *</label>
                        <input
                          v-model="userForm.name"
                          type="text"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter full name"
                        />
                      </div>

                      <!-- Email -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Email Address *</label>
                        <input
                          v-model="userForm.email"
                          type="email"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter email address"
                        />
                      </div>

                      <!-- Phone -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Phone Number</label>
                        <input
                          v-model="userForm.phone"
                          type="tel"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter phone number"
                        />
                      </div>

                      <!-- Date of Birth -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Date of Birth</label>
                        <input
                          v-model="userForm.dateOfBirth"
                          type="date"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Right Column - Account Information -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                      </svg>
                      Account Information
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- User Type -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">User Type *</label>
                        <select
                          v-model="userForm.userType"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                          <option value="">Select User Type</option>
                          <option value="STUDENT">Student</option>
                          <option value="FACULTY">Faculty</option>
                          <option value="STAFF">Staff</option>
                          <option value="EXTERNAL">External</option>
                        </select>
                      </div>

                      <!-- Student ID / Employee ID -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          {{ userForm.userType === 'STUDENT' ? 'Student ID' : 'Employee ID' }}
                        </label>
                        <input
                          v-model="userForm.idNumber"
                          type="text"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          :placeholder="userForm.userType === 'STUDENT' ? 'Enter student ID' : 'Enter employee ID'"
                        />
                      </div>

                      <!-- Department -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Department</label>
                        <input
                          v-model="userForm.department"
                          type="text"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter department"
                        />
                      </div>

                      <!-- Address -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Address</label>
                        <textarea
                          v-model="userForm.address"
                          rows="3"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter address"
                        ></textarea>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Modal Footer -->
            <div class="bg-gray-50 px-6 py-4 sm:flex sm:flex-row-reverse border-t border-gray-200">
              <button
                type="submit"
                :disabled="isSaving"
                class="w-full inline-flex justify-center rounded-lg border border-transparent shadow-sm px-6 py-3 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-all"
              >
                <svg v-if="isSaving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                {{ isSaving ? 'Adding...' : 'Add User' }}
              </button>
              <button
                @click="closeModal"
                type="button"
                class="mt-3 w-full inline-flex justify-center rounded-lg border border-gray-300 shadow-sm px-6 py-3 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm transition-all"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Edit User Modal -->
    <div v-if="showEditModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
          <form @submit.prevent="saveUser">
            <!-- Modal Header -->
            <div class="bg-gradient-to-r from-indigo-600 to-purple-700 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-xl font-semibold text-white">
                  Edit User
                </h3>
                <button
                  @click="closeModal"
                  type="button"
                  class="text-white hover:text-gray-200 transition-colors"
                >
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>

            <!-- Modal Body - Same as Add Modal but with different header -->
            <div class="bg-white px-6 py-6">
              <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <!-- Left Column - Personal Information -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                      Personal Information
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- Full Name -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Full Name *</label>
                        <input
                          v-model="userForm.name"
                          type="text"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          placeholder="Enter full name"
                        />
                      </div>

                      <!-- Email -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Email Address *</label>
                        <input
                          v-model="userForm.email"
                          type="email"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          placeholder="Enter email address"
                        />
                      </div>

                      <!-- Phone -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Phone Number</label>
                        <input
                          v-model="userForm.phone"
                          type="tel"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          placeholder="Enter phone number"
                        />
                      </div>

                      <!-- Date of Birth -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Date of Birth</label>
                        <input
                          v-model="userForm.dateOfBirth"
                          type="date"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Right Column - Account Information -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                      </svg>
                      Account Information
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- User Type -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">User Type *</label>
                        <select
                          v-model="userForm.userType"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                        >
                          <option value="STUDENT">Student</option>
                          <option value="FACULTY">Faculty</option>
                          <option value="STAFF">Staff</option>
                          <option value="EXTERNAL">External</option>
                        </select>
                      </div>

                      <!-- Student ID / Employee ID -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          {{ userForm.userType === 'STUDENT' ? 'Student ID' : 'Employee ID' }}
                        </label>
                        <input
                          v-model="userForm.idNumber"
                          type="text"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          :placeholder="userForm.userType === 'STUDENT' ? 'Enter student ID' : 'Enter employee ID'"
                        />
                      </div>

                      <!-- Department -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Department</label>
                        <input
                          v-model="userForm.department"
                          type="text"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          placeholder="Enter department"
                        />
                      </div>

                      <!-- Address -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Address</label>
                        <textarea
                          v-model="userForm.address"
                          rows="3"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                          placeholder="Enter address"
                        ></textarea>
                      </div>

                      <!-- Status -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
                        <select
                          v-model="userForm.status"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                        >
                          <option value="ACTIVE">Active</option>
                          <option value="SUSPENDED">Suspended</option>
                          <option value="INACTIVE">Inactive</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Modal Footer -->
            <div class="bg-gray-50 px-6 py-4 sm:flex sm:flex-row-reverse border-t border-gray-200">
              <button
                type="submit"
                :disabled="isSaving"
                class="w-full inline-flex justify-center rounded-lg border border-transparent shadow-sm px-6 py-3 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-all"
              >
                <svg v-if="isSaving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                {{ isSaving ? 'Updating...' : 'Update User' }}
              </button>
              <button
                @click="closeModal"
                type="button"
                class="mt-3 w-full inline-flex justify-center rounded-lg border border-gray-300 shadow-sm px-6 py-3 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm transition-all"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- View User Modal -->
    <div v-if="showViewModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
          <!-- Modal Header -->
          <div class="bg-gradient-to-r from-green-600 to-blue-700 px-6 py-4">
            <div class="flex items-center justify-between">
              <h3 class="text-xl font-semibold text-white">
                User Details
              </h3>
              <button
                @click="closeModal"
                type="button"
                class="text-white hover:text-gray-200 transition-colors"
              >
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Modal Body -->
          <div class="bg-white px-6 py-6" v-if="viewingUser">
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
              <!-- Left Column - Personal Information -->
              <div class="space-y-6">
                <div class="bg-gray-50 rounded-lg p-4">
                  <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                    <svg class="w-5 h-5 mr-2 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    Personal Information
                  </h4>
                  
                  <div class="space-y-3">
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Full Name:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.name }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Email:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.email }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Phone:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.phone || 'Not provided' }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Date of Birth:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.dateOfBirth || 'Not provided' }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Address:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.address || 'Not provided' }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Right Column - Account Information -->
              <div class="space-y-6">
                <div class="bg-gray-50 rounded-lg p-4">
                  <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                    <svg class="w-5 h-5 mr-2 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                    </svg>
                    Account Information
                  </h4>
                  
                  <div class="space-y-3">
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">User ID:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.id }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">User Type:</span>
                      <span :class="getUserTypeClass(viewingUser.userType)" class="px-2 py-1 text-xs rounded-full">
                        {{ viewingUser.userType }}
                      </span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Status:</span>
                      <span :class="getStatusClass(viewingUser.status)" class="px-2 py-1 text-xs rounded-full">
                        {{ viewingUser.status }}
                      </span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">ID Number:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.idNumber || 'Not provided' }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Department:</span>
                      <span class="text-sm text-gray-900">{{ viewingUser.department || 'Not provided' }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Registered:</span>
                      <span class="text-sm text-gray-900">{{ formatDate(viewingUser.registeredAt) }}</span>
                    </div>
                    <div class="flex justify-between">
                      <span class="text-sm font-medium text-gray-500">Last Activity:</span>
                      <span class="text-sm text-gray-900">{{ formatDate(viewingUser.lastActivity) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Library Activity Stats -->
            <div class="mt-8">
              <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                <svg class="w-5 h-5 mr-2 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                Library Activity
              </h4>
              
              <div class="grid grid-cols-3 gap-4">
                <div class="text-center p-4 bg-blue-50 rounded-lg">
                  <div class="text-2xl font-bold text-blue-600">{{ viewingUser.borrowingCount }}</div>
                  <div class="text-sm text-gray-600">Total Borrowings</div>
                </div>
                <div class="text-center p-4 bg-green-50 rounded-lg">
                  <div class="text-2xl font-bold text-green-600">{{ viewingUser.activeBorrowings }}</div>
                  <div class="text-sm text-gray-600">Active Borrowings</div>
                </div>
                <div class="text-center p-4 bg-red-50 rounded-lg">
                  <div class="text-2xl font-bold text-red-600">{{ viewingUser.penalties || 0 }}</div>
                  <div class="text-sm text-gray-600">Penalties</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// State
const users = ref([])
const isLoading = ref(true)
const isSaving = ref(false)
const showAddModal = ref(false)
const showEditModal = ref(false)
const showViewModal = ref(false)
const searchQuery = ref('')
const selectedUserType = ref('')
const selectedStatus = ref('')
const viewingUser = ref(null)

const userForm = reactive({
  id: null,
  name: '',
  email: '',
  phone: '',
  dateOfBirth: '',
  userType: '',
  idNumber: '',
  department: '',
  address: '',
  status: 'ACTIVE'
})

// Computed
const filteredUsers = computed(() => {
  let filtered = users.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(user =>
      user.name.toLowerCase().includes(query) ||
      user.email.toLowerCase().includes(query) ||
      (user.phone && user.phone.includes(query))
    )
  }

  // Filter by user type
  if (selectedUserType.value) {
    filtered = filtered.filter(user => user.userType === selectedUserType.value)
  }

  // Filter by status
  if (selectedStatus.value) {
    filtered = filtered.filter(user => user.status === selectedStatus.value)
  }

  return filtered
})

// Methods
const fetchUsers = async () => {
  try {
    isLoading.value = true
    
    // Mock data
    users.value = [
      {
        id: 1,
        name: 'John Doe',
        email: 'john.doe@university.edu',
        phone: '+1 234 567 8900',
        dateOfBirth: '1995-05-15',
        userType: 'STUDENT',
        idNumber: 'ST2021001',
        department: 'Computer Science',
        address: '123 University Ave, City, State 12345',
        status: 'ACTIVE',
        registeredAt: '2024-01-15T10:30:00Z',
        lastActivity: '2024-12-20T14:22:00Z',
        borrowingCount: 15,
        activeBorrowings: 2,
        penalties: 0
      },
      {
        id: 2,
        name: 'Dr. Sarah Wilson',
        email: 'sarah.wilson@university.edu',
        phone: '+1 234 567 8901',
        dateOfBirth: '1980-08-22',
        userType: 'FACULTY',
        idNumber: 'FC2020015',
        department: 'Mathematics',
        address: '456 Faculty Row, City, State 12345',
        status: 'ACTIVE',
        registeredAt: '2023-08-20T09:15:00Z',
        lastActivity: '2024-12-21T16:45:00Z',
        borrowingCount: 42,
        activeBorrowings: 5,
        penalties: 1
      },
      {
        id: 3,
        name: 'Mike Johnson',
        email: 'mike.johnson@university.edu',
        phone: '+1 234 567 8902',
        dateOfBirth: '1988-12-10',
        userType: 'STAFF',
        idNumber: 'ST2019008',
        department: 'Library Services',
        address: '789 Staff Street, City, State 12345',
        status: 'ACTIVE',
        registeredAt: '2023-11-10T11:20:00Z',
        lastActivity: '2024-12-19T13:30:00Z',
        borrowingCount: 8,
        activeBorrowings: 1,
        penalties: 0
      },
      {
        id: 4,
        name: 'Emily Chen',
        email: 'emily.chen@gmail.com',
        phone: '+1 234 567 8903',
        dateOfBirth: '1992-03-18',
        userType: 'EXTERNAL',
        idNumber: 'EX2024001',
        department: 'External Researcher',
        address: '321 External Blvd, City, State 12345',
        status: 'SUSPENDED',
        registeredAt: '2024-03-05T14:45:00Z',
        lastActivity: '2024-12-10T10:15:00Z',
        borrowingCount: 3,
        activeBorrowings: 0,
        penalties: 2
      },
      {
        id: 5,
        name: 'David Brown',
        email: 'david.brown@university.edu',
        phone: '+1 234 567 8904',
        dateOfBirth: '2000-09-25',
        userType: 'STUDENT',
        idNumber: 'ST2024089',
        department: 'Engineering',
        address: '654 Student Lane, City, State 12345',
        status: 'ACTIVE',
        registeredAt: '2024-09-01T08:00:00Z',
        lastActivity: '2024-12-21T12:00:00Z',
        borrowingCount: 7,
        activeBorrowings: 3,
        penalties: 0
      }
    ]
    
  } catch (error) {
    console.error('Error fetching users:', error)
  } finally {
    isLoading.value = false
  }
}

const getUserTypeClass = (type) => {
  const classes = {
    STUDENT: 'bg-blue-100 text-blue-800',
    FACULTY: 'bg-purple-100 text-purple-800',
    STAFF: 'bg-green-100 text-green-800',
    EXTERNAL: 'bg-gray-100 text-gray-800'
  }
  return classes[type] || 'bg-gray-100 text-gray-800'
}

const getStatusClass = (status) => {
  const classes = {
    ACTIVE: 'bg-green-100 text-green-800',
    SUSPENDED: 'bg-red-100 text-red-800',
    INACTIVE: 'bg-gray-100 text-gray-800'
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const filterUsers = () => {
  // Filtering is handled by computed property
}

const viewUser = (user) => {
  viewingUser.value = user
  showViewModal.value = true
}

const editUser = (user) => {
  userForm.id = user.id
  userForm.name = user.name
  userForm.email = user.email
  userForm.phone = user.phone
  userForm.dateOfBirth = user.dateOfBirth
  userForm.userType = user.userType
  userForm.idNumber = user.idNumber
  userForm.department = user.department
  userForm.address = user.address
  userForm.status = user.status
  
  showEditModal.value = true
}

const saveUser = async () => {
  try {
    isSaving.value = true
    
    if (showAddModal.value) {
      // Add new user
      const newUser = {
        ...userForm,
        id: Date.now(),
        registeredAt: new Date().toISOString(),
        lastActivity: new Date().toISOString(),
        borrowingCount: 0,
        activeBorrowings: 0,
        penalties: 0
      }
      users.value.unshift(newUser)
      
      alert('User added successfully!')
    } else {
      // Update existing user
      const index = users.value.findIndex(u => u.id === userForm.id)
      if (index !== -1) {
        users.value[index] = {
          ...users.value[index],
          ...userForm
        }
      }
      
      alert('User updated successfully!')
    }
    
    closeModal()
    
  } catch (error) {
    console.error('Error saving user:', error)
    alert('Failed to save user')
  } finally {
    isSaving.value = false
  }
}

const toggleUserStatus = async (user) => {
  try {
    const newStatus = user.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE'
    const action = newStatus === 'SUSPENDED' ? 'suspend' : 'activate'
    
    if (confirm(`Are you sure you want to ${action} this user?`)) {
      user.status = newStatus
      alert(`User ${action}d successfully!`)
    }
  } catch (error) {
    console.error('Error updating user status:', error)
    alert('Failed to update user status')
  }
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  showViewModal.value = false
  
  // Reset form
  Object.keys(userForm).forEach(key => {
    if (key === 'id') {
      userForm[key] = null
    } else if (key === 'status') {
      userForm[key] = 'ACTIVE'
    } else {
      userForm[key] = ''
    }
  })
}

// Lifecycle
onMounted(() => {
  fetchUsers()
})
</script>
