<template>
  <footer class="bg-gray-800 text-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        
        <!-- Contact Information -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Contact Us</h3>
          <p class="text-gray-300">📍 Zone II, 3/2 Street, Xuan Khanh, Ninh Kieu</p>
          <p class="text-gray-300">📞 (+83) 090 222 999</p>
          <p class="text-gray-300">✉️ info@onlinelibrary.com</p>
        </div>

        <!-- Opening Hours -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Opening Hours</h3>
          <p class="text-gray-300">Mon - Fri: 7:00 AM – 9:00 PM</p>
          <p class="text-gray-300">Sat - Sun: 8:00 AM – 7:00 PM</p>
          <p class="text-gray-300 mt-4">📚 Public Holidays: Closed</p>
        </div>

        <!-- Quick Links -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Quick Links</h3>
          <ul class="text-gray-300 space-y-2">
            <li><router-link to="/" class="hover:underline">Home</router-link></li>
            <li><router-link to="/books" class="hover:underline">Browse Books</router-link></li>
            <li><router-link to="/profile" class="hover:underline">My Account</router-link></li>
          </ul>
        </div>

        <!-- Services & Social -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Our Services</h3>
          <p class="text-gray-300">• Book Borrowing</p>
          <p class="text-gray-300">• Research Support</p>
          <p class="text-gray-300">• Digital Resources</p>
          <p class="text-gray-300">• Study Rooms</p>

        </div>

      </div>

      <!-- Bottom line -->
      <div class="border-t border-gray-700 mt-10 pt-6 text-center text-sm text-gray-400">
        <p>&copy; 2025 Online Library. All rights reserved.</p>
        <div class="mt-2 space-x-4">
          <a href="/privacy-policy" class="hover:underline">Privacy Policy</a>
          <a href="/terms-and-conditions" class="hover:underline">Terms of Service</a>
        </div>
      </div>
    </div>
  </footer>
</template>
