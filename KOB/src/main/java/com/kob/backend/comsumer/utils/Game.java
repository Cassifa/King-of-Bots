package com.kob.backend.comsumer.utils;

import java.util.Arrays;
import java.util.Random;

public class Game {
    private Integer rows,cols,inner_walls_count;
    private int[][] g;
    final private int[] dx={-1,1,0,0},dy={0,0,1,-1};
    private int[][] cpg;

    public Game(Integer rows,Integer cols,Integer inner_walls_count){
        this.cols=cols;
        this.rows=rows;
        this.inner_walls_count=inner_walls_count;
        this.g=new int[rows][cols];
        this.cpg=new int[rows][cols];
    }

    public int[][] getG(){//返回地图
        return g;
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

    public void createmap(){
        for(int i=0;i<1000;i++)
            if(draw())break;
    }
}
