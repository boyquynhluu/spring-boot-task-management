import { Link, useNavigate } from 'react-router-dom';
import { FaHome, FaTasks, FaUser } from 'react-icons/fa';
import { useUser } from '../utils/Profile';
import AuthService from '../services/AuthService';
import { useDispatch } from 'react-redux';
import { logout } from '../redux/authSlice';
import { useState } from 'react';
import Tasks from '../pages/Tasks';

function Header() {
  // Get user from redux
  const user = useUser();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [isTaskOpen, setIsTaskOpen] = useState(false);

  // Logout
  const handleLogout = async (e) => {
    e.preventDefault();
    try {
      const resData = await AuthService.logout();
      console.log('handleLogout: ', resData);
      // Set state user
      dispatch(logout());
      if (user === null) {
        navigate('/login');
      }
    } catch (error) {
      console.log(error);
      alert('Logout Fail!');
    }
  };

  return (
    <header className="bg-gradient-to-r from-blue-500 to-indigo-600 shadow-lg">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        {/* Logo */}
        <h1 className="flex items-center text-2xl font-extrabold text-white tracking-wide">
          <FaTasks className="mr-2 text-yellow-300" />
          TaskManager
        </h1>

        {/* Navigation */}
        <nav className="space-x-6 flex items-center">
          <Link
            to="/"
            className="flex items-center text-white hover:text-yellow-300 font-medium transition"
          >
            <FaHome className="mr-1" /> Home
          </Link>
          {/* Task*/}
          <Link
            onClick={() => setIsTaskOpen(true)}
            className="flex items-center text-white hover:text-yellow-300 font-medium transition"
          >
            <FaTasks className="mr-1" /> Tasks
          </Link>

          <>
            {user ? (
              <button
                onClick={handleLogout}
                className="flex items-center text-white hover:text-yellow-300 font-medium transition"
              >
                <FaUser className="mr-1" /> Hello {user.name} Logout
              </button>
            ) : (
              <Link
                to="/login"
                className="flex items-center text-white hover:text-yellow-300 font-medium transition"
              >
                <FaUser className="mr-1" /> Login
              </Link>
            )}
          </>
        </nav>

        {/* Modal Create Task */}
        <Tasks isOpen={isTaskOpen} onClose={() => setIsTaskOpen(false)} />
      </div>
    </header>
  );
}

export default Header;
