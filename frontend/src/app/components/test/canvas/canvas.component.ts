import { ShapeFactory } from './factories/shape.factory'
import { BazenFactory, FontanaFactory,StoFactory,StolicaFactory,ZeleninoFactory } from './factories/factories'
import { Component } from '@angular/core';
import { Shape } from './shape'
import { CdkDragEnd } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent {

  shapes : Shape[] = []

  pictureTitles : string[] = ["zelenino", "bazen", "fontana", "sto", "stolica"]
  factories : ShapeFactory[] = [new ZeleninoFactory(), new BazenFactory(), new FontanaFactory(), new StoFactory(), new StolicaFactory()]

  dragEnd($event: CdkDragEnd) {
    console.log($event.source);
  }

  create(shapeIndex : number){
    const newShape : Shape = this.factories[shapeIndex].create(this.pictureTitles[shapeIndex]);
    this.shapes.push(newShape)

  }

}
