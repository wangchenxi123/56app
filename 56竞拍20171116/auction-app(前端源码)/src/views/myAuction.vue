<template>
  <div class="myAuction">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>我的竞拍</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top:0px;" :style="{minHeight: minHeight + 'px'}">
        <mt-swipe v-if="showSwipe" :auto="0" :defaultIndex="defaultIndex" :show-indicators="false" :continuous="false" style="height: 3rem;">
          <mt-swipe-item>
            <mt-navbar v-model="query.selete_type" class="myAuction-navbar">
              <mt-tab-item id="ALL">全部</mt-tab-item>
              <mt-tab-item id="IS_IN_PROGRESS">正在拍</mt-tab-item>
              <mt-tab-item id="WINNING">我拍中</mt-tab-item>
              <mt-tab-item id="FAILURE">未拍中</mt-tab-item>
            </mt-navbar>
          </mt-swipe-item>
          <mt-swipe-item>
            <mt-navbar v-model="query.selete_type" class="myAuction-navbar">
              <mt-tab-item id="WAITING_PAYMENT" v-if="$global.showPay">待付款</mt-tab-item>
              <mt-tab-item id="WAITING_CHOICE_ADDRESS">待填收货地址</mt-tab-item>
              <mt-tab-item id="WAITING_RECEIPT">待签收</mt-tab-item>
              <mt-tab-item id="WAITING_SUN_ALONE">待晒单</mt-tab-item>
            </mt-navbar>
          </mt-swipe-item>
        </mt-swipe>
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
        <div class="goodsDetailPai-nav-container">
          <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a class="dealList-item">
                  <div @click="goto(item)">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <template v-if="item.sealType === 'IS_IN_PROGRESS'">
                      <mt-button type="danger" size="small" @click="$router.push({name: 'goodsDetail', query: {id: item.itemId}})">参与竞拍</mt-button>
                    </template>
                    <template v-else-if="item.sealType === 'FAILURE'">
                      <mt-button type="danger" size="small" plain @click="$router.push({name: 'goodsDetail', query: {id: item.itemId}})">前往下一期</mt-button>
                    </template>
                    <template v-else>
                      <mt-button type="primary" size="small" plain @click="$router.push({name: 'orderDetail', query: {orderId: item.order.id}})">查看详情</mt-button>
                    </template>
                  </div>
                </a>
                <div class="dealList-state">
                  <b>{{getStateMessage(index, item)}}</b>
                </div>
              </li>
            </ul>
        </div>
        </div>

        <template v-if="false">
        <mt-tab-container v-model="query.selete_type" class="goodsDetailPai-nav-container">
          <mt-tab-container-item id="ALL">
            <!-- 全部 -->
            <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a @click="goto(item)" class="dealList-item">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.itemBean.small_picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <mt-button type="danger" size="small" >参与竞拍</mt-button>
                  </div>
                </a>
                <div class="dealList-state">
                  <p>成交人:onolyy
                    <!-- <span>已返回拍币:0枚</span> -->
                  </p>
                </div>
              </li>
            </ul>
          </mt-tab-container-item>
          <mt-tab-container-item id="IS_IN_PROGRESS">
            <!-- 正在拍 -->
            <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a @click="goto(item)" class="dealList-item">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.itemBean.small_picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <mt-button type="danger" size="small" >参与竞拍</mt-button>
                  </div>
                </a>
                <div class="dealList-state">
                  <p>
                    <b>进行中...</b>
                    <!-- <span>已返回拍币:0枚</span> -->
                  </p>
                </div>
              </li>
            </ul>
          </mt-tab-container-item>
          <mt-tab-container-item id="WINNING">
            <!-- 我拍中 -->
            <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a @click="goto(item)" class="dealList-item">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.itemBean.small_picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <mt-button type="danger" size="small" >参与竞拍</mt-button>
                  </div>
                </a>
                <div class="dealList-state">
                  <p>成交人:
                    <!-- <span>已返回拍币:0枚</span> -->
                  </p>
                </div>
              </li>
            </ul>
          </mt-tab-container-item>
          <mt-tab-container-item id="NOT_WINNING">
            <!-- 未拍中 -->
            <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a @click="goto(item)" class="dealList-item">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.itemBean.small_picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <mt-button type="danger" size="small" >前往下一期</mt-button>
                  </div>
                </a>
                <div class="dealList-state">
                  <p>成交人:
                    <!-- <span>已返回拍币:0枚</span> -->
                  </p>
                </div>
              </li>
            </ul>
          </mt-tab-container-item>
          <mt-tab-container-item id="WAITING_PAYMENT">
            <!-- 待付款 -->
            <ul class="newDeal-list">
              <li v-for="(item, index) in resource.content" :key="item.id">
                <a @click="goto(item)" class="dealList-item">
                  <div class="dealList-time">{{item.time}}</div>
                  <img class="dealList-img" :src="$api.file.see(item.itemBean.small_picture)">
                  <div class="dealList-title">
                    <h5>{{item.itemBean.name}}</h5>
                    <p>市场价:￥{{item.itemBean.price}}</p>
                    <p>竞拍价:
                      <span>￥{{item.price}}</span>
                    </p>
                  </div>
                  <div class="dealList-data">
                    <b>{{getPercent(index, item)}}%</b>
                    <span>节省
                      <i class="icon-down"></i>
                    </span>
                    <mt-button type="danger" size="small" >去付款</mt-button>
                  </div>
                </a>
                <div class="dealList-state">
                  <p>成交人:
                    <!-- <span>已返回拍币:0枚</span> -->
                  </p>
                </div>
              </li>
            </ul>
          </mt-tab-container-item>
        </mt-tab-container>
        </template>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
