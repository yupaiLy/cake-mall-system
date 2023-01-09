import Vue from "vue";
// 1. 引入路由依赖
import VueRouter from "vue-router";
// 2.引入页面
import UserDataTable from "../views/UserDataTable.vue";
import OrderDataTable from "../views/OrderDataTable.vue";
import CakeDataTable from "../views/CakeDataTable.vue";
import CatChart from "../views/CatChart.vue";
import CakeChart from "../views/CakeChart.vue";

Vue.use(VueRouter);
let originPush = VueRouter.prototype.push;
VueRouter.prototype.push = function (localtion, resolve, rejest) {
  if (resolve && rejest) {
    originPush.call(this, localtion, resolve, rejest);
  } else
    originPush.call(
      this,
      localtion,
      () => {},
      () => {}
    );
};

const routes = [
  {
    path: "/users",
    name: "users",
    component: UserDataTable,
  },
  {
    path: "/orders",
    name: "orders",
    component: OrderDataTable,
  },
  {
    path: "/cakes",
    name: "cakes",
    component: CakeDataTable,
  },
  {
    path: "/catChart",
    name: "catChart",
    component: CatChart,
  },
  {
    path: "/cakeChart",
    name: "cakeChart",
    component: CakeChart,
  },
];

const router = new VueRouter({
  mode: "hash",
  routes: routes,
});

export default router;
