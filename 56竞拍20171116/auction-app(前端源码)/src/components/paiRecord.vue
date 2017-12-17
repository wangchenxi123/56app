<template>
  <div class="paiRecord">
    <table>
      <tr v-for="(prl, index) in content" :key="prl.price">
        <td>
          <img :src="$api.file.see(prl.avatar)" onerror="this.src='static/icon/icon-appear.svg'" alt="求头像">
        </td>
        <td>{{prl.name}}</td>
        <td>{{index === 0 ? '领先':'出局'}}</td>
        <td>{{prl.address}}</td>
        <td>￥{{prl.price}}</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  props: {
    content: {
      type: Array,
      default: function () {
        return []
      }
    }
  },
  name: 'paiRecord',
  data () {
    return {
      paiRecordList: [
        {
          img: 'static/icon/icon-appear.svg',
          name: 'onolyy一只熊onolyy一只熊onolyy一只熊onolyy一只熊',
          state: '领先',
          area: '内蒙古呼和浩特',
          price: '1425.90'
        },
        {
          img: 'static/icon/icon-appear.svg',
          name: '一只熊',
          state: '出局',
          area: '广东广州',
          price: '1425.90'
        },
        {
          img: 'static/icon/icon-appear.svg',
          name: '熊胖胖',
          state: '出局',
          area: '广东深圳',
          price: '1425.90'
        }
      ]
    }
  },
  computed: {},
  mounted () {
  },
  created () {
  },
  methods: {}
}
</script>

<style scoped>
.paiRecord {
  display: block;
  padding: 10px;
  background-color: #ffffff;
  border-bottom: 1px solid #f2f2f2;
  border-top: 1px solid #f2f2f2;
}

.paiRecord table {
  width: 100%;
  font-size: 12px;
  display: block;
}

.paiRecord table tr {
  border-bottom: 1px solid #f2f2f2;
  white-space: nowrap;
  overflow: hidden;
  display: block;
  line-height: 44px;
  vertical-align: middle;
  width: 100%;
}

.paiRecord table tr td {
  white-space: nowrap;
  overflow: hidden;
  display: inline-block;
  text-overflow: ellipsis;
}

.paiRecord table tr td:nth-child(1),
.paiRecord table tr td:nth-child(3) {
  width: 10%;
}

.paiRecord table tr td:nth-child(2),
.paiRecord table tr td:nth-child(4) {
  width: 25%;
}

.paiRecord table tr td:nth-child(5) {
  width: 30%;
}

.paiRecord table tr:nth-child(1) {
  color: #ef0000;
}

.paiRecord table tr td img {
  width: 32px;
  height: 32px;
  border-radius:50%;
  vertical-align: middle;
}
</style>
