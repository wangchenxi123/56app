<template>
  <div class="activity">
    <ul class="act-list" style="text-align: center;">
      <li v-for="(act, index) in resource" :key="act.id">
        <a @click="goto(act.url)">
          <img class="act-icon" :src="$api.file.see(act.picture)">
          <div class="act-title">{{act.name}}</div>
        </a>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'activity',
  data () {
    return {
      activity: [],
      resource: {}
    }
  },
  created () {
    this.findActivities()
  },
  methods: {
    findActivities () {
      this.$api.findActivities({}, data => {
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

<style>
.activity {
  background-color: #FFFFFF;
  margin-top: -5px;
}

ul.act-list {
  white-space: nowrap;
  margin: 0;
  padding: 0;
  overflow-x: scroll;
}

ul.act-list li {
  list-style: none;
  width: 25%;
  text-align: center;
  display: inline-block;
}

ul.act-list li a {
  padding: 10px 0;
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  font-size: 12px;
  text-align: center;
  outline: none;
  display: block;
  color: #333333;
  transition: 0.4s;
}

ul.act-list li a:before {
  color: #666666;
  background-color: #FFFFFF;
  transition: 0.4s;
}

ul.act-list li a:after,
ul.act-list li a:active {
  color: #000000;
  background-color: #f2f2f2;
  transition: 0.2s;
  border-radius: 50%;
}

ul.act-list li a img {
  width: 44px;
  height: 44px;
  margin: 0 auto;
}

.act-title {
  margin-top: 5px;
}
</style>
