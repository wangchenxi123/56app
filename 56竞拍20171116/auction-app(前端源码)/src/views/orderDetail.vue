<template>
  <div class="orderDetail">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>订单详情</span>
      </div>
      <div class="page-content" style="background-color: #F8F8F8;">
        <div class="goodsflow">
          <div>
            <span style="font-size: 15px;"><b>订单状态：</b></span>
            <span style="color: #888;">{{$const.ORDER_STATE[resource.order_state]}}</span>
          </div>
          <br/>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 0}">
            <div class="goodsflow-dot " :class="{'background-red': stateIndex >= 0}"></div>
            <div class="goodsflow-head">恭喜您，赢得成交</div>
            <div class="goodsflow-info"></div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 1}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 1}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 1 ? '已支付':'待支付'}}
              <span v-if="stateIndex > 1" style="font-size: 12px;">(￥{{resource.price}})</span>
              <div class="goodsflow-operation">
                <mt-button v-if="stateIndex === 1" type="danger" size="small" style="width: 80px;height: 25px;" @click="pay">支付</mt-button>
              </div>
            </div>
            <div class="goodsflow-info">
              <div class="goodsflow-info-content" v-if="stateIndex > 1">
                <div>
                  已支付订单￥{{resource.price}}
                </div>
              </div>
            </div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 2}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 2}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 2 ? '已选择收货地址':'待选择收货地址'}}
              <div class="goodsflow-operation">
                <mt-button type="danger" size="small" style="width: 80px;height: 25px;" @click="chooseAddress" v-if="stateIndex === 2">选择地址</mt-button>
              </div>
            </div>
            <div class="goodsflow-info">
              <div class="goodsflow-info-content" v-if="stateIndex > 2">
                <div>
                  已选择收货信息：
                  联系人：{{resource.addressBean.name}}，
                  联系电话：{{resource.addressBean.phone}}，
                  联系地址：{{resource.addressBean.address}} {{resource.addressBean.detailed_address}}
                  {{resource.addressBean.penguin ? ('联系QQ：' + resource.addressBean.penguin) : ''}}
                  {{resource.addressBean.alipay ? ('支付宝：' + resource.addressBean.alipay) : ''}}
                </div>
              </div>
            </div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 3}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 3}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 3 ? '备货中1-3个工作日发货':'待备货'}}
              <div class="goodsflow-operation">
              </div>
            </div>
            <div class="goodsflow-info">
              <div class="goodsflow-info-content" v-if="stateIndex > 3">
                <div>
                  已下单，等待商家发货：您参与的"{{resource.item_name}}" 已经下单了，我们会尽快安排发货。
                </div>
              </div>
            </div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 4}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 4}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 4 ? '已发货':'待发货'}}
              <div class="goodsflow-operation">
              </div>
            </div>
            <div class="goodsflow-info">
              <div class="goodsflow-info-content" v-if="stateIndex > 4">
                <div>
                  您参与竞拍的 "{{resource.item_name}}" 已发货，请注意查收。
                </div>
              </div>
            </div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 5}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 5}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 5 ? '已签收':'待签收'}}
              <div class="goodsflow-operation">
                <mt-button type="danger" size="small" style="width: 80px;height: 25px;" :loading="confirming" @click="confirm" v-if="stateIndex === 5">确认收货</mt-button>
              </div>
            </div>
            <div class="goodsflow-info">
              <div class="goodsflow-info-content" v-if="stateIndex >= 5">
                <div>
                  <template v-if="resource.item_type === 'IN_KIND'">
                    快递单号为： "{{resource.express_number}}"
                  </template>
                  <template v-else-if="resource.item_type === 'VIRTUAL'">
                    卡号： "{{resource.card}}"
                    卡密 "{{resource.density}}"
                  </template>
                </div>
              </div>
            </div>
          </div>
          <div class="goodsflow-cell" :class="{'border-red': stateIndex > 6}">
            <div class="goodsflow-dot" :class="{'background-red': stateIndex >= 6}"></div>
            <div class="goodsflow-head">
              {{stateIndex > 6 ? '已晒单':'待晒单'}}
              <div class="goodsflow-operation">
                <mt-button type="danger" size="small" style="width: 80px;height: 25px;" @click="toShowOrder('toadd')" v-if="stateIndex === 6">晒单</mt-button>
                <mt-button type="danger" size="small" style="width: 80px;height: 25px;" @click="toShowOrder('tosee')" v-if="stateIndex > 6">查看晒单</mt-button>
              </div>
            </div>
            <div class="goodsflow-info">
              <div></div>
            </div>
          </div>
        </div>
        <div style="margin-top: 15px;background-color: #ffffff;">
          <div style="padding: 10px;border-bottom: 1px solid #888;">商品信息</div>
            <div style="font-size: 14px;padding: 0 5px;">
              <div style="display:inline-block;width:25%;">
                <img style="width: 100%;height:100%;" :src="$api.file.see(resource.item_picture)" onerror="this.src='static/img/product/img-computer.png'">
              </div>
              <div style="display:inline-block;width:45%;vertical-align: super;">
                <div>成交人：{{resource.name}}</div>
                <div>市场价：{{resource.marketPrice}}</div>
                <div>竞拍价：<span>{{resource.price}}</span></div>
              </div>
              <div style="position:relative;top: -14px;display:inline-block;text-align: right;width:25%;">
                <b>{{getPercent(resource)}}%</b>
                <div style="font-size: 12px;">节省</div>
              </div>
            </div>
        </div>
      </div>
    </div>
    <!-- 选择地址 -->
    <mt-popup :modal="false" v-model="addrPopupVisible" position="bottom" style="width: 100%;">
      <div style="border-top: 2px solid red;">
        <div style="width: 100%;text-align: center;">
          <mt-button style="float:left" @click="addrPopupVisible = false">取消</mt-button>
          <div style="display: inline-block;width: 50%;line-height:40px;">选择地址</div>
          <mt-button style="float:right" @click="choice">提交</mt-button>
        </div>
        <div>
          <div @click="select(al)" class="address-list" :class="{'select-active': selectAddrressId === al.id}" v-for="(al,$index) in addResource" :key="al.id">
            <h3>{{al.name}}
              <span>{{al.phone}}</span>
              <label v-if="selectAddrressId === al.id" style="background-color: red;color: #fff">√选中</label>
            </h3>
            <h5>{{al.detailed_address}}</h5>
            <p>
              <img src="static/icon/icon-qq.svg" alt="">{{al.penguin}}</p>
            <h4>
              <img src="static/icon/icon-zhifubao.svg" alt="">{{al.alipay}}</h4>
          </div>
          <div class="address-add" style="text-align:center;">
            <router-link tag="p" :to="{name: 'editUserAddress'}">
              <span>+</span>添加地址</router-link>
          </div>
        </div>
      </div>
    </mt-popup>
    <!-- 选择支付 -->
    <mt-popup :modal="false" v-model="payPopupVisible" position="bottom" style="width: 100%;">
      <div style="border-top: 2px solid red;">
        <div style="width: 100%;text-align: center;">
          <mt-button style="float:left" @click="payPopupVisible = false">取消</mt-button>
          <div style="display: inline-block;width: 50%;line-height:40px;">选择支付方式</div>
          <mt-button style="float:right" @click="pay">确定</mt-button>
        </div>
        <div>
          <div class="payList">
            <mt-radio align="right" v-model="frpId" :options="options"></mt-radio>
          </div>
        </div>
      </div>
    </mt-popup>
  </div>
