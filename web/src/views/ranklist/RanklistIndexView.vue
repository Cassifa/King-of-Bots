<template>
    <div class="container">
        <div class="row">
          <div class="col-3">
            <ContentField class="ContentField">
                <img :src="$store.state.user.photo" style="width:100%" class="Myphoto">
            </ContentField>
          </div>
          <div class="col-9">
            <div class="card" style="margin-top:20px">
                <!-- Bot列表 -->
                <div class="card-body">
                    <table class="table table-hover">
                        <thead>
                          <tr style="text-align:center">
                            <th scope="col">#</th>
                            <th scope="col">Player</th>
                            <th scope="col">Rating</th>
                          </tr>
                        </thead>
                        <tbody class="table-group-divider">
                          <tr v-for="(user,index) in users" :key="user.id" style="text-align:center">
                            <td>
                                {{index+1}}
                            </td>
                            <td>
                                <img :src="user.photo" class="user-user-photo">
                                &nbsp;
                                <span class="user-user-name">{{user.username}}</span>
                            </td>
                            <td>
                                {{user.rating}}
                            </td>
                          </tr>
                        </tbody>
                    </table>            

                    <!-- 切换页面 -->
                    <nav aria-label="Page navigation example" style="float:right"><!-- 右置 -->
                        <ul class="pagination">
                          <li class="page-item" @click="updateCurrentPage(-1)"><a class="page-link" href="#">上一页</a></li>

                          <li class="page-item " v-for="page in pages" :key="page.num" @click="updateCurrentPage(page.num)">
                            <a :class="'page-link '+page.is_active" href="#">{{page.num}}</a>
                          </li>
                          
                          <li class="page-item" @click="updateCurrentPage(-2)"><a class="page-link" href="#">下一页</a></li>
                        </ul>
                      </nav>
                 </div>
            </div>
          </div>
        </div>
      </div>
</template>
  
<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import $ from 'jquery';
import { ref } from 'vue';

export default({
      name:"RankIndexView",
      components:{
          ContentField
      },
      setup(){
        const store=useStore();
        let current_page=1;
        let users=ref([]);
        let total_users=0;
        let pages=ref([]);

        const updatePages=()=>{//更新当前底部显示页
            let new_pages=ref([]);
            let max_pages=parseInt(Math.ceil(total_users/10));
            let down=Math.max(1,current_page-2);
            let top=Math.min(max_pages,current_page+2);
            for(let i=down;i<=top;i++){
                new_pages.value.push({
                    num:i,
                    is_active: i===current_page ? "active" :"",
                });
            }
            pages.value=new_pages.value;
        }

        const updateCurrentPage=(num)=>{//更新当前选择页
            let max_pages=parseInt(Math.ceil(total_users/10));
            if(num===-1)num=Math.max(1,current_page-1);
            if(num===-2)num=Math.min(max_pages,current_page+1);
            current_page=num;
            pull_page(current_page);
        }

        const pull_page=page=>{//更新当前展示的对局记录
            current_page=page;//https://app5356.acapp.acwing.com.cn/ http://127.0.0.1:3000/
            $.ajax({
                    url:"http://127.0.0.1:3000/api/ranklist/getlist/",
                    type:"get",
                    data:{
                        page,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        users.value=resp.users;
                        total_users=resp.users_count;
                        updatePages();
                    },
            });
        };

        pull_page(current_page);

        return{
            pull_page,
            updateCurrentPage,

            users,
            pages,
        }
      }
})

</script>

<style scoped>
.user-user-photo{
    width: 4vh;
    border-radius: 50%;
}
.user-user-name{
    font-style: italic;
}
.Myphoto{
  border-radius: 10%;
}
</style>