<template>
  <div class="profile">
    <div class="header">
      <img src="../../assets/images/鸣人.jpg" alt />
      <span class="name">{{userInfo.name}}</span>
      <!-- <span class="address">收货地址: {{userInfo.address}}</span> -->
    </div>
    <div class="main">
      <h2>个人订单汇总</h2>
      <table>
        <thead class="order-header">
          <tr>
            <th>收货人</th>
            <th>收货地址</th>
            <th>收货电话</th>
            <th>总价</th>
            <th>状态</th>
            <th>下单时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr class="order-list" v-for="item in orderList" :key="item.id">
            <td>{{item.name}}</td>
            <td>{{item.address}}</td>
            <td>{{item.phone}}</td>
            <td class="total">{{item.total}}</td>
            <td class="status">{{item.status}}</td>
            <td class="orderTime">{{ item.orderTime }}</td>
            <td><button :disabled="(item.status=='未支付')?false:true" @click="deleteOrder(item.code)">取消订单</button>
            <button :disabled="(item.status=='未支付')?false:true" @click="PayOrder(item.code)">支付订单</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { reqProfileInfo,reqProfileOrder,requpdateStatus } from '@/api/index.js'
import { reactive, onMounted, toRefs } from 'vue'
import { getAccessToken } from '@/utils/token'
export default {
  setup() {
    const state = reactive({
      userInfo: {},
      orderList: []
    })
    const data= reactive({
        code:'',
        status:''
      })
    onMounted(() => {
      console.log('profile')
      reqProfileInfo( getAccessToken()).then(res => {
        console.log(res);
        state.userInfo = res.data.data
      })
      // 请求所有订单信息
      reqProfileOrder().then((res) => {
        console.log(res)
        state.orderList = res.data.data
      })
    })
    function deleteOrder(code) {
      data.code=code
      data.status='6'
      console.log(data);
      requpdateStatus(data).then(res=>{
        if(res.data.code.code==20000){
          alert(res.data.code.msg)
          reqProfileOrder().then((res) => {
          state.orderList = res.data.data
        })
        }else{
          alert(res.data.code.msg)
        }
      })
    }
    function PayOrder(code){
      data.code=code
      data.status='1'
      requpdateStatus(data).then(res=>{
        if(res.data.code.code==20000){
          alert(res.data.code.msg)
          reqProfileOrder().then(res => {
          state.orderList = res.data.data
        })
        }else{
          alert(res.data.code.msg)
        }
      })
    }
    return {
      ...toRefs(state),

      deleteOrder,
      PayOrder
    }
  }
}
</script>

<style lang="less" scoped>
.profile {
  width: 1200px;
  margin: 20px auto;
  .header {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    img {
      width: 150px;
      border-radius: 50%;
    }
    .name {
      margin-top: 10px;
      font-weight: 600;
      font-size: 20;
    }
    .address {
      margin-top: 20px;
    }
  }
  .main {
    margin-top: 20px;
    h2 {
      text-align: center;
    }
    table {
      margin-left: -225px;
    }
    .order-header tr {
      width: 1685px;
      display: flex;
      justify-content: space-around;
      th {
        width: 170px;
        align-items: center;
      }
    }
    .order-list {
      height: 50px;
      display: flex;
      justify-content: space-around;
      border-bottom: 1px solid @hoverColor;
      td {
        width: 240px;
        text-align: center;
        line-height: 50px;
        overflow: hidden;
        button{
        height: 40px;
        width: 100px;
        transition: 0.4s;
        border: 1px solid #fff;
        border-radius: 7px ;
        }
        button:before{
        background-color: #fff;
        }
        button:not(.disabled):hover{
        
        background-color: #466eae; /* Green */
        color: white;
      }
      }

      img {
        height: 40px !important;
        vertical-align: middle;
      }
    }
  }
}
</style>