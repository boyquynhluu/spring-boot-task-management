import Dashboard from './pages/Dashboard';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import OAuthRedirect from './pages/OAuthRedirect';
import VerifyAccount from './pages/VerifyAccount';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUser } from './redux/authSlice';
import ProtectedRoute from './components/ProtectedRoute';
import Tasks from './pages/Tasks';

function App() {
  const dispatch = useDispatch();
  const { status } = useSelector((state) => state.auth);

  useEffect(() => {
    if (status === 'idle') {
      console.log('CALL fetchUser');
      dispatch(fetchUser());
    }
  }, [status, dispatch]);

  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/oauth2/success" element={<OAuthRedirect />} />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />
        <Route path="/verify" element={<VerifyAccount />} />
        <Route path="/task" element={<Tasks />} />
      </Routes>
    </Router>
  );
}

export default App;
