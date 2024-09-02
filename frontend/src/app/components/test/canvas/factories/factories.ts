import { Shape } from "../shape";
import { ShapeFactory } from "./shape.factory";

export class BazenFactory extends ShapeFactory{
    setSize(shape: Shape) {
        shape.width = 140
        shape.height = 46
    }
}

export class StoFactory extends ShapeFactory{

    setSize(shape: Shape) {
        shape.width = shape.height = 30
    }

}

export class ZeleninoFactory extends ShapeFactory{

    setSize(shape: Shape) {
        shape.width = shape.height = 30
    }

}
export class FontanaFactory extends ShapeFactory{

    setSize(shape: Shape) {
        shape.width = shape.height = 50
    }

}

export class StolicaFactory extends ShapeFactory{

    setSize(shape: Shape) {
        shape.width = 40
        shape.height = 15
    }

}