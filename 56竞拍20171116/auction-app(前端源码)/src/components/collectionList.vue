<template>
  <div class="collectionList">
    <ul class="collection-list">
      <li v-for="(cl, index) in content" :key="cl.id">
        <div class="goods-img">
          <img :src="$api.file.see(cl.small_picture)" alt="">
        </div>
        <div class="goods-info">
          <h5>{{cl.name}}</h5>
          <br>
          <p>市场价格:
            <span>￥{{cl.price}}</span>
          </p>
          <!-- <p>开展期数: {{cl.timeLine}} 期</p> -->
          <label @click="isCollect(cl, index)">
            <i class="icon-collectionGroup icon-collection active"></i>
            <b>收藏</b>
          </label>
          <mt-button type="danger" size="small" @click="$router.push({name: 'goodsDetail', query: {id: cl.id}})">前往下一期</mt-button>
        </div>
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
  name: 'collectionList',
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
      collection: [
        {
          img: 'static/img/product/img-computer.png',
          title: 'Apple Macbook Pro 256GB商务超轻便',
          price: '6668.00',
          timeLine: '867',
          isActive: true
        },
        {
          img: 'static/img/product/img-car.png',
          title: 'Apple Macbook Pro 256GB商务超轻便',
          price: '6668.00',
          timeLine: '867',
          isActive: true
        },
        {
          img: 'static/img/product/img-camera.png',
          title: 'Apple Macbook Pro 256GB商务超轻便',
          price: '6668.00',
          timeLine: '867',
          isActive: true
        },
        {
          img: 'static/img/product/img-computer.png',
          title: 'Apple Macbook Pro 256GB商务超轻便',
          price: '6668.00',
          timeLine: '867',
          isActive: true
        }
      ]
    }
  },
  methods: {
    isCollect (row, index) {
      this.$api.deleteCollection(row.collectionId, data => {
        this.content.splice(index, 1)
        this.$toast({
          message: '取消收藏'
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

<style scoped>
.mint-button--small {
  font-size: 12px;
  padding: 0px 5px !important;
  height: 28px;
  margin: 0;
}

ul.collection-list .goods-info .mint-button--danger {
  background-color: #ef0000;
  margin: 10px 0 10px 10px;
}

.collectionList {
  background-color: #ffffff;
  padding: 10px 10px 0 10px;
}

ul.collection-list {
  margin: 0;
  padding: 0;
}

ul.collection-list li {
  display: inline-block;
  border-bottom: 1px solid #f2f2f2;
  white-space: nowrap;
  overflow: hidden;
  display: block;
  text-overflow: ellipsis;
  padding-top: 10px;
}

ul.collection-list .goods-img {
  width: 30%;
  float: left;
}

ul.collection-list .goods-img img {
  width: 100%;
  height: auto;
}

ul.collection-list .goods-info {
  width: 70%;
  float: left;
}

ul.collection-list .goods-info h5 {
  font-weight: 600;
  color: #33333;
  margin-left: 10px;
  margin-bottom: 3px;
  font-size: 14px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  display: inline-block;
}

ul.collection-list .goods-info p {
  color: #333333;
  margin-left: 10px;
  font-size: 14px;
  line-height: 20px;
}

ul.collection-list .goods-info p span {
  color: #ef0000;
}

ul.collection-list .goods-info label {
  border-right: 1px solid #f2f2f2;
  padding-right: 10px;
  margin-left: 10px;
}

ul.collection-list .goods-info label b {
  font-size: 12px;
  font-weight: 400;
  color: #999999;
  vertical-align: middle;
  margin-left: 3px;
}

.icon-collectionGroup {
  display: inline-block;
  background-image: url("../../static/icon/icon-collection-group.svg");
  background-position: 0;
  background-repeat: no-repeat;
  background-size: 40px;
  width: 20px;
  height: 20px;
  vertical-align: middle;
}

.icon-collection.active {
  background-position: -21px;
}
</style>
