<template>
    <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status==='matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser!='none'"></ResultBoard>
    <div v-if="$store.state.pk.status==='playing'">
        <div class="user-color" v-if="$store.state.user.id==$store.state.pk.a_id">左下</div>
        <div class="user-color" v-else>右上</div>
    </div>

    
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import ResultBoard from '@/components/ResultBoard.vue'
import { onMounted,onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default({
    name:'PKIndexView',
    components:{
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup(){
        const store=useStore();
        store.commit("updateLoser","none");
        store.commit("updateIsRecord",false);
        //当前用户websocket链接
        //wss://app5356.acapp.acwing.com.cn/websocket/${store.state.user.token}/
        //ws://127.0.0.1:3000/websocket/${store.state.user.token}/
        const socketUrl=`ws://127.0.0.1:3000/websocket/${store.state.user.token}`;

        let socket=null;
        onMounted(()=>{
            store.commit("updateOpponent", {
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket=new WebSocket(socketUrl);

            socket.onopen=()=>{
                //建立websocket连接
                console.log("websocket connected");
                store.commit("updateSocket",socket);
            }

            socket.onmessage=msg=>{
                const data=JSON.parse(msg.data);
                if(data.event==="start-matching"){//匹配成功
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo:data.opponent_photo,
                    });

                    store.commit("updateGame",data.game);
                    setTimeout(()=>{
                        store.commit("updateStatus","playing");
                    },200);
                }
                else if(data.event==="move"){
                    const game=store.state.pk.gameObject;
                    const [s0,s1]=game.snakes;
                    s0.set_direction(data.a_direction);
                    s1.set_direction(data.b_direction);
                }
                else if(data.event==="result"){
                    const game=store.state.pk.gameObject;

                    const [s0,s1]=game.snakes;
                    if(data.loser==="all"||data.loser==="a")
                        s0.status="die";
                    if(data.loser==="all"||data.loser==="b")
                        s1.status="die";
                    store.commit("updateLoser",data.loser);//更新loser
                }
            }

            socket.onclose=()=>{
                console.log("websocket disconected");
            }
        });

        onUnmounted(()=>{
            socket.close();
        });
    }
})

</script>

<style scoped>
div.user-color{
    text-align: center;
    color:aquamarine;
    font-size: 30px;
    font-weight: bold;
}
</style>