export class Shape{
    x : number = -1;
    y : number = -1;
    width : number = 0;
    height : number = 0;

    constructor(public cssClass: string){
    }

    public getBounds(){
        const RectALeft = this.x
        const RectARight = this.x+this.width
        const RectABottom = this.y
        const RectATop = this.y+this.height
        return {X1:RectALeft, X2:RectARight, Y1:RectATop, Y2:RectABottom}
    }
}