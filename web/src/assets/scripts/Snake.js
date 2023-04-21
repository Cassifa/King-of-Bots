import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export default class Snake extends AcGameObject{
    constructor(info,gamemap){
        super();

        this.id=info.id;
        this.color=info.color;
        this.gamemap=gamemap;

        this.cells=[new Cell(info.r,info.c)];//🐍身体 cell0蛇头
        this.speed=3;//每秒走5格

        this.next_cell =null;
        //-1 0 1 2 3上右下左 
        this.direacion=-1;
        //idle静止 move移动 die死亡
        this.status='idle';
        this.step=0;//回合数

        this.dr=[-1,0,1,0];
        this.dc=[0,1,0,-1];

        this.eps=1e-2;//容忍误差
        this.eye_direction=0;
        //红色右上1 蓝色左下0
        if(this.id===0) this.eye_direction=0;
        else this.eye_direction=2;

        this.eye_dx=[
            [-1,1],
            [1,1],
            [1,-1],//旋转
            [-1,-1],
        ];
        this.eye_dy=[
            [-1,-1],
            [-1,1],
            [1,1],
            [1,-1],//旋转
        ];
    }

    start(){}

    set_direction(d){
        this.direacion=d;
    }

    check_tail_increasing(){
        if(this.step<=10)return true;
        if(!((this.step-9)%3))return true;
        return false;
    }

    next_step(){
        const d=this.direacion;
        this.eye_direction=d;
        this.next_cell=new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d]);
        this.direacion=-1;
        this.status="move";
        this.step++;

        const k=this.cells.length;
        for(let i=k;i;i--){
            this.cells[i]=JSON.parse(JSON.stringify(this.cells[i-1]));
        }
    }

    update_move(){
        const move_dis=this.speed*this.timedelta/1000;
        const dx=this.next_cell.x-this.cells[0].x;
        const dy=this.next_cell.y-this.cells[0].y;
        const dis=Math.sqrt(dx*dx+dy*dy);

        if(dis<this.eps){
            this.status='idle';
            this.cells[0]=this.next_cell;
            this.next_cell=null;
        }
        else{
            this.cells[0].x+=move_dis*dx/dis;
            this.cells[0].y+=move_dis*dy/dis;
        }
        if(!this.check_tail_increasing()){
            const k=this.cells.length;
            const tail=this.cells[k-1], tail_target=this.cells[k-2];
            const tdx=tail_target.x-tail.x;
            const tdy=tail_target.y-tail.y;
            
            if(dis<this.eps){
                this.cells.pop();
            }
            else{
                tail.x+=move_dis*tdx/dis;
                tail.y+=move_dis*tdy/dis;
            }
        }
    }

    update(){
        if(this.status==="move"){
            this.update_move();
        }
        this.render();
    }


    render(){
        const L=this.gamemap.L;
        const ctx=this.gamemap.ctx;
        ctx.fillStyle=this.color;
        if(this.status==='die')ctx.fillStyle="white";
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x*L,cell.y*L,L/2*0.8,0,Math.PI*2);
            ctx.fill();
        }

        //矩形填充
        for(let i=1;i<this.cells.length;i++){
            const a=this.cells[i-1],b=this.cells[i];
            // if(Math.abs(a.r-b.r)<this.eps&&Math.abs(a.c-b.c)<this.eps){
            //     console.log("A",Math.abs(a.r-b.r)<this.eps&&Math.abs(a.c-b.c)<this.eps,Math.abs(a.r-b.r),Math.abs(a.c-b.c));
            //     console.log("B",Math.abs(a.x-b.x)<this.eps&&Math.abs(a.y-b.y)<this.eps,Math.abs(a.x-b.x),Math.abs(a.y-b.y));
            //     continue;}
            if(Math.abs(a.x-b.x)<this.eps&&Math.abs(a.y-b.y)<this.eps)continue;
            if(Math.abs(a.x-b.x)<this.eps){
                //蛇变瘦
                ctx.fillRect((a.x-0.5+0.1)*L,Math.min(a.y,b.y)*L,L*0.8,Math.abs(a.y-b.y)*L);
            }
            else{
                ctx.fillRect(Math.min(a.x,b.x)*L,(a.y-0.5+0.1)*L,Math.abs(a.x-b.x)*L,L*0.8);
            }

        }

        
        ctx.fillStyle="black";
        for(let i=0;i<2;i++){
            ctx.beginPath();
            // console.log(this.cells[0].x+this.eye_dx[this.eye_direction][i]);
            const eye_x=(this.cells[0].x+this.eye_dx[this.eye_direction][i]*0.15)*L;
            const eye_y=(this.cells[0].y+this.eye_dy[this.eye_direction][i]*0.15)*L;
            ctx.arc(eye_x,eye_y,L*0.05,0,Math.PI*2);
            ctx.fill();
        }

    }
}