webpackJsonp([23],{127:function(t,e,n){n(305);var s=n(6)(n(232),n(353),null,null);t.exports=s.exports},232:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"messageInfo",data:function(){return{notice:{}}},created:function(){var t=this,e=this.$route.query.id;e?this.$api.getNotice(e,function(n){t.notice=n.resource,t.$api.read(e,function(e){t.notice.state="READ"},function(e){t.$toast({message:e.message})})},function(e){t.$router.go(-1),t.$toast({message:e.message})}):this.$router.go(-1)},methods:{routerBack:function(){this.$router.go(-1)}}}},271:function(t,e,n){e=t.exports=n(111)(!1),e.push([t.i,".messageInfo-content{text-align:center;display:block;padding:10px;height:100%}.messageInfo-content h3{font-size:18px;font-weight:600}.messageInfo-content p{font-size:14px;color:#999;margin:10px auto}.messageInfo-content article{text-align:left;text-indent:2em;margin-bottom:10px;font-size:14px;color:#333}",""])},305:function(t,e,n){var s=n(271);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);n(112)("7186c90f",s,!0)},353:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"messageInfo"},[n("div",{staticClass:"page-wrap"},[n("div",{staticClass:"page-title"},[n("a",{on:{click:t.routerBack}},[n("i",{staticClass:"icon-back"})]),t._v(" "),n("span",[t._v("公告消息")])]),t._v(" "),n("div",{staticClass:"page-content"},[n("div",{staticClass:"messageInfo-content"},[n("h3",[t._v(t._s(t.notice.title))]),t._v(" "),n("p",[t._v(t._s(t.$filter.date(t.notice.time)))]),t._v(" "),n("article",[t._v("\n          "+t._s(t.notice.context)+"\n        ")])])])])])},staticRenderFns:[]}}});