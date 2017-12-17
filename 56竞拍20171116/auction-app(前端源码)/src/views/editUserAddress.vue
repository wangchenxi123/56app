<template>
  <div class="editUserAddress">
    <div class="page-wrap">
      <div class="page-title">
        <a @click="$router.go(-1)">
          <i class="icon-back"></i>
        </a>
        <span>编辑收货地址</span>
      </div>
      <div class="page-content">
        <mt-field label="收货人" placeholder="请输入收货人" v-model="addr.name"></mt-field>
        <mt-field label="手机" placeholder="请输入手机号" type="tel" v-model="addr.phone"></mt-field>
        <div @click="showPopup()">
          <mt-field label="地区" placeholder="点击选择地区" v-model="addr.address" :readonly="true" :disabled="true"></mt-field>
        </div>
        <mt-field label="详细地址" placeholder="请输入详细地址" v-model="addr.detailed_address"></mt-field>
        <mt-field label="QQ号码" placeholder="请输入QQ号码(选填)" v-model="addr.penguin"></mt-field>
        <mt-field label="支付宝账号" placeholder="请输入支付宝账号(选填)" v-model="addr.alipay"></mt-field>
        <!-- <div style="padding: 0 10px;">
          <span style="font-size: 14px;width:105px;display:inline-block;">是否默认</span>
          <mt-switch style="display:inline-block;vertical-align: middle;" v-model="addr.acquiescence"></mt-switch>
        </div> -->
        <div style="padding: 0 20px;">
          <mt-button style="width:100%;margin-top: 20px;background-color:red;color:#ffffff;" @click="sumbit">保存</mt-button>
        </div>
      </div>
    </div>
    <!-- 选择地区 -->
    <mt-popup v-model="addrPopupVisible" position="bottom" style="width: 100%;">
      <mt-picker :slots="slots" @change="onValuesChange" :showToolbar="true" style="text-align: center;">
        <mt-button style="float:left" @click="addrPopupVisible = false">取消</mt-button>
        <div style="display: inline-block;width: 50%;line-height:40px;">选择地区</div>
        <mt-button style="float:right" @click="choice">确定</mt-button>
      </mt-picker>
    </mt-popup>
  </div>
</template>

