// src/redux/authSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import UserService from '../services/UserService';

// 🔥 Call API /me
export const fetchUser = createAsyncThunk(
  'auth/fetchUser',
  async (_, { rejectWithValue }) => {
    try {
      const res = await UserService.getUser();
      return res;
    } catch (err) {
      return rejectWithValue(null);
    }
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    loading: false,
    error: null,
    status: 'idle',
  },
  reducers: {
    logout: (state) => {
      state.user = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUser.pending, (state) => {
        state.loading = true;
        state.status = 'loading'; // ✅ thêm
      })
      .addCase(fetchUser.fulfilled, (state, action) => {
        state.user = action.payload;
        state.loading = false;
        state.status = 'succeeded'; // ✅ thêm
      })
      .addCase(fetchUser.rejected, (state) => {
        state.user = null;
        state.loading = false;
        state.status = 'failed'; // ✅ thêm
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
