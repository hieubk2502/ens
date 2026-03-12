# Web – Dự án Vite + Vue 3

## Giới thiệu
Ứng dụng front-end khởi tạo bằng Vite, dùng Vue 3 làm framework chính. Mục tiêu hiện tại là scaffold cơ bản để phát triển nhanh giao diện.

## Yêu cầu môi trường
- Node.js ^20.19.0 hoặc >=22.12.0
- npm đi kèm Node

## Cài đặt và lệnh npm
- `npm install`: cài phụ thuộc.
- `npm run dev`: chạy môi trường phát triển với HMR.
- `npm run build`: build sản phẩm tĩnh ra `dist/`.
- `npm run preview`: phục vụ thử bản build.

## Cấu trúc thư mục chính
- `src/`: mã nguồn Vue.
- `src/main.js`: entry khởi tạo ứng dụng, mount `App.vue`.
- `src/App.vue`: shell ứng dụng, hiện đang hiển thị các component mẫu.
- `src/assets/`: tệp tĩnh (CSS, hình ảnh). `main.css` import `base.css`.
- `src/components/`: component dùng lại; có nhóm `common/`, `business/`, `icons/` và các component mẫu `HelloWorld.vue`, `TheWelcome.vue`.
- `src/api/`: placeholder cho logic gọi API.
- `src/constants/`: nơi khai báo hằng số, cấu hình tĩnh.
- `src/layouts/`: layout cấp trang.
- `src/router/`: cấu hình Vue Router (chưa tạo).
- `src/stores/`: state management (Pinia/Vuex) nếu thêm sau này.
- `src/utils/`: helper hàm tiện ích.
- `src/views/`: màn hình chính của ứng dụng; đã có thư mục `auth/`, `dashboard/` để phát triển tiếp.
- `public/`: tài nguyên tĩnh phục vụ trực tiếp.
- `index.html`: template HTML gốc Vite dùng để inject bundle.
- `vite.config.js`: cấu hình Vite.
- `package.json` và `package-lock.json`: khai báo package và khóa phiên bản.

## Packages và tác dụng
Dependencies:
- `vue`: core framework xây dựng UI theo component.
- `vue-router`: định tuyến SPA.
- `pinia`: state management.
- `axios`: client HTTP.
- `ant-design-vue`: bộ UI component.
- `dayjs`: xử lý thời gian nhẹ gọn.

DevDependencies:
- `vite`: công cụ build/bundler, cung cấp dev server HMR.
- `@vitejs/plugin-vue`: plugin giúp Vite biên dịch file `.vue`.
- `vite-plugin-vue-devtools`: tích hợp Vue Devtools khi phát triển.
- `typescript`, `vue-tsc`: hỗ trợ TypeScript.
- `vitest`, `@vue/test-utils`, `jsdom`: unit test.
- `eslint`, `eslint-plugin-vue`, `@typescript-eslint/*`: linting.

## Quy trình phát triển gợi ý
1. Chạy `npm install`.
2. Khởi động `npm run dev`, phát triển component trong `src/components` và trang trong `src/views`.
3. Khi cần router/state/API, thêm file tương ứng vào `src/router`, `src/stores`, `src/api`.
4. Build trước khi phát hành với `npm run build` và thử nghiệm bằng `npm run preview`.
