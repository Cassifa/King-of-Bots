import { AcGameObject } from "./AcGameObject";
import Snake from "./Snake";
import { Wall } from "./Wall";

export default class GameMap extends AcGameObject{
    constructor(ctx,parent){
        super();

        this.ctx=ctx;
        this.parent=parent;
        this.L=0;

        this.cols=14;//列
        this.rows=13;//行
        this.inner_walls_count=10;

        this.walls=[];

        this.snakes=[
            new Snake({
                r:this.rows-2,
                c:1,
                id:0,
                color:"#4876EC",

            },this),
            new Snake({
                r:1,
                c:this.cols-2,
                id:1,
                color:'#F94848',
            },this),
        ];
        
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
                for(let i=0;i<this.inner_walls_count;i+=4){
                    for(let j=0;j<1001;j++){
                        let r=parseInt(Math.random()*this.rows);
                        let c=parseInt(Math.random()*this.cols);
                        if(g[r][c]||g[this.rows-r-1][this.cols-c-1]||g[this.rows-r-1][c]||g[r][this.cols-c-1])continue;
                        if((r==this.rows-2&&c==1)||(r==1&&c==this.cols-2))continue;
                        g[r][c]=g[this.rows-r-1][this.cols-c-1]=g[this.rows-r-1][c]=g[r][this.cols-c-1]=true;
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

    add_listening_events(){
        this.ctx.canvas.focus();
        const [s0,s1]=this.snakes;
        this.ctx.canvas.addEventListener("keydown",e=>{
            if(e.key==='w')s0.set_direction(0);
            if(e.key==='s')s0.set_direction(2);
            if(e.key==='a')s0.set_direction(3);
            if(e.key==='d')s0.set_direction(1);
            if(e.key==='ArrowUp')s1.set_direction(0);
            if(e.key==='ArrowRight')s1.set_direction(1);
            if(e.key==='ArrowDown')s1.set_direction(2);
            if(e.key==='ArrowLeft')s1.set_direction(3);
        });
    }

    start(){
        this.creat_walls();
        this.add_listening_events();
    }

    update_size(){
        //求div的长宽clientWidth
        this.L=parseInt(Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        this.ctx.canvas.width=this.L*this.cols;
        this.ctx.canvas.height= this.L*this.rows;
    }

    check_ready(){//判断两条蛇是否准备好下一回合
        for(let snake of this.snakes){
            if(snake.status!=="idle")return false;
            if(snake.direacion===-1)return false;
        }
        return true;
    }

    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_valid(cell){
        for(const wall of this.walls){
            if(cell.r===wall.r&&cell.c===wall.c)return false;
        }
        for(let i of this.snakes){
            if(!i.check_tail_increasing()&&cell.r===i.cells[i.cells.length-1].r&&cell.c===i.cells[i.cells.length-1].c)return true;
            for(let j of i.cells){
                if(cell.r===j.r&&cell.c===j.c)return false;
            }
        }
        return true;
    }

    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
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