import Vue from 'vue'
import axios from 'axios'
import querystring from 'querystring'
import { hexMd5 } from '../static/js/md5.js' // MD5加密
import { Promise } from 'es6-promise-polyfill'
window.Promise = Promise
Vue.prototype.$ajax = axios // 挂载到全局
axios.defaults.baseURL = 'http://123.207.51.81' // 访问路径
axios.defaults.withCredentials = true // 跨域带sension
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
axios.defaults.transformRequest = function (data) {
  return querystring.stringify(data)
}
axios.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)   // 返回接口返回的错误信息
  })

export const file = {
  'see': function (fid) {
    if (fid) {
      if (axios.defaults.baseURL) {
        return axios.defaults.baseURL + '/file/see?fid=' + fid
      } else {
        return '/file/see?fid=' + fid
      }
    } else {
      return ''
    }
  },
  'uploadUrl': axios.defaults.baseURL + '/file/upload'
}

/**
 * 获取url参数
 * @param name
 * @returns {*}
 * @constructor
 */
export const GetQueryString = function (name) {
  let reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  let r = window.location.search.substr(1).match(reg)
  if (r !== null) {
    return decodeURI(r[2])
  }
  return ''
}

// 图片上传
export const uploadImage = (data, success, failure) => {
  let conf = {
    url: '/file/uploadImg',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    transformRequest: function (data) {
      return data
    }
  }
  ajax(conf, success, failure)
}

/**
 * 登录
 * @param data
 * @param success
 * @param failure
 */
export const login = (data, success, failure) => {
  let conf = {
    url: '/client/login',
    method: 'post',
    data: {
      identifier: data.identifier,
      password: data.password ? hexMd5(data.password) : null,
      sms: data.sms,
      validate: data.validate,
      refreshToken: data.refreshToken
    }
  }
  ajax(conf, success, failure)
}
/**
 * 退出登录
 * @param data
 * @param success
 * @param failure
 */
export const loginout = (data, success, failure) => {
  let conf = {
    url: '/loginout',
    method: 'get'
  }
  ajax(conf, success, failure)
}

/**
 * 注册
 * @param data
 * @param success
 * @param failure
 */
export const registered = (data, success, failure) => {
  let conf = {
    url: '/client/register',
    method: 'post',
    data: {
      phone: data.phone,
      sms: data.sms,
      password: data.pwd ? hexMd5(data.pwd) : ''
    }
  }
  ajax(conf, success, failure)
}
/**
 * 注册
 * @param data
 * @param success
 * @param failure
 */
export const thirdLoginRegistered = (data, success, failure) => {
  let conf = {
    url: '/client/register/thirdLogin',
    method: 'post',
    data: {
      phone: data.phone,
      sms: data.sms,
      thirdLogin: data.thirdLogin
    }
  }
  ajax(conf, success, failure)
}

/**
 * 获取验证码
 * @param phone
 * @param success
 * @param failure
 */
export const postCode = (phone, success, failure) => {
  let conf = {
    url: '/sendSMS',
    method: 'post',
    data: {
      phone: phone
    }
  }
  ajax(conf, success, failure)
}
/**
 * 获取验证码
 * @param phone
 * @param success
 * @param failure
 */
export const getCode = (phone, success, failure) => {
  let conf = {
    url: '/sendSMS',
    method: 'get',
    params: {
      phone: phone
    }
  }
  ajax(conf, success, failure)
}

/**
 * 检测账号是否已经被注册
 * @param success
 * @param failure
 */
export const detection = (data, success, failure) => {
  let conf = {
    url: '/detection',
    method: 'get',
    params: {
      identifier: data.trim(),
      loginType: 'TELEPHONE'
    }
  }
  ajax(conf, success, failure)
}

/**
 * 获取轮播图列表
 */
