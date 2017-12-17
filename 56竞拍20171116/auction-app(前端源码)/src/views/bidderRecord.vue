<template>
  <div id="bidderRecord">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.go(-1)">
          <i class="icon-back"></i>
        </a>
        <span>出价记录</span>
      </div>
      <div class="page-content">
        <mt-loadmore :top-method="loadTop" ref="loadTop" :style="{'min-height': minHeight + 'px'}">
          <div v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" :infinite-scroll-distance="10">
            <!-- <table style="width: 100%;color: #999999;font-size: 14px;">
              <tr v-for="(i, index) in resource.content" :key="i.price">
                <td style="display: inline-block;width: 10%; height: 40px;">
                  <img style="width:40px;height: 40px;" :src="$api.file.see(i.avatar)" onerror="this.src='static/icon/icon-appear.svg'"/>
                </td>
                <td style="display: inline-block;width: 20%;padding-left:5px;white-space: nowrap;word-wrap: normal;text-overflow: ellipsis;overflow: hidden;">
                  {{i.name}}
                </td>
                <td style="display: inline-block;width: 20%;padding-left:5px;white-space: nowrap;word-wrap: normal;text-overflow: ellipsis;overflow: hidden;">{{index === 0 ? '领先':'出局'}}</td>
                <td style="display: inline-block;width: 20%;padding-left:5px;white-space: nowrap;word-wrap: normal;text-overflow: ellipsis;overflow: hidden;">{{i.address}}</td>
                <td style="text-align:right;display: inline-block;width: 20%;">￥{{i.price}}</td>
              </tr>
            </table> -->
            <pai-record :content="resource.content"></pai-record>
            <div v-if="resource.content.length <= 0">
              <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
                暂无数据
              </div>
            </div>
          </div>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>

<script>
import paiRecord from 'components/paiRecord'
export default {
  components: {
    paiRecord
  },
  name: 'bidderRecord',
  data () {
    return {
      minHeight: 200,
      query: {
        itemId: '',
        sealedId: '',
        pageNo: 0,
        pageSize: 20
      },
      resource: {
        content: [],
        last: false
      },
      loading: false,
      loadType: 'REFRESH'
    }
  },
  created () {
    this.minHeight = window.innerHeight - 103
    this.loading = false
    this.query.pageNo = 0
    this.resource = {
      content: [],
      last: false
    }
    let itemId = this.$route.query.id
    let sealedId = this.$route.query.sealedId
    if (itemId) {
      this.query.itemId = itemId
    }
    if (sealedId) {
      this.query.sealedId = sealedId
    }
  },
  methods: {
    findBidderRcord () {
      this.loading = true
      this.$api.findBiddersRecor(this.query, data => {
        if (this.loadType === 'LOAD_MORE') {
          data.resource.content = this.resource.content.concat(data.resource.content)
        } else if (this.loadType === 'REFRESH') {
          data.resource.content = data.resource.content
        }
        this.resource = data.resource
        this.$refs.loadTop.onTopLoaded()
        this.loading = false
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        this.$toast({
          message: err.message,
          position: 'middle',
          duration: 3000
        })
      })
    },
    loadTop () {
      this.query.pageNo = 1
      this.loadType = 'REFRESH'
      this.findBidderRcord()
    },
    loadMore () {
      if (!this.resource.last && !this.loading) {
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.findBidderRcord()
      }
    }
  }
}
</script>

<style>

</style>