<script>
export default {
  name: 'editUserAddress',
  data () {
    return {
      sumbitting: false,
      addr: {
        name: '',
        phone: '',
        address: '',
        detailed_address: '',
        acquiescence: false,
        penguin: '',
        alipay: ''
      },
      addrPopupVisible: false,
      areaPicker: '',
      slots: [
        {
          flex: 1,
          values: Object.keys(address),
          textAlign: 'right'
        }, {
          divider: true,
          content: '-',
          className: 'slot2'
        }, {
          flex: 1,
          values: Object.values(address)[0],
          textAlign: 'left'
        }
      ]
    }
  },
  created () {
    let obj = this.$route.params.obj
    if (obj) {
      this.addr = obj
    }
  },
  methods: {
    showPopup () {
      this.addrPopupVisible = true
      if (this.addr.address) {
        this.areaPicker.setSlotValue(0, this.addr.address.split(' ')[0])
        this.areaPicker.setSlotValue(1, this.addr.address.split(' ')[1])
      } else {
        this.areaPicker.setSlotValue(0, '北京')
        this.areaPicker.setSlotValue(1, '北京')
      }
    },
    onValuesChange (picker, values) {
      this.areaPicker = picker
      picker.setSlotValues(1, address[values[0]])
      this.province = values[0]
      this.city = values[1]
    },
    choice () {
      this.addr.address = this.province + ' ' + this.city
      this.addrPopupVisible = false
    },
    validateField (field, scope) {
      let valid = true
      switch (field) {
        case 'name':
          if (!scope.name.trim()) {
            this.toast('请输入收货人')
            valid = false
          }
          break
        case 'phone':
          let regx = /^(((13[0-9]{1})|(14[579]{1})|(15[0-35-9]{1})|(17[0135-8]{1})|(18[0-9]{1}))+\d{8})$/
          if (!regx.test(scope.phone)) {
            this.toast('请输入正确手机号')
            valid = false
          }
          break
        case 'address':
          if (!scope.address.trim()) {
            this.toast('请选择地区')
            valid = false
          }
          break
        case 'detailed_address':
          if (!scope.detailed_address.trim()) {
            this.toast('请输入详情地址')
            valid = false
          }
          break
        default:
          break
      }
      return valid
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
    },
    sumbit () {
      for (let key in this.addr) {
        if (!this.validateField(key, this.addr)) {
          return
        }
      }
      if (!this.sumbitting) {
        this.sumbitting = true
        if (!this.addr.penguin) {
          delete this.addr.penguin
        }
        if (!this.addr.alipay) {
          delete this.addr.alipay
        }
        if (this.addr.id) {
          this.$api.updateAddress(this.addr, data => {
            this.toast('保存成功')
            this.$router.go(-1)
          }, err => {
            this.toast(err.message)
          })
        } else {
          this.$api.addAddress(this.addr, data => {
            this.toast('保存成功')
            this.$router.go(-1)
          }, err => {
            this.toast(err.message)
          })
        }
      }
    }
  }
}
var address = {'北京': ['北京'], '广东': ['广州', '深圳', '珠海', '汕头', '韶关', '佛山', '江门', '湛江', '茂名', '肇庆', '惠州', '梅州', '汕尾', '河源', '阳江', '清远', '东莞', '中山', '潮州', '揭阳', '云浮'], '上海': ['上海'], '天津': ['天津'], '重庆': ['重庆'], '辽宁': ['沈阳', '大连', '鞍山', '抚顺', '本溪', '丹东', '锦州', '营口', '阜新', '辽阳', '盘锦', '铁岭', '朝阳', '葫芦岛'], '江苏': ['南京', '苏州', '无锡', '常州', '镇江', '南通', '泰州', '扬州', '盐城', '连云港', '徐州', '淮安', '宿迁'], '湖北': ['武汉', '黄石', '十堰', '荆州', '宜昌', '襄樊', '鄂州', '荆门', '孝感', '黄冈', '咸宁', '随州', '恩施土家族苗族自治州', '仙桃', '天门', '潜江', '神农架林区'], '四川': ['成都', '自贡', '攀枝花', '泸州', '德阳', '绵阳', '广元', '遂宁', '内江', '乐山', '南充', '眉山', '宜宾', '广安', '达州', '雅安', '巴中', '资阳', '阿坝藏族羌族自治州', '甘孜藏族自治州', '凉山彝族自治州'], '陕西': ['西安', '铜川', '宝鸡', '咸阳', '渭南', '延安', '汉中', '榆林', '安康', '商洛'], '河北': ['石家庄', '唐山', '秦皇岛', '邯郸', '邢台', '保定', '张家口', '承德', '沧州', '廊坊', '衡水'], '山西': ['太原', '大同', '阳泉', '长治', '晋城', '朔州', '晋中', '运城', '忻州', '临汾', '吕梁'], '河南': ['郑州', '开封', '洛阳', '平顶山', '安阳', '鹤壁', '新乡', '焦作', '濮阳', '许昌', '漯河', '三门峡', '南阳', '商丘', '信阳', '周口', '驻马店'], '吉林': ['长春', '吉林', '四平', '辽源', '通化', '白山', '松原', '白城', '延边朝鲜族自治州'], '黑龙江': ['哈尔滨', '齐齐哈尔', '鹤岗', '双鸭山', '鸡西', '大庆', '伊春', '牡丹江', '佳木斯', '七台河', '黑河', '绥化', '大兴安岭地区'], '内蒙古': ['呼和浩特', '包头', '乌海', '赤峰', '通辽', '鄂尔多斯', '呼伦贝尔', '巴彦淖尔', '乌兰察布', '锡林郭勒盟', '兴安盟', '阿拉善盟'], '山东': ['济南', '青岛', '淄博', '枣庄', '东营', '烟台', '潍坊', '济宁', '泰安', '威海', '日照', '莱芜', '临沂', '德州', '聊城', '滨州', '菏泽'], '安徽': ['合肥', '芜湖', '蚌埠', '淮南', '马鞍山', '淮北', '铜陵', '安庆', '黄山', '滁州', '阜阳', '宿州', '巢湖', '六安', '亳州', '池州', '宣城'], '浙江': ['杭州', '宁波', '温州', '嘉兴', '湖州', '绍兴', '金华', '衢州', '舟山', '台州', '丽水'], '福建': ['福州', '厦门', '莆田', '三明', '泉州', '漳州', '南平', '龙岩', '宁德'], '湖南': ['长沙', '株洲', '湘潭', '衡阳', '邵阳', '岳阳', '常德', '张家界', '益阳', '郴州', '永州', '怀化', '娄底', '湘西土家族苗族自治州'], '广西': ['南宁', '柳州', '桂林', '梧州', '北海', '防城港', '钦州', '贵港', '玉林', '百色', '贺州', '河池', '来宾', '崇左'], '江西': ['南昌', '景德镇', '萍乡', '九江', '新余', '鹰潭', '赣州', '吉安', '宜春', '抚州', '上饶'], '贵州': ['贵阳', '六盘水', '遵义', '安顺', '铜仁地区', '毕节地区', '黔西南布依族苗族自治州', '黔东南苗族侗族自治州', '黔南布依族苗族自治州'], '云南': ['昆明', '曲靖', '玉溪', '保山', '昭通', '丽江', '普洱', '临沧', '德宏傣族景颇族自治州', '怒江傈僳族自治州', '迪庆藏族自治州', '大理白族自治州', '楚雄彝族自治州', '红河哈尼族彝族自治州', '文山壮族苗族自治州', '西双版纳傣族自治州'], '西藏': ['拉萨', '那曲地区', '昌都地区', '林芝地区', '山南地区', '日喀则地区', '阿里地区'], '海南': ['海口', '三亚', '五指山', '琼海', '儋州', '文昌', '万宁', '东方', '澄迈县', '定安县', '屯昌县', '临高县', '白沙黎族自治县', '昌江黎族自治县', '乐东黎族自治县', '陵水黎族自治县', '保亭黎族苗族自治县', '琼中黎族苗族自治县'], '甘肃': ['兰州', '嘉峪关', '金昌', '白银', '天水', '武威', '酒泉', '张掖', '庆阳', '平凉', '定西', '陇南', '临夏回族自治州', '甘南藏族自治州'], '宁夏': ['银川', '石嘴山', '吴忠', '固原', '中卫'], '青海': ['西宁', '海东地区', '海北藏族自治州', '海南藏族自治州', '黄南藏族自治州', '果洛藏族自治州', '玉树藏族自治州', '海西蒙古族藏族自治州'], '新疆': ['乌鲁木齐', '克拉玛依', '吐鲁番地区', '哈密地区', '和田地区', '阿克苏地区', '喀什地区', '克孜勒苏柯尔克孜自治州', '巴音郭楞蒙古自治州', '昌吉回族自治州', '博尔塔拉蒙古自治州', '石河子', '阿拉尔', '图木舒克', '五家渠', '伊犁哈萨克自治州'], '香港': ['香港'], '澳门': ['澳门'], '台湾': ['台北市', '高雄市', '台北县', '桃园县', '新竹县', '苗栗县', '台中县', '彰化县', '南投县', '云林县', '嘉义县', '台南县', '高雄县', '屏东县', '宜兰县', '花莲县', '台东县', '澎湖县', '基隆市', '新竹市', '台中市', '嘉义市', '台南市']}
</script>

<style>

</style>
