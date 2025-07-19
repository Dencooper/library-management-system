import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1';

// Tạo instance axios với cấu hình mặc định
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const bookApi = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'multipart/form-data',
  },
});

// Thêm interceptor để tự động thêm token vào header
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

bookApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Auth API
export const register = async (data) => {
  try{
    const response = await api.post('/auth/register', data);
    return response;
  } catch (error) {
    console.error('Login error:', error);
  }
  return false
};

export const login = async (data) => {
  try{
    const response = await api.post('/auth/login', data);
    return response;
  } catch (error) {
    console.error('Login error:', error);
  }
  return false
};

export const logout = async () => {
  try{
    const response = await api.post('/auth/logout');
    return response;
  } catch (error) {
    console.error('Logout error:', error);
  }
  return false
};

export const getMe = async () => {
  const response = await api.get('/auth/user');
  return response;
};

// User API
export const createUser = async (data) => {
  try {
    const response = await api.post('/users', data);
    return response;
  } catch (error) {
    console.error('Create user error: ', error);
    return null;
  }
};

export const getUserById = async (id) => {
  const response = await api.get(`/users/${id}`);
  return response;
};

export const getUserByEmail = async (email) => {
  const response = await api.get(`/users/email`, { params: { email } });
  return response;
};

export const getAllUsers = async () => {
  const response = await api.get('/users');
  return response;
};

export const updateUser = async (id, data) => {
  const response = await api.put(`/users/${id}`, data);
  return response;
};

export const updateDeleteStatus = async (id, isDeleted) => {
  const response = await api.patch(`/users/${id}/status`, null, {params: { isDeleted }});
  return response;
};

export const updateBannedUser = async (id, isBanned) => {
  const response = await api.patch(`/users/${id}/ban`, null, {params: { isBanned }});
  return response;
};

export const getUserQuantity = async () => {
  const response = await api.get('/users/quantity');
  return response;
};

// Book API
export const createBook = async (data) => {
  try {
    const response = await bookApi.post('/books', data);
    return response;
  } catch (error) {
    console.error(':', error);
    return null;
  }
};

export const getBook = async (id) => {
  const response = await api.get(`/books/${id}`);
  return response;
};

export const getAllBooks = async () => {
  const response = await api.get('/books');
  return response;
};


export const updateBook = async (id, data) => {
  const response = await bookApi.put(`/books/${id}`, data);
  return response;
};

export const updateBookImage = async (id, imageFile) => {
  const response = await bookApi.patch(`/books/${id}`, imageFile);
  return response;
};

export const getBookQuantity = async () => {
  const response = await api.get('/books/quantity');
  return response;
};

//Book Item API
export const createBookItem = async (data) => {
  try {
    const response = await api.post('/book-items', data);
    return response;
  } catch (error) {
    console.error(':', error);
    return null;
  }
};

export const getAllBookItems = async (isAvailable) => {
  const response = await api.get('/book-items', { params: { isAvailable } });
  return response;
};

export const getBookItemById = async (id) => {
  const response = await api.get(`/book-items/${id}`);
  return response;
};

export const getBookItemByCode = async (code) => {
  const response = await api.get('/book-items/code', { params: { code } });
  return response;
};

export const updateBookItem = async (id, data) => {
  const response = await api.put(`/book-items/${id}`, data);
  return response;
};

export const getBookItemsByBook = async (bookId, isAvailable) => {
  const response = await api.get(`/book-items/book/${bookId}`, { params: { isAvailable } });
  return response;
};


export const deleteBookItem = async (id, isDeleted) => {
  try {
    const response = await api.patch(`/book-items/${id}/delete`, {params: { isDeleted }});
    return response;
  } catch (error) {
    console.error(`Lỗi xóa sách với ID ${id}:`, error);
    return null;
  }
};

// Shelf API
export const createShelf = async (data) => {
  try {
    const response = await api.post('/book-items', data);
    return response;
  } catch (error) {
    console.error(':', error);
    return null;
  }
};

export const getShelfById = async (id) => {
  const response = await api.get(`/shelves/${id}`);
  return response;
};

export const getAllShelves = async () => {
  const response = await api.get('/shelves');
  return response;
};

export const updateShelf = async (id, data) => {
  const response = await api.put(`/shelves/${id}`, data);
  return response;
};

export const updateShelfStatus = async (id, isDeleted) => {
  const response = await api.patch(`/shelves/${id}/status`, {params: { isDeleted }});
  return response;
};

// Borrowing API
export const createBorrowing = async (data) => {
  const response = await api.post('/borrowings', data);
  return response;
};

export const getBorrowingById = async (id) => {
  const response = await api.get(`/borrowings/${id}`);
  return response;
};

export const getBorrowingsByUser = async (email) => {
  const response = await api.get('/borrowings/user', { params: { email } });
  return response;
};

export const getAllBorrowings = async () => {
  const response = await api.get('/borrowings');
  return response;
};

export const getMyAllBorrowings = async () => {
  const response = await api.get('/borrowings/user');
  return response;
};

export const getBorrowingQuantity = async () => {
  const response = await api.get('/borrowings/quantity');
  return response;
};  

export const returnBook = async (data) => {
  console.log(data);
  const response = await api.patch(`/borrowings/return`, data);
  return response;
};

// Penalty API
export const getPenaltyById = async (id) => {
  const response = await api.get(`/penalties/${id}`);
  return response;
};

export const getAllPenalties = async () => {
  const response = await api.get('/penalties');
  return response;
};

export const getUserAllPenalties = async (id) => {
  const response = await api.get(`/penalties/user/${id}`);
  return response;
};

export const updatePenalty = async (id, data) => {
  const response = await api.patch(`/penalties/${id}`, data);
  return response;
};

export const getBorrowingStatistics = async ({ from, to }) => {
  const response = await api.get('/borrowings/statistic', { params: { from, to } });
  return response;
};

export const getBookStatistics = async ({ from, to }) => {
  const response = await api.get('/borrowings/book-statistic', { params: { from, to } });
  return response;
};

export const getUserStatistics = async ({ from, to }) => {
  const response = await api.get('/borrowings/user-statistic', { params: { from, to } });
  return response;
};

export const getPenaltyStatistics = async ({ from, to }) => {
  const response = await api.get('/penalties/statistic', { params: { from, to } });
  return response;
};


