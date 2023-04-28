<template>
    <div class="result-board" style="text-align: center">
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
div.result-board{/*居中div*/
    height: 30vmin;
    width: 40vmin;
    background-color: rgb(50,50,50,0.5);
    margin: 0 auto;
    position: absolute;/*给大div一个绝对定位*/
    top: 50%;
    left: 50%;
    margin-top: -18vmin;/*往上移动自身高度的一半*/
    margin-left: -20vmin;/*往左移动自身宽度的一半*/
}
div.result-board-text{
    text-align: center;
    color: white;
    font-size: 7vmin;
    font-weight: 600;
    font-style: italic;
    padding-top: 8vmin;
}
div.result-board-btn{
    text-align: center;
    padding-top:2vmin;
    height: 5vmin;
}
</style>