</template>

<script>
  import '../../static/js/iframe.js'
  export default {
    name: 'orderDetail',
    data () {
      return {
        orderId: '',
        resource: {
          addressBean: {}
        },
        addrPopupVisible: false,
        addResource: {},
        selectAddrressId: '',
        confirming: false,
        payPopupVisible: false,
        frpId: 'ALIPAY_H5',
        options: [
          {
            label: '支付宝',
            value: 'ALIPAY_H5'
          },
          {
            label: 'QQ钱包',
            value: 'QQ_WALLET'
          },
          {
            label: '网银支付',
            value: 'ONLINE_BANKING'
          }
        ]
      }
    },
    computed: {
      stateIndex: function () {
        let result = 0
        switch (this.resource.order_state) {
          case 'WAITING_PAYMENT':
            result = 1
            break
          case 'WAITING_CHOICE_ADDRESS':
            result = 2
            break
          case 'WAITING_SHIP':
            result = 3
            break
          case 'ALREADY_SHIPMENTS':
            result = 4
            break
          case 'WAITING_RECEIPT':
            result = 5
            break
          case 'WAITING_SUN_ALONE':
            result = 6
            break
          case 'CONSUMMATION':
            result = 7
            break
        }
        return result
      }
    },
    beforeRouteLeave (to, form, next) {
      let iframeBackBtn = document.getElementById('iframeBackBtn')
      if (iframeBackBtn) {
        iframeBackBtn.click()
        next(false)
      } else {
        next()
      }
    },
    created () {
      this.orderId = this.$route.query.orderId
      if (this.orderId) {
        this.findOrder()
      } else {
        this.$router.go(-1)
      }
    },
    methods: {
      routerBack () {
        this.$router.go(-1)
      },
      findOrder () {
        this.$api.getOrder(this.orderId, data => {
          this.resource = data.resource
        }, err => {
          this.$toast({
            message: err.message
          })
          this.$router.go(-1)
        })
      },
      getPercent (row) {
        let result = ((1 - (parseFloat(row.price) / parseFloat(row.marketPrice))) * 100).toFixed(2)
        if (isNaN(result)) {
          return 100
        } else {
          return result
        }
      },
      pay () {
        if (this.orderId) {
          let sendData = {
            orderId: this.orderId,
            frpId: this.frpId
          }
          this.$api.payment(sendData, data => {
            let url = data.resource.url
            let type = data.resource.type
            window.iframePay.open(url, this.closePay, type)
          }, err => {
            this.$toast({
              message: err.message
            })
            console.log(err.message)
          })
        }
      },
      closePay () {
        this.findOrder()
      },
      findAddresss () {
        this.$api.findAddresss(null, data => {
          this.addResource = data.resource
        }, err => {
          this.$toast({
            message: err.message
          })
        })
      },
      chooseAddress () {
        this.addrPopupVisible = true
        this.findAddresss()
      },
      select (row) {
        this.selectAddrressId = row.id
      },
      choice () {
        if (!this.selectAddrressId) {
          this.$toast({
            message: '请选择地址'
          })
          return
        }
        let sendData = {
          orderId: this.orderId,
          addressId: this.selectAddrressId
        }
        this.$api.settingAddress(sendData, data => {
          this.findOrder()
          this.addrPopupVisible = false
          this.$toast({
            message: '选择成功'
          })
        }, err => {
          this.addrPopupVisible = false
          this.$toast({
            message: err.message
          })
        })
      },
      confirm () {
        if (this.confirming) {
          return
        }
        this.confirming = true
        this.$api.confirmOrder(this.orderId, data => {
          this.findOrder()
          this.confirming = false
          this.$toast({
            message: '收货成功'
          })
        }, err => {
          this.confirming = false
          this.$toast({
            message: `收货失败，${err.message}`
          })
        })
      },
      toShowOrder (flg) {
        if (flg === 'toadd') {
          this.$router.push({name: 'addShowOrder', query: {orderId: this.orderId}})
        } else if (flg === 'tosee') {
          this.$router.push({name: 'showOrderDetail', query: {orderId: this.orderId}})
        }
      }
    }
  }
