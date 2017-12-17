<template>
  <div class="home">
    <div class="page-wrap">
      <div class="page-title">
        <span>{{$const.TITLE}}</span>
        <router-link :to="{name: 'message'}" class="icon-right">
          <i class="icon-message">
            <i class="circle-red" v-if="unreadNumber > 0"></i>
          </i>
        </router-link>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top: 0px;" :style="{minHeight: minHeight + 'px'}">
          <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" infinite-scroll-distance="10">
            <!-- 轮播图-->
            <auction-swipe ref="swipe"></auction-swipe>
            <!-- 左右滑动活动标签-->
            <auction-activity ref="activity"></auction-activity>
            <!-- 56头条-->
            <auction-headline></auction-headline>
            <!-- 商品列表-->
            <mt-navbar v-model="selected" class="home-navbar">
              <mt-tab-item id="HOT">正在热拍</mt-tab-item>
              <mt-tab-item id="NOVICE">新手推荐</mt-tab-item>
              <mt-tab-item id="IS_IN_PROGRESS">我在拍</mt-tab-item>
            </mt-navbar>
            <!-- 产品列表 -->
            <product-grids :resource="resource" :loadType="loadType"></product-grids>
          </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
import auctionSwipe from 'components/swipe'
import auctionActivity from 'components/activity'
import auctionHeadline from 'components/headline'
import productGrids from 'components/productGrids'
var scrollTop = 0
var list = ['goodsDetail']
export default {
  name: 'home',
  data () {
    return {
      minHeight: '',
      unreadNumber: 0,
      selected: 'HOT',
      loadType: 'REFRESH',
      loading: false,
      query: {
        pageNo: 0,
        pageSize: 10
      },
      resource: {
        content: [],
        totalPages: 1,
        last: false
      }
    }
  },
  computed: {
  },
  watch: {
    selected: function (val, oldVal) {
      if (val === 'IS_IN_PROGRESS') {
        if (this.$global.USER.id) {
          this.query.pageNo = 1
          this.loadType = 'REFRESH'
          this.resource = {
            content: [],
            totalPages: 1,
            last: false
          }
          this.findItems()
        } else {
          this.selected = oldVal
          this.$router.push('/login')
        }
      } else {
        this.query.pageNo = 1
        this.loadType = 'REFRESH'
        this.findItems()
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    if (list.indexOf(from.name) === -1) {
      document.body.scrollTop = 0
      scrollTop = 0
    } else {
      document.body.scrollTop = scrollTop
    }
    next()
  },
  beforeRouteLeave (to, from, next) {
    scrollTop = document.body.scrollTop
    next()
  },
  activated () {
    this.minHeight = window.innerHeight - 103
    this.loading = false
    this.findUnreadNumber()
  },
  deactivated () {
    this.loading = true
  },
  components: {
    auctionSwipe, auctionActivity, auctionHeadline, productGrids
  },
  created () {
  },
  methods: {
    loadTop () {
      this.query.pageNo = 1
      this.loadType = 'REFRESH'
      this.findItems()
      this.findUnreadNumber()
      this.$refs.swipe.findCarousels()
      this.$refs.activity.findActivities()
    },
    findItems () {
      if (this.selected === 'NOVICE') {
        this.$api.findNoviceItems(this.query, result => {
          this.resource = result.resource
          if (this.$refs.loadTop) {
            this.$refs.loadTop.onTopLoaded()
          }
        }, err => {
          console.log(err.message)
          if (this.$refs.loadTop) {
            this.$refs.loadTop.onTopLoaded()
          }
        })
      } else if (this.selected === 'HOT') {
        this.$api.findItems(this.query, result => {
          this.resource = result.resource
          if (this.$refs.loadTop) {
            this.$refs.loadTop.onTopLoaded()
          }
        }, err => {
          console.log(err.message)
          if (this.$refs.loadTop) {
            this.$refs.loadTop.onTopLoaded()
          }
        })
      } else if (this.selected === 'IS_IN_PROGRESS') {
        this.findAuctionRecords()
      }
    },
    findAuctionRecords () {
      let sendData = {
        selete_type: this.selected,
        pageNo: this.query.pageNo,
        pageSize: this.query.pageSize
      }
      this.$api.findAuctionRecords(sendData, result => {
        result.resource.currentTime = result.resource.time
        this.resource = result.resource
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
      }, err => {
        console.log(err.message)
        if (this.$refs.loadTop) {
          this.$refs.loadTop.onTopLoaded()
        }
      })
    },
    getSystemTime () {
      this.$api.getSystemTime({}, data => {
        this.$global.systemTime = data.resource
      }, err => {
        console.log(err.message)
      })
    },
    loadMore () {
      if (!this.resource.last && !this.loading) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findItems()
      }
    },
    findUnreadNumber () {
      this.$api.findUnreadNumber(null, data => {
        this.unreadNumber = parseInt(data.resource)
      }, err => {
        console.log(err.message)
      })
    }
  }
}
</script>

<style>
.icon-message {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-image: url('../../static/icon/icon-message.svg');
  background-repeat: no-repeat;
  background-position: 0;
  background-size: 20px;
  margin-bottom: -3px;
}

.home {
  font-family: "微软雅黑";
}

.page-wrap {
  position: relative;
  z-index: 1;
}

.home .page-title span {
  margin-right: -54px;
}

.page-content {
  padding-top: 48px;
  padding-bottom: 55px;
}

.home .icon-right {
  position: absolute;
  right: 0px;
  top: 0px;
}


/*商品列表 navbar style*/

.home .home-navbar {
  margin-top: 10px;
  border-top: 1px solid #f2f2f2;
}

.home .home-navbar-container {
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

i.circle-red {
  display: inline-block;
  position: absolute;
  width: 10px;
  height: 10px;
  border-radius: 30px;
  background-color: #41ef00;
  right: 10px;
}
</style>
