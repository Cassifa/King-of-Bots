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
