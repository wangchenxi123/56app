<template>
  <div class="page-tabbar" v-show="isShow">
    <mt-tabbar v-model="selected" fixed>
      <mt-tab-item id="home" class="page-tabbar-item">
        <i slot="icon" class="icon-tabbar icon-home" :class="{active: isActive === 'home'}"></i> 主页
      </mt-tab-item>
      <mt-tab-item id="newDeal" class="page-tabbar-item">
        <i slot="icon" class="icon-tabbar icon-pai" :class="{active: isActive === 'newDeal'}"></i> 最新成交
      </mt-tab-item>
      <mt-tab-item id="allGoods" class="page-tabbar-item">
        <i slot="icon" class="icon-tabbar icon-allpai" :class="{active: isActive === 'allGoods'}"></i> 全部商品
      </mt-tab-item>
      <mt-tab-item id="userCenter" class="page-tabbar-item">
        <i slot="icon" class="icon-tabbar icon-user" :class="{active: isActive === 'userCenter'}"></i> 个人中心
      </mt-tab-item>
    </mt-tabbar>
  </div>
</template>

<script>
export default {
  name: 'page-tabbar',
  data () {
    return {
      selected: 'home',
      isActive: '',
      isShow: '',
      SUPPORT: ['home', 'userCenter', 'allGoods', 'newDeal']
    }
  },
  created () {
  },
  methods: {
  },
  watch: {
    $route: function (val, oldValue) {
      this.isActive = val.name
      this.selected = val.name
      if (this.SUPPORT.indexOf(val.name) !== -1) {
        this.isShow = true
      } else {
        this.isShow = false
      }
    },
    selected (val, oldValue) {
      if (this.SUPPORT.indexOf(val) !== -1) {
        this.$router.push({name: val})
      }
    }
  }
}
</script>

<style scoped>
.icon-tabbar {
	background: url("../../static/icon/icon-tabbar.svg") no-repeat;
	background-size: 236px;
}
/*默认状态*/

.icon-tabbar.icon-home {
	background-position: 0px 1px;
}

.icon-tabbar.icon-pai {
	background-position: -66px 2px;
}

.icon-tabbar.icon-allpai {
	background-position: -140px 2px;
}

.icon-tabbar.icon-user {
	background-position: -213px 2px;
}
/*选择状态*/

.icon-tabbar.icon-home.active {
	background-position: -1px -38px;
}

.icon-tabbar.icon-pai.active {
	background-position: -65px -38px;
}

.icon-tabbar.icon-allpai.active {
	background-position: -140px -38px;
}

.icon-tabbar.icon-user.active {
	background-position: -213px -38px;
}

.page-tabbar {
  overflow: hidden;
}

.page-wrap {
  overflow: auto;
  height: 100%;
  padding-bottom: 100px;
}

.mint-tabbar {
  border-top: 1px solid #ebebeb;
  background-color: #ffffff;
  color: #999999;
}

.mint-tab-item {
  padding: 5px 0;
}

.mint-tabbar>.mint-tab-item.is-selected {
  color: #ef0000;
  background-color: rgba(0, 0, 0, 0);
}
</style>
