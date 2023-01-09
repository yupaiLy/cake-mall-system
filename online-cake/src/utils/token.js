export function getAccessToken() {

	let access_token = window.sessionStorage.getItem('access_token')
	access_token = decodeURIComponent(access_token)
	return access_token ? access_token : null
}

export function setAccessToken(access_token) {
	access_token = encodeURIComponent(access_token)
	window.sessionStorage.setItem('access_token', access_token)
}

export function getRefreshToken() {
	let refresh_token = window.localStorage.getItem('refresh_token')
	return refresh_token ? refresh_token : null
}

export function setRefreshToken(refresh_token) {
	window.localStorage.setItem('refresh_token', refresh_token)
}
