<template>
  <div class="signDetail">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>积分明细</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top:0px;" :style="{minHeight: minHeight + 'px'}">
        <div class="signDetail-head">
          <ul class="signDetail-title">
            <li>
              <div>类型/时间</div>
            </li>
            <li>
              <div>支出/收入</div>
            </li>
          </ul>
        </div>
        <div v-infinite-scroll="loadMore" :infinite-scroll-disabled="loading" infinite-scroll-distance="10">
        <div class="signDetail-content">
          <ul class="signDetail-list">
            <li v-for="(sdl,index) in resource.content" :key="sdl.id">
              <div class="signDetail-item">
                <h5>{{sdl.reason}}</h5>
                <p>
                  <b>{{$filter.date(sdl.time)}}</b>
                  <i>
                    <span :class="{}">{{sdl.number>0?'+':''}}{{sdl.number}}</span>积分</i>
                </p>
              </div>
            </li>
          </ul>
          <div v-if="resource.content.length <= 0">
            <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
              无更多数据
            </div>
          </div>
        </div>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
export default {
  name: 'signDetail',
  data () {
    return {
      minHeight: '',
      resource: {
        content: [],
        last: false
      },
      query: {
        pageNo: 0,
        pageSize: 20
      },
      loadType: 'REFRESH',
      loading: false,
      signDetailList: [
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '25'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '-5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '-5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '-5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '5'
        },
        {
          title: '每天签到积分奖励',
          dateTime: '2017-08-11 11:36:46',
          data: '-5'
        }
      ]
    }
  },
  components: {
  },
  created () {
    this.minHeight = window.innerHeight - 103
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    integralFlow () {
      this.$api.integralFlow(this.query, data => {
        if (this.loadType === 'LOAD_MORE') {
          data.resource.content = this.resource.content.concat(data.resource.content)
        }
        this.resource = data.resource
        this.$refs.loadTop.onTopLoaded()
        this.loading = false
      }, err => {
        this.loading = false
        this.$refs.loadTop.onTopLoaded()
        this.$toast({
          message: err.message
        })
      })
    },
    loadTop () {
      this.loadType = 'REFRESH'
      this.query.pageNo = 1
      this.integralFlow()
    },
    loadMore () {
      if (!this.resource.last && !this.loading) {
        this.loading = true
        this.query.pageNo++
        this.loadType = 'LOAD_MORE'
        this.integralFlow()
      }
    }
  }
}
</script>

<style>
ul {
  margin: 0;
  padding: 0;
}

.signDetail-head {
  position: fixed;
  display: inline-block;
  z-index: 4;
  width: 100%;
  background-color: #ffffff;
}

.mint-navbar .mint-tab-item.is-selected {
  border-bottom: 2px solid #ef0000;
  color: #ef0000;
  background-color: #ffffff;
}

.signDetail .mint-tab-item-label {
  font-size: 14px;
}

.signDetail .mint-navbar .mint-tab-item {
  padding: 14px 0;
}

ul.signDetail-title {
  display: inline-block;
  width: 100%;
  background-color: #FFFFFF;
  margin-top: 3px;
  background-color: #ffffff;
  border-bottom: 1px solid #f2f2f2;
  border-top: 1px solid #f2f2f2;
}

ul.signDetail-title li {
  float: left;
  width: 50%;
  font-size: 12px;
  color: #333333;
  padding: 10px 0;
  line-height: 24px;
}

ul.signDetail-title li:nth-child(2) {
  text-align: right;
}

ul.signDetail-title li div {
  padding: 0 10px;
  display: block;
  border-right: 1px solid #f2f2f2;
}

.signDetail-content {
  padding-top: 53px;
  background-color: #ffffff;
}

ul.signDetail-list {
  display: block;
  padding: 10px;

  margin-top: -8px;
}

ul.signDetail-list li {
  border-bottom: 1px solid #f2f2f2;
  padding: 5px 0;
  background-color: #ffffff;
}

ul.signDetail-list li h5 {
  font-size: 14px;
  font-weight: 400;
  line-height: 24px;
  color: #333333;
}

ul.signDetail-list li p {
  line-height: 24px;
  font-size: 14px;
}

ul.signDetail-list li p b {
  color: #999999;
  font-size: 12px;
  font-weight: 400;
}

ul.signDetail-list li p i {
  float: right;
  margin-top: -12px;
}

ul.signDetail-list li p i span {
  font-size: 16px;
  margin-right: 5px;
}
</style>
