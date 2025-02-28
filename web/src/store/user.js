import $ from 'jquery';

export default ({
  state: {
    id:"",
    username:"",
    photo:"",
    token:"",
    is_login:false,
  },
  getters: {
  },
  mutations: {
    updateUser(state,user){//更新登录信息
        state.id=user.id;
        state.username=user.username;
        state.photo=user.photo;
        state.is_login=user.is_login;
    },
    updateToken(state,token){//更新token
        state.token=token;
    },
    logout(state){//退出
        state.id="";
        state.username="";
        state.photo="";
        state.is_login=false;
        state.token="";
    }
  },
  actions: {//修改state用
    //前端记录token 函数存在state区
    login(contxt,data){// https://app5356.acapp.acwing.com.cn/ http://127.0.0.1:3000/
      $.ajax({
        url:"http://127.0.0.1:3000/api/user/account/token/",
        type:"post",
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp){
            if(resp.error_message=="success"){
                  //登录持久化
                  localStorage.setItem("jwt_token",resp.token);
                  //commit调用mutations
                  contxt.commit("updateToken",resp.token);
                  data.success(resp);
            }
            else{
                data.error(resp);
            }
        },
        error(resp){
          data.error(resp);
        }
      });
    },

    getinfo(contxt,data){//https://app5356.acapp.acwing.com.cn/ 
      $.ajax({
        url:"http://127.0.0.1:3000/api/user/account/info/",
        type:"get",
        headers:{//取出state属性
          Authorization:"Bearer "+contxt.state.token,
        },
        success(resp){
            if(resp.error_message==="success"){
                contxt.commit("updateUser",{
                    ...resp,
                    is_login:true,
                });
                data.success(resp);
            }else{
                data.error(resp);
            }
        },
        error(resp){
          data.error(resp);
        }
      });
    },
    
    logout(contxt){
      localStorage.removeItem("jwt_token");
        contxt.commit("logout");
    }
  },
  modules: {
  }
})
