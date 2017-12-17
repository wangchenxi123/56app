<template>
  <div class="showOrderDetail">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>晒单详情</span>
      </div>
      <div class="page-content">
  
        <div class="media-list">
          <a class="media-item">
            <div class="media-img">
              <img :src="$api.file.see(resource.head_pic)" alt="">
            </div>
            <div class="media-title">
              <h5>{{resource.username}}</h5>
              <p>{{$filter.date(resource.time)}}</p>
              <div class="circle-red"></div>
            </div>
          </a>
          <a class="media-item">
            <div class="media-img">
              <img src="static/icon/icon-pai-on.svg" alt="">
            </div>
            <div class="media-title">
              <h5>{{resource.itemName}}</h5>
              <div class="circle-red"></div>
            </div>
          </a>
        </div>
        <div class="showOrderDetail-info">
          <h3>{{resource.title}}</h3>
          <p>
            {{resource.context}}
          </p>
          <ul class="photoList">
            <li v-for="p in resource.pictureList" :key="p">
              <img :src="$api.file.see(p)" alt="">
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'showOrderDetail',
  data () {
    return {
      resource: {}
    }
  },
  components: {
  },
  created () {
    let orderId = this.$route.query.orderId
    if (orderId) {
      this.$api.findShowOrders({orderId: orderId, pageNo: 1, pageSize: 1}, data => {
        this.resource = data.resource.content[0]
      }, err => {
        this.$toast({
          message: err.message
        })
      })
    }
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.media-list {
  width: 100%;
  padding: 0;
  margin-bottom: 10px;
  border-top: 1px solid #f2f2f2;
}

a.media-item {
  display: inline-block;
  margin: 0 0 -4px 0;
  padding: 0;
  width: 100%;
  position: relative;
  border-bottom: 1px solid #f2f2f2;
  background-color: #FFFFFF;
}

a.media-item .media-img {
   width:15%;
  float: left;
}

a.media-item .media-img img {
  margin: 10px 0 5px 10px;
  height: 36px;
  width:36px;
  border-radius:50%;
}

.media-title {
  width: 85%;
  float: left;
}

.media-title h5 {
  padding: 14px 10px 5px 10px;
  font-size: 14px;
  font-weight: 500;
  color: #333333;
}

.media-title p {
  padding: 0 30px 10px 10px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  font-size: 12px;
  color: #999999;
}

.showOrderDetail a.media-item:nth-child(2) img {
  background-color: #ffffff;
  padding: 5px 0 5px 5px;
  width: 24px;
  height: 24px;
}

.showOrderDetail-info {
  background-color: #FFFFFF;
  padding: 10px 20px;
  margin-top: -10px;
}

.showOrderDetail-info h3 {
  font-size: 16px;
  color: #333333;
  margin: 10px auto;
}

.showOrderDetail-info p {
  text-indent: 2em;
  text-align: left;
  font-weight: 400;
  font-size: 14px;
  color: #333333;
  margin: 10px auto;
  line-height: 20px;
}

ul.photoList {
  margin: 0;
  padding: 0;
}

ul.photoList li {
  margin-bottom: 20px;
}

ul.photoList li img {
  width: 100%;
  box-shadow: 1px 3px 15px #eeeeee;
}
</style>
