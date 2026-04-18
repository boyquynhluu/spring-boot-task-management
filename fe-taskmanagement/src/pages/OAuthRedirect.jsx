import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchUser } from '../redux/authSlice';

function OAuthRedirect() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const handleOAuth = async () => {
      try {
        // 🔥 gọi API /me để lấy user từ cookie
        const result = await dispatch(fetchUser());

        // ✅ nếu thành công
        if (fetchUser.fulfilled.match(result)) {
          console.log('OAuth success:', result.payload);
          navigate('/home');
        } else {
          console.log('OAuth failed');
          navigate('/login');
        }
      } catch (error) {
        console.error('OAuth error:', error);
        navigate('/login');
      }
    };

    handleOAuth();
  }, [dispatch, navigate]);

  return (
    <div style={{ textAlign: 'center', marginTop: '100px' }}>
      <h2>Đang đăng nhập với Google...</h2>
    </div>
  );
}

export default OAuthRedirect;
