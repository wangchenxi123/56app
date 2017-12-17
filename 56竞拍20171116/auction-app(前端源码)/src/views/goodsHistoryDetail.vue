<template>
  <div class="goodsDetail">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>商品详情</span>
      </div>
      <div class="page-content">
        <mt-loadmore :top-method="loadTop" ref="loadTop" >
        <!-- 产品轮播图 -->
        <goods-swipe :imgs="resource.itemBean.bigPictures" :history="true"></goods-swipe>
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
                  <span>{{biddersBeans[0].address}}</span>{{biddersBeans[0].name}}</h5>
                <p>以
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
                <router-link :to="{name: 'bidderRecord', query: {sealedId: $route.query.record_id}}" v-model="selected">查看出价记录</router-link>
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
        </mt-loadmore>
        <!-- 前往下一期 -->
        <div class="goods-pai-tab">
          <ul class="goods-pai-content">
            <router-link tag="li" :to="{name: 'goodsDetail', query: {id: resource.itemBean.id}}" style="width: 100%;color: #ffffff;background-color:red; line-height:35px;">
              前往下一期
            </router-link>
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
  name: 'goodsHistoryDetail',
  data () {
    return {
      selected: 'findShowOrders',
      number: 1,
      isActive: false,
      resource: {
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
        record_id: '',
        orderId: '',
        pageNo: 0,
        pageSize: 10
      },
      queryResource: {
        content: [],
        last: false
      },
      scrollTop: 0
    }
  },
  components: {
    goodsSwipe, payInfo, paiRule, orderList, dealList
  },
  watch: {
    selected: function (val, oldVal) {
      this.query.pageNo = 0
      this.queryResource = {
        content: [],
        last: false
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (['bidderRecord', 'showOrderDetail'].indexOf(from.name) === -1 || !vm.resource.itemBean.id) {
        document.body.scrollTop = 0
        Object.assign(vm.$data, vm.$options.data())
        vm.loading = false
        if (vm.$route.query.record_id) {
          vm.query.record_id = vm.$route.query.record_id
          vm.getNewDeal()
        } else {
          vm.$router.go(-1)
        }
      } else {
        document.body.scrollTop = vm.scrollTop
      }
    })
  },
  beforeRouteLeave (to, from, next) {
    this.scrollTop = document.body.scrollTop
    this.loading = true
    if (['bidderRecord'].indexOf(to.name) === -1) {
      this.$destroy()
    }
    next()
  },
  created () {
  },
  activated () {
    this.loading = false
  },
  deactivated () {
  },
  methods: {
    getNewDeal () {
      this.$api.getNewDeal(this.$route.query.record_id, data => {
        this.resource = data.resource.item
        this.query.itemId = data.resource.item.itemBean.id
        this.myBidderNum = data.resource.number
        this.biddersBeans = this.resource.biddersBeans.splice(0, 3)
        this.prices = [this.resource.price]
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
      }, err => {
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
        this.$toast({
          message: err.message
        })
      })
    },
    loadTop () {
      this.getNewDeal()
    },
    routerBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style>
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
  color: #333333;
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
  padding: 3px 3px;
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
  width: 60px;
}

table.goods-new-table tr:nth-child(2) td:nth-child(3),
table.goods-new-table tr:nth-child(3) td:nth-child(3) {
  width: 40px;
}

table.goods-new-table tr:nth-child(2) td:nth-child(5),
table.goods-new-table tr:nth-child(3) td:nth-child(5) {
  max-width: 90px;
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
  width: 20%;
  text-align: center;
  color: #333333;
  font-size: 12px;
}

ul.goods-pai-content li:nth-child(2) {
  width: 50%;
  text-align: center;
  display: inline-block;
  white-space: nowrap;
}

ul.goods-pai-content li:nth-child(2) input {
  width: 45%;
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
</style>
