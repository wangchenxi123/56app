<template>
  <div class="recharge">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>充值</span>
      </div>
  
      <div class="page-content">
        <div class="recharge-head">
          <div style="display: inline-block;">
          <img class="recharge-head-headimg" :src="$api.file.see($global.USER.head_pic)" alt="">
          </div>
          <div style="display: inline-block;vertical-align: top;">
          <h5>{{$global.USER.username}}</h5>
          <p>拍币:
            <span>{{$global.USER.coin}}</span>
          </p>
          </div>
        </div>
        <div class="title">选择充值金额(元)</div>
        <div class="recharge-price">
          <ul class="recharge-price-grids">
            <li v-for="(pl,index) in priceList" :key="index" :class="{active: pl.value == number}" @click="selPrice(pl.value)">
              <a>{{pl.value}}</a>
            </li>
            <li  @click="number = ''">
              <mt-field style="padding: 0px;" :attr="{style: 'text-align: center;'}" :disableClear="true" placeholder="其他金额" type="number" v-model="inputNumber"></mt-field>
              <!-- <a>{{inputNumber}}</a> -->
            </li>
          </ul>
          <p>预计获得:
            <span>{{number || inputNumber || 0}}拍币</span>
          </p>
        </div>

        <div class="title">选择支付方式</div>
        <div class="payList">
          <img slot="icon" src="static/icon/icon-zhifubao.svg">
          <b>推荐已开通支付宝账户的用户使用</b>
          <img slot="icon" style="top: 66px;" src="../../static/img/QqPay.png">
          <b style="top: 89px;">推荐已开通QQ钱包的用户使用</b>
          <img slot="icon" style="top: 124px;" src="../../static/icon/unionpay.svg">
          <b style="top: 144px;">推荐已开通网银支付的用户使用</b>
          <mt-radio align="right" v-model="value" :options="options"></mt-radio>
        </div>
  
        <mt-button type="danger" size="large" @click="recharge">充值</mt-button>
      </div>
    </div>
  </div>
</template>

<script>
  import '../../static/js/iframe.js'
  export default {
    name: 'messageInfo',
    data () {
      return {
        value: '',
        popupVisible3: false,
        inputNumber: '',
        number: 20,
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
        ],
        priceList: [
          { value: 20 },
          { value: 50 },
          { value: 100 },
          { value: 200 },
          { value: 500 }
        ]
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
      next()
    },
    created () {
      if (!this.$global.USER.id) {
        this.$toast({
          message: '请先登录'
        })
        this.$router.replace('/login')
      }
    },
    methods: {
      routerBack () {
        this.$router.push('/userCenter')
      },
      selPrice (value) {
        this.number = value
        this.inputNumber = ''
      },
      recharge () {
        let rechargeNum = parseFloat(this.number || this.inputNumber)
        if (rechargeNum > 0) {
          let sendData = {
            number: rechargeNum,
            frpId: this.frpId
          }
          this.$api.recharge(sendData, data => {
            let url = data.resource.url
            let type = data.resource.type
            window.iframePay.open(url, this.closeRecharge, type)
          }, err => {
            this.$toast({
              message: err.message
            })
            console.log(err.message)
          })
        }
      },
      closeRecharge () {
        this.$api.getUserInfo('', data => {
          this.$global.USER = data.resource
        }, err => {
          if (err.responseCode !== '401') {
            this.$toast({
              message: err.message
            })
          } else {
            this.$router.replace({name: 'login'})
          }
        })
      }
    }
  }
</script>

<style >
.recharge .mint-button--danger {
  background-color: #ef0000;
  width: 90%;
  margin-left: 5%;
  margin-top: 20px;
}

.mint-popup-3 {
  width: 100%;
  height: 100%;
  background-color: #fff;
}

.mint-popup-3 .mint-button {
  width: 90%;
  position: absolute;
  top: 260px;
  transform: translateY(-50%);
}

.title {
  display: block;
  line-height: 48px;
  font-size: 14px;
  margin-left: 10px;
  color: #999999;
}

.recharge-head {
  display: inline-block;
  background-color: #ef0000;
  width: 100%;
  padding-bottom: 5px;
}

img.recharge-head-headimg {
  width: 48px;
  height: auto;
  padding: 10px 10px 10px 10px;
  float: left;
}

.recharge-head h5 {
  font-size: 18px;
  color: #FFFFFF;
  font-weight: 400;
  margin-top: 10px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
}

.recharge-head p {
  color: #FFFFFF;
  font-size: 14px;
  margin-top: 5px;
}

.recharge-head p span {
  font-weight: 600;
  font-size: 18px;
}

.recharge-price {
  padding: 10px;
  background-color: #FFFFFF;
  border-bottom: 1px solid #f2f2f2;
  border-top: 1px solid #f2f2f2;
}

ul.recharge-price-grids {
  margin: 0;
  padding: 0;
  /*background-color: red;*/
  display: inline-block;
  width: 100%;
}

ul.recharge-price-grids li {
  width: 33%;
  float: left;
  text-align: center;
}

ul.recharge-price-grids li a {
  display: block;
  padding: 14.5px 0;
  margin: 5px;
  border: 1px solid #f2f2f2;
  border-radius: 8px;
  color: #999999;
  font-size: 14px;
  font-weight: 400;
}

ul.recharge-price-grids li.active a {
  background-color: #ef0000;
  color: #ffffff;
}

.mint-radio-input:checked+.mint-radio-core {
  background-color: #ef0000;
  border-color: #ef0000;
}

.payList {
  position: relative;
  margin-top: -10px;
}

.payList img {
  width: 36px;
  height: 36px;
  position: absolute;
  z-index: 2;
  top: 10px;
  left: 10px;
}

.payList b {
  font-weight: 400;
  font-size: 12px;
  color: #999999;
  position: absolute;
  z-index: 2;
  top: 33px;
  left: 55px;
}

.payList .mint-radio-label {
  position: absolute;
  z-index: 2;
  top: 13px;
  left: 50px;
  font-size: 14px;
}

.payList .mint-cell-wrapper {
  height: 56px;
}

.recharge-form-number {
  display: block;
  padding-top: 50px;
  text-align: center;
}

.recharge-form-number img {
  height: 60px;
  margin-bottom: 20px;
}

.recharge-form-number h3 {
  color: #333333;
  font-size: 18px;
  margin-bottom: 10px;
}
</style>
