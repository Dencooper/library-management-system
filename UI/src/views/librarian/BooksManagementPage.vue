<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Book Management</h1>
          <p class="text-gray-600 mt-2">Manage your library's book collection</p>
        </div>
        <button
          @click="showAddModal = true"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors flex items-center"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
          </svg>
          Add New Book
        </button>
      </div>

      <!-- Search and Filters -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-2">Search Books</label>
            <div class="relative">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by title, author, or ISBN..."
                class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                @input="filterBooks"
              />
              <svg class="absolute left-3 top-2.5 h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>

          <!-- Category Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Category</label>
            <select
              v-model="selectedCategory"
              @change="filterBooks"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Categories</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>

          <!-- Author Filter -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Author</label>
            <select
              v-model="selectedAuthor"
              @change="filterBooks"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Authors</option>
              <option v-for="author in authors" :key="author.id" :value="author.id">
                {{ author.name }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <!-- Books Table -->
      <div class="bg-white rounded-lg shadow-lg overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h2 class="text-lg font-semibold text-gray-900">
            Books ({{ filteredBooks.length }})
          </h2>
        </div>

        <!-- Loading -->
        <div v-if="isLoading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>

        <!-- Books Table -->
        <div v-else-if="filteredBooks.length > 0" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cover
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Book Info
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Author
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Categories
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Shelf
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Price
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
                v-for="book in filteredBooks"
                :key="book.id"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex-shrink-0 h-20 w-14">
                    <img
                      :src="book.imageUrl || '/placeholder.svg?height=80&width=56'"
                      :alt="book.title"
                      class="h-20 w-14 object-cover rounded shadow-sm"
                    />
                  </div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm font-medium text-gray-900">{{ book.title }}</div>
                  <div class="text-sm text-gray-500">ISBN: {{ book.isbn }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ book.author.name }}</div>
                </td>
                <td class="px-6 py-4">
                  <div class="flex flex-wrap gap-1">
                    <span
                      v-for="category in book.categories.slice(0, 2)"
                      :key="category.id"
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                    >
                      {{ category.name }}
                    </span>
                    <span v-if="book.categories.length > 2" class="text-xs text-gray-500">
                      +{{ book.categories.length - 2 }} more
                    </span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ book.shelf.code }}</div>
                  <div class="text-xs text-gray-500">{{ book.shelf.location }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">${{ book.price?.toFixed(2) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                      book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ book.isAvailable ? 'Available' : 'Not Available' }}
                  </span>
                </td>
                <td class="px-3 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex items-center justify-end">
                    <router-link
                      :to="`/librarian/book-items?bookTitle=${book.title}`"
                      class="text-blue-600 hover:text-blue-900 px-1 py-1 rounded font-medium transition-colors"
                    >
                      Item
                    </router-link>
                    <button
                      @click="editBook(book)"
                      class="text-indigo-600 hover:text-indigo-900 px-1 py-1 rounded font-medium transition-colors"
                    >
                      Edit
                    </button>
                    <button
                      @click="deleteBook(book)"
                      class="text-red-600 hover:text-red-900 px-1 py-1 rounded font-medium transition-colors"
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 class="text-lg font-medium text-gray-900 mb-2">No books found</h3>
          <p class="text-gray-600">Try adjusting your search criteria</p>
        </div>
      </div>
    </div>

    <!-- Add/Edit Book Modal -->
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-6xl sm:w-full">
          <form @submit.prevent="saveBook" enctype="multipart/form-data">
            <!-- Modal Header -->
            <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-xl font-semibold text-white">
                  {{ showAddModal ? 'Add New Book' : 'Edit Book' }}
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
              <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- Left Column - Book Details -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                      </svg>
                      Book Information
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- Title -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Title *</label>
                        <input
                          v-model="bookForm.title"
                          type="text"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter book title"
                        />
                      </div>

                      <!-- ISBN -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">ISBN *</label>
                        <input
                          v-model="bookForm.isbn"
                          type="text"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                          placeholder="Enter ISBN"
                        />
                      </div>

                      <!-- Price -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Price *</label>
                        <div class="relative">
                          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <span class="text-gray-500 text-lg">$</span>
                          </div>
                          <input
                            v-model="bookForm.price"
                            type="number"
                            step="0.01"
                            min="0"
                            required
                            class="w-full pl-8 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                            placeholder="0.00"
                          />
                        </div>
                      </div>

                      <!-- Author -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Author *</label>
                        <select
                          v-model="bookForm.authorId"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                          <option value="">Select Author</option>
                          <option v-for="author in authors" :key="author.id" :value="author.id">
                            {{ author.name }}
                          </option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Middle Column - Categories and Shelf -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
                      </svg>
                      Classification
                    </h4>
                    
                    <div class="space-y-4">
                      <!-- Categories -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Categories *</label>
                        <div class="max-h-64 overflow-y-auto border border-gray-300 rounded-lg p-4 bg-white">
                          <div class="grid grid-cols-1 gap-3">
                            <div v-for="category in categories" :key="category.id" class="flex items-center">
                              <input
                                :id="`category-${category.id}`"
                                v-model="bookForm.categoryIds"
                                :value="category.id"
                                type="checkbox"
                                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                              />
                              <label :for="`category-${category.id}`" class="ml-3 text-sm text-gray-700 font-medium">
                                {{ category.name }}
                              </label>
                            </div>
                          </div>
                        </div>
                        <p class="text-xs text-gray-500 mt-2">Select at least one category</p>
                      </div>

                      <!-- Shelf -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Shelf *</label>
                        <select
                          v-model="bookForm.shelfId"
                          required
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                          <option value="">Select Shelf</option>
                          <option v-for="shelf in shelves" :key="shelf.id" :value="shelf.id">
                            {{ shelf.code }} - {{ shelf.location }}
                          </option>
                        </select>
                      </div>

                    </div>
                  </div>
                </div>

                <!-- Right Column - Image Upload or Book Details -->
                <div class="space-y-6">
                  <div class="bg-gray-50 rounded-lg p-4">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                      <svg class="w-5 h-5 mr-2 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" />
                      </svg>
                      {{ showAddModal ? 'Book Cover' : 'Book Details' }}
                    </h4>
                    
                    <!-- Image Upload for Add Modal -->
                    <div v-if="showAddModal" class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-lg hover:border-gray-400 transition-colors bg-white">
                      <div class="space-y-1 text-center">
                        <!-- Preview Image -->
                        <div v-if="imagePreview" class="mb-4">
                          <img :src="imagePreview" alt="Preview" class="mx-auto h-48 object-contain rounded-lg shadow-md" />
                        </div>
                        
                        <!-- Upload Icon -->
                        <svg v-else class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48">
                          <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        
                        <div class="flex text-sm text-gray-600 justify-center">
                          <label class="relative cursor-pointer bg-white rounded-md font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-blue-500">
                            <span class="bg-blue-50 px-3 py-2 rounded-md">{{ imagePreview ? 'Change image' : 'Upload a file' }}</span>
                            <input
                              ref="fileInput"
                              type="file"
                              accept="image/*"
                              @change="handleImageUpload"
                              class="sr-only"
                            />
                          </label>
                          <p class="pl-1">or drag and drop</p>
                        </div>
                        <p class="text-xs text-gray-500">PNG, JPG, GIF up to 10MB</p>
                      </div>
                    </div>

                    <!-- Book Preview for Edit Modal -->
                    <div v-else class="space-y-4">
                      <!-- Current Image -->
                      <div class="flex flex-col items-center">
                        <img 
                          :src="bookForm.id && books.find(b => b.id === bookForm.id)?.imageUrl || '/placeholder.svg?height=200&width=140'" 
                          alt="Book Cover" 
                          class="h-48 object-contain rounded-lg shadow-md mb-3" 
                        />
                        <button 
                          type="button"
                          @click="openImageUpdateModal(bookForm.id)"
                          class="mt-2 inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                          </svg>
                          Update Image
                        </button>
                      </div>

                      <!-- Additional Book Info -->
                      <div class="mt-4 pt-4 border-t border-gray-200">
                        <div class="text-sm text-gray-600">
                          <div class="flex justify-between mb-2">
                            <span class="font-medium">Created:</span>
                            <span>{{ new Date().toLocaleDateString() }}</span>
                          </div>
                          <div class="flex justify-between mb-2">
                            <span class="font-medium">Last Updated:</span>
                            <span>{{ new Date().toLocaleDateString() }}</span>
                          </div>
                          <div class="flex justify-between">
                            <span class="font-medium">Book ID:</span>
                            <span class="font-mono">{{ bookForm.id }}</span>
                          </div>
                        </div>
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
                :disabled="isSaving || bookForm.categoryIds.length === 0"
                class="w-full inline-flex justify-center rounded-lg border border-transparent shadow-sm px-6 py-3 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-all"
              >
                <svg v-if="isSaving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                {{ isSaving ? 'Saving...' : (showAddModal ? 'Add Book' : 'Update Book') }}
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

    <!-- Image Update Modal -->
    <div v-if="showImageUpdateModal" class="fixed inset-0 z-50 overflow-y-auto" @click="closeImageUpdateModal">
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <div @click.stop class="inline-block align-bottom bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <form @submit.prevent="updateImage">
            <!-- Modal Header -->
            <div class="bg-gradient-to-r from-purple-600 to-indigo-700 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-xl font-semibold text-white">
                  Update Book Cover
                </h3>
                <button
                  @click="closeImageUpdateModal"
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
              <div class="space-y-6">
                <!-- Current Image -->
                <div class="flex justify-center">
                  <div class="w-1/2 pr-2">
                    <h4 class="text-sm font-medium text-gray-700 mb-2">Current Image</h4>
                    <img 
                      :src="currentBookForImageUpdate && books.find(b => b.id === currentBookForImageUpdate)?.imageUrl || '/placeholder.svg?height=200&width=140'" 
                      alt="Current Cover" 
                      class="h-48 w-full object-contain rounded-lg border border-gray-300" 
                    />
                  </div>
                  <div class="w-1/2 pl-2">
                    <h4 class="text-sm font-medium text-gray-700 mb-2">New Image</h4>
                    <div v-if="imagePreview" class="h-48 w-full flex items-center justify-center">
                      <img :src="imagePreview" alt="New Cover" class="h-full object-contain rounded-lg border border-gray-300" />
                    </div>
                    <div v-else class="h-48 w-full flex items-center justify-center bg-gray-100 rounded-lg border border-gray-300">
                      <span class="text-gray-400 text-sm">No image selected</span>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Image Upload -->
              <div class="mt-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">Upload New Image</label>
                <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-lg hover:border-gray-400 transition-colors">
                  <div class="space-y-1 text-center">
                    <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48">
                      <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    
                    <div class="flex text-sm text-gray-600 justify-center">
                      <label class="relative cursor-pointer bg-white rounded-md font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-blue-500">
                        <span class="bg-blue-50 px-3 py-2 rounded-md">Select a file</span>
                        <input
                          ref="imageUpdateInput"
                          type="file"
                          accept="image/*"
                          @change="handleImageUpload"
                          class="sr-only"
                        />
                      </label>
                      <p class="pl-1">or drag and drop</p>
                    </div>
                    <p class="text-xs text-gray-500">PNG, JPG, GIF up to 10MB</p>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Modal Footer -->
            <div class="bg-gray-50 px-6 py-4 sm:flex sm:flex-row-reverse border-t border-gray-200">
              <button
                type="submit"
                :disabled="!selectedImageFile || isSaving"
                class="w-full inline-flex justify-center rounded-lg border border-transparent shadow-sm px-6 py-3 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-all"
              >
                <svg v-if="isSaving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                {{ isSaving ? 'Updating...' : 'Update Cover' }}
              </button>
              <button
                @click="closeImageUpdateModal"
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
    <!-- Success/Error Messages -->
    <div v-if="message" class="fixed top-4 right-4 z-50">
      <div class="bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg flex items-center">
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
        </svg>
        {{ message }}
      </div>
    </div>

    <div v-if="errorMessage" class="fixed top-4 right-4 z-50">
      <div class="bg-red-500 text-white px-6 py-3 rounded-lg shadow-lg flex items-center">
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllBooks, getAllShelves, createBook, updateBook, updateBookImage } from '@/api'

