<template>
  <div id="messageList">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.go(-1)">
          <i class="icon-back"></i>
        </a>
        <span>{{title}}</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top: 0px;" :style="{minHeight: minHeight + 'px'}">
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" infinite-scroll-distance="10" :infinite-scroll-immediate-check="false">
        <div @click="goto(item)" v-for="item in resource.content" :key="item.id">
          <mt-cell :title="item.title" :label="$filter.date(item.time)" is-link>
            <div class="circle-red" v-if="item.state === 'UNREAD'"></div>
          </mt-cell>
        </div>
        </div>
        <div v-if="resource.content.length <= 0">
          <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
            暂无通知
          </div>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
export default {
  name: 'messageList',
  data () {
    return {
      minHeight: '',
      title: '',
      type: '',
      query: {
        pageNo: 1,
        pageSize: 10
      },
      loading: true,
      loadType: 'REFRESH',
      resource: {
        content: [],
        last: false
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      if (from.name !== 'messageInfo') {
        vm.query.pageNo = 1
        vm.loadType = 'REFRESH'
        vm.resource = {
          content: [],
          last: false
        }
        vm.findNotices()
      }
    })
  },
  activated () {
    this.minHeight = window.innerHeight - 103
    this.loading = true
    this.type = this.$route.query.type
    if (this.type) {
      if (this.type === 'system') {
        this.title = '系统公告'
      } else if (this.type === 'personal') {
        this.title = '个人通知'
      }
    } else {
      this.$router.go(-1)
    }
  },
  deactivated () {
    this.loading = true
  },
  methods: {
    loadTop () {
      this.loading = true
      this.query.pageNo = 1
      this.findNotices()
    },
    loadMore () {
      if (!this.resource.last && !this.loading) {
        this.query.pageNo++
        this.findNotices()
      }
    },
    goto (row) {
      row.state = 'READ'
      this.$router.push({name: 'messageInfo', query: {id: row.id}})
    },
    findNotices () {
      this.loading = true
      if (this.type === 'system') {
        this.$api.findAnnouncementNotice(this.query, data => {
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
            message: err.message
          })
        })
      } else if (this.type === 'personal') {
        this.$api.findPersonalNotice(this.query, data => {
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
            message: err.message
          })
        })
      }
    }
  }
}
</script>

<style>
div.circle-red {
  width: 10px;
  height: 10px;
  border-radius: 30px;
  background-color: #ef0000;
}
</style>
