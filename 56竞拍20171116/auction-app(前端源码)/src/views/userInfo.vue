<template>
  <div class="userInfo">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>个人设置</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top: 0px;" :style="{minHeight: minHeight + 'px'}">
        <mt-cell title="头像" is-link>
          <img class="item-head" :src="$api.file.see(user.head_pic)" alt="头像">
          <input type="file" class="fileInput" @change="upload($event)">
        </mt-cell>
        <mt-cell title="用户名" is-link @click.native="openNamePrompt">
          <span>{{user.username}}</span>
        </mt-cell>
        <mt-cell title="手机号码" is-link @click.native="showPhonePopup()">
          <span>{{user.iphone}}</span>
        </mt-cell>
        <mt-cell title="用户密码" is-link @click.native="showPwdPopup()">
          <span>修改</span>
        </mt-cell>
        <br>
        <mt-cell title="用户ID">
          <span>{{user.id}}</span>
        </mt-cell>
      </div>
      </mt-loadmore>
    </div>
  
    <mt-popup v-model="phonePopupVisible" position="right" class="mint-popup-fade" :modal="false">
      <div class="update-title">
        <img src="static/img/img-update.svg">
        <h5>修改用户手机号</h5>
      </div>
  
      <mt-field label="旧手机号" type="tel" v-model="user.iphone" :readonly="true"></mt-field>
      <mt-field label="手机验证码" v-model="updatePhoneData.oldSms">
        <div style="">
        <mt-button size="small" type="default" @click="getCode('old')">{{oldCodeMessage}}</mt-button>
        </div>
      </mt-field>
      <br>
      <mt-field label="新手机号" type="tel" :state="updatePhoneState.phone" :attr="{maxLength: 11}" v-model="updatePhoneData.phone" @blur.native.capture="check('phone')"></mt-field>
      <div style="color: red;font-size: 14px;text-align: center;">{{phoneErrorMessage}}</div>
      <mt-field label="手机验证码" v-model="updatePhoneData.newSms">
        <mt-button size="small" type="default" @click="getCode('new')">{{newCodeMessage}}</mt-button>
      </mt-field>
      <br>
      <mt-button @click="updatePhone" size="large" type="danger" class="confirmBtn">确定修改</mt-button>
      <mt-button @click="phonePopupVisible = false" size="large" type="danger" class="confirmBtn" plain>取消</mt-button>
    </mt-popup>
    <mt-popup v-model="pwdPopupVisible" position="right" class="mint-popup-fade" :modal="false">
      <div class="update-title">
        <img src="static/img/img-update.svg">
        <h5>修改登录密码</h5>
      </div>
      <template v-if="updatePwdByPhone">
        <mt-field label="手机号" type="tel" v-model="user.iphone" :readonly="true"></mt-field>
        <mt-field 
          label="验证码" 
          placeholder="请输入验证码" 
          :attr="{
            maxLength: 8
          }"
          v-model="updatePwdData.sms">
          <a href="javascript: void(0);" style="color:#aaa" @click="getCode('pwd')">{{pwdCodeMessage}}</a>
        </mt-field>
      </template>
      <template v-else>
      <mt-field label="旧密码" type="password" :attr="{maxLength: 20}" placeholder="请输入旧密码" :state="updatePwdState.oldPassword" v-model="updatePwdData.oldPassword"></mt-field>
      </template>
      <br>
      <mt-field label="新密码" type="password" :attr="{maxLength: 20}" placeholder="请输入新密码" :state="updatePwdState.newPassword" v-model="updatePwdData.newPassword"></mt-field>
      <mt-field label="确认密码" type="password" :attr="{maxLength: 20}" placeholder="再次输入新密码" :state="updatePwdState.pwdConfirm" v-model="updatePwdData.pwdConfirm"  @blur.native.capture="checkPwd()"></mt-field>
      <div style="color: red;font-size: 14px;text-align: center;">{{pwdConfirmMessage}}</div>
      <br>
      <div style="width: 100%;text-align: right;">
        <span style="font-size: 14px;padding-right: 20px;color: #888" @click="pwdChange">
          {{updatePwdByPhone?'←旧密码修改':'→手机验证码修改'}}
        </span>
      </div>
      <mt-button @click="updatePwd" size="large" type="danger" class="confirmBtn">确定修改</mt-button>
      <mt-button @click="pwdPopupVisible = false" size="large" type="danger" class="confirmBtn" plain>取消</mt-button>
    </mt-popup>
  </div>
</template>

