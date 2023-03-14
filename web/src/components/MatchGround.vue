<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                    <div class="user-photo"><img :src="$store.state.user.photo" alt=""></div>
                    <div class="user-username"> {{$store.state.user.username}} </div>
            </div>
            <div class="col-6">
                    <div class="user-photo"><img :src="$store.state.pk.opponent_photo" alt=""></div>
                    <div class="user-username"> {{$store.state.pk.opponent_username}} </div>
            </div>
            <div class="col-12 buttonc">
                <button type="button" class="btn btn-warning btn-lg">开始匹配</button>
            </div>
        </div>
    </div>
</template>


<script>
export default{
    name:"MatchGround",
    components:{
    }
}
</script>

<style scoped>
.matchground{
    width: 60vw;
    height: 70vh;
    margin: 20px auto;
    background-color: rgb(50,50,50,0.5);
}
div.user-photo{
    text-align: center;
    padding-top: 10vh;
}
div.user-photo>img{
    border-radius: 50%;
    width: 20vh;
}
div.user-username{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
.buttonc{
    text-align: center;
    padding-top: 15vh;
}
</style>