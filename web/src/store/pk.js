export default({
  state: {
    status:"matching",//matching匹配 playing对战中
    socket:null,
    opponent_username:"",
    opponent_photo:"",
    gamemap:null,
    a_id:0,
    a_sx:0,
    a_sy:0,
    b_id:0,
    b_sx:0,
    b_sy:0,
    gameObject:null,
    loser:"none",//all a,b
    select_bot:0,
    placeId:0,//
  },
  getters: {
  },
  mutations: {
    updateSocket(state,socket){
        state.socket=socket;
    },
    updateSelectBot(state,select_bot){
        state.select_bot=select_bot;
    },
    updateOpponent(state,opponent){
        state.opponent_username=opponent.username;
        state.opponent_photo=opponent.photo;
    },
    updateStatus(state,status){
        state.status=status;
    },
    updateGame(state,game){
      state.gamemap=game.game_map;
      state.a_id=game.a_id;
      state.b_id=game.b_id;
      state.a_sx=game.a_sx;
      state.a_sy=game.a_sy;
      state.b_sx=game.b_sx;
      state.b_sy=game.b_sy;
    },
    updateGameObject(state,gameObject){
      state.gameObject=gameObject;
    },
    updateLoser(state,loser){
      state.loser=loser;
    },
    updatePlaceId(state,place){
      state.placeId=place;
    }
  },
  actions: {
  },
  modules: {
  }
})
