<template>
  <div id="app">
      <router-view></router-view>
      <auction-tabbar></auction-tabbar>
  </div>
</template>

<script>
  import auctionTabbar from 'components/tabbar'
  export default {
    name: 'app',
    components: {
      auctionTabbar
    },
    data () {
      return {
      }
    },
    computed: {
    },
    created () {
      window.document.title = this.$const.TITLE
      // this.$global.initWebSocket()
      // this.$global.loginByToken()
    },
    methods: {
    }
  }
</script>

<style>
*{
  -webkit-touch-callout:none;  /*系统默认菜单被禁用*/     
    -webkit-user-select:none; /*webkit浏览器*/     
    -khtml-user-select:none; /*早期浏览器*/     
    -moz-user-select:none;/*火狐*/     
    -ms-user-select:none; /*IE10*/     
    user-select:none;  
}
input{
   -webkit-user-select:auto; /*webkit浏览器*/ 
}
  html, body {
    padding: 0;
    margin: 0;
    height: 100%;
  }
  h1,h2,h3,h4,h5,p{
    margin:0;
    padding:0;
  }
  #app {
    position: relative;
    height:100%;
  }
  
  .mint-cell-title{
  font-size: 14px;
  margin-top: 3px;
}
.mint-cell-value{
  font-size: 12px;
   margin-top: 3px;
}
/* danger button style*/
.mint-button--small{
  font-size: 12px;
  padding:0px 5px !important;
  height:28px;
  margin:0;
}
.mint-button--danger{
  background-color:#ef0000;
  margin:0;
}
.mint-tabbar.is-fixed{
  z-index: 4 !important;
}
.icon-back{
  background-image: url('../static/icon/icon-back.svg');
  display: inline-block;
  height:16px;
  width:16px;
  background-position: 0;
  background-size:16px;
  vertical-align: middle;
}
.icon-back-gray{
  background-image: url('../static/icon/icon-back-gray.svg');
  display: inline-block;
  height:16px;
  width:16px;
  background-position: 0;
  background-size:16px;
  vertical-align: middle;
}
.icon-share{
  background-image: url('../static/icon/icon-share.svg');
  display: inline-block;
  height:20px;
  width:20px;
  background-position: 0;
  background-size:20px;
  vertical-align: middle;
}
.product-img{
  position: relative;
  z-index: 1;
}
.icon-finish{
   background-image: url('../static/icon/icon-finish.svg');
  display: inline-block;
  background-repeat: no-repeat;
  height:75%;
  width:75%;
  top:10%;
  right:0;
  background-position: 0;
  background-size:75%;
  position: absolute;
  z-index: 3;
}
 .page-title{
  height:48px;
  line-height: 48px;
  text-align: center;
  color:#FFFFFF;
  background-color:#ef0000;
  font-size: 18px;
  font-weight: 700;
  position: fixed;
  width: 100%;
  z-index: 4;
}
.page-content{
  padding-top: 48px;
  padding-bottom: 55px;
}
.page-title span{
  margin-left: -48px;
}
.page-title a{
  background-color:#EF0000;
  padding-left:15px;
  padding-right:15px;
  float: left;
  transition: 0.2s;
  border-radius: 50%;
}
.page-title a.icon-right {
  text-align: center;
  float: right;
  transition: 0.4s;
  border-radius: 50%;
}
.page-title a:before{
  background-color:#ef0000;
  transition: 0.4s;
}
.page-title a:active{
  background-color: #c90d0d;
  transition: 0.2s;
  border-radius: 0px;
border-radius: 0;
}
</style>
