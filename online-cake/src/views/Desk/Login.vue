<template>
  <div class="login">
    <!-- 控制提示内容 -->
    <div class="container" ref="container">
      <!-- register -->
      <div class="form-container sign-up-container">
        <div class="form">
          <h2>sign up</h2>
          <input v-model="username" type="text" name="username" id="username" placeholder="Username..." />
          <input v-model="password" type="password" name="password" id="password" placeholder="Password..." />
          <input v-model="name" type="text" name="consignee" id="consignee" placeholder="name..." />
          <input v-model="address" type="text" name="address" id="address" placeholder="Address..." />
        <div class="radio">男<input v-model="gender" type="radio" value="1"  name="sex"  >女<input name="sex" v-model="gender" type="radio" value="0"> </div>
          <button class="signUp" @click="register">注册</button>
        </div>
      </div>
      <!-- login 开始左 -->
      <div class="form-container sign-in-container">
        <div class="form">
          <h2>sign in</h2>
          <input v-model="username" type="text" name="username" id="usernmae" placeholder="Username..." />
          <input v-model="password" type="password" name="password" id="password" placeholder="Password..." />
          <button class="signIn" @click="login">登陆</button>
        </div>
      </div>
      <!-- overlay container -->
      <div class="overlay_container">
        <div class="overlay">
          <!-- overlay left -->
          <div class="overlay_panel overlay_left_container">
            <h2>welcome back!</h2>
            <button id="sign-in" @click="signInClick">回到登陆</button>
          </div>
          <!-- overlay right 开始右 -->
          <div class="overlay_panel overlay_right_container">
            <h2>hello friend!</h2>
            <button id="sign-up" @click="signUpClick">进入注册</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { setAccessToken} from '@/utils/token.js'
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import { reqRegister, reqLogin } from '../../api/index.js'
// import { setAccessToken,setRefreshToken } from '../../utils/token.js'
export default defineComponent({
  setup() {
    const obj = reactive({
      container: null,
      username: '',
      name: '', //收件人
      password: '',
      
      address: '',
      gender:'1',
      type: '',
      isShowAlert: false
    })

    // 弹出提示曾内容和控制是否显示
    // 弹出失败
    function alertInfoMsg(msg, type) {
      alert(msg)
      obj.type = type
      obj.isShowAlert = true
      setTimeout(() => {
        obj.isShowAlert = false
      }, 2000)
    }

    // 清空obj中的属性
    function clearState() {
      obj.username = ''
      obj.name = ''
      obj.password = ''
      obj.address = ''
      obj.gender = '1'
    }
    // 动画切换
    function signInClick() {
      obj.container.classList.remove('active')
      clearState()
    }
    function signUpClick() {
      obj.container.classList.add('active')
      clearState()
    }
    // 注册
    function register() {
      let data = {
        username: obj.username,
        password: obj.password,
        name: obj.name,
        address:obj.address,
        gender:obj.gender
      }
      if (obj.username == '' || obj.password == '' || obj.gender == '') {
        alertInfoMsg('用户名,密码,性别都必须填', 'error')
      } else {
        reqRegister(data).then(res => {
          console.log(res)
          if (res.data.code.code == 20000) {
            alertInfoMsg('注册成功', 'success')
            signInClick()
          } else {
            alert(res.data.code.msg)
          }
        })
      }
    }
    // 登录
    const router = useRouter()
    function login() {
      if (obj.username == '' || obj.password == '') {
        alertInfoMsg('用户名或密码不能为空', 'error')
      } else {
        const data = {
          username: obj.username,
          password: obj.password
        }
        reqLogin(data).then(res => {
          if (res.data.code.code == 20000) {
            // 登录成功
            alertInfoMsg('登录成功', 'success')
            setAccessToken(obj.username)
            router.push({ name: 'home' })
            
          } else {
            alert(res.data.code.msg)
          }
        })
      }
    }

    return {
      ...toRefs(obj),
      signInClick,
      signUpClick,
      register,
      login
    }
  }
})

</script>

<style scoped lang="less">
.login {
  display: flex;
  top: 2px;
  justify-content: center;
  align-items: center;
  height: 90vh;
  background-color: #f6f5f7;
}
h2 {
  margin-bottom: 10px;
  font-size: 32px;
  text-transform: capitalize;
}

.container {
  position: relative;
  width: 768px;
  height: 480px;
  background-color: white;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  overflow: hidden;
}

.form-container {
  position: absolute;
  top: 0;
  width: 50%;
  height: 100%;
  background-color: white;
  transition: all 0.6s ease-in-out;
}

.form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  padding: 0 50px;
}
.radio{
  padding: 10px;
}
.radio input{
  width: auto;
  margin: 40px;
}

input {
  width: 100%;
  margin: 8px 0;
  padding: 12px;
  background-color: #eee;
  border: none;
}

.forget-password {
  display: inline-block;
  height: 20px;
  text-decoration: none;
  color: #bbb;
  text-transform: capitalize;
  font-size: 12px;
}

.forget-password:hover {
  color: lightslategray;
  border-bottom: 2px solid #ff4b2b;
}

button {
  cursor: pointer;
  background: @hoverColor;
  padding: 10px 50px;
  border: 1px solid transparent;
  border-radius: 20px;
  text-transform: uppercase;
  color: white;
  margin-top: 10px;
  outline: none;
  transition: transform 80;
}

button:active {
  transform: scale(0.95);
}

.overlay_container {
  position: absolute;
  top: 0;
  width: 50%;
  height: 100%;
  z-index: 100;
  right: 0;
  overflow: hidden;
  transition: all 0.6s ease-in-out;
}

.overlay {
  position: absolute;
  width: 200%;
  height: 100%;
  left: -100%;
  background-color: @hoverColor;
}

.overlay_panel {
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 50%;
  height: 100%;
  color: white;
  padding: 0 40px;
  text-align: center;
}

.overlay_panel button {
  background-color: transparent;
  border: 1px solid white;
}

.overlay_panel p {
  font-size: 12px;
  margin: 10px 0 15px 0;
}

.overlay_right_container {
  right: 0;
}

.container.active .sign-up-container {
  transform: translateX(100%);
  z-index: 5;
}

.container.active .sign-in-container {
  transform: translateX(100%);
}

.container.active .overlay_container {
  transform: translateX(-100%);
}

.container.active .overlay {
  transform: translateX(50%);
}
</style>
