import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../utils/Profile';
import TaskService from '../services/TaskService';

function Tasks({ isOpen, onClose }) {
  const user = useUser();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: '',
    description: '',
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleTask = async (e) => {
    e.preventDefault();
    try {
      if (user === null) {
        navigate('/login');
      }
      const resData = await TaskService.create(formData);
      console.log('Create Task: ', resData);
      onClose();
    } catch (error) {
      console.log(error);
      alert('Tạo Task Không Thành Công!');
    }
  };

  if (!isOpen) return null; // nếu chưa mở thì không render gì

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg w-96 p-6 relative">
        {/* Nút đóng */}
        <button
          onClick={onClose}
          className="absolute top-2 right-2 text-gray-500 hover:text-red-500"
        >
          ✕
        </button>

        <h2 className="text-2xl font-bold text-center mb-6 text-green-600">
          Tạo Task Bằng AI
        </h2>

        <form onSubmit={handleTask}>
          <label className="block text-gray-700 font-medium mb-2">
            Tên Task Cần Tạo
          </label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className="w-full mb-4 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
            placeholder="Nhập Tên Task Cần Tạo"
            required
          />

          <input
            type="text"
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full mb-4 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
            placeholder="Mô Tả Thông Tin Task"
            required
          />

          <button
            type="submit"
            className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 transition"
          >
            Tạo Task
          </button>
        </form>
      </div>
    </div>
  );
}

export default Tasks;
