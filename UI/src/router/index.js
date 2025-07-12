import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// Import components
import HomePage from '@/views/HomePage.vue'
import BookDetail from '@/views/BookDetail.vue'

// Auth pages
import LoginPage from '@/views/auth/LoginPage.vue'
import RegisterPage from '@/views/auth/RegisterPage.vue'

// USER pages
import ProfilePage from '@/views/ProfilePage.vue'
import BorrowBookPage from '@/views/BookPage.vue'
import MyBorrowingsPage from '@/views/user/MyBorrowingsPage.vue'
import MyPenaltiesPage from '@/views/user/MyPenaltiesPage.vue'

// // LIBRARIAN pages
// import LibrarianDashboard from '@/views/librarian/LibrarianDashboard.vue'
import BooksManagementPage from '@/views/librarian/BooksManagementPage.vue'
import BookItemsManagementPage from '@/views/librarian/BookItemsManagementPage.vue'
import ShelvesManagementPage from '@/views/librarian/ShelvesManagementPage.vue'
import BorrowingsManagementPage from '@/views/librarian/BorrowingsManagamentPage.vue'
import PenaltiesManagementPage from '@/views/librarian/PenaltiesManagementPage.vue'
// import ReportsPage from '@/views/librarian/ReportsPage.vue'

// // ADMIN pages
import BooksManagementPageAdmin from '@/views/admin/BooksManagementPage.vue'
import BookItemsManagementPageAdmin from '@/views/admin/BookItemsManagementPage.vue'
import ShelvesManagementPageAdmin from '@/views/admin/ShelvesManagementPage.vue'
import BorrowingsManagementPageAdmin from '@/views/admin/BorrowingsManagamentPage.vue'
import PenaltiesManagementPageAdmin from '@/views/admin/PenaltiesManagementPage.vue'
import AccountsManagementPageAdmin from '@/views/admin/AccountManagementPage.vue'

const routes = [
  // Public routes
  {
    path: '/',
    name: 'homepage',
    component: HomePage
  },
  {
    path: '/books',
    name: 'books',
    component: BorrowBookPage,
  },
  {
    path: '/books/:id',
    name: 'BookDetail',
    component: BookDetail
  },
  
  // Auth routesx`
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
    meta: { requiresAuth: true}
  },
  {
    path: '/my-borrowings',
    name: 'my-borrowings',
    component: MyBorrowingsPage,
    meta: { requiresAuth: true, role: 'USER' }
  },
  {
    path: '/my-penalties',
    name: 'my-penalties',
    component: MyPenaltiesPage,
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
    path: '/librarian/penalties',
    name: 'librarian-penalties',
    component: PenaltiesManagementPage,
    meta: { requiresAuth: true, role: 'LIBRARIAN' }
  },
  // {
  //   path: '/librarian/reports',
  //   name: 'librarian-reports',
  //   component: ReportsPage,
  //   meta: { requiresAuth: true, role: 'LIBRARIAN' }
  // },
  
  // ADMIN routes
  // {
  //   path: '/admin/dashboard',
  //   name: 'admin-dashboard',
  //   component: AdminDashboard,
  //   meta: { requiresAuth: true, role: 'ADMIN' }
  // },
  {
    path: '/admin/accounts',
    name: 'admin-accounts',
    component: AccountsManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/books',
    name: 'admin-books',
    component: BooksManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/book-items',
    name: 'admin-book-items',
    component: BookItemsManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/shelves',
    name: 'admin-shelves',
    component: ShelvesManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/borrowings',
    name: 'admin-borrowings',
    component: BorrowingsManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/penalties',
    name: 'admin-penalties',
    component: PenaltiesManagementPageAdmin,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
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