import globalBus from '../globaBus'
import dealList from 'components/dealList'
export default {
  name: 'myAuction',
  data () {
    return {
      minHeight: '',
      defaultIndex: 0,
      showSwipe: false,
      query: {
        selete_type: 'ALL',
        pageNo: 0,
        pageSize: 10
      },
      resource: {
        content: [],
        last: false
      },
      loading: false,
      loadType: 'REFRESH'
    }
  },
  watch: {
    'query.selete_type': function (val, oldVal) {
      this.loadTop()
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (['goodsDetail', 'goodsHistoryDetail'].indexOf(from.name) === -1) {
        vm.$set(vm.query, 'selete_type', to.query.type)
        if (['ALL', 'IS_IN_PROGRESS', 'WINNING', 'FAILURE'].indexOf(to.query.type) === -1) {
          vm.defaultIndex = 1
        } else {
          vm.defaultIndex = 0
        }
        vm.showSwipe = true
        vm.query.pageNo = 0
        vm.resource = {
          content: [],
          last: false
        }
        // vm.loadTop()
      }
    })
  },
  beforeRouteLeave (to, from, next) {
    if (['goodsDetail', 'goodsHistoryDetail'].indexOf(to.name) === -1) {
      this.showSwipe = false
    }
    next()
  },
  activated () {
    this.loading = false
    this.minHeight = window.innerHeight - 103
  },
  deactivated () {
    this.loading = true
  },
  created () {
    this.showPayPage()
  },
  components: {
    dealList
  },
  computed: {
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    loadTop () {
      this.resource.content = []
      this.query.pageNo = 1
      this.loadType = 'REFRESH'
      this.findAuctionRecords()
      this.showPayPage()
    },
    findAuctionRecords () {
      this.loading = true
      this.$api.findAuctionRecords(this.query, data => {
        if (this.loadType === 'LOAD_MORE') {
          data.resource.content = this.resource.content.concat(data.resource.content)
        }
        this.resource = data.resource
        this.loading = false
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
      }, err => {
        this.loading = false
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
        this.$toast({
          message: err.message
        })
      })
    },
    loadMore () {
      if (!this.resource.last && !this.loading) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findAuctionRecords()
      }
    },
    getPercent (index, row) {
      return ((1 - (parseFloat(row.price) / parseFloat(row.itemBean.price))) * 100).toFixed(2)
    },
    goto (row) {
      if (row.sealType === 'IS_IN_PROGRESS') {
        this.$router.push({name: 'goodsDetail', query: {id: row.itemBean.id}})
      } else {
        this.$router.push({name: 'goodsHistoryDetail', query: {record_id: row.id}})
      }
    },
    getStateMessage (index, row) {
      let message = ''
      switch (row.sealType) {
        case 'ALL':
          break
        case 'WINNING':
          break
        case 'FAILURE':
          message = `成交人：${row.username}`
          break
        case 'IS_IN_PROGRESS':
          message = `进行中...`
          break
        case 'WAITING_PAYMENT':
          message = `待付款`
          break
        case 'WAITING_CHOICE_ADDRESS':
          message = '待选择收货地址'
          break
        case 'WAITING_SHIP':
          message = '待发货'
          break
        case 'WAITING_RECEIPT':
          message = '待收货'
          break
        case 'WAITING_SUN_ALONE':
          message = '待晒单'
          break
        case 'SEALED':
          message = '完成'
          break
      }
      return message
    },
    showPayPage () {
      this.$api.showPayPage(null, data => {
        this.$set(this.$global, 'showPay', data.resource)
      }, err => {
        this.$toast({
          message: err.message
        })
      })
    }
  }
}
</script>

