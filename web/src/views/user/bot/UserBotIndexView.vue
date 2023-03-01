<template>
    <div class="container">
        <div class="row">
          <div class="col-3">
            <ContentField>
                <img :src="$store.state.user.photo" style="width:100%">
            </ContentField>
          </div>
          <div class="col-9">
            <div class="card" style="margin-top:20px">
                <div class="card-header">
                    <span style="font-size:120%">我的Bot</span>
                    <button type="button" class="btn btn-primary float-end col-2" data-bs-toggle="modal" data-bs-target="#add-bot">创建Bot</button>
                    <!-- Modal -->
                    <div class="modal fade" id="add-bot" tabindex="-1">
                        <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                            <div class="modal-header">
                            <h1 class="modal-title fs-5">创建Bot</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">Bot名称</label>
                                        <input v-model="botadd.title" type="email" class="form-control" id="dd-bot-title" placeholder="请输入bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">简介</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="dd-bot-code" class="form-label">代码</label>
                                        <textarea v-model="botadd.content" class="form-control" id="dd-bot-code" rows="10" placeholder="请编写bot代码"></textarea>
                                        <!-- <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" /> -->
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <div class="error_message">{{ botadd.error_message}}</div>
                                <button type="button" class="btn btn-primary col-2" @click="add_bot">确认</button>
                                <button type="button" class="btn btn-secondary col-2" data-bs-dismiss="modal">关闭</button>
                            </div>
                        </div>
                        </div>
                    </div>

                </div>

                <!-- Bot列表 -->
                <div class="card-body">
                    <table class="table table-hover">
                        <thead>
                          <tr>
                            <th scope="col">#</th>
                            <th scope="col">名称</th>
                            <th scope="col">创建时间</th>
                            <th scope="col">修改</th>
                          </tr>
                        </thead>
                        <tbody class="table-group-divider">
                          <tr v-for="(bot,index) in bots" :key="bot.id">
                            <td>{{ index+1 }}</td>
                            <td>{{ bot.title }}</td>
                            <td>{{bot.createtime}}</td>
                            <td>
                                <button type="button" class="btn btn-secondary col-4" data-bs-toggle="modal" :data-bs-target="'#add-bot'+bot.id">修改</button>
                                <button type="button" class="btn btn-danger col-4" @click="remove_bot(bot)">删除</button>
                                <!-- 列表Modal -->
                                <div class="modal fade" :id="'add-bot'+bot.id" tabindex="-1">
                                    <div class="modal-dialog modal-xl">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                        <h1 class="modal-title fs-5">修改Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <form>
                                                <div class="mb-3">
                                                    <label for="add-bot-title" class="form-label">Bot名称</label>
                                                    <input v-model="bot.title" type="email" class="form-control" id="dd-bot-title" placeholder="请输入bot名称">
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-description" class="form-label">简介</label>
                                                    <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="dd-bot-code" class="form-label">代码</label>
                                                    <textarea v-model="bot.content" class="form-control" id="dd-bot-code" rows="10" placeholder="请编写bot代码"></textarea>
                                                    <!-- <VAceEditor v-model:value="bot.content" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" /> -->
                                                </div>
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                            <div class="error_message">{{ bot.error_message}}</div>
                                            <button type="button" class="btn btn-primary col-2" @click="update_bot(bot)">确认</button>
                                            <button type="button" class="btn btn-secondary col-2" data-bs-dismiss="modal" @click="refresh_bots">关闭</button>
                                        </div>
                                    </div>
                                    </div>
                                </div>

                            </td>
                          </tr>
                        </tbody>
                    </table>
                 </div>
            </div>
          </div>
        </div>
      </div>
</template>
  
<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
//reactive绑定对象
import {ref,reactive} from 'vue';
import {Modal} from 'bootstrap/dist/js/bootstrap';
//Model API
// import { VAceEditor } from 'vue3-ace-editor';
// import ace from 'ace-builds';
//代码编辑区

export default({
      name:"UserBotIndexView",

      components:{
          ContentField,
        //   VAceEditor,
      },

      setup() {
        // ace.config.set("basePath","https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const store=useStore();
        let bots=ref([]);
        const botadd=reactive({
            title:"",
            description:"",
            content:"",
            error_message:"",
        })
        const refresh_bots=()=>{
            $.ajax({
                    url:"http://127.0.0.1:3000/user/bot/getlist/",
                    type:"get",
                    data:{
                        userId:store.state.user.id,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        bots.value=resp;
                    },
            });
        }
        refresh_bots();
        const add_bot=()=>{
            botadd.error_message="";
                $.ajax({
                    url:"http://127.0.0.1:3000/user/bot/add/",
                    type:"post",
                    data:{
                        title:botadd.title,
                        description:botadd.description,
                        content:botadd.content,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            refresh_bots();
                            botadd.title="";
                            botadd.description="";
                            botadd.content="";
                            Modal.getInstance("#add-bot").hide();
                        }
                        else botadd.error_message=resp.error_message;
                    },
                });
        }

        const remove_bot=(bot)=>{
                $.ajax({
                    url:"http://127.0.0.1:3000/user/bot/remove/",
                    type:"post",
                    data:{
                        bot_id:bot.id,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="删除成功"){
                            refresh_bots();
                        }
                    },
                });
        }

        const update_bot=(bot)=>{
            bot.error_message="";
                $.ajax({
                    url:"http://127.0.0.1:3000/user/bot/add/",
                    type:"post",
                    data:{
                        title:bot.title,
                        description:bot.description,
                        content:bot.content,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_message==="success"){
                            refresh_bots();
                            Modal.getInstance("#add-bot"+bot.id).hide();
                        }
                        else bot.error_message=resp.error_message;
                    },
                });
        }

        return{
            bots,
            botadd,

            add_bot,
            remove_bot,
            update_bot,
            refresh_bots,
        }
    }
})

</script>

<style scoped>
div.error_message{
    color: red;
}
</style>