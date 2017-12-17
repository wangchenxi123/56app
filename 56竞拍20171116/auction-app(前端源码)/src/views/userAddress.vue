<template>
  <div class="userAddress">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>收货地址</span>
      </div>
      <mt-loadmore :top-method="loadTop" ref="loadTop" style="padding-top:48px;">
      <div class="page-content" style="padding-top: 0px;" :style="{minHeight: minHeight + 'px'}">
        <div class="address-list" v-for="(al,$index) in resource" :key="al.id">
          <h3>{{al.name}}
            <span>{{al.phone}}</span>
            <!-- <b v-if="al.acquiescence">默认</b> -->
            <label @click.stop="$router.push({name: 'editUserAddress', query: {id: al.id}, params: {obj: al}})">
              <img src="static/icon/icon-edit.svg" alt="">修改</label>
            <label @click.stop="deleteAddr(al)">
              删除</label>
          </h3>
          <h5>{{al.detailed_address}}</h5>
          <p>
            <img src="static/icon/icon-qq.svg" alt="">{{al.penguin}}</p>
          <h4>
            <img src="static/icon/icon-zhifubao.svg" alt="">{{al.alipay}}</h4>
        </div>
        <div v-if="resource.length <= 0">
          <div style="text-align: center;color: #999999;font-size: 14px;padding: 20px;">
            暂无收货地址
          </div>
        </div>
        <div class="address-fade">
          <div class="address-add">
            <router-link tag="p" :to="{name: 'editUserAddress'}">
              <span>+</span>添加地址</router-link>
          </div>
        </div>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
export default {
  name: 'userAddress',
  data () {
    return {
      minHeight: '',
      resource: []
    }
  },
  activated () {
    this.minHeight = window.innerHeight - 123
    this.findAddresss()
  },
  deactivated () {},
  methods: {
    routerBack () {
      this.$router.go(-1)
    },
    findAddresss () {
      this.$api.findAddresss(null, data => {
        this.resource = data.resource
        this.$refs.loadTop.onTopLoaded()
      }, err => {
        this.$refs.loadTop.onTopLoaded()
        if (err.responseCode !== '401') {
          this.$toast({
            message: err.message
          })
        } else {
          this.$router.replace({name: 'login'})
        }
      })
    },
    loadTop () {
      this.findAddresss()
    },
    deleteAddr (row) {
      this.$api.deleteAddress(row.id, data => {
        this.findAddresss()
        this.$toast({
          message: '删除成功'
        })
      }, err => {
        this.findAddresss()
        this.$toast({
          message: err.message
        })
      })
    }
  }
}
</script>

<style scoped>
.address-list {
  background-color: #ffffff;
  padding: 0px 10px 10px 10px;
  margin-top: 20px;
}

.address-list:before {
  content: '';
  height: 6px;
  width: 100%;
  background-image: url('../../static/img/border-letter.svg');
  position: absolute;
  top: 1;
  left: 0;
}

.address-list:after {
  content: '';
  height: 6px;
  width: 100%;
  background-image: url('../../static/img/border-letter.svg');
  position: absolute;
  bottom: 1;
  left: 0;
}

.address-list h3 {
  font-weight: 700;
  font-size: 14px;
  color: #333333;
  line-height: 24px;
  padding-top: 20px;
}

.address-list h3 b {
  font-weight: 400;
  background-color: #ef0000;
  font-size: 12px;
  color: #ffffff;
  padding: 3px 5px;
  margin-right: 10px;
  border-radius: 3px;
}

.address-list h3 span {
  margin-left: 10px;
  margin-right: 10px;
}

.address-list h3 label img {
  width: 16px;
  vertical-align: middle;
  margin-right: 5px;
  margin-left: 10px;
}

.address-list h3 label {
  color: #999999;
  font-weight: 400;
  float: right;
  margin-top: -3px;
  font-size: 12px;
  border-left: 1px solid #eeeeee;
  background-color: #FFFFFF;
  border-radius: 0;
  transition: 0.4s;
}

.address-list h3 label:before {
  background-color: #FFFFFF;
  border-radius: 0;
  transition: 0.4s;
}

.address-list h3 label:active {
  background-color: #eeeeee;
  border-radius: 50%;
  transition: 0.2s;
}

.address-list h5 {
  font-size: 14px;
  line-height: 24px;
  color: #333333;
  font-weight: 400;
}

.address-list h4 {
  padding-bottom: 15px;
  font-size: 14px;
  vertical-align: middle;
  font-weight: 400;
  margin-top: 5px;
}

.address-list h4 img,
.address-list p img {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  vertical-align: middle;
}

.address-list p {
  font-size: 14px;
  vertical-align: middle;
  font-weight: 400;
  float: right;
  margin-top: 5px;
  padding-left: 10px;
}

.address-fade {
  height: 44px;
  position: fixed;
  bottom: 10px;
  left: 0;
  width: 100%;
  display: block;
  text-align: center;
}

.address-add {
  height: 44px;
  background-color: #ef0000;
  display: block;
  width: 60%;
  line-height: 44px;
  color: #ffffff;
  border-radius: 30px;
  box-shadow: 1px 1px 10px #f9b1b1;
  transition: 0.4s;
  margin: 0 auto;
}

.address-add:before {
  background-color: #ef0000;
  transition: 0.4s;
}

.address-add:active {
  background-color: #c90d0d;
  transition: 0.2s;
}

.address-add span {
  background-color: #ffffff;
  color: #ef0000;
  height: 32px;
  width: 32px;
  display: inline-block;
  margin-right: 10px;
  vertical-align: middle;
  border-radius: 50%;
  line-height: 32px;
  font-size: 24px;
}
</style>
