import { createRouter, createWebHistory } from 'vue-router';
import NotFound from "../views/error/NotFound";
import PKIndexView from "../views/pk/PKIndexView";
import RanklistIndexView from "../views/ranklist/RanklistIndexView";
import RecordIndexView from "../views/record/RecordIndexView";
import RecordContendView from "../views/record/RecordContendView";
import UserBotIndexView from "../views/user/bot/UserBotIndexView";
import UserAccountLoginView from "../views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView";
import store from '../store/index';

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PKIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RanklistIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {//路由加参
    path:"/record/:recordId/",
    name:"record_content",
    component:RecordContendView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,
  },
  {
    path:"/404/",
    name:"NotFound",
    component:NotFound,
  },
  {
    path:"/:catchAll(.*)",
    redirect:"/404/",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
//router起作用前执行
//to:去哪里,from从哪里执行,next要不要执行
router.beforeEach((to,from,next)=>{
    if(to.meta.requestAuth&&!store.state.user.is_login){
      next({name:"user_account_login"});
    }else{
      next();
    }
})
export default router
