<template>
  <div class="userCenter">
    <div class="page-wrap">
      <mt-loadmore :top-method="loadTop" ref="loadTop" >
      <div class="page-title">
        <span>个人中心</span>
        <router-link :to="{name: 'setting'}" class="icon-right">
          <i class="icon-setting"></i>
        </router-link>
      </div>
      <div class="page-content">
        <div class="userCenter-head">
          <template v-if="$global.USER.id">
            <router-link :to="{name: 'userInfo'}">
              <img class="userCenter-head-headimg" :src="$api.file.see(user.head_pic)" onerror="this.src='static/icon/icon-appear.svg'" alt="头像">
              <div class="userCenter-head-info">
                <p>{{user.username}}</p>
                <h3>ID:{{user.id}}</h3>
              </div>
              <div class="userCenter-head-vip" @click="gotoUserInfo">
                <img src="static/icon/icon-vip.svg">
                <i>个人设置</i>
              </div>
            </router-link>
          </template>
          <template v-else>
            <router-link tag="div" style="width: 100%;height: 100%;" :to="{name: 'login'}">
              <div style="position: relative;height: 50%;">
                <img style="width: 15%;height: 100%;left: 50%;position: absolute;bottom: -20%;transform:translateX(-50%);" src="static/icon/icon-appear.svg" alt="">
              </div>
              <div style="height: 50%; text-align: center;">
                <p style="padding-top: 5%;color: white; margin-top: 4px;">登录</p>
              </div>
            </router-link>
          </template>
        </div>
        <div class="userCenter-charge">
          <div class="userCenter-charge-list">
            <router-link :to="{name: 'userCenter'}">
              <h4>拍币:{{user.coin || 0}}</h4>
            </router-link>
            <a href="" class="new">充值</a>
            <router-link :to="{name: 'userCenter'}">
              <h4>赠币:{{user.integral || 0}}</h4>
            </router-link>
          </div>
          <div style="clear: both;"></div>
        </div>
        <div class="userCenter-pai-record">
          <mt-cell title="竞拍记录">
            <img slot="icon" src="../../static/icon/icon-record.svg">
          </mt-cell>
          <ul>
            <li>
              <a @click="toMyAuction('ALL')">
                <i class="icon-recorditem icon-pai-all"></i>
                <span>全部</span>
              </a>
            </li>
            <li>
              <a @click="toMyAuction('IS_IN_PROGRESS')">
                <i class="icon-recorditem icon-pai-time"></i>
                <span>正在拍</span>
              </a>
            </li>
            <li>
              <a @click="toMyAuction('WINNING')">
                <i class="icon-recorditem icon-pai-finish"></i>
                <span>我拍中</span>
              </a>
            </li>
            <li>
              <a @click="toMyAuction('FAILURE')">
                <i class="icon-recorditem icon-pai-no"></i>
                <span>未拍中</span>
              </a>
            </li>
            <li>
              <template v-if="$global.showPay">
                <a @click="toMyAuction('WAITING_PAYMENT')">
                  <i class="icon-recorditem icon-pai-wallet"></i>
                  <span>待付款</span>
                </a>
              </template>
              <template v-else>
                <a @click="toMyAuction('WAITING_SUN_ALONE')">
                  <i class="icon-recorditem icon-pai-wallet"></i>
                  <span>待晒单</span>
                </a>
              </template>
            </li>
          </ul>
        </div>
        <div class="userCenter-function-list">
          <mt-cell title="我的晒单" is-link :to="{name: 'showOrder', query: {userId: user.id, type: 'self'}}">
            <span class="text-danger">晒单返币</span>
            <img slot="icon" src="../../static/icon/icon-carma.svg">
          </mt-cell>
          <mt-cell title="收货地址" is-link to="userAddress">
            <img slot="icon" src="../../static/icon/icon-car.svg">
          </mt-cell>
          <mt-cell title="宝贝收藏" is-link to="collection">
            <img slot="icon" src="../../static/icon/icon-star.svg">
          </mt-cell>
          <mt-cell title="客服中心" is-link to="helpCenter">
            <span>周一至周六 9:00-18:30</span>
            <img slot="icon" src="../../static/icon/icon-headset.svg">
          </mt-cell>
          <p style="padding: 10px 0;font-size: 13px;text-align: center;">
            声明：所有商品竞拍活动与苹果公司(Apple Inc)无关
          </p>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
