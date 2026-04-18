import axios from '../utils/AxiosInstance';

const UserService = {
  getUser: async () => {
    const response = await axios.get('/api/users/me');
    return response.data; // 👈 BE trả thẳng user
  },
};

export default UserService;
