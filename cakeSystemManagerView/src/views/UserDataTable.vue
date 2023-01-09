<template ref="father">
  <div>
    <!-- 动态查询表单  -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="账号">
        <el-input
          prefix-icon="el-icon-user-solid"
          v-model="searchUsername"
          placeholder="用户账号"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input
          prefix-icon="el-icon-s-custom"
          v-model="searchName"
          placeholder="用户姓名"
        ></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="searchGender" clearable placeholder="请选择性别">
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="0"></el-option>
          <el-option label="secret" value="-1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账号状态" prop="gender">
        <el-select v-model="searchStatus" clearable placeholder="请选择状态">
          <el-option label="正常" value="1"></el-option>
          <el-option label="封禁" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="selectUserInfoByChoose"
          >查询</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 批量删除,新增操作区 -->
    <el-row>
      <el-button icon="el-icon-delete" @click="batchDelete" type="danger" plain
        >批量删除</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-edit"
        plain
        @click="addDialogVisible = true"
        >添加</el-button
      >
    </el-row>
    <!-- 添加用户对话框表单 -->
    <el-dialog title="添加用户" :visible.sync="addDialogVisible" width="35%">
      <span>
        <el-form
          ref="user"
          :model="user"
          :rules="rules"
          label-width="80px"
          v-loading="addLoding"
        >
          <el-form-item label="账号" prop="username">
            <el-input
              v-model="user.username"
              prefix-icon="el-icon-user-solid"
              placeholder="用户账号"
            ></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="user.name"
              prefix-icon="el-icon-user-solid"
              placeholder="用户姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="user.gender" placeholder="请选择性别">
              <el-option label="男" value="1"></el-option>
              <el-option label="女" value="0"></el-option>
              <el-option label="secret" value="-1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="user.status" placeholder="请选择账号状态">
              <el-option label="正常" value="1"></el-option>
              <el-option label="封禁" value="0"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="地址" prop="address">
            <el-input
              placeholder="用户地址"
              prefix-icon="el-icon-position"
              v-model="user.address"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input show-password v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addUser('user')"
              >立即添加</el-button
            >
            <el-button @click="resetUserForm('user')">重置</el-button>
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>

    <!-- users表格数据 -->
    <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column align="center" prop="username" label="账号">
      </el-table-column>
      <el-table-column align="center" prop="password" label="密码">
      </el-table-column>
      <el-table-column align="center" prop="name" label="姓名">
      </el-table-column>
      <el-table-column align="center" prop="gender" label="性别">
      </el-table-column>
      <el-table-column align="center" prop="address" label="地址">
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button @click="modifyUser(scope.row)" type="primary"
            >修改</el-button
          >
          <el-button
            icon="el-icon-delete"
            @click="deleteButtonUser(scope.row)"
            type="danger"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 修改dialog界面 -->
    <UserDialog ref="son" @reloadView="selectUserInfoByChoose"> </UserDialog>

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
import axios from "axios";
import PagingTool from "../components/PagingTool.vue";
import UserDialog from "./ModifyUserDialog.vue";

import {
  PostSignUpAPI,
  DeleteRemoveUserAPI,
  PostSelectUserDataByChooseAPI,
} from "../request/api";

export default {
  components: {
    PagingTool,
    UserDialog,
  },

  data() {
    return {
      pageSize: 5,
      currentPage: 1,
      data_num: 0,
      // 添加用户数据对话框是否展示
      addDialogVisible: false,
      // 添加后打转提示
      addLoding: false,

      // 动态查询
      searchUsername: "",
      searchName: "",
      searchGender: "",
      searchStatus: "",

      // 多选的id
      ids: [],

      // user模型
      user: {
        id: "",
        username: "",
        name: "",
        gender: "",
        address: "",
        password: "",
        status: "",
      },
      // 复选框选中数据集合
      multipleSelection: [],
      // user表格数据
      tableData: [],
      // 添加用户校验规则
      rules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "长度在 8 到 16 个字符",
            trigger: "blur",
          },
        ],
        name: [
          {
            min: 2,
            max: 16,
            message: "长度在 2 到 16 个字符",
            trigger: "blur",
          },
        ],
        gender: [
          {
            required: true,
            message: "请选择性别",
            trigger: "change",
          },
        ],
        status: [
          {
            required: true,
            message: "请选择账号状态",
            trigger: "change",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "长度在 8 到 16 个字符",
            trigger: "blur",
          },
        ],
      },
      loading: true,
    };
  },
  methods: {
    // 条件查询
    selectUserInfoByChoose() {
      this.loading = true;
      console.log(this.searchUsername);
      PostSelectUserDataByChooseAPI({
        username: this.searchUsername,
        name: this.searchName,
        gender: this.searchGender,
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        status: this.searchStatus,
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
      this.selectUserInfoByChoose();
    },
    // 当前页码变化
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val;
      this.selectUserInfoByChoose();
    },
    // 修改用户
    modifyUser(rowUser) {
      this.$refs.son.user = JSON.parse(JSON.stringify(rowUser)); // 深拷贝
      if (this.$refs.son.user.gender == "男") {
        this.$refs.son.user.gender = "1";
      } else if (this.$refs.son.user.gender == "女") {
        this.$refs.son.user.gender = "0";
      } else {
        this.$refs.son.user.gender = "-1";
      }
      if (this.$refs.son.user.status == "正常") {
        this.$refs.son.user.status = "1";
      } else if (this.$refs.son.user.status == "封禁") {
        this.$refs.son.user.status = "0";
      }
      this.$refs.son.status = true;
    },

    // 通用删除,供批量删除和单删除按钮用
    generalDelete() {
      this.$confirm("此操作将永久删除该用户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          DeleteRemoveUserAPI({
            data: { idList: this.ids },
          })
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", resp.data.code.msg, "success");
                this.selectUserInfoByChoose();
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
    // button单个删除user
    deleteButtonUser(rowUser) {
      this.ids = [];
      this.ids.push(rowUser.id);
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

    //通知
    showNotify(title, message, type) {
      this.$notify({
        title: title,
        message: message,
        type: type,
      });
    },
    //添加用户
    addUser(user) {
      this.$refs[user].validate((valid) => {
        if (valid) {
          this.addLoding = true;
          PostSignUpAPI(this.user)
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", resp.data.code.msg, "success");
              } else if (resp.data.code.code == 20001) {
                this.showNotify("失败", resp.data.code.msg, "error");
              }
            })
            .catch((resp) => {
              this.$message.error("网络错误, 请稍后再试");
            })
            .finally((resp) => {
              this.addLoding = false;
              this.selectUserInfoByChoose();
            });
        } else {
          return false;
        }
      });
    },
    // 重置
    resetUserForm(user) {
      this.$refs[user].resetFields();
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
  },
  beforeMount() {
    this.selectUserInfoByChoose();
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
</style>
