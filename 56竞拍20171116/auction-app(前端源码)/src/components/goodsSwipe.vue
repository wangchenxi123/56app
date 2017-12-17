<template>
  <div id="swipe" class="swipe">
    <mt-swipe :show-indicators="true" class="yhq-swipe" :style="{height: goodsHeight + 'px'}" ref="swipe">
      <mt-swipe-item v-if="history|| isFinish">
        <img :src="$api.file.see(imgs[0])" alt="图片" ref="swipeImg">
        <i class="icon-finish" style="background-size: 50%;"></i>
      </mt-swipe-item>
      <mt-swipe-item v-else v-for="img in imgs" :key="img">
        <img :src="$api.file.see(img)" alt="图片" ref="swipeImg">
      </mt-swipe-item>
    </mt-swipe>
  </div>
</template>

<script>
export default {
  name: 'goodsSwipe',
  props: {
    imgs: {
      type: Array,
      default: function () {
        return []
      }
    },
    history: {
      type: Boolean,
      default: function () {
        return false
      }
    },
    isFinish: {
      type: Boolean,
      default: function () {
        return false
      }
    }
  },
  data () {
    return {
      goodsHeight: (document.body.clientWidth * 0.5),
      items: []
    }
  },
  computed: {},
  mounted () {
    // console.log(this.$refs['swipeImg'][0].height)
  },
  created () {
  },
  methods: {}
}
</script>

<style scoped>
.swipe {
  position: relative;
  z-index: 1;
}

.yhq-swipe img {
  width: 100%;
  height: 100%;
}



/*@media screen and (min-width: 1200px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 720px;
      height: auto;
    }
  }
  
  @media screen and (min-width: 960px) and (max-width: 1199px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 720px;
      height: auto;
    }
  }
  
  @media screen and (min-width: 768px) and (max-width: 959px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 720px;
      height: auto;
    }
  }
  
  @media only screen and (min-width: 480px) and (max-width: 767px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 720px;
      height: auto;
    }
  }
  
  @media only screen and (min-width: 321px) and (max-width: 479px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 100%;
      height: auto;
    }
  }
  
  @media only screen and (max-width: 320px) {
    .yhq-swipe {
      height: 360px;
      text-align: center;
    }
    .yhq-swipe img {
      width: 100%;
      height: auto;
    }
  }*/
</style>
