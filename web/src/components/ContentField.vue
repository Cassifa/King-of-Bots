<template>
    <div class="container content-flied">
        <div class="card">
            <div class="card-body">
                <slot>

                </slot>
            </div>
        </div>
    </div>
</template>

<script>
export default{
    name:'ContentField',

}
</script>

<style scoped>
div.content-flied{
    margin-top: 20px;
}
div{
    background-color: rgba(0,0,0,0);
    border-color: rgba(0,0,0,0);
}
</style>