export const findCarousels = (data, success, failure) => {
  let conf = {
    url: '/client/carouselFigures',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 获取首页分类列表
 */
export const findActivities = (data, success, failure) => {
  let conf = {
    url: '/client/labels',
    method: 'get'
  }
  ajax(conf, success, failure)
}

/**
 * 首页查询所有商品
 */
export const findItems = (data, success, failure) => {
  let conf = {
    url: '/client/items',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 首页查询所有新手商品
 */
export const findNoviceItems = (data, success, failure) => {
  let conf = {
    url: '/client/novice/items',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 首页查询单个商品
 */
export const getItem = (id, success, failure) => {
  let conf = {
    url: '/client/item',
    method: 'get',
    params: {
      itemId: id,
      number: 20
    }
  }
  ajax(conf, success, failure)
}

/**
 * 用户对某一个商品进行竞拍操作
 */
export const auction = (data, success, failure) => {
  let conf = {
    url: '/client/shot',
    method: 'post',
    data: {
      itemId: data.itemId,
      periods: data.periods,
      number: data.number
    }
  }
  ajax(conf, success, failure)
}

/**
 * 查询某一个商品竞拍记录
 */
export const findBiddersRecor = (data, success, failure) => {
  let conf = {
    url: '/client/bidders',
    method: 'get',
    params: {
      itemId: data.itemId,
      sealedId: data.sealedId,
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 获取系统时间
 */
export const getSystemTime = (data, success, failure) => {
  let conf = {
    url: '/client/system_time',
    method: 'get'
  }
  ajax(conf, success, failure)
}

/**
 * 查询最新成交
 */
export const findNewDeals = (data, success, failure) => {
  let conf = {
    url: '/client/transactions',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 根据最新成交记录封存id查找该期记录
 */
export const getNewDeal = (id, success, failure) => {
  let conf = {
    url: '/client/transaction',
    method: 'get',
    params: {
      record_id: id
    }
  }
  ajax(conf, success, failure)
}

/**
 * 根据最新成交记录封存id查找该期记录
 */
export const findDealHistorysByItemId = (data, success, failure) => {
  let conf = {
    url: '/client/transaction/history',
    method: 'get',
    params: {
      itemId: data.itemId,
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 前台查询晒单
 */
export const findShowOrders = (data, success, failure) => {
  let conf = {
    url: '/client/show',
    method: 'get',
    params: {
      userId: data.userId,
      itemId: data.itemId,
      orderId: data.orderId,
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 查询收藏列表
 */
export const findCollections = (data, success, failure) => {
  let conf = {
    url: '/client/user/collection',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 添加收藏
 */
export const addCollection = (itemId, success, failure) => {
  let conf = {
    url: '/client/user/collection',
    method: 'post',
    data: {
      itemId: itemId
    }
  }
  ajax(conf, success, failure)
}

/**
 * 删除收藏
 */
export const deleteCollection = (collectionId, success, failure) => {
  let conf = {
    url: '/client/user/collection',
    method: 'post',
    params: {
      _method: 'delete'
    },
    data: {
      collectionId: collectionId
    }
  }
  ajax(conf, success, failure)
}

/**
 * 用户添加晒单
 */
export const addShowOrder = (data, success, failure) => {
  let conf = {
    url: '/client/show',
    method: 'post',
    data: {
      showItem: data ? JSON.stringify(data) : null
    }
  }
  ajax(conf, success, failure)
}
/**
 * 查询所有商品分类
 */
export const findSorts = (data, success, failure) => {
  let conf = {
    url: '/client/sorts',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}

/**
 * 查询指定分类的商品分类
 */
export const findItemsBySortId = (sortId, success, failure) => {
  let conf = {
    url: '/client/sort',
    method: 'get',
    params: {
      sortId: sortId
    }
  }
  ajax(conf, success, failure)
}

/**
 * 获取个人资料
 */
export const getUserInfo = (data, success, failure) => {
  let conf = {
    url: '/client/user',
    method: 'get'
  }
  ajax(conf, success, failure)
}

/**
 * 更改头像
 */
export const updateUserHeadPic = (headPicId, success, failure) => {
  let conf = {
    url: '/client/user/avatar',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      headPicId: headPicId
    }
  }
  ajax(conf, success, failure)
}

/**
 * 更改用户名
 */
export const updateUserName = (name, success, failure) => {
  let conf = {
    url: '/client/user/name',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      name: name
    }
  }
  ajax(conf, success, failure)
}
/**
 * 更改手机号码
 */
export const updateUserPhone = (data, success, failure) => {
  let conf = {
    url: '/client/user/phone',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      phone: data.phone,
      oldPhoneSMS: data.oldSms,
      newPhoneSMS: data.newSms
    }
  }
  ajax(conf, success, failure)
}
/**
 * 更改密码
 */
export const updateUserPwd = (data, success, failure) => {
  let conf = {
    url: '/client/password',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      newPassword: data.newPassword ? hexMd5(data.newPassword) : '',
      oldPassword: data.oldPassword ? hexMd5(data.oldPassword) : '',
      sms: data.sms
    }
  }
  ajax(conf, success, failure)
}
/**
 * 个人竞拍记录
 */
export const findAuctionRecords = (data, success, failure) => {
  let conf = {
    url: '/client/auction/record',
    method: 'get',
    params: {
      selete_type: data.selete_type,
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}
/**
 * 邀请有奖
 */
export const invitationToReward = (data, success, failure) => {
  let conf = {
    url: '/client/invite/reward',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 推广注册普通用户
 */
export const extensionRegistered = (data, success, failure) => {
  let conf = {
    url: '/promotion/register',
    method: 'post',
    data: {
      userId: data.userId,
      phone: data.phone,
      sms: data.sms
    }
  }
  ajax(conf, success, failure)
}
/**
 * 为中奖订单添加选定收获地址
 */
export const settingAddress = (data, success, failure) => {
  let conf = {
    url: '/client/order/address',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      orderId: data.orderId,
      addressId: data.addressId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 添加一条地址信息
 */
export const addAddress = (data, success, failure) => {
  let conf = {
    url: '/client/user/address',
    method: 'post',
    data: {
      userAddress: JSON.stringify(data)
    }
  }
  ajax(conf, success, failure)
}
/**
 * 获取用户所有地址
 */
export const findAddresss = (data, success, failure) => {
  let conf = {
    url: '/client/user/address',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 删除一个地址
 */
export const deleteAddress = (addressId, success, failure) => {
  let conf = {
    url: '/client/user/address',
    method: 'post',
    params: {
      _method: 'delete'
    },
    data: {
      addressId: addressId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 修改一个地址
 */
export const updateAddress = (data, success, failure) => {
  let conf = {
    url: '/client/user/address',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      userAddress: JSON.stringify(data)
    }
  }
  ajax(conf, success, failure)
}
/**
 * 分页查询用户公告通知信息
 */
export const findAnnouncementNotice = (data, success, failure) => {
  let conf = {
    url: '/client/notice/announcement/list',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}
/**
 * 分页查询用户公告通知信息
 */
export const findPersonalNotice = (data, success, failure) => {
  let conf = {
    url: '/client/notice/personal/list',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}
/**
 * 查看通知
 */
export const getNotice = (noticeId, success, failure) => {
  let conf = {
    url: '/client/notice',
    method: 'get',
    params: {
      noticeId: noticeId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 用户未读通知数
 */
export const findUnreadNumber = (data, success, failure) => {
  let conf = {
    url: '/client/notice/hasUnread',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 系统未读通知数
 */
export const getSystemUnreadNum = (data, success, failure) => {
  let conf = {
    url: '/client/notice/announcement/hasUnread',
    method: 'get'
  }
  ajax(conf, success, failure)
}

/**
 * 个人未读通知数
 */
export const getPersonUnreadNum = (data, success, failure) => {
  let conf = {
    url: '/client/notice/personal/hasUnread',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 阅读通知
 */
export const read = (noticeId, success, failure) => {
  let conf = {
    url: '/client/notice/read',
    method: 'post',
    data: {
      noticeId: noticeId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 每日签到
 */
export const sign = (data, success, failure) => {
  let conf = {
    url: '/activity/sign',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 获取用户签到信息
 */
export const getUserSignInfo = (data, success, failure) => {
  let conf = {
    url: '/activity/sign/message',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 领取固定奖品
 */
export const receiveTreasure = (data, success, failure) => {
  let conf = {
    url: '/activity/sign/prize',
    method: 'get'
  }
  ajax(conf, success, failure)
}
/**
 * 兑换奖励
 */
export const receive = (exchangeKey, success, failure) => {
  let conf = {
    url: '/activity/sign/exchange',
    method: 'get',
    params: {
      exchangeKey: exchangeKey
    }
  }
  ajax(conf, success, failure)
}
/**
 * 获取积分流动详情
 */
export const integralFlow = (data, success, failure) => {
  let conf = {
    url: '/client/integral/flow',
    method: 'get',
    params: {
      pageNo: data.pageNo,
      pageSize: data.pageSize
    }
  }
  ajax(conf, success, failure)
}
/**
 * 充值拍币
 */
export const recharge = (data, success, failure) => {
  let conf = {
    url: '/client/recharge',
    method: 'post',
    data: {
      number: data.number,
      frpId: data.frpId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 订单付款下单
 */
export const payment = (data, success, failure) => {
  let conf = {
    url: '/client/order/payment',
    method: 'post',
    data: {
      orderId: data.orderId,
      frpId: data.frpId
    }
  }
  ajax(conf, success, failure)
}
/**
 * 获取订单
 */
export const getOrder = (data, success, failure) => {
  let conf = {
    url: '/client/order',
    method: 'get',
    params: {
      orderId: data
    }
  }
  ajax(conf, success, failure)
}
/**
 * 确认收货
 */
export const confirmOrder = (data, success, failure) => {
  let conf = {
    url: '/client/order/sign',
    method: 'post',
    params: {
      _method: 'put'
    },
    data: {
      orderId: data
    }
  }
  ajax(conf, success, failure)
}
/**
 * 查询充值展示标志位
 */
export const showPayPage = (data, success, failure) => {
  let conf = {
    url: '/client/setting/recharge',
    method: 'get'
  }
  ajax(conf, success, failure)
}

// 统一处理返回数据
export const allHandle = {
  handleSuccess () {},
  handleCatch () {}
}

/**
 * 过滤空值的属性
 * @param obj
 * @constructor
 */
function FilterData (obj) {
  for (var key in obj) {
    if (obj[key] == null || obj[key] === '') {
      delete obj[key]
    }
  }
}

function ajax (conf, success, failure) {
  FilterData(conf.params) // 过滤空属性;
  FilterData(conf.data)  // 过滤空属性;
  axios(conf)
    .then((res) => {
      allHandle.handleSuccess(res, success, failure)
    })
    .catch((err) => {
      allHandle.handleCatch(err)
    })
}
