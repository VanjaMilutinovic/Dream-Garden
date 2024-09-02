import { CdkDragEnd } from '@angular/cdk/drag-drop';
import { Component } from '@angular/core';
import { Shape } from './shape'

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent {

  shapes : Shape[] = []

  pictureTitles : string[] = ["zelenino", "bazen", "fontana", "sto", "stolica"]

  dragEnd($event: CdkDragEnd) {
    console.log($event.source);
  }

  create(shapeIndex : number){

    const newShape : Shape = new Shape();
    newShape.class = this.pictureTitles[shapeIndex]; 
    this.shapes.push(newShape)

  }

}
