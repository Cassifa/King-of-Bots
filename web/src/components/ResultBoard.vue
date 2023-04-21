<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser==='all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser=='a'&&$store.state.pk.a_id==$store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser=='b'&&$store.state.pk.b_id==$store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-warning btn-lg col-6">再来</button>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';
export default {

    setup(){
        const store=useStore();
        const restart=()=>{
            store.commit("updateLoser","none");
            store.commit("updateStatus","matching");
            store.commit("updateOpponent", {
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })

        }

        return{
            restart,
        }
    }
}
</script>

<style scoped>
div.result-board{
    height: 30vh;
    width: 40vh;
    background-color: rgb(50,50,50,0.5);
    position: absolute;
    top: 30vh;
    left: 35vh;
}
div.result-board-text{
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 600;
    font-style: italic;
    padding-top: 10vh;
}
div.result-board-btn{
    text-align: center;
    padding-top: 5vh;
}
</style>