<template>
  <div class="sign">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>签到</span>
      </div>
      <div class="page-content">
        <mt-loadmore :top-method="loadTop" ref="loadTop">
        <div class="sign-head">
          <img src="static/img/carousel/sign.jpg" alt="">
        </div>
        <div class="sign-day">
          <ul class="sign-dayList">
            <li v-for="(day,$index) in dayList" :key="$index">
              <h5>DAY {{$index+1}}</h5>
              <p>积分
                <span>+{{day.data}}</span>
              </p>
            </li>
          </ul>
          <div>
          <mt-button type="danger" @click="sign">
            <img src="static/icon/icon-calender.svg" alt="图标"> 每日签到送福利
          </mt-button>
          </div>
          <router-link :to="{name: 'signRule'}">
            <img src="static/img/carousel/sign-rule.jpg" alt="">
          </router-link>
        </div>
        <div class="sign-record">
          <div class="sign-record-data">
            <h3>{{resource.days}}
              <span>天</span>
            </h3>
          </div>
          <h5>已经连续签到</h5>
          <p>再连续签到
            <span>{{getDay}}天</span>可领取
            <span>宝箱x1</span>
          </p>
          <img src="static/img/box.png" alt="">
          <div v-if="parseInt(resource.days) === 4 || parseInt(resource.days) === 7">
          <mt-button type="danger" @click="receiveTreasure">
            领取宝箱
          </mt-button>
          </div>
        </div>
        <router-link :to="{name: 'signDetail'}" class="banner-large-link">
          <img src="static/img/carousel/sign-record.jpg" alt="">
        </router-link>
        <div class="sign-shop">
          <h3>积分兑换</h3>
          <ul class="sign-shop-list">
            <li v-for="(ssl,index) in signShopList" :key="index">
              <div @click="receive(ssl.key)">
                <h5>
                  <span>￥{{ssl.money}}</span>赠币</h5>
                <p>
                  <span>{{ssl.record}}</span>积分</p>
              </div>
            </li>
          </ul>
        </div>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'sign',
  data () {
    return {
      resource: {
        days: 0
      },
      dayList: [
        {
          data: '10'
        },
        {
          data: '10'
        },
        {
          data: '20'
        },
        {
          data: '30'
        },
        {
          data: '30'
        },
        {
          data: '50'
        },
        {
          data: '50'
        }
      ],
      signShopList: [
        {
          money: '1',
          record: '100',
          key: 'ONE_GIFT'
        },
        {
          money: '2',
          record: '200',
          key: 'TWO_GIFT'
        },
        {
          money: '3',
          record: '300',
          key: 'THREE_GIFT'
        },
        {
          money: '5',
          record: '500',
          key: 'FIVE_GIFT'
        }
      ]
    }
  },
  components: {
  },
  computed: {
    getDay: function () {
      let day = 0
      let days = parseInt(this.resource.days)
      if (days < 4) {
        day = 4 - days
      } else {
        day = 7 - days
      }
      return day
    }
  },
  created () {
  },
  activated () {
    this.findUserSignInfo()
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    findUserSignInfo () {
      this.$api.getUserSignInfo(null, data => {
        this.resource = data.resource
        this.$refs.loadTop.onTopLoaded()
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        this.$toast({
          message: err.message
        })
      })
    },
    loadTop () {
      this.findUserSignInfo()
    },
    sign () {
      this.$api.sign(null, data => {
        this.findUserSignInfo()
        this.$toast({
          message: '签到成功'
        })
      }, err => {
        this.$toast({
          message: err.message
        })
      })
    },
    receive (key) {
      this.$api.receive(key, data => {
        this.findUserSignInfo()
        this.$toast({
          message: '兑换成功'
        })
      }, err => {
        this.$toast({
          message: err.message
        })
      })
    },
    receiveTreasure () {
      this.$api.receiveTreasure(null, data => {
        this.findUserSignInfo()
        this.$messageBox({
          message: data.resource.message
        })
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
a.banner-large-link img {
  width: 100%;
}

.sign-head img {
  width: 100%;
}

.sign-day {
  background-color: #FFFFFF;
  padding: 10px;
  margin-top: -5px;
  text-align: center;
}

.sign-day button {
  box-shadow: 1px 1px 20px #fcd9d9;
}

.sign-day button img {
  height: 40%;
}

.sign-day a img {
  width: 100%;
}

ul.sign-dayList {
  margin: 0 auto;
  padding: 0;
  display: inline-block;
  max-width: 350px;
}

ul.sign-dayList li {
  float: left;
  text-align: center;
  width: 25%;
  margin-bottom: 10px;
}

ul.sign-dayList li:nth-child(7) {
  width: 50%;
}

ul.sign-dayList li:nth-child(7) h5,
ul.sign-dayList li:nth-child(7) p {
  display: block;
}

ul.sign-dayList li h5 {
  background-color: rgba(239, 0, 0, 0.8);
  display: inline-block;
  color: #FFFFFF;
  padding: 5px 10px;
  font-size: 14px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

ul.sign-dayList li p {
  font-size: 12px;
  line-height: 32px;
  padding: 5px 7px;
  color: #703315;
  display: inline-block;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  box-shadow: 1px 1px 20px #fcd9d9;
}

ul.sign-dayList li p span {
  font-size: 14px;
  font-weight: 800;
  margin-left: 5px;
}

.sign-record {
  background-color: #FFE00D;
  padding: 15px 15px 5px 15px;
  text-align: center;
}

.sign-record img {
  width: 100%;
}

.sign-record-data {
  background-color: #FFFFFF;
  box-shadow: 1px 1px 20px #fcd9d9;
  border-radius: 50%;
  height: 100px;
  width: 100px;
  text-align: center;
  vertical-align: middle;
  margin: 0 auto 20px auto;
}

.sign-record-data h3 {
  font-weight: 700;
  font-size: 36px;
  line-height: 100px;
  color: #703315;
}

.sign-record-data h3 span {
  font-size: 16px;
}

.sign-record p {
  font-size: 14px;
  line-height: 36px;
}

.sign-record p span {
  font-size: 18px;
  color: #703315;
  font-weight: 700;
  padding: 0 5px;
}

.sign-record img {
  width: 30%;
  min-width: 80px;
  max-width: 100px;
}

.sign-shop {
  padding: 10px;
  background-color: #FFFFFF;
  margin-top: -5px;
}

.sign-shop h3 {
  text-align: center;
  border-bottom: 1px solid #F2F2F2;
  padding-bottom: 10px;
  font-weight: 400;
  font-size: 16px;
}

ul.sign-shop-list {
  margin: 0;
  padding: 0;
  display: inline-block;
  width: 100%;
}

ul.sign-shop-list li {
  margin-top: 20px;
  float: left;
  width: 50%;
}

ul.sign-shop-list li div {
  text-align: center;
  background-color: #FFFFFF;
  box-shadow: 1px 1px 10px #eeeeee;
  width: 80%;
  margin: 0 auto;
  padding: 10px;
  border-radius: 8px;
  transition: 0.4s;
  border-radius: 0;
}

ul.sign-shop-list li div:before {
  transition: 0.4s;
  background-color: #FFFFFF;
  border-radius: 0;
}

ul.sign-shop-list li div:active {
  transition: 0.2s;
  background-color: #EEEEEE;
  border-radius: 50%;
}

ul.sign-shop-list li h5 {
  line-height: 24px;
  font-size: 14px;
  font-weight: 400;
}

ul.sign-shop-list li p span,
ul.sign-shop-list li h5 span {
  font-size: 16px;
  margin: 0 3px;
  font-weight: 700;
}

ul.sign-shop-list li p {
  font-size: 14px;
  font-weight: 400;
}
</style>
