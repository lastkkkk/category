import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/Components/Main'
import Login from '@/components/login'
import Forget from '@/components/forget'
import  Users from '@/components/users/users'
import GoodsList from '@/components/goods/goodslist'
import GoodsCate from '@/components/goods/goodscate'
import GPClist from '@/components/GPClist/GPClist'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path:'/login',
      name:'login',
      component:Login
    },
    {
      path:'/forget',
      name:'forget',
      component:Forget
    },
    {
      path:'/',
      name:"main",
      component:Main,
      redirect:'/users',
      children:[
        {
          path:'/users',
          name:"users",
          component:Users,
        },
        {
          path:'/goodslist',
          name:"goodslist",
          component:GoodsList,
        },
        {
          path:'/goodscate',
          name:"goodscate",
          component:GoodsCate,
        },
        {
          path:'/gpclist',
          name:"GPCList",
          component:GPClist,
        },
      ]
    },
  ]
})
