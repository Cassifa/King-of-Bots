<template>
  <content-field>
      <div class="row justify-content-md-center">
          <div class="col-3">
              <form @submit.prevent="register">
                  <div class="mb-3">
                      <label for="username" class="form-label">用户名</label>
                      <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入昵称">
                  </div>
                  <div class="mb-3">
                      <label for="password" class="form-label">密码</label>
                      <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                  </div>
                  <div class="mb-3">
                      <label for="confirmedPassword" class="form-label">确认密码</label>
                      <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
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
import {ref} from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';
import router from '@/router';

export default {
    name:"UserAccountRegisterView",
    components:{
        ContentField,
    },
    setup(){
      let store=useStore();
      let username=ref('');
      let error_message=ref('');
      let password=ref('');
      let confirmedPassword=ref('');
      let defaultBot=ref("");
      defaultBot.value=`
package com.kob.botrunningsystem.utils;

import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot implements java.util.function.Supplier<Integer>{

    class Cell{
        public int x,y;
        public Cell(int x,int y){
            this.x=x;
            this.y=y;
        }
    }

    private  boolean check_tail_increasing(int step){//检验是否增长
        if(step<=10)return true;
        return step%3==1;
    }

    public List<Cell> getCells(int sx,int sy,String steps){
        steps=steps.substring(1,steps.length()-1);
        List<Cell> ans=new ArrayList<>();
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int x=sx,y=sy,now=0;
        ans.add(new Cell(x,y));
        for(int i=0;i<steps.length();i++){
            int d=steps.charAt(i)-'0';
            x+=dx[d];
            y+=dy[d];
            ans.add(new Cell(x,y));
            if(!check_tail_increasing(++now))
                ans.remove(0);
        }
        return ans;
    }

    public Integer nextMove(String input){
        String[] stirs=input.split("#");
        //地图#me_sx#me_sy#me_操作#you_sx#you_sy#对手操作
        //0:地图 1:me_sx 2:me_sy 3:me_操作 4:you_sx 5:you_sy 6:you_操作
        int[][] g=new int[13][14];
        //处理地图
        for(int i=0,k=0;i<13;i++){
            for(int j=0;j<14;j++,k++){
                if(stirs[0].charAt(k)=='1')
                    g[i][j]=1;
            }
        }
        int aSx=Integer.parseInt(stirs[1]),aSy=Integer.parseInt(stirs[2]);
        int bSx=Integer.parseInt(stirs[4]),bSy=Integer.parseInt(stirs[5]);

        List<Cell> aCells=getCells(aSx,aSy,stirs[3]);
        List<Cell> bCells=getCells(bSx,bSy,stirs[6]);
        //数组尾是蛇头

        for(Cell c:aCells) g[c.x][c.y]=1;
        for(Cell c:bCells) g[c.x][c.y]=1;

        return nextMove(g ,aCells,bCells);
    }

    public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {
        //aCells:自己，数组尾为蛇头
        HashSet<Integer> st=new HashSet<>();
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int key=new Random().nextInt(4);
        for(int i=key,t=0;t<4;i=(i+1)%4,t++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){
                st.add(i);
            }
        }
        if(st.size()==0){
            return 0;
        }
        for (Integer direction:st){
            for(int i=0;i<4;i++){
                int x=aCells.get(aCells.size()-1).x+dx[i]+dx[direction];
                int y=aCells.get(aCells.size()-1).y+dy[i]+dy[direction];
                if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){
                    return direction;
                }
            }
        }
        for(int i=0;i<4;i++)
            if(st.contains(i))return i;
        return 0;
    }


    @Override
    public Integer get() {
        File file=new File("input.txt");
        try {
            Scanner sc=new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
`;

      const login=()=>{//注册后自动登录
            //调用actions函数
            store.dispatch("login",{
                username:username.value,
                password:password.value,
                success(){
                    store.dispatch("getinfo",{
                        success(){
                            router.push({name:"pk_index"});
                        },
                    });
                },
            });
        }

      
        const add_bot=()=>{//添加默认Bot //https://app5356.acapp.acwing.com.cn/ http://127.0.0.1:3000/
                $.ajax({
                    url:"http://127.0.0.1:3000/api/user/bot/add/",
                    type:"post",
                    data:{
                        title:"Lev2",
                        description:"这是一个默认Bot会根据局面判断接下来两步内的可行方案",
                        content:defaultBot.value,
                    },
                    headers:{
                        Authorization:"Bearer "+store.state.user.token,
                    },
                    success(){
                        console.log("已添加默认Bot");
                    },
                });
        }

      const register=()=>{
          error_message.value="";//https://app5356.acapp.acwing.com.cn/ http://127.0.0.1:3000/
          $.ajax({
            url:"http://127.0.0.1:3000/api/user/account/register/",
            type:"post",
            data:{
              username:username.value,
              password:password.value,
              confirmedPassword:confirmedPassword.value,
            },
            success(resp){
              if(resp.error_message==="注册成功"){
                login();
                setTimeout(()=>{add_bot();},1000);//login执行后再加Bot
              }else{
                    error_message.value=resp.error_message;
              }
            },
          });
      }

      return{
        username,
        error_message,
        password,
        confirmedPassword,

        register,
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