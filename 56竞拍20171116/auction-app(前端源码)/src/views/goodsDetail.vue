<template>
  <div class="goodsDetail">
    <div class="page-wrap">
      <div class="page-title-white">
        <a @click="routerBack">
          <i class="icon-back-gray"></i>
        </a>
        <!-- <span>商品详情</span> -->
      </div>
      <div class="page-content">
        <div @touchstart="touchStart" @touchmove="touchStart">
          <mt-loadmore :top-method="loadTop" ref="loadTop">
            <!-- 产品轮播图 -->
            <goods-swipe :imgs="resource.itemBean.bigPictures" :isFinish="resource.settlement"></goods-swipe>
            <div class="goods-price">
              <p>
                ￥
                <transition-group tag="a" name="list-complete">
                  <i class="list-complete-item" v-for="p in prices" :key="p">{{p}}</i>
                </transition-group>
                <span>成交价</span>
                <b>市场价:
                  <s>￥{{resource.itemBean.price}}</s>
                </b>
              </p>
            </div>
            <div class="goods-title">
              <p>
                <span>热拍</span>
                <b>{{resource.itemBean.name}}</b>
              </p>
              <h3 ref="countDown"></h3>
            </div>
            <div class="goods-new" v-if="biddersBeans.length > 0">
              <div class="goods-newpay">
                <img src="static/img/img-newpay.png" alt="">
              </div>
              <table class="goods-new-table">
                <tr :class="{'slideAcitve': newBiddersShow}">
                  <td>
                    <img :src="$api.file.see(biddersBeans[0].avatar)" onerror="this.src='static/icon/icon-appear.svg'" alt="求头像">
                  </td>
                  <td colspan="4">
                    <h5>
                      <span>{{biddersBeans[0].address}}</span>{{biddersBeans[0].name}}<span style="background-color: red;margin-left: 5px;">领先</span></span></h5>
                    <p>{{resource.settlement?'':'若无人出价,将'}}以
                      <span>￥{{biddersBeans[0].price}}</span>拍得本商品</p>
                  </td>
                </tr>
                <tr v-if="index !== 0 && index <= 2" v-for="(i, index) in biddersBeans" :key="i.price">
                  <td>
                    <img :src="$api.file.see(i.avatar)" onerror="this.src='static/icon/icon-appear.svg'" alt="求头像">
                  </td>
                  <td>{{i.name}}</td>
                  <td>出局</td>
                  <td>{{i.address}}</td>
                  <td>￥{{i.price}}</td>
                </tr>
                <tr v-show="biddersBeans.length > 2">
                  <td colspan="5">
                    <router-link :to="{name: 'bidderRecord', query: {id: resource.itemBean.id}}" v-model="selected">查看出价记录</router-link>
                  </td>
                </tr>
              </table>
            </div>
            <div class="goods-view-list">
              <mt-cell title="我已消耗的拍币/赠币" icon="more">
                <span class="text-danger-large">{{myBidderNum}}枚</span>
                <img slot="icon" src="static/icon/icon-money.svg">
              </mt-cell>
            </div>

            <!-- 竞拍信息 -->
            <pay-info :itemInfo="resource"></pay-info>
            <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
              <!-- 商品列表-->
              <mt-navbar v-model="selected" class="goodsDetailPai-nav">
                <mt-tab-item id="findShowOrders">晒单分享</mt-tab-item>
                <mt-tab-item id="findDealHistorys">往期成交</mt-tab-item>
                <mt-tab-item id="3">竞拍规则</mt-tab-item>
              </mt-navbar>
              <mt-tab-container v-model="selected" class="goodsDetailPai-nav-container">
                <mt-tab-container-item id="findShowOrders">
                  <!-- 晒单分享-->
                  <order-list v-if="selected === 'findShowOrders'" :content="queryResource.content"></order-list>
                </mt-tab-container-item>
                <mt-tab-container-item id="findDealHistorys">
                  <!-- 往期成交-->
                  <deal-List v-if="selected === 'findDealHistorys'" :content="queryResource.content" :showDealed="false"></deal-List>
                </mt-tab-container-item>
                <mt-tab-container-item id="3">
                  <!-- 竞拍规则-->
                  <pai-rule :itemInfo="resource"></pai-rule>
                </mt-tab-container-item>
              </mt-tab-container>
            </div>
          </mt-loadmore>
        </div>
        <!-- 底部购买信息栏 -->
        <div class="goods-pai-tab">
          <ul class="goods-pai-content" style="position: relative">
            <template v-if="!resource.settlement">
              <li>
                <a @click="isCollect">
                  <i class="icon-collectionGroup icon-collection" :class="{active: isActive}"></i>
                  <p>收藏</p>
                </a>
              </li>
              <template v-if="auction === 0">
                <li>
                  <span @click="low">-</span>
                  <input ref="numInput" type="number" @focus="popupVisible = true" @blur="inputBlur" v-model="number" min="1">
                  <b @click="add">+</b>
                </li>
                <li>
                  <a @click="bidder">
                    <h5>出价</h5>
                    <p>{{resource.plus_code}}拍币/次</p>
                  </a>
                </li>
                <mt-popup v-model="popupVisible" :modal="false" style="width: 100%;position: absolute ;top: -35px;border-top: .5px solid rgb(246, 222, 222);border-bottom: .5px solid rgb(246, 222, 222)">
                  <div style="text-align:center;">选择自动出价次数</div>
                  <div style="text-align:center;">
                    <mt-button size="small" style="width: 20%; margin: 10px 1%;" :class="{selActive: number == 28}" @click="selNumber(28)">28</mt-button>
                    <mt-button size="small" style="width: 20%; margin: 10px 1%;" :class="{selActive: number == 50}" @click="selNumber(50)">50</mt-button>
                    <mt-button size="small" style="width: 20%; margin: 10px 1%;" :class="{selActive: number == 88}" @click="selNumber(88)">88</mt-button>
                    <mt-button size="small" style="width: 20%; margin: 10px 1%;" :class="{selActive: number == 188}" @click="selNumber(188)">188</mt-button>
                  </div>
                </mt-popup>
              </template>
              <template v-else>
                <li style="width: 80%;color: #ffffff;background-color: red; line-height:35px; font-size: 14px;">
                  剩余 {{auction}} 次出价
                </li>
              </template>
            </template>
            <template v-else>
              <li @click="toNext()" style="width: 100%;color: #ffffff;background-color:red; line-height:35px;">
                前往下一期
              </li>
            </template>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import goodsSwipe from 'components/goodsSwipe'
  import payInfo from 'components/payInfo'
  import paiRule from 'components/paiRule'
  import orderList from 'components/orderList'
  import dealList from 'components/dealList'
  export default {
    name: 'goodsDetail',
    data () {
      return {
        selected: 'findShowOrders',
        number: 1,
        isActive: false,
        auction: 0,
        resource: {
          increase_the_price: 0,
          itemBean: {
            bigPictures: []
          }
        },
        prices: [],
        systemTime: '',
        countDown: '',
        timer: '',
        biddersBeans: [],
        newBiddersShow: false,
        myBidderNum: 0,
        query: {
          userId: '',
          itemId: '',
          orderId: '',
          pageNo: 0,
          pageSize: 10
        },
        queryResource: {
          content: [],
          last: false
        },
        loading: false,
        loadType: 'REFRESH',
        scrollTop: 0,
        Bidderring: false,
        popupVisible: false
      }
    },
    components: {
      goodsSwipe, payInfo, paiRule, orderList, dealList
    },
    watch: {
      '$global.WSMessage': function (val, oldVal) {
        if (val.itemId === this.resource.itemBean.id) {
          if (val.message_type === 'BIDDER') {
            if (!this.resource.settlement && !this.resource.interrupted) {
              this.$set(this.resource, 'price', val.price)
              this.prices.push(val.price)
              this.prices.splice(0, 1)
              this.countDown = 10000
              this.setTimer(val)
              let newBiddersBeans = {
                address: val.address,
                avatar: val.head_pic,
                itemId: val.itemId,
                name: val.username,
                price: val.price,
                time: val.time,
                userId: val.userId
              }
              this.newBiddersShow = true
              this.biddersBeans.unshift(newBiddersBeans)
              if (this.biddersBeans.length > 3) {
                this.biddersBeans.splice(-1, 1)
              }
              window.setTimeout(() => {
                this.newBiddersShow = false
              }, 700)
            }
          } else if (val.message_type === 'HEADLINES' && val.userId) {
            this.$set(this.resource, 'settlement', true)
            window.clearInterval(this.timer)
            this.countDown = 0
            this.setTimer(val)
          } else if (val.message_type === 'BID_SUCCESS_NOTICE') {
            if (this.auction >= 1) {
              this.myBidderNum = parseInt(this.myBidderNum) + (1 * parseInt(this.resource.plus_code))
              this.auction--
            }
          } else if (val.message_type === 'BID_ERROR_NOTICE') {
            this.auction = 0
          } else if (val.message_type === 'SYSTEM_SLEEP') {
            this.$set(this.resource, 'interrupted', true)
            val.interrupted = true
            this.setTimer(val)
          } else {
            if (!this.resource.settlement && !this.resource.interrupted) {
              this.countDown = 10000
              this.setTimer(val)
            }
          }
        }
      },
      selected: function (val, oldVal) {
        this.query.pageNo = 0
        this.queryResource = {
          content: [],
          last: false
        }
        this.loading = false
        this.query.itemId = this.$route.query.id
        this.loadMore()
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        document.body.scrollTop = vm.scrollTop
        if (['bidderRecord', 'goodsHistoryDetail', 'showOrderDetail'].indexOf(from.name) === -1 || from.query.newDeal) {
          document.body.scrollTop = 0
          vm.number = 1
          vm.selected = vm.$options.data().selected
          vm.query.pageNo = 0
          vm.queryResource = vm.$options.data().queryResource
        }
      })
    },
    beforeRouteLeave (to, from, next) {
      this.scrollTop = document.body.scrollTop
      this.loading = true
      next()
    },
    created () {
    },
    activated () {
      window.clearInterval(this.timer)
      this.loading = false
      if (this.$route.query.id) {
        this.query.itemId = this.$route.query.id
        this.getItem()
      } else {
        this.$router.go(-1)
      }
    },
    deactivated () {
      this.loading = true
      window.clearInterval(this.timer)
    },
    methods: {
      toNext () {
        this.loadTop()
        this.query.pageNo = 0
        this.loading = false
        this.loadType = 'REFRESH'
        this.selected = 'findShowOrders'
        this.queryResource = {
          content: [],
          last: false
        }
      },
      getItem () {
        this.$api.getItem(this.$route.query.id, data => {
          this.resource = data.resource.resource
          this.systemTime = data.resource.time
          this.myBidderNum = data.resource.number
          this.auction = data.resource.auction
          this.biddersBeans = this.resource.biddersBeans.splice(0, 3)
          this.prices = [this.resource.price]
          this.countDown = 10000 - (parseInt(this.systemTime) - parseInt(this.resource.instant_time))
          this.$nextTick(() => {
            this.setTimer(this.resource)
          })
          this.$refs.loadTop.onTopLoaded()
        }, err => {
          this.$refs.loadTop.onTopLoaded()
          this.$toast({
            message: err.message,
            duration: 3000
          })
        })
      },
      loadTop () {
        this.loadType = 'REFRESH'
        this.getItem()
      },
      setTimer (temp) {
        window.clearInterval(this.timer)
        let time = this.$refs['countDown']
        let _this = this
        if (temp.interrupted) {
          time.innerText = '休眠中...'
          return
        }
        if (temp.settlement) {
          time.innerText = '00:00:00'
        } else {
          time.innerHTML = _this.formData(parseInt(_this.countDown))
          this.timer = window.setInterval(function () {
            _this.countDown = _this.countDown - 1000
            time.innerHTML = _this.formData(parseInt(_this.countDown))
            if (_this.countDown < 1000) {
              window.clearInterval(_this.timer)
            }
          }, 1000)
        }
      },
      formData (t) {
        let text = '00:00:00'
        if (t >= 1000 && t < 10000) {
          text = '00:00:0' + t
        } else if (t >= 10000 && t < 100000) {
          text = '00:00:' + t
        }
        return text.slice(0, 8)
      },
      routerBack () {
        this.$router.go(-1)
      },
      add () {
        if (this.popupVisible) {
          this.$refs.numInput.focus()
        }
        this.number++
      },
      low () {
        if (this.popupVisible) {
          this.$refs.numInput.focus()
        }
        if (this.number > 1) {
          this.number--
        }
      },
      isCollect () {
        if (this.popupVisible) {
          this.$set(this, 'popupVisible', false)
        }
        this.$api.addCollection(this.query.itemId, data => {
          this.$toast({
            message: '收藏成功',
            duration: 3000
          })
        }, err => {
          this.$toast({
            message: err.message,
            duration: 3000
          })
        })
      },
      bidder () {
        if (this.popupVisible) {
          this.$set(this, 'popupVisible', false)
        }
        if (!this.$global.USER.id) {
          this.$router.push({name: 'login'})
        }
        if (this.Bidderring) {
          return
        }
        let sendData = {
          itemId: this.resource.itemBean.id,
          periods: this.resource.number_periods,
          number: this.number
        }
        this.Bidderring = true
        this.$api.auction(sendData, data => {
          if (sendData.number > 1) {
            this.auction = sendData.number
          }
          if (sendData.number === 1) {
            this.myBidderNum = parseInt(this.myBidderNum) + (1 * parseInt(this.resource.plus_code))
          }
          this.Bidderring = false
        }, err => {
          this.Bidderring = false
          this.$toast({
            message: err.message,
            position: 'middle',
            duration: 3000
          })
          if (err.responseCode === '410') {
            this.$router.push({name: 'recharge'})
          }
        })
      },
      findShowOrders () {
        this.loading = true
        this.$api.findShowOrders(this.query, data => {
          if (this.selected === 'findShowOrders') {
            if (this.loadType === 'LOAD_MORE') {
              data.resource.content = this.queryResource.content.concat(data.resource.content)
            }
            this.queryResource = data.resource
          }
          this.loading = false
        }, err => {
          this.loading = false
          this.$toast({
            message: err.message,
            position: 'middle',
            duration: 3000
          })
        })
      },
      findDealHistorys () {
        this.loading = true
        this.$api.findDealHistorysByItemId(this.query, data => {
          if (this.selected === 'findDealHistorys') {
            if (this.loadType === 'LOAD_MORE') {
              data.resource.content = this.queryResource.content.concat(data.resource.content)
            }
            this.queryResource = data.resource
          }
          this.loading = false
        }, err => {
          this.loading = false
          this.$toast({
            message: err.message,
            position: 'middle',
            duration: 3000
          })
        })
      },
      loadMore () {
        if (!this.loading && !this.queryResource.last && this.hasOwnProperty(this.selected)) {
          this.query.pageNo++
          this.loadType = 'LOAD_MORE'
          this[this.selected]()
        }
      },
      selNumber (num) {
        this.$refs.numInput.focus()
        this.number = num
      },
      inputBlur () {
        if (this.number < 1) {
          this.number = 1
        }
      },
      touchStart () {
        if (this.popupVisible) {
          this.$set(this, 'popupVisible', false)
          this.$refs.numInput.blur()
        }
      }
    }
  }
