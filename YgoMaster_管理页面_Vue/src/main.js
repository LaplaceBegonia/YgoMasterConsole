import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import router from './router/index'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'
import Avue from '@smallwei/avue';
import '@smallwei/avue/lib/index.css';
import axios  from "axios";
import request from "./axios/request.js"

Vue.prototype.axios=axios
Vue.use(ElementUI)
Vue.use(Avue);
Vue.config.productionTip = false
import MD5 from "js-md5"
//将md5转换成vue原型：
Vue.prototype.md5 = MD5

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
