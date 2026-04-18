// axiosInstance.js
import axios from 'axios';

const BASE_URL = 'http://localhost:8081';

const axiosInstance = axios.create({
  // baseURL: BASE_URL,
  baseURL: '/',
  timeout: 10000,
  withCredentials: true, // ✅ QUAN TRỌNG
});

// axios riêng để gọi refresh (không loop)
const axiosWithoutInterceptor = axios.create({
  // baseURL: BASE_URL,
  baseURL: '/',
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (res) => res,
  async (err) => {
    const originalConfig = err.config;

    // ❗ tránh loop vô hạn
    if (
      err.response?.status === 401 &&
      !originalConfig._retry &&
      !originalConfig.url.includes('/api/auth/refresh') &&
      document.cookie.includes('refreshToken') // 🔥 thêm dòng này
    ) {
      originalConfig._retry = true;

      try {
        // ✅ gọi refresh bằng cookie
        await axiosWithoutInterceptor.post('/api/auth/refresh');

        // ✅ gọi lại request cũ
        return axiosInstance(originalConfig);
      } catch (refreshErr) {
        // ❌ refresh fail → logout
        window.location.href = '/login';
        return Promise.reject(refreshErr);
      }
    }

    return Promise.reject(err);
  }
);

export default axiosInstance;
