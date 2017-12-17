<template>
  <div class="productGrids">
    <div class="home-navbar-container">
      <a href="javascript: void(0);" @click="goto(item)" class="goodsList" v-for="item in itemList" :key="item.id">
        <div class="goodslist-box">
          <img class="tag-tenpro" src="static/icon/icon-ten.svg" alt="10元专区" v-show="item.plus_code === 10">
          <div class="product-img">
            <i class="icon-finish" v-if="item.settlement"></i>
            <img class="goodsList-img" :src="$api.file.see(item.itemBean.small_picture)" onerror="this.src='static/img/product/img-car.png'" alt="product">
          </div>
          <b :ref="'time' + item.itemBean.id"></b>
          <p :class="{active: item.active}">当前价:
            <span>￥{{item.price}}</span>
          </p>
          <h5>{{item.itemBean.name}}</h5>
          <mt-button v-if="!item.interrupted" :type="item.settlement?'default':'danger'" size="small">{{item.settlement?'去下一期':'参与竞拍'}}</mt-button>
        </div>
      </a>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'productGrids',
    props: {
      resource: {
        type: Object,
        default: function () {
          return {
            content: []
          }
        }
      },
      loadType: {
        type: String,
        default: function () {
          return 'REFRESH'
        }
      }
    },
    data () {
      return {
        itemList: {},
        timerList: {},
        message: {}
      }
    },
    computed: {
    },
    watch: {
      resource: function (val, oldVal) {
        if (this.loadType === 'REFRESH') {
          this.itemList = {}
          for (let key in this.timerList) {
            window.clearInterval(this.timerList[key].timer)
          }
          this.timerList = {}
        }
        val.content.forEach(item => {
          if (!this.itemList.hasOwnProperty(item.itemBean.id)) {
            this.$set(this.itemList, item.itemBean.id, item)
            if ((this.timerList[item.itemBean.id] && this.timerList[item.itemBean.id].timer)) {
              window.clearInterval(this.timerList[item.itemBean.id].timer)
            }
            if (!this.timerList[item.itemBean.id]) {
              this.timerList[item.itemBean.id] = {
                // time: parseInt(1000 * Math.random(0, 1)),
                time: 10000 - (parseInt(val.currentTime) - parseInt(item.instant_time)),
                timer: ''
              }
            }
            this.$nextTick(() => {
              this.setTimer(item)
            })
          }
        })
      },
      '$global.WSMessage': function (val, old) {
        let item = val
        item.itemBean = {
          id: val.itemId
        }
        let itemId = val.itemId
        if (val.message_type === 'BIDDER') {
          if (this.timerList.hasOwnProperty(itemId) && !this.itemList[itemId].settlement && !this.itemList[itemId].interrupted) {
            this.timerList[itemId].time = 10000
            this.$set(this.itemList[itemId], 'price', val.price)
            this.$set(this.itemList[itemId], 'active', true)
            window.setTimeout(() => {
              if (this.itemList[itemId]) {
                this.$set(this.itemList[itemId], 'active', false)
              }
            }, 1000)
            if (this.timerList[itemId].timer) {
              window.clearInterval(this.timerList[itemId].timer)
            }
            this.setTimer(item)
          }
        } else if (val.message_type === 'HEADLINES' && val.userId) {
          if (this.timerList.hasOwnProperty(itemId)) {
            this.itemList[itemId].settlement = true
            if (this.timerList[itemId].timer) {
              window.clearInterval(this.timerList[itemId].timer)
            }
            this.$refs['time' + itemId][0].innerText = '00:00:00'
          }
        } else if (val.message_type === 'SYSTEM_SLEEP') {
          this.$set(this.itemList[itemId], 'interrupted', true)
          item.interrupted = true
          if (this.timerList[itemId].timer) {
            window.clearInterval(this.timerList[itemId].timer)
          }
          this.setTimer(item)
        } else {
          if (this.timerList[itemId] && !this.itemList[itemId].settlement && !this.itemList[itemId].interrupted) {
            this.timerList[itemId].time = 10000
            if (this.timerList[itemId].timer) {
              window.clearInterval(this.timerList[itemId].timer)
            }
            this.setTimer(item)
          }
        }
      }
    },
    created () {
    },
    methods: {
      goto (item) {
        this.$router.push({name: 'goodsDetail', query: {id: item.itemBean.id}})
      },
      setTimer (temp) {
        let time = this.$refs['time' + temp.itemBean.id][0]
        let _this = this
        if (temp.interrupted) {
          time.innerText = '休眠中...'
          return
        }
        if (temp.settlement) {
          time.innerText = '00:00:00'
        } else {
          time.innerHTML = _this.formData(parseInt(_this.timerList[temp.itemBean.id].time))
          this.timerList[temp.itemBean.id].timer = window.setInterval(function () {
            _this.timerList[temp.itemBean.id].time = _this.timerList[temp.itemBean.id].time - 1000
            time.innerHTML = _this.formData(parseInt(_this.timerList[temp.itemBean.id].time))
            if (_this.timerList[temp.itemBean.id].time < 1000) {
              window.clearInterval(_this.timerList[temp.itemBean.id].timer)
            }
          }, 1000)
        }
      },
      formData (t) {
        let text = '00:00:00'
        if (t >= 1000 && t < 10000) {
          text = '00:00:0' + t
        } else if (t >= 10000 && t < 100000) {
          text = '00:00:' + t
        }
        return text.slice(0, 8)
      },
      re (item) {
        let itemId = item.itemBean.id
        this.timerList[itemId].time = 10000
        this.$set(this.itemList[itemId], 'price', parseInt(this.itemList[itemId].price) + 1 + '.00')
        this.$set(this.itemList[itemId], 'active', true)
        window.setTimeout(() => {
          this.$set(this.itemList[itemId], 'active', false)
        }, 1000)
        if (this.timerList[itemId].timer) {
          window.clearInterval(this.timerList[itemId].timer)
        }
        this.setTimer(item)
      }
    }
  }
