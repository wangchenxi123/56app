<template>
  <div id="swipe" class="swipe">
    <mt-swipe :show-indicators="false" class="yhq-swipe">
      <mt-swipe-item v-for="item in resource.content" :key="item.id">
        <a @click="goto(item.url)">
          <img :src="$api.file.see(item.pictureId)" alt="图片">
        </a>
      </mt-swipe-item>
    </mt-swipe>
  </div>
</template>

<script>
export default {
  name: 'swipe',
  data () {
    return {
      query: {
        pageNo: 1,
        pageSize: 5
      },
      resource: {
        content: []
      }
    }
  },
  created () {
    this.findCarousels()
  },
  methods: {
    findCarousels () {
      this.$api.findCarousels(this.query, data => {
        this.resource = data.resource
      }, err => {
        console.log(err)
      })
    },
    goto (url) {
      location.href = url
    }
  }
}
</script>

<style scoped>
.swipe {
  position: relative;
  z-index: 1;
}

@media screen and (min-width: 1200px) {
  .yhq-swipe {
    height: 234px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 750px;
    height: auto;
  }
}

@media screen and (min-width: 960px) and (max-width: 1199px) {
  .yhq-swipe {
    height: 234px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 750px;
    height: auto;
  }
}

@media screen and (min-width: 768px) and (max-width: 959px) {
  .yhq-swipe {
    height: 234px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 100%;
    height: auto;
  }
}

@media only screen and (min-width: 480px) and (max-width: 767px) {
  .yhq-swipe {
    height: 234px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 750px;
    height: auto;
  }
}

@media only screen and (min-width: 321px) and (max-width: 479px) {
  .yhq-swipe {
    height: 140px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 100%;
    height: auto;
  }
}

@media only screen and (max-width: 320px) {
  .yhq-swipe {
    height: 115px;
    text-align: center;
  }
  .yhq-swipe img {
    width: 100%;
    height: auto;
  }
}
</style>
