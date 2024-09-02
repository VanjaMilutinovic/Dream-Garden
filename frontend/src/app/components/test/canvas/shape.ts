export class Shape{
    x : number = -1;
    y : number = -1;
    width : number = 0;
    height : number = 0;
    class : string = "";

    constructor(private _class: string){
        this.class = _class;
    }
}