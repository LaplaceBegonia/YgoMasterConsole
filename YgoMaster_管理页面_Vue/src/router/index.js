//引入VueRouter
import VueRouter from 'vue-router'
//引入Luyou 组件
import Vue from "vue";
import home from '../components/home.vue'

Vue.use(VueRouter);
//创建router实例对象，去管理一组一组的路由规则
const router = new VueRouter({
    routes:[
        {
            path:'/',
            component:home,
            name:"首页"
        },
    ]
})

//暴露router
export default router