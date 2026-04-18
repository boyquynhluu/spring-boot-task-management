import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

function VerifyAccount() {
  const [status, setStatus] = useState('loading');
  const [searchParams] = useSearchParams();
  //const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get('token');

    if (!token) {
      setStatus('error');
      return;
    }
    const verifyHandle = async (token) => {
      try {
        const resData = await AuthService.verify(token);
        console.log('resData: ', resData);
        setStatus('success');
        //navigate('/home');
      } catch (error) {
        setStatus('error');
      }
    };

    verifyHandle(token);
  }, [searchParams]);

  if (status === 'loading') return <h2>Đang xác thực...</h2>;
  if (status === 'success') return <h2>Xác thực thành công 🎉</h2>;
  return <h2>Link không hợp lệ hoặc đã hết hạn ❌</h2>;
}

export default VerifyAccount;
