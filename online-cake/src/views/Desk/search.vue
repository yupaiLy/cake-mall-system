<template>
  <div class="search">
    <div class="main" v-if="goodList.length !== 0">
      <div class="cake" v-for="item in goodList" :key="item.id">
          <img :src="item.imgUrl" style="width:150px;height: 150px">
          <span class="title">{{ item.name }}</span>
          <span class="desc">{{ item.description }}</span>

        <div class="footer">
          <span class="price">￥{{ item.price }}</span>
          <button @click="addCart(item)">加入购物车</button>
        </div>
      </div>
      <!-- 分页器 -->
      <Pagination :pageSize="pageSize" :total="total"  :pageNo="currentPage" :continues="2" @getPageNo="getPageNo" />
    </div>
    <div class="no-data" v-else>
      <h2>没有找到该商品，请重新搜索试试~~</h2>
    </div>
  </div>
</template>

<script>
import { reactive, toRefs, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { reqSearchGoods,reqAddToShopCart } from "../../api/index.js";
import { getAccessToken } from "@/utils/token.js";

export default {
  setup() {
    const route = useRoute();
    const data = reactive({
      currentPage: 1,
      pageSize: 4,
      name: "",
    });

    const state = reactive({
      goodList: [],
      currentPage: data.currentPage,
      pageSize: data.pageSize,
      isShowLoading: false,
      total:''
    });

    function getGoods() {
      data.name = route.params.keyword;
      reqSearchGoods(data).then((res) => {
        console.log(res);
        state.goodList = res.data.data;
        state.total= res.data.data_num
      });
    }
    function getPageNo(num){
      data.currentPage=num
      state.currentPage=num
      getGoods()
    }

    const data1 = reactive({
      username: "",
      cakeId: "",
      num:1
    });
    // function alertInfoMsg(msg, type) {
    //   alert(msg);
    //   obj.type = type;
    //   obj.isShowAlert = true;
    //   setTimeout(() => {
    //     obj.isShowAlert = false;
    //   }, 2000);
    // }
    const router = useRouter();

    function addCart(item) {
      if (getAccessToken()) {
        data1.username = getAccessToken();
        data1.cakeId = item.id;
        reqAddToShopCart(data1).then((res) => {
          console.log(res);
          if (res.data.code.code == 20000) {
            // 加入购物车成功
            // alertInfoMsg("加入购物车成功", "success");
            alert(res.data.code.msg)
          } else {
            alert(res.data.code.msg);
          }
        });
      } else {
        alert("请登录");
        router.push({ name: "login" });
      }
    }

    watchEffect(() => {
      getGoods();
    });

    return {
      ...toRefs(state),
      addCart,
      getPageNo
    };
  },
};
</script>

<style lang="less" scoped>
.search {
  width: 1200px;
  margin: 20px auto;
}
.main {
  .cake {
    box-sizing: border-box;
    align-items: left;
    img {
      width: 100%;
    }
    .title {
      font-size: 18x;
      color: @fontColor;
      margin: 50px;
    }
    .desc {
      color: rgb(184, 171, 171);
    }
    .footer {
      margin-top: 15px;
      display: flex;
      width: 100%;
      justify-content: space-between;
      color: @hoverColor;
      div {
        width: 100px;
        background-color: @fontColor;
        color: #fff;
        font-size: 15px;
        text-align: center;
      }
      .price{
        margin-left: 47px;
      }
      button {
        height: 95px;
        width: 155px;
        transition: 0.4s;
        border: 1px solid #fff;
        border-width: 25px;
        border-radius: 7px ;
      }
      button:before{
        background-color: #fff;
      }
      button:hover {
        background-color: #466eae; /* Green */
        color: white;
      }
    }
  }
  .no-data {
    h2 {
      color: @fontColor;
      text-align: center;
    }
  }
}
</style>
