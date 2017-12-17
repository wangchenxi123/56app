<template>
  <div class="allGoods">
    <div class="page-wrap">
      <div class="page-title">全部商品</div>
      <div class="page-content">
        <div class="sort-nav">
          <ul class="sort-nav-list" :style="{height:screenHeight+'px'}">
            <li v-for="(sort,$index) in sortResource.content" :class="{active: sort.id === selectSortId}" :key="sort.id" @click="selSort(sort)">{{sort.name}}</li>
          </ul>
        </div>
        <mt-loadmore :top-method="loadTop" ref="loadTop" >
          <div class="sort-content" :style="{width:screenWidth+'px',height:screenHeight+'px'}">
            <ul class="sort-content-list">
              <li v-for="(cl,key) in items" :key="key" @click="goto(cl)">
                <div class="sort-content-img" style="display: inline-block; float:none;width: 25%;">
                  <img :src="$api.file.see(cl.itemBean.small_picture)" alt="产品图"  onerror="this.src='static/img/product/img-car.png'" >
                  <i class="icon-finish" v-if="cl.settlement"></i>
                </div>
                <div class="sort-content-list-info" style="">
                  <h5>{{cl.itemBean.name}}</h5>
                  <p>{{cl.tag}}</p>
                  <span :class="{active: cl.active}">￥{{cl.price}}</span>
                  <mt-button v-if="cl.interrupted" type="default" size="small">休眠中...</mt-button>
                  <mt-button v-else :type="cl.settlement?'default':'danger'" size="small">{{cl.settlement?'去下一期':'参与竞拍'}}</mt-button>
                </div>
              </li>
            </ul>
          </div>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>

<script>
  var scrollTop = 0
  var list = ['goodsDetail', 'goodsHistoryDetail']
  export default {
    name: 'allGoods',
    data () {
      return {
        screenWidth: (document.body.clientWidth - 90),
        screenHeight: (document.body.clientHeight - 100),
        selectSortId: 0,
        querySort: {
          pageNo: 1,
          pageSize: 20
        },
        sortResource: {
          content: [],
          last: false
        },
        resource: {
          items: []
        },
        items: {}
      }
    },
    watch: {
      '$global.WSMessage': function (val, oldVal) {
        let itemId = val.itemId
        if (val.message_type === 'BIDDER') {
          if (this.items && this.items.hasOwnProperty(itemId) && !this.items[itemId].settlement && !this.items[itemId].interrupted) {
            this.$set(this.items[itemId], 'price', val.price)
            this.$set(this.items[itemId], 'active', true)
            window.setTimeout(() => {
              if (this.items.hasOwnProperty(itemId)) {
                this.$set(this.items[itemId], 'active', false)
              }
            }, 1000)
          }
        } else if (val.message_type === 'HEADLINES' && val.userId) {
          if (this.items.hasOwnProperty(itemId)) {
            this.$set(this.items[itemId], 'settlement', true)
          }
        } else if (val.message_type === 'SYSTEM_SLEEP') {
          this.$set(this.items[itemId], 'interrupted', true)
        } else {
          if (this.items.hasOwnProperty(itemId) && !this.items[itemId].interrupted) {
            this.$set(this.items[itemId], 'active', true)
            window.setTimeout(() => {
              if (this.items.hasOwnProperty(itemId)) {
                this.$set(this.items[itemId], 'active', false)
              }
            }, 1000)
          }
        }
      },
      screenWidth (valWidth) {
        if (!this.timer) {
          this.screenWidth = valWidth
          this.timer = true
          let that = this
          setTimeout(function () {
            that.timer = false
          }, 1000)
        }
      }
    },
    created () {
    },
    beforeRouteEnter (to, from, next) {
      if (list.indexOf(from.name) === -1) {
        document.body.scrollTop = 0
        scrollTop = 0
      } else {
        document.body.scrollTop = scrollTop
      }
      next(vm => {
        if (list.indexOf(from.name) === -1) {
          vm.findSorts()
        }
      })
    },
    beforeRouteLeave (to, from, next) {
      scrollTop = document.body.scrollTop
      next()
    },
    activated () {
    },
    deactivated () {
    },
    mounted () {
      const that = this
      window.onresize = () => {
        return (() => {
          window.screenWidth = document.body.clientWidth
          that.screenWidth = window.screenWidth - 90
        })()
      }
    },

    methods: {
      goto (row) {
        if (this.items[row.itemBean.id].settlement) {
          this.$router.push({name: 'goodsHistoryDetail', query: {record_id: row.id}})
        } else {
          this.$router.push({name: 'goodsDetail', query: {id: row.itemBean.id}})
        }
      },
      findSorts () {
        this.$api.findSorts(this.querySort, data => {
          if (this.querySort.pageNo === 1 && data.resource.content[0]) {
            this.selectSortId = data.resource.content[0].id
          }
          this.sortResource = data.resource
          if (this.selectSortId !== 0) {
            this.findItems()
          }
        }, err => {
          this.$toast({
            message: err.message,
            duration: 3000
          })
        })
      },
      findItems () {
        this.$api.findItemsBySortId(this.selectSortId, data => {
          this.items = {}
          if (data.resource.items) {
            data.resource.items.forEach(item => {
              if (!this.items.hasOwnProperty(item.itemBean.id)) {
                this.$set(this.items, item.itemBean.id, item)
              }
            })
          }
          this.resource = data.resource
          this.$refs.loadTop.onTopLoaded()
        }, err => {
          this.$refs.loadTop.onTopLoaded()
          this.$toast({
            message: err.message,
            duration: 3000
          })
        })
      },
      selSort (row) {
        if (this.selectSortId !== row.id) {
          this.selectSortId = row.id
          this.findItems()
        }
      },
      loadTop () {
        if (this.selectSortId === 0) {
          this.findSorts()
          this.$refs.loadTop.onTopLoaded()
          return
        }
        this.loadType = 'REFRESH'
        this.findItems()
      }
    }
  }
