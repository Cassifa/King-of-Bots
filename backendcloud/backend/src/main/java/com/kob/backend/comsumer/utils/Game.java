package com.kob.backend.comsumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.comsumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows,cols,inner_walls_count;
    private int[][] g;
    final private int[] dx={-1,1,0,0},dy={0,0,1,-1};
    private int[][] cpg;
    private final Player playerA,playerB;
    private Integer nextStepA=null,nextStepB=null;
    private String status="playing";//playing finshed
    private String loser="";//a,b,all
    private final ReentrantLock lock=new ReentrantLock();

    public Game(Integer rows,Integer cols,Integer inner_walls_count,Integer ida,Integer idb){
        this.cols=cols;
        this.rows=rows;
        this.inner_walls_count=inner_walls_count;
        this.g=new int[rows][cols];
        this.cpg=new int[rows][cols];
        playerA=new Player(ida,rows-2,1,new ArrayList<>());
        playerB=new Player(idb,1,cols-2,new ArrayList<>());
    }

    //获取类内元素
    public Player getPlayerA(){
        return playerA;
    }
    public Player getPlayerB(){
        return playerB;
    }
    public int[][] getG(){//返回地图
        return g;
    }

    //设置next
    public synchronized void setNextStepA(Integer nextStepA){
        this.nextStepA=nextStepA;
    }
    public synchronized void setNextStepB(Integer nextStepB){
        this.nextStepB=nextStepB;
    }

    private boolean draw(){//画一次地图
        for(int i=0;i<rows;i++)//初始化
            for(int j=0;j<cols;j++)
                    g[i][j]=0;

        //四周墙
        for(int i=0;i<rows;i++)
            g[i][0]=g[i][cols-1]=1;
        for(int i=0;i<cols;i++)
            g[0][i]=g[rows-1][i]=1;

        //随机
        Random random=new Random();
        for(int i=0;i<this.inner_walls_count;i+=4){
            for(int j=0;j<1001;j++){
                int r=random.nextInt(rows);//返回0~x-1
                int c=random.nextInt(cols);
                if(g[r][c]==1||g[rows-r-1][cols-c-1]==1||g[rows-r-1][c]==1||g[r][cols-c-1]==1)continue;
                if((r==rows-2&&c==1)||(r==1&&c==cols-2))continue;
                g[r][c]=g[this.rows-r-1][this.cols-c-1]=g[this.rows-r-1][c]=g[r][this.cols-c-1]=1;
                break;
            }
        }

        for(int i=0;i<rows;i++)
                for(int j=0;j<cols;j++)
                        cpg[i][j]=g[i][j];

        if(is_connected(this.rows-2,1)){
            return true;
        }
        return false;
    }
    //判断合法
    private  boolean is_connected(int x,int y){
        if(x==1&&y==this.cols-2)return true;
        for(int i=0;i<4;i++){
            int a=x+dx[i],b=y+dy[i];
            if(x<1||x==rows-1||y<1||y==cols-1)continue;
            if(cpg[a][b]==1)continue;
            cpg[a][b]=1;
            if(this.is_connected(a,b)) return true;
        }
        return false;
    }

    public void createMap(){
        for(int i=0;i<1000;i++)
            if(draw())break;
    }

    private boolean nextStep(){//等待下次操作
        try{//等待前端动画
            Thread.sleep(200);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        for(int i=0;i<50;i++){//最大等待时间5秒
            try{
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA!=null&&nextStepB!=null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){//判断某条蛇合法
        int n=cellsA.size();
        Cell cell=cellsA.get(n-1);
        if(g[cell.getX()][cell.getY()]==1)return false;
        for(int i=0;i<n-1;i++){
            Cell now=cellsA.get(i);
            if(now.getX().equals(cell.getX())&&now.getY().equals(cell.getY()))return false;
        }
        for(int i=0;i<n-1;i++){
            Cell now=cellsB.get(i);
            if(now.getX().equals(cell.getX())&&now.getY().equals(cell.getY()))return false;
        }
        return true;
    }
    private void judge(){//判断操作合法
        List<Cell> cellsA=playerA.getCells();
        List<Cell> cellsB=playerB.getCells();
        boolean canA=check_valid(cellsA,cellsB);
        boolean canB=check_valid(cellsB,cellsA);
        if(!canA||!canB){
            status="finished";
            if(!canA&&!canB) loser="all";
            else if(!canA) loser="a";
            else loser="b";
        }
    }
    private void sendAllMessage(String x){
        //一方断开链接则无视其操作
        if(WebSocketServer.users.get(playerA.getId())!=null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(x);
        if(WebSocketServer.users.get(playerB.getId())!=null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(x);
    }

    private void sendMove(){//广播操作
        lock.lock();
        try {
            JSONObject resp=new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA=nextStepB=null;
        }finally {
            lock.unlock();
        }
    }

    private String getMapString(){
        StringBuilder ans=new StringBuilder();
        for(int i=0;i<rows;i++)
                for(int j=0;j<cols;j++)
                        ans.append(g[i][j]);
        return ans.toString();
    }
    private void saveToDataBase(){
        Record record=new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }
    private void sendResult(){//广播结果
        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        sendAllMessage(resp.toJSONString());
        saveToDataBase();
    }
    @Override
    public void run(){
        //最多会有600步
        for(int i=0;i<1000;i++){
            if(!nextStep()){//游戏终止
                status="finished";
                lock.lock();
                try{
                    if(nextStepA==null&&nextStepB==null){
                        loser="all";
                    }else if(nextStepA==null){
                        loser="a";
                    } else if (nextStepB==null) {
                        loser="b";
                    }
                }finally {
                    lock.unlock();
                }
                //广播结果
                sendResult();
                break;
            }
            else {
                judge();
                if(status.equals("playing")){
                    sendMove();
                }
                else {
                    sendResult();
                    break;
                }
            }
        }
    }
}
