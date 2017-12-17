import Vue from 'vue'
import {
  Button,
  Cell,
  CellSwipe,
  InfiniteScroll,
  Toast,
  Popup,
  Swipe,
  SwipeItem,
  Lazyload,
  IndexList,
  IndexSection,
  Header,
  Navbar,
  Tabbar,
  TabItem,
  Spinner,
  Radio,
  TabContainer,
  TabContainerItem,
  MessageBox,
  Badge,
  Field,
  Loadmore,
  Picker,
  Switch } from 'mint-ui'

Vue.prototype.$toast = Toast
Vue.prototype.$messageBox = MessageBox
Vue.use(InfiniteScroll)
Vue.use(Lazyload)
Vue.component(Popup.name, Popup)
Vue.component(Swipe.name, Swipe)
Vue.component(SwipeItem.name, SwipeItem)
Vue.component(IndexList.name, IndexList)
Vue.component(IndexSection.name, IndexSection)
Vue.component(Header.name, Header)
Vue.component(Navbar.name, Navbar)
Vue.component(Tabbar.name, Tabbar)
Vue.component(TabItem.name, TabItem)
Vue.component(Button.name, Button)
Vue.component(Cell.name, Cell)
Vue.component(CellSwipe.name, CellSwipe)
Vue.component(Spinner.name, Spinner)
Vue.component(TabContainer.name, TabContainer)
Vue.component(TabContainerItem.name, TabContainerItem)
Vue.component(Field.name, Field)
Vue.component(Radio.name, Radio)
Vue.component(Badge.name, Badge)
Vue.component(Loadmore.name, Loadmore)
Vue.component(Picker.name, Picker)
Vue.component(Switch.name, Switch)
