# 体验
前台:
http://cake.twopair.cn/

后台:
http://admin.cake.twopair.cn/

# 目录架构
```java
/
├── cakemanager        // 后台
├── cakeSystemManagerView      // 后台界面
├── online-cake        // 前台界面
```

# 部署
## 后台服务部署
1. 进入`cakemanager`文件夹

2. 手动创建mysql数据库`cake`, [点我打开sql脚本](https://pastes.twopair.cn/154)

    不出意外此时`cake`库中应有如下**6**张表 

![](https://hexoljj.oss-cn-shenzhen.aliyuncs.com/img/202301091757314.png)

3. 修改数据库配置文件 `/cakemanager/src/main/resources/jdbc.properties`
```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=YOUR_URL
jdbc.username=YOUR_USERNAME
jdbc.password=YOUR_PASSWORD
```

4. IDEA部署至`localhost:8080`

5. 部署配置如下
![](https://hexoljj.oss-cn-shenzhen.aliyuncs.com/img/202301091826330.png)

## 后台管理界面部署
1. 进入`cakeSystemManagerView`文件夹

2. 依次运行
    ```bash
    npm i

    npm run serve #默认8081端口(8080已被后台服务占用)
    ```

3. 浏览器访问`localhost:8081`
![](https://hexoljj.oss-cn-shenzhen.aliyuncs.com/img/202301091849484.png)

## 前台界面部署
1. 进入`online-cake`文件夹

2. 依次运行
    ```bash
    npm i

    npm run dev #3000端口
    ```

3. 浏览器访问`localhost:3000`
![](https://hexoljj.oss-cn-shenzhen.aliyuncs.com/img/202301091847200.png)

# Tips
后台蛋糕管理图片上传服务为阿里云oss提供,如有需要请修改

`/cakeSystemManagerView/src/views/ModifyCakeDialog.vue`
`/cakeSystemManagerView/src/views/CakeDataTable.vue`

![](https://hexoljj.oss-cn-shenzhen.aliyuncs.com/img/202301091927587.png)
