import axios from 'axios'
// 引入进度条
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
const request = axios.create({
	baseURL: '/mock',
	timeout: 5000
})

request.interceptors.request.use(config => {
	nprogress.start()
	// 请求拦截器
	return config
})

request.interceptors.response.use(
	res => {
		nprogress.done()
		return res.data
	},
	err => {
		return new Promise.reject('fail' + err)
	}
)

export default request