</script>

<style scoped>
  /* danger button style*/

  .mint-button--small {
    font-size: 12px;
    padding: 0px 5px !important;
    height: 28px;
    margin: 0;
  }

  .mint-button--danger {
    background-color: #ef0000;
    margin: 0;
  }

  .page-title {
    height: 48px;
    line-height: 48px;
    text-align: center;
    color: #FFFFFF;
    background-color: #ef0000;
    font-size: 18px;
    font-weight: 700;
    position: fixed;
    width: 100%;
    z-index: 4;
  }

  .allGoods .page-content {
    padding-top: 48px;
    padding-bottom: 40px;
    height: 100%;
  }

  .allGoods {
    height: 100%;
    background-color: #fbfbfb;
  }

  .sort-nav {
    float: left;
    display: inline-block;
    margin: 0;
    padding: 0;
    width: 30%;
    height: 100%;
    background: #F2F2F2;
  }

  ul.sort-nav-list {
    margin: 0;
    padding: 0;
    height: 100% !important;
  }

  ul.sort-nav-list li {
    text-align: center;
    font-size: 20px;
    line-height: 44px;
    border-bottom: 1px solid #f2f2f2;
    color: #999999;
    position: relative;
    border-right: 1px solid #f2f2f2;
    font-weight: 600;
  }

  ul.sort-nav-list li.active {
    background-color: #ffffff;
    color: #000000;
    color: #EF0000;
    border-right: 1px solid #ffffff;
    transition: 0.4s;
  }

  ul.sort-nav-list li.active:active {
    background-color: #eeeeee;
    border-top-right-radius: 50%;
    border-bottom-right-radius: 50%;
    transition: 0.2s;
  }

  .sort-content {
    display: inline-block;
    background-color: #ffffff;
    overflow-y: scroll;
  }

  ul.sort-content-list {
    margin: 0;
    padding-left: 10px;
  }

  ul.sort-content-list li {
    border-bottom: 1px solid #f2f2f2;
    display: block;
    width: 100%;
    padding: 10px 0;
    transition: 0.4s;
    background-color: #ffffff;
  }

  ul.sort-content-list li:bofore {
    transition: 0.4s;
    background-color: #ffffff;
  }

  ul.sort-content-list li:active {
    transition: 0.2s;
    background-color: #eeeeee;
    transition: 0.2s;
    border-radius: 50%;
  }

  ul.sort-content-list li .sort-content-img {
    position: relative;
    z-index: 1;
    width: 30% !important;
    max-width: 90px;
    float: left;
    margin-left: 5%;
    display: block;
  }

  ul.sort-content-list li img {
    width: 100%;
  }

  ul.sort-content-list li .sort-content-list-info {
    display: block;
    width: 65%;
    float: right;
  }

  ul.sort-content-list li .sort-content-list-info h5 {
    font-size: 16px;
    white-space: nowrap;
    word-wrap: normal;
    text-overflow: ellipsis;
    overflow: hidden;
    margin: 3px 10px 0 0;
    font-weight: 500;
    padding-left: 10px;
    color: #333333;
  }

  ul.sort-content-list li .sort-content-list-info p {
    font-size: 14px;
    white-space: nowrap;
    word-wrap: normal;
    text-overflow: ellipsis;
    overflow: hidden;
    margin: 0 0 6px 0;
    font-weight: 600;
    padding-left: 10px;
    color: #333333;
  }

  ul.sort-content-list li .sort-content-list-info span {
    color: #ef0000;
    font-size: 16px;
    font-weight: 600;
    margin-left: 10px;
    float: left;
    display: inline-block;
    line-height: 28px;
  }

  ul.sort-content-list li .sort-content-list-info button {
    display:inline-block;
    float: left;
    margin-left: 10px;
  }

  span.active {
    border-radius: 30px;
    animation: priceAcitve 1s infinite ease;
    -moz-animation: priceAcitve 1s infinite ease;
    /* Firefox */
    -webkit-animation: priceAcitve 1s infinite ease;
    /* Safari and Chrome */
    -o-animation: priceAcitve 1s infinite ease;
    /* Opera */
  }

  /* 价格背景脉动动画 */
  @keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-moz-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-webkit-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }

  @-o-keyframes priceAcitve {
    from {
      background-color: #F9B1B1;
    }
    to {
      background0color: #FFFFFF;
    }
  }
</style>
