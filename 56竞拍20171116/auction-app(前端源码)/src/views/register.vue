<template>
  <div id="register">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.replace({name: 'login'})">
          <i class="icon-back"></i>
        </a>
        <span>新用户注册</span>
      </div>
      <div class="page-content">
        <mt-field 
          type="tel"
          style="margin-top: 10px;border-top: 1px solid #ccc"
          label="手机号码"
          placeholder="请输入手机号码" 
          :attr="{
            maxLength: 11
          }"
          :state="fieldState.phone" 
          v-model="registerData.phone"
          @blur.native.capture="check('phone')">
        </mt-field>
        <mt-field 
          label="验证号码"
          placeholder="请输入验证码" 
          :attr="{
            maxLength: 8
          }"
          v-model="registerData.sms">
          <a href="javascript: void(0);" style="color:#aaa" @click="getCode()">{{codeMessage}}</a>
        </mt-field>
        <mt-field
          type="password"
          style="letter-spacing: 1px;"
          label=" 新密码"
          placeholder="请输入登录密码"
          :attr="{
            maxLength: 20
          }"
          v-model="registerData.pwd">
        </mt-field>
        <mt-field
          type="password"
          style="border-bottom: 1px solid #ccc"
          label="重输密码"
          placeholder="请再次输入新密码"
          :attr="{
            maxLength: 20
          }"
          v-model="registerData.pwd2">
          <span v-if="registerData.pwd2 && registerData.pwd2 !== registerData.pwd" style="color: red;">两次密码不一致</span>
        </mt-field>
        <div style="padding: 0px 10px;">
        <mt-button style="width:100%;margin-top: 20px;background-color:red;color:#ffffff;font-weight: 600;" :disabled="submitting" @click="sumbit('registerData')">注册</mt-button>
        <a href="javascript: void(0);" style="color:#aaa;display: block;margin-top: 10px;" @click="$router.replace({name: 'login'})">已有账号？去登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'register',
  data () {
    return {
      registerData: {
        phone: '',
        sms: '',
        pwd: '',
        pwd2: '',
        agreement: true
      },
      fieldState: {
        phone: '',
        sms: ''
      },
      toastClient: '',
      codeGetting: false,
      codeMessage: '获取验证码',
      submitting: false
    }
  },
  created () {
  },
  activated () {
  },
  methods: {
    check (key) {
      if (this.validateField(key, this.registerData)) {
        this.$api.detection(this.registerData[key], data => {
          if (data.result) {
            this.fieldState[key] = 'success'
          } else {
            this.fieldState[key] = 'error'
            if (key === 'phone') {
              this.toast('此手机号已存在')
            }
          }
        }, e => {
          this.fieldState[key] = 'error'
          this.toast(`检测失败,${e.message}`)
        })
      }
    },
    validateField (field, scope) {
      let valid = true
      switch (field) {
        case 'phone':
          let regx = /^(((13[0-9]{1})|(14[579]{1})|(15[0-35-9]{1})|(17[0135-8]{1})|(18[0-9]{1}))+\d{8})$/
          if (!regx.test(scope.phone)) {
            this.toast('请输入正确手机号')
            valid = false
          } else {
          }
          break
        case 'sms':
          if (!scope.sms.trim()) {
            this.toast('请输入验证码')
            valid = false
          }
          break
        case 'pwd':
          if (!scope.pwd.trim()) {
            this.toast('请输入登录密码')
            valid = false
          }
          break
        case 'agreement':
          if (!scope.agreement) {
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
      if (formName === 'registerData') {
        for (let k in this.registerData) {
          valid = this.validateField(k, this.registerData)
          if (!valid) {
            return
          }
        }
        if (valid && !this.submitting) {
          this.submitting = true
          this.$api.registered(this.registerData, data => {
            window.localStorage.setItem('expirationTime', data.resource.expirationTime)
            window.localStorage.setItem('token', data.resource.token)
            window.localStorage.setItem('identifier', data.resource.identifier)
            this.toast('注册成功')
            this.submitting = false
            this.$router.replace({name: 'userCenter'})
          }, e => {
            this.submitting = false
            this.toast(`注册失败,${e.message}`)
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
      if (!this.registerData.phone.trim()) {
        return
      }
      if (!this.codeGetting && this.validateField('phone', this.registerData)) {
        if (this.fieldState['phone'] === 'error') {
          this.toast('此手机号已存在')
          return
        } else if (this.fieldState['phone'] !== 'success') {
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
        this.$api.postCode(this.registerData.phone, (data) => {
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
