# 蛋糕售卖前后台管理系统

## 代码结构：

## 核心代码部分:
### 无感刷新：
```js
function refreshToken() {
	return axios({
		method: 'post',
		url: 'http://192.168.137.1:13400/omc/api/common/update/accessToken',
		data: {
			refreshToken: getRefreshToken()
		}
	})
}
request.interceptors.request.use(config => {
	nprogress.start()
	if (getAccessToken()) {
		config.headers.authentication = getAccessToken()
	}
	// 请求拦截器
	return config
})
// 是否正在刷新的标记
let isRefreshing = false
// 当token正在刷新的时候，将请求放在请求队列中,token刷新完毕，执行这些请求
let requests = []

request.interceptors.response.use(
	res => {
		if (res.data.code === 400) {
			let config = res.config
			if (!isRefreshing) {
				isRefreshing = true
				return refreshToken()
					.then(res => {
						// 刷新token成功，将最新的token更新到header中，同时保存在localStorage中
						let token = res.data.response.access_token
						setAccessToken(token)
						// 重置一下配置
						config.headers['authentication'] = token
						requests.forEach(cb => cb(token))
						//清空队列
						requests = []
						// 重试当前请求并返回promise
						return request(config)
					})
					.catch(err => {
						console.error('refreshtoken error =>', err)
						//刷新token失败,跳转到首页重新登录吧
						// window.location.href = '/login'
						return Promise.reject(err)
					})
					.finally(() => {
						isRefreshing = false
					})
			} else {
				return new Promise(resolve => {
					requests.push(token => {
						// config.baseURL = ''
						config.headers['authentication'] = token
						resolve(request(config)) // 请求 1 携带自生配重新进入requests请求队列，等待token刷新
					})
				})
			}
		}
		nprogress.done()
		return res
	},
	err => {
		return Promise.reject('fail' + err)
	}
)
```
