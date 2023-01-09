<template>
  <div class="swiper-container">
    <div class="swiper-wrapper">
      <div class="swiper-slide" v-for="item in bannerList" :key="item.id">
        <img :src="item.imgUrl" alt />
      </div>
    </div>
    <!-- 小圆点-->
    <div class="swiper-pagination"></div>
  </div>
</template>

<script>
import Swiper from 'swiper'
import { reactive, onMounted } from 'vue'
import { reqBanners } from '../api/index'
import 'swiper/css/swiper.min.css'
export default {
  name: 'Carousel',
  setup() {
    const state = reactive({
      bannerList: []
    })
    onMounted(() => {
      const mySwiper = new Swiper('.swiper-container', {
        loop: true,
        // autoplay: {
        //   delay: 3000,
        //   stopOnLastSlide: false,
        //   disableOnInteraction: false,
        //   cirular: true
        // },
        autoplay: true,
        /*clickable :true,点击分页器的指示点分页器会控制Swiper切换。 */
        pagination: {
          el: '.swiper-pagination',
          observer: true,
          observeParents: true
        },
        autoplayDisableOnInteraction: false, //滑动之后， 定时器也不会被清除
        observer: true, //修改swiper自己或子元素时，自动初始化swiper
        observeParents: false, //修改swiper的父元素时，自动初始化swiper
        onSlideChangeEnd: function (swiper) {
          swiper.update()
          swiper.startAutoplay()
          swiper.reLoop()
        }
      })
      /* 鼠标滑过pagination控制swiper切换*/
      for (let i = 0; i < mySwiper.pagination.bullets.length; i++) {
        mySwiper.pagination.bullets[i].onmouseover = function () {
          this.click()
        }
        //如果你在swiper初始化后才决定使用clickable，可以这样设置
        mySwiper.params.pagination.clickable = true
        //此外还需要重新初始化pagination
        mySwiper.pagination.destroy()
        mySwiper.pagination.init()
        mySwiper.pagination.bullets.eq(0).addClass('swiper-pagination-bullet-active')
      }
      // 获取轮播图数据
      // reqBanners().then(res => {
      // state.bannerList = res.data.banners;
      state.bannerList = [
        {
          id: '1',
          imgUrl: '../../images/banner1.jpg'
        },
        {
          id: '2',
          imgUrl: '../../images/banner2.jpg'
        },
        {
          id: '3',
          imgUrl: '../../images/banner3.jpg'
        },
        {
          id: '4',
          imgUrl: '../../images/banner4.jpg'
        }
      ]
      // })
    })
    return state
  }
}
</script>
<style scoped lang='less'>
.swiper-container {
  width: vw;
  height: 791px;
  img {
    width: 100%;
  }
  .swiper-wrapper {
    height: 100%;
    width: 100%;
  }
}
.swiper-slide {
  width: 100%;
  height: 791px;
  text-align: center;
  font-size: 18px;
  background: #fff;
  /* Center slide text vertically */
  display: -webkit-box;
  display: -ms-flexbox;
  display: -webkit-flex;
  display: flex;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  -webkit-justify-content: center;
  justify-content: center;
  -webkit-box-align: center;
  -ms-flex-align: center;
  -webkit-align-items: center;
  align-items: center;
}
</style>