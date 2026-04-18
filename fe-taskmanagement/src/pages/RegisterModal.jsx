import React, { useState } from 'react';
import AuthService from '../services/AuthService';
import { FaEye, FaEyeSlash } from 'react-icons/fa';

function RegisterModal({ isOpen, onClose }) {
  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({});

  const togglePassword = () => {
    setShowPassword(!showPassword);
  };

  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await AuthService.register(formData);
      console.log('Register success:', data.message);
      alert('Đăng ký thành công! ' + data.message);
      onClose();
    } catch (error) {
      console.error('Register error:', error.response?.data || error.message);
      const errors = error.response?.data;
      if (errors) {
        setErrors(errors);
      }
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
          Đăng ký tài khoản
        </h2>

        <form onSubmit={handleSubmit}>
          <label className="block text-gray-700 font-medium mb-2">
            Họ và tên
          </label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full mb-4 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
            placeholder="Nhập họ và tên"
            required
          />

          <label className="block text-gray-700 font-medium mb-2">
            Username
          </label>
          {/* show error */}
          {errors.username && <p style={{ color: 'red' }}>{errors.username}</p>}
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            className="w-full mb-4 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
            placeholder="Nhập username"
            required
          />

          <label className="block text-gray-700 font-medium mb-2">Email</label>
          {/* show error */}
          {errors.email && <p style={{ color: 'red' }}>{errors.email}</p>}
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className="w-full mb-4 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
            placeholder="Nhập email"
            required
          />

          <div className="mb-6">
            <label className="block text-gray-700 font-medium mb-2">
              Mật khẩu
            </label>
            {/* show error */}
            {errors.password && (
              <p style={{ color: 'red' }}>{errors.password}</p>
            )}
            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-green-400"
                placeholder="Nhập mật khẩu"
                required
              />
              <button
                type="button"
                onClick={togglePassword}
                className="absolute right-2 top-2 text-sm text-gray-600 hover:text-green-500"
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 transition"
          >
            Đăng ký
          </button>
        </form>
      </div>
    </div>
  );
}

export default RegisterModal;
