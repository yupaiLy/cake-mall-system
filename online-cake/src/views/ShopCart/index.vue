<template>
  <div class="shop-cart">
    <div class="no-goods-show" v-if="shopCartList.length==0">
      <img src="../../assets/images/cartempty.png" alt />
      <span>您的购物车里还没有商品</span>
    </div>
    <div class="main" v-else>
      <div class="header">
        <li class="goods-ti">商品</li>
        <li class="birthday-card-ti"></li>
        <li class="unit-Price" style="width:115px">单价</li>
        <li class="number-ti" style="width:150px">数量</li>
        <li class="money-ti" colspan="2" style="width:180px">金额</li>
      </div>

      <tbody>
        <tr id="12799" class="cart-object-item" v-for="item in shopCartList" :key="item.cakeId">
          <td class="goods-img">
            <img :src="item.cakeImgUrl" />
          </td>
          <td class="goods-cake">
            <div>
              <p class="cart-tips">最早配送时间{{time}}</p>
              <h4 class="cart-title">
                <a>{{item.cakeName}}</a>
              </h4>
              <span class="goods-spec">
                规格：
                <span>40g(4包)</span>
              </span>
            </div>
          </td>
          <td class="select-birthday-td"></td>
          <td class="cart-unit-Price">¥{{item.price}}</td>
          <td class="number-li">
            <div class="number quantity-update">
              <button @click="changeGoodsNum(item.cakeId,-1,item.num)">-</button>
              <input @blur="changeNum(item.cakeId,item.num)" oninput="if(!/^[0-9]+$/.test(value)) value=value.replace(/\D/g,'');if(value>100)value=100;if(value<0)value=null" type="text"  class="quantity _quantity" data-indent="12799" data-price="40.000" data-max="99" v-model="item.num" />
              <button @click="changeGoodsNum(item.cakeId,1,item.num)">+</button>
            </div>
          </td>
          <td class="money" id="total_amount_12799">¥{{item.price*item.num}}</td>
          <td class="delete" @click="deleteGoods(item.cakeId)">X</td>
        </tr>
        <!-- 普通商品结束 -->
      </tbody>

      <!-- 结算 -->
      <div class="cart-submit cart-area">
        <button  @click="empty"><i></i>全部清空</button>
        <ul class="cart-total-detail">
          <li>商品金额：¥ {{totalPrice}}</li>
          <!--- <li>配送费：¥ 12.00</li> --->
          <li>活动优惠：¥ 0.00</li>
        </ul>
        <div class="cart-total">
          <span>
            合计：¥
            <span>{{totalPrice}}</span>
          </span>
        </div>
        <span class="cart-balance" id="cost-freight-tip"></span>
        <div class="cart-notice-ac">
          <span></span>
        </div>
        <div class="cart-submit-button">
          <button @click="submit">去结算</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getAccessToken } from '@/utils/token.js'
import { defineComponent, reactive, computed, toRefs , onBeforeMount} from 'vue'
import { useRouter } from 'vue-router'
import { reqProfileShopCart, reqChangeGoodsNum, reqDeleteGoods, reqGenerateOrder } from '../../api/index.js'
export default defineComponent({
  computed: {},
  setup() {
    const state = reactive({
      shopCartList: [],
      total: 0
    })
    const router = useRouter()
    // 计算总价格
    const totalPrice = computed(() => {
      return state.shopCartList.reduce((pre, cur): number => {
        return pre + cur.price * cur.num
      }, 0)
    })

    const time=Time()

    function Time(){
      const today=new Date()
      const time= today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
      return time
    }
    // 初始化购物车数据
    function getShopCartInfo() {
      reqProfileShopCart().then(res => {
        console.log(res, 'init')
        state.shopCartList = res.data.data
      })
    }
    const username=getAccessToken();

    onBeforeMount(()=>{
      getShopCartInfo()
    })


    function changeNum(cakeId,num){
      reqChangeGoodsNum({ username, cakeId, num }).then(res=>{
        console.log(res);
        if (res.data.code.code==20000) {
          getShopCartInfo()
        }else{
          alert(res.data.code.msg)
        }
        
      })
    }
    // 修改商品的个数
    function changeGoodsNum(cakeId, changeNum, num) {
      if (changeNum == -1 && num <= 1) {
        return alert('当前数量无法继续操作')
      }else{
        num=num+changeNum
      reqChangeGoodsNum({ username, cakeId, num }).then(res => {
        console.log(res);
        
        if (res.data.code.code==20000) {
          getShopCartInfo()
        }else{
          alert(res.data.code.msg)
        }
      })
    }
    }
    // 删除商品
    function deleteGoods(cakeId) {
      reqDeleteGoods({ username,cakeId }).then(res => {
        getShopCartInfo()
      })
    }

    //清空购物车
    function empty(){
      reqGenerateOrder().then(res=>{
        if (res.data.code.code) {
          alert(res.data.code.msg)
          getShopCartInfo()
        }
      })
    }
    // 发起订单
    function submit() {
      // reqGenerateOrder().then(res => {
      //   console.log(res)
      // })
      const username=getAccessToken()
      router.push({ name: 'pay', params: { username: username, totalPrice: totalPrice.value } })
    }
    return {
      ...toRefs(state),
      changeGoodsNum,
      totalPrice,
      deleteGoods,
      empty,
      changeNum,
      submit,
      time,
      Time
    }
  }
})
</script>