<style>
.home .myAuction-navbar {
  margin-top: 10px;
  border-top: 1px solid #f2f2f2;
  position: fixed;
}

.home .myAuction-navbar-container {
  margin-top: 3px;
}

.mint-navbar .mint-tab-item.is-selected {
  border-bottom: 2px solid #ef0000;
  color: #ef0000;
}

.home .mint-tab-item-label {
  font-size: 14px;
}

.home .mint-navbar .mint-tab-item {
  padding: 13px 0;
}

.mint-button--small {
  font-size: 12px;
  padding: 0px 5px !important;
  height: 28px;
  margin: 0;
}

.mint-button--danger {
  background-color: #ef0000;
  margin: 0;
}

.icon-down {
  display: inline-block;
  background-image: url('../../static/icon/icon-down.svg');
  background-position: 0;
  background-size: 8px 8px;
  width: 8px;
  height: 8px;
  margin-left: 5px;
}

.goodsDetailPai-nav-container {
  margin-top: 3px;
}

ul.newDeal-list {
  margin: 0;
  padding: 0;
}

ul.newDeal-list li {
  width: 100%;
  display: block;
  border-bottom: 1px solid #f2f2f2;
  padding: 0;
  background-color: #FFFFFF;
}

ul.newDeal-list li a.dealList-item {
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  background-color: #ffffff;
  transition: 0.4s;
  padding-bottom: 5px;
  display: inline-block;
  width: 100%;
}

ul.newDeal-list li a.dealList-item:before {
  background-color: #FFFFFF;
  transition: 0.4s;
}

ul.newDeal-list li a.dealList-item:active {
  background-color: #f2f2f2;
  transition: 0.2s;
  border-radius: 50%;
}

ul.newDeal-list li a .dealList-time {
  color: #999999;
  font-size: 14px;
  padding-left: 10px;
  padding-top: 10px;
}

ul.newDeal-list li a img.dealList-img {
  float: left;
  width: 30%;
}

ul.newDeal-list li a .dealList-title {
  float: left;
  width: 50%;
}

ul.newDeal-list li a .dealList-title p {
  font-size: 14px;
  margin: 3px 0;
  padding-left: 10px;
  color: #333333;
}

ul.newDeal-list li a .dealList-title h5 {
  font-size: 14px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  margin: 10px 0;
  font-weight: 600;
  padding-left: 10px;
  color: #333333;
}

ul.newDeal-list li a .dealList-title p span {
  color: #EF0000;
}

ul.newDeal-list li a .dealList-data {
  float: right;
  width: 20%;
  margin-top: 5px;
  display: block;
}

ul.newDeal-list li a .dealList-data b {
  display: block;
  color: #333333;
}

ul.newDeal-list li a .dealList-data span {
  display: inline-block;
  font-size: 12px;
  margin-bottom: 5px;
  color: #999999;
}

ul.newDeal-list li a .dealList-data button {
  display: block;
}

ul.newDeal-list li .dealList-state {
  background-color: #f2f2f2;
  display: block;
  padding: 10px;
  font-size: 12px;
}

ul.newDeal-list li .dealList-state p b {
  color: #ef0000;
  font-weight: 400;
}

ul.newDeal-list li .dealList-state p span {
  float: right;
  color: #ef0000;
  font-weight: 400;
}
</style>
