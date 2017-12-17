<template>
  <div id="smsLogin">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.replace({name: 'login'})">
          <i class="icon-back"></i>
        </a>
        <span>验证码登录</span>
      </div>
      <div class="page-content">
        <mt-field 
          type="tel"
          style="margin-top: 10px;border-top: 1px solid #ccc" 
          label="账户" 
          placeholder="请输入手机号码" 
          :attr="{
            maxLength: 11
          }"
          :state="fieldState.identifier" 
          v-model="loginForm.identifier">
        </mt-field>
        <mt-field 
          style="border-bottom: 1px solid #ccc" 
          label="验证码" 
          placeholder="请输入验证码" 
          :attr="{
            maxLength: 8
          }"
          v-model="loginForm.sms">
          <a href="javascript: void(0);" style="color:#aaa" @click="getCode()">{{codeMessage}}</a>
        </mt-field>
        <div style="padding: 0px 10px;">
        <mt-button style="width:100%;margin-top: 20px;background-color:red;color:#ffffff;font-weight: 600;" @click="sumbit('loginForm')">登录</mt-button>
        <mt-button style="width:100%;margin-top: 5px;border:1px solid red;color:red;font-weight: 600;" plain @click="$router.push({name: 'register'})">手机号快速注册</mt-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'smsLogin',
  data () {
    return {
      loginForm: {
        identifier: '',
        password: '123456',
        sms: '',
        validate: true
      },
      fieldState: {
        identifier: '',
        sms: ''
      },
      toastClient: '',
      codeGetting: false,
      codeMessage: '获取验证码'
    }
  },
  created () {
  },
  activated () {
  },
  methods: {
    validateField (field, scope) {
      let valid = true
      switch (field) {
        case 'identifier':
          let regx = /^(((13[0-9]{1})|(14[579]{1})|(15[0-35-9]{1})|(17[0135-8]{1})|(18[0-9]{1}))+\d{8})$/
          if (!scope.identifier.trim()) {
            this.toast('请输入手机号码')
            this.fieldState['identifier'] = 'error'
            valid = false
          } else if (!regx.test(scope.identifier)) {
            this.toast('请输入正确手机号')
            this.fieldState['identifier'] = 'error'
            valid = false
          } else {
            this.fieldState['identifier'] = 'success'
          }
          break
        case 'sms':
          if (!scope.sms.trim()) {
            this.toast('请输入验证码')
            valid = false
          }
          break
        case 'validate':
          if (!scope.validate) {
            this.toast('请勾选同意协议')
            valid = false
          }
          break
        default:
          break
      }
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
            // this.$global.getUserInfo()
            window.localStorage.setItem('expirationTime', data.resource.expirationTime)
            window.localStorage.setItem('token', data.resource.token)
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
    },
    getCode () {
      if (!this.codeGetting && this.validateField('identifier', this.loginForm)) {
        if (this.fieldState['identifier'] !== 'success') {
          this.toast('请输入手机号码')
          return
        }
        this.codeGetting = true
        this.codeMessage = 60
        let codeInterval = setInterval(() => {
          if (this.codeMessage === 0) {
            this.codeMessage = '获取验证码'
            this.codeGetting = false
            window.clearInterval(codeInterval)
          } else {
            this.codeMessage--
          }
        }, 1000)
        this.$api.getCode(this.loginForm.identifier, (data) => {
          this.toast('验证码发送成功')
        }, (e) => {
          this.codeGetting = false
          window.clearInterval(codeInterval)
          this.codeMessage = '获取验证码'
          this.toast('验证码发送失败')
        })
      }
    }
  }
}
</script>

<style>

</style>
