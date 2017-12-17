<template>
  <div class="newDeal">
    <!-- 成交列表 -->
    <div class="dealList-List">
      <router-link :to="{name: 'newDeal'}" class="dealList-item" v-for="list in dealLists" :key="list">
        <div class="dealList-time">{{list.time}}</div>
        <img class="dealList-img" :src="list.img">
        <div class="dealList-title">
          <h5>{{list.name}}</h5>
          <p>市场价:￥{{list.prePrice}}</p>
          <p>市场价:
            <span>￥{{list.price}}</span>
          </p>
        </div>
        <div class="dealList-data">
          <b>{{list.precent}}%</b>
          <span>节省
            <i class="icon-down"></i>
          </span>
          <mt-button type="danger" size="small" v-if="isShowFollow">参与竞拍</mt-button>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script>
import auctionHeadline from 'components/headline'
export default {
  name: 'newdeal',
  data () {
    return {
      dealLists: [
        {
          time: '2017-07-21 14:07:24',
          img: 'static/img/product/img-computer.png',
          name: '帅气的呢城帅气的呢城帅气的呢城帅气的呢城帅气的呢城',
          prePrice: '9328.00',
          price: '2328.00',
          precent: '93.43'
        },
        {
          time: '2017-07-21 14:07:24',
          img: 'static/img/product/img-car.png',
          name: '一只熊',
          prePrice: '9328.00',
          price: '2328.00',
          precent: '93.43'
        },
        {
          time: '2017-07-21 14:07:24',
          img: 'static/img/product/img-car.png',
          name: '一只熊',
          prePrice: '9328.00',
          price: '2328.00',
          precent: '93.43'
        },
        {
          time: '2017-07-21 14:07:24',
          img: 'static/img/product/img-car.png',
          name: '一只熊',
          prePrice: '9328.00',
          price: '2328.00',
          precent: '93.43'
        },
        {
          time: '2017-07-21 14:07:24',
          img: 'static/img/product/img-camera.png',
          name: 'onolyy',
          prePrice: '9328.00',
          price: '2328.00',
          precent: '93.43'
        }
      ]
    }
  },
  computed: {
    isShowFollow: function () {
      let show = false
      let notSupport = ['goodsDetail']
      let index = notSupport.indexOf(this.$route.name)
      if (index === -1) {
        show = true
      }
      return show
    }
  },
  components: {
    auctionHeadline
  }
}
</script>

<style scoped>
/* danger button style*/

.mint-button--small {
  font-size: 12px;
  padding: 0px 5px !important;
  height: 28px;
  margin: 0;
}

.mint-button--danger {
  background-color: #ef0000;
  margin: 0;
}

.icon-down {
  display: inline-block;
  background-image: url('static/icon/icon-down.svg');
  background-position: 0;
  background-size: 8px 8px;
  width: 8px;
  height: 8px;
  margin-left: 5px;
}

.dealList-List {
  background-color: #ffffff;
  width: 100%;
}

a.dealList-item {
  display: block;
  border-bottom: 1px solid #f2f2f2;
  float: left;
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  width: 100%;
  background-color: #ffffff;
  transition: 0.4s;
  padding-bottom: 10px;
}

a.dealList-item:before {
  background-color: #FFFFFF;
  transition: 0.4s;
}

a.dealList-item:active {
  background-color: #f2f2f2;
  transition: 0.2s;
  border-radius: 50%;
}

.dealList-time {
  color: #999999;
  font-size: 14px;
  padding-left: 10px;
  padding-top: 10px;
}

img.dealList-img {
  float: left;
  width: 30%;
}

.dealList-title {
  float: left;
  width: 50%;
}

.dealList-title p {
  font-size: 14px;
  margin: 3px 0;
  padding-left: 10px;
  color: #333333;
}

.dealList-title h5 {
  font-size: 14px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  margin: 10px 0;
  font-weight: 600;
  padding-left: 10px;
  color: #333333;
}

.dealList-title p span {
  color: #EF0000;
}

.dealList-data {
  float: right;
  width: 20%;
  margin-top: 5px;
  display: block;
}

.dealList-data b {
  display: block;
  color: #333333;
}

.dealList-data span {
  display: inline-block;
  font-size: 12px;
  margin-bottom: 5px;
  color: #999999;
}

.dealList-data button {
  display: block;
  margin-top: 5px;
}
</style>
