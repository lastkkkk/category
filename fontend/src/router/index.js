import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'
import Login from '@/components/Login'
import  Users from '@/components/users/users'
import GoodsList from '@/components/goods/goodslist'
import GPClist from '@/components/GPClist/GPClist'
import GPCate from '@/components/GPClist/gpcate'
import UNCate from '@/components/GPClist/uncate'
import Index from '@/components/Index'

Vue.use(Router)

export default new Router({
  mode:"history",
  routes: [
    {
      path:'/',
      name:'login',
      component:Login
    },
    {
      path:'/main',
      name:"main",
      component:Main,
      redirect:'/users',
      // meta:{
      //   requireAuth:true
      // },
      children:[
        {
          path:"/index",
          name:"index",
          meta:{
            requireAuth:true,
          },
          component:Index,
        },
        {
          path:'/users',
          name:"users",
          meta:{
            requireAuth:true,
          },
          component:Users,
        },
        {
          path:'/goodslist',
          name:"goodslist",
          component:GoodsList,
          meta:{
            requireAuth:true
          },
        },
        {
          path:'/gpclist',
          name:"GPCList",
          component:GPClist,
          meta:{
            requireAuth:true
          },
        },
        {
          path:'/gpcate',
          name:"GPCate",
          component:GPCate,
          meta:{
            requireAuth:true
          },
        },
        {
          path:'/uncate',
          name:"UNCate",
          component:UNCate,
          meta:{
            requireAuth:true
          },
        },
      ]
    },
  ]
})
