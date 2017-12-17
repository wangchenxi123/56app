<template>
  <div class="tenYuan">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="routerBack">
          <i class="icon-back"></i>
        </a>
        <span>十元专区</span>
      </div>
  
      <div class="page-content">
        <product-grids></product-grids>
      </div>
    </div>
  </div>
</template>

<script>
import productGrids from 'components/productGrids'
export default {
  data () {
    return {}
  },
  methods: {
    routerBack () {
      this.$router.go(-1)
    }
  },
  components: {
    productGrids
  }
}
</script>

<style scoped>

</style>
