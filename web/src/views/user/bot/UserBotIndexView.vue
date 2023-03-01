<template>
    <ContentField>Bot</ContentField>
</template>
  
<script>

import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
export default({
      name:"UserBotIndexView",
      components:{
          ContentField
      },
      setup() {
        let store=useStore();
        // $.ajax({
        //     url:"http://127.0.0.1:3000/user/bot/add/",
        //     type:"post",
        //     data:{
        //         title:"Bot标题",
        //         description:"描述",
        //         content:"内容",
        //     },
        //     headers:{
        //         Authorization:"Bearer "+store.state.user.token,
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     },
        // });
        // $.ajax({
        //     url:"http://127.0.0.1:3000/user/bot/remove/",
        //     type:"post",
        //     data:{
        //         bot_id:6,
        //     },
        //     headers:{
        //         Authorization:"Bearer "+store.state.user.token,
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     },
    //     // });
    //     $.ajax({
    //         url:"http://127.0.0.1:3000/user/bot/update/",
    //         type:"post",
    //         data:{
    //             bot_id:2,
    //             title:"new Bot标题",
    //             description:"new 描述",
    //             content:"new 内容",

    //         },
    //         headers:{
    //             Authorization:"Bearer "+store.state.user.token,
    //         },
    //         success(resp){
    //             console.log(resp);
    //         },
    //         error(resp){
    //             console.log(resp);
    //         },
    //     });
    //   }
    
    $.ajax({
            url:"http://127.0.0.1:3000/user/bot/getlist/",
            type:"get",
            data:{
                userId:1,
            },
            headers:{
                Authorization:"Bearer "+store.state.user.token,
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            },
        });
      }
})

</script>

<style scoped>
</style>