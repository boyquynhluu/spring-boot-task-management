import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
  const { user, loading, status } = useSelector((state) => state.auth);

  // ⏳ Chưa check xong login → chặn
  if (status === 'idle' || loading) {
    return <div>Loading...</div>;
  }

  // ❌ chưa login
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // ✅ đã login
  return children;
}

export default ProtectedRoute;
