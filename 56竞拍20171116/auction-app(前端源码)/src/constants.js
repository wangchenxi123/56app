// 常量对象
export const ITEM_QUERY_TYPE = [
  {value: 'POPULAR', label: '热门爆款'},
  {value: 'TODAY', label: '今日增加'},
  {value: 'DISCOUNT', label: '优惠最多'},
  {value: 'PRICE_AFTER_COUPON', label: '券后最低'},
  {value: 'END', label: '最后疯抢'}
]
export const ORDER_STATE = {
  WAITING_PAYMENT: '待付款',
  WAITING_CHOICE_ADDRESS: '待选择收货地址',
  WAITING_SHIP: '待发货',
  ALREADY_SHIPMENTS: '已出货',
  WAITING_RECEIPT: '待收货',
  WAITING_SUN_ALONE: '待晒单',
  CONSUMMATION: '完成'
}
export const WS_TYPE = {
  HEADLINES: '拍卖头条',
  BIDDER: '出价人信息',
  SELF_NOTICE: '个人通知',
  SYSTEM_NOTICE: '系统通知',
  BID_SUCCESS_NOTICE: '出价成功通知',
  BID_ERROR_NOTICE: '出价失败通知'
}

export const TITLE = '竞拍'

export const LINK = '客服QQ：xxxxxxxx'
export const SERVECE_TIME = '服务时间：周一至周六9:30-18:30'
