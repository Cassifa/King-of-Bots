<template>
    <div class="matchground">

        <div class="row">
            <div class="col-4">
                    <div class="user-photo"><img :src="$store.state.user.photo" alt=""></div>
                    <div class="user-username"> {{$store.state.user.username}} </div>
            </div>
            
            <div class="col-4">
                <div class="user-select-bot">
                    <!-- 双向绑定 -->
                    <select v-model="select_bot" class="form-select" aria-label="Disabled select example">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{bot.title}}</option>
                      </select>
                </div>
            </div>

            <div class="col-4">
                    <div class="user-photo"><img :src="$store.state.pk.opponent_photo" alt=""></div>
                    <div class="user-username"> {{$store.state.pk.opponent_username}} </div>
            </div>

            <div class="col-12 buttonc">
                <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg">{{match_btn_info}}</button>
            </div>
        </div>

    </div>
</template>


<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default{
    name:"MatchGround",
    components:{
    },

    setup() {
        const store=useStore();
        let match_btn_info=ref("开始匹配");
        let bots =ref([]);
        let select_bot=ref("-1");

        const click_match_btn=()=>{
            if(match_btn_info.value=="开始匹配"){
                match_btn_info.value="取消匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    bot_id:select_bot.value,//传输时已是数据库id
                }))
            } 
            else{
                match_btn_info.value="开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",

                }))
            }
        }

        
        const refresh_bots=()=>{
            $.ajax({
                    url:"http://127.0.0.1:3000/user/bot/getlist/",
                    type:"get",
                    data:{
                        userId:store.state.user.id,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        bots.value=resp;
                    },
            });
        }
        refresh_bots();//执行一次从云端获取Bots

        return{
            match_btn_info,
            bots,
            select_bot,

            click_match_btn,
        }
    }
}
</script>

<style scoped>
.matchground{
    width: 60vw;
    height: 70vmin;
    margin: 20px auto;
    background-color: rgb(50,50,50,0.5);
}
div.user-photo{
    text-align: center;
    padding-top: 10vmin;
}
div.user-photo>img{
    border-radius: 50%;
    width: 20vmin;
}
div.user-username{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vmin;
}
.buttonc{
    text-align: center;
    padding-top: 15vmin;
}
.user-select-bot{
    padding-top: 20vmin;
}
.user-select-bot>select{
    width: 60%;
    margin: auto;
}
.user-select-bot option{
    font-style: italic;
    text-align: center;
}
button{
    width: min(40%,20vmax)
}
</style>