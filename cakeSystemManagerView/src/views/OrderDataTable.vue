<template ref="father">
  <div>
    <!-- 动态查询表单  -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="编号">
        <el-input
          prefix-icon="el-icon-user-solid"
          v-model="searchCode"
          placeholder="订单编号"
        ></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
          prefix-icon="el-icon-s-custom"
          v-model="searchPhone"
          placeholder="收货手机号"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="searchStatus" clearable placeholder="请选择状态">
          <el-option
            v-for="item in orderOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          type="primary"
          @click="selectOrderInfoByChoose"
          >查询</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 批量删除,新增操作区 -->
    <el-row>
      <el-button icon="el-icon-delete" @click="batchDelete" type="danger" plain
        >批量删除</el-button
      >
      <el-button icon="el-icon-edit" type="primary" plain @click="showAdd"
        >添加</el-button
      >
    </el-row>

    <!-- 添加订单对话框表单 -->
    <el-dialog title="添加订单" :visible.sync="addDialogVisible" width="35%">
      <span>
        <el-form
          ref="order"
          :model="order"
          :rules="rules"
          label-width="80px"
          v-loading="addLoading"
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
            <el-button type="primary" @click="addOrder('order')"
              >立即添加</el-button
            >
            <el-button @click="resetOrderForm('order')">重置</el-button>
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>

    <!-- orders表格数据 -->
    <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column align="center" prop="code" label="订单编号">
      </el-table-column>
      <el-table-column align="center" prop="total" label="订单金额">
      </el-table-column>
      <el-table-column align="center" prop="userId" label="下单账号">
      </el-table-column>
      <el-table-column align="center" prop="status" label="订单状态">
      </el-table-column>
      <el-table-column align="center" prop="name" label="收货人">
      </el-table-column>
      <el-table-column align="center" prop="address" label="收货地址">
      </el-table-column>
      <el-table-column align="center" prop="phone" label="收货电话">
      </el-table-column>
      <el-table-column align="center" prop="orderTime" label="下单时间">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button @click="modifyOrder(scope.row)" type="primary"
            >修改</el-button
          >
          <el-button @click="deleteButtonOrder(scope.row)" type="danger"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 修改dialog界面 -->
    <OrderDialog ref="son" @reloadView="selectOrderInfoByChoose"> </OrderDialog>

    <!-- 分页栏 -->
    <el-row>
      <div>
        <el-pagination
          background
          :hide-on-single-page="false"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data_num"
        >
        </el-pagination>
      </div>
    </el-row>
  </div>
</template>

<script>
import PagingTool from "../components/PagingTool.vue";
import OrderDialog from "./ModifyOrderDialog.vue";
import {
  PostAddOrderAPI,
  PostSelectOrderDataByChooseAPI,
  DeleteRemoveOrderAPI,
} from "../request/api";

