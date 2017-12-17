<template>
  <div class="showOrder">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>晒单</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top:0px;" :style="{minHeight: minHeight + 'px'}">
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
          <order-list :content="resource.content"></order-list>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
import globalBus from '../globaBus'
import orderList from 'components/orderList'
export default {
  name: 'showOrder',
  data () {
    return {
      minHeight: '',
      query: {
        userId: '',
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
  components: {
    orderList
  },
  beforeRouteEnter (to, from, next) {
    if (to.query.type === 'self') {
      if (!to.query.userId) {
        next('/login')
        return
      }
    }
    next(vm => {
      if (to.query.userId) {
        vm.query.userId = to.query.userId
      }
      if (from.name !== 'showOrderDetail') {
        vm.query.pageNo = 1
        vm.loadType = 'REFRESH'
        vm.resource = {
          content: [],
          last: false
        }
        vm.findShowOrders()
      }
    })
  },
  activated () {
    this.loading = false
    this.minHeight = window.innerHeight - 103
  },
  deactivated () {
    this.loading = true
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    findShowOrders () {
      this.loading = true
      this.$api.findShowOrders(this.query, data => {
        if (this.loadType === 'LOAD_MORE') {
          data.resource.content = this.resource.content.concat(data.resource.content)
        }
        this.resource = data.resource
        this.loading = false
        this.$refs.loadTop.onTopLoaded()
      }, err => {
        this.loading = false
        this.$refs.loadTop.onTopLoaded()
        this.$toast({
          message: err.message,
          position: 'middle',
          duration: 3000
        })
        if (err.responseCode === '401') {
          this.$router.replace({name: 'login'})
        }
      })
    },
    loadTop () {
      this.query.pageNo = 1
      this.loadType = 'REFRESH'
      this.resource.content = []
      this.findShowOrders()
    },
    loadMore () {
      if (!this.loading && !this.resource.last) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findShowOrders()
      }
    }
  }
}
</script>

<style>

</style>
