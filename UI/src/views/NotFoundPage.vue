<template>
  <div class="min-h-screen bg-gray-100 flex flex-col justify-center items-center px-4">
    <div class="max-w-md w-full text-center">
      <h1 class="text-9xl font-bold text-gray-900">404</h1>
      <p class="mt-4 text-xl text-gray-600">Trang bạn đang tìm kiếm không tồn tại.</p>
      <div class="mt-6">
        <router-link
          to="/"
          class="inline-flex items-center px-4 py-2 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
            />
          </svg>
          Trở về trang chủ
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
// No additional logic needed
</script>