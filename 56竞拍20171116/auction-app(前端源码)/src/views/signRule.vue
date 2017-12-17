<template>
  <div class="signRule">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>签到规则</span>
      </div>
      <div class="page-content">
        <div class="signRule-content">
          <ol class="signRule-list">
            <li>
              <p>每天登陆且签到可获得相应积分，连续签到天数越多获得的积分越多。获取规则如下：</p>
              <ul>
                <li v-for=" (srd,$index) in signRuleDay" :key="$index">连续签到
                  <span>{{$index+1}}</span>天，获得
                  <span>{{srd}}</span>积分</li>
                <li>每7天为一个轮回</li>
              </ul>
            </li>
            <li>
              <p>如果中断签到，连续签到记录也将清零</p>
            </li>
            <li>
              <p>本活动最终解释权归56拍卖所有</p>
            </li>
          </ol>
        </div>
  
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'signRule',
  data () {
    return {
      signRuleDay: [
        10,
        10,
        20,
        30,
        30,
        50,
        50
      ]
    }
  },
  components: {
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style>
.signRule-content {
  display: block;
  padding: 0px 10px 10px 30px;
  background-color: #FFFFFF;
}

ol.signRule-list {
  margin: 0;
  padding: 0;
}

ol.signRule-list>li {
  border-bottom: 1px solid #f2f2f2;
  font-size: 14px;
}

ol.signRule-list>li p {
  line-height: 24px;
  padding: 8px;
}

ol.signRule-list>li:nth-child(1) ul {
  margin: -5px 0 10px 0;
  padding: 0 0 0 10px;
}

ol.signRule-list>li:nth-child(1) ul>li {
  font-size: 14px;
  line-height: 24px;
}

ol.signRule-list>li:nth-child(1) ul>li span {
  font-size: 16px;
  margin: 0 5px;
  color: #ef0000;
}
</style>
