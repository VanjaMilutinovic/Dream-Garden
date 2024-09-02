import { Shape } from '../shape'

export abstract class ShapeFactory{

    protected abstract setSize(shape: Shape) : void;

    create(className: string) : Shape{
        const newShape : Shape = new Shape(className);
        this.setSize(newShape);
        return newShape;
    }
}