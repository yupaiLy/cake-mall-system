<template>
  <div class="category">
    <div class="header">
      <span class="title">分类:</span>
      <span
        v-for="(item,index) in categoryList"
        :key="item.id"
        :class="{active:currentIndex==index}"
        @click="categoryClick(item.name,index)">{{item.name}}</span>
    </div>
    <div class="main">
      <div class="cake" v-for="item in goodsList" :key="item.id">
        <img :src="item.imgUrl" alt class="back"  />
        <span class="title">{{item.name}}</span>
        <p class="desc">{{item.description}}</p>
        <span class="price">￥ {{item.price}}/一份</span>
        <div class="add-cart">
          <svg
            t="1639824237437"
            lass="icon"
            viewBox="0 0 1025 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1562"
            width="28"
            height="28"
          >
            <path
              d="M756.685272 66.086238c-111.451578 0-201.814873 87.405798-201.814873 195.188647 0 107.809456 90.363295 195.187624 201.814873 195.187624 111.476138 0 201.810779-87.379191 201.810779-195.187624C958.497075 153.491013 868.16141 66.086238 756.685272 66.086238zM756.685272 411.90869c-86.003802 0-155.70146-67.433997-155.70146-150.633805 0-83.200831 69.697658-150.658365 155.70146-150.658365 86.001755 0 155.726021 67.45651 155.726021 150.658365C912.409246 344.475716 842.688051 411.90869 756.685272 411.90869zM821.007245 796.503003c-1.5555-0.32031-3.162168-0.490187-4.81284-0.490187L417.735675 796.012816l-18.169673-67.016468 335.668721-24.971916c12.850272-0.935347 22.557821-11.433949 22.278445-23.747983l29.81955-185.661209c-10.027858 1.343665-20.255271 2.051827-30.644374 2.051827-6.184136 0-12.305847-0.252769-18.368204-0.737839l-26.183568 163.020497-325.014569 24.134811-85.406162-315.013318 248.455313 0c-7.391695-14.728129-13.293385-30.354764-17.522913-46.671141l-241.818854 0c-0.588429 0-1.172765 0.027631-1.748914 0.067541l-28.967095-106.840339c-0.383758-12.56578-11.052237-22.639689-24.132764-22.639689L87.978877 191.987592c-13.323062 0-24.122531 10.446411-24.122531 23.335571 0 12.890183 10.802538 23.334547 24.122531 23.334547l128.761632 0 151.115805 557.358176-45.213883 0c-1.539126 0-3.042435 0.14941-4.497646 0.425716-2.851068-0.279376-5.744093-0.425716-8.674983-0.425716-46.644534 0-84.448302 36.555274-84.448302 81.697523 0 45.092104 37.802744 81.673985 84.448302 81.673985 46.642487 0 84.448302-36.581881 84.448302-81.673985 0-13.382417-3.345348-25.998341-9.232711-37.143703l10.528279 0c2.203284 0.30496 4.475133 0.315193 6.758238 0l334.597268 0c-5.89248 11.145362-9.233735 23.761286-9.233735 37.143703 0 45.092104 37.803768 81.673985 84.474909 81.673985 46.61895 0 84.448302-36.581881 84.448302-81.673985C896.259676 835.576755 863.302519 800.937202 821.007245 796.503003zM309.473895 922.238574c-25.448799 0-46.082712-19.920634-46.082712-44.530282s20.633913-44.58145 46.082712-44.58145 46.057128 19.974872 46.057128 44.58145S334.922695 922.238574 309.473895 922.238574zM811.811375 922.238574c-25.447776 0-46.059175-19.920634-46.059175-44.530282s20.611399-44.58145 46.059175-44.58145c25.424239 0 46.059175 19.974872 46.059175 44.58145S837.23459 922.238574 811.811375 922.238574zM859.246962 240.029006l-80.576949 0 0-78.004233c0-7.820481-6.547427-14.163237-14.648308-14.163237l-14.647284 0c-8.07939 0-14.647284 6.342756-14.647284 14.163237l0 78.004233-80.551365 0c-8.102927 0-14.674915 6.343779-14.674915 14.164261l0 14.188821c0 7.821504 6.571988 14.189844 14.674915 14.189844l80.551365 0 0 77.978649c0 7.818434 6.569941 14.188821 14.647284 14.188821l14.647284 0c8.10088 0 14.648308-6.370387 14.648308-14.188821l0-77.978649 80.576949 0c8.07325 0 14.620677-6.369363 14.620677-14.189844l0-14.188821C873.869686 246.372785 867.320212 240.029006 859.246962 240.029006z"
              p-id="1563"
              fill="#684029"
            />
          </svg>
          &nbsp;&nbsp;
          <button @click="addToShopCart(item.id)">加入购物车</button>
        </div>
      </div>
    </div>
    <Pagination :pageSize="pageSize" :total="total" :pageNo="currentPage" :continues="2" @getPageNo="getPageNo" />
  </div>
