<template ref="father">
  <div>
    <!-- 动态查询表单  -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="ID">
        <el-input
          prefix-icon="el-icon-user-solid"
          v-model="searchId"
          placeholder="蛋糕ID"
        ></el-input>
      </el-form-item>
      <el-form-item label="名称">
        <el-input
          prefix-icon="el-icon-s-custom"
          v-model="searchName"
          placeholder="蛋糕名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="searchStatus" clearable placeholder="请选择状态">
          <el-option label="上架" value="1"></el-option>
          <el-option label="下架" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="selectCakeInfoByChoose"
          >查询</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 批量删除,新增操作区 -->
    <el-row>
      <el-button icon="el-icon-delete" @click="batchDelete" type="danger" plain>批量删除</el-button>
      <el-button icon="el-icon-edit" type="primary" plain @click="showAdd">添加</el-button>
    </el-row>

    <!-- 添加蛋糕对话框表单 -->
    <el-dialog title="添加蛋糕" :visible.sync="addDialogVisible" width="35%">
      <span>
        <el-form
          ref="cake"
          :model="cake"
          :rules="rules"
          label-width="80px"
          v-loading="addLoading"
        >
          <el-form-item label="名称" prop="name">
            <el-input
              v-model="cake.name"
              prefix-icon="el-icon-user-solid"
              placeholder="蛋糕名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="cid">
            <el-select v-model="cake.cid" clearable placeholder="请选择分类">
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

          <el-form-item label="描述" prop="description">
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
            <el-button type="primary" @click="addCake('cake')"
              >立即添加</el-button
            >
            <el-button @click="resetCakeForm('cake')">重置</el-button>
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>

    <!-- cakes表格数据 -->
    <el-table
      :data="tableData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column align="center" prop="name" label="名称">
      </el-table-column>
      <el-table-column align="center" prop="cid" label="分类">
      </el-table-column>
      <el-table-column align="center" prop="price" label="价格">
      </el-table-column>
      <el-table-column align="center" prop="number" label="销量">
      </el-table-column>
      <el-table-column align="center" prop="imgUrl" label="图片">
        <!-- 图片的显示 -->
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.imgUrl"
            lazy
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="description" label="描述">
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button  @click="modifyCake(scope.row)" type="primary"
            >修改</el-button
          >
          <el-button @click="deleteButtonCake(scope.row)" type="danger"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 修改dialog界面 -->
    <CakeDialog ref="son" @reloadView="selectCakeInfoByChoose"> </CakeDialog>

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
import CakeDialog from "./ModifyCakeDialog.vue";
import {
  PostAddCakeAPI,
  PostSelectCakeDataByChooseAPI,
  getAllCatAPI,
  DeleteRemoveCakeAPI,
} from "../request/api";

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
  components: {
    PagingTool,
    CakeDialog,
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

    return {
      pageSize: 5,
      currentPage: 1,
      data_num: 0,
      // 添加蛋糕数据对话框是否展示
      addDialogVisible: false,
      // 添加后打转提示
      addLoading: false,

      catOptions: [],

      // 动态查询
      searchId: "",
      searchName: "",
      searchStatus: "",

      // 多选的id
      ids: [],

      // 添加图片的链接
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
      // 复选框选中数据集合
      multipleSelection: [],
      // user表格数据
      tableData: [],
      // 添加蛋糕校验规则
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
      loading: true,
    };
  },
  methods: {
    // 获取所有分类
    getAllCat() {
      getAllCatAPI()
        .then((resp) => {
          this.catOptions = [];
          var list = resp.data.data;
          for (let index = 0; index < list.length; index++) {
            var t = new Object();
            t.label = list[index].name;
            t.value = list[index].id;
            this.catOptions.push(t);
          }
          this.showNotify("分类获取", "获取分类成功!", "success");
        })
        .catch((resp) => {
          this.showNotify("分类获取", "获取分类失败!", "error");
        });
    },
    showAdd() {
      this.addDialogVisible = true;
    },
    // 条件查询
    selectCakeInfoByChoose() {
      this.loading = true;
      console.log(this.searchId);
      PostSelectCakeDataByChooseAPI({
        id: this.searchId,
        name: this.searchName,
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
      this.selectCakeInfoByChoose();
    },
    // 当前页码变化
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val;
      this.selectCakeInfoByChoose();
    },

    //通知
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

    //添加蛋糕
    addCake(cake) {
      this.$refs[cake].validate((valid) => {
        if (valid) {
          this.addLoading = true;
          PostAddCakeAPI(this.cake)
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
              this.addLoading = false;
              this.selectCakeInfoByChoose();
            });
        } else {
          return false;
        }
      });
    },
    // 修改蛋糕
    modifyCake(rowCake) {
      this.$refs.son.cake = JSON.parse(JSON.stringify(rowCake)); // 深拷贝
      this.$refs.son.catOptions = JSON.parse(JSON.stringify(this.catOptions)); // 深拷贝
      this.$refs.son.file = JSON.parse(JSON.stringify(rowCake.imgUrl)); // 深拷贝

      for (let index = 0; index < this.catOptions.length; index++) {
        if (this.catOptions[index].label == rowCake.cid) {
          this.$refs.son.cake.cid = JSON.parse(
            JSON.stringify(this.catOptions[index].value)
          );
          break;
        }
      }

      if (this.$refs.son.cake.status == "上架") {
        this.$refs.son.cake.status = "1";
      } else {
        this.$refs.son.cake.status = "0";
      }
      this.$refs.son.status = true;
    },
    // 重置
    resetCakeForm(cake) {
      this.$refs[cake].resetFields();
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
          DeleteRemoveCakeAPI({
            data: { idList: this.ids },
          })
            .then((resp) => {
              if (resp.data.code.code == 20000) {
                this.showNotify("成功", resp.data.code.msg, "success");
                this.selectCakeInfoByChoose();
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
    deleteButtonCake(rowCake) {
      this.ids = [];
      this.ids.push(rowCake.id);
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
    this.selectCakeInfoByChoose();
    this.getAllCat();
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
