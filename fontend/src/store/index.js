import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    adminMenus:[],
    user: {
      userName: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).username
    }
  },
  mutations: {
    login (state, user) {
      state.user = user
      window.localStorage.setItem('user', JSON.stringify(user))
    },
    logout(state){
      state.user = []
      window.localStorage.removeItem('user')
    },
    initAdminMenu (state, menus) {
      state.adminMenus = menus
    }

  },
  plugins:[createPersistedState({
    storage: window.localStorage,
    reducer(data) {
      return {
        // 设置只储存state中的myData
        user: data.user
      }
    }
  })]
})