</script>

<style scoped>
.goodsflow {
  padding: 5px 10px 0px 10px;
  font-size: 14px;
  background-color: #ffffff;
}
.goodsflow-cell {
  position: relative;
  border-left: 2px solid #E8E8E8;
  padding-left: 20px;
}
.goodsflow-dot {
  width: 10px;
  height: 10px;
  border-radius: 30px;
  background-color: #E8E8E8;
  position:absolute;
  top:0px;
  left:-6px;
}
.goodsflow-head {
  position: relative;
  top: -5px;
}
.goodsflow-info {
  padding: 10px 0 20px 0;
}
.goodsflow-info-content {
  position: relative;
  width: 100%;left: -10px;
  padding:5px 0px 5px 10px;
  background-color: #ddd;
  border-radius:5px;
  font-size: 12px;
  color: #5e5353;
}
.goodsflow-operation {
  position: absolute;
  right: 0;
  top:-5px;
}
.goodsflow-info-content>div {
  padding-right: 10px
}
.border-red {
  border-color: red;
}
.background-red {
  background-color: red;
}

.address-list {
  background-color: #ffffff;
  padding: 0px 10px 10px 10px;
  margin-top: 20px;
}

.select-active:before{
  background-color: red !important;
}
.select-active:after{
  background-color: red;
}

