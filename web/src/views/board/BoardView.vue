<template>
    <div class="container content-flied">
        <div class="card">
            <!-- 项目说明 -->
            <div class="card-body"><div class="container text-center">
                <div class="row">
                  <div class="col">
                    项目代码地址 &nbsp; <a href="https://git.acwing.com/Cassifa/king-of-bob" target="_blank" style="--bs-link-opacity: .5">Click Here to Check</a>
                  </div>
                  <div class="col">
                    项目说明文档 &nbsp; <a href="https://git.acwing.com/Cassifa/king-of-bob/-/blob/master/README.md" target="_blank" style="--bs-link-opacity: .5">README.md</a>
                  </div>
            </div>
            <!-- BOts列表 -->
            </div>
                <ol class="list-group list-group-numbered">

                    <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                        <div class="fw-bold">Bot-模板</div>
                        <pre class="white-space: pre font-size: large">{{announcement}}</pre>
                        <pre class="white-space: pre">{{Bot0}}</pre>
                    </div>
                    </li>

                    <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                        <div class="fw-bold">Bot-Lev1</div>
                        这是一个初级Bot 若下一步存在合法的方案则会根据局面随机输出一个方案
                        <pre class="white-space: pre">{{Bot1}}</pre>
                    </div>
                    <span class="badge bg-primary rounded-pill">1</span>
                    </li>

                    <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                        <div class="fw-bold">Bot-Lev2</div>
                        这是一个二级Bot 会根据局面判断接下来两步内的可行方案
                        <pre class="white-space: pre">{{Bot2}}</pre>
                    </div>
                    <span class="badge bg-primary rounded-pill">2</span>
                    </li>

                    <li class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                        <div class="fw-bold">Bot-Lev3</div>
                        这是一个较为智能的Bot 会根据局面递归的对接下来的移动进行评估并走出期望存活时间最长的一步 但此Bot不会考虑对手接下来的行动
                        <pre class="white-space: pre">{{Bot3}}</pre>
                    </div>
                    <span class="badge bg-primary rounded-pill">3</span>
                    </li>

                </ol>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';

export default {
    name: 'BoardView',
    components:{
    },
    setup() {
        let Bot0=ref('');
        let Bot1=ref('');
        let Bot2=ref('');
        let Bot3=ref('');
        let announcement=ref('');//模板说明
        announcement.value="这是提交代码的模板,重写其中nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells)函数以实现Bot代码 请注意部分Bot代码可能需要额外的头文件以支持运行";
        Bot1.value=`    public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int key=new Random().nextInt(4);
        for(int i=key,t=0;t<4;i=(i+1)%4,t++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x>=0&&x<13&&y>=0&&y<14&&g[x][y]==0){
                return i;
            }
        }
        return 0;
    }`;

        Bot2.value=`    public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {
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
    }`;

    Bot3.value=`    private int[][] now_g= new int[13][14];
    private int eval(int x,int y,int left){//这一步走哪里 剩余搜索长度
        if(left==0)return 0;//剩余步数用尽
        if(now_g[x][y]==1)return 0;//不合法
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int ans=0;
        now_g[x][y]=1;
        for(int i=0;i<4;i++){
            int a=x+dx[i],b=y+dy[i];
            ans=Math.max(ans,eval(a,b,left-1));
        }
        now_g[x][y]=0;
        return ans+1;
    }
    public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {
        for (int i=0;i<13;i++)
            for(int j=0;j<14;j++)
                now_g[i][j]=g[i][j];
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int ans=0,res=0;
        for(int i=0;i<4;i++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            //对当前这步进行估值
            int t=eval(x,y,7);
            if(t>res){
                ans=i;res=t;
            }
            else if(t==res&&(aCells.size()&1)==1){
                ans=i;res=t;
            }
        }
        return ans;
    }`;
    Bot0.value=`package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot implements java.util.function.Supplier<Integer>{

    static class  Cell{
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
        ans.add(new Cell(x, y));
        for(int i=0;i<steps.length();i++){
            int d=steps.charAt(i)-'0';
            x+=dx[d];
            y+=dy[d];
            ans.add(new Cell(x, y));
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

    public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {//重写这个函数
        //g为当前地图,地图左上角为0,0 g边界为外墙
        //Cell为一格蛇身体 含有x,y两个坐标信息
        //aCells:自己，数组尾为蛇头
        //返回值分别代表 0-上 1-右 2-下 3-左
        //请确保您的函数可以返回合法的值
        //请注意,您可能需要额外添加一些头文件
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

        return{
            announcement,
            Bot0,
            Bot1,
            Bot2,
            Bot3,
        }
        
    }

}
</script>

<style scoped>
.container{
    margin-top: 2%;
    margin-bottom: 2%;
}
</style>