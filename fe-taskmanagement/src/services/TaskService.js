import axios from '../utils/AxiosInstance';

const TaskService = {
  create: async (formData) => {
    const response = await axios.post('/api/tasks', formData);
    return response.data;
  },
};

export default TaskService;
