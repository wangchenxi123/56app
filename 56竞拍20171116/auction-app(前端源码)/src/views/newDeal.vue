<template>
  <div class="newDeal">
    <div class="page-wrap">
      <div class="page-title">最近成交</div>
      <div class="page-content">
        <mt-loadmore :top-method="loadTop" ref="loadTop" >
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
        <!-- 56头条-->
        <auction-headline></auction-headline>
        <!-- 成交列表 -->
        <deal-List :content="resource.content"></deal-List>
        </div>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>

<script>
import auctionHeadline from 'components/headline'
import dealList from 'components/dealList'
var scrollTop = 0
var list = ['goodsDetail', 'goodsHistoryDetail']
export default {
  name: 'newdeal',
  data () {
    return {
      query: {
        pageNo: 0,
        pageSize: 20
      },
      resource: {
        content: [],
        last: false
      },
      loading: false,
      loadType: 'REFRESH',
      scrollTop: 0
    }
  },
  components: {
    auctionHeadline, dealList
  },
  created () {
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
    this.loading = false
  },
  deactivated () {
    this.loading = true
  },
  methods: {
    findNewDeals () {
      this.loading = true
      this.$api.findNewDeals(this.query, data => {
        if (this.loadType === 'LOAD_MORE') {
          data.resource.content = this.resource.content.concat(data.resource.content)
        }
        this.resource = data.resource
        this.$refs.loadTop.onTopLoaded()
        this.loading = false
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        this.loading = false
        this.$toast({
          message: err.message,
          duration: 3000
        })
      })
    },
    loadTop () {
      this.query.pageNo = 1
      this.loadType = 'REFRESH'
      this.findNewDeals()
    },
    loadMore () {
      if (!this.loading && !this.resource.last) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findNewDeals()
      }
    }
  }
}
</script>
