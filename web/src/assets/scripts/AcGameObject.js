const AC_GAME_OBJESTS =[];
export class AcGameObject{
    constructor(){
        AC_GAME_OBJESTS.push(this);
        this.timedelta=0;
        this.has_called_start=false;
    }

    start(){
    }

    update(){
    }

    on_destory(){
    }

    destory(){//销毁
        this.on_destory();
        for(let i in AC_GAME_OBJESTS){
            const obj=AC_GAME_OBJESTS[i];
            if(obj==this){
                AC_GAME_OBJESTS.splice(i);
                break;
            }
        }
    }
}
let last_timestamp;
const step= timestamp =>{//每秒执行60次
    for(let obj of AC_GAME_OBJESTS){
        //如果没有初始化过则初始化
        if(!obj.has_called_start){
            obj.has_called_start=true;
            obj.start();//初始化函数
        }
        //否则更新
        else{
            obj.timedelta=timestamp-last_timestamp;
            obj.update();//更新函数
        }
    }
    last_timestamp=timestamp;
    requestAnimationFrame(step)
}
requestAnimationFrame(step);