import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';

const HomePage = () => {
  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* HEADER */}
      <Header />

      {/* HERO */}
      <section className="bg-blue-600 text-white text-center py-20">
        <h2 className="text-4xl font-bold mb-4">
          Quản lý công việc hiệu quả 🚀
        </h2>
        <p className="text-lg mb-6">
          Tạo, theo dõi và hoàn thành task một cách dễ dàng
        </p>
        <a
          href="/tasks"
          className="bg-white text-blue-600 px-6 py-3 rounded-xl font-semibold hover:bg-gray-200"
        >
          Bắt đầu ngay
        </a>
      </section>

      {/* FEATURES */}
      <section className="py-16 px-8 grid md:grid-cols-3 gap-8 flex-grow">
        <div className="bg-white p-6 rounded-2xl shadow hover:shadow-lg transition">
          <h3 className="text-xl font-bold mb-2">📋 Quản lý task</h3>
          <p>Tạo, sửa, xoá và theo dõi công việc dễ dàng</p>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow hover:shadow-lg transition">
          <h3 className="text-xl font-bold mb-2">⏰ Deadline rõ ràng</h3>
          <p>Không bỏ lỡ bất kỳ công việc quan trọng nào</p>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow hover:shadow-lg transition">
          <h3 className="text-xl font-bold mb-2">📊 Thống kê</h3>
          <p>Xem tiến độ và hiệu suất làm việc</p>
        </div>
      </section>

      {/* FOOTER */}
      <Footer />
    </div>
  );
};

export default HomePage;
