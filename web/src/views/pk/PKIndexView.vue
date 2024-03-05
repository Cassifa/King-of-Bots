<template>
    <!-- 游戏页面 -->
    <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
    <!-- 匹配页面 -->
    <MatchGround v-if="$store.state.pk.status==='matching'"></MatchGround>
    <!-- 结果公告栏 -->
    <ResultBoard v-if="$store.state.pk.loser!='none'"></ResultBoard>
    <!-- <div v-if="$store.state.pk.status==='playing'">
        <div class="user-color" v-if="$store.state.user.id==$store.state.pk.a_id">您的角色初始在左下角</div>
        <div class="user-color" v-else>您的角色初始在右上角</div>
    </div> -->
    <div class="user-color" v-if="$store.state.pk.status==='playing'&&$store.state.pk.select_bot==-1">点击AWSD以移动</div>

    
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
        const socketUrl=`ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket=null;
        onMounted(()=>{//挂载后执行
            store.commit("updateOpponent", {//更新对手信息
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket=new WebSocket(socketUrl);//建立与后端通信连接

            socket.onopen=()=>{
                //建立websocket连接
                console.log("websocket connected");
                store.commit("updateSocket",socket);
            }

            socket.onmessage=msg=>{
                const data=JSON.parse(msg.data);//解析消息
                if(data.event==="start-matching"){//匹配成功
                    store.commit("updateOpponent",{//更新对手信息
                        username:data.opponent_username,
                        photo:data.opponent_photo,
                    });

                    store.commit("updateGame",data.game);//更新游戏信息
                    if(store.state.user.id==store.state.pk.a_id)
                        store.commit("updatePlaceId",0);
                    else store.commit("updatePlaceId",1);
                    setTimeout(()=>{
                        store.commit("updateStatus","playing");
                    },200);
                }
                //接收后端广播的操作
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
            store.state.pk.socket.send(JSON.stringify({
                event:"stop-matching",
            }))
            socket.close();
        });
    }
})

</script>

<style scoped>
div.user-color{
    text-align: center;
    color:red;
    font-size: 30px;
    font-weight: bold;
}
</style>