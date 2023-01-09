<template>
  <div>
    <el-row>
      <h1 class="hh">蛋糕商城后台管理系统</h1>
      <!-- <el-avatar :size="size" :src="circleUrl"></el-avatar> -->
    </el-row>
    <el-tabs
      v-model="activeName"
      @tab-click="handleClick"
      style="text-align: center"
    >
      <el-tab-pane
        class="el-icon-user-solid"
        label="用户管理"
        name="userManager"
        >用户管理</el-tab-pane
      >
      <el-tab-pane
        class="el-icon-edit-outline"
        label="订单管理"
        name="orderManager"
        >订单管理</el-tab-pane
      >
      <el-tab-pane class="el-icon-burger" label="蛋糕管理" name="cakeManager"
        >蛋糕管理</el-tab-pane
      >
      <el-tab-pane class="el-icon-burger" label="分类统计" name="CatCat"
        >分类统计</el-tab-pane
      >
      <el-tab-pane class="el-icon-burger" label="蛋糕统计" name="CakeCat"
        >蛋糕统计</el-tab-pane
      >
    </el-tabs>
  </div>
</template>
<script>
export default {
  data() {
    return {
      // size: "large",
      activeName: "userManager",
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab.name);
      var name = tab.name;
      if (name == "userManager") {
        this.$router.push({ name: "users" });
      } else if (name == "cakeManager") {
        this.$router.push({ name: "cakes" });
      } else if (name == "CatCat") {
        this.$router.push({ name: "catChart" });
      } else if (name == "CakeCat") {
        this.$router.push({ name: "cakeChart" });
      } else if (name == "orderManager") {
        this.$router.push({ name: "orders" });
      }
    },
  },
};
</script>

<style>
.hh {
  text-align: center;
  color: red;
}
</style>
