<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>


<script>
import GameMap from './GmaeMap.vue';

export default{
    name:"PlayGround",
    components:{
        GameMap,
    }
}
</script>

<style scoped>
.playground{
    width: 60vw;
    height: 70vh;
    margin: 20px auto;
}
</style>