<template lang="">
  <div>
    <div id="Cat" :style="{ width: '100%', height: '500px' }"></div>
  </div>
</template>
<script>
import * as echarts from "echarts";
import {
  getCakeTotalNumInCategoryAPI,
} from "../request/api";
export default {
  data() {
    return {
      data: [],
      myChart: null,
    };
  },
  mounted() {
    this.drawCat();
  },
  methods: {
    //通知
    showNotify(title, message, type) {
      this.$notify({
        title: title,
        message: message,
        type: type,
      });
    },
    drawCat() {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById("Cat"));
      // 绘制图表
      this.myChart.setOption({
        color: [
          "#dd6b66",
          "#759aa0",
          "#e69d87",
          "#8dc1a9",
          "#ea7e53",
          "#eedd78",
          "#73a373",
          "#73b9bc",
          "#7289ab",
          "#91ca8c",
          "#f49f42",
        ],
        tooltip: {},
        title: {
          text: "分类销量统计玫瑰图",
        },
        series: [
          {
            type: "pie",
            data: [],
            roseType: "area",
          },
        ],
      });
      getCakeTotalNumInCategoryAPI()
        .then((resp) => {
          if (resp.data.code.code == 20000) {
            this.showNotify("查询数据成功", resp.data.code.msg, "success");
            this.data = resp.data.data;
            this.myChart.setOption({
              series: [
                {
                  type: "pie",
                  data: this.data,
                  roseType: "area",
                },
              ],
            });
          }
        })
        .catch(() => {
          this.showNotify("查询数据失败", resp.data.code.msg, "error");
        });
    },
  },
};
</script>

<style lang=""></style>
