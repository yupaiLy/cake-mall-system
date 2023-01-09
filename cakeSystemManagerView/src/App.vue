<template>
  <div id="app">
    <el-container>
      <el-header height="">
        <HeadBar />
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
      <Footer />
    </el-container>
  </div>
</template>

<script>
import axios from "axios";
import HeadBar from "./components/HeadBar.vue";
import SideBar from "./components/SideBar.vue";
import Footer from "./components/FooterBar.vue";

export default {
  name: "app",
  components: {
    HeadBar,
    SideBar,
    Footer,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
  /* margin-top: 0px; */
}
</style>