const router = useRouter()

// State
const books = ref([])
const authors = ref([])
const categories = ref([])
const shelves = ref([])
const isLoading = ref(true)
const isSaving = ref(false)
const showAddModal = ref(false)
const showEditModal = ref(false)
const searchQuery = ref('')
const selectedCategory = ref('')
const selectedAuthor = ref('')
const message = ref('')
const errorMessage = ref('')
const imagePreview = ref('')
const selectedImageFile = ref(null)

const showImageUpdateModal = ref(false)
const currentBookForImageUpdate = ref(null)

const bookForm = reactive({
  id: null,
  title: '',
  isbn: '',
  price: '',
  authorId: '',
  categoryIds: [],
  shelfId: ''
})

// Computed
const filteredBooks = computed(() => {
  let filtered = books.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(book =>
      book.title.toLowerCase().includes(query) ||
      book.author.name.toLowerCase().includes(query) ||
      book.isbn.toLowerCase().includes(query)
    )
  }

  // Filter by category
  if (selectedCategory.value) {
    filtered = filtered.filter(book => 
      book.categories.some(cat => cat.id === parseInt(selectedCategory.value))
    )
  }

  // Filter by author
  if (selectedAuthor.value) {
    filtered = filtered.filter(book => 
      book.author.id === parseInt(selectedAuthor.value)
    )
  }

  return filtered
})

