<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{name:'home'}">King of Bots</router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link :class="route_name == 'pk_index' ? 'nav-link active':'nav-link'" aria-current="page" :to="{name:'pk_index'}">对战</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'record_index' ? 'nav-link active':'nav-link'" :to="{name:'record_index'}">对局回放</router-link>
          </li>
          <li class="nav-item">
            <!-- 活跃时高亮 -->
            <router-link :class="route_name == 'ranklist_index' ? 'nav-link active':'nav-link'" :to="{name:'ranklist_index'}">天梯排行</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'board' ? 'nav-link active':'nav-link'" :to="{name:'board'}">公告板</router-link>
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              {{ store.state.user.username }}
            </a>
            <ul class="dropdown-menu" style="margin:0">
              <li><router-link class="dropdown-item" :to="{name:'user_bot_index'}">我的bot</router-link></li>
              <li><a class="dropdown-item" @click="logout">退出</a></li>
            </ul>
          </li>
        </ul>

        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link class="nav-link" active-class="active" :to="{name:'user_account_login'}">登录</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" active-class="active" :to="{name:'user_account_register'}">注册</router-link>
          </li>
        </ul>

      </div>
    </div>
  </nav>
</template>

<script>
import { computed } from '@vue/runtime-core';
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';

export default{
    setup(){
      const store=useStore();
      const route = useRoute();
      let route_name=computed(()=>route.name)

      const logout=()=>{
        store.dispatch("logout");
      }

      return{
        route_name,
        store,

        logout,
      }
    }
}
</script>

<style scoped>

</style>

