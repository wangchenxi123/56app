<template>
  <div class="newDeal">
    <!-- 成交列表 -->
    <ul class="newDeal-list">
      <li v-for="(list,index) in content" :key="list.id">
        <a href="javascript: void(0);" @click="goto(list)" class="dealList-item">
          <div class="dealList-time">{{$filter.date(list.time)}}</div>
  
          <div class="dealList-img">
            <img v-if="showDealed" :src="$api.file.see(list.item_picture)" onerror="this.src='static/img/product/img-computer.png'">
            <img v-else :src="$api.file.see(list.userMessage.head_pic)" class="dealList-img-head" onerror="this.src='static/icon/icon-appear.svg'">
            <i class="icon-finish" v-if="showDealed?true:false"></i>
          </div>
  
          <div class="dealList-title">
            <h5>{{list.name}}</h5>
            <p>市场价:{{$filter.currency(showDealed ? list.marketPrice : list.itemMessage.price)}}</p>
            <p>竞拍价:<span>￥{{showDealed ? list.price : list.market_price}}</span></p>
          </div>
          <div class="dealList-data">
            <b>{{getPercent(index, list)}}%</b>
            <mt-button type="danger" size="small" v-if="isShowFollow">参与竞拍</mt-button>
          </div>
        </a>
      </li>
    </ul>
    <div v-if="content.length <= 0">
      <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
        暂无数据
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'dealList',
  props: {
    content: {
      type: Array,
      default: function () {
        return []
      }
    },
    showDealed: {
      type: Boolean,
      default: function () {
        return true
      }
    }
  },
  data () {
    return {
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
  deactivated () {
  },
  methods: {
    goto (item) {
      if (this.showDealed) {
        this.$router.push({name: 'goodsHistoryDetail', query: { newDeal: 1, record_id: item.record_id }})
      } else {
        this.$router.push({name: 'goodsHistoryDetail', query: { record_id: item.id }})
      }
    },
    getPercent (index, row) {
      if (this.showDealed) {
        return ((1 - (parseFloat(row.price) / parseFloat(row.marketPrice))) * 100).toFixed(2)
      } else {
        return ((1 - (parseFloat(row.market_price) / parseFloat(row.itemMessage.price))) * 100).toFixed(2)
      }
    }
  }
}
</script>

<style scoped>
/* danger button style*/

.mint-button--small {
  font-size: 16px;
  padding: 0px 15px !important;
  height: 28px;
  margin: 0;
}

.mint-button--danger {
  background-color: #ef0000;
  margin: 0;
}

.icon-down {
  display: inline-block;
  background-image: url('../../static/icon/icon-down.svg');
  background-position: 0;
  background-size: 8px 8px;
  width: 8px;
  height: 8px;
  margin-left: 5px;
}

ul.newDeal-list {
  margin: 0;
  padding: 0;
}

ul.newDeal-list li {
  width: 100%;
  display: block;
  border-bottom: 1px solid #f2f2f2;
  background-color: #FFFFFF;
}

ul.newDeal-list li a.dealList-item {
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  background-color: #ffffff;
  transition: 0.4s;
  display: inline-block;
  width: 100%;
}

ul.newDeal-list li a.dealList-item:before {
  background-color: #FFFFFF;
  transition: 0.4s;
}

ul.newDeal-list li a.dealList-item:active {
  background-color: #f2f2f2;
  transition: 0.2s;
  border-radius: 50%;
}

ul.newDeal-list li a .dealList-time {
  color: #999999;
  font-size: 18px;
  padding-left: 10px;
  padding-top: 10px;
}

ul.newDeal-list li a .dealList-img {
  float: left;
  width: 15%;
  position: relative;
  padding: 10px;
}

ul.newDeal-list li a .dealList-img img {
  width: 100%;
}

ul.newDeal-list li a .dealList-title {
  float: left;
  width: 40%;
}

ul.newDeal-list li a .dealList-title p {
  font-size: 14px;
  margin: 3px 0;
  padding-left: 10px;
  color: #333333;
}

ul.newDeal-list li a .dealList-title h5 {
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

ul.newDeal-list li a .dealList-title p span {
  color: #EF0000;
}

ul.newDeal-list li a .dealList-data {
  float: right;
  width: 30%;
  margin-top: 5px;
  display: block;
}

ul.newDeal-list li a .dealList-data b {
  display: block;
  color: #333333;
  font-size: 20px;
  padding-bottom: 10px;
}

ul.newDeal-list li a .dealList-data span {
  display: inline-block;
  font-size: 12px;
  margin-bottom: 5px;
  color: #999999;
}

ul.newDeal-list li a .dealList-data button {
  display: block;
}
img.dealList-img-head{
  width:60px !important;
  height: 60px !important;
  border-radius:30px;
  margin-left:10px;
  margin-top: 20px;
}
</style>
