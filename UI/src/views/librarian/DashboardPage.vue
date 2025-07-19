<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 py-6">
      <!-- Header -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-900">Dashboard</h1>
        <div class="flex space-x-2">
          <button
            @click="refreshAllData"
            :disabled="isRefreshing"
            class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50 flex items-center"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            {{ isRefreshing ? 'Refreshing...' : 'Refresh' }}
          </button>
          <button
            @click="exportAllToExcel"
            :disabled="isExporting"
            class="px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50 flex items-center"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            {{ isExporting ? 'Exporting...' : 'Export' }}
          </button>
        </div>
      </div>
      <!-- Borrowing Statistics -->
      <div class="bg-white rounded-lg shadow mb-6 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-gray-900">Borrowing Stats</h2>
          <div class="flex space-x-2">
            <button
              @click="refreshBorrowingStats"
              :disabled="borrowingStats.isLoading"
              class="px-2 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200 disabled:opacity-50"
            >
              {{ borrowingStats.isLoading ? 'Loading...' : 'Refresh' }}
            </button>
            <button
              @click="exportBorrowingStats"
              :disabled="borrowingStats.isExporting || !isValidDateRange(borrowingStats.dateRange)"
              class="px-2 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200 disabled:opacity-50"
            >
              {{ borrowingStats.isExporting ? 'Exporting...' : 'Export' }}
            </button>
          </div>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
          <div>
            <label class="block text-sm text-gray-700">From</label>
            <input
              v-model="borrowingStats.dateRange.from"
              type="date"
              @change="validateBorrowingDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(borrowingStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div>
            <label class="block text-sm text-gray-700">To</label>
            <input
              v-model="borrowingStats.dateRange.to"
              type="date"
              @change="validateBorrowingDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(borrowingStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div class="flex items-end">
            <button
              @click="fetchBorrowingStats"
              :disabled="!isValidDateRange(borrowingStats.dateRange)"
              class="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              Apply Filter
            </button>
          </div>
        </div>
        <div v-if="borrowingStats.isLoading" class="flex justify-center py-4">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
        </div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-3 gap-4">
          <div class="bg-blue-50 p-3 rounded">
            <p class="text-sm text-gray-500">Total Borrowings</p>
            <p class="text-xl font-bold text-blue-600">{{ formatNumber(borrowingStats.data.totalBorrowings) }}</p>
          </div>
          <div class="bg-yellow-50 p-3 rounded">
            <p class="text-sm text-gray-500">Active Borrowing</p>
            <p class="text-xl font-bold text-yellow-600">{{ formatNumber(borrowingStats.data.activeBorrowing) }}</p>
          </div>
          <div class="bg-green-50 p-3 rounded">
            <p class="text-sm text-gray-500">On-time Returns</p>
            <p class="text-xl font-bold text-green-600">{{ formatNumber(borrowingStats.data.onTimeReturns) }}</p>
          </div>
          <div class="bg-red-50 p-3 rounded">
            <p class="text-sm text-gray-500">Overdue</p>
            <p class="text-xl font-bold text-red-600">{{ formatNumber(borrowingStats.data.overdueBorrowing) }}</p>
          </div>
          <div class="bg-orange-50 p-3 rounded">
            <p class="text-sm text-gray-500">Late Returns</p>
            <p class="text-xl font-bold text-orange-600">{{ formatNumber(borrowingStats.data.lateReturns) }}</p>
          </div>
          <div class="bg-teal-50 p-3 rounded">
            <p class="text-sm text-gray-500">Avg. Borrow Duration</p>
            <p class="text-xl font-bold text-teal-600">{{ borrowingStats.data.averageBorrowDuration.toFixed(1) }} days</p>
          </div>
        </div>
      </div>
      <!-- Collection Statistics -->
      <div class="bg-white rounded-lg shadow mb-6 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-gray-900">Book Stats</h2>
          <div class="flex space-x-2">
            <button
              @click="refreshCollectionStats"
              :disabled="collectionStats.isLoading"
              class="px-2 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200 disabled:opacity-50"
            >
              {{ collectionStats.isLoading ? 'Loading...' : 'Refresh' }}
            </button>
            <button
              @click="exportCollectionStats"
              :disabled="collectionStats.isExporting || !isValidDateRange(collectionStats.dateRange)"
              class="px-2 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200 disabled:opacity-50"
            >
              {{ collectionStats.isExporting ? 'Exporting...' : 'Export' }}
            </button>
          </div>
        </div>

        <div v-if="collectionStats.isLoading" class="flex justify-center py-4">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
        </div>
        <div v-else>
          <div class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-5 gap-4 mb-6">
            <div class="bg-blue-50 p-3 rounded">
              <p class="text-sm text-gray-500">Total Books</p>
              <p class="text-xl font-bold text-blue-600">{{ formatNumber(collectionStats.data.totalBooks) }}</p>
            </div>
            <div class="bg-green-50 p-3 rounded">
              <p class="text-sm text-gray-500">Available Books</p>
              <p class="text-xl font-bold text-green-600">{{ formatNumber(collectionStats.data.availableBooks) }}</p>
            </div>
            <div class="bg-yellow-50 p-3 rounded">
              <p class="text-sm text-gray-500">Borrowed Books</p>
              <p class="text-xl font-bold text-yellow-600">{{ formatNumber(collectionStats.data.borrowedBooks) }}</p>
            </div>
            <div class="bg-red-50 p-3 rounded">
              <p class="text-sm text-gray-500">Damaged Books</p>
              <p class="text-xl font-bold text-red-600">{{ formatNumber(collectionStats.data.damagedBooks) }}</p>
            </div>
            <div class="bg-gray-200 p-3 rounded">
              <p class="text-sm text-gray-500">Lost Books</p>
              <p class="text-xl font-bold text-gray-700">{{ formatNumber(collectionStats.data.lostBooks) }}</p>
            </div>
            <div class="bg-blue-50 p-3 rounded">
              <p class="text-sm text-gray-500">Availability Rate</p>
              <p class="text-xl font-bold text-blue-600">{{ collectionStats.data.availabilityRate.toFixed(1) }}%</p>
            </div>
            <div class="bg-teal-50 p-3 rounded">
              <p class="text-sm text-gray-500">Utilization Rate</p>
              <p class="text-xl font-bold text-teal-600">{{ collectionStats.data.utilizationRate.toFixed(1) }}%</p>
            </div>
            <div class="bg-pink-50 p-3 rounded">
              <p class="text-sm text-gray-500">Total Categories</p>
              <p class="text-xl font-bold text-pink-600">20</p>
            </div>
            <div class="bg-indigo-50 p-3 rounded">
              <p class="text-sm text-gray-500">Total Authors</p>
              <p class="text-xl font-bold text-indigo-600">30</p>
            </div>

          </div>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
            <div>
              <label class="block text-sm text-gray-700">From</label>
              <input
                v-model="collectionStats.dateRange.from"
                type="date"
                @change="validateCollectionDateRange"
                :class="['w-full p-2 border rounded', isValidDateRange(collectionStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
              />
            </div>
            <div>
              <label class="block text-sm text-gray-700">To</label>
              <input
                v-model="collectionStats.dateRange.to"
                type="date"
                @change="validateCollectionDateRange"
                :class="['w-full p-2 border rounded', isValidDateRange(collectionStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
              />
            </div>
            <div class="flex items-end">
              <button
                @click="fetchCollectionStats"
                :disabled="!isValidDateRange(collectionStats.dateRange)"
                class="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
              >
                Apply Filter
              </button>
            </div>
          </div>
          <h3 class="text-md font-semibold mb-2">Popular Books (Top 10)</h3>
          <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow mb-6">
            <thead>
              <tr class="border-b bg-gray-50">
                <th class="text-left py-2 px-3 text-gray-700">Rank</th>
                <th class="text-left py-2 px-3 text-gray-700">Title</th>
                <th class="text-left py-2 px-3 text-gray-700">Author</th>
                <th class="text-center py-2 px-3 text-gray-700">Borrows</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(book, index) in collectionStats.data.popularBooks" :key="book.id" class="border-b hover:bg-gray-50">
                <td class="py-2 px-3">{{ index + 1 }}</td>
                <td class="py-2 px-3">{{ book.title }}</td>
                <td class="py-2 px-3">{{ book.author }}</td>
                <td class="py-2 px-3 text-center">{{ book.borrowCount }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- User Activity Statistics -->
      <div class="bg-white rounded-lg shadow mb-6 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-gray-900">User Activity</h2>
          <div class="flex space-x-2">
            <button
              @click="refreshUserStats"
              :disabled="userStats.isLoading"
              class="px-2 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200 disabled:opacity-50"
            >
              {{ userStats.isLoading ? 'Loading...' : 'Refresh' }}
            </button>
            <button
              @click="exportUserStats"
              :disabled="userStats.isExporting || !isValidDateRange(userStats.dateRange)"
              class="px-2 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200 disabled:opacity-50"
            >
              {{ userStats.isExporting ? 'Exporting...' : 'Export' }}
            </button>
          </div>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
          <div>
            <label class="block text-sm text-gray-700">From</label>
            <input
              v-model="userStats.dateRange.from"
              type="date"
              @change="validateUserDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(userStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div>
            <label class="block text-sm text-gray-700">To</label>
            <input
              v-model="userStats.dateRange.to"
              type="date"
              @change="validateUserDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(userStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div class="flex items-end">
            <button
              @click="fetchUserStats"
              :disabled="!isValidDateRange(userStats.dateRange)"
              class="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              Apply Filter
            </button>
          </div>
        </div>
        <div v-if="userStats.isLoading" class="flex justify-center py-4">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-green-600"></div>
        </div>
        <div v-else>
          <div class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-4 gap-4 mb-6">
            <div class="bg-green-50 p-3 rounded">
              <p class="text-sm text-gray-500">Active Users</p>
              <p class="text-xl font-bold text-green-600">{{ formatNumber(userStats.data.activeUsers) }}</p>
            </div>
            <div class="bg-blue-50 p-3 rounded">
              <p class="text-sm text-gray-500">Total Users</p>
              <p class="text-xl font-bold text-blue-600">{{ formatNumber(userStats.data.totalUsers) }}</p>
            </div>
            <div class="bg-yellow-50 p-3 rounded">
              <p class="text-sm text-gray-500">New Registrations</p>
              <p class="text-xl font-bold text-yellow-600">{{ formatNumber(userStats.data.newRegistrations) }}</p>
            </div>
            <div class="bg-teal-50 p-3 rounded">
              <p class="text-sm text-gray-500">Avg Borrows/User</p>
              <p class="text-xl font-bold text-teal-600">{{ userStats.data.avgBorrowsPerUser.toFixed(1) }}</p>
            </div>
          </div>

          <h3 class="text-md font-semibold mb-2">Top Active Users (Anonymized)</h3>
          <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow">
            <thead>
              <tr class="border-b bg-gray-50">
                <th class="text-left py-2 px-3 text-gray-700">ID</th>
                <th class="text-left py-2 px-3 text-gray-700">Email</th>
                <th class="text-left py-2 px-3 text-gray-700">Full Name</th>
                <th class="text-center py-2 px-3 text-gray-700">Borrows</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in userStats.data.topActiveUsers" :key="user.userId" class="border-b hover:bg-gray-50">
                <td class="py-2 px-3">{{ user.userId }}</td>
                <td class="py-2 px-3">{{ user.email }}</td>
                <td class="py-2 px-3">{{ user.fullName }}</td>
                <td class="py-2 px-3 text-center">{{ user.borrowCount }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- Financial Statistics -->
      <div class="bg-white rounded-lg shadow mb-6 p-4">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-gray-900">Financial Stats</h2>
          <div class="flex space-x-2">
            <button
              @click="refreshFinancialStats"
              :disabled="financialStats.isLoading"
              class="px-2 py-1 text-sm bg-blue-100 text-blue-700 rounded hover:bg-blue-200 disabled:opacity-50"
            >
              {{ financialStats.isLoading ? 'Loading...' : 'Refresh' }}
            </button>
            <button
              @click="exportFinancialStats"
              :disabled="financialStats.isExporting || !isValidDateRange(financialStats.dateRange)"
              class="px-2 py-1 text-sm bg-green-100 text-green-700 rounded hover:bg-green-200 disabled:opacity-50"
            >
              {{ financialStats.isExporting ? 'Exporting...' : 'Export' }}
            </button>
          </div>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
          <div>
            <label class="block text-sm text-gray-700">From</label>
            <input
              v-model="financialStats.dateRange.from"
              type="date"
              @change="validateFinancialDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(financialStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div>
            <label class="block text-sm text-gray-700">To</label>
            <input
              v-model="financialStats.dateRange.to"
              type="date"
              @change="validateFinancialDateRange"
              :class="['w-full p-2 border rounded', isValidDateRange(financialStats.dateRange) ? 'border-gray-300' : 'border-red-300']"
            />
          </div>
          <div class="flex items-end">
            <button
              @click="fetchFinancialStats"
              :disabled="!isValidDateRange(financialStats.dateRange)"
              class="w-full p-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            >
              Apply Filter
            </button>
          </div>
        </div>
        <div v-if="financialStats.isLoading" class="flex justify-center py-4">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-red-600"></div>
        </div>
        <div v-else>
          <div class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-4 gap-4 mb-6">
            <div class="bg-red-50 p-3 rounded">
              <p class="text-sm text-gray-500">Total Penalties</p>
              <p class="text-xl font-bold text-red-600">${{ formatNumber(financialStats.data.totalPenalties) }}</p>
            </div>
            <div class="bg-green-50 p-3 rounded">
              <p class="text-sm text-gray-500">Collected</p>
              <p class="text-xl font-bold text-green-600">${{ formatNumber(financialStats.data.collectedPenalties) }}</p>
            </div>
            <div class="bg-orange-50 p-3 rounded">
              <p class="text-sm text-gray-500">Outstanding</p>
              <p class="text-xl font-bold text-orange-600">${{ formatNumber(financialStats.data.outstandingPenalties) }}</p>
            </div>
            <div class="bg-blue-50 p-3 rounded">
              <p class="text-sm text-gray-500">Penalty Count</p>
              <p class="text-xl font-bold text-blue-600">{{ formatNumber(financialStats.data.penaltyCount) }}</p>
            </div>
            <div class="bg-blue-50 p-3 rounded">
              <p class="text-sm text-gray-500">Outstanding Count</p>
              <p class="text-xl font-bold text-blue-600">{{ formatNumber(financialStats.data.outstandingCount) }}</p>
            </div>
            <div class="bg-teal-50 p-3 rounded">
              <p class="text-sm text-gray-500">Collection Rate</p>
              <p class="text-xl font-bold text-teal-600">{{ financialStats.data.collectionRate.toFixed(1) }}%</p>
            </div>
            <div class="bg-pink-50 p-3 rounded">
              <p class="text-sm text-gray-500">Avg. Penalty Amount</p>
              <p class="text-xl font-bold text-pink-600">${{ financialStats.data.avgPenaltyAmount.toFixed(2) }}</p>
            </div>
          </div>

          <h3 class="text-md font-semibold mb-2">Penalty Breakdown by Type</h3>
          <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-6">
            <div class="bg-gray-100 p-3 rounded">
              <p class="text-sm text-gray-500">Damage Penalty Count</p>
              <p class="text-lg font-bold text-gray-800">{{ financialStats.data.penaltyBreakdown.damageCount }}</p>
            </div>
            <div class="bg-gray-100 p-3 rounded">
              <p class="text-sm text-gray-500">Damage Fees</p>
              <p class="text-lg font-bold text-gray-800">${{ formatNumber(financialStats.data.penaltyBreakdown.damageFees) }}</p>
            </div>
            <div class="bg-gray-100 p-3 rounded">
              <p class="text-sm text-gray-500">Lost Penalty Count</p>
              <p class="text-lg font-bold text-gray-800">{{financialStats.data.penaltyBreakdown.lostCount }}</p>
            </div>
            <div class="bg-gray-100 p-3 rounded">
              <p class="text-sm text-gray-500">Lost Book Fees</p>
              <p class="text-lg font-bold text-gray-800">${{ formatNumber(financialStats.data.penaltyBreakdown.lostBookFees) }}</p>
            </div>
          </div>

          <h3 class="text-md font-semibold mb-2">Monthly Financial Data</h3>
          <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow">
            <thead>
              <tr class="border-b bg-gray-50">
                <th class="text-left py-2 px-3 text-gray-700">Month</th>
                <th class="text-left py-2 px-3 text-gray-700">Total Penalties</th>
                <th class="text-left py-2 px-3 text-gray-700">Collected</th>
                <th class="text-left py-2 px-3 text-gray-700">Outstanding</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="monthData in financialStats.data.monthlyData" :key="monthData.month" class="border-b hover:bg-gray-50">
                <td class="py-2 px-3">{{ monthData.month }}</td>
                <td class="py-2 px-3">${{ formatNumber(monthData.totalPenalties) }}</td>
                <td class="py-2 px-3">${{ formatNumber(monthData.collectedPenalties) }}</td>
                <td class="py-2 px-3">${{ formatNumber(monthData.outstandingPenalties) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- Messages -->
      <div v-if="message" class="fixed bottom-4 right-4 bg-green-500 text-white px-4 py-2 rounded shadow">
        {{ message }}
      </div>
      <div v-if="errorMessage" class="fixed bottom-4 right-4 bg-red-500 text-white px-4 py-2 rounded shadow">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as XLSX from 'xlsx'
import { getBorrowingStatistics, getBookStatistics, getUserStatistics, getPenaltyStatistics } from '@/api'

// Global state
const isRefreshing = ref(false)
const isExporting = ref(false)
const message = ref('')
const errorMessage = ref('')

// Reactive states
const borrowingStats = reactive({
  isLoading: false,
  isExporting: false,
  dateRange: { from: '', to: '' },
  data: {
    totalBorrowings: 0, onTimeReturns: 0, overdueBorrowing: 0,
    activeBorrowing: 0, lateReturns: 0,
    averageBorrowDuration: 0
  }
})
const collectionStats = reactive({
  isLoading: false,
  isExporting: false,
  dateRange: { from: '', to: '' },
  data: {
    totalBooks: 0, availableBooks: 0, borrowedBooks: 0, damagedBooks: 0, lostBooks: 0,
    popularBooks: [],
    availabilityRate: 0, utilizationRate: 0
  }
})
const userStats = reactive({
  isLoading: false,
  isExporting: false,
  dateRange: { from: '', to: '' },
  data: {
    activeUsers: 0, totalUsers: 0, newRegistrations: 0,
    avgBorrowsPerUser: 0,
    topActiveUsers: []
  }
})
const financialStats = reactive({
  isLoading: false,
  isExporting: false,
  dateRange: { from: '', to: '' },
  data: {
    totalPenalties: 0, collectedPenalties: 0, outstandingPenalties: 0,
    penaltyCount: 0, outstandingCount: 0, collectionRate: 0,
    penaltyBreakdown: { damageCount: 0, damageFees: 0, lostCount: 0, lostBookFees: 0},
    monthlyData: [], avgPenaltyAmount: 0
  }
})

// Utility functions
const formatNumber = (num) => new Intl.NumberFormat().format(num)
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
};
const isValidDateRange = (dateRange) => {
  if (!dateRange.from || !dateRange.to) return true
  return new Date(dateRange.from) <= new Date(dateRange.to)
}

// Borrowing Statistics Methods
const validateBorrowingDateRange = () => {
  if (!isValidDateRange(borrowingStats.dateRange)) {
    errorMessage.value = 'Invalid date range for Borrowing Stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } else {
    errorMessage.value = ''
  }
}
const fetchBorrowingStats = async () => {
  if (!isValidDateRange(borrowingStats.dateRange)) return
  borrowingStats.isLoading = true
  try {
    const response = await getBorrowingStatistics({
      from: borrowingStats.dateRange.from,
      to: borrowingStats.dateRange.to
    })
    if (response.data) {
      borrowingStats.data = response.data.data;
    }
  } catch (error) {
    console.error('Error fetching borrowing statistics:', error)
    errorMessage.value = 'Failed to load borrowing stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    borrowingStats.isLoading = false
  }
}
const refreshBorrowingStats = () => fetchBorrowingStats()
const exportBorrowingStats = async () => {
  if (!isValidDateRange(borrowingStats.dateRange)) return
  borrowingStats.isExporting = true
  try {
    const workbook = XLSX.utils.book_new()
    const data = [
      ['Borrowing Statistics'],
      ['Period', `${borrowingStats.dateRange.from} to ${borrowingStats.dateRange.to}`],
      ['Total Borrowings', borrowingStats.data.totalBorrowings],
      ['On-time Returns', borrowingStats.data.onTimeReturns],
      ['Late Returns', borrowingStats.data.lateReturns],
      ['Overdue Borrowing', borrowingStats.data.overdueBorrowing],
      ['Active Borrowing', borrowingStats.data.activeBorrowing],
      ['Average Borrow Duration', `${borrowingStats.data.averageBorrowDuration.toFixed(1)} days`],
    ]
    const sheet = XLSX.utils.aoa_to_sheet(data)
    XLSX.utils.book_append_sheet(workbook, sheet, 'Borrowing')
    XLSX.writeFile(workbook, `borrowing-stats-${borrowingStats.dateRange.from}.xlsx`)
    message.value = 'Borrowing stats exported!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error exporting borrowing statistics:', error)
    errorMessage.value = 'Failed to export borrowing stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    borrowingStats.isExporting = false
  }
}

// Collection Statistics Methods
const validateCollectionDateRange = () => {
  if (!isValidDateRange(collectionStats.dateRange)) {
    errorMessage.value = 'Invalid date range for Collection Stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } else {
    errorMessage.value = ''
  }
}
const fetchCollectionStats = async () => {
  if (!isValidDateRange(collectionStats.dateRange)) return
  collectionStats.isLoading = true
  try {
    const response = await getBookStatistics({
      from: collectionStats.dateRange.from,
      to: collectionStats.dateRange.to
    })
    console.log(collectionStats.dateRange.from);
    if (response.data) collectionStats.data = response.data.data
  } catch (error) {
    console.error('Error fetching collection statistics:', error)
    errorMessage.value = 'Failed to load collection stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    collectionStats.isLoading = false
  }
}
const refreshCollectionStats = () => fetchCollectionStats()
const exportCollectionStats = async () => {
  if (!isValidDateRange(collectionStats.dateRange)) return
  collectionStats.isExporting = true
  try {
    const workbook = XLSX.utils.book_new()
    const data = [
      ['Collection Statistics'],
      ['Period', `${collectionStats.dateRange.from} to ${collectionStats.dateRange.to}`],
      ['Total Books', collectionStats.data.totalBooks],
      ['Available Books', collectionStats.data.availableBooks],
      ['Borrowed Books', collectionStats.data.borrowedBooks],
      ['Damaged Books', collectionStats.data.damagedBooks],
      ['Lost Books', collectionStats.data.lostBooks],
      ['Availability Rate', `${collectionStats.data.availabilityRate.toFixed(1)}%`],
      ['Utilization Rate', `${collectionStats.data.utilizationRate.toFixed(1)}%`],
      ['Total Categories', collectionStats.data.totalCategories],
      ['Total Authors', collectionStats.data.totalAuthors],
      [''],
      ['Popular Books'],
      ['Rank', 'Title', 'Author', 'Borrows'],
      ...collectionStats.data.popularBooks.map((book, index) => [index + 1, book.title, book.author, book.borrowCount]),
      [''],
    ]
    const sheet = XLSX.utils.aoa_to_sheet(data)
    XLSX.utils.book_append_sheet(workbook, sheet, 'Collection')
    XLSX.writeFile(workbook, `collection-stats-${collectionStats.dateRange.from}.xlsx`)
    message.value = 'Collection stats exported!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error exporting collection statistics:', error)
    errorMessage.value = 'Failed to export collection stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    collectionStats.isExporting = false
  }
}

// User Activity Statistics Methods
const validateUserDateRange = () => {
  if (!isValidDateRange(userStats.dateRange)) {
    errorMessage.value = 'Invalid date range for User Stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } else {
    errorMessage.value = ''
  }
}
const fetchUserStats = async () => {
  if (!isValidDateRange(userStats.dateRange)) return
  userStats.isLoading = true
  try {
    const response = await getUserStatistics({
      from: userStats.dateRange.from,
      to: userStats.dateRange.to
    })
    if (response.data) userStats.data = response.data.data
  } catch (error) {
    console.error('Error fetching user statistics:', error)
    errorMessage.value = 'Failed to load user stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    userStats.isLoading = false
  }
}
const refreshUserStats = () => fetchUserStats()
const exportUserStats = async () => {
  if (!isValidDateRange(userStats.dateRange)) return
  userStats.isExporting = true
  try {
    const workbook = XLSX.utils.book_new()
    const data = [
      ['User Activity Statistics'],
      ['Period', `${userStats.dateRange.from} to ${userStats.dateRange.to}`],
      ['Active Users', userStats.data.activeUsers],
      ['Total Users', userStats.data.totalUsers],
      ['New Registrations', userStats.data.newRegistrations],
      ['Library Visits', userStats.data.libraryVisits],
      ['Avg Borrows/User', userStats.data.avgBorrowsPerUser.toFixed(1)],
      ['Avg Visits/User', userStats.data.avgVisitsPerUser.toFixed(1)],
      [''],
      ['User Demographics'],
      ['Students', userStats.data.userDemographics.students],
      ['Faculty', userStats.data.userDemographics.faculty],
      ['Staff', userStats.data.userDemographics.staff],
      ['External', userStats.data.userDemographics.external],
      [''],
      ['Top Active Users'],
      ['User ID', 'Type', 'Borrows', 'Visits'],
      ...userStats.data.topActiveUsers.map(user => [user.userId, user.userType, user.borrowCount, user.visitCount]),
      [''],
      ['Peak Usage Days', userStats.data.peakUsageDays.join(', ')],
      ['Peak Usage Hours', userStats.data.peakUsageHours.map(h => `${h}:00`).join(', ')]
    ]
    const sheet = XLSX.utils.aoa_to_sheet(data)
    XLSX.utils.book_append_sheet(workbook, sheet, 'User Activity')
    XLSX.writeFile(workbook, `user-stats-${userStats.dateRange.from}.xlsx`)
    message.value = 'User stats exported!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error exporting user statistics:', error)
    errorMessage.value = 'Failed to export user stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    userStats.isExporting = false
  }
}

