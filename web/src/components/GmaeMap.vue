<template>
    <div ref="parent" class="gamemap">
        <!-- 可输入tabindex="0" -->
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>


<script>
import GameMap from '@/assets/scripts/GameMap';
import { ref,onMounted } from 'vue';
import { useStore } from 'vuex'; 
export default{
    name:"GameMap",

    setup(){
        const store=useStore();
        let parent=ref(null);
        let canvas=ref(null);

        //组件挂载完后需要执行得操作
        onMounted(()=>{
            store.commit("updateGameObject",
                new GameMap(canvas.value.getContext('2d'),parent.value,store));

        });

        return{
            parent,
            canvas,
        }
    }
}
</script>

<style scoped>
.gamemap{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>