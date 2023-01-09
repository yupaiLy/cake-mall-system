
import Desk from '../views/Desk/index.vue'
const routes = [
	{ path: '/', redirect: '/desk' },

	{
		path: '/desk',
		name: 'desk',
		component: Desk,
		redirect: { name: 'home' },
		children: [
			{
				path: 'home',
				name: 'home',
				component: () => import('../views/Desk/Home.vue')
			},
			{
				path: 'login',
				name: 'login',
				component: () => import('../views/Desk/Login.vue')
			},
			{
				path: 'hot',
				name: 'hot',
				component: () => import('../views/Desk/Hot.vue')
			},
			{
				path: 'shopcart',
				name: 'shopcart',
				meta: { isAuth: true },
				component: () => import('../views/ShopCart/index.vue')
			},
			{
				path: 'pay/:username/:totalPrice',
				name: 'pay',
				meta: { isAuth: true },
				component: () => import('../views/ShopCart/Pay.vue')
			},
			// {
			// 	path: 'detail/:id',
			// 	name: 'detail',
			// 	component: () => import('../views/Desk/GoodsDetail.vue')
			// },
			{
				path: 'goodscategory',
				name: 'goodscategory',
				component: () => import('../views/Desk/Category.vue')
			},
			{
				path: 'search/:keyword',
				name: 'search',
				component: () => import('../views/Desk/Search.vue')
			},
			{
				path: 'profile',
				name: 'profile',
				meta: { isAuth: true },
				component: () => import('../views/Profile/index.vue')
			}
		]
	}
]
export default routes
