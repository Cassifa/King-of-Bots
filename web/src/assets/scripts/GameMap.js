import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export default class GameMap extends AcGameObject{
    constructor(ctx,parent){
        super();

        this.ctx=ctx;
        this.parent=parent;
        this.L=0;

        this.cols=13;//列
        this.rows=13;//行
        this.inner_walls_count=30;

        this.walls=[];
    }


    is_connected(x,y,g){
        if(x==1&&y==this.cols-2)return true;
        let dx=[0,0,1,-1],dy=[1,-1,0,0];
        for(let i=0;i<4;i++){
            let a=x+dx[i],b=y+dy[i];
            if(g[a][b])continue;
            g[a][b]=true;
            if(this.is_connected(a,b,g))return true;
        }
        return false;
    }

    creat_walls(){
            const g=[];
            let copy_g=[];
            do{
                for(let r=0;r<this.rows;r++){
                    g[r]=[];
                    for(let c=0;c<this.cols;c++)
                        g[r][c]=false;
                }
    
                //四周墙
                for(let r=0;r<this.rows;r++)
                    g[r][0]=g[r][this.cols-1]=true;
    
                for(let c=0;c<this.cols;c++)
                    g[0][c]=g[this.rows-1][c]=true;

                //内部障碍物
                for(let i=0;i<this.inner_walls_count;i+=2 ){
                    for(let j=0;j<1001;j++){
                        let r=parseInt(Math.random()*this.rows);
                        let c=parseInt(Math.random()*this.cols);
                        if(g[r][c]||g[c][r])continue;
                        if((r==this.rows-2&&c==1)||(r==1&&c==this.cols-2))continue;
                        g[r][c]=g[c][r]=true;
                        break;
                    }
                }
                //复制数组
                copy_g=JSON.parse(JSON.stringify(g));
            }
            while(!this.is_connected(this.rows-2,1,copy_g));
            
            for(let r=0;r<this.rows;r++)
                for(let c=0;c<this.cols;c++)
                    if(g[r][c])this.walls.push(new Wall(r,c,this));
        }
    start(){
        this.creat_walls();
    }

    update_size(){
        //求div的长宽clientWidth
        this.L=parseInt(Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        this.ctx.canvas.width=this.L*this.cols;
        this.ctx.canvas.height= this.L*this.rows;
    }
    update(){
        this.update_size();
        this.rander();
    }

    rander(){
        const color_even="#AAD751",color_odd="#A2D149";
        for(let r=0;r<this.cols;r++){
            for(let c=0;c<this.rows;c++){
                if((r+c)&1){
                    this.ctx.fillStyle=color_even;
                }
                else{
                    this.ctx.fillStyle=color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L);
            }
        }
    }
}