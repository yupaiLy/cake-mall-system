<template lang="">
  <div>
    <!-- 添加用户对话框表单 -->
    <el-dialog title="修改用户" :visible.sync="status" width="50%">
      <span>
        <el-form
          ref="user"
          :model="user"
          :rules="rules"
          label-width="80px"
          v-loading="modifyLoading"
        >
          <el-form-item label="账号" prop="username">
            <el-input
              v-model="user.username"
              prefix-icon="el-icon-user-solid"
              placeholder="用户账号"
              :disabled="true"
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
            <el-button type="primary" @click="modifyUser('user')"
              >立即修改</el-button
            >
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { PutUpdateUserAPI } from "../request/api";

export default {
  data() {
    return {
      status: false,
      // 修改后打转提示
      modifyLoading: false,
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
      rules: {
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
    //修改用户
    modifyUser(user) {
      // this.$emit('reloadView');
      // alert(22);
      this.$refs[user].validate((valid) => {
        if (valid) {
          this.modifyLoading = true;
          PutUpdateUserAPI(this.user)
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