// Methods
const fetchData = async () => {
  try {
    isLoading.value = true
    
    // Fetch all required data
    await Promise.all([
      fetchBooks(),
      fetchAuthors(),
      fetchCategories(),
      fetchShelves()
    ])
    
  } catch (error) {
    console.error('Error fetching data:', error)
    errorMessage.value = 'Failed to load data'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  }
}

const fetchBooks = async () => {
  try {
    isLoading.value = true
    const response = await getAllBooks()
    books.value = response.data.data
  } catch (error) {
    console.error('Error fetching books:', error)
    errorMessage.value = 'Failed to load books'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isLoading.value = false
  }
}

const fetchAuthors = async () => {
  authors.value = [
    { id: 1, country: "Vietnam", name: "Nguyen Nhat Anh" },
    { id: 2, country: "Vietnam", name: "Nam Cao" },
    { id: 3, country: "Vietnam", name: "Nguyen Huy Thiep" },
    { id: 4, country: "Vietnam", name: "To Hoai" },
    { id: 5, country: "Vietnam", name: "Xuan Dieu" },
    { id: 6, country: "Vietnam", name: "Trinh Cong Son" },
    { id: 7, country: "United States", name: "Stephen King" },
    { id: 8, country: "United Kingdom", name: "J.K. Rowling" },
    { id: 9, country: "United States", name: "Isaac Asimov" },
    { id: 10, country: "United Kingdom", name: "Agatha Christie" },
    { id: 11, country: "United Kingdom", name: "Jane Austen" },
    { id: 12, country: "United Kingdom", name: "George Orwell" },
    { id: 13, country: "Israel", name: "Yuval Noah Harari" },
    { id: 14, country: "United States", name: "Carl Sagan" },
    { id: 15, country: "United States", name: "Dan Brown" },
    { id: 16, country: "Germany", name: "Albert Einstein" },
    { id: 17, country: "Greece", name: "Plato" },
    { id: 18, country: "Austria", name: "Sigmund Freud" },
    { id: 19, country: "Scotland", name: "Adam Smith" },
    { id: 20, country: "Germany", name: "Karl Marx" },
    { id: 21, country: "United Kingdom", name: "J.R.R. Tolkien" },
    { id: 22, country: "United States", name: "Ernest Hemingway" },
    { id: 23, country: "Germany", name: "Friedrich Nietzsche" },
    { id: 24, country: "Canada", name: "Malcolm Gladwell" },
    { id: 25, country: "United States", name: "Noam Chomsky" },
    { id: 26, country: "United States", name: "Anthony Bourdain" },
    { id: 27, country: "Spain", name: "Pablo Picasso" },
    { id: 28, country: "Germany", name: "Ludwig van Beethoven" },
    { id: 29, country: "United States", name: "Bill Gates" },
    { id: 30, country: "United States", name: "Michael Jordan" }
  ]
}