var scrollTop = 0
var list = ['userInfo', 'setting', 'recharge', 'muAuction', 'share', 'userAddress', 'collection', 'helpCenter']
export default {
  name: 'userCenter',
  data () {
    return {
    }
  },
  computed: {
    user: function () {
      return this.$global.USER
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
    this.loadTop()
  },
  methods: {
    toMyAuction (type) {
      if (this.$global.USER.id) {
        this.$router.push({name: 'myAuction', query: {type: type}})
      } else {
        this.$router.push({ name: 'login' })
      }
    },
    gotoUserInfo () {
      this.$router.push({ name: 'userInfo' })
    },
    loadTop () {
      this.$api.getUserInfo('', data => {
        this.$global.USER = data.resource
        let wsUrlArray = this.$global.WS.url.split('/')
        let oldUrlId = wsUrlArray[wsUrlArray.length - 1]
        if (oldUrlId !== data.resource.id) {
          this.$global.WS.close()
        }
        this.$refs.loadTop.onTopLoaded()
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        if (err.responseCode !== '401') {
          this.$toast({
            message: err.message
          })
        }
      })
      this.showPayPage()
    },
    toRecharge () {
      if (this.$global.showPay) {
        this.$router.push({name: 'recharge'})
      }
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

<style scoped>
	.news{
		display: block;
		width: 100%;
		height: 30px;
		background: #EF0000;
		font-size: 18px;
		line-height: 30px;
		color: white;
		font-weight: bold;
		border-radius: 15px;
	}
.text-danger {
  color: #ef0000;
}
.userCenter-head{
	background: url(../../static/img/bg.png) no-repeat;
	background-size:100% 100%;
}
.new{
	display:block;
	line-height: 40px;
	background: red;
	float: left;
	width: 20% !important;
	color: white;
	border-radius: 20px;
	font-size: 18px;
	font-weight: 600;
}
a {
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}

.userCenter .page-title {
  height: 48px;
  line-height: 48px;
  text-align: center;
  color: #FFFFFF;
  background-color: #ef0000;
  font-size: 18px;
  font-weight: 700;
  position: fixed;
  width: 100%;
  z-index: 2;
}

.icon-setting {
  display: inline-block;
  height: 20px;
  width: 20px;
  background-image: url('../../static/icon/icon-setting.svg');
  background-repeat: no-repeat;
  background-position: 0;
  background-size: 20px;
  margin-bottom: -3px;
}

.userCenter .page-title span {
  margin-right: -112px;
}

.userCenter-head a:before,
.userCenter .page-title a.icon-right:before {
  background-color: #ef0000;
  transition: 0.4s;
  border-radius: 50%;
}

.userCenter-head a:active,
.userCenter .page-title a.icon-right:active {
  background-color: #c90d0d;
  transition: 0.2s;
  border-radius: 0px;
}

.page-content {
  padding-bottom: 55px;
}

.userCenter-head {
  width: 100%;
  height: 110px;
}

.userCenter-head a {
  color: #FFFFFF;
  transition: 0.4s;
  border-radius: 50%;
  width: 100%;
  display: block;
  height: 100%;
}

img.userCenter-head-headimg {
  width: 60px;
  height: 60px;
  border-radius: 30px;
  display:inline-block;
  margin-left:10px;
}

.userCenter-head-info {
  width: 50%;
  display: inline-block;
}

.userCenter-head-info p {
  margin: 0;
  font-size: 18px;
  padding-bottom: 10px;
  width: 100%;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  padding-left: 10px;
  font-weight: 400;
}

.userCenter-head-info h3 {
  font-size: 12px;
  max-width:110px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  background: #c90d0d;
  padding: 5px 10px 5px 10px;
  border-radius: 20px;
  box-shadow: 1px 2px 10px #c90d0d;
  margin-left: 5px;
}

.userCenter-head-vip {
  background-color: #FFC23C;
  display: inline-block;
  float: right;
  margin-top: 30px;
  padding: 5px 5px 3px 10px;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  width: 10%;
  max-width: 80px;
  min-width: 70px;
}

.userCenter-head-vip i {
  font-size: 12px;
  font-style: normal;
  float: right;
  margin-top: 1px;
}

.userCenter-head-vip img {
  width: 16px;
  height: 16px;
}

.userCenter-charge {
  margin-bottom: 10px;
  background-color: #FFFFFF;
  padding: 30px 0;
}

.userCenter-charge-list {
  /*20170914修改 不换行*/
	white-space: nowrap;
}

.userCenter-charge-list a {
  margin: 0;
  width: 40%;
  display:block;
  text-align: center;
  transition: 0.4s;
  float: left;
}
.userCenter-charge-list a h4 {
  padding:0;
  font-size: 20px;
  font-family: PingFangSC-Medium, sans-serif;
  color:black;
  font-weight: 400;
  width: 100%;
  text-align: center;
  height: 40px;
  line-height: 40px;
}


.userCenter-charge img,
.userCenter-pai-record img,
.userCenter-function-list img {
  width: 24px;
  height: 24px;
}

.userCenter-pai-record {
  background-color: #ffffff;
  margin-bottom: 10px;
  border-bottom: 1px solid #f2f2f2;
}

.userCenter-pai-record ul {
  margin: 0;
  padding: 0;
  white-space: nowrap;
  border-top: 1px solid #f2f2f2;
}

.userCenter-pai-record ul li {
  display: inline-block;
  padding: 0;
  margin: 0 0 0 -5px;
  width: 20%;
  text-align: center;
  list-style: none;
}

.userCenter-pai-record ul li a {
  padding: 10px 0;
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  font-size: 12px;
  text-align: center;
  outline: none;
  display: block;
  color: #333333;
  transition: 0.4s;
}

.userCenter-charge-list a:before,
.userCenter-pai-record ul li a:before {
  color: #666666;
  background-color: #FFFFFF;
  transition: 0.4s;
}

.userCenter-charge-list a:active,
.userCenter-charge-list a:after,
.userCenter-pai-record ul li a:after,
.userCenter-pai-record ul li a:active {
  color: #000000;
  background-color: #f2f2f2;
  transition: 0.2s;
  border-radius: 50%;
}

.userCenter-pai-record ul li a i {
  padding: 5px 0;
}

.userCenter-pai-record ul li a span {
  display: block;
}
</style>
