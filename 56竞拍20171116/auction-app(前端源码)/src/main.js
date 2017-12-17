import Vue from 'vue'
import App from './App'
import router from './router'
import globalBus from './globaBus'
import * as api from './api' // 导入api接口
import * as constants from './constants' // 导入常量
import './filter' // 引用自定义过滤器
import './mintUi'
Vue.prototype.$api = api // 挂载到全局，在组件里通过this.api获取
Vue.prototype.$const = constants // 挂载到全局，在组件里通过this.constants对象
Vue.prototype.$global = globalBus

router.beforeEach((to, from, next) => {
  next()
})

Vue.config.productionTip = false
/* eslint-disable no-new */
const vm = new Vue({
  el: '#app',
  router,
  render: h => h(App)
})

// 统一处理ajax数据返回
api.allHandle.handleSuccess = function (data, success, failure) {
  if (data.responseCode === '200') {
    success(data.resultParm)
  } else if (data.responseCode === '401') {
    globalBus.USER = {}
    // vm.$router.push('/login')
    failure(data)
  } else if (data.responseCode === '407') {
    vm.$messageBox({
      title: '公告',
      message: '系统维护中',
      showConfirmButton: false,
      closeOnClickModal: false
    })
  } else {
    if (failure) {
      failure(data)
    } else {
      vm.$messageBox({
        title: data.responseCode,
        message: data.message,
        confirmButtonText: '关闭'
      })
    }
  }
}

// 处理ajax最后的异常
api.allHandle.handleCatch = function (err) {
  console.error(err)
  if (err.toString().indexOf('404') > -1) {
    vm.$messageBox({
      title: '404',
      message: '网络连接异常'
    })
  } else {
    vm.$messageBox({
      message: '网络连接失败'
    })
  }
}
