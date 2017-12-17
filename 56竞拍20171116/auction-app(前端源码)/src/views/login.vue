<template>
  <div id="login">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.go(-1)">
          <i class="icon-back"></i>
        </a>
        <span>登录</span>
      </div>
      <div class="page-content">
        <mt-field 
          style="margin-top: 10px;border-top: 1px solid #ccc;" 
          label="账号" 
          placeholder="请输入你的手机号码" 
          :attr="{
            maxLength: 11
          }"
          :state="fieldState.identifier" 
          v-model="loginForm.identifier" 
          @blur.native.capture="check('identifier')"
          >
        </mt-field>
        <mt-field 
          label="密码"
          style="border-top: 1px solid #ccc;"
          placeholder="请输入登录密码"
          :attr="{
            maxLength: 20
          }"
          type="password" 
          :state="fieldState.password" 
          v-model="loginForm.password">
        </mt-field>
        <div style="padding: 0px 10px;">
          <mt-button style="width:100%;margin: 20px 0 0 0;background-color:red;color:#ffffff;font-weight: 600;" @click="sumbit('loginForm')">登录</mt-button>
          <mt-button style="width:100%;margin-top: 5px;border:1px solid red;color: red;font-weight: 600;" plain @click="$router.push({name: 'register'})">手机号快速注册</mt-button>
          <a href="javascript: void(0);" style="color:#aaa;font-size: 16px;margin-top: 15px;display: block;color: red;padding-left: 10px;font-weight: bold;" @click="$router.replace({name: 'smsLogin'})">使用短信验证码登录</a>
        </div>
      </div>
    </div>
    <div style="position: absolute;bottom:150px;width: 100%;">
        	<div style="width: 30%;margin-left:5%;height: 1px;border-top: 1px solid #CCCCCC;float: left;margin-top: 9px;padding-bottom: 9px;"></div>
        	<div style="width: 30%;font-size: 14px;height: 20px;line-height: 20px;text-align: center;float: left;font-size: 16px;">
        		第三方登录
        	</div>
        	<div style="width: 30%;height: 1px;border-top: 1px solid #CCCCCC;float: left;margin-top: 9px; padding-bottom: 9px;"></div>
        </div>
    <a href=""style="display: block;width: 50px;position: absolute;left: 50px;bottom: 50px;"><img src="../../dist/static/img/login/qq.png" style="width: 50px;"/></br><p style="text-align: center;color: black;font-size: 20px;">QQ</p></a>
    <a href=""style="display: block;width: 50px;position: absolute;right: 50px;bottom: 50px;"><img src="../../dist/static/img/login/wx.png" style="width: 50px;"/></br><p style="text-align: center;color: black;font-size: 18px;">微信</p></a>
  </div>
</template>

<script>
export default {
  name: 'login',
  data () {
    return {
      loginForm: {
        identifier: '',
        password: '',
        validate: true
      },
      fieldState: {
        identifier: '',
        password: ''
      },
      toastClient: ''
    }
  },
  created () {
  },
  activated () {
  },
  methods: {
    check (fieldName) {
      if (!this.loginForm[fieldName]) {
        this.fieldState[fieldName] = 'error'
      } else {
        this.fieldState[fieldName] = 'success'
      }
    },
    validateField (field, scope) {
      let valid = true
      switch (field) {
        case 'identifier':
          if (!scope.identifier.trim()) {
            this.toast('请输入账号')
            valid = false
          }
          break
        case 'password':
          if (!scope.password.trim()) {
            this.toast('请输入密码')
            valid = false
          }
          break
        case 'validate':
          if (!scope.validate) {
            this.toast('请勾选同意')
            valid = false
          }
          break
        default:
          break
      }
      this.check(field)
      return valid
    },
    sumbit (formName) {
      let valid = true
      if (formName === 'loginForm') {
        for (let k in this.loginForm) {
          valid = this.validateField(k, this.loginForm)
          if (!valid) {
            return
          }
        }
        if (valid) {
          this.$api.login(this.loginForm, data => {
            window.localStorage.setItem('expirationTime', data.resource.expirationTime)
            window.localStorage.setItem('token', data.resource.token)
            window.localStorage.setItem('identifier', this.loginForm.identifier)
            this.toast('登录成功')
            this.$router.replace({name: 'userCenter'})
          }, e => {
            this.toast(`登录失败,${e.message}`)
          })
        }
      }
    },
    toast (message) {
      if (this.toastClient) {
        this.toastClient.close()
      }
      this.toastClient = this.$toast({
        message: message,
        position: 'middle',
        duration: 2000
      })
    }
  }
}
</script>

<style>
.mint-cell-title{
	font-size: 18px;
	font-family: PingFangSC-Regular, sans-serif;
	font-weight: 600;
}
</style>
