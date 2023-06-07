<template>
    <div class="container">
        <div class="row">
          <div class="col-3">
            <ContentField>
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
                            <th scope="col">蓝方</th>
                            <th scope="col">红方</th>
                            <th scope="col">Winner</th>
                            <th scope="col">对战时间</th>
                            <th scope="col">操作</th>
                          </tr>
                        </thead>
                        <tbody class="table-group-divider">
                          <tr v-for="(record,index) in records" :key="record.record.id" style="text-align:center">
                            <td>
                                {{index+1}}
                            </td>
                            <td>
                                <img :src="record.a_photo" class="record-user-photo">
                                &nbsp;
                                <span class="record-user-name">{{record.a_username}}</span>
                            </td>
                            <td>
                                <img :src="record.b_photo" class="record-user-photo">
                                &nbsp;
                                <span class="record-user-name">{{record.b_username}}</span>
                            </td>
                            <td>
                                {{record.result}}
                            </td>
                            <td>
                                {{record.record.createTime}}
                            </td>
                            <td>
                                <button @click="open_record_content(record.record.id)" type="button" class="btn btn-secondary">查看录像</button>
                            </td>
                          </tr>
                        </tbody>
                    </table>            
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
import router from '../../router/index';

export default({
      name:"RecordIndexView",
      components:{
          ContentField
      },
      setup(){
        const store=useStore();
        let current_page=1;
        let records=ref([]);
        let total_records=0;
        let pages=ref([]);

        const updatePages=()=>{//更新当前底部显示页
            let new_pages=ref([]);
            let max_pages=parseInt(Math.ceil(total_records/10));
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
            let max_pages=parseInt(Math.ceil(total_records/10));
            if(num===-1)num=Math.max(1,current_page-1);
            if(num===-2)num=Math.min(max_pages,current_page+1);
            current_page=num;
            pull_page(current_page);
        }

        const pull_page=page=>{//更新当前展示的对局记录
            current_page=page;//https://app5356.acapp.acwing.com.cn/ http://127.0.0.1:3000/
            $.ajax({
                    url:"https://app5356.acapp.acwing.com.cn/api/record/getlist/",
                    type:"get",
                    data:{
                        page,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(resp){
                        records.value=resp.records;
                        total_records=resp.record_count;
                        updatePages();
                    },
            });
        };

        pull_page(current_page);

        const stringTo2D=map=>{
            let g=[];
            for(let i=0,k=0;i<13;i++){
                let line=[];
                for(let j=0;j<14;j++,k++){
                    if(map[k]=='0')line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }

        const open_record_content = recordId =>{//1:04
            for(const record of records.value){
                //第一个record为所有数据,第二个为数据库record
                if(record.record.id===recordId){
                    store.commit("updateIsRecord",true);
                    store.commit("updateGame",{
                        game_map:stringTo2D(record.record.map),
                        a_id:record.record.aid,
                        a_sx:record.record.asx,
                        a_sy:record.record.asy,
                        b_id:record.record.bid,
                        b_sx:record.record.bsx,
                        b_sy:record.record.bsy,
                    });
                    store.commit("updateSteps",{
                        a_step:record.record.asteps,
                        b_step:record.record.bsteps,
                    });
                    store.commit("updateLoser",record.record.loser);
                    router.push({
                        name:"record_content",
                        params:{//路由参数
                            recordId
                        }
                    });
                    break;
                }
            }
        }
        return{
            pull_page,
            open_record_content,
            updateCurrentPage,

            records,
            pages,
        }
      }
})

</script>

<style scoped>
.record-user-photo{
    width: 4vh;
    border-radius: 50%;
}
.record-user-name{
    font-style: italic;
}
.Myphoto{
  border-radius: 10%;
}
</style>