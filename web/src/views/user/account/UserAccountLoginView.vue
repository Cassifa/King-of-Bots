<template>
    <content-field v-if="show_content">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button  type="submit" class="btn btn-info">提交</button>
                </form>
            </div>
        </div>
    </content-field>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router/index';

export default {
    name:"UserAccountLoginView",
    components: {
        ContentField,
    },
    setup(){
        let store=useStore();
        let username=ref('');
        let error_message=ref('');
        let password=ref('');
        let show_content=ref(false);

        const jwt_token=localStorage.getItem("jwt_token");
        if(jwt_token){
            ///调用mutations
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success(){
                    router.push({name:"home"});
                }
            })
        }
        show_content.value=true;
        const login=()=>{
            error_message.value="";
            //调用actions函数
            store.dispatch("login",{
                username:username.value,
                password:password.value,
                success(){
                    store.dispatch("getinfo",{
                        success(){
                            router.push({name:'home'});
                        },
                    });
                },
                error(){
                    error_message.value="用户名或密码错误";
                },
            });
        }

        return{
            username,
            error_message,
            password,
            show_content,

            login,
        }
    }
}
</script>

<style scoped>
button{
    width: 100%;
}
.error-message{
    color: red;
}
.form-label{
    font-size: large;
    font-style: italic;
}
</style>