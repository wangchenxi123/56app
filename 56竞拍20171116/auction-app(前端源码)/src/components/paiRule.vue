<template>
  <div class="paiRule">
    <ol class="paiRule-list">
      <li>所有商品竞拍初始价均为0元起，每一次出价会消耗一定数量的拍币，同时商品价格以{{itemInfo.increase_the_price ? itemInfo.increase_the_price : 0.1}}元递增。</li>
      <li>在初始倒计时内即可出价，初始倒计时后进入竞拍倒计时，当您出价后，该商品的计时器将被自动重置，以便其他用户进行出价竞争。如果没有其他用户对该件商品出价，计时器归零时，您便成功拍得了该商品。</li>
      <li>若拍卖成功，请在3天内以成交价购买竞拍商品，超过3天未下单，视为放弃，不返拍币。</li>
      <li>若拍卖失败，将退还所消耗拍币的30%，赠币除外。</li>
    </ol>
  </div>
</template>

<script>
export default {
  name: 'paiRule',
  props: {
    itemInfo: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  data () {
    return {
    }
  },
  computed: {},
  mounted () {
  },
  created () {
  },
  methods: {}
}
</script>

<style scoped>
.paiRule {
  display: block;
  padding: 10px;
  background-color: #ffffff;
  border-bottom: 1px solid #f2f2f2;
  border-top: 1px solid #f2f2f2;
}

ol.paiRule-list {
  margin: 0;
  font-size: 12px;
  color: #333333;
  padding: 0 0 0 20px;
}

ol.paiRule-list li {
  margin: 10px 0;
}
</style>
