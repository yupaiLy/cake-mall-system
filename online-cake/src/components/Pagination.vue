<template>
  <div class="pagination">
    <button :disabled="pageNo<=1" @click="$emit('getPageNo',pageNo-1)">上一页</button>
    <button v-if="startNumAndEndNum.start>1" @click="$emit('getPageNo',1)" :class="{active:pageNo===1}">1</button>
    <button v-if="startNumAndEndNum.start>2 ">···</button>

    <button
      v-for="(page,index) in startNumAndEndNum.end"
      :key="index"
      v-show="page>=startNumAndEndNum.start"
      @click="$emit('getPageNo',page)"
      :class="{active:pageNo===page}"
    >{{page}}</button>

    <button v-if="startNumAndEndNum.end<totalPage-1">···</button>
    <button v-if="startNumAndEndNum.end<totalPage" @click="$emit('getPageNo',totalPage)" :class="{active:pageNo===totalPage}">{{totalPage}}</button>
    <button @click="$emit('getPageNo',pageNo+1)" :disabled="pageNo>=totalPage">下一页</button>
    <button style="margin-left: 30px">共 {{total}} 条</button>
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  //        3           
  props: ['total', 'pageSize', 'pageNo', 'continues'],
  computed: {
    // 总页面
    totalPage() {
      return Math.ceil(this.total / this.pageSize)
    },
    //计算连续的页码的起始和结束数字
    startNumAndEndNum() {
      const { continues, pageNo, totalPage } = this
      let start = 0,
        end = 0
      // 不正常现象,连续的页数比总页数要多
      if (continues > totalPage) {
        start = 1
        end = totalPage
      } else {
        // 正常情况下
        start = pageNo - parseInt(continues / 2)
        end = pageNo + parseInt(continues / 2)
        // 起始页只能大于1
        if (start < 1) {
          start = 1
          end = continues
        }
        // 终止页也只能小于总页码
        if (end > totalPage) {
          end = totalPage
          start = totalPage - continues + 1
        }
      }
      return { start, end }
    }
  }
}
</script>

<style lang="less" scoped>
.pagination {
  text-align: center;
  button {
    margin: 0 5px;
    background-color: #f4f4f5;
    color: #606266;
    outline: none;
    border-radius: 2px;
    padding: 0 4px;
    vertical-align: top;
    display: inline-block;
    font-size: 13px;
    min-width: 35.5px;
    height: 28px;
    line-height: 28px;
    cursor: pointer;
    box-sizing: border-box;
    text-align: center;
    border: 0;
    &[disabled] {
      color: #c0c4cc;
      cursor: not-allowed;
    }
    &.active {
      cursor: not-allowed;
      background-color: @fontColor;
      color: #fff;
    }
  }
}
.active {
  background-color: skyblue;
}
</style>