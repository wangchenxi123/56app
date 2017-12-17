<template>
  <div class="headline" style="position: relative">
    <img src="static/icon/icon-headline.svg" alt="通知" class="headline-title">
    <div class="headline-content">
      <transition-group name="headline-complete" tag="p" style="position: relative">
        <router-link tag="div" class="headline-list-complete-item" style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" :to="{name: 'goodsDetail', query: {id: m.itemId}}" v-for="(m, index) in $global.HOME_HEAD_LINE" :key="m.key">
          <span>{{m.username}}</span>以
          <span>￥{{m.price}}元</span>拍得{{m.itemName}}
        </router-link>
      </transition-group>
    </div>
    <router-link :to="{name: 'home'}" class="headline-link">
      <img src="static/icon/icon-link.svg" alt="link">
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'headline',
  data () {
    return {
      setIntervaler: ''
    }
  },
  computed: {
    HEAD_LINE: function () {
      return this.$global.HOME_HEAD_LINE
    }
  },
  watch: {
    '$global.HOME_HEAD_LINE': function (val, oldVal) {
      if (val.length > 2 && !this.$global.HEAD_LINE_INTERVALER) {
        this.setInterval()
      }
    }
  },
  created () {
    if (this.$global.HOME_HEAD_LINE.length > 2 && !this.$global.HEAD_LINE_INTERVALER) {
      this.setInterval()
    }
  },
  destroyed () {
    window.clearInterval(this.$global.HEAD_LINE_INTERVALER)
    this.$global.HEAD_LINE_INTERVALER = ''
  },
  methods: {
    setInterval () {
      window.clearInterval(this.$global.HEAD_LINE_INTERVALER)
      this.$global.HEAD_LINE_INTERVALER = window.setInterval(() => {
        let m = this.$global.HOME_HEAD_LINE.splice(0, 1)[0]
        window.setTimeout(() => {
          this.$global.HOME_HEAD_LINE.push(m)
        }, 700)
      }, 2700)
    }
  }
}
</script>

<style scoped>
.headline {
  border-bottom: 1px solid #fbfbfb;
  border-top: 1px solid #fbfbfb;
  padding-left: 10px;
  padding-right: 0px;
  background-color: #FFFFFF;
}

.headline img.headline-title {
  width: 20%;
  display: inline-block;
  max-width: 70px;
  min-width: 50px;
  height: 14px;
  padding: 11px 0 11px;
}

.headline .headline-content {
  font-size: 12px;
  color: #999999;
  height: 36px;
  line-height: 36px;
  width: 72%;
  display: inline-block;
  overflow-y: hidden;
  padding: 3px 0 0;
}

.headline .headline-content span {
  color: #F08000;
}

.headline-link {
  position: absolute;
  right: 0px;
  text-align: center;
  height: 36px;
  line-height: 36px;
  margin: 0;
  padding: 6px 10px 0 10px;
  float: right;
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}

.headline-link img {
  height: 16px;
  width: 10px;
  padding: 8px 0 0;
}

.headline-list-complete-item {
  transition: all .7s;
  /* display: inline-block; */
}
.headline-complete-leave-to
/* .list-complete-leave-active for below version 2.1.8 */ {
  opacity: 0;
  transform: translateY(-20px);
}
.headline-complete-leave-active {
  position: absolute;
  width: 100%;
  overflow: hidden;
  text-overflow:ellipsis;
  white-space: nowrap;
}
</style>
