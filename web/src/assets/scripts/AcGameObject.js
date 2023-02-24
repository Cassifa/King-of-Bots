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

    destory(){
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
const step= timestamp =>{
    for(let obj of AC_GAME_OBJESTS){
        if(!obj.has_called_start){
            obj.has_called_start=true;
            obj.start();
        }
        else{
            obj.timedelta=timestamp-last_timestamp;
            obj.update();
        }
    }
    last_timestamp=timestamp;
    requestAnimationFrame(step)
}
requestAnimationFrame(step);