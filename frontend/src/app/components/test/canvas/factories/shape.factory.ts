import { Shape } from '../shape'

export abstract class ShapeFactory{

    constructor(public availible : number = 0){
    }

    protected abstract setSize(shape: Shape) : void;

    create(className: string) : Shape | null{
        if(!this.availible) return null;
        const newShape : Shape = new Shape(className);
        this.setSize(newShape);
        this.availible--;
        return newShape;
    }
}