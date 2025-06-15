import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// Import components
import HomePage from '@/views/HomePage.vue'
import BookDetail from '@/views/BookDetail.vue'

// Auth pages
import LoginPage from '@/views/auth/LoginPage.vue'
import RegisterPage from '@/views/auth/RegisterPage.vue'

// USER pages
import ProfilePage from '@/views/user/ProfilePage.vue'
import BorrowBookPage from '@/views/user/BorrowBookPage.vue'
import MyBorrowingRequestsPage from '@/views/user/MyBorrowingRequestsPage.vue'
import MyBorrowingsPage from '@/views/user/MyBorrowingsPage.vue'
import ReturnBookPage from '@/views/user/ReturnBookPage.vue'

// // LIBRARIAN pages
// import LibrarianDashboard from '@/views/librarian/LibrarianDashboard.vue'
import BooksManagementPage from '@/views/librarian/BooksManagementPage.vue'
import BookItemsManagementPage from '@/views/librarian/BookItemsManagementPage.vue'
import ShelvesManagementPage from '@/views/librarian/ShelvesManagementPage.vue'
import BorrowingsManagementPage from '@/views/librarian/BorrowingsManagamentPage.vue'
import BorrowingRequestsManagementPage from '@/views/librarian/BorrowingRequestsManagementPage.vue'
import PenaltiesManagementPage from '@/views/librarian/PenaltiesManagementPage.vue'
// import ReportsPage from '@/views/librarian/ReportsPage.vue'

// // ADMIN pages
// import AdminDashboard from '@/views/admin/AdminDashboard.vue'
// import ManageUsersPage from '@/views/admin/ManageUsersPage.vue'
// import ManageLibrariansPage from '@/views/admin/ManageLibrariansPage.vue'
// import SystemSettingsPage from '@/views/admin/SystemSettingsPage.vue'

const routes = [
  // Public routes
  {
    path: '/',
    name: 'homepage',
    component: HomePage
  },
  {
    path: '/book/:id',
    name: 'BookDetail',
    component: BookDetail
  },
  
  // Auth routes
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterPage,
    meta: { requiresGuest: true }
  },
  
  // USER routes
  {
    path: '/profile',
    name: 'profile',
    component: ProfilePage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  {
    path: '/borrow',
    name: 'borrow',
    component: BorrowBookPage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  {
    path: '/my-borrowing-requests',
    name: 'my-borrowing-requests',
    component: MyBorrowingRequestsPage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  {
    path: '/my-borrowings',
    name: 'my-borrowings',
    component: MyBorrowingsPage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  {
    path: '/return',
    name: 'return',
    component: ReturnBookPage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  
  // // LIBRARIAN routes
  // {
  //   path: '/librarian/dashboard',
  //   name: 'librarian-dashboard',
  //   component: LibrarianDashboard,
  //   meta: { requiresAuth: true, role: 'LIBRARIAN' }
  // },
  {
    path: '/librarian/books',
    name: 'librarian-books',
    component: BooksManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  {
    path: '/librarian/book-items',
    name: 'librarian-book-items',
    component: BookItemsManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  {
    path: '/librarian/shelves',
    name: 'librarian-shelves',
    component: ShelvesManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  {
    path: '/librarian/borrowings',
    name: 'librarian-borrowings',
    component: BorrowingsManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  {
    path: '/librarian/borrowing-requests',
    name: 'librarian-borrowing-requests',
    component: BorrowingRequestsManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  {
    path: '/librarian/penalties',
    name: 'librarian-penalties',
    component: PenaltiesManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  }
  // {
  //   path: '/librarian/reports',
  //   name: 'librarian-reports',
  //   component: ReportsPage,
  //   meta: { requiresAuth: true, role: 'LIBRARIAN' }
  // },
  
  // // ADMIN routes
  // {
  //   path: '/admin/dashboard',
  //   name: 'admin-dashboard',
  //   component: AdminDashboard,
  //   meta: { requiresAuth: true, role: 'ADMIN' }
  // },
  // {
  //   path: '/admin/users',
  //   name: 'admin-users',
  //   component: ManageUsersPage,
  //   meta: { requiresAuth: true, role: 'ADMIN' }
  // },
  // {
  //   path: '/admin/librarians',
  //   name: 'admin-librarians',
  //   component: ManageLibrariansPage,
  //   meta: { requiresAuth: true, role: 'ADMIN' }
  // },
  // {
  //   path: '/admin/settings',
  //   name: 'admin-settings',
  //   component: SystemSettingsPage,
  //   meta: { requiresAuth: true, role: 'ADMIN' }
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0, behavior: 'smooth' }
  }
})

// Navigation guards
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // Initialize auth if not already done
  if (!authStore.user && authStore.accessToken) {
    await authStore.checkAuthOnStartup()
  }
  
  // Check if route requires guest (login/register pages)
  if (to.meta.requiresGuest && authStore.isAuthenticated) {
    return next('/')
  }
  
  // Check if route requires authentication
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  }
  
  // Check role-based access
  if (to.meta.role && !authStore.hasRole(to.meta.role)) {
    // Redirect to appropriate dashboard based on user role
    if (authStore.isAdmin) {
      return next('/admin/dashboard')
    } else if (authStore.isLibrarian) {
      return next('/librarian/dashboard')
    } else {
      return next('/')
    }
  }
  
  next()
})

export default router