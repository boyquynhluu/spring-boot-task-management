import { useSelector } from 'react-redux';

export function useUser() {
  return useSelector((state) => state.auth.user);
}
