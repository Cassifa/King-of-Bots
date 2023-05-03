export default({
  state: {
    id_record:false,
    a_step:"",
    b_step:"",
    record_loser:"",
  },
  getters: {
  },
  mutations: {
    updateIsRecord(state,is_record) {//更新是否为录像
      state.is_record=is_record;
    },
    updateSteps(state,data){//更新当前操作
      state.a_step=data.a_step;
      state.b_step=data.b_step;
    },
    updateLoser(state,loser) {//更新失败方
      state.record_loser=loser;
    },
  },
  actions: {
  },
  modules: {
  }
})