const fetchCategories = async () => {
  categories.value = [
    { id: 1, name: "Novel" },
    { id: 2, name: "Science Fiction" },
    { id: 3, name: "Mystery" },
    { id: 4, name: "Horror" },
    { id: 5, name: "Romance" },
    { id: 6, name: "History" },
    { id: 7, name: "Science" },
    { id: 8, name: "Philosophy" },
    { id: 9, name: "Psychology" },
    { id: 10, name: "Economics" },
    { id: 11, name: "Politics" },
    { id: 12, name: "Art" },
    { id: 13, name: "Music" },
    { id: 14, name: "Cuisine" },
    { id: 15, name: "Travel" },
    { id: 16, name: "Sports" },
    { id: 17, name: "Information Technology" },
    { id: 18, name: "Medicine" },
    { id: 19, name: "Religion" },
    { id: 20, name: "Children's Books" }
  ]
}

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

const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    selectedImageFile.value = file
    
    // Create preview
    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

const editBook = (book) => {
  bookForm.id = book.id
  bookForm.title = book.title
  bookForm.isbn = book.isbn
  bookForm.price = book.price
  bookForm.authorId = book.author.id
  bookForm.categoryIds = book.categories.map(cat => cat.id)
  bookForm.shelfId = book.shelf.id
  
  // Set image preview if exists
  if (book.imageUrl) {
    imagePreview.value = book.imageUrl
  }
  
  showEditModal.value = true
}

