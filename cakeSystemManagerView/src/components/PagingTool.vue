<template>
  <div>
    <el-pagination
      background
      :hide-on-single-page="false"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="5"
      layout="total, sizes, prev, pager, next, jumper"
      :total="50"
    >
    </el-pagination>
  </div>
</template>
<script>
export default {
  methods: {
    // 每页条数变化回调方法
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    // 当前页码变化
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  },
  data() {
    return {
      currentPage: 5,
    };
  },
};
</script>
<style lang=""></style>
