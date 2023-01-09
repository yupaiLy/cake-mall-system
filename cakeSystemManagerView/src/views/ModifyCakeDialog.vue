<template lang="">
  <div>
    <!-- 修改蛋糕对话框表单 -->
    <el-dialog title="添加蛋糕" :visible.sync="status" width="50%">
      <span>
        <el-form
          ref="cake"
          :model="cake"
          :rules="rules"
          label-width="80px"
          v-loading="modifyLoading"
        >
          <el-form-item label="名称" prop="name">
            <el-input
              v-model="cake.name"
              prefix-icon="el-icon-user-solid"
              placeholder="蛋糕名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="cid">
            <el-select v-model="cake.cid" placeholder="请选择分类">
              <el-option
                v-for="item in catOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input v-model="cake.price" placeholder="蛋糕价格"></el-input>
          </el-form-item>
          <el-form-item label="销量" prop="number">
            <el-input v-model="cake.number" placeholder="蛋糕销量"></el-input>
          </el-form-item>

          <el-form-item label="图片" prop="imgUrl">
            <!-- 图片的添加 -->
            <el-upload
              class="avatar-uploader"
              action=""
              :on-success="handleSuccess"
              :http-request="uploadFile"
              :before-upload="beforeAvatarUpload"
              :limit="1"
            >
              <img v-if="file" :src="file" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>

          <el-form-item label="描述" prop="price">
            <el-input
              type="textarea"
              autosize
              v-model="cake.description"
              prefix-icon="el-icon-s-operation"
              placeholder="蛋糕描述"
            ></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="cake.status" placeholder="请选择状态">
              <el-option label="上架" value="1"></el-option>
              <el-option label="下架" value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="modifyCake('cake')"
              >立即修改</el-button
            >
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { PutUpdateCakeAPI } from "../request/api";

const OSS = require("ali-oss");

const client = new OSS({
  // yourregion填写Bucket所在地域。以华东1（杭州）为例，Region填写为oss-cn-hangzhou。
  region: "xxx",
  // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
  accessKeyId: "xxx",
  accessKeySecret: "xxx",
  // 填写Bucket名称。
  bucket: "xxx",
});

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
    return {
      status: false,
      // 修改后打转提示
      modifyLoading: false,
      // 分类列表
      catOptions: [],
      // 图片的链接
      file: "",
      // cake模型
      cake: {
        id: "",
        name: "",
        price: "",
        number: "",
        status: "",
        imgUrl: "",
        description: "",
        cid: "",
        likesNumber: "",
      },
      rules: {
        name: [
          { required: true, message: "请输入名称", trigger: "blur" },
          {
            min: 2,
            max: 16,
            message: "长度在 2 到 16 个字符",
            trigger: "blur",
          },
        ],
        cid: [
          {
            required: true,
            message: "请选择分类",
            trigger: "blur",
          },
        ],
        price: [
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
            trigger: "blur",
          },
        ],
        number: [
          {
            required: true,
            trigger: "blur",
            validator: valiNumberPass2,
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
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    async uploadFile(options) {
      try {
        let file = options.file; // 拿到 file

        let fileName = file.name.substr(0, file.name.lastIndexOf("."));

        let date = new Date().getTime();

        let fileNames = `${date}_${fileName}`; // 拼接文件名，保证唯一，这里使用时间戳+原文件名

        // 上传文件,这里是上传到OSS的 cake文件夹下
        client.put("cake/" + fileNames, file).then((res) => {
          if (res.res.statusCode === 200) {
            options.onSuccess(res);
            this.showNotify("图片上传", "上传成功!", "success");
          } else {
            options.onError("上传失败");
            this.showNotify("图片上传", "上传失败!", "error");
          }
        });
      } catch (e) {
        options.onError("上传失败");
        this.showNotify("图片上传", "上传失败!", "error");
      }
    },
    // 图片上传成功回调函数
    handleSuccess(res) {
      this.file = res.url;
      this.cake.imgUrl = res.url;
    },
    //修改蛋糕
    modifyCake(cake) {
      // this.$emit('reloadView');
      // alert(22);
      this.$refs[cake].validate((valid) => {
        if (valid) {
          this.modifyLoading = true;
          PutUpdateCakeAPI(this.cake)
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
