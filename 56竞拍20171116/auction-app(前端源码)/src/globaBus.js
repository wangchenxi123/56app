import Vue from 'vue'
// apicloud
window.apiready = function () {
  if (window.api) {
    var time = 0
    window.api.addEventListener({
      name: 'pause'
    }, (ret, err) => {
      time = new Date().getTime()
    })
    window.api.addEventListener({
      name: 'resume'
    }, (ret, err) => {
      if (new Date().getTime() - time >= 600000) {
        window.location.reload()
      }
    })
  }
}
export default new Vue({
  data () {
    return {
      baseURL: '',
      USER: {},
      WS: '',
      WSMessage: {},
      HOME_HEAD_LINE: [],
      showPay: false,
      HEAD_LINE_INTERVALER: '',
      toastClient: ''
    }
  },
  computed: {
  },
  watch: {
    WSMessage: function (val, oldVal) {
      if (val.message_type === 'HEADLINES' && val.userId) {
        val.key = val.itemId + '' + val.price + '' + val.time
        this.HOME_HEAD_LINE.push(val)
      }
    }
  },
  created () {
  },
  methods: {
    initWebSocket () {
      // if (this.WS) {
      //   return
      // }
      let id = this.USER.id
      if (!id) {
        id = 0
      }
      let url = `ws://${window.location.host}/websocket/${id}`
      let baseURL = this.baseURL = this.$ajax.defaults.baseURL
      if (baseURL) {
        url = `ws://${baseURL.split('//')[1]}/websocket/${id}`
      }
      if ('WebSocket' in window) {
        this.WS = new window.WebSocket(url)
      } else if ('MozWebSocket' in window) {
        this.WS = new window.MozWebSocket(url)
      } else {
        alert('您的浏览器不支持WebSocket。')
        return
      }
      let _this = this
      this.WS.onopen = function () {
        console.log('open')
      }
      this.WS.onmessage = function (event) {
        _this.WSMessage = JSON.parse(event.data)
      }
      this.WS.onclose = function () {
        window.setTimeout(() => {
          _this.initWebSocket()
        }, 1500)
        console.log('close')
      }
      // this.WS.onerror = function () {
      //   // _this.WS = ''
      //   _this.initWebSocket()
      //   console.log('error')
      // }
    },
    getUserInfo () {
      this.$api.getUserInfo({}, data => {
        this.USER = data.resource
        if (this.WS) {
          this.WS.close()
          // this.WS = ''
        } else {
          this.initWebSocket()
        }
      }, err => {
        if (err.resultCode === '401') {
          this.loginByToken()
        }
        console.log(err.message)
      })
    },
    loginByToken () {
      let token = window.localStorage.getItem('token')
      if (token) {
        let sendDate = {
          identifier: window.localStorage.getItem('identifier'),
          refreshToken: token
        }
        this.$api.login(sendDate, data => {
          this.getUserInfo()
          window.localStorage.setItem('expirationTime', data.resource.expirationTime)
          window.localStorage.setItem('token', data.resource.token)
        }, err => {
          console.log(err.message)
        })
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
})
