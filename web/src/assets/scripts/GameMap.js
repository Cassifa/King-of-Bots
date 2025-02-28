import { AcGameObject } from "./AcGameObject";
import Snake from "./Snake";
import { Wall } from "./Wall";

export default class GameMap extends AcGameObject{
    constructor(ctx,parent,store){
        super();

        this.ctx=ctx;
        this.parent=parent;
        this.L=0;

        this.store=store;
        this.cols=14;//列
        this.rows=13;//行

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

    creat_walls(){
            const g=this.store.state.pk.gamemap;
            for(let r=0;r<this.rows;r++)
                for(let c=0;c<this.cols;c++)
                    if(g[r][c]==1)this.walls.push(new Wall(r,c,this));
        }

    add_listening_events(){
        //如果是回放
        if(this.store.state.record.is_record){
            let k=0;
            const [snake0,snake1]= this.snakes;
            const loser=this.store.state.record.record_loser;
            const a_step=this.store.state.record.a_step;//获取操作序列
            const b_step=this.store.state.record.b_step;
            // const mp=["上","右","下","左"];
            //每350ms命令蛇移动
            const interval_id=setInterval(()=>{
                if(k>=a_step.length-1){//如果有死亡
                    if(loser==="all"||loser==="a")
                        snake0.status="die";
                    if(loser==="all"||loser==="b")
                        snake1.status="die";
                    clearInterval(interval_id);
                    
                }else{//展示下一步
                    snake0.set_direction(parseInt(a_step[k]));
                    snake1.set_direction(parseInt(b_step[k]));
                    //红色右上1 蓝色左下0
                    // console.log("当前是第"+(k+1)+"回合：\n");
                    // console.log("红蛇走了："+mp[parseInt(a_step[k])]+", 蓝蛇走了："+mp[parseInt(b_step[k])]+"\n");
                    k++;
                }

            },350);
        }else{
            this.ctx.canvas.focus();//接受输入
            this.ctx.canvas.addEventListener("keydown",e=>{
                let d=-1;
                if(e.key==='w')d=0;
                if(e.key==='s')d=2;
                if(e.key==='a')d=3;
                if(e.key==='d')d=1;
                if(d!=-1){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event:"move",
                        direction:d,
                    }));
                }
            });
        }

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

    next_step(){//让每条蛇移动
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    // check_valid(cell){//判断移动是否合法
    //     for(const wall of this.walls){
    //         if(cell.r===wall.r&&cell.c===wall.c)return false;
    //     }
    //     for(let i of this.snakes){//临界情况
    //         if(!i.check_tail_increasing()&&cell.r===i.cells[i.cells.length-1].r&&cell.c===i.cells[i.cells.length-1].c)return true;
    //         for(let j of i.cells){
    //             if(cell.r===j.r&&cell.c===j.c)return false;
    //         }
    //     }
    //     return true;
    // }

    update(){
        this.update_size();//更新地图大小
        if(this.check_ready()){//准备好下一回合就移动
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