<style scoped lang="less">
.shop-cart {
  width: 1200px;
  margin: 40px auto 0;
  .no-goods-show {
    height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    a {
      margin-top: 10px;
      color: @fontColor;
    }
    a:hover {
      color: @hoverColor;
    }
  }
  .header {
    background: #fafafa;
    font-size: 13px;
    width: 1252px;
    overflow: hidden;
    line-height: 44px;
    border: 1px solid #e1e1e1;
    li {
      float: left;
      height: 44px;
    }
    .goods-ti {
      margin-left: 74px;
      width: 341px;
    }
    .birthday-card-ti {
      width: 385px;
    }
  }
  // 购物车商品
  tbody {
    width: 1200px;
  }
  .cart-object-item {
    width: 1200px !important;
    position: relative;
    img {
      width: 140px;
      border: 1px solid #efefef;
    }
    .goods-cake {
      height: 134px;
      padding-left: 20px;
      p {
        background-image: linear-gradient(180deg, #ceab78 50%, #c69c6d 100%);
        margin-bottom: 0;
        display: inline-block;
        color: #fff;
        padding: 0 6px;
        font-size: 12px;
        line-height: 18px;
        margin-bottom: 2px;
      }
    }
    a:visited,
    a:link,
    a {
      color: #684029;
      text-decoration: none;
      outline: none;
      cursor: pointer;
      -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
      -webkit-tap-highlight-color: transparent;
    }
  }
  .goods-spec {
    font-size: 13px;
    color: #d5bfa7;
    display: block;
  }
  .cart-unit-Price {
    padding-left: 490px;
    font-size: 13px;
  }
  .number-li {
    div {
      margin-left: 54px;
      display: flex;
      // button {
      //   height: 22px;
      //   width: 22px;
      //   font-size: 20px;
      //   background-color: #fff;
      //   border-radius: 5px;
      //   border: 1px solid #d5bfa7;
      // }
      // button:hover{
      //   border:1px solid rgb(63, 180, 195);
      // }
      button {
        width: 22px;
        font-size: 20px;
        height: 22px;
        transition: 0.4s;
        border: 1px solid #fff;
        border-radius: 7px ;
      }
      button:before{
        background-color: #fff;
      }
      button:hover {
        background-color: #466eae; /* Green */
        color: white;
      }
      input {
        height: 22px;
        width: 48px;
        border: solid #e7e7e7;
        border-width: 1px 0;
        padding-left: 20px;
        box-sizing: border-box;
      }
    }
  }
  .money {
    display: block;
    width: 70px;
    text-align: center;
    line-height: 134px;
    height: 134px;
    color: red;
    margin-left: 63px;
  }
  .delete {
    position: absolute;
    right: -77px;
    top: 55px;
    color: #ccc;
    cursor: pointer;
  }

  // 结算
  .cart-submit {
    position: relative;
    width: 1130px;
    height: 180px;
    background: #fbfbfb;
    border: 1px solid #e1e1e1;
    padding: 26px 60px 32px 60px;
    margin: 20px 0;
    li {
      font-size: 13px;
      text-align: right;
      line-height: 18px;
      margin-bottom: 10px;
    }
    button {
        width: 90px;
        font-size: 15px;
        height: 35px;
        transition: 0.4s;
        border: 1px solid #fff;
        border-radius: 7px ;
        background-color: #684029;
        color:#fff
      }

      button:hover {
        background-color: #466eae; /* Green */
        color: white;
      }
    .cart-total {
      text-align: right;
    }
    .cart-total > span {
      display: inline-block;
      width: 232px;
      border-top: 2px solid #e2e2e2;
      line-height: 42px;
      font-size: 13px;
    }
  }
  .cart-submit-button button {
    float: right;
    background: #684029;
    width: 232px;
    height: 40px;
    display: inline-block;
    color: #fff;
    line-height: 40px;
    text-align: center;
    font-size: 14px;
    margin-top: 14px;
  }
  .cart-total span {
    color: red;
  }
  .cart-submit-button a::after {
    display: block;
    clear: both;
  }
}
</style>
