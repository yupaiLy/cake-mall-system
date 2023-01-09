import { createRouter, createWebHashHistory } from "vue-router";
import routes from "./route.js";
const router = createRouter({
	history: createWebHashHistory(),
	routes,
});

import { getAccessToken, setAccessToken } from "@/utils/token.js";
import { reqAuthentication } from "../api/index.js";
// 进入管理员界面进行鉴权校验

router.beforeEach(async (to, from, next) => {
	// 用户未登录校验
	if (to.meta.isAuth) {
		if (getAccessToken()) {
			// 登录身份确认
			// if (to.path.indexOf("/back") === 0) {
			// 	let res = await reqAuthentication(getAccessToken());
			// 	if (res.data.code.code == 20000) {
			// 		next();
			// 	} else {
			// 		alert("非管理员无法进入");
			// 		next({ name: "desk" });
			// 	}
			// }
			next()

		} else {
			alert("当前未登录");
			next({ name: "login" });
		}
	}
	if (getAccessToken()) {
		if (to.path.indexOf("/desk/login") === 0) {
			const res = confirm("是否退出？");
			if (res == true) {
				setAccessToken('')
				setInterval(function () {
					window.location.reload();
				}, 100)
				console.log(to.path.indexOf("/desk/login"));
			} else {
				router.push({ name: 'home' })
			}
		}

	}
	console.log(to);

	next();
});
export default router;
