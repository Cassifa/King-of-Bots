<template>
  <NavBar></NavBar>
  <router-view/>
</template>


<script>
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
import $ from 'jquery';

export default{
  name:"App",
  components:{
    NavBar,
  },
  setup(){
      $.ajax({
        url:"http://127.0.0.1:3000/user/account/token/",
        type:"post",
        data: {
          username: "sgf",
          password: "123",
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      });

      $.ajax({
        url:"http://127.0.0.1:3000/user/account/info/",
        type:"get",
        headers:{
          Authorization:"Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmYzI0MjIwYWZiZDU0MzU1OTAxZWFiOTY0NjRiNjcxNCIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY3NzQ4MTM4NywiZXhwIjoxNjc4NjkwOTg3fQ.9X6jrxF67YiCrUfiTwxwQeJdEnwstTDUeO8K3-Tf6A4",
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      });

  }

}
</script>

<style>
body{
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
