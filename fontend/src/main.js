// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './style/theme.css'
import './style/characters.css'
import './assets/css/global.css'
import './assets/icons/iconfont/iconfont.css';
import './assets/icons/iconfont/iconfont.js'
import XLSX from 'xlsx'
import axios from 'axios'
import TreeTable from 'vue-table-with-tree-grid'
Vue.config.productionTip = false
Vue.prototype.$axios = axios
axios.defaults.baseURL = 'http://127.0.0.1:8081'
axios.defaults.withCredentials = true
Vue.use(ElementUI)
Vue.use(XLSX)
Vue.component('tree-table',TreeTable)
Vue.config.productionTip = false
axios.defaults.withCredentials = true
//导航守卫
router.beforeEach((to, from, next) => {
  if(store.state.user.userName){
    // console.log('进去')
    initAdminMenu(router,store)
  }
    // 已登录状态下访问 login 页面直接跳转到后台首页
    if (store.state.user && to.path.startsWith('/login')) {
      next({
        path: 'index'
      })
    }
    //如果为真的话
    if (to.meta.requireAuth) {
      //store里存了这个用户 就放行
      if (store.state.user.userName) {
        axios.get('/authentication').then(resp => {
          if (resp) next()
        })
      } else {
        //未存的话 需要跳转到login
        next({
          path: 'login',
          query: {redirect: to.fullPath}
        })
      }
    } else {
      next()
    }
  }
)
const formatRoutes = (routes) => {
  let fmtRoutes = []
  routes.forEach(route => {
    if (route.children) {
      route.children = formatRoutes(route.children)
    }

    let fmtRoute = {
      path: route.path,
      component: resolve => {
        require(['./components/' + route.component + '.vue'], resolve)
      },
      name: route.name,
      nameZh: route.nameZh,
      iconCls: route.iconCls,
      children: route.children
    }
    fmtRoutes.push(fmtRoute)
  })
  return fmtRoutes
}
const initAdminMenu = (router, store) => {
  if (store.state.adminMenus.length > 0) {
    return
  }
  axios.get('/menu').then(resp => {
    if (resp && resp.status === 200) {
      var fmtRoutes = formatRoutes(resp.data)
      router.addRoutes(fmtRoutes)
      store.commit('initAdminMenu', fmtRoutes)
    }
  })
}







/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  store,
  components: { App },
  template: '<App/>'
})

