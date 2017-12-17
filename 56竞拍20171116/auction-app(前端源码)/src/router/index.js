import Vue from 'vue'
import Router from 'vue-router'

const enter = resolve => require(['views/enter'], resolve)
const home = resolve => require(['views/home'], resolve)
const newDeal = resolve => require(['views/newDeal'], resolve)
const allGoods = resolve => require(['views/allGoods'], resolve)
const userCenter = resolve => require(['views/userCenter'], resolve)
const tenYuan = resolve => require(['views/tenYuan'], resolve)
const message = resolve => require(['views/message'], resolve)
const messageInfo = resolve => require(['views/messageInfo'], resolve)
const recharge = resolve => require(['views/recharge'], resolve)
const showOrder = resolve => require(['views/showOrder'], resolve)
const showOrderDetail = resolve => require(['views/showOrderDetail'], resolve)
const addShowOrder = resolve => require(['views/addShowOrder'], resolve)
const helpCenter = resolve => require(['views/helpCenter'], resolve)
const sign = resolve => require(['views/sign'], resolve)
const signRule = resolve => require(['views/signRule'], resolve)
const signDetail = resolve => require(['views/signDetail'], resolve)
const goodsDetail = resolve => require(['views/goodsDetail'], resolve)
const userInfo = resolve => require(['views/userInfo'], resolve)
const myAuction = resolve => require(['views/myAuction'], resolve)
const userAddress = resolve => require(['views/userAddress'], resolve)
const editUserAddress = resolve => require(['views/editUserAddress'], resolve)
const setting = resolve => require(['views/setting'], resolve)
const collection = resolve => require(['views/collection'], resolve)
const bidderRecord = resolve => require(['views/bidderRecord'], resolve)
const goodsHistoryDetail = resolve => require(['views/goodsHistoryDetail'], resolve)
const login = resolve => require(['views/login'], resolve)
const smsLogin = resolve => require(['views/smsLogin'], resolve)
const register = resolve => require(['views/register'], resolve)
const messageList = resolve => require(['views/messageList'], resolve)
const orderDetail = resolve => require(['views/orderDetail'], resolve)
const agreement = resolve => require(['views/agreement'], resolve)

Vue.use(Router)

export default new Router({
  base: '/app/',
  routes: [
    {
      path: '/',
      redirect: '/enter'
    },
    {
      path: '/enter',
      component: enter,
      children: [
        {
          path: '/',
          redirect: '/home'
        },
        {
          path: '/home',
          name: 'home',
          component: home,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/newDeal',
          name: 'newDeal',
          component: newDeal,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/allGoods',
          name: 'allGoods',
          component: allGoods,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/userCenter',
          name: 'userCenter',
          component: userCenter,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/tenYuan',
          name: 'tenYuan',
          component: tenYuan,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/message',
          name: 'message',
          component: message,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/messageList',
          name: 'messageList',
          component: messageList,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/messageInfo',
          name: 'messageInfo',
          component: messageInfo,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/recharge',
          name: 'recharge',
          component: recharge
        },
        {
          path: '/showOrder',
          name: 'showOrder',
          component: showOrder,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/showOrderDetail',
          name: 'showOrderDetail',
          component: showOrderDetail,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/addShowOrder',
          name: 'addShowOrder',
          component: addShowOrder
        },
        {
          path: '/helpCenter',
          name: 'helpCenter',
          component: helpCenter,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/sign',
          name: 'sign',
          component: sign
        },
        {
          path: '/signRule',
          name: 'signRule',
          component: signRule
        },
        {
          path: '/signDetail',
          name: 'signDetail',
          component: signDetail
        },
        {
          path: '/goodsDetail',
          name: 'goodsDetail',
          component: goodsDetail,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/goodsHistoryDetail',
          name: 'goodsHistoryDetail',
          component: goodsHistoryDetail,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/orderDetail',
          name: 'orderDetail',
          component: orderDetail
        },
        {
          path: '/bidderRecord',
          name: 'bidderRecord',
          component: bidderRecord,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/userInfo',
          name: 'userInfo',
          component: userInfo
        },
        {
          path: '/myAuction',
          name: 'myAuction',
          component: myAuction
        },
        {
          path: '/userAddress',
          name: 'userAddress',
          component: userAddress
        },
        {
          path: '/editUserAddress',
          name: 'editUserAddress',
          component: editUserAddress
        },
        {
          path: '/setting',
          name: 'setting',
          component: setting,
          meta: {
            noAuth: true
          }
        },
        {
          path: '/collection',
          name: 'collection',
          component: collection
        },
        {
          path: '/agreement',
          name: 'agreement',
          component: agreement,
          meta: {
            noAuth: true
          }
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: login,
      meta: {
        noAuth: true
      }
    },
    {
      path: '/smsLogin',
      name: 'smsLogin',
      component: smsLogin,
      meta: {
        noAuth: true
      }
    },
    {
      path: '/register',
      name: 'register',
      component: register,
      meta: {
        noAuth: true
      }
    }
  ]
})
