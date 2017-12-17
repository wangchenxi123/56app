<template>
  <div class="collection">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>宝贝收藏</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top:0;" :style="{minHeight: minHeight + 'px'}">
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
          <collection-list :content="resource.content"></collection-list>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
import globalBus from '../globaBus'
import collectionList from 'components/collectionList'
export default {
  name: 'collection',
  data () {
    return {
      minHeight: '',
      query: {
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
  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (from.name === 'userCenter') {
        vm.query.pageNo = 1
        vm.loadType = 'REFRESH'
        vm.resource = {
          content: [],
          last: false
        }
        vm.loading = true
        vm.findCollections()
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
    findCollections () {
      this.$api.findCollections(this.query, data => {
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
      // this.resource.content = []
      this.loadType = 'REFRESH'
      this.findCollections()
    },
    loadMore () {
      if (!this.loading && !this.resource.last) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findCollections()
      }
    }
  },
  mounted () {
  },
  components: {
    collectionList
  },
  watch: {
  }
}
</script>

<style scoped>

</style>