<script>
import globalBus from '../globaBus'
export default {
  data () {
    return {
      minHeight: '',
      phonePopupVisible: false,
      pwdPopupVisible: false,
      uploading: false,
      updatePhoneData: {
        phone: '',
        oldSms: '',
        newSms: ''
      },
      oldCodeMessage: '获取验证码',
      newCodeMessage: '获取验证码',
      oldCodeGetting: false,
      newCodeGetting: false,
      phoneErrorMessage: '',
      updatePhoneState: {
        phone: ''
      },
      updatePwdData: {
        oldPassword: '',
        newPassword: '',
        pwdConfirm: '',
        sms: ''
      },
      updatePwdState: {
        oldPassword: '',
        newPassword: '',
        pwdConfirm: ''
      },
      pwdConfirmMessage: '',
      updatePwding: false,
      updatePhoneing: false,
      updatePwdByPhone: false,
      pwdCodeMessage: '获取验证码',
      pwdCodeGetting: false
    }
  },
  computed: {
    user: function () {
      return this.$global.USER
    }
  },
  created () {
  },
  activated () {
    this.minHeight = window.innerHeight - 103
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    openNamePrompt () {
      let title = this.user.username ? '修改用户名' : '新用户名'
      this.$messageBox.prompt(' ', title).then(({ value, action }) => {
        if (value && value.trim() && action === 'confirm') {
          this.$api.updateUserName(value, data => {
            this.$set(this.$global.USER, 'username', value)
            this.$toast({
              message: '修改成功',
              iconClass: 'mintui mintui-success',
              duration: 1000
            })
          }, err => {
            this.$toast({
              message: err.message
            })
          })
        }
      }).catch(action => {
      })
    },
    showPhonePopup () {
      this.phonePopupVisible = true
      this.updatePhoneData = {
        phone: '',
        oldSms: '',
        newSms: ''
      }
      this.phoneErrorMessage = ''
      this.updatePhoneState = {
        phone: ''
      }
    },
    showPwdPopup () {
      this.pwdPopupVisible = true
      this.updatePwdData = {
        oldPassword: '',
        newPassword: '',
        pwdConfirm: ''
      }
      this.updatePwdState = {
        oldPassword: '',
        newPassword: '',
        pwdConfirm: ''
      }
      this.pwdConfirmMessage = ''
    },
    updatePhone () {
      if (this.phoneErrorMessage || !this.updatePhoneData.oldSms || !this.updatePhoneData.newSms || !this.updatePhoneData.phone) {
        return
      }
      if (this.updatePhoneing) {
        return
      }
      this.updatePhoneing = true
      this.$api.updateUserPhone(this.updatePhoneData, data => {
        this.$set(this.$global.USER, 'iphone', this.updatePhoneData.phone)
        this.phonePopupVisible = false
        this.updatePhoneing = false
        this.$toast({
          message: '修改成功',
          iconClass: 'mintui mintui-success',
          duration: 1000
        })
      }, err => {
        this.phonePopupVisible = false
        this.updatePhoneing = false
        this.toast('修改失败,' + err.message)
      })
    },
    pwdChange () {
      this.updatePwdByPhone = !this.updatePwdByPhone
      this.updatePwdData = {
        oldPassword: '',
        newPassword: '',
        pwdConfirm: '',
        sms: ''
      }
    },
    updatePwd () {
      if (this.updatePwdByPhone) {
        if (!this.updatePwdData.sms) {
          return
        }
      } else {
        if (!this.updatePwdData.oldPassword) {
          return
        }
      }
      if (this.pwdConfirmMessage || !this.updatePwdData.newPassword || !this.updatePwdData.pwdConfirm) {
        return
      }
      if (this.updatePwding) {
        return
      }
      this.updatePwding = true
      this.$api.updateUserPwd(this.updatePwdData, data => {
        this.pwdPopupVisible = false
        this.updatePwding = false
        this.$toast({
          message: '修改成功',
          iconClass: 'mintui mintui-success',
          duration: 1000
        })
      }, err => {
        this.pwdPopupVisible = false
        this.updatePwding = false
        this.toast('修改失败,' + err.message)
      })
    },
    upload (e) {
      let file = e.target.files[0]
      // 清空上传文件域
      e.target.value = null
      if (!/image\/\w+/.test(file.type)) {
        this.$toast({
          message: '只能上传图片'
        })
        return
      }
      if (file.size >= 5000000) {
        this.$toast({
          message: '图片不能大于5M'
        })
        return
      }
      let snedData = new FormData()
      snedData.append('file', file)
      snedData.append('type', 'IMAGE')
      this.uploading = true
      this.$api.uploadImage(snedData, (data) => {
        let getData = data
        this.$api.updateUserHeadPic(getData.file.id, data => {
          this.$set(this.$global.USER, 'head_pic', getData.file.id)
        }, err => {
          this.$toast({
            message: err.message
          })
        })
        this.uploading = false
      }, (e) => {
        this.$toast({
          message: e.message
        })
        this.uploading = false
      })
    },
    loadTop () {
      this.$api.getUserInfo('', data => {
        this.$global.USER = data.resource
        this.$refs.loadTop.onTopLoaded()
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        if (err.responseCode !== '401') {
          this.$toast({
            message: err.message
          })
        } else {
          this.$router.replace({name: 'login'})
        }
      })
    },
    checkPwd () {
      if (this.updatePwdData.pwdConfirm !== this.updatePwdData.newPassword) {
        this.updatePwdState.pwdConfirm = 'error'
        this.pwdConfirmMessage = '两次密码不一致'
        return false
      } else {
        this.updatePwdState.pwdConfirm = ''
        this.pwdConfirmMessage = ''
        return true
      }
    },
    check (key) {
      if (this.validateField(key, this.updatePhoneData)) {
        this.$api.detection(this.updatePhoneData[key], data => {
          if (data.result) {
            this.updatePhoneState[key] = 'success'
            this.phoneErrorMessage = ''
          } else {
            this.updatePhoneState[key] = 'error'
            if (key === 'phone') {
              this.phoneErrorMessage = '此手机号已存在'
              this.toast('此手机号已存在')
            }
          }
        }, e => {
          this.updatePhoneState[key] = 'error'
          this.phoneErrorMessage = '检测失败'
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
            this.phoneErrorMessage = '请输入正确手机号'
            this.updatePhoneState['phone'] = 'error'
            valid = false
          }
          break
        default:
          break
      }
      return valid
    },
    getCode (flg) {
      if (flg === 'old') {
        if (!this.oldCodeGetting && this.user.iphone) {
          this.oldCodeGetting = true
          this.oldCodeMessage = 60
          let oldCodeInterval = setInterval(() => {
            if (this.oldCodeMessage === 0) {
              this.oldCodeMessage = '获取验证码'
              this.oldCodeGetting = false
              window.clearInterval(oldCodeInterval)
            } else {
              this.oldCodeMessage--
            }
          }, 1000)
          this.$api.postCode(this.user.iphone, (data) => {
            this.toast('验证码发送成功')
          }, (e) => {
            this.oldCodeGetting = false
            window.clearInterval(oldCodeInterval)
            this.oldCodeMessage = '获取验证码'
            this.toast('验证码发送失败')
          })
        }
      } else if (flg === 'new') {
        if (!this.newCodeGetting && this.validateField('phone', this.updatePhoneData)) {
          if (this.phoneErrorMessage) {
            return
          }
          this.newCodeGetting = true
          this.newCodeMessage = 60
          let newCodeInterval = setInterval(() => {
            if (this.newCodeMessage === 0) {
              this.newCodeMessage = '获取验证码'
              this.newCodeGetting = false
              window.clearInterval(newCodeInterval)
            } else {
              this.newCodeMessage--
            }
          }, 1000)
          this.$api.postCode(this.updatePhoneData.phone, (data) => {
            this.toast('验证码发送成功')
          }, (e) => {
            this.newCodeGetting = false
            window.clearInterval(newCodeInterval)
            this.newCodeMessage = '获取验证码'
            this.toast('验证码发送失败')
          })
        }
      } else if (flg === 'pwd') {
        if (!this.pwdCodeGetting && this.user.iphone) {
          this.pwdCodeGetting = true
          this.pwdCodeMessage = 60
          let pwdCodeInterval = setInterval(() => {
            if (this.pwdCodeMessage === 0) {
              this.pwdCodeMessage = '获取验证码'
              this.pwdCodeGetting = false
              window.clearInterval(pwdCodeInterval)
            } else {
              this.pwdCodeMessage--
            }
          }, 1000)
          this.$api.postCode(this.user.iphone, (data) => {
            this.toast('验证码发送成功')
          }, (e) => {
            this.pwdCodeGetting = false
            window.clearInterval(pwdCodeInterval)
            this.pwdCodeMessage = '获取验证码'
            this.toast('验证码发送失败')
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
img.item-head {
  width: 32px;
  height: 32px;
  border-radius:16px;
}

.mint-popup-fade {
  width: 100%;
  height: 100%;
  background-color: #fbfbfb;
}

.mint-popup-fade .mint-button.confirmBtn {
  width: 90%;
  margin: 10px auto;
}

.formItemButton {
  position: absolute;
  z-index: 2001;
}

.update-title {
  display: block;
  text-align: center;
  padding: 10px 0;
}

.update-title h5 {
  font-weight: 400;
  font-size: 18px;
  color: #333333;
}

.update-title img {
  width: 30%;
  margin: 10px auto;
}

.auctionFormBtn {
  position: absolute;
  right: 10px;
  width: 80px;
  top: -18px;
}

.mint-field-core {
  width: 50%;
}

.mint-msgbox-title {
  font-size: 16px;
  font-weight: 400;
}

.mint-msgbox-input input {
  height: 32px;
  line-height: 32px;
  padding-top: 0px;
}

input.fileInput {
  background-color: rgba(0, 0, 0, 0);
  opacity: 0;
  position: absolute;
  width: 100%;
  height: 48px;
  left: 0;
  z-index: 3;
}
</style>