.address-list:before {
  content: '';
  height: 6px;
  width: 100%;
  background-image: url('../../static/img/border-letter.svg');
  position: absolute;
  top: 1;
  left: 0;
}

.address-list:after {
  content: '';
  height: 6px;
  width: 100%;
  background-image: url('../../static/img/border-letter.svg');
  position: absolute;
  bottom: 1;
  left: 0;
}

.address-list h3 {
  font-weight: 700;
  font-size: 14px;
  color: #333333;
  line-height: 24px;
  padding-top: 20px;
}

.address-list h3 b {
  font-weight: 400;
  background-color: #ef0000;
  font-size: 12px;
  color: #ffffff;
  padding: 3px 5px;
  margin-right: 10px;
  border-radius: 3px;
}

.address-list h3 span {
  margin-left: 10px;
  margin-right: 10px;
}

.address-list h3 label img {
  width: 16px;
  vertical-align: middle;
  margin-right: 5px;
  margin-left: 10px;
}

.address-list h3 label {
  color: #999999;
  font-weight: 400;
  float: right;
  margin-top: -3px;
  font-size: 12px;
  border-left: 1px solid #eeeeee;
  background-color: #FFFFFF;
  border-radius: 0;
  transition: 0.4s;
}

.address-list h3 label:before {
  background-color: #FFFFFF;
  border-radius: 0;
  transition: 0.4s;
}

.address-list h3 label:active {
  background-color: #eeeeee;
  border-radius: 50%;
  transition: 0.2s;
}

.address-list h5 {
  font-size: 14px;
  line-height: 24px;
  color: #333333;
  font-weight: 400;
}

.address-list h4 {
  padding-bottom: 15px;
  font-size: 14px;
  vertical-align: middle;
  font-weight: 400;
  margin-top: 5px;
}

.address-list h4 img,
.address-list p img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  vertical-align: middle;
}

.address-list p {
  font-size: 14px;
  vertical-align: middle;
  font-weight: 400;
  float: right;
  margin-top: 5px;
  padding-left: 10px;
}

.address-fade {
  height: 44px;
  position: fixed;
  bottom: 10px;
  left: 0;
  width: 100%;
  display: block;
  text-align: center;
}

.address-add {
  height: 44px;
  background-color: #ef0000;
  display: block;
  width: 60%;
  line-height: 44px;
  color: #ffffff;
  border-radius: 30px;
  box-shadow: 1px 1px 10px #f9b1b1;
  transition: 0.4s;
  margin: 0 auto;
}

.address-add:before {
  background-color: #ef0000;
  transition: 0.4s;
}

</style>
