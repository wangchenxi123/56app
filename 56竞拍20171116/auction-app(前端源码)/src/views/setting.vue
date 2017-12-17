<template>
  <div class="setting">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>系统设置</span>
      </div>
      <div class="page-content">
        <mt-cell title="常见问题" is-link to="helpCenter"></mt-cell>
        <mt-cell title="用户协议" is-link to="agreement"></mt-cell>
        <mt-cell title="关于我们" is-link to="helpCenter"></mt-cell>
        <br>
        <br>
        <mt-button v-if="$global.USER.id" size="large" type="danger" style="width: 90%;display: block;margin: 20px auto;background-color: red;" @click="loginOut">退出登录</mt-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'setting',
  data () {
    return {
    }
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    loginOut () {
      this.$api.loginout(null, data => {
        this.$global.USER = {}
        window.localStorage.removeItem('token')
        window.localStorage.removeItem('identifier')
        if (this.$global.WS) {
          this.$global.WS.close()
        }
        this.$router.replace('/userCenter')
        this.$toast({
          message: '退出成功'
        })
      }, err => {
        this.$toast({
          message: err.message
        })
      })
    }
  },
  mounted () {
  },
  watch: {
  }
}
</script>

<style scoped>
.mint-button {
  width: 90%;
  margin: 10px auto;
}
</style>
