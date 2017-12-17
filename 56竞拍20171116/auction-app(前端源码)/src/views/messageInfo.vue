<template>
  <div class="messageInfo">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>公告消息</span>
      </div>
      <div class="page-content">
        <div class="messageInfo-content">
          <h3>{{notice.title}}</h3>
          <p>{{$filter.date(notice.time)}}</p>
          <article>
            {{notice.context}}
          </article>
        </div>
      </div>
    </div>
  </div>
</template>

<script scoped>
export default {
  name: 'messageInfo',
  data () {
    return {
      notice: {}
    }
  },
  created () {
    let id = this.$route.query.id
    if (id) {
      this.$api.getNotice(id, data => {
        this.notice = data.resource
        this.$api.read(id, data => {
          this.notice.state = 'READ'
        }, err => {
          this.$toast({
            message: err.message
          })
        })
      }, err => {
        this.$router.go(-1)
        this.$toast({
          message: err.message
        })
      })
    } else {
      this.$router.go(-1)
    }
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style>
.messageInfo-content {
  text-align: center;
  display: block;
  padding: 10px;
  height: 100%;
}

.messageInfo-content h3 {
  font-size: 18px;
  font-weight: 600;
}

.messageInfo-content p {
  font-size: 14px;
  color: #999999;
  margin: 10px auto;
}

.messageInfo-content article {
  text-align: left;
  text-indent: 2em;
  margin-bottom: 10px;
  font-size: 14px;
  color: #333333;
}
</style>