// Financial Statistics Methods
const validateFinancialDateRange = () => {
  if (!isValidDateRange(financialStats.dateRange)) {
    errorMessage.value = 'Invalid date range for Financial Stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } else {
    errorMessage.value = ''
  }
}
const fetchFinancialStats = async () => {
  if (!isValidDateRange(financialStats.dateRange)) return
  financialStats.isLoading = true
  try {
    const response = await getPenaltyStatistics({
      from: financialStats.dateRange.from,
      to: financialStats.dateRange.to
    })
    if (response.data) financialStats.data = response.data.data
  } catch (error) {
    console.error('Error fetching financial statistics:', error)
    errorMessage.value = 'Failed to load financial stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    financialStats.isLoading = false
  }
}
const refreshFinancialStats = () => fetchFinancialStats()
const exportFinancialStats = async () => {
  if (!isValidDateRange(financialStats.dateRange)) return
  financialStats.isExporting = true
  try {
    const workbook = XLSX.utils.book_new()
    const data = [
      ['Financial Statistics'],
      ['Period', `${financialStats.dateRange.from} to ${financialStats.dateRange.to}`],
      ['Total Penalties', `$${financialStats.data.totalPenalties}`],
      ['Collected', `$${financialStats.data.collectedPenalties}`],
      ['Outstanding', `$${financialStats.data.outstandingPenalties}`],
      ['Penalty Count', financialStats.data.penaltyCount],
      ['Outstanding Count', financialStats.data.outstandingCount],
      ['Collection Rate', `${financialStats.data.collectionRate.toFixed(1)}%`],
      ['Average Penalty Amount', `$${financialStats.data.avgPenaltyAmount.toFixed(2)}`],
      [''],
      ['Penalty Breakdown by Type'],
      ['Late Return Fees', `$${financialStats.data.penaltyBreakdown.lateReturnFees}`],
      ['Damage Fees', `$${financialStats.data.penaltyBreakdown.damageFees}`],
      ['Lost Book Fees', `$${financialStats.data.penaltyBreakdown.lostBookFees}`],
      ['Other Fees', `$${financialStats.data.penaltyBreakdown.otherFees}`],
      [''],
      ['Monthly Financial Data'],
      ['Month', 'Total Penalties', 'Collected', 'Outstanding'],
      ...financialStats.data.monthlyData.map(m => [m.month, `$${m.totalPenalties}`, `$${m.collectedPenalties}`, `$${m.outstandingPenalties}`])
    ]
    const sheet = XLSX.utils.aoa_to_sheet(data)
    XLSX.utils.book_append_sheet(workbook, sheet, 'Financial')
    XLSX.writeFile(workbook, `financial-stats-${financialStats.dateRange.from}.xlsx`)
    message.value = 'Financial stats exported!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error exporting financial statistics:', error)
    errorMessage.value = 'Failed to export financial stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    financialStats.isExporting = false
  }
}

