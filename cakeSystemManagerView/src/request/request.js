// 做拦截

import axios from "axios";

// 创建一个实例
const instance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 3000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 部分接口需要token
    let token = localStorage.getItem("token");
    if (token) {
      config.headers = {
        "xxx-token": token,
      };
    }
    return config;
  },
  (err) => {
    return Promise().reject(err);
  }
);

// 响应拦截器
instance.interceptors.request.use(
  (res) => {
    return res;
  },
  (err) => {
    return Promise().reject(err);
  }
);

export default instance;
