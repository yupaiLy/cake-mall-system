import Vue from "vue";
import App from "./App.vue";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "./plugins/element.js";
Vue.use(ElementUI);

import VueCookies from "vue-cookies";
Vue.use(VueCookies);

// 引入router中的index.js文件
import router from "./router";

Vue.config.productionTip = false;
new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
