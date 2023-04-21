<template>
    <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status==='matching'"></MatchGround>
    
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import { onMounted,onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default({
    name:'PKIndexView',
    components:{
        PlayGround,
        MatchGround,
    },
    setup(){
        const store=useStore();
        //当前用户websocket链接
        const socketUrl=`ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket=null;
        onMounted(()=>{
            store.commit("updateOpponent", {
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket=new WebSocket(socketUrl);

            socket.onopen=()=>{
                //建立websocket连接
                console.log("connected");
                store.commit("updateSocket",socket);
            }

            socket.onmessage=msg=>{
                const data=JSON.parse(msg.data);
                if(data.event==="start-matching"){//匹配成功
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo:data.opponent_photo,
                    });

                    store.commit("updateGamemap",data.gamemap);
                    setTimeout(()=>{
                        store.commit("updateStatus","playing");
                    },500);
                }
            }

            socket.onclose=()=>{
                console.log("disconected");
            }
        });

        onUnmounted(()=>{
            socket.close();
        });
    }
})

</script>

<style scoped>
</style>