</template>

<script>
import { reactive, toRefs,onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'
import { reqGoodsByCategory, reqGoodsCategory, reqAddToShopCart } from '@/api/index.js'
import { getAccessToken } from '@/utils/token'
export default {
  setup() {
    const state = reactive({
      categoryList: [],
      currentIndex: 1, //默认的选中状,切换商品分类,
      goodsList: [],
      currentPage:1,
      pageSize:4,
      total:'',
      name:''
    })
    const data = {
        currentPage: state.currentPage,
        pageSize: state.pageSize,
        name: ''
      }
    

    onBeforeMount(()=>{
      reqGoodsCategory().then(res => {
      state.categoryList = res.data.data
      console.log(res);
      state.name=res.data.data[0].name
      categoryClick(state.name,1)
    })
    })

    function getPageNo(num){
      state.currentPage=num
      data.currentPage=num
      reqGoodsByCategory(data).then(res=>{
        state.goodsList=res.data.data
        state.total=res.data.data_num
      })
    }

    // 点击切换查询商品种类
    function categoryClick(name, index) {
      data.currentPage=1
      state.currentPage=1
      state.currentIndex = index
      data.name=name
      reqGoodsByCategory(data).then(res => {
        console.log(res);
        state.goodsList = res.data.data
        state.total=res.data.data_num
      })
    }
    const router = useRouter()
    // function goDetail(id, index) {
    //   state.currentIndex = index
    //   router.push({ name: 'detail', params: { id } })
    // }
    // 默认执行第一

    // 加入到购物车
    const addToShopCart = cakeId => {
      if(getAccessToken()) {
        const  username=getAccessToken()
        const num=1
        reqAddToShopCart({ cakeId,username,num },getAccessToken()).then(res => {
          if (res.data.code.code==20000) {
            alert('加入成功')
          } else {
            alert('加入失败')
          }
        })
      } else  {
        alert('请登录')
        router.push({name:'login'})
      }
    }

    return {
      ...toRefs(state),
      categoryClick,
      addToShopCart,
      getPageNo
    }
  }
}
</script>

<style lang="less" scoped>
.category {
  width: 1200px;
  margin: 20px auto;
  .header {
    border-bottom: 1px solid #ccc;
    border-top: 1px solid #ccc;
    padding: 30px 0;
    font-size: 14px;
    color: @fontColor;
    span {
      margin-right: 40px;
      cursor: pointer;
    }
    .active {
      color: @hoverColor;
    }
    .title {
      color: #9b9b9b;
    }
  }
  .main {
    margin-top: 20px;
    display: flex;
    justify-content: space-around;
    align-content: center;
    flex-wrap: wrap;
    .cake {
      margin-left: 20px;
      width: 400px;
      height: 364px;
      box-sizing: border-box;
      padding: 0 10px;
      display: flex;
      flex-direction: column;
      align-items: center;
      .back {
        cursor: pointer;
        height: 200px;
        width: 200px;
        background-color: #fff;
      }
      .title {
        margin-top: 10px;
        color: @fontColor;
      }
      .price {
        margin: 4px 0;
        color: @hoverColor;
      }
      .add-cart {
        position: relative;
        text-align: right;
        width: 140px;
        height: 38px;
        line-height: 28px;
        text-align: center;
        color: @fontColor;
        cursor: pointer;
        svg {
          position: absolute;
          top: 27px;
          left: -25px;
        }
        button {
          margin-top:-30px;
        height:80px;
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
}
//
.active {
  color: @hoverColor !important;
}
</style>
