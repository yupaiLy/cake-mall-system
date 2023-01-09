// 整体倒入request.js
import request from "./request";

// 按需导出每个请求

//请求userdata(分页查询)
export const PostSelectUserDataByPageAPI = (params) =>
  request.post("/users/paging", params);
//请求userdata(条件查询)
export const PostSelectUserDataByChooseAPI = (params) =>
  request.post("/users/choose", params);
//请求添加用户(注册)
export const PostSignUpAPI = (params) => request.post("/users", params);
//请求删除用户(单/多)
export const DeleteRemoveUserAPI = (params) => request.delete("/users", params);
//请求修改用户(单)
export const PutUpdateUserAPI = (params) => request.put("/users", params);

// order-----------
//请求orderdata(条件查询)
export const PostSelectOrderDataByChooseAPI = (params) =>
  request.post("/orders/choose", params);
//请求添加订单
export const PostAddOrderAPI = (params) => request.post("/orders", params);
//请求删除订单(单/多)
export const DeleteRemoveOrderAPI = (params) => request.delete("/orders", params);
//请求修改订单(单)
export const PutUpdateOrderAPI = (params) => request.put("/orders", params);

// cake-----------
//请求userdata(条件查询)
export const PostSelectCakeDataByChooseAPI = (params) =>
  request.post("/cakes/choose", params);
//请求添加蛋糕
export const PostAddCakeAPI = (params) => request.post("/cakes", params);
//请求删除蛋糕(单/多)
export const DeleteRemoveCakeAPI = (params) => request.delete("/cakes", params);
//请求修改蛋糕(单)
export const PutUpdateCakeAPI = (params) => request.put("/cakes", params);
//cakeEchart图用
export const getAllCakeNumAPI = (params) => request.get("/cakes/getAllCakeNum", params);

// catgory-----------
//获取所有分类
export const getAllCatAPI = () => request.get("/categorys");
//获取所有分类下商品销量总和
export const getCakeTotalNumInCategoryAPI = () => request.get("/categorys/getCakeTotalNumInCategory");