</script>

<style>
  .page-title-white{
    height:48px;
    line-height: 48px;
    text-align: center;
    color:#000000;
    background-color:#ffffff;
    font-size: 18px;
    font-weight: 700;
    position: fixed;
    width: 100%;
    z-index: 4;
  }
  .page-title-white span{
    margin-left: -48px;
  }
  .page-title-white a{
    background-color:#ffffff;
    padding-left:15px;
    padding-right:15px;
    float: left;
    transition: 0.2s;
    border-radius: 50%;
  }
  .page-title-white a.icon-right {
    text-align: center;
    float: right;
    transition: 0.4s;
    border-radius: 50%;
  }
  .page-title-white a:before{
    background-color:#ffffff;
    transition: 0.4s;
  }
  .page-title-white a:active{
    background-color: #bbbbbb;
    transition: 0.2s;
    border-radius: 0px;
    border-radius: 0;
  }
  button,
  input,
  a {
    text-decoration: none;
    outline: none;
  }

  .goods-price {
    background-color: #ef0000;
    height: 48px;
    color: #FFFFFF;
    line-height: 48px;
    padding: 0 10px;
    display: block;
    font-size: 18px;
  }

  .goods-price p b {
    float: right;
    font-weight: 400;
    font-size: 12px;
  }

  .goods-price p span {
    background-color: #FFFFFF;
    color: #ef0000;
    font-size: 12px;
    margin-left: 8px;
    padding: 3px 5px;
  }
  .goods-title {
    padding: 5px 10px;
    background-color: #ffffff;
    border-bottom: 1px solid #f2f2f2;
  }

  .goods-title p {
    font-size: 14px;
    line-height: 24px;
    margin-bottom: 10px;
    color: #333333;
  }

  .goods-title p span {
    color: #ffffff;
    background-color: #33D333;
    padding: 3px 5px;
    font-size: 12px;
  }

  .goods-title h3 {
    text-align: center;
    font-size: 32px;
    color: #ef0000;
    margin-bottom: 10px;
  }

  .goods-new {
    display: block;
    margin: 10px;
    background-color: #ffffff;
    box-shadow: 1px 2px 10px #eeeeee;
  }

  table.goods-new-table {
    margin: 0;
    padding: 0;
    font-size: 14px;
    display: block;
    width: 100%;
    vertical-align: middle;
  }

  table.goods-new-table tr {
    border-bottom: 1px solid #f2f2f2;
    white-space: nowrap;
    overflow: hidden;
    display: block;
    line-height: 44px;
    width: 100%;
  }

  table.goods-new-table tr td {
    white-space: nowrap;
    word-wrap: normal;
    text-overflow: ellipsis;
    overflow: hidden;
    display: inline-block;
  }

  table.goods-new-table tr:nth-child(1) td:first-child {
    width: 16%;
    min-width: 44px;
    max-width: 47px;
    padding: 10px 0;
  }

  table.goods-new-table tr:nth-child(1) td:first-child img {
    width: 44px;
    height: 44px;
    vertical-align: middle;
  }

  table.goods-new-table tr:nth-child(1) td h5,
  table.goods-new-table tr:nth-child(1) td p {
    max-width: 250px;
    width: 100%;
    white-space: nowrap;
    word-wrap: normal;
    text-overflow: ellipsis;
    overflow: hidden;
    margin: 0;
    padding: 0;
  }

  table.goods-new-table tr:nth-child(1) td h5 {
    font-size: 14px;
    font-weight: 400;
    line-height: 24px;
  }

  table.goods-new-table tr:nth-child(1) td h5 span {
    font-size: 12px;
    padding: 3px 5px;
    color: #ffffff;
    background-color: #cccccc;
    margin-right: 5px;
  }

  table.goods-new-table tr:nth-child(1) td p span {
    color: #ef0000;
  }

  table.goods-new-table tr:nth-child(1) td p {
    margin: 0;
    padding: 0;
    color: #999999;
    font-size: 14px;
    line-height: 24px;
  }

  table.goods-new-table tr:nth-child(2) td:nth-child(1),
  table.goods-new-table tr:nth-child(3) td:nth-child(1) {
    width: 36px;
  }

  table.goods-new-table tr:nth-child(2) td:nth-child(2),
  table.goods-new-table tr:nth-child(3) td:nth-child(2),
  table.goods-new-table tr:nth-child(2) td:nth-child(4),
  table.goods-new-table tr:nth-child(3) td:nth-child(4) {
    max-width: 60px;
    width:25%;
  }

  table.goods-new-table tr:nth-child(2) td:nth-child(3),
  table.goods-new-table tr:nth-child(3) td:nth-child(3) {
    width: 40px;
  }

  table.goods-new-table tr:nth-child(2) td:nth-child(5),
  table.goods-new-table tr:nth-child(3) td:nth-child(5) {
    width: 20%;
    max-width: 90px;
    text-align: center
  }

  table.goods-new-table tr:nth-child(4) {
    text-align: center;
  }

  table.goods-new-table tr:nth-child(4) td a {
    color: #999999;
    display: block;
    padding: 0 50px;
    background-color: #ffffff;
    transition: 0.4s;
    border-radius: 0;
  }

  table.goods-new-table tr:nth-child(4) td a:before {
    background-color: #ffffff;
    transition: 0.4s;
    border-radius: 0;
  }

  table.goods-new-table tr:nth-child(4) td a:active {
    background-color: #eeeeee;
    transition: 0.2s;
    border-radius: 50%;
  }

  table.goods-new-table tr td img {
    width: 32px;
    height: 32px;
    border-radius:50%;
    vertical-align: middle;
  }

  table.goods-new-table tr:nth-child(2),
  table.goods-new-table tr:nth-child(3) {
    color: #999999;
    padding: 0;
    margin: 0;
  }


  .goods-newpay {
    text-align: center;
  }

  .goods-newpay img {
    width: 50%;
    margin: 0 auto;
  }

  .goods-view-list img {
    width: 24px;
    height: 24px;
  }

  .text-danger-large {
    color: #ef0000;
    font-size: 16px;
  }

  .goodsDetail .goodsDetailPai-nav {
    margin-top: 10px;
    border-top: 1px solid #f2f2f2;
  }

  .goodsDetail .goodsDetailPai-nav-container {
    margin-top: 3px;
  }

  .mint-navbar .mint-tab-item.is-selected {
    border-bottom: 2px solid #ef0000;
    color: #ef0000;
  }

  .goodsDetail .mint-tab-item-label {
    font-size: 14px;
  }

  .goodsDetail .mint-navbar .mint-tab-item {
    padding: 13px 0;
  }

  .goods-pai-tab {
    height: 51px;
    width: 100%;
    display: block;
    background-color: #fbfbfb;
    border-top: 1px solid #eeeeee;
    position: fixed;
    z-index: 4;
    bottom: 0;
  }

  ul.goods-pai-content {
    margin: 0;
    padding: 0;
    display: inline-block;
    width: 100%;
    height: 100%;
  }

  ul.goods-pai-content li {
    padding: 0;
    margin: 0;
    height: 100%;
    display: inline-block;
    vertical-align: middle;
    padding-top: 7px;
  }

  ul.goods-pai-content li:nth-child(1) {
    float: left;
    width: 15%;
    text-align: center;
    color: #333333;
    font-size: 12px;
  }

  ul.goods-pai-content li:nth-child(2) {
    width: 55%;
    text-align: center;
    display: inline-block;
    white-space: nowrap;
  }

  ul.goods-pai-content li:nth-child(2) input {
    width: 40%;
    border: 1px solid #999999;
    height: 35px;
    color: #333333;
    text-indent: 0.5em;
    font-weight: 400;
  }

  ul.goods-pai-content li:nth-child(2) span,
  ul.goods-pai-content li:nth-child(2) b {
    display: inline-block;
    border: 1px solid #999999;
    height: 36px;
    line-height: 35px;
    width: 36px;
    font-size: 20px;
    color: #333333;
    vertical-align: middle;
    text-align: center;
    background-color: #fbfbfb;
    transition: 0.4s;
    font-weight: 400;
  }

  ul.goods-pai-content li:nth-child(2) span:before,
  ul.goods-pai-content li:nth-child(2) b:before {
    background-color: #fbfbfb;
    transition: 0.4s;
  }

  ul.goods-pai-content li:nth-child(2) span:active,
  ul.goods-pai-content li:nth-child(2) b:active {
    background-color: #eeeeee;
    transition: 0.2s;
  }

  ul.goods-pai-content li:nth-child(2) span {
    border-top-left-radius: 15px;
    border-bottom-left-radius: 15px;
  }

  ul.goods-pai-content li:nth-child(2) b {
    border-top-right-radius: 15px;
    border-bottom-right-radius: 15px;
  }

  ul.goods-pai-content li:nth-child(3) {
    float: right;
    width: 30%;
    text-align: center;
    color: #ffffff;
    font-size: 12px;
    background-color: #ef0000;
    transition: 0.4s;
  }

  ul.goods-pai-content li:nth-child(3):before {
    background-color: #ef0000;
    transition: 0.4s;
  }

  ul.goods-pai-content li:nth-child(3):active {
    background-color: #c90d0d;
    transition: 0.2s;
    border-radius: 0px;
    border-radius: 0;
  }

  ul.goods-pai-content li:nth-child(3) a p {
    font-size: 12px;
  }

  ul.goods-pai-content li:nth-child(3) a h5 {
    font-size: 14px;
    margin-top: 3px;
  }

  ul.goods-pai-content li:nth-child(1) img {
    width: 20px;
    height: 20px;
  }

  .icon-collectionGroup {
    display: inline-block;
    background-image: url("../../static/icon/icon-collection-group.svg");
    background-position: 0;
    background-repeat: no-repeat;
    background-size: 40px;
    width: 20px;
    height: 20px;
  }

  .icon-collection {}

  .icon-collection.active {
    background-position: -21px;
  }

  .list-complete-item {
    transition: all .8s;
    display: inline-block;
  }

  .list-complete-enter
    /* .list-complete-leave-active for below version 2.1.8 */ {
    transform: translateY(20px);
  }
  .list-complete-leave-to
    /* .list-complete-leave-active for below version 2.1.8 */ {
    transform: translateY(-20px);
  }
  .list-complete-leave-active {
    position: absolute;
  }
  .slideAcitve {
    animation: slide-in .5s
  }
  @keyframes slide-in {
    0% {
      transform: translateX(50%);
    }
    100% {
      transform: translateX(0);
    }
  }

  .selActive {
    background-color: red;
    color: #ffffff;
  }
</style>
