<template>
  <div class="showOrderDetail">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>添加晒单</span>
      </div>
      <div class="page-content">
        <div class="media-list">
          <!-- <a class="media-item">
            <div class="media-img">
              <img src="static/icon/icon-appear.svg" alt="">
            </div>
            <div class="media-title">
              <h5>{{resource.name}}</h5>
              <p>{{$filter.date(resource.time)}}</p>
              <div class="circle-red"></div>
            </div>
          </a> -->
          <a class="media-item">
            <div class="media-img">
              <img src="static/icon/icon-pai-on.svg" alt="">
            </div>
            <div class="media-title">
              <h5>{{resource.item_name}}</h5>
              <div class="circle-red"></div>
            </div>
          </a>
        </div>
        <mt-field label="标题" :attr="{ maxlength: 30 }" placeholder="输入晒单标题" v-model="addItem.title"></mt-field>
        <mt-field label="内容" :attr="{ maxlength: 100 }" type="textarea" rows="3" placeholder="输入内容" v-model="addItem.context"></mt-field>
        <mt-cell>
          <div slot="title">图片</div>
          <input type="file" multiple ref="uploadInput" style="display: none" @change="upload($event)">
          <mt-button size="small" type="danger" @click="$refs.uploadInput.click()" :loading="uploading" v-if="addItem.pictureList.length < 3">上传图片
          </mt-button>
        </mt-cell>
        <mt-cell v-for="(img, index) in addItem.pictureList" :key="img">
          <div slot="title" style="width: 40%;max-width: 200px;display: inline-block;margin-top: 20px;">
            <img style="width: 100%;height: 100%;" :src="$api.file.see(img)" onerror="this.src='static/icon/icon-appear.svg'" alt="">
          </div>
          <mt-button size="small" type="danger" @click="addItem.pictureList.splice(index, 1)">删除
          </mt-button>
        </mt-cell>
      </div>
      <mt-button style="width: 80%;margin:auto;display: block;" :loading="submitting" size="small" type="danger"  @click="submit">提交
      </mt-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'addShowOrder',
  data () {
    return {
      addItem: {
        title: '',
        itemId: '',
        context: '',
        pictureList: [],
        grade: 5,
        orderId: ''
      },
      uploading: false,
      submitting: false,
      resource: {},
      photo: []
    }
  },
  components: {
  },
  created () {
    let orderId = this.$route.query.orderId
    if (orderId) {
      this.$api.getOrder(orderId, data => {
        this.resource = data.resource
        this.addItem.orderId = data.resource.id
      }, err => {
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
    },
    upload (e) {
      let files = []
      for (let i = 0; i < e.target.files.length; i++) {
        files.push(e.target.files.item(i))
      }
      // 清空上传文件域
      e.target.value = null
      files.map((file) => {
        if (file) {
          if (!/image\/\w+/.test(file.type)) {
            this.$toast({
              message: '只能上传图片'
            })
            return
          }
          if (file.size >= 5000000) {
            this.$toast({
              message: '图片不能大于5M'
            })
            return
          }
          let snedData = new FormData()
          snedData.append('file', file)
          snedData.append('type', 'IMAGE')
          this.uploading = true
          this.$api.uploadImage(snedData, (data) => {
            this.addItem.pictureList.push(data.file.id)
            this.uploading = false
          }, (e) => {
            this.$toast({
              message: e.message
            })
            this.uploading = false
          })
        }
      })
    },
    submit () {
      if (!this.addItem.title.trim()) {
        this.toast('请输入标题')
        return
      }
      if (!this.addItem.context.trim()) {
        this.toast('请输入内容')
        return
      }
      this.submitting = true
      this.$api.addShowOrder(this.addItem, data => {
        this.toast('提交成功')
        this.submitting = false
        this.$emit('addShowOrder', true)
        this.$router.go(-1)
      }, err => {
        this.submitting = false
        this.toast(err.message)
      })
    },
    toast (message) {
      if (this.toastClient) {
        this.toastClient.close()
      }
      this.toastClient = this.$toast({
        message: message,
        position: 'middle',
        duration: 2000
      })
    }
  }
}
</script>

<style scoped>
.media-list {
  width: 100%;
  padding: 0;
  margin-bottom: 10px;
  border-top: 1px solid #f2f2f2;
}

a.media-item {
  display: inline-block;
  margin: 0 0 -4px 0;
  padding: 0;
  width: 100%;
  position: relative;
  border-bottom: 1px solid #f2f2f2;
  background-color: #FFFFFF;
}

a.media-item .media-img {
  width: 15%;
  float: left;
}

a.media-item .media-img img {
  margin: 10px 0 5px 10px;
  min-width: 24px;
  max-width: 56px;
}

.media-title {
  width: 85%;
  float: left;
}

.media-title h5 {
  padding: 14px 10px 5px 10px;
  font-size: 14px;
  font-weight: 500;
  color: #333333;
}

.media-title p {
  padding: 0 30px 10px 10px;
  white-space: nowrap;
  word-wrap: normal;
  text-overflow: ellipsis;
  overflow: hidden;
  font-size: 12px;
  color: #999999;
}

.showOrderDetail a.media-item:nth-child(2) img {
  background-color: #ffffff;
  padding: 0px 0 0 10px;
  width: 24px;
  height: 24px;
}

.showOrderDetail-info {
  background-color: #FFFFFF;
  padding: 10px 20px;
  margin-top: -10px;
}

.showOrderDetail-info h3 {
  font-size: 16px;
  color: #333333;
  margin: 10px auto;
}

.showOrderDetail-info p {
  text-indent: 2em;
  text-align: left;
  font-weight: 400;
  font-size: 14px;
  color: #333333;
  margin: 10px auto;
  line-height: 20px;
}

ul.photoList {
  margin: 0;
  padding: 0;
}

ul.photoList li {
  margin-bottom: 20px;
}

ul.photoList li img {
  width: 100%;
  box-shadow: 1px 3px 15px #eeeeee;
}

input.fileInput {
  background-color: rgba(0, 0, 0, 0);
  opacity: 0;
  position: absolute;
  width: 100%;
  height: 48px;
  left: 0;
  z-index: 3;
}
</style>
