import Vue from 'vue'

// 日期时间格式显示
Vue.filter('date', function (value) {
  const monthsShort = {'Jan': 1, 'Feb': 2, 'Mar': 3, 'Apr': 4, 'May': 5, 'Jun': 6, 'Jul': 7, 'Aug': 8, 'Sep': 9, 'Oct': 10, 'Nov': 11, 'Dec': 12}
  if (!value) return ''
  let dateArray = new Date(parseInt(value)).toString().split(' ')
  let year = dateArray[3]
  let month = monthsShort[dateArray[1]]
  let day = dateArray[2]
  let time = dateArray[4]
  return year + '-' + month + '-' + day + ' ' + time
})

// 金钱格式显示
Vue.filter('currency', function (value, sign) {
  var symbol = sign || '￥'
  return symbol + '' + parseFloat(value).toFixed(2)
})

// 挂载到全局
Vue.prototype.$filter = {
  date: Vue.filter('date'),
  currency: Vue.filter('currency')
}