</script>

<style>
  .productGrids {
    font-family: "微软雅黑";
  }

  .product-img {
    position: relative;
    z-index: 1;
  }

  .icon-finish {
    background-image: url('../../static/icon/icon-finish.svg');
    display: inline-block;
    background-repeat: no-repeat;
    height: 70%;
    width: 70%;
    top: 10%;
    right: 0;
    background-position: 0;
    background-size: 70%;
    position: absolute;
    z-index: 3;
  }

  .page-wrap {
    position: relative;
    z-index: 1;
  }

  .page-title {
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

  .page-content {
    padding-top: 48px;
    padding-bottom: 55px;
  }


  /* danger button style*/

  .mint-button--small {
    font-size: 16px;
    padding: 0px 15px !important;
    height: 32px;
    margin: 0;
  }

  .mint-button--danger {
    background-color: #ef0000;
    margin: 0;
  }



  /*商品列表 list style*/

  a.goodsList {
    text-decoration: none;
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    text-align: center;
    display: inline-block;
    width: 50%;
    margin: 0;
    background-color: #ffffff;
    transition: 0.4s;
    position: relative;
  }

  a.goodsList:before {
    background-color: #FFFFFF;
    transition: 0.4s;
  }

  a.goodsList:active {
    background-color: #f2f2f2;
    transition: 0.2s;
    border-radius: 50%;
  }

  a.goodsList .goodslist-box {
    border-right: 1px solid #fbfbfb;
    border-bottom: 1px solid #fbfbfb;
    padding: 0 10px 10px 10px;
  }

  a.goodsList p {
    color: #ef0000;
    font-size: 16px;
    height: 24px;
    line-height: 24px;
    margin: 0;
  }

  a.goodsList p.active {
    border-radius: 30px;
    animation: priceAcitve 1s infinite ease;
    -moz-animation: priceAcitve 1s infinite ease;
    /* Firefox */
    -webkit-animation: priceAcitve 1s infinite ease;
    /* Safari and Chrome */
    -o-animation: priceAcitve 1s infinite ease;
    /* Opera */
  }

  a.goodsList p span {
    font-size: 16px;
  }

  a.goodsList h5 {
    margin: 5px 0;
    font-size: 12px;
    white-space: nowrap;
    word-wrap: normal;
    text-overflow: ellipsis;
    overflow: hidden;
    color: #333333;
    font-weight: 500;
    height: 18px;
    line-height: 18px;
  }

  a.goodsList .goodsList-img {
    width: 100%;
  }

  a.goodsList b {
    color: #EF0000;
    font-size: 18px;
  }

  .tag-tenpro {
    position: absolute;
    z-index: 2;
    width: 30px;
    height: 30px;
    top: 0;
    left: 0;
  }



  /* 价格背景脉动动画 */

  @keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-moz-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-webkit-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-o-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }
</style>
