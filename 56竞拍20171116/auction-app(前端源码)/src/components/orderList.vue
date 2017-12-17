<template>
  <div class="orderList">
    <router-link :to="{name: 'showOrderDetail', query: {orderId: ol.orderId}}" class="orderItem" v-for="(ol, index) in content" :key="ol.id">
      <div class="orderItem-left">
        <img :src="$api.file.see(ol.head_pic)" onerror="this.src='static/icon/icon-appear.svg'" alt="头像">
      </div>
      <div class="orderItem-right">
        <div class="orderItem-content">
          <h3>{{ol.username}}
            <span>{{$filter.date(ol.time)}}</span>
          </h3>
          <h5>{{ol.itemName}}</h5>
          <p class="orderItem-content-message">{{ol.context}}</p>
        </div>
        <ul class="orderItem-photo">
          <li v-for="olimg in ol.pictureList" :key="olimg">
            <img :style="{height:photoHeight+'px'}" :src="$api.file.see(olimg)" alt="">
            <!-- <img :style="{height:photoHeight+'px'}" :src="olimg" alt=""> -->
          </li>
        </ul>
      </div>
    </router-link>
    <div v-if="content.length <= 0">
      <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
        暂无数据
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'orderList',
  props: {
    content: {
      type: Array,
      default: function () {
        return []
      }
    }
  },
  data () {
    return {
      photoHeight: 100,
      temData: [
        {
          head_pic: 'static/icon/icon-appear.svg',
          username: '圣诞快乐飞机',
          time: new Date().getTime(),
          itemName: '山卡拉黄齑淡饭',
          context: '奥斯卡刘德华分开了安徽省翻盘啊维生素分类；卡顿就离开；发家收到了；',
          pictureList: [
            'static/icon/icon-appear.svg',
            'static/icon/icon-appear.svg',
            'static/icon/icon-appear.svg'
          ]
        }
      ]
    }
  },
  created () {
    if (window.innerWidth) {
      let pwidth = window.innerWidth - 20
      this.photoHeight = parseInt(pwidth / 3.5)
    }
  },
  methods: {
  },
  mouted () {
  }
}
</script>

<style>
.orderList {
  padding: 0 10px;
  background-color: #FFFFFF;
}

a.orderItem {
  display: inline-block;
  padding-top: 10px;
  padding-bottom: 10px;
  width: 100%;
  border-bottom: 1px solid #f2f2f2;
  background-color: #FFFFFF;
  margin-bottom: -4px;
  transition: 0.4s;
  border-radius: 0;
}

a.orderItem:before {
  transition: 0.4s;
  background-color: #FFFFFF;
  border-radius: 0;
}

a.orderItem:active {
  transition: 0.2s;
  background-color: #eeeeee;
  border-radius: 50%;
}

a.orderItem .orderItem-left {
  width: 15%;
  float: left;
}

a.orderItem .orderItem-left img {
  /* max-width: 56px;
  min-width: 24px;
   */
   width:50px;
   height: 50px;
   border-radius:50%;
  float: right;
}

a.orderItem .orderItem-right {
  width: 85%;
  float: left;
}

a.orderItem .orderItem-right .orderItem-content h3,
a.orderItem .orderItem-right .orderItem-content h5,
a.orderItem .orderItem-right .orderItem-content p {
  margin-left: 10px;
  font-weight: 400;
}

a.orderItem .orderItem-right .orderItem-content h3 {
  font-size: 16px;
  color: #333333;
}

a.orderItem .orderItem-right .orderItem-content h3 span {
  color: #999999;
  float: right;
  font-size: 12px;
}

a.orderItem .orderItem-right .orderItem-content h5 {
  font-size: 12px;
  color: #999999;
  display: block;
  background-color: #f2f2f2;
  padding: 5px 10px;
  margin-top: 10px;
  margin-bottom: 10px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
}

a.orderItem .orderItem-right .orderItem-content p {
  font-size: 14px;
  color: #333333;
  overflow: hidden;
  max-height: 40px;
}

a.orderItem .orderItem-right ul.orderItem-photo {
  white-space: nowrap;
  margin: 5px 0 0 0;
  padding: 0;
  overflow-x: scroll;
}

a.orderItem .orderItem-right ul.orderItem-photo li {
  list-style: none;
  width: 30.33%;
  text-align: center;
  display: inline-block;
  margin-right: 1%;
}

a.orderItem .orderItem-right ul.orderItem-photo li img {
  width: 100%;
  height: auto;
  margin: 10px;
  box-shadow: 1px 2px 15px #eeeeee;
}
</style>
