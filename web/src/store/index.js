import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePK from './pk'
import ModuleRecord from './record'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {//修改state用
  },
  modules: {
    user:ModuleUser,
    pk:ModulePK,
    record:ModuleRecord,
  }
})
