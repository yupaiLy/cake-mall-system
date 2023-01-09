<template>
  <div class="hot">
    <div class="cake" v-for="item in hotList" :key="item.id">
        <img :src="item.imgUrl" />
        <span class="title">{{item.name}}</span>
        <span class="desc">种类:{{item.cid}}</span>
        <p class="intro">{{item.description}}</p>
      <div class="footer">
        <span>￥{{item.price}}</span>
        <button class="add-cart" @click="addToShopCart(item.id)">加入购物车</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import router from '@/router/index.js'
import { getAccessToken } from '@/utils/token.js'
import { defineComponent, onMounted, reactive, toRefs } from 'vue'
import { reqHotGoods, reqAddToShopCart } from '../../api/index.js'
export default defineComponent({
  setup() {
    const state = reactive({
      hotList: []//这里请求所有的数据

    })
    const data1 = reactive({
      username: "",
      cakeId: "",
      num:1
    });
    onMounted(() => {
      reqHotGoods().then(res => {
        console.log(res)
        state.hotList = res.data.data
      })
    })
    function addToShopCart(cakeId) {
      if (getAccessToken()) {
        data1.username = getAccessToken();
        data1.cakeId = cakeId;
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
    return {
      ...toRefs(state),
      addToShopCart
    }
  }
})
</script>

<style scoped lang="less">
.hot {
  display: flex;
  width: 1200px;
  margin: 20px auto 0;
  flex-wrap: wrap;
  .cake {
    width: 320px;
    margin: 20px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: left;
    img {
      width: 320px;
      height: 180px;
    }
    .title {
      font-size: 18x;
      color: @fontColor;
    }
    .desc {
      float: right;
      color: rgb(184, 171, 171);
    }
    .add-cart {
      cursor: pointer;
    }
    .intro {
      font-size: 14px;
      color: @fontColor;
      margin-top: 5px;
    }
    .footer {
      margin-top: 15px;
      display: flex;
      width: 100%;
      justify-content: space-between;
      color: @hoverColor;
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
}
</style>
