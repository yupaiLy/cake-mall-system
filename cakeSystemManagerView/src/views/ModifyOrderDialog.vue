<template lang="">
  <div>
    <!-- 修改订单对话框表单 -->
    <el-dialog title="修改订单" :visible.sync="status" width="50%">
      <span>
        <el-form
          ref="order"
          :model="order"
          :rules="rules"
          label-width="80px"
          v-loading="modifyLoading"
        >
          <el-form-item label="账号" prop="userId">
            <el-input
              v-model="order.userId"
              prefix-icon="el-icon-user-solid"
              placeholder="下单人账号"
            ></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="order.status"
              clearable
              placeholder="请选择状态"
            >
              <el-option
                v-for="item in orderOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="收货人" prop="name">
            <el-input v-model="order.name" placeholder="收货人姓名"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input
              v-model="order.address"
              placeholder="收货人地址"
            ></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="order.phone" placeholder="收货人电话"></el-input>
          </el-form-item>
          <el-form-item label="总金额" prop="total">
            <el-input v-model="order.total" placeholder="应付金额"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="modifyOrder('order')"
              >立即修改</el-button
            >
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { PutUpdateOrderAPI } from "../request/api";

export default {
  data() {
    var valiNumberPass1 = (rule, value, callback) => {
      //包含小数的数字
      let reg = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g;
      if (value === "") {
        callback(new Error("请输入内容"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入数字"));
      } else {
        callback();
      }
    };
    var valiNumberPass2 = (rule, value, callback) => {
      //正整数
      let reg = /^[+]{0,1}(\d+)$/g;
      if (value === "") {
        callback(new Error("请输入内容"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入0及0以上的整数"));
      } else {
        callback();
      }
    };
    var checkTel = (rule, value, callback) => {
      //正整数
      let reg = /^1(3\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/g;
      if (value === "") {
        callback(new Error("请输入内容"));
      } else if (!reg.test(value)) {
        callback(new Error("请输入11位手机号"));
      } else {
        callback();
      }
    };
    return {
      status: false,
      // 修改后打转提示
      modifyLoading: false,
      orderOptions: [
        { value: "0", label: "未支付" },
        { value: "1", label: "已支付" },
        { value: "2", label: "未发货" },
        { value: "3", label: "已发货" },
        { value: "4", label: "已收货" },
        { value: "5", label: "已完结" },
        { value: "6", label: "已取消" },
      ],

      // order模型
      order: {
        id: "",
        code: "",
        userId: "",
        status: "",
        address: "",
        name: "",
        phone: "",
        total: "",
      },
      rules: {
        userId: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 2,
            max: 16,
            message: "长度在 2 到 16 个字符",
            trigger: "blur",
          },
        ],
        name: [
          { required: true, message: "请输入收货人姓名", trigger: "blur" },
          {
            min: 1,
            max: 8,
            message: "长度在 1 到 8 个字符",
            trigger: "blur",
          },
        ],
        address: [
          { required: true, message: "请输入收货地址", trigger: "blur" },
          {
            min: 5,
            max: 100,
            message: "长度在 5 到 100 个字符",
            trigger: "blur",
          },
        ],
        total: [
          {
            required: true,
            trigger: "blur",
            validator: valiNumberPass1,
          },
        ],
        status: [
          {
            required: true,
            message: "请选择状态",
            trigger: "change",
          },
        ],
        phone: [
          {
            required: true,
            trigger: "blur",
            validator: checkTel,
          },
        ],
      },
    };
  },
  methods: {
    showNotify(title, message, type) {
      this.$notify({
        title: title,
        message: message,
        type: type,
      });
    },

    //修改订单
    modifyOrder(order) {
      this.$refs[order].validate((valid) => {
        if (valid) {
          this.modifyLoading = true;
          PutUpdateOrderAPI(this.order)
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", resp.data.code.msg, "success");
                this.$emit("reloadView"); // 触发父组件刷新事件
              } else if (resp.data.code.code == 20001) {
                this.showNotify("失败", resp.data.code.msg, "error");
              }
            })
            .catch((resp) => {
              this.$message.error("网络错误, 请稍后再试");
            })
            .finally((resp) => {
              this.modifyLoading = false;
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>
<style lang=""></style>
