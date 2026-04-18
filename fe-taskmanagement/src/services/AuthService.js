import axios from '../utils/AxiosInstance';

const AuthService = {
  register: async (userRequest) => {
    try {
      const response = await axios.post('/api/auth/register', userRequest);
      return response.data;
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },

  verify: async (token) => {
    try {
      const response = await axios.get('/api/auth/verify', {
        params: { token: token },
      });
      return response.data;
    } catch (error) {
      console.error('Verify error:', error);
      throw error;
    }
  },

  login: async (authRequest) => {
    try {
      console.log('authRequest: ', authRequest);
      const response = await axios.post('/api/auth/login', authRequest);
      return response.data;
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  },

  logout: async () => {
    try {
      const response = await axios.post('/api/auth/logout');
      return response.data;
    } catch (error) {
      console.error('Logout failed:', error);
      throw error;
    }
  },
};

export default AuthService;
