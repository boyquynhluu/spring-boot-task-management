// src/pages/LoginPage.jsx
import React, { useState, useEffect } from 'react';
import { FcGoogle } from 'react-icons/fc'; // icon Google
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import AuthService from '../services/AuthService';
import RegisterModal from './RegisterModal';
import { GOOGLE_LOGIN_URL } from '../constants/Constants';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUser } from '../redux/authSlice';

const LoginPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    usernameOrEmail: '',
    password: '',
  });

  const { user } = useSelector((state) => state.auth);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (user) {
      navigate('/home');
    }
  }, [user, navigate]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await AuthService.login(formData);
      // 👇 gọi lại fetchUser để lấy user từ cookie
      dispatch(fetchUser());
    } catch (error) {
      setError(error.response?.data?.message);
      alert('Sai username or password');
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = GOOGLE_LOGIN_URL;
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-r from-blue-400 to-indigo-600">
      {/* Form login */}
      <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
        <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">
          Đăng nhập
        </h2>
        <form
          className="space-y-4"
          onSubmit={(e) => {
            e.preventDefault();
            handleLogin(e);
          }}
        >
          <div>
            {error && <p className="text-red-500">{error}</p>}
            {/* Ô nhập username or email */}
            <label className="block text-gray-700 font-medium mb-2">
              Email hoặc Username
            </label>
            <input
              type="text"
              name="usernameOrEmail"
              value={formData.usernameOrEmail}
              onChange={handleChange}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nhập email hoặc username"
              required
            />
          </div>
          {/* Label + input + icon */}
          <div className="mb-6">
            <label
              htmlFor="password"
              className="block text-gray-700 font-medium mb-2"
            >
              Mật khẩu
            </label>
            <div className="relative">
              <input
                id="password"
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 pr-10"
                placeholder="Nhập mật khẩu"
                required
              />
              <span
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-2.5 cursor-pointer text-gray-500 hover:text-blue-600"
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </span>
            </div>
          </div>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
          >
            Đăng nhập
          </button>
        </form>

        <div className="flex items-center my-6">
          <hr className="flex-grow border-gray-300" />
          <span className="px-2 text-gray-500">Hoặc</span>
          <hr className="flex-grow border-gray-300" />
        </div>

        <button
          onClick={handleGoogleLogin}
          className="w-full flex items-center justify-center border py-2 rounded-lg hover:bg-gray-100 transition"
        >
          <FcGoogle className="mr-2 text-xl" />
          Đăng nhập với Google
        </button>

        {/* Nút Đăng ký */}
        <div className="text-center mt-4">
          <p
            onClick={() => setIsRegisterOpen(true)}
            style={{
              color: 'blue',
              cursor: 'pointer',
            }}
          >
            Chưa có tài khoản?
          </p>
        </div>
      </div>
      {/* Modal Register */}
      <RegisterModal
        isOpen={isRegisterOpen}
        onClose={() => setIsRegisterOpen(false)}
      />
    </div>
  );
};

export default LoginPage;
