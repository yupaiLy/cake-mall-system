import request from './request.js'
import mockRequest from './mockRequest.js'
import { getAccessToken } from '@/utils/token.js'
// import {  } from '@/utils/token'
// mock请求首页轮播图
export const reqBanners = () => mockRequest.get('/banner')
// 用户注册
export const reqRegister = data => request({ url: '/users', method: 'post', data })
// 用户登陆
export const reqLogin = data =>
  request({
    url: '/users/doLogin',
    method: 'post',
    data
  })
// 查询商品所有种类
export const reqGoodsCategory = () => request({ url: '/categorys', method: 'get' })
// 模糊查询商品
export const reqSearchGoods = data => request({ url: '/cakes/choose', method: 'post', data })
// 管理员身份认证
export const reqAuthentication = () => request({ url: '/users/getInfo', method: 'get' })
// 查询所有热销商品
export const reqHotGoods = () => request({ url: '/cakes/getHotCake', method: 'get' })
// 查询所有新品
export const reqNewGoods = data => request({ url: '/cakes/choose', method: 'post', data })

// 根据商品类型查询数据
export const reqGoodsByCategory = data => request({ url: '/categorys/getCakeByCatName', method: 'post', data })
// 更新个人信息
export const reqProfileInfo = () => request({ url: '/users/getInfo?username=' + getAccessToken(), method: 'get' })
// 商品添加到购物车
export const reqAddToShopCart = data => request({ url: '/carts/detailsCart', method: 'post', data })
// 查看个人购物车
export const reqProfileShopCart = () => request({ url: '/carts/getCartInfo?username=' + getAccessToken(), method: 'get' })
// 查看个人订单
export const reqProfileOrder = () => request({ url: '/orders/' + getAccessToken(), method: 'get' })
//修改订单状态
export const requpdateStatus = data => request({ url: '/orders/updateStatus', method: 'put', data })

// 修改购物车中商品的数量
export const reqChangeGoodsNum = data => request({ url: '/carts/shoppingCart', method: 'post', data })
// 删除购物车中的商品
export const reqDeleteGoods = data => request({ url: '/carts/cleanOneCart', method: 'post', data })
// 提交订单
export const reqSubmitOrder = data => request({ url: '/orders', method: 'post', data })
// 清空购物车生成订单
export const reqGenerateOrder = () => request({ url: '/carts/cleanAllCart?username=' + getAccessToken(), method: 'get' })
/* 管理员接口 */

// 所有商品种类
export const reqAllGoodsCategory = () => request({ url: '/categorys', method: 'get' })