// Global Methods
const refreshAllData = async () => {
  isRefreshing.value = true
  try {
    await Promise.all([
      fetchBorrowingStats(),
      fetchCollectionStats(),
      fetchUserStats(),
      fetchFinancialStats()
    ])
    message.value = 'All stats refreshed!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error refreshing all data:', error)
    errorMessage.value = 'Failed to refresh stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    isRefreshing.value = false
  }
}
const exportAllToExcel = async () => {
  const invalidRanges = []
  if (!isValidDateRange(borrowingStats.dateRange)) invalidRanges.push('Borrowing')
  if (!isValidDateRange(collectionStats.dateRange)) invalidRanges.push('Collection')
  if (!isValidDateRange(userStats.dateRange)) invalidRanges.push('User')
  if (!isValidDateRange(financialStats.dateRange)) invalidRanges.push('Financial')
  if (invalidRanges.length > 0) {
    errorMessage.value = `Invalid date ranges in: ${invalidRanges.join(', ')}`
    setTimeout(() => errorMessage.value = '', 2000)
    return
  }
  isExporting.value = true
  try {
    const workbook = XLSX.utils.book_new()
    const summaryData = [
      ['Library Statistics'],
      ['Period', `${borrowingStats.dateRange.from} to ${borrowingStats.dateRange.to}`],
      [''],
      ['Borrowing Stats'],
      ['Total Borrowings', borrowingStats.data.totalBorrowings],
      ['On-time Returns', borrowingStats.data.onTimeReturns],
      ['Late Returns', borrowingStats.data.lateReturns],
      ['Overdue Borrowing', borrowingStats.data.overdueBorrowing],
      ['Active Borrowing', borrowingStats.data.activeBorrowing],
      ['Average Borrow Duration', `${borrowingStats.data.averageBorrowDuration.toFixed(1)} days`],
      [''],
      ['Collection Stats'],
      ['Total Books', collectionStats.data.totalBooks],
      ['Available Books', collectionStats.data.availableBooks],
      ['Borrowed Books', collectionStats.data.borrowedBooks],
      ['Damaged Books', collectionStats.data.damagedBooks],
      ['Lost Books', collectionStats.data.lostBooks],
      ['Availability Rate', `${collectionStats.data.availabilityRate.toFixed(1)}%`],
      ['Utilization Rate', `${collectionStats.data.utilizationRate.toFixed(1)}%`],
      ['Total Categories', collectionStats.data.totalCategories],
      ['Total Authors', collectionStats.data.totalAuthors],
      [''],
      ['Popular Books'],
      ['Rank', 'Title', 'Author', 'Borrows'],
      ...collectionStats.data.popularBooks.map((book, index) => [index + 1, book.title, book.author, book.borrowCount]),
      [''],
      ['User Stats'],
      ['Active Users', userStats.data.activeUsers],
      ['Total Users', userStats.data.totalUsers],
      ['New Registrations', userStats.data.newRegistrations],
      ['Avg Borrows/User', userStats.data.avgBorrowsPerUser.toFixed(1)],
      [''],
      ['Top Active Users'],
      ['User ID', 'Email', 'Full Name', 'Borrows'],
      ...userStats.data.topActiveUsers.map(user => [user.userId, user.email, , user.fullName, user.borrowCount]),
      [''],
      ['Financial Stats'],
      ['Total Penalties', `$${financialStats.data.totalPenalties}`],
      ['Collected', `$${financialStats.data.collectedPenalties}`],
      ['Outstanding', `$${financialStats.data.outstandingPenalties}`],
      ['Penalty Count', financialStats.data.penaltyCount],
      ['Outstanding Count', financialStats.data.outstandingCount],
      ['Collection Rate', `${financialStats.data.collectionRate.toFixed(1)}%`],
      ['Average Penalty Amount', `$${financialStats.data.avgPenaltyAmount.toFixed(2)}`],
      [''],
      ['Penalty Breakdown by Type'],
      ['Damage Penalty Count', `$${financialStats.data.penaltyBreakdown.damageCount}`],
      ['Damage Fees', `$${financialStats.data.penaltyBreakdown.damageFees}`],
      ['Lost Penalty Count', `$${financialStats.data.penaltyBreakdown.lostCount}`],
      ['Lost Book Fees', `$${financialStats.data.penaltyBreakdown.lostBookFees}`],
      [''],
      ['Monthly Financial Data'],
      ['Month', 'Total Penalties', 'Collected', 'Outstanding'],
      ...financialStats.data.monthlyData.map(m => [m.month, `$${m.totalPenalties}`, `$${m.collectedPenalties}`, `$${m.outstandingPenalties}`])
    ]
    const sheet = XLSX.utils.aoa_to_sheet(summaryData)
    XLSX.utils.book_append_sheet(workbook, sheet, 'Summary')
    XLSX.writeFile(workbook, `library-stats-${new Date().toISOString().split('T')[0]}.xlsx`)
    message.value = 'All stats exported!'
    setTimeout(() => message.value = '', 2000)
  } catch (error) {
    console.error('Error exporting all statistics:', error)
    errorMessage.value = 'Failed to export stats'
    setTimeout(() => errorMessage.value = '', 2000)
  } finally {
    isExporting.value = false
  }
}

// Initialize
onMounted(() => {
  const today = new Date();
  const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
  const lastDayOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

  const formattedFirstDay = formatDate(firstDayOfMonth);
  const formattedLastDay = formatDate(lastDayOfMonth);

  borrowingStats.dateRange.from = formattedFirstDay;
  borrowingStats.dateRange.to = formattedLastDay;

  collectionStats.dateRange.from = formattedFirstDay;
  collectionStats.dateRange.to = formattedLastDay;

  userStats.dateRange.from = formattedFirstDay;
  userStats.dateRange.to = formattedLastDay;

  financialStats.dateRange.from = formattedFirstDay;
  financialStats.dateRange.to = formattedLastDay;

  refreshAllData();
})
</script>
