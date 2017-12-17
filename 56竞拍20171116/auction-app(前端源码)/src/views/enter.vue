<template>
  <div id="enter">
      <keep-alive :exclude="excludeList">
        <router-view></router-view>
      </keep-alive>
  </div>
</template>

<script>
  import globalBus from '../globaBus'
  export default {
    name: 'enter',
    components: {
    },
    data () {
      return {
        excludeList: [
          'editUserAddress',
          'message',
          'messageInfo',
          'showOrderDetail',
          'bidderRecord',
          'orderDetail',
          'recharge',
          'addShowOrder',
          'login',
          'smsLogin',
          'register',
          'helpCenter'
        ]
      }
    },
    computed: {
    },
    beforeRouteEnter (to, from, next) {
      let token = window.localStorage.getItem('token')
      if (token) {
        let sendDate = {
          identifier: window.localStorage.getItem('identifier'),
          refreshToken: token
        }
        globalBus.$api.login(sendDate, data => {
          window.localStorage.setItem('expirationTime', data.resource.expirationTime)
          window.localStorage.setItem('token', data.resource.token)
          globalBus.$api.getUserInfo({}, data => {
            globalBus.USER = data.resource
            if (globalBus.WS) {
              globalBus.WS.close()
            } else {
              globalBus.initWebSocket()
            }
            next()
          }, err => {
            next()
            if (globalBus.WS) {
              globalBus.WS.close()
            } else {
              globalBus.initWebSocket()
            }
            console.log(err.message)
          })
        }, err => {
          if (globalBus.WS) {
            globalBus.WS.close()
          } else {
            globalBus.initWebSocket()
          }
          if (to.meta.noAuth) {
            next()
          } else {
            next('/login')
          }
          console.log(err.message)
        })
      } else {
        if (globalBus.WS) {
          globalBus.WS.close()
        } else {
          globalBus.initWebSocket()
        }
        if (to.meta.noAuth) {
          next()
        } else {
          next('/login')
        }
      }
    },
    created () {
      this.setHeadLine()
    },
    methods: {
      setHeadLine () {
        this.$set(this.$global, 'HOME_HEAD_LINE', [])
        this.$api.findNewDeals({ pageNo: 1, pageSize: 20 }, data => {
          data.resource.content.forEach(item => {
            let temp = {
              username: item.name,
              price: item.price,
              itemId: item.item_id,
              itemName: item.item_name,
              key: item.item_id + '' + item.price + '' + item.time
            }
            this.$global.HOME_HEAD_LINE.push(temp)
          })
        }, err => {
          console.log(err.message)
        })
      }
    }
  }
</script>

<style>
</style>
