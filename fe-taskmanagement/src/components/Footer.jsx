import { FaFacebook, FaTwitter, FaGithub, FaEnvelope } from 'react-icons/fa';
import zaloLogo from '../assets/Zalo.svg.webp';

function Footer() {
  return (
    <footer className="bg-gray-900 text-gray-300 py-10">
      <div className="container mx-auto px-6 grid grid-cols-1 md:grid-cols-3 gap-8">
        {/* Cột 1: Thông tin */}
        <div>
          <h2 className="text-xl font-bold text-white mb-4">TaskManager</h2>
          <p className="mb-2">
            Quản lý công việc hiệu quả, đơn giản và trực quan.
          </p>
          <p className="flex items-center">
            <FaEnvelope className="mr-2" /> vanductai.dev@gmail.com
          </p>
        </div>

        {/* Cột 2: Liên kết nhanh */}
        <div>
          <h2 className="text-xl font-bold text-white mb-4">Liên kết nhanh</h2>
          <ul className="space-y-2">
            <li>
              <a href="/" className="hover:text-blue-400 transition">
                Trang chủ
              </a>
            </li>
            <li>
              <a href="/tasks" className="hover:text-blue-400 transition">
                Công việc
              </a>
            </li>
          </ul>
        </div>

        {/* Cột 3: Mạng xã hội */}
        <div>
          <h2 className="text-xl font-bold text-white mb-4">Kết nối</h2>
          <div className="flex space-x-4">
            <a
              href="https://web.facebook.com/boyquynhluu92"
              className="hover:text-blue-500 transition"
            >
              <FaFacebook size={24} />
            </a>
            <a
              href="https://zalo.me/your-zalo-id"
              className="hover:opacity-80 transition"
            >
              <img src={zaloLogo} alt="Zalo" className="w-6 h-6" />
            </a>
            <a
              href="https://twitter.com"
              className="hover:text-blue-400 transition"
            >
              <FaTwitter size={24} />
            </a>
            <a
              href="https://github.com"
              className="hover:text-gray-400 transition"
            >
              <FaGithub size={24} />
            </a>
          </div>
        </div>
      </div>

      {/* Dòng cuối */}
      <div className="text-center mt-8 border-t border-gray-700 pt-4">
        <p>© 2026 TaskManager. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
