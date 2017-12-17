<template>
  <div class="payInfo">
    <img src="static/img/img-jingpai.png" alt="">
    <img src="static/img/img-payInfo.png" alt="">
    <table>
      <tr>
        <td>起拍价</td>
        <td>￥0.00</td>
        <td>加价幅度</td>
        <td>￥{{itemInfo.increase_the_price ? itemInfo.increase_the_price : 0.1}}</td>
      </tr>
      <tr>
        <td>手续费</td>
        <td>{{itemInfo.plus_code ? itemInfo.plus_code : 1}}拍币/1次</td>
        <td>竞拍倒计时</td>
        <td>10s</td>
      </tr>
      <tr>
        <td>差价购买</td>
        <td>敬请期待</td>
        <td>退币比例</td>
        <td>30%</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'payInfo',
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
.payInfo {
  margin-top: 10px;
  background-color: #ffffff;
  display: block;
  border-top: 1px solid #f2f2f2;
  border-bottom: 1px solid #f2f2f2;
  padding: 10px;
}

.payInfo img {
  width: 100%;
}

.payInfo table {
  color: #999999;
  font-size: 14px;
  width: 100%;
}

.payInfo table tr td {
  width: 25%;
  line-height: 32px;
}
</style>