const deleteBook = async (book) => {
  if (confirm(`Are you sure you want to delete "${book.title}"?`)) {
    try {
      // Add your delete API call here
      // await deleteBookAPI(book.id)
      
      books.value = books.value.filter(b => b.id !== book.id)
      message.value = 'Book deleted successfully'
      setTimeout(() => message.value = '', 3000)
    } catch (error) {
      console.error('Error deleting book:', error)
      errorMessage.value = 'Failed to delete book'
      setTimeout(() => errorMessage.value = '', 3000)
    }
  }
}

const saveBook = async () => {
  try {
    isSaving.value = true
    
    // Create FormData
    const formData = new FormData()
    
    // Add book data as JSON string
    const bookData = {
      title: bookForm.title,
      isbn: bookForm.isbn,
      price: parseFloat(bookForm.price),
      authorId: parseInt(bookForm.authorId),
      categoryIds: bookForm.categoryIds.map(id => parseInt(id)),
      shelfId: parseInt(bookForm.shelfId),
    }
    
    formData.append('book', JSON.stringify(bookData))
    
    // Add image file if selected
    if (selectedImageFile.value) {
      formData.append('image', selectedImageFile.value)
    }
    
    if (showAddModal.value) {
      const response = await createBook(formData)
      const newBook = response.data.data
      books.value.unshift(newBook)
      message.value = 'Book added successfully'
    } else {
      const response = await updateBook(bookForm.id, formData)
      const updatedBook = response.data.data
      const index = books.value.findIndex(b => b.id === bookForm.id)
      if (index !== -1) {
        
        books.value[index] = {
          ...books.value[index],
          ...updatedBook
        }
      }
      message.value = 'Book updated successfully'
    }
    
    closeModal()
    setTimeout(() => message.value = '', 3000)
    
  } catch (error) {
    console.error('Error saving book:', error)
    errorMessage.value = 'Failed to save book'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isSaving.value = false
  }
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  
  // Reset form
  Object.keys(bookForm).forEach(key => {
    if (key === 'id') {
      bookForm[key] = null
    } else if (key === 'categoryIds') {
      bookForm[key] = []
    } else if (key === 'isAvailable') {
      bookForm[key] = true
    } else {
      bookForm[key] = ''
    }
  })
  
  // Reset image
  imagePreview.value = ''
  selectedImageFile.value = null
}

// Thêm các methods mới để xử lý modal cập nhật hình ảnh
const openImageUpdateModal = (bookId) => {
  currentBookForImageUpdate.value = bookId
  imagePreview.value = ''
  selectedImageFile.value = null
  showImageUpdateModal.value = true
}

const closeImageUpdateModal = () => {
  showImageUpdateModal.value = false
  currentBookForImageUpdate.value = null
  imagePreview.value = ''
  selectedImageFile.value = null
}

const updateImage = async () => {
  if (!currentBookForImageUpdate.value || !selectedImageFile.value) return
  console.log(selectedImageFile.value);
  try {
    isSaving.value = true
    
    const formData = new FormData()
    formData.append('image', selectedImageFile.value)
    
    await updateBookImage(currentBookForImageUpdate.value, formData)
    
    const index = books.value.findIndex(b => b.id === currentBookForImageUpdate.value)
    if (index !== -1) {
      books.value[index] = {
        ...books.value[index],
        imageUrl: imagePreview.value
      }
    }
    
    message.value = 'Book cover updated successfully'
    setTimeout(() => message.value = '', 3000)
    closeImageUpdateModal()
    
  } catch (error) {
    console.error('Error updating book image:', error)
    errorMessage.value = 'Failed to update book cover'
    setTimeout(() => errorMessage.value = '', 3000)
  } finally {
    isSaving.value = false
  }
}

// Lifecycle
onMounted(() => {
  fetchData()
})
</script>