export default {
  components: {
    PagingTool,
    OrderDialog,
  },

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
      pageSize: 5,
      currentPage: 1,
      data_num: 0,
      // 添加订单数据对话框是否展示
      addDialogVisible: false,
      // 添加后打转提示
      addLoading: false,

      orderOptions: [
        { value: "0", label: "未支付" },
        { value: "1", label: "已支付" },
        { value: "2", label: "未发货" },
        { value: "3", label: "已发货" },
        { value: "4", label: "已收货" },
        { value: "5", label: "已完结" },
        { value: "6", label: "已取消" },
      ],

      // 动态查询
      searchCode: "",
      searchPhone: "",
      searchStatus: "",

      // 多选的id
      ids: [],

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
        orderTime: "",
      },
      // 复选框选中数据集合
      multipleSelection: [],
      // user表格数据
      tableData: [],
      // 添加订单校验规则
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
      loading: true,
    };
  },
  methods: {
    showAdd() {
      this.addDialogVisible = true;
    },
    // 条件查询
    selectOrderInfoByChoose() {
      this.loading = true;
      console.log(this.searchCode);
      PostSelectOrderDataByChooseAPI({
        code: this.searchCode,
        phone: this.searchPhone,
        status: this.searchStatus,
        currentPage: this.currentPage,
        pageSize: this.pageSize,
      })
        .then((resp) => {
          if (resp.data.code.code == 20000) {
            this.data_num = resp.data.data_num;
            this.tableData = resp.data.data;
            this.showNotify("成功", resp.data.code.msg, "success");
          } else if (resp.data.code.code == 20001) {
            this.showNotify("失败", resp.data.code.msg, "error");
          } else if (resp.data.code.code == 30000) {
            // 特殊操作分页越界, 重置
            this.currentPage = 1;
            this.showNotify("成功", "查询成功", "success");
            this.data_num = resp.data.data_num;
            this.tableData = resp.data.data;
          }
        })
        .catch((resp) => {
          this.$message("网络错误!请稍后再试");
        })
        .finally(() => {
          this.loading = false;
        });
    },

    // 每页条数变化回调方法
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.selectOrderInfoByChoose();
    },
    // 当前页码变化
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val;
      this.selectOrderInfoByChoose();
    },

    //通知
    showNotify(title, message, type) {
      this.$notify({
        title: title,
        message: message,
        type: type,
      });
    },

    //添加订单
    addOrder(order) {
      this.$refs[order].validate((valid) => {
        if (valid) {
          this.addLoading = true;
          PostAddOrderAPI({
            username: this.order.userId,
            name: this.order.name,
            address: this.order.address,
            status: this.order.status,
            phone: this.order.phone,
            total: this.order.total,
          })
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", "添加成功", "success");
              } else if (resp.data.code.code == 20001) {
                this.showNotify("失败", "添加失败", "error");
              }
            })
            .catch((resp) => {
              this.$message.error("网络错误, 请稍后再试");
            })
            .finally((resp) => {
              this.addLoading = false;
              this.selectOrderInfoByChoose();
            });
        } else {
          return false;
        }
      });
    },
    // 修改订单
    modifyOrder(rowOrder) {
      this.$refs.son.order = JSON.parse(JSON.stringify(rowOrder)); // 深拷贝
      this.$refs.son.orderOptions = JSON.parse(
        JSON.stringify(this.orderOptions)
      ); // 深拷贝
      for (let index = 0; index < this.orderOptions.length; index++) {
        if (this.orderOptions[index].label == rowOrder.cid) {
          this.$refs.son.order.cid = JSON.parse(
            JSON.stringify(this.orderOptions[index].value)
          );
          break;
        }
      }
      this.$refs.son.status = true;
    },
    // 重置
    resetOrderForm(order) {
      this.$refs[order].resetFields();
      this.file = "";
    },

    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 4 === 1) {
        return "warning-row";
      } else if (rowIndex % 4 === 3) {
        return "success-row";
      }
      return "";
    },
    // 复选框选中后执行的方法
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection = val;
      this.ids = [];
      // 获取id(考虑最后一页)
      for (let index = 0; index < val.length; index++) {
        this.ids.push(val[index].id);
      }
      console.log(this.ids);
    },
    // 通用删除,供批量删除和单删除按钮用
    generalDelete() {
      this.$confirm("此操作将永久删除该商品, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          DeleteRemoveOrderAPI({
            data: { idList: this.ids },
          })
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", resp.data.code.msg, "success");
                this.selectOrderInfoByChoose();
              } else if (resp.data.code.code == 20001) {
                this.showNotify("失败", resp.data.code.msg, "error");
              }
            })
            .catch((resp) => {
              this.$message("网络错误!请稍后再试");
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        })
        .finally(() => {
          // 刷新ids
          this.ids = [];
          console.log(this.ids);
        });
    },
    // button单个删除
    deleteButtonOrder(rowOrder) {
      this.ids = [];
      this.ids.push(rowOrder.id);
      this.generalDelete();
      console.log(this.ids);
    },
    // 批量删除
    batchDelete() {
      if (this.ids.length == 0) {
        this.$message("请先选择");
        return;
      }
      console.log(this.ids);
      this.generalDelete();
    },
  },
  beforeMount() {
    this.selectOrderInfoByChoose();
  },
};
</script>

<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
